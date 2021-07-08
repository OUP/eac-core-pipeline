package com.oup.eac.web.controllers.registration;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.util.Assert;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.oup.eac.domain.Component;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.Customer.CustomerType;
import com.oup.eac.domain.Field;
import com.oup.eac.domain.ProductRegistrationDefinition;
import com.oup.eac.domain.Question;
import com.oup.eac.domain.Registration;
import com.oup.eac.domain.Tag.TagType;
import com.oup.eac.dto.ProductRegistrationDto;
import com.oup.eac.dto.RegistrationDto;
import com.oup.eac.service.RegistrationService;
import com.oup.eac.service.ServiceLayerException;
import com.oup.eac.web.controllers.EACSimpleFormController;
import com.oup.eac.web.controllers.authentication.AuthenticationWorkFlow;
import com.oup.eac.web.controllers.helpers.SessionHelper;
import com.oup.eac.web.validators.registration.AccountProductFormValidator;

/**
 * The Class ProductRegistrationUpdateController.
 * 
 * @author David Hay
 * 
 */
public class ProductRegistrationUpdateController extends EACSimpleFormController {

    public static final String UPDATING_PRODUCT_REGISTRATION = "updatingProductRegistration";
    public static final String DEFAULT_ARGUMENT_SEPARATOR = ",";
    private static final Logger LOGGER = Logger.getLogger(ProductRegistrationFormController.class);
    private final RegistrationService registrationService;
    AccountProductFormValidator validate=new AccountProductFormValidator();
    /**
     * @param registrationServiceP
     *            the registration service
     */
    public ProductRegistrationUpdateController(final RegistrationService registrationServiceP) {
        super();
        Assert.notNull(registrationServiceP);
        this.registrationService = registrationServiceP;
    }

    /**
     * This makes sure the JSP displays the correct Title and text on the submit
     * button.
     * 
     * @param request
     *            the request
     * @param binder
     *            the binder
     * @throws Exception
     *             the exception
     */
    @Override
    public final void initBinder(final HttpServletRequest request, final ServletRequestDataBinder binder) throws Exception {
        super.initBinder(request, binder);
        request.setAttribute(UPDATING_PRODUCT_REGISTRATION, Boolean.TRUE);
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
        Customer customer = getCustomer(request);
        Assert.notNull(customer);
        if (customer.getCustomerType() == CustomerType.SHARED) {
        	RedirectView rv = new RedirectView(EACViews.PROFILE_VIEW, true, true, false);
            ModelAndView result = new ModelAndView(rv);
            return result;
        }
        
        String registationId = SessionHelper.getRegistrationId(request);
        if (registationId==null) {
            LOGGER.error("No RegistrationId available in session");
            return AuthenticationWorkFlow.getErrorState(request);
        }
        
        try {
        	 
        	Registration reg  =loadRegistration(registationId, customer.getId());
            registrationService.updateRegistration(registrationDto, customer, reg);
            
        } catch (ServiceLayerException e) {
            LOGGER.error("Problem Updating Product Registration.", e);
            convertMessages(e, errors);
            return showForm(request, response, errors);
        }
        request.removeAttribute(UPDATING_PRODUCT_REGISTRATION);
        SessionHelper.removeRegistration(request);
        ModelAndView result = new ModelAndView(new RedirectView(EACViews.PROFILE_VIEW, true));
        return result;
    }

    /**
     * Gets the customer.
     *
     * @param request the request
     * @return the customer
     */
    private Customer getCustomer(final HttpServletRequest request) {
        Customer customer = SessionHelper.getCustomer(request);
        if (customer == null) {
            throw new IllegalStateException("No Customer in Session.");
        }
        return customer;
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
        Customer customer = getCustomer(request);
        ProductRegistrationDefinition prodRegDef = SessionHelper.getProductRegistrationDefinition(request);
        if (prodRegDef == null) {
            throw new IllegalStateException("No ProductRegistrationDefinition in Session.");
        }
        ProductRegistrationDto model = registrationService.getProductPageDefinitionByRegistrationDefinition(prodRegDef, customer, SessionHelper.getLocale(request));
        if (model == null) {
            throw new IllegalStateException("Problem getting RegistrationDto form backing object.");
        }
        model.setReadOnly(customer.getCustomerType() == CustomerType.SHARED);
        return model;
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
    
    private Registration<? extends ProductRegistrationDefinition> loadRegistration(final String registrationId, final String customerId) {
		Registration<? extends ProductRegistrationDefinition> registration;
		try {
			registration = registrationService.getProductRegistration(registrationId, customerId);
		} catch (ServiceLayerException e) {
			throw new IllegalArgumentException("Invalid registration id: " + registrationId);
		}
		if (registration == null) {
			throw new IllegalArgumentException("Invalid registration id: " + registrationId);
		}
		return registration;
	}
    
    }
