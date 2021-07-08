package com.oup.eac.service.merge;

import java.util.HashMap;
import java.util.Map;

import com.oup.eac.domain.ActivationCodeRegistration;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.LicenceTemplate;
import com.oup.eac.domain.LinkedProduct;
import com.oup.eac.domain.ProductRegistrationDefinition;
import com.oup.eac.domain.Registration;
import com.oup.eac.domain.RegistrationDefinition.RegistrationDefinitionType;

/**
 * Holds the data used to populate the eRightsLicenceId fields in Registration and LinkedRegistration.
 * @author David Hay
 * 
 */
public class RegistrationLicenceMergeInfoDto {

    public final Customer customer;
    
    private final Registration<? extends ProductRegistrationDefinition> registration;
    
    private Integer eRightsLicenceId;
    
    private final Map<Integer,LinkedProduct> linkedRegistrations = new HashMap<Integer,LinkedProduct>();

    public RegistrationLicenceMergeInfoDto(Customer customer, Registration<? extends ProductRegistrationDefinition> registration){
        this.customer = customer;
        this.registration = registration;
    }
    
    public Customer getCustomer() {
        return customer;
    }

    public Registration<? extends ProductRegistrationDefinition> getRegistration() {
        return registration;
    }

    public Integer geteRightsLicenceId() {
        return eRightsLicenceId;
    }

    public void seteRightsLicenceId(Integer eRightsLicenceId) {
        this.eRightsLicenceId = eRightsLicenceId;
    }

    public Map<Integer, LinkedProduct> getLinkedRegistrations() {
        return linkedRegistrations;
    }

    /**
     * Copied this code from RegistrationServiceImpl
     * @return
     */
    public LicenceTemplate getLicenceTemplate(){
        LicenceTemplate licenceTemplate = registration.getRegistrationDefinition().getLicenceTemplate();
        if(registration.getRegistrationDefinition().getRegistrationDefinitionType() == RegistrationDefinitionType.ACTIVATION_CODE_REGISTRATION) {
            licenceTemplate = ((ActivationCodeRegistration)registration).getActivationCode().getActivationCodeBatch().getLicenceTemplate();
        }
        return licenceTemplate;
    }
}
