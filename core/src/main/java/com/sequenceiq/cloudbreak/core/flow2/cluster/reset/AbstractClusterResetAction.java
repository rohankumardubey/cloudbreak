package com.sequenceiq.cloudbreak.core.flow2.cluster.reset;

import java.util.Optional;

import javax.inject.Inject;

import org.springframework.statemachine.StateContext;

import com.sequenceiq.cloudbreak.cloud.event.Payload;
import com.sequenceiq.cloudbreak.core.flow2.AbstractAction;
import com.sequenceiq.cloudbreak.core.flow2.cluster.ClusterMinimalContext;
import com.sequenceiq.cloudbreak.domain.StackMinimal;
import com.sequenceiq.cloudbreak.logger.MDCBuilder;
import com.sequenceiq.cloudbreak.reactor.api.event.StackFailureEvent;
import com.sequenceiq.cloudbreak.service.stack.StackService;

public abstract class AbstractClusterResetAction<P extends Payload> extends AbstractAction<ClusterResetState, ClusterResetEvent, ClusterMinimalContext, P> {
    @Inject
    private StackService stackService;

    protected AbstractClusterResetAction(Class<P> payloadClass) {
        super(payloadClass);
    }

    @Override
    protected ClusterMinimalContext createFlowContext(String flowId, StateContext<ClusterResetState, ClusterResetEvent> stateContext, P payload) {
        StackMinimal stack = stackService.getByIdMinimal(payload.getStackId());
        MDCBuilder.buildMdcContext(stack.getId().toString(), stack.getName(), stack.getOwner(), "CLUSTER");
        return new ClusterMinimalContext(flowId, stack);
    }

    @Override
    protected Object getFailurePayload(P payload, Optional<ClusterMinimalContext> flowContext, Exception ex) {
        return new StackFailureEvent(payload.getStackId(), ex);
    }
}
