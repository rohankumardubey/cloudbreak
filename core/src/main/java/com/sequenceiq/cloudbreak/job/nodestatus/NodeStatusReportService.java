package com.sequenceiq.cloudbreak.job.nodestatus;

import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import com.google.common.base.Joiner;
import com.sequenceiq.cloudbreak.api.endpoint.v4.stacks.response.StackV4Response;
import com.sequenceiq.cloudbreak.auth.ThreadBasedUserCrnProvider;
import com.sequenceiq.cloudbreak.converter.v4.stacks.StackToStackV4ResponseConverter;
import com.sequenceiq.cloudbreak.dto.StackDto;
import com.sequenceiq.cloudbreak.event.ResourceEvent;
import com.sequenceiq.node.health.client.model.CdpNodeStatuses;
import com.sequenceiq.notification.NotificationService;

public abstract class NodeStatusReportService {

    @Inject
    private NotificationService notificationService;

    @Inject
    private StackToStackV4ResponseConverter stackToStackV4ResponseConverter;

    public void sendNotificationIfNeeded(StackDto stack, CdpNodeStatuses cdpNodeStatuses) {
        Set<String> getAffectedNodes = getAffectedNodes(cdpNodeStatuses);
        if (!getAffectedNodes.isEmpty()) {
            StackV4Response stackResponse = stackToStackV4ResponseConverter.convert(stack);
            notificationService.send(getReportEvent(), List.of(Joiner.on(", ").join(getAffectedNodes)),
                    stackResponse, ThreadBasedUserCrnProvider.getUserCrn());
        }
    }

    public abstract ResourceEvent getReportEvent();

    public abstract Set<String> getAffectedNodes(CdpNodeStatuses cdpNodeStatuses);
}
