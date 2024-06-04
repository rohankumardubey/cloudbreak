package com.sequenceiq.cloudbreak.controller.v4;

import static com.sequenceiq.cloudbreak.auth.ThreadBasedUserCrnProvider.doAs;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.EnumSet;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sequenceiq.cloudbreak.api.endpoint.v4.common.StackType;
import com.sequenceiq.cloudbreak.api.endpoint.v4.dto.NameOrCrn;
import com.sequenceiq.cloudbreak.api.endpoint.v4.stacks.request.ChangeImageCatalogV4Request;
import com.sequenceiq.cloudbreak.api.endpoint.v4.stacks.request.RotateSaltPasswordRequest;
import com.sequenceiq.cloudbreak.api.endpoint.v4.stacks.response.SaltPasswordStatus;
import com.sequenceiq.cloudbreak.api.endpoint.v4.stacks.response.SaltPasswordStatusResponse;
import com.sequenceiq.cloudbreak.api.endpoint.v4.stacks.response.rotaterdscert.StackRotateRdsCertificateV4Response;
import com.sequenceiq.cloudbreak.api.endpoint.v4.stacks.response.upgrade.StackCcmUpgradeV4Response;
import com.sequenceiq.cloudbreak.api.model.CcmUpgradeResponseType;
import com.sequenceiq.cloudbreak.api.model.RotateRdsCertResponseType;
import com.sequenceiq.cloudbreak.api.model.RotateSaltPasswordReason;
import com.sequenceiq.cloudbreak.service.rotaterdscert.StackRotateRdsCertificateService;
import com.sequenceiq.cloudbreak.service.stack.flow.StackOperationService;
import com.sequenceiq.cloudbreak.service.upgrade.ccm.StackCcmUpgradeService;
import com.sequenceiq.cloudbreak.structuredevent.CloudbreakRestRequestThreadLocalService;
import com.sequenceiq.distrox.v1.distrox.StackOperations;
import com.sequenceiq.distrox.v1.distrox.StackUpgradeOperations;
import com.sequenceiq.flow.api.model.FlowIdentifier;
import com.sequenceiq.flow.api.model.FlowType;

@ExtendWith(MockitoExtension.class)
class StackV4ControllerTest {

    private static final String USER_CRN = "crn:cdp:iam:us-west-1:hortonworks:user:test@test.com";

    private static final String STACK_CRN = "crn:cdp:datalake:us-west-1:hortonworks:datalake:guid";

    private static final long WORKSPACE_ID = 1236L;

    private static final String ACCOUNT_ID = "hortonworks";

    private static final String STACK_NAME = "stack name";

    @Mock
    private StackOperations stackOperations;

    @Mock
    private StackOperationService stackOperationService;

    @Mock
    private CloudbreakRestRequestThreadLocalService restRequestThreadLocalService;

    @Mock
    private StackCcmUpgradeService stackCcmUpgradeService;

    @Mock
    private StackUpgradeOperations stackUpgradeOperations;

    @Mock
    private StackRotateRdsCertificateService stackRotateRdsCertificateService;

    @InjectMocks
    private StackV4Controller underTest;

    @BeforeEach
    void setUp() {
        lenient().when(restRequestThreadLocalService.getRequestedWorkspaceId()).thenReturn(WORKSPACE_ID);
    }

    @Test
    void changeImageCatalogInternalTest() {
        String imageCatalog = "image-catalog";
        ChangeImageCatalogV4Request request = new ChangeImageCatalogV4Request();
        request.setImageCatalog(imageCatalog);

        underTest.changeImageCatalogInternal(WORKSPACE_ID, STACK_NAME, USER_CRN, request);

        verify(stackOperations).changeImageCatalog(NameOrCrn.ofName(STACK_NAME), WORKSPACE_ID, imageCatalog);
    }

    @Test
    void rangerRazEnabledTest() {
        String stackCrn = "test-crn";

        underTest.rangerRazEnabledInternal(WORKSPACE_ID, stackCrn, USER_CRN);

        verify(stackOperationService).rangerRazEnabled(stackCrn);
    }

    @Test
    void generateImageCatalogInternalTest() {
        underTest.generateImageCatalogInternal(WORKSPACE_ID, STACK_NAME, USER_CRN);

        verify(stackOperations).generateImageCatalog(NameOrCrn.ofName(STACK_NAME), WORKSPACE_ID);
    }

    @Test
    void testCcmUpgrade() {
        FlowIdentifier flowId = new FlowIdentifier(FlowType.FLOW, "1");
        StackCcmUpgradeV4Response actual = new StackCcmUpgradeV4Response(CcmUpgradeResponseType.TRIGGERED, flowId, null, STACK_CRN);
        when(stackCcmUpgradeService.upgradeCcm(NameOrCrn.ofCrn(STACK_CRN))).thenReturn(actual);
        StackCcmUpgradeV4Response result = underTest.upgradeCcmByCrnInternal(WORKSPACE_ID, STACK_CRN, USER_CRN);
        Assertions.assertSame(actual, result);
    }

    @Test
    void rotateSaltPasswordInternal() {
        String stackCrn = "crn";
        RotateSaltPasswordRequest request = new RotateSaltPasswordRequest(com.sequenceiq.cloudbreak.api.model.RotateSaltPasswordReason.MANUAL);

        doAs(USER_CRN, () -> underTest.rotateSaltPasswordInternal(WORKSPACE_ID, stackCrn, request, USER_CRN));

        verify(stackOperations).rotateSaltPassword(NameOrCrn.ofCrn(stackCrn), ACCOUNT_ID, RotateSaltPasswordReason.MANUAL);
    }

    @Test
    void getSaltPasswordStatus() {
        String stackCrn = "crn";
        when(stackOperations.getSaltPasswordStatus(NameOrCrn.ofCrn(stackCrn), ACCOUNT_ID)).thenReturn(SaltPasswordStatus.OK);

        SaltPasswordStatusResponse response = doAs(USER_CRN, () -> underTest.getSaltPasswordStatus(WORKSPACE_ID, stackCrn));

        assertEquals(SaltPasswordStatus.OK, response.getStatus());
    }

    @Test
    void modifyProxyConfigInternal() {
        String stackCrn = "crn";
        String previousProxyConfigCrn = "proxy-crn";

        doAs(USER_CRN, () -> underTest.modifyProxyConfigInternal(WORKSPACE_ID, stackCrn, previousProxyConfigCrn, USER_CRN));

        verify(stackOperations).modifyProxyConfig(NameOrCrn.ofCrn(stackCrn), ACCOUNT_ID, previousProxyConfigCrn);
    }

    @Test
    void testPrepareClusterUpgradeByCrnInternal() {
        String imageId = "imageId";
        FlowIdentifier flowIdentifier = new FlowIdentifier(FlowType.FLOW, "pollId");
        when(stackUpgradeOperations.prepareClusterUpgrade(NameOrCrn.ofCrn(STACK_CRN), ACCOUNT_ID, imageId)).thenReturn(flowIdentifier);

        FlowIdentifier result = doAs(USER_CRN, () -> underTest.prepareClusterUpgradeByCrnInternal(WORKSPACE_ID, STACK_CRN, imageId, USER_CRN));

        assertEquals(flowIdentifier, result);
    }

    @Test
    void syncTest() {
        FlowIdentifier flowIdentifier = mock(FlowIdentifier.class);
        when(stackOperations.sync(NameOrCrn.ofName(STACK_NAME), ACCOUNT_ID, EnumSet.of(StackType.WORKLOAD, StackType.DATALAKE))).thenReturn(flowIdentifier);

        FlowIdentifier result = doAs(USER_CRN, () -> underTest.sync(WORKSPACE_ID, STACK_NAME, "dummyAccountId"));

        assertThat(result).isSameAs(flowIdentifier);
    }

    @Test
    void testRotateRdsCertificate() {
        FlowIdentifier flowId = new FlowIdentifier(FlowType.FLOW, "1");
        StackRotateRdsCertificateV4Response actual = new StackRotateRdsCertificateV4Response(RotateRdsCertResponseType.TRIGGERED, flowId, null, STACK_CRN);
        when(stackRotateRdsCertificateService.rotateRdsCertificate(NameOrCrn.ofCrn(STACK_CRN), WORKSPACE_ID)).thenReturn(actual);
        StackRotateRdsCertificateV4Response result = underTest.rotateRdsCertificateByCrnInternal(WORKSPACE_ID, STACK_CRN, USER_CRN);
        Assertions.assertSame(actual, result);
    }
}
