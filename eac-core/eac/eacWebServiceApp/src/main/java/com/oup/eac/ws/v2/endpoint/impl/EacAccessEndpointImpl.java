package com.oup.eac.ws.v2.endpoint.impl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.oup.eac.domain.utils.audit.AuditLogger;
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
import com.oup.eac.ws.v2.binding.common.ActivationCodeBatch;
import com.oup.eac.ws.v2.binding.common.ActivationCodeLicence;
import com.oup.eac.ws.v2.binding.common.CreateUser;
import com.oup.eac.ws.v2.binding.common.CreateUserWithConcurrency;
import com.oup.eac.ws.v2.binding.common.Credential;
import com.oup.eac.ws.v2.binding.common.ErrorStatus;
import com.oup.eac.ws.v2.binding.common.LocaleType;
import com.oup.eac.ws.v2.binding.common.PasswordCredential;
import com.oup.eac.ws.v2.binding.common.WsUserId;
import com.oup.eac.ws.v2.endpoint.EacAccessEndpoint;
import com.oup.eac.ws.v2.ex.WebServiceException;
import com.oup.eac.ws.v2.service.ActivationCodeAdapter;
import com.oup.eac.ws.v2.service.ActivationCodeServiceAdapter;
import com.oup.eac.ws.v2.service.CreateUserAccountAdapter;
import com.oup.eac.ws.v2.service.CreateUserAccountWithConcurrencyAdapter;
import com.oup.eac.ws.v2.service.CredentialPolicyAdapter;
import com.oup.eac.ws.v2.service.ExternalProductIdsAdapter;
import com.oup.eac.ws.v2.service.ExternalUserIdsAdapter;
import com.oup.eac.ws.v2.service.FullUserEntitlementsAdapter;
//import com.oup.eac.ws.v2.service.GuestActivationCodeAdapter;
import com.oup.eac.ws.v2.service.KillUserSessionAdapter;
import com.oup.eac.ws.v2.service.ProductRegistrationAdapter;
import com.oup.eac.ws.v2.service.ResetPasswordWithUrlAdapter;
import com.oup.eac.ws.v2.service.UpdateUserAccountAdapter;
import com.oup.eac.ws.v2.service.UpdateUserAccountWithConcurrencyAdapter;
import com.oup.eac.ws.v2.service.UserEntitlementsAdapter;
import com.oup.eac.ws.v2.service.UserServiceAdapter;
import com.oup.eac.ws.v2.service.ValidatePasswordCredentialsAdapter;
import com.oup.eac.ws.v2.service.utils.ErrorStatusUtils;
import com.oup.eac.ws.v2.service.utils.LocaleUtils;

/**
 * The Class EacAccessEndpointImpl.
 */
@Endpoint
public class EacAccessEndpointImpl implements EacAccessEndpoint {

	private static final String NAMESPACE="http://eac.oup.com/2.0/eac-access-services";

	@Resource(name = "${ws.user.entitlements.adapter.bean.name}")
	private UserEntitlementsAdapter userEntitlementsAdapter;

	@Resource(name = "${ws.activationcode.service.adapter.bean.name}")
	private ActivationCodeServiceAdapter activationCodeServiceAdapter;

	@Resource(name = "${ws.activation.code.adapter.bean.name}")
	private ActivationCodeAdapter activationCodeAdapter;

	@Resource(name = "${ws.user.service.adapter.bean.name.v2}")
	private UserServiceAdapter userServiceAdapter;

	@Resource(name = "${ws.create.user.adapter.bean.name.v2}")
	private CreateUserAccountAdapter createUserAdapter;

	@Resource(name = "${ws.update.user.adapter.bean.name.v2}")
	private UpdateUserAccountAdapter updateUserAdapter;

	@Resource(name = "${ws.external.userids.adapter.bean.name.v2}")
	private ExternalUserIdsAdapter externalUserIdsAdapter;

	@Resource(name = "${ws.external.productids.adapter.bean.name.v2}")
	private ExternalProductIdsAdapter externalProductIdsAdapter;

	@Resource(name = "${ws.validate.password.credentials.adapter.bean.name.v2}")
	private ValidatePasswordCredentialsAdapter validatePasswordCredentialsAdapter;

	@Resource(name = "${ws.product.registration.adapter.bean.name.v2}")
	private ProductRegistrationAdapter productRegistrationAdapter;

	@Resource(name = "${ws.kill.user.session.adapter.bean.name.v2}")
	private KillUserSessionAdapter killUserSessionAdapter;

	@Resource(name = "${ws.create.user.with.concurrency.adapter.bean.name.v2}")
	private CreateUserAccountWithConcurrencyAdapter createUserWithConcurrencyAdapter;


	@Resource(name = "${ws.update.user.with.concurrency.adapter.bean.name.v2}")
	private UpdateUserAccountWithConcurrencyAdapter updateUserWithConcurrencyAdapter;

	@Resource(name = "${ws.full.user.entitlements.adapter.bean.name}")
	private FullUserEntitlementsAdapter fullUserEntitlementsAdapter;

	@Resource(name = "${ws.credential.policy.adapter.bean.name}")
	private CredentialPolicyAdapter credentialPolicyAdapter;
	
	@Resource(name = "${ws.reset.password.with.url.bean.name}")
	private ResetPasswordWithUrlAdapter resetPasswordWithUrlAdapter;
	
	@Resource
	private WebServiceContext wsContext;

	/**
	 * {@inheritDoc}
	 * @throws ErightsException 
	 * @throws AccessDeniedException 
	 */
	@PayloadRoot(localPart = "GetUserEntitlementsRequest", namespace = NAMESPACE)
	@ResponsePayload
	@Override
	public final GetUserEntitlementsResponse getUserEntitlements(@RequestPayload final GetUserEntitlementsRequest request) throws WebServiceException, AccessDeniedException, ErightsException {        
		String system = request.getSystemId();
		WsUserId wsUserId = request.getWsUserId();
		return userEntitlementsAdapter.getUserEntitlementGroups(system, wsUserId);
	}

	/**
	 * {@inheritDoc}
	 */
	@PayloadRoot(localPart = "CreateActivationCodeBatchRequest", namespace=NAMESPACE)
	@ResponsePayload
	@Override
	public final CreateActivationCodeBatchResponse createActivationCodeBatch(@RequestPayload final CreateActivationCodeBatchRequest request) 
			throws WebServiceException, ProductNotFoundException, UserNotFoundException, LicenseNotFoundException, 
			AccessDeniedException, GroupNotFoundException, ErightsException{
		ActivationCodeBatch activationCodeBatch = request.getActivationCodeBatch();
		ActivationCodeLicence activationCodeLicence = request.getActivationCodeLicence();
		return activationCodeServiceAdapter.createActivationCodeBatch(activationCodeBatch, activationCodeLicence);
	}

	/**
	 * {@inheritDoc}
	 */
	@PayloadRoot(localPart = "RedeemActivationCodeRequest", namespace=NAMESPACE)
	@ResponsePayload
	@Override
	public final RedeemActivationCodeResponse redeemActivationCode(@RequestPayload final RedeemActivationCodeRequest request) throws WebServiceException {
		long startTime = System.currentTimeMillis();
		WsUserId wsUserId  = request.getWsUserId();
		String systemId = request.getSystemId();
		String activationCode = request.getActivationCode();
		LocaleType localeInfo = request.getLocale();
		Locale locale = localeInfo == null ? Locale.getDefault() : LocaleUtils.getLocale(localeInfo);
		RedeemActivationCodeResponse response = null;
		try {
			response = activationCodeAdapter.redeemActivationCode(systemId,wsUserId,activationCode,locale);
		} catch (AccessDeniedException | ErightsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		AuditLogger.logEvent(":: Time to redeemActivationCode :: " + (System.currentTimeMillis() - startTime));
		return response;
	}

	/**
	 * {@inheritDoc}
	 */
	@PayloadRoot(localPart = "ValidateActivationCodeRequest", namespace=NAMESPACE)
	@ResponsePayload
	@Override
	public ValidateActivationCodeResponse validateActivationCode(@RequestPayload  final ValidateActivationCodeRequest request) throws WebServiceException {
		return activationCodeAdapter.validateActivationCode(request.getSystemId(), request.getActivationCode());
	}

	/**
	 * {@inheritDoc}
	 */
	@PayloadRoot(localPart = "SearchActivationCodeRequest", namespace=NAMESPACE)
	@ResponsePayload
	@Override
	public SearchActivationCodeResponse searchActivationCode(@RequestPayload  final SearchActivationCodeRequest request) throws WebServiceException {
		return activationCodeAdapter.searchActivationCode(request.getSystemId(), request.getActivationCode(), request.isLikeMatch());
	}

	/**
	 * {@inheritDoc}
	 */
	@PayloadRoot(localPart="AuthenticateRequest", namespace=NAMESPACE)
	@ResponsePayload
	@Override
	public final AuthenticateResponse authenticateUser(@RequestPayload AuthenticateRequest request) {

		Credential credential = request.getCredential();

		if(credential.getIpCredential() != null){
			AuthenticateResponse resp = new AuthenticateResponse();
			ErrorStatus errorStatus = ErrorStatusUtils.getClientErrorStatus("IP Credentials are not supported");            
			resp.setErrorStatus(errorStatus);
			return resp;
		}

		PasswordCredential passwordCredential = credential.getPasswordCredential();
		return userServiceAdapter.authenticate(passwordCredential.getUserName(), passwordCredential.getPassword());
	}

	/**
	 * {@inheritDoc}
	 */
	@PayloadRoot(localPart="LogoutRequest", namespace=NAMESPACE)
	@ResponsePayload
	@Override
	public final LogoutResponse logout(@RequestPayload LogoutRequest request) {
		return userServiceAdapter.logout(request.getSessionToken());
	}

	/**
	 * {@inheritDoc}
	 */
	@PayloadRoot(localPart="ChangePasswordRequest", namespace=NAMESPACE)
	@ResponsePayload
	@Override
	public final ChangePasswordResponse changePassword(@RequestPayload ChangePasswordRequest request) {
		return userServiceAdapter.changePassword(request.getWsUserId(), request.getNewPassword());
	}

	/**
	 * {@inheritDoc}
	 */
	@PayloadRoot(localPart="ResetPasswordRequest", namespace=NAMESPACE)
	@ResponsePayload
	@Override
	public final ResetPasswordResponse resetPassword(@RequestPayload ResetPasswordRequest request) {
		//Added as part of OLB requirement, need to revert after Live
		Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
		return userServiceAdapter.resetPassword(request.getWsUserId(), currentUser.getName());
	}

	/**
	 * {@inheritDoc}
	 */
	@PayloadRoot(localPart="CreateUserAccountRequest", namespace=NAMESPACE)
	@ResponsePayload
	@Override
	public final CreateUserAccountResponse createUserAccount(@RequestPayload CreateUserAccountRequest request){
		long startTime = System.currentTimeMillis();
		CreateUser[] users = request.getUser();
		CreateUserAccountResponse result =  createUserAdapter.createUserAccounts(users);
		AuditLogger.logEvent(":: Time to CreateUserAccountRequest :: " + (System.currentTimeMillis() - startTime));
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@PayloadRoot(localPart="UpdateUserAccountRequest", namespace=NAMESPACE)
	@ResponsePayload
	@Override
	public UpdateUserAccountResponse updateUserAccount(@RequestPayload UpdateUserAccountRequest request) throws WebServiceException {
		long startTime = System.currentTimeMillis();
		UpdateUserAccountResponse result = this.updateUserAdapter.updateUserAccount(request);
		AuditLogger.logEvent(":: Time to UpdateUserAccountRequest :: " + (System.currentTimeMillis() - startTime));
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@PayloadRoot(localPart="SetExternalUserIdsRequest", namespace=NAMESPACE)
	@ResponsePayload
	@Override
	public SetExternalUserIdsResponse setExternalUserIds(@RequestPayload SetExternalUserIdsRequest request) throws WebServiceException {
		long startTime = System.currentTimeMillis();
		SetExternalUserIdsResponse result = this.externalUserIdsAdapter.setExternalUserIds(request);
		AuditLogger.logEvent(":: Time to SetExternalUserIdsRequest :: " + (System.currentTimeMillis() - startTime));
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@PayloadRoot(localPart="SetExternalProductIdsRequest", namespace=NAMESPACE)
	@ResponsePayload
	@Override
	public SetExternalProductIdsResponse setExternalProductIds(@RequestPayload SetExternalProductIdsRequest request) throws WebServiceException {
		long startTime = System.currentTimeMillis();
		SetExternalProductIdsResponse result = this.externalProductIdsAdapter.setExternalProductIds(request);
		AuditLogger.logEvent(":: Time to SetExternalProductIdsRequest :: " + (System.currentTimeMillis() - startTime));
		return result;
	}


	@PayloadRoot(localPart="ValidatePasswordCredentialsRequest", namespace=NAMESPACE)
	@ResponsePayload
	@Override
	public ValidatePasswordCredentialsResponse validate(@RequestPayload ValidatePasswordCredentialsRequest request) throws WebServiceException {
		long startTime = System.currentTimeMillis();
		ValidatePasswordCredentialsResponse result = this.validatePasswordCredentialsAdapter.validate(request);
		AuditLogger.logEvent(":: Time to ValidatePasswordCredentialsRequest :: " + (System.currentTimeMillis() - startTime));
		return result;
	}

	@PayloadRoot(localPart="ProductRegistrationRequest", namespace=NAMESPACE)
	@ResponsePayload
	@Override
	public ProductRegistrationResponse createProductRegistration(@RequestPayload ProductRegistrationRequest request) throws WebServiceException {
		long startTime = System.currentTimeMillis();
		ProductRegistrationResponse result = this.productRegistrationAdapter.createProductRegistration(request);
		AuditLogger.logEvent(":: Time to ProductRegistrationRequest :: " + (System.currentTimeMillis() - startTime));
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@PayloadRoot(localPart="KillUserSessionRequest", namespace=NAMESPACE)
	@ResponsePayload
	@Override
	public final KillUserSessionResponse killUserSession(@RequestPayload KillUserSessionRequest request) throws WebServiceException {
		long startTime = System.currentTimeMillis();
		KillUserSessionResponse result = this.killUserSessionAdapter.killUserSession(request);
		AuditLogger.logEvent(":: Time to KillUserSessionRequest :: " + (System.currentTimeMillis() - startTime));
		return result;                
	}


	/**
	 * {@inheritDoc}
	 */
	@PayloadRoot(localPart="CreateUserAccountWithConcurrencyRequest", namespace=NAMESPACE)
	@ResponsePayload
	@Override
	public final CreateUserAccountWithConcurrencyResponse createUserAccountWithConcurrency(@RequestPayload CreateUserAccountWithConcurrencyRequest request){
		long startTime = System.currentTimeMillis();
		CreateUserWithConcurrency[] users = request.getUser();
		CreateUserAccountWithConcurrencyResponse result =  createUserWithConcurrencyAdapter.createUserAccountsWithConcurrency(users);
		AuditLogger.logEvent(":: Time to CreateUserAccountWithConcurrencyRequest :: " + (System.currentTimeMillis() - startTime));
		return result;
	}



	/**
	 * {@inheritDoc}
	 */
	@PayloadRoot(localPart="UpdateUserAccountWithConcurrencyRequest", namespace=NAMESPACE)
	@ResponsePayload
	@Override
	public UpdateUserAccountWithConcurrencyResponse updateUserAccountWithConcurrency(@RequestPayload UpdateUserAccountWithConcurrencyRequest request) throws WebServiceException {
		long startTime = System.currentTimeMillis();
		UpdateUserAccountWithConcurrencyResponse result = this.updateUserWithConcurrencyAdapter.updateUserAccountWithConcurrency(request);
		AuditLogger.logEvent(":: Time to UpdateUserAccountWithConcurrencyRequest :: " + (System.currentTimeMillis() - startTime));
		return result;
	}


	/**
	 * {@inheritDoc}
	 * @throws ErightsException 
	 * @throws AccessDeniedException 
	 */
	@PayloadRoot(localPart = "GetFullUserEntitlementsRequest", namespace = NAMESPACE)
	@ResponsePayload
	@Override
	public final GetFullUserEntitlementsResponse getFullUserEntitlements(@RequestPayload final GetFullUserEntitlementsRequest request) throws WebServiceException, AccessDeniedException, ErightsException {               
		
		WsUserId wsUserId = request.getWsUserId();
		Set<String> systemIdSet = new HashSet<String>(Arrays.asList(request.getSystemId()));
		Set<String> productSystemIdSet = new HashSet<String>(Arrays.asList(request.getProductSystemId()));
		Set<String> productOrgUnitSet = new HashSet<String>(Arrays.asList(request.getProductOrgUnit()));
		String licenceState = request.getLicenceState();
		return fullUserEntitlementsAdapter.getFullUserEntitlementGroups(wsUserId, systemIdSet, productSystemIdSet, productOrgUnitSet, licenceState);
	}


	/**
	 * {@inheritDoc}
	 */
	@PayloadRoot(localPart = "GuestRedeemActivationCodeRequest", namespace=NAMESPACE)
	@ResponsePayload
	@Override
	public final GuestRedeemActivationCodeResponse guestRedeemActivationCode(@RequestPayload final GuestRedeemActivationCodeRequest request) throws WebServiceException {
		long startTime = System.currentTimeMillis();
		String activationCode = request.getActivationCode();
		GuestRedeemActivationCodeResponse response = activationCodeAdapter.guestRedeemActivationCode(activationCode);
		AuditLogger.logEvent(":: Time to GuestRedeemActivationCodeRequest :: " + (System.currentTimeMillis() - startTime));
		return response;
	}

	/**
	 * {@inheritDoc}
	 */
	@PayloadRoot(localPart="GetCredentialPolicyRequest", namespace=NAMESPACE)
	@ResponsePayload 
	@Override
	public final GetCredentialPolicyResponse getCredentialPolicy(@RequestPayload final GetCredentialPolicyRequest request) throws WebServiceException {
		long startTime = System.currentTimeMillis();
		GetCredentialPolicyResponse response=  credentialPolicyAdapter.getCredentialPolicy(request);
		AuditLogger.logEvent(":: Time to GetCredentialPolicyRequest :: " + (System.currentTimeMillis() - startTime));
		return response;
	}

	@PayloadRoot(localPart="ResetPasswordWithUrlRequest", namespace=NAMESPACE)
	@ResponsePayload 
	@Override
	public ResetPasswordWithUrlResponse resetPassword(@RequestPayload final ResetPasswordWithUrlRequest request) throws WebServiceException {
		long startTime = System.currentTimeMillis();
		ResetPasswordWithUrlResponse response = resetPasswordWithUrlAdapter.resetPassword(request);
		AuditLogger.logEvent(":: Time to ResetPasswordWithUrlRequest :: " + (System.currentTimeMillis() - startTime));
		return response;
	}


}
