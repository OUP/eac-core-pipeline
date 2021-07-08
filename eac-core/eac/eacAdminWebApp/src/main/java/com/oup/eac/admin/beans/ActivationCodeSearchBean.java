package com.oup.eac.admin.beans;

import com.oup.eac.domain.ActivationCodeState;
import com.oup.eac.dto.ActivationCodeSearchCriteria;

public class ActivationCodeSearchBean extends PageTrackingBean {

    private ActivationCodeState activationCodeState = ActivationCodeState.ALL;
	private String code;
    private String productId;
    private String eacGroupId;
    
    

	public String getEacGroupId() {
		return eacGroupId;
	}

	public void setEacGroupId(String eacGroupId) {
		this.eacGroupId = eacGroupId;
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
        this.code = code;
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


    public ActivationCodeSearchCriteria toActivationCodeSearchCriteria() {
        ActivationCodeSearchCriteria searchCriteria = new ActivationCodeSearchCriteria();
		searchCriteria.setProductId(getProductId());
		searchCriteria.setActivationCodeState(getActivationCodeState());
		searchCriteria.setCode(getCode());
		searchCriteria.setEacGroupId(getEacGroupId());
		return searchCriteria;
	}
}
