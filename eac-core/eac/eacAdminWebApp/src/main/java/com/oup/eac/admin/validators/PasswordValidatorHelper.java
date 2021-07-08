package com.oup.eac.admin.validators;

import org.apache.commons.lang.StringUtils;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.validation.Errors;

import com.oup.eac.common.utils.crypto.PasswordUtils;

public final class PasswordValidatorHelper {
	
	private PasswordValidatorHelper() {
	}
	
	public static void validate(final String password, final String confirmPassword, final MessageContext context) {
		String error = validate(password, confirmPassword);
		if (error != null) {
			context.addMessage(new MessageBuilder().error().code(error).build());
		}
	}
	
	public static void validate(final String password, final String confirmPassword, final Errors errors) {
		String error = validate(password, confirmPassword);
		if (error != null) {
			errors.reject(error);
		}
	}
	
	private static String validate(final String password, final String confirmPassword) {
		if (StringUtils.isBlank(password)) {
			return "error.password.required";
		}
		if (StringUtils.isBlank(confirmPassword)) {
			return "error.password.again.required";
		}
		if (!StringUtils.equals(password, confirmPassword)) {
			return "error.passwords.dont.match";
		}
		if (!PasswordUtils.isPasswordValid(password)) {
			return PasswordUtils.INVALID_PASSWORD_MSG_CODE;
		}
		return null;
	}

}
