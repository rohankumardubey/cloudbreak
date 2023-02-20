package com.sequenceiq.datalake.flow.dr.restore.handler;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.dyngr.exception.PollerException;
import com.dyngr.exception.PollerStoppedException;
import com.dyngr.exception.UserBreakException;
import com.sequenceiq.cloudbreak.common.event.Selectable;
import com.sequenceiq.cloudbreak.eventbus.Event;
import com.sequenceiq.datalake.entity.SdxCluster;
import com.sequenceiq.datalake.entity.operation.SdxOperationStatus;
import com.sequenceiq.datalake.flow.dr.restore.event.DatalakeDatabaseRestoreFailedEvent;
import com.sequenceiq.datalake.flow.dr.restore.event.DatalakeDatabaseRestoreWaitRequest;
import com.sequenceiq.datalake.flow.dr.restore.event.DatalakeFullRestoreInProgressEvent;
import com.sequenceiq.datalake.repository.SdxClusterRepository;
import com.sequenceiq.datalake.service.sdx.PollingConfig;
import com.sequenceiq.datalake.service.sdx.dr.SdxBackupRestoreService;
import com.sequenceiq.datalake.service.sdx.flowcheck.CloudbreakFlowService;
import com.sequenceiq.flow.api.model.FlowCheckResponse;
import com.sequenceiq.flow.event.EventSelectorUtil;
import com.sequenceiq.flow.reactor.api.handler.ExceptionCatcherEventHandler;
import com.sequenceiq.flow.reactor.api.handler.HandlerEvent;

import static com.sequenceiq.cloudbreak.common.exception.NotFoundException.notFound;

@Component
public class DatalakeDatabaseRestoreWaitHandler extends ExceptionCatcherEventHandler<DatalakeDatabaseRestoreWaitRequest> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatalakeDatabaseRestoreWaitHandler.class);

    @Value("${sdx.stack.restore.status.sleeptime_sec:20}")
    private int sleepTimeInSec;

    @Value("${sdx.stack.restore.status.duration_min:90}")
    private int durationInMinutes;

    @Inject
    private SdxBackupRestoreService sdxBackupRestoreService;

    @Inject
    private SdxClusterRepository sdxClusterRepository;

    @Inject
    private CloudbreakFlowService cloudbreakFlowService;

    @Override
    public String selector() {
        return EventSelectorUtil.selector(DatalakeDatabaseRestoreWaitRequest.class);
    }

    @Override
    protected Selectable defaultFailureEvent(Long resourceId, Exception e, Event<DatalakeDatabaseRestoreWaitRequest> event) {
        return new DatalakeDatabaseRestoreFailedEvent(resourceId, null, e);
    }

    @Override
    protected Selectable doAccept(HandlerEvent<DatalakeDatabaseRestoreWaitRequest> event) {
        DatalakeDatabaseRestoreWaitRequest request = event.getData();
        Long sdxId = request.getResourceId();
        String userId = request.getUserId();
        Selectable response;
        try {
            LOGGER.info("Start polling datalake database restore for id: {}", sdxId);
            PollingConfig pollingConfig = new PollingConfig(sleepTimeInSec, TimeUnit.SECONDS, durationInMinutes, TimeUnit.MINUTES);
            sdxBackupRestoreService.waitCloudbreakFlow(sdxId, pollingConfig, "Database restore");
            response = new DatalakeFullRestoreInProgressEvent(sdxId, userId, request.getOperationId());
        } catch (UserBreakException userBreakException) {
            LOGGER.info("Database restore polling exited before timeout. Cause: ", userBreakException);
            sdxBackupRestoreService.updateDatabaseStatusEntry(event.getData().getOperationId(),
                    SdxOperationStatus.FAILED, userBreakException.getLocalizedMessage());
            response = new DatalakeDatabaseRestoreFailedEvent(sdxId, userId, userBreakException);
        } catch (PollerStoppedException pollerStoppedException) {
            LOGGER.info("Database restore poller stopped for cluster: {}", sdxId);
            sdxBackupRestoreService.updateDatabaseStatusEntry(event.getData().getOperationId(),
                    SdxOperationStatus.FAILED, pollerStoppedException.getLocalizedMessage());
            // Get at which step the timeout happens.
            SdxCluster sdxCluster = sdxClusterRepository.findById(sdxId).orElseThrow(notFound("SDX cluster", sdxId));
            FlowCheckResponse lastKnownFlowCheckResponse = cloudbreakFlowService.getLastKnownFlowCheckResponse(sdxCluster);
            String extraInfo = "";
            if (lastKnownFlowCheckResponse != null && lastKnownFlowCheckResponse.getCurrentState() != null) {
                String state = String.format(" during the transition from %s to its next state", lastKnownFlowCheckResponse.getCurrentState());
                String flowType = lastKnownFlowCheckResponse.getFlowType() != null ?
                        String.format(", set up in %s", lastKnownFlowCheckResponse.getFlowType()) : "";
                String triggeredEvent = lastKnownFlowCheckResponse.getNextEvent() != null ?
                        String.format(", triggered by %s", lastKnownFlowCheckResponse.getNextEvent()) : "";
                extraInfo = String.format("%s%s%s", state, flowType, triggeredEvent);
            }
            response = new DatalakeDatabaseRestoreFailedEvent(sdxId, userId,
                    new PollerStoppedException("Database restore timed out after " + durationInMinutes + " minutes" + extraInfo));
        } catch (PollerException exception) {
            LOGGER.info("Database restore polling failed for cluster: {}", sdxId);
            sdxBackupRestoreService.updateDatabaseStatusEntry(event.getData().getOperationId(),
                    SdxOperationStatus.FAILED, exception.getLocalizedMessage());
            response = new DatalakeDatabaseRestoreFailedEvent(sdxId, userId, exception);
        }
        return response;
    }
}
