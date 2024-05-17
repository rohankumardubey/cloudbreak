package com.sequenceiq.externalizedcompute.flow.delete;

import static com.sequenceiq.externalizedcompute.entity.ExternalizedComputeClusterStatusEnum.DELETE_IN_PROGRESS;
import static com.sequenceiq.externalizedcompute.entity.ExternalizedComputeClusterStatusEnum.REINITIALIZE_IN_PROGRESS;
import static com.sequenceiq.externalizedcompute.flow.delete.ExternalizedComputeClusterDeleteFlowEvent.EXTERNALIZED_COMPUTE_CLUSTER_DELETE_FINALIZED_EVENT;
import static com.sequenceiq.externalizedcompute.flow.delete.ExternalizedComputeClusterDeleteFlowEvent.EXTERNALIZED_COMPUTE_CLUSTER_DELETE_STARTED_EVENT;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.statemachine.action.Action;
import org.springframework.test.util.ReflectionTestUtils;

import com.sequenceiq.cloudbreak.eventbus.Event;
import com.sequenceiq.cloudbreak.eventbus.EventBus;
import com.sequenceiq.externalizedcompute.flow.AbstractExternalizedComputeClusterAction;
import com.sequenceiq.externalizedcompute.flow.ExternalizedComputeClusterContext;
import com.sequenceiq.externalizedcompute.service.ExternalizedComputeClusterService;
import com.sequenceiq.externalizedcompute.service.ExternalizedComputeClusterStatusService;
import com.sequenceiq.flow.core.AbstractActionTestSupport;
import com.sequenceiq.flow.core.FlowParameters;
import com.sequenceiq.flow.core.FlowRegister;
import com.sequenceiq.flow.reactor.ErrorHandlerAwareReactorEventFactory;

@ExtendWith(MockitoExtension.class)
class ExternalizedComputeClusterDeleteActionsTest {

    public static final String USER_CRN = "userCrn";

    @InjectMocks
    private ExternalizedComputeClusterDeleteActions underTest;

    @Mock
    private ExternalizedComputeClusterStatusService externalizedComputeClusterStatusService;

    @Mock
    private ExternalizedComputeClusterService externalizedComputeClusterService;

    @Mock
    private FlowRegister runningFlows;

    @Mock
    private EventBus eventBus;

    @Mock
    private ErrorHandlerAwareReactorEventFactory reactorEventFactory;

    @Mock
    private FlowParameters flowParameters;

    private ExternalizedComputeClusterContext context;

    @BeforeEach
    void setUp() {
        context = new ExternalizedComputeClusterContext(flowParameters, 1L, USER_CRN);
    }

    @Test
    public void testExternalizedComputeClusterDeleteWithPreserve() throws Exception {
        AbstractExternalizedComputeClusterAction<ExternalizedComputeClusterDeleteEvent> externalizedComputeClusterDelete =
                (AbstractExternalizedComputeClusterAction<ExternalizedComputeClusterDeleteEvent>) underTest.externalizedComputeClusterDelete();
        initActionPrivateFields(externalizedComputeClusterDelete);
        ExternalizedComputeClusterDeleteEvent deleteEvent = new ExternalizedComputeClusterDeleteEvent(1L, USER_CRN, true, true);

        Event event = mock(Event.class);
        when(reactorEventFactory.createEvent(anyMap(), eq(deleteEvent))).thenReturn(event);
        new AbstractActionTestSupport<>(externalizedComputeClusterDelete).doExecute(context, deleteEvent, Map.of());

        verify(externalizedComputeClusterStatusService, times(1)).setStatus(1L, REINITIALIZE_IN_PROGRESS, "Cluster delete initiated");
        verify(externalizedComputeClusterService, times(1)).initiateDelete(1L);
        verify(eventBus).notify(eq(EXTERNALIZED_COMPUTE_CLUSTER_DELETE_STARTED_EVENT.selector()), eq(event));
    }

    @Test
    public void testExternalizedComputeClusterDeleteWithoutPreserve() throws Exception {
        AbstractExternalizedComputeClusterAction<ExternalizedComputeClusterDeleteEvent> externalizedComputeClusterDelete =
                (AbstractExternalizedComputeClusterAction<ExternalizedComputeClusterDeleteEvent>) underTest.externalizedComputeClusterDelete();
        initActionPrivateFields(externalizedComputeClusterDelete);
        ExternalizedComputeClusterDeleteEvent deleteEvent = new ExternalizedComputeClusterDeleteEvent(1L, USER_CRN, true, false);

        Event event = mock(Event.class);
        when(reactorEventFactory.createEvent(anyMap(), eq(deleteEvent))).thenReturn(event);
        new AbstractActionTestSupport<>(externalizedComputeClusterDelete).doExecute(context, deleteEvent, Map.of());

        verify(externalizedComputeClusterStatusService, times(1)).setStatus(1L, DELETE_IN_PROGRESS, "Cluster delete initiated");
        verify(externalizedComputeClusterService, times(1)).initiateDelete(1L);
        verify(eventBus).notify(eq(EXTERNALIZED_COMPUTE_CLUSTER_DELETE_STARTED_EVENT.selector()), eq(event));
    }

    @Test
    public void testExternalizedComputeClusterDeleteFinishedWithPreserve() throws Exception {
        AbstractExternalizedComputeClusterAction<ExternalizedComputeClusterDeleteEvent> externalizedComputeClusterDelete =
                (AbstractExternalizedComputeClusterAction<ExternalizedComputeClusterDeleteEvent>) underTest.externalizedComputeClusterDeleteFinished();
        initActionPrivateFields(externalizedComputeClusterDelete);
        ExternalizedComputeClusterDeleteEvent deleteEvent = new ExternalizedComputeClusterDeleteEvent(1L, USER_CRN, true, true);

        Event event = mock(Event.class);
        when(reactorEventFactory.createEvent(anyMap(), eq(deleteEvent))).thenReturn(event);
        new AbstractActionTestSupport<>(externalizedComputeClusterDelete).doExecute(context, deleteEvent, Map.of());

        verify(externalizedComputeClusterStatusService, times(1)).setStatus(1L, REINITIALIZE_IN_PROGRESS,
                "Cluster delete finished. Starting new cluster creation.");
        verify(externalizedComputeClusterService, times(1)).deleteLiftieClusterNameForCluster(1L);
        verify(eventBus).notify(eq(EXTERNALIZED_COMPUTE_CLUSTER_DELETE_FINALIZED_EVENT.selector()), eq(event));
    }

    @Test
    public void testExternalizedComputeClusterDeleteFinishedWithoutPreserve() throws Exception {
        AbstractExternalizedComputeClusterAction<ExternalizedComputeClusterDeleteEvent> externalizedComputeClusterDelete =
                (AbstractExternalizedComputeClusterAction<ExternalizedComputeClusterDeleteEvent>) underTest.externalizedComputeClusterDeleteFinished();
        initActionPrivateFields(externalizedComputeClusterDelete);
        ExternalizedComputeClusterDeleteEvent deleteEvent = new ExternalizedComputeClusterDeleteEvent(1L, USER_CRN, true, false);

        Event event = mock(Event.class);
        when(reactorEventFactory.createEvent(anyMap(), eq(deleteEvent))).thenReturn(event);
        new AbstractActionTestSupport<>(externalizedComputeClusterDelete).doExecute(context, deleteEvent, Map.of());

        verifyNoInteractions(externalizedComputeClusterStatusService);
        verify(externalizedComputeClusterService, times(1)).deleteExternalizedComputeCluster(1L);
        verify(eventBus).notify(eq(EXTERNALIZED_COMPUTE_CLUSTER_DELETE_FINALIZED_EVENT.selector()), eq(event));
    }

    private void initActionPrivateFields(Action<?, ?> action) {
        ReflectionTestUtils.setField(action, null, runningFlows, FlowRegister.class);
        ReflectionTestUtils.setField(action, null, eventBus, EventBus.class);
        ReflectionTestUtils.setField(action, null, reactorEventFactory, ErrorHandlerAwareReactorEventFactory.class);
    }

}