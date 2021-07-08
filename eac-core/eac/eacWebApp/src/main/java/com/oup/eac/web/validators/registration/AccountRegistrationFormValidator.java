package com.oup.eac.web.validators.registration;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;

import com.oup.eac.common.utils.EACSettings;
import com.oup.eac.common.utils.crypto.PasswordUtils;
import com.oup.eac.common.utils.email.InternationalEmailAddress;
import com.oup.eac.common.utils.username.UsernameValidator;
import com.oup.eac.domain.Customer;
import com.oup.eac.dto.AccountRegistrationDto;
import com.oup.eac.integration.facade.exceptions.ErightsException;
import com.oup.eac.service.CustomerService;

/**
 * @author harlandd Registration form validator
 */
public class AccountRegistrationFormValidator extends RegistrationFormValidator {

	private final CustomerService customerService;
	private final UsernameValidator usernameValidator;

	public AccountRegistrationFormValidator(CustomerService customerService, UsernameValidator usernameValidator) {
		this.customerService = customerService;
		this.usernameValidator = usernameValidator;
	}

	/**
	 * @param clazz
	 *            the class
	 * @return does this validator support this class
	 */
	public final boolean supports(final Class clazz) {
		return AccountRegistrationDto.class.isAssignableFrom(clazz);
	}

	/**
	 * @param obj
	 *            the command object
	 * @param errors
	 *            the errors
	 */
	public final void validate(final Object obj, final Errors errors) {
		AccountRegistrationDto accountRegistrationFbo = (AccountRegistrationDto) obj;
		accountRegistrationFbo.cleanAnswers();
		
		if (StringUtils.isBlank(accountRegistrationFbo.getFirstName())) {
			errors.rejectValue("firstName", "error.not-specified", new Object[] { "title.firstname" }, "First Name is required.");
		}

		if (StringUtils.isBlank(accountRegistrationFbo.getFamilyName())) {
			errors.rejectValue("familyName", "error.not-specified", new Object[] { "title.familyname" }, "Surname is required.");
		}

		validateEmail(accountRegistrationFbo.getEmail(), errors);

		validateUsername(accountRegistrationFbo.getUsername(), errors);

		validatePassword(accountRegistrationFbo.getPassword(), errors);

		validateConfirmPassword(accountRegistrationFbo.getConfirmPassword(), accountRegistrationFbo.getPassword(), errors);

		super.validate(obj, errors);
	}

	private void validateEmail(String email, final Errors errors) {
		if (StringUtils.isBlank(email)) {
			errors.rejectValue("email", "error.not-specified", new Object[] { "title.email" }, "Email address is required.");
		} else if (!InternationalEmailAddress.isValid(email)) {
			errors.rejectValue("email", "error.must.be.valid.email", new Object[] { "title.email" }, "Email address must be a valid email address.");
		}
	}

	private void validateUsername(String username, final Errors errors) {
		if (StringUtils.isBlank(username)) {
			errors.rejectValue("username", "error.not-specified", new Object[] { "title.username" }, "Username is required.");
		} else {
			if (!usernameValidator.isValid(username)) {
				errors.rejectValue("username", "error.must.be.valid.username", new Object[] { "title.username" }, "Username must be valid.");
			} else {
				Customer user = null;
				try {
					user = customerService.getCustomerByUsername(username);
				} catch (ErightsException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (user != null) {
					errors.rejectValue("username", "error.username.taken", new Object[] { EACSettings.getProperty(EACSettings.EAC_LOGIN_URL) },
							"This username is already taken. Please try another.");
				}
			}
		}
	}

	private void validatePassword(String password, final Errors errors) {
		if (StringUtils.isBlank(password)) {
			errors.rejectValue("passwordcheck", "error.not-specified", new Object[] { "title.password" }, "Password is required.");
		} else {
			boolean valid = PasswordUtils.isPasswordValid(password);
			if (!valid) {
				errors.rejectValue("passwordcheck", PasswordUtils.INVALID_PASSWORD_MSG_CODE, new Object[] { "title.password" }, "Password is too easy.");
			}
		}
	}

	private void validateConfirmPassword(String confirmPassword, String password, final Errors errors) {
		if (StringUtils.isBlank(confirmPassword)) {
			errors.rejectValue("confirmPassword", "error.not-specified", new Object[] { "title.confirmpassword" }, "Confirm Password is required.");
		} else if (!confirmPassword.equals(password)) {
			errors.reject("error.must.be.same", new Object[] { "title.password", "title.confirmpassword" }, "Password and "
					+ "Confirm Password must be the same");
		}
	}
}
