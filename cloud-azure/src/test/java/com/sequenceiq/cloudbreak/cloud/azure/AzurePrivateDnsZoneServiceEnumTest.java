package com.sequenceiq.cloudbreak.cloud.azure;

import static com.sequenceiq.cloudbreak.cloud.azure.AzurePrivateDnsZoneServiceEnum.POSTGRES;
import static com.sequenceiq.cloudbreak.cloud.azure.AzurePrivateDnsZoneServiceEnum.STORAGE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class AzurePrivateDnsZoneServiceEnumTest {

    @Test
    void testNumberOfZoneTypes() {
        assertEquals(2, testServicesSource().count(), "Please add test for missing enums");
    }

    @ParameterizedTest
    @MethodSource(value = "testServicesSource")
    void testServiceEnumValues(Pair<AzurePrivateDnsZoneServiceEnum, AzurePrivateDnsZoneServiceEnumValues> serviceEnumAndExpectedValues) {
        AzurePrivateDnsZoneServiceEnum serviceEnum = serviceEnumAndExpectedValues.getKey();
        AzurePrivateDnsZoneServiceEnumValues expectedValues = serviceEnumAndExpectedValues.getValue();

        assertEquals(expectedValues.getResourceType(), serviceEnum.getResourceType());
        assertEquals(expectedValues.getSubResource(), serviceEnum.getSubResource());
        assertEquals(expectedValues.getDnsZoneName(), serviceEnum.getDnsZoneName());
        assertEquals(expectedValues.getDnsZoneForwarder(), serviceEnum.getDnsZoneForwarder());
        assertThat(expectedValues.getDnsZoneNamePattern()).asList().hasSize(1);
        List<String> dnsZoneNamePatterns = serviceEnum.getDnsZoneNamePatterns().stream().map(Pattern::pattern).collect(Collectors.toList());
        assertThat(dnsZoneNamePatterns).asList().hasSameElementsAs(expectedValues.getDnsZoneNamePattern());
    }

    private static Stream<Pair<AzurePrivateDnsZoneServiceEnum, AzurePrivateDnsZoneServiceEnumValues>> testServicesSource() {
        return Stream.of(
                Pair.of(POSTGRES, new AzurePrivateDnsZoneServiceEnumValues(
                        "Microsoft.DBforPostgreSQL/servers",
                        "postgresqlServer",
                        "privatelink.postgres.database.azure.com",
                        "postgres.database.azure.com",
                        List.of("privatelink\\.postgres\\.database\\.azure\\.com"))),

                Pair.of(STORAGE, new AzurePrivateDnsZoneServiceEnumValues(
                        "Microsoft.Storage/storageAccounts",
                        "Blob",
                        "privatelink.blob.core.windows.net",
                        "blob.core.windows.net",
                        List.of("privatelink\\.blob\\.core\\.windows\\.net")
                ))
        );
    }

    private static class AzurePrivateDnsZoneServiceEnumValues {
        private final String resourceType;

        private final String subResource;

        private final String dnsZoneName;

        private final String dnsZoneForwarder;

        private final List<String> dnsZoneNamePattern;

        private AzurePrivateDnsZoneServiceEnumValues(String resourceType, String subResource, String dnsZoneName, String dnsZoneForwarder,
                List<String> dnsZoneNamePattern) {
            this.resourceType = resourceType;
            this.subResource = subResource;
            this.dnsZoneName = dnsZoneName;
            this.dnsZoneNamePattern = dnsZoneNamePattern;
            this.dnsZoneForwarder = dnsZoneForwarder;
        }

        private String getResourceType() {
            return resourceType;
        }

        private String getSubResource() {
            return subResource;
        }

        private String getDnsZoneName() {
            return dnsZoneName;
        }

        private List<String> getDnsZoneNamePattern() {
            return dnsZoneNamePattern;
        }

        private String getDnsZoneForwarder() {
            return dnsZoneForwarder;
        }
    }

}
