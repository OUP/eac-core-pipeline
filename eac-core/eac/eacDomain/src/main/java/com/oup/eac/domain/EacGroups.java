package com.oup.eac.domain;

import java.util.HashSet;
import java.util.Set;

//De-duplication
//@Entity
public class EacGroups extends UpdatedAudit{

	private String groupName;
	
	private AdminUser createdBy;
	
	private AdminUser updatedBy;
	
	private boolean editable;
	
	private Set<Product> products = new HashSet<Product>();

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public Set<Product> getProducts() {
		return products;
	}

	public void setProducts(Set<Product> products) {
		this.products = products;
	}

	public void addProduct(Product product){
		products.add(product);
	}

	public void removeProduct(Product product){
		products.remove(product);
	}


	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
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
	
	@Override
    public boolean equals(Object obj) {
    	EacGroups group=(EacGroups)obj;
    	return this.getId().equals(group.getId());
    }
	
	@Override
    public int hashCode() {
    	if(null==this.getId()){
    		this.setId("");
    	}
    	return this.getId().hashCode();
    }
	
	@Override
	public String toString() {
	    return getClass().getName() + "[" + groupName + "]" + "@" + Integer.toHexString(hashCode());
	}
	
}
