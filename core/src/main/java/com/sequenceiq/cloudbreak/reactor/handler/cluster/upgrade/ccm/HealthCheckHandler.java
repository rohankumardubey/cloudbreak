package com.sequenceiq.cloudbreak.reactor.handler.cluster.upgrade.ccm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.sequenceiq.cloudbreak.common.event.Selectable;
import com.sequenceiq.cloudbreak.reactor.api.event.cluster.upgrade.ccm.UpgradeCcmFailedEvent;
import com.sequenceiq.cloudbreak.reactor.api.event.cluster.upgrade.ccm.UpgradeCcmHealthCheckRequest;
import com.sequenceiq.cloudbreak.reactor.api.event.cluster.upgrade.ccm.UpgradeCcmHealthCheckResult;
import com.sequenceiq.flow.event.EventSelectorUtil;
import com.sequenceiq.flow.reactor.api.handler.ExceptionCatcherEventHandler;
import com.sequenceiq.flow.reactor.api.handler.HandlerEvent;

import reactor.bus.Event;

@Component
public class HealthCheckHandler extends ExceptionCatcherEventHandler<UpgradeCcmHealthCheckRequest> {

    private static final Logger LOGGER = LoggerFactory.getLogger(HealthCheckHandler.class);

    @Override
    public String selector() {
        return EventSelectorUtil.selector(UpgradeCcmHealthCheckRequest.class);
    }

    @Override
    protected Selectable defaultFailureEvent(Long resourceId, Exception e, Event<UpgradeCcmHealthCheckRequest> event) {
        LOGGER.error("Health check for CCM upgrade has failed", e);
        return new UpgradeCcmFailedEvent(resourceId, event.getData().getOldTunnel(), getClass(), e);
    }

    @Override
    public Selectable doAccept(HandlerEvent<UpgradeCcmHealthCheckRequest> event) {
        UpgradeCcmHealthCheckRequest request = event.getData();
        Long stackId = request.getResourceId();
        // kept for forward compatibility with CB-14585
        return new UpgradeCcmHealthCheckResult(stackId, request.getClusterId(), request.getOldTunnel());
    }
}
