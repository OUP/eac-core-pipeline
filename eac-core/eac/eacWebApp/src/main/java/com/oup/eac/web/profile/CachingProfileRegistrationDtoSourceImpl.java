package com.oup.eac.web.profile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oup.eac.domain.Customer;
import com.oup.eac.dto.profile.ProfileRegistrationDto;
import com.oup.eac.dto.profile.RegistrationStatus;
import com.oup.eac.service.ServiceLayerException;

/**
 * Implementation of a caching facade for ProfileRegistrationDtoSource where the cache key is Customer and Session Id.
 * 
 * @author David Hay.
 *
 */
@Component
public class CachingProfileRegistrationDtoSourceImpl implements CachingProfileRegistrationDtoSource {

    private final ProfileRegistrationDtoSource profileRegistrationDtoSource;
    private static final Logger LOG = Logger.getLogger(CachingProfileRegistrationDtoSourceImpl.class);

    /**
     * Instantiates a new profile registration dto session source impl.
     * 
     * @param profileRegistrationDtoSourceP
     *            the profile registration dto source p
     */
    @Autowired
    public CachingProfileRegistrationDtoSourceImpl(final ProfileRegistrationDtoSource profileRegistrationDtoSourceP) {
        profileRegistrationDtoSource = profileRegistrationDtoSourceP;
    }

    /**
     * {@inheritDoc}
     * 
     * @throws ServiceLayerException
     */
    @Override
    public final List<ProfileRegistrationDto> getProfileRegistrationDtos(final Customer customer, final HttpSession session) throws ServiceLayerException {
        List<ProfileRegistrationDto> unfiltered = profileRegistrationDtoSource.getProfileRegistrationDtos(customer);
        List<ProfileRegistrationDto> result = filter(unfiltered);
        if (LOG.isDebugEnabled()) {
            String msg = String.format("Caching [%d] ProfileRegistrations for Customer Id[%s] UserName[%s] and SessionId[%s]", result.size(), customer.getId(),
                    customer.getUsername(), session.getId());
            LOG.debug(msg);
        }
        return result;
    }
    
    /**
     * Filter.
     * 
     * @param dtos the data
     * @return the list
     */
    private List<ProfileRegistrationDto> filter(final List<ProfileRegistrationDto> dtos) {
        List<ProfileRegistrationDto> result = new ArrayList<ProfileRegistrationDto>();
        List<RegistrationStatus> statusToIgnore = Arrays.<RegistrationStatus>asList(RegistrationStatus.OTHER, RegistrationStatus.INCOMPLETE);
        if (dtos != null) {
            for (ProfileRegistrationDto dto : dtos) {
                RegistrationStatus status = dto.getRegistrationStatus();
                if (!statusToIgnore.contains(status)) {
                    result.add(dto);
                } else {
                    if (LOG.isDebugEnabled()) {
                        String msg = String.format("Filtered out registration for product[%s] status[%s]", dto.getProductName(), dto.getRegistrationStatus());
                        LOG.debug(msg);
                    }
                }
            }
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void removeFromCache(final Customer customer, final HttpSession session) {
        if (LOG.isDebugEnabled()) {
            String msg = String.format("Evicted ProfileRegistrations for Customer Id[%s] UserName[%s] and SessionId[%s]", customer.getId(),
                    customer.getUsername(), session.getId());
            LOG.debug(msg);
        }
    }

}
