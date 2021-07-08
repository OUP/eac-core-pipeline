package com.oup.eac.service;

import java.util.List;

import com.oup.eac.domain.ActivationCode;
import com.oup.eac.domain.entitlement.ProductEntitlementGroupDto;
import com.oup.eac.domain.entitlement.ProductEntitlementInfoDto;
import com.oup.eac.dto.CustomerRegistrationsDto;
import com.oup.eac.dto.LicenceDto;

public interface UserEntitlementsService {
	
	public List<ProductEntitlementGroupDto> getUserEntitlementGroups(CustomerRegistrationsDto registrationsDto, String systemId);

    public List<ProductEntitlementInfoDto> getProductEntitlementInfos(CustomerRegistrationsDto registrationsDto, String systemId);
    
    public List<ProductEntitlementGroupDto> getFullUserEntitlementGroups(CustomerRegistrationsDto custRegistrationsDto);
    
    public List<ProductEntitlementGroupDto> getProblemProductEntitlementGroups(List<LicenceDto> problemLicenceDtos);
    
    public List<ProductEntitlementInfoDto> getGuestProductEntitlementInfos(ActivationCode activationCode);

	public List<ProductEntitlementInfoDto> getProductEntitlementInfo(
			List<LicenceDto> licenceDtos);

}
