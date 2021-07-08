package com.oup.eac.ws.v1.service;

import com.oup.eac.service.ServiceLayerException;
import com.oup.eac.ws.v1.userdata.binding.RegistrationInformationResponse;
import com.oup.eac.ws.v1.userdata.binding.UserNameResponse;

/**
 * Adapter to convert user details to WS user details.
 * 
 */
public interface UserServiceAdapter {

    /**
     * @param sessionToken
     *            session token
     * @return user name response
     * @throws ServiceLayerException
     *             the exception
     */
    UserNameResponse getUserName(final String sessionToken) throws ServiceLayerException;

    /**
     * @param sessionToken
     *            session token
     * @return registration information response
     * @throws ServiceLayerException
     *             the exception
     */
    RegistrationInformationResponse getRegistrationInformation(final String sessionToken) throws ServiceLayerException;
    
}
