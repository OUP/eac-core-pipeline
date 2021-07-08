package com.oup.eac.domain;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

/**
 * Abstract class for all domain classes to extend.
 * 
 * @author harlandd
 */
@MappedSuperclass
public abstract class CreatedAudit extends BaseDomainObject {

    @Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
    @Column(nullable = false, updatable = false)
    private DateTime createdDate;

    /**
     * @return the createdDate
     */
	public DateTime getCreatedDate() {
        return createdDate;
    }

    /**
     * @param createdDate the createdDate to set
     */
	public void setCreatedDate(final DateTime createdDate) {
        this.createdDate = createdDate;
    }

}
