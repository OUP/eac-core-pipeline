package com.oup.eac.ws.v2.service.impl.entitlements;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.oup.eac.domain.ActivationCodeRegistration;
import com.oup.eac.domain.Customer;
import com.oup.eac.dto.ExternalCustomerIdDto;
import com.oup.eac.service.ExternalIdService;
import com.oup.eac.ws.v2.service.entitlements.ExternalCustomerIdDtoSource;

public class ExternalCustomerDtoSourceImpl implements ExternalCustomerIdDtoSource {

    private final ExternalIdService externalIdService;
    
    public ExternalCustomerDtoSourceImpl(ExternalIdService externalIdService) {
        this.externalIdService = externalIdService;
    }

	@Override
	public List<ExternalCustomerIdDto> getExternalCustomersId(String systemId, List<ActivationCodeRegistration> registrations) {
		List<Customer> customers = getCustomersInRegistrations(registrations);
		List<ExternalCustomerIdDto> customerDtos = new ArrayList<ExternalCustomerIdDto>();
		for(Customer customer : customers) {
			customerDtos.add(getExternalCustomerId(systemId, customer));
		}
		return customerDtos;
	}
	
   
    public ExternalCustomerIdDto getExternalCustomerId(String systemId, Customer customer) {
        ExternalCustomerIdDto result = externalIdService.getExternalCustomerIds(customer, systemId);
        return result;
    }
    
    private List<Customer> getCustomersInRegistrations(List<ActivationCodeRegistration> registrations) {
        Set<Customer> customers = new HashSet<Customer>();
        if(registrations != null) {
            for(ActivationCodeRegistration registration : registrations) {
                customers.add(registration.getCustomer());
            }
        }
        List<Customer> result = new ArrayList<Customer>(customers);
        return result;
    }
}
