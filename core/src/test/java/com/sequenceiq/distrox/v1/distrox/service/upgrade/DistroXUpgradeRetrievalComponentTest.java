package com.sequenceiq.distrox.v1.distrox.service.upgrade;

import static com.sequenceiq.cloudbreak.ImageCatalogMock.DEFAULT_IMAGE_CATALOG_PATH;
import static com.sequenceiq.cloudbreak.auth.ThreadBasedUserCrnProvider.doAs;
import static com.sequenceiq.cloudbreak.cloud.model.catalog.ImagePackageVersion.PYTHON38;
import static com.sequenceiq.cloudbreak.common.mappable.CloudPlatform.AWS;
import static com.sequenceiq.cloudbreak.service.image.ImageCatalogService.CDP_DEFAULT_CATALOG_NAME;
import static com.sequenceiq.common.model.OsType.CENTOS7;
import static com.sequenceiq.common.model.OsType.RHEL8;
import static com.sequenceiq.distrox.api.v1.distrox.model.upgrade.DistroXUpgradeShowAvailableImages.LATEST_ONLY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.inject.Inject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.sequenceiq.cloudbreak.ImageCatalogMock;
import com.sequenceiq.cloudbreak.api.endpoint.v4.common.ResourceStatus;
import com.sequenceiq.cloudbreak.api.endpoint.v4.common.StackType;
import com.sequenceiq.cloudbreak.api.endpoint.v4.common.Status;
import com.sequenceiq.cloudbreak.auth.PaywallCredentialPopulator;
import com.sequenceiq.cloudbreak.auth.altus.EntitlementService;
import com.sequenceiq.cloudbreak.auth.crn.AccountIdService;
import com.sequenceiq.cloudbreak.client.RestClientFactory;
import com.sequenceiq.cloudbreak.cloud.model.catalog.Image;
import com.sequenceiq.cloudbreak.cluster.service.ClouderaManagerProductsProvider;
import com.sequenceiq.cloudbreak.cluster.service.ClusterComponentConfigProvider;
import com.sequenceiq.cloudbreak.cmtemplate.CmTemplateGeneratorService;
import com.sequenceiq.cloudbreak.cmtemplate.CmTemplateProcessorFactory;
import com.sequenceiq.cloudbreak.cmtemplate.generator.configuration.CmTemplateGeneratorConfigurationResolver;
import com.sequenceiq.cloudbreak.cmtemplate.generator.dependencies.ServiceDependencyMatrixService;
import com.sequenceiq.cloudbreak.cmtemplate.generator.support.DeclaredVersionService;
import com.sequenceiq.cloudbreak.cmtemplate.generator.support.SupportedVersionService;
import com.sequenceiq.cloudbreak.cmtemplate.generator.template.GeneratedCmTemplateService;
import com.sequenceiq.cloudbreak.common.service.PlatformStringTransformer;
import com.sequenceiq.cloudbreak.converter.ImageToClouderaManagerRepoConverter;
import com.sequenceiq.cloudbreak.core.CloudbreakImageCatalogException;
import com.sequenceiq.cloudbreak.core.CloudbreakImageNotFoundException;
import com.sequenceiq.cloudbreak.domain.Blueprint;
import com.sequenceiq.cloudbreak.domain.BlueprintUpgradeOption;
import com.sequenceiq.cloudbreak.domain.stack.Stack;
import com.sequenceiq.cloudbreak.domain.stack.StackStatus;
import com.sequenceiq.cloudbreak.domain.stack.cluster.Cluster;
import com.sequenceiq.cloudbreak.dto.StackDto;
import com.sequenceiq.cloudbreak.sdx.common.PlatformAwareSdxConnector;
import com.sequenceiq.cloudbreak.sdx.common.model.SdxBasicView;
import com.sequenceiq.cloudbreak.service.ComponentConfigProviderService;
import com.sequenceiq.cloudbreak.service.DefaultClouderaManagerRepoService;
import com.sequenceiq.cloudbreak.service.StackMatrixService;
import com.sequenceiq.cloudbreak.service.StackTypeResolver;
import com.sequenceiq.cloudbreak.service.cluster.ClusterDBValidationService;
import com.sequenceiq.cloudbreak.service.cluster.ClusterRepairService;
import com.sequenceiq.cloudbreak.service.image.CsdParcelNameMatcher;
import com.sequenceiq.cloudbreak.service.image.CurrentImagePackageProvider;
import com.sequenceiq.cloudbreak.service.image.CurrentImageUsageCondition;
import com.sequenceiq.cloudbreak.service.image.ImageCatalogService;
import com.sequenceiq.cloudbreak.service.image.ImageService;
import com.sequenceiq.cloudbreak.service.image.ModelImageTestBuilder;
import com.sequenceiq.cloudbreak.service.image.PreWarmParcelParser;
import com.sequenceiq.cloudbreak.service.image.StatedImage;
import com.sequenceiq.cloudbreak.service.parcel.ClouderaManagerProductTransformer;
import com.sequenceiq.cloudbreak.service.parcel.ManifestRetrieverService;
import com.sequenceiq.cloudbreak.service.parcel.ParcelFilterService;
import com.sequenceiq.cloudbreak.service.parcel.ParcelService;
import com.sequenceiq.cloudbreak.service.runtimes.SupportedRuntimes;
import com.sequenceiq.cloudbreak.service.stack.InstanceGroupService;
import com.sequenceiq.cloudbreak.service.stack.InstanceMetaDataService;
import com.sequenceiq.cloudbreak.service.stack.RuntimeVersionService;
import com.sequenceiq.cloudbreak.service.stack.StackDtoService;
import com.sequenceiq.cloudbreak.service.stack.StackService;
import com.sequenceiq.cloudbreak.service.stack.StackStopRestrictionService;
import com.sequenceiq.cloudbreak.service.stack.StackViewService;
import com.sequenceiq.cloudbreak.service.upgrade.ClusterUpgradeAvailabilityService;
import com.sequenceiq.cloudbreak.service.upgrade.ClusterUpgradeCandidateFilterService;
import com.sequenceiq.cloudbreak.service.upgrade.ComponentBuildNumberComparator;
import com.sequenceiq.cloudbreak.service.upgrade.ComponentVersionComparator;
import com.sequenceiq.cloudbreak.service.upgrade.ComponentVersionProvider;
import com.sequenceiq.cloudbreak.service.upgrade.CurrentImageRetrieverService;
import com.sequenceiq.cloudbreak.service.upgrade.ImageComponentVersionsComparator;
import com.sequenceiq.cloudbreak.service.upgrade.ImageFilterParamsFactory;
import com.sequenceiq.cloudbreak.service.upgrade.UpgradeImageInfoFactory;
import com.sequenceiq.cloudbreak.service.upgrade.UpgradeOptionsResponseFactory;
import com.sequenceiq.cloudbreak.service.upgrade.UpgradePermissionProvider;
import com.sequenceiq.cloudbreak.service.upgrade.UpgradePreconditionService;
import com.sequenceiq.cloudbreak.service.upgrade.UpgradeService;
import com.sequenceiq.cloudbreak.service.upgrade.ccm.StackCcmUpgradeService;
import com.sequenceiq.cloudbreak.service.upgrade.image.BlueprintUpgradeOptionCondition;
import com.sequenceiq.cloudbreak.service.upgrade.image.BlueprintUpgradeOptionValidator;
import com.sequenceiq.cloudbreak.service.upgrade.image.CentosToRedHatUpgradeAvailabilityService;
import com.sequenceiq.cloudbreak.service.upgrade.image.ClusterUpgradeImageFilter;
import com.sequenceiq.cloudbreak.service.upgrade.image.ImageFilterResult;
import com.sequenceiq.cloudbreak.service.upgrade.image.filter.CentosToRedHatUpgradeImageFilter;
import com.sequenceiq.cloudbreak.service.upgrade.image.filter.CloudPlatformBasedUpgradeImageFilter;
import com.sequenceiq.cloudbreak.service.upgrade.image.filter.CmAndStackVersionUpgradeImageFilter;
import com.sequenceiq.cloudbreak.service.upgrade.image.filter.CurrentImageUpgradeImageFilter;
import com.sequenceiq.cloudbreak.service.upgrade.image.filter.EntitlementDrivenPackageLocationFilter;
import com.sequenceiq.cloudbreak.service.upgrade.image.filter.IgnoredCmVersionUpgradeImageFilter;
import com.sequenceiq.cloudbreak.service.upgrade.image.filter.ImageCreationBasedUpgradeImageFilter;
import com.sequenceiq.cloudbreak.service.upgrade.image.filter.ImageFilterUpgradeService;
import com.sequenceiq.cloudbreak.service.upgrade.image.filter.ImageRegionUpgradeImageFilter;
import com.sequenceiq.cloudbreak.service.upgrade.image.filter.NonCmUpgradeImageFilter;
import com.sequenceiq.cloudbreak.service.upgrade.image.filter.OsVersionBasedUpgradeImageFilter;
import com.sequenceiq.cloudbreak.service.upgrade.image.filter.RuntimeDependencyBasedUpgradeImageFilter;
import com.sequenceiq.cloudbreak.service.upgrade.image.filter.TargetImageIdImageFilter;
import com.sequenceiq.cloudbreak.service.upgrade.image.filter.UpgradeImageFilter;
import com.sequenceiq.cloudbreak.service.upgrade.image.locked.CmVersionMatcher;
import com.sequenceiq.cloudbreak.service.upgrade.image.locked.LockedComponentChecker;
import com.sequenceiq.cloudbreak.service.upgrade.image.locked.LockedComponentService;
import com.sequenceiq.cloudbreak.service.upgrade.image.locked.ParcelMatcher;
import com.sequenceiq.cloudbreak.service.upgrade.image.locked.StackVersionMatcher;
import com.sequenceiq.cloudbreak.service.upgrade.matrix.UpgradeMatrixDefinition;
import com.sequenceiq.cloudbreak.service.upgrade.matrix.UpgradeMatrixService;
import com.sequenceiq.cloudbreak.service.upgrade.sync.component.ImageReaderService;
import com.sequenceiq.cloudbreak.service.upgrade.validation.PythonVersionBasedRuntimeVersionValidator;
import com.sequenceiq.cloudbreak.service.user.UserService;
import com.sequenceiq.cloudbreak.structuredevent.CloudbreakRestRequestThreadLocalService;
import com.sequenceiq.cloudbreak.workspace.model.Tenant;
import com.sequenceiq.cloudbreak.workspace.model.Workspace;
import com.sequenceiq.distrox.api.v1.distrox.model.upgrade.DistroXUpgradeShowAvailableImages;
import com.sequenceiq.distrox.api.v1.distrox.model.upgrade.DistroXUpgradeV1Request;
import com.sequenceiq.distrox.api.v1.distrox.model.upgrade.DistroXUpgradeV1Response;
import com.sequenceiq.distrox.v1.distrox.StackUpgradeOperations;
import com.sequenceiq.distrox.v1.distrox.controller.DistroXUpgradeV1Controller;
import com.sequenceiq.distrox.v1.distrox.converter.UpgradeConverter;
import com.sequenceiq.distrox.v1.distrox.service.upgrade.rds.DistroXRdsUpgradeService;

@ExtendWith(SpringExtension.class)
@TestPropertySource(properties = {
        "cb.stoprestriction.restrictedCloudPlatform=AWS",
        "cb.runtimes.latest=7.2.18"
})
public class DistroXUpgradeRetrievalComponentTest {

    private static final String USER_CRN = "crn:cdp:iam:us-west-1:1234:user:1";

    private static final String CLUSTER_CRN = "crn:cdp:datahub:us-west-1:Hortonworks:cluster:bdecadf7-fb39-4bef-85c4-0cdb9e203111";

    private static final String CLUSTER_NAME = "test-cluster";

    private static final Long STACK_ID = 1L;

    private static final long WORKSPACE_ID = 12L;

    private static final String REGION = "eu-central-1";

    private static final String CLOUD_PLATFORM = AWS.name();

    private final ImageCatalogMock imageCatalogMock = new ImageCatalogMock();

    @Inject
    private DistroXUpgradeV1Controller distroXUpgradeV1Controller;

    @MockBean
    private EntitlementService entitlementService;

    @MockBean
    private StackService stackService;

    @MockBean
    @Qualifier("stackViewServiceDeprecated")
    private StackViewService stackViewService;

    @MockBean
    private PlatformAwareSdxConnector platformAwareSdxConnector;

    @MockBean
    private ClusterDBValidationService clusterDBValidationService;

    @MockBean
    private UserService userService;

    @MockBean
    private UpgradeService upgradeService;

    @MockBean
    private InstanceGroupService instanceGroupService;

    @MockBean
    private UpgradePreconditionService upgradePreconditionService;

    @MockBean
    private ClusterComponentConfigProvider clusterComponentConfigProvider;

    @MockBean
    private ImageCatalogService imageCatalogService;

    @MockBean
    private ImageService imageService;

    @MockBean
    private ClusterRepairService clusterRepairService;

    @MockBean
    private ParcelService parcelService;

    @MockBean
    private InstanceMetaDataService instanceMetaDataService;

    @MockBean
    private CurrentImageUsageCondition currentImageUsageCondition;

    @MockBean
    private RuntimeVersionService runtimeVersionService;

    @MockBean
    private StackStopRestrictionService stackStopRestrictionService;

    @MockBean
    private ComponentConfigProviderService componentConfigProviderService;

    @MockBean
    private StackDtoService stackDtoService;

    @MockBean
    private CurrentImagePackageProvider currentImagePackageProvider;

    @MockBean
    private StackMatrixService stackMatrixService;

    @MockBean
    private PaywallCredentialPopulator paywallCredentialPopulator;

    @MockBean
    private DistroXUpgradeService distroXUpgradeService;

    @MockBean
    private DistroXRdsUpgradeService rdsUpgradeService;

    @MockBean
    private StackCcmUpgradeService stackCcmUpgradeService;

    private final Stack stack = createStack();

    private final StackDto mockStackDto = createMockStackDto();

    @BeforeEach
    public void before() throws CloudbreakImageCatalogException {
        when(clusterDBValidationService.isGatewayRepairEnabled(any())).thenReturn(true);
        when(entitlementService.isDifferentDataHubAndDataLakeVersionAllowed(anyString())).thenReturn(false);
        when(stackService.getByNameOrCrnInWorkspace(any(), any())).thenReturn(stack);
        when(stackDtoService.getById(STACK_ID)).thenReturn(mockStackDto);
        when(platformAwareSdxConnector.getSdxBasicViewByEnvironmentCrn(any())).thenReturn(createSdxBasicView());
        when(imageCatalogService.getAllCdhImages(any(), any(), any(), any())).thenReturn(imageCatalogMock.getAllCdhImages(CLOUD_PLATFORM));
        when(currentImagePackageProvider.currentInstancesContainsPackage(STACK_ID, imageCatalogMock.getAllCdhImages(CLOUD_PLATFORM), PYTHON38)).thenReturn(true);
        when(currentImageUsageCondition.currentImageUsedOnInstances(any(), any())).thenReturn(true);
    }

    @Test
    void testUpgradeClusterByNameWhenTheCurrentIs7216() throws CloudbreakImageNotFoundException, CloudbreakImageCatalogException {
        Image currentCatalogImage = imageCatalogMock.getLatestImageByRuntimeAndPlatformAndOs("7.2.16", CLOUD_PLATFORM, CENTOS7);
        setupImageCatalogMocks(currentCatalogImage);

        DistroXUpgradeV1Response actual = doAs(USER_CRN, () -> distroXUpgradeV1Controller.upgradeClusterByName(CLUSTER_NAME, createRequest(LATEST_ONLY)));

        assertTrue(actual.reason().isEmpty(), actual.reason());
        assertUpgradeCandidateNumber(1, actual);
        assertUpgradeCandidate(actual, "3d617c75-f252-4879-8da8-f60f2e57324b", "7.2.17 runtime upgrade");
    }

    @Test
    void testUpgradeClusterByNameWhenTheCurrentIs7216WithoutPython() throws CloudbreakImageNotFoundException, CloudbreakImageCatalogException {
        Image currentCatalogImage = imageCatalogMock.getLatestImageByRuntimeAndPlatformAndOs("7.2.16", CLOUD_PLATFORM, CENTOS7);
        setupImageCatalogMocks(currentCatalogImage);
        when(currentImageUsageCondition.currentImageUsedOnInstances(any(), any())).thenReturn(false);
        when(currentImagePackageProvider.currentInstancesContainsPackage(STACK_ID, imageCatalogMock.getAllCdhImages(CLOUD_PLATFORM), PYTHON38))
                .thenReturn(false);

        DistroXUpgradeV1Response actual = doAs(USER_CRN, () -> distroXUpgradeV1Controller.upgradeClusterByName(CLUSTER_NAME, createRequest(LATEST_ONLY)));

        assertTrue(actual.reason().isEmpty(), actual.reason());
        assertUpgradeCandidateNumber(1, actual);
        assertUpgradeCandidate(actual, "16c21dea-03f8-4178-8afd-2e1c89bea2da", "7.2.16 OS upgrade candidate for the current image");
    }

    @Test
    void testUpgradeClusterByNameWhenTheCurrentIs7217RedHat() throws CloudbreakImageNotFoundException, CloudbreakImageCatalogException {
        Image currentCatalogImage = imageCatalogMock.getLatestImageByRuntimeAndPlatformAndOs("7.2.17", CLOUD_PLATFORM, RHEL8);
        setupImageCatalogMocks(currentCatalogImage);

        DistroXUpgradeV1Response actual = doAs(USER_CRN, () -> distroXUpgradeV1Controller.upgradeClusterByName(CLUSTER_NAME, createRequest(LATEST_ONLY)));

        assertTrue(actual.reason().isEmpty(), actual.reason());
        assertUpgradeCandidateNumber(1, actual);
        assertUpgradeCandidate(actual, "85e571ef-725e-4b3e-a6ce-2c2878ff207c", "7.2.18 RedHat runtime upgrade");
    }

    @Test
    void testUpgradeClusterByNameWhenTheCurrentIs7217WithoutOsUpgrade() throws CloudbreakImageNotFoundException, CloudbreakImageCatalogException {
        Image currentCatalogImage = imageCatalogMock.getLatestImageByRuntimeAndPlatformAndOs("7.2.17", CLOUD_PLATFORM, CENTOS7);
        setupImageCatalogMocks(currentCatalogImage);
        when(currentImageUsageCondition.currentImageUsedOnInstances(any(), any())).thenReturn(false);

        DistroXUpgradeV1Response actual = doAs(USER_CRN, () -> distroXUpgradeV1Controller.upgradeClusterByName(CLUSTER_NAME, createRequest(LATEST_ONLY)));

        assertTrue(actual.reason().isEmpty(), actual.reason());
        assertUpgradeCandidateNumber(2, actual);
        assertUpgradeCandidate(actual, "3d617c75-f252-4879-8da8-f60f2e57324b", "7.2.17 OS upgrade candidate for the current image");
        assertUpgradeCandidate(actual, "4b6a691a-bdb3-4ef2-9be8-5d64a4636e31", "7.2.17 RedHat OS upgrade");
    }

    @Test
    void testUpgradeClusterByNameWhenTheCurrentIs7217Centos() throws CloudbreakImageNotFoundException, CloudbreakImageCatalogException {
        Image currentCatalogImage = imageCatalogMock.getLatestImageByRuntimeAndPlatformAndOs("7.2.17", CLOUD_PLATFORM, CENTOS7);
        setupImageCatalogMocks(currentCatalogImage);

        DistroXUpgradeV1Response actual = doAs(USER_CRN, () -> distroXUpgradeV1Controller.upgradeClusterByName(CLUSTER_NAME, createRequest(LATEST_ONLY)));

        assertTrue(actual.reason().isEmpty(), actual.reason());
        assertUpgradeCandidateNumber(1, actual);
        assertUpgradeCandidate(actual, "4b6a691a-bdb3-4ef2-9be8-5d64a4636e31", "7.2.17 RedHat runtime upgrade");
    }

    @Test
    void testUpgradeClusterByNameWhenTheCurrentIs7212() throws CloudbreakImageNotFoundException, CloudbreakImageCatalogException {
        Image currentCatalogImage = imageCatalogMock.getLatestImageByRuntimeAndPlatformAndOs("7.2.12", CLOUD_PLATFORM, CENTOS7);
        setupImageCatalogMocks(currentCatalogImage);

        DistroXUpgradeV1Response actual = doAs(USER_CRN, () -> distroXUpgradeV1Controller.upgradeClusterByName(CLUSTER_NAME, createRequest(LATEST_ONLY)));

        assertTrue(actual.reason().isEmpty());
        assertUpgradeCandidateNumber(4, actual);
        assertUpgradeCandidate(actual, "ddf985e2-781a-40e7-b4d0-17673b306bad", "7.2.14 runtime upgrade");
        assertUpgradeCandidate(actual, "21ef3ca1-51e5-4d97-880e-06998c51c707", "7.2.15 runtime upgrade");
        assertUpgradeCandidate(actual, "16c21dea-03f8-4178-8afd-2e1c89bea2da", "7.2.16 runtime upgrade");
        assertUpgradeCandidate(actual, "3d617c75-f252-4879-8da8-f60f2e57324b", "7.2.17 runtime upgrade");
    }

    private void assertUpgradeCandidateNumber(int expected, DistroXUpgradeV1Response actual) {
        assertEquals(expected, actual.upgradeCandidates().size(), String.format("Upgrade candidates: %s", actual.upgradeCandidates()));
    }

    private void setupImageCatalogMocks(com.sequenceiq.cloudbreak.cloud.model.catalog.Image currentCatalogImage)
            throws CloudbreakImageNotFoundException, CloudbreakImageCatalogException {
        ImageFilterResult imageFilterResult = imageCatalogMock.getAvailableImages(currentCatalogImage.getUuid(), CLOUD_PLATFORM);
        com.sequenceiq.cloudbreak.cloud.model.Image currentModelImage = createCurrentModelImage(currentCatalogImage);
        when(imageService.getImage(STACK_ID)).thenReturn(currentModelImage);
        when(imageCatalogService.getImage(any(), any(), any(), any())).thenReturn(createStatedImage(currentCatalogImage));
        when(imageCatalogService.getImageFilterResult(any(), any(), any(), anyBoolean(), any())).thenReturn(imageFilterResult);
    }

    private void assertUpgradeCandidate(DistroXUpgradeV1Response actual, String imageId, String description) {
        assertTrue(actual.upgradeCandidates().stream().anyMatch(image -> image.getImageId().equals(imageId)),
                String.format("%s not found with image id %s", description, imageId));
    }

    private StatedImage createStatedImage(com.sequenceiq.cloudbreak.cloud.model.catalog.Image currentCatalogImage) {
        return StatedImage.statedImage(currentCatalogImage, DEFAULT_IMAGE_CATALOG_PATH, CDP_DEFAULT_CATALOG_NAME);
    }

    private com.sequenceiq.cloudbreak.cloud.model.Image createCurrentModelImage(Image currentCatalogImage) {
        return ModelImageTestBuilder.builder()
                .withImageName(currentCatalogImage.getImageSetsByProvider().get(CLOUD_PLATFORM.toLowerCase()).get(REGION))
                .withImageId(currentCatalogImage.getUuid())
                .withImageCatalogUrl(DEFAULT_IMAGE_CATALOG_PATH)
                .withImageCatalogName(CDP_DEFAULT_CATALOG_NAME)
                .build();
    }

    private Stack createStack() {
        Tenant tenant = new Tenant();
        tenant.setName("tenant");

        Workspace workspace = new Workspace();
        workspace.setId(WORKSPACE_ID);
        workspace.setName("default-workspace");
        workspace.setTenant(tenant);

        Blueprint blueprint = new Blueprint();
        blueprint.setStatus(ResourceStatus.DEFAULT);
        blueprint.setBlueprintUpgradeOption(BlueprintUpgradeOption.ENABLED);

        Cluster cluster = new Cluster();
        cluster.setBlueprint(blueprint);
        cluster.setId(STACK_ID);
        cluster.setName(CLUSTER_NAME);
        cluster.setRangerRazEnabled(false);

        StackStatus stackStatus = new StackStatus();
        stackStatus.setStatus(Status.AVAILABLE);

        Stack stack = new Stack();
        stack.setId(STACK_ID);
        stack.setName(CLUSTER_NAME);
        stack.setType(StackType.WORKLOAD);
        stack.setCloudPlatform(CLOUD_PLATFORM);
        stack.setStackStatus(stackStatus);
        stack.setWorkspace(workspace);
        stack.setCluster(cluster);
        stack.setRegion(REGION);
        return stack;
    }

    private StackDto createMockStackDto() {
        StackDto stackDto = Mockito.mock(StackDto.class);
        when(stackDto.getResourceCrn()).thenReturn(CLUSTER_CRN);
        when(stackDto.getId()).thenReturn(STACK_ID);
        when(stackDto.getName()).thenReturn("stack-name");
        when(stackDto.getWorkspaceId()).thenReturn(WORKSPACE_ID);
        when(stackDto.getType()).thenReturn(StackType.WORKLOAD);
        when(stackDto.getStack()).thenReturn(stack);
        when(stackDto.getBlueprint()).thenReturn(stack.getCluster().getBlueprint());
        return stackDto;
    }

    private DistroXUpgradeV1Request createRequest(DistroXUpgradeShowAvailableImages showAvailableImages) {
        DistroXUpgradeV1Request request = new DistroXUpgradeV1Request();
        request.setShowAvailableImages(showAvailableImages);
        return request;
    }

    private Optional<SdxBasicView> createSdxBasicView() {
        return Optional.of(new SdxBasicView(CLUSTER_NAME, null, null, false, null, null));
    }

    @TestConfiguration
    @Import(value = {
            DistroXUpgradeV1Controller.class,
            UpgradeConverter.class,
            AccountIdService.class,
            CloudbreakRestRequestThreadLocalService.class,
            DistroXUpgradeAvailabilityService.class,
            StackUpgradeOperations.class,
            DistroXUpgradeResponseFilterService.class,
            ClusterUpgradeAvailabilityService.class,
            CurrentImageRetrieverService.class,
            ClusterUpgradeCandidateFilterService.class,
            ClusterUpgradeImageFilter.class,
            BlueprintUpgradeOptionValidator.class,
            CmTemplateProcessorFactory.class,
            BlueprintUpgradeOptionCondition.class,
            ImageFilterUpgradeService.class,
            CentosToRedHatUpgradeImageFilter.class,
            CloudPlatformBasedUpgradeImageFilter.class,
            CmAndStackVersionUpgradeImageFilter.class,
            CurrentImageUpgradeImageFilter.class,
            EntitlementDrivenPackageLocationFilter.class,
            IgnoredCmVersionUpgradeImageFilter.class,
            ImageCreationBasedUpgradeImageFilter.class,
            ImageRegionUpgradeImageFilter.class,
            NonCmUpgradeImageFilter.class,
            OsVersionBasedUpgradeImageFilter.class,
            RuntimeDependencyBasedUpgradeImageFilter.class,
            TargetImageIdImageFilter.class,
            UpgradeOptionsResponseFactory.class,
            ComponentVersionProvider.class,
            PlatformStringTransformer.class,
            ImageFilterParamsFactory.class,
            ClouderaManagerProductsProvider.class,
            ClouderaManagerProductTransformer.class,
            PreWarmParcelParser.class,
            CsdParcelNameMatcher.class,
            ParcelFilterService.class,
            ImageComponentVersionsComparator.class,
            CentosToRedHatUpgradeAvailabilityService.class,
            LockedComponentChecker.class,
            StackVersionMatcher.class,
            CmVersionMatcher.class,
            ParcelMatcher.class,
            UpgradeImageInfoFactory.class,
            UpgradePermissionProvider.class,
            ComponentBuildNumberComparator.class,
            ComponentVersionComparator.class,
            UpgradeMatrixService.class,
            UpgradeMatrixDefinition.class,
            SupportedRuntimes.class,
            PythonVersionBasedRuntimeVersionValidator.class,
            LockedComponentService.class,
            CmTemplateGeneratorService.class,
            SupportedVersionService.class,
            ServiceDependencyMatrixService.class,
            GeneratedCmTemplateService.class,
            DeclaredVersionService.class,
            CmTemplateGeneratorConfigurationResolver.class,
            CmTemplateProcessorFactory.class,
            ImageReaderService.class,
            DefaultClouderaManagerRepoService.class,
            StackTypeResolver.class,
            ImageToClouderaManagerRepoConverter.class,
            RestClientFactory.class,
            ManifestRetrieverService.class,
            CloudbreakRestRequestThreadLocalService.class,
    })
    static class Config {

        @Inject
        private Set<UpgradeImageFilter> upgradeImageFilters;

        @Bean
        public List<UpgradeImageFilter> orderedUpgradeImageFilters() {
            return upgradeImageFilters.stream()
                    .sorted(Comparator.comparingInt(UpgradeImageFilter::getFilterOrderNumber))
                    .collect(Collectors.toList());
        }

    }
}


