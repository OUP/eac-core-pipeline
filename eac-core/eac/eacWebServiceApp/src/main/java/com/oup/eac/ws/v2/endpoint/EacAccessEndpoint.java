package com.oup.eac.ws.v2.endpoint;

import org.springframework.ws.server.endpoint.annotation.RequestPayload;

import com.amazonaws.services.cloudfront.model.AccessDeniedException;
import com.oup.eac.integration.facade.exceptions.ErightsException;
import com.oup.eac.integration.facade.exceptions.GroupNotFoundException;
import com.oup.eac.integration.facade.exceptions.LicenseNotFoundException;
import com.oup.eac.integration.facade.exceptions.ProductNotFoundException;
import com.oup.eac.integration.facade.exceptions.UserNotFoundException;
import com.oup.eac.ws.v2.binding.access.AuthenticateRequest;
import com.oup.eac.ws.v2.binding.access.AuthenticateResponse;
import com.oup.eac.ws.v2.binding.access.ChangePasswordRequest;
import com.oup.eac.ws.v2.binding.access.ChangePasswordResponse;
import com.oup.eac.ws.v2.binding.access.CreateActivationCodeBatchRequest;
import com.oup.eac.ws.v2.binding.access.CreateActivationCodeBatchResponse;
import com.oup.eac.ws.v2.binding.access.CreateUserAccountRequest;
import com.oup.eac.ws.v2.binding.access.CreateUserAccountResponse;
import com.oup.eac.ws.v2.binding.access.CreateUserAccountWithConcurrencyRequest;
import com.oup.eac.ws.v2.binding.access.CreateUserAccountWithConcurrencyResponse;
import com.oup.eac.ws.v2.binding.access.GetCredentialPolicyRequest;
import com.oup.eac.ws.v2.binding.access.GetCredentialPolicyResponse;
import com.oup.eac.ws.v2.binding.access.GetFullUserEntitlementsRequest;
import com.oup.eac.ws.v2.binding.access.GetFullUserEntitlementsResponse;
import com.oup.eac.ws.v2.binding.access.GetUserEntitlementsRequest;
import com.oup.eac.ws.v2.binding.access.GetUserEntitlementsResponse;
import com.oup.eac.ws.v2.binding.access.GuestRedeemActivationCodeRequest;
import com.oup.eac.ws.v2.binding.access.GuestRedeemActivationCodeResponse;
import com.oup.eac.ws.v2.binding.access.KillUserSessionRequest;
import com.oup.eac.ws.v2.binding.access.KillUserSessionResponse;
import com.oup.eac.ws.v2.binding.access.LogoutRequest;
import com.oup.eac.ws.v2.binding.access.LogoutResponse;
import com.oup.eac.ws.v2.binding.access.ProductRegistrationRequest;
import com.oup.eac.ws.v2.binding.access.ProductRegistrationResponse;
import com.oup.eac.ws.v2.binding.access.RedeemActivationCodeRequest;
import com.oup.eac.ws.v2.binding.access.RedeemActivationCodeResponse;
import com.oup.eac.ws.v2.binding.access.ResetPasswordRequest;
import com.oup.eac.ws.v2.binding.access.ResetPasswordResponse;
import com.oup.eac.ws.v2.binding.access.ResetPasswordWithUrlRequest;
import com.oup.eac.ws.v2.binding.access.ResetPasswordWithUrlResponse;
import com.oup.eac.ws.v2.binding.access.SearchActivationCodeRequest;
import com.oup.eac.ws.v2.binding.access.SearchActivationCodeResponse;
import com.oup.eac.ws.v2.binding.access.SetExternalProductIdsRequest;
import com.oup.eac.ws.v2.binding.access.SetExternalProductIdsResponse;
import com.oup.eac.ws.v2.binding.access.SetExternalUserIdsRequest;
import com.oup.eac.ws.v2.binding.access.SetExternalUserIdsResponse;
import com.oup.eac.ws.v2.binding.access.UpdateUserAccountRequest;
import com.oup.eac.ws.v2.binding.access.UpdateUserAccountResponse;
import com.oup.eac.ws.v2.binding.access.UpdateUserAccountWithConcurrencyRequest;
import com.oup.eac.ws.v2.binding.access.UpdateUserAccountWithConcurrencyResponse;
import com.oup.eac.ws.v2.binding.access.ValidateActivationCodeRequest;
import com.oup.eac.ws.v2.binding.access.ValidateActivationCodeResponse;
import com.oup.eac.ws.v2.binding.access.ValidatePasswordCredentialsRequest;
import com.oup.eac.ws.v2.binding.access.ValidatePasswordCredentialsResponse;
import com.oup.eac.ws.v2.ex.WebServiceException;

/**
 * The Interface EacAccessEndpoint.
 */
public interface EacAccessEndpoint {

    /**
     * Gets the user entitlements.
     *
     * @param request the request
     * @return the user entitlements
     * @throws WebServiceException the web service exception
     * @throws ErightsException 
     * @throws AccessDeniedException 
     */
    GetUserEntitlementsResponse getUserEntitlements(GetUserEntitlementsRequest request) throws WebServiceException, AccessDeniedException, ErightsException;

    /**
     * Create a batch of activation codes.
     *
     * @param request the request
     * @return the generated activation codes
     * @throws WebServiceException 
     * @throws WebServiceException the web service exception
     */
    CreateActivationCodeBatchResponse createActivationCodeBatch(CreateActivationCodeBatchRequest request) throws WebServiceException, 
    ProductNotFoundException, UserNotFoundException, LicenseNotFoundException, AccessDeniedException, GroupNotFoundException, ErightsException;

    /**
     * Redeems an activation code.
     *
     * @param request contains the user, the system and the activation code.
     * @return the response message containing the success status.
     * @throws WebServiceException 
     * @throws WebServiceException the web service exception
     */
    RedeemActivationCodeResponse redeemActivationCode(RedeemActivationCodeRequest request) throws WebServiceException;
    
    /**
     * Validate an activation code.
     *
     * @param request contains the system and the activation code.
     * @return the response message containing the success status.
     * @throws WebServiceException 
     * @throws WebServiceException the web service exception
     */
    ValidateActivationCodeResponse validateActivationCode(ValidateActivationCodeRequest request) throws WebServiceException;

    /**
     * Search for licence info by activation code
     * 
     * @param request the request
     * @return the licence info
     * @throws WebServiceException
     */
    SearchActivationCodeResponse searchActivationCode(@RequestPayload  final SearchActivationCodeRequest request) throws WebServiceException;
    
    /**
     * Authenticate user.
     *
     * @param request the authenticate request
     * @return the authenticate response
     */
    AuthenticateResponse authenticateUser(AuthenticateRequest request);

    /**
     * Logout.
     *
     * @param request the request
     * @return the logout response
     */
    LogoutResponse logout(LogoutRequest request);

    /**
     * Change password.
     *
     * @param request the request
     * @return the change password response
     */
    ChangePasswordResponse changePassword(ChangePasswordRequest request);

    /**
     * Reset password.
     *
     * @param request the request
     * @return the change password response
     */
    ResetPasswordResponse resetPassword(ResetPasswordRequest request);

    /**
     * Creates the user account.
     * 
     * @param request
     *            the request
     * @return the creates the user account response
     */
    CreateUserAccountResponse createUserAccount(CreateUserAccountRequest request);

    /**
     * Update user account.
     * 
     * @param request
     *            the request
     * @return the update user account response
     * @throws WebServiceException 
     */
    UpdateUserAccountResponse updateUserAccount(UpdateUserAccountRequest request) throws WebServiceException;
    
    
    /**
     * Sets the external user ids.
     *
     * @param request the request
     * @return the sets the external user ids response
     * @throws WebServiceException the web service exception
     */
    SetExternalUserIdsResponse setExternalUserIds(SetExternalUserIdsRequest request) throws WebServiceException;
    
    /**
     * Sets the external product ids.
     *
     * @param request the request
     * @return the sets the external product ids response
     * @throws WebServiceException the web service exception
     */
    SetExternalProductIdsResponse setExternalProductIds(SetExternalProductIdsRequest request) throws WebServiceException;
    
    /**
     * Validate password credentials.
     *
     * @param request the request
     * @return the validate password credentials response
     * @throws WebServiceException the web service exception
     */
    ValidatePasswordCredentialsResponse validate(ValidatePasswordCredentialsRequest request) throws WebServiceException;
    
    /**
     * Creates the product registration.
     *
     * @param request the request
     * @return the product registration request
     * @throws WebServiceException the web service exception
     */
    ProductRegistrationResponse createProductRegistration(ProductRegistrationRequest request) throws WebServiceException;
    
    /**
     * Kill user session.
     *
     * @param request the KillUserSessionRequest
     * @return the KillUserSessionResponse
     */
    KillUserSessionResponse killUserSession(KillUserSessionRequest request) throws WebServiceException;
    
    
    /**
     * Creates the user account WithConcurrency.
     * 
     * @param request
     *            the request
     * @return the creates the user account WithConcurrency response
     */
    CreateUserAccountWithConcurrencyResponse createUserAccountWithConcurrency(CreateUserAccountWithConcurrencyRequest request);
    
    /**
     * Update user account With Concurrency
     * 
     * @param request
     *            the request
     * @return the update user account response
     * @throws WebServiceException 
     */
    UpdateUserAccountWithConcurrencyResponse updateUserAccountWithConcurrency(UpdateUserAccountWithConcurrencyRequest request) throws WebServiceException;
    

    /**
     * Gets the user full entitlements.
     *
     * @param request the request
     * @return the user entitlements
     * @throws WebServiceException the web service exception
     * @throws ErightsException 
     * @throws AccessDeniedException 
     */
    GetFullUserEntitlementsResponse getFullUserEntitlements(GetFullUserEntitlementsRequest request) throws WebServiceException, AccessDeniedException, ErightsException;
    
    /**
     * Guest Redeems an activation code.
     *
     * @param request contains the activation code.
     * @return the response message containing the success status.
     * @throws WebServiceException 
     * @throws WebServiceException the web service exception
     */
    GuestRedeemActivationCodeResponse guestRedeemActivationCode(GuestRedeemActivationCodeRequest request) throws WebServiceException;
    
    /**
     * Gets EAC Credentials Policies
     *
     * @param No Parameters
     * @return the response message contains policies
     * @throws WebServiceException 
     * @throws WebServiceException the web service exception
     */
    GetCredentialPolicyResponse getCredentialPolicy(GetCredentialPolicyRequest request) throws WebServiceException;
    
    /**
     * Resets Password
     *
     * @param request contain userName, tokenId, successUrl, failureUrl 
     * @return the response message contains Success or failure message
     * @throws WebServiceException 
     * @throws WebServiceException the web service exception
     */
    ResetPasswordWithUrlResponse resetPassword(ResetPasswordWithUrlRequest request) throws WebServiceException;
}