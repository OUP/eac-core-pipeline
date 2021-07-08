package com.oup.eac.integration.facade.impl;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Semaphore;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.util.Assert;

import com.oup.eac.common.date.utils.DateUtils;
import com.oup.eac.common.utils.exception.ExceptionUtil;
import com.oup.eac.domain.ActivationCode;
import com.oup.eac.domain.AdminUser;
import com.oup.eac.domain.ConcurrentLicenceTemplate;
import com.oup.eac.domain.ExternalSystem;
import com.oup.eac.domain.ExternalSystemIdType;
import com.oup.eac.domain.LicenceTemplate;
import com.oup.eac.domain.RegisterableProduct;
import com.oup.eac.domain.RollingLicenceTemplate;
import com.oup.eac.domain.UsageLicenceTemplate;
import com.oup.eac.domain.LicenceTemplate.LicenceType;
import com.oup.eac.domain.utils.audit.AuditLogger;
import com.oup.eac.dto.ActivationCodeBatchDto;
import com.oup.eac.dto.AuthenticationResponseDto;
import com.oup.eac.dto.CustomerDto;
import com.oup.eac.dto.DivisionDto;
import com.oup.eac.dto.EnforceableProductDto;
import com.oup.eac.dto.ExternalIdDto;
import com.oup.eac.dto.GroupDto;
import com.oup.eac.dto.GuestRedeemActivationCodeDto;
import com.oup.eac.dto.LicenceDetailDto;
import com.oup.eac.dto.LicenceDto;
import com.oup.eac.dto.LicenceDtoDateTime;
import com.oup.eac.dto.LoginPasswordCredentialDto;
import com.oup.eac.dto.ProductGroupDto;
import com.oup.eac.dto.RollingLicenceDetailDto;
import com.oup.eac.dto.StandardConcurrentLicenceDetailDto;
import com.oup.eac.dto.UsageLicenceDetailDto;
import com.oup.eac.dto.WsUserIdDto;
import com.oup.eac.integration.erights.*;
import com.oup.eac.integration.facade.ErightsFacade;
import com.oup.eac.integration.facade.exceptions.ChildProductFoundException;
import com.oup.eac.integration.facade.exceptions.DivisionAlreadyExistsException;
import com.oup.eac.integration.facade.exceptions.DivisionNotFoundException;
import com.oup.eac.integration.facade.exceptions.ErightsException;
import com.oup.eac.integration.facade.exceptions.ErightsSessionNotFoundException;
import com.oup.eac.integration.facade.exceptions.GroupNotFoundException;
import com.oup.eac.integration.facade.exceptions.InvalidCredentialsException;
import com.oup.eac.integration.facade.exceptions.LicenseNotFoundException;
import com.oup.eac.integration.facade.exceptions.ParentGroupNotFoundException;
import com.oup.eac.integration.facade.exceptions.ParentProductNotFoundException;
import com.oup.eac.integration.facade.exceptions.PasswordPolicyViolatedException;
import com.oup.eac.integration.facade.exceptions.ProductNotFoundException;
import com.oup.eac.integration.facade.exceptions.SessionConcurrencyException;
import com.oup.eac.integration.facade.exceptions.UserAlreadyExistsException;
import com.oup.eac.integration.facade.exceptions.UserLoginCredentialAlreadyExistsException;
import com.oup.eac.integration.facade.exceptions.UserNotFoundException;
import com.oup.eac.integration.utils.datasync.UserDataSychronisable;


/**
 * Erights facade.
 * 
 * @author harlandd
 * @author Ian Packard
 */
@org.springframework.stereotype.Service(value="erightsFacade")
public class ErightsFacadeImpl implements ErightsFacade {

	private static final Logger LOGGER = Logger.getLogger(ErightsFacadeImpl.class);
	private DateUtils dateConverter ;
	
	private final ConcurrentMap<String,Semaphore> customerLockMap = new ConcurrentHashMap<String, Semaphore>();;

	/*@Autowired
	@Qualifier("networkTolerantErightsPort")
	private OupRightAccessServiceDefinition erightsPort;*/

	@Autowired
	@Qualifier("acesNetworkTolerantErightsPort")
	private AccessServiceV10 acesErightsPort;

	/**
	 * @param sessionKey
	 *            the session key
	 * @throws ErightsException
	 *             the exception
	 */
	@Override
	public final void logout(final String sessionKey) throws ErightsException {
		LogoutRequest request = new LogoutRequest();
		request.setSessionToken(sessionKey);
		LogoutResponse response = getAcesEndpoint().logout(request);
		/*LogoutResponseWS response = getEndpoint().logout(sessionKey);*/
		switch (response.getStatus()) {
		case SUCCESS:
			break;
		case SESSION_NOT_FOUND:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new ErightsSessionNotFoundException(response.getErrorMessage(), response.getStatus().value());
		default:
			LOGGER.error("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new ErightsException(response.getErrorMessage(), response.getStatus().value());
		}
	}

	/**
	 * @param userId
	 *            the userId
	 * @param password
	 *            the password
	 * @throws ErightsException
	 *             the exception
	 */
	@Override
	public final void changePasswordByUserId(final String userId, final String password) throws ErightsException,PasswordPolicyViolatedException {
		ChangePasswordByUserIdRequest request = new ChangePasswordByUserIdRequest();
		request.setPassword(password);
		request.setUserId(userId);
		ChangePasswordByUserIdResponse response = getAcesEndpoint().changePasswordByUserId(request);
		/*ChangePasswordResponseWS response = getEndpoint().changePasswordByUserId(erightsUserId.intValue(), password);*/
		switch (response.getStatus()) {
		case SUCCESS:
			runUserDataSync(null,userId,password,null);
			break;
		case USER_NOT_FOUND:
		case PASSWORD_VERIFICATION_FAILED:
		case LOGIN_PASSWORD_CREDENTIAL_NOT_FOUND:
		case MULTIPLE_LOGIN_PASSWORD_CREDENTIALS_FOUND:
		case MULTIPLE_USERS_FOUND:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new ErightsException(response.getErrorMessage(), response.getStatus().value());
		case PASSWORD_POLICY_VIOLATED:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new PasswordPolicyViolatedException(response.getErrorMessage() );
		default:
			LOGGER.error("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new ErightsException(response.getErrorMessage(), response.getStatus().value(), response.getErrorCode());
		}
	}
	
	
	/**
	 * @param userId
	 *            the userId
	 * @param password
	 *            the password
	 * @throws ErightsException
	 *             the exception
	 */
	@Override
	public final void resetPassword(final String userId, final String token,final String successUrl,final String failureUrl) throws ErightsException,PasswordPolicyViolatedException {
		ResetPasswordRequest request = new ResetPasswordRequest();
		request.setUserId(userId);
		request.setTokenId(token);
		request.setSuccessUrl(successUrl);
		request.setFailureUrl(failureUrl);
		ResetPasswordResponse response = getAcesEndpoint().resetPassword(request);
		/*ChangePasswordResponseWS response = getEndpoint().changePasswordByUserId(erightsUserId.intValue(), password);*/
		switch (response.getStatus()) {
		case SUCCESS:
			break;
		case USER_NOT_FOUND:
		case PASSWORD_VERIFICATION_FAILED:
		case LOGIN_PASSWORD_CREDENTIAL_NOT_FOUND:
		case MULTIPLE_LOGIN_PASSWORD_CREDENTIALS_FOUND:
		case MULTIPLE_USERS_FOUND:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new ErightsException(response.getErrorMessage(), response.getStatus().value());
		case PASSWORD_POLICY_VIOLATED:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new PasswordPolicyViolatedException(response.getErrorMessage());
		default:
			LOGGER.error("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new ErightsException(response.getErrorMessage(), response.getStatus().value());
		}
	}
	/**
	 * @param erightsUserId
	 *            the erightsUserId
	 * @param licenceTemplate
	 *            the licenceTemplate
	 * @param productIds
	 *            the productIds
	 * @param enabled
	 *            is the licence enabled
	 * @param erightsGroupId
	 * 			  the group id           	
	 * @return the licence id
	 * @throws ErightsException
	 *             The exception
	 * @throws MalformedURLException
	 */
	@Override
	public final String addLicense(final String userId, final LicenceTemplate licenceTemplate, final List<String> productIds, final boolean enabled)
			throws ErightsException {

		AddLicenseRequest request = new AddLicenseRequest();
		OupLicenseWS oupLicenseWS= ErightsObjectFactory.getOupLicenseWS(licenceTemplate, productIds, enabled);
		request.setUserId(userId);
		request.setLicense(oupLicenseWS);
		AddLicenseResponse response = getAcesEndpoint().addLicense(request);
		/*AddLicenseResponseWS response = getEndpoint().addLicense(erightsUserId.intValue(), ErightsObjectFactory.getOupLicenseWS(licenceTemplate, productIds, enabled));*/
		switch (response.getStatus()) {
		case SUCCESS:
			return response.getLicenseId();
		case USER_NOT_FOUND:
		case PRODUCT_NOT_FOUND:
		case LICENSE_TEMPLATE_NOT_FOUND:
		default:
			LOGGER.error("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new ErightsException(response.getErrorMessage(), response.getStatus().value());
		}
	}

	@Override
	public CustomerDto createUserAccount(final CustomerDto customerDto)
			throws ErightsException, UserAlreadyExistsException {
		OupUserWS oupUserWS = ErightsObjectFactory.getOupUserWS(customerDto);
		CreateUserAccountRequest createUserAccountRequest = new CreateUserAccountRequest();
		createUserAccountRequest.setUser(oupUserWS);
		CreateUserAccountResponse response = getAcesEndpoint().createUserAccount(createUserAccountRequest);
		switch (response.getStatus()) {
		case SUCCESS:
			return new CustomerDto(response.getUserId(), customerDto);
		/*case USER_ALREADY_EXISTS:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new UserAlreadyExistsException(response.getErrorMessage());*/
		default:
			LOGGER.error("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new ErightsException(response.getErrorMessage(), response.getStatus().value(), response.getErrorCode());
		}
	}

	@Override
	public final void updateUserAccount(final CustomerDto customerDto) throws ErightsException, GroupNotFoundException, UserLoginCredentialAlreadyExistsException, UserNotFoundException, PasswordPolicyViolatedException {
		OupUserWS oupUserWS = ErightsObjectFactory.getOupUserWS(customerDto);
		UpdateUserAccountRequest updateUserAccountRequest = new UpdateUserAccountRequest();
		updateUserAccountRequest.setUser(oupUserWS);
		UpdateUserAccountResponse response = getAcesEndpoint().updateUserAccount(updateUserAccountRequest);
		switch (response.getStatus()) {
		case SUCCESS:
			runUserDataSync(null,null,null,customerDto);
			return;
		case USER_NOT_FOUND:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new UserNotFoundException(response.getErrorMessage());
		case GROUP_NOT_FOUND:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new GroupNotFoundException(response.getErrorMessage());
		case USER_LOGIN_CREDENTIAL_ALREADY_EXIST:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new UserLoginCredentialAlreadyExistsException(response.getErrorMessage());

		case PASSWORD_POLICY_VIOLATED:
			LOGGER.debug("Password Policy Violated. Error message: " + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new PasswordPolicyViolatedException(response.getErrorMessage());
		default:
			LOGGER.error("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value() + "ErrorCode : " + response.getErrorCode());
			throw new ErightsException(response.getErrorMessage(), response.getStatus().value(), response.getErrorCode());
		}
	}

	/**
	 * @param erightsUserId
	 *            the erightsUserId
	 * @param erightsLicenceId
	 *            the erightsLicenceId
	 * @throws ErightsException
	 *             the exception
	 */
	@Override
	public final void activateLicense(final String userId, final String licenceId) throws UserNotFoundException, LicenseNotFoundException, ErightsException {
		ActivateLicenseRequest request = new ActivateLicenseRequest();
		request.setLicenseId(licenceId);
		request.setUserId(userId);
		ActivateLicenseResponse response = getAcesEndpoint().activateLicense(request);
		switch (response.getStatus()) {
		case SUCCESS:
			break;
		case USER_NOT_FOUND:
			LOGGER.error("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new UserNotFoundException(response.getErrorMessage());
		case LICENSE_NOT_FOUND:
			LOGGER.error("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new LicenseNotFoundException(response.getErrorMessage());
		default:
			LOGGER.error("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new ErightsException(response.getErrorMessage(), response.getStatus().value());
		}
	}

	/**
	 * @param username
	 *            the username
	 * @param password
	 *            the password
	 * @throws ErightsException
	 *             the exception
	 */
	@Override
	public final void changePasswordByUsername(final String username, final String password) throws ErightsException,PasswordPolicyViolatedException {
		ChangePasswordByUsernameRequest request = new ChangePasswordByUsernameRequest();
		request.setPassword(password);
		request.setUsername(username);
		ChangePasswordByUsernameResponse response = getAcesEndpoint().changePasswordByUsername(request);
		/*ChangePasswordResponseWS response = getEndpoint().changePasswordByUsername(username, password);*/
		switch (response.getStatus()) {
		case SUCCESS:
			runUserDataSync(username,null,password,null);
			break;
		case PASSWORD_POLICY_VIOLATED:
			LOGGER.debug("Password Policy Violated. Error message: " + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new PasswordPolicyViolatedException(response.getErrorMessage());
		case USER_NOT_FOUND:
		case PASSWORD_VERIFICATION_FAILED:
		case LOGIN_PASSWORD_CREDENTIAL_NOT_FOUND:
		case MULTIPLE_LOGIN_PASSWORD_CREDENTIALS_FOUND:
		case MULTIPLE_USERS_FOUND:
		default:
			LOGGER.error("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new ErightsException(response.getErrorMessage(), response.getStatus().value());
		}
	}


	/**
	 * @param username
	 *            the username
	 * @param password
	 *            the password
	 * @return ErightsDecisionDto
	 * @throws ErightsException
	 *             the exception
	 * @throws InvalidCredentialsException
	 *             the exception
	 */
	@Override
	public final AuthenticationResponseDto authenticateUser(final LoginPasswordCredentialDto loginPasswordCredential) throws SessionConcurrencyException, ErightsException, InvalidCredentialsException {
		AuthenticationRequest request = new AuthenticationRequest();
		OupCredentialWS oupCredentialWS = ErightsObjectFactory.getOupCredentialWS(loginPasswordCredential);
		request.setCredentialWS(oupCredentialWS);
		AuthenticationResponse response = getAcesEndpoint().authentication(request);
		/*AuthenticationResponseWS response = getEndpoint().authenticate(ErightsObjectFactory.getOldCustomApiOupCredentialWS(loginPasswordCredential));*/
		switch (response.getStatus()) {
		case SUCCESS:
			return ErightsObjectFactory.getAuthenticationResponse(response);
		case FAILURE_ADMIN_TYPE_USER_CANNOT_LOGIN:
		case FAILURE_EXPLICIT_USER_NOT_FOUND:
		case FAILURE_NO_AUTH_PROFILE_MATCHED:
		case FAILURE_NO_CREDENTIALS:
		case FAILURE_NO_CREDENTIALS_MATCHED:
		case FAILURE_TOO_MANY_GLOBAL_POLICY:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new InvalidCredentialsException(response.getErrorMessage(), response.getStatus().value());
		case FAILURE_SESSION_CONCURRENCY_EXCEEDED:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new SessionConcurrencyException(response.getErrorMessage(), response.getStatus().value());
		case FAILURE_MAX_USERS_IN_SESSION_EXCEEDED:
		case FAILURE_NO_LICENSES_FOUND:
		case FAILURE_NO_USERS_IN_SESSION:
			//case FAILURE_INTERNAL_ERROR:
			//case FAILURE_TIMEOUT: 
			//case ERROR:
		default:
			LOGGER.error("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new ErightsException(response.getErrorMessage(), response.getStatus().value());
		}
	}

	/**
	 * @param sessionKey
	 *            the sessionKey
	 * @return customer ids
	 * @throws ErightsException
	 *             the exception
	 */

	@Override
	public final List<String> getCustomerIdsFromSession(final String sessionKey) throws ErightsException {
		if(StringUtils.isBlank(sessionKey)){
			return null;
		}
		GetUserIdsFromSessionRequest request = new GetUserIdsFromSessionRequest();
		request.setSessionToken(sessionKey);
		GetUserIdsFromSessionResponse response = getAcesEndpoint().getUserIdsFromSession(request);
		/*UserIdsFromSessionResponseWS response = getEndpoint().getUserIdsFromSession(sessionKey);*/
		switch (response.getStatus()) {
		case SUCCESS:
			return response.getUserIds();
		case SESSION_NOT_FOUND:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new ErightsException(response.getErrorMessage(), response.getStatus().value());
		default:
			LOGGER.error("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new ErightsException(response.getErrorMessage(), response.getStatus().value());
		}
	}

	/**
	 * @param url
	 *            the url
	 * @return product ids
	 * @throws ErightsException
	 *             the exception
	 */
	@Override
	public final List<String> getProductIdsByUrl(final String url) throws ErightsException {
		GetProductsFromURLRequest request= new GetProductsFromURLRequest();
		request.setUrl(url);
		GetProductsFromURLResponse response = getAcesEndpoint().getProductsFromURL(request);
		/*ProductsFromURLResponseWS response = getEndpoint().getProductsFromURL(url);*/
		switch (response.getStatus()) {
		case SUCCESS:
			return response.getProductIds();
		case NO_PRODUCTS_FOUND:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new ErightsException(response.getErrorMessage(), response.getStatus().value());
		default:
			LOGGER.error("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new ErightsException(response.getErrorMessage(), response.getStatus().value());
		}
	}

	@Override
	public GroupDto getGroup(final Integer erightsGroupId) throws ErightsException, GroupNotFoundException {
		GetGroupRequest request= new GetGroupRequest();
		request.setGroupId(erightsGroupId.intValue());
		GetGroupResponse response = getAcesEndpoint().getGroup(request);
		/*GetGroupResponseWS response = getEndpoint().getGroup(erightsGroupId.intValue());*/
		switch (response.getStatus()) {
		case SUCCESS:
			return ErightsObjectFactory.getGroupDto(response);
		case GROUP_NOT_FOUND:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new GroupNotFoundException(response.getErrorMessage());
		default:
			LOGGER.error("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new ErightsException(response.getErrorMessage(), response.getStatus().value());
		}
	}



	@Override
	public GroupDto createGroup(GroupDto groupDto) throws ErightsException, ParentGroupNotFoundException {
		OupGroupWS groupWS = ErightsObjectFactory.getGroup(groupDto);
		CreateGroupRequest request = new CreateGroupRequest();
		request.setOupGroupWS(groupWS);
		CreateGroupResponse response = getAcesEndpoint().createGroup(request);
		/*CreateGroupResponseWS response = getEndpoint().createGroup(ErightsObjectFactory.getGroup(groupDto));*/

		switch (response.getStatus()) {
		case SUCCESS:
			return new GroupDto(response.getGroupId(), groupDto);
		case PARENT_GROUP_NOT_FOUND:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new ParentGroupNotFoundException(response.getErrorMessage());
		default:
			LOGGER.error("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new ErightsException(response.getErrorMessage(), response.getStatus().value());
		}
	}



	@Override
	public EnforceableProductDto createProduct(EnforceableProductDto enforceableProduct, LicenceDetailDto licenceDetailDto)
			throws ErightsException, ParentProductNotFoundException {

		CreateProductRequest request = new CreateProductRequest();
		OupProductLicenseWS oupProductLicenseWS = new OupProductLicenseWS();
		if(licenceDetailDto.getStartDate() != null)
		{
			oupProductLicenseWS.setStartDate(dateConverter.safeConvertLocalDate(licenceDetailDto.getStartDate()));
		}
		if(licenceDetailDto.getEndDate() != null)
		{
			oupProductLicenseWS.setEndDate(dateConverter.safeConvertLocalDate(licenceDetailDto.getEndDate()));
		}
		oupProductLicenseWS.setLicenseDetail(ErightsObjectFactory.getOupLiceseDetail(licenceDetailDto));	
		
		oupProductLicenseWS.setLicenseDetail(ErightsObjectFactory.getOupLiceseDetail(licenceDetailDto));	
		OupProductWS oupProductWS = ErightsObjectFactory.getProduct(enforceableProduct);
		oupProductWS.setProductLicense(oupProductLicenseWS);
		request.setProduct(oupProductWS);
		CreateProductResponse response = getAcesEndpoint().createProduct(request);
		/*CreateProductResponseWS response = getEndpoint().createProduct(ErightsObjectFactory.getProduct(enforceableProduct));*/

		switch (response.getStatus()) {
		case SUCCESS:
		return new EnforceableProductDto(response.getProductId(), enforceableProduct);
		case PARENT_PRODUCT_NOT_FOUND:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new ParentProductNotFoundException(response.getErrorMessage());
		default:
			LOGGER.error("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new ErightsException(response.getErrorMessage(), response.getStatus().value());
		}
	}

	@Override
	public void deactivateLicense(final String userId,
			final String licenceId) throws ErightsException, LicenseNotFoundException, UserNotFoundException {
		DeactivateLicenseRequest request = new DeactivateLicenseRequest();
		request.setLicenseId(licenceId);
		request.setUserId(userId);
		DeactivateLicenseResponse response = getAcesEndpoint().deactivateLicense(request);
		/*DeactivateLicenseResponseWS response = getEndpoint().deactivateLicense(erightsUserId.intValue(), erightsLicenceId.intValue());*/

		switch (response.getStatus()) {
		case SUCCESS:
			return;	 
		case LICENSE_NOT_FOUND:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new LicenseNotFoundException(response.getErrorMessage());
		case USER_NOT_FOUND:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new UserNotFoundException(response.getErrorMessage());
		default:
			LOGGER.error("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new ErightsException(response.getErrorMessage(), response.getStatus().value());
		}

	}

	@Override
	public void deleteGroup(final Integer erightsGroupId) throws ErightsException, GroupNotFoundException {
		DeleteGroupRequest request = new DeleteGroupRequest();
		request.setGroupId(erightsGroupId.intValue());
		DeleteGroupResponse response = getAcesEndpoint().deleteGroup(request);
		/*DeleteGroupResponseWS response = getEndpoint().deleteGroup(erightsGroupId.intValue());*/

		switch (response.getStatus()) {
		case SUCCESS:
			return;
		case GROUP_NOT_FOUND:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new GroupNotFoundException(response.getErrorMessage());	        
		default:
			LOGGER.error("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new ErightsException(response.getErrorMessage(), response.getStatus().value());
		}
	}

	@Override
	public void deleteProduct(final String productId) throws ErightsException, ProductNotFoundException, ChildProductFoundException {
		DeleteProductRequest request = new DeleteProductRequest();
		request.setProductId(productId);
		DeleteProductResponse response = getAcesEndpoint().deleteProduct(request);
		/*DeleteProductResponseWS response = getEndpoint().deleteProduct(erightsProductId.intValue());*/

		switch (response.getStatus()) {
		case SUCCESS:
			return;
		case CHILD_PRODUCT_FOUND:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new ChildProductFoundException(response.getErrorMessage(), response.getChildrenIds());
		case PRODUCT_NOT_FOUND:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new ProductNotFoundException(response.getErrorMessage());
		default:
			LOGGER.error("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new ErightsException(response.getErrorMessage(), response.getStatus().value(), response.getErrorCode());
		}		
	}

	@Override
	public void deleteUserAccount(final String erightsUserId)
			throws ErightsException, UserNotFoundException {
		DeleteUserAccountRequest deleteUserAccountRequest =  new DeleteUserAccountRequest();
		deleteUserAccountRequest.setUserId(erightsUserId);
		DeleteUserAccountResponse response = getAcesEndpoint().deleteUserAccount(deleteUserAccountRequest);

		switch (response.getStatus()) {
		case SUCCESS:
			return;
		case USER_NOT_FOUND:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new UserNotFoundException(response.getErrorMessage());
		default:
			LOGGER.error("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new ErightsException(response.getErrorMessage(), response.getStatus().value());
		}

	}

	@Override
	public List<Integer> getGroupUsers(final Integer erightsGroupId,
			Boolean includeIndirectParents) throws ErightsException,
			GroupNotFoundException, UserNotFoundException {
		GetGroupUsersRequest request = new GetGroupUsersRequest();
		request.setGroupId(erightsGroupId.intValue());
		request.setIncludeIndirectParents(includeIndirectParents);
		GetGroupUsersResponse response = getAcesEndpoint().getGroupUsers(request);
		/*GetGroupUsersResponseWS response = getEndpoint().getGroupUsers(erightsGroupId.intValue(), includeIndirectParents);*/

		switch (response.getStatus()) {
		case SUCCESS:
			return null ; //response.getUserIds();
		case GROUP_NOT_FOUND:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new GroupNotFoundException(response.getErrorMessage());
		case USER_NOT_FOUND:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new UserNotFoundException(response.getErrorMessage());
		default:
			LOGGER.error("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new ErightsException(response.getErrorMessage(), response.getStatus().value());
		}
	}

	@Override
	public List<LicenceDto> getLicensesForUser(final String userId, final String licenceId)
			throws LicenseNotFoundException, UserNotFoundException,
			ErightsException {
		GetLicensesForUserRequest request = new GetLicensesForUserRequest();
		request.setUserId(userId);
		if(licenceId!= null){
			request.setLicenseId(licenceId);
		}
		GetLicensesForUserResponse response = getAcesEndpoint().getLicensesForUser(request);
		/*GetLicensesForUserResponseWS response = getEndpoint().getLicensesForUser(erightsUserId.intValue());*/

		switch (response.getStatus()) {
		case SUCCESS:
			return ErightsObjectFactory.getLicences(response.getLicenses());
		case LICENSE_NOT_FOUND:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new LicenseNotFoundException(response.getErrorMessage());
		case USER_NOT_FOUND:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new UserNotFoundException(response.getErrorMessage());
		default:
			LOGGER.error("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new ErightsException(response.getErrorMessage(), response.getStatus().value());
		}
	}

	@Override
	public List<LicenceDto> getLicensesForUserProduct(String userId, String productId) throws ErightsException, UserNotFoundException, 
	ProductNotFoundException {
		GetLicensesForUserProductRequest request = new GetLicensesForUserProductRequest();
		request.setProductId(productId);
		request.setUserId(userId);
		
		GetLicensesForUserProductResponse response = getAcesEndpoint().getLicensesForUserProduct(request);
		/*LicensesForUserProductResponseWS response = getEndpoint().getLicensesForUserProduct(erightsUserId.intValue(), erightsProductId.intValue());*/

		switch (response.getStatus()) {
		case SUCCESS:
			return ErightsObjectFactory.getLicences(response.getLicenses());
		case PRODUCT_NOT_FOUND:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new ProductNotFoundException(response.getErrorMessage());
		case USER_NOT_FOUND:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new UserNotFoundException(response.getErrorMessage());
		default:
			LOGGER.error("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new ErightsException(response.getErrorMessage(), response.getStatus().value());
		}
	}

	@Override
	public EnforceableProductDto getProduct(String productId) throws ProductNotFoundException, ErightsException {
		GetProductRequest request = new GetProductRequest();
		request.setProductId(productId);
		GetProductResponse response = getAcesEndpoint().getProduct(request);
		/*GetProductResponseWS response = getEndpoint().getProduct(erightsProductId.intValue());*/

		switch (response.getStatus()){
		case SUCCESS:
			return ErightsObjectFactory.getProduct(response.getProduct());
		case PRODUCT_NOT_FOUND:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
				throw new ProductNotFoundException(productId);
		default:
			LOGGER.error("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new ErightsException(response.getErrorMessage(), response.getStatus().value());
		}
	}

	@Override
	public CustomerDto getUserAccount(String userId) throws UserNotFoundException, ErightsException {
		GetUserAccountRequest getUserAccountRequest = new GetUserAccountRequest();
		getUserAccountRequest.setUserId(userId);
		GetUserAccountResponse response = getAcesEndpoint().getUserAccount(getUserAccountRequest);

		switch (response.getStatus()) {
		case SUCCESS:
			return ErightsObjectFactory.getCustomerDto(response.getUser());
		case USER_NOT_FOUND:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new UserNotFoundException(response.getErrorMessage());
		default:
			LOGGER.error("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new ErightsException(response.getErrorMessage(), response.getStatus().value());
		}
	}

	@Override
	public void removeLicence(final String userId, final String licenceId) throws LicenseNotFoundException, UserNotFoundException,			
	ErightsException {
		RemoveLicenseRequest request = new RemoveLicenseRequest();
		request.setLicenseId(licenceId);
		request.setUserId(userId);
		RemoveLicenseResponse response = getAcesEndpoint().removeLicense(request);
		/*RemoveLicenseResponseWS response = getEndpoint().removeLicense(erightsUserId.intValue(), erightsLicenceId.intValue());*/

		switch (response.getStatus()) {
		case SUCCESS:
			return;
		case USER_NOT_FOUND:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new UserNotFoundException(response.getErrorMessage());	        
		case LICENSE_NOT_FOUND:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new LicenseNotFoundException(response.getErrorMessage());
		default:
			LOGGER.error("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new ErightsException(response.getErrorMessage(), response.getStatus().value());
		}

	}

	@Override
	public void updateGroup(final GroupDto group) throws GroupNotFoundException, ParentGroupNotFoundException, ErightsException {
		UpdateGroupRequest request= new UpdateGroupRequest();
		OupGroupWS oupGroupWS = ErightsObjectFactory.getGroup(group);
		request.setOupGroupWS(oupGroupWS);
		UpdateGroupResponse response = getAcesEndpoint().updateGroup(request);
		/*UpdateGroupResponseWS response = getEndpoint().updateGroup(ErightsObjectFactory.getGroup(group));*/

		switch (response.getStatus()) {
		case SUCCESS:
			return;
		case GROUP_NOT_FOUND:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new GroupNotFoundException(response.getErrorMessage());	
		case PARENT_GROUP_NOT_FOUND:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new ParentGroupNotFoundException(response.getErrorMessage());	
		default:
			LOGGER.error("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new ErightsException(response.getErrorMessage(), response.getStatus().value());
		}		
	}

	@Override
	public void updateLicence(final String userId, final LicenceDto licence) throws UserNotFoundException,
	ProductNotFoundException, LicenseNotFoundException,
	ErightsException {
	
			OupLicenseWS oupLicenseWS = ErightsObjectFactory.getOupLicenseWS(licence);
			UpdateLicenseRequest request = new UpdateLicenseRequest();
			request.setLicense(oupLicenseWS);
			request.setUserId(userId);
			UpdateLicenseResponse response = getAcesEndpoint().updateLicense(request);
			processUpdateLicenceResponse(response);
	
	}
	
	// S.Sarkar ************Renew License **********************
	@Override
	public void renewLicence(final String userId, final LicenceDto licence) throws UserNotFoundException,
	ProductNotFoundException, LicenseNotFoundException,
	ErightsException {
		if(licence.isExpired()){
			Semaphore newLock = new Semaphore(1);
	        Semaphore lock = this.customerLockMap.putIfAbsent(userId, newLock);
	        if(lock == null){
	            lock = newLock;
	        }
	        Assert.notNull(lock);
	        try {
	            lock.acquire();
	            try {
	            	//removeLicence(userId, licence.getLicenseId());
	        		LicenceTemplate licenceTemplate = licenseDtoToLicenceTemplate(licence.getLicenceDetail());
	        		if(licenceTemplate!=null){
	        			addLicense(userId, licenceTemplate, licence.getProductIds(), true);
	        		}else{
	        			LOGGER.info("***** licenceTemplate not found");
	        		}
	            } finally {
	                lock.release();
	            }
	        } catch (InterruptedException ex) {
	            //handleInterruptedException(customer, ex);
	        }
		}		
	}

	@Override
	public void updateLicenceUsingDateTimes(final String userId, final LicenceDtoDateTime licence) throws UserNotFoundException,
	ProductNotFoundException, LicenseNotFoundException,
	ErightsException {
		UpdateLicenseRequest request = new UpdateLicenseRequest();
		OupLicenseWS oupLicenseWS = ErightsObjectFactory.getOupLicenseWSWithDateTimes(licence);
		request.setUserId(userId);
		request.setLicense(oupLicenseWS);
		UpdateLicenseResponse response = getAcesEndpoint().updateLicense(request);
		/*UpdateLicenseResponseWS response = getEndpoint().updateLicense(userId.intValue(), ErightsObjectFactory.getOupLicenseWSWithDateTimes(licence));*/


		processUpdateLicenceResponse(response);
	}

	protected void processUpdateLicenceResponse(UpdateLicenseResponse response) throws UserNotFoundException, ProductNotFoundException, LicenseNotFoundException, ErightsException {
		switch (response.getStatus()) {
		case SUCCESS:
			return;	       	        
		case LICENSE_NOT_FOUND:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new LicenseNotFoundException(response.getErrorMessage());	
		case PRODUCT_NOT_FOUND:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new ProductNotFoundException(response.getErrorMessage());
		case USER_NOT_FOUND:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new UserNotFoundException(response.getErrorMessage());	
		default:
			LOGGER.error("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new ErightsException(response.getErrorMessage(), response.getStatus().value());
		}

	}

	/**
	 * TODO when we upgrade to spring 3.1 we can consider using springs @Cacheable annotation
	 * rather than googlecodes version. This will allow us to use cache keys based on something like
	 * 
	 * @Cacheable(value="erightsProductCache", key="enforceableProduct.erightsId")
	 * 
	 * with 
	 * 
	 * @CacheEvict to remove products when they are updated or deleted
	 * 
	 * See http://static.springsource.org/spring/docs/3.1.0.M1/spring-framework-reference/html/cache.html
	 */
	@Override
	public void updateProduct(final EnforceableProductDto enforceableProduct)
			throws ProductNotFoundException, ParentProductNotFoundException, ErightsException {
		/*OupProductLicenseWS oupProductLicenseWS = new OupProductLicenseWS();
		oupProductLicenseWS.setLicenseDetail(ErightsObjectFactory.getOupLiceseDetail(licenceDetailDto));	*/
		OupUpdateProductWS oupProductWS = ErightsObjectFactory.getUpdateProduct(enforceableProduct);
		UpdateProductRequest request = new UpdateProductRequest();
		request.setProduct(oupProductWS);
		UpdateProductResponse response = getAcesEndpoint().updateProduct(request);
		/*UpdateProductResponseWS response = getEndpoint().updateProduct(ErightsObjectFactory.getProduct(enforceableProduct));*/

		switch (response.getStatus()) {
		case SUCCESS:
			return;
		case PRODUCT_NOT_FOUND:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new ProductNotFoundException(response.getErrorMessage());	
		case PARENT_PRODUCT_NOT_FOUND:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new ParentProductNotFoundException(response.getErrorMessage());
		case NO_PRODUCTS_FOUND:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new ProductNotFoundException(response.getErrorMessage());	
		default:
			LOGGER.error("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new ErightsException(response.getErrorMessage(), response.getStatus().value());
		}	
	}
	

	public void updateProduct(final EnforceableProductDto enforceableProduct, LicenceDetailDto licenceDetailDto, List<Integer> erightsProductIds)
			throws ProductNotFoundException, ParentProductNotFoundException, ErightsException {

		OupProductLicenseWS oupProductLicenseWS = new OupProductLicenseWS();
		if(licenceDetailDto.getStartDate() != null)
		{
			oupProductLicenseWS.setStartDate(dateConverter.safeConvertLocalDate(licenceDetailDto.getStartDate()));
		}
		if(licenceDetailDto.getEndDate() != null)
		{
			oupProductLicenseWS.setEndDate(dateConverter.safeConvertLocalDate(licenceDetailDto.getEndDate()));
		}
		oupProductLicenseWS.setLicenseDetail(ErightsObjectFactory.getOupLiceseDetail(licenceDetailDto));	
		
		OupProductWS oupProductWS = ErightsObjectFactory.getProduct(enforceableProduct);
		oupProductWS.setProductLicense(oupProductLicenseWS);
		OupUpdateProductWS oupUpdateProductWS=ErightsObjectFactory.convertOupProduct(oupProductWS);
		/*ActivationDetail activationDetail = new ActivationDetail();
		activationDetail.setActivationType(ActivationType.fromValue(enforceableProduct.getActivationStrategy().toString()));*/
		
		UpdateProductRequest request = new UpdateProductRequest();
		request.setProduct(oupUpdateProductWS);
		UpdateProductResponse response = getAcesEndpoint().updateProduct(request);
		/*UpdateProductResponseWS response = getEndpoint().updateProduct(ErightsObjectFactory.getProduct(enforceableProduct));*/

		switch (response.getStatus()) {
		case SUCCESS:
			return;
		case PRODUCT_NOT_FOUND:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new ProductNotFoundException(response.getErrorMessage());	
		case PARENT_PRODUCT_NOT_FOUND:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new ParentProductNotFoundException(response.getErrorMessage());	
		default:
			LOGGER.error("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new ErightsException(response.getErrorMessage(), response.getStatus().value());
		}	
	}
	
	
	
	@Override
	public void addLinkedProduct(final String linkedProductID, String  parentProductID) throws ProductNotFoundException, ParentProductNotFoundException, ErightsException 
	{
		/*EnforceableProductDto enforceableProduct = getProduct(linkedProductID);
		OupProductWS oupProductWS = ErightsObjectFactory.getProduct(enforceableProduct);*/
		/*OupProductIds oupProductIds= new OupProductIds();
		oupProductIds.getProductId().add(parentProductID);*/
		OupLinkedProductIds oupLinkedProductIds=new OupLinkedProductIds();
		oupLinkedProductIds.getLinkedProductId().add(linkedProductID);
		//oupProductWS.setParentIds(oupProductIds);
		
		AddLinkedProductsRequest request=new AddLinkedProductsRequest();
		request.setLinkedProductId(oupLinkedProductIds);
		request.setParentProductId(parentProductID);
		AddLinkedProductsResponse response = getAcesEndpoint().addLinkedProducts(request);
		
		switch (response.getStatus()) {
		case SUCCESS:
			return;
		case PRODUCT_NOT_FOUND:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new ProductNotFoundException(response.getErrorMessage());	
		case PARENT_PRODUCT_NOT_FOUND:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new ParentProductNotFoundException(response.getErrorMessage());	
		default:
			LOGGER.error("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new ErightsException(response.getErrorMessage(), response.getStatus().value());
		}
	}
	
	
	@Override
	public void removeLinkedProduct(String linkedProductID, String parentProductID)
			throws ProductNotFoundException, ParentProductNotFoundException,
			ErightsException {
		
		
		OupLinkedProductIds oupLinkedProductIds=new OupLinkedProductIds();
		oupLinkedProductIds.getLinkedProductId().add(linkedProductID);
			
		RemoveLinkedProductRequest request=new RemoveLinkedProductRequest();
		request.setLinkedProductId(oupLinkedProductIds);
		request.setParentProductId(parentProductID);
		RemoveLinkedProductResponse response = getAcesEndpoint().removeLinkedProduct(request);
		
		switch (response.getStatus()) {
		case SUCCESS:
			return;
		case PRODUCT_NOT_FOUND:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new ProductNotFoundException(response.getErrorMessage());	
		case PARENT_PRODUCT_NOT_FOUND:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new ParentProductNotFoundException(response.getErrorMessage());	
		default:
			LOGGER.error("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new ErightsException(response.getErrorMessage(), response.getStatus().value());
		
	}
		
	}
	/**
	 * Get oup endpoint.
	 * 
	 * @throws ErightsException
	 *             the exception
	 * @return the endpoint
	 */
	/*private OupRightAccessServiceDefinition getEndpoint() throws ErightsException {
		return erightsPort;
	}*/

	private AccessServiceV10 getAcesEndpoint() throws ErightsException {
		return acesErightsPort;
	}

	/**
	 * @param userId
	 *            the erightId
	 * @return user sessions
	 * @throws ErightsException
	 *             the exception
	 *         ErightsSessionNotFoundException
	 *              the exception    
	 */
	@Override
	public final List<String> getSessionsByUserId(final String userId) throws ErightsException, ErightsSessionNotFoundException{
		GetSessionByUserIdRequest request = new GetSessionByUserIdRequest();
		request.setUserId(userId);
		GetSessionByUserIdResponse response = getAcesEndpoint().getSessionByUserId(request);
		/*GetSessionsByUserIdResponseWS response = getEndpoint().getSessionsByUserId(userId);*/

		switch (response.getStatus()) {
		case SUCCESS:
			return response.getSession();
		case SESSION_NOT_FOUND:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new ErightsSessionNotFoundException(response.getErrorMessage(), response.getStatus().value());
		default:
			LOGGER.error("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new ErightsException(response.getErrorMessage(), response.getStatus().value());                

		}
	}

	@Override
	public final void authorizeRequest(String sessionId, String url, String userId, String licenceId) throws UserNotFoundException, LicenseNotFoundException, AccessDeniedException, ErightsException {
		AuthorizeRequestRequest request = new AuthorizeRequestRequest();
		request.setLicenseId(licenceId);
		request.setSessionKey(sessionId);
		request.setUrl(url);
		request.setUserId(userId);
		AuthorizeRequestResponse response = getAcesEndpoint().authorizeRequest(request);
		/*AuthorizeRequestResponseWS response = getEndpoint().authorizeRequest(sessionId, url, erightsUserId, erightsLicenceId);*/
		switch (response.getStatus()) {
		case SUCCESS:
			break;
		case USER_NOT_FOUND:
			LOGGER.error("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new UserNotFoundException(response.getErrorMessage());
		case LICENSE_NOT_FOUND:
			LOGGER.error("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new LicenseNotFoundException(response.getErrorMessage());
		case ACCESS_DENIED:
			LOGGER.error("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new AccessDeniedException(response.getErrorMessage());
		default:
			LOGGER.error("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new ErightsException(response.getErrorMessage(), response.getStatus().value());
		}
	}

	@Override
	public void removeUserExternalId(String externalId, String customerId) throws ErightsException {
		RemoveUserExternalIdRequest request = new RemoveUserExternalIdRequest();
		request.setExternalId(externalId);
		request.setUserId(customerId);
		RemoveUserExternalIdResponse response= getAcesEndpoint().removeUserExternalId(request);
		switch (response.getStatus()) {
		case SUCCESS:
			return;
		case EXTERNAL_ID_NOT_FOUND:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new LicenseNotFoundException(response.getErrorMessage());
		default:
			LOGGER.error("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new ErightsException(response.getErrorMessage(), response.getStatus().value());
		}		
	}
	
	@Override
	public void removeProductExternalId(String externalId, String productId)
			throws ErightsException {
		RemoveProductExternalIdRequest request = new RemoveProductExternalIdRequest();
		request.setExternalId(externalId);
		request.setProductId(productId);
		RemoveProductExternalIdResponse response= getAcesEndpoint().removeProductExternalId(request);
		switch (response.getStatus()) {
		case SUCCESS:
			return;
		case EXTERNAL_ID_NOT_FOUND:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new LicenseNotFoundException(response.getErrorMessage());
		default:
			LOGGER.error("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new ErightsException(response.getErrorMessage(), response.getStatus().value());
		}
		
	}
	
	@Override
	public CustomerDto getUserAccountByUsername(String username) throws UserNotFoundException, ErightsException {
		GetUserAccountByUsernameRequest getUserAccountRequest = new GetUserAccountByUsernameRequest();
		getUserAccountRequest.setUsername(username);
		GetUserAccountByUsernameResponse response = getAcesEndpoint().getUserAccountByUsername(getUserAccountRequest);

		switch (response.getStatus()) {
		case SUCCESS:
			return ErightsObjectFactory.getCustomerDto(response.getUser());
		case USER_NOT_FOUND:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new UserNotFoundException (response.getErrorMessage(), response.getStatus().value(), response.getErrorCode());
		default:
			LOGGER.error("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			if (response.getErrorCode() != null ) {
				throw new ErightsException(response.getErrorMessage(), response.getStatus().value(),response.getErrorCode());
			} else {
				throw new ErightsException(response.getErrorMessage(), response.getStatus().value());
			}
		}
	}

	
	/**
	 * @param externalSystem
	 * @return void
	 * @throws ErightsException
	 This method will create new external system in atypon.   
	 */
	@Override
	public void createExternalSystem(ExternalSystem externalSystem)
			throws ErightsException {
		
		ExternalSystemWS externalSystemWS = ErightsObjectFactory.getExternalSystemWs(externalSystem);
		CreateExternalSystemRequest createExternalSystemRequest = new CreateExternalSystemRequest();
		createExternalSystemRequest.setExternalSystem(externalSystemWS);
		CreateExternalSystemResponse response = getAcesEndpoint().createExternalSystem(createExternalSystemRequest);
		switch (response.getStatus())
		{
		case SUCCESS:


			return;
		default:
			LOGGER.error("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new ErightsException(response.getErrorMessage(), response.getStatus().value());
		}
		
		
	}

	/**
	 * @param externalSystem
	 * @return void
	 * @throws ErightsException
	 This method will update existing external system and its types and also add new external systems types in atypon.   
	 */
	@Override
	public void updateExternalSystem(ExternalSystem externalSystem,String oldSystemName)
			throws ErightsException {
		
		UpdateExternalSystemIdTypeRequest updateExternalSystemIdTypeRequest=null;
		UpdateExternalSystemIdTypeResponse response = new UpdateExternalSystemIdTypeResponse();
		UpdateExternalSystemRequest updateExternalSystemRequest=new UpdateExternalSystemRequest();
		UpdateExternalSystemResponse externalSystemResponse=new UpdateExternalSystemResponse();
		for(ExternalSystemIdType externalSystemIdType : externalSystem.getExternalSystemIdTypes())
		{
			updateExternalSystemIdTypeRequest=new UpdateExternalSystemIdTypeRequest();
			updateExternalSystemIdTypeRequest.setSystemId(oldSystemName);

			//when externalSystemIdType.getOldExternalSystemIdtype() is null it create new external system in atypon db.
			if(externalSystemIdType.getOldExternalSystemIdtype()!=null)
			updateExternalSystemIdTypeRequest.setCurrentTypeId(externalSystemIdType.getOldExternalSystemIdtype().getName());
			
			SystemTypeId systemTypeId=new SystemTypeId();
			systemTypeId.setTypeId(externalSystemIdType.getName());
			systemTypeId.setDescription(externalSystemIdType.getDescription());
			updateExternalSystemIdTypeRequest.setSystemTypeIdentifier(systemTypeId);
			response = getAcesEndpoint().updateExternalSystemIdType(updateExternalSystemIdTypeRequest);
		}
		switch (response.getStatus())
		{

			case SUCCESS:
						updateExternalSystemRequest.setCurrentSystemId(oldSystemName);
						SystemId systemId=new SystemId();
						systemId.setSystemId(externalSystem.getName());
						systemId.setDescription(externalSystem.getDescription());
						updateExternalSystemRequest.setExternalSystem(systemId);
						externalSystemResponse = getAcesEndpoint().updateExternalSystem(updateExternalSystemRequest);
					
					switch (externalSystemResponse.getStatus())
					{
					case SUCCESS:
						return;
					default:
						LOGGER.error("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
						throw new ErightsException(response.getErrorMessage(), response.getStatus().value());
					}
			default:




					LOGGER.error("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
					throw new ErightsException(response.getErrorMessage(), response.getStatus().value());
		}
		
	}

	/**
	 * @param externalSystem
	 * @return void
	 * @throws ErightsException
	 This method will delete  existing external system and its types from atypon db.   
	 */
	@Override
	public void deleteExternalSystem(ExternalSystem externalSystem)
			throws ErightsException {
		
		DeleteExternalSystemAndIdTypeRequest deleteExternalSystemAndIdTypeRequest = new DeleteExternalSystemAndIdTypeRequest();
		deleteExternalSystemAndIdTypeRequest.setSystemId(externalSystem.getName());
		DeleteExternalSystemAndIdTypeResponse response = getAcesEndpoint().deleteExternalSystemAndIdType(deleteExternalSystemAndIdTypeRequest);
		switch (response.getStatus())
		{
		case SUCCESS:


			return;
		default:
			LOGGER.error("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new ErightsException(response.getErrorMessage(), response.getStatus().value());
		}
		
	}

	/**
	 * @param externalSystem
	 * @return void
	 * @throws ErightsException
	 This method will delete  existing external system types from atypon db.   
	 */
	@Override
	public void deleteExternalSystemTypes(ExternalSystem externalSystem,List<ExternalSystemIdType> externalSystemIdTypes)
			throws ErightsException {
		DeleteExternalSystemAndIdTypeRequest deleteExternalSystemAndIdTypeRequest = new DeleteExternalSystemAndIdTypeRequest();
		DeleteExternalSystemAndIdTypeResponse response=new DeleteExternalSystemAndIdTypeResponse();
		response.setStatus(ExternalSystemSTATUS.SUCCESS);
		
		if(externalSystemIdTypes.size()>0)
		{
			deleteExternalSystemAndIdTypeRequest.setSystemId(externalSystem.getName());
			for(int i=0;i<externalSystemIdTypes.size();i++)
				deleteExternalSystemAndIdTypeRequest.getTypeIds().add(externalSystemIdTypes.get(i).getName());
			response = getAcesEndpoint().deleteExternalSystemAndIdType(deleteExternalSystemAndIdTypeRequest);
		}
		switch (response.getStatus())
		{
		case SUCCESS:


			return;
		default:
			LOGGER.error("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new ErightsException(response.getErrorMessage(), response.getStatus().value());
		}
		
	}

	@Override
	public String addLicenseUserProduct(String userId, String productId)
			throws ErightsException {
		AddLicenseUserProductRequest addLicenseUserProductRequest=new AddLicenseUserProductRequest();
		addLicenseUserProductRequest.setUserId(userId);
		addLicenseUserProductRequest.setProductId(productId);
		addLicenseUserProductRequest.setSendActivationMail(false);
		addLicenseUserProductRequest.setUrl("http://www.google.com");
		AddLicenseUserProductResponse response=getAcesEndpoint().addLicenseUserProduct(addLicenseUserProductRequest);
		
		switch (response.getStatus())
		{
		case SUCCESS:
			return response.getLicenseId();
		default:
			LOGGER.error("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new ErightsException(response.getErrorMessage(), response.getStatus().value());
		}
		
	}

	@Override
	public void adminActivateLicense(String userId,
			String licenceId,final boolean sendEmail) throws UserNotFoundException,
			LicenseNotFoundException, ErightsException {
		AdminActivateLicenseRequest adminActivateLicenseRequest=new AdminActivateLicenseRequest();
		adminActivateLicenseRequest.setLicenseId(licenceId);
		adminActivateLicenseRequest.setSendMail(sendEmail);
		adminActivateLicenseRequest.setUserId(userId);
		
		AdminActivateLicenseResponse response=getAcesEndpoint().adminActivateLicense(adminActivateLicenseRequest);
		
		switch (response.getStatus()) {
		case SUCCESS:
			break;
		case USER_NOT_FOUND:
			LOGGER.error("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new UserNotFoundException(response.getErrorMessage());
		case LICENSE_NOT_FOUND:
			LOGGER.error("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new LicenseNotFoundException(response.getErrorMessage());
		default:
			LOGGER.error("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new ErightsException(response.getErrorMessage(), response.getStatus().value());
		}
	}

	@Override
	public void adminDeactivateLicense(Integer userId,
			Integer erightsLicenceId,final boolean sendEmail) throws UserNotFoundException,
			LicenseNotFoundException, ErightsException {
		AdminDeactivateLicenseRequest adminDeactivateLicenseRequest=new AdminDeactivateLicenseRequest();
		//activateLicenseRequest.setLicenseId(value);
		//activateLicenseRequest.setSendMail(value);
		//activateLicenseRequest.setUserId(value);
		
		AdminDeactivateLicenseResponse response=getAcesEndpoint().adminDeactivateLicense(adminDeactivateLicenseRequest);
		
		switch (response.getStatus()) {
		case SUCCESS:
			break;
		case USER_NOT_FOUND:
			LOGGER.error("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new UserNotFoundException(response.getErrorMessage());
		case LICENSE_NOT_FOUND:
			LOGGER.error("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new LicenseNotFoundException(response.getErrorMessage());
		default:
			LOGGER.error("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new ErightsException(response.getErrorMessage(), response.getStatus().value());
		}
		
	}

	@Override
	public ProductGroupDto createProductGroup(ProductGroupDto productGroupDto)
			throws ProductNotFoundException, ErightsException {
		CreateProductGroupRequest request = new CreateProductGroupRequest();
		OupProductGroupWS productGroupWS = ErightsObjectFactory.getOupProductGroupWS(productGroupDto);
		request.setProductGroup(productGroupWS);
		
		CreateProductGroupResponse response = getAcesEndpoint().createProductGroup(request);
		switch (response.getStatus()) {
		case SUCCESS:
			return new ProductGroupDto();
		/*case  NO_PRODUCT_GROUP_FOUND:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new UserAlreadyExistsException(response.getErrorMessage());
		*/default:
			LOGGER.error("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new ErightsException(response.getErrorMessage(), response.getStatus().value());
		}
		
	}

	@Override
	public void updateProductGroup(ProductGroupDto productGroupDto)
			throws ProductNotFoundException, ErightsException {
		UpdateProductGroupRequest request = new UpdateProductGroupRequest();
		OupProductGroupWS productGroupWS = ErightsObjectFactory.getOupProductGroupWS(productGroupDto);
		request.setProductGroup(productGroupWS);
		request.setCurrentName(productGroupDto.getCurrentGroupName());
		UpdateProductGroupResponse response = getAcesEndpoint().updateProductGroup(request);
		switch (response.getStatus()) {
		case SUCCESS:
			return;
		/*case USER_NOT_FOUND:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new UserNotFoundException(response.getErrorMessage());
		case GROUP_NOT_FOUND:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new GroupNotFoundException(response.getErrorMessage());
		case USER_LOGIN_CREDENTIAL_ALREADY_EXIST:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new UserLoginCredentialAlreadyExistsException(response.getErrorMessage());

		case PASSWORD_POLICY_VIOLATED:
			LOGGER.debug("Password Policy Violated. Error message: " + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new PasswordPolicyViolatedException(response.getErrorMessage());
		*/default:
			LOGGER.error("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new ErightsException(response.getErrorMessage(), response.getStatus().value());
		}
		
	}

	@Override
	public ProductGroupDto getProductGroup(String productId, String currentProductName)
			throws ProductNotFoundException, ErightsException {
		GetProductGroupRequest request = new GetProductGroupRequest();
		request.setId(productId);
		request.setName(currentProductName);
		GetProductGroupResponse response = getAcesEndpoint().getProductGroup(request);
		switch (response.getStatus()) {
		case SUCCESS:
			return ErightsObjectFactory.getProductGroup(response.getProductGroup());
		case NO_PRODUCT_GROUP_FOUND:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new ProductNotFoundException(response.getErrorMessage());		    
		default:
			LOGGER.error("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new ErightsException(response.getErrorMessage(), response.getStatus().value());
		}
	}

	@Override
	public void deleteProductGroup(String producName)
			throws ProductNotFoundException, ErightsException {
		DeleteProductGroupRequest request = new DeleteProductGroupRequest();
		request.setName(producName);
		DeleteProductGroupResponse response = getAcesEndpoint().deleteProductGroup(request);
		switch (response.getStatus()) {
		case SUCCESS:
			return;
		case NO_PRODUCT_GROUP_FOUND:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			/*throw new ChildProductFoundException(response.getErrorMessage());*/
		/*case PRODUCT_NOT_FOUND:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new ProductNotFoundException(response.getErrorMessage());
		*/default:
			LOGGER.error("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new ErightsException(response.getErrorMessage(), response.getStatus().value());
		}	
	}
	
	public OupUpdateProductWS convert(OupProductWS oupProductWS)
	{
		OupUpdateProductWS oupUpdateProductWS= new OupUpdateProductWS();
		
		oupUpdateProductWS.setActivationDetail(oupProductWS.getActivationDetail());
		oupUpdateProductWS.setAdminEmail(oupProductWS.getAdminEmail());
		oupUpdateProductWS.setHomePage(oupProductWS.getHomePage());
		oupUpdateProductWS.setId(oupProductWS.getId());
		oupUpdateProductWS.setLandingPage(oupProductWS.getLandingPage());
		oupUpdateProductWS.setName(oupProductWS.getName());
		oupUpdateProductWS.setProductLicense(oupProductWS.getProductLicense());
		oupUpdateProductWS.setRegisterableType(oupProductWS.getRegisterableType());
		oupUpdateProductWS.setSendUserConfirmationEmail(false);
		oupUpdateProductWS.setSla(oupProductWS.getSla());
		oupUpdateProductWS.setState(oupProductWS.getState());
		oupUpdateProductWS.setSuspended(false);
		oupUpdateProductWS.getUrls().addAll(oupProductWS.getUrls());
		oupUpdateProductWS.getExternal().addAll(oupProductWS.getExternal());
	
		/*if(oupProductWS.getRegistrationDefinitionType()!=null)
		{
			
		oupUpdateProductWS.setRegistrationDefinitionType(oupProductWS.getRegistrationDefinitionType());
		}*/
		
		return oupUpdateProductWS;
	}
	
	@Override
	public void updateLinkedProduct(int linkedProductID, int parentProductID)
			throws ProductNotFoundException, ParentProductNotFoundException,
			ErightsException {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public ActivationCodeBatchDto createActivationCodeBatch(final ActivationCodeBatchDto activationCodeBatchDto) throws ProductNotFoundException, 
	UserNotFoundException, LicenseNotFoundException, AccessDeniedException, GroupNotFoundException,ErightsException{
		CreateActivationCodeBatchRequest request = new CreateActivationCodeBatchRequest() ;
		request.setActivationCodeBatch(ErightsObjectFactory.getOupActivationCodeBatch(activationCodeBatchDto));
		OupActivationCodeLicenseWS oupActivationCodeLicenseWS = new OupActivationCodeLicenseWS() ;
		oupActivationCodeLicenseWS.setLicenseDetail(ErightsObjectFactory.getOupLicenseDetail(activationCodeBatchDto.getLicenceDetailDto()));
		
		if(activationCodeBatchDto.getLicenceDetailDto().getStartDate() != null)
			oupActivationCodeLicenseWS.setStartDate(dateConverter.safeConvertLocalDate(activationCodeBatchDto.getLicenceDetailDto().getStartDate()));
		
		if(activationCodeBatchDto.getLicenceDetailDto().getEndDate() != null)
			oupActivationCodeLicenseWS.setEndDate(dateConverter.safeConvertLocalDate(activationCodeBatchDto.getLicenceDetailDto().getEndDate()));
		
		
		ActivationCodeLicense activationCodeLicense = new ActivationCodeLicense();
		activationCodeLicense.setLicenseDetails(oupActivationCodeLicenseWS);
		
		if(activationCodeBatchDto.getProductId() != null){
			activationCodeLicense.setProductId(activationCodeBatchDto.getProductId());
		} else if(activationCodeBatchDto.getProductGroupId() != null){
			activationCodeLicense.setProductGroupId(activationCodeBatchDto.getProductGroupId());
		}
		
		request.setActivationCodeLicense(activationCodeLicense);
		
		
		CreateActivationCodeBatchResponse response = getAcesEndpoint().createActivationCodeBatch(request);
		
		switch (response.getStatus()) {
		case SUCCESS:
			return new ActivationCodeBatchDto(response.getActivationCode());
		case PRODUCT_NOT_FOUND:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new ProductNotFoundException(response.getErrorMessage());
		case USER_NOT_FOUND:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new UserNotFoundException(response.getErrorMessage());
		case LICENSE_NOT_FOUND:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new LicenseNotFoundException(response.getErrorMessage());
		/*case LICENSE_TEMPLATE_NOT_FOUND:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new LicenceTemplateNotFoundException(response.getErrorMessage());*/
		case ACCESS_DENIED:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new AccessDeniedException(response.getErrorMessage());
		case GROUP_NOT_FOUND:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new GroupNotFoundException(response.getErrorMessage());
		default:
			LOGGER.error("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new ErightsException(response.getErrorMessage(), response.getStatus().value(), response.getErrorCode());
		}
	}	
	
	@Override
	public void updateActivationCodeBatch(final ActivationCodeBatchDto activationCodeBatchDto) throws ProductNotFoundException, 
	UserNotFoundException, LicenseNotFoundException, AccessDeniedException, GroupNotFoundException,ErightsException{
		UpdateActivationCodeBatchRequest request = new UpdateActivationCodeBatchRequest() ;
		
		request.setBatchId(activationCodeBatchDto.getBatchId());
		request.setCurrentbatchId(activationCodeBatchDto.getCurrentBatchId());
		if(activationCodeBatchDto.getValidFrom() != null )
			request.setValidFrom(dateConverter.safeConvertLocalDate(activationCodeBatchDto.getValidFrom()));
		if( activationCodeBatchDto.getValidTo() != null )
			request.setValidTo(dateConverter.safeConvertLocalDate(activationCodeBatchDto.getValidTo()));
		
		
		OupActivationCodeLicenseWS oupActivationCodeLicenseWS = new OupActivationCodeLicenseWS() ;
		oupActivationCodeLicenseWS.setLicenseDetail(ErightsObjectFactory.getOupLicenseDetail(activationCodeBatchDto.getLicenceDetailDto()));
		if( activationCodeBatchDto.getLicenceDetailDto().getStartDate() != null )
			oupActivationCodeLicenseWS.setStartDate(dateConverter.safeConvertLocalDate(activationCodeBatchDto.getLicenceDetailDto().getStartDate()));
		if( activationCodeBatchDto.getLicenceDetailDto().getEndDate() != null )
			oupActivationCodeLicenseWS.setEndDate(dateConverter.safeConvertLocalDate(activationCodeBatchDto.getLicenceDetailDto().getEndDate()));
		
		
		ActivationCodeLicense activationCodeLicense = new ActivationCodeLicense();
		activationCodeLicense.setLicenseDetails(oupActivationCodeLicenseWS);
		
		if(activationCodeBatchDto.getProductId() != null){
			activationCodeLicense.setProductId(activationCodeBatchDto.getProductId());
		} else if(activationCodeBatchDto.getProductGroupId() != null){
			activationCodeLicense.setProductGroupId(activationCodeBatchDto.getProductGroupId());
		}
		
		request.setActivationCodeLicense(activationCodeLicense);		
		UpdateActivationCodeBatchResponse response = getAcesEndpoint().updateActivationCodeBatch(request);
		
		switch (response.getStatus()) {
		case SUCCESS:
			return;
		case PRODUCT_NOT_FOUND:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new ProductNotFoundException(response.getErrorMessage());
		case USER_NOT_FOUND:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new UserNotFoundException(response.getErrorMessage());
		case LICENSE_NOT_FOUND:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new LicenseNotFoundException(response.getErrorMessage());
		/*case LICENSE_TEMPLATE_NOT_FOUND:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new LicenceTemplateNotFoundException(response.getErrorMessage());*/
		case ACCESS_DENIED:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new AccessDeniedException(response.getErrorMessage());
		case GROUP_NOT_FOUND:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new GroupNotFoundException(response.getErrorMessage());
		default:
			LOGGER.error("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new ErightsException(response.getErrorMessage(), response.getStatus().value());
		}
	}

	@Override
	public void deleteActivationCodeBatch(String batchId) throws ProductNotFoundException, 
	UserNotFoundException, LicenseNotFoundException, AccessDeniedException, GroupNotFoundException,ErightsException {
		DeleteActivationCodeBatchRequest request = new DeleteActivationCodeBatchRequest() ;
		request.setBatchId(batchId);

		DeleteActivationCodeBatchResponse response = getAcesEndpoint().deleteActivationCodeBatch(request);
		switch (response.getStatus()) {
		case SUCCESS:
			return;
		case PRODUCT_NOT_FOUND:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new ProductNotFoundException(response.getErrorMessage());
		case USER_NOT_FOUND:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new UserNotFoundException(response.getErrorMessage());
		case LICENSE_NOT_FOUND:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new LicenseNotFoundException(response.getErrorMessage());
		/*case LICENSE_TEMPLATE_NOT_FOUND:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new LicenceTemplateNotFoundException(response.getErrorMessage());*/
		case ACCESS_DENIED:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new AccessDeniedException(response.getErrorMessage());
		case GROUP_NOT_FOUND:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new GroupNotFoundException(response.getErrorMessage());
		default:
			LOGGER.error("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new ErightsException(response.getErrorMessage(), response.getStatus().value());
		}
	}
	
	@Override
	public GetActivationCodeBatchByBatchIdResponse checkActivationCodeBatchExists(String batchId)
			throws ProductNotFoundException, UserNotFoundException,
			LicenseNotFoundException, AccessDeniedException,
			GroupNotFoundException, ErightsException {
		GetActivationCodeBatchByBatchIdRequest request = new GetActivationCodeBatchByBatchIdRequest() ;
		request.setBatchId(batchId);		
		GetActivationCodeBatchByBatchIdResponse response = getAcesEndpoint().getActivationCodeBatchByBatchId(request);
		return response;
	}

	@Override
	public ActivationCodeBatchDto getActivationCodeBatch(String batchId, boolean activationCodeRequired)
			throws ProductNotFoundException, UserNotFoundException,
			LicenseNotFoundException, AccessDeniedException,
			GroupNotFoundException, ErightsException {
	//	ActivationCodeBatchDto activationCodeBatchDto = new ActivationCodeBatchDto();
		GetActivationCodeBatchByBatchIdRequest request = new GetActivationCodeBatchByBatchIdRequest() ;
		
		request.setBatchId(batchId);
		request.setActivationCodeRequired(activationCodeRequired);
		GetActivationCodeBatchByBatchIdResponse response = getAcesEndpoint().getActivationCodeBatchByBatchId(request);
		
		/*GetActivationCodeDetailsByActivationCodeRequest codeDetailsReq = new GetActivationCodeDetailsByActivationCodeRequest();
		GetActivationCodeDetailsByActivationCodeResponse codeDetailsResponse = new GetActivationCodeDetailsByActivationCodeResponse();
		List<String> codes = response.getActivationCode();
		for (String code : codes) {
			codeDetailsReq.setActivationCode(code);			
			codeDetailsResponse = getAcesEndpoint().getActivationCodeDetailsByActivationCode(codeDetailsReq);
		}*/
				
		switch (response.getStatus()) {
		case SUCCESS:
			return ErightsObjectFactory.getActivationCodeBatch(response);
		case PRODUCT_NOT_FOUND:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new ProductNotFoundException(response.getErrorMessage());
		case USER_NOT_FOUND:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new UserNotFoundException(response.getErrorMessage());
		case LICENSE_NOT_FOUND:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new LicenseNotFoundException(response.getErrorMessage());
		/*case LICENSE_TEMPLATE_NOT_FOUND:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new LicenceTemplateNotFoundException(response.getErrorMessage());*/
		case ACCESS_DENIED:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new AccessDeniedException(response.getErrorMessage());
		case GROUP_NOT_FOUND:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new GroupNotFoundException(response.getErrorMessage());
		default:
			LOGGER.error("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new ErightsException(response.getErrorMessage(), response.getStatus().value());
		}
		
	}
	
	/**
	 * Gets the activation code batch by activation code.
	 *
	 * @param activationCode the activation code
	 * @return the activation code batch by activation code
	 * @throws ProductNotFoundException the product not found exception
	 * @throws UserNotFoundException the user not found exception
	 * @throws LicenseNotFoundException the license not found exception
	 * @throws AccessDeniedException the access denied exception
	 * @throws GroupNotFoundException the group not found exception
	 * @throws ErightsException the erights exception
	 */
	@Override
	public ActivationCodeBatchDto getActivationCodeBatchByActivationCode(String activationCode) 
			throws ProductNotFoundException, UserNotFoundException,
			LicenseNotFoundException, AccessDeniedException,
			GroupNotFoundException, ErightsException {

		GetActivationCodeBatchByActivationCodeRequest requestByCode = new GetActivationCodeBatchByActivationCodeRequest() ;
		requestByCode.setActivationCode(activationCode);
		
		GetActivationCodeBatchByActivationCodeResponse response = 
				getAcesEndpoint().getActivationCodeBatchByActivationCode(requestByCode);
		/*
		GetActivationCodeBatchByBatchIdRequest request = new GetActivationCodeBatchByBatchIdRequest();
		request.setBatchId(responseByCode.getBatchId());
		GetActivationCodeBatchByBatchIdResponse response = getAcesEndpoint().getActivationCodeBatchByBatchId(request);
		*/
		switch (response.getStatus()) {
		case SUCCESS:
			return ErightsObjectFactory.getActivationCodeBatch(response);
		case PRODUCT_NOT_FOUND:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new ProductNotFoundException(response.getErrorMessage());
		case USER_NOT_FOUND:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new UserNotFoundException(response.getErrorMessage());
		case LICENSE_NOT_FOUND:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new LicenseNotFoundException(response.getErrorMessage());
		/*case LICENSE_TEMPLATE_NOT_FOUND:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new LicenceTemplateNotFoundException(response.getErrorMessage());*/
		case ACCESS_DENIED:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new AccessDeniedException(response.getErrorMessage());
		case GROUP_NOT_FOUND:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new GroupNotFoundException(response.getErrorMessage());
		default:
			LOGGER.error("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new ErightsException(response.getErrorMessage(), response.getStatus().value());
		}
		
	
	}
	
	/**
	 * Gets the activation code details by activation code.
	 *
	 * @param activationCode the activation code
	 * @return the activation code details by activation code
	 * @throws ProductNotFoundException the product not found exception
	 * @throws UserNotFoundException the user not found exception
	 * @throws LicenseNotFoundException the license not found exception
	 * @throws AccessDeniedException the access denied exception
	 * @throws GroupNotFoundException the group not found exception
	 * @throws ErightsException the erights exception
	 */
	@Override
	public ActivationCodeBatchDto getActivationCodeDetailsByActivationCode(String activationCode) 
			throws ProductNotFoundException, UserNotFoundException,
			LicenseNotFoundException, AccessDeniedException,
			GroupNotFoundException, ErightsException {

		GetActivationCodeDetailsByActivationCodeRequest requestByCode = new GetActivationCodeDetailsByActivationCodeRequest() ;
		requestByCode.setActivationCode(activationCode);		
		GetActivationCodeDetailsByActivationCodeResponse response = 
				getAcesEndpoint().getActivationCodeDetailsByActivationCode(requestByCode);
		
		switch (response.getStatus()) {
		case SUCCESS:
			return ErightsObjectFactory.getActivationCodeDetailsByActivationCode(response);
		case PRODUCT_NOT_FOUND:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new ProductNotFoundException(response.getErrorMessage());
		case USER_NOT_FOUND:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new UserNotFoundException(response.getErrorMessage());
		case LICENSE_NOT_FOUND:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new LicenseNotFoundException(response.getErrorMessage());
		/*case LICENSE_TEMPLATE_NOT_FOUND:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new LicenceTemplateNotFoundException(response.getErrorMessage());*/
		case ACCESS_DENIED:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new AccessDeniedException(response.getErrorMessage());
		case GROUP_NOT_FOUND:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new GroupNotFoundException(response.getErrorMessage());
		default:
			LOGGER.error("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new ErightsException(response.getErrorMessage(), response.getStatus().value(), response.getErrorCode());
		}
		
	
	}
	
	@Override
	public List<LicenceDto> redeemActivationCode(ActivationCode activationCode, String userId, String url, boolean sendActivationMail
			, boolean completed)
			throws ProductNotFoundException, UserNotFoundException,
			LicenseNotFoundException, AccessDeniedException,
			GroupNotFoundException, ErightsException {
		String code = activationCode.getCode();
		RedeemActivationCodeRequest request = new RedeemActivationCodeRequest() ;
		request.setActivationCode(code);
		request.setUserId(userId);
		request.setCompleted(completed);
		if(url != null && !url.isEmpty()) {
			request.setUrl(url);
		}
		request.setSendActivationMail(sendActivationMail);
		long startTime = System.currentTimeMillis(); 
		RedeemActivationCodeResponse response =  getAcesEndpoint().redeemActivationCode(request);
		AuditLogger.logEvent("Time to  redeemActivationCode REST implementation :: " 
				+ (System.currentTimeMillis() - startTime));
		/*GetActivationCodeBatchByActivationCodeRequest request = new GetActivationCodeBatchByActivationCodeRequest() ;
		request.setActivationCode(activationCode);
		GetActivationCodeBatchByActivationCodeResponse response = getAcesEndpoint().getActivationCodeBatchByActivationCode(request);*/
		
		switch (response.getStatus()) {
		case SUCCESS:
			return ErightsObjectFactory.getLicences(response.getLicenses());
		case PRODUCT_NOT_FOUND:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new ProductNotFoundException(response.getErrorMessage());
		case USER_NOT_FOUND:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new UserNotFoundException(response.getErrorMessage());
		case LICENSE_NOT_FOUND:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new LicenseNotFoundException(response.getErrorMessage());
		/*case LICENSE_TEMPLATE_NOT_FOUND:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new LicenceTemplateNotFoundException(response.getErrorMessage());*/
		case ACCESS_DENIED:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new AccessDeniedException(response.getErrorMessage());
		case GROUP_NOT_FOUND:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new GroupNotFoundException(response.getErrorMessage());
		default:
			LOGGER.error("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new ErightsException(response.getErrorMessage(), response.getStatus().value(),response.getErrorCode()); 
		}
		
	}

	@Override
	public List<DivisionDto> createDivision(List<DivisionDto> divisionDtos)
			throws AccessDeniedException, DivisionAlreadyExistsException,
			DivisionNotFoundException, ErightsException {
		CreateDivisionResponse createDivisionResponse = new CreateDivisionResponse() ;
		CreateDivisionRequest createDivisionRequest = new CreateDivisionRequest() ;
		createDivisionRequest.getDivisions().addAll(ErightsObjectFactory.getOupDivisions(divisionDtos)) ;
		createDivisionResponse = getAcesEndpoint().createDivision(createDivisionRequest) ;
		switch (createDivisionResponse.getStatus()) {
		case SUCCESS:

			return ErightsObjectFactory.getDivisionDtos(createDivisionResponse.getDivisions());

		case DIVISION_ALREADY_EXISTS:
			LOGGER.debug("Erights failure. Error message:" + createDivisionResponse.getErrorMessage() + " Status:" + createDivisionResponse.getStatus().value());
			throw new DivisionAlreadyExistsException(createDivisionResponse.getErrorMessage());
		default:
			LOGGER.error("Erights failure. Error message:" + createDivisionResponse.getErrorMessage() + " Status:" + createDivisionResponse.getStatus().value());
			throw new ErightsException(createDivisionResponse.getErrorMessage(), createDivisionResponse.getStatus().value());
		}
		
	}

	@Override
	public List<DivisionDto> updateDivision(List<DivisionDto> divisionDtos)
			throws AccessDeniedException, DivisionNotFoundException,
			ErightsException {
		UpdateDivisionResponse updateDivisionResponse = new UpdateDivisionResponse() ;
		UpdateDivisionRequest updateDivisionRequest = new UpdateDivisionRequest() ;
		updateDivisionRequest.getDivisions().addAll(ErightsObjectFactory.getOupDivisions(divisionDtos)) ;
		updateDivisionResponse = getAcesEndpoint().updateDivision(updateDivisionRequest) ;
		switch (updateDivisionResponse.getStatus()) {
		case SUCCESS:

			return ErightsObjectFactory.getDivisionDtos(updateDivisionResponse.getDivisions());

		case DIVISION_NOT_FOUND:
			LOGGER.debug("Erights failure. Error message:" + updateDivisionResponse.getErrorMessage() + " Status:" + updateDivisionResponse.getStatus().value());
			throw new DivisionNotFoundException(updateDivisionResponse.getErrorMessage());
		default:
			LOGGER.error("Erights failure. Error message:" + updateDivisionResponse.getErrorMessage() + " Status:" + updateDivisionResponse.getStatus().value());
			throw new ErightsException(updateDivisionResponse.getErrorMessage(), updateDivisionResponse.getStatus().value());
		}
	}

	@Override
	public List<DivisionDto> getAllDivisions()
			throws AccessDeniedException, DivisionNotFoundException,
			ErightsException {


		GetAllDivisionsResponse getAllDivisionsResponse = new GetAllDivisionsResponse() ;
		GetAllDivisionsRequest getAllDivisionsRequest = new GetAllDivisionsRequest() ;
		getAllDivisionsResponse = getAcesEndpoint().getAllDivisions(getAllDivisionsRequest) ;
		switch (getAllDivisionsResponse.getStatus()) {
		case SUCCESS:
			return ErightsObjectFactory.getDivisionDtos(getAllDivisionsResponse.getDivisions());

		case DIVISION_NOT_FOUND:
			LOGGER.debug("Erights failure. Error message:" + getAllDivisionsResponse.getErrorMessage() + " Status:" + getAllDivisionsResponse.getStatus().value());
			throw new DivisionNotFoundException(getAllDivisionsResponse.getErrorMessage());
		default:
			LOGGER.error("Erights failure. Error message:" + getAllDivisionsResponse.getErrorMessage() + " Status:" + getAllDivisionsResponse.getStatus().value());
			throw new ErightsException(getAllDivisionsResponse.getErrorMessage(), getAllDivisionsResponse.getStatus().value());

		}

	}

	@Override
	public void deleteDivision(List<DivisionDto> divisionDtos)
			throws AccessDeniedException, DivisionNotFoundException,
			ErightsException {
		DeleteDivisionResponse deleteDivisionResponse = new DeleteDivisionResponse() ;
		DeleteDivisionRequest deleteDivisionRequest = new DeleteDivisionRequest() ;
		List<Integer> oupDivisionIds = new ArrayList<Integer>() ;
		for(DivisionDto divisionDto : divisionDtos){
			oupDivisionIds.add(divisionDto.getId());
		}
		deleteDivisionRequest.getDivisionId().addAll(oupDivisionIds) ;
		deleteDivisionResponse = getAcesEndpoint().deleteDivision(deleteDivisionRequest) ;
		switch (deleteDivisionResponse.getStatus()) {
		case SUCCESS:

			return ;
		case DIVISION_NOT_FOUND:
			LOGGER.debug("Erights failure. Error message:" + deleteDivisionResponse.getErrorMessage() + " Status:" + deleteDivisionResponse.getStatus().value());
			throw new DivisionNotFoundException(deleteDivisionResponse.getErrorMessage());
		default:
			LOGGER.error("Erights failure. Error message:" + deleteDivisionResponse.getErrorMessage() + " Status:" + deleteDivisionResponse.getStatus().value());
			throw new ErightsException(deleteDivisionResponse.getErrorMessage(), deleteDivisionResponse.getStatus().value());
		}
	}
	
	@Override
	public List<String> getAllValidatorEmails()throws AccessDeniedException,
	ErightsException {

		GetAllValidatorEmailsRequest getAllValidatorEmailsRequest = new GetAllValidatorEmailsRequest() ;
		GetAllValidatorEmailsResponse getAllValidatorEmailsResponse = new GetAllValidatorEmailsResponse() ;
		getAllValidatorEmailsResponse = getAcesEndpoint().getAllValidatorEmails(getAllValidatorEmailsRequest) ;
		switch (getAllValidatorEmailsResponse.getStatus()) {
		case SUCCESS:
			return  ErightsObjectFactory.getAllValidatorEmails(getAllValidatorEmailsResponse.getValidatorEmails());

		default:
			LOGGER.error("Erights failure. Error message:" + getAllValidatorEmailsResponse.getErrorMessage() + " Status:" + getAllValidatorEmailsResponse.getStatus().value());
			throw new ErightsException(getAllValidatorEmailsResponse.getErrorMessage(), getAllValidatorEmailsResponse.getStatus().value());
		}
	}


	
	@Override
	public List<ExternalSystem> getAllExternalSystems() throws ErightsException {

		GetAllExternalSystemsRequest getAllExternalSystemsRequest = new GetAllExternalSystemsRequest() ;
		GetAllExternalSystemsResponse getAllExternalSystemsResponse = new GetAllExternalSystemsResponse() ;
		getAllExternalSystemsResponse = getAcesEndpoint().getAllExternalSystems(getAllExternalSystemsRequest);
		switch (getAllExternalSystemsResponse.getStatus() ) {
		case SUCCESS:
			return  ErightsObjectFactory.getAllExternalSystem(getAllExternalSystemsResponse.getExternalSystem());
		default:
			LOGGER.error("Erights failure. Error message:" + getAllExternalSystemsResponse.getErrorMessage() + " Status:" + getAllExternalSystemsResponse.getStatus().value());
			throw new ErightsException(getAllExternalSystemsResponse.getErrorMessage(), getAllExternalSystemsResponse.getStatus().value());

		}

	}
	
	@Override
	public ExternalSystem getExternalSystem(String externalSystem)
			throws ErightsException {

		GetExternalSystemRequest getExternalSystemRequest = new GetExternalSystemRequest();
		getExternalSystemRequest.setSystemId(externalSystem);
		GetExternalSystemResponse response = getAcesEndpoint().getExternalSystem(getExternalSystemRequest);

		switch (response.getStatus()) {
		case SUCCESS:
			return ErightsObjectFactory.getExternalSystem(response.getExternalSystem());
					//getExternalSystemWs(response.getExternalSystem());
		default:
			LOGGER.error("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new ErightsException(response.getErrorMessage(), response.getStatus().value());
		}
	}

	@Override
	public CustomerDto getUserAccountByExternalId(ExternalIdentifier externalIdentifier)
			throws ErightsException {
		GetUserAccountByExternalIdRequest getUserAccountByExternalIdRequest = new GetUserAccountByExternalIdRequest();
		getUserAccountByExternalIdRequest.setExternalUserId(externalIdentifier);
		GetUserAccountByExternalIdResponse response = getAcesEndpoint().getUserAccountByExternalId(getUserAccountByExternalIdRequest);
		switch (response.getStatus()) {
		case SUCCESS:
			return ErightsObjectFactory.getCustomerDto(response.getUser());
		case USER_NOT_FOUND:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new UserNotFoundException(response.getErrorMessage());
		default:
			LOGGER.error("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new ErightsException(response.getErrorMessage(), response.getStatus().value());
		}
	}
	
	@Override
	public EnforceableProductDto getProductByExternalId(ExternalIdDto externalIdDto) throws ProductNotFoundException, ErightsException{
		GetProductByExternalIdRequest getProductByExternalIdRequest = new GetProductByExternalIdRequest() ;
		ExternalIdentifier externalIdentifier = new ExternalIdentifier() ;
		externalIdentifier.setId(externalIdDto.getId());
		externalIdentifier.setSystemId(externalIdDto.getSystemId());
		externalIdentifier.setTypeId(externalIdDto.getType());
		getProductByExternalIdRequest.setExternalProductId(externalIdentifier);
		GetProductByExternalIdResponse getProductByExternalIdResponse = getAcesEndpoint().getProductByExternalId(getProductByExternalIdRequest) ;
		switch (getProductByExternalIdResponse.getStatus()) {
		case SUCCESS:
			return ErightsObjectFactory.getProduct(getProductByExternalIdResponse.getProduct());
		case PRODUCT_NOT_FOUND:
			LOGGER.debug("Erights failure. Error message:" + getProductByExternalIdResponse.getErrorMessage() + " Status:" + getProductByExternalIdResponse.getStatus().value());
			throw new ProductNotFoundException(getProductByExternalIdResponse.getErrorMessage());
		default:
			LOGGER.debug("Erights failure. Error message:" + getProductByExternalIdResponse.getErrorMessage() + " Status:" + getProductByExternalIdResponse.getStatus().value());
			throw new ErightsException(getProductByExternalIdResponse.getErrorMessage());
		}
	}

	@Override
	public EnforceableProductDto getProductByName(String productName)
			throws ErightsException {
		// TODO Auto-generated method stub
		GetProductByProductNameRequest getProductByProductNameRequest = new GetProductByProductNameRequest() ;
		getProductByProductNameRequest.setProductName(productName);
		GetProductByProductNameResponse getProductByProductNameResponse = getAcesEndpoint().getProductByProductName(getProductByProductNameRequest) ;
		switch (getProductByProductNameResponse.getStatus()) {
		case SUCCESS:
			return ErightsObjectFactory.getProduct(getProductByProductNameResponse.getProduct());
		case PRODUCT_NOT_FOUND:
			LOGGER.debug("Erights failure. Error message:" + getProductByProductNameResponse.getErrorMessage() + " Status:" + getProductByProductNameResponse.getStatus().value());
			throw new ProductNotFoundException(getProductByProductNameResponse.getErrorMessage());
		default:
			LOGGER.debug("Erights failure. Error message:" + getProductByProductNameResponse.getErrorMessage() + " Status:" + getProductByProductNameResponse.getStatus().value());
			throw new ErightsException(getProductByProductNameResponse.getErrorMessage());
		}
	}
	
	@Override
	public List<GuestRedeemActivationCodeDto> guestRedeemActivationCode(String activationCode) throws ErightsException {
		GuestRedeemActivationCodeRequest request = new GuestRedeemActivationCodeRequest() ;
		request.setActivationCode(activationCode);
		GuestRedeemActivationCodeResponse response = getAcesEndpoint().guestRedeemActivationCode(request) ;
		switch ( response.getStatus()) {
		case SUCCESS :
			return ErightsObjectFactory.OupGuesdtRedeemProductToGuestRedeemDto(response.getEntitlement()) ;
		case ERROR :
			if (response.getErrorCode() == 2004 ) {
				throw new ErightsException(response.getErrorMessage(), response.getStatus().toString(), response.getErrorCode()) ;
			} else if (response.getErrorCode() == 2053) {
				throw new ErightsException(response.getAllowedUsages().toString(), response.getStatus().toString(), response.getErrorCode()) ;
			} else if (response.getErrorCode() == 2054) {
				throw new ErightsException(response.getErrorMessage(), response.getStatus().toString(), response.getErrorCode()) ;
			} else {
				throw new ErightsException(response.getErrorMessage(), response.getStatus().toString(), response.getErrorCode()) ;
				/*LOGGER.debug("Erights failure. Error message:" 
						+ response.getErrorMessage() 
						+ " Status:" + response.getStatus().value());
							throw new ErightsException(response.getErrorMessage());*/
			}
		default :
			LOGGER.debug("Erights failure. Error message:" 
					+ response.getErrorMessage() 
					+ " Status:" + response.getStatus().value());
						throw new ErightsException(response.getErrorMessage());
				
		}
	}
	@Override
	public void mergeCustomer(String custid,String email) throws AccessDeniedException,
	DivisionNotFoundException, ErightsException {
		MergeCustomerRequest mergeCustomerRequest =  new MergeCustomerRequest();
		mergeCustomerRequest.setMasterUserId(custid);
		mergeCustomerRequest.setEmailAddress(email);
		MergeCustomerResponse response = getAcesEndpoint().mergeCustomer(mergeCustomerRequest);

		switch (response.getStatus()) {
		case SUCCESS:
			return;
		case USER_NOT_FOUND:
			LOGGER.debug("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new UserNotFoundException(response.getErrorMessage());
		default:
			LOGGER.error("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new ErightsException(response.getErrorMessage(), response.getStatus().value());
		}
	
	}
	@Override
	public void bulkCreateActivationCodeBatchRequest(List<ActivationCodeBatchDto> activationCodeBatchDtos ,AdminUser adminUser ) throws ErightsException {
		BulkCreateActivationCodeBatchRequest bulkCreateActivationCodeBatchRequest =  new BulkCreateActivationCodeBatchRequest();
		bulkCreateActivationCodeBatchRequest.setRequesterEmail(adminUser.getEmailAddress());
		bulkCreateActivationCodeBatchRequest.setRequesterId(adminUser.getId());
		for(ActivationCodeBatchDto activationCodeBatchDto : activationCodeBatchDtos){
			ActivationCodeBatchDetails activationCodeBatchDetails = new ActivationCodeBatchDetails();
			activationCodeBatchDetails.setActivationCodeBatch(ErightsObjectFactory.getOupActivationCodeBatch(activationCodeBatchDto));
			activationCodeBatchDetails.setActivationCodeLicense(ErightsObjectFactory.getActivationCodeLicense(activationCodeBatchDto));
			bulkCreateActivationCodeBatchRequest.getActivationCodeBatchDetails().add(activationCodeBatchDetails);
		}
		BulkCreateActivationCodeBatchResponse response = getAcesEndpoint().bulkCreateActivationCodeBatch(bulkCreateActivationCodeBatchRequest);
		switch (response.getStatus()) {
		case SUCCESS:
			return;
		default:
			LOGGER.error("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new ErightsException(response.getErrorMessage(), response.getStatus().value());
		}
	
	}
	
	@Override
	public String validateUserAccount(WsUserIdDto wsUserIdDto) throws ErightsException {
		ValidateUserAccountRequest request = new ValidateUserAccountRequest() ;
		ValidateUserAccountResponse response = new ValidateUserAccountResponse() ;
		request.setUser(ErightsObjectFactory.getUserIdentifier(wsUserIdDto));
		response = getAcesEndpoint().validateUserAccount(request);
		switch (response.getStatus()) {
		case SUCCESS:
			return response.getUserId();
		default:
			LOGGER.error("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new ErightsException(response.getErrorMessage(), response.getStatus().value(),response.getErrorCode());
		}
	
	}
	
	@Override
	public void killUserSession(WsUserIdDto wsUserIdDto) throws ErightsException {
		KillUserSessionRequest request = new KillUserSessionRequest() ;
		KillUserSessionResponse response = new KillUserSessionResponse() ;
		request.setUser(ErightsObjectFactory.getUserIdentifier(wsUserIdDto));
		response = getAcesEndpoint().killUserSession(request);
		switch (response.getStatus()) {
		case SUCCESS:
			return;
		default:
			LOGGER.error("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new ErightsException(response.getErrorMessage(), response.getStatus().value(),response.getErrorCode());
		}
	
	}
	
	@Override
	public List<RegisterableProduct> validateActivationCode(String activationCode, String systemId ) throws ErightsException{
		ValidateActivationCodeRequest request = new ValidateActivationCodeRequest() ;
		ValidateActivationCodeResponse response = new ValidateActivationCodeResponse() ;
		
		request.setActivationCode(activationCode);
		request.setSystemId(systemId);
		response = getAcesEndpoint().validateActivationCode(request);
		switch (response.getStatus()) {
		case SUCCESS:
			return ErightsObjectFactory.convertOupProductsToRegisterableProducts(response.getProducts());
		default:
			LOGGER.error("Erights failure. Error message:" + response.getErrorMessage() + " Status:" + response.getStatus().value());
			throw new ErightsException(response.getErrorMessage(), response.getStatus().value(),response.getErrorCode());
		}
	}

	@Override
	public void updateLicenseModifiedDate(String licenseId) {
		UpdateLicenseModifiedDateRequest request = new UpdateLicenseModifiedDateRequest();
		UpdateLicenseModifiedDateResponse response = new UpdateLicenseModifiedDateResponse();

		request.setLicenseId(licenseId);
		try {
			response = getAcesEndpoint().updateLicenseModifiedDate(request);
		} catch (ErightsException e) {
			LOGGER.error(ExceptionUtil.getStackTrace(e));
		}
		switch (response.getStatus()) {
		case SUCCESS:
			return;
		default:
			LOGGER.error("Erights failure. Error message:"
					+ response.getErrorMessage() + " Status:"
					+ response.getStatus().value() + " error Code : "
					+ response.getErrorCode());
		}
	}
	
	// Sudhamoy Sarkar (CTS) ************licenseDtoToLicenceTemplate (Renew License) Start**********************
	private LicenceTemplate licenseDtoToLicenceTemplate(LicenceDetailDto licenseDetailDto) {
		LicenceTemplate licenceTemplate = null;
		if (licenseDetailDto.getLicenceType().toString().equals(LicenceType.CONCURRENT.toString())) {
			ConcurrentLicenceTemplate concurrentLicenceTemplate = new ConcurrentLicenceTemplate() ;
			StandardConcurrentLicenceDetailDto standardDto = (StandardConcurrentLicenceDetailDto)licenseDetailDto ;
			concurrentLicenceTemplate.setTotalConcurrency(standardDto.getTotalConcurrency());
			concurrentLicenceTemplate.setUserConcurrency(standardDto.getUserConcurrency());
			licenceTemplate = concurrentLicenceTemplate ;
			
		} else if (licenseDetailDto.getLicenceType().toString().equals(LicenceType.ROLLING.toString())) {
			RollingLicenceTemplate rollingLicenceTemplate = new RollingLicenceTemplate() ;
			RollingLicenceDetailDto rollingDto = (RollingLicenceDetailDto)licenseDetailDto ;
			rollingLicenceTemplate.setBeginOn(rollingDto.getBeginOn());
			rollingLicenceTemplate.setTimePeriod(rollingDto.getTimePeriod());
			rollingLicenceTemplate.setUnitType(rollingDto.getUnitType());
			licenceTemplate = rollingLicenceTemplate ;
		} else if (licenseDetailDto.getLicenceType().toString().equals(LicenceType.USAGE.toString())) {
			UsageLicenceTemplate usageLicenceTemplate = new UsageLicenceTemplate() ; 
			UsageLicenceDetailDto usageDto = (UsageLicenceDetailDto)licenseDetailDto ;
			usageLicenceTemplate.setAllowedUsages(usageDto.getAllowedUsages());
			licenceTemplate = usageLicenceTemplate ;
		} 
		if (licenseDetailDto.getStartDate() != null ){
			licenceTemplate.setStartDate(licenseDetailDto.getStartDate());
		}
		if (licenseDetailDto.getEndDate() != null ) {
			licenceTemplate.setEndDate(licenseDetailDto.getEndDate());
		}
		return licenceTemplate ;
	}
	// Sudhamoy Sarkar (CTS) ************licenseDtoToLicenceTemplate (Renew License) End**********************
	/**
	* This function will start a thread to sync user data with CEB application.
	* Function should be called only after successful user update in EAC. 
	* @param  userName  user name of the self managed user 
	* @param  userId    EAC user id of the self managed user 
	* @return void     no need to return anything as it would be asynchronous call
	* */
	private void runUserDataSync(String userName,String userId,String password,CustomerDto customerDto){
		try{
			UserDataSychronisable userDataSychronisable = new UserDataSychronisable(userName,userId,password,customerDto);
			Thread userDataSyncThread = new Thread(userDataSychronisable);
			userDataSyncThread.start();			
		}catch(Exception ex){
			LOGGER.error("Error in Launching datasync thread");
			
		}
	}
}
