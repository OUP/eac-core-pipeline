package com.oup.eac.admin.validators;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.oup.eac.admin.beans.AccountBean;
import com.oup.eac.common.utils.email.InternationalEmailAddress;
import com.oup.eac.common.utils.username.UsernameValidator;
import com.oup.eac.domain.AdminUser;
import com.oup.eac.service.AdminService;

@Component("accountBeanValidator")
public class AccountBeanValidator {

	private final AdminService adminService;
	private final UsernameValidator usernameValidator;

	@Autowired
	public AccountBeanValidator(final AdminService adminService, final UsernameValidator usernameValidator) {
		this.adminService = adminService;
		this.usernameValidator = usernameValidator;
	}

	public void validate(final Object target, final Errors errors) {
		AccountBean accountBean = (AccountBean) target;
		
		validateUsername(accountBean, errors);
		validateFirstName(accountBean, errors);
		validateFamilyName(accountBean, errors);
		validateEmail(accountBean, errors);
		
		if (accountBean.isChangePassword()) {
			validatePassword(accountBean, errors);
		}
	}
	
	private void validateUsername(final AccountBean accountBean, final Errors errors) {
		if (!usernameValidator.isValid(accountBean.getSelectedAdminUser().getUsername())) {
			errors.rejectValue("selectedAdminUser.username", "error.username.invalid");
		}
		
		AdminUser existingUser = adminService.getAdminUserByUsername(accountBean.getSelectedAdminUser().getUsername());
		
		if(existingUser == null){
            existingUser = adminService.getAdminUserByUsernameUnInitialised(accountBean.getSelectedAdminUser().getUsername());
        }
		
		if (existingUser != null && !StringUtils.equals(existingUser.getId(), accountBean.getSelectedAdminUser().getId())) {
			errors.rejectValue("selectedAdminUser.username", "error.username.taken");
		}
	}
	
	private void validateFirstName(final AccountBean accountBean, final Errors errors) {
		if (StringUtils.isBlank(accountBean.getSelectedAdminUser().getFirstName())) {
			errors.rejectValue("selectedAdminUser.firstName", "error.firstName.empty");
		}
	}
	
	private void validateFamilyName(final AccountBean accountBean, final Errors errors) {
		if (StringUtils.isBlank(accountBean.getSelectedAdminUser().getFamilyName())) {
			errors.rejectValue("selectedAdminUser.familyName", "error.familyName.empty");
		}
	}
	
	private void validateEmail(final AccountBean accountBean, final Errors errors) {
		String email = accountBean.getSelectedAdminUser().getEmailAddress();
		if (StringUtils.isBlank(email) || !InternationalEmailAddress.isValid(email)) {
			errors.rejectValue("selectedAdminUser.emailAddress", "error.invalidEmail");
		}
	}

	private void validatePassword(final AccountBean accountBean, final Errors errors) {
		PasswordValidatorHelper.validate(accountBean.getPassword(), accountBean.getConfirmPassword(), errors);
	}
	
	
}
