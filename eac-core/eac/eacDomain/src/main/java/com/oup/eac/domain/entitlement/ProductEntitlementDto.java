/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.0.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.oup.eac.domain.entitlement;

import java.util.List;

import com.oup.eac.domain.ProductRegistrationDefinition;
import com.oup.eac.domain.Registration;
import com.oup.eac.dto.LicenceDto;

/**
 * Class ProductEntitlementDto.
 */
public class ProductEntitlementDto implements java.io.Serializable {

    private List<ProductDetailsDto> productList;
    private LicenceDto licence;
    private String activationCode;
    private Registration<? extends ProductRegistrationDefinition> registration;
    
    public List<ProductDetailsDto> getProductList() {
        return productList;
    }
    public void setProductList(List<ProductDetailsDto> productList) {
        this.productList = productList;
    }
    public LicenceDto getLicence() {
        return licence;
    }
    public void setLicence(LicenceDto licence) {
        this.licence = licence;
    }
    public String getActivationCode() {
        return activationCode;
    }
    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }
    
	public Registration<? extends ProductRegistrationDefinition> getRegistration() {
		return registration;
	}
	
	public void setRegistration(
			Registration<? extends ProductRegistrationDefinition> registration) {
		this.registration = registration;
	}

    public boolean inEac() {
        return this.registration != null;
    }
}
