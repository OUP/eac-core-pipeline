package com.oup.eac.ws.v2.service.impl.entitlements;

import java.util.List;

import com.oup.eac.domain.entitlement.ProductDetailsDto;
import com.oup.eac.domain.entitlement.ProductEntitlementDto;
import com.oup.eac.ws.v2.binding.common.FullProductDetails;
import com.oup.eac.ws.v2.binding.common.FullProductEntitlement;
import com.oup.eac.ws.v2.binding.common.GuestProductEntitlement;
import com.oup.eac.ws.v2.binding.common.ProductDetails;
import com.oup.eac.ws.v2.binding.common.ProductEntitlement;
import com.oup.eac.ws.v2.service.entitlements.EntitlementsAdapterHelper;
import com.oup.eac.ws.v2.service.entitlements.LicenceData;
import com.oup.eac.ws.v2.service.entitlements.LicenceDtoConverter;
import com.oup.eac.ws.v2.service.utils.IdUtils;

public class EntitlementsAdapterHelperImpl implements EntitlementsAdapterHelper {

	private final LicenceDtoConverter licenceConverter;

	public EntitlementsAdapterHelperImpl(final LicenceDtoConverter licenceConverter) {
		this.licenceConverter = licenceConverter;
	}

	@Override
	public ProductEntitlement getProductEntitlement(ProductEntitlementDto entitlement) {
		ProductEntitlement result = new ProductEntitlement();
		LicenceData converted = this.licenceConverter.convertLicenceDto(entitlement.getLicence());

		result.setActivationCode(entitlement.getActivationCode());
		result.setInfo(converted.getInfo());
		result.setLicence(converted.getDetail());
		result.setProduct(getProductDetails(entitlement.getProductList()));
		return result;
	}

	private ProductDetails[] getProductDetails(List<ProductDetailsDto> productList) {
		int size = productList == null ? 0 : productList.size();
		ProductDetails[] result = new ProductDetails[size];
		for (int i = 0; i < result.length; i++) {
			result[i] = getProductDetails(productList.get(i));
		}
		return result;

	}

	private ProductDetails getProductDetails(ProductDetailsDto productDetailsDto) {
		ProductDetails result = new ProductDetails();
		
		String	productName = productDetailsDto.getProduct().getProductName();
		result.setProductName(productName);
		
		result.setProductIds(
						IdUtils.getIds(
								productDetailsDto.getProduct().getId(),
								productDetailsDto.getExternalProductIds()
								));
		return result;
	}
	
	
	@Override
    public FullProductEntitlement getFullProductEntitlement(ProductEntitlementDto entitlement) {
        FullProductEntitlement result = new FullProductEntitlement();
        LicenceData converted = this.licenceConverter.convertLicenceDto(entitlement.getLicence());

        result.setActivationCode(entitlement.getActivationCode());
        result.setInfo(converted.getInfo());
        result.setLicence(converted.getDetail());
        result.setProduct(getFullProductDetails(entitlement.getProductList()));
        return result;
    }
	
	
	private FullProductDetails[] getFullProductDetails(List<ProductDetailsDto> productList) {
        int size = productList == null ? 0 : productList.size();
        FullProductDetails[] result = new FullProductDetails[size];
        for (int i = 0; i < result.length; i++) {
            result[i] = getFullProductDetail(productList.get(i));
        }
        return result;

    }
	
	private FullProductDetails getFullProductDetail(ProductDetailsDto productDetailsDto) {
        FullProductDetails result = new FullProductDetails();
        
        String productName = productDetailsDto.getProduct().getProductName();        
        String homePage = productDetailsDto.getProduct().getHomePage();
        String landingPage = productDetailsDto.getProduct().getLandingPage();
        //String productOrgUnit = productDetailsDto.getProduct().getDivision().getDivisionType();
		String productOrgUnit="";
        if(productDetailsDto.getProduct().getDivision()!=null){
        	productOrgUnit= productDetailsDto.getProduct().getDivision().getDivisionType();
        }
        result.setProductName(productName);        
        result.setProductHomePage(homePage);
        result.setProductLandingPage(landingPage);
        result.setProductOrgUnit(productOrgUnit);
        //if(homePage != null){ result.setProductHomePage(homePage);}
        //if(landingPage != null){ result.setProductLandingPage(landingPage);}
        
        result.setProductIds(IdUtils.getIds(productDetailsDto.getProduct().getId(),productDetailsDto.getExternalProductIds()));
        return result;
    }
	
	@Override
    public GuestProductEntitlement getGuestProductEntitlement(ProductEntitlementDto entitlement) {
        GuestProductEntitlement result = new GuestProductEntitlement();
        
        result.setProduct(getProductDetails(entitlement.getProductList()));
        result.setActivationCode(entitlement.getActivationCode());
        
        return result;
    }
	
}
