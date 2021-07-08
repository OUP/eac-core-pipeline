package com.oup.eac.domain;

import org.hibernate.LazyInitializationException;

public class ExternalCustomerId extends ExternalId<Customer> {
    
    
    private Customer customer;

    /**
     * Gets the customer.
     *
     * @return the customer
     */
	public Customer getCustomer() {
        return customer;
    }

    /**
     * Sets the customer.
     *
     * @param customer the new customer
     */
	public void setCustomer(final Customer customer) {
        this.customer = customer;
    }

    @Override
    public Customer getDomainObject() {
        return customer;
    }

    @Override
    public com.oup.eac.domain.ExternalId.DomainObjectType getDomainObjectType() {
        return DomainObjectType.CUSTOMER;
    }    
    
    
    public static String getStringValue(ExternalCustomerId extCustomerId){
    	String result;
    	if(extCustomerId == null){
    		result = "Null ExternalCustomerId";
    	}else{
        	try{
        		String $username = "";
        		String $system = "";
        		String $typeId = "";
        		String $extId = extCustomerId.getExternalId();
        		if(extCustomerId.getCustomer() != null){
        			$username = extCustomerId.getCustomer().getUsername();
        		}
        		ExternalSystemIdType extTypeId = extCustomerId.getExternalSystemIdType();
        		if(extTypeId != null){
        			$typeId = extTypeId.getName();
        			ExternalSystem sys = extTypeId.getExternalSystem();
        			if(sys != null){
        				$system  = sys.getName();
        			}
        		}
        		
				result  = String.format("ExternalCustomerId[username:%s, system:%s, typeId:%s, extId:%s]", $username, $system, $typeId, $extId);
        	}catch(LazyInitializationException lie){
        		result =  extCustomerId.getClass().getName() + "@" + Integer.toHexString(extCustomerId.hashCode());
        	}
    	}
    	return result;
    }

}
