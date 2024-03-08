package com.sequenceiq.externalizedcompute.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import java.util.HashMap;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.sequenceiq.cloudbreak.auth.crn.RegionAwareCrnGenerator;
import com.sequenceiq.cloudbreak.common.json.Json;
import com.sequenceiq.externalizedcompute.api.model.ExternalizedComputeClusterApiStatus;
import com.sequenceiq.externalizedcompute.api.model.ExternalizedComputeClusterResponse;
import com.sequenceiq.externalizedcompute.entity.ExternalizedComputeCluster;
import com.sequenceiq.externalizedcompute.entity.ExternalizedComputeClusterStatus;
import com.sequenceiq.externalizedcompute.entity.ExternalizedComputeClusterStatusEnum;

@ExtendWith(MockitoExtension.class)
public class ExternalizedComputeClusterConverterServiceTest {

    @Mock
    private ExternalizedComputeClusterStatusService externalizedComputeClusterStatusService;

    @Spy
    private RegionAwareCrnGenerator regionAwareCrnGenerator;

    @InjectMocks
    private ExternalizedComputeClusterConverterService externalizedComputeClusterConverterService;

    @Test
    void convertToResponse() {
        ReflectionTestUtils.setField(regionAwareCrnGenerator, "partition", "cdp");
        ReflectionTestUtils.setField(regionAwareCrnGenerator, "region", "us-west-1");
        ExternalizedComputeCluster externalizedComputeCluster = new ExternalizedComputeCluster();
        externalizedComputeCluster.setName("ext-cluster");
        externalizedComputeCluster.setEnvironmentCrn("envCrn");
        externalizedComputeCluster.setLiftieName("liftiename");
        externalizedComputeCluster.setResourceCrn("ext-cluster-crn");
        externalizedComputeCluster.setCreated(123L);
        externalizedComputeCluster.setId(1L);
        externalizedComputeCluster.setAccountId("accid");
        externalizedComputeCluster.setTags(Json.silent(new HashMap<>()));
        ExternalizedComputeClusterStatus status = new ExternalizedComputeClusterStatus();
        status.setExternalizedComputeCluster(externalizedComputeCluster);
        status.setStatus(ExternalizedComputeClusterStatusEnum.AVAILABLE);
        status.setStatusReason("available");
        when(externalizedComputeClusterStatusService.getActualStatus(externalizedComputeCluster)).thenReturn(status);
        ExternalizedComputeClusterResponse externalizedComputeClusterResponse = externalizedComputeClusterConverterService.convertToResponse(
                externalizedComputeCluster);
        assertEquals("crn:cdp:compute:us-west-1:accid:cluster:liftiename", externalizedComputeClusterResponse.getLiftieClusterCrn());
        assertEquals(ExternalizedComputeClusterApiStatus.AVAILABLE, externalizedComputeClusterResponse.getStatus());
        assertEquals("available", externalizedComputeClusterResponse.getStatusReason());
    }

    @Test
    void convertToResponseNoLiftieName() {
        ReflectionTestUtils.setField(regionAwareCrnGenerator, "partition", "cdp");
        ReflectionTestUtils.setField(regionAwareCrnGenerator, "region", "us-west-1");
        ExternalizedComputeCluster externalizedComputeCluster = new ExternalizedComputeCluster();
        externalizedComputeCluster.setName("ext-cluster");
        externalizedComputeCluster.setEnvironmentCrn("envCrn");
        externalizedComputeCluster.setResourceCrn("ext-cluster-crn");
        externalizedComputeCluster.setCreated(123L);
        externalizedComputeCluster.setId(1L);
        externalizedComputeCluster.setAccountId("accid");
        externalizedComputeCluster.setTags(Json.silent(new HashMap<>()));
        ExternalizedComputeClusterStatus status = new ExternalizedComputeClusterStatus();
        status.setExternalizedComputeCluster(externalizedComputeCluster);
        status.setStatus(ExternalizedComputeClusterStatusEnum.AVAILABLE);
        status.setStatusReason("available");
        when(externalizedComputeClusterStatusService.getActualStatus(externalizedComputeCluster)).thenReturn(status);
        ExternalizedComputeClusterResponse externalizedComputeClusterResponse = externalizedComputeClusterConverterService.convertToResponse(
                externalizedComputeCluster);
        assertNull(externalizedComputeClusterResponse.getLiftieClusterCrn());
        assertEquals(ExternalizedComputeClusterApiStatus.AVAILABLE, externalizedComputeClusterResponse.getStatus());
        assertEquals("available", externalizedComputeClusterResponse.getStatusReason());
    }
}