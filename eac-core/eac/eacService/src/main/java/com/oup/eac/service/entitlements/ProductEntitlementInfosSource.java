package com.oup.eac.service.entitlements;

import java.util.List;

import com.oup.eac.domain.entitlement.ProductEntitlementInfoDto;
import com.oup.eac.dto.CustomerRegistrationsDto;
import com.oup.eac.dto.ExternalProductIdDto;
import com.oup.eac.dto.LicenceDto;

/**
 * This class contains methods to convert between domain objects and ws response objects.
 * @author David Hay
 *
 */
public interface ProductEntitlementInfosSource  {

	List<ProductEntitlementInfoDto> getProductEntitlementInfos(ExternalProductIdDto externalProductIdDto, CustomerRegistrationsDto customerRegDto);

	List<ProductEntitlementInfoDto> getProductEntitlements(
			List<LicenceDto> licenses);
    
}
