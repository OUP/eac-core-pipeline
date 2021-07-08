package com.oup.eac.ws.v2.service;
import com.oup.eac.ws.v2.binding.access.CreateUserAccountResponse;
import com.oup.eac.ws.v2.binding.common.CreateUser;

/**
 * The Interface CreateUserAccountAdapter.
 */
public interface CreateUserAccountAdapter {

    /**
     * Creates the user accounts.
     *
     * @param users the users
     * @return the creates the user account response
     */
    CreateUserAccountResponse createUserAccounts(CreateUser[] users);
}
