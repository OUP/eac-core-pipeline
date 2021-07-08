package com.oup.eac.integration.erights;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.oup.eac.common.utils.EACSettings;
import com.oup.eac.common.utils.ProxyUtils;

@Component("acesNetworkTolerantErightsPort")
public class AcesNetworkTolerantErightsPort implements AccessServiceV10, ApplicationListener<ContextStartedEvent> {

	private static final Logger LOGGER = Logger.getLogger(AcesNetworkTolerantErightsPort.class);

	private final AccessServiceV10 acesErightsPort;
	private int maxRetries;

	@Autowired
	public AcesNetworkTolerantErightsPort(@Qualifier("acesErightsPort") final AccessServiceV10 acesErightsPort) {
		this.acesErightsPort = acesErightsPort;
	}

	private Object invokeMethod(final String methodName, final Object[] params) {
		return invokeMethod(methodName, params, 0);
	}
	
	private Object invokeMethod(final String methodName, final Object[] params, final int retryCount) {
		ProxyUtils.setProxy();
		List<Class<?>> parameterClasses = getParameterClasses(params);
		try {
			Method method = acesErightsPort.getClass().getMethod(methodName, parameterClasses.toArray(new Class<?>[0]));
			return method.invoke(acesErightsPort, params);
		} catch (final InvocationTargetException ite) {
			// Exceptions coming out of the web service call can only be RuntimeExceptions or Errors
			// because the API of the OupRightAccessServiceDefinition does not declare any checked exceptions.
			Throwable t = ite.getTargetException();
			ite.printStackTrace();
			if (t instanceof Error) {
				throw (Error) t; // We can't do anything with these so we propagate
			} else if (! (t instanceof RuntimeException)) {
				// The wrapped API can't throw anything other than a RuntimeException, but we double-check
				// just in case a future change to the API declares a checked exception to be thrown.
				throw new RuntimeException("Error invoking web service method on wrapped ACESAccessServiceDefinition: " + t, t);
			}
			RuntimeException e = (RuntimeException) t; // Can *only* be a RuntimeException (no worry of ClassCastException here).
			if (isConnectionRefused(e)) {
				if (retryCount < maxRetries) {
					sleep(100);
					LOGGER.info("Got connection refused on '" + methodName + "' - retrying invocation (attempt=" + (retryCount + 1) + ")");
					return invokeMethod(methodName, params, retryCount + 1);
				}
				LOGGER.error("Aborting '" + methodName + "' after " + (maxRetries + 1) + " attempts due to connection refused");
				throw e;
			}
			throw e;
		} catch (final Exception e) {
			// Errors caused by this implementation
			throw new RuntimeException("Error invoking web service method on wrapped ACESAccessServiceDefinition: " + e, e);
		}
	}

	/*private Class<?> getParameterClass(final Object param) {
		List<Class<?>> parameterClasses = new ArrayList<Class<?>>();
		for (Object param : params) {

			if (param instanceof Integer) {
				theClass = Integer.TYPE;
			} else {
				theClass = param.getClass();
			}
			parameterClasses.add(theClass);
		}
		Class<?> theClass;
		theClass = param.getClass();
		return theClass;
	}*/

	private List<Class<?>> getParameterClasses(final Object[] params) {
		List<Class<?>> parameterClasses = new ArrayList<Class<?>>();
		for (Object param : params) {
			Class<?> theClass;
			if (param instanceof Integer) {
				theClass = Integer.TYPE;
			} else {
				theClass = param.getClass();
			}
			parameterClasses.add(theClass);
		}
		return parameterClasses;
	}


	private boolean isConnectionRefused(final Exception exception) {
		Throwable rootCause = getRootCause(exception);
		return rootCause instanceof ConnectException && StringUtils.contains(rootCause.getMessage(), "Connection refused");
	}

	private Throwable getRootCause(final Throwable e) {
		if (e.getCause() != null) {
			return getRootCause(e.getCause());
		}
		return e;
	}

	private void sleep(final long millis) {
		try {
			Thread.sleep(millis);
		} catch (final InterruptedException e) {
		}
	}



	public void setMaxRetries(final int maxRetries) {
		Assert.isTrue(maxRetries >= 0);
		this.maxRetries = maxRetries;
	}

	@Override
	public void onApplicationEvent(final ContextStartedEvent event) {
		setMaxRetries(Integer.parseInt(EACSettings.getProperty("erights.connectionRefused.retries", "3")));
	}

	@Override
	public UpdateUserAccountResponse updateUserAccount( UpdateUserAccountRequest updateUserAccountRequest) {
		return (UpdateUserAccountResponse) invokeMethod("updateUserAccount",  new Object[] {  updateUserAccountRequest});

	}

	@Override
	public CreateUserAccountResponse createUserAccount( CreateUserAccountRequest createUserAccountRequest) {
		//return (CreateUserAccountResponse) invokeMethod("createUserAccount",  createUserAccountRequest);
		return (CreateUserAccountResponse) invokeMethod("createUserAccount", new Object[] { createUserAccountRequest });
	}

	@Override
	public DeleteUserAccountResponse deleteUserAccount( DeleteUserAccountRequest deleteUserAccountRequest) {
		return (DeleteUserAccountResponse) invokeMethod("deleteUserAccount",  new Object[] {deleteUserAccountRequest});
	}

	@Override
	public GetUserAccountResponse getUserAccount( GetUserAccountRequest getUserAccountRequest) {
		return (GetUserAccountResponse) invokeMethod("getUserAccount", new Object[] { getUserAccountRequest});
	}

	@Override
	public AuthenticationResponse authentication(AuthenticationRequest authenticationRequest) {
		return (AuthenticationResponse) invokeMethod("authentication", new Object[] { authenticationRequest});
	}

	@Override
	public DeleteProductResponse deleteProduct(
			DeleteProductRequest deleteProductRequest) {
		return (DeleteProductResponse) invokeMethod("deleteProduct", new Object[] { deleteProductRequest});
	}

	@Override
	public GetUserIdsFromSessionResponse getUserIdsFromSession(
			GetUserIdsFromSessionRequest getUserIdsFromSessionRequest) {
		return (GetUserIdsFromSessionResponse) invokeMethod("getUserIdsFromSession", new Object[] { getUserIdsFromSessionRequest});
	}

	@Override
	public UpdateLicenseResponse updateLicense(
			UpdateLicenseRequest updateLicenseRequest) {
		return (UpdateLicenseResponse) invokeMethod("updateLicense", new Object[] { updateLicenseRequest});
	}

	@Override
	public GetProductsFromURLResponse getProductsFromURL(
			GetProductsFromURLRequest getProductsFromURLRequest) {
		return (GetProductsFromURLResponse) invokeMethod("getProductsFromURL", new Object[] { getProductsFromURLRequest});
	}

	@Override
	public CreateGroupResponse createGroup(CreateGroupRequest createGroupRequest) {
		return (CreateGroupResponse) invokeMethod("createGroup", new Object[] { createGroupRequest});
	}

	@Override
	public GetGroupUsersResponse getGroupUsers(
			GetGroupUsersRequest getGroupUsersRequest) {
		return (GetGroupUsersResponse) invokeMethod("getGroupUsers", new Object[] { getGroupUsersRequest});
	}

	@Override
	public RemoveLicenseResponse removeLicense(
			RemoveLicenseRequest removeLicenseRequest) {
		return (RemoveLicenseResponse) invokeMethod("removeLicense", new Object[] { removeLicenseRequest});
	}

	@Override
	public GetGroupResponse getGroup(GetGroupRequest getGroupRequest) {
		return (GetGroupResponse) invokeMethod("getGroup", new Object[] { getGroupRequest});
	}

	@Override
	public AddLicenseResponse addLicense(AddLicenseRequest addLicenseRequest) {
		return (AddLicenseResponse) invokeMethod("addLicense", new Object[] { addLicenseRequest});
	}

	@Override
	public ChangePasswordByUsernameResponse changePasswordByUsername(
			ChangePasswordByUsernameRequest changePasswordByUsernameRequest) {
		return (ChangePasswordByUsernameResponse) invokeMethod("changePasswordByUsername", new Object[] { changePasswordByUsernameRequest});
	}

	@Override
	public GetProductResponse getProduct(GetProductRequest getProductRequest) {
		return (GetProductResponse) invokeMethod("getProduct", new Object[] { getProductRequest});
	}

	@Override
	public DeactivateLicenseResponse deactivateLicense(
			DeactivateLicenseRequest deactivateLicenseRequest) {
		return (DeactivateLicenseResponse) invokeMethod("deactivateLicense", new Object[] { deactivateLicenseRequest});
	}

	@Override
	public UpdateGroupResponse updateGroup(UpdateGroupRequest updateGroupRequest) {
		return (UpdateGroupResponse) invokeMethod("updateGroup", new Object[] { updateGroupRequest});
	}

	@Override
	public GetSessionByUserIdResponse getSessionByUserId(
			GetSessionByUserIdRequest getSessionByUserIdRequest) {
		return (GetSessionByUserIdResponse) invokeMethod("getSessionByUserId", new Object[] { getSessionByUserIdRequest});
	}

	@Override
	public CreateProductResponse createProduct(
			CreateProductRequest createProductRequest) {
		return (CreateProductResponse) invokeMethod("createProduct", new Object[] { createProductRequest});
	}

	@Override
	public GetLicensesForUserProductResponse getLicensesForUserProduct(
			GetLicensesForUserProductRequest getLicensesForUserProductRequest) {
		return (GetLicensesForUserProductResponse) invokeMethod("getLicensesForUserProduct", new Object[] { getLicensesForUserProductRequest});
	}

	@Override
	public DeleteGroupResponse deleteGroup(DeleteGroupRequest deleteGroupRequest) {
		return (DeleteGroupResponse) invokeMethod("deleteGroup", new Object[] { deleteGroupRequest});
	}

	@Override
	public ChangePasswordByUserIdResponse changePasswordByUserId(
			ChangePasswordByUserIdRequest changePasswordByUserIdRequest) {
		return (ChangePasswordByUserIdResponse) invokeMethod("changePasswordByUserId", new Object[] { changePasswordByUserIdRequest});
	}

	@Override
	public GetLicensesForUserResponse getLicensesForUser(
			GetLicensesForUserRequest getLicensesForUserRequest) {
		return (GetLicensesForUserResponse) invokeMethod("getLicensesForUser", new Object[] { getLicensesForUserRequest});
	}

	@Override
	public UpdateProductResponse updateProduct(
			UpdateProductRequest updateProductRequest) {
		return (UpdateProductResponse) invokeMethod("updateProduct", new Object[] { updateProductRequest});
	}

	@Override
	public AuthorizeRequestResponse authorizeRequest(
			AuthorizeRequestRequest authorizeRequestRequest) {
		return (AuthorizeRequestResponse) invokeMethod("authorizeRequest", new Object[] { authorizeRequestRequest});
	}

	@Override
	public LogoutResponse logout(LogoutRequest logoutRequest) {
		return (LogoutResponse) invokeMethod("logout", new Object[] { logoutRequest});
	}

	@Override
	public ActivateLicenseResponse activateLicense(
			ActivateLicenseRequest activateLicenseRequest) {
		return (ActivateLicenseResponse) invokeMethod("activateLicense", new Object[] { activateLicenseRequest});
	}

	@Override
	public CreateUserAccountAddLicenseResponse createUserAccountAddLicense(
			CreateUserAccountAddLicenseRequest createUserAccountAddLicenseRequest) {
		return (CreateUserAccountAddLicenseResponse) invokeMethod("createUserAccountAddLicense", new Object[] { createUserAccountAddLicenseRequest});
	}

	@Override
	public GetUserAccountByUsernameResponse getUserAccountByUsername(
			GetUserAccountByUsernameRequest getUserAccountByUsernameRequest) {
		return (GetUserAccountByUsernameResponse) invokeMethod("getUserAccountByUsername", new Object[] { getUserAccountByUsernameRequest});	
	}

	@Override
	public RemoveUserExternalIdResponse removeUserExternalId(
			RemoveUserExternalIdRequest removeUserExternalIdRequest) {
		return (RemoveUserExternalIdResponse) invokeMethod("removeUserExternalId", new Object[] { removeUserExternalIdRequest});
	}

	@Override
	public RemoveProductExternalIdResponse removeProductExternalId(
			RemoveProductExternalIdRequest removeProductExternalIdRequest) {
		return (RemoveProductExternalIdResponse) invokeMethod("removeProductExternalId", new Object[] { removeProductExternalIdRequest});
	}

	@Override
	public UpdateExternalSystemResponse updateExternalSystem(
			UpdateExternalSystemRequest updateExternalSystemRequest) {
		return (UpdateExternalSystemResponse) invokeMethod("updateExternalSystem", new Object[] { updateExternalSystemRequest});
	}

	@Override
	public UpdateExternalSystemIdTypeResponse updateExternalSystemIdType(
			UpdateExternalSystemIdTypeRequest updateExternalSystemIdTypeRequest) {
		return (UpdateExternalSystemIdTypeResponse) invokeMethod("updateExternalSystemIdType", new Object[] { updateExternalSystemIdTypeRequest});

	}

	@Override
	public GetExternalSystemResponse getExternalSystem(
			GetExternalSystemRequest getExternalSystemRequest) {
		return (GetExternalSystemResponse) invokeMethod("getExternalSystem", new Object[] { getExternalSystemRequest});
		
	}

	@Override
	public DeleteExternalSystemAndIdTypeResponse deleteExternalSystemAndIdType(
			DeleteExternalSystemAndIdTypeRequest deleteExternalSystemAndIdTypeRequest) {
		return (DeleteExternalSystemAndIdTypeResponse) invokeMethod("deleteExternalSystemAndIdType", new Object[] { deleteExternalSystemAndIdTypeRequest});

	}

	@Override
	public GetAllExternalSystemsResponse getAllExternalSystems(
			GetAllExternalSystemsRequest getAllExternalSystemsRequest) {
		// TODO Auto-generated method stub
		return (GetAllExternalSystemsResponse) invokeMethod("getAllExternalSystems", new Object[] { getAllExternalSystemsRequest});
	}

	@Override
	public CreateExternalSystemResponse createExternalSystem(
			CreateExternalSystemRequest createExternalSystemRequest) {
		return (CreateExternalSystemResponse) invokeMethod("createExternalSystem", new Object[] { createExternalSystemRequest});

	}

	@Override
	public CreateActivationCodeBatchResponse createActivationCodeBatch(
			CreateActivationCodeBatchRequest createActivationCodeBatchRequest) {
		return (CreateActivationCodeBatchResponse) invokeMethod("createActivationCodeBatch", new Object[] { createActivationCodeBatchRequest});
	}

	@Override
	public DeleteActivationCodeBatchResponse deleteActivationCodeBatch(
			DeleteActivationCodeBatchRequest deleteActivationCodeBatchRequest) {
		return (DeleteActivationCodeBatchResponse) invokeMethod("deleteActivationCodeBatch", new Object[] { deleteActivationCodeBatchRequest});
	}

	@Override
	public GetActivationCodeDetailsByActivationCodeResponse getActivationCodeDetailsByActivationCode(
			GetActivationCodeDetailsByActivationCodeRequest getActivationCodeDetailsByActivationCodeRequest) {
		return (GetActivationCodeDetailsByActivationCodeResponse) invokeMethod("getActivationCodeDetailsByActivationCode",
				new Object[] { getActivationCodeDetailsByActivationCodeRequest});
	}

	@Override
	public RedeemActivationCodeResponse redeemActivationCode(
			RedeemActivationCodeRequest redeemActivationCodeRequest) {
		// TODO Auto-generated method stub
		return (RedeemActivationCodeResponse) invokeMethod("redeemActivationCode", new Object[] { redeemActivationCodeRequest});
	}

	@Override
	public SearchActivationCodeResponse searchActivationCode(
			SearchActivationCodeRequest searchActivationCodeRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SearchActivationCodeBatchResponse searchActivationCodeBatch(
			SearchActivationCodeBatchRequest searchActivationCodeBatchRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GetActivationCodeBatchByActivationCodeResponse getActivationCodeBatchByActivationCode(
			GetActivationCodeBatchByActivationCodeRequest getActivationCodeBatchByActivationCodeRequest) {
		return (GetActivationCodeBatchByActivationCodeResponse) invokeMethod("getActivationCodeBatchByActivationCode", 
				new Object[] { getActivationCodeBatchByActivationCodeRequest});
		
	}

	@Override
	public UpdateActivationCodeBatchResponse updateActivationCodeBatch(
			UpdateActivationCodeBatchRequest updateActivationCodeBatchRequest) {
		return (UpdateActivationCodeBatchResponse) invokeMethod("updateActivationCodeBatch", new Object[] { updateActivationCodeBatchRequest});
	}
	
	/*@Override
	public GetActivationCodeBatchByBatchIdResponse getActivationCodeBatchByBatchId(
			GetActivationCodeBatchByBatchIdRequest getActivationCodeBatchByBatchIdRequest) {
		return (GetActivationCodeBatchByBatchIdResponse) invokeMethod("getActivationCodeBatchByBatchId", new Object[] { GetActivationCodeBatchByBatchIdRequest});
	}*/
	
	@Override
	public AddLicenseUserProductResponse addLicenseUserProduct(
			AddLicenseUserProductRequest addLicenseUserProductRequest) {
		return (AddLicenseUserProductResponse) invokeMethod("addLicenseUserProduct", new Object[] { addLicenseUserProductRequest});
	}

	@Override
	public GetActivationCodeBatchByBatchIdResponse getActivationCodeBatchByBatchId(
			GetActivationCodeBatchByBatchIdRequest getActivationCodeBatchByBatchIdRequest) {
		// TODO Auto-generated method stub
		return (GetActivationCodeBatchByBatchIdResponse) invokeMethod("getActivationCodeBatchByBatchId", new Object[] { getActivationCodeBatchByBatchIdRequest});
	}

	@Override
	public GetAllValidatorEmailsResponse getAllValidatorEmails(
			GetAllValidatorEmailsRequest getAllValidatorEmailsRequest) {
		// TODO Auto-generated method stub
		return (GetAllValidatorEmailsResponse) invokeMethod("getAllValidatorEmails", new Object[] { getAllValidatorEmailsRequest});
	}

	@Override
	public AdminDeactivateLicenseResponse adminDeactivateLicense(
			AdminDeactivateLicenseRequest adminDeactivateLicenseRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AdminActivateLicenseResponse adminActivateLicense(
			AdminActivateLicenseRequest adminActivateLicenseRequest) {
		return (AdminActivateLicenseResponse) invokeMethod("adminActivateLicense", new Object[] { adminActivateLicenseRequest});
	}

	@Override
	public UpdateProductGroupResponse updateProductGroup(
			UpdateProductGroupRequest updateProductGroupRequest) {
		return (UpdateProductGroupResponse) invokeMethod("updateProductGroup", new Object[] { updateProductGroupRequest });
	}

	@Override
	public CreateProductGroupResponse createProductGroup(
			CreateProductGroupRequest createProductGroupRequest) {
		// TODO Auto-generated method stub
		return (CreateProductGroupResponse) invokeMethod("createProductGroup", new Object[] { createProductGroupRequest });
	}
	@Override
	
	public DeleteProductGroupResponse deleteProductGroup(
			DeleteProductGroupRequest deleteProductGroupRequest) {
		// TODO Auto-generated method stub
		return (DeleteProductGroupResponse) invokeMethod("deleteProductGroup", new Object[] { deleteProductGroupRequest });
	}

	@Override
	public GetProductGroupResponse getProductGroup(
			GetProductGroupRequest getProductGroupRequest) {
		// TODO Auto-generated method stub
		return (GetProductGroupResponse) invokeMethod("getProductGroup", new Object[] { getProductGroupRequest });
	}

	@Override
	public AddLinkedProductsResponse addLinkedProducts(
			AddLinkedProductsRequest addLinkedProductsRequest) {
		// TODO Auto-generated method stub
		return (AddLinkedProductsResponse) invokeMethod("addLinkedProducts", new Object[] { addLinkedProductsRequest });
	}

	@Override
	public RemoveLinkedProductResponse removeLinkedProduct(
			RemoveLinkedProductRequest removeLinkedProductRequest) {
		return (RemoveLinkedProductResponse) invokeMethod("removeLinkedProduct", new Object[] { removeLinkedProductRequest });
	}
	
	

	@Override
	public ResetPasswordResponse resetPassword(
			ResetPasswordRequest resetPasswordRequest) {
		return (ResetPasswordResponse) invokeMethod("resetPassword", new Object[] { resetPasswordRequest });
	}

	@Override
	public DeleteDivisionResponse deleteDivision(
			DeleteDivisionRequest deleteDivisionRequest) {
		return (DeleteDivisionResponse) invokeMethod("deleteDivision", new Object[] { deleteDivisionRequest });
	}

	@Override
	public UpdateDivisionResponse updateDivision(
			UpdateDivisionRequest updateDivisionRequest) {
		return (UpdateDivisionResponse) invokeMethod("updateDivision", new Object[] { updateDivisionRequest });
	}

	@Override
	public CreateDivisionResponse createDivision(
			CreateDivisionRequest createDivisionRequest) {
		return (CreateDivisionResponse) invokeMethod("createDivision", new Object[] { createDivisionRequest });
	}

	@Override
	public GetAllDivisionsResponse getAllDivisions(
			GetAllDivisionsRequest getAllDivisionsRequest) {
		return (GetAllDivisionsResponse) invokeMethod("getAllDivisions", new Object[] { getAllDivisionsRequest });
	}

	@Override
	public ChangePasswordByExternalIdResponse changePasswordByExternalId(
			ChangePasswordByExternalIdRequest changePasswordByExternalIdRequest) {
		return (ChangePasswordByExternalIdResponse) invokeMethod("changePasswordByExternalId", new Object[] { changePasswordByExternalIdRequest });
	}

	@Override
	public GetProductByExternalIdResponse getProductByExternalId(
			GetProductByExternalIdRequest getProductByExternalIdRequest) {
		return (GetProductByExternalIdResponse) invokeMethod("getProductByExternalId", new Object[] { getProductByExternalIdRequest });
	}

	@Override
	public GetUserAccountByExternalIdResponse getUserAccountByExternalId(
			GetUserAccountByExternalIdRequest getUserAccountByExternalIdRequest) {
		return (GetUserAccountByExternalIdResponse) invokeMethod("getUserAccountByExternalId", new Object[] { getUserAccountByExternalIdRequest });
	}

	@Override
	public GetProductByProductNameResponse getProductByProductName(
			GetProductByProductNameRequest getProductByProductNameRequest) {
		// TODO Auto-generated method stub
		return (GetProductByProductNameResponse) invokeMethod("getProductByProductName", new Object[] { getProductByProductNameRequest });
	}

	@Override
	public GuestRedeemActivationCodeResponse guestRedeemActivationCode(
			GuestRedeemActivationCodeRequest guestRedeemActivationCodeRequest) {
		// TODO Auto-generated method stub
		return (GuestRedeemActivationCodeResponse) invokeMethod("guestRedeemActivationCode", new Object[] { guestRedeemActivationCodeRequest });
	}

	@Override
	public MergeCustomerResponse mergeCustomer(
			MergeCustomerRequest mergeCustomerRequest) {
		// TODO Auto-generated method stub
		return (MergeCustomerResponse) invokeMethod("mergeCustomer", new Object[] { mergeCustomerRequest });
		
	}

	@Override
	public SearchUserAccountsResponse searchUserAccounts(
			SearchUserAccountsRequest searchUserAccountsRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BulkCreateActivationCodeBatchResponse bulkCreateActivationCodeBatch(
			BulkCreateActivationCodeBatchRequest bulkCreateActivationCodeBatchRequest) {
		// TODO Auto-generated method stub
		return (BulkCreateActivationCodeBatchResponse) invokeMethod("bulkCreateActivationCodeBatch", new Object[] { bulkCreateActivationCodeBatchRequest });
	}

	@Override
	public ValidateUserAccountResponse validateUserAccount(
			ValidateUserAccountRequest validateUserAccountRequest) {
		// TODO Auto-generated method stub
		return (ValidateUserAccountResponse) invokeMethod("validateUserAccount", new Object[] { validateUserAccountRequest });
	}

	@Override
	public KillUserSessionResponse killUserSession(
			KillUserSessionRequest killUserSessionRequest) {
		// TODO Auto-generated method stub
		return (KillUserSessionResponse) invokeMethod("killUserSession", new Object[] { killUserSessionRequest });
	}

	@Override
	public ValidateActivationCodeResponse validateActivationCode(
			ValidateActivationCodeRequest validateActivationCodeRequest) {
		// TODO Auto-generated method stub
		return (ValidateActivationCodeResponse) invokeMethod("validateActivationCode", new Object[] { validateActivationCodeRequest });
	}

	@Override
	public UpdateLicenseModifiedDateResponse updateLicenseModifiedDate(
			UpdateLicenseModifiedDateRequest updateLicenseModifiedDateRequest) {
		// TODO Auto-generated method stub
		return (UpdateLicenseModifiedDateResponse) invokeMethod(
				"updateLicenseModifiedDate",
				new Object[] { updateLicenseModifiedDateRequest });
	}
	
}
