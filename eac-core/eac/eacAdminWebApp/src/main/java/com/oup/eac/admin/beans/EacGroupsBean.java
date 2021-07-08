package com.oup.eac.admin.beans;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.oup.eac.domain.AdminUser;
import com.oup.eac.domain.EacGroups;
import com.oup.eac.domain.Product;

public class EacGroupsBean implements Serializable{

	
	private String groupId;
	private String groupName;
	private boolean editMode;
	//private Set<Product> productList=new HashSet<Product>();
	private Map<String , String> existingProducts = new HashMap<String, String>();
	private Set<String> productIdsToAdd = new HashSet<String>();
	private Set<String> productIdsToRemove = new HashSet<String>();
	private boolean editable;
	private AdminUser createdBy;
	private AdminUser updatedBy;
	
	public EacGroupsBean(String groupId){
		editMode=false;
		editable=true; 
		this.groupId=groupId;
	}
	
	public EacGroupsBean(){
		editMode=false;
		editable=true; 
	}
	
	public EacGroupsBean(EacGroups eacGroup) {
		this.editMode = true;
		this.groupId = eacGroup.getId();
		this.groupName = eacGroup.getGroupName();
		for (Product product : eacGroup.getProducts()) {
			existingProducts.put(product.getId(), product.getProductName());
		}
		this.editable = eacGroup.isEditable();
		this.createdBy = eacGroup.getCreatedBy();
		this.updatedBy = eacGroup.getUpdatedBy();
	}

	
	
	public boolean isEditable() {
		return editable;
	}


	public void setEditable(boolean editable) {
		this.editable = editable;
	}


	public String getGroupId() {
		return groupId;
	}


	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}


	public Map<String, String> getExistingProducts() {
		return existingProducts;
	}

	public void setExistingProducts(Map<String, String> existingProducts) {
		this.existingProducts = existingProducts;
	}

	public Set<String> getProductIdsToAdd() {
		return productIdsToAdd;
	}

	public void setProductIdsToAdd(Set<String> productIdsToAdd) {
		this.productIdsToAdd = productIdsToAdd;
	}

	public Set<String> getProductIdsToRemove() {
		return productIdsToRemove;
	}

	public void setProductIdsToRemove(Set<String> productIdsToRemove) {
		this.productIdsToRemove = productIdsToRemove;
	}

	public AdminUser getCreatedBy() {
		return createdBy;
	}


	public void setCreatedBy(AdminUser createdBy) {
		this.createdBy = createdBy;
	}


	public AdminUser getUpdatedBy() {
		return updatedBy;
	}


	public void setUpdatedBy(AdminUser updatedBy) {
		this.updatedBy = updatedBy;
	}


	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public boolean isEditMode() {
		return editMode;
	}

	public void setEditMode(boolean editMode) {
		this.editMode = editMode;
	}

}
