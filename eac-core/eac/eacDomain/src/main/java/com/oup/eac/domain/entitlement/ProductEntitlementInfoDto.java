package com.oup.eac.domain.entitlement;

/**
 * A wrapper class holding a ProductEntitlement structure and the associated eRights licence id.
 * The eRights licence id is used to help structure ProductEntitlements into ProductEntitlementGroups.
 * This class cannot move to the domain project because of the dependencies on generated code.
 * @see com.oup.eac.ws.v2.service.impl.UserEntitlementsAdapterImpl 
 * @author David Hay
 */
public class ProductEntitlementInfoDto {
    private String licenceId;
    private ProductEntitlementDto entitlement;

    public ProductEntitlementDto getEntitlement() {
        return entitlement;
    }

    public void setEntitlement(ProductEntitlementDto entitlement) {
        this.entitlement = entitlement;
    }

	public String getLicenceId() {
		return licenceId;
	}

	public void setLicenceId(String licenceId) {
		this.licenceId = licenceId;
	}

}
