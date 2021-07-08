package com.oup.eac.dto.licence;

import org.joda.time.DateTime;

import com.oup.eac.dto.LicenceDto;


/**
 * The Interface LicenceDescriptionSource.
 */
public interface LicenceDescriptionGenerator {
    
    public String getLicenceDescription(LicenceDto licenceDto, DateTime now);
}
