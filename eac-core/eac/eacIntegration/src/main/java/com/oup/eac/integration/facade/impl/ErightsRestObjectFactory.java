package com.oup.eac.integration.facade.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.oup.eac.domain.AddUsersToGroup;
import com.oup.eac.domain.AddUsersToGroup.UserIdAndRoleNamePairs;
import com.oup.eac.domain.EnrolUser;
import com.oup.eac.domain.GenerateUserIdToken;
import com.oup.eac.domain.Platform;
import com.oup.eac.domain.UpdateUserAuth;
import com.oup.eac.domain.UserDetailsReport;
import com.oup.eac.dto.CustomerDto;
import com.oup.eac.dto.LoginPasswordCredentialDto;
import com.oup.eac.ws.rest.v1.AddUsersToGroupRequest;
import com.oup.eac.ws.rest.v1.CreatePlatformRequest;
import com.oup.eac.ws.rest.v1.CreateUserRequest;
import com.oup.eac.ws.rest.v1.DeletePlatformRequest;
import com.oup.eac.ws.rest.v1.EnrolUserRequest;
import com.oup.eac.ws.rest.v1.GenerateReportRequest;
import com.oup.eac.ws.rest.v1.GenerateReportRequest.ReportName;
import com.oup.eac.ws.rest.v1.GenerateUserIdTokenRequest;
import com.oup.eac.ws.rest.v1.OupAuthProfileWS;
import com.oup.eac.ws.rest.v1.OupCredentialLoginPasswordWS;
import com.oup.eac.ws.rest.v1.OupCredentialWS;
import com.oup.eac.ws.rest.v1.OupGroupDetailsWS;
import com.oup.eac.ws.rest.v1.OupUserEnrollWS;
import com.oup.eac.ws.rest.v1.UpdatePlatformRequest;
import com.oup.eac.ws.rest.v1.UpdateUserAuthRequest;
import com.oup.eac.ws.rest.v1.UserDetailsGenerateUserToken;
import com.oup.eac.ws.rest.v1.UserIdAndRoleNamePairStatus;

public class ErightsRestObjectFactory {

	public static CreatePlatformRequest getPlatform(Platform platform) {

		CreatePlatformRequest createPlatformRequest = new CreatePlatformRequest();

		if (platform != null) {
			if (platform.getCode() != null) {
				createPlatformRequest.setCode(platform.getCode());
			}

			if (platform.getName() != null) {
				createPlatformRequest.setName(platform.getName());
			}

			if (platform.getDescription() != null) {
				createPlatformRequest.setDescription(platform.getDescription());
			}
		}

		return createPlatformRequest;
	}

	public static UpdatePlatformRequest editPlatform(Platform platform) {

		UpdatePlatformRequest updatePlatformRequest = new UpdatePlatformRequest();

		if (platform != null) {
			if (platform.getCode() != null) {
				updatePlatformRequest.setCode(platform.getCode());
			}

			if (platform.getName() != null) {
				updatePlatformRequest.setName(platform.getName());
			}

			if (platform.getDescription() != null) {
				updatePlatformRequest.setDescription(platform.getDescription());
			}
		}

		return updatePlatformRequest;
	}

	/**
	 * to convert platform into deletePlatformRequest
	 * 
	 * @param platform
	 * @return
	 */
	public static DeletePlatformRequest deletePlatform(Platform platform) {

		DeletePlatformRequest deletePlatformRequest = new DeletePlatformRequest();

		if (platform != null) {
			if (platform.getCode() != null) {
				deletePlatformRequest.setCode(platform.getCode());
			}

		}

		return deletePlatformRequest;
	}

	public static GenerateReportRequest generateReport(
			UserDetailsReport userDetailsReport) {
		GenerateReportRequest request = new GenerateReportRequest();
		if (userDetailsReport.getUserId() != null
				&& !userDetailsReport.getUserId().isEmpty()) {
			Map<String, Object> requestParams = new HashMap<String, Object>();
			requestParams.put("userId", userDetailsReport.getUserId());
			request.setRequestParams(requestParams);
		}
		if (userDetailsReport.getAdminEmailAddress() != null
				&& !userDetailsReport.getAdminEmailAddress().isEmpty()) {
			request.setRequestorEmail(userDetailsReport.getAdminEmailAddress());
		}
		request.setReportName(ReportName.RIGHT_TO_SEE_MY_DATA.toString());
		return request;
	}

	public static EnrolUserRequest enrolUser(EnrolUser enrolUser) {
		EnrolUserRequest request = new EnrolUserRequest();
		OupGroupDetailsWS groupDetails = new OupGroupDetailsWS();
		OupUserEnrollWS userInfo = new OupUserEnrollWS();
		OupAuthProfileWS authProfile = new OupAuthProfileWS();
		OupCredentialWS credentialDataWS = new OupCredentialWS();
		OupCredentialLoginPasswordWS loginPasswordCredential = new OupCredentialLoginPasswordWS();

		if (StringUtils.isNotBlank(enrolUser.getFirstName())) {
			userInfo.setFirstName(enrolUser.getFirstName());
		}
		if (StringUtils.isNotBlank(enrolUser.getLastName())) {
			userInfo.setLastName(enrolUser.getLastName());
		}
		if (StringUtils.isNotBlank(enrolUser.getUserName())) {
			loginPasswordCredential.setUsername(enrolUser.getUserName());
		}
		if (StringUtils.isNotBlank(enrolUser.getPassword())) {
			loginPasswordCredential.setPassword(enrolUser.getPassword());
		}

		credentialDataWS.setLoginPasswordCredential(loginPasswordCredential);
		authProfile.getCredentials().add(credentialDataWS);
		authProfile.setMatchMode(enrolUser.getMatchMode());
		userInfo.getAuthProfile().add(authProfile);

		userInfo.setIsTnCAccepted(String.valueOf(enrolUser.getIsTncAccepted()));
		userInfo.setSuspended(String.valueOf(enrolUser.getSuspended()));

		request.setUser(userInfo);

		if (StringUtils.isNotBlank(enrolUser.getGroupId())) {
			groupDetails.setGroupId(enrolUser.getGroupId());
		}
		if (StringUtils.isNotBlank(enrolUser.getUserRole())) {
			groupDetails.setUserRoleName(enrolUser.getUserRole());
		}
		groupDetails.setInvite(String.valueOf(enrolUser.getInvite()));

		request.setGroupDetails(groupDetails);

		return request;
	}

	public static GenerateUserIdTokenRequest generateUserIdToken(
			GenerateUserIdToken userToken) {

		GenerateUserIdTokenRequest generateUserIdTokenRequest = new GenerateUserIdTokenRequest();
		UserDetailsGenerateUserToken userDetailsGenerateUserToken = new UserDetailsGenerateUserToken();

		if (StringUtils.isNotBlank(userToken.getUserId())) {
			userDetailsGenerateUserToken.setUserId(userToken.getUserId());
		}

		if (StringUtils.isNotBlank(userToken.getUserName())) {
			userDetailsGenerateUserToken.setUserName(userToken.getUserName());
		}
		generateUserIdTokenRequest.getUserDetails().add(
				userDetailsGenerateUserToken);
		generateUserIdTokenRequest.setTokenType(userToken.getTokenType());
		if (userToken.getGenerateFlag() != null) {
			generateUserIdTokenRequest.setGenerateFlag(String.valueOf(userToken
					.getGenerateFlag()));
		}
		return generateUserIdTokenRequest;

	}

	public static UpdateUserAuthRequest updateUserAuth(
			UpdateUserAuth updateUserAuth) {

		UpdateUserAuthRequest updateUserAuthRequest = new UpdateUserAuthRequest();
		com.oup.eac.ws.rest.v1.UpdateUserAuth user = new com.oup.eac.ws.rest.v1.UpdateUserAuth();

		user.setUserId(updateUserAuth.getUserId());
		user.setToken(updateUserAuth.getToken());

		if (StringUtils.isNotBlank(updateUserAuth.getNewPassword())) {
			user.setNewPassword(updateUserAuth.getNewPassword());
		}
		if (StringUtils.isNotBlank(updateUserAuth.getNewUserName())) {
			user.setNewUserName(updateUserAuth.getNewUserName());
		}

		updateUserAuthRequest.setUser(user);

		return updateUserAuthRequest;

	}
	
	/**Converter from customer Object.
	 * to createUserAccount rest EAC api
	 * @param enrolUser
	 * @return
	 */
	public static CreateUserRequest createUser(CustomerDto enrolUser) {
		CreateUserRequest request = new CreateUserRequest();
		OupUserEnrollWS userInfo = new OupUserEnrollWS();
		OupAuthProfileWS authProfile = new OupAuthProfileWS();
		OupCredentialWS credentialDataWS = new OupCredentialWS();
		OupCredentialLoginPasswordWS loginPasswordCredential = new OupCredentialLoginPasswordWS();

		if (StringUtils.isNotBlank(enrolUser.getFirstName())) {
			userInfo.setFirstName(enrolUser.getFirstName());
		}
		if (StringUtils.isNotBlank(enrolUser.getFamilyName())) {
			userInfo.setLastName(enrolUser.getFamilyName());
		}
		if(enrolUser.getLoginPasswordCredential() != null){
			LoginPasswordCredentialDto login = enrolUser.getLoginPasswordCredential();
			if(StringUtils.isNotBlank(login.getUsername())){
				loginPasswordCredential.setUsername(login.getUsername());	
			}
			if(StringUtils.isNotBlank(login.getPassword())){
				loginPasswordCredential.setPassword(login.getPassword());
			}
		}
		/* if (StringUtils.isNotBlank(enrolUser.getLoginPasswordCredential())) {
			loginPasswordCredential.setUsername(enrolUser.getUsername());
		}
		if (StringUtils.isNotBlank(enrolUser.getLoginPasswordCredential())) {
			loginPasswordCredential.setPassword(enrolUser.getPassword());
		}*/

		credentialDataWS.setLoginPasswordCredential(loginPasswordCredential);
		authProfile.getCredentials().add(credentialDataWS);
		authProfile.setMatchMode("ANY");
		userInfo.getAuthProfile().add(authProfile);

		userInfo.setIsTnCAccepted(String.valueOf(enrolUser.isTncAccepted()));
		userInfo.setSuspended(String.valueOf(enrolUser.isSuspended()));
		userInfo.setConcurrency(String.valueOf(enrolUser.getConcurrency()));
		userInfo.setPrivacyPolicyAccepted(String.valueOf(enrolUser.isPrivacyPolicyAccepted()));
		request.setUser(userInfo);
		

		return request;
	}
	
	
	/**addUserToGroup convert to api request.
	 * @param addUserToGroup
	 * @return
	 */
	public static AddUsersToGroupRequest addUserToGroup(AddUsersToGroup addUserToGroup) {
		AddUsersToGroupRequest request = new AddUsersToGroupRequest();
		request.setGroupId(addUserToGroup.getGroupId());
		for(UserIdAndRoleNamePairs pair : addUserToGroup.getUserIdAndRoleNamePairs()){
			UserIdAndRoleNamePairs requestPair = new AddUsersToGroup().new UserIdAndRoleNamePairs();
			requestPair.setUserId(pair.getUserId());
			requestPair.setRoleName(pair.getRoleName());
			request.getUserIdAndRoleNamePairs().add(requestPair);
		}
		request.setInviteFlag(addUserToGroup.getInviteFlag());
		return request;
	}
}
