package com.oup.eac.ws.v2.service.entitlements;

import com.oup.eac.domain.Customer;
import com.oup.eac.dto.ExternalCustomerIdDto;
import com.oup.eac.ws.v2.binding.common.CreateUser;
import com.oup.eac.ws.v2.binding.common.CreateUserWithConcurrency;
import com.oup.eac.ws.v2.binding.common.User;

public interface CustomerConverter {

    /**
     * Convert customer.
     *
     * @param customer the customer
     * @return the user
     */
    User convertCustomerToUser(final ExternalCustomerIdDto extCustIdDto);

    /**
     * Convert user.
     *
     * @param user the user
     * @return the customer
     */
    Customer convertCreateUserToCustomer(CreateUser user);
    
    
    /**
     * Convert user.
     *
     * @param user the user
     * @return the customer
     */
    Customer convertCreateUserWithConcurrencyToCustomer(CreateUserWithConcurrency user);

}
