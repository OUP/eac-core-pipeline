package com.oup.eac.ws.v2.service.impl;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;

import java.util.Locale;
import java.util.UUID;

import javax.naming.NamingException;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.easymock.IArgumentMatcher;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.context.MessageSource;

import com.oup.eac.common.utils.crypto.PasswordUtils;
import com.oup.eac.common.utils.username.UsernameValidator;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.Customer.CustomerType;
import com.oup.eac.domain.Password;
import com.oup.eac.integration.facade.exceptions.ErightsException;
import com.oup.eac.service.CustomerService;
import com.oup.eac.service.ServiceLayerException;
import com.oup.eac.service.exceptions.PasswordPolicyViolatedServiceLayerException;
import com.oup.eac.service.exceptions.ServiceLayerValidationException;
import com.oup.eac.service.exceptions.UsernameExistsException;
import com.oup.eac.ws.v2.binding.access.UpdateUserAccountWithConcurrencyRequest;
import com.oup.eac.ws.v2.binding.access.UpdateUserAccountWithConcurrencyResponse;
import com.oup.eac.ws.v2.binding.common.Credential;
import com.oup.eac.ws.v2.binding.common.IPCredential;
import com.oup.eac.ws.v2.binding.common.PasswordCredential;
import com.oup.eac.ws.v2.binding.common.UpdateUserWithConcurrency;
import com.oup.eac.ws.v2.binding.common.WsUserId;
import com.oup.eac.ws.v2.binding.common.types.StatusCode;
import com.oup.eac.ws.v2.ex.WebServiceException;
import com.oup.eac.ws.v2.ex.WebServiceValidationException;
import com.oup.eac.ws.v2.service.WsCustomerLookup;

public class UpdateUserAccountWithConcurrencyAdapterMockTest /* extends AbstractMockTest */ {
	/*
	 * 
	 * private UpdateUserAccountWithConcurrencyAdapterImpl
	 * updateUserWithConcurrencyAdapter;
	 * 
	 * private MessageSource messageSource; private CustomerService customerService;
	 * private WsCustomerLookup customerLookup; private UsernameValidator
	 * usernameValidator;
	 * 
	 * public UpdateUserAccountWithConcurrencyAdapterMockTest() throws
	 * NamingException { super(); }
	 * 
	 * @Before public void setup() { this.messageSource =
	 * createMock(MessageSource.class); this.customerLookup =
	 * createMock(WsCustomerLookup.class); this.customerService =
	 * createMock(CustomerService.class); this.usernameValidator =
	 * createMock(UsernameValidator.class); setMocks(messageSource, customerLookup,
	 * customerService, usernameValidator);
	 * 
	 * this.updateUserWithConcurrencyAdapter = new
	 * UpdateUserAccountWithConcurrencyAdapterImpl(messageSource, customerService,
	 * customerLookup, usernameValidator); }
	 * 
	 * @Test public void testFailureUnknownUser() throws WebServiceException {
	 * UpdateUserAccountWithConcurrencyRequest request = new
	 * UpdateUserAccountWithConcurrencyRequest(); WsUserId wsUserId = new
	 * WsUserId(); UpdateUserWithConcurrency user = new UpdateUserWithConcurrency();
	 * request.setUser(user); request.setWsUserId(wsUserId);
	 * 
	 * String randomMessage = UUID.randomUUID().toString();
	 * EasyMock.expect(customerLookup.getCustomerByWsUserId(wsUserId)).andThrow(new
	 * WebServiceValidationException(randomMessage));
	 * 
	 * EasyMock.replay(getMocks()); UpdateUserAccountWithConcurrencyResponse
	 * response =
	 * updateUserWithConcurrencyAdapter.updateUserAccountWithConcurrency(request);
	 * EasyMock.verify(getMocks());
	 * 
	 * Assert.assertEquals(StatusCode.CLIENT_ERROR,
	 * response.getErrorStatus().getStatusCode());
	 * Assert.assertEquals(randomMessage,
	 * response.getErrorStatus().getStatusReason()); }
	 * 
	 * @Test public void testFailureServiceLayerValidationException() throws
	 * WebServiceException, ServiceLayerException, ErightsException { String
	 * username = "davidhay"; UpdateUserAccountWithConcurrencyRequest request = new
	 * UpdateUserAccountWithConcurrencyRequest(); WsUserId wsUserId = new
	 * WsUserId(); UpdateUserWithConcurrency user = getBasicUpdateUser(username);
	 * request.setUser(user); request.setWsUserId(wsUserId); try{ String
	 * randomMessage = UUID.randomUUID().toString(); Customer customer =
	 * getCustomer();
	 * EasyMock.expect(customerLookup.getCustomerByWsUserId(wsUserId)).andReturn(
	 * customer);
	 * 
	 * //
	 * EasyMock.expect(customerService.getCustomerByUsername(username)).andReturn(
	 * customer);// we are not changing the username
	 * EasyMock.expect(this.usernameValidator.isValid("davidhay")).andReturn(true);
	 * //customerService.updateCustomer(customer, false);
	 * //expectLastCall().andThrow(new
	 * ServiceLayerValidationException(randomMessage));
	 * 
	 * customerService.updateCustomerFromWS(customer, null);
	 * 
	 * expectLastCall().andThrow(new
	 * ServiceLayerException("problem updating user account"));
	 * EasyMock.replay(getMocks()); UpdateUserAccountWithConcurrencyResponse
	 * response =
	 * updateUserWithConcurrencyAdapter.updateUserAccountWithConcurrency(request);
	 * EasyMock.verify(getMocks()); Assert.fail("exception expected"); //
	 * Assert.assertEquals(StatusCode.CLIENT_ERROR,
	 * response.getErrorStatus().getStatusCode()); //
	 * Assert.assertEquals("problem updating user account",
	 * response.getErrorStatus().getStatusReason()); } catch (WebServiceException
	 * wse) { // Assert.assertEquals(randomMessage,wse.getCause().getMessage());
	 * Assert.assertEquals("problem updating user account",wse.getMessage()); }
	 * finally { EasyMock.verify(getMocks()); } }
	 * 
	 * @Test public void testFailureServiceLayerException() throws
	 * WebServiceException, PasswordPolicyViolatedServiceLayerException,
	 * ServiceLayerException, ErightsException { String randomMessage =
	 * UUID.randomUUID().toString(); try { String username = "davidhay";
	 * UpdateUserAccountWithConcurrencyRequest request = new
	 * UpdateUserAccountWithConcurrencyRequest(); WsUserId wsUserId = new
	 * WsUserId(); UpdateUserWithConcurrency user = getBasicUpdateUser(username);
	 * request.setUser(user); request.setWsUserId(wsUserId);
	 * 
	 * 
	 * Customer customer = getCustomer();
	 * EasyMock.expect(customerLookup.getCustomerByWsUserId(wsUserId)).andReturn(
	 * customer);
	 * EasyMock.expect(this.usernameValidator.isValid("davidhay")).andReturn(true);
	 * 
	 * //
	 * EasyMock.expect(customerService.getCustomerByUsername(username)).andReturn(
	 * customer);// we are not changing the username
	 * 
	 * //customerService.updateCustomer(customer, false);
	 * //expectLastCall().andThrow(new ServiceLayerException(randomMessage));
	 * customerService.updateCustomerFromWS(customer, null);
	 * 
	 * expectLastCall().andThrow(new
	 * ServiceLayerException("problem updating user account"));
	 * 
	 * EasyMock.replay(getMocks());
	 * 
	 * @SuppressWarnings("unused") UpdateUserAccountWithConcurrencyResponse response
	 * = updateUserWithConcurrencyAdapter.updateUserAccountWithConcurrency(request);
	 * Assert.fail("exception expected"); }catch (WebServiceException wse) { //
	 * Assert.assertEquals(randomMessage,wse.getCause().getMessage());
	 * Assert.assertEquals("problem updating user account",wse.getMessage()); }
	 * finally { EasyMock.verify(getMocks()); } }
	 * 
	 * @Test public void testSuccessSameUserName() throws WebServiceException,
	 * ServiceLayerException, ErightsException { String username = "davidhay";
	 * UpdateUserAccountWithConcurrencyRequest request = new
	 * UpdateUserAccountWithConcurrencyRequest(); WsUserId wsUserId = new
	 * WsUserId(); UpdateUserWithConcurrency user = getBasicUpdateUser(username);
	 * request.setUser(user); request.setWsUserId(wsUserId);
	 * 
	 * Customer customer = getCustomer();
	 * EasyMock.expect(customerLookup.getCustomerByWsUserId(wsUserId)).andReturn(
	 * customer);
	 * 
	 * Password updatedPassword = new
	 * Password(user.getCredentials().getPasswordCredential().getPassword(), false);
	 * 
	 * //
	 * EasyMock.expect(customerService.getCustomerByUsername(username)).andReturn(
	 * customer);// we are not changing the username
	 * 
	 * // EasyMock.expectLastCall();
	 * 
	 * // customerService.updateCustomer(eqCustomerUsernameAndPassword("davidhay",
	 * updatedPassword), EasyMock.eq(false)); // expectLastCall();
	 * customerService.updateCustomerFromWS(customer, null); expectLastCall();
	 * 
	 * EasyMock.expect(this.usernameValidator.isValid("davidhay")).andReturn(true);
	 * EasyMock.replay(getMocks());
	 * 
	 * UpdateUserAccountWithConcurrencyResponse response =
	 * updateUserWithConcurrencyAdapter.updateUserAccountWithConcurrency(request);
	 * EasyMock.verify(getMocks());
	 * 
	 * Assert.assertNull(response.getErrorStatus()); }
	 * 
	 * @Test public void testSuccessSameUserNameBlankPassword() throws
	 * WebServiceException, ServiceLayerException,ErightsException { String username
	 * = "davidhay"; UpdateUserAccountWithConcurrencyRequest request = new
	 * UpdateUserAccountWithConcurrencyRequest(); WsUserId wsUserId = new
	 * WsUserId(); UpdateUserWithConcurrency user = getBasicUpdateUser(username);
	 * user.getCredentials().getPasswordCredential().setPassword("    ");// set the
	 * password to blank request.setUser(user); request.setWsUserId(wsUserId);
	 * 
	 * Customer customer = getCustomer(); Password originalPassword =
	 * customer.getWrappedPassword();
	 * 
	 * EasyMock.expect(customerLookup.getCustomerByWsUserId(wsUserId)).andReturn(
	 * customer);
	 * 
	 * //
	 * EasyMock.expect(customerService.getCustomerByUsername(username)).andReturn(
	 * customer);// we are not changing the username
	 * 
	 * //customerService.updateCustomer(eqCustomerUsernameAndPassword("davidhay",
	 * originalPassword), EasyMock.eq(false)); //expectLastCall();
	 * customerService.updateCustomerFromWS(customer, null); expectLastCall();
	 * EasyMock.expect(this.usernameValidator.isValid("davidhay")).andReturn(true);
	 * 
	 * EasyMock.expectLastCall();
	 * 
	 * EasyMock.replay(getMocks()); UpdateUserAccountWithConcurrencyResponse
	 * response =
	 * updateUserWithConcurrencyAdapter.updateUserAccountWithConcurrency(request);
	 * EasyMock.verify(getMocks());
	 * 
	 * Assert.assertNull(response.getErrorStatus()); }
	 * 
	 * @Test public void testFaliureIPCredentialName() throws WebServiceException,
	 * ServiceLayerException {
	 * 
	 * UpdateUserAccountWithConcurrencyRequest request = new
	 * UpdateUserAccountWithConcurrencyRequest(); WsUserId wsUserId = new
	 * WsUserId(); UpdateUserWithConcurrency user = new UpdateUserWithConcurrency();
	 * user.setEmailAddress("david.hay@test.com"); Credential cred = new
	 * Credential(); IPCredential ip = new IPCredential(); ip.setIP("127.0.0.1");
	 * cred.setIpCredential(ip); user.setCredentials(cred); request.setUser(user);
	 * request.setWsUserId(wsUserId);
	 * 
	 * Customer customer = getCustomer();
	 * EasyMock.expect(customerLookup.getCustomerByWsUserId(wsUserId)).andReturn(
	 * customer); EasyMock.replay(getMocks());
	 * 
	 * UpdateUserAccountWithConcurrencyResponse response =
	 * updateUserWithConcurrencyAdapter.updateUserAccountWithConcurrency(request);
	 * EasyMock.verify(getMocks());
	 * 
	 * Assert.assertEquals(StatusCode.CLIENT_ERROR,
	 * response.getErrorStatus().getStatusCode());
	 * Assert.assertEquals("IP Credentials are not supported.",
	 * response.getErrorStatus().getStatusReason()); }
	 * 
	 * @Test public void testSuccessDifferentUserName() throws WebServiceException,
	 * PasswordPolicyViolatedServiceLayerException, ServiceLayerException,
	 * ErightsException { String username = "davidhay";
	 * UpdateUserAccountWithConcurrencyRequest request = new
	 * UpdateUserAccountWithConcurrencyRequest(); WsUserId wsUserId = new
	 * WsUserId(); UpdateUserWithConcurrency user = getBasicUpdateUser(username);
	 * request.setUser(user); request.setWsUserId(wsUserId);
	 * 
	 * Customer customer = getCustomer();
	 * EasyMock.expect(customerLookup.getCustomerByWsUserId(wsUserId)).andReturn(
	 * customer);
	 * 
	 * //
	 * EasyMock.expect(customerService.getCustomerByUsername(username)).andReturn(
	 * null);// username not taken
	 * 
	 * // customerService.updateCustomer(customer, false); // expectLastCall();
	 * 
	 * customerService.updateCustomerFromWS(customer, null); expectLastCall();
	 * 
	 * EasyMock.expect(this.usernameValidator.isValid("davidhay")).andReturn(true);
	 * 
	 * EasyMock.expectLastCall();
	 * 
	 * EasyMock.replay(getMocks()); UpdateUserAccountWithConcurrencyResponse
	 * response =
	 * updateUserWithConcurrencyAdapter.updateUserAccountWithConcurrency(request);
	 * EasyMock.verify(getMocks());
	 * 
	 * Assert.assertNull(response.getErrorStatus());
	 * 
	 * }
	 * 
	 * @Test public void testFailureUserNameAlreadyTaken() throws
	 * WebServiceException, ServiceLayerException, ErightsException { String
	 * username = "davidhay"; UpdateUserAccountWithConcurrencyRequest request = new
	 * UpdateUserAccountWithConcurrencyRequest(); WsUserId wsUserId = new
	 * WsUserId(); UpdateUserWithConcurrency user = getBasicUpdateUser(username);
	 * request.setUser(user); request.setWsUserId(wsUserId);
	 * 
	 * Customer customer1 = getCustomer(); Customer customer2 = getCustomer();
	 * EasyMock.expect(customerLookup.getCustomerByWsUserId(wsUserId)).andReturn(
	 * customer1);
	 * 
	 * //EasyMock.expect(customerService.getCustomerByUsername(username)).andReturn(
	 * customer2);// username is already taken!
	 * customerService.updateCustomerFromWS(customer1, null); expectLastCall();
	 * customerService.updateCustomerFromWS(EasyMock.anyObject(Customer.class),
	 * EasyMock.anyObject(String.class)); expectLastCall().andThrow(new
	 * UsernameExistsException(CreateUserAccountAdapterImpl.
	 * ERR_USERNAME_ALREADY_TAKEN));
	 * 
	 * EasyMock.expect(this.usernameValidator.isValid("davidhay")).andReturn(true);
	 * EasyMock.replay(getMocks()); UpdateUserAccountWithConcurrencyResponse
	 * response =
	 * updateUserWithConcurrencyAdapter.updateUserAccountWithConcurrency(request);
	 * EasyMock.verify(getMocks());
	 * 
	 * Assert.assertEquals(StatusCode.CLIENT_ERROR,
	 * response.getErrorStatus().getStatusCode());
	 * Assert.assertEquals(CreateUserAccountAdapterImpl.ERR_USERNAME_ALREADY_TAKEN,
	 * response.getErrorStatus().getStatusReason());
	 * 
	 * }
	 * 
	 * @Test public void testFailureInvalidEmailAddress() throws
	 * WebServiceException, ServiceLayerException { String username = "davidhay";
	 * UpdateUserWithConcurrency user = getBasicUpdateUser(username);
	 * user.setEmailAddress("david@test"); checkValidationFailure(user,
	 * "The email address is invalid."); }
	 * 
	 * @Test public void testFailureInvalidTimeZone() throws WebServiceException,
	 * ServiceLayerException { UpdateUserWithConcurrency user =
	 * getBasicUpdateUser("davidhay"); user.setTimeZone("Oxford/Europe");
	 * EasyMock.expect(this.usernameValidator.isValid("davidhay")).andReturn(true);
	 * checkValidationFailure(user,
	 * "The Time Zone ID is not valid : Oxford/Europe"); }
	 * 
	 * @Test public void testFailureInvalidUserName() throws WebServiceException,
	 * ServiceLayerException { String username = "      "; UpdateUserWithConcurrency
	 * user = getBasicUpdateUser(username); checkValidationFailure(user,
	 * "The username cannot be blank."); }
	 * 
	 * @Test public void testFailureInvalidPassword() throws WebServiceException,
	 * ServiceLayerException { String username = "davidhay";
	 * UpdateUserWithConcurrency user = getBasicUpdateUser(username);
	 * user.getCredentials().getPasswordCredential().setPassword("ABC");
	 * Class<Object[]> clazz = Object[].class; expect(
	 * this.messageSource.getMessage(EasyMock.eq(PasswordUtils.
	 * INVALID_PASSWORD_MSG_CODE), anyObject(clazz), EasyMock
	 * .eq(PasswordUtils.DEFAULT_INVALID_PASSWORD_MSG),
	 * anyObject(Locale.class))).andReturn("ERROR_MESSAGE");
	 * EasyMock.expect(this.usernameValidator.isValid("davidhay")).andReturn(true);
	 * checkValidationFailure(user, "ERROR_MESSAGE"); }
	 * 
	 * @Test public void testSuccessSameUserNameSamePassword() throws
	 * WebServiceException, PasswordPolicyViolatedServiceLayerException,
	 * ServiceLayerException, ErightsException { Customer customer = getCustomer();
	 * Password originalPassword = customer.getWrappedPassword();
	 * 
	 * String username = "davidhay"; UpdateUserAccountWithConcurrencyRequest request
	 * = new UpdateUserAccountWithConcurrencyRequest(); WsUserId wsUserId = new
	 * WsUserId(); UpdateUserWithConcurrency user = getBasicUpdateUser(username);//
	 * user.getCredentials().getPasswordCredential().setPassword(originalPassword.
	 * getValue());// same password request.setUser(user);
	 * request.setWsUserId(wsUserId);
	 * 
	 * EasyMock.expect(customerLookup.getCustomerByWsUserId(wsUserId)).andReturn(
	 * customer);
	 * 
	 * //
	 * EasyMock.expect(customerService.getCustomerByUsername(username)).andReturn(
	 * customer);// we are not changing the username
	 * 
	 * //customerService.updateCustomer(eqCustomerUsernameAndPassword("davidhay",
	 * originalPassword), EasyMock.eq(false)); // expectLastCall();
	 * customerService.updateCustomerFromWS(customer, null); expectLastCall();
	 * 
	 * EasyMock.expect(this.usernameValidator.isValid("davidhay")).andReturn(true);
	 * 
	 * EasyMock.replay(getMocks()); UpdateUserAccountWithConcurrencyResponse
	 * response =
	 * updateUserWithConcurrencyAdapter.updateUserAccountWithConcurrency(request);
	 * EasyMock.verify(getMocks());
	 * 
	 * Assert.assertNull(response.getErrorStatus()); }
	 * 
	 * @Test public void
	 * testChangePasswordPolicyViolatedCurrentPasswordSameAsPreviousPasswordException
	 * () { try{
	 * 
	 * Customer customer = getCustomer(); String username = "davidhay";
	 * UpdateUserAccountWithConcurrencyRequest request = new
	 * UpdateUserAccountWithConcurrencyRequest(); WsUserId wsUserId = new
	 * WsUserId(); wsUserId.setUserName(username); UpdateUserWithConcurrency user =
	 * getBasicUpdateUser(username);//
	 * user.getCredentials().getPasswordCredential().setPassword("password1Az");//
	 * password same as current password request.setUser(user);
	 * request.setWsUserId(wsUserId);
	 * EasyMock.expect(customerLookup.getCustomerByWsUserId(wsUserId)).andReturn(
	 * customer);
	 * 
	 * //
	 * EasyMock.expect(customerService.getCustomerByUsername(username)).andReturn(
	 * customer); //customerService.updateCustomer(customer,false); //
	 * EasyMock.expectLastCall().andThrow(new
	 * PasswordPolicyViolatedServiceLayerException("New password should not match current password"
	 * )); customerService.updateCustomerFromWS(customer, null);
	 * 
	 * expectLastCall().andThrow(new
	 * PasswordPolicyViolatedServiceLayerException("New password should not match current password"
	 * ));
	 * 
	 * EasyMock.expect(this.usernameValidator.isValid("davidhay")).andReturn(true);
	 * 
	 * EasyMock.replay(getMocks()); UpdateUserAccountWithConcurrencyResponse
	 * response =
	 * updateUserWithConcurrencyAdapter.updateUserAccountWithConcurrency(request);
	 * EasyMock.verify(getMocks());
	 * Assert.assertEquals(response.getErrorStatus().getStatusReason()
	 * ,"New password should not match current password" );
	 * 
	 * } catch(Exception ex){
	 * Assert.assertEquals(PasswordPolicyViolatedServiceLayerException.class,
	 * ex.getClass());
	 * Assert.assertEquals("New password should not match current password",
	 * ex.getMessage()); } }
	 * 
	 * @Test public void
	 * testChangePasswordPolicyViolatedPreviousPasswordsException() { try{
	 * 
	 * Customer customer = getCustomer(); String username = "davidhay";
	 * UpdateUserAccountWithConcurrencyRequest request = new
	 * UpdateUserAccountWithConcurrencyRequest(); WsUserId wsUserId = new
	 * WsUserId(); UpdateUserWithConcurrency user = getBasicUpdateUser(username);//
	 * user.getCredentials().getPasswordCredential().setPassword("oldPassword1");//
	 * password same as current password request.setUser(user);
	 * request.setWsUserId(wsUserId);
	 * EasyMock.expect(customerLookup.getCustomerByWsUserId(wsUserId)).andReturn(
	 * customer);
	 * 
	 * //
	 * EasyMock.expect(customerService.getCustomerByUsername(username)).andReturn(
	 * customer); // customerService.updateCustomer(customer,false); //
	 * EasyMock.expectLastCall().andThrow(new
	 * PasswordPolicyViolatedServiceLayerException("New password should not match previous 4 passwords"
	 * )); // expectLastCall();
	 * 
	 * // expectLastCall(); customerService.updateCustomerFromWS(customer, null);
	 * EasyMock.expectLastCall().andThrow(new
	 * PasswordPolicyViolatedServiceLayerException("New password should not match previous 4 passwords"
	 * ));
	 * EasyMock.expect(this.usernameValidator.isValid("davidhay")).andReturn(true);
	 * 
	 * EasyMock.replay(getMocks()); UpdateUserAccountWithConcurrencyResponse
	 * response =
	 * updateUserWithConcurrencyAdapter.updateUserAccountWithConcurrency(request);
	 * EasyMock.verify(getMocks());
	 * Assert.assertEquals(response.getErrorStatus().getStatusReason()
	 * ,"New password should not match previous 4 passwords" );
	 * 
	 * } catch(Exception ex){
	 * Assert.assertEquals(PasswordPolicyViolatedServiceLayerException.class,
	 * ex.getClass());
	 * Assert.assertEquals("New password should not match previous 4 passwords",
	 * ex.getMessage()); } }
	 * 
	 * private void checkValidationFailure(UpdateUserWithConcurrency user, String
	 * validationMessage) throws WebServiceException {
	 * UpdateUserAccountWithConcurrencyRequest request = new
	 * UpdateUserAccountWithConcurrencyRequest(); WsUserId wsUserId = new
	 * WsUserId(); request.setUser(user); request.setWsUserId(wsUserId);
	 * 
	 * Customer customer1 = getCustomer();
	 * EasyMock.expect(customerLookup.getCustomerByWsUserId(wsUserId)).andReturn(
	 * customer1);
	 * 
	 * EasyMock.replay(getMocks()); UpdateUserAccountWithConcurrencyResponse
	 * response =
	 * updateUserWithConcurrencyAdapter.updateUserAccountWithConcurrency(request);
	 * EasyMock.verify(getMocks());
	 * 
	 * Assert.assertEquals(StatusCode.CLIENT_ERROR,
	 * response.getErrorStatus().getStatusCode());
	 * Assert.assertEquals(validationMessage,
	 * response.getErrorStatus().getStatusReason()); }
	 * 
	 * private Customer getCustomer() { Customer customer = new Customer();
	 * customer.setPassword(new Password("password1Az", false));
	 * customer.setId(UUID.randomUUID().toString());// give the customer an id
	 * return customer; }
	 * 
	 * private UpdateUserWithConcurrency getBasicUpdateUser(String username) {
	 * UpdateUserWithConcurrency user = new UpdateUserWithConcurrency(); Credential
	 * creds = new Credential(); PasswordCredential passwordCredential = new
	 * PasswordCredential(); passwordCredential.setUserName(username);
	 * passwordCredential.setPassword("ABC123abc");// not-blank
	 * creds.setPasswordCredential(passwordCredential); user.setCredentials(creds);
	 * user.setEmailAddress("david.hay@test.com"); user.setFirstName(null);
	 * user.setLastName(null); user.setLocale(null); user.setTimeZone(null); return
	 * user; }
	 * 
	 *//**
		 * EasyMock custom matcher for Customer objects
		 * 
		 * @param username       the username
		 * @param hashedPassword the hashed password
		 * 
		 * @see UpdateUserAccountWithConcurrencyAdapterMockTest
		 * @return the customer
		 */
	/*
	 * protected Customer eqCustomerUsernameAndPassword(final String username, final
	 * Password updatedPassword) { IArgumentMatcher matcher = new IArgumentMatcher()
	 * {
	 * 
	 * private boolean isEq(Object expected, Object actual) { if (expected == null)
	 * { return actual == null; } else { return expected.equals(actual); } }
	 * 
	 * @Override public boolean matches(Object arg) { if (arg instanceof Customer ==
	 * false) { return false; } Customer customer = (Customer) arg;
	 * 
	 * boolean chk1 = isEq(username, customer.getUsername()); boolean chk2 =
	 * isEq(updatedPassword, customer.getWrappedPassword()); boolean result = chk1
	 * && chk2; return result; }
	 * 
	 * @Override public void appendTo(StringBuffer out) {
	 * out.append("eqCustomerUsernameAndPassword("); out.append(username);
	 * out.append(","); out.append(updatedPassword); out.append(")"); } };
	 * EasyMock.reportMatcher(matcher); return null; }
	 * 
	 * @Test public void testSuccessUserConcurrencyBlank() throws
	 * WebServiceException, ServiceLayerException, ErightsException { String
	 * username = "chiragjoshi"; UpdateUserAccountWithConcurrencyRequest request =
	 * new UpdateUserAccountWithConcurrencyRequest(); WsUserId wsUserId = new
	 * WsUserId(); UpdateUserWithConcurrency user = getBasicUpdateUser(username);
	 * request.setUser(user); request.setWsUserId(wsUserId);
	 * 
	 * Customer customer = getCustomer(); CustomerType ct =
	 * CustomerType.SPECIFIC_CONCURRENCY; // Not setting user concurrency //
	 * ct.setConcurrency(userConcurrency); customer.setCustomerType(ct);
	 * EasyMock.expect(customerLookup.getCustomerByWsUserId(wsUserId)).andReturn(
	 * customer);
	 * EasyMock.expect(usernameValidator.isValid(username)).andReturn(true); //
	 * EasyMock.expect(customerService.getCustomerByUsername(username)).andReturn(
	 * customer); // customerService.updateCustomer(customer, false);
	 * 
	 * customerService.updateCustomerFromWS(customer, null); expectLastCall();
	 * 
	 * EasyMock.expectLastCall();
	 * 
	 * EasyMock.replay(getMocks()); UpdateUserAccountWithConcurrencyResponse
	 * response =
	 * updateUserWithConcurrencyAdapter.updateUserAccountWithConcurrency(request);
	 * EasyMock.verify(getMocks());
	 * 
	 * Assert.assertNull(response.getErrorStatus()); }
	 * 
	 * @Test public void testSuccessUserConcurrencyPositive() throws
	 * WebServiceException, ServiceLayerException, ErightsException { String
	 * username = "chiragjoshi"; int userConcurrency = 15;
	 * UpdateUserAccountWithConcurrencyRequest request = new
	 * UpdateUserAccountWithConcurrencyRequest(); WsUserId wsUserId = new
	 * WsUserId(); UpdateUserWithConcurrency user = getBasicUpdateUser(username);
	 * user.setUserConcurrency(userConcurrency); request.setUser(user);
	 * request.setWsUserId(wsUserId);
	 * 
	 * Customer customer = getCustomer(); CustomerType ct =
	 * CustomerType.SPECIFIC_CONCURRENCY; ct.setConcurrency(userConcurrency);
	 * customer.setCustomerType(ct);
	 * EasyMock.expect(customerLookup.getCustomerByWsUserId(wsUserId)).andReturn(
	 * customer);
	 * EasyMock.expect(usernameValidator.isValid(username)).andReturn(true); //
	 * EasyMock.expect(customerService.getCustomerByUsername(username)).andReturn(
	 * customer); //customerService.updateCustomer(customer, false);
	 * customerService.updateCustomerFromWS(customer, null); expectLastCall();
	 * 
	 * EasyMock.expectLastCall();
	 * 
	 * EasyMock.replay(getMocks()); UpdateUserAccountWithConcurrencyResponse
	 * response =
	 * updateUserWithConcurrencyAdapter.updateUserAccountWithConcurrency(request);
	 * EasyMock.verify(getMocks());
	 * 
	 * Assert.assertNull(response.getErrorStatus()); }
	 * 
	 * @Test public void testSuccessUserConcurrencyZero() throws
	 * WebServiceException, ServiceLayerException, ErightsException { String
	 * username = "chiragjoshi"; int userConcurrency = 0;
	 * UpdateUserAccountWithConcurrencyRequest request = new
	 * UpdateUserAccountWithConcurrencyRequest(); WsUserId wsUserId = new
	 * WsUserId(); UpdateUserWithConcurrency user = getBasicUpdateUser(username);
	 * user.setUserConcurrency(userConcurrency); request.setUser(user);
	 * request.setWsUserId(wsUserId);
	 * 
	 * Customer customer = getCustomer(); CustomerType ct =
	 * CustomerType.SPECIFIC_CONCURRENCY; ct.setConcurrency(userConcurrency);
	 * customer.setCustomerType(ct);
	 * EasyMock.expect(customerLookup.getCustomerByWsUserId(wsUserId)).andReturn(
	 * customer);
	 * EasyMock.expect(usernameValidator.isValid(username)).andReturn(true); //
	 * EasyMock.expect(customerService.getCustomerByUsername(username)).andReturn(
	 * customer); // customerService.updateCustomer(customer, false); //
	 * expectLastCall(); customerService.updateCustomerFromWS(customer, null);
	 * EasyMock.expectLastCall();
	 * 
	 * EasyMock.replay(getMocks()); UpdateUserAccountWithConcurrencyResponse
	 * response =
	 * updateUserWithConcurrencyAdapter.updateUserAccountWithConcurrency(request);
	 * EasyMock.verify(getMocks());
	 * 
	 * Assert.assertNull(response.getErrorStatus()); }
	 * 
	 * @Test public void testFailureUserConcurrencyNegative() throws
	 * WebServiceException, ServiceLayerException { String username = "chiragjoshi";
	 * int userConcurrency = -15; UpdateUserAccountWithConcurrencyRequest request =
	 * new UpdateUserAccountWithConcurrencyRequest(); WsUserId wsUserId = new
	 * WsUserId(); UpdateUserWithConcurrency user = getBasicUpdateUser(username);
	 * user.setUserConcurrency(userConcurrency); request.setUser(user);
	 * request.setWsUserId(wsUserId);
	 * 
	 * Customer customer = getCustomer(); CustomerType ct =
	 * CustomerType.SPECIFIC_CONCURRENCY; ct.setConcurrency(userConcurrency);
	 * customer.setCustomerType(ct);
	 * EasyMock.expect(customerLookup.getCustomerByWsUserId(wsUserId)).andReturn(
	 * customer);
	 * EasyMock.expect(usernameValidator.isValid(username)).andReturn(true);
	 * 
	 * EasyMock.replay(getMocks()); UpdateUserAccountWithConcurrencyResponse
	 * response =
	 * updateUserWithConcurrencyAdapter.updateUserAccountWithConcurrency(request);
	 * EasyMock.verify(getMocks());
	 * 
	 * Assert.assertEquals(StatusCode.CLIENT_ERROR,
	 * response.getErrorStatus().getStatusCode());
	 * Assert.assertEquals("The user concurrency cannot be negative : " +
	 * userConcurrency, response.getErrorStatus().getStatusReason()); }
	 * 
	 * 
	 * 
	 */}
