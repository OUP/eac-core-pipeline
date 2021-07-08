package com.oup.eac.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.hibernate.annotations.Index;

@Entity
public class ActivationCode extends BaseDomainObject {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activation_code_batch_id", nullable = false)
    private ActivationCodeBatch activationCodeBatch;
    
    @Column(unique = true)
    @Index(name = "activation_code_idx")
    private String code;
    
    private Integer allowedUsage;
    
    private Integer actualUsage = Integer.valueOf(0);
    
   /* @OneToMany(fetch = FetchType.LAZY, mappedBy = "activationCode")*/
    @Transient
    private Set<ActivationCodeRegistration> registrations = new HashSet<ActivationCodeRegistration>();

    public ActivationCode() {
		super();
	}
    
	public ActivationCode(final ActivationCodeBatch activationCodeBatch, final String code, final Integer allowedUsage) {
		super();
		this.activationCodeBatch = activationCodeBatch;
		this.code = code;
		this.allowedUsage = allowedUsage;
	}
	
	/**
     * @return the actualUsage
     */
	public Integer getActualUsage() {
        return actualUsage;
    }

    /**
     * @param actualUsage the actualUsage to set
     */
	public void setActualUsage(final Integer actualUsage) {
        this.actualUsage = actualUsage;
    }
    
    /**
     * @return the allowedUsage
     */
	public Integer getAllowedUsage() {
        return allowedUsage;
    }

    /**
     * @param allowedUsage the allowedUsage to set
     */
	public void setAllowedUsage(final Integer allowedUsage) {
        this.allowedUsage = allowedUsage;
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
	public void setCode(final String code) {
        this.code = code;
    }

    /**
     * @return the activationCodeBatch
     */
	public ActivationCodeBatch getActivationCodeBatch() {
        return activationCodeBatch;
    }

    /**
     * @param activationCodeBatch the activationCodeBatch to set
     */
	public void setActivationCodeBatch(final ActivationCodeBatch activationCodeBatch) {
        this.activationCodeBatch = activationCodeBatch;
    }
   
	public void incrementActualUsage() {
        setActualUsage(Integer.valueOf(getActualUsage().intValue() + 1));
    }
	
	public void decrementActualUsage(){
		setActualUsage(Integer.valueOf(getActualUsage().intValue() - 1));
	}
    
	public boolean isUsageLimitReached() {
    	return getActualUsage().intValue() >= getAllowedUsage().intValue();
    }

    /**
     * @return the registrations
     */
    public Set<ActivationCodeRegistration> getRegistrations() {
        return registrations;
    }

    /**
     * @param registrations the registrations to set
     */
    public void setRegistrations(Set<ActivationCodeRegistration> registrations) {
        this.registrations = registrations;
    }
	
}
