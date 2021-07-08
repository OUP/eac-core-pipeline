package com.oup.eac.integration.facade.impl;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.oup.eac.domain.AddUsersToGroup;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.EnrolUser;
import com.oup.eac.domain.GenerateUserIdToken;
import com.oup.eac.domain.Platform;
import com.oup.eac.domain.UpdateUserAuth;
import com.oup.eac.domain.UserDetailsReport;
import com.oup.eac.dto.CustomerDto;
import com.oup.eac.integration.facade.ErightsFacade;
import com.oup.eac.integration.facade.ErightsRestFacade;
import com.oup.eac.ws.rest.v1.AbstractResponse;
import com.oup.eac.ws.rest.v1.AddUsersToGroupRequest;
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
@Service("rewindableErightsRestFacade")
public class RewindableErightsRestFacade implements ErightsRestFacade {

	private final Logger LOGGER = Logger.getLogger(RewindableErightsRestFacade.class);

	private final ErightsRestFacade erightsRestFacade;

	@Autowired
	public RewindableErightsRestFacade(@Qualifier("erightsRestFacade") ErightsRestFacade erightsRestFacade) {
		this.erightsRestFacade = erightsRestFacade;
	}

	
	@Override
	public GetAllRolesResponse getAllRoles() {
		return erightsRestFacade.getAllRoles();
	}

	@Override
	public GetAllPlatformsResponse getAllPlatforms() {
		return erightsRestFacade.getAllPlatforms();
	}

	@Override
	public DeletePlatformResponse deletePlatform(Platform request) {
		return erightsRestFacade.deletePlatform(request);
	}

	@Override
	public CreatePlatformResponse createPlatform(Platform platform) {
		return erightsRestFacade.createPlatform(platform);
	}

	@Override
	public UpdatePlatformResponse updatePlatform(Platform platform) {
		return erightsRestFacade.updatePlatform(platform);
	}


	@Override
	public GenerateReportResponse generateUserDetailsReport(
			UserDetailsReport userDetailsReport) {
		// TODO Auto-generated method stub
		return erightsRestFacade.generateUserDetailsReport(userDetailsReport);
	}


	@Override
	public EnrolUserResponse enrolUser(EnrolUser enrolUser) {
		// TODO Auto-generated method stub
		return erightsRestFacade.enrolUser(enrolUser);
	}


	@Override
	public GenerateUserIdTokenResponse generateUserIdToken(
			GenerateUserIdToken userToken) {
		// TODO Auto-generated method stub
		return erightsRestFacade.generateUserIdToken(userToken);
	}


	@Override
	public Map<String, String> evictCache(Map<String, String> requestBody) {
		return erightsRestFacade.evictCache(requestBody);
	}


	@Override
	public AbstractResponse insertFailFeeder(Map<String, String> requestBody) {
		return erightsRestFacade.insertFailFeeder(requestBody);
	}


	@Override
	public UpdateUserAuthResponse updateUserAuth(UpdateUserAuth userDetail) {
		// TODO Auto-generated method stub
		return erightsRestFacade.updateUserAuth(userDetail);
	}


	/* (non-Javadoc)
	 * @see com.oup.eac.integration.facade.ErightsRestFacade#createUser(com.oup.eac.domain.Customer)
	 */
	@Override
	public CreateUserResponse createUser(CustomerDto user) {
		return erightsRestFacade.createUser(user);
	}


	/* (non-Javadoc)
	 * @see com.oup.eac.integration.facade.ErightsRestFacade#addUsersToGroup(com.oup.eac.ws.rest.v1.AddUsersToGroupRequest)
	 */
	@Override
	public AddUsersToGroupResponse addUsersToGroup(AddUsersToGroup addUsersToGroup){
		return erightsRestFacade.addUsersToGroup(addUsersToGroup);
	}

}
