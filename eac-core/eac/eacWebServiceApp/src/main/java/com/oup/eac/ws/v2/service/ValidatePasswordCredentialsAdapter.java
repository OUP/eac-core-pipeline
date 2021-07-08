package com.oup.eac.ws.v2.service;

import com.oup.eac.ws.v2.binding.access.ValidatePasswordCredentialsRequest;
import com.oup.eac.ws.v2.binding.access.ValidatePasswordCredentialsResponse;

public interface ValidatePasswordCredentialsAdapter {

    /**
     * Validate.
     *
     * @param request the request
     * @return the validate password credentials response
     */
    public ValidatePasswordCredentialsResponse validate(ValidatePasswordCredentialsRequest request);
    
}
