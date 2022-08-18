package com.sequenceiq.cloudbreak.service.upgrade.rds;

import static com.sequenceiq.cloudbreak.event.ResourceEvent.CLUSTER_RDS_UPGRADE_ALREADY_UPGRADED;
import static com.sequenceiq.cloudbreak.event.ResourceEvent.CLUSTER_RDS_UPGRADE_NOT_AVAILABLE;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sequenceiq.cloudbreak.api.endpoint.v4.common.Status;
import com.sequenceiq.cloudbreak.api.endpoint.v4.dto.NameOrCrn;
import com.sequenceiq.cloudbreak.api.endpoint.v4.stacks.response.database.StackDatabaseServerResponse;
import com.sequenceiq.cloudbreak.api.endpoint.v4.stacks.response.upgrade.RdsUpgradeV4Response;
import com.sequenceiq.cloudbreak.api.model.RdsUpgradeResponseType;
import com.sequenceiq.cloudbreak.common.database.MajorVersion;
import com.sequenceiq.cloudbreak.common.database.TargetMajorVersion;
import com.sequenceiq.cloudbreak.core.flow2.service.ReactorFlowManager;
import com.sequenceiq.cloudbreak.event.ResourceEvent;
import com.sequenceiq.cloudbreak.logger.MDCBuilder;
import com.sequenceiq.cloudbreak.message.CloudbreakMessagesService;
import com.sequenceiq.cloudbreak.service.database.DatabaseService;
import com.sequenceiq.cloudbreak.service.stack.StackDtoService;
import com.sequenceiq.cloudbreak.structuredevent.CloudbreakRestRequestThreadLocalService;
import com.sequenceiq.cloudbreak.view.StackView;
import com.sequenceiq.flow.api.model.FlowIdentifier;

@Service
public class RdsUpgradeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RdsUpgradeService.class);

    @Inject
    private CloudbreakRestRequestThreadLocalService restRequestThreadLocalService;

    @Inject
    private StackDtoService stackDtoService;

    @Inject
    private ReactorFlowManager reactorFlowManager;

    @Inject
    private DatabaseService databaseService;

    @Inject
    private CloudbreakMessagesService messagesService;

    public RdsUpgradeV4Response upgradeRds(NameOrCrn nameOrCrn, TargetMajorVersion targetMajorVersion) {
        String accountId = restRequestThreadLocalService.getAccountId();
        StackView stack = stackDtoService.getStackViewByNameOrCrn(nameOrCrn, accountId);
        MDCBuilder.buildMdcContext(stack);
        LOGGER.info("RDS upgrade has been initiated for stack {}", nameOrCrn.getNameOrCrn());

        if (getCurrentRdsVersion(nameOrCrn).equals(targetMajorVersion.getVersion())) {
            return alreadyOnLatestAnswer(targetMajorVersion);
        } else {
            return checkStackStatusAndTrigger(stack, targetMajorVersion);
        }
    }

    private String getCurrentRdsVersion(NameOrCrn nameOrCrn) {
        StackDatabaseServerResponse databaseServer = databaseService.getDatabaseServer(nameOrCrn);
        return Optional.ofNullable(databaseServer)
                .map(StackDatabaseServerResponse::getMajorVersion)
                .map(MajorVersion::getVersion)
                .orElse(MajorVersion.VERSION_10.getVersion());
    }

    private RdsUpgradeV4Response alreadyOnLatestAnswer(TargetMajorVersion targetMajorVersion) {
        LOGGER.info("External database is already on version {}", targetMajorVersion);
        return new RdsUpgradeV4Response(RdsUpgradeResponseType.SKIP, FlowIdentifier.notTriggered(),
                getMessage(CLUSTER_RDS_UPGRADE_ALREADY_UPGRADED, List.of(targetMajorVersion.getVersion())), targetMajorVersion);
    }

    private RdsUpgradeV4Response checkStackStatusAndTrigger(StackView stack, TargetMajorVersion targetMajorVersion) {
        if (!stack.getStatus().isAvailable() && Status.EXTERNAL_DATABASE_UPGRADE_FAILED != stack.getStatus()) {
            LOGGER.info("Stack {} is not available for RDS upgrade", stack.getName());
            return new RdsUpgradeV4Response(RdsUpgradeResponseType.ERROR, FlowIdentifier.notTriggered(),
                    getMessage(CLUSTER_RDS_UPGRADE_NOT_AVAILABLE), targetMajorVersion);
        }

        LOGGER.info("External database for stack {} will be upgraded to version {}", stack.getName(), targetMajorVersion.getVersion());
        return triggerRdsUpgradeFlow(stack, targetMajorVersion);
    }

    private RdsUpgradeV4Response triggerRdsUpgradeFlow(StackView stack, TargetMajorVersion targetMajorVersion) {
        FlowIdentifier triggeredFlowId = reactorFlowManager.triggerRdsUpgrade(stack.getId(), targetMajorVersion);
        return new RdsUpgradeV4Response(RdsUpgradeResponseType.TRIGGERED, triggeredFlowId, null, targetMajorVersion);
    }

    private String getMessage(ResourceEvent resourceEvent) {
        return messagesService.getMessage(resourceEvent.getMessage());
    }

    private String getMessage(ResourceEvent resourceEvent, List<String> args) {
        return messagesService.getMessage(resourceEvent.getMessage(), args);
    }
}
