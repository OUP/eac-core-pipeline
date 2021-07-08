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
public abstract class UpdatedAudit extends CreatedAudit {

    @Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
    @Column(nullable = true, updatable = true)
    private DateTime updatedDate;

	/**
	 * @return the updatedDate
	 */
	public DateTime getUpdatedDate() {
		return updatedDate;
	}

	/**
	 * @param updatedDate the updatedDate to set
	 */
	public void setUpdatedDate(final DateTime updatedDate) {
		this.updatedDate = updatedDate;
	}

}
