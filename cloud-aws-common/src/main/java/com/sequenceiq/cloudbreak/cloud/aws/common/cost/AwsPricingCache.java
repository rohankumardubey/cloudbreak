package com.sequenceiq.cloudbreak.cloud.aws.common.cost;

import java.io.IOException;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.sequenceiq.cloudbreak.cloud.PricingCache;
import com.sequenceiq.cloudbreak.cloud.aws.common.cost.model.PriceListElement;
import com.sequenceiq.cloudbreak.cloud.model.ExtendedCloudCredential;
import com.sequenceiq.cloudbreak.common.json.JsonUtil;
import com.sequenceiq.cloudbreak.common.mappable.CloudPlatform;
import com.sequenceiq.cloudbreak.service.CloudbreakRuntimeException;
import com.sequenceiq.cloudbreak.util.FileReaderUtils;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.pricing.PricingClient;
import software.amazon.awssdk.services.pricing.model.Filter;
import software.amazon.awssdk.services.pricing.model.FilterType;
import software.amazon.awssdk.services.pricing.model.GetProductsRequest;
import software.amazon.awssdk.services.pricing.model.GetProductsResponse;
import software.amazon.awssdk.services.pricing.model.NotFoundException;

@Service("awsPricingCache")
public class AwsPricingCache implements PricingCache {

    private static final Logger LOGGER = LoggerFactory.getLogger(AwsPricingCache.class);

    private static final String PRICING_API_ENDPOINT_REGION = "us-east-1";

    private static final String AWS_STORAGE_PRICING_JSON_LOCATION = "cost/aws-storage-pricing.json";

    private static final int HOURS_IN_30_DAYS = 720;

    @Inject
    private PricingClient awsPricing;

    private static Map<String, Map<String, Double>> loadStoragePriceList() {
        ClassPathResource classPathResource = new ClassPathResource(AWS_STORAGE_PRICING_JSON_LOCATION);
        if (classPathResource.exists()) {
            try {
                String json = FileReaderUtils.readFileFromClasspath(AWS_STORAGE_PRICING_JSON_LOCATION);
                return JsonUtil.readValue(json, new TypeReference<>() {
                });
            } catch (IOException e) {
                throw new RuntimeException("Failed to load AWS storage price json!", e);
            }
        }
        throw new RuntimeException("AWS storage price json file could not found!");
    }

    @Cacheable(cacheNames = "awsCostCache", key = "{ #region, #instanceType }")
    public double getPriceForInstanceType(String region, String instanceType) {
        PriceListElement priceListElement = getPriceList(region, instanceType);
        return priceListElement.getTerms().getOnDemand().values().stream().findFirst().orElseThrow(() -> createNotFoundException(region, instanceType))
                .getPriceDimensions().values().stream().findFirst().orElseThrow(() -> createNotFoundException(region, instanceType))
                .getPricePerUnit().getUsd();
    }

    @Cacheable(cacheNames = "awsCostCache", key = "{ #region, #instanceType }")
    public int getCpuCountForInstanceType(String region, String instanceType, ExtendedCloudCredential extendedCloudCredential) {
        PriceListElement priceListElement = getPriceList(region, instanceType);
        return priceListElement.getProduct().getAttributes().getVcpu();
    }

    @Cacheable(cacheNames = "awsCostCache", key = "{ #region, #instanceType }")
    public int getMemoryForInstanceType(String region, String instanceType, ExtendedCloudCredential extendedCloudCredential) {
        PriceListElement priceListElement = getPriceList(region, instanceType);
        String memory = priceListElement.getProduct().getAttributes().getMemory();
        return Integer.parseInt(memory.replaceAll("\\D", ""));
    }

    @Cacheable(cacheNames = "awsCostCache", key = "{ #region, #storageType, #volumeSize }")
    public double getStoragePricePerGBHour(String region, String storageType, int volumeSize) {
        if (volumeSize == 0 || storageType == null) {
            LOGGER.info("The provided volumeSize is 0 or the storageType is null, so returning 0.0 as storage price per GBHour.");
            return 0.0;
        }
        Map<String, Double> pricingForRegion = loadStoragePriceList().getOrDefault(region, Map.of());
        double priceInGBMonth = pricingForRegion.getOrDefault(storageType, 0.0);
        return priceInGBMonth / HOURS_IN_30_DAYS;
    }

    private PriceListElement getPriceList(String region, String instanceType) {
        try {
            GetProductsResponse productsResponse = getProducts(region, instanceType);
            String priceListString = productsResponse.priceList().stream().findFirst()
                    .orElseThrow(() -> NotFoundException.builder()
                            .message(String.format("Couldn't find the price list for the region %s and instance type %s!", region, instanceType)).build());
            return JsonUtil.readValue(priceListString, PriceListElement.class);
        } catch (IOException e) {
            throw new CloudbreakRuntimeException("Failed to get price list from provider!", e);
        }
    }

    private GetProductsResponse getProducts(String region, String instanceType) {
        GetProductsRequest productsRequest = GetProductsRequest.builder()
                .serviceCode("AmazonEC2")
                .formatVersion("aws_v1")
                .filters(
                        Filter.builder().type(FilterType.TERM_MATCH).field("regionCode").value(region).build(),
                        Filter.builder().type(FilterType.TERM_MATCH).field("instanceType").value(instanceType).build(),
                        Filter.builder().type(FilterType.TERM_MATCH).field("operatingSystem").value("Linux").build(),
                        Filter.builder().type(FilterType.TERM_MATCH).field("preInstalledSw").value("NA").build(),
                        Filter.builder().type(FilterType.TERM_MATCH).field("capacitystatus").value("Used").build(),
                        Filter.builder().type(FilterType.TERM_MATCH).field("tenancy").value("Shared").build())
                .build();
        return awsPricing.getProducts(productsRequest);
    }

    private NotFoundException createNotFoundException(String regionName, String instanceType) {
        return NotFoundException.builder()
                .message(String.format("Couldn't find the price for region [%s] and instance type [%s].", regionName, instanceType))
                .build();
    }

    @Bean
    public PricingClient getAwsPricing() {
        return PricingClient.builder()
                .region(Region.of(PRICING_API_ENDPOINT_REGION))
                .build();
    }

    @Override
    public CloudPlatform getCloudPlatform() {
        return CloudPlatform.AWS;

    }
}
