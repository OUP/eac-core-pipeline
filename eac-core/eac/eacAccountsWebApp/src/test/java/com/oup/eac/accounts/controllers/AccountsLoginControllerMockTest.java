package com.oup.eac.accounts.controllers;

import java.util.Locale;

import javax.naming.NamingException;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import com.oup.eac.accounts.validators.AccountsLoginFormValidator;
import com.oup.eac.common.mock.AbstractMockTest;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.User.EmailVerificationState;
import com.oup.eac.dto.CustomerSessionDto;
import com.oup.eac.dto.LoginDto;
import com.oup.eac.service.CustomerService;
import com.oup.eac.service.DomainSkinResolverService;
import com.oup.eac.service.ServiceLayerException;
import com.oup.eac.web.controllers.helpers.CookieHelper;

public class AccountsLoginControllerMockTest extends AbstractMockTest {

    private CustomerService customerServiceMock;
    private AccountsLoginFormValidator loginFormValidatorMock;
    private DomainSkinResolverService domainSkinResolverServiceMock;
    private AccountsLoginController accountsLoginFormControler;
    
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private LoginDto loginDto;
    private BindingResult bindingResult;
    private CustomerSessionDto customerSessionDto;
    private Customer customer;
    private Locale locale;
    
    private final String ACTIVATION_CODE_VIEW = "redirect:/activationCode.htm";
    private final String LOGIN_FORM_VIEW = "loginForm";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    public AccountsLoginControllerMockTest() throws NamingException {
        super();
    }
    
    @Before
    public void setup(){
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        customerServiceMock = EasyMock.createMock(CustomerService.class);
        loginFormValidatorMock = EasyMock.createMock(AccountsLoginFormValidator.class);
        domainSkinResolverServiceMock = EasyMock.createMock(DomainSkinResolverService.class);
        setMocks(customerServiceMock, loginFormValidatorMock, domainSkinResolverServiceMock);
        accountsLoginFormControler = new AccountsLoginController(customerServiceMock, loginFormValidatorMock, domainSkinResolverServiceMock);
        loginDto = new LoginDto();
        loginDto.setUsername(USERNAME);
        loginDto.setPassword(PASSWORD);
        customerSessionDto = new CustomerSessionDto();
        customer = new Customer();
        customerSessionDto.setCustomer(customer);

        locale = Locale.FRANCE;
        customer.setEmailVerificationState(EmailVerificationState.VERIFIED);
        customer.setResetPassword(false);
        
        bindingResult = new BindException(loginDto, "loginDto");
        }
    
    @Test
    public void testloginSuccess() throws Exception{
        
        customer.setLocale(locale);
        EasyMock.expect(customerServiceMock.getCustomerByUsernameAndPassword(USERNAME, PASSWORD)).andReturn(customerSessionDto);
        EasyMock.expectLastCall();

        replayMocks();
        ModelAndView result = accountsLoginFormControler.authenticate(loginDto, bindingResult, request, response);
        Assert.assertNotNull(result);
        String viewName = result.getViewName();
        Assert.assertEquals(ACTIVATION_CODE_VIEW, viewName);
        verifyMocks();
    }
    
    @Test
    public void testServiceLayerException() throws Exception {

        customer.setResetPassword(true);
        EasyMock.expect(customerServiceMock.getCustomerByUsernameAndPassword(USERNAME, PASSWORD)).andThrow(new ServiceLayerException());

        replayMocks();

        ModelAndView result = accountsLoginFormControler.authenticate(loginDto, bindingResult, request, response);
        Assert.assertNotNull(result);
        Assert.assertEquals(LOGIN_FORM_VIEW, result.getViewName());
        verifyMocks();
    }
    
    @Test
    public void testPrimaryDomainSessionAvailableButInvalid() throws Exception {
        String session = "IUHDUYGBSDUDG";
        
        request.setMethod("GET");
        request.setCookies(CookieHelper.createErightsCookie(session));
        
        EasyMock.expect(customerServiceMock.getCustomerFromSession(session)).andReturn(null);
        
        replayMocks();

        ModelAndView result = accountsLoginFormControler.showForm(request, response);

        Assert.assertEquals(LOGIN_FORM_VIEW, result.getViewName());

        verifyMocks();
    }    
    
    @Test
    public void testPrimaryDomainSessionAvailableAndValid() throws Exception {
        String session = "IUHDUYGBSDUDG";
        
        request.setMethod("GET");
        request.setCookies(CookieHelper.createErightsCookie(session));
        
        EasyMock.expect(customerServiceMock.getCustomerFromSession(session)).andReturn(customer);
        
        replayMocks();

        ModelAndView result = accountsLoginFormControler.showForm(request, response);

        Assert.assertNotNull(result);
        String viewName = result.getViewName();
        Assert.assertEquals(ACTIVATION_CODE_VIEW, viewName);
        /*Assert.assertEquals(URL+ "?" + ERSESSION + "=" + session, rv.getUrl());*/

        verifyMocks();
    }
    
}
