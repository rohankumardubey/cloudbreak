package com.sequenceiq.cloudbreak.reactor.api.event.cluster;

import com.sequenceiq.cloudbreak.reactor.api.ClusterPlatformRequest;

public class ClusterServicesRestartRequest extends ClusterPlatformRequest {
    public ClusterServicesRestartRequest(Long stackId) {
        super(stackId);
    }
}
