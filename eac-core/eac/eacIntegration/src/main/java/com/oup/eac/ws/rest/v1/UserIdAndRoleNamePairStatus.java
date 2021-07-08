package com.oup.eac.ws.rest.v1;

import java.io.Serializable;


public class UserIdAndRoleNamePairStatus extends AbstractResponse
		implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The user id. */
	private String userId;

	/** The old role name. */
	private String oldRoleName;

	/** The role name. */
	private String roleName;

	private String oupGroupId;

	/** The error messages. */
	private String errorMessage;

	/** The error code. */
	private String errorCode;

	/** The groupId */
	private String groupId;

	/** The requestorId */
	private String requestorId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getOldRoleName() {
		return oldRoleName;
	}

	public void setOldRoleName(String oldRoleName) {
		this.oldRoleName = oldRoleName;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getOupGroupId() {
		return oupGroupId;
	}

	public void setOupGroupId(String oupGroupId) {
		this.oupGroupId = oupGroupId;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getRequestorId() {
		return requestorId;
	}

	public void setRequestorId(String requestorId) {
		this.requestorId = requestorId;
	}
	
	
	
	

}
