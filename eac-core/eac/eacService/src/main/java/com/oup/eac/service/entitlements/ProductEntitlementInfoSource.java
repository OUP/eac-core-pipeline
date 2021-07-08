package com.oup.eac.service.entitlements;

import com.oup.eac.domain.entitlement.ProductEntitlementInfoDto;
import com.oup.eac.dto.ExternalProductIdDto;
import com.oup.eac.dto.LicenceDto;

public interface ProductEntitlementInfoSource {
    
    /**
     * Gets the product entitlement.
     *
     * @param externalProductIdDto the external product id dto
     * @param licenceDto the licence dto
     * @param customerProductSource the customer product source
     * @param activationCodeSource the activation code source
     * @return the product entitlement
     */
    public ProductEntitlementInfoDto getProductEntitlement(ExternalProductIdDto externalProductIdDto, LicenceDto licenceDto, CustomerProductSource customerProductSource, ActivationCodeSource activationCodeSource);

	ProductEntitlementInfoDto getProductEntitlementDetails(LicenceDto licenceDto);
}
