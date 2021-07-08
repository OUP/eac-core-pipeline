package com.oup.eac.web.controllers.registration;

import javax.naming.NamingException;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.web.servlet.ModelAndView;

import com.oup.eac.common.mock.AbstractMockTest;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.ProductPageDefinition;
import com.oup.eac.domain.ProductRegistrationDefinition;
import com.oup.eac.service.RegistrationDefinitionService;
import com.oup.eac.service.RegistrationService;
import com.oup.eac.web.controllers.helpers.SessionHelper;
import com.oup.eac.web.interceptors.ProductRegistrationUpdateInterceptor;

public class ProductRegistrationUpdateInterceptorMockTest extends AbstractMockTest {

    private ProductRegistrationUpdateInterceptor sut;    
    private RegistrationDefinitionService registrationDefinitionService;
    private RegistrationService registrationService;
    private Customer customer;
    private ProductRegistrationDefinition prd;
    private Object handler;
    private ModelAndView modelAndView;

    /**
     * Instantiates a new product registration update interceptor mock test.
     * 
     * @throws NamingException
     *             the naming exception
     */
    public ProductRegistrationUpdateInterceptorMockTest() throws NamingException {
        super();
    }

    /**
     * Setup.
     */
    @Before
    public final void setup() {        
        registrationDefinitionService = EasyMock.createMock(RegistrationDefinitionService.class);
        sut = new ProductRegistrationUpdateInterceptor(registrationService);
        setMocks(registrationDefinitionService);
        this.customer = new Customer();
        this.customer.setId("cust-id-123");
        this.prd = new ProductRegistrationDefinition();
        this.prd.setPageDefinition(new ProductPageDefinition());
        this.handler = null;
        this.modelAndView = new ModelAndView();
    }


    /**
     * Test get happy path when Customer in session.
     * 
     * @throws Exception
     *             if something goes wrong
     */
    @Test
    public final void testGetErrorNoCustomer() throws Exception {

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod("GET");
        MockHttpSession session = new MockHttpSession();
        request.setSession(session);

        request.setParameter(ProductRegistrationUpdateInterceptor.PARAM_REGISTRATION_ID, "abc-123");

        MockHttpServletResponse response = new MockHttpServletResponse();

        replayMocks();
        try {
            sut.preHandle(request, response, null);
            sut.postHandle(request, response, handler, modelAndView);
        } catch (IllegalStateException ex) {
            Assert.assertEquals("No Customer in Session", ex.getMessage());
        } finally {
            verifyMocks();
        }

    }

    /**
     * Test get happy path when Customer in session.
     * 
     * @throws Exception
     *             if something goes wrong
     */
    @Test
    public final void testGetErrorNoRegistrationId() throws Exception {

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod("GET");
        MockHttpSession session = new MockHttpSession();
        request.setSession(session);

        SessionHelper.setCustomer(request, customer);

        MockHttpServletResponse response = new MockHttpServletResponse();

        replayMocks();
        try {
            sut.preHandle(request, response, null);
        } catch (IllegalStateException ex) {
            Assert.assertEquals("No Registration Id in Request", ex.getMessage());
        } finally {
            verifyMocks();
        }

    }

    /**
     * Test post happy path.
     * 
     * @throws Exception
     *             the exception
     */
    @Test
    public final void testPostHappyPath() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod("POST");
        MockHttpSession session = new MockHttpSession();
        request.setSession(session);

        SessionHelper.setCustomer(request, customer);
        SessionHelper.setProductRegistrationDefinition(request, prd);

        MockHttpServletResponse response = new MockHttpServletResponse();

        replayMocks();
        sut.preHandle(request, response, null);
        verifyMocks();
    }

    /**
     * Test post happy path.
     * 
     * @throws Exception
     *             the exception
     */
    @Test
    public final void testPostErrorNoProductRegistrationDefinition() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod("POST");        
        MockHttpSession session = new MockHttpSession();
        request.setSession(session);
        SessionHelper.setCustomer(request, customer);

        MockHttpServletResponse response = new MockHttpServletResponse();

        replayMocks();
        try {
            sut.preHandle(request, response, null);
        } catch (IllegalStateException ex) {
            Assert.assertEquals("No Product Registration Definition in Session", ex.getMessage());
        } finally {
            verifyMocks();
        }
    }

    /**
     * Test error if not GET or POST.
     * 
     * @throws Exception
     *             the exception
     */
    @Test
    public final void testErrorBadRequestMethod() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod("HEAD");
        MockHttpSession session = new MockHttpSession();
        request.setSession(session);
        SessionHelper.setCustomer(request, customer);

        MockHttpServletResponse response = new MockHttpServletResponse();

        replayMocks();
        try {
            sut.preHandle(request, response, null);
        } catch (IllegalStateException ex) {
            Assert.assertEquals("Unexpected Http Request Method type : HEAD", ex.getMessage());
        } finally {
            verifyMocks();
        }
    }


    /**
     * Test get happy path when customer is not in session but EAC cookie is
     * present.
     * 
     * @throws Exception
     *             if something goes wrong
     */
    @Test
    public final void testErrorGetLoggedInNoCustomerInSession() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod("GET");
        MockHttpSession session = new MockHttpSession();
        request.setSession(session);

        Assert.assertNull(SessionHelper.getCustomer(request));

        request.setParameter(ProductRegistrationUpdateInterceptor.PARAM_REGISTRATION_ID, "abc-123");

        MockHttpServletResponse response = new MockHttpServletResponse();

        replayMocks();
        try {
            sut.preHandle(request, response, null);
            Assert.fail("exception expected");
        } catch (Exception ex) {
            Assert.assertTrue(ex instanceof IllegalStateException);
            IllegalStateException ill = (IllegalStateException) ex;
            Assert.assertEquals("No Customer in Session", ill.getMessage());
        } finally {
            verifyMocks();
        }
    }

}
