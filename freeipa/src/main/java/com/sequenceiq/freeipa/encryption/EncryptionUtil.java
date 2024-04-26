package com.sequenceiq.freeipa.encryption;

import jakarta.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.sequenceiq.cloudbreak.cloud.CloudConnector;
import com.sequenceiq.cloudbreak.cloud.CryptoConnector;
import com.sequenceiq.cloudbreak.cloud.exception.CloudConnectorException;
import com.sequenceiq.cloudbreak.cloud.init.CloudPlatformConnectors;
import com.sequenceiq.cloudbreak.cloud.model.CloudCredential;
import com.sequenceiq.cloudbreak.cloud.model.Platform;
import com.sequenceiq.cloudbreak.cloud.model.Variant;
import com.sequenceiq.cloudbreak.cloud.model.encryption.EncryptRequest;
import com.sequenceiq.cloudbreak.cloud.model.encryption.EncryptionKeySource;
import com.sequenceiq.cloudbreak.cloud.model.encryption.EncryptionKeyType;
import com.sequenceiq.cloudbreak.common.mappable.CloudPlatform;
import com.sequenceiq.cloudbreak.service.secret.model.SecretResponse;
import com.sequenceiq.cloudbreak.service.secret.service.SecretService;
import com.sequenceiq.environment.api.v1.credential.model.response.CredentialResponse;
import com.sequenceiq.environment.api.v1.environment.model.response.DetailedEnvironmentResponse;
import com.sequenceiq.freeipa.converter.cloud.CredentialToCloudCredentialConverter;
import com.sequenceiq.freeipa.dto.Credential;

@Component
public class EncryptionUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(EncryptionUtil.class);

    @Inject
    private CloudPlatformConnectors cloudPlatformConnectors;

    @Inject
    private CredentialToCloudCredentialConverter cloudCredentialConverter;

    @Inject
    private SecretService secretService;

    public String encrypt(CloudPlatform cloudPlatform, String input, DetailedEnvironmentResponse environment, String secretName,
            EncryptionKeySource secretEncryptionKeySource) {
        CloudConnector cloudConnector = cloudPlatformConnectors.get(Platform.platform(cloudPlatform.name()), Variant.variant(cloudPlatform.name()));
        CryptoConnector cryptoConnector = cloudConnector.cryptoConnector();
        CloudCredential cloudCredential = cloudCredentialConverter.convert(convertToCredential(environment.getCredential()));
        EncryptRequest parameters = EncryptRequest.builder()
                .withInput(input)
                .withKeySource(secretEncryptionKeySource)
                .withCloudCredential(cloudCredential)
                .withRegionName(environment.getLocation().getName())
                .withEnvironmentCrn(environment.getCrn())
                .withSecretName(secretName)
                .build();
        return cryptoConnector.encrypt(parameters);
    }

    public EncryptionKeySource getEncryptionKeySource(CloudPlatform cloudPlatform, String luksKmsKey) {
        return switch (cloudPlatform) {
            case AWS -> EncryptionKeySource.builder()
                    .withKeyType(EncryptionKeyType.AWS_KMS_KEY_ARN)
                    .withKeyValue(luksKmsKey)
                    .build();
            default -> throw new CloudConnectorException(String.format("Couldn't specify secret encryption key source for cloud platform %s.", cloudPlatform));
        };
    }

    private Credential convertToCredential(CredentialResponse credentialResponse) {
        SecretResponse secretResponse = credentialResponse.getAttributes();
        String attributes = secretService.getByResponse(secretResponse);
        return new Credential(
                credentialResponse.getCloudPlatform(),
                credentialResponse.getName(),
                attributes,
                credentialResponse.getCrn(),
                credentialResponse.getAccountId());
    }
}
