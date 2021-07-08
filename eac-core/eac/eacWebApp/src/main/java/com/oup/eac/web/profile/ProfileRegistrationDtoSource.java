package com.oup.eac.web.profile;

import java.util.List;

import com.oup.eac.domain.Customer;
import com.oup.eac.dto.profile.ProfileRegistrationDto;
import com.oup.eac.service.ServiceLayerException;

/**
 * A Source of ProfileRegistrationDto data for a given customer.
 * 
 * @author David Hay
 * 
 */
public interface ProfileRegistrationDtoSource {

    /**
     * Gets the profile registration dtos.
     *
     * @param customer the customer
     * @return the profile registration dtos
     * @throws ServiceLayerException the service layer exception
     */
    List<ProfileRegistrationDto> getProfileRegistrationDtos(Customer customer) throws ServiceLayerException;
    
}
