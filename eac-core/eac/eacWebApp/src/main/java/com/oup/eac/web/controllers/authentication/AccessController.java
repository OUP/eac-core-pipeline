package com.oup.eac.web.controllers.authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.util.Assert;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.oup.eac.domain.ActivationCode;
import com.oup.eac.domain.ProductRegistrationDefinition;
import com.oup.eac.domain.Registration;
import com.oup.eac.domain.RegistrationActivation.ActivationStrategy;
import com.oup.eac.domain.RegistrationDefinition.RegistrationDefinitionType;
import com.oup.eac.dto.EnforceableProductDto;
import com.oup.eac.dto.RegistrationActivationDto;
import com.oup.eac.service.CustomerService;
import com.oup.eac.service.DomainSkinResolverService;
import com.oup.eac.service.LicenceService;
import com.oup.eac.service.ProductService;
import com.oup.eac.service.RegistrationDefinitionService;
import com.oup.eac.service.RegistrationService;
import com.oup.eac.service.WhiteListUrlService;
import com.oup.eac.web.controllers.authentication.AuthenticationWorkFlow.RegistrationNotAllowedReason;
import com.oup.eac.web.controllers.context.RequestContext;
import com.oup.eac.web.controllers.helpers.RegistrationNotAllowedMessageCodeSource;
import com.oup.eac.web.controllers.helpers.SessionHelper;

/**
 * @author harlandd Access controller for EAC.
 */
public class AccessController implements Controller {

    private final RegistrationDefinitionService registrationDefinitionService;
    private final DomainSkinResolverService domainSkinResolverService;
    private final CustomerService customerService;
    private final RegistrationService registrationService;
    private final ProductService productService;
    private final LicenceService licenceService;
    private final RegistrationNotAllowedMessageCodeSource registrationNoAllowedMessageCodeSource;
    private final WhiteListUrlService whiteListUrlService;
    
    /**
     * Instantiates a new access controller.
     *
     * @param registrationDefinitionService the registration definition service
     * @param domainSkinResolverService the domain skin resolver service
     * @param customerService the customer service
     * @param registrationService the registration service
     * @param productService the product service
     * @param licenceService the licence service
     * @param registrationNoAllowedMessageCodeSource the registration no allowed message code source
     */
    public AccessController(final RegistrationDefinitionService registrationDefinitionService,
                            final DomainSkinResolverService domainSkinResolverService, 
                            final CustomerService customerService,
                            final RegistrationService registrationService,
                            final ProductService productService,
                            final LicenceService licenceService,
                            final RegistrationNotAllowedMessageCodeSource registrationNoAllowedMessageCodeSource, 
                            final WhiteListUrlService whiteListUrlService) {
        super();
        Assert.notNull(registrationDefinitionService);
        Assert.notNull(domainSkinResolverService);
        Assert.notNull(customerService);
        Assert.notNull(registrationService);
        Assert.notNull(productService);
        Assert.notNull(licenceService);
        Assert.notNull(registrationNoAllowedMessageCodeSource);
        Assert.notNull(whiteListUrlService);
        this.registrationDefinitionService = registrationDefinitionService;
        this.domainSkinResolverService = domainSkinResolverService;
        this.customerService = customerService;
        this.registrationService = registrationService;
        this.productService = productService;
        this.licenceService = licenceService;
        this.registrationNoAllowedMessageCodeSource = registrationNoAllowedMessageCodeSource;
        this.whiteListUrlService = whiteListUrlService;
    }

    /**
     * @param request
     *            the HttpServletRequest
     * @param response
     *            the HttpServletResponse
     * @return the ModelAndView
     * @throws Exception
     *             any checked exception
     */
    @Override
    public final ModelAndView handleRequest(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

        AuthenticationWorkFlow.cleanSession(request);
        
        RequestContext requestContext = new RequestContext();
        
        if(!AuthenticationWorkFlow.isUserRequestValid(request, requestContext, whiteListUrlService)) {
        	return AuthenticationWorkFlow.getErrorState(request);
        }
        
        AuthenticationWorkFlow.initRequestContext(request, domainSkinResolverService, requestContext);
        AuthenticationWorkFlow.loadRegisterableProductIfAvailable(request, productService, requestContext);
   
        if(!AuthenticationWorkFlow.isPrimaryDomainSessionAvailable(request, requestContext)) {
        	return AuthenticationWorkFlow.getLoginState(requestContext, request, response);
        }
        
        if(!AuthenticationWorkFlow.isPrimaryDomainSessionValid(request, customerService, requestContext)) {
        	return AuthenticationWorkFlow.getLoginState(requestContext, request, response);
        }
        
        if(!AuthenticationWorkFlow.isDenyReasonAvailable(request)) {
            return AuthenticationWorkFlow.getSuccessfulEndState(request);
        }        
        
        //REGISTRATION NOT ALLOWED : PRODUCT
        if(!AuthenticationWorkFlow.isProductRegistrationDefined(request, requestContext, registrationDefinitionService)) {
            return getRegistrationNotAllowedState(requestContext, RegistrationNotAllowedReason.PRODUCT_REG_DEF);
        }
        
        //REGISTRATION NOT ALLOWED : CUSTOMER
        if(!AuthenticationWorkFlow.isCustomerAuthorisedToRegisterForProduct(requestContext)) {
            return getRegistrationNotAllowedState(requestContext, RegistrationNotAllowedReason.CUSTOMER);
        }
        
        if(!AuthenticationWorkFlow.isCustomersEmailValidated(requestContext)) {
            if(AuthenticationWorkFlow.isCustomerEmailValidationRequired(requestContext, registrationDefinitionService)) {
                if(!AuthenticationWorkFlow.isCustomerValidationEmailSent(requestContext)) {
                    AuthenticationWorkFlow.sendCustomerValidationEmail(requestContext, customerService, request);
                }
                return AuthenticationWorkFlow.getEmailNotValidatedState(requestContext);
            }
        } 
        if(requestContext.getProductRegistrationDefinition().getRegistrationDefinitionType() == RegistrationDefinitionType.ACTIVATION_CODE_REGISTRATION) {
        	if(!AuthenticationWorkFlow.isCustomerRegistered(registrationService, request, requestContext) || 
        			AuthenticationWorkFlow.isCustomerReregistering(request, registrationService)) {
        		if(!AuthenticationWorkFlow.isActivationCodeAvailable(request)) {
        			return AuthenticationWorkFlow.getActivationCodeRegistrationState();
        		}
        		ActivationCode activationCode = SessionHelper.getActivationCode(request);
        		Registration<?> registration = AuthenticationWorkFlow.saveActivationCodeRegistration(request, registrationService, activationCode);
        		requestContext.setProductRegistration((Registration<ProductRegistrationDefinition>) registration);
        		SessionHelper.removeActivationCode(request);
        		SessionHelper.removeReregister(request);
        		return AuthenticationWorkFlow.getSuccessfulEndState(request);
        	}
        	if(!requestContext.getProductRegistration().isCompleted()){
        	/*if(!AuthenticationWorkFlow.isProductRegistrationComplete(registrationService, requestContext, request)) {*/
        		if(!AuthenticationWorkFlow.isProductRegistrationComplete(registrationService, requestContext, request)){
        			return AuthenticationWorkFlow.getProductRegistrationState();
        		}
        		//SessionHelper.setAwaitingValidation(request.getSession(), true);
        	}else{
        		if(SessionHelper.isCompleted(request.getSession()) !=null && SessionHelper.isCompleted(request.getSession())){
	    			SessionHelper.setAwaitingValidation(request.getSession(), false);
	    			SessionHelper.setCompleted(request.getSession(), false);
        		}else
        			SessionHelper.setAwaitingValidation(request.getSession(), true);
        			
        	}
        	
        }
        
        if(requestContext.getProductRegistrationDefinition().getRegistrationDefinitionType() == RegistrationDefinitionType.PRODUCT_REGISTRATION){
	        if(!AuthenticationWorkFlow.isCustomerRegistered(registrationService, request, requestContext)) {
	        	if(SessionHelper.isCompleted(request.getSession()) ==null){
	        		if(!AuthenticationWorkFlow.isProductRegistrationComplete(registrationService, requestContext, request) && !requestContext.getProductRegistration().isCompleted()) {
	        			return AuthenticationWorkFlow.getProductRegistrationState();
	        		}
	        		SessionHelper.setAwaitingValidation(request.getSession(), false);
	        	}else
	        		SessionHelper.setAwaitingValidation(request.getSession(), false);
	        }
	        else if (AuthenticationWorkFlow.isCustomerReregistering(request, registrationService)){
	        	SessionHelper.removeReregister(request);
	        	if(!AuthenticationWorkFlow.isProductRegistrationComplete(registrationService, requestContext, request)){
	        		return AuthenticationWorkFlow.getProductRegistrationState();
	        	}
	        	SessionHelper.setAwaitingValidation(request.getSession(), false);
	        }
	        else{
	        	SessionHelper.setAwaitingValidation(request.getSession(), true);
	        }
        }

    	if(!AuthenticationWorkFlow.isAwaitingValidation(requestContext, request)) {
			RegistrationActivationDto registrationActivationDto = new RegistrationActivationDto(requestContext.getCustomer(),
    													SessionHelper.getLocale(request),
    													SessionHelper.getForwardUrl(request.getSession(), false),
    													requestContext.getProductRegistration(),
    													requestContext.getProductRegistrationDefinition());
			//product -de-duplication
			EnforceableProductDto enforceableProduct = productService.getEnforceableProductByErightsId(requestContext.getProductRegistrationDefinition().getProduct().getId());
			
			if(requestContext.getProductRegistrationDefinition().getRegistrationDefinitionType() == RegistrationDefinitionType.PRODUCT_REGISTRATION){
				registrationService.saveRegistrationActivation(registrationActivationDto, enforceableProduct);
			}else{
				registrationService.saveActivationRegistrationActivation(registrationActivationDto, enforceableProduct);
			}
			requestContext.setProductRegistration((Registration<ProductRegistrationDefinition>) registrationActivationDto.getRegistration());
			SessionHelper.setRegistrationId(request, registrationActivationDto.getRegistration().getId());
			SessionHelper.setAwaitingValidation(request.getSession(), true);
			SessionHelper.removeIsCompleted(request);
			requestContext.getProductRegistration().setCompleted(true);
			return AuthenticationWorkFlow.getSuccessfulEndState(request);
    	}
    	
    	if(!AuthenticationWorkFlow.isRegistrationEnabled(requestContext)) {
    		if(AuthenticationWorkFlow.isDenied(requestContext)) {    			
    			return AuthenticationWorkFlow.getLicenceDeniedState(request, requestContext);
    		} 
    		if(requestContext.getProductRegistrationDefinition().getRegistrationActivation().
    				getActivationStrategy(SessionHelper.getLocale(request)) != ActivationStrategy.INSTANT && requestContext.getProductRegistration().isAwaitingValidation()){
    			return AuthenticationWorkFlow.getLicenceNotActivatedState(request, requestContext);
    		}
    	}
    	
    	/*if(!AuthenticationWorkFlow.isRegistrationActivated(requestContext)) {
    		if(AuthenticationWorkFlow.isDenied(requestContext)) {    			
    			return AuthenticationWorkFlow.getLicenceDeniedState(request, requestContext);
    		} 
    		if(AuthenticationWorkFlow.getLicenceNotActivatedState(request, requestContext)!=null){
    		 	return AuthenticationWorkFlow.getLicenceNotActivatedState(request, requestContext);
    		}
    	}*/
    	
    	if(!AuthenticationWorkFlow.isActiveLicenceAvailable(requestContext, licenceService)) {
    		return AuthenticationWorkFlow.getNoActiveLicenceState(requestContext, licenceService);
    	}
    	
    	if(AuthenticationWorkFlow.isConcurrencyExceeded(request)) {
    		return AuthenticationWorkFlow.getConcurrencyExceededState(requestContext);
    	}
    	
    	return AuthenticationWorkFlow.getErrorState(request);
    }

    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception  {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));  
    }
    
    /**
     * Gets the registration not allowed state.
     *
     * @param requestContext the request context
     * @param reason the reason
     * @return the registration not allowed state
     */
    private ModelAndView getRegistrationNotAllowedState(RequestContext requestContext, RegistrationNotAllowedReason reason) {
        return AuthenticationWorkFlow.getRegistrationNotAllowedState(requestContext, reason, this.registrationNoAllowedMessageCodeSource);
    }
}
