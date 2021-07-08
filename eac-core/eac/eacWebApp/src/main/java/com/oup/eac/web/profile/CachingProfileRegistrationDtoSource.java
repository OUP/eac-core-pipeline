package com.oup.eac.web.profile;

import java.util.List;

import javax.servlet.http.HttpSession;

import com.oup.eac.domain.Customer;
import com.oup.eac.dto.profile.ProfileRegistrationDto;
import com.oup.eac.service.ServiceLayerException;

/**
 * A caching facade for ProfileRegistrationDtoSource where the cache key is Customer and Session Id.
 * 
 * @author David Hay
 *
 */
public interface CachingProfileRegistrationDtoSource {

    /**
     * Gets the profile registration dtos.
     *
     * @param customer the customer
     * @param session the session
     * @return the profile registration dtos
     * @throws ServiceLayerException the service layer exception
     */
    List<ProfileRegistrationDto> getProfileRegistrationDtos(Customer customer, HttpSession session) throws ServiceLayerException;

    /**
     * Removes any associated with the customer and session from the cache.
     *
     * @param customer the customer
     * @param session the session
     */
    void removeFromCache(Customer customer, HttpSession session);
}
