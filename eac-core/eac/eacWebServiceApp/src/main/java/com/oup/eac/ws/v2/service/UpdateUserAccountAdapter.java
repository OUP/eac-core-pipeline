package com.oup.eac.ws.v2.service;

import com.oup.eac.ws.v2.binding.access.UpdateUserAccountRequest;
import com.oup.eac.ws.v2.binding.access.UpdateUserAccountResponse;
import com.oup.eac.ws.v2.ex.WebServiceException;

/**
 * The Interface UpdateUserAccountAdapter.
 */
public interface UpdateUserAccountAdapter {

    /**
     * Update user account.
     *
     * @param request the request
     * @return the update user account response
     * @throws PasswordPolicyViolatedServiceLayerException 
     * @throws PasswordAlreadyExistsException 
     * @throws PasswordAlreadyUsedException 
     */
    public UpdateUserAccountResponse updateUserAccount(UpdateUserAccountRequest request) throws WebServiceException;
}
