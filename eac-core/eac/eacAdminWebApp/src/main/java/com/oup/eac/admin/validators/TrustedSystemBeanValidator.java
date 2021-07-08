package com.oup.eac.admin.validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.oup.eac.admin.beans.TrustedSystemBean;

@Component
public class TrustedSystemBeanValidator implements Validator {
	

	@Override
	public void validate(final Object target, final Errors errors) {
		TrustedSystemBean trustedSystemBean = (TrustedSystemBean) target;
		
		if (TrustedSystemBean.NEW.equals(trustedSystemBean.getSelectedTrustedSystemId())) {
			validateNewTrustedSystem(trustedSystemBean, errors);
		}
		else{
			validateUpdateTrustedSystem(trustedSystemBean, errors);
		}
	}
	
	private void validateNewTrustedSystem(TrustedSystemBean trustedSystemBean, Errors errors) {
		boolean nullFileds = false ;
		String newUserName = trustedSystemBean.getNewUserName();
		String newPassword = trustedSystemBean.getNewPassword();
		String newConfirnPassword = trustedSystemBean.getNewConfirmPassword() ;
		if (newUserName == null){
			errors.rejectValue("", "error.trustedSystemUserNameRequired");
			nullFileds =true ;
		}
		if (newPassword == null){
			errors.rejectValue("", "error.trustedSystemNewPasswordRequired");
			nullFileds =true ;
		}
		if (newConfirnPassword == null){
			errors.rejectValue("", "error.trustedSystemNewConfirmPasswordRequired");
			nullFileds =true ;
		}
		if (newPassword != null && newConfirnPassword != null && !nullFileds ) {
			if (!newPassword.equals(newConfirnPassword)){
				errors.rejectValue("", "error.trustedSystemPasswordNotMatched");
			} else if (!validatePassword(newPassword)){
				errors.rejectValue("", "error.trustedSystemPasswordInvalid");
			}
		}
	}
	private boolean validatePassword(String password){
		String pattern = "^(?=.*[a-z])(?=.*[A-Z])\\S{6,}$" ;
		Pattern p = Pattern.compile(pattern);  
		Matcher m = p.matcher(password); 
		return m.matches() ;
	}
	private void validateUpdateTrustedSystem(TrustedSystemBean trustedSystemBean, Errors errors) {
		boolean nullFileds = false ;
		String newUserName = trustedSystemBean.getNewUserName();
		String newPassword = trustedSystemBean.getNewPassword();
		String newConfirnPassword = trustedSystemBean.getNewConfirmPassword() ;
		if (newUserName == null){
			errors.rejectValue("", "error.trustedSystemUserNameRequired");
			nullFileds =true ;
		}
		if (newPassword == null){
			errors.rejectValue("", "error.trustedSystemNewPasswordRequired");
			nullFileds =true ;
		}
		if (newConfirnPassword == null){
			errors.rejectValue("", "error.trustedSystemNewConfirmPasswordRequired");
			nullFileds =true ;
		}
		if (newPassword != null && newConfirnPassword != null && !nullFileds) {
			if (!newPassword.equals(newConfirnPassword)){
				errors.rejectValue("", "error.trustedSystemPasswordNotMatched");
			} else if (!validatePassword(newPassword)){
				errors.rejectValue("", "error.trustedSystemPasswordInvalid");
			}
		}
	}

	@Override
	public boolean supports(final Class<?> clazz) {
		final boolean supports = TrustedSystemBean.class.isAssignableFrom(clazz);
		return supports;
	}
}
