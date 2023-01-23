package com.sequenceiq.cloudbreak.job.nodestatus;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import com.cloudera.thunderhead.telemetry.nodestatus.NodeStatusProto;
import com.google.api.client.util.Sets;
import com.sequenceiq.cloudbreak.client.RPCResponse;
import com.sequenceiq.cloudbreak.event.ResourceEvent;
import com.sequenceiq.node.health.client.model.CdpNodeStatuses;

@Service
public class HighMemoryUsageReportService extends NodeStatusReportService {
    @Override
    public ResourceEvent getReportEvent() {
        return ResourceEvent.NODE_STATUS_HIGH_MEMORY_USAGE;
    }

    @Override
    public Set<String> getAffectedNodes(CdpNodeStatuses cdpNodeStatuses) {
        Set<String> affectedNodes = Sets.newHashSet();
        Optional<RPCResponse<NodeStatusProto.NodeStatusReport>> systemMetricsReport = cdpNodeStatuses.getSystemMetricsReport();
        if (systemMetricsReport.isPresent()) {
            NodeStatusProto.NodeStatusReport nodeStatusReport = systemMetricsReport.get().getResult();
            if (nodeStatusReport != null) {
                affectedNodes = CollectionUtils.emptyIfNull(nodeStatusReport.getNodesList()).stream()
                        .filter(NodeStatusProto.NodeStatus::hasSystemMetrics)
                        .filter(nodeStatus -> nodeStatus.getSystemMetrics().hasMemory())
                        .filter(nodeStatus -> nodeStatus.getSystemMetrics().getMemory().hasVirtualMemory())
                        .filter(nodeStatus -> nodeStatus.getSystemMetrics().getMemory().getVirtualMemory().getPercent() > 90)
                        .map(nodeStatus -> nodeStatus.getStatusDetails().getHost())
                        .collect(Collectors.toSet());
            }
        }
        return affectedNodes;
    }
}
