package com.sequenceiq.it.cloudbreak.testcase.e2e.sdx;

import static com.sequenceiq.it.cloudbreak.cloud.HostGroupType.IDBROKER;
import static com.sequenceiq.it.cloudbreak.cloud.HostGroupType.MASTER;
import static com.sequenceiq.it.cloudbreak.context.RunningParameter.doNotWaitForFlow;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import jakarta.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import com.sequenceiq.cloudbreak.api.endpoint.v4.imagecatalog.responses.ImageV4Response;
import com.sequenceiq.common.model.OsType;
import com.sequenceiq.it.cloudbreak.assertion.audit.DatalakeAuditGrpcServiceAssertion;
import com.sequenceiq.it.cloudbreak.client.SdxTestClient;
import com.sequenceiq.it.cloudbreak.cloud.v4.CloudProvider;
import com.sequenceiq.it.cloudbreak.cloud.v4.CommonClusterManagerProperties;
import com.sequenceiq.it.cloudbreak.context.Description;
import com.sequenceiq.it.cloudbreak.context.TestContext;
import com.sequenceiq.it.cloudbreak.dto.sdx.SdxTestDto;
import com.sequenceiq.it.cloudbreak.exception.TestFailException;
import com.sequenceiq.it.cloudbreak.util.SdxUtil;
import com.sequenceiq.it.cloudbreak.util.VolumeUtils;
import com.sequenceiq.it.cloudbreak.util.spot.UseSpotInstances;
import com.sequenceiq.it.util.imagevalidation.ImageValidatorE2ETest;
import com.sequenceiq.it.util.imagevalidation.ImageValidatorE2ETestUtil;
import com.sequenceiq.sdx.api.model.SdxClusterShape;
import com.sequenceiq.sdx.api.model.SdxClusterStatusResponse;
import com.sequenceiq.sdx.api.model.SdxDatabaseAvailabilityType;
import com.sequenceiq.sdx.api.model.SdxDatabaseRequest;

public class SdxUpgradeTests extends PreconditionSdxE2ETest implements ImageValidatorE2ETest {
    private static final Logger LOGGER = LoggerFactory.getLogger(SdxUpgradeTests.class);

    @Inject
    private SdxTestClient sdxTestClient;

    @Inject
    private SdxUtil sdxUtil;

    @Inject
    private CommonClusterManagerProperties commonClusterManagerProperties;

    @Inject
    private DatalakeAuditGrpcServiceAssertion datalakeAuditGrpcServiceAssertion;

    @Inject
    private SdxUpgradeDatabaseTestUtil sdxUpgradeDatabaseTestUtil;

    @Inject
    private ImageValidatorE2ETestUtil imageValidatorE2ETestUtil;

    @Test(dataProvider = TEST_CONTEXT)
    @UseSpotInstances
    @Description(
            given = "there is a running Cloudbreak, and an SDX cluster in available state",
            when = "upgrade called on the SDX cluster",
            then = "SDX upgrade should be successful, the cluster should be up and running"
    )
    public void testSDXUpgrade(TestContext testContext) {
        List<String> actualVolumeIds = new ArrayList<>();
        List<String> expectedVolumeIds = new ArrayList<>();

        SdxTestDto sdxTestDto = testContext.given(SdxTestDto.class)
                .withCloudStorage()
                .withExternalDatabase(sdxDbRequest(testContext.getCloudProvider()));
        setupRuntimeParameters(testContext, sdxTestDto);
        sdxTestDto
                .when(sdxTestClient.create())
                .await(SdxClusterStatusResponse.RUNNING)
                .awaitForHealthyInstances()
                .then((tc, testDto, client) -> {
                    List<String> instances = sdxUtil.getInstanceIds(testDto, client, MASTER.getName());
                    instances.addAll(sdxUtil.getInstanceIds(testDto, client, IDBROKER.getName()));
                    expectedVolumeIds.addAll(getCloudFunctionality(tc).listInstancesVolumeIds(testDto.getName(), instances));
                    return testDto;
                })
                .when(sdxTestClient.upgrade())
                .await(SdxClusterStatusResponse.DATALAKE_UPGRADE_IN_PROGRESS, doNotWaitForFlow())
                .await(SdxClusterStatusResponse.RUNNING)
                .awaitForHealthyInstances()
                .then((tc, testDto, client) -> {
                    List<String> instanceIds = sdxUtil.getInstanceIds(testDto, client, MASTER.getName());
                    instanceIds.addAll(sdxUtil.getInstanceIds(testDto, client, IDBROKER.getName()));
                    actualVolumeIds.addAll(getCloudFunctionality(tc).listInstancesVolumeIds(testDto.getName(), instanceIds));
                    return testDto;
                })
                .then((tc, testDto, client) -> VolumeUtils.compareVolumeIdsAfterRepair(testDto, actualVolumeIds, expectedVolumeIds))
                .then((tc, testDto, client) -> sdxUpgradeDatabaseTestUtil.checkCloudProviderDatabaseVersionFromMasterNode(
                        testDto.getResponse().getDatabaseEngineVersion(), tc, testDto))
                // This assertion is disabled until the Audit Service is not configured.
                //.then(datalakeAuditGrpcServiceAssertion::upgradeClusterByNameInternal)
                .validate();
    }

    private SdxDatabaseRequest sdxDbRequest(CloudProvider cloudProvider) {
        SdxDatabaseRequest sdxDatabaseRequest = new SdxDatabaseRequest();
        sdxDatabaseRequest.setAvailabilityType(SdxDatabaseAvailabilityType.NONE);
        sdxDatabaseRequest.setDatabaseEngineVersion(cloudProvider.getEmbeddedDbUpgradeSourceVersion());
        return sdxDatabaseRequest;
    }

    private void setupRuntimeParameters(TestContext testContext, SdxTestDto sdxTestDto) {
        if (imageValidatorE2ETestUtil.isImageValidation()) {
            Optional<ImageV4Response> imageUnderValidation = imageValidatorE2ETestUtil.getImage(testContext);
            String imageUnderValidationVersion = imageUnderValidation.get().getVersion();
            String os = imageUnderValidation.get().getOs();
            if (OsType.RHEL8.getOs().equalsIgnoreCase(os) && "7.2.17".equals(imageUnderValidationVersion)) {
                // RHEL8 was introduced with 7.2.17, meaning we can not upgrade from a previous version but have to get the latest image with same runtime
                ImageV4Response latestImageWithSameRuntime = imageValidatorE2ETestUtil.getLatestImageWithSameRuntimeAsImageUnderValidation(testContext);
                if (latestImageWithSameRuntime == null) {
                    throw new TestFailException("No other image found with version " + imageUnderValidationVersion);
                }
                sdxTestDto.withImage(imageValidatorE2ETestUtil.getImageCatalogName(), latestImageWithSameRuntime.getUuid());
            } else {
                String runtimeVersion = commonClusterManagerProperties.getUpgrade().getMatrix().get(imageUnderValidationVersion);
                if (runtimeVersion == null) {
                    throw new TestFailException("Upgrade matrix entry is not defined for image version " + imageUnderValidationVersion);
                }
                sdxTestDto
                        .withOs(os)
                        .withRuntimeVersion(runtimeVersion);
            }
        } else {
            sdxTestDto.withRuntimeVersion(commonClusterManagerProperties.getUpgrade().getCurrentRuntimeVersion());
        }
    }

    @Test(dataProvider = TEST_CONTEXT)
    @UseSpotInstances
    @Description(
            given = "there is a running Cloudbreak, and a HA SDX cluster in available state",
            when = "upgrade called on the HA SDX cluster",
            then = "HA SDX upgrade should be successful, the cluster should be up and running"
    )
    public void testSDXHAUpgrade(TestContext testContext) {
        List<String> actualVolumeIds = new ArrayList<>();
        List<String> expectedVolumeIds = new ArrayList<>();

        testContext
                .given(SdxTestDto.class)
                    .withClusterShape(SdxClusterShape.MEDIUM_DUTY_HA)
                    .withCloudStorage()
                    .withRuntimeVersion(commonClusterManagerProperties.getUpgrade().getCurrentHARuntimeVersion())
                .when(sdxTestClient.create())
                .await(SdxClusterStatusResponse.RUNNING)
                .awaitForHealthyInstances()
                .then((tc, testDto, client) -> {
                    List<String> instances = sdxUtil.getInstanceIds(testDto, client, MASTER.getName());
                    instances.addAll(sdxUtil.getInstanceIds(testDto, client, IDBROKER.getName()));
                    expectedVolumeIds.addAll(getCloudFunctionality(tc).listInstancesVolumeIds(testDto.getName(), instances));
                    return testDto;
                })
                .when(sdxTestClient.upgrade())
                .await(SdxClusterStatusResponse.DATALAKE_UPGRADE_IN_PROGRESS, doNotWaitForFlow())
                .await(SdxClusterStatusResponse.RUNNING)
                .awaitForHealthyInstances()
                .then((tc, testDto, client) -> {
                    List<String> instanceIds = sdxUtil.getInstanceIds(testDto, client, MASTER.getName());
                    instanceIds.addAll(sdxUtil.getInstanceIds(testDto, client, IDBROKER.getName()));
                    actualVolumeIds.addAll(getCloudFunctionality(tc).listInstancesVolumeIds(testDto.getName(), instanceIds));
                    return testDto;
                })
                .then((tc, testDto, client) -> VolumeUtils.compareVolumeIdsAfterRepair(testDto, actualVolumeIds, expectedVolumeIds))
                // This assertion is disabled until the Audit Service is not configured.
                //.then(datalakeAuditGrpcServiceAssertion::upgradeClusterByNameInternal)
                .validate();
    }

    @Override
    public String getCbImageId(TestContext testContext) {
        return testContext.get(SdxTestDto.class).getResponse().getStackV4Response().getImage().getId();
    }
}
