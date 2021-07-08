package com.oup.eac.domain;

import java.util.ArrayList;
import java.util.List;

public class AddUsersToGroup {

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
			userIdAndRoleNamePairs = new ArrayList<AddUsersToGroup.UserIdAndRoleNamePairs>();
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
	
	public class UserIdAndRoleNamePairs {
		/** The user id. */
	    protected String userId;

	    /** The role name. */
	    protected String roleName;

		public String getUserId() {
			return userId;
		}

		public void setUserId(String userId) {
			this.userId = userId;
		}

		public String getRoleName() {
			return roleName;
		}

		public void setRoleName(String roleName) {
			this.roleName = roleName;
		}
	    
	    
	}
	
}
