package com.oup.eac.ws.v2.service;

import java.util.Locale;

import org.springframework.security.access.AccessDeniedException;

import com.oup.eac.integration.facade.exceptions.ErightsException;
import com.oup.eac.integration.facade.exceptions.GroupNotFoundException;
import com.oup.eac.integration.facade.exceptions.LicenseNotFoundException;
import com.oup.eac.integration.facade.exceptions.ProductNotFoundException;
import com.oup.eac.integration.facade.exceptions.UserNotFoundException;
import com.oup.eac.ws.v2.binding.access.GuestRedeemActivationCodeResponse;
import com.oup.eac.ws.v2.binding.access.RedeemActivationCodeResponse;
import com.oup.eac.ws.v2.binding.access.SearchActivationCodeResponse;
import com.oup.eac.ws.v2.binding.access.ValidateActivationCodeResponse;
import com.oup.eac.ws.v2.binding.common.WsUserId;
import com.oup.eac.ws.v2.ex.WebServiceException;

/**
 * The Interface ActivationCodeAdapter.
 */
public interface ActivationCodeAdapter {

    /**
     * Redeem activation code.
     *
     * @param the optional systemId - this will be used to get system specific external ids for the response
     * @param wsUserId the ws user id
     * @param activationCode the activation code
     * @param locale the locale
     * @return the redeem activation code response
     * @throws WebServiceException the web service exception
     */
    RedeemActivationCodeResponse redeemActivationCode(String systemId, WsUserId wsUserId, String activationCode, Locale locale) throws WebServiceException, ProductNotFoundException, UserNotFoundException, LicenseNotFoundException, AccessDeniedException, GroupNotFoundException, ErightsException ;
    
    /**
     * Validate activation code.
     *
     * @param systemId the systemId
     * @param activationCode the activation code
     * @return the validate activation code response
     * @throws WebServiceException the web service exception
     */
    ValidateActivationCodeResponse validateActivationCode(String systemId, String activationCode) throws WebServiceException;

    /**
     * Search for licence info by activation code
     * 
     * @param systemId the systemId
     * @param activationCode the activationCode
     * @param likeSearch do a lie search or not
     * @return the licence info
     * @throws WebServiceException
     */
    SearchActivationCodeResponse searchActivationCode(String systemId, String activationCode, boolean likeSearch) throws WebServiceException;
    
    /**
     * Guest Redeem activation code.
     *
     * @param activationCode the activation code
     * @return the redeem activation code response
     * @throws WebServiceException the web service exception
     */
    GuestRedeemActivationCodeResponse guestRedeemActivationCode(String activationCode) throws WebServiceException;
    
    
}
