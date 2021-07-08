package com.oup.eac.ws.v2.service;

import com.oup.eac.ws.v2.binding.access.ResetPasswordWithUrlRequest;
import com.oup.eac.ws.v2.binding.access.ResetPasswordWithUrlResponse;
import com.oup.eac.ws.v2.ex.WebServiceException;

public interface ResetPasswordWithUrlAdapter {
	
	ResetPasswordWithUrlResponse resetPassword(ResetPasswordWithUrlRequest request) throws WebServiceException;

}
