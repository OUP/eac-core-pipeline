package com.oup.eac.ws.v2.service.impl.entitlements;

import org.exolab.castor.types.Date;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import com.oup.eac.domain.RollingLicenceTemplate.RollingBeginOn;
import com.oup.eac.domain.RollingLicenceTemplate.RollingUnitType;
import com.oup.eac.dto.LicenceDetailDto;
import com.oup.eac.dto.LicenceDto;
import com.oup.eac.dto.RollingLicenceDetailDto;
import com.oup.eac.dto.StandardConcurrentLicenceDetailDto;
import com.oup.eac.dto.UsageLicenceDetailDto;
import com.oup.eac.ws.v2.binding.common.ConcurrencyLicenceDetails;
import com.oup.eac.ws.v2.binding.common.ExtendedLicenceDetails;
import com.oup.eac.ws.v2.binding.common.LicenceDetails;
import com.oup.eac.ws.v2.binding.common.LicenceInfo;
import com.oup.eac.ws.v2.binding.common.RollingLicenceDetail;
import com.oup.eac.ws.v2.binding.common.RollingLicenceInfo;
import com.oup.eac.ws.v2.binding.common.UsageLicenceDetails;
import com.oup.eac.ws.v2.binding.common.UsageLicenceInfo;
import com.oup.eac.ws.v2.binding.common.types.BeginEnum;
import com.oup.eac.ws.v2.binding.common.types.UnitEnum;
import com.oup.eac.ws.v2.service.entitlements.LicenceData;
import com.oup.eac.ws.v2.service.entitlements.LicenceDtoConverter;

public class LicenceDtoConverterImpl implements LicenceDtoConverter {

    /**
     * {@inheritDoc}
     */
    public final LicenceData convertLicenceDto(final LicenceDto licenceDto) {
        LicenceData result = new LicenceData();
        if (licenceDto == null) {
            return result;
        }
        
        LicenceDetails detail = new LicenceDetails();
        licenceDto.getEndDate();
        detail.setEnabled(licenceDto.isEnabled());
        detail.setStartDate(getCastorDate(licenceDto.getStartDate()));
        detail.setEndDate(getCastorDate(licenceDto.getEndDate()));
        
        LicenceDetailDto dtoDetails = licenceDto.getLicenceDetail();
        ExtendedLicenceDetails extended = getExtended(dtoDetails);
        detail.setExtendedDetails(extended);
        result.setDetail(detail);

        final LicenceInfo info;
        if (dtoDetails != null) {
	        switch (dtoDetails.getLicenceType()) {
	        case USAGE:
	        	UsageLicenceInfo usageInfo = new UsageLicenceInfo();
	            UsageLicenceDetailDto usageDto = (UsageLicenceDetailDto)dtoDetails;
	            usageInfo.setUsagesRemaining(usageDto.getUsagesRemaining());
	            info = usageInfo;
	            break;
	        case ROLLING:
	    		RollingLicenceInfo rollingInfo = new RollingLicenceInfo();
	    		RollingLicenceDetailDto rollingDto = (RollingLicenceDetailDto)dtoDetails;
	    		if (rollingDto.getFirstUse() != null) {
	    			rollingInfo.setFirstUsedDate(rollingDto.getFirstUse().toDateTime().toDate());
	    		}
	    		info = rollingInfo;
	    		break;
	        case STANDARD:
	        case CONCURRENT:
	    	default:
	    		info = new LicenceInfo();
	    		break;
	        }
        } else {
        	info = new LicenceInfo();
        }
        
        result.setInfo(info);
        info.setActive(licenceDto.isActive());
        info.setExpired(licenceDto.isExpired());
        if (licenceDto.getExpiryDateAndTime() != null) {
        	info.setExpiryDateAndTime(licenceDto.getExpiryDateAndTime().toDateTime().toDate());
        }
        return result;
    }

    /**
     * Gets the extended.
     *
     * @param licenceDto the licence dto
     * @return the extended
     */
    private ExtendedLicenceDetails getExtended(final LicenceDetailDto licenceDto) {
        if(licenceDto == null){
            return null;
        }
        final ExtendedLicenceDetails result;
        switch (licenceDto.getLicenceType()) {
        case ROLLING:            
            assert (licenceDto instanceof RollingLicenceDetailDto);
            result = getRolling((RollingLicenceDetailDto) licenceDto);            
            break;
        case CONCURRENT:
            assert (licenceDto instanceof StandardConcurrentLicenceDetailDto);            
            result = getConcurrent((StandardConcurrentLicenceDetailDto) licenceDto);
            break;
        case USAGE:
            assert (licenceDto instanceof UsageLicenceDetailDto);                        
            result = getUsage((UsageLicenceDetailDto) licenceDto);
            break;
        case STANDARD:
        default:
            result = null;
        }        
        return result;
    }

    /**
     * Gets the usage.
     *
     * @param licenceDto the licence dto
     * @return the usage
     */
    private ExtendedLicenceDetails getUsage(final UsageLicenceDetailDto licenceDto) {
        ExtendedLicenceDetails ext = new ExtendedLicenceDetails();
        UsageLicenceDetails result = new UsageLicenceDetails();
        result.setAllowedUsages(licenceDto.getAllowedUsages());
        ext.setUsageLicence(result);
        return ext;
    }

    /**
     * Gets the concurrent.
     *
     * @param licenceDto the licence dto
     * @return the concurrent
     */
    private ExtendedLicenceDetails getConcurrent(final StandardConcurrentLicenceDetailDto licenceDto) {
        ExtendedLicenceDetails ext = new ExtendedLicenceDetails();
        ConcurrencyLicenceDetails result = new ConcurrencyLicenceDetails();
        result.setConcurrency(licenceDto.getUserConcurrency());
        ext.setConcurrentLicence(result);
        return ext;
    }

    /**
     * Gets the rolling.
     *
     * @param licenceDto the licence dto
     * @return the rolling
     */
    private ExtendedLicenceDetails getRolling(final RollingLicenceDetailDto licenceDto) {
        ExtendedLicenceDetails ext = new ExtendedLicenceDetails();
        RollingLicenceDetail result = new RollingLicenceDetail();
        result.setBeginOn(getBeginOn(licenceDto.getBeginOn()));        
        result.setPeriodUnit(getUnitEnum(licenceDto.getUnitType()));        
        result.setPeriodValue(licenceDto.getTimePeriod());
        ext.setRollingLicence(result);
        return ext;
        
    }
    
    private BeginEnum getBeginOn(RollingBeginOn beginOn) {
        if(beginOn == null){
            return null;
        }else{
            return BeginEnum.valueOf(beginOn.name());
        }
    }

    /**
     * Gets the unit enum.
     *
     * @param rollingUnitType the rolling unit type
     * @return the unit enum
     */
    private UnitEnum getUnitEnum(final RollingUnitType rollingUnitType) {
        if (rollingUnitType == null) {
            return null;
        }else{
            return UnitEnum.valueOf(rollingUnitType.name());
        }

    }

    /**
     * Gets the castor date.
     *
     * @param dt the dt
     * @return the castor date
     */
    private Date getCastorDate(final LocalDate ld) {
        if (ld == null) {
            return null;
        }
        DateTime dt = ld.toDateTimeAtStartOfDay();
        org.exolab.castor.types.Date result = new org.exolab.castor.types.Date(dt.toDate());
        return result;
    }
}
