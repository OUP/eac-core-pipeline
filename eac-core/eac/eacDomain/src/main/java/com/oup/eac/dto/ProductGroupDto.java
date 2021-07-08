package com.oup.eac.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.Assert;

import com.oup.eac.domain.EacGroups;
import com.oup.eac.domain.Product;

public class ProductGroupDto {
	
	private String productGroupId;
	
	private String productGroupName;
	
	private String currentGroupName;
	
	private List<String> productIds = new ArrayList<String>();

	/*public ProductGroupDto(final EacGroups eacGroup) {
		Assert.notNull(eacGroup);
		this.productGroupName = eacGroup.getGroupName();
		for(Product product: eacGroup.getProducts()){
			this.productIds.add(product.getE)
		}
		//this.groupIds = (eacGroup.getProducts(). != null) ? groupIds : this.groupIds ;
		
	}*/
	
	public ProductGroupDto(String productGroupId, String productGroupName,
			String currentGroupName, List<String> productIds) {
		super();
		this.productGroupId = productGroupId;
		this.productGroupName = productGroupName;
		this.currentGroupName = currentGroupName;
		this.productIds = productIds;
	}

	public ProductGroupDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getProductGroupId() {
		return productGroupId;
	}

	public void setProductGroupId(String productGroupId) {
		this.productGroupId = productGroupId;
	}

	public String getProductGroupName() {
		return productGroupName;
	}

	public void setProductGroupName(String productGroupName) {
		this.productGroupName = productGroupName;
	}

	public List<String> getProductIds() {
		return productIds;
	}

	public void setProductIds(List<String> productIds) {
		this.productIds = productIds;
	}

	/**
	 * @return the currentGroupName
	 */
	public String getCurrentGroupName() {
		return currentGroupName;
	}

	/**
	 * @param currentGroupName the currentGroupName to set
	 */
	public void setCurrentGroupName(String currentGroupName) {
		this.currentGroupName = currentGroupName;
	}
	
	

}
