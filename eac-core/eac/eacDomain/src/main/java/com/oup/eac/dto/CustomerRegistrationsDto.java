package com.oup.eac.dto;

import java.util.List;

import com.oup.eac.domain.Customer;
import com.oup.eac.domain.ProductRegistrationDefinition;
import com.oup.eac.domain.Registration;

public class CustomerRegistrationsDto {

    private final Customer user;

    private final List<Registration<? extends ProductRegistrationDefinition>> registrations;

    private final List<LicenceDto> licences;
    
    /**
     * Instantiates a new customer registrations dto.
     * 
     * @param user1
     *            the user
     * @param registrations1
     *            the registrations
     * @param productIdToLicenseDataMap1
     *            the product id to license data map
     */
    public CustomerRegistrationsDto(final Customer user1, final List<Registration<? extends ProductRegistrationDefinition>> registrations1, List<LicenceDto> licences1) {
        super();
        this.user = user1;
        this.registrations = registrations1;
        this.licences = licences1;
    }

    /**
     * Gets the user.
     * 
     * @return the user
     */
    public final Customer getUser() {
        return user;
    }

    /**
     * Gets the registrations.
     * 
     * @return the registrations
     */
    public final List<Registration<? extends ProductRegistrationDefinition>> getRegistrations() {
        return registrations;
    }

    public List<LicenceDto> getLicences() {
        return licences;
    }

}
