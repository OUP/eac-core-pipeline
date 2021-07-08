package com.oup.eac.ws.v2.service;

import com.oup.eac.ws.v2.binding.access.UpdateUserAccountWithConcurrencyRequest;
import com.oup.eac.ws.v2.binding.access.UpdateUserAccountWithConcurrencyResponse;
import com.oup.eac.ws.v2.ex.WebServiceException;

/**
 * The Interface UpdateUserAccountWithConcurrencyAdapter.
 */
public interface UpdateUserAccountWithConcurrencyAdapter {

    /**
     * Update user account with concurrency
     *
     * @param request the request
     * @return the update user account response
     * @throws PasswordAlreadyExistsException 
     */
    public UpdateUserAccountWithConcurrencyResponse updateUserAccountWithConcurrency(UpdateUserAccountWithConcurrencyRequest request) throws WebServiceException;
}
