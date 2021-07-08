package com.oup.eac.web.profile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oup.eac.domain.Customer;
import com.oup.eac.domain.ProductRegistrationDefinition;
import com.oup.eac.domain.Registration;
import com.oup.eac.dto.CustomerRegistrationsDto;
import com.oup.eac.dto.LicenceDto;
import com.oup.eac.dto.profile.ProfileRegistrationDto;
import com.oup.eac.service.RegistrationService;
import com.oup.eac.service.ServiceLayerException;

/**
 * Implementation of a source of ProfileRegistrationDto data for a given customer.
 * 
 * @author David Hay
 *
 */
@Component
public class ProfileRegistrationDtoSourceImpl implements ProfileRegistrationDtoSource {

    private final RegistrationService registrationService;

    /**
     * Instantiates a new profile registration dto source impl.
     *
     * @param registrationServiceP the registration service
     */
    @Autowired
    public ProfileRegistrationDtoSourceImpl(final RegistrationService registrationServiceP) {
        this.registrationService = registrationServiceP;
    }

    @Override
    public final List<ProfileRegistrationDto> getProfileRegistrationDtos(final Customer customer) throws ServiceLayerException {
        List<ProfileRegistrationDto> result = new ArrayList<ProfileRegistrationDto>();

        CustomerRegistrationsDto temp = registrationService.getEntitlementsForCustomerRegistrations(customer,null, true);
        final Map<String, LicenceDto> licMap = new HashMap<String, LicenceDto>();

        if (temp.getLicences() != null) {
            for (LicenceDto lic : temp.getLicences()) {
                licMap.put(lic.getLicenseId(), lic);
            }
        }

        if (temp.getRegistrations() != null) {
            for (final Registration<? extends ProductRegistrationDefinition> reg : temp.getRegistrations()) {
                LicenceDto dto = licMap.get(reg.getId());
                ProfileRegistrationDto profReg = new ProfileRegistrationDto(reg, dto);
                result.add(profReg);
            }
        }

        return result;
    }

}
