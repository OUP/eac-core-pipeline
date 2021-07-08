package com.oup.eac.web.controllers.authentication;

import java.util.HashMap;
import java.util.Map;

import javax.naming.NamingException;

import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

import com.oup.eac.common.mock.AbstractMockTest;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.ProductRegistration;
import com.oup.eac.domain.ProductRegistrationDefinition;
import com.oup.eac.domain.RegisterableProduct;
import com.oup.eac.domain.utils.audit.TestingAppender;
import com.oup.eac.service.RegistrationService;

/**
 * 
 * @author David Hay
 *
 */
public class ValidatorRegistrationAllowControllerMockTest extends AbstractMockTest {

    private static final String TEST_PRODUCT_NAME = "testProductName";
    private static final String TEST_CUSTOMER_USERNAME = "testCustomerUsername";
    private static final String TEST_PRODUCT_EMAIL = "testProductEmail";
    private static final String TEST_PRODUCT_HOME = "testProductHome";
    private static final String TEST_PRODUCT_LANDING = "testProductLanding";
    private static final String TEST_URL = "http://www.google.com";
    


    public ValidatorRegistrationAllowControllerMockTest() throws NamingException {
        super();
    }

    private ValidatorRegistrationAllowController sut;
    
    private RegistrationService registrationService;
    private ProductRegistration registration;
    private ProductRegistrationDefinition prd = new ProductRegistrationDefinition();
    private Customer customer;
    private RegisterableProduct product;
    private TestingAppender testingAppender;
    
    
    @Before
    public void setup() {
        this.registrationService = EasyMock.createMock(RegistrationService.class);
        this.registration = new ProductRegistration();
        this.customer = new Customer();
        this.prd = new ProductRegistrationDefinition();
        this.product = new RegisterableProduct();
        
        this.registration.setCustomer(customer);
        this.registration.setRegistrationDefinition(prd);
        this.prd.setProduct(product);
        this.product.setHomePage(TEST_PRODUCT_HOME);
        this.product.setEmail(TEST_PRODUCT_EMAIL);
        this.product.setLandingPage(TEST_PRODUCT_LANDING);
        
        this.product.setProductName(TEST_PRODUCT_NAME);
        this.customer.setUsername(TEST_CUSTOMER_USERNAME);
               
        setMocks(this.registrationService);
        sut = new ValidatorRegistrationAllowController();
        this.testingAppender = new TestingAppender();

    }
    
    @Test
    public void testNoTokenParameter() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();        
        MockHttpServletResponse response = new MockHttpServletResponse();
        
        ModelAndView mav = sut.handleRequest(request, response);
        Assert.assertEquals("activationError", mav.getViewName());
    }

    @Test
    public void testNotActivated() throws Exception {
        boolean activated = false;
        checkAllowLink(activated, TEST_URL);
        Assert.assertEquals(0, testingAppender.getMessages().size());
    }
    
    @Test
    public void testActivated() throws Exception {
        boolean activated = true;
        checkAllowLink(activated, TEST_URL);
        
    }
    
    private void checkAllowLink(boolean activated, String originalUrl) throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("username", TEST_CUSTOMER_USERNAME);
        request.setParameter("product", product.getProductName());
        MockHttpServletResponse response = new MockHttpServletResponse();
        
        Map<String,Object> map1 = new HashMap<String,Object>();
        map1.put("originalUrl", originalUrl);
        map1.put("activated", activated);
        map1.put("registration", registration);
        map1.put("username", TEST_CUSTOMER_USERNAME);
        map1.put("product", product.getProductName());
        
        ModelAndView mav = sut.handleRequest(request, response);
        
        Assert.assertEquals("validatorLicenceAllowed", mav.getViewName());
        Map<String,Object> model = mav.getModel();
        Assert.assertEquals(2, model.size());
        Assert.assertEquals(TEST_CUSTOMER_USERNAME, model.get("username"));
        Assert.assertEquals(TEST_PRODUCT_NAME, model.get("product"));
        /*Assert.assertEquals(TEST_PRODUCT_HOME, model.get("productHome"));
        Assert.assertEquals(TEST_PRODUCT_EMAIL, model.get("email"));*/
        //Assert.assertEquals(TEST_URL, model.get("originalUrl"));
        
    }

    
    
}
