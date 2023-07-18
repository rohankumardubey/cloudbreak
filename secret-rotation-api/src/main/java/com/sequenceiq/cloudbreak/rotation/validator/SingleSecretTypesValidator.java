package com.sequenceiq.cloudbreak.rotation.validator;

import java.util.Collections;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.sequenceiq.cloudbreak.rotation.SecretType;
import com.sequenceiq.cloudbreak.rotation.SecretTypeConverter;
import com.sequenceiq.cloudbreak.rotation.annotation.OnlySingleSecretTypes;
import com.sequenceiq.common.api.util.ValidatorUtil;

public class SingleSecretTypesValidator implements ConstraintValidator<OnlySingleSecretTypes, List<String>> {

    @Override
    public boolean isValid(List<String> secrets, ConstraintValidatorContext context) {
        if (secrets.stream().anyMatch(secret -> Collections.frequency(secrets, secret) > 1)) {
            ValidatorUtil.addConstraintViolation(context, "There is at least one duplication in the request!");
            return false;
        }
        try {
            List<SecretType> secretTypes = SecretTypeConverter.mapSecretTypes(secrets);
            if (secretTypes.stream().anyMatch(SecretType::multiSecret)) {
                ValidatorUtil.addConstraintViolation(context, "Only single secret types allowed!");
                return false;
            }
        } catch (Exception e) {
            ValidatorUtil.addConstraintViolation(context, e.getMessage());
            return false;
        }
        return true;
    }
}
