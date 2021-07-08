package com.oup.eac.integration.facade.impl;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.oup.eac.domain.ActivationCode;
import com.oup.eac.domain.AdminUser;
import com.oup.eac.domain.ExternalSystem;
import com.oup.eac.domain.ExternalSystemIdType;
import com.oup.eac.domain.LicenceTemplate;
import com.oup.eac.domain.RegisterableProduct;
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
import com.oup.eac.dto.WsUserIdDto;
import com.oup.eac.integration.erights.ExternalIdentifier;
import com.oup.eac.integration.erights.GetActivationCodeBatchByBatchIdResponse;
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

/**
 * Provides the ability to 'rewind' a limited number of operations that occur on the {@link ErightsFacade} - undoing any
 * data/state that had been created as part of the original operation.
 * <p>
 * This class decorates the real ErightsFacade. Every time a rewindable method is invoked on an instance of this class,
 * it adds the opposite rewind operation to its rewind history. A subsequent call to the implementation specific
 * <code>rewind</code> method will then invoke each operation in the rewind history - rewinding the state of erights to
 * what it was prior to the original operations taking place. Each rewind invocation removes itself from the rewind
 * history so that once <code>rewind</code> has completed, the rewind history will be empty.
 * <p>
 * Any errors that occur during rewind will be logged but will not cause the <code>rewind</code> method to throw an
 * exception. This won't break clients calling <code>rewind</code>, but may potentially leave data in erights.
 * 
 * @author keelingw
 * 
 */
@Service("rewindableErightsFacade")
public class RewindableErightsFacade implements ErightsFacade {

	private final Logger LOGGER = Logger.getLogger(RewindableErightsFacade.class);

	private final ErightsFacade erightsFacade;
	private Stack<RewindOperation> rewindOperations = new Stack<RewindOperation>();

	@Autowired
	public RewindableErightsFacade(@Qualifier("erightsFacade") ErightsFacade erightsFacade) {
		this.erightsFacade = erightsFacade;
	}

	@Override
	public void activateLicense(String erightsUserId, String erightsLicenceId) throws UserNotFoundException, LicenseNotFoundException, ErightsException {
		erightsFacade.activateLicense(erightsUserId, erightsLicenceId);
		addRewindOperation("deactivateLicense", erightsUserId, erightsLicenceId);
	}

	@Override
	public String addLicense(String erightsUserId, LicenceTemplate licenceTemplate, List<String> erightsProductIds, boolean enabled) throws ErightsException {
		String licenseId = erightsFacade.addLicense(erightsUserId, licenceTemplate, erightsProductIds, enabled);
		addRewindOperation("removeLicence", erightsUserId, licenseId);
		return licenseId;
	}

	@Override
	public AuthenticationResponseDto authenticateUser(LoginPasswordCredentialDto loginPasswordCredential) throws SessionConcurrencyException, ErightsException,
			InvalidCredentialsException {
		return erightsFacade.authenticateUser(loginPasswordCredential);
	}

	/*@Override
	public void changePasswordByUserId(String erightsUserId, String password) throws ErightsException {
		erightsFacade.changePasswordByUserId(erightsUserId, password);
	}
*/
	@Override
	public void changePasswordByUsername(String username, String password) throws ErightsException {
		erightsFacade.changePasswordByUsername(username, password);
	}

	@Override
	public GroupDto createGroup(GroupDto group) throws ErightsException, ParentGroupNotFoundException {
		GroupDto groupDto = erightsFacade.createGroup(group);
		addRewindOperation("deleteGroup", groupDto.getErightsId());
		return groupDto;
	}
/*
	@Override
	public EnforceableProductDto createProduct(EnforceableProductDto enforceableProduct, LicenceDetailDto licenceDetailDto) throws ErightsException, ParentProductNotFoundException {
		EnforceableProductDto enforceableProductDto = erightsFacade.createProduct(enforceableProduct, licenceDetailDto);
		addRewindOperation("deleteProduct", enforceableProductDto.getErightsId());
		return enforceableProductDto;
	}*/

	@Override
	public CustomerDto createUserAccount(CustomerDto customerDto) throws ErightsException, UserAlreadyExistsException {
		CustomerDto ret = erightsFacade.createUserAccount(customerDto);
		addRewindOperation("deleteUserAccount", ret.getUserId());
		return ret;
	}

	@Override
	public void deactivateLicense(String erightsUserId, String erightsLicenceId) throws ErightsException, UserNotFoundException, LicenseNotFoundException {
		erightsFacade.deactivateLicense(erightsUserId, erightsLicenceId);
		addRewindOperation("activateLicense", erightsUserId, erightsLicenceId);
	}

	@Override
	public void deleteGroup(Integer erightsGroupId) throws ErightsException, GroupNotFoundException {
		erightsFacade.deleteGroup(erightsGroupId);
	}

	@Override
	public void deleteProduct(String erightsProductId) throws ErightsException, ProductNotFoundException, ChildProductFoundException {
		erightsFacade.deleteProduct(erightsProductId);
	}

	@Override
	public void deleteUserAccount(String erightsUserId) throws ErightsException, UserNotFoundException {
		erightsFacade.deleteUserAccount(erightsUserId);
	}

	@Override
	public GroupDto getGroup(Integer erightsGroupId) throws ErightsException, GroupNotFoundException {
		return erightsFacade.getGroup(erightsGroupId);
	}

	@Override
	public List<Integer> getGroupUsers(Integer erightsGroupId, Boolean includeIndirectParents) throws ErightsException, GroupNotFoundException,
			UserNotFoundException {
		return erightsFacade.getGroupUsers(erightsGroupId, includeIndirectParents);
	}

	@Override
	public List<LicenceDto> getLicensesForUser(String erightsUserId, String licenseId) throws LicenseNotFoundException, UserNotFoundException, ErightsException {
		return erightsFacade.getLicensesForUser(erightsUserId, licenseId);
	}

	@Override
	public List<LicenceDto> getLicensesForUserProduct(String erightsUserId, String erightsProductId) throws ErightsException, UserNotFoundException,
			ProductNotFoundException {
		return erightsFacade.getLicensesForUserProduct(erightsUserId, erightsProductId);
	}

	@Override
	public EnforceableProductDto getProduct(String erightsProductId) throws ProductNotFoundException, ErightsException {
		return erightsFacade.getProduct(erightsProductId);
	}

	@Override
	public List<String> getProductIdsByUrl(String url) throws ErightsException {
		return erightsFacade.getProductIdsByUrl(url);
	}

	@Override
	public CustomerDto getUserAccount(String erightsUserId) throws UserNotFoundException, ErightsException {
		return erightsFacade.getUserAccount(erightsUserId);
	}

	@Override
	public List<String> getCustomerIdsFromSession(String sessionKey) throws ErightsException {
		return erightsFacade.getCustomerIdsFromSession(sessionKey);
	}

	@Override
	public void logout(String session) throws ErightsException {
		erightsFacade.logout(session);
	}

	@Override
	public void removeLicence(String erightsUserId, String erightsLicenceId) throws LicenseNotFoundException, UserNotFoundException, ErightsException {
		erightsFacade.removeLicence(erightsUserId, erightsLicenceId);
	}

	@Override
	public void updateGroup(GroupDto group) throws GroupNotFoundException, ParentGroupNotFoundException, ErightsException {
		erightsFacade.updateGroup(group);
	}
	
	@Override
	public void renewLicence(String erightsUserId, LicenceDto licence) throws UserNotFoundException, ProductNotFoundException, LicenseNotFoundException,
			ErightsException {
		erightsFacade.renewLicence(erightsUserId, licence);
	}

	@Override
	public void updateLicence(String erightsUserId, LicenceDto licence) throws UserNotFoundException, ProductNotFoundException, LicenseNotFoundException,
			ErightsException {
		erightsFacade.updateLicence(erightsUserId, licence);
	}

	@Override
	public void updateProduct(EnforceableProductDto enforceableProduct) throws ProductNotFoundException, ParentProductNotFoundException, ErightsException {
		erightsFacade.updateProduct(enforceableProduct);
	}

	@Override
	public void updateUserAccount(CustomerDto customerDto) throws UserNotFoundException, GroupNotFoundException, UserLoginCredentialAlreadyExistsException,PasswordPolicyViolatedException,
			ErightsException {
		erightsFacade.updateUserAccount(customerDto);
	}

	public void rewind() {
		while (!rewindOperations.empty()) {
			RewindOperation operation = rewindOperations.pop();
			operation.invoke();
		}
	}

	private void addRewindOperation(String rewindMethodName, Object... params) {
		List<Class<?>> parameterClasses = new ArrayList<Class<?>>();
		for (Object param : params) {
			parameterClasses.add(param.getClass());
		}
		try {
			Method method = erightsFacade.getClass().getMethod(rewindMethodName, parameterClasses.toArray(new Class<?>[0]));
			rewindOperations.push(new RewindOperation(method, params));
		} catch (Exception e) {
			LOGGER.error("Error adding rewind operation: " + e, e);
		}
	}

	private class RewindOperation {
		final Method method;
		final Object[] params;

		RewindOperation(Method method, Object[] params) {
			super();
			this.method = method;
			this.params = params;
		}

		void invoke() {
			try {
				method.invoke(erightsFacade, params);
			} catch (Exception e) {
				LOGGER.warn("Error rewinding e-rights operation: " + e);
			}
		}
	}

    @Override
    public void updateLicenceUsingDateTimes(String erightsUserId, LicenceDtoDateTime licence)
            throws UserNotFoundException, ProductNotFoundException, LicenseNotFoundException, ErightsException {
        erightsFacade.updateLicenceUsingDateTimes(erightsUserId, licence);
    }
    
    @Override
    public List<String> getSessionsByUserId(String userId) throws ErightsException, ErightsSessionNotFoundException{
        return erightsFacade.getSessionsByUserId(userId);
    }

    @Override
    public void authorizeRequest(String sessionId, String url, String erightsUserId, String erightsLicenceId) throws UserNotFoundException, LicenseNotFoundException, AccessDeniedException, ErightsException {
        erightsFacade.authorizeRequest(sessionId, url, erightsUserId, erightsLicenceId);
        addRewindOperation("deactivateLicense", erightsUserId, erightsLicenceId);
    }

	@Override
	public void removeUserExternalId(String externalId, String userId) throws ErightsException {
		erightsFacade.removeUserExternalId(externalId, userId);
		
	}

	@Override
	public CustomerDto getUserAccountByUsername(String username)
			throws ErightsException {
		return erightsFacade.getUserAccountByUsername(username);
	}

	@Override
	public void removeProductExternalId(String externalId, String productId)
			throws ErightsException {
		erightsFacade.removeProductExternalId(externalId, productId);
		
	}

	@Override
	public void createExternalSystem(ExternalSystem externalSystem)
			throws ErightsException {
		erightsFacade.createExternalSystem(externalSystem);
		
	}

	@Override
	public void deleteExternalSystem(ExternalSystem externalSystem)
			throws ErightsException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateExternalSystem(ExternalSystem externalSystem,String oldSystemName)
			throws ErightsException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteExternalSystemTypes(ExternalSystem externalSystem,
			List<ExternalSystemIdType> externalSystemIdTypes)
			throws ErightsException {
		erightsFacade.deleteExternalSystemTypes(externalSystem, externalSystemIdTypes);
		
	}

	@Override
	public void updateProduct(EnforceableProductDto enforceableProduct,
			LicenceDetailDto licenceDetailDto, List<Integer> erightsProductIds)
			throws ProductNotFoundException, ParentProductNotFoundException,
			ErightsException {
		erightsFacade.updateProduct(enforceableProduct, licenceDetailDto, erightsProductIds);
		
		
	}

	@Override
	public String addLicenseUserProduct(String userId, String productId)
			throws ErightsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void adminActivateLicense(String erightsUserId,
			String erightsLicenceId,final boolean sendEmail) throws UserNotFoundException,
			LicenseNotFoundException, ErightsException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void adminDeactivateLicense(Integer erightsUserId,
			Integer erightsLicenceId,final boolean sendEmail) throws UserNotFoundException,
			LicenseNotFoundException, ErightsException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ProductGroupDto createProductGroup(ProductGroupDto productGroupDto)
			throws ProductNotFoundException, ErightsException {
		// TODO Auto-generated method stub
		return erightsFacade.createProductGroup(productGroupDto);
	}

	@Override
	public void updateProductGroup(ProductGroupDto productGroupDto)
			throws ProductNotFoundException, ErightsException {
		// TODO Auto-generated method stub
		erightsFacade.updateProductGroup(productGroupDto);
	}

	@Override
	public ProductGroupDto getProductGroup(String productId, String productName)
			throws ProductNotFoundException, ErightsException {
		// TODO Auto-generated method stub
		return erightsFacade.getProductGroup(productId,productName);
	}

	@Override
	public void deleteProductGroup(String productName)
			throws ProductNotFoundException, ErightsException {
		// TODO Auto-generated method stub
		erightsFacade.deleteProductGroup( productName);
	}

	@Override
	public void updateLinkedProduct(int linkedProductID, int parentProductID)
			throws ProductNotFoundException, ParentProductNotFoundException,
			ErightsException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addLinkedProduct(String linkedProductID, String parentProductID)
			throws ProductNotFoundException, ParentProductNotFoundException,
			ErightsException {
		erightsFacade.addLinkedProduct(linkedProductID, parentProductID);
		
		
	}

	@Override
	public void removeLinkedProduct(String linkedProductID, String parentProductID)
			throws ProductNotFoundException, ParentProductNotFoundException,
			ErightsException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resetPassword(String erightsUserId, String token,
			String successUrl, String failureUrl) throws ErightsException,
			PasswordPolicyViolatedException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ActivationCodeBatchDto createActivationCodeBatch(ActivationCodeBatchDto activationCodeBatchDto) throws ProductNotFoundException, 
	UserNotFoundException, LicenseNotFoundException, AccessDeniedException, GroupNotFoundException,ErightsException{
		// TODO Auto-generated method stub
		return erightsFacade.createActivationCodeBatch(activationCodeBatchDto);
	}

	@Override
	public void updateActivationCodeBatch(ActivationCodeBatchDto activationCodeBatchDto)
			throws ProductNotFoundException, UserNotFoundException,
			LicenseNotFoundException, AccessDeniedException,
			GroupNotFoundException, ErightsException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteActivationCodeBatch(String batchId)
			throws ProductNotFoundException, UserNotFoundException,
			LicenseNotFoundException, AccessDeniedException,
			GroupNotFoundException, ErightsException {
		erightsFacade.deleteActivationCodeBatch(batchId);
		
	}

	@Override
	public ActivationCodeBatchDto getActivationCodeBatch(String batchId, boolean isActivationCodeReq)
			throws ProductNotFoundException, UserNotFoundException,
			LicenseNotFoundException, AccessDeniedException,
			GroupNotFoundException, ErightsException {
		return erightsFacade.getActivationCodeBatch(batchId, isActivationCodeReq);
	}

	/*@Override
	public void redeemActivationCode(ActivationCode activationCode, int userId,
			String url, boolean sendActivationMail)
			throws ProductNotFoundException, UserNotFoundException,
			LicenseNotFoundException, AccessDeniedException,
			GroupNotFoundException, ErightsException {
		// TODO Auto-generated method stub
		
	}*/

	@Override
	public List<DivisionDto> createDivision(List<DivisionDto> divisionDtos)
			throws AccessDeniedException, DivisionAlreadyExistsException,
			DivisionNotFoundException, ErightsException {
		return erightsFacade.createDivision(divisionDtos);
	}

	@Override
	public List<DivisionDto> updateDivision(List<DivisionDto> divisionDtos)
			throws AccessDeniedException, DivisionNotFoundException,
			ErightsException {
		return erightsFacade.updateDivision(divisionDtos);
	}

	@Override
	public List<DivisionDto> getAllDivisions() throws AccessDeniedException,
			DivisionNotFoundException, ErightsException {
		return erightsFacade.getAllDivisions();
	}

	@Override
	public void deleteDivision(List<DivisionDto> divisionDtos)
			throws AccessDeniedException, DivisionNotFoundException,
			ErightsException {
		erightsFacade.deleteDivision(divisionDtos);
	}

	@Override
	public List<String> getAllValidatorEmails() throws AccessDeniedException,
			ErightsException {
		return erightsFacade.getAllValidatorEmails() ;
	}

	@Override
	public List<ExternalSystem> getAllExternalSystems() throws ErightsException {
		// TODO Auto-generated method stub
		return erightsFacade.getAllExternalSystems() ;
	}

	@Override
	public ExternalSystem getExternalSystem(String externalSystem)
			throws ErightsException {
		return erightsFacade.getExternalSystem(externalSystem) ;
	}
	
	@Override
	public ActivationCodeBatchDto getActivationCodeBatchByActivationCode(
			String activationCode) throws ProductNotFoundException,
			UserNotFoundException, LicenseNotFoundException,
			AccessDeniedException, GroupNotFoundException, ErightsException {
		return erightsFacade.getActivationCodeDetailsByActivationCode(activationCode);
	}

	@Override
	public ActivationCodeBatchDto getActivationCodeDetailsByActivationCode(
			String activationCode) throws ProductNotFoundException,
			UserNotFoundException, LicenseNotFoundException,
			AccessDeniedException, GroupNotFoundException, ErightsException {
		return erightsFacade.getActivationCodeBatchByActivationCode(activationCode);
	}

	@Override
	public CustomerDto getUserAccountByExternalId(ExternalIdentifier external)
			throws ErightsException {
		return erightsFacade.getUserAccountByExternalId(external);
	}

	@Override
	public GetActivationCodeBatchByBatchIdResponse checkActivationCodeBatchExists(
			String batchId) throws ProductNotFoundException,
			UserNotFoundException, LicenseNotFoundException,
			AccessDeniedException, GroupNotFoundException, ErightsException {
		return erightsFacade.checkActivationCodeBatchExists(batchId);
	}

	@Override
	public EnforceableProductDto getProductByExternalId(
			ExternalIdDto externalDto) throws ProductNotFoundException,
			ErightsException {
		return erightsFacade.getProductByExternalId(externalDto);
	}


	

	@Override
	public EnforceableProductDto getProductByName(String productName)
			throws ErightsException {
		return erightsFacade.getProductByName(productName);
	}


	@Override
	public List<GuestRedeemActivationCodeDto> guestRedeemActivationCode(
			String activationCode) throws ErightsException {
		return erightsFacade.guestRedeemActivationCode(activationCode);
	}

	@Override
	public void changePasswordByUserId(String userId, String password)
			throws PasswordPolicyViolatedException, ErightsException {
		erightsFacade.changePasswordByUserId(userId, password);
		
	}

	@Override
	public EnforceableProductDto createProduct(
			EnforceableProductDto enforceableProduct,
			LicenceDetailDto licenceDetailDto) throws ErightsException,
			ParentProductNotFoundException {
		return erightsFacade.createProduct(enforceableProduct, licenceDetailDto);
	}

	@Override
	public List<LicenceDto> redeemActivationCode(ActivationCode activationCode,
			String userId, String url, boolean sendActivationMail,
			boolean completed) throws ProductNotFoundException,
			UserNotFoundException, LicenseNotFoundException,
			AccessDeniedException, GroupNotFoundException, ErightsException {
		return erightsFacade.redeemActivationCode(activationCode, userId, url, sendActivationMail, completed);
	}

	@Override
	public void mergeCustomer(String custid, String email)
			throws AccessDeniedException, DivisionNotFoundException,
			ErightsException {
		erightsFacade.mergeCustomer(custid, email);
		
	}

	@Override
	public void bulkCreateActivationCodeBatchRequest(
			List<ActivationCodeBatchDto> activationCodeBatchDto,
			AdminUser adminUser) throws ErightsException {
		erightsFacade.bulkCreateActivationCodeBatchRequest(activationCodeBatchDto, adminUser);
		
	}

	@Override
	public String validateUserAccount(WsUserIdDto wsUserIdDto)
			throws ErightsException {
		
		return erightsFacade.validateUserAccount(wsUserIdDto);
	}

	@Override
	public void killUserSession(WsUserIdDto wsUserIdDto)
			throws ErightsException {
		erightsFacade.killUserSession(wsUserIdDto);
		
	}

	@Override
	public List<RegisterableProduct> validateActivationCode(
			String activationCode, String systemId) throws ErightsException {
		return erightsFacade.validateActivationCode(activationCode, systemId);
	}

	@Override
	public void updateLicenseModifiedDate(String licenseId) {
		// TODO Auto-generated method stub
		erightsFacade.updateLicenseModifiedDate(licenseId);
	}
	

	

}
