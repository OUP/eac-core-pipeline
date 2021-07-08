package com.oup.eac.ws.v2.service;
import com.oup.eac.ws.v2.binding.access.CreateUserAccountWithConcurrencyResponse;
import com.oup.eac.ws.v2.binding.common.CreateUserWithConcurrency;

/**
 * The Interface CreateUserAccountWithConcurrencyAdapter.
 */
public interface CreateUserAccountWithConcurrencyAdapter {

    /**
     * Creates the user accounts.
     *
     * @param users the users
     * @return the creates the user account response
     */
    CreateUserAccountWithConcurrencyResponse createUserAccountsWithConcurrency(CreateUserWithConcurrency[] users);
}
