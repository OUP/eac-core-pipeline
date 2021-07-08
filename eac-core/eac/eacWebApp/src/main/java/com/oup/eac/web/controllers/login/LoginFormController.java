package com.oup.eac.web.controllers.login;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.util.Assert;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.oup.eac.common.utils.EACSettings;
import com.oup.eac.domain.Customer;
import com.oup.eac.dto.CustomerSessionDto;
import com.oup.eac.dto.LoginDto;
import com.oup.eac.service.CustomerService;
import com.oup.eac.service.DomainSkinResolverService;
import com.oup.eac.service.ProductService;
import com.oup.eac.service.ServiceLayerException;
import com.oup.eac.service.WhiteListUrlService;
import com.oup.eac.web.controllers.EACSimpleFormController;
import com.oup.eac.web.controllers.authentication.AuthenticationWorkFlow;
import com.oup.eac.web.controllers.context.RequestContext;
import com.oup.eac.web.controllers.helpers.CookieHelper;
import com.oup.eac.web.controllers.helpers.SessionHelper;
import com.oup.eac.web.controllers.registration.EACViews;

/**
 * @author harlandd Login controller.
 */
public class LoginFormController extends EACSimpleFormController {

    private static final String FLOW_STATE = "Login";
    private static final Logger LOGGER = Logger.getLogger(LoginFormController.class);
    private final CustomerService customerService;
    private final DomainSkinResolverService domainSkinResolverService;
    private final ProductService productService;
    private final WhiteListUrlService whiteListUrlService;

    /**
     * Create new instance.
     *
     * @param customerServiceP the customer service p
     * @param domainSkinResolverService the domain skin resolver service
     * @param productService the product service
     */
    public LoginFormController(final CustomerService customerServiceP, final DomainSkinResolverService domainSkinResolverService,
                        final ProductService productService, final WhiteListUrlService whiteListUrlService) {
        super();
        Assert.notNull(customerServiceP);
        Assert.notNull(domainSkinResolverService);
        Assert.notNull(productService);
        Assert.notNull(whiteListUrlService);
        this.customerService = customerServiceP;
        this.domainSkinResolverService = domainSkinResolverService;
        this.productService = productService;
        this.whiteListUrlService = whiteListUrlService;
    }

    @SuppressWarnings("deprecation")
    @Override
    protected final ModelAndView showForm(final HttpServletRequest request, final HttpServletResponse response, final BindException errors) throws Exception {
    
        // the forward URL in the session indicates where to redirect to 'after login'
        AuthenticationWorkFlow.cleanSession(request, false);
        
        RequestContext requestContext = new RequestContext();
        if (StringUtils.isNotBlank(request.getParameter(EACSettings.getProperty(EACSettings.ERIGHTS_URL_PARAMETER_NAME)))) {
	        if (AuthenticationWorkFlow.isUserRequestValid(request, requestContext, whiteListUrlService)) {
	        	AuthenticationWorkFlow.initRequestContext(request, domainSkinResolverService, requestContext);
	        	AuthenticationWorkFlow.loadRegisterableProductIfAvailable(request, productService, requestContext);
	        }   else{
	        	return new ModelAndView(EACViews.ERROR_PAGE);
	        }
        }
        //if the forwardURL was empty, it is now the profile page.
        String forwardURL = SessionHelper.getForwardUrl(request);
        Assert.isTrue(StringUtils.isNotBlank(forwardURL));
   
        if (!AuthenticationWorkFlow.isPrimaryDomainSessionAvailable(request, requestContext)) {
            return super.showForm(request, response, errors);
        }
        
        if (!AuthenticationWorkFlow.isPrimaryDomainSessionValid(request, customerService, requestContext)) {
            //Erights session is invalid so remove erights cookie.
            CookieHelper.invalidateErightsCookie(response);
            return super.showForm(request, response, errors);
        }
        
        return AuthenticationWorkFlow.getSuccessfulEndState(request);
    }
    
    /**
     * @param request
     *            the request
     * @param response
     *            the response
     * @param command
     *            the command
     * @param errors
     *            the errors
     * @return ModelAndView
     * @throws Exception
     *             any checked exception
     */
    @Override
    public final ModelAndView onSubmit(final HttpServletRequest request, final HttpServletResponse response, final Object command, final BindException errors)
            throws Exception {

        LoginDto loginFbo = (LoginDto) command;
        CustomerSessionDto customerSessionDto = null;
        try {
            customerSessionDto = customerService.getCustomerByUsernameAndPassword(loginFbo.getUsername(), loginFbo.getPassword(), true, true);
            
            AuthenticationWorkFlow.initErSession(request, response, customerSessionDto);

            Customer customer = customerSessionDto.getCustomer();
            Locale locale = customer.getLocale();
            SessionHelper.setLocale(request, response, locale);
            SessionHelper.setCustomer(request, customer);
            
            if (customer.isResetPassword()) {
                return new ModelAndView(new RedirectView(EACViews.CHANGE_PASSWORD_VIEW));
            }
            
            // redirect to original url, note adapter may redirect you back to access controller if the user doesn't have a license for the product.
            return AuthenticationWorkFlow.getSuccessfulEndState(request);

        } catch (ServiceLayerException e) {
            LOGGER.debug(e.getMessage());
            convertMessages(e, errors);
            return showForm(request, response, errors);
        }
    }
    
    @Override
    protected final Map<String, String> referenceData(final HttpServletRequest request) throws Exception {
        Map<String, String> refData = new HashMap<String, String>();
        refData.put("FLOW_STATE", FLOW_STATE);
        return refData;
    }
}
