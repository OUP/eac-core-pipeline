/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.0.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.oup.eac.domain.entitlement;

import java.util.List;

import com.oup.eac.domain.Product;

/**
 * Class ProductEntitlementGroupDto.
 */
public class ProductEntitlementGroupDto implements java.io.Serializable {

    private ProductEntitlementDto entitlement;
    private List<ProductEntitlementDto> linkedEntitlements;
    private boolean isOrphan;
    
    public ProductEntitlementDto getEntitlement() {
        return entitlement;
    }
    public void setEntitlement(ProductEntitlementDto entitlement) {
        this.entitlement = entitlement;
    }
    public List<ProductEntitlementDto> getLinkedEntitlements() {
        return linkedEntitlements;
    }
    public void setLinkedEntitlements(List<ProductEntitlementDto> linkedEntitlements) {
        this.linkedEntitlements = linkedEntitlements;
    } 
	public Product getProduct(int productListIndex) {
		return entitlement.getProductList().get(productListIndex).getProduct();
	}
    public boolean isOrphan() {
        return isOrphan;
    }
    public void setOrphan(boolean isOrphan) {
        this.isOrphan = isOrphan;
    }

	
}
