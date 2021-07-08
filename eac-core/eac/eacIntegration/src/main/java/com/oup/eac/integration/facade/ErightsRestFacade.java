package com.oup.eac.integration.facade;

import java.util.Map;

import com.oup.eac.domain.AddUsersToGroup;
import com.oup.eac.domain.EnrolUser;
import com.oup.eac.domain.GenerateUserIdToken;
import com.oup.eac.domain.Platform;
import com.oup.eac.domain.UpdateUserAuth;
import com.oup.eac.domain.UserDetailsReport;
import com.oup.eac.dto.CustomerDto;
import com.oup.eac.ws.rest.v1.AbstractResponse;
import com.oup.eac.ws.rest.v1.AddUsersToGroupResponse;
import com.oup.eac.ws.rest.v1.CreatePlatformResponse;
import com.oup.eac.ws.rest.v1.CreateUserResponse;
import com.oup.eac.ws.rest.v1.DeletePlatformResponse;
import com.oup.eac.ws.rest.v1.EnrolUserResponse;
import com.oup.eac.ws.rest.v1.GenerateReportResponse;
import com.oup.eac.ws.rest.v1.GenerateUserIdTokenResponse;
import com.oup.eac.ws.rest.v1.GetAllPlatformsResponse;
import com.oup.eac.ws.rest.v1.GetAllRolesResponse;
import com.oup.eac.ws.rest.v1.UpdatePlatformResponse;
import com.oup.eac.ws.rest.v1.UpdateUserAuthResponse;

public interface ErightsRestFacade {

	public GetAllRolesResponse getAllRoles();
	
	public GetAllPlatformsResponse getAllPlatforms();
	
	public DeletePlatformResponse deletePlatform(Platform request) ;
	
	public CreatePlatformResponse createPlatform(Platform platform);
	
	public UpdatePlatformResponse updatePlatform(Platform platform);

	GenerateReportResponse generateUserDetailsReport(
			UserDetailsReport userDetailsReport);
	
	public EnrolUserResponse enrolUser(EnrolUser enrolUser);
	
	public GenerateUserIdTokenResponse generateUserIdToken(GenerateUserIdToken userToken);
	
	/** Api takes cache pattern as input,
	 * which evicts it redis instance and returns count of keys. 
	 * @param requestBody
	 * @return
	 */
	public Map<String, String> evictCache(Map<String, String> requestBody);
	
	/**Inserts data into fail feeder table,
	 * which can be read after trigger was successful in cloud.
	 * @param requestBody
	 * @return
	 */
	public AbstractResponse insertFailFeeder(Map<String, String> requestBody);
	
	public UpdateUserAuthResponse updateUserAuth(UpdateUserAuth userDetail);
	
	/**CreateUser rest Api.
	 * @param user
	 * @return
	 */
	public CreateUserResponse createUser(CustomerDto user);
	
	/**AddUsersToGroup rest Api.
	 * @param addUsersToGroup
	 * @return
	 */
	public AddUsersToGroupResponse addUsersToGroup(AddUsersToGroup addUsersToGroup);
}
