package com.oup.eac.integration.facade.impl;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.http.HttpEntity;

import com.oup.eac.domain.AddUsersToGroup;
import com.oup.eac.domain.EnrolUser;
import com.oup.eac.domain.GenerateUserIdToken;
import com.oup.eac.domain.Platform;
import com.oup.eac.domain.UpdateUserAuth;
import com.oup.eac.domain.UserDetailsReport;
import com.oup.eac.dto.CustomerDto;
import com.oup.eac.integration.facade.ErightsRestFacade;
import com.oup.eac.integration.rest.template.EacRestTemplate;
import com.oup.eac.ws.rest.v1.AbstractResponse;
import com.oup.eac.ws.rest.v1.AddUsersToGroupRequest;
import com.oup.eac.ws.rest.v1.AddUsersToGroupResponse;
import com.oup.eac.ws.rest.v1.CreatePlatformRequest;
import com.oup.eac.ws.rest.v1.CreatePlatformResponse;
import com.oup.eac.ws.rest.v1.CreateUserRequest;
import com.oup.eac.ws.rest.v1.CreateUserResponse;
import com.oup.eac.ws.rest.v1.DeletePlatformRequest;
import com.oup.eac.ws.rest.v1.DeletePlatformResponse;
import com.oup.eac.ws.rest.v1.EnrolUserRequest;
import com.oup.eac.ws.rest.v1.EnrolUserResponse;
import com.oup.eac.ws.rest.v1.GenerateReportRequest;
import com.oup.eac.ws.rest.v1.GenerateReportResponse;
import com.oup.eac.ws.rest.v1.GenerateUserIdTokenRequest;
import com.oup.eac.ws.rest.v1.GenerateUserIdTokenResponse;
import com.oup.eac.ws.rest.v1.GetAllPlatformsRequest;
import com.oup.eac.ws.rest.v1.GetAllPlatformsResponse;
import com.oup.eac.ws.rest.v1.GetAllRolesRequest;
import com.oup.eac.ws.rest.v1.GetAllRolesResponse;
import com.oup.eac.ws.rest.v1.UpdatePlatformRequest;
import com.oup.eac.ws.rest.v1.UpdatePlatformResponse;
import com.oup.eac.ws.rest.v1.UpdateUserAuthRequest;
import com.oup.eac.ws.rest.v1.UpdateUserAuthResponse;



public class ErightsRestFacadeImpl implements ErightsRestFacade {

	private static final Logger LOGGER = Logger
			.getLogger(ErightsRestFacadeImpl.class);

	private static String URI;

	private static final String GET_ALL_ROLES_SERVICE = "getAllRoles";

	private static final String GET_ALL_PLATFORMS_SERVICE = "platforms";

	private static final String CREATE_PLATFORMS_SERVICE = "platforms";

	private static final String UPDATE_PLATFORMS_SERVICE = "platforms";

	private static final String DELETE_PLATFORM_SERVICE = "platforms/remove";

	private static final String GENERATE_USER_DETAILS_SERVICE = "reports";

	private static final String ENROL_USER_ACCOUNT = "enrolUserAccount";

	private static final String GENERATE_USER_ID_TOKEN = "generateUserIdToken";
	
	private static final String CACHE_EVICTION = "evictCache";
	
	private static final String FAIL_FEEDER = "failfeeder";
	
	private static final String UPDATE_USER_AUTH = "updateUserAuth";
	
	private static final String CREATE_USER_ACCOUNT = "createUserAccount";
	
	private static final String ADD_USERS_TO_GROUP = "addUsersToGroup";
	

	EacRestTemplate eacRestTemplate = new EacRestTemplate();

	public static String getURI() {
		return URI;
	}

	public static void setURI(String uRI) {
		URI = uRI;
	}

	@Override
	public GetAllRolesResponse getAllRoles() {
		LOGGER.debug("Calling Rest service Service :: " + URI
				+ GET_ALL_ROLES_SERVICE);
		GetAllRolesResponse response = eacRestTemplate.postForObject(
				EacRestTemplate.API_VERSION_0_1, URI + GET_ALL_ROLES_SERVICE,
				new GetAllRolesRequest(), GetAllRolesResponse.class);
		LOGGER.debug("GetAllRolesResponse :: " + response.getStatus());
		return response;
	}

	@Override
	public GetAllPlatformsResponse getAllPlatforms() {
		LOGGER.debug("Calling Rest service Service :: " + URI
				+ GET_ALL_PLATFORMS_SERVICE);
		GetAllPlatformsResponse response = eacRestTemplate.getForObject(
				EacRestTemplate.API_VERSION_0_1, URI
						+ GET_ALL_PLATFORMS_SERVICE,
				new GetAllPlatformsRequest(), GetAllPlatformsResponse.class);
		LOGGER.debug("GetAllPlatformsResponse :: " + response.getStatus());
		return response;
	}

	@Override
	public DeletePlatformResponse deletePlatform(Platform platform) {
		DeletePlatformRequest deletePlatformRequest = new DeletePlatformRequest();
		deletePlatformRequest = ErightsRestObjectFactory
				.deletePlatform(platform);
		LOGGER.debug("Calling Rest service Service :: " + URI
				+ DELETE_PLATFORM_SERVICE);
		DeletePlatformResponse response = eacRestTemplate.postForObject(
				EacRestTemplate.API_VERSION_0_1, URI + DELETE_PLATFORM_SERVICE,
				deletePlatformRequest, DeletePlatformResponse.class);
		LOGGER.debug("DeletePlatformsResponse :: " + response.getStatus());
		return response;
	}

	@Override
	public CreatePlatformResponse createPlatform(Platform platform) {

		CreatePlatformRequest createPlatformRequest = new CreatePlatformRequest();
		createPlatformRequest = ErightsRestObjectFactory.getPlatform(platform);

		LOGGER.debug("Calling Rest service Service :: " + URI
				+ CREATE_PLATFORMS_SERVICE);
		CreatePlatformResponse response = eacRestTemplate.postForObject(
				EacRestTemplate.API_VERSION_0_1,
				URI + CREATE_PLATFORMS_SERVICE, createPlatformRequest,
				CreatePlatformResponse.class);
		LOGGER.debug("CreatePlatformResponse :: " + response.getStatus());
		return response;

	}

	@Override
	public UpdatePlatformResponse updatePlatform(Platform platform) {
		UpdatePlatformRequest updatePlatformRequest = new UpdatePlatformRequest();
		updatePlatformRequest = ErightsRestObjectFactory.editPlatform(platform);

		HttpEntity<UpdatePlatformRequest> requestEntity = new HttpEntity<UpdatePlatformRequest>(
				updatePlatformRequest);
		LOGGER.debug("Calling Rest service Service :: " + URI
				+ UPDATE_PLATFORMS_SERVICE);
		UpdatePlatformResponse response = eacRestTemplate.putForObject(
				EacRestTemplate.API_VERSION_0_1,
				URI + UPDATE_PLATFORMS_SERVICE, requestEntity,
				UpdatePlatformResponse.class);
		LOGGER.debug("UpdatePlatformResponse :: " + response.getStatus());
		return response;
	}

	@Override
	public GenerateReportResponse generateUserDetailsReport(
			UserDetailsReport userDetailsReport) {
		GenerateReportRequest generateReportRequest = new GenerateReportRequest();
		generateReportRequest = ErightsRestObjectFactory
				.generateReport(userDetailsReport);

		LOGGER.debug("Calling Rest service Service :: " + URI
				+ GENERATE_USER_DETAILS_SERVICE);
		GenerateReportResponse response = eacRestTemplate.postForObject(
				EacRestTemplate.API_VERSION_0_1, URI
						+ GENERATE_USER_DETAILS_SERVICE, generateReportRequest,
				GenerateReportResponse.class);
		LOGGER.debug("GenerateReportResponse :: " + response.getStatus());
		return response;
	}

	@Override
	public EnrolUserResponse enrolUser(EnrolUser enrolUser) {
		EnrolUserRequest enrolUserRequest = new EnrolUserRequest();
		enrolUserRequest = ErightsRestObjectFactory.enrolUser(enrolUser);

		LOGGER.debug("Calling Rest service Service :: " + URI
				+ ENROL_USER_ACCOUNT);
		EnrolUserResponse response = eacRestTemplate.postForObject(
				EacRestTemplate.API_VERSION_0_1, URI + ENROL_USER_ACCOUNT,
				enrolUserRequest, EnrolUserResponse.class);
		LOGGER.debug("EnrolUserResponse :: " + response.getStatus());
		return response;
	}

	@Override
	public GenerateUserIdTokenResponse generateUserIdToken(
			GenerateUserIdToken userToken) {
		GenerateUserIdTokenRequest generateUserIdTokenRequest = new GenerateUserIdTokenRequest();
		generateUserIdTokenRequest = ErightsRestObjectFactory
				.generateUserIdToken(userToken);

		LOGGER.debug("Calling Rest service Service :: " + URI
				+ GENERATE_USER_ID_TOKEN);
		GenerateUserIdTokenResponse response = eacRestTemplate.postForObject(
				EacRestTemplate.API_VERSION_0_1, URI + GENERATE_USER_ID_TOKEN,
				generateUserIdTokenRequest, GenerateUserIdTokenResponse.class);
		LOGGER.debug("GenerateUserIdTokenResponse :: " + response.getStatus());
		return response;
	}

	/* (non-Javadoc)
	 * @see com.oup.eac.integration.facade.ErightsRestFacade#evictCache(java.util.Map)
	 */
	@Override
	public Map<String, String> evictCache(Map<String, String> requestBody) {
		
		LOGGER.debug("Calling Rest service Service :: " + URI + CACHE_EVICTION);
		@SuppressWarnings("unchecked")
		Map<String, String> response = (Map<String, String>) eacRestTemplate.
				postForObject(EacRestTemplate.API_VERSION_0_1, URI
				+ CACHE_EVICTION, requestBody,
				Map.class);
		LOGGER.debug(" EvictCache Response :: " + response);
		return response;
	}
	
	/* (non-Javadoc)
	 * @see com.oup.eac.integration.facade.ErightsRestFacade#insertFailFeeder(java.util.Map)
	 */
	@Override
	public AbstractResponse insertFailFeeder(Map<String, String> requestBody) {
		
		LOGGER.debug("Calling Rest service Service :: " + URI + FAIL_FEEDER);
		AbstractResponse response = eacRestTemplate.
				postForObject(EacRestTemplate.API_VERSION_0_1, URI
				+ FAIL_FEEDER, requestBody,
				AbstractResponse.class);
		LOGGER.debug(" FailFeeder insertion Response :: " + response);
		return response;
	}

	@Override
	public UpdateUserAuthResponse updateUserAuth(UpdateUserAuth userDetail) {
		UpdateUserAuthRequest updateUserAuthRequest = new UpdateUserAuthRequest();
		updateUserAuthRequest = ErightsRestObjectFactory
				.updateUserAuth(userDetail);

		LOGGER.debug("Calling Rest service Service :: " + URI
				+ UPDATE_USER_AUTH);
		UpdateUserAuthResponse response = eacRestTemplate.postForObject(
				EacRestTemplate.API_VERSION_0_1, URI + UPDATE_USER_AUTH,
				updateUserAuthRequest, UpdateUserAuthResponse.class);
		LOGGER.debug("UpdateUserAuthResponse :: " + response.getStatus());
		return response;
	}
	
	
	/* (non-Javadoc)
	 * @see com.oup.eac.integration.facade.ErightsRestFacade#createUser(com.oup.eac.domain.Customer)
	 */
	@Override
	public CreateUserResponse createUser(CustomerDto enrolUser) {
		CreateUserRequest createUser = ErightsRestObjectFactory.createUser(enrolUser);

		LOGGER.debug("Calling Rest service Service :: " + URI
				+ CREATE_USER_ACCOUNT);
		CreateUserResponse response = eacRestTemplate.postForObject(
				EacRestTemplate.API_VERSION_0_1, URI + CREATE_USER_ACCOUNT,
				createUser, CreateUserResponse.class);
		LOGGER.debug("CreateUserResponse :: " + response.getStatus());
		return response;
	}

	/* (non-Javadoc)
	 * @see com.oup.eac.integration.facade.ErightsRestFacade#addUsersToGroup(com.oup.eac.domain.AddUsersToGroup)
	 */
	@Override
	public AddUsersToGroupResponse addUsersToGroup(AddUsersToGroup addUsersToGroup) {
		AddUsersToGroupRequest request = ErightsRestObjectFactory.addUserToGroup(addUsersToGroup);

		LOGGER.debug("Calling Rest service Service :: " + URI
				+ ADD_USERS_TO_GROUP);
		AddUsersToGroupResponse response = eacRestTemplate.postForObject(
				EacRestTemplate.API_VERSION_0_1, URI + ADD_USERS_TO_GROUP,
				request, AddUsersToGroupResponse.class);
		LOGGER.debug("AddUsersToGroupResponse :: " + response.getStatus());
		return response;
	}
}
