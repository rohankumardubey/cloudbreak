package com.sequenceiq.datalake.controller.sdx;

import javax.inject.Inject;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import com.sequenceiq.authorization.annotation.CheckPermissionByResourceCrn;
import com.sequenceiq.authorization.annotation.CheckPermissionByResourceName;
import com.sequenceiq.authorization.annotation.ResourceCrn;
import com.sequenceiq.authorization.annotation.ResourceName;
import com.sequenceiq.authorization.resource.AuthorizationResourceAction;
import com.sequenceiq.cloudbreak.api.endpoint.v4.dto.NameOrCrn;
import com.sequenceiq.cloudbreak.auth.ThreadBasedUserCrnProvider;
import com.sequenceiq.cloudbreak.auth.security.internal.TenantAwareParam;
import com.sequenceiq.cloudbreak.structuredevent.rest.annotation.AccountEntityType;
import com.sequenceiq.datalake.entity.SdxCluster;
import com.sequenceiq.datalake.service.upgrade.SdxRuntimeUpgradeService;
import com.sequenceiq.datalake.service.upgrade.recovery.SdxUpgradeRecoveryService;
import com.sequenceiq.sdx.api.endpoint.SdxUpgradeEndpoint;
import com.sequenceiq.sdx.api.model.SdxRecoverableResponse;
import com.sequenceiq.sdx.api.model.SdxRecoveryRequest;
import com.sequenceiq.sdx.api.model.SdxRecoveryResponse;
import com.sequenceiq.sdx.api.model.SdxUpgradeRequest;
import com.sequenceiq.sdx.api.model.SdxUpgradeResponse;

@Controller
@AccountEntityType(SdxCluster.class)
public class SdxUpgradeController implements SdxUpgradeEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(SdxUpgradeController.class);

    @Inject
    private SdxRuntimeUpgradeService sdxRuntimeUpgradeService;

    @Inject
    private SdxUpgradeRecoveryService sdxUpgradeRecoveryService;

    @Override
    @CheckPermissionByResourceName(action = AuthorizationResourceAction.UPGRADE_DATALAKE)
    public SdxUpgradeResponse upgradeClusterByName(@ResourceName String clusterName, SdxUpgradeRequest request) {
        String userCrn = ThreadBasedUserCrnProvider.getUserCrn();
        if (request.isDryRun() || request.isShowAvailableImagesSet()) {
            return sdxRuntimeUpgradeService.checkForUpgradeByName(userCrn, clusterName, request, ThreadBasedUserCrnProvider.getAccountId());
        } else {
            return sdxRuntimeUpgradeService.triggerUpgradeByName(userCrn, clusterName, request, ThreadBasedUserCrnProvider.getAccountId());
        }
    }

    @Override
    @CheckPermissionByResourceCrn(action = AuthorizationResourceAction.UPGRADE_DATALAKE)
    public SdxUpgradeResponse upgradeClusterByCrn(@TenantAwareParam @ResourceCrn String clusterCrn, SdxUpgradeRequest request) {
        String userCrn = ThreadBasedUserCrnProvider.getUserCrn();
        if (request.isDryRun() || request.isShowAvailableImagesSet()) {
            return sdxRuntimeUpgradeService.checkForUpgradeByCrn(userCrn, clusterCrn, request, ThreadBasedUserCrnProvider.getAccountId());
        } else {
            return sdxRuntimeUpgradeService.triggerUpgradeByCrn(userCrn, clusterCrn, request, ThreadBasedUserCrnProvider.getAccountId());
        }
    }

    @Override
    @CheckPermissionByResourceName(action = AuthorizationResourceAction.RECOVER_DATALAKE)
    public SdxRecoveryResponse recoverClusterByName(@ResourceName String name, @Valid SdxRecoveryRequest recoverSdxClusterRequest) {
        String userCrn = ThreadBasedUserCrnProvider.getUserCrn();
        return sdxUpgradeRecoveryService.triggerRecovery(userCrn, NameOrCrn.ofName(name), recoverSdxClusterRequest);
    }

    @Override
    @CheckPermissionByResourceCrn(action = AuthorizationResourceAction.RECOVER_DATALAKE)
    public SdxRecoveryResponse recoverClusterByCrn(@ResourceCrn @TenantAwareParam String crn,
            @Valid SdxRecoveryRequest recoverSdxClusterRequest) {
        String userCrn = ThreadBasedUserCrnProvider.getUserCrn();
        return sdxUpgradeRecoveryService.triggerRecovery(userCrn, NameOrCrn.ofCrn(crn), recoverSdxClusterRequest);
    }

    @Override
    @CheckPermissionByResourceName(action = AuthorizationResourceAction.RECOVER_DATALAKE)
    public SdxRecoverableResponse getClusterRecoverableByName(@ResourceName String name) {
        String userCrn = ThreadBasedUserCrnProvider.getUserCrn();
        return sdxUpgradeRecoveryService.validateRecovery(userCrn, NameOrCrn.ofName(name));
    }

    @Override
    @CheckPermissionByResourceCrn(action = AuthorizationResourceAction.RECOVER_DATALAKE)
    public SdxRecoverableResponse getClusterRecoverableByCrn(@ResourceCrn @TenantAwareParam String crn) {
        String userCrn = ThreadBasedUserCrnProvider.getUserCrn();
        return sdxUpgradeRecoveryService.validateRecovery(userCrn, NameOrCrn.ofCrn(crn));
    }

}
