package com.oup.eac.web.controllers.thirdpartyui;

import javax.naming.NamingException;
import javax.servlet.http.Cookie;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

import com.oup.eac.common.mock.AbstractMockTest;
import com.oup.eac.domain.Customer;
import com.oup.eac.service.CustomerService;
import com.oup.eac.service.exceptions.CustomerNotFoundServiceLayerException;

public class EacValidateCookieControllerMockTest extends AbstractMockTest {

    private static final String SUCCESS_URL = "http://localost:8080/successUrl";
    private static final String FAILURE_URL = "http://localost:8080/failureUrl";
    private static final String ERROR_URL = "http://localost:8080/errorUrl";
    private static final String EAC = "EAC";

    private EacValidateCookieController sut;
    private CustomerService customerService;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private Customer customer;

    public EacValidateCookieControllerMockTest() throws NamingException {
        super();
    }

    @Before
    public void setup() {
        customerService = EasyMock.createMock(CustomerService.class);
        sut = new EacValidateCookieController(customerService);
        setMocks(this.customerService);

        this.request = new MockHttpServletRequest();
        this.request.setMethod("GET");
        this.response = new MockHttpServletResponse();
        this.customer = new Customer();
    }

    @Test
    public void testValidCookie() throws CustomerNotFoundServiceLayerException {
        request.setCookies(new Cookie(EAC, "validValue"));
        EasyMock.expect(customerService.getCustomerFromSession("validValue")).andReturn(customer);
        replayMocks();
        ModelAndView result = sut.validateEacCookie(request, response, SUCCESS_URL, FAILURE_URL, ERROR_URL);
        checkResult(result, true, SUCCESS_URL + "?ERSESSION=validValue");
        verifyMocks();
    }

    @Test
    public void testInvalidCookie1() throws CustomerNotFoundServiceLayerException {
        request.setCookies(new Cookie(EAC, "invalidValue"));
        EasyMock.expect(customerService.getCustomerFromSession("invalidValue")).andReturn(null);
        replayMocks();
        ModelAndView result = sut.validateEacCookie(request, response, SUCCESS_URL, FAILURE_URL, ERROR_URL);
        checkResult(result, true, FAILURE_URL);
        verifyMocks();
    }

    @Test
    public void testInvalidCookie2() throws CustomerNotFoundServiceLayerException {
        request.setCookies(new Cookie(EAC, "invalidValue"));
        EasyMock.expect(customerService.getCustomerFromSession("invalidValue")).andThrow(new CustomerNotFoundServiceLayerException());
        replayMocks();
        ModelAndView result = sut.validateEacCookie(request, response, SUCCESS_URL, FAILURE_URL, ERROR_URL);
        checkResult(result, true, FAILURE_URL);
        verifyMocks();
    }

    @Test
    public void testInvalidCookie3() throws CustomerNotFoundServiceLayerException {
        replayMocks();
        ModelAndView result = sut.validateEacCookie(request, response, SUCCESS_URL, FAILURE_URL, ERROR_URL);
        checkResult(result, true, FAILURE_URL);
        verifyMocks();
    }

    @Test
    public void testInvalidCookie4() throws CustomerNotFoundServiceLayerException {
        request.setCookies(new Cookie(EAC, "     "));
        replayMocks();
        ModelAndView result = sut.validateEacCookie(request, response, SUCCESS_URL, FAILURE_URL, ERROR_URL);
        checkResult(result, true, FAILURE_URL);
        verifyMocks();
    }

    @Test
    public void testError1ExceptionWithErrorUrl() throws CustomerNotFoundServiceLayerException {
        request.setCookies(new Cookie(EAC, "validValue"));
        EasyMock.expect(customerService.getCustomerFromSession("validValue")).andThrow(new RuntimeException());
        replayMocks();
        ModelAndView result = sut.validateEacCookie(request, response, SUCCESS_URL, FAILURE_URL, ERROR_URL);
        checkResult(result, true, ERROR_URL);        
       verifyMocks();
    }

    @Test
    public void testError2ExceptionWithoutErrorUrl() throws CustomerNotFoundServiceLayerException {
        request.setCookies(new Cookie(EAC, "validValue"));

        EasyMock.expect(customerService.getCustomerFromSession("validValue")).andThrow(new RuntimeException());

        replayMocks();

        ModelAndView result = sut.validateEacCookie(request, response, SUCCESS_URL, FAILURE_URL, null);
        checkResult(result, false, EacValidateCookieController.ERROR_JSP);
        checkErrors(result, EacValidateCookieController.ERROR_CODE_UNEXPECTED_PROBLEM_VALIDATING_COOKIE, "validValue");
        verifyMocks();
    }

    @Test
    public void testError3BlankSuccessUrl() throws CustomerNotFoundServiceLayerException {
        request.setCookies(new Cookie(EAC, "validValue"));
        replayMocks();
        ModelAndView result = sut.validateEacCookie(request, response, "     ", FAILURE_URL, null);
        checkResult(result, false, EacValidateCookieController.ERROR_JSP);
        checkErrors(result, EacValidateCookieController.ERROR_CODE_SUCCESS_URL_BLANK, EacValidateCookieController.PARAM_SUCCESS_URL);
        verifyMocks();
    }

    @Test
    public void testError4InvalidSuccessUrl() throws CustomerNotFoundServiceLayerException {
        request.setCookies(new Cookie(EAC, "validValue"));
        replayMocks();
        ModelAndView result = sut.validateEacCookie(request, response, "hbbtp://localhost", FAILURE_URL, null);
        checkResult(result, false, EacValidateCookieController.ERROR_JSP);
        checkErrors(result, EacValidateCookieController.ERROR_CODE_SUCCESS_URL_INVALID, EacValidateCookieController.PARAM_SUCCESS_URL);
        verifyMocks();
    }

    @Test
    public void testError5BlankFailureUrl() throws CustomerNotFoundServiceLayerException {
        request.setCookies(new Cookie(EAC, "validValue"));
        replayMocks();
        ModelAndView result = sut.validateEacCookie(request, response, SUCCESS_URL, "     ", null);
        checkResult(result, false, EacValidateCookieController.ERROR_JSP);
        checkErrors(result, EacValidateCookieController.ERROR_CODE_FAILURE_URL_BLANK, EacValidateCookieController.PARAM_FAILURE_URL);
        verifyMocks();
    }

    @Test
    public void testError6InvalidFailureUrl() throws CustomerNotFoundServiceLayerException {
        request.setCookies(new Cookie(EAC, "validValue"));
        replayMocks();
        ModelAndView result = sut.validateEacCookie(request, response, SUCCESS_URL, "hbbtp://localhost", null);
        checkResult(result, false, EacValidateCookieController.ERROR_JSP);
        checkErrors(result, EacValidateCookieController.ERROR_CODE_FAILURE_URL_INVALID, EacValidateCookieController.PARAM_FAILURE_URL);
        verifyMocks();
    }

    private void checkErrors(ModelAndView result, String expectedCode, Object... expectedParams) {
        Assert.assertEquals(expectedCode, result.getModelMap().get(EacValidateCookieController.ERROR_MSG_CODE));
        Object[] params = (Object[]) result.getModelMap().get(EacValidateCookieController.ERROR_MSG_PARAMS);
        if (expectedParams == null || expectedParams.length == 0) {
            Assert.assertTrue(params == null || params.length == 0);
        } else {
            for (int i = 0; i < expectedParams.length; i++) {
                Assert.assertEquals(expectedParams[i], params[i]);
            }
        }
    }

    private void checkResult(ModelAndView result, boolean redirect, String expectedView) {
        View view = result.getView();
        boolean isRedirect = view instanceof RedirectView;
        Assert.assertEquals(redirect, isRedirect);
        if (isRedirect) {
            Assert.assertTrue(result.getModelMap().isEmpty());
            RedirectView rv = (RedirectView) view;
            Assert.assertEquals(expectedView, rv.getUrl());
        } else {
            Assert.assertEquals(expectedView, result.getViewName());
        }
    }
}
