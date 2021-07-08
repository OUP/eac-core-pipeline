package com.oup.eac.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.apache.log4j.Logger;
import org.hibernate.annotations.Index;

/**
 * @author harlandd A user registration.
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "registration_type", discriminatorType = DiscriminatorType.STRING)
@org.hibernate.annotations.Table(
        indexes = { 
                @Index(name = "registration_idx", columnNames = { "customer_id" }),		//, "registration_definition_id"
                @Index(name = "reg_by_date_and_owner_idx", columnNames = { "createdDate", "customer_id" })
                },
        appliesTo = "registration")
public abstract class Registration<T extends ProductRegistrationDefinition> extends UpdatedAudit {
	
    protected static final Logger LOG = Logger.getLogger(Registration.class);
    
	public static enum RegistrationType {
		PRODUCT,
		ACTIVATION_CODE;
	}
	
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Transient
    private boolean activated;
    
    @Transient
    private boolean completed;
    
    @Transient
    private boolean denied;
    
    @Transient
    private boolean enabled;
    
    @Transient
    private boolean awaitingValidation;
    
    @Transient
    private boolean expired;
    
    
    /*@Column(nullable = true, name="erights_licence_id")
    private Integer erightsLicenceId;*/
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "registration")
    private Set<LinkedRegistration> linkedRegistrations = new HashSet<LinkedRegistration>();
    
    public abstract RegistrationType getRegistrationType();
    
    /**
	 * @return the checksValidated
	 */
	public boolean isAwaitingValidation() {
		return awaitingValidation;
	}

	/**
	 * @param awaitingValidation the checksValidated to set
	 */
	public void setAwaitingValidation(final boolean awaitingValidation) {
		this.awaitingValidation = awaitingValidation;
	}

	/**
	 * @return the denied
	 */
	public boolean isDenied() {
		return denied;
	}


	/**
	 * @param denied the denied to set
	 */
	public void setDenied(final boolean denied) {
		if(denied && isActivated()) {
			activated = false;
		}
		this.denied = denied;
	}

	/**
	 * isEnabled 
	 * @return 
	 * boolean 
	 * @author Developed by TCS
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * setEnabled 
	 * @param enabled 
	 * void 
	 * @author Developed by TCS
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	/**
	 * @return the registrationDefinition
	 */
	abstract public T getRegistrationDefinition();

	/**
	 * @param registrationDefinition the registrationDefinition to set
	 */
	public abstract void setRegistrationDefinition(final T registrationDefinition);

	/**
	 * @return the completed
	 */
	public boolean isCompleted() {
		return completed;
	}

	/**
	 * @param completed the completed to set
	 */
	public void setCompleted(final boolean completed) {
		this.completed = completed;
	}
	
    /**
     * @return the activated
     */
	public boolean isActivated() {
        return activated;
    }

    /**
     * @param activated
     *            the activated to set
     */
	public void setActivated(final boolean activated) {
    	if(activated && !isCompleted()) {
    		throw new IllegalStateException("Registration must be completed before activation.");
    	}
    	if(activated && isDenied()) {
    		denied = false;
    	}
    	this.activated = activated;
    }

    /**
     * @return the user
     */
	public Customer getCustomer() {
        return customer;
    }

    /**
     * @param customer
     *            the user
     */
	public void setCustomer(final Customer customer) {
        this.customer = customer;
    }

   /* public Integer getErightsLicenceId() {
        return erightsLicenceId;
    }
*/
   // public void setErightsLicenceId(Integer erightsLicenceId) {
        /*if (this.erightsLicenceId != null) {
            String msg = String.format("Orphan Licence : overwriting old erightsLicenceId[%s] with new erightsLicenceId[%s]",  this.erightsLicenceId, erightsLicenceId);
            LOG.error(msg);
        }*/
     /*   this.erightsLicenceId = erightsLicenceId;
    }*/

    public Set<LinkedRegistration> getLinkedRegistrations() {
        return linkedRegistrations;
    }

    public void setLinkedRegistrations(Set<LinkedRegistration> linkedRegistrations) {
        this.linkedRegistrations = linkedRegistrations;
    }

	public boolean isExpired() {
		return expired;
	}

	public void setExpired(boolean expired) {
		this.expired = expired;
	}
	

    
}
