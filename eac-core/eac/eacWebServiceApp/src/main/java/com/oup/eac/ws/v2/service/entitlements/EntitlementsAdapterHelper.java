package com.oup.eac.ws.v2.service.entitlements;

import com.oup.eac.domain.entitlement.ProductEntitlementDto;
import com.oup.eac.ws.v2.binding.common.FullProductEntitlement;
import com.oup.eac.ws.v2.binding.common.GuestProductEntitlement;
import com.oup.eac.ws.v2.binding.common.ProductEntitlement;

public interface EntitlementsAdapterHelper {

	ProductEntitlement getProductEntitlement(ProductEntitlementDto entitlement);
	
	FullProductEntitlement getFullProductEntitlement(ProductEntitlementDto entitlement);
	
	GuestProductEntitlement getGuestProductEntitlement(ProductEntitlementDto entitlement);

}
