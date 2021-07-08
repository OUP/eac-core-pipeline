package com.oup.eac.web.controllers.authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.util.Assert;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import com.oup.eac.common.utils.EACSettings;
import com.oup.eac.common.utils.email.InternationalEmailAddress;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.Customer.CustomerType;
import com.oup.eac.dto.PasswordResetDto;
import com.oup.eac.integration.facade.exceptions.UserNotFoundException;
import com.oup.eac.service.CustomerService;
import com.oup.eac.service.ServiceLayerException;
import com.oup.eac.service.exceptions.PasswordPolicyViolatedServiceLayerException;
import com.oup.eac.web.controllers.EACSimpleFormController;
import com.oup.eac.web.controllers.context.DirectRequestContext;
import com.oup.eac.web.controllers.context.DirectRequestContextFactory;
import com.oup.eac.web.controllers.context.RequestContext;
import com.oup.eac.web.controllers.helpers.SessionHelper;
import com.oup.eac.web.controllers.registration.EACViews;

/**
 * @author harlandd User password reset controller.
 */
public class PasswordResetFormController extends EACSimpleFormController {

    private static final Logger LOGGER = Logger.getLogger(PasswordResetFormController.class);

    private final CustomerService userService;
	private final DirectRequestContextFactory directRequestContextFactory;

    /**
     * Instantiates a new password reset form controller.
     * 
     * @param userServiceP
     *            the user service
     */
	public PasswordResetFormController(final CustomerService userServiceP, final DirectRequestContextFactory directRequestContextFactory) {
        super();
        Assert.notNull(userServiceP);
		Assert.notNull(directRequestContextFactory);
        this.userService = userServiceP;
		this.directRequestContextFactory = directRequestContextFactory;
    }

	@SuppressWarnings("deprecation")
	@Override
	protected ModelAndView showForm(final HttpServletRequest request, final HttpServletResponse response, BindException errors) throws Exception {
		RequestContext requestContext = new RequestContext();
		DirectRequestContext directRequestContext = directRequestContextFactory.getDirectRequestContext(request, requestContext);
		if (directRequestContext != null) {
			directRequestContext.loadRegisterableProductIfAvailable(request, requestContext);
		}
		return super.showForm(request, response, errors);
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
    /*@Override
    @SuppressWarnings("deprecation")
    protected final ModelAndView processFormSubmission(final HttpServletRequest request, final HttpServletResponse response, final Object command,
            final BindException errors) throws Exception {
        if (errors.hasErrors()) {
            return super.processFormSubmission(request, response, command, errors);
        }
        final PasswordResetDto passwordResetFbo = (PasswordResetDto) command;
        final String username = passwordResetFbo.getUsername();
        final Customer user = userService.getCustomerByUsername(username);
        if (user == null) {
            if (InternationalEmailAddress.isValid(username)) {
                userService.notifyCustomerAccountNotFound(username, SessionHelper.getLocale(request));
            }
            return new ModelAndView(EACViews.PASSWORD_RESET_SUCCESS_PAGE);
        }
        if (user.getCustomerType() == CustomerType.SHARED) {
        	userService.passwordResetAttemptDenied(user, SessionHelper.getLocale(request));
        	return new ModelAndView(EACViews.PASSWORD_RESET_SUCCESS_PAGE);
        }else{        
        	return super.processFormSubmission(request, response, command, errors);
        }
    }*/


    /**
     * On submit.
     *
     * @param request the request
     * @param response the response
     * @param command the command
     * @param errors the errors
     * @return the model and view
     * @throws Exception any checked exception
     */
    @Override
    protected final ModelAndView onSubmit(final HttpServletRequest request, final HttpServletResponse response,
            final Object command, final BindException errors) throws Exception {
    	if (errors.hasErrors()) {
    		return showForm(request, response, errors);
        }
    	final PasswordResetDto passwordResetFbo = (PasswordResetDto) command;
        final String username = passwordResetFbo.getUsername();
        try {
        	 final Customer user = userService.getCustomerByUsername(username);
	        if (user == null) {
	            if (InternationalEmailAddress.isValid(username)) {
	            	userService.notifyCustomerAccountNotFound(username, SessionHelper.getLocale(request));
	            }
	            return new ModelAndView(EACViews.PASSWORD_RESET_SUCCESS_PAGE);
	        }
	        if (user.getCustomerType() == CustomerType.SHARED) {
	        	userService.passwordResetAttemptDenied(user, SessionHelper.getLocale(request));
	        	return new ModelAndView(EACViews.PASSWORD_RESET_SUCCESS_PAGE);
	        } else {
            	userService.updateResetCustomerPassword(user, SessionHelper.getLocale(request), EACSettings.getProperty(EACSettings.EAC_HOST_URL) + "/eac/", null);
            } 
        } catch (UserNotFoundException e){
        	ServiceLayerException ser = new ServiceLayerException(e.getMessage());
        	convertMessages(ser, errors);
        	return new ModelAndView(EACViews.PASSWORD_RESET_SUCCESS_PAGE);
        } catch (final PasswordPolicyViolatedServiceLayerException e) {
        
        	LOGGER.debug(e.getMessage());
            convertMessages(e, errors);
            return showForm(request, response, errors);
        } catch (final ServiceLayerException e) {
        	LOGGER.debug(e.getMessage());
            convertMessages(e, errors);
            return showForm(request, response, errors);
        }
        return new ModelAndView(EACViews.PASSWORD_RESET_SUCCESS_PAGE);
    }
}
