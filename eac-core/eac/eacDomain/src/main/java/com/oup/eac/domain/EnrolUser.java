package com.oup.eac.domain;

public class EnrolUser {

	private String firstName;

	private String lastName;

	private String userName;

	private String password;

	private String groupId;

	private String userRole;

	private String matchMode;

	private Boolean suspended;

	private Boolean isTncAccepted;

	private Boolean invite;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public String getMatchMode() {
		return matchMode;
	}

	public void setMatchMode(String matchMode) {
		this.matchMode = matchMode;
	}

	public Boolean getSuspended() {
		return suspended;
	}

	public void setSuspended(Boolean suspended) {
		this.suspended = suspended;
	}

	public Boolean getIsTncAccepted() {
		return isTncAccepted;
	}

	public void setIsTncAccepted(Boolean isTncAccepted) {
		this.isTncAccepted = isTncAccepted;
	}

	public Boolean getInvite() {
		return invite;
	}

	public void setInvite(Boolean invite) {
		this.invite = invite;
	}

}
