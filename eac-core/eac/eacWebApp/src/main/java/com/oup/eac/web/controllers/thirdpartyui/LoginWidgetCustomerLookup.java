package com.oup.eac.web.controllers.thirdpartyui;

import com.oup.eac.dto.WebCustomerDto;

/**
 * The Interface LoginWidgetCustomerLookup.
 */
public interface LoginWidgetCustomerLookup {

    /**
     * Gets the and cache user from eac cookue.
     *
     * @param eRightsSessionKey the eac session key
     * @return the and cache user from eac cookue
     */
    WebCustomerDto getAndCacheCustomerFromErightsSessionKey(String eRightsSessionKey);
    
    
    /**
     * Gets the user from eac cookie.
     *
     * @param eRightsSessionKey the eac session key
     * @return the user from eac cookie
     */
    WebCustomerDto getCustomerFromErightsSessionKey(String eRightsSessionKey);
    
    
    /**
     * Removes the Cache Entry for the specified eRightsSessionKey.
     *
     * @param eRightsSessionKey the eac session key
     */
    void removeCacheEntryForErightsSessionKey(String eRightsSessionKey);
}