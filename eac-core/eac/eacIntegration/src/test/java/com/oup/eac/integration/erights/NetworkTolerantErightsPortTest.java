package com.oup.eac.integration.erights;

import java.net.ConnectException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class NetworkTolerantErightsPortTest {
	
	//private final NetworkTolerantErightsPort networkTolerantErightsPort = new NetworkTolerantErightsPort(new StubbedErightsPort());
	
	private final AcesNetworkTolerantErightsPort networkTolerantErightsPort = new AcesNetworkTolerantErightsPort(new StubbedErightsPort());

	@Test(expected=IllegalStateException.class)
	public void shouldPropagateRuntimeExceptionWhenNotConnectionRefused() throws Exception {
		CreateGroupRequest request = new CreateGroupRequest();
		request.setOupGroupWS(new OupGroupWS());
		networkTolerantErightsPort.createGroup(request);
	}
	
	@Test(expected=Error.class)
	public void shouldPropagateError() throws Exception {
		GetLicensesForUserRequest request = new GetLicensesForUserRequest();
		request.setUserId("12345");
		networkTolerantErightsPort.getLicensesForUser(request);
	}
	
	@Test
	public void shouldRetryWhenConnectionRefusedAndWontThrowException() {
		UpdateGroupRequest request = new UpdateGroupRequest();
		request.setOupGroupWS(new OupGroupWS());
		networkTolerantErightsPort.setMaxRetries(3);
		networkTolerantErightsPort.updateGroup(request);
	}
	
	@Test(expected=RuntimeException.class)
	public void shouldRetryWhenConnectionRefusedAndWillThrowExceptionWhenRetriesExhausted() {
		UpdateGroupRequest request = new UpdateGroupRequest();
		request.setOupGroupWS(new OupGroupWS());
		networkTolerantErightsPort.setMaxRetries(1);
		networkTolerantErightsPort.updateGroup(request);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void shouldNotAllowMaxRetryValueLessThanZero() {
		networkTolerantErightsPort.setMaxRetries(-2);
	}
	
	private class StubbedErightsPort implements AccessServiceV10 {
		
		private final Map<String, Integer> invocationCount = new HashMap<String, Integer>();

		@Override
		public DeleteProductResponse deleteProduct(
				DeleteProductRequest deleteProductRequest) {
			return null;
		}

		@Override
		public GetUserIdsFromSessionResponse getUserIdsFromSession(
				GetUserIdsFromSessionRequest getUserIdsFromSessionRequest) {
			return null;
		}

		@Override
		public UpdateLicenseResponse updateLicense(
				UpdateLicenseRequest updateLicenseRequest) {
			return null;
		}

		@Override
		public GetProductsFromURLResponse getProductsFromURL(
				GetProductsFromURLRequest getProductsFromURLRequest) {
			return null;
		}

		@Override
		public CreateGroupResponse createGroup(
				CreateGroupRequest createGroupRequest) {
			throw new IllegalStateException("createGroup called");
		}

		@Override
		public GetGroupUsersResponse getGroupUsers(
				GetGroupUsersRequest getGroupUsersRequest) {
			return null;
		}

		@Override
		public RemoveLicenseResponse removeLicense(
				RemoveLicenseRequest removeLicenseRequest) {
			return null;
		}

		@Override
		public GetGroupResponse getGroup(GetGroupRequest getGroupRequest) {
			return null;
		}

		@Override
		public AddLicenseResponse addLicense(AddLicenseRequest addLicenseRequest) {
			return null;
		}

		@Override
		public ChangePasswordByUsernameResponse changePasswordByUsername(
				ChangePasswordByUsernameRequest changePasswordByUsernameRequest) {
			return null;
		}

		@Override
		public GetProductResponse getProduct(GetProductRequest getProductRequest) {
			return null;
		}

		@Override
		public AuthenticationResponse authentication(
				AuthenticationRequest authenticationRequest) {
			return null;
		}

		@Override
		public UpdateUserAccountResponse updateUserAccount(
				UpdateUserAccountRequest updateUserAccountRequest) {
			return null;
		}

		@Override
		public DeactivateLicenseResponse deactivateLicense(
				DeactivateLicenseRequest deactivateLicenseRequest) {
			return null;
		}

		@Override
		public UpdateGroupResponse updateGroup(
				UpdateGroupRequest updateGroupRequest) {
			Integer count = invocationCount.get("updateGroup");
			if (count == null) {
				count = 0;
			}
			count = count + 1;
			if (count == 3) {
				// We succeed on the third call.
				return new UpdateGroupResponse();
			}
			invocationCount.put("updateGroup", count);
			throw new RuntimeException(new ConnectException("Connection refused"));
		}

		@Override
		public CreateUserAccountAddLicenseResponse createUserAccountAddLicense(
				CreateUserAccountAddLicenseRequest createUserAccountAddLicenseRequest) {
			return null;
		}

		@Override
		public GetSessionByUserIdResponse getSessionByUserId(
				GetSessionByUserIdRequest getSessionByUserIdRequest) {
			
			return null;
		}

		@Override
		public CreateProductResponse createProduct(
				CreateProductRequest createProductRequest) {
			return null;
		}

		@Override
		public GetLicensesForUserProductResponse getLicensesForUserProduct(
				GetLicensesForUserProductRequest getLicensesForUserProductRequest) {
			return null;
		}

		@Override
		public CreateUserAccountResponse createUserAccount(
				CreateUserAccountRequest createUserAccountRequest) {
			return null;
		}

		@Override
		public DeleteUserAccountResponse deleteUserAccount(
				DeleteUserAccountRequest deleteUserAccountRequest) {
			return null;
		}

		@Override
		public GetUserAccountResponse getUserAccount(
				GetUserAccountRequest getUserAccountRequest) {
			return null;
		}

		@Override
		public DeleteGroupResponse deleteGroup(
				DeleteGroupRequest deleteGroupRequest) {
			return null;
		}

		@Override
		public ChangePasswordByUserIdResponse changePasswordByUserId(
				ChangePasswordByUserIdRequest changePasswordByUserIdRequest) {
			return null;
		}

		@Override
		public GetLicensesForUserResponse getLicensesForUser(
				GetLicensesForUserRequest getLicensesForUserRequest) {
			throw new Error("getLicencesForUser called");
		}

		@Override
		public UpdateProductResponse updateProduct(
				UpdateProductRequest updateProductRequest) {
			return null;
		}

		@Override
		public AuthorizeRequestResponse authorizeRequest(
				AuthorizeRequestRequest authorizeRequestRequest) {
			return null;
		}

		@Override
		public LogoutResponse logout(LogoutRequest logoutRequest) {
			return null;
		}

		@Override
		public ActivateLicenseResponse activateLicense(
				ActivateLicenseRequest activateLicenseRequest) {
			return null;
		}

		@Override
		public GetUserAccountByUsernameResponse getUserAccountByUsername(
				GetUserAccountByUsernameRequest getUserAccountByUsernameRequest) {
			return null;
		}

		@Override
		public RemoveUserExternalIdResponse removeUserExternalId(
				RemoveUserExternalIdRequest removeUserExternalIdRequest) {
			return null;
		}

		@Override
		public RemoveProductExternalIdResponse removeProductExternalId(
				RemoveProductExternalIdRequest removeProductExternalIdRequest) {
			return null;
		}

		@Override
		public UpdateExternalSystemResponse updateExternalSystem(
				UpdateExternalSystemRequest updateExternalSystemRequest) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public UpdateExternalSystemIdTypeResponse updateExternalSystemIdType(
				UpdateExternalSystemIdTypeRequest updateExternalSystemIdTypeRequest) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public GetExternalSystemResponse getExternalSystem(
				GetExternalSystemRequest getExternalSystemRequest) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public DeleteExternalSystemAndIdTypeResponse deleteExternalSystemAndIdType(
				DeleteExternalSystemAndIdTypeRequest deleteExternalSystemAndIdTypeRequest) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public GetAllExternalSystemsResponse getAllExternalSystems(
				GetAllExternalSystemsRequest getAllExternalSystemsRequest) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CreateExternalSystemResponse createExternalSystem(
				CreateExternalSystemRequest createExternalSystemRequest) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CreateActivationCodeBatchResponse createActivationCodeBatch(
				CreateActivationCodeBatchRequest createActivationCodeBatchRequest) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public DeleteActivationCodeBatchResponse deleteActivationCodeBatch(
				DeleteActivationCodeBatchRequest deleteActivationCodeBatchRequest) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public GetActivationCodeDetailsByActivationCodeResponse getActivationCodeDetailsByActivationCode(
				GetActivationCodeDetailsByActivationCodeRequest getActivationCodeDetailsByActivationCodeRequest) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public RedeemActivationCodeResponse redeemActivationCode(
				RedeemActivationCodeRequest redeemActivationCodeRequest) {
			// TODO Auto-generated method stub
			return null;
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
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public GetAllValidatorEmailsResponse getAllValidatorEmails(
				GetAllValidatorEmailsRequest getAllValidatorEmailsRequest) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public UpdateActivationCodeBatchResponse updateActivationCodeBatch(
				UpdateActivationCodeBatchRequest updateActivationCodeBatchRequest) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public AddLicenseUserProductResponse addLicenseUserProduct(
				AddLicenseUserProductRequest addLicenseUserProductRequest) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public GetActivationCodeBatchByBatchIdResponse getActivationCodeBatchByBatchId(
				GetActivationCodeBatchByBatchIdRequest getActivationCodeBatchByBatchIdRequest) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public SearchUserAccountsResponse searchUserAccounts(
				SearchUserAccountsRequest searchUserAccountsRequest) {
			// TODO Auto-generated method stub
			return null;
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
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public UpdateProductGroupResponse updateProductGroup(
				UpdateProductGroupRequest updateProductGroupRequest) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CreateProductGroupResponse createProductGroup(
				CreateProductGroupRequest createProductGroupRequest) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public DeleteProductGroupResponse deleteProductGroup(
				DeleteProductGroupRequest deleteProductGroupRequest) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public GetProductGroupResponse getProductGroup(
				GetProductGroupRequest getProductGroupRequest) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public AddLinkedProductsResponse addLinkedProducts(
				AddLinkedProductsRequest addLinkedProductsRequest) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public RemoveLinkedProductResponse removeLinkedProduct(
				RemoveLinkedProductRequest removeLinkedProductRequest) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public ResetPasswordResponse resetPassword(
				ResetPasswordRequest resetPasswordRequest) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public DeleteDivisionResponse deleteDivision(
				DeleteDivisionRequest deleteDivisionRequest) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public UpdateDivisionResponse updateDivision(
				UpdateDivisionRequest updateDivisionRequest) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CreateDivisionResponse createDivision(
				CreateDivisionRequest createDivisionRequest) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public GetAllDivisionsResponse getAllDivisions(
				GetAllDivisionsRequest getAllDivisionsRequest) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public GuestRedeemActivationCodeResponse guestRedeemActivationCode(
				GuestRedeemActivationCodeRequest guestRedeemActivationCodeRequest) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public ChangePasswordByExternalIdResponse changePasswordByExternalId(
				ChangePasswordByExternalIdRequest changePasswordByExternalIdRequest) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public MergeCustomerResponse mergeCustomer(
				MergeCustomerRequest mergeCustomerRequest) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public GetProductByProductNameResponse getProductByProductName(
				GetProductByProductNameRequest getProductByProductNameRequest) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public GetProductByExternalIdResponse getProductByExternalId(
				GetProductByExternalIdRequest getProductByExternalIdRequest) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public BulkCreateActivationCodeBatchResponse bulkCreateActivationCodeBatch(
				BulkCreateActivationCodeBatchRequest bulkCreateActivationCodeBatchRequest) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public GetUserAccountByExternalIdResponse getUserAccountByExternalId(
				GetUserAccountByExternalIdRequest getUserAccountByExternalIdRequest) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public ValidateActivationCodeResponse validateActivationCode(
				ValidateActivationCodeRequest validateActivationCodeRequest) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public ValidateUserAccountResponse validateUserAccount(
				ValidateUserAccountRequest validateUserAccountRequest) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public KillUserSessionResponse killUserSession(
				KillUserSessionRequest killUserSessionRequest) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public UpdateLicenseModifiedDateResponse updateLicenseModifiedDate(
				UpdateLicenseModifiedDateRequest updateLicenseModifiedDateRequest) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
}
