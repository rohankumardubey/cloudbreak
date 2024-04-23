package com.sequenceiq.it.cloudbreak.assertion.freeipa;

import java.util.Collections;
import java.util.List;

import jakarta.inject.Inject;

import org.springframework.stereotype.Component;

import com.sequenceiq.cloudbreak.structuredevent.event.cdp.CDPStructuredEvent;
import com.sequenceiq.it.cloudbreak.assertion.EventAssertionCommon;
import com.sequenceiq.it.cloudbreak.context.TestContext;
import com.sequenceiq.it.cloudbreak.dto.freeipa.FreeIpaTestDto;
import com.sequenceiq.it.cloudbreak.microservice.FreeIpaClient;

@Component
public class FreeIpaListStructuredEventAssertions {

    @Inject
    private EventAssertionCommon eventAssertionCommon;

    public FreeIpaTestDto checkCreateEvents(TestContext testContext, FreeIpaTestDto testDto, FreeIpaClient client) {
        List<CDPStructuredEvent> auditEvents = client.getDefaultClient()
                .structuredEventsV1Endpoint()
                .getAuditEvents(testDto.getCrn(), Collections.emptyList(), 0, 100);
        eventAssertionCommon.noFlowEventsAreAllowedInDB(auditEvents);
        eventAssertionCommon.noRestEventsAreAllowedInDB(auditEvents);
        return testDto;
    }

    public FreeIpaTestDto checkDeleteEvents(TestContext testContext, FreeIpaTestDto testDto, FreeIpaClient client) {
        List<CDPStructuredEvent> auditEvents = client.getDefaultClient()
                .structuredEventsV1Endpoint()
                .getAuditEvents(testDto.getCrn(), Collections.emptyList(), 0, 100);
        eventAssertionCommon.noFlowEventsAreAllowedInDB(auditEvents);
        eventAssertionCommon.noRestEventsAreAllowedInDB(auditEvents);
        return testDto;
    }

    public FreeIpaTestDto checkStopEvents(TestContext testContext, FreeIpaTestDto testDto, FreeIpaClient client) {
        List<CDPStructuredEvent> auditEvents = client.getDefaultClient()
                .structuredEventsV1Endpoint()
                .getAuditEvents(testDto.getCrn(), Collections.emptyList(), 0, 100);
        eventAssertionCommon.noFlowEventsAreAllowedInDB(auditEvents);
        eventAssertionCommon.noRestEventsAreAllowedInDB(auditEvents);
        return testDto;
    }

    public FreeIpaTestDto checkStartEvents(TestContext testContext, FreeIpaTestDto testDto, FreeIpaClient client) {
        List<CDPStructuredEvent> auditEvents = client.getDefaultClient()
                .structuredEventsV1Endpoint()
                .getAuditEvents(testDto.getCrn(), Collections.emptyList(), 0, 100);
        eventAssertionCommon.noFlowEventsAreAllowedInDB(auditEvents);
        eventAssertionCommon.noRestEventsAreAllowedInDB(auditEvents);
        return testDto;
    }
}
