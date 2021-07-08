package com.oup.eac.web.controllers.registration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.util.Assert;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.oup.eac.domain.Component;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.Field;
import com.oup.eac.domain.Question;
import com.oup.eac.domain.Tag.TagType;
import com.oup.eac.dto.LicenceDto;
import com.oup.eac.dto.ProductRegistrationDto;
import com.oup.eac.dto.RegistrationDto;
import com.oup.eac.integration.facade.ErightsFacade;
import com.oup.eac.service.CustomerService;
import com.oup.eac.service.RegistrationService;
import com.oup.eac.service.ServiceLayerException;
import com.oup.eac.service.exceptions.CustomerNotFoundServiceLayerException;
import com.oup.eac.web.controllers.EACSimpleFormController;
import com.oup.eac.web.controllers.authentication.AuthenticationWorkFlow;
import com.oup.eac.web.controllers.helpers.CookieHelper;
import com.oup.eac.web.controllers.helpers.SessionHelper;
import com.oup.eac.web.validators.registration.AccountProductFormValidator;

/**
 * @author harlandd Product registration controller.
 */
public class ProductRegistrationFormController extends EACSimpleFormController {

	private static final String FLOW_STATE = "Product Registration";
    private static final Logger LOGGER = Logger.getLogger(ProductRegistrationFormController.class);
    private final RegistrationService registrationService;
    private final CustomerService customerService;
    private final ErightsFacade erightsFacade;
    
    AccountProductFormValidator validate=new AccountProductFormValidator();
    /**
     * @param registrationServiceP
     *            the registration service
     * @param customerServiceP
     *            the user service
     */
    public ProductRegistrationFormController(final RegistrationService registrationServiceP, final CustomerService customerServiceP, final ErightsFacade erightsFacade) {
        super();
        Assert.notNull(registrationServiceP);
        Assert.notNull(customerServiceP);
        Assert.notNull(erightsFacade);
        this.registrationService = registrationServiceP;
        this.customerService = customerServiceP;
        this.erightsFacade = erightsFacade;
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
        RegistrationDto registrationDto = (RegistrationDto) command;
        validate(command, errors,request,response);
    	
    	if (errors.hasErrors()) {
    		return showForm(request, response, errors);
        }
        // Try and use an existing customer. If no customer is available, get
        // customer from cookie
        Customer customer = SessionHelper.getCustomer(request);
        if (customer == null) {
            Cookie cookie = CookieHelper.getErightsCookie(request);
            if (cookie != null) {
                try {
                    customer = customerService.getCustomerFromSession(cookie.getValue());
                } catch (CustomerNotFoundServiceLayerException e) {
                    // No valid customer for session so try to logout
                    try {
                        customerService.logout(cookie.getValue());
                    } catch (ServiceLayerException ex) {
                        LOGGER.debug(ex.getMessage());
                    }                   
                    // No valid customer for session so invalidate the cookie
                    CookieHelper.invalidateErightsCookie(response);
                    return new ModelAndView(new RedirectView(EACViews.ACCESS_VIEW));
                }
            }
            if (customer == null) {
                // No valid customer found
                return new ModelAndView(new RedirectView(EACViews.ACCESS_VIEW));
            }
        }
        try {
            registrationService.saveCompleteRegsitration(registrationDto, customer, SessionHelper.getRegisterableProduct(request).getId());
            if(SessionHelper.getRegistrationId(request)!=null){
	            String regId = SessionHelper.getRegistrationId(request);
	            LOGGER.debug("Registration Id : "+ regId);
	            List<LicenceDto> licenceDto = erightsFacade.getLicensesForUser(customer.getId(), regId);
	            LicenceDto licence = licenceDto.get(0);
	            SessionHelper.setLicenceDto(request.getSession(), licence);
	            if(licence.isExpired()){
	            	erightsFacade.renewLicence(customer.getId(), licence);
	            }
	            else{
	            	erightsFacade.updateLicence(customer.getId(), licence);
	            }
            }else
            	SessionHelper.setAwaitingValidation(request.getSession(), false);
            SessionHelper.setCompleted(request.getSession(), true);
            LOGGER.debug("Registration Completed finally");
        } catch (ServiceLayerException e) {
            LOGGER.error("Problem registering product.", e);
            convertMessages(e, errors);
            return showForm(request, response, errors);
        }
        return AuthenticationWorkFlow.getSuccessfulEndState(request);
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

        // Check if customer is available. Do not expect this to happen
        Customer customer = SessionHelper.getCustomer(request);
        if (customer == null) {
            throw new Exception("Invalid user session. No customer is available.");
        }

        ProductRegistrationDto registrationDto = registrationService.getProductPageDefinitionByRegistrationDefinition(SessionHelper.getProductRegistrationDefinition(request), customer, SessionHelper.getLocale(request));
        return registrationDto;
    }

    @Override
    protected Map referenceData(HttpServletRequest request) throws Exception {
    	Map<String, String> refData = new HashMap<String, String>();
    	refData.put("FLOW_STATE", FLOW_STATE);
    	return refData;
    }
    
    public void validate(final Object obj, final Errors errors, HttpServletRequest request, HttpServletResponse response) {
        RegistrationDto registrationDto = (RegistrationDto) obj;
        registrationDto.cleanAnswers();
        for(Component component : registrationDto.getComponents()) {
	        for (Field field : component.getFields()) {
	        	Question question = field.getElement().getQuestion();
	            String answer = registrationDto.getAnswersForQuestion(field);
	            if (StringUtils.isBlank(answer)) {
	                // validate required fields
	                if (field.isRequired()) {
	                    /*errors.rejectValue("answers['" + question.getId() + "']", "error.not-specified", new Object[] { field.getElement().getTitleText() },
	                    		field.getElement().getTitleText() + " is required.");*/
	                	try {
	        				Object[] msg=validate.resolveMessage(request, "error.not-specified", field.getElement().getTitleText() + " is required.", new Object[] { field.getElement().getTitleText() }, response);
	        				errors.rejectValue("answers['" + question.getId() + "']", "error.not-specified", new Object[] { msg[0] }, field.getElement().getTitleText() + " is required.");
	        			} catch (JspException e) {
	        				// TODO Auto-generated catch block
	        				e.printStackTrace();
	        			}
	                    
	                }
	            } else {
	                if (field.getElement().getTag().getTagType() != TagType.MULTISELECT && StringUtils.isNotBlank(field.getElement().getRegularExpression())) {
	                    Pattern pattern = Pattern.compile(field.getElement().getRegularExpression());
	                    Matcher matcher = pattern.matcher(answer);
	                    if (!matcher.find()) {
	                        /*errors.rejectValue("answers['" + question.getId() + "']", "error.regularexpression", new Object[] { field.getElement()
	                                .getTitleText() }, field.getElement().getTitleText() + " is invalid");*/
	                    	try {
		        				Object[] msg=validate.resolveMessage(request, "error.regularexpression", field.getElement().getTitleText() + " is required.", new Object[] { field.getElement().getTitleText() }, response);
		        				errors.rejectValue("answers['" + question.getId() + "']", "error.regularexpression", new Object[] { msg[0] }, field.getElement().getTitleText() + " is required.");
		        			} catch (JspException e) {
		        				// TODO Auto-generated catch block
		        				e.printStackTrace();
		        			}
	                    }
	                }
	            }
	
	        }
        }
    }
    
    
}
