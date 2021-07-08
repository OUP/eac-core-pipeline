package com.oup.eac.ws.v2.service;

import com.oup.eac.ws.v2.binding.access.GetCredentialPolicyRequest;
import com.oup.eac.ws.v2.binding.access.GetCredentialPolicyResponse;
import com.oup.eac.ws.v2.ex.WebServiceException;
import com.oup.eac.ws.v2.ex.WebServiceValidationException;

/**
 * The Interface EACCredentialPolicyAdapter.
 */

public interface CredentialPolicyAdapter {
	
	/**
     * Gets EAC Credentials Policies
     *
     * @param No Parameters
     * @return the response message contains policies
     * @throws WebServiceException 
     * @throws WebServiceException the web service exception
     */	
	
	public GetCredentialPolicyResponse getCredentialPolicy(GetCredentialPolicyRequest request) throws WebServiceException;

}
