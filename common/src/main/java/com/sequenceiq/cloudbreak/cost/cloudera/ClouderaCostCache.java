package com.sequenceiq.cloudbreak.cost.cloudera;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.sequenceiq.cloudbreak.common.cost.service.RegionEmissionFactorService;
import com.sequenceiq.cloudbreak.common.json.JsonUtil;
import com.sequenceiq.cloudbreak.util.FileReaderUtils;

@Service
public class ClouderaCostCache {
    private static final Logger LOGGER = LoggerFactory.getLogger(RegionEmissionFactorService.class);

    private static final String CLOUDERA_PRICE_LOCATION = "cost/cloudera-price.json";

    private Map<String, Double> priceCache;

    @PostConstruct
    public void init() {
        try {
            this.priceCache = loadPriceList().stream().collect(Collectors.toMap(ClouderaPrice::getType, ClouderaPrice::getCost));
        } catch (IOException e) {
            LOGGER.warn("Failed to load price cache!", e);
        }
    }

    public Double getPriceByType(String instanceType) {
        Double price = priceCache.get(instanceType);
        return price == null ? 1.0 : price;
    }

    private List<ClouderaPrice> loadPriceList() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource(CLOUDERA_PRICE_LOCATION);
        if (classPathResource.exists()) {
            String json = FileReaderUtils.readFileFromClasspath(CLOUDERA_PRICE_LOCATION);
            return JsonUtil.readValue(json, new TypeReference<List<ClouderaPrice>>() {});
        }
        throw new RuntimeException("Failed to load price cache!");
    }
}


