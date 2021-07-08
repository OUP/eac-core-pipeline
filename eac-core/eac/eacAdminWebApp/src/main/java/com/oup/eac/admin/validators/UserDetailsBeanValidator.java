package com.oup.eac.admin.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.oup.eac.admin.beans.UserDetailsBean;
import com.oup.eac.service.UserDetailsReportService;

@Component
public class UserDetailsBeanValidator implements Validator {
	
	private UserDetailsReportService userDetailsReportService;

	@Override
	public void validate(final Object target, final Errors errors) {
		UserDetailsBean userDetailsBean = (UserDetailsBean) target;
		validateUserDetailsBean(userDetailsBean, errors);
	}
	
	private void validateUserDetailsBean(UserDetailsBean userDetailsBean, Errors errors) {
		
		String searchUserName = userDetailsBean.getSearchUserName();
		if (searchUserName == null || searchUserName.isEmpty()){
			errors.rejectValue("", "error.userNameRequired");
		}
	}

	@Override
	public boolean supports(Class<?> clazz) {
		final boolean supports = UserDetailsBean.class.isAssignableFrom(clazz);
		return supports;
	}
	
}

