package com.oup.eac.accounts.controllers;

import java.util.Locale;

import javax.naming.NamingException;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import com.oup.eac.accounts.validators.AccountsResetPasswordFormValidator;
import com.oup.eac.domain.Customer;
import com.oup.eac.dto.PasswordResetDto;
import com.oup.eac.service.CustomerService;
import com.oup.eac.service.ServiceLayerException;
import com.oup.eac.service.exceptions.PasswordPolicyViolatedServiceLayerException;
import com.oup.eac.web.controllers.helpers.SessionHelper;

public class AccountsPasswordResetControllerMockTest /* extends AbstractMockTest */ {
	/*
	 * 
	 * private CustomerService customerServiceMock; private
	 * AccountsResetPasswordFormValidator resetPasswordFormValidatorMock; private
	 * MockHttpServletRequest request; private MockHttpServletResponse response;
	 * private AccountsPasswordResetController resetPasswordController; private
	 * PasswordResetDto resetPasswordDto; private static final String USERNAME =
	 * "username"; private BindingResult bindingResult; private Customer customer;
	 * private Locale locale; private final String PASSWORD_RESET_SUCCESS =
	 * "passwordResetSuccess";
	 * 
	 * public AccountsPasswordResetControllerMockTest() throws NamingException {
	 * super(); }
	 * 
	 * @Before public void setup(){ request = new MockHttpServletRequest(); response
	 * = new MockHttpServletResponse(); customerServiceMock =
	 * EasyMock.createMock(CustomerService.class); resetPasswordFormValidatorMock =
	 * EasyMock.createMock(AccountsResetPasswordFormValidator.class);
	 * setMocks(customerServiceMock, resetPasswordFormValidatorMock);
	 * resetPasswordController = new
	 * AccountsPasswordResetController(customerServiceMock,
	 * resetPasswordFormValidatorMock); resetPasswordDto = new PasswordResetDto();
	 * resetPasswordDto.setUsername(USERNAME); bindingResult = new
	 * BindException(resetPasswordDto, "passwordResetDto"); customer = new
	 * Customer(); customer.setUsername(USERNAME); locale = Locale.US;
	 * customer.setLocale(locale);
	 * 
	 * }
	 * 
	 * @Test public void testResetPasswordSuccess() throws
	 * Exception,ServiceLayerException, PasswordPolicyViolatedServiceLayerException
	 * { EasyMock.expect(customerServiceMock.getCustomerByUsername(USERNAME)).
	 * andReturn(this.customer);
	 * 
	 * final String baseurl="null/eacAccounts/";
	 * 
	 * customerServiceMock.updateResetCustomerPassword(this.customer,
	 * SessionHelper.getLocale(request),baseurl, null); EasyMock.expectLastCall();
	 * replayMocks();
	 * 
	 * ModelAndView result = resetPasswordController.resetPassword(resetPasswordDto,
	 * bindingResult, request, response); Assert.assertNotNull(result); String
	 * viewName = result.getViewName(); Assert.assertEquals(PASSWORD_RESET_SUCCESS,
	 * viewName); verifyMocks(); }
	 */}

