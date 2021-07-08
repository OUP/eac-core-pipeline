package com.oup.eac.dto;

import org.joda.time.DateTime;

/**
 * Version of LicenceDto with DateTimes rather than just Dates for licence
 * starts and licence ends. This helps with Orcs migration.
 * 
 * @author David Hay
 * 
 */
public class LicenceDtoDateTime extends BaseLicenceDto {

    private DateTime startDateTime;

    private DateTime endDateTime;

    public LicenceDtoDateTime(LicenceDto licenceDto, DateTime startDateTime, DateTime endDateTime) {
        super(licenceDto.getLicenseId(), licenceDto.getExpiryDateAndTime(), licenceDto.isExpired(), licenceDto.isActive(), licenceDto.isCompleted(), licenceDto.isAwaitingValidation(),
        		licenceDto.isDenied());
        setEnabled(licenceDto.isEnabled());
        setLicenceTemplateId(licenceDto.getLicenceTemplateId());
        setProductIds(licenceDto.getProductIds());
        setLicenceDetail(licenceDto.getLicenceDetail());
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    public DateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(DateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public DateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(DateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

}
