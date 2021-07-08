package com.oup.eac.dto;

import java.io.Serializable;
import java.util.Locale;

import org.joda.time.DateTime;

import com.oup.eac.domain.User.EmailVerificationState;

public class ReportCriteria implements Serializable {
    
    private static String DATE_FORMAT = "dd/MM/yyyy";
    
    public static enum RegistrationSelectionType {
        ALL,
        PRODUCT,
        ACTIVATION_CODE;
    }
    public static enum RegistrationState {
    	DENIED,
    	PENDING,
    	ACTIVATED,
    	EXPIRED,
    	DISABLED
    }
    private String divisionId;
    private String productId;
    private String customerUsername;
    private DateTime fromDate;
    private DateTime toDate;
    private boolean pending;
    private boolean activated;
    private boolean denied;
    private boolean expired;
    private boolean disabled;
    private int maxResults;
    private RegistrationSelectionType registrationSelectionType;
    private int startIndex;
    
    private DateTime customerCreatedFromDate;
    private DateTime customerCreatedToDate;
    private String externalProductId;
	private EmailVerificationState eMailVarificationState;
    private Locale locale;
    private String timeZone;
    private DateTime lastLoginFromDate;
    private DateTime lastLoginToDate;
    private DateTime RegistrationCreatedFromDate;
    private DateTime RegistrationCreatedToDate;
    private String productExternalSystemIdType;
    private String platformCode;
    

	public ReportCriteria() {
        super();
        this.pending = true;
        this.activated = true;
        this.denied = true;
        this.disabled = true ;
        this.expired = true ;
        this.registrationSelectionType = RegistrationSelectionType.ALL;
    }
   
    public ReportCriteria(String divisionId, String platformCode, String productId, String customerUsername, DateTime fromDate, DateTime toDate, boolean pending, boolean activated, boolean denied, boolean expired, boolean disabled, int maxResults,
            DateTime customerCreatedFromDate,
            DateTime customerCreatedToDate,
            String externalProductId,
            EmailVerificationState eMailVarificationState,
            Locale locale,
            String timeZone,
            DateTime lastLoginFromDate,
            DateTime lastLoginToDate,
            DateTime RegistrationCreatedFromDate,
            DateTime RegistrationCreatedToDate,
            int startIndex,
            RegistrationSelectionType registrationSelectionType,
            String productExternalSystemIdType) {
        super();
        this.divisionId = divisionId;
        this.productId = productId;
        this.customerUsername = customerUsername;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.pending = pending;
        this.activated = activated;
        this.denied = denied;
        this.expired = expired ;
        this.disabled = disabled ;
        this.maxResults = maxResults;
        this.customerCreatedFromDate = customerCreatedFromDate;
        this.customerCreatedToDate = customerCreatedToDate;
        this.externalProductId = externalProductId;
        this.eMailVarificationState = eMailVarificationState;
        this.locale = locale;
        this.timeZone = timeZone;
        this.lastLoginFromDate =lastLoginFromDate;
        this.lastLoginToDate =lastLoginToDate;
        this.RegistrationCreatedFromDate= RegistrationCreatedFromDate;
        this.RegistrationCreatedToDate=RegistrationCreatedToDate;
        this.startIndex = startIndex;
        this.registrationSelectionType = registrationSelectionType;
        this.productExternalSystemIdType = productExternalSystemIdType;
        this.platformCode = platformCode;
    }
    
    
    public String getProductExternalSystemIdType() {
		return productExternalSystemIdType;
	}

	public void setProductExternalSystemIdType(
			String productExternalSystemIdType) {
		this.productExternalSystemIdType = productExternalSystemIdType;
	}

	/**
     * @return the divisionId
     */
    public String getDivisionId() {
        return divisionId;
    }

    /**
     * @param divisionId the divisionId to set
     */
    public void setDivisionId(String divisionId) {
        this.divisionId = divisionId;
    }

    /**
     * @return the productId
     */
    public String getProductId() {
        return productId;
    }

    /**
     * @param productId the productId to set
     */
    public void setProductId(String productId) {
        this.productId = productId;
    }

    /**
     * @return the customerUsername
     */
    public String getCustomerUsername() {
        return customerUsername;
    }

    /**
     * @param customerUsername the customerUsername to set
     */
    public void setCustomerUsername(String customerUsername) {
        this.customerUsername = customerUsername;
    }

    /**
     * @return the fromDate
     */
    public DateTime getFromDate() {
        return fromDate;
    }

    /**
     * @param fromDate the fromDate to set
     */
    public void setFromDate(DateTime fromDate) {
        this.fromDate = fromDate;
    }

    /**
     * @return the toDate
     */
    public DateTime getToDate() {
        return toDate;
    }

    /**
     * @param toDate the toDate to set
     */
    public void setToDate(DateTime toDate) {
        this.toDate = toDate;
    }

    /**
     * @return the pending
     */
    public boolean isPending() {
        return pending;
    }

    /**
     * @param pending the pending to set
     */
    public void setPending(boolean pending) {
        this.pending = pending;
    }

    /**
     * @return the activated
     */
    public boolean isActivated() {
        return activated;
    }

    /**
     * @param activated the activated to set
     */
    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    /**
     * @return the denied
     */
    public boolean isDenied() {
        return denied;
    }
    
    public boolean isNotDenied() {
        return !isDenied();
    }

    /**
     * @param denied the denied to set
     */
    public void setDenied(boolean denied) {
        this.denied = denied;
    }

    /**
     * @return the maxResults
     */
    public int getMaxResults() {
        return maxResults;
    }

    /**
     * @return the registrationSelectionType
     */
    public RegistrationSelectionType getRegistrationSelectionType() {
        return registrationSelectionType;
    }

    /**
     * @param registrationSelectionType the registrationSelectionType to set
     */
    public void setRegistrationSelectionType(RegistrationSelectionType registrationSelectionType) {
        this.registrationSelectionType = registrationSelectionType;
    }

    /**
     * @param maxResults the maxResults to set
     */
    public void setMaxResults(int maxResults) {
        this.maxResults = maxResults;
    }
    
    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public DateTime getCustomerCreatedFromDate() {
        return customerCreatedFromDate;
    }

    public void setCustomerCreatedFromDate(DateTime customerCreatedFromDate) {
        this.customerCreatedFromDate = customerCreatedFromDate;
    }

    public DateTime getCustomerCreatedToDate() {
        return customerCreatedToDate;
    }

    public void setCustomerCreatedToDate(DateTime customerCreatedToDate) {
        this.customerCreatedToDate = customerCreatedToDate;
    }

    public EmailVerificationState geteMailVarificationState() {
        return eMailVarificationState;
    }

    public void seteMailVarificationState(
            EmailVerificationState eMailVarificationState) {
        this.eMailVarificationState = eMailVarificationState;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public DateTime getLastLoginFromDate() {
        return lastLoginFromDate;
    }

    public void setLastLoginFromDate(DateTime lastLoginFromDate) {
        this.lastLoginFromDate = lastLoginFromDate;
    }

    public DateTime getLastLoginToDate() {
        return lastLoginToDate;
    }

    public void setLastLoginToDate(DateTime lastLoginToDate) {
        this.lastLoginToDate = lastLoginToDate;
    }

    public DateTime getRegistrationCreatedFromDate() {
        return RegistrationCreatedFromDate;
    }

    public void setRegistrationCreatedFromDate(DateTime registrationCreatedFromDate) {
        RegistrationCreatedFromDate = registrationCreatedFromDate;
    }

    public DateTime getRegistrationCreatedToDate() {
        return RegistrationCreatedToDate;
    }

    public void setRegistrationCreatedToDate(DateTime registrationCreatedToDate) {
        RegistrationCreatedToDate = registrationCreatedToDate;
    }

    public static ReportCriteria valueOf(String divisionId, String platformCode, String productId, String customerId, DateTime fromDate, DateTime toDate, boolean pending, boolean activated, boolean denied, boolean expired, boolean disabled, int maxResults,DateTime customerCreatedFromDate, DateTime customerCreatedToDate, String externalCustomerId, EmailVerificationState eMailVarificationState, Locale locale, String timeZone, DateTime lastLoginFromDate, DateTime lastLoginToDate, DateTime RegistrationCreatedFromDate, DateTime RegistrationCreatedToDate,int startIndex, RegistrationSelectionType registrationSelectionType, String productExternalSystemIdType) {
        return new ReportCriteria(divisionId, platformCode, productId, customerId, fromDate, toDate,  pending, activated, denied, expired, disabled, maxResults, customerCreatedFromDate, customerCreatedToDate, externalCustomerId, eMailVarificationState, locale, timeZone, lastLoginFromDate, lastLoginToDate, RegistrationCreatedFromDate, RegistrationCreatedToDate, startIndex,registrationSelectionType, productExternalSystemIdType);
    }

    public boolean isExpired() {
		return expired;
	}

	public void setExpired(boolean expired) {
		this.expired = expired;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public String getExternalProductId() {
		return externalProductId;
	}

	public void setExternalProductId(String externalProductId) {
		this.externalProductId = externalProductId;
	}

    public String getPlatformCode() {
		return platformCode;
	}

	public void setPlatformCode(String platformCode) {
		this.platformCode = platformCode;
	}

	/* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ReportCriteria ["
                + (divisionId != null ? "divisionId=" + divisionId + ", " : "")
                + (platformCode != null ? "platformCode=" + platformCode + ", " : "")
                + (productId != null ? "productId=" + productId + ", " : "")
                + (customerUsername != null ? "customerUsername="
                        + customerUsername + ", " : "")
                + (fromDate != null ? "fromDate=" + fromDate.toString(DATE_FORMAT) + ", " : "")
                + (toDate != null ? "toDate=" + toDate.toString(DATE_FORMAT) + ", " : "")
                + "pending="
                + pending
                + ", activated="
                + activated
                + ", denied="
                + denied
                + ", maxResults="
                + maxResults
                + (customerCreatedFromDate != null ? "customerCreatedFromDate=" + customerCreatedFromDate.toString(DATE_FORMAT) + ", " : "")                
                + (customerCreatedToDate != null ? "customerCreatedToDate=" + customerCreatedToDate.toString(DATE_FORMAT) + ", " : "")                
                + (externalProductId != null ? "externalProductId=" + externalProductId + ", " : "")                
                + (eMailVarificationState != null ? "eMailVarificationState=" + eMailVarificationState + ", " : "")
                + (locale != null ? "locale=" + locale + ", " : "")
                + (timeZone != null ? "timeZone=" + timeZone + ", " : "")
                + (lastLoginFromDate != null ? "lastLoginFromDate=" + lastLoginFromDate.toString(DATE_FORMAT) + ", " : "")              
                + (lastLoginToDate != null ? "lastLoginToDate=" + lastLoginToDate.toString(DATE_FORMAT) + ", " : "")              
                + (RegistrationCreatedFromDate != null ? "RegistrationCreatedFromDate=" + RegistrationCreatedFromDate.toString(DATE_FORMAT) + ", " : "")                
                + (RegistrationCreatedToDate != null ? "RegistrationCreatedToDate=" + RegistrationCreatedToDate.toString(DATE_FORMAT) + ", " : "")
                + (productExternalSystemIdType != null ? "productExternalSystemIdType=" + productExternalSystemIdType + ", " : "") 
                + ", startIndex="
                + startIndex
                + ","
                + (registrationSelectionType != null ? "registrationSelectionType="
                        + registrationSelectionType
                        : "") + "]";
    }
    
}
