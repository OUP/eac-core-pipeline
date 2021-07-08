package com.oup.eac.service.impl.entitlements;

import java.util.Map;

import com.oup.eac.domain.Customer;
import com.oup.eac.domain.Product;
import com.oup.eac.domain.ProductRegistrationDefinition;
import com.oup.eac.domain.Registration;
import com.oup.eac.service.entitlements.CustomerProductSource;
public class CustomerProductSourceImpl extends ProductSourceImpl implements CustomerProductSource {

    private final Customer customer;
    private final Registration<? extends ProductRegistrationDefinition> registration;

    public CustomerProductSourceImpl(Map<String, Product> productMap, Customer customer, Registration<? extends ProductRegistrationDefinition> registration) {
        super(productMap);
        this.customer = customer;
        this.registration = registration;
    }

    public CustomerProductSourceImpl(Map<String, Product> productMap, Customer customer) {
        this(productMap, customer, null);
    }
    
    @Override
    public Customer getCustomer() {
        return customer;
    }
    
    @Override
    public Registration<? extends ProductRegistrationDefinition> getRegistration() {
        return registration;
    }

}
