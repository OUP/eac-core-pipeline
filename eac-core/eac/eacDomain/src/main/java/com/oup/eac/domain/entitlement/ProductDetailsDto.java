/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.0.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.oup.eac.domain.entitlement;

import java.util.List;

import com.oup.eac.domain.ExternalProductId;
import com.oup.eac.domain.Product;

/**
 * Class ProductDetailsDto.
 */
public class ProductDetailsDto implements java.io.Serializable {

    private List<ExternalProductId> externalProductIds;
    private Product product;
    
	public List<ExternalProductId> getExternalProductIds() {
		return externalProductIds;
	}
	public void setExternalProductIds(List<ExternalProductId> externalProductIds) {
		this.externalProductIds = externalProductIds;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
    
}
