package com.oup.eac.web.controllers.authentication;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.oup.eac.common.utils.EACSettings;
import com.oup.eac.common.utils.url.InvalidURLException;
import com.oup.eac.common.utils.url.URLUtils;
import com.oup.eac.domain.AccountRegistrationDefinition;
import com.oup.eac.domain.ActivationCode;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.Customer.CustomerType;
import com.oup.eac.domain.ErightsDenyReason;
import com.oup.eac.domain.ErightsLicenceDecision;
import com.oup.eac.domain.Product;
import com.oup.eac.domain.Product.ProductState;
import com.oup.eac.domain.ProductRegistration;
import com.oup.eac.domain.ProductRegistrationDefinition;
import com.oup.eac.domain.RegisterableProduct;
import com.oup.eac.domain.RegisterableProduct.RegisterableType;
import com.oup.eac.domain.Registration;
import com.oup.eac.domain.RegistrationActivation.ActivationStrategy;
import com.oup.eac.domain.UrlSkin;
import com.oup.eac.domain.User.EmailVerificationState;
import com.oup.eac.dto.CustomerRegistrationsDto;
import com.oup.eac.dto.CustomerSessionDto;
import com.oup.eac.dto.LicenceDto;
import com.oup.eac.integration.facade.exceptions.ErightsException;
import com.oup.eac.integration.facade.exceptions.UserNotFoundException;
import com.oup.eac.service.CustomerService;
import com.oup.eac.service.DomainSkinResolverService;
import com.oup.eac.service.LicenceService;
import com.oup.eac.service.ProductService;
import com.oup.eac.service.RegistrationDefinitionService;
import com.oup.eac.service.RegistrationService;
import com.oup.eac.service.ServiceLayerException;
import com.oup.eac.service.WhiteListUrlService;
import com.oup.eac.service.exceptions.CustomerNotFoundServiceLayerException;
import com.oup.eac.service.exceptions.NoRegisterableProductFoundException;
import com.oup.eac.web.controllers.context.RequestContext;
import com.oup.eac.web.controllers.helpers.CookieHelper;
import com.oup.eac.web.controllers.helpers.RegistrationNotAllowedMessageCodeSource;
import com.oup.eac.web.controllers.helpers.SessionHelper;
import com.oup.eac.web.controllers.registration.EACViews;

public class AuthenticationWorkFlow {
	
    private static final String LICENSE_DENY_TYPES = "licenseDenyTypes";
    private static final String DENY_REASON = "denyReason";

    public enum RegistrationNotAllowedReason {
        PRODUCT_REG_DEF,
        CUSTOMER
    };
    
    private static final String MESSAGE_CODE = "messageCode";
	private static final String SLA = "sla";
    private static final String ALL_EXPIRED = "allExpired";
    private static final String LICENCE_DTOS = "licenceDtos";
    private static final String EMAIL = "email";
    private static final String PRODUCT = "product";    
    private static final String REGISTRATION_NOT_ALLOWED_REASON = "registrationNotAllowedReason";
    private static final Logger LOGGER = Logger.getLogger(AuthenticationWorkFlow.class);
	
    public static boolean isCustomerValidationEmailSent(final RequestContext authenticationContext) {
        if(authenticationContext.getCustomer().getEmailVerificationState() == EmailVerificationState.EMAIL_SENT) {
            LOGGER.debug("Customer validation email sent.");
            return true;
        }
        LOGGER.debug("Customer validation email has not been sent.");
        return false;
    }   	
	
    public static boolean isCustomersEmailValidated(final RequestContext authenticationContext) {
        if(authenticationContext.getCustomer().getEmailVerificationState() == EmailVerificationState.VERIFIED) {
            LOGGER.debug("Customer Email is validated.");
            return true;
        }
        LOGGER.debug("Customer Email is not validated.");
        return false;
    }	
	
	public static boolean isCustomerEmailValidationRequired(final RequestContext authenticationContext, final RegistrationDefinitionService registrationDefinitionService) throws ServiceLayerException {
	    AccountRegistrationDefinition accountRegistrationDefinition = registrationDefinitionService.getAccountRegistrationDefinitionByProduct(authenticationContext.getRegisterableProduct());
	    authenticationContext.setAccountRegistrationDefinition(accountRegistrationDefinition);
        if(authenticationContext.getAccountRegistrationDefinition().isValidationRequired()) {
            LOGGER.debug("Customer Email Validation is required.");
            return true;
        }
        LOGGER.debug("Customer Email Validation is not required.");
        return false;
	}
	
    public static ModelAndView getEmailNotValidatedState(final RequestContext authenticationContext) throws UnsupportedEncodingException {
        Map<String, String> map = new HashMap<String, String>();
        map.put(EMAIL, authenticationContext.getRegisterableProduct().getEmail());
        return new ModelAndView(EACViews.VALIDATE_EMAIL, map);
    }
	
	public static ModelAndView getSuccessfulEndState(final HttpServletRequest request) {
	    String forwardUrl = SessionHelper.getForwardUrl(request.getSession());
	    LOGGER.debug("Successful end state entered with url: " + forwardUrl);
		return new ModelAndView(new RedirectView(forwardUrl, false));
	}
	
	public static ModelAndView getErrorState(final HttpServletRequest request) {
		
		ErightsDenyReason erightsDenyReason = getErightsDenyReason(request);
		if(erightsDenyReason == null) {
			return new ModelAndView(EACViews.ERROR_PAGE);
		}
		return new ModelAndView(EACViews.ERROR_PAGE, DENY_REASON, erightsDenyReason.toString());
	}

	public static ModelAndView getLoginState(final RequestContext authenticationContext, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
		Map<String, String> params = new HashMap<String, String>();
		String eacParamName = EACSettings.getProperty(EACSettings.ERIGHTS_URL_PARAMETER_NAME);
		params.put(eacParamName, authenticationContext.getUserRequestedUrl());
		String redirectUrl = URLUtils.appendParams(EACViews.LOGIN_VIEW, params);
		return new ModelAndView(new RedirectView(redirectUrl, true));
	}
	
	/**
	 * Gets the registration not allowed state.
	 *
	 * @param requestContext the request context
	 * @param reason the reason
	 * @param registrationNoAllowedMessageCodeSource the registration no allowed message code source
	 * @return the registration not allowed state
	 */
	public static ModelAndView getRegistrationNotAllowedState(final RequestContext requestContext, RegistrationNotAllowedReason reason, RegistrationNotAllowedMessageCodeSource registrationNoAllowedMessageCodeSource) {
		RegisterableProduct product = null;
        Customer customer = requestContext.getCustomer();
        if(requestContext.getProductRegistrationDefinition()!=null)
    	 product = (RegisterableProduct) requestContext.getProductRegistrationDefinition().getProduct();
        String productEmail = null;
        String productName = null;
    	
    	if  (product != null) {
    	    productName = product.getProductName();
    	    productEmail = product.getEmail();
    	}
        String messageCode = registrationNoAllowedMessageCodeSource.getMessageCode(reason, customer, product, ProductState.ACTIVE);

        Map<String, String> map = new HashMap<String, String>();
        map.put(PRODUCT, productName);
    	map.put(EMAIL, productEmail);
    	map.put(MESSAGE_CODE, messageCode);
    	map.put(REGISTRATION_NOT_ALLOWED_REASON, reason.name());
    	
		return new ModelAndView(EACViews.REGISTRATION_NOT_ALLOWED, map);
	}
	
	public static ModelAndView getConcurrencyExceededState(final RequestContext authenticationContext) {
    	Map<String, String> map = new HashMap<String, String>();
    	map.put(EMAIL, authenticationContext.getRegisterableProduct().getEmail());
		return new ModelAndView(EACViews.CONCURRENCY_EXCEEDED, map);
	}
	
	public static ModelAndView getNoActiveLicenceState(final RequestContext authenticationContext, final LicenceService licenceService) {
    	Map<String, Object> map = new HashMap<String, Object>();
    	map.put(EMAIL, authenticationContext.getRegisterableProduct().getEmail());
    	List<LicenceDto> licenceDtos = new ArrayList<LicenceDto>();
    	map.put(LICENCE_DTOS, licenceDtos);
		try {
			licenceDtos.addAll(licenceService.getLicensesForUserProduct(authenticationContext.getCustomer(), authenticationContext.getRegisterableProduct()));
			if (isAllLicencesExpired(licenceDtos)) {
				map.put(ALL_EXPIRED, Boolean.TRUE);
			}
		} catch (ServiceLayerException e) {
			LOGGER.error("Unable to get licence details for user '" + authenticationContext.getCustomer().getId() + "' and product + '"
					+ authenticationContext.getRegisterableProduct().getId() + "': " + e, e);
		}
		LicenceDto.sortByExpiryDateDate(licenceDtos, false);
		return new ModelAndView(EACViews.NO_ACTIVE_LICENCE, map);
	}
	
	private static boolean isAllLicencesExpired(List<LicenceDto> licenceDtos) {
		boolean allExpired = true;
		for (LicenceDto licenceDto : licenceDtos) {
			if (!licenceDto.isExpired()) {
				allExpired = false;
				break;
			}
		}
		return allExpired;
	}
	
	public static boolean isProductRegistrationComplete(RegistrationService registrationService, final RequestContext authenticationContext, final HttpServletRequest request) {
		boolean count = registrationService.getCompletedRegistrationInformation(authenticationContext.getProductRegistrationDefinition().getProduct(), authenticationContext.getCustomer());
		//Long answersCount = registrationDefinitionService.getAnswersForCustomerAndRegistrationDefinition(authenticationContext.getProductRegistrationDefinition().getProduct(), authenticationContext.getCustomer());
		Registration<?> reg = new ProductRegistration();
		if(authenticationContext.getProductRegistration()!=null)
			reg = authenticationContext.getProductRegistration();
		if(count) {
			//reg.setDenied(false);
			//reg.setActivated(false);
			SessionHelper.setCompleted(request.getSession() , true);
			authenticationContext.setProductRegistration((Registration<ProductRegistrationDefinition>) reg);
			LOGGER.debug("Product Registration is completed");
			return true;
		}
		LOGGER.debug("Product Registration is not completed");
		return false;
    }
	
	public static boolean isAwaitingValidation(final RequestContext authenticationContext, final HttpServletRequest request) {
		if(SessionHelper.isAwaitingValidation(request.getSession())!=null && SessionHelper.isAwaitingValidation(request.getSession())) {
			LOGGER.debug("Product Registration is awaiting validation");
			return true;
		}
		LOGGER.debug("Product Registration is not awaiting validation");
		return false;
	}
  
    public static boolean isRegistrationEnabled(final RequestContext authenticationContext) {
    	if(authenticationContext.getProductRegistration().isEnabled()) {
    		LOGGER.debug("Product Registration is Enabled");
    		return true;
    	}
    	LOGGER.debug("Product Registration is not enabled");
    	return false;
    }
    
    public static boolean isDenied(final RequestContext authenticationContext) {
    	if(authenticationContext.getProductRegistration().isDenied()) {
    		LOGGER.debug("Product Registration is denied");
    		return true;
    	}
    	LOGGER.debug("Product Registration is not denied");
    	return false;
    }
    
    public static ModelAndView getLicenceNotActivatedState(final HttpServletRequest request, final RequestContext authenticationContext) throws UnsupportedEncodingException {
    	LOGGER.debug(authenticationContext.getProductRegistrationDefinition().getRegistrationActivation());
    	LOGGER.info("Activation strategy : " +authenticationContext.getProductRegistrationDefinition().getRegistrationActivation().getActivationStrategy(SessionHelper.getLocale(request)));
    	if(authenticationContext.getProductRegistrationDefinition().getRegistrationActivation().getActivationStrategy(SessionHelper.getLocale(request)) == ActivationStrategy.SELF) {
        	Map<String, String> map = new HashMap<String, String>();
        	map.put(PRODUCT, authenticationContext.getRegisterableProduct().getProductName());
        	map.put(EMAIL, authenticationContext.getRegisterableProduct().getEmail());
    		return new ModelAndView(EACViews.ACTIVATE_LICENCE_PAGE, map);
    	} 
    		Map<String, String> map = new HashMap<String, String>();
    		map.put(PRODUCT, authenticationContext.getRegisterableProduct().getProductName());
    		map.put(EMAIL, authenticationContext.getProductRegistrationDefinition().getRegistrationActivation().getProperty("validatorEmail", SessionHelper.getLocale(request)));
    		map.put(SLA, authenticationContext.getRegisterableProduct().getServiceLevelAgreement());
    		return new ModelAndView(EACViews.AWAITING_LICENCE_ACTIVATION_PAGE, map);
    }
    
    public static ModelAndView getProductRegistrationState() {
    	return new ModelAndView(new RedirectView(EACViews.PRODUCT_REGISTRATION_VIEW));
    }
    
    public static ModelAndView getLicenceDeniedState(final HttpServletRequest request, final RequestContext authenticationContext) {
    	Map<String, String> map = new HashMap<String, String>();
    	map.put(PRODUCT, authenticationContext.getRegisterableProduct().getProductName());
    	map.put(EMAIL, authenticationContext.getProductRegistrationDefinition().getRegistrationActivation().getProperty("validatorEmail", SessionHelper.getLocale(request)));
    	return new ModelAndView(EACViews.LICENCE_DENIED_PAGE, map);
    }
    
    public static boolean isActivationCodeAvailable(final HttpServletRequest request) {
    	ActivationCode activationCode = SessionHelper.getActivationCode(request);
    	if(activationCode == null) {
    		LOGGER.debug("Activation code is not available");
    		return false;
    	}
    	LOGGER.debug("Activation is available: " + activationCode.getCode());
    	return true;
    }
    
    public static boolean isCustomerReregistering(final HttpServletRequest request, final RegistrationService registrationService) throws ServiceLayerException {
    	Boolean register = SessionHelper.getReregister(request);
    	if(register == null) {
    		LOGGER.debug("Customer is not re-registering");
    		return false;
    	}
    	LOGGER.debug("Customer is re-registering");
    	return true;
    }
    
    public static ModelAndView getActivationCodeRegistrationState() {
    	return new ModelAndView(new RedirectView(EACViews.INTERNAL_ACTIVATION_CODE_VIEW));
    }
	
    public static void cleanSession(final HttpServletRequest request) {
        cleanSession(request, true);
    }
    
    public static void cleanSession(final HttpServletRequest request, boolean clearForwardUrl) {
        if( clearForwardUrl) {
            SessionHelper.setForwardUrl(request.getSession(), null);
        }
        SessionHelper.setRegisterableProduct(request.getSession(), null);
        SessionHelper.setProductRegistrationDefinition(request.getSession(), null);
        SessionHelper.setUrlSkin(request, null);
        SessionHelper.removeErSession(request);
        SessionHelper.setChangeCustomer(request, null);
        SessionHelper.setCustomerAndWebUserName(request, null, null);
        SessionHelper.setRegistrationId(request, null);
        SessionHelper.setLicenseId(request, null);
    }

    public static void sendCustomerValidationEmail(final RequestContext requestContext, final CustomerService customerService, final HttpServletRequest request) throws ServiceLayerException, UserNotFoundException, ErightsException {
        Customer customer = requestContext.getCustomer();
        customerService.updateSendEmailValidationEmail(customer, SessionHelper.getLocale(request), requestContext.getUserRequestedUrl());
    }
    
    //Khushbu
	/**
	 * @param request
	 * @param requestContext
	 * @param productService
	 * @return
	 */
	public static boolean isUserRequestValid(final HttpServletRequest request, final RequestContext requestContext, 
			final WhiteListUrlService whiteListUrlService)  { 
		List<String> urls = whiteListUrlService.getUrls();
		
		final String userRequestedUrl = getUserRequestedUrl(request, urls);
		if(StringUtils.isBlank(userRequestedUrl)) {
			LOGGER.debug("User request is not given");
			return false;
		}            
		LOGGER.debug("User request: " + userRequestedUrl);
		requestContext.setUserRequestedUrl(userRequestedUrl);
		return true;
	}    
	
	public static void initRequestContext(final HttpServletRequest request, final DomainSkinResolverService domainSkinResolverService,
										final RequestContext requestContext) {
	    resolveSkin(request, requestContext.getUserRequestedUrl(), domainSkinResolverService);
	}
	
	public static Registration<?> saveActivationCodeRegistration(final HttpServletRequest request, final RegistrationService registrationService, final ActivationCode code) throws ServiceLayerException {
		Registration<?> registration = null;
		try {
			registration = registrationService.saveActivationCodeRegistration(code, SessionHelper.getCustomer(request));
			SessionHelper.setAwaitingValidation(request.getSession(), false);
			SessionHelper.setCompleted(request.getSession(), true);
			SessionHelper.setLicenseId(request, registration.getId());
		} catch (ErightsException e) {
			LOGGER.debug(e.getMessage());
			throw new ServiceLayerException("There was a problem with your activation code.");
		} catch (Exception e) {
			LOGGER.debug(e.getMessage());
			throw new ServiceLayerException("There was a problem with your activation code.");
		}
		return registration;
	}
	
	
	/*public static void saveProductRegistration(final HttpServletRequest request, final RegistrationService registrationService, final RequestContext requestContext) throws ServiceLayerException {
		Registration<ProductRegistrationDefinition> productRegistration = registrationService.saveProductRegistration(requestContext.getCustomer(), 
																												requestContext.getProductRegistrationDefinition());
		LOGGER.debug("In product Registration");	
		requestContext.setProductRegistration(productRegistration);
		SessionHelper.setAwaitingValidation(request.getSession(), productRegistration.isAwaitingValidation());
		SessionHelper.setCompleted(request.getSession(), productRegistration.isCompleted());
		//SessionHelper.setRegistrationId(request, productRegistration.getId());
		SessionHelper.setCompletedRegistration(request.getSession(), "Completed");
		LOGGER.debug("Awsiyting Completed value : "+ SessionHelper.getCompletedRegistration(request.getSession()));
	}*/
	
	public static void loadRegisterableProductIfAvailable(final HttpServletRequest request, final ProductService productService, 
			final RequestContext requestContext) {
	    final String requestedUrl = requestContext.getUserRequestedUrl();
		try {
		    if (!URLUtils.isRelativeURL(requestedUrl)) {
		    	LOGGER.debug("URL for product found: " + requestedUrl);
    	        final RegisterableProduct registerableProduct = getRegisterableProductByUrl(requestedUrl, productService);
    	        LOGGER.debug("Request session for product found: " + request.getSession());
    			requestContext.setRegisterableProduct(registerableProduct);
    			SessionHelper.setRegisterableProduct(request.getSession(), registerableProduct);
		    } else {
	            LOGGER.debug("No product available for relative url: " + requestedUrl);		        
		    }
		} catch (NoRegisterableProductFoundException e) {
			LOGGER.debug("No product available for url: " + requestedUrl);
		} catch (ServiceLayerException e) {
			LOGGER.debug("No product available for url: " + requestedUrl, e);
		} catch (InvalidURLException e) {
            LOGGER.debug("No product available for invalid url: " + requestedUrl);
        } catch (ErightsException e) {
        	LOGGER.debug("No product available for url: " + requestedUrl);
		}
	}
    
	/**
	 * Checks if is product registration defined.
	 *
	 * @param request the request
	 * @param requestContext the request context
	 * @param registrationDefinitionService the registration definition service
	 * @return true, if is product registration defined
	 */
	public static boolean isProductRegistrationDefined(final HttpServletRequest request, final RequestContext requestContext,
	        final RegistrationDefinitionService registrationDefinitionService)  {		
		String url = requestContext.getUserRequestedUrl();
		String msg = String.format("Could not load product registration definition for product with url: %s", url);
		boolean test =isProductRegistrationDefined(request, requestContext, registrationDefinitionService, msg);
		return test;
	}
	
	/**
	 * Checks if is product registration defined.
	 *
	 * @param request the request
	 * @param requestContext the request context
	 * @param registrationDefinitionService the registration definition service
	 * @param msg the msg
	 * @return true, if is product registration defined
	 */
	public static boolean isProductRegistrationDefined(final HttpServletRequest request, final RequestContext requestContext,
			final RegistrationDefinitionService registrationDefinitionService, final String msg) {		
        final ProductRegistrationDefinition productRegistrationDefinition = getProductRegistrationDefinition(requestContext.getRegisterableProduct(),
                registrationDefinitionService);
        if (productRegistrationDefinition == null) {
        	LOGGER.debug(msg);
        	return false;
        } 
        LOGGER.debug("Product Registration Definition with awaiting validation : "+productRegistrationDefinition.getRegistrationDefinitionType());
        requestContext.setProductRegistrationDefinition(productRegistrationDefinition);
        SessionHelper.setProductRegistrationDefinition(request, productRegistrationDefinition);
        return true;
	}

    public static boolean isCustomerRegistered(final RegistrationService registrationService, final HttpServletRequest request, final RequestContext authenticationContext) {
    	
    	//TODO consider reducing expense of getting full registration here. Perhaps we can
    	//get registration by id via RegistrationService.getRegistrationById and then
    	//fully initialise only when required and certain parts of workflow.
    	Registration<?> registration = null;
    	CustomerRegistrationsDto customerRegistration;
    	String licenseId =  null;
    	if(SessionHelper.getLicenseId(request)!=null)
    		licenseId=SessionHelper.getLicenseId(request);
    	LOGGER.debug("license id : "+licenseId);
		try {
			customerRegistration = registrationService.getEntitlementsForCustomerRegistrations(authenticationContext.getCustomer(), licenseId, true);
		} catch (ServiceLayerException e) {
			LOGGER.debug("Error while getting registration for customer " + e.getMessage());
			return false;
		} 
	/*	try {*/
		List<Registration<?>> registrations = customerRegistration.getRegistrations();
		if(registrations!=null && registrations.size()!=0){
			for(Registration<?> reg : registrations){
				//LOGGER.debug(authenticationContext.getRegisterableProduct().getErightsId() + " : IDS : " + reg.getRegistrationDefinition().getProduct().getErightsId());
				List<Product> linkedProducts = reg.getRegistrationDefinition().getProduct().getLinkedProducts();

				if(authenticationContext.getRegisterableProduct().getId().equals(reg.getRegistrationDefinition().getProduct().getId())){
					if ( registration == null || ( registration.isExpired() && !reg.isExpired() ) ) {
						registration = reg;
						SessionHelper.setRegistrationId(request, registration.getId());
					}
				}
				for(Product linkedProduct : linkedProducts){
					if(authenticationContext.getRegisterableProduct().getId().equals(linkedProduct.getId())){
						if ( registration == null || ( registration.isExpired() && !reg.isExpired() ) ) {
							registration = reg;
							SessionHelper.setRegistrationId(request, registration.getId());
						}
					}
				}
			}
		}
			//registrationService.getEntitlementsForCustomerRegistrations(customer); 
					//customerService.getRegistrationByRegistrationDefinitionAndCustomer(authenticationContext.getProductRegistrationDefinition(), authenticationContext.getCustomer());
		/*} catch (ServiceLayerException e) {
			LOGGER.debug(e.getMessage());
        	return false;
		}*/

        if(registration==null) {
        	// Customer has no registration
        	registration= new ProductRegistration();
        	authenticationContext.setProductRegistration((Registration<ProductRegistrationDefinition>) registration);
        	LOGGER.debug("Registration is null for product url: " + authenticationContext.getUserRequestedUrl());
        	return false;
    	}
    	LOGGER.debug("Registration found for product url: " + authenticationContext.getUserRequestedUrl());
		authenticationContext.setProductRegistration((Registration<ProductRegistrationDefinition>) registration);
		return true;
    }
	
	public static boolean isDenyReasonAvailable(final HttpServletRequest request) {
		LOGGER.debug("Request : " + request);
        final ErightsDenyReason erightsDenyReason = getErightsDenyReason(request);
        if(erightsDenyReason == null) {
            return false;
        }
        LOGGER.debug("Erights rejected request. Deny Type: " + erightsDenyReason);
        return true;
	}
	
	public static boolean isActiveLicenceAvailable(final RequestContext authenticationContext, final LicenceService licenceService) {
		try {
			if(authenticationContext.getProductRegistration().isActivated())
				  return true;
			List<LicenceDto> licences = licenceService.getLicensesForUserProduct(authenticationContext.getCustomer(), authenticationContext.getRegisterableProduct());
			return isActiveLicenceAvailable(licences);
		} catch (ServiceLayerException e) {
			LOGGER.error("Unable to determine whether there is an active licence available for customer '" + authenticationContext.getCustomer().getId()
					+ "' and product '" + authenticationContext.getRegisterableProduct().getId() + "': " + e, e);
			return false;
		}
	}
	
	private static boolean isActiveLicenceAvailable(List<LicenceDto> licences) {
        if (licences != null) {
	        for(LicenceDto licenceDto : licences) {
	            if(licenceDto.isActive()) {
	                return true;
	            }
	        }
	    }
		return false;
	}
	
	public static boolean isConcurrencyExceeded(final HttpServletRequest request) {
		Set<ErightsLicenceDecision> decisions = getErightsLicenceDecisions(request);
		if(decisions == null || decisions.isEmpty()) {
			LOGGER.debug("Erights has not supplied any licence decisions so assuming that concurrency has not been exceeded");
			return false;
		}
		for(ErightsLicenceDecision decision : decisions) {
			if(decision == ErightsLicenceDecision.DENY_CONCURRENCY) {
				LOGGER.debug("Erights has supplied a deny licence concurrency decision");
				return true;
			}
		}
		LOGGER.debug("Erights has not supplied any licence decisions so assuming that concurrency has not been exceeded");
		return false;
	}
	
	public static boolean isPrimaryDomainSessionAvailable(final HttpServletRequest request, final RequestContext authenticationContext)  {
        final String cookieValue = getCookieValue(request);
        if(StringUtils.isBlank(cookieValue)) {
        	return false;
        }
    	authenticationContext.setCookieValue(cookieValue);
    	return true;
	}
	
	public static boolean isPrimaryDomainSessionValid(final HttpServletRequest request, final CustomerService customerService,
														final RequestContext authenticationContext)  {
        Customer customer = getCustomer(authenticationContext.getCookieValue(), customerService);
        if(customer == null) {
        	return false;
        }
    	authenticationContext.setCustomer(customer);
        //Make customer available in the session
        SessionHelper.setCustomer(request, customer);
    	//It is possible that the cookie value is correct but that we are in a new jsession. In this
        //instance we must make sure that we always set ERSESSION back in the current session
        //once we have determined that the cookie is valid.
        SessionHelper.setErightsSession(request.getSession(), authenticationContext.getCookieValue());
    	return true;
	}
	
	public static boolean isCustomerAuthorisedToRegisterForProduct(final RequestContext context) {
		if(context.getCustomer().getCustomerType() == CustomerType.SELF_SERVICE || context.getCustomer().getCustomerType() == CustomerType.SPECIFIC_CONCURRENCY) {
			if(context.getRegisterableProduct().getRegisterableType() == RegisterableType.SELF_REGISTERABLE) {
				 LOGGER.debug("Customer not authorized for product " +context.getRegisterableProduct().getRegisterableType());
				return true;
			}
		}
		LOGGER.debug("Customer authorized for product " +context.getCustomer().getCustomerType());
		return false;
	}
	
	public static final void initErSession(final HttpServletRequest request, final HttpServletResponse response,  final CustomerSessionDto customerSessionDto) {
	    String erSession = customerSessionDto.getSession();
	    SessionHelper.setErightsSession(request.getSession(), erSession);
        CookieHelper.setErightsCookie(response, erSession);
        LOGGER.debug("Customer session key: " + erSession);  
	}
	
	private static RegisterableProduct getRegisterableProductByUrl(final String url, final ProductService productService) throws ServiceLayerException, NoRegisterableProductFoundException, ErightsException{
		return productService.getRegisterableProductByUrl(url);
	}
	
	private static ErightsDenyReason getErightsDenyReason(final HttpServletRequest request) {
        // Get Atypon Deny Type
        final String denyReasonName = EACSettings.getProperty(EACSettings.ERIGHTS_DENY_REASON_PARAMETER_NAME, DENY_REASON);
        LOGGER.debug("Request : " + request.getParameter(denyReasonName));
        final String denyReason = request.getParameter(denyReasonName);
        if (StringUtils.isBlank(denyReason)) {
            LOGGER.debug("No deny reason is given.");
        	return null;
        }      
        return ErightsDenyReason.getErightsDenyReason(Integer.parseInt(denyReason)); //
    }
	
	static Set<ErightsLicenceDecision> getErightsLicenceDecisions(final HttpServletRequest request) {
        // Get Atypon Licence Decisions
        final String licenseDenyReasonsName = EACSettings.getProperty(EACSettings.ERIGHTS_LICENCE_DECISIONS_PARAMETER_NAME, LICENSE_DENY_TYPES);
        final String licenseDenyReasons = request.getParameter(licenseDenyReasonsName);
        if (StringUtils.isBlank(licenseDenyReasons)) {
            LOGGER.debug("No licence decisions are given.");
        	return new HashSet<ErightsLicenceDecision>();
        }     
        return getDecisions(licenseDenyReasons);
    }
	
	private static Set<ErightsLicenceDecision> getDecisions(final String licenseDenyReasons) {
		final Set<ErightsLicenceDecision> decisions = new HashSet<ErightsLicenceDecision>();
		final String[] decisionsArray = licenseDenyReasons.split(",");
		for(String decision : decisionsArray) {
			decisions.add(ErightsLicenceDecision.getErightsLicenceDecision(Integer.parseInt(decision)));
		}
		return decisions;
	}
	
	private static Customer getCustomer(final String cookieValue, final CustomerService customerService) {
        try {
            Customer customer = customerService.getCustomerFromSession(cookieValue);
            LOGGER.debug("Customer found for session key: " + cookieValue);
            return customer;
        } catch (CustomerNotFoundServiceLayerException e) {
            // No valid customer for session
            LOGGER.debug("Customer not found for session key: " + cookieValue);
            return null;
        }
    }
	
    private static String getCookieValue(final HttpServletRequest request) {
        final Cookie cookie = CookieHelper.getErightsCookie(request);
        LOGGER.debug("Cookie is null: " + (cookie == null));
        final String cookieValue = cookie != null ? cookie.getValue() : null; 
        if (StringUtils.isBlank(cookieValue)) {
        	return null;
        }
        LOGGER.debug("Cookie value: " + cookieValue);
        return cookieValue;       
    }
	
    /**
     * @param request
     * @return
     */
    private static String getUserRequestedUrl(final HttpServletRequest request, 
    		List<String> whiteListUrls) {
        final String eacParamName = EACSettings.getProperty(EACSettings.ERIGHTS_URL_PARAMETER_NAME);
        LOGGER.debug("Looking for erights url parameter with name: " + eacParamName);
        final String userRequestedUrl = request.getParameter(eacParamName);
        LOGGER.debug("User requested Url = " + userRequestedUrl);
        if (StringUtils.isBlank(userRequestedUrl)) {
            LOGGER.debug("Unable to process request as erights parameter was not provided.");
            return null;
        } else {
        	if (URLUtils.checkValidUrl(userRequestedUrl, whiteListUrls)) {
        		SessionHelper.setForwardUrl(request.getSession(), userRequestedUrl);
        		return userRequestedUrl;
        	} else {
        		return null;
        	}
        }
    }   
    
    public static ProductRegistrationDefinition getProductRegistrationDefinition(final RegisterableProduct registerableProduct,
			final RegistrationDefinitionService registrationDefinitionService) {
    	try {
    		return registrationDefinitionService.getProductRegistrationDefinitionByProduct(registerableProduct);
    	} catch (ServiceLayerException e) {
    		LOGGER.debug(e.getMessage());
    		return null;
		}
    }
    
    public static AccountRegistrationDefinition getAccountRegistrationDefinition(final HttpServletRequest request, final RegisterableProduct registerableProduct,
			final RegistrationDefinitionService registrationDefinitionService) {
    	try { 
    		if (registerableProduct == null) {
    			LOGGER.debug("No registerable product available. Unable to load account registration definition. Perhaps the session timed out?");
    		}
    		return registrationDefinitionService.getAccountRegistrationDefinitionByProduct(registerableProduct);
    	} catch (ServiceLayerException e) {
    		LOGGER.debug("No registration definition defined for product id: " + ((registerableProduct != null) ? registerableProduct.getId() : "no product found"));
    		return null;
		}
    }
    
    private static void resolveSkin(final HttpServletRequest request, final String userRequestedUrl, final DomainSkinResolverService domainSkinResolverService) {
        // Set the url skin containing the css path from the requested url
        UrlSkin skin = domainSkinResolverService.getSkinFromDomain(userRequestedUrl);
        SessionHelper.setUrlSkin(request, skin);
    }
    
    /**
     * Gets the account creation state.
     *
     * @param request the request
     * @return the account creation state
     */
    public static ModelAndView getAccountCreationState(final HttpServletRequest request) {
    	return new ModelAndView(new RedirectView(EACViews.ACCOUNT_REGISTRATION_VIEW));
    }

}
