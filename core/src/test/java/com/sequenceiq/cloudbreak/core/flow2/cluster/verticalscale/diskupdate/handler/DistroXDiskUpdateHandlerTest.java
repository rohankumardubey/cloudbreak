package com.sequenceiq.cloudbreak.core.flow2.cluster.verticalscale.diskupdate.handler;

import static com.sequenceiq.cloudbreak.core.flow2.cluster.verticalscale.diskupdate.DistroXDiskUpdateStateSelectors.DATAHUB_DISK_UPDATE_EVENT;
import static com.sequenceiq.cloudbreak.core.flow2.cluster.verticalscale.diskupdate.DistroXDiskUpdateStateSelectors.DATAHUB_DISK_UPDATE_FINISH_EVENT;
import static com.sequenceiq.cloudbreak.core.flow2.cluster.verticalscale.diskupdate.DistroXDiskUpdateStateSelectors.FAILED_DATAHUB_DISK_UPDATE_EVENT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.sequenceiq.cloudbreak.api.endpoint.v4.stacks.request.DiskUpdateRequest;
import com.sequenceiq.cloudbreak.cloud.model.Volume;
import com.sequenceiq.cloudbreak.core.flow2.cluster.verticalscale.diskupdate.event.DistroXDiskUpdateEvent;
import com.sequenceiq.cloudbreak.core.flow2.cluster.verticalscale.diskupdate.event.DistroXDiskUpdateFailedEvent;
import com.sequenceiq.cloudbreak.eventbus.Event;
import com.sequenceiq.cloudbreak.service.CloudbreakException;
import com.sequenceiq.cloudbreak.service.datalake.DiskUpdateService;
import com.sequenceiq.flow.reactor.api.event.BaseFlowEvent;
import com.sequenceiq.flow.reactor.api.event.EventSender;

@ExtendWith(MockitoExtension.class)
class DistroXDiskUpdateHandlerTest {

    private static final String TEST_CLUSTER = "TEST_CLUSTER";

    private static final String ACCOUNT_ID = "ACCOUNT_ID";

    @Mock
    private DiskUpdateService diskUpdateService;

    private DistroXDiskUpdateHandler underTest;

    @Mock
    private EventSender eventSender;

    @Captor
    private ArgumentCaptor<BaseFlowEvent> captor;

    @Captor
    private ArgumentCaptor<DistroXDiskUpdateFailedEvent> failureCaptor;

    @BeforeEach
    void setUp() {
        underTest = new DistroXDiskUpdateHandler(eventSender);
        ReflectionTestUtils.setField(underTest, null, diskUpdateService, DiskUpdateService.class);
    }

    @Test
    void testDiskUpdateAction() throws Exception {
        String selector = DATAHUB_DISK_UPDATE_EVENT.event();
        DiskUpdateRequest diskUpdateRequest = new DiskUpdateRequest();
        diskUpdateRequest.setGroup("compute");
        diskUpdateRequest.setSize(100);
        diskUpdateRequest.setVolumeType("gp2");
        DistroXDiskUpdateEvent event = DistroXDiskUpdateEvent.builder()
                .withClusterName(TEST_CLUSTER)
                .withAccountId(ACCOUNT_ID)
                .withDiskUpdateRequest(diskUpdateRequest)
                .withSelector(selector)
                .withVolumesToBeUpdated(List.of(mock(Volume.class)))
                .withCloudPlatform("AWS")
                .withStackId(1L)
                .build();
        underTest.accept(new Event<>(event));
        verify(diskUpdateService, times(1)).updateDiskTypeAndSize(eq(diskUpdateRequest), eq(event.getVolumesToBeUpdated()),
                eq(1L));
        verify(eventSender, times(1)).sendEvent(captor.capture(), any());
        assertEquals(DATAHUB_DISK_UPDATE_FINISH_EVENT.selector(), captor.getValue().getSelector());
    }

    @Test
    void testDiskUpdateFailureAction() throws Exception {
        String selector = DATAHUB_DISK_UPDATE_EVENT.event();
        DiskUpdateRequest diskUpdateRequest = new DiskUpdateRequest();
        diskUpdateRequest.setGroup("compute");
        diskUpdateRequest.setSize(100);
        diskUpdateRequest.setVolumeType("gp2");
        DistroXDiskUpdateEvent event = DistroXDiskUpdateEvent.builder()
                .withClusterName(TEST_CLUSTER)
                .withAccountId(ACCOUNT_ID)
                .withDiskUpdateRequest(diskUpdateRequest)
                .withSelector(selector)
                .withVolumesToBeUpdated(List.of(mock(Volume.class)))
                .withCloudPlatform("AWS")
                .build();
        doThrow(new CloudbreakException("Test")).when(diskUpdateService).updateDiskTypeAndSize(eq(diskUpdateRequest), eq(event.getVolumesToBeUpdated()),
                eq(1L));
        underTest.accept(new Event<>(event));
        verify(eventSender, times(1)).sendEvent(failureCaptor.capture(), any());
        assertEquals(FAILED_DATAHUB_DISK_UPDATE_EVENT.selector(), failureCaptor.getValue().getSelector());
    }
}
