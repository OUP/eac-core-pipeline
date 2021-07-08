package com.oup.eac.ws.v2.service;

import org.springframework.security.access.AccessDeniedException;

import com.oup.eac.integration.facade.exceptions.ErightsException;
import com.oup.eac.ws.v2.binding.access.GetUserEntitlementsResponse;
import com.oup.eac.ws.v2.binding.common.WsUserId;
import com.oup.eac.ws.v2.ex.WebServiceException;

/**
 * The Interface UserEntitlementsAdapter.
 */
public interface UserEntitlementsAdapter {

	/**
	 * Gets the user entitlement groups.
	 *
	 * @param systemId the system id
	 * @param wsUserId the ws user id
	 * @return the user entitlement groups
	 * @throws WebServiceException the web service exception
	 * @throws ErightsException 
	 * @throws AccessDeniedException 
	 */
	GetUserEntitlementsResponse getUserEntitlementGroups(String systemId, WsUserId wsUserId) throws WebServiceException, AccessDeniedException, ErightsException;
    
}
