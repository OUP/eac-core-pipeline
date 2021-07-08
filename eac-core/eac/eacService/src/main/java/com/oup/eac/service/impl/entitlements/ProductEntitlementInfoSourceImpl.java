package com.oup.eac.service.impl.entitlements;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oup.eac.domain.ExternalProductId;
import com.oup.eac.domain.Product;
import com.oup.eac.domain.RegisterableProduct;
import com.oup.eac.domain.entitlement.ProductDetailsDto;
import com.oup.eac.domain.entitlement.ProductEntitlementDto;
import com.oup.eac.domain.entitlement.ProductEntitlementInfoDto;
import com.oup.eac.dto.EnforceableProductDto;
import com.oup.eac.dto.ExternalProductIdDto;
import com.oup.eac.dto.LicenceDto;
import com.oup.eac.service.ProductService;
import com.oup.eac.service.entitlements.ActivationCodeSource;
import com.oup.eac.service.entitlements.CustomerProductSource;
import com.oup.eac.service.entitlements.ProductEntitlementInfoSource;

/**
 * This class is used to convert from a licenceDto to a ProductEntitlementInfo using supplied product and activation code 'sources'.
 * The licenceDto object comes from atypon web service.
 * The two 'source' objects use data from the eac database.
 * 
 * @author David Hay
 */
@Component("productEntitlementInfoSourceImpl")
public class ProductEntitlementInfoSourceImpl implements ProductEntitlementInfoSource {

    private static final Logger LOG = Logger.getLogger(ProductEntitlementInfoSourceImpl.class);

    public static final boolean FILTER_INVACTIVE_LICENCES = false;
    
    private ProductService productService;

    /**
     * Instantiates a new product entitlements source impl.
     * 
     * @param productConv
     *            the product converter
     * @param licenceDtoConv
     *            the licence dto converter
     */
    @Autowired
    public ProductEntitlementInfoSourceImpl(final ProductService productService) {
        super();
        this.productService = productService;
    }

    @Override
    public ProductEntitlementInfoDto getProductEntitlement(ExternalProductIdDto externalProductIdDto, LicenceDto licenceDto, CustomerProductSource customerProductSource, ActivationCodeSource activationCodeSource) {

        if(FILTER_INVACTIVE_LICENCES && (licenceDto.isActive() == false)){
            // this filters out in-active licences
            return null;
        }
        
        ProductEntitlementDto pe = new ProductEntitlementDto();
        pe.setLicence(licenceDto);
        pe.setRegistration(customerProductSource.getRegistration());
        
        
        String licenceId = licenceDto.getLicenseId();
        
        //PRODUCT DETAILS [ there could be more than 1 product associated with a licence ]
        List<String> eRightsProductIds = licenceDto.getProductIds();
        List<ProductDetailsDto> productList = getProductDetails(externalProductIdDto, customerProductSource, licenceId, eRightsProductIds);
        pe.setProductList(productList);

        //ACTIVATION CODE [there could be an activation code associated with a licence ]
        String activationCode  = licenceDto.getActivationCode();
        pe.setActivationCode(activationCode);

        //WRAP ENTITLENENT IN PRODUCT ENTITLEMENT INFO (this lets us group the entitlements later)
        ProductEntitlementInfoDto result = new ProductEntitlementInfoDto();
        result.setEntitlement(pe);
        result.setLicenceId(licenceId);
        
        return result;
    }

    private List<ProductDetailsDto> getProductDetails(ExternalProductIdDto externalProductIdDto, CustomerProductSource customerProductSource, String eRightsLicenceId, List<String> eRightsProductIds) {
       List<ProductDetailsDto> result = new ArrayList<ProductDetailsDto>();
       if(eRightsProductIds != null){
           for(String eRightsProductId : eRightsProductIds) {
        	   Product product = customerProductSource.getRegistration().getRegistrationDefinition().getProduct();
              // Product product = getProduct(customerProductSource, eRightsProductId, eRightsLicenceId);
               if(product != null && product.getId().equals(eRightsProductId)){
                   List<ExternalProductId> externalProductIds = externalProductIdDto.getExternalProductIds(product);
                   ProductDetailsDto prodDetail = new ProductDetailsDto();
                   prodDetail.setProduct(product);
                   prodDetail.setExternalProductIds(externalProductIds);
                   result.add(prodDetail);
               }
           }
       }
       return result;
    }
    
    /**
     * Gets the eac product corresponding to the eRightsProductId
     *
     * @param customerProductSource the customer product source
     * @param eRightsProductId the eRightsProduct id
     * @param eRightsLicenceId the eRightsLicence id - for log messages only
     * @return the eac Procuct - may be null if it can't be found.
     */
    private Product getProduct(final CustomerProductSource customerProductSource, final String eRightsProductId, final String eRightsLicenceId){
        //the product *should* be associated with the customer's registrations
        Product product = customerProductSource.getProductById(eRightsProductId);
        if(product == null){
            String customerId = customerProductSource.getCustomer().getId();

            //the product may be in eac but just not associated with the customer
            List<Product> eacProducts = productService.getProductAndExternalIdsByErightsId(eRightsProductId);
            
            if(eacProducts == null || eacProducts.isEmpty()){
                //the product is not in eac!
                String msg = String.format("For customerId[%s], Eac doesn't contain erightsProductId[%d] associated with erightsLicenceId[%d]",customerId, eRightsProductId, eRightsLicenceId);
                LOG.error(msg);
            } else {
            	Product eacProduct = eacProducts.get(0);
                //the product IS in eac but not associated with this customer.
                String msg = String.format("For customerId[%s], CustomerProductSource doesn't contain eRrightsProductId[%d] associated with eRightsLicenceId [%d]", customerId, eRightsProductId, eRightsLicenceId);
                LOG.warn(msg);
                product = eacProduct;
                if(eacProducts.size() > 1) {
                	String msg2 = String.format("For customerId[%s], CustomerProductSource doesn't contain eRrightsProductId[%d] associated with eRightsLicenceId [%d] but there are [%d] products with this eRightsId in EAC", customerId, eRightsProductId, eRightsLicenceId, eacProducts.size());
                    LOG.error(msg2);
                }
            }
        }
        return product;
    }
    
    @Override
    public ProductEntitlementInfoDto getProductEntitlementDetails(LicenceDto licenceDto) {

        
        ProductEntitlementDto pe = new ProductEntitlementDto();
        pe.setLicence(licenceDto);
        //pe.setRegistration(customerProductSource.getRegistration());
        
        
        String eRightsLicenceId = licenceDto.getLicenseId();
        
        //PRODUCT DETAILS [ there could be more than 1 product associated with a licence ]
        EnforceableProductDto product = licenceDto.getProducts() ;
        Product eacProduct = new RegisterableProduct() ;
        eacProduct.setProductName(product.getName());
        eacProduct.setId(product.getProductId());
        ProductDetailsDto productDetails = new ProductDetailsDto() ;
        productDetails.setProduct(eacProduct);
        productDetails.setExternalProductIds(licenceDto.getProducts().getExternalIds());
        List<ProductDetailsDto> productList = new ArrayList<ProductDetailsDto>() ;
        productList.add(productDetails) ;
        pe.setProductList(productList);

        //ACTIVATION CODE [there could be an activation code associated with a licence ]
        String activationCode  = licenceDto.getActivationCode();
        pe.setActivationCode(activationCode);

        //WRAP ENTITLENENT IN PRODUCT ENTITLEMENT INFO (this lets us group the entitlements later)
        ProductEntitlementInfoDto result = new ProductEntitlementInfoDto();
        result.setEntitlement(pe);
        
        return result;
    }
}
