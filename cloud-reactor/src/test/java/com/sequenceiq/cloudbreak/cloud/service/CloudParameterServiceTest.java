package com.sequenceiq.cloudbreak.cloud.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jakarta.ws.rs.BadRequestException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.microsoft.aad.msal4j.MsalServiceException;
import com.sequenceiq.cloudbreak.cloud.event.platform.GetPlatformDatabaseCapabilityRequest;
import com.sequenceiq.cloudbreak.cloud.event.platform.GetPlatformDatabaseCapabilityResult;
import com.sequenceiq.cloudbreak.cloud.event.platform.GetPlatformNoSqlTablesRequest;
import com.sequenceiq.cloudbreak.cloud.event.platform.GetPlatformNoSqlTablesResult;
import com.sequenceiq.cloudbreak.cloud.model.CloudCredential;
import com.sequenceiq.cloudbreak.cloud.model.ExtendedCloudCredential;
import com.sequenceiq.cloudbreak.cloud.model.PlatformDatabaseCapabilities;
import com.sequenceiq.cloudbreak.cloud.model.nosql.CloudNoSqlTable;
import com.sequenceiq.cloudbreak.cloud.model.nosql.CloudNoSqlTables;
import com.sequenceiq.cloudbreak.eventbus.Event;
import com.sequenceiq.cloudbreak.eventbus.EventBus;
import com.sequenceiq.flow.reactor.ErrorHandlerAwareReactorEventFactory;

@ExtendWith(MockitoExtension.class)
class CloudParameterServiceTest {

    @Mock
    private EventBus eventBus;

    @Mock
    private ErrorHandlerAwareReactorEventFactory eventFactory;

    @InjectMocks
    private CloudParameterService underTest;

    @Test
    void getNoSqlTables() {
        CloudNoSqlTables expected = new CloudNoSqlTables(List.of(new CloudNoSqlTable("a"), new CloudNoSqlTable("b")));
        GetPlatformNoSqlTablesResult response = new GetPlatformNoSqlTablesResult(1L, expected);
        doAnswer(invocation -> {
            Event<GetPlatformNoSqlTablesRequest> ev = invocation.getArgument(1);
            ev.getData().getResult().onNext(response);
            return null;
        }).when(eventBus).notify(anyString(), any(Event.class));
        CloudNoSqlTables noSqlTables = underTest.getNoSqlTables(
                new ExtendedCloudCredential(new CloudCredential("id", "name", "acc"),
                        "aws", "desc", "account", new ArrayList<>()), "region", "aws", null);
        assertEquals(expected, noSqlTables);
    }

    @Test
    void getDatabaseCapabilities() {
        PlatformDatabaseCapabilities expected = new PlatformDatabaseCapabilities(new HashMap<>(), null);
        GetPlatformDatabaseCapabilityResult response = new GetPlatformDatabaseCapabilityResult(1L, expected);
        doAnswer(invocation -> {
            Event<GetPlatformDatabaseCapabilityRequest> ev = invocation.getArgument(1);
            ev.getData().getResult().onNext(response);
            return null;
        }).when(eventBus).notify(anyString(), any(Event.class));
        PlatformDatabaseCapabilities platformDatabaseCapabilities = underTest.getDatabaseCapabilities(
                new ExtendedCloudCredential(new CloudCredential("id", "name", "acc"),
                        "aws", "desc", "account", new ArrayList<>()), "region", "aws", null);
        assertEquals(expected, platformDatabaseCapabilities);
    }

    @Test
    void getDatabaseCapabilitiesFailsWithAuthorizationError() {
        MsalServiceException errorResponse = mock(MsalServiceException.class);
        when(errorResponse.statusCode()).thenReturn(401);
        doAnswer(invocation -> {
            Event<GetPlatformDatabaseCapabilityRequest> ev = invocation.getArgument(1);
            ev.getData().getResult().onNext(new GetPlatformDatabaseCapabilityResult("Unauthorized", errorResponse, 1L));
            return null;
        }).when(eventBus).notify(anyString(), any(Event.class));
        assertThrows(BadRequestException.class, () -> underTest.getDatabaseCapabilities(
                new ExtendedCloudCredential(new CloudCredential("id", "name", "acc"),
                        "aws", "desc", "account", new ArrayList<>()), "region", "aws", null));
    }

    @Test
    void getDatabaseCapabilitiesFailsWithTooManyRequestError() {
        MsalServiceException errorResponse = mock(MsalServiceException.class);
        when(errorResponse.statusCode()).thenReturn(427);
        doAnswer(invocation -> {
            Event<GetPlatformDatabaseCapabilityRequest> ev = invocation.getArgument(1);
            ev.getData().getResult().onNext(new GetPlatformDatabaseCapabilityResult("Unauthorized", errorResponse, 1L));
            return null;
        }).when(eventBus).notify(anyString(), any(Event.class));
        assertThrows(GetCloudParameterException.class, () -> underTest.getDatabaseCapabilities(
                new ExtendedCloudCredential(new CloudCredential("id", "name", "acc"),
                        "aws", "desc", "account", new ArrayList<>()), "region", "aws", null));
    }
}
