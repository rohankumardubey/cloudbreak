package com.sequenceiq.cloudbreak.quartz.metric.statusmetric;

import com.sequenceiq.cloudbreak.common.mappable.CloudPlatform;

public record StackCountByStatusAndCloudPlatform(StackCountByStatusView stackCountByStatusView, CloudPlatform cloudPlatform) {

    public String status() {
        return stackCountByStatusView.getStatus();
    }

    public int count() {
        return stackCountByStatusView.getCount();
    }
}
