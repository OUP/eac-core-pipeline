package com.oup.eac.ws.rest.v1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.oup.eac.domain.AddUsersToGroup.UserIdAndRoleNamePairs;

public class AddUsersToGroupRequest implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * groupId.
	 */
	private String groupId;
	
	/** The userIdAndRoleNamePairsStatus */
	private List<UserIdAndRoleNamePairs> userIdAndRoleNamePairs;
	
	/**
	 * inviteFlag.
	 */
	private String inviteFlag;

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public List<UserIdAndRoleNamePairs> getUserIdAndRoleNamePairs() {
		if(userIdAndRoleNamePairs == null){
			userIdAndRoleNamePairs = new ArrayList<UserIdAndRoleNamePairs>();
		}
		return userIdAndRoleNamePairs;
	}

	public void setUserIdAndRoleNamePairs(
			List<UserIdAndRoleNamePairs> userIdAndRoleNamePairs) {
		this.userIdAndRoleNamePairs = userIdAndRoleNamePairs;
	}

	public String getInviteFlag() {
		return inviteFlag;
	}

	public void setInviteFlag(String inviteFlag) {
		this.inviteFlag = inviteFlag;
	}
	
	
}
