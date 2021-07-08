package com.oup.eac.web.validators.profile;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import com.oup.eac.common.utils.email.InternationalEmailAddress;
import com.oup.eac.common.utils.username.UsernameValidator;
import com.oup.eac.dto.BasicProfileDto;
import com.oup.eac.web.validators.EACValidator;

/**
 * Validator for Basic Profile page.
 * 
 * @author David Hay
 * 
 * @see com.oup.eac.dto.BasicProfileDto
 * @see com.oup.eac.web.controllers.profile.BasicProfileController
 * 
 */
@Component
public class BasicProfileValidator extends EACValidator {

    @SuppressWarnings("unused")
    private static final String FIELD_TIMEZONE = "timezone";
    @SuppressWarnings("unused")
    private static final String FIELD_USER_LOCALE = "userLocale";
    @SuppressWarnings("unused")
    private static final String FIELD_FIRST_NAME = "firstName";
    @SuppressWarnings("unused")
    private static final String FIELD_FAMILY_NAME = "familyName";
    private static final String FIELD_USERNAME = "username";
    private static final String FIELD_EMAIL = "email";

    @SuppressWarnings("unused")
    private static final String LABEL_TIMEZONE = "label.timezone";
    @SuppressWarnings("unused")
    private static final String LABEL_LOCALE = "label.locale";
    @SuppressWarnings("unused")
    private static final String LABEL_FIRSTNAME = "label.firstname";
    @SuppressWarnings("unused")
    private static final String LABEL_FAMILYNAME = "label.familyname";
    private static final String LABEL_USERNAME = "label.username";
    private static final String LABEL_EMAIL = "label.email";

    private static final String ERROR_NOT_SPECIFIED = "error.not-specified";
    private static final String ERROR_MUST_BE_VALID_USERNAME = "error.must.be.valid.username";
    private static final String ERROR_MUST_BE_VALID_EMAIL = "error.must.be.valid.email";
    @SuppressWarnings("unused")
    private static final String ERROR_MUST_BE_VALID_TIMEZONE = "error.must.be.valid.timezone";

    private final UsernameValidator usernameValidator;

    private static final Logger LOG = Logger.getLogger(BasicProfileValidator.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean supports(final Class<?> clazz) {
        return BasicProfileDto.class.isAssignableFrom(clazz);
    }

    /**
     * Instantiates a new basic profile validator.
     * 
     * @param usernameValidatorP
     *            the username validator p
     */
    @Autowired
    public BasicProfileValidator(final UsernameValidator usernameValidatorP) {
        this.usernameValidator = usernameValidatorP;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void validate(final Object target, final Errors errors) {
        BasicProfileDto dto = (BasicProfileDto) target;
        ErrorContext ctx = new ErrorContext(errors);
        
        String email = dto.getEmail();
        if (StringUtils.isBlank(email)) {
            ctx.rejectValue(FIELD_EMAIL, ERROR_NOT_SPECIFIED, "Email is required.", LABEL_EMAIL);
        } else {
            if (!InternationalEmailAddress.isValid(email)) {
                ctx.rejectValue(FIELD_EMAIL, ERROR_MUST_BE_VALID_EMAIL, "Email must be a valid email address.", LABEL_EMAIL);
            }
        }

        String username = dto.getUsername();
        if (StringUtils.isBlank(username)) {
            ctx.rejectValue(FIELD_USERNAME, ERROR_NOT_SPECIFIED, "Username is required.", LABEL_USERNAME);
        } else {
            boolean invalidUserName = !usernameValidator.isValid(username);
            if (invalidUserName) {
                ctx.rejectValue(FIELD_USERNAME, ERROR_MUST_BE_VALID_USERNAME, "Username must be valid.", LABEL_USERNAME, username);
            }
        }

        if (LOG.isDebugEnabled()) {
            String msg = String.format("%d errors", errors.getErrorCount());
            LOG.debug(msg);
        }

    }

    public static class ErrorContext {
        private final Errors errors;

        /**
         * Instantiates a new error context.
         *
         * @param errorsP the errors p
         */
        public ErrorContext(final Errors errorsP) {
            this.errors = errorsP;
        }

        /**
         * Gets the errors.
         *
         * @return the errors
         */
        public final Errors getErrors() {
            return errors;
        }

        /**
         * Reject value.
         *
         * @param field the field
         * @param errorCode the error code
         * @param defaultMessage the default message
         * @param errorArgs the error args
         */
        public final void rejectValue(final String field, final String errorCode, final String defaultMessage, final Object... errorArgs) {
            errors.rejectValue(field, errorCode, errorArgs, defaultMessage);
        }
    }
}
