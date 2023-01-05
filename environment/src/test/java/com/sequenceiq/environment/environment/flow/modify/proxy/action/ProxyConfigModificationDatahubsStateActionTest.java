package com.sequenceiq.environment.environment.flow.modify.proxy.action;

import static org.mockito.Mockito.verify;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sequenceiq.cloudbreak.event.ResourceEvent;
import com.sequenceiq.environment.environment.EnvironmentStatus;
import com.sequenceiq.environment.environment.flow.modify.proxy.EnvProxyModificationContext;
import com.sequenceiq.environment.environment.flow.modify.proxy.EnvProxyModificationState;
import com.sequenceiq.environment.environment.flow.modify.proxy.event.EnvProxyModificationDefaultEvent;
import com.sequenceiq.environment.environment.flow.modify.proxy.event.EnvProxyModificationHandlerSelectors;
import com.sequenceiq.environment.environment.service.EnvironmentStatusUpdateService;
import com.sequenceiq.flow.core.ActionTest;

@ExtendWith(MockitoExtension.class)
class ProxyConfigModificationDatahubsStateActionTest extends ActionTest {

    @Mock
    private EnvironmentStatusUpdateService environmentStatusUpdateService;

    @InjectMocks
    private ProxyConfigModificationDatahubsStateAction underTest;

    private EnvProxyModificationContext context;

    private EnvProxyModificationDefaultEvent payload;

    @BeforeEach
    void setUp() {
        context = new EnvProxyModificationContext(flowParameters, null, null);
        payload = EnvProxyModificationDefaultEvent.builder()
                .withProxyConfigCrn(null)
                .withPreviousProxyConfigCrn(null)
                .withResourceId(1L)
                .build();
    }

    @Test
    void doExecute() throws Exception {
        underTest.doExecute(context, payload, Map.of());

        verify(environmentStatusUpdateService).updateEnvironmentStatusAndNotify(context, payload,
                EnvironmentStatus.PROXY_CONFIG_MODIFICATION_ON_DATAHUBS_IN_PROGRESS, ResourceEvent.ENVIRONMENT_PROXY_CONFIG_MODIFICATION_ON_DATAHUBS_STARTED,
                EnvProxyModificationState.PROXY_CONFIG_MODIFICATION_DATAHUBS_STATE);
        verifySendEvent(EnvProxyModificationHandlerSelectors.TRACK_DATAHUBS_PROXY_MODIFICATION_EVENT.selector());
    }

}
