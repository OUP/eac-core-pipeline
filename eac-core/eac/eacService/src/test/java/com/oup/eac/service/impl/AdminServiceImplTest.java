package com.oup.eac.service.impl;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import javax.naming.NamingException;

import org.apache.velocity.app.VelocityEngine;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.MessageSource;

import com.oup.eac.data.AdminUserDao;
import com.oup.eac.data.CustomerDao;
import com.oup.eac.domain.AdminUser;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.Password;
import com.oup.eac.service.AdminService;
import com.oup.eac.service.EmailService;
import com.oup.eac.service.exceptions.UsernameExistsException;

public class AdminServiceImplTest /* extends AbstractMockTest */ {
	/*
	 * 
	 * private AdminService adminService;
	 * 
	 * private AdminUserDao mockAdminServiceDao; private CustomerDao
	 * mockCustomerDao; private VelocityEngine mockVelocityEngine; private
	 * EmailService mockEmailService; private MessageSource mockMessageSource;
	 * 
	 * public AdminServiceImplTest() throws NamingException { super(); }
	 * 
	 *//**
		 * @throws Exception Sets up data and create mocks ready for testing.
		 */
	/*
	 * @Before public final void setUp() throws Exception { mockAdminServiceDao =
	 * EasyMock.createMock(AdminUserDao.class); mockCustomerDao =
	 * EasyMock.createMock(CustomerDao.class); mockVelocityEngine =
	 * EasyMock.createMock(VelocityEngine.class); mockEmailService =
	 * EasyMock.createMock(EmailService.class); adminService = new
	 * AdminServiceImpl(mockAdminServiceDao, mockCustomerDao, mockVelocityEngine,
	 * mockEmailService, mockMessageSource); setMocks(mockAdminServiceDao,
	 * mockCustomerDao); }
	 * 
	 * @Test public void getAdminUserByUsername() { AdminUser adminUser = new
	 * AdminUser(); adminUser.setUsername("admin"); adminUser.setPassword(new
	 * Password("Passw0rd", false));
	 * expect(mockAdminServiceDao.getAdminUserByUsername("admin")).andReturn(
	 * adminUser); replay(getMocks()); AdminUser returnedAdminUser =
	 * adminService.getAdminUserByUsername("admin"); verify(getMocks());
	 * assertEquals(adminUser, returnedAdminUser); }
	 * 
	 * @Test(expected = UsernameExistsException.class) public void
	 * shouldThrowExceptionWhenUsernameInUseByExistingAdminUser() throws
	 * UsernameExistsException { AdminUser existingAdminUser = new AdminUser();
	 * existingAdminUser.setUsername("a_user"); existingAdminUser.setId("12345");
	 * EasyMock.expect(mockAdminServiceDao.getAdminUserByUsername("a_user")).
	 * andReturn(existingAdminUser);
	 * EasyMock.expect(adminService.getAdminUserByUsernameUnInitialised("a_user")).
	 * andReturn(existingAdminUser);
	 * EasyMock.expect(mockCustomerDao.getCustomerByUsername("a_user")).andReturn(
	 * null); replay(getMocks());
	 * 
	 * AdminUser newAdminUser = new AdminUser(); newAdminUser.setUsername("a_user");
	 * newAdminUser.setId("54321");
	 * 
	 * adminService.saveAdminUser(newAdminUser); }
	 * 
	 * @Test(expected = UsernameExistsException.class) public void
	 * shouldThrowExceptionWhenUsernameInUseByExistingCustomer() throws
	 * UsernameExistsException { Customer existingCustomer = new Customer();
	 * existingCustomer.setUsername("a_user"); existingCustomer.setId("12345");
	 * EasyMock.expect(mockAdminServiceDao.getAdminUserByUsername("a_user")).
	 * andReturn(null);
	 * EasyMock.expect(mockAdminServiceDao.getAdminUserByUsernameUnInitialised(
	 * "a_user")).andReturn(null);
	 * EasyMock.expect(mockCustomerDao.getCustomerByUsername("a_user")).andReturn(
	 * existingCustomer); replay(getMocks());
	 * 
	 * AdminUser newAdminUser = new AdminUser(); newAdminUser.setUsername("a_user");
	 * newAdminUser.setId("54321");
	 * 
	 * adminService.saveAdminUser(newAdminUser); }
	 */}
