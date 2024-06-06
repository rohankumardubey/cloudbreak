package com.sequenceiq.cloudbreak.sdx.paas;

import static com.sequenceiq.cloudbreak.sdx.RdcConstants.HiveMetastoreDatabase.HIVE_METASTORE_DATABASE_HOST;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cloudera.api.swagger.model.ApiEndPoint;
import com.cloudera.api.swagger.model.ApiMapEntry;
import com.cloudera.api.swagger.model.ApiRemoteDataContext;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sequenceiq.cloudbreak.sdx.common.model.SdxBasicView;
import com.sequenceiq.cloudbreak.sdx.paas.service.PaasSdxDescribeService;
import com.sequenceiq.sdx.api.endpoint.SdxEndpoint;
import com.sequenceiq.sdx.api.model.SdxClusterResponse;
import com.sequenceiq.sdx.api.model.SdxClusterStatusResponse;

@ExtendWith(MockitoExtension.class)
public class PaasSdxDescribeServiceTest {

    private static final String PAAS_CRN = "crn:cdp:datalake:us-west-1:tenant:datalake:crn1";

    private static final String ENV_CRN = "crn:cdp:environments:us-west-1:tenant:environment:crn1";

    @Mock
    private SdxEndpoint sdxEndpoint;

    @InjectMocks
    private PaasSdxDescribeService underTest;

    @Test
    public void testListCrn() {
        when(sdxEndpoint.getByEnvCrn(any())).thenReturn(List.of(getSdxClusterResponse()));
        Set<String> sdxCrns = underTest.listSdxCrns(ENV_CRN);
        assertTrue(sdxCrns.contains(PAAS_CRN));
        verify(sdxEndpoint).getByEnvCrn(eq(ENV_CRN));
    }

    @Test
    void testGetPaasSdxLocally() throws IllegalAccessException {
        LocalPaasSdxService mockLocalSdxService = mock(LocalPaasSdxService.class);
        FieldUtils.writeField(underTest, "localPaasSdxService", Optional.of(mockLocalSdxService), true);
        when(mockLocalSdxService.getSdxBasicView(anyString())).thenReturn(Optional.of(new SdxBasicView(null, "crn", null, false, 1L, null)));

        underTest.getSdxByEnvironmentCrn("envCrn");

        verifyNoInteractions(sdxEndpoint);
        verify(mockLocalSdxService).getSdxBasicView(anyString());
    }

    @Test
    void testGetPaasSdxUsingDlService() throws IllegalAccessException {
        FieldUtils.writeField(underTest, "localPaasSdxService", Optional.empty(), true);
        when(sdxEndpoint.getByEnvCrn(anyString())).thenReturn(List.of(getSdxClusterResponse()));

        underTest.getSdxByEnvironmentCrn("envCrn");

        verify(sdxEndpoint).getByEnvCrn(anyString());
    }

    @Test
    void testGetHmsConfigIfEmpty() throws IllegalAccessException, JsonProcessingException {
        PaasRemoteDataContextSupplier rdcSupplier = mock(PaasRemoteDataContextSupplier.class);
        FieldUtils.writeField(underTest, "remoteDataContextSupplier", Optional.of(rdcSupplier), true);
        when(rdcSupplier.getPaasSdxRemoteDataContext(any())).thenReturn(Optional.empty());

        assertEquals(Map.of(), underTest.getHmsServiceConfig(PAAS_CRN));

        when(rdcSupplier.getPaasSdxRemoteDataContext(any())).thenReturn(Optional.of(new ObjectMapper().writeValueAsString(new ApiRemoteDataContext())));

        assertEquals(Map.of(), underTest.getHmsServiceConfig(PAAS_CRN));

        ApiRemoteDataContext apiRemoteDataContext = new ApiRemoteDataContext();
        ApiEndPoint apiEndPoint = new ApiEndPoint();
        apiEndPoint.setName("hive");
        apiRemoteDataContext.setEndPoints(List.of(apiEndPoint));
        when(rdcSupplier.getPaasSdxRemoteDataContext(any())).thenReturn(Optional.of(new ObjectMapper().writeValueAsString(apiRemoteDataContext)));

        assertEquals(Map.of(), underTest.getHmsServiceConfig(PAAS_CRN));
    }

    @Test
    void testGetHmsConfig() throws IllegalAccessException, JsonProcessingException {
        PaasRemoteDataContextSupplier rdcSupplier = mock(PaasRemoteDataContextSupplier.class);
        FieldUtils.writeField(underTest, "remoteDataContextSupplier", Optional.of(rdcSupplier), true);
        ApiRemoteDataContext apiRemoteDataContext = new ApiRemoteDataContext();
        ApiEndPoint apiEndPoint = new ApiEndPoint();
        apiEndPoint.setName("hive");
        ApiMapEntry apiMapEntry = new ApiMapEntry();
        apiMapEntry.setKey(HIVE_METASTORE_DATABASE_HOST);
        apiMapEntry.setValue("host");
        apiEndPoint.setServiceConfigs(List.of(apiMapEntry));
        apiRemoteDataContext.setEndPoints(List.of(apiEndPoint));
        when(rdcSupplier.getPaasSdxRemoteDataContext(any())).thenReturn(Optional.of(new ObjectMapper().writeValueAsString(apiRemoteDataContext)));

        assertEquals(Map.of(HIVE_METASTORE_DATABASE_HOST, "host"), underTest.getHmsServiceConfig(PAAS_CRN));
    }

    private SdxClusterResponse getSdxClusterResponse() {
        SdxClusterResponse sdxClusterResponse = new SdxClusterResponse();
        sdxClusterResponse.setCrn(PAAS_CRN);
        sdxClusterResponse.setEnvironmentCrn(ENV_CRN);
        sdxClusterResponse.setStatus(SdxClusterStatusResponse.RUNNING);
        return sdxClusterResponse;
    }
}
