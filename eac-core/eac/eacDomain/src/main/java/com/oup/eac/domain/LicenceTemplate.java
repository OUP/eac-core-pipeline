/**
 * 
 */
package com.oup.eac.domain;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author harlandd
 * 
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "LICENCE_TYPE", discriminatorType = DiscriminatorType.STRING)
public abstract class LicenceTemplate extends UpdatedAudit {

    public static enum LicenceType {
    	
        STANDARD(StandardLicenceTemplate.class), 
        CONCURRENT(ConcurrentLicenceTemplate.class), 
        USAGE(UsageLicenceTemplate.class), 
        ROLLING(RollingLicenceTemplate.class);
        
        private Class<? extends LicenceTemplate> licenceClass;

		private LicenceType(Class<? extends LicenceTemplate> licenceClass) {
			this.licenceClass = licenceClass;
		}

		/**
		 * @return the licenceClass
		 */
		public Class<? extends LicenceTemplate> getLicenceClass() {
			return licenceClass;
		}
        
    }

    @Type(type = "org.joda.time.contrib.hibernate.PersistentLocalDate")
    @DateTimeFormat(pattern="dd/MM/yyyy") 
    private LocalDate startDate;

    @Type(type = "org.joda.time.contrib.hibernate.PersistentLocalDate")
    @DateTimeFormat(pattern="dd/MM/yyyy")
    private LocalDate endDate;

    public LicenceTemplate() {
		super();
	}

	public LicenceTemplate(final LicenceTemplate licenceTemplate) {
		super();
		this.setStartDate(licenceTemplate.getStartDate());
		this.setEndDate(licenceTemplate.getEndDate());
	}

	/**
     * @return the licence type
     */
    @Index (name = "licence_template_type_idx")
    public abstract LicenceType getLicenceType();

    /**
     * @return the startDate
     */
	public LocalDate getStartDate() {
        return startDate;        
    }

    /**
     * @param startDate
     *            the startDate to set
     */
	public void setStartDate(final LocalDate startDate) {
        this.startDate = startDate;
    }

    /**
     * @return the endDate
     */
	public LocalDate getEndDate() {
        return endDate;
    }

    /**
     * @param endDate
     *            the endDate to set
     */
	public void setEndDate(final LocalDate endDate) {
        this.endDate = endDate;
    }

}
