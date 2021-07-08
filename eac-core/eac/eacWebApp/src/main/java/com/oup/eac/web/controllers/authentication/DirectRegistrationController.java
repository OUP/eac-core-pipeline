package com.oup.eac.web.controllers.authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.oup.eac.domain.RegisterableProduct;
import com.oup.eac.domain.RegisterableProduct.RegisterableType;
import com.oup.eac.service.CustomerService;
import com.oup.eac.service.WhiteListUrlService;
import com.oup.eac.web.controllers.context.DirectRequestContext;
import com.oup.eac.web.controllers.context.DirectRequestContextFactory;
import com.oup.eac.web.controllers.context.RequestContext;
import com.oup.eac.web.controllers.helpers.SessionHelper;

@Controller("directRegistrationController")
public class DirectRegistrationController {

    public static final Logger LOG = Logger.getLogger(DirectRegistrationController.class);

    private final CustomerService customerService;
	private final DirectRequestContextFactory directRequestContextFactory;
	private final WhiteListUrlService whiteListUrlService;

    /**
     * Instantiates a new direct registration controller.
     *
     * @param registrationDefinitionServiceP the registration definition service p
     * @param domainSkinResolverServiceP the domain skin resolver service p
     * @param customerServiceP the customer service p
     * @param productServiceP the product service p
     */
    @Autowired
	public DirectRegistrationController(final CustomerService customerServiceP, 
			final DirectRequestContextFactory directRequestContextFactory, 
			final WhiteListUrlService whiteListUrlService) {
		Assert.notNull(customerServiceP);
		Assert.notNull(directRequestContextFactory);
		Assert.notNull(whiteListUrlService);
        this.customerService = customerServiceP;
		this.directRequestContextFactory = directRequestContextFactory;
		this.whiteListUrlService = whiteListUrlService;
    }

    /**
     * The direct registration controller processing method.
     *
     * @param request the request
     * @param response the response
     * @return the model and view
     */
    @RequestMapping(method = RequestMethod.GET, value = "/register.htm")
    public final ModelAndView process(final HttpServletRequest request, final HttpServletResponse response) {

        AuthenticationWorkFlow.cleanSession(request);

        RequestContext requestContext = new RequestContext();

		DirectRequestContext dc = directRequestContextFactory.getDirectRequestContext(request, requestContext);

        if (dc == null) {
            return AuthenticationWorkFlow.getErrorState(request);
        }

        // gets the RegisterableProduct ( if available ) and sets the domainSkin
        // in the requestContext
        dc.loadRegisterableProductIfAvailable(request, requestContext);

        RegisterableProduct registerableProduct = SessionHelper.getRegisterableProduct(request);
        
        if(registerableProduct == null) {
        	return AuthenticationWorkFlow.getErrorState(request);
        }
        
        if(registerableProduct.getRegisterableType() != RegisterableType.SELF_REGISTERABLE) {
        	LOG.debug("Registration is not allowed via this registration method as the the registerable type is not self registerable.");
        	return AuthenticationWorkFlow.getErrorState(request);
        }
        
        if (!dc.isProductRegistrationDefined(request, requestContext)) {
            return AuthenticationWorkFlow.getErrorState(request);
        }

        // at this point we know there is a valid product we are trying to
        // register for
        boolean isLoggedIn = false;

        boolean isSessionAvailable = AuthenticationWorkFlow.isPrimaryDomainSessionAvailable(request, requestContext);
        if (isSessionAvailable) {
            isLoggedIn = AuthenticationWorkFlow.isPrimaryDomainSessionValid(request, customerService, requestContext);
        }

        final ModelAndView result;

        boolean canContinueToProtected = isLoggedIn;
        if (canContinueToProtected) {

            if (dc.isLandingPageMissing(requestContext)) {
                result = AuthenticationWorkFlow.getErrorState(request);
            } else {
                result = AuthenticationWorkFlow.getSuccessfulEndState(request);
            }
            
        } else {
            Assert.isNull(SessionHelper.getCustomer(request));

            // we must have a RegisterableProduct when we go to display the
            // Account Creation Form
            Assert.notNull(SessionHelper.getRegisterableProduct(request));

            result = AuthenticationWorkFlow.getAccountCreationState(request);
        }

        return result;
    }

}
