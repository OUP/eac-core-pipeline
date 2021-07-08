package com.oup.eac.web.controllers.authentication;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.util.Assert;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import com.oup.eac.common.utils.token.TokenConverter;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.Customer.CustomerType;
import com.oup.eac.domain.UrlSkin;
import com.oup.eac.dto.ChangePasswordDto;
import com.oup.eac.service.CustomerService;
import com.oup.eac.service.DomainSkinResolverService;
import com.oup.eac.service.ServiceLayerException;
import com.oup.eac.service.exceptions.PasswordPolicyViolatedServiceLayerException;
import com.oup.eac.web.controllers.EACSimpleFormController;
import com.oup.eac.web.controllers.helpers.SessionHelper;
import com.oup.eac.web.controllers.registration.EACViews;

/**
 * @author harlandd Customer change password controller.
 */
public class ChangePasswordFormController extends EACSimpleFormController {

    private static final Logger LOGGER = Logger.getLogger(ChangePasswordFormController.class);
    
    private static final String ATTR_AFTER_CHANGE_PASSWORD_DENIED_URL = "afterChangePasswordErrorUrl";
    private static final String CHANGE_PASSWORD_DENIED_JSP = "changePasswordDenied";
    

    
    private final CustomerService customerService;
    private final DomainSkinResolverService domainSkinResolverService;
    

    /**
     * @param customerServiceP the user service
     * @param domainSkinResolverServiceP the domain skin resolver
     */
    public ChangePasswordFormController(final CustomerService customerServiceP, final DomainSkinResolverService domainSkinResolverServiceP) {
        super();
        Assert.notNull(customerServiceP);
        Assert.notNull(domainSkinResolverServiceP);
        this.customerService = customerServiceP;
        this.domainSkinResolverService = domainSkinResolverServiceP;
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
    @Override
    protected final ModelAndView onSubmit(final HttpServletRequest request, final HttpServletResponse response,
            final Object command, final BindException errors)
            throws Exception {
        ChangePasswordDto changePasswordDto = (ChangePasswordDto) command;
        /*if(changePasswordDto.getUsername()!=null && changePasswordDto.getUsername().equalsIgnoreCase("null"))
        	changePasswordDto.setUsername(getUsername(request));*/
        
        String username = getUsername(request);
        String URL = getUrl(request);
        if( username != null ) {
        	changePasswordDto.setUsername(username);
        } else {
        	try {
        		String changePwdToken = SessionHelper.getChangePwdToken(request.getSession()) ;
	        	ChangePasswordDto changePasswordToken = (ChangePasswordDto) TokenConverter.decrypt(changePwdToken,new ChangePasswordDto());
	        	
	        	changePasswordDto.setUsername(changePasswordToken.getUsername());
        	} catch (Exception e) {
        		return new ModelAndView(EACViews.PASSWORD_RESET_FAILURE_PAGE);
        	}
        }
        try {
        	
            customerService.saveChangeCustomerPassword(changePasswordDto, null);
        }catch (PasswordPolicyViolatedServiceLayerException e) {
            LOGGER.debug(e.getMessage());
            errors.reject("", e.getMessage());
            return showForm(request, response, errors);
        }
        catch (ServiceLayerException e) {
            LOGGER.debug(e.getMessage());
            convertMessages(e, errors);
            return showForm(request, response, errors);
        }
        SessionHelper.removeChangeCustomer(request);
        SessionHelper.removeChangePwdToken(request);
        if(URL!=null)
        	return new ModelAndView("redirect:" +URL);
        return AuthenticationWorkFlow.getSuccessfulEndState(request);
    }

    /**
     * @param request
     *            the request
     * @param command
     *            the command
     * @param errors
     *            the errors
     * @return a map of reference data
     * @throws Exception
     *             any checked exception
     */
    @Override
    protected final Map<String, String> referenceData(final HttpServletRequest request, final Object command, final Errors errors) throws Exception {
        // Resolve Skin
        ChangePasswordDto changePasswordDto = (ChangePasswordDto) command;
        //String username = getUsername(request);
        String username=null;
        username = getUsername(request);
        if(username == null || username.isEmpty() ) {
        	try {
        		String changePwdToken = SessionHelper.getChangePwdToken(request.getSession()) ;
	        	ChangePasswordDto changePasswordToken = (ChangePasswordDto) TokenConverter.decrypt(changePwdToken,new ChangePasswordDto());
	        	
	        	username = changePasswordToken.getUsername();
        	} catch (Exception e) {
        		Assert.notNull(username);
        	}
        }
        	
        
        
        Assert.notNull(username);
        changePasswordDto.setUsername(username);
             
        UrlSkin skin = domainSkinResolverService.getSkinFromDomain(SessionHelper.getForwardUrl(request.getSession()));
        SessionHelper.setUrlSkin(request, skin);
        
        Map<String, String> map = new HashMap<String, String>();
        return map;
    }
    
    @SuppressWarnings("deprecation")
    @Override
    protected ModelAndView showForm(HttpServletRequest request,
            HttpServletResponse response,
            BindException errors)
     throws Exception{
    	//Customer customer = SessionHelper.getCustomer(request);
    	Customer customer=null;
    	ChangePasswordDto changePasswordToken=null;
    	String userName=null;
    	if(request.getParameter("token")!=null)
    	{
    		String token=request.getParameter("token");
    		//Added URL as part of OLB requirement, need to revert after Live
    		changePasswordToken = (ChangePasswordDto) TokenConverter.decrypt(token,new ChangePasswordDto());
    	
    		userName=changePasswordToken.getUsername();
    		String url =changePasswordToken.getURL();
    		customer = customerService.getCustomerByUsername(userName);
    		if(customer==null)
    		{
    			 return new ModelAndView(EACViews.PASSWORD_RESET_FAILURE_PAGE);
    		}
    		
    		SessionHelper.setChangePwdToken(request.getSession(),token);
    		SessionHelper.setURL(request.getSession(),url);
    		//request.setAttribute("username", token);
    	}
    	else
    	{
    		customer = SessionHelper.getCustomer(request);
    		if(customer == null)
    		{
    			 return new ModelAndView(EACViews.PASSWORD_RESET_FAILURE_PAGE);
    		}
    				
    	}
    	
    	
    	
    	if(customer == null || customer.getCustomerType() == CustomerType.SHARED) {
    		String afterErrorPageUrl = SessionHelper.getForwardUrl(request);            
            LOGGER.warn("Change Password prohibited, after error page is shown, will invite user to return to : [" + afterErrorPageUrl + "]");
            request.setAttribute(ATTR_AFTER_CHANGE_PASSWORD_DENIED_URL, afterErrorPageUrl);
            ModelAndView result = new ModelAndView(CHANGE_PASSWORD_DENIED_JSP);
            return result;
    	} else {
    		return super.showForm(request, response, errors);
    	}
    }
    
    /**
     * Get username for change password.
     * Should be set up in session, unless session recreated by interceptor
     * @param request
     * @return
     */
    private final String getUsername(final HttpServletRequest request){
        String username = SessionHelper.getChangeCustomer(request);
        if (StringUtils.isBlank(username)) {
            Customer customer = SessionHelper.getCustomer(request);
            if (customer != null) {
                username = customer.getUsername();
            }
        }
        return username;
    }
    private final String getUrl(final HttpServletRequest request){
        String url = SessionHelper.getURL(request.getSession());
        if (!StringUtils.isBlank(url)) {
            return url;
        }
        return null;
    }
}
