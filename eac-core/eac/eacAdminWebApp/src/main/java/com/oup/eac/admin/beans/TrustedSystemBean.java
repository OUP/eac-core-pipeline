package com.oup.eac.admin.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.oup.eac.domain.Customer;
import com.oup.eac.domain.EnrolUser;
import com.oup.eac.domain.TrustedSystem;

public class TrustedSystemBean implements Serializable {
	
	public static final String NEW = "-1" ;
	
	private String selectedTrustedSystemId;
	private String newUserName;
	private String newPassword;
	private String newConfirmPassword;

	private boolean editFlag=false;
	private List<TrustedSystem> trustedSystems = new ArrayList<TrustedSystem>();
	
	public TrustedSystemBean(List<TrustedSystem> trustedSystems){
		this.trustedSystems = trustedSystems ;
	}
	

	public String getNewUserName() {
		return newUserName;
	}
	public void setNewUserName(String newUserName) {
		this.newUserName = newUserName;
	}
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	public String getNewConfirmPassword() {
		return newConfirmPassword;
	}
	public void setNewConfirmPassword(String newConfirmPassword) {
		this.newConfirmPassword = newConfirmPassword;
	}
	public boolean isEditFlag() {
		return editFlag;
	}
	public void setEditFlag(boolean editFlag) {
		this.editFlag = editFlag;
	}
	public List<TrustedSystem> getTrustedSystems() {
		return trustedSystems;
	}
	public void setTrustedSystems(List<TrustedSystem> trustedSystems) {
		this.trustedSystems = trustedSystems;
	}
	public String getSelectedTrustedSystemId() {
		return selectedTrustedSystemId;
	}


	public void setSelectedTrustedSystemId(String selectedTrustedSystemId) {
		this.selectedTrustedSystemId = selectedTrustedSystemId;
	}


	public TrustedSystem getUpdatedTrustedSystems() {
		TrustedSystem selectedTrustedSystem = null;
		
		if (NEW.equals(selectedTrustedSystemId)) {
			selectedTrustedSystem = new TrustedSystem();
			selectedTrustedSystem.setUserName(newUserName);
			selectedTrustedSystem.setPassword(newPassword);
			this.editFlag=false;
		} else {
			for (int i = 0; i < trustedSystems.size(); i++) {
				TrustedSystem trustedSystem = trustedSystems.get(i);
				if (selectedTrustedSystemId.equals(trustedSystem.getUserId())) {
					trustedSystem.setPassword(newPassword);
					selectedTrustedSystem = trustedSystem;
					this.editFlag=true;
					break;
				}
			}
		}
		
		return selectedTrustedSystem;
	}
	
	

}

