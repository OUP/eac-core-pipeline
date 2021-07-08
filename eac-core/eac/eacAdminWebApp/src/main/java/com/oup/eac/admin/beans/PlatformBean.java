package com.oup.eac.admin.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.oup.eac.domain.ExternalSystem;
import com.oup.eac.domain.Platform;

public class PlatformBean implements Serializable {
	
	public static final Integer NEW = -1 ;
	
	private Integer selectedPlatformGuid;
	private String newPlatformName;
	private String newPlatformCode;
	private String newPlatformDescription;

	private boolean editFlag=false;
	private List<Platform> platforms = new ArrayList<Platform>();
	
	public Platform getUpdatedPlatform() {
		Platform selectedPlatform = null;
		
		if (NEW.equals(selectedPlatformGuid)) {
			selectedPlatform = new Platform();
			selectedPlatform.setName(newPlatformName);
			selectedPlatform.setCode(newPlatformCode);
			selectedPlatform.setDescription(newPlatformDescription);
			this.editFlag=false;
		}
		else
		{
			for (int i = 0; i < platforms.size(); i++) {
				Platform platform = platforms.get(i);
				if (selectedPlatformGuid.equals(platform.getPlatformId())) {
					this.editFlag=true;
					selectedPlatform = platform;
					break;
				}
			}
			this.editFlag=true;
		}
		
		return selectedPlatform;
	}
	
	public Integer getSelectedPlatformGuid() {
		return selectedPlatformGuid;
	}

	public void setSelectedPlatformGuid(Integer selectedPlatformGuid) {
		this.selectedPlatformGuid = selectedPlatformGuid;
	}
	
	public PlatformBean(final List<Platform> platforms) {
		this.platforms = platforms;
	}

	public List<Platform> getPlatforms() {
		return platforms;
	}
	
	public void setPlatforms(final List<Platform> platforms) {
		
		this.platforms = platforms;
	}
	
	public String getNewPlatformName() {
		return newPlatformName;
	}

	public void setNewPlatformName(String newPlatformName) {
		this.newPlatformName = newPlatformName;
	}

	public String getNewPlatformCode() {
		return newPlatformCode;
	}

	public void setNewPlatformCode(String newPlatformCode) {
		this.newPlatformCode = newPlatformCode;
	}

	public String getNewPlatformDescription() {
		return newPlatformDescription;
	}

	public void setNewPlatformDescription(String newPlatformDescription) {
		this.newPlatformDescription = newPlatformDescription;
	}
	
	public boolean isEditFlag() {
		return editFlag;
	}

	public void setEditFlag(boolean editFlag) {
		this.editFlag = editFlag;
	}

}
