package com.oup.eac.web.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.oup.eac.common.utils.activationcode.ActivationCodeGenerator;
import com.oup.eac.common.utils.activationcode.EacNumericActivationCode;
import com.oup.eac.common.utils.token.TokenConverter;
import com.oup.eac.data.AbstractDbMessageTest;
import com.oup.eac.domain.AccountPageDefinition;
import com.oup.eac.domain.ActivationCode;
import com.oup.eac.domain.ActivationCodeBatch;
import com.oup.eac.domain.ActivationCodeBatch.ActivationCodeFormat;
import com.oup.eac.domain.ActivationCodeRegistrationDefinition;
import com.oup.eac.domain.Answer;
import com.oup.eac.domain.Component;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.Element;
import com.oup.eac.domain.ErightsDenyReason;
import com.oup.eac.domain.InstantRegistrationActivation;
import com.oup.eac.domain.LinkedProduct;
import com.oup.eac.domain.ProductPageDefinition;
import com.oup.eac.domain.ProductRegistrationDefinition;
import com.oup.eac.domain.Question;
import com.oup.eac.domain.RegisterableProduct;
import com.oup.eac.domain.RegisterableProduct.RegisterableType;
import com.oup.eac.domain.Registration;
import com.oup.eac.domain.SelfRegistrationActivation;
import com.oup.eac.domain.StandardLicenceTemplate;
import com.oup.eac.domain.ValidatedRegistrationActivation;
import com.oup.eac.domain.WhiteListUrl;
import com.oup.eac.domain.util.SampleDataFactory;
import com.oup.eac.dto.LicenceDto;
import com.oup.eac.dto.RegistrationDto;
import com.oup.eac.dto.TokenDto;
import com.oup.eac.integration.facade.ErightsFacade;
import com.oup.eac.integration.facade.impl.FakeErightsFacadeImpl;
import com.oup.eac.service.CustomerService;
import com.oup.eac.service.RegistrationService;
import com.oup.eac.service.WhiteListUrlService;
import com.oup.eac.web.controllers.authentication.AccessController;
import com.oup.eac.web.controllers.authentication.ActivationCodeFormController;
import com.oup.eac.web.controllers.authentication.RegistrationAllowController;
import com.oup.eac.web.controllers.authentication.ReregisterController;
import com.oup.eac.web.controllers.authentication.ValidatorRegistrationAllowController;
import com.oup.eac.web.controllers.authentication.ValidatorRegistrationDenyController;
import com.oup.eac.web.controllers.helpers.CookieHelper;
import com.oup.eac.web.controllers.helpers.SessionHelper;
import com.oup.eac.web.controllers.registration.EACViews;

/**
 * An integration test for the access controller.
 * 
 * @see com.oup.eac.web.controllers.authentication.AccessControllerMockTest
 */
@Ignore
@ContextConfiguration(locations = {"classpath*:/eac/eac*-beans.xml", "classpath*:/eac/test.eac*-beans.xml", "classpath*:/eac/web.eac*-beans.xml", "classpath*:/eac/web.eac-servlet.xml"})
@DirtiesContext
public class AccessControllerTest extends AbstractDbMessageTest {

    private static final String EMAILS_MESSAGE_SOURCE_BASENAME = "emails";
    
    @Autowired
    private CustomerService customerService;
    
    @Autowired
    private RegistrationService registrationService;
    
    private ProductRegistrationDefinition productRegistrationDefinition;
    
    private ActivationCodeRegistrationDefinition activationCodeRegistrationDefinition;
    
    private ActivationCodeGenerator eacNumericActivationCodeGenerator = new EacNumericActivationCode();
    
    private Customer customer1, customer2, customer3;
    private Element element1;
    private Question question1;
    private ActivationCode activationCode1;
    private ActivationCode activationCode2;
    private LinkedProduct linkedProduct1;
    private LinkedProduct linkedProduct2;
    private RegisterableProduct product, product2, product4;
    private Registration<ProductRegistrationDefinition> customer2ProductRegistration, customer3ProductRegistration;
    private Registration<ActivationCodeRegistrationDefinition> customer2ActivationCodeRegistration;
    
    private String sessionId = "osdijfsdpfompsdfpsdkf";
    private String customer2SessionId = "oiweuroweijfnosdnfosdfn";
    private List<String> urlList = new ArrayList<String>();
    private String WhiteListURL = "http://test.oup.com/protected/resource.htm";
    private WhiteListUrlService whiteListUrl;
 
    @Autowired
    private AccessController accessController;
    
    @Autowired
    private RegistrationAllowController registrationAllowController;
    
    @Autowired
    private ValidatorRegistrationDenyController validatorRegistrationDenyController;
    
    @Autowired
    private ValidatorRegistrationAllowController validatorRegistrationAllowController;
    
    @Autowired
    private ActivationCodeFormController activationCodeFormController;
    
    @Autowired
    private ReregisterController reregisterController;
    
    @Autowired
    private ErightsFacade fakeErightsFacade;
    
    private MockHttpServletRequest httpServletRequest;
    private MockHttpServletResponse httpServletResponse;
    private MockHttpSession httpSession;
    
   
    
    
    @Before
    public final void setUp() throws Exception {
    	 WhiteListUrl url = new WhiteListUrl();
         url.setUrl(WhiteListURL);
         url.setId("abc");
         urlList.add(WhiteListURL);
         
    	httpServletRequest = new MockHttpServletRequest();
    	httpServletResponse = new MockHttpServletResponse();
    	httpSession = new MockHttpSession();
    	httpServletRequest.setSession(httpSession);
    	customer1 = SampleDataFactory.createCustomer();
    	customer1.setId("1");
    	customer2 = SampleDataFactory.createCustomer();
    	customer2.setId("2");
    	customer3 = SampleDataFactory.createCustomer();
    	customer3.setId("3");
    	/*whiteListUrl = SampleDataFactory.create*/
    	getSampleDataCreator().loadCustomer(customer1);
    	getSampleDataCreator().loadCustomer(customer2);
    	getSampleDataCreator().loadCustomer(customer3);
    	getSampleDataCreator().loadWhiteListUrl(url);
    	
    	FakeErightsFacadeImpl.reset();
    	    	
    	product = getSampleDataCreator().createRegisterableProduct(1,"product 1", RegisterableType.SELF_REGISTERABLE);
    	product2 = getSampleDataCreator().createRegisterableProduct(2,"product 2", RegisterableType.SELF_REGISTERABLE);
    	RegisterableProduct product3 = getSampleDataCreator().createRegisterableProduct(3,"product 3", RegisterableType.SELF_REGISTERABLE);
    	product4 = getSampleDataCreator().createRegisterableProduct(4,"product 4",RegisterableType.SELF_REGISTERABLE);
    	
    	linkedProduct1 = getSampleDataCreator().createLinkedProduct(product4, 223);
    	linkedProduct2 = getSampleDataCreator().createLinkedProduct(product4, 224);
    	StandardLicenceTemplate licenceTemplate = getSampleDataCreator().createStandardLicenceTemplate();
        InstantRegistrationActivation instantLicenceActivation = getSampleDataCreator().createInstantRegistrationActivation();
        SelfRegistrationActivation selfLicenceActivation = getSampleDataCreator().createSelfRegistrationActivation();
        
        ProductPageDefinition productPageDefinition = getSampleDataCreator().createProductPageDefinition();
        AccountPageDefinition accountPageDefinition = getSampleDataCreator().createAccountPageDefinition();
        
        ValidatedRegistrationActivation validatedLicenceActivation = getSampleDataCreator().createValidatedRegistrationActivation();
        productRegistrationDefinition = getSampleDataCreator().createProductRegistrationDefinition(product, licenceTemplate, selfLicenceActivation, productPageDefinition);
        getSampleDataCreator().createProductRegistrationDefinition(product4, licenceTemplate, selfLicenceActivation, productPageDefinition);
        getSampleDataCreator().createProductRegistrationDefinition(product2, licenceTemplate, validatedLicenceActivation, productPageDefinition);
        getSampleDataCreator().createAccountRegistrationDefinition(product2, validatedLicenceActivation, accountPageDefinition);
        activationCodeRegistrationDefinition = getSampleDataCreator().createActivationCodeRegistrationDefinition(product3, licenceTemplate, instantLicenceActivation, null);
        getSampleDataCreator().createAccountRegistrationDefinition(product3, instantLicenceActivation, accountPageDefinition);
        ActivationCodeBatch activationCodeBatch = getSampleDataCreator().createActivationCodeBatch(licenceTemplate, ActivationCodeFormat.EAC_NUMERIC, activationCodeRegistrationDefinition, null, null);
        activationCode1 = getSampleDataCreator().createActivationCode(activationCodeBatch, eacNumericActivationCodeGenerator);
        
        activationCode2 = getSampleDataCreator().createActivationCode(activationCodeBatch, eacNumericActivationCodeGenerator);
        //getSampleDataCreator().loadActivationCode(activationCode2);
        getSampleDataCreator().createAccountRegistrationDefinition(product, instantLicenceActivation, accountPageDefinition);
        Component component = getSampleDataCreator().createComponent("label.marketinginformation");
        getSampleDataCreator().createPageComponent(productPageDefinition, component, 1);
        question1 = getSampleDataCreator().createQuestion();
        element1 = getSampleDataCreator().createElement(question1);
        
        String messageKey = question1.getElementText();
        String messageText = "message text for " + messageKey;
        createDbMessage(EMAILS_MESSAGE_SOURCE_BASENAME,messageKey, messageText);
        
        getSampleDataCreator().createField(component, element1, 1);
        getSampleDataCreator().createTextField(element1);
        
        //Create template who's licences will expire immediately
        StandardLicenceTemplate standardLicenceTemplate = SampleDataFactory.createStandardLicenceTemplate();
        standardLicenceTemplate.setEndDate(new LocalDate().minusDays(1));
        getSampleDataCreator().loadStandardLicenceTemplate(standardLicenceTemplate);
        
        customer2ProductRegistration = SampleDataFactory.createProductRegistration(customer2, productRegistrationDefinition, new DateTime(), new DateTime());
        customer2ProductRegistration.setCompleted(true);
        customer2ProductRegistration.setActivated(true);
        customer2ProductRegistration.setAwaitingValidation(true);
        customer2ProductRegistration.setDenied(false);
    	getSampleDataCreator().loadRegistration(customer2ProductRegistration);
    	
    	//Create an existing licence in fake erights. Licence will already have expired
    	fakeErightsFacade.addLicense(customer2.getId(), standardLicenceTemplate, Arrays.asList(new String[]{product.getId()}), true);
    	
        customer3ProductRegistration = SampleDataFactory.createProductRegistration(customer3, productRegistrationDefinition, new DateTime(), new DateTime());
        customer3ProductRegistration.setCompleted(true);
        customer3ProductRegistration.setActivated(true);
        customer3ProductRegistration.setAwaitingValidation(true);
        customer3ProductRegistration.setDenied(false);
    	getSampleDataCreator().loadRegistration(customer3ProductRegistration);
    	
    	customer2ActivationCodeRegistration = SampleDataFactory.createActivationCodeRegistration(customer2, activationCodeRegistrationDefinition, activationCode1);
    	customer2ActivationCodeRegistration.setCompleted(true);
    	customer2ActivationCodeRegistration.setActivated(true);
    	customer2ActivationCodeRegistration.setAwaitingValidation(true);
    	customer2ActivationCodeRegistration.setDenied(false);
    	getSampleDataCreator().loadRegistration(customer2ActivationCodeRegistration);
    	
    	//Create an existing licence in fake erights. Licence will already have expired
    	fakeErightsFacade.addLicense(customer2.getId(), standardLicenceTemplate, Arrays.asList(new String[]{product3.getId()}), true);
    	
    	
    	loadAllDataSets();
    } 
    
    @Test
    @DirtiesContext
    public void oupUATRequestNotValid() throws Exception {
    	httpServletRequest.setParameter("url", "");
        ModelAndView modelAndView = accessController.handleRequest(httpServletRequest, httpServletResponse);
        assertEquals(EACViews.ERROR_PAGE, getView(modelAndView));
    }    
    
    @Test
    @DirtiesContext
    public void testRegisterableProductAvailable() throws Exception {
    	httpServletRequest.setParameter("url", "http://localhost/nothing.html");
    	accessController.handleRequest(httpServletRequest, httpServletResponse);
    	assertNull(SessionHelper.getRegisterableProduct(httpServletRequest));
    	httpServletRequest.setParameter("url", "http://localhost/product1.html");
        ModelAndView modelAndView = accessController.handleRequest(httpServletRequest, httpServletResponse);
        assertNotNull(SessionHelper.getRegisterableProduct(httpServletRequest));
        assertEquals(EACViews.LOGIN_VIEW + "?url=http://localhost/product1.html", getView(modelAndView));
    }    
    
    @Test
    @DirtiesContext
    public void testCookieNotAvailable() throws Exception {
    	httpServletRequest.setParameter("url", "http://localhost/product1.html");
        ModelAndView modelAndView = accessController.handleRequest(httpServletRequest, httpServletResponse);
        assertEquals(EACViews.LOGIN_VIEW + "?url=http://localhost/product1.html", getView(modelAndView));
    }    
    
    @Test
    @DirtiesContext
    public void testCookieAvailableSessionInvalid() throws Exception {
    	CookieHelper.setErightsCookie(httpServletResponse, "invalidvalue");
    	httpServletRequest.setParameter("url", "http://localhost/product1.html");
        ModelAndView modelAndView = accessController.handleRequest(httpServletRequest, httpServletResponse);
        assertEquals(EACViews.LOGIN_VIEW + "?url=http://localhost/product1.html", getView(modelAndView));
    }
    
    @Test
    @DirtiesContext
    public void testProductRegistrationDefined() throws Exception {
    	httpServletRequest.setParameter("url", "http://localhost/nothing.html");
    	httpServletRequest.setCookies(CookieHelper.createErightsCookie(sessionId));
    	ModelAndView modelAndView = accessController.handleRequest(httpServletRequest, httpServletResponse);
    	assertNull(SessionHelper.getProductRegistrationDefinition(httpServletRequest));
    	httpServletRequest.setParameter("url", "http://localhost/product1.html");
        modelAndView = accessController.handleRequest(httpServletRequest, httpServletResponse);
        assertNull(SessionHelper.getProductRegistrationDefinition(httpServletRequest));
        assertEquals("http://localhost/product1.html?ERSESSION="+sessionId, getView(modelAndView));
    }
    
    @Test
    @DirtiesContext
    public void testCookieAvailableSessionValidWithNoDenyReason() throws Exception {
    	httpServletRequest.setCookies(CookieHelper.createErightsCookie(sessionId));
    	httpServletRequest.setParameter("url", "http://localhost/product1.html");
        ModelAndView modelAndView = accessController.handleRequest(httpServletRequest, httpServletResponse);
        assertEquals("http://localhost/product1.html?ERSESSION="+sessionId, getView(modelAndView));
    }
        
    
    /**
     * Preconditions: 	Session is available
     * 				  	Session is valid
     * 				  	Deny reason is given
     * 					No registration
     * 					Registration will not be complete (has a page definition)
     * 					Licence activation is SELF
     */
    @Test
    @DirtiesContext
    public void testSelfLicenceActivation() throws Exception {
    	httpServletRequest.setCookies(CookieHelper.createErightsCookie(sessionId));
    	httpServletRequest.setParameter("url", "http://localhost/product1.html");
    	httpServletRequest.setParameter("denyReason", Integer.toString(ErightsDenyReason.DENY_NO_LICENSES_IN_SESSION.getReasonCode()));
        ModelAndView modelAndView = accessController.handleRequest(httpServletRequest, httpServletResponse);
        assertEquals(EACViews.PRODUCT_REGISTRATION_VIEW, getView(modelAndView));
        
        Registration<ProductRegistrationDefinition> registration = customerService.getRegistrationByRegistrationDefinitionAndCustomer(SessionHelper.getProductRegistrationDefinition(httpServletRequest), customer1);
        assertFalse("Is completed", registration.isCompleted());
        assertFalse("Is awaiting validation", registration.isAwaitingValidation());
        assertFalse("Is denied", registration.isDenied());
        assertFalse("Is activated", registration.isActivated());
        RegistrationDto registrationDto = registrationService.getProductPageDefinitionByRegistrationDefinition(productRegistrationDefinition, customer1, null);
        Answer answer = new Answer(question1);
        answer.setAnswerText("answer");
        Map<String, Object> answers = new HashMap<String, Object>();
        answers.put(question1.getId(), answer.getAnswerText());
        registrationDto.setAnswers(answers);
        registrationService.saveCompleteRegsitration(registrationDto, customer1, SessionHelper.getRegistrationId(httpServletRequest));
        assertTrue("Is completed", registration.isCompleted());
        assertFalse("Is awaiting validation", registration.isAwaitingValidation());
        assertFalse("Is denied", registration.isDenied());
        assertFalse("Is activated", registration.isActivated());        
        modelAndView = accessController.handleRequest(httpServletRequest, httpServletResponse);
        assertEquals("http://localhost/product1.html?ERSESSION="+sessionId, getView(modelAndView));
        assertTrue("Is completed", registration.isCompleted());
        assertTrue("Is awaiting validation", registration.isAwaitingValidation());
        assertFalse("Is denied", registration.isDenied());
        assertFalse("Is activated", registration.isActivated());  
        assertEquals("http://localhost/product1.html?ERSESSION="+sessionId, getView(modelAndView));
        modelAndView = accessController.handleRequest(httpServletRequest, httpServletResponse);
        assertTrue("Is completed", registration.isCompleted());
        assertTrue("Is awaiting validation", registration.isAwaitingValidation());
        assertFalse("Is denied", registration.isDenied());
        assertFalse("Is activated", registration.isActivated()); 
        assertEquals(EACViews.ACTIVATE_LICENCE_PAGE, getView(modelAndView));
        
        List<LicenceDto> userLicences = fakeErightsFacade.getLicensesForUserProduct(customer1.getId(), product.getId());
        LicenceDto licence = userLicences.get(0);
        String erightsId = licence.getLicenseId();
        
        TokenDto token = new TokenDto(erightsId, customer1.getId(), registration.getId(), "http://localhost/product1.html", licence.getProducts().getProductId());
        httpServletRequest.setParameter("token", TokenConverter.encrypt(token));
        modelAndView = registrationAllowController.handleRequest(httpServletRequest, httpServletResponse);
        assertEquals("http://localhost/product1.html", getView(modelAndView));
        
        modelAndView = accessController.handleRequest(httpServletRequest, httpServletResponse);
        assertTrue("Is completed", registration.isCompleted());
        assertTrue("Is awaiting validation", registration.isAwaitingValidation());
        assertFalse("Is denied", registration.isDenied());
        assertTrue("Is activated", registration.isActivated()); 
        assertEquals(EACViews.ERROR_PAGE, getView(modelAndView));
        
        httpServletRequest.setParameter("denyReason", "");
        modelAndView = accessController.handleRequest(httpServletRequest, httpServletResponse);
        assertTrue("Is completed", registration.isCompleted());
        assertTrue("Is awaiting validation", registration.isAwaitingValidation());
        assertFalse("Is denied", registration.isDenied());
        assertTrue("Is activated", registration.isActivated()); 
        assertEquals("http://localhost/product1.html?ERSESSION="+sessionId, getView(modelAndView));
        
        //Test the users session dying. Cookie is still available so automatically rebuild required session state
        httpServletRequest.getSession().invalidate();
        
        modelAndView = accessController.handleRequest(httpServletRequest, httpServletResponse);
        assertTrue("Is completed", registration.isCompleted());
        assertTrue("Is awaiting validation", registration.isAwaitingValidation());
        assertFalse("Is denied", registration.isDenied());
        assertTrue("Is activated", registration.isActivated()); 
        assertEquals("http://localhost/product1.html?ERSESSION="+sessionId, getView(modelAndView));
    }
    
    /**
     * Preconditions: 	Session is available
     * 				  	Session is valid
     * 				  	Deny reason is given
     * 					No registration
     * 					Registration will not be complete (has a page definition)
     * 					Licence activation is SELF
     */
    @Test
    @DirtiesContext
    public void testValidatedLicenceActivationDenied() throws Exception {
    	httpServletRequest.setCookies(CookieHelper.createErightsCookie(sessionId));
    	httpServletRequest.setParameter("url", "http://localhost/product2.html");
    	httpServletRequest.setParameter("denyReason", Integer.toString(ErightsDenyReason.DENY_NO_LICENSES_IN_SESSION.getReasonCode()));
    	ModelAndView modelAndView = accessController.handleRequest(httpServletRequest, httpServletResponse);
        assertEquals(EACViews.PRODUCT_REGISTRATION_VIEW, getView(modelAndView));
        
        Registration<ProductRegistrationDefinition> registration = customerService.getRegistrationByRegistrationDefinitionAndCustomer(SessionHelper.getProductRegistrationDefinition(httpServletRequest), customer1);
        assertFalse("Is completed", registration.isCompleted());
        assertFalse("Is awaiting validation", registration.isAwaitingValidation());
        assertFalse("Is denied", registration.isDenied());
        assertFalse("Is activated", registration.isActivated());
        RegistrationDto registrationDto = registrationService.getProductPageDefinitionByRegistrationDefinition(SessionHelper.getProductRegistrationDefinition(httpServletRequest), customer1, null);
        Answer answer = new Answer(question1);
        answer.setAnswerText("answer");
        Map<String, Object> answers = new HashMap<String, Object>();
        answers.put(question1.getId(), answer.getAnswerText());
        registrationDto.setAnswers(answers);
        registrationService.saveCompleteRegsitration(registrationDto, customer1, SessionHelper.getRegistrationId(httpServletRequest));
        assertTrue("Is completed", registration.isCompleted());
        assertFalse("Is awaiting validation", registration.isAwaitingValidation());
        assertFalse("Is denied", registration.isDenied());
        assertFalse("Is activated", registration.isActivated());        
        modelAndView = accessController.handleRequest(httpServletRequest, httpServletResponse);
        assertEquals("http://localhost/product2.html?ERSESSION="+sessionId, getView(modelAndView));
        assertTrue("Is completed", registration.isCompleted());
        assertTrue("Is awaiting validation", registration.isAwaitingValidation());
        assertFalse("Is denied", registration.isDenied());
        assertFalse("Is activated", registration.isActivated());  
        assertEquals("http://localhost/product2.html?ERSESSION="+sessionId, getView(modelAndView));
        modelAndView = accessController.handleRequest(httpServletRequest, httpServletResponse);
        assertTrue("Is completed", registration.isCompleted());
        assertTrue("Is awaiting validation", registration.isAwaitingValidation());
        assertFalse("Is denied", registration.isDenied());
        assertFalse("Is activated", registration.isActivated()); 
        assertEquals(EACViews.AWAITING_LICENCE_ACTIVATION_PAGE, getView(modelAndView));
        
        TokenDto token = new TokenDto("12345", customer1.getId(), registration.getId(), "http://localhost/product1.html", "ProductTestDenied");
        httpServletRequest.setParameter("token", TokenConverter.encrypt(token));
        modelAndView = validatorRegistrationDenyController.handleRequest(httpServletRequest, httpServletResponse);
        assertEquals(EACViews.VALIDATOR_LICENCE_DENIED_PAGE, getView(modelAndView));
        assertTrue("Is completed", registration.isCompleted());
        assertTrue("Is awaiting validation", registration.isAwaitingValidation());
        assertTrue("Is denied", registration.isDenied());
        assertFalse("Is activated", registration.isActivated()); 
    }
 
    /**
     * Preconditions: 	Session is available
     * 				  	Session is valid
     * 				  	Deny reason is given
     * 					No registration
     * 					Registration will not be complete (has a page definition)
     * 					Licence activation is VALIDATED
     */
    @Test
    @DirtiesContext
    public void testValidatedLicenceActivationAllowedThenDenied() throws Exception {
    	httpServletRequest.setCookies(CookieHelper.createErightsCookie(sessionId));
    	httpServletRequest.setParameter("url", "http://localhost/product2.html");
    	httpServletRequest.setParameter("denyReason", Integer.toString(ErightsDenyReason.DENY_NO_LICENSES_IN_SESSION.getReasonCode()));
    	ModelAndView modelAndView = accessController.handleRequest(httpServletRequest, httpServletResponse);
        assertEquals(EACViews.PRODUCT_REGISTRATION_VIEW, getView(modelAndView));
        
        Registration<ProductRegistrationDefinition> registration = customerService.getRegistrationByRegistrationDefinitionAndCustomer(SessionHelper.getProductRegistrationDefinition(httpServletRequest), customer1);
        assertFalse("Is completed", registration.isCompleted());
        assertFalse("Is awaiting validation", registration.isAwaitingValidation());
        assertFalse("Is denied", registration.isDenied());
        assertFalse("Is activated", registration.isActivated());
        RegistrationDto registrationDto = registrationService.getProductPageDefinitionByRegistrationDefinition(SessionHelper.getProductRegistrationDefinition(httpServletRequest), customer1, null);
        Answer answer = new Answer(question1);
        answer.setAnswerText("answer");
        Map<String, Object> answers = new HashMap<String, Object>();
        answers.put(question1.getId(), answer.getAnswerText());
        registrationDto.setAnswers(answers);
        registrationService.saveCompleteRegsitration(registrationDto, customer1, SessionHelper.getRegistrationId(httpServletRequest));
        assertTrue("Is completed", registration.isCompleted());
        assertFalse("Is awaiting validation", registration.isAwaitingValidation());
        assertFalse("Is denied", registration.isDenied());
        assertFalse("Is activated", registration.isActivated());        
        modelAndView = accessController.handleRequest(httpServletRequest, httpServletResponse);
        assertEquals("http://localhost/product2.html?ERSESSION="+sessionId, getView(modelAndView));
        assertTrue("Is completed", registration.isCompleted());
        assertTrue("Is awaiting validation", registration.isAwaitingValidation());
        assertFalse("Is denied", registration.isDenied());
        assertFalse("Is activated", registration.isActivated());  
        assertEquals("http://localhost/product2.html?ERSESSION="+sessionId, getView(modelAndView));
        modelAndView = accessController.handleRequest(httpServletRequest, httpServletResponse);
        assertTrue("Is completed", registration.isCompleted());
        assertTrue("Is awaiting validation", registration.isAwaitingValidation());
        assertFalse("Is denied", registration.isDenied());
        assertFalse("Is activated", registration.isActivated()); 
        assertEquals(EACViews.AWAITING_LICENCE_ACTIVATION_PAGE, getView(modelAndView));
        
        List<LicenceDto> userLicences = fakeErightsFacade.getLicensesForUserProduct(customer1.getId(), product2.getId());
        LicenceDto licence = userLicences.get(0);
        String erightsId = licence.getLicenseId();
        
        
        TokenDto token = new TokenDto(erightsId, customer1.getId(), registration.getId(), "http://localhost/product1.html", licence.getProducts().getProductId());
        httpServletRequest.setParameter("token", TokenConverter.encrypt(token));
        modelAndView = validatorRegistrationAllowController.handleRequest(httpServletRequest, httpServletResponse);
        assertEquals(EACViews.VALIDATOR_LICENCE_ALLOWED_PAGE, getView(modelAndView));
        assertTrue("Is completed", registration.isCompleted());
        assertTrue("Is awaiting validation", registration.isAwaitingValidation());
        assertFalse("Is denied", registration.isDenied());
        assertTrue("Is activated", registration.isActivated()); 
   
        modelAndView = validatorRegistrationDenyController.handleRequest(httpServletRequest, httpServletResponse);
        assertEquals(EACViews.VALIDATOR_LICENCE_DENIED_PAGE, getView(modelAndView));
        assertTrue("Is completed", registration.isCompleted());
        assertTrue("Is awaiting validation", registration.isAwaitingValidation());
        assertTrue("Is denied", registration.isDenied());
        assertFalse("Is activated", registration.isActivated()); 
    }
    
    /**
     * Preconditions: 	Session is available
     * 				  	Session is valid
     * 				  	Deny reason is given
     * 					No registration
     * 					Registration will not be complete (has a page definition)
     * 					Licence activation is VALIDATED
     */
    @Test
    @DirtiesContext
    public void testValidatedLicenceActivationDeniedThenAllowed() throws Exception {
    	httpServletRequest.setCookies(CookieHelper.createErightsCookie(sessionId));
    	httpServletRequest.setParameter("url", "http://localhost/product2.html");
    	httpServletRequest.setParameter("denyReason", Integer.toString(ErightsDenyReason.DENY_NO_LICENSES_IN_SESSION.getReasonCode()));
    	ModelAndView modelAndView = accessController.handleRequest(httpServletRequest, httpServletResponse);
        assertEquals(EACViews.PRODUCT_REGISTRATION_VIEW, getView(modelAndView));
        
        Registration<ProductRegistrationDefinition> registration = customerService.getRegistrationByRegistrationDefinitionAndCustomer(SessionHelper.getProductRegistrationDefinition(httpServletRequest), customer1);
        assertFalse("Is completed", registration.isCompleted());
        assertFalse("Is awaiting validation", registration.isAwaitingValidation());
        assertFalse("Is denied", registration.isDenied());
        assertFalse("Is activated", registration.isActivated());
        RegistrationDto registrationDto = registrationService.getProductPageDefinitionByRegistrationDefinition(SessionHelper.getProductRegistrationDefinition(httpServletRequest), customer1, null);
        Answer answer = new Answer(question1);
        answer.setAnswerText("answer");
        Map<String, Object> answers = new HashMap<String, Object>();
        answers.put(question1.getId(), answer.getAnswerText());
        registrationDto.setAnswers(answers);
        registrationService.saveCompleteRegsitration(registrationDto, customer1, SessionHelper.getRegistrationId(httpServletRequest));
        assertTrue("Is completed", registration.isCompleted());
        assertFalse("Is awaiting validation", registration.isAwaitingValidation());
        assertFalse("Is denied", registration.isDenied());
        assertFalse("Is activated", registration.isActivated());        
        modelAndView = accessController.handleRequest(httpServletRequest, httpServletResponse);
        assertEquals("http://localhost/product2.html?ERSESSION="+sessionId, getView(modelAndView));
        assertTrue("Is completed", registration.isCompleted());
        assertTrue("Is awaiting validation", registration.isAwaitingValidation());
        assertFalse("Is denied", registration.isDenied());
        assertFalse("Is activated", registration.isActivated());  
        assertEquals("http://localhost/product2.html?ERSESSION="+sessionId, getView(modelAndView));
        modelAndView = accessController.handleRequest(httpServletRequest, httpServletResponse);
        assertTrue("Is completed", registration.isCompleted());
        assertTrue("Is awaiting validation", registration.isAwaitingValidation());
        assertFalse("Is denied", registration.isDenied());
        assertFalse("Is activated", registration.isActivated()); 
        assertEquals(EACViews.AWAITING_LICENCE_ACTIVATION_PAGE, getView(modelAndView));
        
        TokenDto token = new TokenDto("12345", customer1.getId(), registration.getId(), "http://localhost/product1.html", "TestValidatedDenied");
        httpServletRequest.setParameter("token", TokenConverter.encrypt(token));
        
        modelAndView = validatorRegistrationDenyController.handleRequest(httpServletRequest, httpServletResponse);
        assertEquals(EACViews.VALIDATOR_LICENCE_DENIED_PAGE, getView(modelAndView));
        assertTrue("Is completed", registration.isCompleted());
        assertTrue("Is awaiting validation", registration.isAwaitingValidation());
        assertTrue("Is denied", registration.isDenied());
        assertFalse("Is activated", registration.isActivated()); 
        
    	modelAndView = validatorRegistrationAllowController.handleRequest(httpServletRequest, httpServletResponse);
    	assertEquals(EACViews.VALIDATOR_LICENCE_ALLOWED_PAGE, getView(modelAndView));
    	
        assertTrue("Is completed", registration.isCompleted());
        assertTrue("Is awaiting validation", registration.isAwaitingValidation());
		assertFalse("Is denied", registration.isDenied());
		assertTrue("Is activated", registration.isActivated());
    }
    
    /**
     * Preconditions: 	Session is available
     * 				  	Session is valid
     * 				  	Deny reason is given
     * 					No registration
     * 					Registration will be complete 
     * 					Licence activation is INSTANT
     */
    @Test
    @DirtiesContext
    public void testActivationCodeRegistration() throws Exception {
    	httpServletRequest.setCookies(CookieHelper.createErightsCookie(sessionId));
    	httpServletRequest.setParameter("url", "http://localhost/product3.html");
    	httpServletRequest.setParameter("denyReason", Integer.toString(ErightsDenyReason.DENY_NO_LICENSES_IN_SESSION.getReasonCode()));
    	ModelAndView modelAndView = accessController.handleRequest(httpServletRequest, httpServletResponse);
        assertEquals(EACViews.INTERNAL_ACTIVATION_CODE_VIEW, getView(modelAndView));
        
        httpServletRequest.setParameter("code", "123445");
        httpServletRequest.setMethod("POST");
        modelAndView = activationCodeFormController.handleRequest(httpServletRequest, httpServletResponse);
        assertEquals("activationCodeForm", getView(modelAndView));
        
        httpServletRequest.setParameter("code", activationCode1.getCode());
        httpServletRequest.setMethod("POST");
        modelAndView = activationCodeFormController.handleRequest(httpServletRequest, httpServletResponse);
        assertEquals("http://localhost/product3.html?ERSESSION="+sessionId, getView(modelAndView));

        Registration<ProductRegistrationDefinition> registration = customerService.getRegistrationByRegistrationDefinitionAndCustomer(SessionHelper.getProductRegistrationDefinition(httpServletRequest), customer1);
        assertTrue("Is completed", registration.isCompleted());
        assertFalse("Is awaiting validation", registration.isAwaitingValidation());
        assertFalse("Is denied", registration.isDenied());
        assertFalse("Is activated", registration.isActivated()); 
        
        modelAndView = accessController.handleRequest(httpServletRequest, httpServletResponse);
        assertTrue("Is completed", registration.isCompleted());
        assertTrue("Is awaiting validation", registration.isAwaitingValidation());
        assertFalse("Is denied", registration.isDenied());
        assertTrue("Is activated", registration.isActivated()); 
        assertEquals("http://localhost/product3.html?ERSESSION="+sessionId, getView(modelAndView));
        
        httpServletRequest.setParameter("denyReason", "");
        
        modelAndView = accessController.handleRequest(httpServletRequest, httpServletResponse);
        assertTrue("Is completed", registration.isCompleted());
        assertTrue("Is awaiting validation", registration.isAwaitingValidation());
        assertFalse("Is denied", registration.isDenied());
        assertTrue("Is activated", registration.isActivated()); 
        assertEquals("http://localhost/product3.html?ERSESSION="+sessionId, getView(modelAndView));
    }
    
    /**
     * Preconditions: 	Session is available
     * 				  	Session is valid
     * 				  	Deny reason is given
     * 					No registration
     * 					Registration will not be complete (has a page definition)
     * 					Licence activation is SELF
     */
    @Test
    @DirtiesContext
    public void testLinkedProducts() throws Exception {
    	httpServletRequest.setCookies(CookieHelper.createErightsCookie(sessionId));
    	httpServletRequest.setParameter("url", "http://localhost/product4.html");
    	httpServletRequest.setParameter("denyReason", Integer.toString(ErightsDenyReason.DENY_NO_LICENSES_IN_SESSION.getReasonCode()));
        ModelAndView modelAndView = accessController.handleRequest(httpServletRequest, httpServletResponse);
        assertEquals(EACViews.PRODUCT_REGISTRATION_VIEW, getView(modelAndView));
        
        Registration<ProductRegistrationDefinition> registration = customerService.getRegistrationByRegistrationDefinitionAndCustomer(SessionHelper.getProductRegistrationDefinition(httpServletRequest), customer1);
        assertFalse("Is completed", registration.isCompleted());
        assertFalse("Is awaiting validation", registration.isAwaitingValidation());
        assertFalse("Is denied", registration.isDenied());
        assertFalse("Is activated", registration.isActivated());
        RegistrationDto registrationDto = registrationService.getProductPageDefinitionByRegistrationDefinition(productRegistrationDefinition, customer1, null);
        Answer answer = new Answer(question1);
        answer.setAnswerText("answer");
        Map<String, Object> answers = new HashMap<String, Object>();
        answers.put(question1.getId(), answer.getAnswerText());
        registrationDto.setAnswers(answers);
        registrationService.saveCompleteRegsitration(registrationDto, customer1, SessionHelper.getRegistrationId(httpServletRequest));
        assertTrue("Is completed", registration.isCompleted());
        assertFalse("Is awaiting validation", registration.isAwaitingValidation());
        assertFalse("Is denied", registration.isDenied());
        assertFalse("Is activated", registration.isActivated());        
        modelAndView = accessController.handleRequest(httpServletRequest, httpServletResponse);
        assertEquals("http://localhost/product4.html?ERSESSION="+sessionId, getView(modelAndView));
        assertTrue("Is completed", registration.isCompleted());
        assertTrue("Is awaiting validation", registration.isAwaitingValidation());
        assertFalse("Is denied", registration.isDenied());
        assertFalse("Is activated", registration.isActivated());  
        assertEquals("http://localhost/product4.html?ERSESSION="+sessionId, getView(modelAndView));
        modelAndView = accessController.handleRequest(httpServletRequest, httpServletResponse);
        assertTrue("Is completed", registration.isCompleted());
        assertTrue("Is awaiting validation", registration.isAwaitingValidation());
        assertFalse("Is denied", registration.isDenied());
        assertFalse("Is activated", registration.isActivated()); 
        assertEquals(EACViews.ACTIVATE_LICENCE_PAGE, getView(modelAndView));
        
        List<LicenceDto> userLicences = fakeErightsFacade.getLicensesForUserProduct(customer1.getId(), product4.getId());
        LicenceDto licence = userLicences.get(0);
        String erightsId = licence.getLicenseId();

        
        TokenDto token = new TokenDto(erightsId, customer1.getId(), registration.getId(), "http://localhost/product4.html", licence.getProducts().getProductId());
        httpServletRequest.setParameter("token", TokenConverter.encrypt(token));
        modelAndView = registrationAllowController.handleRequest(httpServletRequest, httpServletResponse);
        assertEquals("http://localhost/product4.html", getView(modelAndView));
        
        modelAndView = accessController.handleRequest(httpServletRequest, httpServletResponse);
        assertTrue("Is completed", registration.isCompleted());
        assertTrue("Is awaiting validation", registration.isAwaitingValidation());
        assertFalse("Is denied", registration.isDenied());
        assertTrue("Is activated", registration.isActivated()); 
        assertEquals(EACViews.ERROR_PAGE, getView(modelAndView));
        
        httpServletRequest.setParameter("denyReason", "");
        modelAndView = accessController.handleRequest(httpServletRequest, httpServletResponse);
        assertTrue("Is completed", registration.isCompleted());
        assertTrue("Is awaiting validation", registration.isAwaitingValidation());
        assertFalse("Is denied", registration.isDenied());
        assertTrue("Is activated", registration.isActivated()); 
        assertEquals("http://localhost/product4.html?ERSESSION="+sessionId, getView(modelAndView));
        
        assertEquals("Check licence for product 4", 1, userLicences.size());
        assertTrue("Check licence has correct product", userLicences.get(0).getProductIds().contains(product4.getErightsId()));
        
        userLicences = fakeErightsFacade.getLicensesForUser(customer1.getId(), null);
        assertEquals("Check correct number of licences for user", 3, userLicences.size());
        
        userLicences = fakeErightsFacade.getLicensesForUserProduct(customer1.getId(), linkedProduct1.getId());
        assertEquals("Check licence for linked product 1", 1, userLicences.size());
        assertTrue("Check licence is active", userLicences.get(0).isEnabled());
        assertTrue("Check licence has correct product", userLicences.get(0).getProductIds().contains(linkedProduct1.getId()));
        userLicences = fakeErightsFacade.getLicensesForUserProduct(customer1.getId(), linkedProduct2.getId());
        assertEquals("Check licence for linked product 2", 1, userLicences.size());
        assertTrue("Check licence is active", userLicences.get(0).isEnabled());
        assertTrue("Check licence has correct product", userLicences.get(0).getProductIds().contains(linkedProduct2.getId()));
        
    }
    
    @Test
    @DirtiesContext
    public void testSelfLicenceActivationWithCompletedAndExpriedRegistration() throws Exception {
    	httpServletRequest.setCookies(CookieHelper.createErightsCookie(customer2SessionId));
    	httpServletRequest.setParameter("url", "http://localhost/product1.html");
    	httpServletRequest.setParameter("denyReason", Integer.toString(ErightsDenyReason.DENY_ALL_LICENSE_DENIED.getReasonCode()));
        ModelAndView modelAndView = accessController.handleRequest(httpServletRequest, httpServletResponse);
        assertEquals(EACViews.NO_ACTIVE_LICENCE, getView(modelAndView));
        
        Registration<ProductRegistrationDefinition> registration = customerService.getRegistrationByRegistrationDefinitionAndCustomer(SessionHelper.getProductRegistrationDefinition(httpServletRequest), customer2);
        assertTrue("Complete registration", customer2ProductRegistration.getId().equals(registration.getId()));
        assertTrue("Is completed", registration.isCompleted());
        assertTrue("Is awaiting validation", registration.isAwaitingValidation());
        assertFalse("Is denied", registration.isDenied());
        assertTrue("Is activated", registration.isActivated());

    }
    
    @Test
    @DirtiesContext
    public void testProductRegistrationWithCompletedButExpriedRegistrationAndReRegistration() throws Exception {
    	
    	//Set up the session. The user has been denied by the adapter. DENY_ALL_LICENSE_DENIED
    	httpServletRequest.setCookies(CookieHelper.createErightsCookie(customer2SessionId));
    	httpServletRequest.setParameter("url", "http://localhost/product1.html");
    	httpServletRequest.setParameter("denyReason", Integer.toString(ErightsDenyReason.DENY_ALL_LICENSE_DENIED.getReasonCode()));
        ModelAndView modelAndView = accessController.handleRequest(httpServletRequest, httpServletResponse);
        
        //User should be shown NO_ACTIVE_LICENCE. This may allow them to choose to re-register
        assertEquals(EACViews.NO_ACTIVE_LICENCE, getView(modelAndView));
        
        //Check the state of the current registration. Only problem should be that it is expired.
        Registration<ProductRegistrationDefinition> registration = customerService.getRegistrationByRegistrationDefinitionAndCustomer(SessionHelper.getProductRegistrationDefinition(httpServletRequest), customer2);
        assertTrue("Complete registration", customer2ProductRegistration.getId().equals(registration.getId()));
        assertTrue("Is completed", registration.isCompleted());
        assertTrue("Is awaiting validation", registration.isAwaitingValidation());
        assertFalse("Is denied", registration.isDenied());
        assertTrue("Is activated", registration.isActivated());

        //Check user is not currently re-registering
        assertEquals("Check session does not contain re-register flag", null, SessionHelper.getReregister(httpServletRequest));
        
        //Fake the user clicking in NO_ACTIVE_LICENCE indicating that they wish to re-register
        modelAndView = reregisterController.handleRequest(httpServletRequest, httpServletResponse);
        
        //User clicks link, is forwarded to content and should have the re-register flag set in the session
        assertEquals("Check user forwarded to content", "http://localhost/product1.html?ERSESSION="+customer2SessionId, getView(modelAndView));
        assertEquals("Check session contains re-register flag", true, SessionHelper.getReregister(httpServletRequest));
        
        //Adapter should now pass user back to access controller, with re-register flag set. This time
        //user should be passed to product registration, re-register flag should be removed and new registration record should
        //be created in "incomplete" state
        modelAndView = accessController.handleRequest(httpServletRequest, httpServletResponse);
        assertEquals("Check user forwarded to registration page", EACViews.PRODUCT_REGISTRATION_VIEW, getView(modelAndView));
        assertEquals("Check session does not contain re-register flag", null, SessionHelper.getReregister(httpServletRequest));
        
        //Check the state of the current registration. Only problem should be that it is expired.
        registration = customerService.getRegistrationByRegistrationDefinitionAndCustomer(SessionHelper.getProductRegistrationDefinition(httpServletRequest), customer2);
        assertTrue("Check different registration", !customer2ProductRegistration.getId().equals(registration.getId()));
        assertTrue("Is not completed", !registration.isCompleted());
        assertTrue("Is not awaiting validation", !registration.isAwaitingValidation());
        assertFalse("Is not denied", registration.isDenied());
        assertTrue("Is not activated", !registration.isActivated());
   }
    
    @Test
    @DirtiesContext
    public void testActivationCodeRegistrationWithCompletedButExpriedRegistrationAndReRegistration() throws Exception {
    	
    	//Set up the session. The user has been denied by the adapter. DENY_ALL_LICENSE_DENIED
    	httpServletRequest.setCookies(CookieHelper.createErightsCookie(customer2SessionId));
    	httpServletRequest.setParameter("url", "http://localhost/product3.html");
    	httpServletRequest.setParameter("denyReason", Integer.toString(ErightsDenyReason.DENY_ALL_LICENSE_DENIED.getReasonCode()));
        ModelAndView modelAndView = accessController.handleRequest(httpServletRequest, httpServletResponse);
        
        //User should be shown NO_ACTIVE_LICENCE. This may allow them to choose to re-register
        assertEquals(EACViews.NO_ACTIVE_LICENCE, getView(modelAndView));
        
        //Check the state of the current registration. Only problem should be that it is expired.
        Registration<ProductRegistrationDefinition> registration = customerService.getRegistrationByRegistrationDefinitionAndCustomer(SessionHelper.getProductRegistrationDefinition(httpServletRequest), customer2);
        assertTrue("Complete registration", customer2ActivationCodeRegistration.getId().equals(registration.getId()));
        assertTrue("Is completed", registration.isCompleted());
        assertTrue("Is awaiting validation", registration.isAwaitingValidation());
        assertFalse("Is denied", registration.isDenied());
        assertTrue("Is activated", registration.isActivated());

        //Check user is not currently re-registering
        assertEquals("Check session does not contain re-register flag", null, SessionHelper.getReregister(httpServletRequest));
        
        //Fake the user clicking in NO_ACTIVE_LICENCE indicating that they wish to re-register
        modelAndView = reregisterController.handleRequest(httpServletRequest, httpServletResponse);
        
        //User clicks link, is forwarded to content and should have the re-register flag set in the session
        assertEquals("Check user forwarded to content", "http://localhost/product3.html?ERSESSION="+customer2SessionId, getView(modelAndView));
        assertEquals("Check session contains re-register flag", true, SessionHelper.getReregister(httpServletRequest));
        
        //Adapter should now pass user back to access controller, with re-register flag set. This time
        //user should be passed to product registration, re-register flag should be removed and new registration record should
        //be created in "incomplete" state
        modelAndView = accessController.handleRequest(httpServletRequest, httpServletResponse);
        assertEquals("Check user forwarded to activation code page", EACViews.INTERNAL_ACTIVATION_CODE_VIEW, getView(modelAndView));
        assertEquals("Check session still contains re-register flag", true, SessionHelper.getReregister(httpServletRequest));
        
        //Check current registration is unchanged
        registration = customerService.getRegistrationByRegistrationDefinitionAndCustomer(SessionHelper.getProductRegistrationDefinition(httpServletRequest), customer2);
        assertTrue("Complete registration", customer2ActivationCodeRegistration.getId().equals(registration.getId()));
        assertTrue("Is completed", registration.isCompleted());
        assertTrue("Is awaiting validation", registration.isAwaitingValidation());
        assertFalse("Is denied", registration.isDenied());
        assertTrue("Is activated", registration.isActivated());
        
        //Fake user entering activation code. This should create a registration and clear the re-register flag
        httpServletRequest.setParameter("code", activationCode2.getCode());
        httpServletRequest.setMethod("POST");
        modelAndView = activationCodeFormController.handleRequest(httpServletRequest, httpServletResponse);
        assertEquals("http://localhost/product3.html?ERSESSION="+customer2SessionId, getView(modelAndView));

        registration = customerService.getRegistrationByRegistrationDefinitionAndCustomer(SessionHelper.getProductRegistrationDefinition(httpServletRequest), customer2);
        assertTrue("Check different registration", !customer2ActivationCodeRegistration.getId().equals(registration.getId()));
        assertTrue("Is completed", registration.isCompleted());
        assertFalse("Is awaiting validation", registration.isAwaitingValidation());
        assertFalse("Is denied", registration.isDenied());
        assertFalse("Is activated", registration.isActivated()); 
        
        assertEquals("Check session does not contain re-register flag", null, SessionHelper.getReregister(httpServletRequest));        
   }
    
    private String getView(ModelAndView modelAndView) {
    	if(modelAndView.getView() instanceof RedirectView) {
    		return ((RedirectView)modelAndView.getView()).getUrl();
    	}
    	return modelAndView.getViewName();
    }
    
  
}
