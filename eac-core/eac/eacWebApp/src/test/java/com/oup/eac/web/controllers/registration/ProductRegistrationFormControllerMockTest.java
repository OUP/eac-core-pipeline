package com.oup.eac.web.controllers.registration;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import javax.naming.NamingException;
import javax.servlet.http.Cookie;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

import com.oup.eac.common.mock.AbstractMockTest;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.ProductRegistrationDefinition;
import com.oup.eac.domain.RegisterableProduct;
import com.oup.eac.dto.LicenceDto;
import com.oup.eac.dto.ProductRegistrationDto;
import com.oup.eac.dto.RegistrationDto;
import com.oup.eac.integration.facade.ErightsFacade;
import com.oup.eac.service.CustomerService;
import com.oup.eac.service.RegistrationService;
import com.oup.eac.service.ServiceLayerException;
import com.oup.eac.service.exceptions.CustomerNotFoundServiceLayerException;
import com.oup.eac.web.controllers.helpers.SessionHelper;

public class ProductRegistrationFormControllerMockTest extends AbstractMockTest {

    private static final String REGISTRATION_ID = "regId123";
    private static final String FORWARD_URL = "http://www.google.com";
    private static final String ERIGHTS_SESSION_ID = "atypon1";
    private static final Object ACCESS_URL = "access.htm";

    public ProductRegistrationFormControllerMockTest() throws NamingException {
        super();
    }

    private ProductRegistrationFormController sut;
    private ErightsFacade erightsFacade;
    private RegistrationService registrationServiceP;
    private CustomerService customerServiceP;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private MockHttpSession session;
    private Customer customer;
    private ProductRegistrationDefinition prd;
    private RegisterableProduct product;

    @Before
    public void setup() {
        this.registrationServiceP = EasyMock.createMock(RegistrationService.class);
        this.customerServiceP = EasyMock.createMock(CustomerService.class);
        this.erightsFacade = EasyMock.createMock(ErightsFacade.class);
        this.sut = new ProductRegistrationFormController(registrationServiceP, customerServiceP, erightsFacade);
        this.product = new RegisterableProduct();
        setMocks(this.registrationServiceP, this.customerServiceP, this.erightsFacade);

        this.request = new MockHttpServletRequest();
        this.response = new MockHttpServletResponse();
        this.session = new MockHttpSession();
        this.request.setSession(session);
        this.customer = new Customer();
        this.prd = new ProductRegistrationDefinition();

    }

    @Test
    public void testGetNoCustomerInSession() throws Exception {
        request.setMethod("GET");
        SessionHelper.setCustomer(request, null);
        replayMocks();
        try {
            sut.formBackingObject(request);
            Assert.fail("exception expected");
        } catch (Exception ex) {
            Assert.assertEquals("Invalid user session. No customer is available.", ex.getMessage());
        } 
    }

    @Test
    public void testGetWithCustomerInSession() throws Exception {
        request.setMethod("GET");
        SessionHelper.setCustomer(request, customer);
        SessionHelper.setProductRegistrationDefinition(request, prd);

        ProductRegistrationDto regDto = new ProductRegistrationDto();
        EasyMock.expect(
                this.registrationServiceP.getProductPageDefinitionByRegistrationDefinition(EasyMock.eq(prd), EasyMock.eq(customer),
                        EasyMock.anyObject(Locale.class))).andReturn(regDto);
        replayMocks();
        try {
            Object result = sut.formBackingObject(request);
            Assert.assertEquals(regDto, result);
        } catch (Exception ex) {
            Assert.fail("unexpected exception");
        } 
    }

    /*@Test
    public void testSubmit1Success() throws Exception {
        request.setMethod("POST");
        SessionHelper.setForwardUrl(session, FORWARD_URL);
        String customerId = UUID.randomUUID().toString();
        customer.setId(customerId);
        SessionHelper.setCustomer(request, customer);
        SessionHelper.setProductRegistrationDefinition(request, prd);
        SessionHelper.setRegistrationId(request, REGISTRATION_ID);
        product.setId(REGISTRATION_ID);
        SessionHelper.setRegisterableProduct(request, product);
        RegistrationDto command = new ProductRegistrationDto();
        BindException errors = new BindException(command, "registrationDto");
        registrationServiceP.saveCompleteRegsitration(command, customer, REGISTRATION_ID);
        EasyMock.expectLastCall();
        
        List<LicenceDto> licList = new ArrayList<LicenceDto>();
        LicenceDto license = new LicenceDto();
        license.setLicenseId(UUID.randomUUID().toString());
        licList.add(license);
        EasyMock.expect(this.erightsFacade.getLicensesForUser(EasyMock.anyObject(String.class), EasyMock.eq(REGISTRATION_ID))).andReturn(licList);
        this.erightsFacade.updateLicence(EasyMock.anyObject(String.class), EasyMock.eq(license));
        EasyMock.expectLastCall();
        replayMocks();
        try {
            ModelAndView result = sut.onSubmit(request, response, command, errors);
            RedirectView rv = (RedirectView) result.getView();
            Assert.assertEquals(FORWARD_URL, rv.getUrl());
        } catch (Exception ex) {
            Assert.fail("unexpected exception");
        } 
    }*/

    /*@Test
    public void testSubmit2Success() throws Exception {
        request.setMethod("POST");
        SessionHelper.setForwardUrl(session, FORWARD_URL);
        Cookie cookie = new Cookie("EAC", ERIGHTS_SESSION_ID);
        request.setCookies(cookie);
        String customerId = UUID.randomUUID().toString();
        customer.setId(customerId);
        SessionHelper.setCustomer(request, customer);
        SessionHelper.setProductRegistrationDefinition(request, prd);
        SessionHelper.setRegistrationId(request, REGISTRATION_ID);
        product.setId(REGISTRATION_ID);
        SessionHelper.setRegisterableProduct(request, product);
        RegistrationDto command = new ProductRegistrationDto();
        BindException errors = new BindException(command, "registrationDto");

        EasyMock.expect(customerServiceP.getCustomerFromSession(ERIGHTS_SESSION_ID)).andReturn(customer);
        registrationServiceP.saveCompleteRegsitration(command, customer, REGISTRATION_ID);
        EasyMock.expectLastCall();

        List<LicenceDto> licList = new ArrayList<LicenceDto>();
        LicenceDto license = new LicenceDto();
        license.setLicenseId(UUID.randomUUID().toString());
        licList.add(license);
        EasyMock.expect(this.erightsFacade.getLicensesForUser(EasyMock.anyObject(String.class), EasyMock.eq(REGISTRATION_ID))).andReturn(licList);
        this.erightsFacade.updateLicence(EasyMock.anyObject(String.class), EasyMock.eq(license));
        EasyMock.expectLastCall();
        
        replayMocks();
        try {
            ModelAndView result = sut.onSubmit(request, response, command, errors);
            RedirectView rv = (RedirectView) result.getView();
            Assert.assertEquals(FORWARD_URL, rv.getUrl());
        } catch (Exception ex) {
            Assert.fail("unexpected exception");
        }
    }*/

    @Test
    public void testSubmit3Fail() throws Exception {
        request.setMethod("POST");
        SessionHelper.setForwardUrl(session, FORWARD_URL);
        Cookie cookie = new Cookie("EAC", ERIGHTS_SESSION_ID);
        request.setCookies(cookie);
        SessionHelper.setCustomer(request, null);
        SessionHelper.setProductRegistrationDefinition(request, prd);
        SessionHelper.setRegistrationId(request, REGISTRATION_ID);
        RegistrationDto command = new ProductRegistrationDto();
        BindException errors = new BindException(command, "registrationDto");

        EasyMock.expect(customerServiceP.getCustomerFromSession(ERIGHTS_SESSION_ID)).andReturn(null);
        EasyMock.expectLastCall();

        replayMocks();
        try {
            ModelAndView result = sut.onSubmit(request, response, command, errors);
            RedirectView rv = (RedirectView) result.getView();
            Assert.assertEquals(ACCESS_URL, rv.getUrl());
        } catch (Exception ex) {
            Assert.fail("unexpected exception");
        } 
    }

    @Test
    public void testSubmit4Fail() throws Exception {
        request.setMethod("POST");
        SessionHelper.setForwardUrl(session, FORWARD_URL);
        Cookie cookie = new Cookie("EAC", ERIGHTS_SESSION_ID);
        request.setCookies(cookie);
        SessionHelper.setCustomer(request, null);
        SessionHelper.setProductRegistrationDefinition(request, prd);
        SessionHelper.setRegistrationId(request, REGISTRATION_ID);
        RegistrationDto command = new ProductRegistrationDto();
        BindException errors = new BindException(command, "registrationDto");

        EasyMock.expect(customerServiceP.getCustomerFromSession(ERIGHTS_SESSION_ID)).andThrow(new CustomerNotFoundServiceLayerException());
        customerServiceP.logout(ERIGHTS_SESSION_ID);
        EasyMock.expectLastCall();

        replayMocks();
        try {
            ModelAndView result = sut.onSubmit(request, response, command, errors);
            RedirectView rv = (RedirectView) result.getView();
            Assert.assertEquals(ACCESS_URL, rv.getUrl());
            Cookie eac = response.getCookie("EAC");
            Assert.assertEquals(0, eac.getMaxAge());
        } catch (Exception ex) {
            Assert.fail("unexpected exception");
        } 
    }

    @Test
    public void testSubmit5Fail() throws Exception {
        request.setMethod("POST");
        SessionHelper.setForwardUrl(session, FORWARD_URL);
        SessionHelper.setCustomer(request, null);
        SessionHelper.setProductRegistrationDefinition(request, prd);
        SessionHelper.setRegistrationId(request, REGISTRATION_ID);
        RegistrationDto command = new ProductRegistrationDto();
        BindException errors = new BindException(command, "registrationDto");

        replayMocks();
        try {
            ModelAndView result = sut.onSubmit(request, response, command, errors);
            RedirectView rv = (RedirectView) result.getView();
            Assert.assertEquals(ACCESS_URL, rv.getUrl());
        } catch (Exception ex) {
            Assert.fail("unexpected exception");
        } 
    }
    
    @Test
    public void testSubmit6Fail() throws Exception {
        request.setMethod("POST");
        SessionHelper.setForwardUrl(session, FORWARD_URL);
        Cookie cookie = new Cookie("EAC", ERIGHTS_SESSION_ID);
        request.setCookies(cookie);
        String customerId = UUID.randomUUID().toString();
        customer.setId(customerId);
        SessionHelper.setCustomer(request, customer);
        SessionHelper.setProductRegistrationDefinition(request, prd);
        SessionHelper.setRegistrationId(request, REGISTRATION_ID);
        product.setId(REGISTRATION_ID);
        SessionHelper.setRegisterableProduct(request, product);
        
        RegistrationDto command = new ProductRegistrationDto();
        BindException errors = new BindException(command, "registrationDto");

        EasyMock.expect(customerServiceP.getCustomerFromSession(ERIGHTS_SESSION_ID)).andReturn(customer);
        registrationServiceP.saveCompleteRegsitration(command, customer, REGISTRATION_ID);
        EasyMock.expectLastCall().andThrow(new ServiceLayerException());

        List<LicenceDto> licList = new ArrayList<LicenceDto>();
        LicenceDto license = new LicenceDto();
        license.setLicenseId(UUID.randomUUID().toString());
        licList.add(license);
        EasyMock.expect(this.erightsFacade.getLicensesForUser(EasyMock.anyObject(String.class), EasyMock.eq(REGISTRATION_ID))).andReturn(licList);
        this.erightsFacade.updateLicence(EasyMock.anyObject(String.class), EasyMock.eq(license));
        EasyMock.expectLastCall();
        
        replayMocks();
        try {
            ModelAndView result = sut.onSubmit(request, response, command, errors);
            View view = result.getView();
            Assert.assertNull(view);//meaning form would be displayed
        } catch (Exception ex) {
            Assert.fail("unexpected exception");
        } 
    }

}