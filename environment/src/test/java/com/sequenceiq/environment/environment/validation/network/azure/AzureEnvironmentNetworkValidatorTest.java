package com.sequenceiq.environment.environment.validation.network.azure;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.sequenceiq.cloudbreak.auth.altus.EntitlementService;
import com.sequenceiq.cloudbreak.cloud.azure.AzureCloudSubnetParametersService;
import com.sequenceiq.cloudbreak.cloud.model.CloudSubnet;
import com.sequenceiq.cloudbreak.validation.ValidationResult;
import com.sequenceiq.cloudbreak.validation.ValidationResult.ValidationResultBuilder;
import com.sequenceiq.common.api.type.ServiceEndpointCreation;
import com.sequenceiq.environment.environment.domain.Region;
import com.sequenceiq.environment.environment.dto.EnvironmentDto;
import com.sequenceiq.environment.environment.dto.EnvironmentValidationDto;
import com.sequenceiq.environment.environment.validation.ValidationType;
import com.sequenceiq.environment.environment.validation.network.NetworkTestUtils;
import com.sequenceiq.environment.network.CloudNetworkService;
import com.sequenceiq.environment.network.dao.domain.RegistrationType;
import com.sequenceiq.environment.network.dto.AzureParams;
import com.sequenceiq.environment.network.dto.NetworkDto;
import com.sequenceiq.environment.parameter.dto.AzureParametersDto;
import com.sequenceiq.environment.parameter.dto.AzureResourceGroupDto;
import com.sequenceiq.environment.parameter.dto.ParametersDto;
import com.sequenceiq.environment.parameter.dto.ResourceGroupUsagePattern;

@ExtendWith(MockitoExtension.class)
class AzureEnvironmentNetworkValidatorTest {
    private static final String MY_SINGLE_RG = "mySingleRg";

    private AzureEnvironmentNetworkValidator underTest;

    @Mock
    private CloudNetworkService cloudNetworkService;

    @Mock
    private AzurePrivateEndpointValidator azurePrivateEndpointValidator;

    @Mock
    private AzureCloudSubnetParametersService azureCloudSubnetParametersService;

    @Mock
    private EntitlementService entitlementService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        underTest = new AzureEnvironmentNetworkValidator(cloudNetworkService,
                azurePrivateEndpointValidator, azureCloudSubnetParametersService, entitlementService);
        ReflectionTestUtils.setField(underTest, "azureAvailabilityZones", Set.of("1", "2", "3"));
    }

    @Test
    void testValidateDuringFlowWhenTheNetworkIsNull() {
        ValidationResultBuilder validationResultBuilder = new ValidationResultBuilder();

        underTest.validateDuringFlow(null, null, validationResultBuilder);

        assertTrue(validationResultBuilder.build().hasError());
    }

    @Test
    void testValidateDuringFlowWhenTheAzureNetworkParamsContainsRequiredFields() {
        ValidationResultBuilder validationResultBuilder = new ValidationResultBuilder();
        AzureParams azureParams = getAzureParams("", "");
        azureParams.setFlexibleServerSubnetIds(new HashSet<>());

        NetworkDto networkDto = NetworkDto.builder()
                .withId(1L)
                .withName("networkName")
                .withResourceCrn("aResourceCRN")
                .withAzure(azureParams)
                .withSubnetMetas(Map.of())
                .build();

        EnvironmentValidationDto environmentValidationDto = EnvironmentValidationDto.builder()
                .withEnvironmentDto(getEnvironmentDtoWithRegion())
                .build();

        underTest.validateDuringFlow(environmentValidationDto, networkDto, validationResultBuilder);

        assertFalse(validationResultBuilder.build().hasError());
    }

    @Test
    void testValidateDuringFlowWhenPrivateEndpointAndEnvCreationThenPrivateEndpointValidationsAreRun() {
        ValidationResultBuilder validationResultBuilder = new ValidationResultBuilder();
        AzureParams azureParams = getAzureParams("networkId", "networkResourceGroupName");
        azureParams.setFlexibleServerSubnetIds(new HashSet<>());
        NetworkDto networkDto = NetworkTestUtils.getNetworkDtoBuilder(azureParams, null, null, azureParams.getNetworkId(), null, 1, RegistrationType.EXISTING)
                .withServiceEndpointCreation(ServiceEndpointCreation.ENABLED_PRIVATE_ENDPOINT)
                .build();
        EnvironmentValidationDto environmentValidationDto = environmentValidationDtoWithSingleRg(MY_SINGLE_RG, ResourceGroupUsagePattern.USE_SINGLE);

        underTest.validateDuringFlow(environmentValidationDto, networkDto, validationResultBuilder);

        EnvironmentDto environmentDto = environmentValidationDto.getEnvironmentDto();
        verify(azurePrivateEndpointValidator).checkNewPrivateDnsZone(validationResultBuilder, environmentDto, networkDto);
        verify(azurePrivateEndpointValidator).checkMultipleResourceGroup(validationResultBuilder, environmentDto,
                networkDto);
        verify(azurePrivateEndpointValidator).checkNewPrivateDnsZone(validationResultBuilder, environmentDto, networkDto);
        verify(azurePrivateEndpointValidator).checkExistingManagedPrivateDnsZone(validationResultBuilder, environmentDto, networkDto);
    }

    @Test
    void testValidateDuringFlowWhenEnvironmentIsBeingEditedThenPrivateEndpointValidationsSkipped() {
        ValidationResultBuilder validationResultBuilder = new ValidationResultBuilder();
        AzureParams azureParams = getAzureParams("", "networkResourceGroupName");
        azureParams.setFlexibleServerSubnetIds(new HashSet<>());

        NetworkDto networkDto = getNetworkDto(azureParams);
        when(cloudNetworkService.retrieveSubnetMetadata(any(EnvironmentDto.class), any())).thenReturn(getCloudSubnets(false));
        EnvironmentValidationDto environmentDto = environmentValidationDtoWithSingleRg(MY_SINGLE_RG, ResourceGroupUsagePattern.USE_SINGLE);
        environmentDto.setValidationType(ValidationType.ENVIRONMENT_EDIT);

        underTest.validateDuringFlow(environmentDto, networkDto, validationResultBuilder);

        verify(azurePrivateEndpointValidator, never()).checkNewPrivateDnsZone(any(), any(), any());
        verify(azurePrivateEndpointValidator, never()).checkMultipleResourceGroup(any(), any(), any());
        verify(azurePrivateEndpointValidator, never()).checkNewPrivateDnsZone(any(), any(), any());
        verify(azurePrivateEndpointValidator, never()).checkExistingManagedPrivateDnsZone(any(), any(), any());
        verify(azurePrivateEndpointValidator, never()).checkExistingRegisteredOnlyPrivateDnsZone(any(), any(), any());
        assertFalse(validationResultBuilder.build().hasError());
    }

    private static Stream<Set<String>> provideFlexibleServerSubnetArguments() {
        return Stream.of(
                Set.of("azure/flexibleSubnet"),
                Set.of("flexibleSubnet1"),
                Set.of("azure/flexibleSubnet, flexibleSubnet1"),
                Set.of("azure/flexibleSubnet1, azure/flexibleSubnet2"),
                Set.of("flexibleSubnet1, flexibleSubnet2")
        );
    }

    @ParameterizedTest
    @MethodSource("provideFlexibleServerSubnetArguments")
    void testValidateDuringFlowWhenFlexibleServerSubnetIdsAreValid(Set<String> input) {
        ValidationResultBuilder validationResultBuilder = new ValidationResultBuilder();
        AzureParams azureParams = getAzureParams("networkId", "networkResourceGroupName");
        azureParams.setFlexibleServerSubnetIds(input);
        NetworkDto networkDto = NetworkTestUtils.getNetworkDtoBuilder(azureParams, null, null, azureParams.getNetworkId(), null, 1, RegistrationType.EXISTING)
                .build();
        EnvironmentValidationDto environmentValidationDto = environmentValidationDtoWithSingleRg(MY_SINGLE_RG, ResourceGroupUsagePattern.USE_SINGLE);
        when(entitlementService.isAzureDatabaseFlexibleServerEnabled(anyString())).thenReturn(true);
        Set<String> subnetNames = input.stream()
                .map(subnet -> {
                    if (subnet.contains("/")) {
                        return StringUtils.substringAfterLast(subnet, "/");
                    } else {
                        return subnet;
                    }
                }).collect(Collectors.toSet());
        Map<String, CloudSubnet> subnetToCloudSubnet = subnetNames.stream().collect(Collectors.toMap(Function.identity(), sn -> new CloudSubnet(sn, null)));
        when(cloudNetworkService.retrieveSubnetMetadata(environmentValidationDto.getEnvironmentDto(), networkDto))
                .thenReturn(subnetToCloudSubnet);

        when(cloudNetworkService.getSubnetMetadata(environmentValidationDto.getEnvironmentDto(), networkDto, subnetNames))
                .thenReturn(subnetToCloudSubnet);

        when(azureCloudSubnetParametersService.isFlexibleServerDelegatedSubnet(any(CloudSubnet.class))).thenReturn(true);
        underTest.validateDuringFlow(environmentValidationDto, networkDto, validationResultBuilder);

        assertFalse(validationResultBuilder.build().hasError());
    }

    @Test
    void testValidateDuringFlowWhenFlexibleServerSubnetIdsAreInvalid() {
        ValidationResultBuilder validationResultBuilder = new ValidationResultBuilder();
        AzureParams azureParams = getAzureParams("networkId", "networkResourceGroupName");
        azureParams.setFlexibleServerSubnetIds(Set.of("azure/flexibleSubnet"));
        NetworkDto networkDto = NetworkTestUtils.getNetworkDtoBuilder(azureParams, null, null, azureParams.getNetworkId(), null, 1, RegistrationType.EXISTING)
                .build();
        EnvironmentValidationDto environmentValidationDto = environmentValidationDtoWithSingleRg(MY_SINGLE_RG, ResourceGroupUsagePattern.USE_SINGLE);
        when(entitlementService.isAzureDatabaseFlexibleServerEnabled(anyString())).thenReturn(true);
        when(cloudNetworkService.retrieveSubnetMetadata(environmentValidationDto.getEnvironmentDto(), networkDto))
                .thenReturn(Map.ofEntries(Map.entry("flexibleSubnet", new CloudSubnet())));
        when(cloudNetworkService.getSubnetMetadata(environmentValidationDto.getEnvironmentDto(), networkDto, Set.of("flexibleSubnet")))
                .thenReturn(Map.ofEntries(Map.entry(networkDto.getSubnetIds().iterator().next(), new CloudSubnet())));
        when(azureCloudSubnetParametersService.isFlexibleServerDelegatedSubnet(any(CloudSubnet.class))).thenReturn(false);
        underTest.validateDuringFlow(environmentValidationDto, networkDto, validationResultBuilder);

        assertTrue(validationResultBuilder.build().hasError());
    }

    @Test
    void testValidateDuringFlowWhenFlexibleServerSubnetIdsAreInvalidButCheckSkipped() {
        ValidationResultBuilder validationResultBuilder = new ValidationResultBuilder();
        AzureParams azureParams = getAzureParams("networkId", "networkResourceGroupName");
        azureParams.setFlexibleServerSubnetIds(Set.of("flexibleSubnet"));
        NetworkDto networkDto = NetworkTestUtils.getNetworkDtoBuilder(azureParams, null, null, azureParams.getNetworkId(), null, 1, RegistrationType.EXISTING)
                .build();
        EnvironmentValidationDto environmentValidationDto = environmentValidationDtoWithSingleRg(MY_SINGLE_RG, ResourceGroupUsagePattern.USE_SINGLE);
        when(cloudNetworkService.retrieveSubnetMetadata(environmentValidationDto.getEnvironmentDto(), networkDto))
                .thenReturn(Map.ofEntries(Map.entry("flexibleSubnet", new CloudSubnet())));
        underTest.validateDuringFlow(environmentValidationDto, networkDto, validationResultBuilder);

        assertFalse(validationResultBuilder.build().hasError());
    }

    private NetworkDto getNetworkDto(AzureParams azureParams) {
        return NetworkDto.builder()
                .withId(1L)
                .withName("networkName")
                .withResourceCrn("aResourceCRN")
                .withAzure(azureParams)
                .withSubnetMetas(Map.of())
                .withNetworkId("networkId")
                .withServiceEndpointCreation(ServiceEndpointCreation.ENABLED_PRIVATE_ENDPOINT)
                .build();
    }

    @Test
    void testValidateDuringRequestWhenTheNetworkDoesNotContainAzureNetworkParams() {
        NetworkDto networkDto = NetworkTestUtils.getNetworkDto(null, null, null, null, null, 1);
        ValidationResultBuilder validationResultBuilder = new ValidationResultBuilder();

        underTest.validateDuringRequest(networkDto, validationResultBuilder);

        NetworkTestUtils.checkErrorsPresent(validationResultBuilder, List.of(
                "The 'AZURE' related network parameters should be specified!",
                "Either the AZURE network id or cidr needs to be defined!"));
    }

    @Test
    void testValidateDuringRequestWhenTheAzureNetworkParamsDoesNotResourceGroupId() {
        AzureParams azureParams = NetworkTestUtils.getAzureParams(true, true, false);
        NetworkDto networkDto = NetworkTestUtils.getNetworkDto(azureParams, null, null, azureParams.getNetworkId(), null, 1);
        ValidationResultBuilder validationResultBuilder = new ValidationResultBuilder();

        underTest.validateDuringRequest(networkDto, validationResultBuilder);

        NetworkTestUtils.checkErrorsPresent(validationResultBuilder, List.of(
                "If networkId is specified, then resourceGroupName must be specified too."));
    }

    @Test
    void testValidateDuringRequestWhenTheAzureNetworkParamsDoesNotContainNetworkId() {
        AzureParams azureParams = NetworkTestUtils.getAzureParams(true, false, true);
        NetworkDto networkDto = NetworkTestUtils.getNetworkDto(azureParams, null, null, azureParams.getNetworkId(), null, 1);
        ValidationResultBuilder validationResultBuilder = new ValidationResultBuilder();

        underTest.validateDuringRequest(networkDto, validationResultBuilder);

        NetworkTestUtils.checkErrorsPresent(validationResultBuilder, List.of(
                "If resourceGroupName is specified, then networkId must be specified too.",
                "Either the AZURE network id or cidr needs to be defined!",
                "If subnetIds are specified, then networkId must be specified too."));
    }

    @Test
    void testValidateDuringRequestWhenNetworkIdWithNoSubnets() {
        AzureParams azureParams = NetworkTestUtils.getAzureParams(true, true, true);
        NetworkDto networkDto = NetworkTestUtils.getNetworkDto(azureParams, null, null, azureParams.getNetworkId(), null, null);

        ValidationResultBuilder resultBuilder = new ValidationResultBuilder();
        underTest.validateDuringRequest(networkDto, resultBuilder);

        NetworkTestUtils.checkErrorsPresent(resultBuilder, List.of(
                "If networkId (aNetworkId) and resourceGroupName (aResourceGroupId) are specified then subnet ids must be specified as well."));
    }

    @Test
    void testValidateDuringRequestWhenNetworkIdWithSubnetsNotExistsOnAzure() {
        int numberOfSubnets = 2;
        AzureParams azureParams = NetworkTestUtils.getAzureParams(true, true, true);
        azureParams.setFlexibleServerSubnetIds(new HashSet<>());
        NetworkDto networkDto = NetworkTestUtils.getNetworkDto(azureParams, null, null, azureParams.getNetworkId(), null, numberOfSubnets);
        EnvironmentDto environmentDto = getEnvironmentDtoWithRegion();
        EnvironmentValidationDto environmentValidationDto = EnvironmentValidationDto.builder()
                .withEnvironmentDto(environmentDto)
                .build();

        when(cloudNetworkService.retrieveSubnetMetadata(environmentDto, networkDto)).thenReturn(Map.of(networkDto.getSubnetIds().stream().findFirst().get(),
                new CloudSubnet()));

        ValidationResultBuilder resultBuilder = new ValidationResultBuilder();
        underTest.validateDuringFlow(environmentValidationDto, networkDto, resultBuilder);

        NetworkTestUtils.checkErrorsPresent(resultBuilder, List.of("If networkId (aNetworkId) and resourceGroupName (aResourceGroupId) are specified then" +
                " subnet ids must be specified and should exist on azure as well. Given subnetids: [\"key1\", \"key0\"], existing ones: [\"key1\"], " +
                "in region: [East US]"));
    }

    @Test
    void testValidateDuringRequestWhenSubnetsWithNoNetworkId() {
        AzureParams azureParams = NetworkTestUtils.getAzureParams(true, false, true);
        NetworkDto networkDto = NetworkTestUtils.getNetworkDto(azureParams, null, null, null, null, 2);

        ValidationResultBuilder resultBuilder = new ValidationResultBuilder();
        underTest.validateDuringRequest(networkDto, resultBuilder);

        NetworkTestUtils.checkErrorsPresent(resultBuilder, List.of("Either the AZURE network id or cidr needs to be defined!",
                "If resourceGroupName is specified, then networkId must be specified too.",
                "If subnetIds are specified, then networkId must be specified too."));
    }

    @Test
    void testValidateDuringRequestWhenNoNetworkCidrAndNetworkId() {
        AzureParams azureParams = NetworkTestUtils.getAzureParams(true, true, true);
        NetworkDto networkDto = NetworkTestUtils.getNetworkDto(azureParams, null, null, azureParams.getNetworkId(), null, 1);

        ValidationResultBuilder resultBuilder = new ValidationResultBuilder();
        underTest.validateDuringRequest(networkDto, resultBuilder);

        ValidationResult validationResult = resultBuilder.build();
        assertFalse(validationResult.hasError());
    }

    @Test
    void testValidateWhenNoNetworkCidrAndNoNetworkId() {
        NetworkDto networkDto = NetworkTestUtils.getNetworkDto(AzureParams.builder().build(), null, null, null, null, 1);

        ValidationResultBuilder resultBuilder = new ValidationResultBuilder();
        underTest.validateDuringRequest(networkDto, resultBuilder);

        NetworkTestUtils.checkErrorsPresent(resultBuilder, List.of(
                "If AZURE subnet ids were provided then network id and resource group name have to be specified, too.",
                "Either the AZURE network id or cidr needs to be defined!",
                "If subnetIds are specified, then networkId must be specified too.")
        );
    }

    @Test
    void testValidateDuringRequestWhenNetworkCidrAndNoNetworkId() {
        AzureParams azureParams = NetworkTestUtils.getAzureParams(true, false, false);
        NetworkDto networkDto = NetworkTestUtils.getNetworkDto(azureParams, null, null, null, "0.0.0.0/0", null);

        ValidationResultBuilder resultBuilder = new ValidationResultBuilder();
        underTest.validateDuringRequest(networkDto, resultBuilder);

        ValidationResult validationResult = resultBuilder.build();
        assertFalse(validationResult.hasError(), validationResult.getFormattedErrors());
    }

    @Test
    void testValidateDuringRequestWhenNetworkCidrAndNoAzureParams() {
        NetworkDto networkDto = NetworkTestUtils.getNetworkDto(null, null, null, null, "0.0.0.0/0", null);

        ValidationResultBuilder resultBuilder = new ValidationResultBuilder();
        underTest.validateDuringRequest(networkDto, resultBuilder);

        ValidationResult validationResult = resultBuilder.build();
        assertFalse(validationResult.hasError(), validationResult.getFormattedErrors());
    }

    @Test
    void testValidateDuringRequestWhenNoNetworkCidrAndNoAzureParams() {
        NetworkDto networkDto = NetworkTestUtils.getNetworkDto(null, null, null, null, null, 1);

        ValidationResultBuilder resultBuilder = new ValidationResultBuilder();
        underTest.validateDuringRequest(networkDto, resultBuilder);

        NetworkTestUtils.checkErrorsPresent(resultBuilder, List.of(
                "The 'AZURE' related network parameters should be specified!",
                "Either the AZURE network id or cidr needs to be defined!")
        );
    }

    @Test
    void testValidateDuringRequestWhenNoNetworkCidrAndNoNetworkId() {
        AzureParams azureParams = NetworkTestUtils.getAzureParams(true, false, true);
        NetworkDto networkDto = NetworkTestUtils.getNetworkDto(azureParams, null, null, azureParams.getNetworkId(), null, 1);

        ValidationResultBuilder resultBuilder = new ValidationResultBuilder();
        underTest.validateDuringRequest(networkDto, resultBuilder);

        NetworkTestUtils.checkErrorsPresent(resultBuilder, List.of(
                "If resourceGroupName is specified, then networkId must be specified too.",
                "Either the AZURE network id or cidr needs to be defined!",
                "If subnetIds are specified, then networkId must be specified too.")
        );
    }

    @Test
    void testValidateDuringRequestWhenNoNetworkCidrAndNoResourceGroupName() {
        AzureParams azureParams = NetworkTestUtils.getAzureParams(true, true, false);
        NetworkDto networkDto = NetworkTestUtils.getNetworkDto(azureParams, null, null, azureParams.getNetworkId(), null, 1);

        ValidationResultBuilder resultBuilder = new ValidationResultBuilder();
        underTest.validateDuringRequest(networkDto, resultBuilder);

        NetworkTestUtils.checkErrorsPresent(resultBuilder, List.of(
                "If networkId is specified, then resourceGroupName must be specified too."));
    }

    @Test
    void testValidateDuringRequestWhenOnlyOneAvailabilityZoneIsGiven() {
        AzureParams azureParams = NetworkTestUtils.getAzureParams(true, false, false);
        azureParams.setAvailabilityZones(Set.of("1"));
        NetworkDto networkDto = NetworkTestUtils.getNetworkDto(azureParams, null, null, null, "0.0.0.0/0", null);

        ValidationResultBuilder resultBuilder = new ValidationResultBuilder();
        underTest.validateDuringRequest(networkDto, resultBuilder);

        ValidationResult validationResult = resultBuilder.build();

        assertFalse(validationResult.hasError(), validationResult.getFormattedErrors());
    }

    @Test
    void testValidateDuringRequestWhenOneAvailabilityZonesIsInvalid() {
        AzureParams azureParams = NetworkTestUtils.getAzureParams(true, false, false);
        azureParams.setAvailabilityZones(Set.of("1", "Invalid1"));
        NetworkDto networkDto = NetworkTestUtils.getNetworkDto(azureParams, null, null, null, "0.0.0.0/0", null);

        ValidationResultBuilder resultBuilder = new ValidationResultBuilder();
        underTest.validateDuringRequest(networkDto, resultBuilder);

        NetworkTestUtils.checkErrorsPresent(resultBuilder, List.of(
                "Availability zones Invalid1 are not valid. Valid availability zones are 1,2,3."));
    }

    @Test
    void testValidateDuringRequestWhenMultipleAvailabilityZonesAreInvalid() {
        AzureParams azureParams = NetworkTestUtils.getAzureParams(true, false, false);
        azureParams.setAvailabilityZones(Set.of("1", "Invalid1", "Invalid2"));
        NetworkDto networkDto = NetworkTestUtils.getNetworkDto(azureParams, null, null, null, "0.0.0.0/0", null);

        ValidationResultBuilder resultBuilder = new ValidationResultBuilder();
        underTest.validateDuringRequest(networkDto, resultBuilder);

        NetworkTestUtils.checkErrorsPresent(resultBuilder, List.of(
                "Availability zones Invalid1,Invalid2 are not valid. Valid availability zones are 1,2,3."));
    }

    @Test
    void testValidateDuringRequestWhenExistingZonesAreEmpty() {
        AzureParams azureParams = NetworkTestUtils.getAzureParams(true, false, false);
        azureParams.setAvailabilityZones(Set.of("1", "2", "3"));
        NetworkDto networkDto = NetworkTestUtils.getNetworkDto(azureParams, null, null, null, "0.0.0.0/0", null);

        ValidationResultBuilder resultBuilder = new ValidationResultBuilder();
        underTest.validateDuringRequest(null, networkDto, resultBuilder);

        assertFalse(resultBuilder.build().hasError());

    }

    @Test
    void testValidateDuringRequestWhenExistingZonesAreContained() {
        AzureParams azureParams = NetworkTestUtils.getAzureParams(true, false, false);
        azureParams.setAvailabilityZones(Set.of("1", "2", "3"));
        NetworkDto networkDto = NetworkTestUtils.getNetworkDto(azureParams, null, null, null, "0.0.0.0/0", null);
        ValidationResultBuilder resultBuilder = new ValidationResultBuilder();
        underTest.validateDuringRequest(setUpExistingEnvironment(Set.of("1", "2")), networkDto, resultBuilder);
        assertFalse(resultBuilder.build().hasError());
    }

    @Test
    void testValidateDuringRequestWhenExistingZonesAreNotContained() {
        AzureParams azureParams = NetworkTestUtils.getAzureParams(true, false, false);
        azureParams.setAvailabilityZones(Set.of("2", "3"));
        NetworkDto networkDto = NetworkTestUtils.getNetworkDto(azureParams, null, null, null, "0.0.0.0/0", null);
        ValidationResultBuilder resultBuilder = new ValidationResultBuilder();
        underTest.validateDuringRequest(setUpExistingEnvironment(Set.of("1", "3")), networkDto, resultBuilder);
        NetworkTestUtils.checkErrorsPresent(resultBuilder, List.of(
                "Provided Availability Zones for environment do not contain the existing Availability Zones. " +
                        "Provided Availability Zones : 2,3. Existing Availability Zones : 1,3"));
    }

    private Map<String, CloudSubnet> getCloudSubnets(boolean privateEndpointNetworkPoliciesEnabled) {
        CloudSubnet cloudSubnetOne = new CloudSubnet();
        cloudSubnetOne.putParameter("privateEndpointNetworkPolicies", privateEndpointNetworkPoliciesEnabled ? "enabled" : "disabled");
        cloudSubnetOne.setName("subnet-one");
        return Map.of("subnet-one", cloudSubnetOne);
    }

    private EnvironmentValidationDto environmentValidationDtoWithSingleRg(String name, ResourceGroupUsagePattern resourceGroupUsagePattern) {
        Region usWestRegion = new Region();
        usWestRegion.setName("eastus");
        usWestRegion.setDisplayName("East US");
        return EnvironmentValidationDto.builder()
                .withValidationType(ValidationType.ENVIRONMENT_CREATION)
                .withEnvironmentDto(EnvironmentDto.builder()
                        .withAccountId("acc")
                        .withParameters(ParametersDto.builder()
                                .withAzureParametersDto(
                                        AzureParametersDto.builder()
                                                .withAzureResourceGroupDto(AzureResourceGroupDto.builder()
                                                        .withName(name)
                                                        .withResourceGroupUsagePattern(resourceGroupUsagePattern)
                                                        .build())
                                                .build()
                                ).build())
                        .withRegions(Set.of(usWestRegion))
                        .build())
                .build();
    }

    private AzureParams getAzureParams(String networkId, String networkResourceGroupName) {
        return AzureParams.builder()
                .withNetworkId(networkId)
                .withResourceGroupName(networkResourceGroupName)
                .build();
    }

    private static EnvironmentDto getEnvironmentDtoWithRegion() {
        Region usWestRegion = new Region();
        usWestRegion.setName("eastus");
        usWestRegion.setDisplayName("East US");
        EnvironmentDto environmentDto = EnvironmentDto.builder()
                .withRegions(Set.of(usWestRegion))
                .build();
        return environmentDto;
    }

    private EnvironmentValidationDto setUpExistingEnvironment(Set<String> availabilityZones) {
        AzureParams azureParams = mock(AzureParams.class);
        when(azureParams.getAvailabilityZones()).thenReturn(availabilityZones);
        NetworkDto networkDto = mock(NetworkDto.class);
        when(networkDto.getAzure()).thenReturn(azureParams);
        EnvironmentDto environmentDto = mock(EnvironmentDto.class);
        when(environmentDto.getNetwork()).thenReturn(networkDto);
        EnvironmentValidationDto environmentValidationDto = mock(EnvironmentValidationDto.class);
        when(environmentValidationDto.getEnvironmentDto()).thenReturn(environmentDto);
        return environmentValidationDto;
    }
}
