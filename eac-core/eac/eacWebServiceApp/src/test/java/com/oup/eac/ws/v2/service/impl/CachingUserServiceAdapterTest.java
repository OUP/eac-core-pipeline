package com.oup.eac.ws.v2.service.impl;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.naming.NamingException;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.easymock.IArgumentMatcher;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.context.MessageSource;

import com.oup.eac.common.mock.AbstractMockTest;
import com.oup.eac.common.utils.crypto.PasswordUtils;
import com.oup.eac.common.utils.username.UsernameValidator;
import com.oup.eac.domain.Answer;
import com.oup.eac.domain.Component;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.Element;
import com.oup.eac.domain.ExternalCustomerId;
import com.oup.eac.domain.Question;
import com.oup.eac.domain.util.SampleDataFactory;
import com.oup.eac.dto.ChangePasswordDto;
import com.oup.eac.dto.CustomerSessionDto;
import com.oup.eac.dto.ExternalCustomerIdDto;
import com.oup.eac.service.CustomerService;
import com.oup.eac.service.ExternalIdService;
import com.oup.eac.service.InvalidCredentialsServiceLayerException;
import com.oup.eac.service.ServiceLayerException;
import com.oup.eac.service.exceptions.AccountLockedServiceLayerException;
import com.oup.eac.service.exceptions.PasswordPolicyViolatedServiceLayerException;
import com.oup.eac.service.exceptions.PasswordSameAsOldServiceLayerException;
import com.oup.eac.service.exceptions.SessionConcurrencyServiceLayerException;
import com.oup.eac.service.exceptions.SessionNotFoundServiceLayerException;
import com.oup.eac.service.exceptions.UserHasNoEmailServiceLayerException;
import com.oup.eac.ws.v2.binding.access.AuthenticateResponse;
import com.oup.eac.ws.v2.binding.access.ChangePasswordResponse;
import com.oup.eac.ws.v2.binding.access.LogoutResponse;
import com.oup.eac.ws.v2.binding.access.ResetPasswordResponse;
import com.oup.eac.ws.v2.binding.common.CredentialName;
import com.oup.eac.ws.v2.binding.common.ErrorStatus;
import com.oup.eac.ws.v2.binding.common.Identifiers;
import com.oup.eac.ws.v2.binding.common.RegistrationInformation;
import com.oup.eac.ws.v2.binding.common.User;
import com.oup.eac.ws.v2.binding.common.WsUserId;
import com.oup.eac.ws.v2.binding.common.types.StatusCode;
import com.oup.eac.ws.v2.binding.userdata.RegistrationInformationResponse;
import com.oup.eac.ws.v2.binding.userdata.UserNameResponse;
import com.oup.eac.ws.v2.ex.WebServiceValidationException;
import com.oup.eac.ws.v2.service.UserServiceAdapter;
import com.oup.eac.ws.v2.service.WsCustomerLookup;
import com.oup.eac.ws.v2.service.entitlements.CustomerConverter;
import com.oup.eac.ws.v2.service.utils.IdUtils;



public class CachingUserServiceAdapterTest extends AbstractMockTest {

    private ExternalIdService externalIdService;

    private CustomerService customerService;
    
    private CustomerConverter customerConverter;
    
    private UserServiceAdapter cachingUserServiceAdapter;
    
    private CachingUserServiceAdapter userviceAdapter;
    
    private Customer customer;
    
    private Customer customerWithAnswers;
    
    private CustomerSessionDto customerSessionDto;
    
    private User user;
    
    private Answer answer, answer2, answer3;

    private Question question, question2;
    
    private WsCustomerLookup customerLookup;

    private MessageSource messageSource;

    private ExternalCustomerIdDto extCustIdDto;
    
    private UsernameValidator validator;

    private User userWithAnswers;
    
    private static final String SESSION_KEY = "abcdefg";
    
    private static final String USERNAME="username";
    
    private final String RESETPASSWORDWS_BASEURL="http://localhost:8080/eac/";
    
      
    public CachingUserServiceAdapterTest() throws NamingException {
        super();
    }    
    
    @Before
    public void setUp() throws Exception {
        this.customer = new Customer();
        
    	Component component = SampleDataFactory.createComponent("label.key");
    	question = SampleDataFactory.createQuestion();
    	Element element = SampleDataFactory.createElement(question);
    	SampleDataFactory.createField(component, element, 1);
    	question2 = SampleDataFactory.createQuestion();
    	Element element2 = SampleDataFactory.createElement(question2);
    	SampleDataFactory.createField(component, element2, 2);
        customer = SampleDataFactory.createCustomer();
        customerWithAnswers = SampleDataFactory.createCustomer();
        customerSessionDto = new CustomerSessionDto();
        customerSessionDto.setCustomer(customer);
        customerSessionDto.setSession(SESSION_KEY);
        user = convertCustomer(customer);
        userWithAnswers = convertCustomer(customerWithAnswers);
        answer = SampleDataFactory.createAnswer(customerWithAnswers, question);
        answer2 = SampleDataFactory.createAnswer(customerWithAnswers, question2);
        
        customerService = EasyMock.createMock(CustomerService.class);
        customerConverter = EasyMock.createMock(CustomerConverter.class);
        customerLookup = EasyMock.createMock(WsCustomerLookup.class);
        messageSource = EasyMock.createMock(MessageSource.class);
        this.externalIdService = EasyMock.createMock(ExternalIdService.class);
        this.validator = EasyMock.createMock(UsernameValidator.class);
        setMocks(customerService, customerConverter, customerLookup, externalIdService, messageSource, validator);
        cachingUserServiceAdapter = new CachingUserServiceAdapter(customerService, customerConverter, customerLookup, externalIdService , messageSource, validator);
        this.extCustIdDto = new ExternalCustomerIdDto(customer, new ArrayList<ExternalCustomerId>());
        
        userviceAdapter = new CachingUserServiceAdapter(customerService, customerConverter, customerLookup, externalIdService, messageSource, validator);
    }
    
    @Test
    public void testGetUsername() throws Exception {
        
        WsUserId wsUserId = new WsUserId();
        expect(customerLookup.getCustomerByWsUserId(wsUserId)).andReturn(customer);
        
        replay(getMocks());
        
        UserNameResponse userNameResponse = cachingUserServiceAdapter.getUserName(wsUserId);
        
        assertEquals(userNameResponse.getUserName(), customer.getFullName());
        
        verify(getMocks());
    }
   
    @Test
    public void testGetUsernameCustomerNotFound() throws Exception {
        WsUserId wsUserId = new WsUserId();
        expect(customerLookup.getCustomerByWsUserId(wsUserId)).andThrow(new WebServiceValidationException("not found"));
        
        replay(getMocks());
        
        UserNameResponse result = cachingUserServiceAdapter.getUserName(wsUserId);
        Assert.assertEquals(StatusCode.CLIENT_ERROR,result.getErrorStatus().getStatusCode());
        Assert.assertEquals("not found",result.getErrorStatus().getStatusReason());
        
        verify(getMocks());
    }
    
    @Test
    public void testGetRegistrationInformation() throws Exception {
        
    	Set<Answer> answers = new HashSet<Answer>();
    	answers.add(answer);
        WsUserId wsUserId = new WsUserId();
        expect(customerLookup.getCustomerByWsUserId(wsUserId)).andReturn(customer);
        ExternalCustomerIdDto externalDto = new ExternalCustomerIdDto(customerWithAnswers);
        expect(externalIdService.getExternalCustomerIds(customer,"SYSTEM1")).andStubReturn(externalDto);
        expect(this.customerLookup.getCustomerWithAnswers(customer.getId())).andReturn(answers);
        expect(customerConverter.convertCustomerToUser(externalDto)).andReturn(userWithAnswers);
        
        replay(getMocks());
        
        RegistrationInformationResponse response = cachingUserServiceAdapter.getRegistrationInformation("SYSTEM1",wsUserId); 
        
        assertNotNull(response);
        assertNotNull(response.getRegistrationInformationResponseSequence().getUser());
        
        User responseUser = response.getRegistrationInformationResponseSequence().getUser();
        
        Assert.assertEquals(userWithAnswers, responseUser);
        
        assertEquals(userWithAnswers.getFirstName(), customerWithAnswers.getFirstName());
        assertEquals(userWithAnswers.getLastName(), customerWithAnswers.getFamilyName());
        
        RegistrationInformation[] registrationInformations = response.getRegistrationInformationResponseSequence().getRegistrationInformation();
        
        assertNotNull(registrationInformations);
        assertEquals(1, registrationInformations.length);
        
        RegistrationInformation registrationInformation = getRegistrationInformationForKey(question.getDescription(), registrationInformations);
        
        assertNotNull(registrationInformation);
        assertEquals(question.getDescription(), registrationInformation.getRegistrationKey());
        assertEquals(answer.getAnswerText(), registrationInformation.getRegistrationValue());
        
        
        verify(getMocks());
    }
    
    @Test
    public void testGetRegistrationInformationCustomerNotFound() throws Exception {
        WsUserId wsUserId = new WsUserId();
        expect(customerLookup.getCustomerByWsUserId(wsUserId)).andThrow(new WebServiceValidationException("oops"));
        
        replay(getMocks());
        
        RegistrationInformationResponse response = cachingUserServiceAdapter.getRegistrationInformation("SYSTEM1",wsUserId); 
        
        assertNotNull(response);
        assertNull(response.getRegistrationInformationResponseSequence());
        Assert.assertEquals(StatusCode.CLIENT_ERROR, response.getErrorStatus().getStatusCode());
        Assert.assertEquals("oops", response.getErrorStatus().getStatusReason());
        verify(getMocks());
    }
    
    @Test
    public void testAuthenticateNoUsername() throws Exception {
        replay(getMocks());
        AuthenticateResponse response = cachingUserServiceAdapter.authenticate("", "password");
        assertNotNull(response);
        ErrorStatus ErrorStatus = response.getErrorStatus();
        assertEquals(StatusCode.CLIENT_ERROR, ErrorStatus.getStatusCode());
        assertEquals("The username cannot be blank.", ErrorStatus.getStatusReason());
        verify(getMocks());
    }
    
    @Test
    public void testAuthenticateNoPassword() throws Exception {
        replay(getMocks());
        AuthenticateResponse response = cachingUserServiceAdapter.authenticate(USERNAME, "");
        
        assertNotNull(response);
        ErrorStatus ErrorStatus = response.getErrorStatus();
        assertEquals(StatusCode.CLIENT_ERROR, ErrorStatus.getStatusCode());
        assertEquals("The password cannot be blank.", ErrorStatus.getStatusReason());
        verify(getMocks());
    }
    
    @Test
    public void testAuthenticate() throws Exception {
    	
        expect(customerLookup.getCustomerByWsUserId(eqUsername(USERNAME))).andStubReturn(customer);
    	expect(customerService.getCustomerByUsernameAndPassword(USERNAME, "password")).andReturn(customerSessionDto);
    	expect(externalIdService.getExternalCustomerIds(customer, null)).andReturn(extCustIdDto);
    	expect(customerConverter.convertCustomerToUser(extCustIdDto)).andReturn(user);
    	
    	replay(getMocks());
    	
        AuthenticateResponse response = cachingUserServiceAdapter.authenticate(USERNAME, "password");
        assertNotNull(response);
        ErrorStatus ErrorStatus = response.getErrorStatus();        
        assertNull(ErrorStatus);
        
        User user = response.getAuthenticateResponseSequence().getUser();
        
        assertNotNull(user);
        assertEquals(customer.getFirstName(), user.getFirstName());
        assertEquals(customer.getFamilyName(), user.getLastName());
        assertEquals(customer.getEmailAddress(), user.getEmailAddress());
        
        verify(getMocks());
    }
    
    @Test
    public void testAuthenticateUppercase() throws Exception {
    	
        expect(customerLookup.getCustomerByWsUserId(eqUsername(USERNAME))).andStubReturn(customer);
    	expect(customerService.getCustomerByUsernameAndPassword(USERNAME, "password")).andReturn(customerSessionDto);
    	expect(externalIdService.getExternalCustomerIds(customer, null)).andReturn(extCustIdDto);
    	expect(customerConverter.convertCustomerToUser(extCustIdDto)).andReturn(user);
    	
    	replay(getMocks());
    	
        AuthenticateResponse response = cachingUserServiceAdapter.authenticate(USERNAME.toUpperCase(), "password");
        assertNotNull(response);
        ErrorStatus ErrorStatus = response.getErrorStatus();        
        assertNull(ErrorStatus);
        
        User user = response.getAuthenticateResponseSequence().getUser();
        
        assertNotNull(user);
        assertEquals(customer.getFirstName(), user.getFirstName());
        assertEquals(customer.getFamilyName(), user.getLastName());
        assertEquals(customer.getEmailAddress(), user.getEmailAddress());
        
        verify(getMocks());
    }
    
    @Ignore
    @Test
    public void testAuthenticateThrowsWebServiceValidationException() throws Exception {
        
    	final String errorMessage = "The username cannot be blank";
    
    	ExternalCustomerIdDto externalDto = new ExternalCustomerIdDto(customerWithAnswers);
    	expect(customerLookup.getCustomerByWsUserId(eqUsername(USERNAME))).andThrow(new WebServiceValidationException(errorMessage));    	
    	expect(customerService.getCustomerByUsernameAndPassword(USERNAME, "password")).andStubReturn(customerSessionDto);
    	
    	/*userviceAdapter.validateAuthenticate("", "password");
    	EasyMock.expectLastCall().andThrow(new WebServiceValidationException(errorMessage));*/
    	 expect(externalIdService.getExternalCustomerIds(EasyMock.anyObject(Customer.class), EasyMock.anyObject(String.class))).andStubReturn(externalDto);
    	 expect(customerConverter.convertCustomerToUser(externalDto)).andReturn(userWithAnswers);
    	testException(StatusCode.CLIENT_ERROR, errorMessage);
    }
    
    @Test
    public void testAuthenticateThrowsAccountLockedServiceLayerException() throws Exception {
    	final String errorMessage = "account locked";
    	expect(customerLookup.getCustomerByWsUserId(eqUsername(USERNAME))).andStubReturn(new Customer());
    	
    	expect(customerService.getCustomerByUsernameAndPassword(USERNAME, "password")).andThrow(new AccountLockedServiceLayerException(errorMessage));
    	testException(StatusCode.CLIENT_ERROR, errorMessage);
    }
    
    @Test
    public void testAuthenticateThrowsSessionConcurrencyServiceLayerException() throws Exception {
    	final String errorMessage = "session concurrency";
        expect(customerLookup.getCustomerByWsUserId(eqUsername(USERNAME))).andStubReturn(new Customer());
    	expect(customerService.getCustomerByUsernameAndPassword(USERNAME, "password")).andThrow(new SessionConcurrencyServiceLayerException(errorMessage));
    	testException(StatusCode.CLIENT_ERROR, errorMessage);
    }
    
    @Ignore
    @Test
    public void testAuthenticateThrowsInvalidCredentialsServiceLayerException() throws Exception {
    	final String errorMessage = "invalid credentials";    	
    	String message = "Internal Server Error Authentication failed because no auth profile matched ";
    	InvalidCredentialsServiceLayerException e =  new InvalidCredentialsServiceLayerException();
    	org.synyx.messagesource.Messages messages = new org.synyx.messagesource.Messages();
    	Map<String, String> map = new HashMap<String, String>();
    	map.put("Message", message);
    	messages.setMessages(null, map);
    	
    	expect(customerLookup.getCustomerByWsUserId(eqUsername(USERNAME))).andReturn(new Customer());
    	expect(customerService.getCustomerByUsernameAndPassword(USERNAME, "password")).andThrow(new InvalidCredentialsServiceLayerException(message));
    	testException(StatusCode.CLIENT_ERROR, errorMessage);
    }
 
    @Test
    public void testAuthenticateThrowsServiceLayerException() throws Exception {
    	final String errorMessage = "erights error";
    	expect(customerLookup.getCustomerByWsUserId(eqUsername(USERNAME))).andStubReturn(new Customer());
    	expect(customerService.getCustomerByUsernameAndPassword(USERNAME, "password")).andThrow(new ServiceLayerException(errorMessage));
    	testException(StatusCode.SERVER_ERROR, errorMessage);
    }
    
    @Test
    public void testLogoutNoSession() throws Exception {
        LogoutResponse response = cachingUserServiceAdapter.logout("");
        assertNotNull(response);
        ErrorStatus ErrorStatus = response.getErrorStatus();
        assertEquals(StatusCode.CLIENT_ERROR, ErrorStatus.getStatusCode());
        assertEquals("The session token cannot be blank.", ErrorStatus.getStatusReason());
    }
    
    @Test
    public void testLogout() throws Exception {
    	customerService.logout(SESSION_KEY);
    	EasyMock.expectLastCall();
    	
    	replay(getMocks());
    	
        LogoutResponse response = cachingUserServiceAdapter.logout(SESSION_KEY);
        assertNotNull(response);
        ErrorStatus ErrorStatus = response.getErrorStatus();
        assertNull(ErrorStatus);
        
        verify(getMocks());
    }
    
    @Test
    public void testLogoutSessionNotFoundServiceLayerException() throws Exception {
    	
    	customerService.logout(SESSION_KEY);
    	EasyMock.expectLastCall().andThrow(new SessionNotFoundServiceLayerException("no session found"));
    	
    	replay(getMocks());
    	
        LogoutResponse response = cachingUserServiceAdapter.logout(SESSION_KEY);
        assertNotNull(response);
        ErrorStatus ErrorStatus = response.getErrorStatus();
        assertNull(ErrorStatus);
        
        verify(getMocks());
    }
    
    @Test
    public void testLogoutServiceLayerException() throws Exception {
    	
    	customerService.logout(SESSION_KEY);
    	EasyMock.expectLastCall().andThrow(new ServiceLayerException("erights error"));
    	
    	replay(getMocks());
    	
        LogoutResponse response = cachingUserServiceAdapter.logout(SESSION_KEY);
        assertNotNull(response);
        ErrorStatus ErrorStatus = response.getErrorStatus();
        assertEquals(StatusCode.SERVER_ERROR, ErrorStatus.getStatusCode());
        assertNotNull(ErrorStatus.getStatusReason());
        assertEquals("erights error", ErrorStatus.getStatusReason());
        
        verify(getMocks());
    }
    
    @Test
    public void testChangePasswordNoUsername() throws Exception {
        
        expect(customerLookup.getCustomerByWsUserId(null)).andThrow(new WebServiceValidationException("oops"));
        replay(getMocks());
        
        ChangePasswordResponse response = cachingUserServiceAdapter.changePassword(null, "newPassword1");
        verify(getMocks());
        assertNotNull(response);
        ErrorStatus ErrorStatus = response.getErrorStatus();
        assertEquals(StatusCode.CLIENT_ERROR, ErrorStatus.getStatusCode());
        assertEquals("oops", ErrorStatus.getStatusReason());
    }
    
    
    @Test
    public void testChangePasswordNoNewPassword() throws Exception {
        replay(getMocks());
        ChangePasswordResponse response = cachingUserServiceAdapter.changePassword(getWsUserId(USERNAME), null);
        verify(getMocks());
        assertNotNull(response);
        ErrorStatus ErrorStatus = response.getErrorStatus();
        assertEquals(StatusCode.CLIENT_ERROR, ErrorStatus.getStatusCode());
        assertEquals("The password cannot be blank.", ErrorStatus.getStatusReason());
    }
    
    @Test
    public void testChangePasswordInvalidNewPassword() throws Exception {
        WsUserId wsUserId = getWsUserId(USERNAME);
        
        Class<Object[]> clazz = Object[].class;
        expect(this.messageSource.getMessage(EasyMock.eq(PasswordUtils.INVALID_PASSWORD_MSG_CODE), anyObject(clazz), EasyMock.eq(PasswordUtils.DEFAULT_INVALID_PASSWORD_MSG), anyObject(Locale.class))).andReturn("ERROR_MESSAGE");
        
        replay(getMocks());
        ChangePasswordResponse response = cachingUserServiceAdapter.changePassword(wsUserId, "password");
        verify(getMocks());
        assertNotNull(response);
        ErrorStatus ErrorStatus = response.getErrorStatus();
        assertEquals(StatusCode.CLIENT_ERROR, ErrorStatus.getStatusCode());
        assertEquals("ERROR_MESSAGE", ErrorStatus.getStatusReason());
    }
    
    @Ignore //new Password can't be same as OLD NFR-SEC020
    @Test
    public void testChangePasswordPasswordSameAsOldServiceLayerException() throws PasswordPolicyViolatedServiceLayerException, Exception {
    	
        WsUserId wsUserId = getWsUserId(USERNAME);
        expect(customerLookup.getCustomerByWsUserId(wsUserId)).andReturn(customer);
        
    	customerService.saveChangeCustomerPassword(EasyMock.isA(ChangePasswordDto.class), customer);
    	EasyMock.expectLastCall().andThrow(new PasswordSameAsOldServiceLayerException());
    	
    	replay(getMocks());
    	
    	ChangePasswordResponse response = cachingUserServiceAdapter.changePassword(wsUserId, "newPassword1");
        assertNotNull(response);
        ErrorStatus ErrorStatus = response.getErrorStatus();
        assertEquals(StatusCode.CLIENT_ERROR, ErrorStatus.getStatusCode());
        assertNotNull(ErrorStatus.getStatusReason());
        assertEquals("The new password cannot be the same as the old password.", ErrorStatus.getStatusReason());
        
        verify(getMocks());
    }
    
    @Test
    public void testChangePasswordServiceLayerException() throws Exception {
    	
        WsUserId wsUserId = getWsUserId(USERNAME);
        expect(customerLookup.getCustomerByWsUserId(wsUserId)).andReturn(customer);
        
    	customerService.saveChangeCustomerPassword(EasyMock.anyObject(ChangePasswordDto.class), EasyMock.anyObject(Customer.class));
    	EasyMock.expectLastCall().andThrow(new ServiceLayerException("An error"));
    	replay(getMocks());
    	
    	ChangePasswordResponse response = cachingUserServiceAdapter.changePassword(wsUserId, "newPassword1");
        assertNotNull(response);
        ErrorStatus ErrorStatus = response.getErrorStatus();
        assertEquals(StatusCode.SERVER_ERROR, ErrorStatus.getStatusCode());
        assertNotNull(ErrorStatus.getStatusReason());
        assertEquals("An error", ErrorStatus.getStatusReason());
        
        verify(getMocks());
    }
 
    @Test
    public void testChangePassword() throws Exception {
    	
        WsUserId wsUserId = getWsUserId(USERNAME);
        expect(customerLookup.getCustomerByWsUserId(wsUserId)).andReturn(customer);
    	customerService.saveChangeCustomerPassword(EasyMock.anyObject(ChangePasswordDto.class), EasyMock.anyObject(Customer.class));
    	
    	replay(getMocks());
    	
    	ChangePasswordResponse response = cachingUserServiceAdapter.changePassword(wsUserId,  "newPassword1");
        assertNotNull(response);
        ErrorStatus ErrorStatus = response.getErrorStatus();
        assertNull(ErrorStatus);
        
        verify(getMocks());
    }
    
    @Test
    public void testChangePasswordPolicyViolatedCurrentPasswordSameAsPreviousPasswordException() throws Exception {
    	  WsUserId wsUserId = getWsUserId(USERNAME);
          expect(customerLookup.getCustomerByWsUserId(wsUserId)).andReturn(customer);
          
      	customerService.saveChangeCustomerPassword(EasyMock.anyObject(ChangePasswordDto.class), EasyMock.anyObject(Customer.class));
      	EasyMock.expectLastCall();
      	EasyMock.expectLastCall().andThrow(new PasswordPolicyViolatedServiceLayerException("New password should not match current password"));
      	
      	replay(getMocks());
      	
      	ChangePasswordResponse response = cachingUserServiceAdapter.changePassword(wsUserId, "oldPassword1");
          assertNotNull(response);
          ErrorStatus ErrorStatus = response.getErrorStatus();
          assertEquals(StatusCode.CLIENT_ERROR, ErrorStatus.getStatusCode());
          assertNotNull(ErrorStatus.getStatusReason());
          assertEquals("New password should not match current password", ErrorStatus.getStatusReason());
          
          verify(getMocks());
    }
    
    
    @Test
    public void testChangePasswordPolicyViolatedPreviousPasswordsException()  throws Exception{
         WsUserId wsUserId = getWsUserId(USERNAME);
          expect(customerLookup.getCustomerByWsUserId(wsUserId)).andReturn(customer);
          
      	customerService.saveChangeCustomerPassword(EasyMock.anyObject(ChangePasswordDto.class), EasyMock.anyObject(Customer.class));
      	EasyMock.expectLastCall().andThrow(new PasswordPolicyViolatedServiceLayerException("New password should not match previous 4 passwords"));
      	
      	replay(getMocks());
      	
      	ChangePasswordResponse response = cachingUserServiceAdapter.changePassword(wsUserId, "newPassword1");
          assertNotNull(response);
          ErrorStatus ErrorStatus = response.getErrorStatus();
          assertEquals(StatusCode.CLIENT_ERROR, ErrorStatus.getStatusCode());
          assertNotNull(ErrorStatus.getStatusReason());
          assertEquals("New password should not match previous 4 passwords", ErrorStatus.getStatusReason());
          
          verify(getMocks());
    }
    
    
    @Test
    public void testResetPasswordNoUsername() throws Exception {
        WsUserId wsUserId = getWsUserId("");
        expect(customerLookup.getCustomerByWsUserId(wsUserId)).andThrow(new WebServiceValidationException("The username cannot be blank."));
        
        replay(getMocks());
        ResetPasswordResponse response = cachingUserServiceAdapter.resetPassword(wsUserId, "");
        assertNotNull(response);
        ErrorStatus ErrorStatus = response.getErrorStatus();
        assertEquals(StatusCode.CLIENT_ERROR, ErrorStatus.getStatusCode());
        assertEquals("The username cannot be blank.", ErrorStatus.getStatusReason());
        verify(getMocks());
    }
    
    @Test
    public void testResetPasswordUserHasNoEmailServiceLayerException() throws Exception {
        WsUserId wsUserId = getWsUserId(USERNAME);
        Customer customer = new Customer();
        customer.setUsername(USERNAME);
        Assert.assertNull(customer.getEmailAddress());
        expect(customerLookup.getCustomerByWsUserId(wsUserId)).andReturn(customer);
        
        
    	customerService.updateResetCustomerPassword(customer, Locale.getDefault(), RESETPASSWORDWS_BASEURL, USERNAME);
    	EasyMock.expectLastCall().andThrow(new UserHasNoEmailServiceLayerException("Customer does not have a email address"));
    	EasyMock.replay(getMocks());
        
    	ResetPasswordResponse response = cachingUserServiceAdapter.resetPassword(wsUserId, USERNAME);
    	
        assertNotNull(response);
        ErrorStatus ErrorStatus = response.getErrorStatus();
        assertEquals(StatusCode.CLIENT_ERROR, ErrorStatus.getStatusCode());
        assertNotNull(ErrorStatus.getStatusReason());
        assertEquals("Customer does not have a email address", ErrorStatus.getStatusReason());
        
        EasyMock.verify(getMocks());
    }
    
  
   
    @Test
    public void testResetPasswordServiceLayerException() throws Exception {
    	
        WsUserId wsUserId = getWsUserId(USERNAME);
        Customer cust = new Customer();
        cust.setUsername(USERNAME);
        expect(customerLookup.getCustomerByWsUserId(wsUserId)).andReturn(cust);

        customerService.updateResetCustomerPassword(EasyMock.anyObject(Customer.class),EasyMock.anyObject(Locale.class),EasyMock.anyObject(String.class), EasyMock.anyObject(String.class));
       
    	EasyMock.expectLastCall().andThrow(new ServiceLayerException("An error"));
    	replay(getMocks());
    	
    	ResetPasswordResponse response = cachingUserServiceAdapter.resetPassword(wsUserId, USERNAME);
        assertNotNull(response);
        ErrorStatus ErrorStatus = response.getErrorStatus();
        assertEquals(StatusCode.SERVER_ERROR, ErrorStatus.getStatusCode());
        assertNotNull(ErrorStatus.getStatusReason());
        assertEquals("An error", ErrorStatus.getStatusReason());
        
        verify(getMocks());
    }
   
  
    @Test
    public void testResetPassword() throws Exception {
    	
        WsUserId wsUserId = getWsUserId(USERNAME);
        expect(customerLookup.getCustomerByWsUserId(wsUserId)).andReturn(this.customer);
         
        customerService.updateResetCustomerPassword(EasyMock.anyObject(Customer.class), EasyMock.anyObject(Locale.class),EasyMock.anyObject(String.class),EasyMock.anyObject(String.class));
    	EasyMock.expectLastCall().times(2);
    	
    	replay(getMocks());
    	 customerService.updateResetCustomerPassword(customer, Locale.getDefault(),RESETPASSWORDWS_BASEURL, this.customer.getUsername());
    	ResetPasswordResponse response = cachingUserServiceAdapter.resetPassword(wsUserId, USERNAME);
        assertNotNull(response);
        ErrorStatus ErrorStatus = response.getErrorStatus();       
        assertNull(ErrorStatus);
        
        verify(getMocks());
    }

	private void testException(StatusCode statusCode, String errorMessage) {
		replay(getMocks());
    	
        AuthenticateResponse response = cachingUserServiceAdapter.authenticate(USERNAME, "password");
        assertNotNull(response);
        ErrorStatus ErrorStatus = response.getErrorStatus();
        assertEquals(statusCode, ErrorStatus.getStatusCode());
        assertNotNull(ErrorStatus.getStatusReason());
        assertEquals(errorMessage, ErrorStatus.getStatusReason());
        
        assertNull(response.getAuthenticateResponseSequence());
        
        verify(getMocks());
	}
	
    private final User convertCustomer(final Customer customer) {
        User user = new User();
        
        Identifiers userIds = IdUtils.getIds(customer.getId(),null);
        user.setUserIds(userIds);
        user.setEmailAddress(customer.getEmailAddress());
        user.setFirstName(customer.getFirstName());
        user.setLastName(customer.getFamilyName());
        CredentialName cred = new CredentialName();
        cred.setUserName(customer.getUsername());
        user.setCredentialName(cred);
        return user;
    }
    
    private WsUserId getWsUserId(String username){
        WsUserId result = new WsUserId();
        result.setUserName(username);
        return result;
    }
    
    private RegistrationInformation getRegistrationInformationForKey(final String key, RegistrationInformation[] registrationInformations) {
        for(RegistrationInformation registrationInformation : registrationInformations) {
            if(registrationInformation.getRegistrationKey().equals(key)) {
                return registrationInformation;
            }
        }
        return null;
    }
    
    private WsUserId eqUsername(final String expectedUsername) {
        EasyMock.reportMatcher(new IArgumentMatcher() {
            @Override
            public void appendTo(StringBuffer buffer) {
                buffer.append("eqProductList("+expectedUsername+")");
            }
     
            @Override
            public boolean matches(Object arg) {
                if (arg instanceof WsUserId ) {
                    @SuppressWarnings("unchecked")
                    WsUserId actual = (WsUserId)arg;
                    return expectedUsername.equals(actual.getUserName());
                }
                return false;
            }
        });
        return null;
    }
    

}
