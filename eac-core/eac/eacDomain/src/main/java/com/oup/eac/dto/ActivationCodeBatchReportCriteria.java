package com.oup.eac.dto;

import java.io.Serializable;

public class ActivationCodeBatchReportCriteria implements Serializable {
    
    private String divisionId;
    private String productId;
    private String eacGroupId;
    private String batchId;
    private int maxResults;
    private String event;
    private String adminEmail;
    private String platformCode;
    
    public ActivationCodeBatchReportCriteria() {
        super();
    }

    public ActivationCodeBatchReportCriteria(String divisionId, String productId, String batchId, int maxResults) {
        super();
        this.divisionId = divisionId;
        this.productId = productId;
        this.batchId = batchId;
        this.maxResults = maxResults;
    }

    public ActivationCodeBatchReportCriteria(String divisionId, String productId, String eacGroupId, String batchId, int maxResults) {
        super();
        this.divisionId = divisionId;
        this.productId = productId;
        this.eacGroupId = eacGroupId;
        this.batchId = batchId;
        this.maxResults = maxResults;
    }
    
    public ActivationCodeBatchReportCriteria(String divisionId, String platformCode, String productId, String eacGroupId, String batchId, int maxResults) {
        super();
        this.divisionId = divisionId;
        this.platformCode = platformCode;
        this.productId = productId;
        this.eacGroupId = eacGroupId;
        this.batchId = batchId;
        this.maxResults = maxResults;
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
     * @return the eacGroupId
     */
    public String getEacGroupId() {
        return eacGroupId;
    }

    /**
     * @param eacGroupId the eacGroupId to set
     */
    public void setEacGroupId(String eacGroupId) {
        this.eacGroupId = eacGroupId;
    }

    /**
     * @return the batchId
     */
    public String getBatchId() {
        return batchId;
    }

    /**
     * @param batchId the batchId to set
     */
    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    /**
     * @return the maxResults
     */
    public int getMaxResults() {
        return maxResults;
    }

    /**
     * @param maxResults the maxResults to set
     */
    public void setMaxResults(int maxResults) {
        this.maxResults = maxResults;
    }
    
    public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getAdminEmail() {
		return adminEmail;
	}

	public void setAdminEmail(String adminEmail) {
		this.adminEmail = adminEmail;
	}

    public String getPlatformCode() {
		return platformCode;
	}

	public void setPlatformCode(String platformCode) {
		this.platformCode = platformCode;
	}

	public static ActivationCodeBatchReportCriteria valueOf(String divisionId, String productId, String batchId, int maxResults) {
        return new ActivationCodeBatchReportCriteria(divisionId, productId, batchId, maxResults);
    }
	
	public static ActivationCodeBatchReportCriteria valueOf(String divisionId, String platformCode, String productId, String batchId, int maxResults) {
        return new ActivationCodeBatchReportCriteria(divisionId, platformCode, productId, batchId, maxResults);
    }
    

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ActivationCodeReportCriteria ["
                + (divisionId != null ? "divisionId=" + divisionId + ", " : "")
                + (platformCode != null ? "platformCode=" + platformCode + ", " : "")
                + (eacGroupId != null ? "eacGroupId=" + eacGroupId + ", " : "")
                + (productId != null ? "productId=" + productId + ", " : "")
                + (batchId != null ? "batchName=" + batchId + ", " : "")
                + "maxResults=" + maxResults + "]";
    }

}
