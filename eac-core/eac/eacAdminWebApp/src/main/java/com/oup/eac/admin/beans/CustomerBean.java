package com.oup.eac.admin.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.oup.eac.admin.actions.CustomerAction;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.Customer.CustomerType;
import com.oup.eac.domain.ExternalCustomerId;
import com.oup.eac.domain.ExternalSystem;
import com.oup.eac.domain.ExternalSystemIdType;
import com.oup.eac.domain.LinkedRegistration;
import com.oup.eac.domain.ProductRegistrationDefinition;
import com.oup.eac.domain.Registration;
import com.oup.eac.domain.entitlement.ProductEntitlementDto;
import com.oup.eac.domain.entitlement.ProductEntitlementGroupDto;
import com.oup.eac.dto.LicenceDto;

/**
 * Bean for storing customer create/edit details.
 * 
 * @author Ian Packard
 *
 */
public class CustomerBean implements Serializable {

    private static final Logger LOG = Logger.getLogger(CustomerAction.class);

    private static final long serialVersionUID = -8643642214078827748L;

	private final Customer customer;
    private String password;
    private String passwordAgain;
    private boolean generatePassword = false;
    private boolean changePassword = false;
	private List<ProductEntitlementGroupDto> productEntitlementGroups;
    private String externalId;
    private ExternalSystemIdType externalSystemIdType;
    private ExternalSystem externalSystem;
    private List<ExternalCustomerId> externalIds;
	private Map<String, RegistrationStateBean> registrationStateMap = new HashMap<String, RegistrationStateBean>();
	private List<Registration<? extends ProductRegistrationDefinition>> problemRegistrations;
    private Set<Integer> matchedErightsIds;
    private Boolean checkboxClicked;
    private String concurrencyValue;
    private List<ProductEntitlementGroupDto> problemtEntitlementGroups;
    private Integer defaultConcurrencyValue;
    private String externalSystemIdTypeStr;
    private String externalSystemStr;
    
    /**
     * Initialise a customer bean with data for an existing customer.
     * 
     * @param dto
     */
	public CustomerBean(final Customer customer, final List<ProductEntitlementGroupDto> productEntitlementGroups) {
		this.customer = customer;
		this.productEntitlementGroups = productEntitlementGroups;
		initialiseRegistrationStateMap(productEntitlementGroups);
		this.defaultConcurrencyValue = CustomerType.SELF_SERVICE.getConcurrency();
    	//Existing customer should use existing external ids
		this.externalIds = new ArrayList<ExternalCustomerId>(this.customer.getExternalIds());		
		this.matchedErightsIds = getMatchedErightsIds(getRegistrations(), this.productEntitlementGroups);
		this.problemRegistrations = getProblemRegistration(customer, this.productEntitlementGroups);
    }

	public List<ProductEntitlementGroupDto> getProblemtEntitlementGroups() {
		return problemtEntitlementGroups;
	}

	public void setProblemtEntitlementGroups(
			List<ProductEntitlementGroupDto> problemtEntitlementGroups) {
		this.problemtEntitlementGroups = problemtEntitlementGroups;
	}

	private void initialiseRegistrationStateMap(List<ProductEntitlementGroupDto> productEntitlementGroups) {
		registrationStateMap.clear();
		for (ProductEntitlementGroupDto productEntitlementGroupDto : productEntitlementGroups) {
		    Registration<? extends ProductRegistrationDefinition> reg = productEntitlementGroupDto.getEntitlement().getRegistration();
            if (reg != null) {
                registrationStateMap.put(reg.getId(), new RegistrationStateBean());
            }
		}
	}
    
    /**
     * Initialise a customer bean when no customer exists.
     */
    public CustomerBean() {
    	this.customer = new Customer();
    	customer.setLocale(Locale.getDefault());
    	customer.setTimeZone(TimeZone.getDefault().getID());
		productEntitlementGroups = new ArrayList<ProductEntitlementGroupDto>();
    	this.concurrencyValue =Integer.toString(customer.getCustomerType().getConcurrency());
    	this.defaultConcurrencyValue = CustomerType.SELF_SERVICE.getConcurrency();
    	//New customer has new external ids
    	this.externalIds = new ArrayList<ExternalCustomerId>();
    	this.problemRegistrations = new ArrayList<Registration<? extends ProductRegistrationDefinition>>();
    }
    
    
    
    public Integer getDefaultConcurrencyValue() {
		return defaultConcurrencyValue;
	}

	public void setDefaultConcurrencyValue(Integer defaultConcurrencyValue) {
		this.defaultConcurrencyValue = defaultConcurrencyValue;
	}

	public Customer getCustomer() {
        return customer;
    }

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPasswordAgain() {
		return passwordAgain;
	}

	public void setPasswordAgain(String passwordAgain) {
		this.passwordAgain = passwordAgain;
	}

	public boolean isChangePassword() {
		return changePassword;
	}

	public void setChangePassword(boolean changePassword) {
		this.changePassword = changePassword;
	}

	public boolean isGeneratePassword() {
		return generatePassword;
	}

	public void setGeneratePassword(boolean generatePassword) {
		this.generatePassword = generatePassword;
	}
	
	public List<ProductEntitlementGroupDto> getProductEntitlementGroups() {
		return productEntitlementGroups;
	}
	
	public void setProductEntitlementGroups(List<ProductEntitlementGroupDto> productEntitlementGroups) {
		this.productEntitlementGroups = productEntitlementGroups;
		initialiseRegistrationStateMap(productEntitlementGroups);
		this.matchedErightsIds = getMatchedErightsIds(this.getRegistrations(), productEntitlementGroups);
		this.problemRegistrations = getProblemRegistration(customer, this.productEntitlementGroups);		
	}

	public void addExternalIdToCustomer() {
		
		//Add current external id to customer
    	ExternalCustomerId externalCustomerId = new ExternalCustomerId();
    	externalCustomerId.setCustomer(customer);
    	externalCustomerId.setExternalId(externalId);
    	externalCustomerId.setExternalSystemIdType(externalSystemIdType);
    	externalSystemIdType.setExternalSystem(externalSystem);
		
		externalIds.add(externalCustomerId);
	}
	
	public void removeExternalIdFromCustomer(final int index) {
		externalIds.remove(index);
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	public ExternalSystemIdType getExternalSystemIdType() {
		return externalSystemIdType;
	}

	public void setExternalSystemIdType(ExternalSystemIdType externalSystemIdType) {
		this.externalSystemIdType = externalSystemIdType;
	}

	public ExternalSystem getExternalSystem() {
		return externalSystem;
	}

	public void setExternalSystem(ExternalSystem externalSystem) {
		this.externalSystem = externalSystem;
	}
	
	public List<ExternalCustomerId> getExternalIds() {
		return externalIds;
	}
	
	public void setExternalIds(final List<ExternalCustomerId> externalIds) {
		this.externalIds = externalIds;
	}

	public List<LicenceDto> getLicences() {
		List<LicenceDto> licences = new ArrayList<LicenceDto>();
		for (ProductEntitlementGroupDto productEntitlementGroupDto : productEntitlementGroups) {
			licences.add(productEntitlementGroupDto.getEntitlement().getLicence());
			for (ProductEntitlementDto linkedEntitlement : productEntitlementGroupDto.getLinkedEntitlements()) {
				licences.add(linkedEntitlement.getLicence());
			}
		}
		return licences;
	}

	public List<Registration<? extends ProductRegistrationDefinition>> getRegistrations() {
		List<Registration<? extends ProductRegistrationDefinition>> registrations = new ArrayList<Registration<? extends ProductRegistrationDefinition>>();
		for (ProductEntitlementGroupDto productEntitlementGroupDto : productEntitlementGroups) {
            Registration<? extends ProductRegistrationDefinition> reg = productEntitlementGroupDto.getEntitlement().getRegistration();
            if (reg != null) {
                registrations.add(reg);
            }
		}
		return registrations;
	}

	public Map<String, RegistrationStateBean> getRegistrationStateMap() {
		return registrationStateMap;
	}

	public void setRegistrationStateMap(Map<String, RegistrationStateBean> registrationState) {
		this.registrationStateMap = registrationState;
	}

	public RegistrationStateBean getRegistrationState(String registrationId) {
		return registrationStateMap.get(registrationId);
	}

    public List<Registration<? extends ProductRegistrationDefinition>> getProblemRegistrations() {        
        return problemRegistrations;
    }
    
    public Set<Integer> getMatchedErightsLicenceIds() {
        return this.matchedErightsIds;
    }
    
    private Set<Integer> getMatchedErightsIds(
            List<Registration<? extends ProductRegistrationDefinition>> registrations,
            List<ProductEntitlementGroupDto> productEntitlementGroups) {
        Set<String> eacLicenceIds = new HashSet<String>();
        if (registrations != null) {
            for (Registration<? extends ProductRegistrationDefinition> reg : registrations) {
                addLicenceId(reg.getId(), eacLicenceIds);
                for (LinkedRegistration linked : reg.getLinkedRegistrations()) {
                    addLicenceId(linked.getId(), eacLicenceIds);
                }
            }
        }
        Set<String> atyponLicenceIds = new HashSet<String>();
        if (productEntitlementGroups != null) {
            for (ProductEntitlementGroupDto groupDto : productEntitlementGroups) {
                ProductEntitlementDto ent = groupDto.getEntitlement();
                addLicenceId(ent.getLicence().getLicenseId(), atyponLicenceIds);
                List<ProductEntitlementDto> linked = groupDto.getLinkedEntitlements();
                if(linked != null){
                    for (ProductEntitlementDto linkedEnt : linked) {
                        addLicenceId(linkedEnt.getLicence().getLicenseId(), atyponLicenceIds);
                    }
                }
            }
        }
        Collection<Integer> inBoth = CollectionUtils.intersection(eacLicenceIds, atyponLicenceIds);
        Set<Integer> result = new HashSet<Integer>();
        result.addAll(inBoth);
        return result;
    }

    private void addLicenceId(String erightsLicenceId, Set<String> erightsLicenceIds) {
        if (erightsLicenceIds != null) {
            erightsLicenceIds.add(erightsLicenceId);
        }
    }


    private List<Registration<? extends ProductRegistrationDefinition>> getProblemRegistration(Customer customer,
            List<ProductEntitlementGroupDto> groups) {
        
        List<Registration<? extends ProductRegistrationDefinition>> result = new ArrayList<Registration<? extends ProductRegistrationDefinition>>();
        
        Map<String, ProductEntitlementGroupDto> lookup = new HashMap<String, ProductEntitlementGroupDto>();
        if (groups != null) {
            for (ProductEntitlementGroupDto group : groups) {
                if (!group.isOrphan()) {
                    Registration<? extends ProductRegistrationDefinition> reg = group.getEntitlement()
                            .getRegistration();
                    if (reg != null) {
                        boolean exists = lookup.containsKey(reg.getId());
                        if (exists) {
                            LOG.warn("registrationId already exists in map" + reg.getId());
                        }
                        lookup.put(reg.getId(), group);
                    } else {
                        LOG.warn("unexpected null registration");
                    }
                }
            }
        }
        // at this point the lookup map contains links from registrationId->entitlementGoup
        Set<Registration<ProductRegistrationDefinition>> registrations = customer.getRegistrations();
        for(Registration<? extends ProductRegistrationDefinition> reg : registrations) {
            if(reg.isCompleted()){
                ProductEntitlementGroupDto group = lookup.get(reg.getId());
                if(group == null){
                    //A problem registration is one that is complete but has  not been linked to an Atypon licence in a ProductEntitlement.
                    result.add(reg);
                } else {
                    boolean linkedRegProblem = false;
                    Iterator<LinkedRegistration> linkedIter = reg.getLinkedRegistrations().iterator();
                    while (linkedIter.hasNext() && !linkedRegProblem) {
                        LinkedRegistration linkedReg = linkedIter.next();

                        Integer linkedErightsId = linkedReg.getErightsLicenceId();
                        if (linkedErightsId != null) {
                            // Try and find linked erights id in linked
                            // entitlements
                            boolean found = false;
                            Iterator<ProductEntitlementDto> iter = group.getLinkedEntitlements().iterator();
                            while (iter.hasNext() && !found) {
                                ProductEntitlementDto linkedEnt = iter.next();
                                if (linkedErightsId.equals(linkedEnt.getLicence().getLicenseId())) {
                                    found = true;
                                }
                            }
                            linkedRegProblem = !found;
                        }
                    }
                    if (linkedRegProblem) {
                        result.add(reg);
                    }
                }
            }
        }
        return result;
    }

	public Boolean getCheckboxClicked() {
		return checkboxClicked;
	}

	public void setCheckboxClicked(Boolean checkboxClicked) {
		this.checkboxClicked = checkboxClicked;
	}

    public String getConcurrencyValue() {
        return concurrencyValue;
    }

    public void setConcurrencyValue(String concurrencyValue) {
        this.concurrencyValue = concurrencyValue;
    }
    
    public void removeLicenceFromProblemEntitlement(String eRightsLicenceId){
    	for (Iterator<ProductEntitlementGroupDto> iterator = this.problemtEntitlementGroups.iterator(); iterator.hasNext(); ) {
    		ProductEntitlementGroupDto problemEntitlement = iterator.next();
    	    if (problemEntitlement.getEntitlement().getLicence().getLicenseId().equals(eRightsLicenceId)) {
    	        iterator.remove();
    	    }
    	}
    	
    }

	/**
	 * @return the externalSystemIdTypeStr
	 */
	public String getExternalSystemIdTypeStr() {
		return externalSystemIdTypeStr;
	}

	/**
	 * @param externalSystemIdTypeStr the externalSystemIdTypeStr to set
	 */
	public void setExternalSystemIdTypeStr(String externalSystemIdTypeStr) {
		this.externalSystemIdTypeStr = externalSystemIdTypeStr;
		if (this.externalSystemIdType == null) {
			this.externalSystemIdType = new ExternalSystemIdType();
		}
		this.externalSystemIdType.setName(externalSystemIdTypeStr);
		this.externalSystemIdType.setDescription("");
	}

	/**
	 * @return the externalSystemStr
	 */
	public String getExternalSystemStr() {
		return externalSystemStr;
	}

	/**
	 * @param externalSystemStr the externalSystemStr to set
	 */
	public void setExternalSystemStr(String externalSystemStr) {
		this.externalSystemStr = externalSystemStr;
		if (this.externalSystem == null) {
			this.externalSystem = new ExternalSystem();
		}
		this.externalSystem.setName(externalSystemStr);
	}

}
