package com.sequenceiq.environment.credential.validation;

import static com.sequenceiq.common.model.CredentialType.AUDIT;
import static com.sequenceiq.common.model.CredentialType.ENVIRONMENT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import jakarta.ws.rs.BadRequestException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sequenceiq.cloudbreak.auth.altus.EntitlementService;
import com.sequenceiq.cloudbreak.common.mappable.CloudPlatform;
import com.sequenceiq.cloudbreak.validation.ValidationResult;
import com.sequenceiq.common.model.CredentialType;
import com.sequenceiq.environment.api.v1.credential.model.parameters.aws.AwsCredentialParameters;
import com.sequenceiq.environment.api.v1.credential.model.parameters.aws.RoleBasedParameters;
import com.sequenceiq.environment.api.v1.credential.model.request.CredentialRequest;
import com.sequenceiq.environment.credential.domain.Credential;
import com.sequenceiq.environment.credential.validation.definition.CredentialDefinitionService;

@ExtendWith(MockitoExtension.class)
class CredentialValidatorTest {

    private static final String ACCOUNT_ID = UUID.randomUUID().toString();

    private static final String USER_CRN = "crn:altus:iam:us-west-1:" + ACCOUNT_ID + ":user:" + UUID.randomUUID().toString();

    private static final String AWS = "AWS";

    private static final String AZURE = "AZURE";

    private static final String GCP = "GCP";

    private static final String FOO = "FOO";

    private static final String BLAH = "BLAH";

    private static final String BAZ = "BAZ";

    private static final String AZURE_DISABLED = " & Azure disabled";

    private static final String AZURE_ENABLED = " & Azure enabled";

    private static final String GCP_ENABLED = " & Google enabled";

    private static final String GCP_DISABLED = " & Google disabled";

    private static final String GCP_AUDIT_ENABLED = " & Google audit enabled";

    private static final String GCP_AUDIT_DISABLED = " & Google audit disabled";

    @Mock
    private CredentialDefinitionService credentialDefinitionService;

    @Mock
    private ProviderCredentialValidator providerCredentialValidator;

    @Mock
    private EntitlementService entitlementService;

    private CredentialValidator underTest;

    @BeforeEach
    void setUp() {
        when(providerCredentialValidator.supportedProvider()).thenReturn("AWS");
        underTest = new CredentialValidator(Set.of(AWS, AZURE, GCP), credentialDefinitionService, List.of(providerCredentialValidator), entitlementService);
    }

    // @formatter:off
    // CHECKSTYLE:OFF
    static Object[][] validateCredentialCloudPlatformDataProvider() {
        return new Object[][]{
                //testCaseName             cloudPlatform    validExpected   credentialType
                {AWS + AZURE_DISABLED,      AWS,               true,           ENVIRONMENT},
                {AZURE + AZURE_DISABLED,    AZURE,             true,           ENVIRONMENT},
                {FOO + AZURE_DISABLED,      FOO,               false,          ENVIRONMENT},
                {FOO + AZURE_ENABLED,       FOO,               false,          ENVIRONMENT},
                {GCP + GCP_AUDIT_ENABLED,   GCP,               true,           AUDIT},
        };
    }
    // CHECKSTYLE:ON
    // @formatter:on

    @ParameterizedTest(name = "{0}")
    @MethodSource("validateCredentialCloudPlatformDataProvider")
    void testValidateCredentialCloudPlatform(String testCaseName,
            String cloudPlatform,
            boolean validExpected,
            CredentialType credentialType) {
        if (validExpected) {
            underTest.validateCredentialCloudPlatform(cloudPlatform, USER_CRN, credentialType);
        } else {
            assertThrows(BadRequestException.class, () -> underTest.validateCredentialCloudPlatform(cloudPlatform, USER_CRN, credentialType));
        }
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("validateCredentialCloudPlatformDataProvider")
    void testIsCredentialCloudPlatformValid(String testCaseName,
            String cloudPlatform,
            boolean validExpected,
            CredentialType credentialType) {
        assertThat(underTest.isCredentialCloudPlatformValid(cloudPlatform, ACCOUNT_ID, credentialType)).isEqualTo(validExpected);
    }

    @Test
    void testValidateCreateWhenNoError() {
        CredentialRequest credentialRequest = new CredentialRequest();
        credentialRequest.setCloudPlatform("AWS");

        assertDoesNotThrow(() -> underTest.validateCreate(credentialRequest));
    }

    @Test
    void testValidateCreateWhenError() {
        CredentialRequest credentialRequest = new CredentialRequest();
        credentialRequest.setCloudPlatform("AWS");

        when(providerCredentialValidator.validateCreate(any(), any())).thenReturn(ValidationResult.builder().error("error").build());
        BadRequestException exc = assertThrows(BadRequestException.class, () -> underTest.validateCreate(credentialRequest));
        assertEquals("error", exc.getMessage());
    }

    @Test
    void testValidateCredentialUpdate() {
        Credential original = new Credential();
        original.setCloudPlatform(CloudPlatform.AWS.name());
        Credential newCred = new Credential();
        newCred.setCloudPlatform(CloudPlatform.AWS.name());

        ValidationResult result = underTest.validateCredentialUpdate(original, newCred, ENVIRONMENT);
        assertFalse(result.hasError());
    }

    @Test
    void testValidateCredentialUpdateWhenInvalidPlatformChange() {
        Credential original = new Credential();
        original.setCloudPlatform(CloudPlatform.AWS.name());
        Credential newCred = new Credential();
        newCred.setCloudPlatform(CloudPlatform.AZURE.name());

        ValidationResult result = underTest.validateCredentialUpdate(original, newCred, ENVIRONMENT);
        assertEquals(1, result.getErrors().size());
        assertThat(result.getErrors().get(0)).contains("CloudPlatform of the credential cannot be changed! Original: 'AWS' New: 'AZURE'.");
    }

    @Test
    void testValidateAwsCredentialRequestNotAWS() {
        CredentialRequest request = new CredentialRequest();
        request.setCloudPlatform("AZURE");
        ValidationResult result = underTest.validateAwsCredentialRequest(request);
        assertTrue(result.hasError());
        assertEquals("Credential request is not for AWS.", result.getErrors().get(0));
    }

    @Test
    void testValidateAwsCredentialRequestNoAwsParams() {
        CredentialRequest request = new CredentialRequest();
        request.setCloudPlatform("AWS");
        ValidationResult result = underTest.validateAwsCredentialRequest(request);
        assertTrue(result.hasError());
        assertEquals("Role ARN is not found in credential request.", result.getErrors().get(0));
    }

    @Test
    void testValidateAwsCredentialRequestKeyBased() {
        CredentialRequest request = new CredentialRequest();
        request.setCloudPlatform("AWS");
        request.setAws(new AwsCredentialParameters());
        ValidationResult result = underTest.validateAwsCredentialRequest(request);
        assertTrue(result.hasError());
        assertEquals("Role ARN is not found in credential request.", result.getErrors().get(0));
    }

    @Test
    void testValidateAwsCredentialRequestNoArn() {
        CredentialRequest request = new CredentialRequest();
        request.setCloudPlatform("AWS");
        AwsCredentialParameters aws = new AwsCredentialParameters();
        aws.setRoleBased(new RoleBasedParameters());
        request.setAws(aws);
        ValidationResult result = underTest.validateAwsCredentialRequest(request);
        assertTrue(result.hasError());
        assertEquals("Role ARN is not found in credential request.", result.getErrors().get(0));
    }

    @Test
    void testValidateAwsCredentialRequestValid() {
        CredentialRequest request = new CredentialRequest();
        request.setCloudPlatform("AWS");
        AwsCredentialParameters aws = new AwsCredentialParameters();
        RoleBasedParameters roleBased = new RoleBasedParameters();
        roleBased.setRoleArn("arn");
        aws.setRoleBased(roleBased);
        request.setAws(aws);
        ValidationResult result = underTest.validateAwsCredentialRequest(request);
        assertFalse(result.hasError());
    }

    @Test
    void testGetValidPlatformsForAccountIdWhenAllEnabledAndEnvironmentCredential() {
        Set<String> result = underTest.getValidPlatformsForAccountId(ACCOUNT_ID, ENVIRONMENT);
        assertEquals(3, result.size());
        assertTrue(result.containsAll(Arrays.asList(AWS, AZURE, GCP)));
    }
}
