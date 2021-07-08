package com.oup.eac.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name="activation_code_batch_importer_status")
public class ActivationCodeBatchImporterStatus
{
    @Id
    @Column(name="activation_code_batch_importer_id", nullable=false)
    @GeneratedValue(generator="gen")
    @GenericGenerator(name="gen", strategy="foreign", parameters=@Parameter(name="property", value="activationCodeBatchImporter"))
    private String activationCodeBatchImporterId;
    
    @OneToOne
    @PrimaryKeyJoinColumn
    private ActivationCodeBatchImporter activationCodeBatchImporter;
    
    @Column(name="product_group_id")
    private String productGroupId;
    
    public static enum StatusCode {
        INSERTED,
        PRODUCT_GROUP_NOT_REQUIRED,
        PRODUCT_GROUP_CREATED,
        ERROR_CREATING_PRODUCT_GROUP,
        ACTIVATION_CODE_CREATED,
        ERROR_CREATING_ACTIVATION_CODE;
    }

    @Enumerated(EnumType.STRING)
    @Column(name="status", nullable=false)
    private StatusCode status;
    

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
     * @return the activationCodeBatchImporter
     */
    public ActivationCodeBatchImporter getActivationCodeBatchImporter() {
        return activationCodeBatchImporter;
    }

    /**
     * @param activationCodeBatchImporter the activationCodeBatchImporter to set
     */
    public void setActivationCodeBatchImporter(
            ActivationCodeBatchImporter activationCodeBatchImporter) {
        this.activationCodeBatchImporter = activationCodeBatchImporter;
    }

    /**
     * @return the productGroupId
     */
    public String getProductGroupId() {
        return productGroupId;
    }

    /**
     * @param productGroupId the productGroupId to set
     */
    public void setProductGroupId(String productGroupId) {
        this.productGroupId = productGroupId;
    }

    /**
     * @return the status
     */
    public StatusCode getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(StatusCode status) {
        this.status = status;
    }
}
