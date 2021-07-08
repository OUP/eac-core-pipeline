package com.oup.eac.service.entitlements;

import com.oup.eac.domain.Customer;
import com.oup.eac.domain.ProductRegistrationDefinition;
import com.oup.eac.domain.Registration;

public interface CustomerProductSource extends ProductSource {
    public Customer getCustomer();
    public Registration<? extends ProductRegistrationDefinition> getRegistration();
}
