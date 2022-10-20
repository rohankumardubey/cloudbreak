package com.sequenceiq.freeipa.flow.freeipa.verticalscale;

import static com.sequenceiq.freeipa.flow.freeipa.verticalscale.FreeIpaVerticalScaleState.FINAL_STATE;
import static com.sequenceiq.freeipa.flow.freeipa.verticalscale.FreeIpaVerticalScaleState.INIT_STATE;
import static com.sequenceiq.freeipa.flow.freeipa.verticalscale.FreeIpaVerticalScaleState.STACK_VERTICALSCALE_FAILED_STATE;
import static com.sequenceiq.freeipa.flow.freeipa.verticalscale.FreeIpaVerticalScaleState.STACK_VERTICALSCALE_FINISHED_STATE;
import static com.sequenceiq.freeipa.flow.freeipa.verticalscale.FreeIpaVerticalScaleState.STACK_VERTICALSCALE_STATE;
import static com.sequenceiq.freeipa.flow.freeipa.verticalscale.event.FreeIpaVerticalScaleEvent.FAILURE_EVENT;
import static com.sequenceiq.freeipa.flow.freeipa.verticalscale.event.FreeIpaVerticalScaleEvent.FAIL_HANDLED_EVENT;
import static com.sequenceiq.freeipa.flow.freeipa.verticalscale.event.FreeIpaVerticalScaleEvent.FINALIZED_EVENT;
import static com.sequenceiq.freeipa.flow.freeipa.verticalscale.event.FreeIpaVerticalScaleEvent.STACK_VERTICALSCALE_EVENT;
import static com.sequenceiq.freeipa.flow.freeipa.verticalscale.event.FreeIpaVerticalScaleEvent.STACK_VERTICALSCALE_FINISHED_EVENT;
import static com.sequenceiq.freeipa.flow.freeipa.verticalscale.event.FreeIpaVerticalScaleEvent.STACK_VERTICALSCALE_FINISHED_FAILURE_EVENT;

import java.util.List;

import org.springframework.stereotype.Component;

import com.sequenceiq.flow.core.config.AbstractFlowConfiguration;
import com.sequenceiq.flow.core.config.AbstractFlowConfiguration.Transition.Builder;
import com.sequenceiq.freeipa.flow.freeipa.verticalscale.event.FreeIpaVerticalScaleEvent;

@Component
public class FreeIpaVerticalScaleFlowConfig extends AbstractFlowConfiguration<FreeIpaVerticalScaleState, FreeIpaVerticalScaleEvent> {
    private static final List<Transition<FreeIpaVerticalScaleState, FreeIpaVerticalScaleEvent>> TRANSITIONS =
            new Builder<FreeIpaVerticalScaleState, FreeIpaVerticalScaleEvent>()
                    .defaultFailureEvent(FAILURE_EVENT)

                    .from(INIT_STATE)
                    .to(STACK_VERTICALSCALE_STATE)
                    .event(STACK_VERTICALSCALE_EVENT)
                    .failureEvent(STACK_VERTICALSCALE_FINISHED_FAILURE_EVENT)

                    .from(STACK_VERTICALSCALE_STATE)
                    .to(STACK_VERTICALSCALE_FINISHED_STATE)
                    .event(STACK_VERTICALSCALE_FINISHED_EVENT)
                    .failureEvent(STACK_VERTICALSCALE_FINISHED_FAILURE_EVENT)

                    .from(STACK_VERTICALSCALE_FINISHED_STATE)
                    .to(FINAL_STATE)
                    .event(FINALIZED_EVENT)
                    .failureEvent(STACK_VERTICALSCALE_FINISHED_FAILURE_EVENT)

                    .build();

    private static final FlowEdgeConfig<FreeIpaVerticalScaleState, FreeIpaVerticalScaleEvent> EDGE_CONFIG = new FlowEdgeConfig<>(
            INIT_STATE,
            FINAL_STATE,
            STACK_VERTICALSCALE_FAILED_STATE,
            FAIL_HANDLED_EVENT);

    public FreeIpaVerticalScaleFlowConfig() {
        super(FreeIpaVerticalScaleState.class, FreeIpaVerticalScaleEvent.class);
    }

    @Override
    protected List<Transition<FreeIpaVerticalScaleState, FreeIpaVerticalScaleEvent>> getTransitions() {
        return TRANSITIONS;
    }

    @Override
    protected FlowEdgeConfig<FreeIpaVerticalScaleState, FreeIpaVerticalScaleEvent> getEdgeConfig() {
        return EDGE_CONFIG;
    }

    @Override
    public FreeIpaVerticalScaleEvent[] getEvents() {
        return FreeIpaVerticalScaleEvent.values();
    }

    @Override
    public FreeIpaVerticalScaleEvent[] getInitEvents() {
        return new FreeIpaVerticalScaleEvent[] {
                STACK_VERTICALSCALE_EVENT
        };
    }

    @Override
    public String getDisplayName() {
        return "Vertical scaling on the FreeIPA";
    }

}
