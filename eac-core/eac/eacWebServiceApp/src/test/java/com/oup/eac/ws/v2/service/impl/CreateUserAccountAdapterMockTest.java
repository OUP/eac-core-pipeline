package com.oup.eac.ws.v2.service.impl;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

import javax.naming.NamingException;

import junit.framework.Assert;

import org.apache.velocity.app.VelocityEngine;
import org.easymock.EasyMock;
import org.hibernate.SessionFactory;
import org.hibernate.impl.SessionFactoryImpl;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;






import org.springframework.security.crypto.password.StandardPasswordEncoder;

import com.oup.eac.common.mock.AbstractMockTest;
import com.oup.eac.common.utils.crypto.PasswordUtils;
import com.oup.eac.common.utils.username.UsernameValidator;
import com.oup.eac.common.utils.username.impl.UsernameValidatorImpl;
import com.oup.eac.data.ActivationCodeDao;
import com.oup.eac.data.AnswerDao;
import com.oup.eac.data.CustomerDao;
import com.oup.eac.data.ExternalIdDao;
import com.oup.eac.data.ExternalSystemDao;
import com.oup.eac.data.ExternalSystemIdTypeDao;
import com.oup.eac.data.LinkedRegistrationDao;
import com.oup.eac.data.RegistrationDao;
import com.oup.eac.data.RegistrationDefinitionDao;
import com.oup.eac.data.impl.hibernate.ActivationCodeHibernateDao;
import com.oup.eac.data.impl.hibernate.LinkedRegistrationHibernateDao;
import com.oup.eac.data.impl.hibernate.RegistrationDefinitionHibernateDao;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.MailCriteria;
import com.oup.eac.dto.CustomerDto;
import com.oup.eac.dto.ExternalIdDto;
import com.oup.eac.dto.WsUserIdDto;
import com.oup.eac.dto.licence.LicenceDescriptionGeneratorSource;
import com.oup.eac.integration.facade.ErightsFacade;
import com.oup.eac.integration.facade.exceptions.ErightsException;
import com.oup.eac.integration.facade.impl.ErightsFacadeImpl;
import com.oup.eac.service.ActivationCodeService;
import com.oup.eac.service.CustomerService;
import com.oup.eac.service.DivisionService;
import com.oup.eac.service.EmailService;
import com.oup.eac.service.ExternalIdService;
import com.oup.eac.service.LicenceService;
import com.oup.eac.service.PageDefinitionService;
import com.oup.eac.service.ProductService;
import com.oup.eac.service.RegistrationDefinitionService;
import com.oup.eac.service.RegistrationService;
import com.oup.eac.service.ServiceLayerException;
import com.oup.eac.service.exceptions.ServiceLayerValidationException;
import com.oup.eac.service.impl.CustomerServiceImpl;
import com.oup.eac.service.impl.ExternalIdServiceImpl;
import com.oup.eac.service.impl.RegistrationServiceImpl;
import com.oup.eac.ws.v2.binding.access.CreateUserAccountResponse;
import com.oup.eac.ws.v2.binding.access.UserStatusType;
import com.oup.eac.ws.v2.binding.common.CreateUser;
import com.oup.eac.ws.v2.binding.common.Credential;
import com.oup.eac.ws.v2.binding.common.ErrorStatus;
import com.oup.eac.ws.v2.binding.common.ExternalIdentifier;
import com.oup.eac.ws.v2.binding.common.LocaleType;
import com.oup.eac.ws.v2.binding.common.PasswordCredential;
import com.oup.eac.ws.v2.binding.common.types.StatusCode;
import com.oup.eac.ws.v2.ex.WebServiceValidationException;
import com.oup.eac.ws.v2.service.entitlements.CustomerConverter;
import com.oup.eac.ws.v2.service.impl.entitlements.CustomerConverterImpl;
import com.oup.eac.ws.v2.service.utils.LocaleUtils;

public class CreateUserAccountAdapterMockTest extends AbstractMockTest {

    private CustomerService mCustomerService;
    private ExternalIdService mExternalIdService;
    private CustomerConverter mCustomerConverter;
    private MessageSource mMessageSource;
    private UsernameValidator usernameValidator;
    private CreateUserAccountAdapterImpl sut;
    private static String USERNAME_POL_REGEX = "^(?=^(?:(?!<.*?>).)*$)\\S{5,255}$";
    private EmailService emailService;
    private VelocityEngine velocityEngine;
    private RegistrationService registrationService;
    private AnswerDao answerDao;
    @Autowired
    private ErightsFacade erightsFacade;
    private CustomerDao customerDao;
    private RegistrationDao registrationDao;
    private StandardPasswordEncoder passwordEncoder;
    private LicenceService licenceService;
    private ProductService productService; 
    private ExternalSystemDao extSysDao;
    private ExternalSystemIdTypeDao externalSystemTypeDao;
    private ExternalIdDao externalIdDao;
    private ActivationCodeService activationCodeService;
    private RegistrationDefinitionDao registrationDefinitionDao;
    private LinkedRegistrationDao linkedRegistrationDao;
    private PageDefinitionService pageDefinitionService;
    private LicenceDescriptionGeneratorSource licenceDescriptionGeneratorSource;
    private ActivationCodeDao activationCodeDao;
    private RegistrationDefinitionService registrationDefinitionService;
    private String const_username;
    private DivisionService divisionService;
   
      public String getRandomString() {
            char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
            StringBuilder sb = new StringBuilder();
            Random random = new Random();
            for (int i = 0; i < 4; i++) {
                char c = chars[random.nextInt(chars.length)];
                sb.append(c);
            }
            String randomString = sb.toString() + String.valueOf(System.currentTimeMillis());
            // System.out.println(output);
            return randomString;
        }

	public String setConstUserName() {
		const_username = getRandomString();
		return const_username;
	}
    
    public CreateUserAccountAdapterMockTest() throws NamingException {
        super();
    }

    @Before
    public void setup() {
        
        
        emailService = new EmailService() {
			
			@Override
			public void sendMail(MailCriteria mailCriteria) {
				// TODO Auto-generated method stub
				
			}
		};
        velocityEngine = new VelocityEngine();
        mMessageSource = createMock(MessageSource.class);
        answerDao = createMock(AnswerDao.class);
        customerDao = createMock(CustomerDao.class);
        registrationDao = createMock(RegistrationDao.class);
        //erightsFacade = new ErightsFacadeImpl();
        licenceService = createMock(LicenceService.class);
        productService = createMock(ProductService.class);
        extSysDao = createMock(ExternalSystemDao.class);
        externalSystemTypeDao = createMock(ExternalSystemIdTypeDao.class);
        externalIdDao = createMock(ExternalIdDao.class);
        activationCodeService = createMock(ActivationCodeService.class);
        pageDefinitionService = createMock(PageDefinitionService.class);
        licenceDescriptionGeneratorSource = createMock(LicenceDescriptionGeneratorSource.class);
        registrationDefinitionService = createMock(RegistrationDefinitionService.class);
        
        passwordEncoder = new StandardPasswordEncoder();
       
        registrationDefinitionDao = new RegistrationDefinitionHibernateDao(null);
        linkedRegistrationDao = new LinkedRegistrationHibernateDao(null);
        
        activationCodeDao = new ActivationCodeHibernateDao(null);
        
        
        setMocks(activationCodeService, pageDefinitionService, licenceDescriptionGeneratorSource, registrationDefinitionService, emailService, answerDao, customerDao, registrationDao, extSysDao, licenceService, externalSystemTypeDao, externalIdDao, productService, mMessageSource);
        registrationService = new RegistrationServiceImpl(answerDao, registrationDao, erightsFacade, emailService, velocityEngine, activationCodeService, registrationDefinitionDao, linkedRegistrationDao, mMessageSource, licenceService, pageDefinitionService, licenceDescriptionGeneratorSource, activationCodeDao, productService, registrationDefinitionService,divisionService);
        mCustomerService = new CustomerServiceImpl(emailService, velocityEngine, registrationService, answerDao, erightsFacade, customerDao, registrationDao, mMessageSource, passwordEncoder, licenceService, productService);
        mExternalIdService = new ExternalIdServiceImpl(extSysDao, externalSystemTypeDao, externalIdDao, mCustomerService, productService, erightsFacade);
        mCustomerConverter = new CustomerConverterImpl();
        usernameValidator = new UsernameValidatorImpl(USERNAME_POL_REGEX);
        
    	
        
        this.sut = new CreateUserAccountAdapterImpl(mMessageSource, mCustomerService, mCustomerConverter, mExternalIdService,usernameValidator);
    }

    @Test
    public void testNull() {
        CreateUserAccountResponse result = sut.createUserAccounts(null);
        Assert.assertNotNull(result);
        Assert.assertEquals(0, result.getUserStatus().length);
    }

    @Test
    public void testEmpty() {
        CreateUser[] empty = {};
        CreateUserAccountResponse result = sut.createUserAccounts(empty);
        Assert.assertNotNull(result);
        Assert.assertEquals(0, result.getUserStatus().length);
    }

    @Test
    public void testOneSuccess() throws WebServiceValidationException, ServiceLayerException, ErightsException {
        CreateUser user = getCreateUser();
        checkSuccess(user);
    }

    private void checkSuccess(CreateUser userData) throws WebServiceValidationException, ServiceLayerException, ErightsException {

        CreateUser[] users = { userData };
        
        //Customer customer = new Customer();
        //CustomerDto customerDto = new CustomerDto(customer);
        WsUserIdDto userIdDto = new WsUserIdDto();
        userIdDto.setUserName(userData.getCredentials().getPasswordCredential().getUserName());
        /*expect(mCustomerService.getCustomerByUsername("davidhay")).andReturn(null);
        expect(mCustomerConverter.convertCreateUserToCustomer(userData)).andReturn(customer);
        EasyMock.expect(this.usernameValidator.isValid("davidhay")).andReturn(true);
        EasyMock.expect(mCustomerService.validateUserAccount(userIdDto)).andReturn(customer.getId());
        EasyMock.expect(mCustomerService.saveCustomer(customer, false)).andReturn(customerDto);*/
        /*expectLastCall();

       replay(getMocks());*/

        CreateUserAccountResponse result = sut.createUserAccounts(users);

        Assert.assertNotNull(result);
        Assert.assertEquals(1, result.getUserStatus().length);
        UserStatusType us1 = result.getUserStatus(0);
        Assert.assertNotNull(us1.getUserId());
        Assert.assertNull(us1.getErrorStatus());
        //Assert.assertEquals(customer.getId(), result.getUserStatus().getClass());
        //verify(getMocks());
    }

    @Test
    public void testManySuccess() throws WebServiceValidationException, ServiceLayerException, ErightsException {
        CreateUser userA = getCreateUser("A"+getRandomString());
        addExternalId(userA, "0198608910"+const_username, "DLP", "ISBN-13");
        CreateUser userB = getCreateUser("B"+const_username);
        CreateUser userC = getCreateUser("C"+const_username);
        CreateUser userD = getCreateUser("D"+const_username);
        CreateUser[] users = { userA, userB, userC, userD };
        //String randomMsg = UUID.randomUUID().toString();
        
        /*for (CreateUser userData : users) {
            Customer customer = new Customer();
            expect(mCustomerService.getCustomerByUsername(getUserName(userData))).andReturn(null);
            expect(mCustomerConverter.convertCreateUserToCustomer(userData)).andReturn(customer);
            mCustomerService.saveCustomer(customer, false);
            expectLastCall().andThrow(new ServiceLayerValidationException(randomMsg));
            
            if ("A".equals(getUserName(userData))) {
                mExternalIdService.saveExternalCustomerIdsForSystemCreatingCustomer(anyObject(Customer.class), anyObject(String.class),
                        EasyMock.<List<ExternalIdDto>> anyObject());
                expectLastCall();
            } else {
                mCustomerService.saveCustomer(customer, false);
                expectLastCall();
            }
        }

        EasyMock.expect(this.usernameValidator.isValid("A")).andReturn(true);
        EasyMock.expect(this.usernameValidator.isValid("B")).andReturn(true);
        EasyMock.expect(this.usernameValidator.isValid("C")).andReturn(true);
        EasyMock.expect(this.usernameValidator.isValid("D")).andReturn(true);

        replay(getMocks());*/

        CreateUserAccountResponse result = sut.createUserAccounts(users);

        Assert.assertNotNull(result);
        Assert.assertEquals(users.length, result.getUserStatus().length);
        for (int i = 0; i < users.length; i++) {
            UserStatusType us1 = result.getUserStatus(i);
            Assert.assertNull(us1.getErrorStatus());
            Assert.assertNotNull(us1.getUserId());
        }

        //verify(getMocks());
    }

    @Test
    public void testSomeSuccess() throws WebServiceValidationException, ServiceLayerException, ErightsException {
        CreateUser userA = getCreateUser("A"+setConstUserName());
        CreateUser userB = getCreateUser("B"+const_username);

        CreateUser userC = getCreateUser("C"+const_username);
        CreateUser userD = getCreateUser("D"+const_username);

        CreateUser[] users = { userA, userB, userC, userD };

        userB.setEmailAddress("david@[][][]");// invalid email address
        userD.setEmailAddress("david@[][][]");// invalid email address

        // A
        Customer customerA = new Customer();
        /*expect(mCustomerService.getCustomerByUsername(getUserName(userA))).andReturn(null);
        expect(mCustomerConverter.convertCreateUserToCustomer(userA)).andReturn(customerA);
        mCustomerService.saveCustomer(customerA, false);
        expectLastCall();*/
        
        //B
        /*expect(mCustomerService.getCustomerByUsername(getUserName(userB))).andReturn(null);
        mCustomerService.saveCustomer(customerA, false);
        expectLastCall();*/
        // C
        Customer customerC = new Customer();
        /*expect(mCustomerService.getCustomerByUsername(getUserName(userC))).andReturn(null);
        expect(mCustomerConverter.convertCreateUserToCustomer(userC)).andReturn(customerC);
        mCustomerService.saveCustomer(customerC, false);
        expectLastCall();*/

        //D
        /*expect(mCustomerService.getCustomerByUsername(getUserName(userD))).andReturn(null);

        EasyMock.expect(this.usernameValidator.isValid("A")).andReturn(true);
        EasyMock.expect(this.usernameValidator.isValid("B")).andReturn(true);
        EasyMock.expect(this.usernameValidator.isValid("C")).andReturn(true);
        EasyMock.expect(this.usernameValidator.isValid("D")).andReturn(true);*/

        //replay(getMocks());

        CreateUserAccountResponse result = sut.createUserAccounts(users);

        Assert.assertNotNull(result);
        Assert.assertEquals(users.length, result.getUserStatus().length);
        for (int i = 0; i < users.length; i++) {
            UserStatusType us1 = result.getUserStatus(i);

            if (i == 0) {
                // user A
                Assert.assertNull(us1.getErrorStatus());
            }
            if (i == 1) {
                // user B
                Assert.assertNotNull(us1.getErrorStatus());
            }
        }
        //verify(getMocks());
    }

    private CreateUser getCreateUser(String username) {
        CreateUser result = getCreateUser();
        result.getCredentials().getPasswordCredential().setUserName(username);
        return result;
    }
    
    private CreateUser addExternalId(CreateUser createUser, String id, String systemId, String typeId) {
        ExternalIdentifier externalIdentifier = new ExternalIdentifier();
        externalIdentifier.setId(id);
        externalIdentifier.setSystemId(systemId);
        externalIdentifier.setTypeId(typeId);
        createUser.addExternal(externalIdentifier);
        return createUser;
    }

    @Test
    public void testAlreadyExists() throws WebServiceValidationException, ServiceLayerException, ErightsException {
        CreateUser user = getCreateUser();
        CreateUser[] users = { user };
        //Customer customer = new Customer();
        //expect(mCustomerService.getCustomerByUsername("davidhay")).andReturn(customer);
        //EasyMock.expect(this.usernameValidator.isValid("davidhay")).andReturn(true);

        //replay(getMocks());

        CreateUserAccountResponse result = sut.createUserAccounts(users);
        result = sut.createUserAccounts(users);

        checkClientError(user, result, "The username is already taken.");
    }

    @Test
    public void testValidBlankFirstname() throws WebServiceValidationException, ServiceLayerException, ErightsException {
        CreateUser user = getCreateUser();
        user.setFirstName("");
        checkSuccess(user);
    }

    @Test
    public void testValidBlankLastname() throws WebServiceValidationException, ServiceLayerException, ErightsException {
        CreateUser user = getCreateUser();
        user.setLastName("");
        checkSuccess(user);
    }

    @Test
    public void testInvalidUsername() throws WebServiceValidationException, ServiceLayerException, ErightsException {
        CreateUser user = getCreateUser("");
        checkInvalid(user, "The username cannot be blank.",false);
    }
    
    @Test
    public void testInvalidLocaleLanguage() throws WebServiceValidationException, ServiceLayerException, ErightsException {
        CreateUser user = getCreateUser();
        LocaleType lt = new LocaleType();
        lt.setLanguage("abc"+setConstUserName());
        user.setLocale(lt);
        //EasyMock.expect(this.usernameValidator.isValid("davidhay")).andReturn(true);
        checkInvalid(user, "The language is not valid : abc"+const_username);
    }
    
    @Test
    public void testValidLocaleLanguageOnly() throws WebServiceValidationException, ServiceLayerException, ErightsException {
        CreateUser user = getCreateUser();
        LocaleType lt = new LocaleType();
        lt.setLanguage("en");
        user.setLocale(lt);
        checkSuccess(user);
    }
    
    @Test
    public void testInvalidLocaleCountry1() throws WebServiceValidationException, ServiceLayerException, ErightsException {
        CreateUser user = getCreateUser();
        LocaleType lt = new LocaleType();
        lt.setLanguage("en");
        lt.setCountry("SCO");
        user.setLocale(lt);
        //EasyMock.expect(this.usernameValidator.isValid("davidhay")).andReturn(true);
        checkInvalid(user, "The country is not valid : SCO");
    }

    
    @Test
    public void testInvalidLocaleCountry2() throws WebServiceValidationException, ServiceLayerException, ErightsException {
        CreateUser user = getCreateUser();
        LocaleType lt = new LocaleType();
        lt.setLanguage("en");
        lt.setCountry("sco");
        user.setLocale(lt);
        //EasyMock.expect(this.usernameValidator.isValid("davidhay")).andReturn(true);        
        checkInvalid(user, "The country is not valid : sco");
    }

    @Test
    public void testInvalidEmail1() throws WebServiceValidationException, ServiceLayerException, ErightsException {
        CreateUser user = getCreateUser();
        user.setEmailAddress("");
        //EasyMock.expect(this.usernameValidator.isValid("davidhay")).andReturn(true);
        checkInvalid(user, "The email address cannot be blank.");
    }
    
    @Test
    public void testBlankPassword() throws WebServiceValidationException, ServiceLayerException, ErightsException {
        CreateUser user = getCreateUser();
        user.getCredentials().getPasswordCredential().setPassword(null);        
        //EasyMock.expect(this.usernameValidator.isValid("davidhay")).andReturn(true);
        checkInvalid(user, "The password cannot be blank.");
    }
    
    @Test
    public void testInvalidPassword() throws WebServiceValidationException, ServiceLayerException, ErightsException {
        CreateUser user = getCreateUser();        
        user.getCredentials().getPasswordCredential().setPassword("ABC");
        
        Class<Object[]> clazz = Object[].class;
        //expect(this.mMessageSource.getMessage(EasyMock.eq(PasswordUtils.INVALID_PASSWORD_MSG_CODE), anyObject(clazz), EasyMock.eq(PasswordUtils.DEFAULT_INVALID_PASSWORD_MSG), anyObject(Locale.class))).andReturn("ERROR_MESSAGE");
        
        //EasyMock.expect(this.usernameValidator.isValid("davidhay")).andReturn(true);
        checkInvalid(user, null);
    }


    @Test
    public void testInvalidEmail2() throws WebServiceValidationException, ServiceLayerException, ErightsException {
        CreateUser user = getCreateUser();
        user.setEmailAddress("david@[][][][]");
        //EasyMock.expect(this.usernameValidator.isValid("davidhay")).andReturn(true);
        checkInvalid(user, "The email address is invalid.");
    }

    private CreateUser getCreateUser() {
        CreateUser user = new CreateUser();

        // BASIC
        user.setFirstName("david");
        user.setLastName("hay");
        user.setEmailAddress("david.hay@oup.com");

        // LOCALE
        LocaleType locale = LocaleUtils.getLocaleType(Locale.CANADA_FRENCH);
        user.setLocale(locale);

        // CREDENTIALS
        Credential cred = new Credential();
        PasswordCredential pwdCred = new PasswordCredential();
        pwdCred.setUserName("davidhay"+setConstUserName());
        pwdCred.setPassword("somepasswordA1");
        cred.setPasswordCredential(pwdCred);
        user.setCredentials(cred);

        return user;
    }
    
    private void checkInvalid(CreateUser user, String expectedErrorMessage) throws ErightsException {
        checkInvalid(user,expectedErrorMessage,true);
    }

    private void checkInvalid(CreateUser user, String expectedErrorMessage, boolean checkCustomerExists) throws ErightsException {
        CreateUser[] users = { user };

        /*if(checkCustomerExists){
            expect(mCustomerService.getCustomerByUsername(user.getCredentials().getPasswordCredential().getUserName())).andReturn(null);
        }
        replay(getMocks());*/

        CreateUserAccountResponse result = sut.createUserAccounts(users);

        checkClientError(user, result, expectedErrorMessage);
    }

    private void checkClientError(CreateUser user, CreateUserAccountResponse result, String expectedErrorMessage) {
        Assert.assertNotNull(result);
        Assert.assertEquals(1, result.getUserStatus().length);
        UserStatusType us1 = result.getUserStatus(0);

        Assert.assertNotNull(us1.getErrorStatus());

        ErrorStatus es = us1.getErrorStatus();
        Assert.assertNull(us1.getUserId());

        //verify(getMocks());

        Assert.assertEquals(StatusCode.CLIENT_ERROR, es.getStatusCode());
        Assert.assertEquals(expectedErrorMessage, es.getStatusReason());
    }

    private String getUserName(CreateUser user) {
        return user.getCredentials().getPasswordCredential().getUserName();
    }

   /* @Test
    public void testServiceLayerValidationException() throws ServiceLayerException, ErightsException {
        
        String randomMsg = UUID.randomUUID().toString();
        
        CreateUser user1 = getCreateUser();
        CreateUser user2 = getCreateUser();
        CreateUser[] users = { user1, user2 };
        Customer customer1 = new Customer();
        Customer customer2 = new Customer();
        
        expect(mCustomerService.getCustomerByUsername(getUserName(user1))).andReturn(null);
        expect(mCustomerService.getCustomerByUsername(getUserName(user2))).andReturn(null);
        
        expect(mCustomerConverter.convertCreateUserToCustomer(user1)).andReturn(customer1);
        expect(mCustomerConverter.convertCreateUserToCustomer(user2)).andReturn(customer2);
        
        mCustomerService.saveCustomer(customer1, false);
        expectLastCall().andThrow(new ServiceLayerValidationException(randomMsg));
        mCustomerService.saveCustomer(customer2, false);
        expectLastCall().andThrow(new ServiceLayerValidationException(randomMsg));
        
        customer2.setId(UUID.randomUUID().toString());//simulate service layer setting id
        EasyMock.expect(this.usernameValidator.isValid(user1.getCredentials().getPasswordCredential().getUserName())).andReturn(true);
        EasyMock.expect(this.usernameValidator.isValid(user2.getCredentials().getPasswordCredential().getUserName())).andReturn(true);
        mCustomerService.saveCustomer(customer2, false);
        expectLastCall().andThrow(new ServiceLayerValidationException(randomMsg));
        
        replay(getMocks());
        CreateUserAccountResponse result = this.sut.createUserAccounts(users);
        //verify(getMocks());
        UserStatusType[] status = result.getUserStatus();
        Assert.assertEquals(2,status.length);
        UserStatusType status1 = status[0];
        UserStatusType status2 = status[1];
        
        Assert.assertEquals(randomMsg,status1.getErrorStatus().getStatusReason());
        Assert.assertEquals(StatusCode.CLIENT_ERROR,status1.getErrorStatus().getStatusCode());
        
        Assert.assertEquals(customer2.getId(),status2.getUserId().getId());        
    }
    
    @Test
    public void testServiceLayerException() throws ServiceLayerException, ErightsException {
        
        String randomMsg = UUID.randomUUID().toString();
        
        CreateUser user1 = getCreateUser();
        CreateUser user2 = getCreateUser();
        CreateUser[] users = { user1, user2 };
        
        
        expect(mCustomerService.getCustomerByUsername(getUserName(user1))).andReturn(null);
        expect(mCustomerService.getCustomerByUsername(getUserName(user2))).andReturn(null);
        
        expect(mCustomerConverter.convertCreateUserToCustomer(user1)).andReturn(customer1);
        expect(mCustomerConverter.convertCreateUserToCustomer(user2)).andReturn(customer2);
        
        mCustomerService.saveCustomer(customer1, false);
        expectLastCall().andThrow(new ServiceLayerException(randomMsg));
        mCustomerService.saveCustomer(customer2, false);
        expectLastCall();
        
        customer2.setId(UUID.randomUUID().toString());//simulate service layer setting id
        EasyMock.expect(this.usernameValidator.isValid(user1.getCredentials().getPasswordCredential().getUserName())).andReturn(true);
        EasyMock.expect(this.usernameValidator.isValid(user2.getCredentials().getPasswordCredential().getUserName())).andReturn(true);
        EasyMock.expect(mCustomerService.saveCustomer(customer1, false)).andReturn(customerDto);
        expectLastCall();
        replay(getMocks());
        CreateUserAccountResponse result = this.sut.createUserAccounts(users);
        //verify(getMocks());
        UserStatusType[] status = result.getUserStatus();
        Assert.assertEquals(2,status.length);
        UserStatusType status1 = status[0];
        UserStatusType status2 = status[1];
        
        Assert.assertEquals(randomMsg,status1.getErrorStatus().getStatusReason());
        Assert.assertEquals(StatusCode.SERVER_ERROR,status1.getErrorStatus().getStatusCode());
        
               
    }*/
    
   /* public UserData createUser() {


        String uniqueUserName = groupCommonUtil.getRandomString();
        OupUserWS oupUserWS = new OupUserWS();
        oupUserWS.setConcurrency("3");
        oupUserWS.setEmailAddress(uniqueUserName + "_Test@TestHost.com");
        oupUserWS.setSuspended("false");
        OupAuthProfileWS oupAuthProfileWS = new OupAuthProfileWS();
        oupAuthProfileWS.setMatchMode(MatchMODE.ANY.toString());
        OupCredentialWS oupCredentialWS = new OupCredentialWS();
        OupCredentialLoginPasswordWS oupCredentialDataWS = new OupCredentialLoginPasswordWS();
        oupCredentialDataWS.setUsername(uniqueUserName);
        oupCredentialDataWS.setPassword(getEncryted(uniqueUserName + "_password"));
        oupCredentialWS.setLoginPasswordCredential(oupCredentialDataWS);
        oupAuthProfileWS.getCredentials().add(oupCredentialWS);
        oupUserWS.getAuthProfile().add(oupAuthProfileWS);
        oupUserWS.setPrivacyPolicyAccepted(String.valueOf(Boolean.TRUE));
        return oupUserWS;

    
    }*/

}