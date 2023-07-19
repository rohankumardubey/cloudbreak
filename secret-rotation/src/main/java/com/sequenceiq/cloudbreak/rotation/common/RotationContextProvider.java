package com.sequenceiq.cloudbreak.rotation.common;

import static com.sequenceiq.cloudbreak.rotation.CommonSecretRotationStep.VAULT;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.sequenceiq.cloudbreak.rotation.MultiSecretType;
import com.sequenceiq.cloudbreak.rotation.SecretRotationStep;
import com.sequenceiq.cloudbreak.rotation.SecretType;
import com.sequenceiq.cloudbreak.rotation.secret.vault.VaultRotationContext;

public interface RotationContextProvider {

    <C extends RotationContext> Map<SecretRotationStep, C> getContexts(String resourceCrn);

    SecretType getSecret();

    default Optional<MultiSecretType> getMultiSecret() {
        if (getSecret().multiSecret()) {
            throw new SecretRotationException("Multi secret type should be provided!", null);
        }
        return Optional.empty();
    }

    default <C extends RotationContext> Set<String> getVaultSecretsForRollback(String resourceCrn, Map<SecretRotationStep, C> contexts) {
        if (getSecret().getSteps().contains(VAULT)) {
            return ((VaultRotationContext) contexts.get(VAULT)).getVaultPathSecretMap().keySet();
        } else {
            return getVaultSecretsForRollback(resourceCrn);
        }
    }

    default Set<String> getVaultSecretsForRollback(String resourceCrn) {
        return Set.of();
    }
}
