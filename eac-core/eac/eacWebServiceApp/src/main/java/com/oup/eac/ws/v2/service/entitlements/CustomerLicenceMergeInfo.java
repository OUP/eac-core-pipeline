package com.oup.eac.ws.v2.service.entitlements;

import java.util.Set;

import com.oup.eac.domain.Customer;

/**
 * Used to check if the Eac Database and Erights web service are out of synch when merging data for GetUserEntitlements Web Service.
 * 
 * @author David Hay
 */
public interface CustomerLicenceMergeInfo {
    
    /**
     * Gets the customer.
     *
     * @return the customer
     */
    public Customer getCustomer();
    
    /**
     * Gets the matched licences.
     *
     * @return the matched licences
     */
    public Set<String> getMatchedLicences();
    
    /**
     * Gets the un matched licences in eac only.
     * This set should be empty if EAC and eRights are in sync.
     * @return the un matched licences in eac only
     */
    public Set<String> getUnMatchedLicencesInEacOnly();
    
    /**
     * Gets the un matched licences in erights only.
     * This set should be empty if EAC and eRights are in sync.
     * @return the un matched licences in erights only
     */
    public Set<String> getUnMatchedLicencesInErightsOnly();
    
    /**
     * Checks if is fully matched.
     *
     * @return true, if is fully matched
     */
    public boolean isFullyMatched();

}
