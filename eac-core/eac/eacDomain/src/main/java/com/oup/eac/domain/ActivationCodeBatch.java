package com.oup.eac.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class ActivationCodeBatch extends UpdatedAudit {

	public enum ActivationCodeFormat {
		EAC_NUMERIC(false);

		private final boolean prefixed;

		private ActivationCodeFormat(final boolean prefixed) {
		 this.prefixed = prefixed;   
		}
		
		public boolean isPrefixed() {
		    return prefixed;
		}
	}
	
	@Enumerated(EnumType.STRING)
	private ActivationCodeFormat activationCodeFormat;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "activation_code_registration_definition_id") 
	private ActivationCodeRegistrationDefinition activationCodeRegistrationDefinition;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "licence_template_id") 
	private LicenceTemplate licenceTemplate;
	
	private String codePrefix;
	
	private Integer numberOfTokens;
	
    @Column(nullable = false, unique = true)
    private String batchId;
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "activationCodeBatch")
    private List<ActivationCode> activationCodes = new ArrayList<ActivationCode>();
    
    @Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
    @DateTimeFormat (pattern="dd/MM/yyyy")
    private DateTime startDate;
    
    @Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
    @DateTimeFormat (pattern="dd/MM/yyyy")
    private DateTime endDate;

    private String addedInPool;
    
	/**
	 * @return the activationCodes
	 */
	public List<ActivationCode> getActivationCodes() {
		return activationCodes;
	}

	/**
	 * @param activationCodes the activationCodes to set
	 */
	public void setActivationCodes(final List<ActivationCode> activationCodes) {
		this.activationCodes = activationCodes;
	}

	/**
	 * @return the activationCodeRegistrationDefinition
	 */
	public ActivationCodeRegistrationDefinition getActivationCodeRegistrationDefinition() {
		return activationCodeRegistrationDefinition;
	}

	/**
	 * @param activationCodeRegistrationDefinition the activationCodeRegistrationDefinition to set
	 */
	public void setActivationCodeRegistrationDefinition(final ActivationCodeRegistrationDefinition activationCodeRegistrationDefinition) {
		this.activationCodeRegistrationDefinition = activationCodeRegistrationDefinition;
	}

	/**
	 * @return the licenceTemplate
	 */
	public LicenceTemplate getLicenceTemplate() {
		return licenceTemplate;
	}

	/**
	 * @param licenceTemplate the licenceTemplate to set
	 */
	public void setLicenceTemplate(final LicenceTemplate licenceTemplate) {
		this.licenceTemplate = licenceTemplate;
	}

	/**
	 * @return the activationCodeFormat
	 */
	public ActivationCodeFormat getActivationCodeFormat() {
		return activationCodeFormat;
	}

	/**
	 * @param activationCodeFormat the activationCodeFormat to set
	 */
	public void setActivationCodeFormat(final ActivationCodeFormat activationCodeFormat) {
		this.activationCodeFormat = activationCodeFormat;
	}

	/**
	 * @return the codePrefix
	 */
	public String getCodePrefix() {
		return codePrefix;
	}

	/**
	 * @param codePrefix the codePrefix to set
	 */
	public void setCodePrefix(final String codePrefix) {
		this.codePrefix = codePrefix;
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
	public void setBatchId(final String batchId) {
        this.batchId = batchId;
    }

	public void addActivationCode(final ActivationCode activationCode) {
        activationCodes.add(activationCode);
        activationCode.setActivationCodeBatch(this);
    }
    
	public void removeActivationCode(final ActivationCode activationCode) {
        activationCodes.remove(activationCode);
        activationCode.setActivationCodeBatch(null);
    }

    public DateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(DateTime startDate) {
        this.startDate = startDate;
    }

    public DateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(DateTime endDate) {
        this.endDate = endDate;
    }
        
    public Integer getNumberOfTokens() {
		return numberOfTokens;
	}

	public void setNumberOfTokens(Integer numberOfTokens) {
		this.numberOfTokens = numberOfTokens;
	}

	public String getValidityPeriod() {
        StringBuffer valPeriod = new StringBuffer();
        if(getStartDate() != null) {
            valPeriod.append(getStartDate().toString("dd/MM/YYYY"));
        }
        if(getEndDate() != null) {
            if(valPeriod.length() > 0) {
                valPeriod.append(" - ");
            }
            valPeriod.append(getEndDate().toString("dd/MM/YYYY"));
        }
        return valPeriod.toString();
    }

	public String getAddedInPool() {
		return addedInPool;
	}

	public void setAddedInPool(String addedInPool) {
		this.addedInPool = addedInPool;
	}
    
	
}
