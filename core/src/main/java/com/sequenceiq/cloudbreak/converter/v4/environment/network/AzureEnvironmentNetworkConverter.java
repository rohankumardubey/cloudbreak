package com.sequenceiq.cloudbreak.converter.v4.environment.network;

import static com.sequenceiq.cloudbreak.constant.AzureConstants.DATABASE_PRIVATE_DNS_ZONE_ID;
import static com.sequenceiq.cloudbreak.constant.AzureConstants.NETWORK_ID;
import static com.sequenceiq.cloudbreak.constant.AzureConstants.NO_OUTBOUND_LOAD_BALANCER;
import static com.sequenceiq.cloudbreak.constant.AzureConstants.NO_PUBLIC_IP;
import static com.sequenceiq.cloudbreak.constant.AzureConstants.RESOURCE_GROUP_NAME;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.sequenceiq.cloudbreak.cloud.model.CloudSubnet;
import com.sequenceiq.cloudbreak.common.mappable.CloudPlatform;
import com.sequenceiq.environment.api.v1.environment.model.EnvironmentNetworkAzureParams;
import com.sequenceiq.environment.api.v1.environment.model.response.EnvironmentNetworkResponse;

@Component
public class AzureEnvironmentNetworkConverter extends EnvironmentBaseNetworkConverter {

    @Override
    protected void attachEndpointGatewaySubnet(EnvironmentNetworkResponse source, Map<String, Object> attributes, CloudSubnet cloudSubnet) {
        // this is intentionally left as a no-op since Azure does not need to attach an endpoint gateway subnet.
    }

    @Override
    Map<String, Object> getAttributesForLegacyNetwork(EnvironmentNetworkResponse source) {
        EnvironmentNetworkAzureParams azure = source.getAzure();
        return Map.of(
                NETWORK_ID, azure.getNetworkId(),
                RESOURCE_GROUP_NAME, azure.getResourceGroupName(),
                NO_PUBLIC_IP, azure.getNoPublicIp(),
                DATABASE_PRIVATE_DNS_ZONE_ID, getDatabasePrivateDnsZoneId(azure),
                NO_OUTBOUND_LOAD_BALANCER, azure.isNoOutboundLoadBalancer()
        );
    }

    private String getDatabasePrivateDnsZoneId(EnvironmentNetworkAzureParams azure) {
        return StringUtils.isNotEmpty(azure.getDatabasePrivateDnsZoneId()) ? azure.getDatabasePrivateDnsZoneId() : "";
    }

    @Override
    public CloudPlatform getCloudPlatform() {
        return CloudPlatform.AZURE;
    }
}
