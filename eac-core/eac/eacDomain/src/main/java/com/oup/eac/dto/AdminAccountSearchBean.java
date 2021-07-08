package com.oup.eac.dto;

import com.oup.eac.domain.Division;
import com.oup.eac.domain.Role;

public class AdminAccountSearchBean {

	private String userName;
	private String familyName;
	private String emailAddress;
	private String firstName;
	private Role selectedRole;
	private Division selectedDivision;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getFamilyName() {
		return familyName;
	}
	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public Role getSelectedRole() {
		return selectedRole;
	}
	public void setSelectedRole(Role selectedRole) {
		this.selectedRole = selectedRole;
	}
	public Division getSelectedDivision() {
		return selectedDivision;
	}
	public void setSelectedDivision(Division selectedDivision) {
		this.selectedDivision = selectedDivision;
	}
	
}
