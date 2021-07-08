package com.oup.eac.web.controllers.registration;

import java.util.Locale;

import javax.naming.NamingException;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.oup.eac.common.mock.AbstractMockTest;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.ProductRegistration;
import com.oup.eac.domain.ProductRegistrationDefinition;
import com.oup.eac.domain.Registration;
import com.oup.eac.dto.ProductRegistrationDto;
import com.oup.eac.service.RegistrationService;
import com.oup.eac.service.ServiceLayerException;
import com.oup.eac.web.controllers.helpers.SessionHelper;

public class ProductRegistrationUpdateControllerMockTest extends AbstractMockTest {

    private static final String PARAM_REGISTRATION_ID = "registrationId";
    private RegistrationService registrationService;
    private ProductRegistrationUpdateController sut;
    private Customer customer;
    private ProductRegistrationDefinition prd;
    private ProductRegistrationDto registrationDto;
    private static final String REGISTRATION_ID = "REG_ID";

    /**
     * Instantiates a new product registration update controller mock test.
     * 
     * @throws NamingException
     *             the naming exception
     */
    public ProductRegistrationUpdateControllerMockTest() throws NamingException {
        super();
    }

    /**
     * Setup.
     */
    @Before
    public final void setup() {
        this.registrationService = EasyMock.createMock(RegistrationService.class);
        sut = new ProductRegistrationUpdateController(registrationService);
        setMocks(registrationService);
        customer = new Customer();
        customer.setId("CUST_ID");
        prd = new ProductRegistrationDefinition();
        registrationDto = new ProductRegistrationDto();
    }

    /**
     * Test get happy path.
     * 
     * @throws Exception
     *             the exception
     */
    @Test
    public final void testGetHappyPath() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter(PARAM_REGISTRATION_ID, REGISTRATION_ID);
        MockHttpSession session = new MockHttpSession();
        request.setSession(session);

        SessionHelper.setCustomer(request, customer);
        SessionHelper.setProductRegistrationDefinition(request, prd);
        session.setNew(true);
        request.setMethod("GET");

        EasyMock.expect(this.registrationService.getProductPageDefinitionByRegistrationDefinition(prd, customer, Locale.ENGLISH)).andReturn(registrationDto);

        EasyMock.replay(getMocks());

        ServletRequestDataBinder binder = new ServletRequestDataBinder(null);
        sut.initBinder(request, binder);

        Object formBackingObject = sut.formBackingObject(request);
        EasyMock.verify(getMocks());

        Assert.assertEquals(registrationDto, formBackingObject);
        Assert.assertEquals(Boolean.TRUE, request.getAttribute(ProductRegistrationUpdateController.UPDATING_PRODUCT_REGISTRATION));
    }

    /**
     * Test get error nothing in session.
     * 
     * @throws Exception
     *             the exception
     */
    @Test
    public final void testGetErrorNoCustomer() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter(PARAM_REGISTRATION_ID, REGISTRATION_ID);
        MockHttpSession session = new MockHttpSession();
        request.setSession(session);
        request.setMethod("GET");

        ServletRequestDataBinder binder = new ServletRequestDataBinder(null);

        replayMocks();
        try {
            sut.initBinder(request, binder);
            sut.formBackingObject(request);
            Assert.fail("exception expected");
        } catch (IllegalStateException ex) {
            Assert.assertEquals("No Customer in Session.", ex.getMessage());
        } finally {
            verifyMocks();
        }
    }

    /**
     * Test get error customer only in session.
     * 
     * @throws Exception
     *             the exception
     */
    @Test
    public final void testGetErrorCustomerOnlyInSession() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter(PARAM_REGISTRATION_ID, REGISTRATION_ID);
        MockHttpSession session = new MockHttpSession();
        request.setSession(session);
        request.setMethod("GET");
        SessionHelper.setCustomer(request, customer);

        ServletRequestDataBinder binder = new ServletRequestDataBinder(null);
        replayMocks();

        try {
            sut.initBinder(request, binder);
            sut.formBackingObject(request);
        } catch (Exception ex) {
            Assert.assertEquals("No ProductRegistrationDefinition in Session.", ex.getMessage());
        } finally {
            verifyMocks();
        }

    }

    /**
     * Test get error null registration dto.
     * 
     * @throws Exception
     *             the exception
     */
    @Test
    public final void testGetErrorNullRegistrationDto() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter(PARAM_REGISTRATION_ID, REGISTRATION_ID);
        MockHttpSession session = new MockHttpSession();
        request.setSession(session);
        request.setMethod("GET");
        SessionHelper.setCustomer(request, customer);
        SessionHelper.setProductRegistrationDefinition(request, prd);

        ServletRequestDataBinder binder = new ServletRequestDataBinder(null);
        EasyMock.expect(this.registrationService.getProductPageDefinitionByRegistrationDefinition(prd, customer, Locale.ENGLISH)).andReturn(null);
        replayMocks();

        try {
            sut.initBinder(request, binder);
            sut.formBackingObject(request);
        } catch (IllegalStateException ex) {
            Assert.assertEquals("Problem getting RegistrationDto form backing object.", ex.getMessage());
        } finally {
            verifyMocks();
        }

    }

    /**
     * Test post happy path.
     * 
     * @throws Exception
     *             if something goes wrong
     */
    @Ignore
    @Test
    public final void testPostHappyPath() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockHttpSession session = new MockHttpSession();
        request.setSession(session);
        
        
        SessionHelper.setRegistrationId(request, REGISTRATION_ID);
        SessionHelper.setCustomer(request, customer);

        SessionHelper.setForwardUrl(session, "http://blah.com");
        session.setNew(true);
        request.setMethod("POST");
        ProductRegistration reg = new ProductRegistration();
        reg.setId(REGISTRATION_ID);
        registrationService.updateRegistration(registrationDto, customer, reg);
        EasyMock.expectLastCall();
        Registration<? extends ProductRegistrationDefinition> registration =null ;
       // List<Registration<? extends ProductRegistrationDefinition>> registrations = new ArrayList<Registration<? extends ProductRegistrationDefinition>>();
       // ProductRegistrationDefinition registration = new ProductRegistrationDefinition();
        String registrationId=null,customerId=null;
      //  EasyMock.expect(this.registrationService.getProductRegistration(registrationId, customerId)).andReturn(
        EasyMock.replay(getMocks());

        ServletRequestDataBinder binder = new ServletRequestDataBinder(null);
        sut.initBinder(request, binder);

        BindException errors = new BindException(registrationDto, "registrationDto");
        ModelAndView result = sut.onSubmit(request, response, registrationDto, errors);
        EasyMock.verify(getMocks());

        Assert.assertTrue(result.getView() instanceof RedirectView);
        RedirectView red = (RedirectView) result.getView();
        Assert.assertEquals("profile.htm", red.getUrl());
        Assert.assertNull(request.getAttribute(ProductRegistrationUpdateController.UPDATING_PRODUCT_REGISTRATION));
        Assert.assertNull(SessionHelper.getRegistrationId(request));

    }

    /**
     * Test post error no customer.
     * 
     * @throws Exception
     *             if something goes wrong
     */

    /**
     * Test post error no customer.
     * 
     * @throws Exception
     *             if something goes wrong
     */
   
    @Ignore
    @Test
    public final void testPostErrorUpdateFailed() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockHttpSession session = new MockHttpSession();
        request.setSession(session);
        
        SessionHelper.setRegistrationId(request, REGISTRATION_ID);

        SessionHelper.setCustomer(request, customer);
        SessionHelper.setForwardUrl(session, "http://blah.com");
        session.setNew(true);
        request.setMethod("POST");
        ProductRegistration reg = new ProductRegistration();
        reg.setId(REGISTRATION_ID);
        registrationService.updateRegistration(registrationDto, customer, reg);
        EasyMock.expectLastCall().andThrow(new ServiceLayerException());

        EasyMock.replay(getMocks());

        ServletRequestDataBinder binder = new ServletRequestDataBinder(null);
        sut.initBinder(request, binder);

        BindException errors = new BindException(registrationDto, "registrationDto");
        ModelAndView result = sut.onSubmit(request, response, registrationDto, errors);
        EasyMock.verify(getMocks());

        // redisplay the form
        Assert.assertFalse(result.getView() instanceof RedirectView);
        Assert.assertEquals(null, result.getViewName());
        Assert.assertEquals(registrationDto, result.getModel().get("registrationDto"));
    }

    /**
     * Test post error no customer.
     * 
     * @throws Exception
     *             if something goes wrong
     */
    @Test
    public final void testPostNoCustomerInSession() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter(PARAM_REGISTRATION_ID, REGISTRATION_ID);
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockHttpSession session = new MockHttpSession();
        request.setSession(session);

        SessionHelper.setForwardUrl(session, "http://blah.com");
        session.setNew(true);
        request.setMethod("POST");

        EasyMock.replay(getMocks());

        ServletRequestDataBinder binder = new ServletRequestDataBinder(null);
        try {
            BindException errors = new BindException(registrationDto, "registrationDto");
            sut.initBinder(request, binder);
            sut.onSubmit(request, response, registrationDto, errors);
            Assert.fail("exception expected");
        } catch (IllegalStateException ex) {
            Assert.assertEquals("No Customer in Session.", ex.getMessage());
        } finally {
            EasyMock.verify(getMocks());
        }

    }
}
