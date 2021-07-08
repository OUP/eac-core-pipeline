package com.oup.eac.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.oup.eac.domain.ActivationCode;
import com.oup.eac.domain.Division;
import com.oup.eac.domain.EacGroups;
import com.oup.eac.domain.ExternalProductId;
import com.oup.eac.domain.LinkedProductNew;
import com.oup.eac.domain.Product;
import com.oup.eac.domain.ProductRegistrationDefinition;
import com.oup.eac.domain.RegisterableProduct;
import com.oup.eac.domain.Registration;
import com.oup.eac.domain.entitlement.ProductDetailsDto;
import com.oup.eac.domain.entitlement.ProductEntitlementDto;
import com.oup.eac.domain.entitlement.ProductEntitlementGroupDto;
import com.oup.eac.domain.entitlement.ProductEntitlementInfoDto;
import com.oup.eac.dto.CustomerRegistrationsDto;
import com.oup.eac.dto.EnforceableProductDto;
import com.oup.eac.dto.ExternalProductIdDto;
import com.oup.eac.dto.LicenceDto;
import com.oup.eac.service.ProductService;
import com.oup.eac.service.UserEntitlementsService;
import com.oup.eac.service.entitlements.ExternalProductIdDtoSource;
import com.oup.eac.service.entitlements.ProductEntitlementGroupSource;
import com.oup.eac.service.entitlements.ProductEntitlementInfosSource;

@Component("userEntitlementsService")
public class UserEntitlementsServiceImpl implements UserEntitlementsService {

	private static final Logger LOG = Logger.getLogger(UserEntitlementsServiceImpl.class);
	private ExternalProductIdDtoSource externalProductIdDtoSource = null;
	private ProductEntitlementInfosSource productEntitlementInfosSource = null;
	private ProductEntitlementGroupSource entitlementGroupSource = null;
	private ProductService productService;

	@Autowired
	public UserEntitlementsServiceImpl(
			final ExternalProductIdDtoSource externalProductIdDtoSource,
			final ProductEntitlementInfosSource productEntitlementInfosSource,
			final ProductEntitlementGroupSource entitlementGroupSource,
			final ProductService productService) {
		Assert.notNull(externalProductIdDtoSource);
		Assert.notNull(productEntitlementInfosSource);
		Assert.notNull(entitlementGroupSource);
		Assert.notNull(productService);
		this.externalProductIdDtoSource = externalProductIdDtoSource;
		this.productEntitlementInfosSource = productEntitlementInfosSource;
		this.entitlementGroupSource = entitlementGroupSource;
		this.productService = productService;
	}

	@Override
	public List<ProductEntitlementGroupDto> getUserEntitlementGroups(CustomerRegistrationsDto registrationsDto, String systemId) {
		List<ProductEntitlementInfoDto> infos = getProductEntitlementInfos(registrationsDto, systemId);
		List<ProductEntitlementGroupDto> groups = this.entitlementGroupSource.getProductEntitlementGroups(registrationsDto.getRegistrations(),infos);
		return groups;
	}

	@Override
	public List<ProductEntitlementInfoDto> getProductEntitlementInfos(CustomerRegistrationsDto registrationsDto, String systemId) {
		List<Registration<? extends ProductRegistrationDefinition>> registrations = registrationsDto.getRegistrations();
		//Change once product externalId is completed
		ExternalProductIdDto externalProductIdDto = this.externalProductIdDtoSource.getExternalProductId(registrations, systemId);
		List<ProductEntitlementInfoDto> infos = this.productEntitlementInfosSource.getProductEntitlementInfos(externalProductIdDto,registrationsDto);

		return infos;
	}
	
	
	@Override
    public List<ProductEntitlementGroupDto> getFullUserEntitlementGroups(CustomerRegistrationsDto custRegistrationsDto) {

//        List<ProductEntitlementInfoDto> infos = getFullProductEntitlementInfos(custRegistrationsDto); // here 
//       List<ProductEntitlementGroupDto> groups = this.entitlementGroupSource.getProductEntitlementGroups(custRegistrationsDto.getRegistrations(), infos);
        
        List<ProductEntitlementGroupDto> groups = this.getProductEntitlementGroups(custRegistrationsDto);
        return groups;
    
    }
	
	
	public List<ProductEntitlementGroupDto> getProductEntitlementGroups(final CustomerRegistrationsDto custRegistrationsDto)
	{
		List<ProductEntitlementGroupDto> entitlementGroupDtosLst=new ArrayList<ProductEntitlementGroupDto>();
		
		ProductEntitlementDto entitlement;
	    List<ProductDetailsDto> productDetailsDtosLst;
		for(LicenceDto licenceDto:custRegistrationsDto.getLicences())
		{
			
			ProductEntitlementGroupDto entitlementGroupDto=new ProductEntitlementGroupDto();
			entitlement=new ProductEntitlementDto();
			productDetailsDtosLst = new ArrayList<ProductDetailsDto>();
			ProductDetailsDto productDetailsDto=new ProductDetailsDto();
			
			//start to set product entitlement
			//convert enforceble product to product
			Product enfoProduct=convertEnforceableProductToProduct(licenceDto.getProducts());
			productDetailsDto.setProduct(enfoProduct);

			List<ExternalProductId> extProdIdLst = new ArrayList<ExternalProductId>(licenceDto.getProducts().getExternalIds());
			if (licenceDto.getProducts().getExternalIds().size() > 0 && extProdIdLst.get(0).getId() != null) {
				productDetailsDto.setExternalProductIds(licenceDto.getProducts().getExternalIds());
			}
			productDetailsDtosLst.add(productDetailsDto);
			entitlement.setProductList(productDetailsDtosLst);
			entitlement.setLicence(licenceDto);
			entitlement.setActivationCode(licenceDto.getActivationCode());
			
			entitlementGroupDto.setEntitlement(entitlement);
			//end of product entitlement
			
			
			//start of link product entitlement for entitilemt group
			ProductEntitlementDto linkentitlementDto=null;
			List<ProductEntitlementDto> entitlementDtosLst=new ArrayList<ProductEntitlementDto>();
			List<ProductDetailsDto> linkProductDetailsDtosLst;
			List<Product> linkedProductLst=enfoProduct.getLinkedProducts();
			for(Product linkProduct: linkedProductLst)
			{
				linkentitlementDto=new ProductEntitlementDto();
				linkProductDetailsDtosLst = new ArrayList<ProductDetailsDto>();
				ProductDetailsDto linkProductdetailsDto=new ProductDetailsDto();
				//setting link product details
				linkProductdetailsDto.setProduct(linkProduct);
				List<ExternalProductId> linkedExtProdIdLst = new ArrayList<ExternalProductId>(linkProduct.getExternalIds());
				
				if (linkProduct.getExternalIds().size() > 0 && linkedExtProdIdLst.get(0).getId() != null) {
					linkProductdetailsDto.setExternalProductIds(new ArrayList<ExternalProductId>(linkProduct.getExternalIds()));
				}
				linkProductDetailsDtosLst.add(linkProductdetailsDto);
				linkentitlementDto.setProductList(linkProductDetailsDtosLst);
				
				
				//setting license details for linked product
				linkentitlementDto.setLicence(licenceDto);
				linkentitlementDto.setActivationCode(licenceDto.getActivationCode());
				entitlementDtosLst.add(linkentitlementDto);
				
			}
			
			entitlementGroupDto.setLinkedEntitlements(entitlementDtosLst);
			//end of link product entitlement for entitilemt group
			entitlementGroupDtosLst.add(entitlementGroupDto);
		}
		
		return entitlementGroupDtosLst;
	
	}

	private List<ProductEntitlementInfoDto> getFullProductEntitlementInfos(CustomerRegistrationsDto custRegistrationsDto) {
        List<Registration<? extends ProductRegistrationDefinition>> registrations = custRegistrationsDto.getRegistrations();

        ExternalProductIdDto externalProductIdDto = this.externalProductIdDtoSource.getFullExternalProductId(registrations); // here
//        ExternalProductIdDto externalProductIdDto1 = getFullExternalProductId(custRegistrationsDto);
        List<ProductEntitlementInfoDto> infos = this.productEntitlementInfosSource.getProductEntitlementInfos(externalProductIdDto, custRegistrationsDto);

        return infos;
    }
	
	private Product convertEnforceableProductToProduct(EnforceableProductDto enfoProduct) {
		Product regProduct = new RegisterableProduct();
		Division division=null;
		//RegisterableProduct regProduct = new RegisterableProduct();
		regProduct.setProductName(enfoProduct.getName());
		regProduct.getExternalIds().addAll(enfoProduct.getExternalIds());
		//regProduct.setErightsId(enfoProduct.getErightsId());
		regProduct.setEmail(enfoProduct.getAdminEmail());
		
		if(enfoProduct.getLandingPage()!=null)
		regProduct.setLandingPage(enfoProduct.getLandingPage());
		
		if(enfoProduct.getHomePage()!=null)
		regProduct.setHomePage(enfoProduct.getHomePage());
		
		if(enfoProduct.getDivision()!=null)
		{
			division=new Division();
			division.setDivisionType(enfoProduct.getDivision().getDivisionType());
			regProduct.setDivision(division);
		}	
		
	
		List<Product> linkProducts=new ArrayList<Product>();
		for(LinkedProductNew linked : enfoProduct.getLinkedProducts())
		{
			Product linkProduct=new RegisterableProduct();
			linkProduct.setProductName(linked.getName());
			linkProduct.setId(linked.getProductId());
			linkProduct.getExternalIds().addAll(linked.getExternalIds());
			linkProduct.setExternalIds(new HashSet<ExternalProductId>(linked.getExternalIds()));
			
			if(linked.getLandingPage()!=null)
			linkProduct.setLandingPage(linked.getLandingPage());
			
			if(linked.getHomePage()!=null)
			linkProduct.setHomePage(linked.getHomePage());
			
			if(linked.getDivision()!=null)
			{
				division=new Division();
				division.setDivisionType(linked.getDivision().getDivisionType());
				linkProduct.setDivision(division);
			}	
			linkProducts.add(linkProduct);
		}
		
		regProduct.setLinkedProducts(linkProducts);
		
			
		regProduct.setId(enfoProduct.getProductId());
		return regProduct;
	}

	@Override
	public List<ProductEntitlementGroupDto> getProblemProductEntitlementGroups(List<LicenceDto> problemLicenceDtos) {
		List<ProductEntitlementGroupDto> result = new ArrayList<ProductEntitlementGroupDto>();
		if(!problemLicenceDtos.isEmpty() || problemLicenceDtos != null){
			for (LicenceDto licenceDto : problemLicenceDtos) {
				 // this filters out in-active licences
				//if(licenceDto.isActive() == true){
					ProductEntitlementDto pe = new ProductEntitlementDto();
			        pe.setLicence(licenceDto);
			        String licenceId = licenceDto.getLicenseId();
			      //PRODUCT DETAILS [ there could be more than 1 product associated with a licence ]
			        List<String> productIds = licenceDto.getProductIds();
			        List<ProductDetailsDto> productList = getProductDetails(productIds, licenceId);
			        pe.setProductList(productList);
			        ProductEntitlementGroupDto pegDto = new ProductEntitlementGroupDto();
			        pegDto.setEntitlement(pe);
			        result.add(pegDto);
		       // }
			}
		}
		return result;
	}
	
	 private List<ProductDetailsDto> getProductDetails(List<String> productIds,  String licenceId) {
	       List<ProductDetailsDto> result = new ArrayList<ProductDetailsDto>();
	       if(productIds != null){
	           for(String productId : productIds) {
	        	   List<Product> eacProducts = productService.getProductByErightsId(productId);
	               Product product = eacProducts.get(0);
	               if(product != null){
	                   ProductDetailsDto prodDetail = new ProductDetailsDto();
	                   prodDetail.setProduct(product);
	                   result.add(prodDetail);
	               }else{
	            	   String msg = String.format("Eac doesn't contain erightsProductId[%d] associated with erightsLicenceId[%d]",productId, licenceId);
	                   LOG.error(msg);
	               }
	           }
	       }
	       return result;
	    }
	 
	 @Override
	 public List<ProductEntitlementInfoDto> getGuestProductEntitlementInfos(ActivationCode activationCode) {
	     
	     List<ProductEntitlementInfoDto> result = new ArrayList<ProductEntitlementInfoDto>();
	     
	     ProductEntitlementInfoDto ent = new ProductEntitlementInfoDto();
	     
	     ProductEntitlementDto pe = new ProductEntitlementDto();
	     
	     List<ProductDetailsDto> productList = null;
	     
	     EacGroups eacGroup = activationCode.getActivationCodeBatch().getActivationCodeRegistrationDefinition().getEacGroup();
         Product product = activationCode.getActivationCodeBatch().getActivationCodeRegistrationDefinition().getProduct();
         
         if(eacGroup != null && product == null){         
             // for Product Group
             Set<Product> products =  eacGroup.getProducts();
             for(Product p : products){
                 pe = new ProductEntitlementDto();
                 ent = new ProductEntitlementInfoDto();
                 // for Product
                 productList = getProductDetails(p);
                 
                 // set product list
                 pe.setProductList(productList);
                
                 // ACTIVATION CODE             
                 pe.setActivationCode(activationCode.getCode());
                     
                 ent.setEntitlement(pe);
                 
                 result.add(ent);
             }
             //productList = getProductDetails(eacGroup.getProducts());
             
         }else if(eacGroup == null && product != null){
             // for Product
             productList = getProductDetails(product);
             
             // set product list
             pe.setProductList(productList);
             
             // ACTIVATION CODE             
             pe.setActivationCode(activationCode.getCode());
                 
             ent.setEntitlement(pe);
             
             result.add(ent);
             
         }else{
             LOG.error("The activation code details are not valid.");
         }
         
         return result;
         
	 }
	 
	 @Override
		public List<ProductEntitlementInfoDto> getProductEntitlementInfo(List<LicenceDto> licenceDtos) {
			List<ProductEntitlementInfoDto> infos = this.productEntitlementInfosSource.getProductEntitlements(licenceDtos) ;
	
			return infos;
		}
	 private List<ProductDetailsDto> getProductDetails(Product product) {
         List<ProductDetailsDto> result = new ArrayList<ProductDetailsDto>();
         List<Product> products = new ArrayList<Product>();
         products.add(product);
         ExternalProductIdDto externalProductIdDto = externalProductIdDtoSource.getExternalProductId(products);
         if(product != null){
             List<ExternalProductId> externalProductIds = externalProductIdDto.getExternalProductIds(product);
             ProductDetailsDto prodDetail = new ProductDetailsDto();
             prodDetail.setProduct(product);
             prodDetail.setExternalProductIds(externalProductIds);
             result.add(prodDetail);
         }
                  
         return result;
      }
}
