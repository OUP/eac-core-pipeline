package com.oup.eac.dto;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Contains licence information exchanged between eac and erights. 
 * 
 * Also contains some immutable values calculated by erights.
 * 
 * @author Ian Packard
 *
 */
public class LicenceDto extends BaseLicenceDto {

	@DateTimeFormat (pattern="dd/MM/yyyy")
	private LocalDate startDate;
	
	@DateTimeFormat (pattern="dd/MM/yyyy")
	private LocalDate endDate;
	
	//for holding exact start date & time as held by atypon
	private DateTime startDateTime;
    
    //for holding exact end date & time as held by atypon
    private DateTime endDateTime;
    
    //for holding exact created date & time as held by atypon
    private DateTime createdDate;
    
    //for holding exact updated date & time as held by atypon
    private DateTime updatedDate;
    
    private String activationCode;

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

	
	public LicenceDto(final String licenseId) {
		super(licenseId);
	}
	
	/**
	 * Licence used for sending data to atypon.
	 */
	public LicenceDto() {
		super();
	}
	
	/**
	 * Creates licence based on immutable licence information provided by erights.
	 * 
	 * @param daysRemaining
	 * @param expired
	 * @param active
	 */
	public LicenceDto(final String licenseId, final DateTime expiryDateAndTime, final boolean expired, final boolean active, 
			final boolean completed, final boolean awaitingValidation, final boolean denied) {
		super(licenseId, expiryDateAndTime, expired, active, completed, awaitingValidation, denied);
	}
	
	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}
	
    public String getActivationCode() {
		return activationCode;
	}

	public void setActivationCode(String activationCode) {
		this.activationCode = activationCode;
	}

	public DateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(DateTime createdDate) {
		this.createdDate = createdDate;
	}

	public DateTime getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(DateTime updatedDate) {
		this.updatedDate = updatedDate;
	}

	/**
     * Sort by expiry date date.
     * Used to sort Licence Dtos by licence expiry date for display in noActiveLicence.jsp 
     *
     * @param licenceDtos the licence dtos
     * @param ascending the ascending
     */
    public static void sortByExpiryDateDate(List<LicenceDto> licenceDtos, boolean ascending) {
        Comparator<LicenceDto> sorter = new Comparator<LicenceDto>() {

            @Override
            public int compare(LicenceDto dto1, LicenceDto dto2) {
                
                if(dto1.hasInfo && dto2.hasInfo){
                    if(dto1.isExpired() && !dto2.isExpired()){
                        return 1;
                    }
                
                    if(!dto1.isExpired() && dto2.isExpired()){
                        return -1;
                    }
                }
                
                DateTime t1 = getSortTime(dto1);
                DateTime t2 = getSortTime(dto2);

                if (t1 == null && t2 == null) {
                    return 0;
                } else if (t1 == null ) {
                    return 1;
                } else if (t2 == null ) {
                    return -1;
                } else {
                    return t1.compareTo(t2);
                }
            }

            private DateTime getSortTime(LicenceDto dto) {
                if (dto.hasInfo) {
                    if (dto.getExpiryDateAndTime() != null) {
                        return dto.getExpiryDateAndTime();
                    }
                }
                if (dto.getEndDateTime() != null) {
                    return dto.getEndDateTime();
                }
                return null;
            }

        };
        if (ascending) {
            Collections.sort(licenceDtos, sorter);
        } else {
            Collections.sort(licenceDtos, Collections.reverseOrder(sorter));
        }
    }

}
