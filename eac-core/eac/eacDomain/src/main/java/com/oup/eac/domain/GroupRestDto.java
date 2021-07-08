package com.oup.eac.domain;

import java.util.List;

import org.joda.time.DateTime;

public class GroupRestDto extends BaseDomainObject {
	private String groupId;	
	private String groupName;	
	private String groupType;	
	private DateTime createdDate;	
	private String archiveStatus;	
	private List<String> parentIds;	
	private String groupUniqueId;	
	private String primaryEmailAddress;	
	private String groupCountryCode;	
	private String groupCurriculumType;	
	private String groupRegistrationStatus;
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getGroupType() {
		return groupType;
	}
	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}
	public DateTime getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(DateTime createdDate) {
		this.createdDate = createdDate;
	}
	public String getArchiveStatus() {
		return archiveStatus;
	}
	public void setArchiveStatus(String archiveStatus) {
		this.archiveStatus = archiveStatus;
	}
	
	public List<String> getParentIds() {
		return parentIds;
	}
	public void setParentIds(List<String> parentIds) {
		this.parentIds = parentIds;
	}
	public String getGroupUniqueId() {
		return groupUniqueId;
	}
	public void setGroupUniqueId(String groupUniqueId) {
		this.groupUniqueId = groupUniqueId;
	}
	public String getPrimaryEmailAddress() {
		return primaryEmailAddress;
	}
	public void setPrimaryEmailAddress(String primaryEmailAddress) {
		this.primaryEmailAddress = primaryEmailAddress;
	}
	public String getGroupCountryCode() {
		return groupCountryCode;
	}
	public void setGroupCountryCode(String groupCountryCode) {
		this.groupCountryCode = groupCountryCode;
	}
	public String getGroupCurriculumType() {
		return groupCurriculumType;
	}
	public void setGroupCurriculumType(String groupCurriculumType) {
		this.groupCurriculumType = groupCurriculumType;
	}
	public String getGroupRegistrationStatus() {
		return groupRegistrationStatus;
	}
	public void setGroupRegistrationStatus(String groupRegistrationStatus) {
		this.groupRegistrationStatus = groupRegistrationStatus;
	}
}
