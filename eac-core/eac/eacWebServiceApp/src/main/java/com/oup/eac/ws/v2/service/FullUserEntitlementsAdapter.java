package com.oup.eac.ws.v2.service;

import java.util.Set;

import org.springframework.security.access.AccessDeniedException;

import com.oup.eac.integration.facade.exceptions.ErightsException;
import com.oup.eac.ws.v2.binding.access.GetFullUserEntitlementsResponse;
import com.oup.eac.ws.v2.binding.common.WsUserId;
import com.oup.eac.ws.v2.ex.WebServiceException;

/**
 * The Interface FullUserEntitlementsAdapter.
 */
public interface FullUserEntitlementsAdapter {

	/**
	 * Gets full user entitlement groups.
	 *
	 * @param wsUserId the ws user id
	 * @param systemIdSet the system id
	 * @param productSystemIdSet the product system id
	 * @param licenceState the licence state
	 * @return the user entitlement groups
	 * @throws WebServiceException the web service exception
	 * @throws ErightsException 
	 * @throws AccessDeniedException 
	 */
	GetFullUserEntitlementsResponse getFullUserEntitlementGroups(WsUserId wsUserId, Set<String> systemIdSet, Set<String> productSystemIdSet, Set<String> productOrgUnitSet, String licenceState) throws WebServiceException, AccessDeniedException, ErightsException;
    
}
