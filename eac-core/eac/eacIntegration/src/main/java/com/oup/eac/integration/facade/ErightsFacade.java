package com.oup.eac.integration.facade;

import java.util.List;

import org.springframework.security.access.AccessDeniedException;

import com.oup.eac.domain.ActivationCode;
import com.oup.eac.domain.AdminUser;
import com.oup.eac.domain.ExternalSystem;
import com.oup.eac.domain.ExternalSystemIdType;
import com.oup.eac.domain.LicenceTemplate;
import com.oup.eac.domain.RegisterableProduct;
import com.oup.eac.dto.ActivationCodeBatchDto;
import com.oup.eac.dto.ActivationCodeBatchReportCriteria;
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
import com.oup.eac.dto.WsUserIdDto;
import com.oup.eac.integration.erights.ExternalIdentifier;
import com.oup.eac.integration.erights.GetActivationCodeBatchByBatchIdResponse;
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


public interface ErightsFacade {

	void activateLicense(final String userId,
			final String licenceId) throws UserNotFoundException,
			LicenseNotFoundException, ErightsException;

	String addLicense(final String userId,
			final LicenceTemplate licenceTemplate,
			final List<String> productIds, final boolean enabled)
					throws ErightsException;

	AuthenticationResponseDto authenticateUser(
			final LoginPasswordCredentialDto loginPasswordCredential)
					throws SessionConcurrencyException, ErightsException,
					InvalidCredentialsException;

	void changePasswordByUserId(final String userId,
			final String password) throws PasswordPolicyViolatedException,
			ErightsException;

	void changePasswordByUsername(final String username, final String password)
			throws PasswordPolicyViolatedException, ErightsException;

	GroupDto createGroup(final GroupDto group) throws ErightsException,
	ParentGroupNotFoundException;

	EnforceableProductDto createProduct(
			final EnforceableProductDto enforceableProduct, LicenceDetailDto licenceDetailDto)
					throws ErightsException, ParentProductNotFoundException;

	CustomerDto createUserAccount(final CustomerDto customerDto)
			throws ErightsException, UserAlreadyExistsException;

	void deactivateLicense(final String userId,
			final String licenceId) throws ErightsException,
			UserNotFoundException, LicenseNotFoundException;

	void deleteGroup(final Integer erightsGroupId) throws ErightsException,
	GroupNotFoundException;

	void deleteProduct(final String erightsProductId) throws ErightsException,
	ProductNotFoundException, ChildProductFoundException;

	void deleteUserAccount(final String erightsUserId)
			throws ErightsException, UserNotFoundException;

	GroupDto getGroup(final Integer erightsGroupId) throws ErightsException,
	GroupNotFoundException;

	List<Integer> getGroupUsers(final Integer erightsGroupId,
			final Boolean includeIndirectParents) throws ErightsException,
			GroupNotFoundException, UserNotFoundException;

	List<LicenceDto> getLicensesForUser(final String userId, final String licenceId)
			throws LicenseNotFoundException, UserNotFoundException,
			ErightsException;

	/**
	 * Get licences for a user filtered by product.
	 * 
	 * @param erightsUserId
	 * @param erightsProductId
	 * @return a list of licences for the user. List may be empty.
	 * @throws ErightsException
	 * @throws UserNotFoundException
	 * @throws ProductNotFoundException
	 */
	List<LicenceDto> getLicensesForUserProduct(final String userId,
			final String productId) throws ErightsException,
			UserNotFoundException, ProductNotFoundException;

	EnforceableProductDto getProduct(final String productId)
			throws ProductNotFoundException, ErightsException;

	List<String> getProductIdsByUrl(final String url) throws ErightsException;

	CustomerDto getUserAccount(final String userId)
			throws UserNotFoundException, ErightsException;

	List<String> getCustomerIdsFromSession(final String sessionKey)
			throws ErightsException;

	void logout(final String session) throws ErightsException;

	void removeLicence(final String userId,
			final String licenceId) throws LicenseNotFoundException,
			UserNotFoundException, ErightsException;

	void updateGroup(final GroupDto group) throws GroupNotFoundException,
	ParentGroupNotFoundException, ErightsException;

	void renewLicence(final String userId, final LicenceDto licence)
			throws UserNotFoundException, ProductNotFoundException,
			LicenseNotFoundException, ErightsException;
			
	void updateLicence(final String userId, final LicenceDto licence)
			throws UserNotFoundException, ProductNotFoundException,
			LicenseNotFoundException, ErightsException;

	void updateProduct(final EnforceableProductDto enforceableProduct)
			throws ProductNotFoundException, ParentProductNotFoundException,
			ErightsException;

	void updateProduct(final EnforceableProductDto enforceableProduct,
			LicenceDetailDto licenceDetailDto, List<Integer> erightsProductIds)
					throws ProductNotFoundException, ParentProductNotFoundException,
					ErightsException;

	void updateUserAccount(final CustomerDto customerDto)
			throws UserNotFoundException, GroupNotFoundException,
			UserLoginCredentialAlreadyExistsException,
			PasswordPolicyViolatedException, ErightsException;

	/**
	 * Similar to updateLicence but uses dateTimes rather than just dates for
	 * the licence start and licence end. Used for ORCS migration.
	 * 
	 * @param erightsUserId
	 * @param licence
	 * @throws UserNotFoundException
	 * @throws ProductNotFoundException
	 * @throws LicenseNotFoundException
	 * @throws ErightsException
	 */
	void updateLicenceUsingDateTimes(String userId,
			LicenceDtoDateTime licence) throws UserNotFoundException,
			ProductNotFoundException, LicenseNotFoundException,
			ErightsException;

	List<String> getSessionsByUserId(final String userId) throws ErightsException,
	ErightsSessionNotFoundException;

	/**
	 * Similar to updateLicence but uses dateTimes rather than just dates for
	 * the licence start and licence end. Used for ORCS migration.
	 * 
	 * @param erightsUserId
	 * @param licence
	 * @throws UserNotFoundException
	 * @throws ProductNotFoundException
	 * @throws LicenseNotFoundException
	 * @throws ErightsException
	 */
	void authorizeRequest(final String sessionId, final String url,
			final String userId, final String licenceId)
					throws UserNotFoundException, LicenseNotFoundException,
					AccessDeniedException, ErightsException;

	// Added for externalId removal from atypon
	void removeUserExternalId(final String externalId, String userId) throws ErightsException;

	void removeProductExternalId(final String externalId, final String productId)
			throws ErightsException;

	// Added for getting userByUsername from atypon
	CustomerDto getUserAccountByUsername(final String username)
			throws UserNotFoundException, ErightsException;

	// Save ExternalSystem in Atypon
	void createExternalSystem(final ExternalSystem externalSystem)
			throws ErightsException;

	// update ExternalSystem in Atypon
	public void updateExternalSystem(ExternalSystem externalSystem,
			String oldSystemName) throws ErightsException;

	// delete ExternalSystem in Atypon
	public void deleteExternalSystem(ExternalSystem externalSystem)
			throws ErightsException;

	// delete ExternalSystemTypes in Atypon
	public void deleteExternalSystemTypes(ExternalSystem externalSystem,
			List<ExternalSystemIdType> externalSystemIdTypes)
					throws ErightsException;

	public String addLicenseUserProduct(final String userId, final String productId)
			throws ErightsException;

	void adminActivateLicense(final String userId,
			final String licenceId, final boolean sendEmail)
					throws UserNotFoundException, LicenseNotFoundException,
					ErightsException;

	void adminDeactivateLicense(final Integer erightsUserId,
			final Integer erightsLicenceId, final boolean sendEmail)
					throws UserNotFoundException, LicenseNotFoundException,
					ErightsException;

	ProductGroupDto createProductGroup(final ProductGroupDto productGroupDto)
			throws ProductNotFoundException, ErightsException;

	void updateProductGroup(final ProductGroupDto productGroupDto)
			throws ProductNotFoundException, ErightsException;

	ProductGroupDto getProductGroup(String productId, String productName)
			throws ProductNotFoundException, ErightsException;

	void deleteProductGroup(String productName)
			throws ProductNotFoundException, ErightsException;

	void updateLinkedProduct(final int linkedProductID, int parentProductID)
			throws ProductNotFoundException, ParentProductNotFoundException,
			ErightsException;

	void addLinkedProduct(final String linkedProductID, String parentProductID)
			throws ProductNotFoundException, ParentProductNotFoundException,
			ErightsException;

	void removeLinkedProduct(final String linkedProductID, String parentProductID)
			throws ProductNotFoundException, ParentProductNotFoundException,
			ErightsException;

	void resetPassword(final String userId, final String token,
			final String successUrl, final String failureUrl)
					throws ErightsException, PasswordPolicyViolatedException;

	ActivationCodeBatchDto createActivationCodeBatch(
			final ActivationCodeBatchDto activationCodeBatchDto)
					throws ProductNotFoundException, UserNotFoundException,
					LicenseNotFoundException, AccessDeniedException,
					GroupNotFoundException, ErightsException;

	void updateActivationCodeBatch(final ActivationCodeBatchDto activationCodeBatchDto)
			throws ProductNotFoundException, UserNotFoundException,
			LicenseNotFoundException, AccessDeniedException,
			GroupNotFoundException, ErightsException;

	void deleteActivationCodeBatch(final String batchId)
			throws ProductNotFoundException, UserNotFoundException,
			LicenseNotFoundException, AccessDeniedException,
			GroupNotFoundException, ErightsException;

	ActivationCodeBatchDto getActivationCodeBatchByActivationCode(
			String activationCode) throws ProductNotFoundException,
			UserNotFoundException, LicenseNotFoundException,
			AccessDeniedException, GroupNotFoundException, ErightsException;
	
	
	ActivationCodeBatchDto getActivationCodeDetailsByActivationCode(
			String activationCode) throws ProductNotFoundException,
			UserNotFoundException, LicenseNotFoundException,
			AccessDeniedException, GroupNotFoundException, ErightsException;
	
	public List<LicenceDto> redeemActivationCode(ActivationCode activationCode, String userId, String url, boolean sendActivationMail, boolean completed)
			throws ProductNotFoundException, UserNotFoundException,
			LicenseNotFoundException, AccessDeniedException,
			GroupNotFoundException, ErightsException;

	List<DivisionDto> createDivision(List<DivisionDto> divisionDtos) throws AccessDeniedException,
	DivisionAlreadyExistsException, DivisionNotFoundException, ErightsException ;

	List<DivisionDto> updateDivision(List<DivisionDto> divisionDtos) throws AccessDeniedException,
	DivisionNotFoundException, ErightsException ;

	List<DivisionDto> getAllDivisions() throws AccessDeniedException,
	DivisionNotFoundException, ErightsException ;

	void deleteDivision(List<DivisionDto> divisionDtos) throws AccessDeniedException,
	DivisionNotFoundException, ErightsException ;

	ExternalSystem getExternalSystem(String externalSystem) throws ErightsException ;

	List<String> getAllValidatorEmails() throws AccessDeniedException,
	ErightsException;

	List<ExternalSystem> getAllExternalSystems() throws ErightsException;

	CustomerDto getUserAccountByExternalId(final ExternalIdentifier external)
			throws ErightsException;

	EnforceableProductDto getProductByExternalId(ExternalIdDto externalDto) throws ProductNotFoundException, ErightsException;
	
	GetActivationCodeBatchByBatchIdResponse checkActivationCodeBatchExists(String batchId)
			throws ProductNotFoundException, UserNotFoundException,
			LicenseNotFoundException, AccessDeniedException,
			GroupNotFoundException, ErightsException;
	
	EnforceableProductDto getProductByName(String productName) throws ErightsException ;

	List<GuestRedeemActivationCodeDto> guestRedeemActivationCode(
			String activationCode) throws ErightsException;
	
	void mergeCustomer(String custid,String email) throws AccessDeniedException,
	DivisionNotFoundException, ErightsException ;

	ActivationCodeBatchDto getActivationCodeBatch(String batchId,
			boolean activationCodeRequired) throws ProductNotFoundException,
			UserNotFoundException, LicenseNotFoundException,
			AccessDeniedException, GroupNotFoundException, ErightsException;

	void bulkCreateActivationCodeBatchRequest(
			List<ActivationCodeBatchDto> activationCodeBatchDto,
			AdminUser adminUser) throws ErightsException;

	String validateUserAccount(WsUserIdDto wsUserIdDto) throws ErightsException;

	void killUserSession(WsUserIdDto wsUserIdDto) throws ErightsException;

	List<RegisterableProduct> validateActivationCode(String activationCode,
			String systemId) throws ErightsException;

	void updateLicenseModifiedDate(String licenseId);	

}
