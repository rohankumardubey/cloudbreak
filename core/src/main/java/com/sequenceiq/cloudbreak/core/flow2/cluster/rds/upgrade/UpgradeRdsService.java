package com.sequenceiq.cloudbreak.core.flow2.cluster.rds.upgrade;

import static com.sequenceiq.cloudbreak.api.endpoint.v4.common.Status.AVAILABLE;
import static com.sequenceiq.cloudbreak.api.endpoint.v4.common.Status.UPDATE_FAILED;
import static com.sequenceiq.cloudbreak.api.endpoint.v4.common.Status.UPDATE_IN_PROGRESS;

import java.util.Objects;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sequenceiq.cloudbreak.api.endpoint.v4.common.DetailedStackStatus;
import com.sequenceiq.cloudbreak.cloud.store.InMemoryStateStore;
import com.sequenceiq.cloudbreak.core.flow2.stack.CloudbreakFlowMessageService;
import com.sequenceiq.cloudbreak.event.ResourceEvent;
import com.sequenceiq.cloudbreak.message.CloudbreakMessagesService;
import com.sequenceiq.cloudbreak.orchestrator.exception.CloudbreakOrchestratorException;
import com.sequenceiq.cloudbreak.service.StackUpdater;
import com.sequenceiq.cloudbreak.service.upgrade.rds.RdsUpgradeOrchestratorService;

@Service
public class UpgradeRdsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpgradeRdsService.class);

    @Inject
    private StackUpdater stackUpdater;

    @Inject
    private CloudbreakFlowMessageService flowMessageService;

    @Inject
    private RdsUpgradeOrchestratorService rdsUpgradeOrchestratorService;

    @Inject
    private CloudbreakMessagesService messagesService;

    void stopServicesState(Long stackId) {
        setStatusAndNotify(stackId, getMessage(ResourceEvent.CLUSTER_RDS_UPGRADE_STOP_SERVICES), ResourceEvent.CLUSTER_RDS_UPGRADE_STOP_SERVICES);
    }

    void backupRdsState(Long stackId) {
        setStatusAndNotify(stackId, getMessage(ResourceEvent.CLUSTER_RDS_UPGRADE_BACKUP_DATA), ResourceEvent.CLUSTER_RDS_UPGRADE_BACKUP_DATA);
    }

    void upgradeRdsState(Long stackId) {
        setStatusAndNotify(stackId, getMessage(ResourceEvent.CLUSTER_RDS_UPGRADE_DBSERVER_UPGRADE), ResourceEvent.CLUSTER_RDS_UPGRADE_DBSERVER_UPGRADE);
    }

    void restoreRdsState(Long stackId) {
        setStatusAndNotify(stackId, getMessage(ResourceEvent.CLUSTER_RDS_UPGRADE_RESTORE_DATA), ResourceEvent.CLUSTER_RDS_UPGRADE_RESTORE_DATA);
    }

    void startServicesState(Long stackId) {
        setStatusAndNotify(stackId, getMessage(ResourceEvent.CLUSTER_RDS_UPGRADE_START_SERVICES), ResourceEvent.CLUSTER_RDS_UPGRADE_START_SERVICES);
    }

    private void setStatusAndNotify(Long stackId, String statusReason, ResourceEvent resourceEvent) {
        LOGGER.debug(statusReason);
        stackUpdater.updateStackStatus(stackId, DetailedStackStatus.EXTERNAL_DATABASE_UPGRADE_IN_PROGRESS, statusReason);
        flowMessageService.fireEventAndLog(stackId, UPDATE_IN_PROGRESS.name(), resourceEvent);
    }

    public void backupRds(Long stackId) throws CloudbreakOrchestratorException {
        rdsUpgradeOrchestratorService.backupRdsData(stackId);
    }

    public void restoreRds(Long stackId) throws CloudbreakOrchestratorException {
        rdsUpgradeOrchestratorService.restoreRdsData(stackId);
    }

    public void rdsUpgradeFinished(Long stackId, Long clusterId) {
        String statusReason = "RDS upgrade finished";
        LOGGER.debug(statusReason);
        InMemoryStateStore.deleteStack(stackId);
        InMemoryStateStore.deleteCluster(clusterId);
        stackUpdater.updateStackStatus(stackId, DetailedStackStatus.AVAILABLE, statusReason);
        flowMessageService.fireEventAndLog(stackId, AVAILABLE.name(), ResourceEvent.CLUSTER_RDS_UPGRADE_FINISHED);
    }

    public void rdsUpgradeFailed(Long stackId, Long clusterId, Exception exception) {
        String statusReason = "RDS upgrade failed with exception " + exception.getMessage();
        LOGGER.debug(statusReason);
        InMemoryStateStore.deleteStack(stackId);
        if (Objects.nonNull(clusterId)) {
            InMemoryStateStore.deleteCluster(clusterId);
        }
        stackUpdater.updateStackStatus(stackId, DetailedStackStatus.EXTERNAL_DATABASE_UPGRADE_FAILED, statusReason);
        flowMessageService.fireEventAndLog(stackId, UPDATE_FAILED.name(), ResourceEvent.CLUSTER_RDS_UPGRADE_FAILED);
    }

    private String getMessage(ResourceEvent resourceEvent) {
        return messagesService.getMessage(resourceEvent.getMessage());
    }
}