package com.oup.eac.dto;

import java.io.Serializable;

public class ActivationCodeReportDto implements Serializable {
    
    private String id;

    private String code;
    
    private String productName;
    
    private Integer allowedUsage;
    
    private Integer actualUsage;
    
    private String eacGroupName;
    
	public ActivationCodeReportDto(String id, String code, String productName,
            Integer allowedUsage, Integer actualUsage, String eacGroupName) {
        super();
        this.id = id;
        this.code = code;
        this.productName = productName;
        this.allowedUsage = allowedUsage;
        this.actualUsage = actualUsage;
        this.eacGroupName = eacGroupName;
    }

    /**
     * @return the allowedUsage
     */
    public Integer getAllowedUsage() {
        return allowedUsage;
    }

    /**
     * @return the actualUsage
     */
    public Integer getActualUsage() {
        return actualUsage;
    }

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @return the productName
     */
    public String getProductName() {
        return productName;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @return the available
     */
    public boolean isAvailable() {
        return (allowedUsage.intValue() - actualUsage.intValue()) > 0;
    }
    
    public String getEacGroupName() {
		return eacGroupName;
	}
    
}
