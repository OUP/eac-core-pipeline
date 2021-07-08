package com.oup.eac.web.controllers.authentication;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.util.Assert;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.oup.eac.domain.ActivationCode;
import com.oup.eac.domain.ProductRegistrationDefinition;
import com.oup.eac.domain.RegisterableProduct;
import com.oup.eac.domain.Registration;
import com.oup.eac.integration.facade.exceptions.ErightsException;
import com.oup.eac.service.ActivationCodeService;
import com.oup.eac.service.CustomerService;
import com.oup.eac.service.DomainSkinResolverService;
import com.oup.eac.service.ProductService;
import com.oup.eac.service.RegistrationService;
import com.oup.eac.service.ServiceLayerException;
import com.oup.eac.service.WhiteListUrlService;
import com.oup.eac.web.controllers.EACSimpleFormController;
import com.oup.eac.web.controllers.context.RequestContext;
import com.oup.eac.web.controllers.helpers.SessionHelper;
import com.oup.eac.web.controllers.registration.EACViews;

/**
 * @author harlandd Customer change password controller.
 */
public class ActivationCodeFormController extends EACSimpleFormController {

    private static final Logger LOGGER = Logger.getLogger(ActivationCodeFormController.class);
    private final RegistrationService registrationService;
    private final DomainSkinResolverService domainSkinResolverService;
    private final ProductService productService;
    private final CustomerService customerService;
    private final ActivationCodeService activationCodeService;
    private final WhiteListUrlService whiteListUrlService;
    
    /**
     * @param customerService
     *            the user service
     * @param domainSkinResolverService
     *            the domain skin resolver
     */
    public ActivationCodeFormController(final RegistrationService registrationService, 
    									final DomainSkinResolverService domainSkinResolverService, 
    									final ProductService productService,
    									final CustomerService customerService,
    									final ActivationCodeService activationCodeService, 
    									final WhiteListUrlService whiteListUrlService) {
        super();
        Assert.notNull(registrationService);
        Assert.notNull(domainSkinResolverService);
        Assert.notNull(productService);
        Assert.notNull(customerService);
        Assert.notNull(activationCodeService);
        Assert.notNull(whiteListUrlService);
        this.registrationService = registrationService;
        this.domainSkinResolverService = domainSkinResolverService;
        this.productService = productService;
        this.customerService = customerService;
        this.activationCodeService = activationCodeService;
        this.whiteListUrlService = whiteListUrlService;
    }
    
    

    @SuppressWarnings("deprecation")
	@Override
    protected ModelAndView showForm(HttpServletRequest request, HttpServletResponse response, BindException errors)
            throws Exception {

        if (isDirect(request)) {
            //If direct activation code request, setup session as required
            AuthenticationWorkFlow.cleanSession(request);
            RequestContext requestContext = new RequestContext();
            
            AuthenticationWorkFlow.isUserRequestValid(request, requestContext, whiteListUrlService);
            
            AuthenticationWorkFlow.initRequestContext(request, domainSkinResolverService, requestContext);
            AuthenticationWorkFlow.loadRegisterableProductIfAvailable(request, productService, requestContext);
        }
        
        return super.showForm(request, response, errors);
    }

	@Override
    protected Map<String,String> referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {

        Map<String, String> map = new HashMap<String, String>();
        String productReturn = null;
        if (isDirect(request)) {
            RegisterableProduct registerableProduct = SessionHelper.getRegisterableProduct(request);
            
            if (registerableProduct == null) {
                //if no product resolved, home url should be value of url parameter
            	productReturn = SessionHelper.getForwardUrl(request);
            } else {
                //if product resolved, use landing page (may be null)
            	productReturn = registerableProduct.getLandingPage();
            }
        }
        map.put("productReturn", productReturn);
        return map;
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
     * @return the ModelAndView
     * @throws Exception
     *             any checked exception
     */
	protected final ModelAndView onSubmit(final HttpServletRequest request, final HttpServletResponse response, final Object command, final BindException errors)
            throws Exception {
	    ActivationCode activationCode = (ActivationCode) command;
	    
    	if (isDirect(request)) {
            return onSubmitWithDirectActivationCode(request, response, command, errors);
        }

    	//Only here if accessed via accessController 
    	//(Request uri contains EACViews.INTERNAL_ACTIVATION_CODE_VIEW)
    	try {
    		Registration<ProductRegistrationDefinition> registration = customerService.getRegistrationByRegistrationDefinitionAndCustomer(
																		SessionHelper.getProductRegistrationDefinition(request), 
																		SessionHelper.getCustomer(request));
    		//If registration is empty, create a new one. If it is not empty but we are re-registering also create a new one
    		if(registration == null || (registration != null && AuthenticationWorkFlow.isCustomerReregistering(request, registrationService))
    				|| (registration!=null && registration.isExpired())) {
    			AuthenticationWorkFlow.saveActivationCodeRegistration(request, registrationService, activationCode);
    			SessionHelper.removeReregister(request);
    		} else {
    			return AuthenticationWorkFlow.getErrorState(request);
    		}
        } catch (ServiceLayerException e) {
            LOGGER.debug(e.getMessage());
            convertMessages(e, errors);
            return showForm(request, response, errors);
        }
        return AuthenticationWorkFlow.getSuccessfulEndState(request);
    }
	
	@Override
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception  {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor("- ", true));        
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
     * @return the ModelAndView
     * @throws Exception
     *             any checked exception
     */
	protected final ModelAndView onSubmitWithDirectActivationCode(final HttpServletRequest request, final HttpServletResponse response, final Object command, final BindException errors)
            throws Exception {
		ActivationCode activationCode = (ActivationCode) command;
        ActivationCode ac = new ActivationCode();
        
        try {
        	ac = activationCodeService.getActivationCodeWithDetails(activationCode.getCode());
		} catch (ErightsException
				| ServiceLayerException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			//LOG.debug(e1.getMessage());
			 errors.reject("error.regularexpression", new Object[] { "label.redeemcode" }, "Activation Code is invalid.");
	            return showForm(request, response, errors);
		}
        if (ac == null) {
            //LOG.debug("Activation Code is invalid.");
            errors.reject("error.regularexpression", new Object[] { "label.redeemcode" }, "Activation Code is invalid.");
            return showForm(request, response, errors);
        }
        
        SessionHelper.setActivationCode(request, activationCode);
        //SessionHelper.setRegisterableProduct(request, ac.getActivationCodeBatch().getActivationCodeRegistrationDefinition().getProduct());
        
        RegisterableProduct registerableProduct = SessionHelper.getRegisterableProduct(request);
        String landingPage = ac.getActivationCodeBatch().getActivationCodeRegistrationDefinition().getProduct().getLandingPage();     
        //if registerableProduct available, return to forwardUrl
        if (registerableProduct != null) {
            LOGGER.debug("Registerable product available for direct activation code. Forwarding to success state.");
            return AuthenticationWorkFlow.getSuccessfulEndState(request);
        } else if (!StringUtils.isBlank(landingPage)) {
            LOGGER.debug("Forwarding to protected landing page defined in product: " + landingPage);
            return new ModelAndView(new RedirectView(landingPage, false));
        } else {
            LOGGER.debug("Configuration Error: No product resolved from url and no landing page available for activation code resovled product with id: " + ac.getActivationCodeBatch().getActivationCodeRegistrationDefinition().getProduct().getId());
            errors.rejectValue("code", "error.problem.activating.token", new Object[0], "There was a problem with your activation code.");
            return showForm(request, response, errors);
        }
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
     * @return the ModelAndView
     * @throws Exception
     *             any checked exception
     */
    @SuppressWarnings("deprecation")
	protected final ModelAndView processFormSubmission(final HttpServletRequest request, final HttpServletResponse response, final Object command,
            final BindException errors) throws Exception {
        if (errors.hasErrors()) {
            return super.processFormSubmission(request, response, command, errors);
        }
        RegisterableProduct registerableProduct = SessionHelper.getRegisterableProduct(request);
        if(registerableProduct != null) {
        	ActivationCode ac = new ActivationCode();
        	//Check validation code given is for this product
        	ActivationCode activationCode = (ActivationCode) command;
        	try{
        		ac = activationCodeService.getActivationCodeWithDetails(activationCode.getCode());

        		if(ac.getActualUsage()== ac.getAllowedUsage()){
        			errors.rejectValue("code", "error.problem.activating.token", new Object[0], "There was a problem with your activation code.");
            		LOGGER.debug("Activation code rejected as the associated product is not applicable to this registration definition.");
            		return super.processFormSubmission(request, response, command, errors);
        		}
        	}catch(ServiceLayerException sla){
        		errors.rejectValue("code", "error.problem.activating.token", new Object[0], "There was a problem with your activation code.");
        		LOGGER.debug("Activation code rejected as the associated product is not applicable to this registration definition.");
        		return super.processFormSubmission(request, response, command, errors);
        	}
        	//ActivationCode ac = activationCodeService.getActivationCodeAndDefinitionByCode(activationCode.getCode());
        	if(!ac.getActivationCodeBatch().getActivationCodeRegistrationDefinition().getProduct().getId().equals(registerableProduct.getId())) {
        		errors.rejectValue("code", "error.problem.activating.token", new Object[0], "There was a problem with your activation code.");
        		LOGGER.debug("Activation code rejected as the associated product is not applicable to this registration definition.");
        		return super.processFormSubmission(request, response, command, errors);
        	}
        }
        return super.processFormSubmission(request, response, command, errors);
    }

    private boolean isDirect(final HttpServletRequest request) {
        return request.getRequestURI().contains(EACViews.DIRECT_ACTIVATION_CODE_VIEW);
    }
}
