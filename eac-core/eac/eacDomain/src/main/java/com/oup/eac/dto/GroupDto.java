package com.oup.eac.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GroupDto implements Serializable  {
	
	private List<Integer> parentIds = new ArrayList<Integer>();
	
	private String name;
	
	private Integer erightsId;
	
	public GroupDto(final Integer erightsId, final GroupDto groupDto) {
		this(erightsId,groupDto.name, groupDto.parentIds);
	}

	/**
	 * Create a group dto. The erightsId must exist in eRights.
	 * 
	 * @param erightsId
	 * @param name
	 * @param parentIds
	 */
	public GroupDto(final Integer erightsId, final String name, final List<Integer> parentIds) {
		this.erightsId = erightsId;
		this.name = name;
		this.parentIds = parentIds;
	}
	
	/**
	 * Create a group dto. The erightsId must exist in eRights.
	 * 
	 * @param erightsId
	 * @param name
	 * @param parentIds
	 */
	public GroupDto(final Integer erightsId, final String name) {
		this.erightsId = erightsId;
		this.name = name;
	}
	
	/**
	 * Create a group dto without knowing the id of the group in eRights.
	 * 
	 * @param name
	 * @param parentIds
	 */
	public GroupDto(final String name, final List<Integer> parentIds) {		
		this.name = name;
		this.parentIds = parentIds;
	}
	
	/**
	 * Create a group dto without knowing the id of the group in eRights.
	 * 
	 * @param name
	 * @param parentIds
	 */
	public GroupDto(final String name) {		
		this.name = name;
	}

	public List<Integer> getParentIds() {
		return parentIds;
	}

	public void setParentIds(List<Integer> parentIds) {
		this.parentIds = parentIds;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getErightsId() {
		return erightsId;
	}

	public void setErightsId(Integer erightsId) {
		this.erightsId = erightsId;
	}
	
	public void addParentGroupId(final Integer id) {
		parentIds.add(id);
	}
}
