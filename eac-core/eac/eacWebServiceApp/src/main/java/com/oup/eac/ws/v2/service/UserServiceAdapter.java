package com.oup.eac.ws.v2.service;

import com.oup.eac.service.ServiceLayerException;
import com.oup.eac.ws.v2.binding.access.AuthenticateResponse;
import com.oup.eac.ws.v2.binding.access.ChangePasswordResponse;
import com.oup.eac.ws.v2.binding.access.LogoutResponse;
import com.oup.eac.ws.v2.binding.access.ResetPasswordResponse;
import com.oup.eac.ws.v2.binding.common.WsUserId;
import com.oup.eac.ws.v2.binding.userdata.RegistrationInformationResponse;
import com.oup.eac.ws.v2.binding.userdata.UserNameResponse;

/**
 * Adapter to convert user details to WS user details.
 * 
 */
public interface UserServiceAdapter {

    /**
     * Gets the user name.
     *
     * @param wsUserId the ws user id
     * @return the user name
     */
    UserNameResponse getUserName(final WsUserId wsUserId);

    /**
     * Gets the registration information.
     *
     * @param systemId the system id
     * @param wsUserId the ws user id
     * @return the registration information
     * @throws ServiceLayerException the service layer exception
     */
    RegistrationInformationResponse getRegistrationInformation(final String systemId, final WsUserId wsUserId) throws ServiceLayerException;
    
    /**
     * @param username the username
     * @param password the password
     * @return the authenticate response
     * @throws ServiceLayerException
     */
    AuthenticateResponse authenticate(String username, String password);
    
    /**
     * @param sessionToken the sessionToken
     * @return the logout response
     * @throws ServiceLayerException
     */
    LogoutResponse logout(String sessionToken);
    
    /**
     * Change password.
     *
     * @param wsUserId the ws user id
     * @param newPassword the new password
     * @return the change password response
     * @throws PasswordAlreadyExistsException 
     */
    ChangePasswordResponse changePassword(WsUserId wsUserId, String newPassword);
    
    /**
     * @param wsUsername 
     * @param username the username
     * @return the reset password response
     * @throws PasswordAlreadyExistsException 
     */
    ResetPasswordResponse resetPassword(WsUserId wsUserId, String wsUsername);
}
