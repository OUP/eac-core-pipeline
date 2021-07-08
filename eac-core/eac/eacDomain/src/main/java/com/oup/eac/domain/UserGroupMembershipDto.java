package com.oup.eac.domain;

import java.sql.Timestamp;

public class UserGroupMembershipDto {
	private String userId;
	private String groupId;
	private String roleName;
	private String status;
	private Timestamp invitationExpiryTimeStamp;
	private boolean dismissFlag;
	private String invitationStatus;
	private Timestamp invitationStatusTimestamp;
	private Timestamp reminderTimestamp;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Timestamp getInvitationExpiryTimeStamp() {
		return invitationExpiryTimeStamp;
	}
	public void setInvitationExpiryTimeStamp(Timestamp invitationExpiryTimeStamp) {
		this.invitationExpiryTimeStamp = invitationExpiryTimeStamp;
	}
	public boolean isDismissFlag() {
		return dismissFlag;
	}
	public void setDismissFlag(boolean dismissFlag) {
		this.dismissFlag = dismissFlag;
	}
	public String getInvitationStatus() {
		return invitationStatus;
	}
	public void setInvitationStatus(String invitationStatus) {
		this.invitationStatus = invitationStatus;
	}
	public Timestamp getInvitationStatusTimestamp() {
		return invitationStatusTimestamp;
	}
	public void setInvitationStatusTimestamp(Timestamp invitationStatusTimestamp) {
		this.invitationStatusTimestamp = invitationStatusTimestamp;
	}
	public Timestamp getReminderTimestamp() {
		return reminderTimestamp;
	}
	public void setReminderTimestamp(Timestamp reminderTimestamp) {
		this.reminderTimestamp = reminderTimestamp;
	}
}
