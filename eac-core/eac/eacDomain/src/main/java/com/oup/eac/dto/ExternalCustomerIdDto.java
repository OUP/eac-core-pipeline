package com.oup.eac.dto;

import java.util.Collections;
import java.util.List;

import com.oup.eac.domain.Customer;
import com.oup.eac.domain.ExternalCustomerId;

public class ExternalCustomerIdDto {

    private static final List<ExternalCustomerId> EMPTY_LIST = Collections.<ExternalCustomerId>emptyList();

    private final Customer customer;
    
    private List<ExternalCustomerId> externalCustomerIds;

    public ExternalCustomerIdDto(Customer customer, List<ExternalCustomerId> ids) {
        this.customer = customer;
        this.externalCustomerIds = ids;
    }

    public ExternalCustomerIdDto(Customer customer) {
        this.customer = customer;
        this.externalCustomerIds = EMPTY_LIST;
    }

    public Customer getCustomer() {
        return customer;
    }

    public List<ExternalCustomerId> getExternalCustomerIds() {
        return externalCustomerIds;
    }
    
    public void setExternalCustomerIds(List<ExternalCustomerId> externalCustomerIdList) {
        this.externalCustomerIds = externalCustomerIdList;
    }
    
}
