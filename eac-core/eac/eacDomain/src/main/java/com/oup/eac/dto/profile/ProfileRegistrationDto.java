package com.oup.eac.dto.profile;

import java.io.Serializable;

import org.springframework.util.Assert;

import com.oup.eac.domain.ProductRegistrationDefinition;
import com.oup.eac.domain.Registration;
import com.oup.eac.dto.LicenceDto;
import com.oup.eac.dto.license.LicenceStatus;

/**
 * A holder for the registration and licence information used for a single row in the registrations table in the profile page.
 * 
 * @author David Hay
 *
 */
public class ProfileRegistrationDto implements Serializable {

    private final Registration<? extends ProductRegistrationDefinition> registration;

    private final LicenceDto licenceDto;

    private final String productName;

    private final String registrationId;

    private final RegistrationStatus registrationStatus;
    private final LicenceStatus licenceStatus;

    /**
     * Instantiates a new profile registration.
     * 
     * @param regP
     *            the reg p
     * @param licP
     *            the lic p
     */
    public ProfileRegistrationDto(final Registration<? extends ProductRegistrationDefinition> regP, final LicenceDto licP) {
        Assert.notNull(regP);
        Assert.notNull(regP.getRegistrationDefinition());
        Assert.notNull(regP.getRegistrationDefinition().getProduct());
        
        this.registration = regP;
        this.licenceDto = licP;
        this.productName = regP.getRegistrationDefinition().getProduct().getProductName();
        this.registrationId = regP.getId();
        
        this.registrationStatus = RegistrationStatus.getFromRegistration(regP);
        this.licenceStatus = LicenceStatus.getLicenceStatus(licenceDto);
    }

    /**
     * Gets the checks for registration page.
     *
     * @return the checks for registration page
     */
    public final boolean getHasRegistrationPage() {
        return this.registration.getRegistrationDefinition().getPageDefinition() != null;
    }

    /**
     * Gets the product name.
     * 
     * @return the product name
     */
    public final String getProductName() {
        return this.productName;
    }

    /**
     * Gets the registration id.
     *
     * @return the registration id
     */
    public final String getRegistrationId() {
        return registrationId;
    }

    /**
     * Gets the licence dto.
     *
     * @return the licence dto
     */
    public final LicenceDto getLicenceDto() {
        return this.licenceDto;
    }

    /**
     * Gets the registration status.
     *
     * @return the registration status
     */
    public final RegistrationStatus getRegistrationStatus() {
        return registrationStatus;
    }

    /**
     * Gets the checks if is manageable.
     *
     * @return the checks if is manageable
     */
    public final boolean getIsManageable() {
        return  getIsActivated() && getHasRegistrationPage();
    }
    
    /**
     * Gets the checks if is activated.
     *
     * @return the checks if is activated
     */
    public final boolean getIsActivated() {
        return  registrationStatus == RegistrationStatus.ACTIVATED;
    }

    /**
     * @return the registration
     */
    public final Registration<? extends ProductRegistrationDefinition> getRegistration() {
        return registration;
    }

    public LicenceStatus getLicenceStatus() {
        return licenceStatus;
    }

    
    
}