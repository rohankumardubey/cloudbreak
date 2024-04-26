package com.sequenceiq.datalake.flow.chain;


import static com.sequenceiq.datalake.flow.dr.validation.DatalakeBackupValidationEvent.DATALAKE_TRIGGER_BACKUP_VALIDATION_EVENT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sequenceiq.cloudbreak.common.event.Selectable;
import com.sequenceiq.cloudbreak.datalakedr.DatalakeDrSkipOptions;
import com.sequenceiq.datalake.entity.SdxCluster;
import com.sequenceiq.datalake.entity.SdxDatabase;
import com.sequenceiq.datalake.flow.detach.event.DatalakeResizeFlowChainStartEvent;
import com.sequenceiq.datalake.flow.dr.validation.event.DatalakeTriggerBackupValidationEvent;
import com.sequenceiq.flow.core.chain.config.FlowTriggerEventQueue;
import com.sequenceiq.sdx.api.model.SdxClusterShape;

@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
public class DatalakeResizeFlowEventChainTest {

    private static final String USER_CRN = "crn:cdp:iam:us-west-1:1234:user:1";

    private static final String BACKUP_LOCATION = "s3a://path/to/backup";

    @InjectMocks
    private DatalakeResizeFlowEventChainFactory factory;

    private SdxCluster sdxCluster;

    @Before
    public void setUp() {
        sdxCluster = getValidSdxCluster();
    }

    @Test
    public void chainCreationTest() {
        DatalakeResizeFlowChainStartEvent event = new DatalakeResizeFlowChainStartEvent(sdxCluster.getId(), sdxCluster, USER_CRN, BACKUP_LOCATION,
                true, true, new DatalakeDrSkipOptions(false, false, false, false), false);
        FlowTriggerEventQueue flowTriggerEventQueue = factory.createFlowTriggerEventQueue(event);
        assertEquals(9, flowTriggerEventQueue.getQueue().size());
        assertTriggerBackupEvent(flowTriggerEventQueue);
    }

    @Test
    public void chainCreationWithRazTest() {
        SdxCluster clusterWithRaz = getValidSdxClusterwithRaz();
        DatalakeResizeFlowChainStartEvent event = new DatalakeResizeFlowChainStartEvent(clusterWithRaz.getId(), clusterWithRaz, USER_CRN, BACKUP_LOCATION,
                true, true, new DatalakeDrSkipOptions(false, false, false, false), false);
        FlowTriggerEventQueue flowTriggerEventQueue = factory.createFlowTriggerEventQueue(event);
        assertEquals(9, flowTriggerEventQueue.getQueue().size());
        assertTriggerBackupEvent(flowTriggerEventQueue);
    }

    private void assertTriggerBackupEvent(FlowTriggerEventQueue flowChainQueue) {
        Selectable triggerBackupEvent = flowChainQueue.getQueue().remove();
        assertEquals(DATALAKE_TRIGGER_BACKUP_VALIDATION_EVENT.selector(), triggerBackupEvent.selector());
        assertEquals(sdxCluster.getId(), triggerBackupEvent.getResourceId());
        assertTrue(triggerBackupEvent instanceof DatalakeTriggerBackupValidationEvent);
        DatalakeTriggerBackupValidationEvent event = (DatalakeTriggerBackupValidationEvent) triggerBackupEvent;
        assertEquals(BACKUP_LOCATION, event.getBackupLocation());

    }

    private SdxCluster getValidSdxCluster() {
        sdxCluster = new SdxCluster();
        sdxCluster.setClusterName("test-sdx-cluster");
        sdxCluster.setClusterShape(SdxClusterShape.LIGHT_DUTY);
        sdxCluster.setEnvName("test-env");
        sdxCluster.setCrn("crn:sdxcluster");
        sdxCluster.setId(1L);
        SdxDatabase sdxDatabase = new SdxDatabase();
        sdxDatabase.setDatabaseCrn("crn:sdxcluster");
        sdxCluster.setSdxDatabase(sdxDatabase);
        return sdxCluster;
    }

    private SdxCluster getValidSdxClusterwithRaz() {
        sdxCluster = new SdxCluster();
        sdxCluster.setClusterName("test-sdx-cluster");
        sdxCluster.setClusterShape(SdxClusterShape.LIGHT_DUTY);
        sdxCluster.setEnvName("test-env");
        sdxCluster.setCrn("crn:sdxcluster");
        sdxCluster.setRangerRazEnabled(true);
        sdxCluster.setId(1L);
        SdxDatabase sdxDatabase = new SdxDatabase();
        sdxDatabase.setDatabaseCrn("crn:sdxcluster");
        sdxCluster.setSdxDatabase(sdxDatabase);
        return sdxCluster;
    }
}
