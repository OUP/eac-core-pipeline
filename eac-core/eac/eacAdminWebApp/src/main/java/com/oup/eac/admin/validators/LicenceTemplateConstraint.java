package com.oup.eac.admin.validators;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
//TODO for example only. remove when required.
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy=LicenceTemplateConstraintValidator.class)
public @interface LicenceTemplateConstraint {
}
