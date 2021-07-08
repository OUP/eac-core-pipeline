package com.oup.eac.web.controllers.profile;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.oup.eac.common.date.utils.DateUtils;
import com.oup.eac.domain.AdminUser;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.Customer.CustomerType;
import com.oup.eac.dto.BasicProfileDto;
import com.oup.eac.dto.profile.ProfileRegistrationDto;
import com.oup.eac.integration.facade.exceptions.ErightsException;
import com.oup.eac.integration.facade.exceptions.UserLoginCredentialAlreadyExistsException;
import com.oup.eac.service.AdminService;
import com.oup.eac.service.CustomerService;
import com.oup.eac.service.ServiceLayerException;
import com.oup.eac.service.exceptions.UsernameExistsException;
import com.oup.eac.web.controllers.EACSimpleFormController;
import com.oup.eac.web.controllers.EACSimpleFormController.LowerCaseStringPropertyEditor;
import com.oup.eac.web.controllers.helpers.RequestHelper;
import com.oup.eac.web.controllers.helpers.SessionHelper;
import com.oup.eac.web.controllers.registration.EACViews;
import com.oup.eac.web.locale.LocaleDropDownSource;
import com.oup.eac.web.profile.CachingProfileRegistrationDtoSource;
import com.oup.eac.web.validators.profile.BasicProfileValidator;

/**
 * Controller for Basic Profile page.
 * 
 * @author David Hay
 * 
 * @see com.oup.eac.web.validators.profile.BasicProfileValidator
 * @see com.oup.eac.dto.BasicProfileDto
 * 
 */
@Controller("basicProfileController")
@RequestMapping("profile.htm")
public class BasicProfileController {

    @SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger(BasicProfileController.class);

    private static final String ERR_CODE_USERNAME_TAKEN = "error.username.taken.update";
    private static final String FIELD_USERNAME = "username";
    private static final String ERR_MSG_USERNAME_TAKEN = "This username is already taken. Please try another.";
    
	private static final String MSG_CODE_DETAILS_UPDATED = "profile.details.updated";

    public static final String FORM_NAME = "basicProfileDto";

    public static final String FORM_VIEW_NAME = "basicProfile";

    public static final String KEY_LOCALES = "profileLocales";

    public static final String KEY_TIMEZONE_IDS = "profileTimezoneIds";

    public static final String KEY_REGISTRATIONS = "profileRegistrationDtos";

    public static final String KEY_SUPRESS_CACHE_REFRESH = "profileSupressCacheRefresh";

    public static final List<String> TIMEZONE_IDS;

    static {
        TIMEZONE_IDS = DateUtils.getOrderedTimeZoneIds();
    }

    private final BasicProfileValidator validator;

    private final LocaleDropDownSource localeDropDownSource;

    private final CustomerService customerService;
    
    private final AdminService adminService;

    private final CachingProfileRegistrationDtoSource profileRegistrationSource;

	private static final String FLASH = "profileFlash";

    private static final String LICENCE_DTO_NOW = "licenceDtoNow";

	/**
     * Instantiates a new basic profile controller.
     *
     * @param validatorP the validator
     * @param localeDropDownSourceP the locale drop down source
     * @param customerServiceP the customer service
     * @param profileRegistrationSourceP the profile registration source p
     * @param adminServiceP the admin service p
     */
    @Autowired
    public BasicProfileController(final BasicProfileValidator validatorP, final LocaleDropDownSource localeDropDownSourceP,
            final CustomerService customerServiceP, final CachingProfileRegistrationDtoSource profileRegistrationSourceP, final AdminService adminServiceP) {
        this.validator = validatorP;
        this.localeDropDownSource = localeDropDownSourceP;
        this.customerService = customerServiceP;
        this.profileRegistrationSource = profileRegistrationSourceP;
        this.adminService = adminServiceP;
    }

    /**
     * Inits the binder.
     * 
     * @param binder
     *            the binder
     */
    @InitBinder
    protected final void initBinder(final WebDataBinder binder) {
        binder.setValidator(this.validator);
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
        binder.registerCustomEditor(String.class, FIELD_USERNAME, new LowerCaseStringPropertyEditor());
    }

    /**
     * Gets the locale drop down.
     * 
     * @param request
     *            the request
     * @return the locale drop down
     */
    @ModelAttribute(KEY_LOCALES)
    public final Map<String, String> getLocaleDropDown(final HttpServletRequest request) {
        Locale locale = SessionHelper.getLocale(request);
        Customer customer = SessionHelper.getCustomer(request);
        Map<String, String> locales = localeDropDownSource.getLocaleDropDown(locale, customer.getLocale());
        return locales;
    }

    /**
     * Gets the time zone drop down.
     * 
     * @return the time zone drop down
     */
    @ModelAttribute(KEY_TIMEZONE_IDS)
    public final List<String> getTimeZoneDropDown() {
        return TIMEZONE_IDS;
    }

    /**
     * Gets the form.
     * 
     * @param request
     *            the request
     * @return the blank form
     */
    @ModelAttribute(FORM_NAME)
    public final BasicProfileDto getForm(final HttpServletRequest request) {
        BasicProfileDto result = new BasicProfileDto();
        Customer customer = SessionHelper.getCustomer(request);
        if (RequestHelper.isGetRequest(request)) {

            // we suggest the current locale if the customer doesn't have one
            Locale locale = customer.getLocale();
            if (locale == null) {
                locale = SessionHelper.getLocale(request);
            }
            result.setUserLocale(locale);

            result.setEmail(customer.getEmailAddress());
            result.setUsername(customer.getUsername());
            result.setFirstName(customer.getFirstName());
            result.setFamilyName(customer.getFamilyName());
            result.setTimezone(customer.getTimeZone());
            result.setReadOnly(customer.getCustomerType() == CustomerType.SHARED);
        }
        return result;
    }

    /**
     * Gets the profile registration dtos.
     * 
     * @param request
     *            the request
     * @param session
     *            the session
     * @return the profile registration dtos
     * @throws ServiceLayerException
     *             the service layer exception ( will be handled by
     *             EACExceptionResolver )
     */
    @ModelAttribute(KEY_REGISTRATIONS)
    public final List<ProfileRegistrationDto> getProfileRegistrationDtos(final HttpServletRequest request, final HttpSession session)
            throws ServiceLayerException {
        Customer customer = SessionHelper.getCustomer(request);
        boolean isGet = RequestHelper.isGetRequest(request); 
        if (isGet && (isCacheRefreshSupressed(session) == false)) {
            //we only clear the cache if the request is a GET and there's NOT a KEY_SUPRESS_CACHE_REFRESH flag in the session
            this.profileRegistrationSource.removeFromCache(customer, session);
        }
        clearSuppressCacheRefresh(session);
        List<ProfileRegistrationDto> profileRegistrations = this.profileRegistrationSource.getProfileRegistrationDtos(customer, session);
        if (profileRegistrations == null) {
            profileRegistrations = new ArrayList<ProfileRegistrationDto>();
        }
        return profileRegistrations;
    }

    /**
     * Checks if is cache refresh.
     *
     * @param session the session
     * @return true, if is cache refresh
     */
    private boolean isCacheRefreshSupressed(final HttpSession session) {
        Boolean attr = (Boolean) session.getAttribute(KEY_SUPRESS_CACHE_REFRESH);
        boolean result = (attr != null) && Boolean.TRUE.equals(attr);
        return result;
    }

    /**
     * Clear suppress cache refresh.
     *
     * @param session the session
     */
    private void clearSuppressCacheRefresh(final HttpSession session) {
        session.removeAttribute(KEY_SUPRESS_CACHE_REFRESH);
    }
    
    /**
     * Sets the suppress cache refresh.
     *
     * @param session the new suppress cache refresh
     */
    private void setSuppressCacheRefresh(final HttpSession session) {
        session.setAttribute(KEY_SUPRESS_CACHE_REFRESH, Boolean.TRUE);
    }

    /**
     * Process get.
     * 
     * @param request
     *            the request
     * @param response
     *            the response
     * @return the string
     */
    @RequestMapping(method = RequestMethod.GET)
    public final String processGet(final HttpServletRequest request, final HttpServletResponse response) {
        
        SessionHelper.setUrlSkin(request, null); //clear any product related css from session
        request.setAttribute(LICENCE_DTO_NOW, new DateTime());
        return FORM_VIEW_NAME;
    }

    /**
     * Process post.
     * 
     * @param dto
     *            the dto
     * @param errors
     *            the errors
     * @param request
     *            the request
     * @param response
     *            the response
     * @param session
     *            session
     * @return the model and view
     */
    @RequestMapping(method = RequestMethod.POST)
    public final ModelAndView processPost(@Valid @ModelAttribute(FORM_NAME) final BasicProfileDto dto, final BindingResult errors,
            final HttpServletRequest request, final HttpServletResponse response, final HttpSession session) {

        try {
        	
            Customer sessionCustomer = SessionHelper.getCustomer(request);
            
            if (sessionCustomer.getCustomerType() == CustomerType.SELF_SERVICE || sessionCustomer.getCustomerType() == CustomerType.SPECIFIC_CONCURRENCY) {
            	
            	String username = dto.getUsername();

            	checkExistingUserName(sessionCustomer, username, errors);

            	if (errors.hasErrors()) {
            		return createValidationResponse(errors);
            	}

            	Locale locale = dto.getUserLocale();
            	SessionHelper.setLocale(request, response, locale);

            	this.customerService.updateCustomerProfile(sessionCustomer.getId(), dto, false);
            
            	Customer updatedCustomer = customerService.getCustomerById(sessionCustomer.getId());
            
            	//this updates the 'webUserName' attribute used in the navigationBar too.
            	SessionHelper.setCustomer(request, updatedCustomer);     	
        	
            	addMessageToFlashContext(session, MSG_CODE_DETAILS_UPDATED);

            }
        } catch (UsernameExistsException sle) {
            return createValidationResponse(errors, sle);
        } catch (ServiceLayerException sle) {
            return createValidationResponse(errors, sle);
        } catch (UserLoginCredentialAlreadyExistsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        //don't clear the stored registration profiles - we are going back to the profile page!
        setSuppressCacheRefresh(request.getSession());

        // clean redirect - without model attributes
        RedirectView rv = new RedirectView(EACViews.PROFILE_VIEW, true, true, false);
        ModelAndView result = new ModelAndView(rv);
        return result;
    }

    /**
     * If the username is not in error then Checks if is user name exists
     * already.
     * 
     * @param customer
     *            the customer
     * @param username
     *            the username
     * @param errors
     *            the errors
     */
    private void checkExistingUserName(final Customer customer, final String username, final BindingResult errors) {
        if (!errors.hasFieldErrors(FIELD_USERNAME)) {
            AdminUser admin = adminService.getAdminUserByUsername(username);
            boolean valid = admin == null;
            if (valid) {
                Customer user= null;
				try {
					user = customerService.getCustomerByUsername(username);
				} catch (UserLoginCredentialAlreadyExistsException e) {
					errors.rejectValue(FIELD_USERNAME, ERR_CODE_USERNAME_TAKEN, ERR_MSG_USERNAME_TAKEN);
				} catch (ErightsException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                valid = user == null || customer.getId().equals(user.getId());
            }
            if (!valid) {
                errors.rejectValue(FIELD_USERNAME, ERR_CODE_USERNAME_TAKEN, ERR_MSG_USERNAME_TAKEN);
            }
        }
    }

    /**
     * Creates the validation response.
     * 
     * @param errors
     *            the errors
     * @param sle
     *            the sle
     * @return the model and view
     */
    private ModelAndView createValidationResponse(final BindingResult errors, final ServiceLayerException sle) {
        EACSimpleFormController.convertMessages(sle, errors);
        return createValidationResponse(errors);
    }

    /**
     * Creates the validation response.
     * 
     * @param errors
     *            the errors
     * @return the model and view
     */
    private ModelAndView createValidationResponse(final BindingResult errors) {
        ModelAndView result = new ModelAndView(FORM_VIEW_NAME, errors.getModel());
        return result;
    }
    
	@SuppressWarnings("unchecked")
	@ModelAttribute(FLASH)
	public final Map<String, Object> getFlashContext(HttpSession session) {
		Map<String, Object> flashContext = (Map<String, Object>) session.getAttribute(FLASH);
		if (flashContext != null) {
			session.removeAttribute(FLASH);
		}
		return flashContext;
	}

	@SuppressWarnings("unchecked")
	private void addMessageToFlashContext(HttpSession session, String msg, Object... args) {
		Map<String, Object> flashContext = (Map<String, Object>) session.getAttribute(FLASH);
		if (flashContext == null) {
			flashContext = new LinkedHashMap<String, Object>();
			session.setAttribute(FLASH, flashContext);
		}
		Object[] msgArgs = args;
		flashContext.put(msg,msgArgs);
	}


}
