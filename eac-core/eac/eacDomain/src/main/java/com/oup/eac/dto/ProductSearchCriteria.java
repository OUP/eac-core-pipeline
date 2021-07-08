package com.oup.eac.dto;

import java.io.Serializable;


public class ProductSearchCriteria implements Serializable {

	private static final int DEFAULT_PAGE_SIZE = 2;
	
    private Integer erightsId;

    private String productName;

   // private String divisionId;

    private int pageSize = DEFAULT_PAGE_SIZE;

    private int page;

	/**
	 * @return the erightsId
	 */
	public final Integer getErightsId() {
		return erightsId;
	}

	/**
	 * @param erightsId the erightsId to set
	 */
	public final void setErightsId(final Integer erightsId) {
		this.erightsId = erightsId;
	}

	/**
	 * @return the productName
	 */
	public final String getProductName() {
		return productName;
	}

	/**
	 * @param productName the productName to set
	 */
	public final void setProductName(final String productName) {
		this.productName = productName;
	}

	
	//division removal
	/**
	 * @return the divisionId
	 *//*
	public final String getDivisionId() {
		return divisionId;
	}

	*//**
	 * @param divisionId the divisionId to set
	 *//*
	public final void setDivisionId(String divisionId) {
		this.divisionId = divisionId;
	}
*/
	/**
	 * @return the pageSize
	 */
	public final int getPageSize() {
		return pageSize;
	}

	/**
	 * @param pageSize the pageSize to set
	 */
	public final void setPageSize(final int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * @return the page
	 */
	public final int getPage() {
		return page;
	}

	/**
	 * @param page the page to set
	 */
	public final void setPage(final int page) {
		this.page = page;
	}
    
}
