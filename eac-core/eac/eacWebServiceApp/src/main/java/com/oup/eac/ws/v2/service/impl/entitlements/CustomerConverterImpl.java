package com.oup.eac.ws.v2.service.impl.entitlements;

import com.oup.eac.domain.Customer;
import com.oup.eac.domain.Customer.CustomerType;
import com.oup.eac.domain.Password;
import com.oup.eac.dto.ExternalCustomerIdDto;
import com.oup.eac.ws.v2.binding.common.CreateUser;
import com.oup.eac.ws.v2.binding.common.CreateUserWithConcurrency;
import com.oup.eac.ws.v2.binding.common.Credential;
import com.oup.eac.ws.v2.binding.common.CredentialName;
import com.oup.eac.ws.v2.binding.common.Identifiers;
import com.oup.eac.ws.v2.binding.common.PasswordCredential;
import com.oup.eac.ws.v2.binding.common.User;
import com.oup.eac.ws.v2.service.entitlements.CustomerConverter;
import com.oup.eac.ws.v2.service.utils.IdUtils;
import com.oup.eac.ws.v2.service.utils.LocaleUtils;

public class CustomerConverterImpl implements CustomerConverter {

    /**
     * Gets the user.
     *
     * @param customer the customer
     * @return the user
     */
    public final User convertCustomerToUser(final ExternalCustomerIdDto extCustIdDto) {
        User user = new User();
        Customer customer = extCustIdDto.getCustomer();
        
        //basic
        user.setEmailAddress(customer.getEmailAddress());
        user.setFirstName(customer.getFirstName());
        user.setLastName(customer.getFamilyName());
        user.setLocale(LocaleUtils.getLocaleType(customer.getLocale()));
        user.setTimeZone(customer.getTimeZone());
        
        CredentialName credId = new CredentialName();
        credId.setUserName(customer.getUsername());
        //userName
        user.setCredentialName(credId);

        //identifiers
        Identifiers ids = IdUtils.getIds(customer.getId(), extCustIdDto.getExternalCustomerIds());
        user.setUserIds(ids);
        return user;
    }

    @Override
    public Customer convertCreateUserToCustomer(CreateUser user) {
        Customer customer = new Customer();
        customer.setEmailAddress(user.getEmailAddress());
        customer.setFirstName(user.getFirstName());
        customer.setFamilyName(user.getLastName());
        customer.setLocale(LocaleUtils.getLocale(user.getLocale()));
        customer.setTimeZone(user.getTimeZone());
        
        Credential cred = user.getCredentials();
        PasswordCredential pwd = cred.getPasswordCredential();
        
        String username = pwd.getUserName();
        String password = pwd.getPassword();
        
        customer.setUsername(username);
        customer.setPassword(new Password(password, false));
        return customer;
    }
    
    @Override
    public Customer convertCreateUserWithConcurrencyToCustomer(CreateUserWithConcurrency user) {
        Customer customer = new Customer();
        customer.setEmailAddress(user.getEmailAddress());
        customer.setFirstName(user.getFirstName());
        customer.setFamilyName(user.getLastName());
        customer.setLocale(LocaleUtils.getLocale(user.getLocale()));
        customer.setTimeZone(user.getTimeZone());
        
        Credential cred = user.getCredentials();
        PasswordCredential pwd = cred.getPasswordCredential();
        
        String username = pwd.getUserName();
        String password = pwd.getPassword();
        
        customer.setUsername(username);
        customer.setPassword(new Password(password, false));
        customer.setCustomerType(CustomerType.SPECIFIC_CONCURRENCY);
        
        
        return customer;
    }

}
