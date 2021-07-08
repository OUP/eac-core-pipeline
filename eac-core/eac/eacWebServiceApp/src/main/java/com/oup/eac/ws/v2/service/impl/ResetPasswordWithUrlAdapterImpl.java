package com.oup.eac.ws.v2.service.impl;

import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.oup.eac.domain.Customer;
import com.oup.eac.service.CustomerService;
import com.oup.eac.service.ServiceLayerException;
import com.oup.eac.service.exceptions.PasswordPolicyViolatedServiceLayerException;
import com.oup.eac.service.exceptions.UserHasNoEmailServiceLayerException;
import com.oup.eac.ws.v2.binding.access.ResetPasswordWithUrlRequest;
import com.oup.eac.ws.v2.binding.access.ResetPasswordWithUrlResponse;
import com.oup.eac.ws.v2.binding.common.ErrorStatus;
import com.oup.eac.ws.v2.binding.common.WsUserId;
import com.oup.eac.ws.v2.binding.common.types.StatusCode;
import com.oup.eac.ws.v2.ex.WebServiceException;
import com.oup.eac.ws.v2.ex.WebServiceValidationException;
import com.oup.eac.ws.v2.service.ResetPasswordWithUrlAdapter;
import com.oup.eac.ws.v2.service.WsCustomerLookup;
import com.oup.eac.ws.v2.service.utils.ErrorStatusUtils;

public class ResetPasswordWithUrlAdapterImpl implements ResetPasswordWithUrlAdapter {

	private final WsCustomerLookup wsCustomerLookup;

	protected final CustomerService customerService;
	
	private static final String validUrlPattern = "^http(s{0,1})://[a-zA-Z0-9_/\\-\\.]+\\.[([A-Za-z/]{2,5})]+[a-zA-Z0-9_/\\&\\?\\=\\-\\.\\~\\%]*";

	@Autowired
	public ResetPasswordWithUrlAdapterImpl(final WsCustomerLookup wsCustomerLookup, final CustomerService customerService){	       
		this.wsCustomerLookup = wsCustomerLookup;
		this.customerService = customerService;
	}

	@Override
	public ResetPasswordWithUrlResponse resetPassword(
			ResetPasswordWithUrlRequest request) throws WebServiceException {
		
		if(StringUtils.isBlank(request.getSuccessUrl()))
			return getResetPasswordResponse(StatusCode.CLIENT_ERROR, "The SuccessUrl cannot be blank.");
		if(StringUtils.isBlank(request.getFailureUrl()))
			return getResetPasswordResponse(StatusCode.CLIENT_ERROR, "The FailureUrl cannot be blank.");
		if(!isValidURL(request.getSuccessUrl()))
			return getResetPasswordResponse(StatusCode.CLIENT_ERROR, "Please enter a valid Success URL.");
		if(!isValidURL(request.getFailureUrl()))
			return getResetPasswordResponse(StatusCode.CLIENT_ERROR, "Please enter a valid Failure URL.");

		Customer customer = null;
		try {
			customer = validateResetPassword(request.getWsUserId());
			//String username = customer.getUsername();
			customerService.updateResetCustomerPasswordwithUrl(customer, Locale.getDefault(),request.getSuccessUrl(), request.getFailureUrl(),request.getTokenId());
			return getResetPasswordResponse(null);
		} catch (PasswordPolicyViolatedServiceLayerException e) {
			//LOG.debug("Password Policy Violated", e);
			return getResetPasswordResponse(StatusCode.CLIENT_ERROR, "There was a problem changing your password. Please contact the system adminsitrator.");
		}catch (UserHasNoEmailServiceLayerException e) {
			//LOG.debug("User: " + username + " does not have an email", e);
			return getResetPasswordResponse(StatusCode.CLIENT_ERROR, e.getMessage());
		}catch (ServiceLayerException sle) {
			return getResetPasswordResponse(StatusCode.SERVER_ERROR, sle.getMessage());
		} catch (WebServiceValidationException wsve) {
			return getResetPasswordResponse(StatusCode.CLIENT_ERROR, wsve.getMessage());
		}
	}

	private final Customer validateResetPassword(final WsUserId wsUserId) throws WebServiceValidationException, ServiceLayerException {
		//validateUsername(username);
		return checkCustomerExists(wsUserId);
	}

	private Customer checkCustomerExists(WsUserId wsUserId) throws WebServiceValidationException {
		return this.wsCustomerLookup.getCustomerByWsUserId(wsUserId);
	}

	private final ResetPasswordWithUrlResponse getResetPasswordResponse(final StatusCode statusCode, final String statusReason) {
		ResetPasswordWithUrlResponse response = new ResetPasswordWithUrlResponse();
		if(statusCode != null){
			response.setErrorStatus(getErrorStatus(statusCode, statusReason));
		}
		return response;
	}

	private final ErrorStatus getErrorStatus(final StatusCode statusCode, final String statusReason) {
		return ErrorStatusUtils.getErrorStatus(statusCode, statusReason);
	}

	private final ResetPasswordWithUrlResponse getResetPasswordResponse(final StatusCode statusCode) {
		return getResetPasswordResponse(statusCode, null);
	}	

	public boolean isValidURL(String url) {  

		return url.matches(validUrlPattern);  
	} 

}
