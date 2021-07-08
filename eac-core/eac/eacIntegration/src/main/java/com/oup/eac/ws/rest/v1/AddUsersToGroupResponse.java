package com.oup.eac.ws.rest.v1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AddUsersToGroupResponse extends AbstractResponse implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** The userIdAndRoleNamePairsStatus */
	private List<UserIdAndRoleNamePairStatus> userIdAndRoleNamePairsStatus;

	public List<UserIdAndRoleNamePairStatus> getUserIdAndRoleNamePairsStatus() {
		if(userIdAndRoleNamePairsStatus == null){
			userIdAndRoleNamePairsStatus = new ArrayList<UserIdAndRoleNamePairStatus>();
		}
		return userIdAndRoleNamePairsStatus;
	}

	public void setUserIdAndRoleNamePairsStatus(
			List<UserIdAndRoleNamePairStatus> userIdAndRoleNamePairsStatus) {
		this.userIdAndRoleNamePairsStatus = userIdAndRoleNamePairsStatus;
	}
	
	
}
