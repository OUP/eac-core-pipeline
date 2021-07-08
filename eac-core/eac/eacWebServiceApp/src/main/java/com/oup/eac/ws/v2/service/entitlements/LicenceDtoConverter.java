package com.oup.eac.ws.v2.service.entitlements;

import com.oup.eac.dto.LicenceDto;

public interface LicenceDtoConverter {
    
    /**
     * Convert licence response dto.
     *
     * @param licenceDto the licence dto
     * @return the licence data containing the licence details and the licence info
     */
    LicenceData convertLicenceDto(LicenceDto licenceDto);
}