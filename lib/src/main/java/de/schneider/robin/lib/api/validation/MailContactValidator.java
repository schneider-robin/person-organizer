package de.schneider.robin.lib.api.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MailContactValidator implements ConstraintValidator<MailContactValidation, String> {

    @Override
    public boolean isValid(
            String mail,
            ConstraintValidatorContext context
    ) {
        return mail.contains("@") && mail.contains(".");
    }
}
