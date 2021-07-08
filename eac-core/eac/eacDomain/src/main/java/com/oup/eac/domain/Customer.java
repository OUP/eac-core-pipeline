package com.oup.eac.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Index;

/**
 * @author harlandd Class representing a user.
 */
@Entity
@DiscriminatorValue(value = "CUSTOMER")
public class Customer extends User {

	public static enum CustomerType {
		
		SELF_SERVICE(5), SHARED(-1), SPECIFIC_CONCURRENCY(1);
		
		private Integer concurrency;
		
		private CustomerType(final Integer concurrency) {
			this.concurrency = concurrency;
		}

		public int getConcurrency() {
			return concurrency;
		}
		
		public void setConcurrency(final Integer concurrency) {
            this.concurrency = concurrency;
        }
	}

    /*@Index(name = "customer_erights_id_idx")
    //@Column(unique = true)
    private Integer erightsId;*/

    @Transient
	private CustomerType customerType= CustomerType.SELF_SERVICE;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "customer_id")
	private Set<Answer> answers = new HashSet<Answer>();
	
	@Transient
    private Set<Registration<ProductRegistrationDefinition>> registrations = new HashSet<Registration<ProductRegistrationDefinition>>();    
    
	@Transient
    private Set<ExternalCustomerId> externalIds = new HashSet<ExternalCustomerId>();
    
    @Transient
    private List<String> sessions;

    @Column(name = "USER_TYPE", updatable=false,  insertable = false)
    @Enumerated(EnumType.STRING)
	private UserType customerUserType = UserType.CUSTOMER;
    
    @Transient
    private int licenseCount;

	private boolean isTncAccepted;
    
	public int getLicenseCount() {
		return licenseCount;
	}

	public void setLicenseCount(int licenseCount) {
		this.licenseCount = licenseCount;
	}

	public UserType getCustomerUserType() {
		return customerUserType;
	}

	public void setCustomerUserType(UserType customerUserType) {
		this.customerUserType = customerUserType;
	}
	/**
	 * Default constructor.
	 */
	public Customer() {
		super();
		ResourceBundle bundle =ResourceBundle.getBundle("eac/eacDomain");
    	if(bundle != null){
    		String defaultValue = bundle.getString("default.concurrency.value");
    		if(!StringUtils.isBlank(defaultValue)){
    			CustomerType.SELF_SERVICE.setConcurrency(Integer.parseInt(defaultValue));
    		}
    	}
	}

	/**
	 * @param firstName
	 *            the first name
	 * @param familyName
	 *            the family name
	 * @param customerType
	 *            the customerType
	 */
	public Customer(final String firstName, final String familyName, final String username, final Password password, final CustomerType customerType) {
		super(firstName, familyName, username, password);		
		this.customerType = customerType;
	}

	public Set<Registration<ProductRegistrationDefinition>> getRegistrations() {
        return registrations;
    }
	
	/**
	 * Returns a filtered copy of the customers registrations. Only registrations with an
	 * activated=true will be returned. Active registrations will have licences in
	 * eRights but they may NOT be active (perhaps if they have been deactivated or expired).
	 * 
	 * @return
	 */
	@Transient
	public Set<Registration<ProductRegistrationDefinition>> getActiveRegistrations() {
		Set<Registration<ProductRegistrationDefinition>> activeRegistrations = new HashSet<Registration<ProductRegistrationDefinition>>();
		for (Registration<ProductRegistrationDefinition> registration : registrations) {
			//Add if the registration has been activated
			if (registration.isActivated()) {
				activeRegistrations.add(registration);
			}
		}
        return activeRegistrations;
    }
	
	/**
	 * Returns a filtered copy of the customers registrations. Only registrations with an
	 * completed=true will be returned. Completed registrations have completed registration 
	 * information but the may not yet be active. Consider using getActiveRegistrations() for 
	 * active registrations.
	 * 
	 * @return
	 */
	@Transient
	public Set<Registration<ProductRegistrationDefinition>> getCompletedRegistrations() {
		Set<Registration<ProductRegistrationDefinition>> completedRegistrations = new HashSet<Registration<ProductRegistrationDefinition>>();
		for (Registration<ProductRegistrationDefinition> registration : registrations) {
			//Add if the registration has been completed
			if (registration.isCompleted()) {
				completedRegistrations.add(registration);
			}
		}
        return completedRegistrations;
    }

    public void setRegistrations(final Set<Registration<ProductRegistrationDefinition>> registrations) {
        this.registrations = registrations;
    }

    /**
	 * @return the customerType
	 */
	public CustomerType getCustomerType() {
		return customerType;
	}

	/**
	 * @param customerType
	 *            the customerType to set
	 */
	public void setCustomerType(final CustomerType customerType) {
		this.customerType = customerType;
	}

	/**
	 * @return erights id
	 */
	/*public Integer getErightsId() {
		return erightsId;
	}*/

	/**
	 * @param erightsId
	 *            erights id
	 */
	/*public void setErightsId(final Integer erightsId) {
		this.erightsId = erightsId;
	}*/
	
    /**
     * @return answers
     */
	public Set<Answer> getAnswers() {
        return answers;
    }

    /**
     * @param answers
     *            answers
     */
	public void setAnswers(final Set<Answer> answers) {
        this.answers = answers;
    }
    
    @Override
	public UserType getUserType() {
        return UserType.CUSTOMER;
    }
    
    /**
     * Gets the external ids.
     * 
     * @return the external ids
     */
	public Set<ExternalCustomerId> getExternalIds() {
        return externalIds;
    }

    /**
     * Sets the external ids.
     * 
     * @param externalIds
     *            the new external ids
     */
	public void setExternalIds(final Set<ExternalCustomerId> externalIds) {
        this.externalIds = externalIds;
    }
    
    @Transient
    public boolean isSuspended() {
    	return !isEnabled();
    }
    
	public void setSuspended(final boolean suspended) {
    	this.setEnabled(!suspended);
    }
	
	@Override
    protected boolean trackFailedLoginAttempts() {
    	return customerType != CustomerType.SHARED;
    }

    public List<String> getSessions() {
        return sessions;
    }

    public void setSessions(List<String> sessions) {
        this.sessions = sessions;
    }
    
    public void setDash() {
    	if(this.sessions.isEmpty()){
    		this.sessions = new ArrayList<String>();
            this.sessions.add("-");
    	}
    }
    
    public void removeSession(String session){
    	if(this.sessions.contains(session)){
    		this.sessions.remove(session);
    	}
    }

	public boolean isTncAccepted() {
		return isTncAccepted;
	}

	public void setTncAccepted(boolean isTncAccepted) {
		this.isTncAccepted = isTncAccepted;
	}
	
    
}
