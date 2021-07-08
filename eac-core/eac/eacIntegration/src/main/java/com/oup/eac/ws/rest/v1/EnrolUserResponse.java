package com.oup.eac.ws.rest.v1;

import java.io.Serializable;


public class EnrolUserResponse extends AbstractResponse implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private UserStatus userStatus;

	/** The userIdAndRoleNamePairsStatus */
	private UserIdAndRoleNamePairStatus userIdAndRoleNamePairsStatus;

	public UserStatus getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(UserStatus userStatus) {
		this.userStatus = userStatus;
	}

	public UserIdAndRoleNamePairStatus getUserIdAndRoleNamePairsStatus() {
		return userIdAndRoleNamePairsStatus;
	}

	public void setUserIdAndRoleNamePairsStatus(
			UserIdAndRoleNamePairStatus userIdAndRoleNamePairsStatus) {
		this.userIdAndRoleNamePairsStatus = userIdAndRoleNamePairsStatus;
	}
	
	

}
