package com.sequenceiq.it.cloudbreak.dto;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sequenceiq.cloudbreak.api.endpoint.v4.common.Status;
import com.sequenceiq.cloudbreak.common.mappable.CloudPlatform;
import com.sequenceiq.it.cloudbreak.assign.Assignable;
import com.sequenceiq.it.cloudbreak.cloud.HostGroupType;
import com.sequenceiq.it.cloudbreak.context.Orderable;
import com.sequenceiq.it.cloudbreak.context.RunningParameter;
import com.sequenceiq.it.cloudbreak.context.TestContext;

public interface CloudbreakTestDto extends Orderable, Assignable {

    Logger LOGGER = LoggerFactory.getLogger(CloudbreakTestDto.class);

    String getLastKnownFlowChainId();

    String getLastKnownFlowId();

    void setLastKnownFlowId(String lastKnownFlowId);

    void setPrivateIps(TestContext testContext);

    Map<HostGroupType, String> getPrivateIps();

    CloudbreakTestDto valid();

    String getName();

    String getResourceNameType();

    CloudPlatform getCloudPlatform();

    default void cleanUp() {
        try {
            deleteForCleanup();
        } catch (Exception ignore) {
            LOGGER.error(String.format("%s cleanup failed because: ", getClass().getSimpleName()), ignore);
        }
    }

    default void deleteForCleanup() {
        LOGGER.warn("cleanUp logic is not implemented for TestDto: {}", getClass().getSimpleName());
    }

    default CloudbreakTestDto refresh() {
        LOGGER.warn("It is not refreshable resource: {}", getName());
        return this;
    }

    default CloudbreakTestDto wait(Map<String, Status> desiredStatus, RunningParameter runningParameter) {
        LOGGER.warn("Did not wait: {}", getName());
        return this;
    }

    default void setCloudPlatform(CloudPlatform cloudPlatform) {
        LOGGER.warn("Did not set up cloud platform ({}): name={}", getClass().getSimpleName(), getName());
    }

    default String getCrn() {
        throw new IllegalStateException(String.format("Error happened, getCrn() is not implemented for TestDto: %s", this));
    }

    default <E extends Enum<E>> String getAwaitExceptionKey(E desiredStatus) {
        return getAwaitExceptionKey(Map.of("status", desiredStatus));
    }

    default <E extends Enum<E>> String getAwaitExceptionKey(Map<String, E> desiredStatuses) {
        return String.format("await %s for desired statuses %s", this, desiredStatuses);
    }

}
