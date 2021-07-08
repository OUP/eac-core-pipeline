package com.oup.eac.dto;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import com.oup.eac.domain.ActivationCodeState;

public class ActivationCodeSearchCriteria implements Serializable {
    
    private ActivationCodeState activationCodeState = ActivationCodeState.ALL;
    private String code;
    private String productId;
    private String eacGroupId;
    
    public ActivationCodeSearchCriteria() {
        super();
    }

    public ActivationCodeSearchCriteria(String code, String productId, ActivationCodeState activationCodeState) {
        super();
        this.code = code;
        this.productId = productId;
        this.activationCodeState = activationCodeState;
    }

    public ActivationCodeSearchCriteria(String code, String productId, String eacGroupId, ActivationCodeState activationCodeState) {
        super();
        this.code = code;
        this.productId = productId;
        this.activationCodeState = activationCodeState;
        this.eacGroupId = eacGroupId;
    }
    
   	public String getEacGroupId() {
   		return eacGroupId;
   	}

   	public void setEacGroupId(String eacGroupId) {
   		this.eacGroupId = eacGroupId;
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
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
    	String santisedCode = code;
    	if (StringUtils.contains(santisedCode, "-")) {
    		santisedCode = StringUtils.replace(santisedCode, "-", "");
    	}
        this.code = santisedCode;
    }

    /**
     * @return the activationCodeState
     */
    public ActivationCodeState getActivationCodeState() {
        return activationCodeState;
    }

    /**
     * @param activationCodeState the activationCodeState to set
     */
    public void setActivationCodeState(ActivationCodeState activationCodeState) {
        this.activationCodeState = activationCodeState;
    }

    public static ActivationCodeSearchCriteria valueOf(String code, String productId, ActivationCodeState activationCodeState) {
        return new ActivationCodeSearchCriteria(code, productId, activationCodeState);
    }

    public static ActivationCodeSearchCriteria valueOf(String code, String productId, String eacGroupId, ActivationCodeState activationCodeState) {
        return new ActivationCodeSearchCriteria(code, productId, eacGroupId, activationCodeState);
    }
}
