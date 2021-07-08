package com.oup.eac.ws.rest.v1;

import java.io.Serializable;

public class OupGroupDetailsWS implements Serializable {
	private static final long serialVersionUID = 1L;

	private String groupId;
	private String userRoleName;
	private String invite;

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getUserRoleName() {
		return userRoleName;
	}

	public void setUserRoleName(String userRoleName) {
		this.userRoleName = userRoleName;
	}

	public String getInvite() {
		return invite;
	}

	public void setInvite(String invite) {
		this.invite = invite;
	}

}
