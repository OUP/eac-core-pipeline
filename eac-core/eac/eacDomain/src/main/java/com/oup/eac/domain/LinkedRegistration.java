package com.oup.eac.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.apache.log4j.Logger;
import org.hibernate.annotations.Index;

@Entity
public class LinkedRegistration extends BaseDomainObject {
    
    protected static final Logger LOG = Logger.getLogger(Registration.class);

    @Index(name = "linked_registration_idx")
    @JoinColumn(name="registration_id", nullable=false)
    @ManyToOne(fetch=FetchType.LAZY)
    private Registration<? extends RegistrationDefinition> registration;
    
    @JoinColumn(name="linked_product_id", nullable=false)
    @ManyToOne(fetch=FetchType.LAZY)
    private LinkedProduct linkedProduct;

    @Column(name="erights_licence_id", nullable=false)
    private Integer erightsLicenceId;

    public Registration<? extends RegistrationDefinition> getRegistration() {
        return registration;
    }

    public void setRegistration(Registration<? extends RegistrationDefinition> registration) {
        this.registration = registration;
    }

    public LinkedProduct getLinkedProduct() {
        return linkedProduct;
    }

    public void setLinkedProduct(LinkedProduct linkedProduct) {
        this.linkedProduct = linkedProduct;
    }

    public Integer getErightsLicenceId() {
        return erightsLicenceId;
    }

    public void setErightsLicenceId(Integer eRightsLicenceId) {
        if (this.erightsLicenceId != null) {
            String msg = String.format("Orphan Licence : LinkedRegistationId[%s] : overwriting old erightsLicenceId[%s] with new erightsLicenceId[%s]", this.getId(), this.erightsLicenceId, eRightsLicenceId);
            LOG.error(msg);
        }
        this.erightsLicenceId = eRightsLicenceId;
    }

}
