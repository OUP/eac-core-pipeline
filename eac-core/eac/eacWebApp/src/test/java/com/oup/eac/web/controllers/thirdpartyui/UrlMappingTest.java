package com.oup.eac.web.controllers.thirdpartyui;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.mail.Session;
import javax.naming.NamingException;
import javax.servlet.ServletException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.UrlFilenameViewController;

import com.oup.eac.web.controllers.EACVersionController;
import com.oup.eac.web.controllers.authentication.AccessController;
import com.oup.eac.web.controllers.authentication.ActivationCodeFormController;
import com.oup.eac.web.controllers.authentication.ChangePasswordFormController;
import com.oup.eac.web.controllers.authentication.CookieErrorController;
import com.oup.eac.web.controllers.authentication.CrossSiteCookieController;
import com.oup.eac.web.controllers.authentication.DirectRegistrationController;
import com.oup.eac.web.controllers.authentication.LogoutController;
import com.oup.eac.web.controllers.authentication.PasswordResetFormController;
import com.oup.eac.web.controllers.authentication.RegistrationAllowController;
import com.oup.eac.web.controllers.authentication.ReregisterController;
import com.oup.eac.web.controllers.authentication.ValidatorRegistrationAllowController;
import com.oup.eac.web.controllers.authentication.ValidatorRegistrationDenyController;
import com.oup.eac.web.controllers.login.LoginFormController;
import com.oup.eac.web.controllers.profile.BasicProfileChangePasswordController;
import com.oup.eac.web.controllers.profile.BasicProfileController;
import com.oup.eac.web.controllers.profile.BasicProfileRedeemActivationCodeController;
import com.oup.eac.web.controllers.registration.AccountRegistrationFormController;
import com.oup.eac.web.controllers.registration.ProductRegistrationFormController;
import com.oup.eac.web.controllers.registration.ProductRegistrationUpdateController;
import com.oup.eac.web.controllers.registration.ResendConfirmationEmailController;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        loader = MockWebApplicationContextLoader.class,
        locations = {
              "classpath*:/eac/eac*-beans.xml", 
              "classpath*:/eac/test.eac*-beans.xml",
              "classpath*:/eac/web.eac-servlet.xml" }
        )
@MockWebApplication(name = "ignored")
public class UrlMappingTest {

    @Autowired
    private ApplicationContext appContext;

    @Autowired
    private DispatcherServlet servlet;

    private Map<String, HandlerMapping> mappings;

    public UrlMappingTest() {
        try {
            SimpleNamingContextBuilder builder = SimpleNamingContextBuilder.emptyActivatedContextBuilder();
            builder.bind("java:/Mail", Session.getInstance(new Properties()));
        } catch (NamingException ne) {
            throw new RuntimeException(ne);
        }
    }

    @Test
    public void testServlet() {
        Assert.assertNotNull(servlet);
    }

    @Before
    public void setMappings() {
        this.mappings = appContext.getBeansOfType(HandlerMapping.class);
    }

    /**
     * Tests url handler mappings.
     * 
     * @see EAC_INTERCEPTORS.xlsx
     * 
     */
    @Test
    public void testControllerMappings() throws Exception {
    	checkHandlerExists("/access.htm", AccessController.class);//1
    	checkHandlerExists("/accessibility.htm", UrlFilenameViewController.class);//2
    	checkHandlerExists("/accountRegistration.htm", AccountRegistrationFormController.class);//3
    	checkHandlerExists("/activationCode.htm", ActivationCodeFormController.class);//4
    	checkHandlerExists("POST", "/basicLogin.htm", BasicLoginController.class);//5
    	checkHandlerExists("/changePassword.htm", ChangePasswordFormController.class);//6    	
    	checkHandlerExists("/cookieError.htm", CookieErrorController.class);//7
        checkHandlerExists("POST", "/cookieFromSession.htm", 	EacCookieController.class);//8
        checkHandlerExists("GET",  "/cookieValidate.htm",  		EacValidateCookieController.class);//9    	
    	checkHandlerExists("/crossSiteCookie.htm", CrossSiteCookieController.class);//10
    	checkHandlerExists("/internalActivationCode.htm", ActivationCodeFormController.class);//11
    	//logged out?
    	checkHandlerExists("/login.htm", LoginFormController.class);//13
    	checkHandlerExists("GET",  "/loginWidget.js" , 	CachingLoginWidgetController.class);//14
    	checkHandlerExists("/logout.htm", LogoutController.class);//15
        checkHandlerExists("/modifyProductRegistration.htm", ProductRegistrationUpdateController.class);//16
        checkHandlerExists("/passwordReset.htm", PasswordResetFormController.class);//17
        checkHandlerExists("/privacyAndLegal.htm", UrlFilenameViewController.class);//18
        checkHandlerExists("/productRegistration.htm", ProductRegistrationFormController.class);//19
        checkHandlerExists("POST", "/profile.htm", 						BasicProfileController.class);//20
        checkHandlerExists("GET",  "/profileChangePassword.htm",        BasicProfileChangePasswordController.class);//21
        checkHandlerExists("GET",  "/profileRedeemActivationCode.htm",  BasicProfileRedeemActivationCodeController.class);//22        
        checkHandlerExists("GET",  "/register.htm", 	DirectRegistrationController.class);//23
        checkHandlerExists("/registrationAllow.htm", RegistrationAllowController.class);//24
        checkHandlerExists("/reregister.htm", ReregisterController.class);//25
        checkHandlerExists("GET",  "/resendConfirmationEmail.htm", ResendConfirmationEmailController.class);//26
        /*checkHandlerExists("/validatorRegistrationAllow.htm", ValidatorRegistrationAllowController.class);//27
        checkHandlerExists("/validatorRegistrationDeny.htm", ValidatorRegistrationDenyController.class);//28
*/        checkHandlerExists("/version.htm", EACVersionController.class);//29
        //no mapping for LoginWidgetController
    }
    
    /**
     * checks that in setting up a mapping for /loginWidget.js we haven't remapping the normal js files too
     */
    @Test
    public void testJavascript() throws ServletException, IOException{
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod("GET");
        request.setRequestURI("/js/jquery-1.5.1.min.js");
        MockHttpServletResponse response = new MockHttpServletResponse();
        servlet.service(request, response);
        Assert.assertEquals(404, response.getStatus());
    }
    
    private void checkHandlerExists(final String requestUri, final Class<?> clazz) throws Exception {
        checkHandlerExists(null, requestUri, clazz);
    }

    private void checkHandlerExists(final String method, String requestUri, final Class<?> clazz) throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest(method, requestUri);

       Iterator<HandlerMapping> iter = mappings.values().iterator();

        boolean found = false;

        while (iter.hasNext() && !found) {
            HandlerMapping mapping = iter.next();

            HandlerExecutionChain handler = mapping.getHandler(request);
            if (handler != null) {
                Assert.assertTrue(clazz.isAssignableFrom(handler.getHandler().getClass()));
                found = true;
            }
        }
        Assert.assertTrue(found);
    }

}

