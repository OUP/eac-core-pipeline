package com.oup.eac.ws.v2.service.impl;

import com.oup.eac.ws.v2.binding.access.ValidatePasswordCredentialsRequest;
import com.oup.eac.ws.v2.binding.access.ValidatePasswordCredentialsResponse;
import com.oup.eac.ws.v2.binding.access.types.CredentialStatusCode;
import com.oup.eac.ws.v2.service.ValidatePasswordCredentialsAdapter;

public class DummyValidatePasswordCredentialsAdapter implements ValidatePasswordCredentialsAdapter {

    @Override
    public ValidatePasswordCredentialsResponse validate(ValidatePasswordCredentialsRequest request) {
        ValidatePasswordCredentialsResponse response = new ValidatePasswordCredentialsResponse();
        response.setStatus(CredentialStatusCode.INVALID_USERNAME_PASSWORD);
        String usernameError = "The username is not unique";
        String passwordError = "The password is not valid";
        String[] reasons = { usernameError, passwordError };
        response.setStatusReason(reasons);
        return response;
    }

}
