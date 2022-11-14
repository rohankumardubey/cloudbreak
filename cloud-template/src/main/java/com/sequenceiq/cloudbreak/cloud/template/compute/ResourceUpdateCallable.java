package com.sequenceiq.cloudbreak.cloud.template.compute;

import static com.sequenceiq.cloudbreak.cloud.scheduler.PollGroup.CANCELLED;
import static java.lang.String.format;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.sequenceiq.cloudbreak.cloud.context.AuthenticatedContext;
import com.sequenceiq.cloudbreak.cloud.model.CloudInstance;
import com.sequenceiq.cloudbreak.cloud.model.CloudResource;
import com.sequenceiq.cloudbreak.cloud.model.CloudResourceStatus;
import com.sequenceiq.cloudbreak.cloud.model.CloudStack;
import com.sequenceiq.cloudbreak.cloud.model.Group;
import com.sequenceiq.cloudbreak.cloud.model.ResourceStatus;
import com.sequenceiq.cloudbreak.cloud.model.Variant;
import com.sequenceiq.cloudbreak.cloud.notification.PersistenceNotifier;
import com.sequenceiq.cloudbreak.cloud.scheduler.CancellationException;
import com.sequenceiq.cloudbreak.cloud.scheduler.PollGroup;
import com.sequenceiq.cloudbreak.cloud.scheduler.SyncPollingScheduler;
import com.sequenceiq.cloudbreak.cloud.store.InMemoryStateStore;
import com.sequenceiq.cloudbreak.cloud.task.PollTask;
import com.sequenceiq.cloudbreak.cloud.template.ComputeResourceBuilder;
import com.sequenceiq.cloudbreak.cloud.template.context.ResourceBuilderContext;
import com.sequenceiq.cloudbreak.cloud.template.init.ResourceBuilders;
import com.sequenceiq.cloudbreak.cloud.template.task.ResourcePollTaskFactory;
import com.sequenceiq.common.api.type.ResourceType;

public class ResourceUpdateCallable implements Callable<ResourceRequestResult<List<CloudResourceStatus>>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceUpdateCallable.class);

    private static final List<ResourceType> VOLUME_SET_RESOURCE_TYPES = List.of(ResourceType.AWS_VOLUMESET, ResourceType.AZURE_VOLUMESET);

    // half an hour with 1 sec delays
    private static final int VOLUME_SET_POLLING_ATTEMPTS = 1800;

    private final SyncPollingScheduler<List<CloudResourceStatus>> syncPollingScheduler;

    private final ResourcePollTaskFactory resourcePollTaskFactory;

    private final PersistenceNotifier persistenceNotifier;

    private final ResourceBuilderContext context;

    private final AuthenticatedContext auth;

    private final CloudResource resource;

    private final CloudInstance instance;

    private final CloudStack stack;

    private final ComputeResourceBuilder<ResourceBuilderContext> builder;

    public ResourceUpdateCallable(ResourceUpdateCallablePayload payload, SyncPollingScheduler<List<CloudResourceStatus>> syncPollingScheduler,
        ResourcePollTaskFactory resourcePollTaskFactory, PersistenceNotifier persistenceNotifier) {
        this.syncPollingScheduler = syncPollingScheduler;
        this.resourcePollTaskFactory = resourcePollTaskFactory;
        this.persistenceNotifier = persistenceNotifier;
        this.context = payload.getContext();
        this.auth = payload.getAuth();
        this.resource = payload.getCloudResource();
        this.instance = payload.getCloudInstance();
        this.stack = payload.getCloudStack();
        this.builder = payload.getBuilder();
    }

    @Override
    public ResourceRequestResult<List<CloudResourceStatus>> call() throws Exception {
        LOGGER.debug("Deleting compute resource {}", resource);
        if (resource.getStatus().resourceExists()) {
            CloudResource updateResource;
            try {
                updateResource = builder.update(context, resource, instance, auth, stack);
            } catch (PreserveResourceException ignored) {
                LOGGER.debug("Preserve resource for later use.");
                CloudResourceStatus status = new CloudResourceStatus(resource, ResourceStatus.CREATED);
                return new ResourceRequestResult<>(FutureResult.SUCCESS, Collections.singletonList(status));
            }
            if (updateResource != null) {
                PollTask<List<CloudResourceStatus>> task = resourcePollTaskFactory
                        .newPollResourceTask(builder, auth, Collections.singletonList(updateResource), context, false);
                List<CloudResourceStatus> pollerResult = syncPollingScheduler.schedule(task);
                return new ResourceRequestResult<>(FutureResult.SUCCESS, pollerResult);
            }
        }
        CloudResourceStatus status = new CloudResourceStatus(resource, ResourceStatus.DELETED);
        return new ResourceRequestResult<>(FutureResult.SUCCESS, Collections.singletonList(status));
    }

    private List<CloudResourceStatus> scheduleTask(PollTask<List<CloudResourceStatus>> task, List<CloudResource> resources) throws Exception {
        boolean volumeSet = resources != null && resources.stream().allMatch(r -> VOLUME_SET_RESOURCE_TYPES.contains(r.getType()));
        if (volumeSet) {
            LOGGER.debug("Scheduling volume set poller with shorter timeout.");
            return syncPollingScheduler.schedule(task, SyncPollingScheduler.POLLING_INTERVAL, VOLUME_SET_POLLING_ATTEMPTS,
                    SyncPollingScheduler.FAILURE_TOLERANT_ATTEMPT);
        } else {
            LOGGER.debug("Scheduling resource poller with default timeout.");
            return syncPollingScheduler.schedule(task);
        }
    }

    private void persistResources(AuthenticatedContext auth, Iterable<CloudResource> cloudResources) {
        for (CloudResource cloudResource : cloudResources) {
            if (cloudResource.isPersistent()) {
                persistenceNotifier.notifyAllocation(cloudResource, auth.getCloudContext());
            }
        }
    }

    private boolean isCancelled(PollGroup pollGroup) {
        return pollGroup == null || CANCELLED.equals(pollGroup);
    }

    private void updateResource(AuthenticatedContext auth, Iterable<CloudResource> cloudResources) {
        for (CloudResource cloudResource : cloudResources) {
            if (cloudResource.isPersistent()) {
                persistenceNotifier.notifyUpdate(cloudResource, auth.getCloudContext());
            }
        }
    }
}
