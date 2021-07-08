package com.oup.eac.web.controllers.emailBroker;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.HtmlUtils;

import com.oup.eac.common.utils.EACSettings;
import com.oup.eac.common.utils.exception.ExceptionUtil;
import com.oup.eac.common.utils.token.TokenConverter;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.utils.audit.AuditLogger;
import com.oup.eac.dto.ChangePasswordDto;
import com.oup.eac.dto.ResetPasswordTokenDto;
import com.oup.eac.dto.TokenDto;
import com.oup.eac.service.CustomerService;
import com.oup.eac.service.RegistrationService;
import com.oup.eac.web.controllers.helpers.SessionHelper;

@Controller
@RequestMapping(value = { "/emailBroker" })
public class HandleEmailLinkController {

	private static Logger LOG = Logger.getLogger(HandleEmailLinkController.class);
	private final RegistrationService registrationService;
	String errUrl = EACSettings.getProperty(EACSettings.EAC_HOST_URL)
			+ EACSettings.getProperty(EACSettings.EMAILBROKER_FAILURE_URL);
	private final CustomerService customerService;
	
	 @Autowired
	    public HandleEmailLinkController(CustomerService customerService,  RegistrationService registrationService) {
	        Assert.notNull(customerService);
	        Assert.notNull(registrationService);
	        
	        this.customerService = customerService;
	        this.registrationService = registrationService;
	    }
	

	@RequestMapping(value = { "/resetPassword.htm" }, method = RequestMethod.GET)
	public ModelAndView handleResetPasswordLink(final HttpServletRequest request,
			final HttpServletResponse response) {

		String tokenString = request.getParameter("token");
		String failureUrl=null;
		String successUrl=null;
		Customer customer=null;
		try {
			ResetPasswordTokenDto token = (ResetPasswordTokenDto) TokenConverter.decrypt(tokenString,
					new ResetPasswordTokenDto());
			String userId=token.getUserId();
			failureUrl=token.getFailureUrl();
			successUrl=token.getSuccessUrl();
			
			customer=customerService.getCustomerById(userId);
			boolean resetPassword =  customer.isResetPassword();
		
			if(token.getClientTokenId()!=null){
				
				ChangePasswordDto changePasswordToken = (ChangePasswordDto) TokenConverter.decrypt(token.getClientTokenId(),
						new ChangePasswordDto());
				if(changePasswordToken.getUsername()==null){
					successUrl= successUrl+"?token="+changePasswordToken.getTokenId();
					failureUrl= failureUrl+"?token="+changePasswordToken.getTokenId();
				}else{
					successUrl= successUrl+"?token="+token.getClientTokenId();
					failureUrl= failureUrl+"?token="+token.getClientTokenId();
				}
				
				SessionHelper.setURL(request.getSession(),changePasswordToken.getURL());
			}
			if(resetPassword && !token.isExpired()){
				return new ModelAndView("redirect:" +successUrl);
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(ExceptionUtil.getStackTrace(e));
			AuditLogger.logEvent(customer, "Reset Password failed. :: " + e.getMessage());
			return new ModelAndView("redirect:" +errUrl);
		}
		return new ModelAndView("redirect:" +failureUrl);
	}
	
	@RequestMapping(value = { "/registrationAllow" }, method = RequestMethod.GET)
	public ModelAndView handleSelfActivationLink(final HttpServletRequest request,
			final HttpServletResponse response) {
		String tokenString = request.getParameter("token");
		String url=null;
		String parameters="";
		try {
			TokenDto token = (TokenDto) TokenConverter.decrypt(tokenString,
					new TokenDto());
			url = token.getOrignalUrl();
			
			if (url==null) {
				url=errUrl;
				return new ModelAndView("redirect:" + url);
			}
			
			parameters="?username="+encodeParameter(token.getUserName())+"&product="+encodeParameter(token.getProductName());
			registrationService.activateLicense(token.getUserId(), token.getLicenseId(), token.getProductName());
			return new ModelAndView("redirect:" + url + parameters ) ;

		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(ExceptionUtil.getStackTrace(e));
			return new ModelAndView("redirect:" + errUrl + parameters);
		}
		
	}

	@RequestMapping(value = { "/validatorRegistrationAllow" }, method = RequestMethod.GET)
	public ModelAndView handleAdminAllowLink(final HttpServletRequest request,
			final HttpServletResponse response) {

		String tokenString = request.getParameter("token");
		String adminAllowSuccessUrl=EACSettings.getProperty(EACSettings.EAC_HOST_URL) 
				+ EACSettings.getProperty(EACSettings.EMAILBROKER_VALIDATOR_REGISTRATION_ALLOW_URL);

		String parameters="";
		try {
			TokenDto token = (TokenDto) TokenConverter.decrypt(tokenString,
					new TokenDto());
			parameters="?username="+encodeParameter(token.getUserName())+"&product="+encodeParameter(token.getProductName());
			if (token.getUserName()== null || token.getProductName() == null || token.getUserId() == null || token.getLicenseId() == null || token.getProductName() == null) {
				return new ModelAndView("redirect:" + errUrl + parameters);
			}

			registrationService.activateLicense(token.getUserId(), token.getLicenseId(), token.getProductName());
			return new ModelAndView("redirect:" + adminAllowSuccessUrl + parameters);

		} catch (Exception e) {
			LOG.error(ExceptionUtil.getStackTrace(e));
			e.printStackTrace();
			return new ModelAndView("redirect:" + errUrl + parameters);
		}
	}

	@RequestMapping(value = { "/validatorRegistrationDeny" }, method = RequestMethod.GET)
	public ModelAndView handleAdminDenyLink(final HttpServletRequest request,
			final HttpServletResponse response) {

		String tokenString = request.getParameter("token");
		String adminDenySuccessUrl=EACSettings.getProperty(EACSettings.EAC_HOST_URL) 
				+ EACSettings.getProperty(EACSettings.EMAILBROKER_VALIDATOR_REGISTRATION_DENY_URL);

		String parameters="";
		try {
			TokenDto token = (TokenDto) TokenConverter.decrypt(tokenString,
					new TokenDto());
			parameters="?username="+encodeParameter(token.getUserName())+"&product="+encodeParameter(token.getProductName());
			if (token.getUserName()== null || token.getProductName() == null || token.getUserId() == null || token.getLicenseId() == null || token.getProductName() == null) {
				return new ModelAndView("redirect:" + errUrl + parameters);
			}
			

			registrationService.deactivateLicense(token.getUserId(), token.getLicenseId(), token.getProductName());
			
			return new ModelAndView("redirect:" + adminDenySuccessUrl + parameters);

		} catch (Exception e) {
			LOG.error(ExceptionUtil.getStackTrace(e));
			e.printStackTrace();
			return new ModelAndView("redirect:" + errUrl + parameters);
		}
	}
	private String encodeParameter(String value) throws UnsupportedEncodingException {
		value = HtmlUtils.htmlEscape(value) ;
		return URLEncoder.encode(value,"UTF-8") ;
	}
	
}
