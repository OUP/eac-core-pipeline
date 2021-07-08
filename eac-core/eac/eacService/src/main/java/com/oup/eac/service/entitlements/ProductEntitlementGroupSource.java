package com.oup.eac.service.entitlements;

import java.util.List;

import com.oup.eac.domain.ProductRegistrationDefinition;
import com.oup.eac.domain.Registration;
import com.oup.eac.domain.entitlement.ProductEntitlementGroupDto;
import com.oup.eac.domain.entitlement.ProductEntitlementInfoDto;

/**
 * The Interface ProductEntitlementGroupSource.
 */
public interface ProductEntitlementGroupSource {

	/**
	 * Gets the product entitlement groups.
	 *
	 * @param registrations - the list of registrations containing the linkedRegistration structures.
	 * @param infos - the list of procuct entitlement infos to group 
	 * @return the product entitlement groups
	 */
	List<ProductEntitlementGroupDto> getProductEntitlementGroups(List<Registration<? extends ProductRegistrationDefinition>> registrations, List<ProductEntitlementInfoDto> infos);

}
