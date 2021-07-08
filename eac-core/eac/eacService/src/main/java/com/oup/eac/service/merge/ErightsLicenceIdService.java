package com.oup.eac.service.merge;

import java.util.List;

import com.oup.eac.domain.Customer;
import com.oup.eac.domain.ProductRegistrationDefinition;
import com.oup.eac.domain.Registration;

/**
 * Used to populate the eRightsLicenceIds fields.
 * 
 * @author David Hay
 *
 */
public interface ErightsLicenceIdService {

    public void saveErightsLicencesInfo(List<RegistrationLicenceMergeInfoDto> updates);
    
    public void saveErightsLicencesInfo(RegistrationLicenceMergeInfoDto update );

    List<Registration<? extends ProductRegistrationDefinition>> getCustomerRegistrations(Customer customer, boolean productRegistrations, boolean activationCodeRegistrations);

}
