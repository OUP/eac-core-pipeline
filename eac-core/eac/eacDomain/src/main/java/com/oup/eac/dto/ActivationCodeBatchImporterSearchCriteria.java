package com.oup.eac.dto;

import java.util.List;

import com.oup.eac.domain.ActivationCodeBatchImporterStatus.StatusCode;

public class ActivationCodeBatchImporterSearchCriteria {
    private String activationCodeBatchImporterId;
    private String productGroupName;
    private List<StatusCode> statues;
    private String activationCode;
    
    /**
     * @return the activationCodeBatchImporterId
     */
    public String getActivationCodeBatchImporterId() {
        return activationCodeBatchImporterId;
    }
    /**
     * @param activationCodeBatchImporterId the activationCodeBatchImporterId to set
     */
    public void setActivationCodeBatchImporterId(
            String activationCodeBatchImporterId) {
        this.activationCodeBatchImporterId = activationCodeBatchImporterId;
    }
    /**
     * @return the productGroupName
     */
    public String getProductGroupName() {
        return productGroupName;
    }
    /**
     * @param productGroupName the productGroupName to set
     */
    public void setProductGroupName(String productGroupName) {
        this.productGroupName = productGroupName;
    }
    /**
     * @return the statues
     */
    public List<StatusCode> getStatues() {
        return statues;
    }
    /**
     * @param statues the statues to set
     */
    public void setStatues(List<StatusCode> statues) {
        this.statues = statues;
    }
    /**
     * @return the activationCode
     */
    public String getActivationCode() {
        return activationCode;
    }
    /**
     * @param activationCode the activationCode to set
     */
    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }
}
