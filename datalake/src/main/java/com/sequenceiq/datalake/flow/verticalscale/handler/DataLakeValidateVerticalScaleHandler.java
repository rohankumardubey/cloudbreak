package com.sequenceiq.datalake.flow.verticalscale.handler;

import static com.sequenceiq.datalake.flow.verticalscale.event.DataLakeVerticalScaleHandlerSelectors.VERTICAL_SCALING_DATALAKE_VALIDATION_HANDLER;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.sequenceiq.datalake.entity.DatalakeStatusEnum;
import com.sequenceiq.datalake.flow.verticalscale.event.DataLakeVerticalScaleEvent;
import com.sequenceiq.datalake.flow.verticalscale.event.DataLakeVerticalScaleFailedEvent;
import com.sequenceiq.datalake.flow.verticalscale.event.DataLakeVerticalScaleStateSelectors;
import com.sequenceiq.flow.reactor.api.event.EventSender;
import com.sequenceiq.flow.reactor.api.handler.EventSenderAwareHandler;

import reactor.bus.Event;
import reactor.rx.Promise;

@Component
public class DataLakeValidateVerticalScaleHandler extends EventSenderAwareHandler<DataLakeVerticalScaleEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataLakeValidateVerticalScaleHandler.class);

    protected DataLakeValidateVerticalScaleHandler(EventSender eventSender) {
        super(eventSender);
    }

    @Override
    public String selector() {
        return VERTICAL_SCALING_DATALAKE_VALIDATION_HANDLER.selector();
    }

    @Override
    public void accept(Event<DataLakeVerticalScaleEvent> verticalScaleDataLakeEvent) {
        LOGGER.debug("In ValidateVerticalScaleDataLakeHandler.accept");
        try {
            DataLakeVerticalScaleEvent result = DataLakeVerticalScaleEvent.builder()
                    .withAccepted(new Promise<>())
                    .withSelector(DataLakeVerticalScaleStateSelectors.VERTICAL_SCALING_DATALAKE_EVENT.selector())
                    .withResourceCrn(verticalScaleDataLakeEvent.getData().getResourceCrn())
                    .withResourceId(verticalScaleDataLakeEvent.getData().getResourceId())
                    .withResourceName(verticalScaleDataLakeEvent.getData().getResourceName())
                    .withStackCrn(verticalScaleDataLakeEvent.getData().getStackCrn())
                    .withVerticalScaleRequest(verticalScaleDataLakeEvent.getData().getVerticalScaleRequest())
                    .build();

            eventSender().sendEvent(result, verticalScaleDataLakeEvent.getHeaders());
            LOGGER.debug("VERTICAL_SCALE_DATALAKE_EVENT event sent");
        } catch (Exception e) {
            DataLakeVerticalScaleFailedEvent failedEvent =
                    new DataLakeVerticalScaleFailedEvent(verticalScaleDataLakeEvent.getData(), e, DatalakeStatusEnum.VERTICAL_SCALE_FAILED);
            eventSender().sendEvent(failedEvent, verticalScaleDataLakeEvent.getHeaders());
            LOGGER.debug("VERTICAL_SCALE_DATALAKE_FAILED event sent");
        }
    }
}
