package com.sequenceiq.cloudbreak.service.cluster.flow.telemetry;

import static com.sequenceiq.cloudbreak.core.bootstrap.service.ClusterDeletionBasedExitCriteriaModel.clusterDeletionBasedModel;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.sequenceiq.cloudbreak.common.orchestration.Node;
import com.sequenceiq.cloudbreak.domain.stack.Stack;
import com.sequenceiq.cloudbreak.domain.stack.instance.InstanceMetaData;
import com.sequenceiq.cloudbreak.orchestrator.exception.CloudbreakOrchestratorFailedException;
import com.sequenceiq.cloudbreak.orchestrator.host.TelemetryOrchestrator;
import com.sequenceiq.cloudbreak.orchestrator.model.GatewayConfig;
import com.sequenceiq.cloudbreak.service.CloudbreakException;
import com.sequenceiq.cloudbreak.service.GatewayConfigService;
import com.sequenceiq.common.api.telemetry.model.Telemetry;

@Component
public class ClusterMonitoringEngine {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClusterMonitoringEngine.class);

    private final TelemetryOrchestrator telemetryOrchestrator;

    private final GatewayConfigService gatewayConfigService;

    public ClusterMonitoringEngine(TelemetryOrchestrator telemetryOrchestrator, GatewayConfigService gatewayConfigService) {
        this.telemetryOrchestrator = telemetryOrchestrator;
        this.gatewayConfigService = gatewayConfigService;
    }

    public void installAndStartMonitoring(Stack stack, Telemetry telemetry) throws CloudbreakException {
        if (telemetry != null && telemetry.isMonitoringFeatureEnabled()) {
            try {
                LOGGER.info("Install and start monitoring for CM server.");
                Set<InstanceMetaData> instanceMetaDataSet = stack.getNotDeletedAndNotZombieInstanceMetaDataSet();
                List<GatewayConfig> gatewayConfigs = gatewayConfigService.getAllGatewayConfigs(stack);
                Set<Node> allNodes = instanceMetaDataSet.stream()
                        .map(im -> new Node(im.getPrivateIp(), im.getPublicIp(), im.getInstanceId(),
                                im.getInstanceGroup().getTemplate().getInstanceType(), im.getDiscoveryFQDN(), im.getInstanceGroup().getGroupName()))
                        .collect(Collectors.toSet());
                telemetryOrchestrator.installAndStartMonitoring(gatewayConfigs, allNodes, clusterDeletionBasedModel(stack.getId(), stack.getCluster().getId()));
            } catch (CloudbreakOrchestratorFailedException ex) {
                throw new CloudbreakException(ex);
            }
        }
    }
}
