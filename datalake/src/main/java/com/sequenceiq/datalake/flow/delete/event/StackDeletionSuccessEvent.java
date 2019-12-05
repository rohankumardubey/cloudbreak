package com.sequenceiq.datalake.flow.delete.event;

import com.sequenceiq.datalake.flow.SdxEvent;

public class StackDeletionSuccessEvent extends SdxEvent {

    private final boolean forced;

    public StackDeletionSuccessEvent(Long sdxId, String userId, boolean forced) {
        super(sdxId, userId);
        this.forced = forced;
    }

    @Override
    public String selector() {
        return "StackDeletionSuccessEvent";
    }

    public boolean isForced() {
        return forced;
    }
}

