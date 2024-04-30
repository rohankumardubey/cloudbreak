package com.sequenceiq.cloudbreak.service.secret;

import java.util.List;
import java.util.Map;

import com.sequenceiq.cloudbreak.service.secret.domain.RotationSecret;
import com.sequenceiq.cloudbreak.service.secret.model.SecretResponse;

public interface SecretEngine {

    String appPath();

    String enginePath();

    String put(String key, String value);

    String put(String key, Map<String, String> value);

    String get(String secret, String field);

    /**
     * This is slow, since it can't be cached, always fetch the latest secret without a version information
     */
    String getLatestSecretWithoutCache(String secretPath, String field);

    RotationSecret getRotation(String secret);

    void delete(String secret);

    boolean isSecret(String secret);

    SecretResponse convertToExternal(String secret);

    List<String> listEntries(String secretPathPrefix);

    void cleanup(String path);
}
