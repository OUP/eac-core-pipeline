package com.oup.eac.admin.validators;

import java.util.Collections;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.springframework.validation.Errors;

import com.oup.eac.admin.beans.AccountBean;
import com.oup.eac.common.utils.username.UsernameValidator;
import com.oup.eac.domain.AdminUser;
import com.oup.eac.domain.Division;
import com.oup.eac.domain.Role;
import com.oup.eac.service.AdminService;

public class AccountBeanValidatorTest {
	
	private AdminService mockAdminService;
	private UsernameValidator mockUsernameValidator;

	private AccountBeanValidator validator;
	
	@Before
	public void setUp() {
		mockAdminService = EasyMock.createMock(AdminService.class);
		mockUsernameValidator = EasyMock.createMock(UsernameValidator.class);
		validator = new AccountBeanValidator(mockAdminService, mockUsernameValidator);
	}
	
	@Test
	public void shouldFailWhenInvalidUsername() {
		String emptyUsername = "";
		AdminUser adminUser = new AdminUser();
		adminUser.setUsername(emptyUsername);
		adminUser.setFirstName("first_name");
		adminUser.setFamilyName("family_name");
		adminUser.setEmailAddress("somebody@somewhere.com");
		AccountBean accountBean = new AccountBean(Collections.<AdminUser>emptyList(), Collections.<Division>emptyList(), Collections.<Role>emptyList());
		accountBean.setSelectedAdminUser(adminUser);
		
		EasyMock.expect(mockUsernameValidator.isValid(emptyUsername)).andReturn(false);
		EasyMock.expect(mockAdminService.getAdminUserByUsername(emptyUsername)).andReturn(null);
        EasyMock.expect(mockAdminService.getAdminUserByUsernameUnInitialised(emptyUsername)).andReturn(null);
		EasyMock.replay(mockUsernameValidator, mockAdminService);
		
		Errors mockErrors = EasyMock.createMock(Errors.class);
		mockErrors.rejectValue("selectedAdminUser.username", "error.username.invalid");

		EasyMock.replay(mockErrors);

		validator.validate(accountBean, mockErrors);

		EasyMock.verify(mockErrors);
	}
	
	@Test
	public void shouldFailWhenUsernameAlreadyExists() {
		String existingUsername = "a_user";
		AdminUser adminUser = new AdminUser();
		adminUser.setUsername(existingUsername);
		adminUser.setFirstName("first_name");
		adminUser.setFamilyName("family_name");
		adminUser.setEmailAddress("somebody@somewhere.com");
		adminUser.setId("12345");
		AccountBean accountBean = new AccountBean(Collections.<AdminUser>emptyList(), Collections.<Division>emptyList(), Collections.<Role>emptyList());
		accountBean.setSelectedAdminUser(adminUser);
		
		AdminUser existingUser = new AdminUser();
		existingUser.setUsername(existingUsername);
		existingUser.setId("54321");
		
		EasyMock.expect(mockUsernameValidator.isValid(existingUsername)).andReturn(true);
		EasyMock.expect(mockAdminService.getAdminUserByUsername(existingUsername)).andReturn(existingUser);
		EasyMock.expect(mockAdminService.getAdminUserByUsernameUnInitialised(existingUsername)).andReturn(existingUser);
		EasyMock.replay(mockUsernameValidator, mockAdminService);
		
		Errors mockErrors = EasyMock.createMock(Errors.class);
		mockErrors.rejectValue("selectedAdminUser.username", "error.username.taken");

		EasyMock.replay(mockErrors);

		validator.validate(accountBean, mockErrors);

		EasyMock.verify(mockErrors);
	}
	
	@Test
	public void shouldNotFailWhenPasswordEmptyAndNotChangePassword() {
		String username = "a_user";
		AdminUser adminUser = new AdminUser();
		adminUser.setUsername(username);
		adminUser.setFirstName("first_name");
		adminUser.setFamilyName("family_name");
		adminUser.setEmailAddress("somebody@somewhere.com");
		AccountBean accountBean = new AccountBean(Collections.<AdminUser>emptyList(), Collections.<Division>emptyList(), Collections.<Role>emptyList());
		accountBean.setSelectedAdminUser(adminUser);
		accountBean.setChangePassword(false);
		
		EasyMock.expect(mockUsernameValidator.isValid(username)).andReturn(true);
		EasyMock.expect(mockAdminService.getAdminUserByUsername(username)).andReturn(null);
        EasyMock.expect(mockAdminService.getAdminUserByUsernameUnInitialised(username)).andReturn(null);
        EasyMock.replay(mockUsernameValidator, mockAdminService);		
		
		Errors mockErrors = EasyMock.createMock(Errors.class);

		EasyMock.replay(mockErrors);

		validator.validate(accountBean, mockErrors);

		EasyMock.verify(mockErrors);
	}
	
	@Test
	public void shouldFailWhenFirstNameEmpty() {
		String username = "a_user";
		AdminUser adminUser = new AdminUser();
		adminUser.setUsername(username);
		adminUser.setFirstName("");
		adminUser.setFamilyName("family_name");
		adminUser.setEmailAddress("somebody@somewhere.com");
		AccountBean accountBean = new AccountBean(Collections.<AdminUser>emptyList(), Collections.<Division>emptyList(), Collections.<Role>emptyList());
		accountBean.setSelectedAdminUser(adminUser);
		
		EasyMock.expect(mockUsernameValidator.isValid(username)).andReturn(true);
        EasyMock.expect(mockAdminService.getAdminUserByUsername(username)).andReturn(null);
        EasyMock.expect(mockAdminService.getAdminUserByUsernameUnInitialised(username)).andReturn(null);
        EasyMock.replay(mockUsernameValidator, mockAdminService);
		
		Errors mockErrors = EasyMock.createMock(Errors.class);
		mockErrors.rejectValue("selectedAdminUser.firstName", "error.firstName.empty");

		EasyMock.replay(mockErrors);

		validator.validate(accountBean, mockErrors);

		EasyMock.verify(mockErrors);
	}
	
	@Test
	public void shouldFailWhenFamilyNameEmpty() {
		String username = "a_user";
		AdminUser adminUser = new AdminUser();
		adminUser.setUsername(username);
		adminUser.setFirstName("first_name");
		adminUser.setFamilyName("");
		adminUser.setEmailAddress("somebody@somewhere.com");
		AccountBean accountBean = new AccountBean(Collections.<AdminUser>emptyList(), Collections.<Division>emptyList(), Collections.<Role>emptyList());
		accountBean.setSelectedAdminUser(adminUser);
		
		EasyMock.expect(mockUsernameValidator.isValid(username)).andReturn(true);
        EasyMock.expect(mockAdminService.getAdminUserByUsername(username)).andReturn(null);
        EasyMock.expect(mockAdminService.getAdminUserByUsernameUnInitialised(username)).andReturn(null);
        EasyMock.replay(mockUsernameValidator, mockAdminService);	
		
		
		Errors mockErrors = EasyMock.createMock(Errors.class);
		mockErrors.rejectValue("selectedAdminUser.familyName", "error.familyName.empty");
		
		EasyMock.replay(mockErrors);
		
		validator.validate(accountBean, mockErrors);
		
		EasyMock.verify(mockErrors);
	}
	
	@Test
	public void shouldFailWhenInvalidEmail() {
		String username = "a_user";
		AdminUser adminUser = new AdminUser();
		adminUser.setUsername(username);
		adminUser.setFirstName("first_name");
		adminUser.setFamilyName("family_name");
		adminUser.setEmailAddress("xyz");
		AccountBean accountBean = new AccountBean(Collections.<AdminUser>emptyList(), Collections.<Division>emptyList(), Collections.<Role>emptyList());
		accountBean.setSelectedAdminUser(adminUser);
		
		EasyMock.expect(mockUsernameValidator.isValid(username)).andReturn(true);
        EasyMock.expect(mockAdminService.getAdminUserByUsername(username)).andReturn(null);
        EasyMock.expect(mockAdminService.getAdminUserByUsernameUnInitialised(username)).andReturn(null);
        EasyMock.replay(mockUsernameValidator, mockAdminService);
		
		Errors mockErrors = EasyMock.createMock(Errors.class);
		mockErrors.rejectValue("selectedAdminUser.emailAddress", "error.invalidEmail");
		
		EasyMock.replay(mockErrors);
		
		validator.validate(accountBean, mockErrors);
		
		EasyMock.verify(mockErrors);
	}
	
	@Test
	public void shouldFailWhenEmptyEmail() {
		String username = "a_user";
		AdminUser adminUser = new AdminUser();
		adminUser.setUsername(username);
		adminUser.setFirstName("first_name");
		adminUser.setFamilyName("family_name");
		AccountBean accountBean = new AccountBean(Collections.<AdminUser>emptyList(), Collections.<Division>emptyList(), Collections.<Role>emptyList());
		accountBean.setSelectedAdminUser(adminUser);
		
		EasyMock.expect(mockUsernameValidator.isValid(username)).andReturn(true);
        EasyMock.expect(mockAdminService.getAdminUserByUsername(username)).andReturn(null);
        EasyMock.expect(mockAdminService.getAdminUserByUsernameUnInitialised(username)).andReturn(null);
        EasyMock.replay(mockUsernameValidator, mockAdminService);
		
		Errors mockErrors = EasyMock.createMock(Errors.class);
		mockErrors.rejectValue("selectedAdminUser.emailAddress", "error.invalidEmail");
		
		EasyMock.replay(mockErrors);
		
		validator.validate(accountBean, mockErrors);
		
		EasyMock.verify(mockErrors);
	}
}
