package com.sequenceiq.cloudbreak.core.cluster;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.sequenceiq.cloudbreak.cluster.api.ClusterApi;
import com.sequenceiq.cloudbreak.cluster.api.ClusterCommissionService;
import com.sequenceiq.cloudbreak.cluster.api.ClusterStatusService;
import com.sequenceiq.cloudbreak.cmtemplate.CmTemplateProcessor;
import com.sequenceiq.cloudbreak.core.flow2.cluster.rds.upgrade.RdsSettingsMigrationService;
import com.sequenceiq.cloudbreak.domain.RDSConfig;
import com.sequenceiq.cloudbreak.domain.stack.Stack;
import com.sequenceiq.cloudbreak.service.ClusterServicesRestartService;
import com.sequenceiq.cloudbreak.service.cluster.ClusterApiConnectors;
import com.sequenceiq.cloudbreak.service.sharedservice.DatalakeService;
import com.sequenceiq.cloudbreak.util.StackUtil;

@ExtendWith(MockitoExtension.class)
class ClusterStartHandlerServiceTest {
    @Mock
    private DatalakeService datalakeService;

    @Mock
    private StackUtil stackUtil;

    @Mock
    private ClusterApiConnectors apiConnectors;

    @Mock
    private ClusterApi connector;

    @Mock
    private ClusterStatusService clusterStatusService;

    @Mock
    private ClusterCommissionService clusterCommissionService;

    @Mock
    private ClusterServicesRestartService clusterServicesRestartService;

    @Mock
    private CmTemplateProcessor cmTemplateProcessor;

    @Mock
    private RdsSettingsMigrationService rdsSettingsMigrationService;

    @InjectMocks
    private ClusterStartHandlerService underTest;

    private Stack stack;

    @BeforeEach
    void setUp() {
        stack = new Stack();
        stack.setCloudPlatform("AWS");
        lenient().when(stackUtil.stopStartScalingEntitlementEnabled(any())).thenReturn(true);
        lenient().when(apiConnectors.getConnector(any(Stack.class))).thenReturn(connector);
        lenient().when(connector.clusterStatusService()).thenReturn(clusterStatusService);
        lenient().when(connector.clusterCommissionService()).thenReturn(clusterCommissionService);
        List<String> decommHosts = new ArrayList<>();
        decommHosts.add("computing-computing0.foo.bar");
        decommHosts.add("computing-compute0.foo.bar");
        decommHosts.add("computing-working0.foo.bar");
        lenient().when(clusterStatusService.getDecommissionedHostsFromCM()).thenReturn(decommHosts);
        Set<String> computeGroups = new HashSet<>();
        computeGroups.add("compute");
        computeGroups.add("computing");
        lenient().when(cmTemplateProcessor.getComputeHostGroups(any())).thenReturn(computeGroups);
    }

    @Test
    void testRefreshClusterOnStart() throws Exception {
        // GIVEN
        Optional<Stack> datalake = Optional.of(new Stack());
        when(datalakeService.getDatalakeStackByDatahubStack(stack)).thenReturn(datalake);
        when(clusterServicesRestartService.isRemoteDataContextRefreshNeeded(any(), any())).thenReturn(true);
        // WHEN
        underTest.startCluster(stack, cmTemplateProcessor, false);
        // THEN
        verify(clusterServicesRestartService).refreshClusterOnStart(stack, datalake.get(), cmTemplateProcessor);
    }

    @Test
    void testStartClusterWithSharedRdsConfigRefresh() throws Exception {
        // GIVEN
        Optional<Stack> datalake = Optional.of(new Stack());
        when(datalakeService.getDatalakeStackByDatahubStack(stack)).thenReturn(datalake);
        when(clusterServicesRestartService.isRemoteDataContextRefreshNeeded(any(), any())).thenReturn(false);
        Set<RDSConfig> rdsConfigs = Set.of(new RDSConfig());
        when(rdsSettingsMigrationService.collectRdsConfigs(any(), any())).thenReturn(rdsConfigs);
        Table<String, String, String> cmServiceConfigs = HashBasedTable.create();
        when(rdsSettingsMigrationService.collectCMServiceConfigs(rdsConfigs)).thenReturn(cmServiceConfigs);
        // WHEN
        underTest.startCluster(stack, cmTemplateProcessor, false);
        // THEN
        verify(connector).startClusterManagerAndAgents();
        verify(rdsSettingsMigrationService).updateCMServiceConfigs(stack, cmServiceConfigs);
        verify(connector).startCluster(true);
    }

    @Test
    void testStartClusterWithSharedRdsConfigRefreshAndException() throws Exception {
        // GIVEN
        Optional<Stack> datalake = Optional.of(new Stack());
        when(datalakeService.getDatalakeStackByDatahubStack(stack)).thenReturn(datalake);
        when(clusterServicesRestartService.isRemoteDataContextRefreshNeeded(any(), any())).thenReturn(false);
        Set<RDSConfig> rdsConfigs = Set.of(new RDSConfig());
        when(rdsSettingsMigrationService.collectRdsConfigs(any(), any())).thenReturn(rdsConfigs);
        Table<String, String, String> cmServiceConfigs = HashBasedTable.create();
        when(rdsSettingsMigrationService.collectCMServiceConfigs(rdsConfigs)).thenReturn(cmServiceConfigs);
        doThrow(new RuntimeException("msg")).when(rdsSettingsMigrationService).updateCMServiceConfigs(any(), any());
        // WHEN
        underTest.startCluster(stack, cmTemplateProcessor, false);
        // THEN
        verify(connector).startClusterManagerAndAgents();
        verify(rdsSettingsMigrationService).updateCMServiceConfigs(stack, cmServiceConfigs);
        verify(connector).startCluster(true);
    }

    @Test
    void testStartClusterWhenNoDatalake() throws Exception {
        // GIVEN
        Optional<Stack> datalake = Optional.empty();
        when(datalakeService.getDatalakeStackByDatahubStack(stack)).thenReturn(datalake);
        // WHEN
        underTest.startCluster(stack, cmTemplateProcessor, false);
        // THEN
        verify(connector).startClusterManagerAndAgents();
        verify(rdsSettingsMigrationService, never()).collectRdsConfigs(any(), any());
        verify(rdsSettingsMigrationService, never()).collectCMServiceConfigs(any());
        verify(rdsSettingsMigrationService, never()).updateCMServiceConfigs(any(), any());
        verify(connector).startCluster(true);
    }

    @Test
    void testStartClusterWithoutSharedRdsConfigRefresh() throws Exception {
        // GIVEN
        Optional<Stack> datalake = Optional.of(new Stack());
        when(datalakeService.getDatalakeStackByDatahubStack(stack)).thenReturn(datalake);
        when(clusterServicesRestartService.isRemoteDataContextRefreshNeeded(any(), any())).thenReturn(false);
        when(rdsSettingsMigrationService.collectRdsConfigs(any(), any())).thenReturn(Set.of());
        // WHEN
        underTest.startCluster(stack, cmTemplateProcessor, false);
        // THEN
        verify(connector).startClusterManagerAndAgents();
        verify(rdsSettingsMigrationService, never()).collectCMServiceConfigs(any());
        verify(rdsSettingsMigrationService, never()).updateCMServiceConfigs(any(), any());
        verify(connector).startCluster(true);
    }

    @Test
    void stopStartScalingFeatureShouldRecommissionComputeGroups() {
        underTest.handleStopStartScalingFeature(stack, cmTemplateProcessor);
        List<String> recommHosts = new ArrayList<>();
        recommHosts.add("computing-computing0.foo.bar");
        recommHosts.add("computing-compute0.foo.bar");
        verify(clusterCommissionService, times(1)).recommissionHosts(eq(recommHosts));
    }

    @Test
    void stopStartScalingFeatureShouldNotRecommissionNonComputeGroups() {
        List<String> decommHosts = new ArrayList<>();
        decommHosts.add("computing-working0.foo.bar");
        when(clusterStatusService.getDecommissionedHostsFromCM()).thenReturn(decommHosts);
        underTest.handleStopStartScalingFeature(stack, cmTemplateProcessor);
        verify(clusterCommissionService, times(0)).recommissionHosts(any());
    }
}
