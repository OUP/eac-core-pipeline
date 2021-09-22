package com.oup.eac.ws.v2.service.impl;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.eq;

import java.util.Locale;

import javax.naming.NamingException;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import com.oup.eac.common.utils.crypto.PasswordUtils;
import com.oup.eac.common.utils.username.UsernameValidator;
import com.oup.eac.domain.Customer;
import com.oup.eac.dto.WsUserIdDto;
import com.oup.eac.integration.facade.ErightsFacade;
import com.oup.eac.integration.facade.exceptions.ErightsException;
import com.oup.eac.service.CustomerService;
import com.oup.eac.ws.v2.binding.access.CreateUserAccountResponse;
import com.oup.eac.ws.v2.binding.access.UserStatusType;
import com.oup.eac.ws.v2.binding.access.ValidatePasswordCredentialsRequest;
import com.oup.eac.ws.v2.binding.access.ValidatePasswordCredentialsResponse;
import com.oup.eac.ws.v2.binding.access.types.CredentialStatusCode;
import com.oup.eac.ws.v2.binding.common.CreateUser;
import com.oup.eac.ws.v2.binding.common.PasswordCredential;
import com.oup.eac.ws.v2.ex.WebServiceValidationException;

public class ValidatePasswordCredentialsAdapterMockTest /* extends AbstractMockTest implements WebServiceMessages */ {
	/*
	 * 
	 * private static final String VALID_PASSWORD = "Password"; private static final
	 * String INVALID_PASSWORD = "password"; private static final String
	 * BLANK_PASSWORD = "     "; private static final String VALID_USERNAME =
	 * "bobTheBuilder"; private static final String INVALID_USERNAME = "   ";
	 * private static final String TEST_INVALID_PASSWORD_MSG = "Bad Password";
	 * 
	 * private ValidatePasswordCredentialsAdapterImpl sut; private MessageSource
	 * messageSource; private CustomerService customerService; private Customer
	 * customer; private UsernameValidator usernameValidator;
	 * 
	 * @Autowired private ErightsFacade fakeErightsFacade;
	 * 
	 * public ValidatePasswordCredentialsAdapterMockTest() throws NamingException {
	 * super(); }
	 * 
	 * @Before public void setup() { this.customerService =
	 * EasyMock.createMock(CustomerService.class); this.messageSource =
	 * EasyMock.createMock(MessageSource.class); this.usernameValidator =
	 * EasyMock.createMock(UsernameValidator.class); this.fakeErightsFacade =
	 * EasyMock.createMock(ErightsFacade.class); this.sut = new
	 * ValidatePasswordCredentialsAdapterImpl(messageSource, customerService,
	 * usernameValidator); this.customer = new Customer();
	 * setMocks(this.customerService, this.messageSource, this.usernameValidator); }
	 * 
	 * private ValidatePasswordCredentialsRequest getRequest(String username, String
	 * password) { ValidatePasswordCredentialsRequest req = new
	 * ValidatePasswordCredentialsRequest(); PasswordCredential pwdCred = new
	 * PasswordCredential(); pwdCred.setUserName(username);
	 * pwdCred.setPassword(password); req.setCredentials(pwdCred); return req; }
	 * 
	 * 
	 * @Test public void testSuccess() throws WebServiceValidationException,
	 * ErightsException { ValidatePasswordCredentialsRequest req =
	 * getRequest(VALID_USERNAME, VALID_PASSWORD);
	 * 
	 * EasyMock.expect(this.usernameValidator.isValid(req.getCredentials().
	 * getUserName())).andReturn(true);
	 * EasyMock.expect(customerService.validateUserAccount(EasyMock.anyObject(
	 * WsUserIdDto.class))).andReturn(null); EasyMock.replay(getMocks());
	 * 
	 * ValidatePasswordCredentialsResponse result = sut.validate(req);
	 * 
	 * Assert.assertEquals(CredentialStatusCode.VALID, result.getStatus()); String[]
	 * reasons = result.getStatusReason(); Assert.assertEquals(0, reasons.length); }
	 * 
	 *//**
		 * Usernames are invalid if they are blank.
		 */
	/*
	 * @Test public void testInvalidBlankUsername() throws
	 * WebServiceValidationException {
	 * 
	 * ValidatePasswordCredentialsRequest req = getRequest(INVALID_USERNAME,
	 * VALID_PASSWORD);
	 * 
	 * // EasyMock.replay(getMocks()); ValidatePasswordCredentialsResponse result =
	 * sut.validate(req); // EasyMock.verify(getMocks());
	 * 
	 * Assert.assertEquals(CredentialStatusCode.INVALID_USERNAME,
	 * result.getStatus()); String[] reasons = result.getStatusReason();
	 * Assert.assertEquals(1, reasons.length);
	 * Assert.assertEquals(ERR_USERNAME_CANNOT_BE_BLANK, reasons[0]); }
	 * 
	 * @Test public void oupUATAlreadyExists() throws WebServiceValidationException,
	 * ErightsException { //
	 * EasyMock.expect(this.customerService.getCustomerByUsername(VALID_USERNAME)).
	 * andReturn(customer); ValidatePasswordCredentialsRequest req =
	 * getRequest(VALID_USERNAME, VALID_PASSWORD);
	 * EasyMock.expect(this.usernameValidator.isValid(VALID_USERNAME)).andReturn(
	 * true); EasyMock.replay(getMocks()); ValidatePasswordCredentialsResponse
	 * result = sut.validate(req); // EasyMock.verify(getMocks());
	 * 
	 * Assert.assertEquals(CredentialStatusCode.INVALID_USERNAME,
	 * result.getStatus()); String[] reasons = result.getStatusReason();
	 * Assert.assertEquals(1, reasons.length);
	 * Assert.assertEquals("The username is invalid.", reasons[0]); }
	 * 
	 * @Test public void testInvalidPassword() throws WebServiceValidationException,
	 * ErightsException { //
	 * EasyMock.expect(this.customerService.getCustomerByUsername(VALID_USERNAME)).
	 * andReturn(null);
	 * //EasyMock.expect(this.messageSource.getMessage(eq(PasswordUtils.
	 * INVALID_PASSWORD_MSG_CODE), anyObject(String[].class),
	 * anyObject(String.class),
	 * anyObject(Locale.class))).andReturn(TEST_INVALID_PASSWORD_MSG);
	 * ValidatePasswordCredentialsRequest req = getRequest(VALID_USERNAME+"abcde",
	 * INVALID_PASSWORD);
	 * //EasyMock.expect(this.usernameValidator.isValid(VALID_USERNAME)).andReturn(
	 * true); //EasyMock.replay(getMocks()); ValidatePasswordCredentialsResponse
	 * result = sut.validate(req); //EasyMock.verify(getMocks());
	 * 
	 * Assert.assertEquals(CredentialStatusCode.INVALID_USERNAME_PASSWORD,
	 * result.getStatus()); String[] reasons = result.getStatusReason();
	 * Assert.assertEquals(2, reasons.length); //
	 * Assert.assertEquals(TEST_INVALID_PASSWORD_MSG, reasons[1]); }
	 * 
	 *//**
		 * Usernames are invalid if they are blank.
		 */
	/*
	 * @Test public void testInvalidPasswordAndInvalidUsername() throws
	 * WebServiceValidationException {
	 * 
	 * // EasyMock.expect(this.messageSource.getMessage(eq(PasswordUtils.
	 * INVALID_PASSWORD_MSG_CODE), anyObject(String[].class),
	 * anyObject(String.class),
	 * anyObject(Locale.class))).andReturn(TEST_INVALID_PASSWORD_MSG);
	 * ValidatePasswordCredentialsRequest req = getRequest(INVALID_USERNAME,
	 * INVALID_PASSWORD);
	 * 
	 * // EasyMock.replay(getMocks()); ValidatePasswordCredentialsResponse result =
	 * sut.validate(req); // EasyMock.verify(getMocks());
	 * 
	 * Assert.assertEquals(CredentialStatusCode.INVALID_USERNAME_PASSWORD,
	 * result.getStatus()); String[] reasons = result.getStatusReason();
	 * Assert.assertEquals(2, reasons.length);
	 * Assert.assertEquals(ERR_USERNAME_CANNOT_BE_BLANK, reasons[0]); //
	 * Assert.assertEquals(TEST_INVALID_PASSWORD_MSG, reasons[1]); }
	 * 
	 *//**
		 * Usernames are invalid if they are blank.
		 */
	/*
	 * @Test public void testInvalidPasswordAndUsernameTaken() throws
	 * WebServiceValidationException, ErightsException {
	 * 
	 * //
	 * EasyMock.expect(this.customerService.getCustomerByUsername(VALID_USERNAME)).
	 * andReturn(customer); //
	 * EasyMock.expect(this.messageSource.getMessage(eq(PasswordUtils.
	 * INVALID_PASSWORD_MSG_CODE), anyObject(String[].class),
	 * anyObject(String.class),
	 * anyObject(Locale.class))).andReturn(TEST_INVALID_PASSWORD_MSG);
	 * ValidatePasswordCredentialsRequest req = getRequest(VALID_USERNAME,
	 * INVALID_PASSWORD); //
	 * EasyMock.expect(this.usernameValidator.isValid(VALID_USERNAME)).andReturn(
	 * true); // EasyMock.replay(getMocks()); ValidatePasswordCredentialsResponse
	 * result = sut.validate(req); // EasyMock.verify(getMocks());
	 * 
	 * Assert.assertEquals(CredentialStatusCode.INVALID_USERNAME_PASSWORD,
	 * result.getStatus()); String[] reasons = result.getStatusReason();
	 * Assert.assertEquals(2, reasons.length);
	 * Assert.assertEquals("The username is invalid.", reasons[0]);
	 * //Assert.assertEquals(TEST_INVALID_PASSWORD_MSG, reasons[1]); }
	 * 
	 * @Test public void testBlankPassword() throws
	 * WebServiceValidationException,ErightsException {
	 * 
	 * //
	 * EasyMock.expect(this.customerService.getCustomerByUsername(VALID_USERNAME)).
	 * andReturn(null); ValidatePasswordCredentialsRequest req =
	 * getRequest(VALID_USERNAME, BLANK_PASSWORD);
	 * //EasyMock.expect(this.usernameValidator.isValid(VALID_USERNAME)).andReturn(
	 * true); //EasyMock.replay(getMocks()); ValidatePasswordCredentialsResponse
	 * result = sut.validate(req); //EasyMock.verify(getMocks());
	 * 
	 * Assert.assertEquals(CredentialStatusCode.INVALID_USERNAME_PASSWORD,
	 * result.getStatus()); String[] reasons = result.getStatusReason();
	 * Assert.assertEquals(2, reasons.length);
	 * Assert.assertEquals(ERR_PASSWORD_CANNOT_BE_BLANK, reasons[1]);
	 * 
	 * }
	 * 
	 * @Test public void testBlankPasswordAndInvalidUsername() throws
	 * WebServiceValidationException {
	 * 
	 * ValidatePasswordCredentialsRequest req = getRequest(INVALID_USERNAME,
	 * BLANK_PASSWORD);
	 * 
	 * // EasyMock.replay(getMocks()); ValidatePasswordCredentialsResponse result =
	 * sut.validate(req); // EasyMock.verify(getMocks());
	 * 
	 * Assert.assertEquals(CredentialStatusCode.INVALID_USERNAME_PASSWORD,
	 * result.getStatus()); String[] reasons = result.getStatusReason();
	 * Assert.assertEquals(2, reasons.length);
	 * Assert.assertEquals(ERR_USERNAME_CANNOT_BE_BLANK, reasons[0]);
	 * Assert.assertEquals(ERR_PASSWORD_CANNOT_BE_BLANK, reasons[1]);
	 * 
	 * }
	 * 
	 * @Test public void testBlankPasswordAndUsernameTaken() throws
	 * WebServiceValidationException,ErightsException {
	 * 
	 * //
	 * EasyMock.expect(this.customerService.getCustomerByUsername(VALID_USERNAME)).
	 * andReturn(customer); ValidatePasswordCredentialsRequest req =
	 * getRequest(VALID_USERNAME, BLANK_PASSWORD);
	 * //EasyMock.expect(this.usernameValidator.isValid(VALID_USERNAME)).andReturn(
	 * true); //EasyMock.replay(getMocks()); ValidatePasswordCredentialsResponse
	 * result = sut.validate(req); //EasyMock.verify(getMocks());
	 * 
	 * Assert.assertEquals(CredentialStatusCode.INVALID_USERNAME_PASSWORD,
	 * result.getStatus()); String[] reasons = result.getStatusReason();
	 * Assert.assertEquals(2, reasons.length);
	 * Assert.assertEquals("The username is invalid.", reasons[0]);
	 * Assert.assertEquals(ERR_PASSWORD_CANNOT_BE_BLANK, reasons[1]);
	 * 
	 * }
	 * 
	 */}
