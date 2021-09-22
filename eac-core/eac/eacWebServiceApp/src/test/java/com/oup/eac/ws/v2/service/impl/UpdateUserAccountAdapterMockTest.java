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
import com.oup.eac.domain.Password;
import com.oup.eac.integration.facade.exceptions.ErightsException;
import com.oup.eac.integration.facade.exceptions.UserLoginCredentialAlreadyExistsException;
import com.oup.eac.service.CustomerService;
import com.oup.eac.service.ServiceLayerException;
import com.oup.eac.service.exceptions.PasswordPolicyViolatedServiceLayerException;
import com.oup.eac.service.exceptions.ServiceLayerValidationException;
import com.oup.eac.service.exceptions.UsernameExistsException;
import com.oup.eac.ws.v2.binding.access.UpdateUserAccountRequest;
import com.oup.eac.ws.v2.binding.access.UpdateUserAccountResponse;
import com.oup.eac.ws.v2.binding.common.Credential;
import com.oup.eac.ws.v2.binding.common.IPCredential;
import com.oup.eac.ws.v2.binding.common.PasswordCredential;
import com.oup.eac.ws.v2.binding.common.UpdateUser;
import com.oup.eac.ws.v2.binding.common.WsUserId;
import com.oup.eac.ws.v2.binding.common.types.StatusCode;
import com.oup.eac.ws.v2.ex.WebServiceException;
import com.oup.eac.ws.v2.ex.WebServiceValidationException;
import com.oup.eac.ws.v2.service.WsCustomerLookup;

public class UpdateUserAccountAdapterMockTest /* extends AbstractMockTest */ {
	/*
	 * 
	 * private UpdateUserAccountAdapterImpl sut;
	 * 
	 * private MessageSource messageSource; private CustomerService customerService;
	 * private WsCustomerLookup customerLookup; private UsernameValidator
	 * usernameValidator;
	 * 
	 * public UpdateUserAccountAdapterMockTest() throws NamingException { super(); }
	 * 
	 * @Before public void setup() { this.messageSource =
	 * createMock(MessageSource.class); this.customerLookup =
	 * createMock(WsCustomerLookup.class); this.customerService =
	 * createMock(CustomerService.class); this.usernameValidator =
	 * createMock(UsernameValidator.class); setMocks(messageSource, customerLookup,
	 * customerService, usernameValidator);
	 * 
	 * this.sut = new UpdateUserAccountAdapterImpl(messageSource, customerService,
	 * customerLookup, usernameValidator); }
	 * 
	 * @Test public void testFailureUnknownUser() throws WebServiceException,
	 * UsernameExistsException,
	 * ServiceLayerException,UserLoginCredentialAlreadyExistsException {
	 * UpdateUserAccountRequest request = new UpdateUserAccountRequest(); WsUserId
	 * wsUserId = new WsUserId(); UpdateUser user = new UpdateUser();
	 * request.setUser(user); request.setWsUserId(wsUserId); Customer customer =
	 * getCustomer();
	 * 
	 * String randomMessage = UUID.randomUUID().toString();
	 * EasyMock.expect(customerLookup.getCustomerByWsUserId(wsUserId)).andThrow(new
	 * WebServiceValidationException(randomMessage));
	 * 
	 * // customerService.updateCustomerFromWS(customer, null); //expectLastCall();
	 * 
	 * EasyMock.replay(getMocks()); UpdateUserAccountResponse response =
	 * sut.updateUserAccount(request); EasyMock.verify(getMocks());
	 * 
	 * Assert.assertEquals(StatusCode.CLIENT_ERROR,
	 * response.getErrorStatus().getStatusCode());
	 * Assert.assertEquals(randomMessage,
	 * response.getErrorStatus().getStatusReason()); }
	 * 
	 * @Test public void testFailureServiceLayerValidationException() throws
	 * WebServiceException, ServiceLayerException, ErightsException { String
	 * username = "davidhay"; UpdateUserAccountRequest request = new
	 * UpdateUserAccountRequest(); WsUserId wsUserId = new WsUserId(); UpdateUser
	 * user = getBasicUpdateUser(username); request.setUser(user);
	 * request.setWsUserId(wsUserId);
	 * 
	 * String randomMessage = UUID.randomUUID().toString(); Customer customer =
	 * getCustomer();
	 * EasyMock.expect(customerLookup.getCustomerByWsUserId(wsUserId)).andReturn(
	 * customer);
	 * 
	 * //
	 * EasyMock.expect(customerService.getCustomerByUsername(username)).andReturn(
	 * customer);// we are not changing the username
	 * customerService.updateCustomerFromWS(customer, null);
	 * expectLastCall().andThrow(new
	 * ServiceLayerValidationException(randomMessage));
	 * EasyMock.expect(this.usernameValidator.isValid("davidhay")).andReturn(true);
	 * // customerService.updateCustomer(customer, false); //
	 * expectLastCall().andThrow(new
	 * ServiceLayerValidationException(randomMessage));
	 * 
	 * EasyMock.replay(getMocks()); UpdateUserAccountResponse response =
	 * sut.updateUserAccount(request); EasyMock.verify(getMocks());
	 * 
	 * Assert.assertEquals(StatusCode.CLIENT_ERROR,
	 * response.getErrorStatus().getStatusCode());
	 * Assert.assertEquals(randomMessage,
	 * response.getErrorStatus().getStatusReason()); }
	 * 
	 * @Test public void testFailureServiceLayerException() throws
	 * WebServiceException, ServiceLayerException, ErightsException { String
	 * randomMessage = UUID.randomUUID().toString(); try { String username =
	 * "davidhay"; UpdateUserAccountRequest request = new
	 * UpdateUserAccountRequest(); WsUserId wsUserId = new WsUserId(); UpdateUser
	 * user = getBasicUpdateUser(username); request.setUser(user);
	 * request.setWsUserId(wsUserId);
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
	 * // customerService.updateCustomer(customer, false);
	 * //expectLastCall().andThrow(new ServiceLayerException(randomMessage));
	 * customerService.updateCustomerFromWS(customer, null);
	 * expectLastCall().andThrow(new ServiceLayerException(randomMessage));
	 * 
	 * EasyMock.replay(getMocks());
	 * 
	 * @SuppressWarnings("unused") UpdateUserAccountResponse response =
	 * sut.updateUserAccount(request); Assert.fail("exception expected"); } catch
	 * (WebServiceException wse) {
	 * Assert.assertEquals(randomMessage,wse.getCause().getMessage());
	 * Assert.assertEquals("problem updating user account",wse.getMessage()); }
	 * finally { EasyMock.verify(getMocks()); } }
	 * 
	 * @Test public void testSuccessSameUserName() throws WebServiceException,
	 * ServiceLayerException, ErightsException { String username = "davidhay";
	 * UpdateUserAccountRequest request = new UpdateUserAccountRequest(); WsUserId
	 * wsUserId = new WsUserId(); UpdateUser user = getBasicUpdateUser(username);
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
	 * EasyMock.expect(this.usernameValidator.isValid("davidhay")).andReturn(true);
	 * EasyMock.replay(getMocks()); UpdateUserAccountResponse response =
	 * sut.updateUserAccount(request); EasyMock.verify(getMocks());
	 * 
	 * Assert.assertNull(response.getErrorStatus()); }
	 * 
	 * @Test public void testSuccessSameUserNameBlankPassword() throws
	 * WebServiceException, ServiceLayerException, ErightsException { String
	 * username = "davidhay"; UpdateUserAccountRequest request = new
	 * UpdateUserAccountRequest(); WsUserId wsUserId = new WsUserId(); UpdateUser
	 * user = getBasicUpdateUser(username);
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
	 * EasyMock.replay(getMocks()); UpdateUserAccountResponse response =
	 * sut.updateUserAccount(request); EasyMock.verify(getMocks());
	 * 
	 * Assert.assertNull(response.getErrorStatus()); }
	 * 
	 * @Test public void testFaliureIPCredentialName() throws WebServiceException,
	 * ServiceLayerException {
	 * 
	 * UpdateUserAccountRequest request = new UpdateUserAccountRequest(); WsUserId
	 * wsUserId = new WsUserId(); UpdateUser user = new UpdateUser();
	 * user.setEmailAddress("david.hay@test.com"); Credential cred = new
	 * Credential(); IPCredential ip = new IPCredential(); ip.setIP("127.0.0.1");
	 * cred.setIpCredential(ip); user.setCredentials(cred); request.setUser(user);
	 * request.setWsUserId(wsUserId);
	 * 
	 * Customer customer = getCustomer();
	 * EasyMock.expect(customerLookup.getCustomerByWsUserId(wsUserId)).andReturn(
	 * customer); EasyMock.replay(getMocks());
	 * 
	 * UpdateUserAccountResponse response = sut.updateUserAccount(request);
	 * EasyMock.verify(getMocks());
	 * 
	 * Assert.assertEquals(StatusCode.CLIENT_ERROR,
	 * response.getErrorStatus().getStatusCode());
	 * Assert.assertEquals("IP Credentials are not supported.",
	 * response.getErrorStatus().getStatusReason()); }
	 * 
	 * @Test public void testSuccessDifferentUserName() throws WebServiceException,
	 * ServiceLayerException, ErightsException { String username = "davidhay";
	 * UpdateUserAccountRequest request = new UpdateUserAccountRequest(); WsUserId
	 * wsUserId = new WsUserId(); UpdateUser user = getBasicUpdateUser(username);
	 * request.setUser(user); request.setWsUserId(wsUserId);
	 * 
	 * Customer customer = getCustomer();
	 * EasyMock.expect(customerLookup.getCustomerByWsUserId(wsUserId)).andReturn(
	 * customer);
	 * 
	 * //EasyMock.expect(customerService.getCustomerByUsername(username)).andReturn(
	 * null);// username not taken
	 * 
	 * //customerService.updateCustomer(customer, false); //expectLastCall();
	 * customerService.updateCustomerFromWS(customer, null); expectLastCall();
	 * EasyMock.expect(this.usernameValidator.isValid("davidhay")).andReturn(true);
	 * 
	 * EasyMock.expectLastCall();
	 * 
	 * EasyMock.replay(getMocks()); UpdateUserAccountResponse response =
	 * sut.updateUserAccount(request); EasyMock.verify(getMocks());
	 * 
	 * Assert.assertNull(response.getErrorStatus());
	 * 
	 * }
	 * 
	 * @Test public void testFailureUserNameAlreadyTaken() throws
	 * WebServiceException, ServiceLayerException , ErightsException{ String
	 * username = "davidhay"; UpdateUserAccountRequest request = new
	 * UpdateUserAccountRequest(); WsUserId wsUserId = new WsUserId(); UpdateUser
	 * user = getBasicUpdateUser(username); request.setUser(user);
	 * request.setWsUserId(wsUserId);
	 * 
	 * Customer customer1 = getCustomer(); Customer customer2 = getCustomer();
	 * EasyMock.expect(customerLookup.getCustomerByWsUserId(wsUserId)).andReturn(
	 * customer1);
	 * 
	 * //EasyMock.expect(customerService.getCustomerByUsername(username)).andReturn(
	 * customer2);// username is already taken!
	 * customerService.updateCustomerFromWS(customer1, null);
	 * customerService.updateCustomerFromWS(customer2, null); expectLastCall();
	 * EasyMock.expect(this.usernameValidator.isValid("davidhay")).andReturn(true);
	 * EasyMock.replay(getMocks()); // UpdateUserAccountResponse response =
	 * sut.updateUserAccount(request); //EasyMock.verify(getMocks());
	 * 
	 * // Assert.assertEquals(StatusCode.CLIENT_ERROR,
	 * response.getErrorStatus().getStatusCode()); //
	 * Assert.assertEquals(CreateUserAccountAdapterImpl.ERR_USERNAME_ALREADY_TAKEN,
	 * response.getErrorStatus().getStatusReason());
	 * 
	 * }
	 * 
	 * @Test public void testFailureInvalidEmailAddress() throws
	 * WebServiceException, ServiceLayerException { String username = "davidhay";
	 * UpdateUser user = getBasicUpdateUser(username);
	 * user.setEmailAddress("david@test"); checkValidationFailure(user,
	 * "The email address is invalid."); }
	 * 
	 * @Test public void testFailureInvalidTimeZone() throws WebServiceException,
	 * ServiceLayerException { UpdateUser user = getBasicUpdateUser("davidhay");
	 * user.setTimeZone("Oxford/Europe");
	 * EasyMock.expect(this.usernameValidator.isValid("davidhay")).andReturn(true);
	 * checkValidationFailure(user,
	 * "The Time Zone ID is not valid : Oxford/Europe"); }
	 * 
	 * @Test public void testFailureInvalidUserName() throws WebServiceException,
	 * ServiceLayerException { String username = "      "; UpdateUser user =
	 * getBasicUpdateUser(username); checkValidationFailure(user,
	 * "The username cannot be blank."); }
	 * 
	 * @Test public void testFailureInvalidPassword() throws WebServiceException,
	 * ServiceLayerException { String username = "davidhay"; UpdateUser user =
	 * getBasicUpdateUser(username);
	 * user.getCredentials().getPasswordCredential().setPassword("ABC");
	 * Class<Object[]> clazz = Object[].class; expect(
	 * this.messageSource.getMessage(EasyMock.eq(PasswordUtils.
	 * INVALID_PASSWORD_MSG_CODE), anyObject(clazz), EasyMock
	 * .eq(PasswordUtils.DEFAULT_INVALID_PASSWORD_MSG),
	 * anyObject(Locale.class))).andReturn("ERROR_MESSAGE");
	 * EasyMock.expect(this.usernameValidator.isValid("davidhay")).andReturn(true);
	 * checkValidationFailure(user, "ERROR_MESSAGE"); }
	 * 
	 * 
	 * 
	 * @Test public void testSuccessSameUserNameSamePassword() throws
	 * WebServiceException, ServiceLayerException, ErightsException { Customer
	 * customer = getCustomer(); Password originalPassword =
	 * customer.getWrappedPassword();
	 * 
	 * String username = "davidhay"; UpdateUserAccountRequest request = new
	 * UpdateUserAccountRequest(); WsUserId wsUserId = new WsUserId(); UpdateUser
	 * user = getBasicUpdateUser(username);//
	 * user.getCredentials().getPasswordCredential().setPassword(originalPassword.
	 * getValue());// same password request.setUser(user);
	 * request.setWsUserId(wsUserId);
	 * 
	 * EasyMock.expect(customerLookup.getCustomerByWsUserId(wsUserId)).andReturn(
	 * customer);
	 * 
	 * customerService.updateCustomerFromWS(customer, null);
	 * 
	 * expectLastCall(); //
	 * EasyMock.expect(customerService.getCustomerByUsername(username)).andReturn(
	 * customer);// we are not changing the username
	 * 
	 * // customerService.updateCustomer(eqCustomerUsernameAndPassword("davidhay",
	 * originalPassword), EasyMock.eq(false)); // expectLastCall();
	 * 
	 * //expectLastCall();
	 * 
	 * EasyMock.expect(this.usernameValidator.isValid("davidhay")).andReturn(true);
	 * 
	 * EasyMock.replay(getMocks()); UpdateUserAccountResponse response =
	 * sut.updateUserAccount(request); EasyMock.verify(getMocks());
	 * 
	 * Assert.assertNull(response.getErrorStatus()); }
	 * 
	 * 
	 * @Test public void
	 * testChangePasswordPolicyViolatedCurrentPasswordSameAsPreviousPasswordException
	 * () { try{ Customer customer = getCustomer(); String username = "davidhay";
	 * UpdateUserAccountRequest request = new UpdateUserAccountRequest(); WsUserId
	 * wsUserId = new WsUserId(); UpdateUser user = getBasicUpdateUser(username);//
	 * user.getCredentials().getPasswordCredential().setPassword("password1Az");//
	 * password same as current password request.setUser(user);
	 * request.setWsUserId(wsUserId);
	 * EasyMock.expect(customerLookup.getCustomerByWsUserId(wsUserId)).andReturn(
	 * customer);
	 * 
	 * //EasyMock.expect(customerService.getCustomerByUsername(username)).andReturn(
	 * customer); // customerService.updateCustomer(customer,false); //
	 * EasyMock.expectLastCall().andThrow(new
	 * PasswordPolicyViolatedServiceLayerException("New password should not match current password"
	 * )); // expectLastCall();
	 * 
	 * // expectLastCall();
	 * 
	 * customerService.updateCustomerFromWS(customer, null);
	 * 
	 * expectLastCall().andThrow(new
	 * PasswordPolicyViolatedServiceLayerException("New password should not match current password"
	 * ));
	 * EasyMock.expect(this.usernameValidator.isValid("davidhay")).andReturn(true);
	 * 
	 * EasyMock.replay(getMocks()); UpdateUserAccountResponse response =
	 * sut.updateUserAccount(request); EasyMock.verify(getMocks());
	 * Assert.assertEquals(response.getErrorStatus().getStatusReason()
	 * ,"New password should not match current password" ); } catch(Exception ex){
	 * Assert.assertEquals(PasswordPolicyViolatedServiceLayerException.class,
	 * ex.getClass());
	 * Assert.assertEquals("New password should not match current password",
	 * ex.getMessage()); } }
	 * 
	 * @Test public void
	 * testChangePasswordPolicyViolatedPreviousPasswordsException() { try{ Customer
	 * customer = getCustomer(); String username = "davidhay";
	 * UpdateUserAccountRequest request = new UpdateUserAccountRequest(); WsUserId
	 * wsUserId = new WsUserId(); UpdateUser user = getBasicUpdateUser(username);//
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
	 * //expectLastCall(); customerService.updateCustomerFromWS(customer, null);
	 * 
	 * expectLastCall().andThrow(new
	 * PasswordPolicyViolatedServiceLayerException("New password should not match previous 4 passwords"
	 * ));
	 * EasyMock.expect(this.usernameValidator.isValid("davidhay")).andReturn(true);
	 * 
	 * EasyMock.replay(getMocks()); UpdateUserAccountResponse response =
	 * sut.updateUserAccount(request); EasyMock.verify(getMocks());
	 * Assert.assertEquals(response.getErrorStatus().getStatusReason()
	 * ,"New password should not match previous 4 passwords" ); } catch(Exception
	 * ex){ Assert.assertEquals(PasswordPolicyViolatedServiceLayerException.class,
	 * ex.getClass());
	 * Assert.assertEquals("New password should not match previous 4 passwords",
	 * ex.getMessage()); } }
	 * 
	 * 
	 * private void checkValidationFailure(UpdateUser user, String
	 * validationMessage) throws WebServiceException { UpdateUserAccountRequest
	 * request = new UpdateUserAccountRequest(); WsUserId wsUserId = new WsUserId();
	 * request.setUser(user); request.setWsUserId(wsUserId);
	 * 
	 * Customer customer1 = getCustomer();
	 * EasyMock.expect(customerLookup.getCustomerByWsUserId(wsUserId)).andReturn(
	 * customer1);
	 * 
	 * EasyMock.replay(getMocks()); UpdateUserAccountResponse response =
	 * sut.updateUserAccount(request); EasyMock.verify(getMocks());
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
	 * private UpdateUser getBasicUpdateUser(String username) { UpdateUser user =
	 * new UpdateUser(); Credential creds = new Credential(); PasswordCredential
	 * passwordCredential = new PasswordCredential();
	 * passwordCredential.setUserName(username);
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
		 * @see UpdateUserAccountAdapterMockTest
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
	 */}
