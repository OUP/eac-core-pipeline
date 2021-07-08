package com.oup.eac.service.impl.entitlements;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.oup.eac.domain.Customer;
import com.oup.eac.domain.LinkedRegistration;
import com.oup.eac.domain.ProductRegistrationDefinition;
import com.oup.eac.domain.Registration;
import com.oup.eac.dto.CustomerRegistrationsDto;
import com.oup.eac.dto.LicenceDto;
import com.oup.eac.service.entitlements.CustomerLicenceMergeInfo;

public class CustomerLicenceMergeInfoImpl implements CustomerLicenceMergeInfo {

    private final Customer customer;
    private final Set<String> matched;
    private final Set<String> eacOnly;
    private final Set<String> eRightsOnly;

    public CustomerLicenceMergeInfoImpl(CustomerRegistrationsDto customerRegDto) {
        this.customer = customerRegDto.getUser();

        Set<String> erights = getErights(customerRegDto.getLicences());
        Set<String> eac = getEac(customerRegDto.getRegistrations());

        //nested blocks limit scope of temporary variables
        {
            Set<String> matched = new TreeSet<String>(eac);
            matched.retainAll(erights);
            this.matched = Collections.unmodifiableSet(matched);
        }

        {
            Set<String> eacOnly = new TreeSet<String>(eac);
            eacOnly.removeAll(erights);
            this.eacOnly = Collections.unmodifiableSet(eacOnly);
        }

        {
            Set<String> eRightsOnly = new TreeSet<String>(erights);
            eRightsOnly.removeAll(eac);
            this.eRightsOnly = Collections.unmodifiableSet(eRightsOnly);
        }
    }

    private Set<String> getEac(List<Registration<? extends ProductRegistrationDefinition>> registrations) {
        Set<String> result = new HashSet<String>();
        if (registrations != null) {
            for (Registration<? extends ProductRegistrationDefinition> registration : registrations) {
                String licenseId = registration.getId();
                if (licenseId != null) {
                    result.add(licenseId);
                }
                Set<LinkedRegistration> linkedRegs = registration.getLinkedRegistrations();
                if (linkedRegs != null) {
                    for (LinkedRegistration linkedReg : linkedRegs) {
                    	licenseId = linkedReg.getId();
                        if (licenseId != null) {
                            result.add(licenseId);
                        }
                    }
                }
            }
        }
        return Collections.unmodifiableSet(result);
    }

    private Set<String> getErights(List<LicenceDto> licences) {
        Set<String> result = new HashSet<String>();
        if (licences != null) {
            for (LicenceDto dto : licences) {
                String licenseId = dto.getLicenseId(); 
                if(licenseId != null){
                    result.add(licenseId);
                }
            }
        }
        return Collections.unmodifiableSet(result);
    }

    @Override
    public Customer getCustomer() {
        return this.customer;
    }

    @Override
    public Set<String> getMatchedLicences() {
        return this.matched;
    }

    @Override
    public Set<String> getUnMatchedLicencesInEacOnly() {
        return this.eacOnly;
    }

    @Override
    public Set<String> getUnMatchedLicencesInErightsOnly() {
        return this.eRightsOnly;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        
        if(customer != null){
            sb.append(String.format(" | Customer Id[%s] username[%s]", this.customer.getId(), this.customer.getUsername()));
        }else{
            sb.append("Customer[null]");
        }
        
        sb.append(String.format(" | Matched %s", this.matched));
        sb.append(String.format(" | In Eac Only %s", this.eacOnly));
        sb.append(String.format(" | In ERights Only %s", this.eRightsOnly));
        return sb.toString();
    }
    
    public boolean isFullyMatched(){
        boolean result = this.eacOnly.isEmpty() && this.eRightsOnly.isEmpty(); 
        return result;
    }

}
