package com.sequenceiq.freeipa.flow.freeipa.upscale.event;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sequenceiq.freeipa.flow.stack.StackEvent;

public class UpscaleCreateUserdataSecretsSuccess extends StackEvent {

    private final List<Long> createdSecretResourceIds;

    @JsonCreator
    public UpscaleCreateUserdataSecretsSuccess(
            @JsonProperty("resourceId") Long stackId,
            @JsonProperty("createdSecretResourceIds") List<Long> createdSecretResourceIds) {
        super(stackId);
        this.createdSecretResourceIds = createdSecretResourceIds;
    }

    public List<Long> getCreatedSecretResourceIds() {
        return createdSecretResourceIds;
    }

    @Override
    public String toString() {
        return "UpscaleCreateUserdataSecretsSuccess{" +
                "createdSecretResourceIds=" + createdSecretResourceIds +
                "} " + super.toString();
    }
}
