package com.sequenceiq.externalizedcompute.flow.create;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sequenceiq.externalizedcompute.flow.ExternalizedComputeClusterContext;
import com.sequenceiq.externalizedcompute.flow.ExternalizedComputeClusterEvent;
import com.sequenceiq.flow.event.EventSelectorUtil;

public class ExternalizedComputeClusterCreateWaitSuccessResponse extends ExternalizedComputeClusterEvent {

    @JsonCreator
    public ExternalizedComputeClusterCreateWaitSuccessResponse(
            @JsonProperty("resourceId") Long externalizedComputeClusterId,
            @JsonProperty("userId") String userId) {
        super(externalizedComputeClusterId, userId);
    }

    public ExternalizedComputeClusterCreateWaitSuccessResponse(ExternalizedComputeClusterContext context) {
        super(context);
    }

    @Override
    public String selector() {
        return EventSelectorUtil.selector(ExternalizedComputeClusterCreateWaitSuccessResponse.class);
    }

}
