package com.oup.eac.admin.beans;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;

import com.oup.eac.dto.CustomerSearchCriteria;

public class CustomerSearchCriteriaBean extends PageTrackingBean {

    private String firstName;
    
    private String familyName;
    
    private String username;
    
    private String email;
    
    private LocalDate createdDateFrom;
    
    private LocalDate createdDateTo;

	private String registrationData;
	
	private String externalId;
	
	private String totalCustomerFound;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(final String familyName) {
        this.familyName = familyName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }
    
    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

	public LocalDate getCreatedDateFrom() {
		return createdDateFrom;
	}

	public void setCreatedDateFrom(final LocalDate createdDateFrom) {
		this.createdDateFrom = createdDateFrom;
	}

	public LocalDate getCreatedDateTo() {
		return createdDateTo;
	}

	public void setCreatedDateTo(final LocalDate createdDateTo) {
		this.createdDateTo = createdDateTo;
	}

	public String getRegistrationData() {
		return registrationData;
	}

	public void setRegistrationData(final String registrationData) {
		this.registrationData = registrationData;
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(final String externalId) {
		this.externalId = externalId;
	}

	String[] getRegistrationDataAsKeywordList() {
		if (StringUtils.isNotBlank(registrationData)) {
			if (registrationData.contains(",")) {
				return splitRegistrationDataRemovingDuplicates();
			}
			return new String[] { registrationData };
		}
		return new String[0];
	}

	private String[] splitRegistrationDataRemovingDuplicates() {
		final Set<String> noDups = new HashSet<String>();
		final String[] values = registrationData.split(",");
		for (String value : values) {
			noDups.add(value.trim().toLowerCase());
		}
		final String[] newValues = new String[noDups.size()];
		noDups.toArray(newValues);
		return newValues;
	}

	public CustomerSearchCriteria toCustomerSearchCriteria() {
		CustomerSearchCriteria customerSearchCriteria = new CustomerSearchCriteria();
		customerSearchCriteria.setUsername(username);
		customerSearchCriteria.setEmail(email);
		customerSearchCriteria.setFirstName(firstName);
		customerSearchCriteria.setFamilyName(familyName);
		customerSearchCriteria.setExternalId(externalId);
		if (createdDateFrom != null) {
			customerSearchCriteria.setCreatedDateFrom(createdDateFrom.toDateTimeAtStartOfDay());
		}
		if (createdDateTo != null) {
			customerSearchCriteria.setCreatedDateTo(createdDateTo.toDateTimeAtStartOfDay());
		}
		customerSearchCriteria.setRegistrationDataKeywords(getRegistrationDataAsKeywordList());
		return customerSearchCriteria;
	}

	public String getTotalCustomerFound() {
		return totalCustomerFound;
	}

	public void setTotalCustomerFound(String totalCustomerFound) {
		this.totalCustomerFound = totalCustomerFound;
	}
}
