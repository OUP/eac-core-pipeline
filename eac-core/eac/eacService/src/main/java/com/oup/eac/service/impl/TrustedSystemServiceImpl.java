package com.oup.eac.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.constants.SearchDomainFields;
import com.oup.eac.cloudSearch.search.impl.AmazonCloudSearchServiceImpl;
import com.oup.eac.common.utils.EACSettings;
import com.oup.eac.common.utils.crypto.PasswordUtils;
import com.oup.eac.common.utils.exception.ExceptionUtil;
import com.oup.eac.domain.AddUsersToGroup;
import com.oup.eac.domain.AddUsersToGroup.UserIdAndRoleNamePairs;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.GenerateUserIdToken;
import com.oup.eac.domain.TrustedSystem;
import com.oup.eac.domain.UpdateUserAuth;
import com.oup.eac.domain.UserGroupMembershipDto;
import com.oup.eac.domain.search.AmazonSearchFields;
import com.oup.eac.domain.search.AmazonSearchRequest;
import com.oup.eac.domain.search.AmazonSearchResponse;
import com.oup.eac.domain.utils.audit.AuditLogger;
import com.oup.eac.dto.CustomerDto;
import com.oup.eac.dto.LoginPasswordCredentialDto;
import com.oup.eac.integration.facade.ErightsRestFacade;
import com.oup.eac.integration.facade.exceptions.ErightsException;
import com.oup.eac.service.TrustedSystemService;
import com.oup.eac.service.util.ConvertSearch;
import com.oup.eac.ws.rest.v1.AddUsersToGroupResponse;
import com.oup.eac.ws.rest.v1.CreateUserResponse;
import com.oup.eac.ws.rest.v1.DeletePlatformResponse;
import com.oup.eac.ws.rest.v1.GenerateUserIdTokenResponse;
import com.oup.eac.ws.rest.v1.UpdateUserAuthResponse;

@Service("trustedSystemService")
public class TrustedSystemServiceImpl implements TrustedSystemService {
	private static final Logger LOG = Logger.getLogger(TrustedSystemServiceImpl.class);
	private final ErightsRestFacade erightsRestFacade;
	private static String USER_ROLE_TRUSTED_SYSTEM = "TRUSTED_SYSTEM" ;
	
	@Autowired
	public TrustedSystemServiceImpl(ErightsRestFacade erightsRestFacade) {
		this.erightsRestFacade = erightsRestFacade ;
		
	}
	
	@Override
	public List<String> createTrustedSystem(TrustedSystem trustedSystem) throws ErightsException {
		//serach groupId of MasterGroup
		String masterGroupId = fetchOupIdOfMasterGroupFromMasterGroupName(EACSettings.getProperty(EACSettings.CES_MASTER_GROUP_NAME),EACSettings.getProperty(EACSettings.CES_MASTER_GROUP_TYPE)) ;
		//dev master id
		//masterGroupId = "d63b0150-64cb-11e8-b9ff-4ddb5ec73393" ;
		
		//generate password token
		String kmsPassword = null ;
		try {
			kmsPassword = PasswordUtils.getKMSEncryptedCode(trustedSystem.getPassword()) ;
		} catch (Exception e) {
			LOG.error(ExceptionUtil.getStackTrace(e));
		}
		
		Customer customer = new Customer() ;
		customer.setUsername(trustedSystem.getUserName());
		CustomerDto user = new CustomerDto(customer);
		user.setTncAccepted(true);
		user.setConcurrency(999999999);
		user.setSuspended(false);
		user.setFamilyName(USER_ROLE_TRUSTED_SYSTEM);
		user.setFirstName(trustedSystem.getUserName());
		user.setPrivacyPolicyAccepted(true);
		LoginPasswordCredentialDto loginPassword = new LoginPasswordCredentialDto(trustedSystem.getUserName(), kmsPassword) ;
		user.setLoginPasswordCredential(loginPassword);
		trustedSystem.setPassword(kmsPassword);
		
		
		List<String> errorMessages = new ArrayList<String>();
		
		CreateUserResponse response = erightsRestFacade.createUser(user) ;
		Map<String, String> map =  response.getErrorMessages();
		for(Entry<String, String> element : map.entrySet()) {
			errorMessages.add(element.getValue());
		}
		if (response.getErrorMessage() != null && !response.getErrorMessage().isEmpty()){
			errorMessages.add(response.getErrorMessage()) ;
		}
		String userId = null ;
		if (errorMessages.size() == 0 ) {
			userId = response.getUserId();
		} else {
			return errorMessages ;
		}
		
		List<String> errorMessagesForAddUserToGroup = new ArrayList<String>();
		AddUsersToGroup addUsersToGroup = new AddUsersToGroup() ;
		UserIdAndRoleNamePairs userAndRole =new AddUsersToGroup().new UserIdAndRoleNamePairs();
		userAndRole.setRoleName(USER_ROLE_TRUSTED_SYSTEM);
		userAndRole.setUserId(userId);
		List<AddUsersToGroup.UserIdAndRoleNamePairs> pairs = new ArrayList<AddUsersToGroup.UserIdAndRoleNamePairs>() ;
		pairs.add(userAndRole) ;
		addUsersToGroup.setUserIdAndRoleNamePairs(pairs);
		addUsersToGroup.setInviteFlag(String.valueOf(false));
		addUsersToGroup.setGroupId(masterGroupId);
		AddUsersToGroupResponse addUsersToGroupResponse = erightsRestFacade.addUsersToGroup(addUsersToGroup) ;
		Map<String, String> addUsersToGroupMap =  addUsersToGroupResponse.getErrorMessages();
		for(Entry<String, String> element : addUsersToGroupMap.entrySet()) {
			errorMessagesForAddUserToGroup.add(element.getValue());
		}
		if (addUsersToGroupResponse.getErrorMessage() != null && !addUsersToGroupResponse.getErrorMessage().isEmpty()){
			errorMessagesForAddUserToGroup.add(addUsersToGroupResponse.getErrorMessage()) ;
		}
		return errorMessagesForAddUserToGroup;
	}
	
	@Override
	public List<String> updateTrustedSystem(TrustedSystem trustedSystem) throws ErightsException {
		GenerateUserIdToken userToken = new GenerateUserIdToken() ;
		userToken.setGenerateFlag(true);
		userToken.setTokenType("UPDATE_AUTH");
		userToken.setUserId(trustedSystem.getUserId());
		GenerateUserIdTokenResponse response = erightsRestFacade.generateUserIdToken(userToken) ;
		
		List<String> errorMessages = new ArrayList<String>();
		Map<String, String> map =  response.getErrorMessages();
		for(Entry<String, String> element : map.entrySet()) {
			errorMessages.add(element.getValue());
		}
		if (errorMessages.size() > 0 ) {
			return errorMessages ;
		}
		String token = null ;
		if ( response.getUserTokenDetails().size() > 0 ) {
			token = response.getUserTokenDetails().get(0).getUserToken() ;
		}
		String kmsPassword = null ;
		try {
			kmsPassword = PasswordUtils.getKMSEncryptedCode(trustedSystem.getPassword()) ;
		} catch (Exception e) {
			LOG.error(ExceptionUtil.getStackTrace(e));
		}
		
		UpdateUserAuth userDetail = new UpdateUserAuth() ;
		userDetail.setUserId(trustedSystem.getUserId());
		userDetail.setToken(token);
		userDetail.setNewPassword(kmsPassword);
		
		
		UpdateUserAuthResponse authresponse= erightsRestFacade.updateUserAuth(userDetail) ;
		
		List<String> errorMessagesAuth = new ArrayList<String>();
		Map<String, String> authMap =  authresponse.getErrorMessages();
		for(Entry<String, String> element : authMap.entrySet()) {
			errorMessagesAuth.add(element.getValue());
		}
		return errorMessagesAuth;
		
	}

	@Override
	public List<String> deleteTrustedSystem(TrustedSystem trustedSystem) throws Exception {
		List<String> errorMessages = new ArrayList<String>();
		DeletePlatformResponse response = null ; //erightsRestFacade.deletePlatform(request);
		if (response.getErrorMessage() != null) {
			errorMessages.add(response.getErrorMessage());
		}
		Map<String, String> map = response.getErrorMessages();
		if (map.size() > 0 && map != null) {
			for (Entry<String, String> element : map.entrySet()) {
				errorMessages.add(element.getValue());
			}
		}
		return errorMessages;
	}
	
	
	private String fetchOupIdOfMasterGroupFromMasterGroupName(final String masterGroupName, final String masterGroupType) {
		AuditLogger.logEvent("Search fetchOupIdOfMasterGroupFromMasterGroupName", " masterGroupName:"+masterGroupName);
    	//cloudsearch
    	AmazonSearchRequest request = new AmazonSearchRequest();
    	request.setResultsPerPage(1000);
    	
    	List<AmazonSearchFields> searchFieldsList = new ArrayList<AmazonSearchFields>();    	
    	
    	if(masterGroupName != null){
    		AmazonSearchFields searchField = new AmazonSearchFields();
    		searchField.setName(SearchDomainFields.GROUP_GROUPNAME);
    		searchField.setValue(masterGroupName);
    		searchField.setType("String");
    		searchFieldsList.add(searchField);
    	}
    	if(masterGroupType != null){
    		AmazonSearchFields searchField = new AmazonSearchFields();
    		searchField.setName(SearchDomainFields.GROUP_GROUPTYPE);
    		searchField.setValue(masterGroupType);
    		searchField.setType("String");
    		searchFieldsList.add(searchField);
    	}
    	
    	List<String> searchResultFieldsList = new ArrayList<String>();
		searchResultFieldsList.add(SearchDomainFields.GROUP_GROUPID);
		searchResultFieldsList.add(SearchDomainFields.GROUP_GROUPNAME);
		
		
		HashMap<String, String> hMap1 = new HashMap<String, String>();
		hMap1.put(SearchDomainFields.GROUP_GROUPNAME, "asc");
		request.setSortFields(hMap1);
		request.setSearchResultFieldsList(searchResultFieldsList);
    	request.setSearchFieldsList(searchFieldsList);
    	
    	AmazonCloudSearchServiceImpl cloudSearch = new AmazonCloudSearchServiceImpl();
    	AmazonSearchResponse response = cloudSearch.searchGroup(request);
    	
    	ConvertSearch convert = new ConvertSearch();
    	String masterGroupId = convert.convertGroupSearch(response).get(0).getGroupId();
    	
		return masterGroupId;
	}

	@Override
	public List<TrustedSystem> fetchAllTrustedSystems() throws Exception {
		List<String> userIds = fetchUserIdsOfTrustedUser() ;
		
		return searchUserNameFromUserIdList(userIds);
	}
	
	private List<TrustedSystem> searchUserNameFromUserIdList(List<String> userIds){
		AmazonSearchRequest request = new AmazonSearchRequest();
    	
    	request.setStartAt(0);
    	request.setResultsPerPage(1000);
    	
    	List<AmazonSearchFields> searchFieldsList = new ArrayList<AmazonSearchFields>();
    	List<Customer> customers = null;
    	String userIdList = new String() ;
    	for (String userId : userIds) {
    		userIdList +=userId + "," ;
    	}
    	AmazonSearchFields searchField = new AmazonSearchFields();
		searchField.setName(SearchDomainFields.USER_USERID);
		searchField.setValue(userIdList);
		searchField.setType("String");
		searchFieldsList.add(searchField);
		
		
    	List<String> searchResultFieldsList = new ArrayList<String>();
		searchResultFieldsList.add(SearchDomainFields.USER_USERID);
		searchResultFieldsList.add(SearchDomainFields.USER_USERNAME);
		
		HashMap<String, String> hMap1 = new HashMap<String, String>();
		hMap1.put("createddate", "desc");
		
		request.setSortFields(hMap1);
		request.setSearchResultFieldsList(searchResultFieldsList);
		
    	request.setSearchFieldsList(searchFieldsList);
    	
    	System.out.println(request);
    	
    	AmazonCloudSearchServiceImpl cloudSearch = new AmazonCloudSearchServiceImpl();
    	AmazonSearchResponse response = null;
    	List<TrustedSystem> trustedSystems = new ArrayList<TrustedSystem>() ;
    	
		response = cloudSearch.searchUser(request,false,false);
		ConvertSearch convert = new ConvertSearch();
    	customers = convert.convertCustomer(response);
    	for (Customer customer : customers ) {
    		TrustedSystem trustedSystem = new TrustedSystem() ;
    		trustedSystem.setUserId(customer.getId());
    		trustedSystem.setUserName(customer.getUsername());
    		trustedSystems.add(trustedSystem);
    	}
		return trustedSystems;
	}
	private List<String> fetchUserIdsOfTrustedUser(){
		AuditLogger.logEvent("Search fetchUserIdsOfTrustedUser");
    	//cloudsearch
    	AmazonSearchRequest request = new AmazonSearchRequest();
    	request.setResultsPerPage(1000);
    	
    	List<AmazonSearchFields> searchFieldsList = new ArrayList<AmazonSearchFields>();    	
    	
		AmazonSearchFields searchField = new AmazonSearchFields();
		searchField.setName(SearchDomainFields.USER_GRP_ROLENAME);
		searchField.setValue(USER_ROLE_TRUSTED_SYSTEM);
		searchField.setType("String");
		searchFieldsList.add(searchField);
    	
    	List<String> searchResultFieldsList = new ArrayList<String>();
		searchResultFieldsList.add(SearchDomainFields.USER_GRP_USERID);
		
		
		HashMap<String, String> hMap1 = new HashMap<String, String>();
		request.setSortFields(hMap1);
		request.setSearchResultFieldsList(searchResultFieldsList);
    	request.setSearchFieldsList(searchFieldsList);
    	
    	AmazonCloudSearchServiceImpl cloudSearch = new AmazonCloudSearchServiceImpl();
    	AmazonSearchResponse response = cloudSearch.searchUserGroupMembership(request) ;
    	
    	ConvertSearch convert = new ConvertSearch();
    	List<UserGroupMembershipDto> userGroupMembership = convert.convertUserGroupMembershipSearch(response) ;
    	Set<String> userIds = new HashSet<String>() ;
    	for (UserGroupMembershipDto UserGroupMembershipDto : userGroupMembership) {
    		userIds.add(UserGroupMembershipDto.getUserId()) ;
    	}
    	List<String> userIdList = new ArrayList<String>() ;
    	userIdList.addAll(userIds) ;
		return userIdList;
	}
}
