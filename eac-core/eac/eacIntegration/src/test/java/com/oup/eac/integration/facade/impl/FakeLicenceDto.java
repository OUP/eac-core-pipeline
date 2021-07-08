package com.oup.eac.integration.facade.impl;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import com.oup.eac.domain.ConcurrentLicenceTemplate;
import com.oup.eac.domain.LicenceTemplate;
import com.oup.eac.domain.LicenceTemplate.LicenceType;
import com.oup.eac.domain.RollingLicenceTemplate;
import com.oup.eac.domain.RollingLicenceTemplate.RollingBeginOn;
import com.oup.eac.domain.RollingLicenceTemplate.RollingUnitType;
import com.oup.eac.domain.UsageLicenceTemplate;
import com.oup.eac.dto.LicenceDetailDto;
import com.oup.eac.dto.LicenceDto;
import com.oup.eac.dto.RollingLicenceDetailDto;
import com.oup.eac.dto.StandardConcurrentLicenceDetailDto;
import com.oup.eac.dto.UsageLicenceDetailDto;

/**
 * Licence used for testing. Delegates to a real licence but performs calculations
 * for "active" and "expiry date" on the fly. This mimics real world erights which
 * would calculate these values every time a licence is obtained.
 *  
 * @author Ian Packard
 *
 */
public class FakeLicenceDto extends LicenceDto {

	private final LicenceDto delegate;
	
	private LocalDateTime creationDateTime;
	
	protected FakeLicenceDto(Integer id, final LicenceTemplate licenceTemplate, List<String> productIds, boolean enabled) {
		
		LicenceDto licenceDto = new LicenceDto(String.valueOf(id), null, false, false, true, false, false); 
    	licenceDto.setEnabled(enabled);
    	licenceDto.setStartDate(licenceTemplate.getStartDate());
    	licenceDto.setEndDate(licenceTemplate.getEndDate());
    	licenceDto.setProductIds(productIds);
    	
    	switch (licenceTemplate.getLicenceType()) {
    	case CONCURRENT:
    		ConcurrentLicenceTemplate clt = (ConcurrentLicenceTemplate)licenceTemplate;
    		licenceDto.setLicenceDetail(new StandardConcurrentLicenceDetailDto(clt.getTotalConcurrency(), clt.getUserConcurrency()));
    		break;
    	case STANDARD:
    		//no detail for standard
    		break;
    	case ROLLING:
    		RollingLicenceTemplate rlt = (RollingLicenceTemplate)licenceTemplate;
    		licenceDto.setLicenceDetail(new RollingLicenceDetailDto(rlt.getBeginOn(), rlt.getUnitType(), rlt.getTimePeriod(), null));
    		break;
    	case USAGE:
    		UsageLicenceTemplate ult = (UsageLicenceTemplate)licenceTemplate;
    		UsageLicenceDetailDto detail = new UsageLicenceDetailDto(ult.getAllowedUsages());
    		detail.setAllowedUsages(ult.getAllowedUsages());
    		licenceDto.setLicenceDetail(detail);
    		
    		break;
    	}
		
		this.delegate = licenceDto;
		
		this.creationDateTime = new LocalDateTime();
		 
	}
	
protected FakeLicenceDto(Integer id, Integer productId, boolean enabled) {
		
		LicenceDto licenceDto = new LicenceDto(String.valueOf(id), null, false, false, true, false, false); 
    	licenceDto.setEnabled(enabled);
    	licenceDto.setStartDate(new LocalDate());
    	licenceDto.setEndDate(new LocalDate().plusDays(3));
    	List<String> productIds = new ArrayList<String>();
    	productIds.add(String.valueOf(productId));
    	licenceDto.setProductIds(productIds);
		this.delegate = licenceDto;
		this.creationDateTime = new LocalDateTime();
		
		 
	}
	
	private LocalDateTime calculateExpiryDateTime(final LicenceDetailDto licenceDetailDto) {
		if (licenceDetailDto != null && licenceDetailDto.getLicenceType().equals(LicenceType.ROLLING)) {
			RollingExpiryDateResolver resolver = new RollingExpiryDateResolver(this);
			return resolver.getRollingExpiryDate();
		} else if (getEndDate() != null) {
			return new LocalDateTime(getEndDate().toDateTimeAtCurrentTime());
		}
		return null;
	}
	
	/*@Override
	public Integer getErightsId() {
		return delegate.getErightsId();
	}

	@Override
	public void setErightsId(Integer erightsId) {
		delegate.setErightsId(erightsId);
	}*/

	@Override
	public boolean isEnabled() {
		return delegate.isEnabled();
	}

	@Override
	public void setEnabled(boolean enabled) {
 
		delegate.setEnabled(enabled);
	}

	@Override
	public LocalDate getStartDate() {
		return delegate.getStartDate();
	}

	@Override
	public void setStartDate(LocalDate startDate) {
		delegate.setStartDate(startDate);
	}

	@Override
	public LocalDate getEndDate() {
		return delegate.getEndDate();
	}

	@Override
	public void setEndDate(LocalDate endDate) {
		delegate.setEndDate(endDate);
	}

	@Override
	public Integer getLicenceTemplateId() {
		return delegate.getLicenceTemplateId();
	}

	@Override
	public void setLicenceTemplateId(Integer licenceTemplateId) {
		delegate.setLicenceTemplateId(licenceTemplateId);
	}

	@Override
	public List<String> getProductIds() {
		return delegate.getProductIds();
	}

	@Override
	public void setProductIds(List<String> productIds) {
		delegate.setProductIds(productIds);
	}

	@Override
	public LicenceDetailDto getLicenceDetail() {
		return delegate.getLicenceDetail();
	}

	@Override
	public void setLicenceDetail(LicenceDetailDto licenceDetail) {
		delegate.setLicenceDetail(licenceDetail);
	}

	@Override
	public boolean isActive() {
		return !isExpired() && delegate.isEnabled();
	}

	@Override
	public boolean isExpired() {
		LocalDateTime expiryDate = getExpiryDateAndTime().toLocalDateTime();
		if (expiryDate == null) return false;
		return expiryDate.isBefore(new LocalDateTime());
	}
	
	@Override
	public DateTime getExpiryDateAndTime() {
		return calculateExpiryDateTime(delegate.getLicenceDetail()).toDateTime();
	}
	
	public LocalDateTime getCreationDate() {
		return creationDateTime;
	}
	
	
	public static class RollingExpiryDateResolver {
		
		private final int licencePeriod;
		private final RollingUnitType licencePeriodUnit;
		private final RollingBeginOn beginOn;
		
		private final LocalDate startDate;
		private final LocalDate endDate;
		private final LocalDateTime dateCreated;
		private final LocalDateTime firstUse;
		
		private final LocalDateTime now;
		
		public RollingExpiryDateResolver(final FakeLicenceDto licence) {
			this.licencePeriod = ((RollingLicenceDetailDto)licence.getLicenceDetail()).getTimePeriod();
			this.licencePeriodUnit = ((RollingLicenceDetailDto)licence.getLicenceDetail()).getUnitType();
			this.beginOn = ((RollingLicenceDetailDto)licence.getLicenceDetail()).getBeginOn();
			
			this.startDate = licence.getStartDate();
			this.endDate = licence.getEndDate();
			this.dateCreated = licence.getCreationDate();
			this.firstUse = ((RollingLicenceDetailDto)licence.getLicenceDetail()).getFirstUse().toLocalDateTime();
			this.now = new LocalDateTime();
		}
		
		/**
		 * Return expiry date based on rolling licence type. Date may be null.
		 * 
		 * @return
		 */
		public LocalDateTime getRollingExpiryDate() {
			
			if (RollingBeginOn.CREATION.equals(beginOn)) {
				//CREATION
				return getRollingExpiryForCreation();
			} else if (RollingBeginOn.FIRST_USE.equals(beginOn)){
				//BEGIN_ON
				return getRollingExpiryForFirstUse();
			} else {
				throw new RuntimeException("Unknown licence begin_on!");
			}
		}
		
		private LocalDateTime getRollingExpiryForFirstUse() {
			//FIRST_USE
			//start date optional
			//end date optional
			//first use date optional
			//licence period required
			if (firstUse == null) {
				//No
				if (endDate == null) {
					//No
					return null;
				} else {
					//Yes
					if (startDate == null) {
						//No
						return null;
					} else {
						//Yes
						//Start date in future?
						if (startDate.isAfter(now)) {
							//Yes
							return endDateLessThanStartDatePlusLicencePeriod();
						} else {
							//No
							return endDateLessThanNowPlusLicencePeriod();
						}
					}
				}
			} else {
				//Yes
				if (endDate == null) {
					//No
					//expiry date = first use + licence period
					return addLicencePeriodToDate(firstUse);
				} else {
					//Yes
					return endDateAfterFirstUsePlusLicencePeriod();
				}
			}
		}
		
		private LocalDateTime endDateAfterFirstUsePlusLicencePeriod() {
			LocalDateTime firstUsePlusLicencePeriod = addLicencePeriodToDate(firstUse);
			if (endDate.isAfter(firstUsePlusLicencePeriod)) {
				//Yes
				return firstUsePlusLicencePeriod;
			} else {
				//No
				return new LocalDateTime(endDate);
			}
		}

		private LocalDateTime endDateLessThanNowPlusLicencePeriod() {
			if (endDate.isBefore(addLicencePeriodToDate(now))) {
				return new LocalDateTime(endDate);				
			} else {
				return null;
			}
		}

		private LocalDateTime addLicencePeriodToDate(final LocalDateTime dateTime) {
			switch (licencePeriodUnit)
			{
				case YEAR:
					return dateTime.plusYears(licencePeriod);
				case MONTH:
					return dateTime.plusMonths(licencePeriod);
				case WEEK:
					return dateTime.plusWeeks(licencePeriod);
				case DAY:
					return dateTime.plusDays(licencePeriod);
				case HOUR:
					return dateTime.plusHours(licencePeriod);
				case MINUTE:
					return dateTime.plusMinutes(licencePeriod);
				case SECOND:
					return dateTime.plusSeconds(licencePeriod);
				case MILLISECOND:
					return dateTime.plusMillis(licencePeriod);
				default:
					throw new RuntimeException("Unknown licencePeriodUnit");
			}
		}

		private LocalDateTime getRollingExpiryForCreation() {
			
			//creation date required
			//startdate optional
			//end date optional
			//licence period required
			
			if (endDate == null) {
				//No
				if (startDate == null) {
					//No
					//expiry date = creation date + licence period
					return addLicencePeriodToDate(dateCreated);
				} else {
					//Yes
					if (startDate.isAfter(now)) {
						//Yes
						//expiry date = start date + licence period
						return addLicencePeriodToDate(new LocalDateTime(startDate));
					} else {
						//No
						//expiry date = creation date + licence period
						return addLicencePeriodToDate(dateCreated);						
					}
				}
			} else {
				//Yes
				if (startDate == null) {
					//No
					return noStartDateOrStartDateNotInFuture();
				} else {
					//Yes
					if (startDate.isAfter(now)) {
						//Yes
						return startDateInFuture();
					} else {
						//No
						return noStartDateOrStartDateNotInFuture();
					}
				}
			}		
		}
		
		private LocalDateTime endDateLessThanStartDatePlusLicencePeriod() {
			if (endDate.isBefore(addLicencePeriodToDate(new LocalDateTime(startDate)))) {
				//Yes
				return new LocalDateTime(endDate);
			} else {
				//No
				return null;
			}
		}
		
		private LocalDateTime startDateInFuture() {
			LocalDateTime startDatePlusLicencePeriod = addLicencePeriodToDate(new LocalDateTime(startDate));
			if (endDate.isAfter(startDatePlusLicencePeriod)) {
				//Yes
				return startDatePlusLicencePeriod;
			} else {
				//No
				return new LocalDateTime(endDate);
			}
		}

		private LocalDateTime noStartDateOrStartDateNotInFuture() {
			LocalDateTime createdPlusLicencePeriod = addLicencePeriodToDate(dateCreated);
			if (endDate.isAfter(createdPlusLicencePeriod)) {
				//Yes
				return addLicencePeriodToDate(dateCreated);
			} else {
				//No
				return new LocalDateTime(endDate);
			}
		}
	}
}
