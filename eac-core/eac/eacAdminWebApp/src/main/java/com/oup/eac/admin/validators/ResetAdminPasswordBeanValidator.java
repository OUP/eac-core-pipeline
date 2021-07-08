package com.oup.eac.admin.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.oup.eac.admin.beans.ResetAdminPasswordBean;

@Component
public class ResetAdminPasswordBeanValidator implements Validator {

	@Override
	public void validate(final Object target, final Errors errors) {
		ResetAdminPasswordBean bean = (ResetAdminPasswordBean) target;
		PasswordValidatorHelper.validate(bean.getPassword(), bean.getConfirmPassword(), errors);
	}
	
	@Override
	public boolean supports(final Class<?> clazz) {
		final boolean supports = ResetAdminPasswordBean.class.isAssignableFrom(clazz);
		return supports;
	}

}
