package com.oup.eac.ws.v2.service;

import java.util.Set;

import com.oup.eac.domain.Product;
import com.oup.eac.dto.EnforceableProductDto;
import com.oup.eac.ws.v2.binding.common.Identifier;
import com.oup.eac.ws.v2.ex.WebServiceValidationException;

public interface WsProductLookup {

    public Product lookupProductByIdentifier(Identifier identifier) throws WebServiceValidationException;
    
    public void validateProductOrgUnit(Set<String> productOrgUnitSet) throws WebServiceValidationException;
    public void validateLicenceState(String licenceState) throws WebServiceValidationException;

    public EnforceableProductDto lookupEnforceableProductByIdentifier(
			Identifier identifier) throws WebServiceValidationException;

	public void checkOupIdPattern(String oupId) throws WebServiceValidationException;
}
