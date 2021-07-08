package com.oup.eac.admin.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.oup.eac.domain.LicenceTemplate;
//TODO for example only. remove when required.
public class LicenceTemplateConstraintValidator implements ConstraintValidator<LicenceTemplateConstraint, LicenceTemplate>{

    @Override
    public void initialize(LicenceTemplateConstraint constraintAnnotation) {
    }

    @Override
    public boolean isValid(LicenceTemplate value, ConstraintValidatorContext context) {
        return false;
    }
}
