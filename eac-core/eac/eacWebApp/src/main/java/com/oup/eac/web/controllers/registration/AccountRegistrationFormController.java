package com.oup.eac.web.controllers.registration;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.util.Assert;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.HtmlUtils;

import com.oup.eac.common.utils.EACSettings;
import com.oup.eac.common.utils.crypto.PasswordUtils;
import com.oup.eac.common.utils.email.InternationalEmailAddress;
import com.oup.eac.common.utils.username.UsernameValidator;
import com.oup.eac.domain.AccountRegistrationDefinition;
import com.oup.eac.domain.Component;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.Element;
import com.oup.eac.domain.Field;
import com.oup.eac.domain.RegisterableProduct;
import com.oup.eac.domain.Tag;
import com.oup.eac.dto.AccountRegistrationDto;
import com.oup.eac.dto.CustomerSessionDto;
import com.oup.eac.dto.RegistrationDto;
import com.oup.eac.integration.facade.exceptions.ErightsException;
import com.oup.eac.service.CustomerService;
import com.oup.eac.service.RegistrationDefinitionService;
import com.oup.eac.service.RegistrationService;
import com.oup.eac.service.ServiceLayerException;
import com.oup.eac.web.controllers.EACSimpleFormController;
import com.oup.eac.web.controllers.authentication.AuthenticationWorkFlow;
import com.oup.eac.web.controllers.helpers.SessionHelper;
import com.oup.eac.web.validators.registration.AccountProductFormValidator;

/**
 * Login controller.
 * 
 * @author harlandd
 */
public class AccountRegistrationFormController extends EACSimpleFormController {

	private static final String FLOW_STATE = "Account Registration";
	
	public static final String DEFAULT_ARGUMENT_SEPARATOR = ",";
	
    private static final Logger LOGGER = Logger.getLogger(AccountRegistrationFormController.class);

    private final RegistrationService registrationService;

    private final CustomerService customerService;

    private final RegistrationDefinitionService registrationDefinitionService;

    private final UsernameValidator usernameValidator;
    AccountProductFormValidator validate=new AccountProductFormValidator();
  
    /**
     * Instantiates a new account registration form controller.
     * 
     * @param registrationServiceP
     *            the registration service
     * @param customerServiceP
     *            the customer service
     * @param registrationDefinitionServiceP
     *            the registration definition service
     */
    public AccountRegistrationFormController(final RegistrationService registrationServiceP, final CustomerService customerServiceP,
            final RegistrationDefinitionService registrationDefinitionServiceP, final UsernameValidator usernameValidator) {
        super();
        Assert.notNull(registrationServiceP);
        Assert.notNull(customerServiceP);
        Assert.notNull(registrationDefinitionServiceP);
        this.registrationService = registrationServiceP;
        this.customerService = customerServiceP;
        this.registrationDefinitionService = registrationDefinitionServiceP;
        this.usernameValidator=usernameValidator;
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
    @SuppressWarnings("deprecation")
    public final ModelAndView onSubmit(final HttpServletRequest request, final HttpServletResponse response, final Object command, final BindException errors)
            throws Exception {
        AccountRegistrationDto accountRegistrationDto = (AccountRegistrationDto) command;
        try {
        	validate(command, errors,request,response);
        	
        	if (errors.hasErrors()) {
        		
        		return showForm(request, response, errors);
            }
            accountRegistrationDto.setUserLocale(SessionHelper.getLocale(request));
            CustomerSessionDto customerSessionDto = customerService.saveCustomerRegistration(accountRegistrationDto);
            LOGGER.debug("Customer session key: " + customerSessionDto.getSession());
            
            AuthenticationWorkFlow.initErSession(request, response, customerSessionDto);
            
            return AuthenticationWorkFlow.getSuccessfulEndState(request);
            
        } catch (ServiceLayerException e) {
            LOGGER.debug("Problem creating user", e);
            convertMessages(e, errors);
            return showForm(request, response, errors);
        }
    }

    /**
     * @param request
     *            the request
     * @return the command object
     * @throws Exception
     *             any checked exception
     */
    @Override
    protected final Object formBackingObject(final HttpServletRequest request) throws Exception {

        // The RegisterableProduct has to be non-null
        RegisterableProduct regProduct = SessionHelper.getRegisterableProduct(request);

        // The customer may be null.
        Customer customer = SessionHelper.getCustomer(request);

        // Get page definition for current product

        final AccountRegistrationDefinition accountRegistrationDefinition = AuthenticationWorkFlow.getAccountRegistrationDefinition(request, regProduct, registrationDefinitionService);
        if (accountRegistrationDefinition == null) {
            LOGGER.debug("Could not load account registration definition. Using defaults.");
            return new AccountRegistrationDto();
        }
        AccountRegistrationDto result = (AccountRegistrationDto)registrationService.getAccountPageDefinitionByRegistrationDefinition(accountRegistrationDefinition, customer, SessionHelper.getLocale(request));
        for(Component component: result.getComponents()){
			for(Field field : component.getFields()){
				Element elem = field.getElement();
				if(elem.getTag().getTagType() == Tag.TagType.HIDDENFIELD &&
						elem.getQuestion().getElementText().equals("label.account.registration.disableInput")){
					String value = field.getDefaultValue();
					result.setDisableInputFlag(value.equals("disable_username"));
				}
			}
		}
        return result;
    }
    
    @Override
    protected Map referenceData(HttpServletRequest request) throws Exception {
    	Map<String, String> refData = new HashMap<String, String>();
    	refData.put("FLOW_STATE", FLOW_STATE);
    	return refData;
    }
    
    public final void validate(final Object obj, final Errors errors, HttpServletRequest request, HttpServletResponse response) {
		AccountRegistrationDto accountRegistrationFbo = (AccountRegistrationDto) obj;
		accountRegistrationFbo.cleanAnswers();
		
		if (StringUtils.isBlank(accountRegistrationFbo.getFirstName())) {
			/*errors.rejectValue("firstName", "error.not-specified", new Object[] { "title.firstname" }, "First Name is required.");*/
			
			try {
				Object[] msg=validate.resolveMessage(request, "error.not-specified", "First Name is required.", new Object[] { "title.firstname" }, response);
				errors.rejectValue("firstName", "error.not-specified", new Object[] { msg[0] }, "First Name is required.");
			} catch (JspException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

		if (StringUtils.isBlank(accountRegistrationFbo.getFamilyName())) {
			/*errors.rejectValue("familyName", "error.not-specified", new Object[] { "title.familyname" }, "Surname is required.");*/
			try {
				Object[] msg=validate.resolveMessage(request, "error.not-specified", "Surname is required.", new Object[] { "title.familyname" }, response);
				errors.rejectValue("familyName", "error.not-specified", new Object[] { msg[0] }, "Surname is required.");
			} catch (JspException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		validateEmail(accountRegistrationFbo.getEmail(), errors,request,response);

		validateUsername(accountRegistrationFbo.getUsername(), errors,request,response);

		validatePassword(accountRegistrationFbo.getPassword(), errors,request,response);

		validateConfirmPassword(accountRegistrationFbo.getConfirmPassword(), accountRegistrationFbo.getPassword(), errors,request,response);

		validate.validate(obj, errors,request,response);
	}
    private void validateEmail(String email, final Errors errors,HttpServletRequest request,HttpServletResponse response) {
		if (StringUtils.isBlank(email)) {
			/*errors.rejectValue("email", "error.not-specified", new Object[] { "title.email" }, "Email address is required.");*/
			try {
				Object[] msg=validate.resolveMessage(request, "error.not-specified", "Email address is required.", new Object[] { "title.email" }, response);
				errors.rejectValue("email", "error.not-specified", new Object[] { msg[0] }, "Email address is required.");
			} catch (JspException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (!InternationalEmailAddress.isValid(email)) {
			/*errors.rejectValue("email", "error.must.be.valid.email", new Object[] { "title.email" }, "Email address must be a valid email address.");*/
			try {
				Object[] msg=validate.resolveMessage(request, "error.must.be.valid.email", "Email address must be a valid email address.", new Object[] { "title.email" }, response);
				errors.rejectValue("email", "error.must.be.valid.email", new Object[] { msg[0] }, "Email address must be a valid email address.");
			} catch (JspException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void validateUsername(String username, final Errors errors,HttpServletRequest request,HttpServletResponse response) {
		if (StringUtils.isBlank(username)) {
			/*errors.rejectValue("username", "error.not-specified", new Object[] { "title.username" }, "Username is required.");*/
			try {
				Object[] msg=validate.resolveMessage(request, "error.not-specified", "Username is required.", new Object[] { "title.username" }, response);
				errors.rejectValue("username", "error.not-specified", new Object[] { msg[0] }, "Username is required.");
			} catch (JspException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			if (!usernameValidator.isValid(username)) {
				/*errors.rejectValue("username", "error.must.be.valid.username", new Object[] { "title.username" }, "Username must be valid.");*/
				try {
					Object[] msg=validate.resolveMessage(request, "error.must.be.valid.username", "Username must be valid.", new Object[] { "title.username" }, response);
					errors.rejectValue("username", "error.must.be.valid.username", new Object[] { msg[0] }, "Username must be valid.");
				} catch (JspException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				Customer user = null;
				try {
					user = customerService.getCustomerByUsername(username);
				} catch (ErightsException e1) {
					// TODO Auto-generated catch block
					//e1.printStackTrace();
				}
				if (user != null) {
					/*errors.rejectValue("username", "error.username.taken", new Object[] { EACSettings.getProperty(EACSettings.EAC_LOGIN_URL) },
							"This username is already taken. Please try another.");*/
					try {
						Object[] msg=validate.resolveMessage(request, "error.username.taken", "This username is already taken. Please try another.", new Object[] {EACSettings.getProperty(EACSettings.EAC_LOGIN_URL) }, response);
						errors.rejectValue("username", "error.username.taken", new Object[] { msg[0] }, "This username is already taken. Please try another.");
					} catch (JspException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}

	private void validatePassword(String password, final Errors errors,HttpServletRequest request,HttpServletResponse response) {
		if (StringUtils.isBlank(password)) {
			/*errors.rejectValue("passwordcheck", "error.not-specified", new Object[] { "title.password" }, "Password is required.");*/
			try {
				Object[] msg=validate.resolveMessage(request, "error.not-specified", "Password is required.", new Object[] { "title.password" }, response);
				errors.rejectValue("passwordcheck", "error.not-specified", new Object[] { msg[0] }, "Password is required.");
			} catch (JspException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			boolean valid = PasswordUtils.isPasswordValid(password);
			if (!valid) {
				/*errors.rejectValue("passwordcheck", PasswordUtils.INVALID_PASSWORD_MSG_CODE, new Object[] { "title.password" }, "Password is too easy.");*/
				try {
					Object[] msg=validate.resolveMessage(request, PasswordUtils.INVALID_PASSWORD_MSG_CODE, "Password is too easy.", new Object[] { "title.password" }, response);
					errors.rejectValue("passwordcheck", PasswordUtils.INVALID_PASSWORD_MSG_CODE, new Object[] { msg[0] }, "Password is too easy.");
				} catch (JspException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	private void validateConfirmPassword(String confirmPassword, String password, final Errors errors,HttpServletRequest request,HttpServletResponse response) {
		if (StringUtils.isBlank(confirmPassword)) {
			/*errors.rejectValue("confirmPassword", "error.not-specified", new Object[] { "title.confirmpassword" }, "Confirm Password is required.");*/
			try {
				Object[] msg=validate.resolveMessage(request, "error.not-specified", "Confirm Password is required.", new Object[] { "title.confirmpassword" }, response);
				errors.rejectValue("confirmPassword", "error.not-specified", new Object[] { msg[0] }, "Confirm Password is required.");
			} catch (JspException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (!confirmPassword.equals(password)) {
			/*errors.reject("error.must.be.same", new Object[] { "title.password", "title.confirmpassword" }, "Password and "
					+ "Confirm Password must be the same");*/
			try {
				Object[] msg=validate.resolveMessage(request, "error.must.be.same", "Confirm Password must be the same", new Object[] { "title.password", "title.confirmpassword" }, response);
				errors.rejectValue("confirmPassword", "error.must.be.same", new Object[] { msg[0],msg[1] }, "Confirm Password must be the same");
			} catch (JspException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	    
    
   
}
