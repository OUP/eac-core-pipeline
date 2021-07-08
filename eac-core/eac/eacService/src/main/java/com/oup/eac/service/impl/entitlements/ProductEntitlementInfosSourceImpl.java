package com.oup.eac.service.impl.entitlements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.oup.eac.domain.ActivationCode;
import com.oup.eac.domain.ActivationCodeRegistration;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.LinkedProduct;
import com.oup.eac.domain.LinkedProductNew;
import com.oup.eac.domain.LinkedRegistration;
import com.oup.eac.domain.Product;
import com.oup.eac.domain.ProductRegistration;
import com.oup.eac.domain.ProductRegistrationDefinition;
import com.oup.eac.domain.RegisterableProduct;
import com.oup.eac.domain.Registration;
import com.oup.eac.domain.RegistrationDefinition;
import com.oup.eac.domain.entitlement.ProductEntitlementInfoDto;
import com.oup.eac.dto.CustomerRegistrationsDto;
import com.oup.eac.dto.EnforceableProductDto;
import com.oup.eac.dto.ExternalProductIdDto;
import com.oup.eac.dto.LicenceDto;
import com.oup.eac.service.entitlements.ActivationCodeSource;
import com.oup.eac.service.entitlements.CustomerProductSource;
import com.oup.eac.service.entitlements.ProductEntitlementInfoSource;
import com.oup.eac.service.entitlements.ProductEntitlementInfosSource;

/**
 * @author David Hay
 */
@Component("productEntitlementInfosSource")
public class ProductEntitlementInfosSourceImpl implements ProductEntitlementInfosSource {

    private static final Logger LOG = Logger.getLogger(ProductEntitlementInfosSourceImpl.class);

    private ProductEntitlementInfoSource productEntitlementSource;

    private boolean licenceMergeInfo;

    /**
     * Instantiates a new product entitlements source impl.
     * 
     * @param productEntitlementSource
     *            the product entitlement source
     * @param licenceMergeInfo
     *            the licence merge info
     */
    @Autowired
    public ProductEntitlementInfosSourceImpl(
    		final ProductEntitlementInfoSource productEntitlementSource,
    		@Value("${user.entitlements.licence.merge.info}") final boolean licenceMergeInfo) {
        this.productEntitlementSource = productEntitlementSource;
        this.licenceMergeInfo = licenceMergeInfo;
    }

    @Override
    public final List<ProductEntitlementInfoDto> getProductEntitlementInfos(final ExternalProductIdDto externalProductIdDto,final CustomerRegistrationsDto customerRegDto) {
        List<ProductEntitlementInfoDto> result = new ArrayList<ProductEntitlementInfoDto>();
        if (customerRegDto == null) {
            return result;
        }
        List<Registration<? extends ProductRegistrationDefinition>> registrations = customerRegDto.getRegistrations();

        // this method performs logging only
        checkLicenceMergeInfo(customerRegDto);
        /*List<String[]> linkProductList = new ArrayList<String[]>() ;
        for (Registration<? extends ProductRegistrationDefinition> product : customerRegDto.getRegistrations()) {
        	for (LinkedRegistration linkedRegistration : product.getLinkedRegistrations()) {
        		linkProductList.add(new String[]{linkedRegistration.getLinkedProduct().getId(), linkedRegistration.getLinkedProduct().getId()}) ;
        	}
        }*/
        Map<String, CustomerProductSource> productSourceMap = getCustomerProductSources(customerRegDto.getUser(), registrations);
        /*for (String[] linkedReg : linkProductList ) {
        	CustomerProductSource i = productSourceMap.get(linkedReg[0]) ;
        	i.getRegistration().getRegistrationDefinition().getProduct().setId(linkedReg[0]) ;
        	i.getRegistration().getRegistrationDefinition().getProduct().setProductName(linkedReg[1]);
        }*/
//****** CustomerProductSource allProductSource = getAllProductSource(customerRegDto.getUser(), registrations);
        ActivationCodeSource activationCodeSource = getActivationCodeSource(registrations);
        List<LicenceDto> licenceDtos = customerRegDto.getLicences();
        if (licenceDtos != null) {
            for (LicenceDto licenceDto : licenceDtos) {
                String licenceId = licenceDto.getLicenseId();

                // lookup the product source by eRightsLicenceId - this helps us look up the correct Product from the eRightsProductId.
                CustomerProductSource regProductSource = productSourceMap.get(licenceId);// lookup the product Source by eRightsLicenceId
             
                // fall back on the 'all product source' if required
 //******		CustomerProductSource productSource = regProductSource != null ? regProductSource : allProductSource;
                
                if(regProductSource != null){
                	ProductEntitlementInfoDto ent = this.productEntitlementSource.getProductEntitlement(externalProductIdDto, licenceDto, regProductSource, activationCodeSource);

                    // the ProductEntitlementInfo could be null if it was filtered out because the licence was inactive
                    if (ent != null) {
                        result.add(ent);
                    }
                	
               }
                for(LinkedProductNew lp : licenceDto.getProducts().getLinkedProducts()){
                	CustomerProductSource regLinkedProductSource = productSourceMap.get(lp.getProductId());
                	Product linkedProduct = new RegisterableProduct() ;
                	linkedProduct.setProductName(lp.getName());
                	linkedProduct.setId(lp.getProductId());
                	LicenceDto linkLicense = licenseClone(licenceDto, linkedProduct) ;
	                if(regLinkedProductSource != null){
	                	ProductEntitlementInfoDto ent = this.productEntitlementSource.getProductEntitlement(externalProductIdDto, linkLicense, regLinkedProductSource, activationCodeSource);
	                	
	                    // the ProductEntitlementInfo could be null if it was filtered out because the licence was inactive
	                    if (ent != null) {
	                        result.add(ent);
	                    }
	                }	
               } 
                
            }
        }
        return result;
    }
    
    private LicenceDto licenseClone(final LicenceDto licenceDto, Product linkedProduct) {
		
		LicenceDto license = new LicenceDto(licenceDto.getLicenseId() + linkedProduct.getId(), licenceDto.getExpiryDateAndTime(), licenceDto.isExpired(), licenceDto.isActive(), licenceDto.isCompleted(), licenceDto.isAwaitingValidation(), licenceDto.isDenied()) ;
		license.setProductIds(Arrays.asList(linkedProduct.getId()));
		EnforceableProductDto products = new EnforceableProductDto() ;
		products.setProductId(linkedProduct.getId());
		products.setName(linkedProduct.getProductName());
		license.setProducts(products);
		
		license.setActivationCode(licenceDto.getActivationCode());

		license.setCreatedDate(licenceDto.getCreatedDate());

		license.setEnabled(licenceDto.isEnabled());
		license.setEndDate(licenceDto.getEndDate());
		license.setEndDateTime(licenceDto.getEndDateTime());
		license.setLicenceDetail(licenceDto.getLicenceDetail());
		license.setLicenceTemplateId(licenceDto.getLicenceTemplateId());
		license.setStartDate(licenceDto.getStartDate());
		license.setStartDateTime(licenceDto.getStartDateTime());
		license.setUpdatedDate(licenceDto.getUpdatedDate());
		return license;
	}
    /**
     * This product source is used when the registration in EAC does not have its eRightsLicenceId populated.
     * 
     * @param customer
     *            the customer
     * @param registrations
     *            the customer's registrations
     * @return the product source
     */
    private CustomerProductSource getAllProductSource(final Customer customer, final List<Registration<? extends ProductRegistrationDefinition>> registrations) {

        final Map<String, Product> allProductMap = new HashMap<String, Product>();

        if (registrations != null) {
            for (Registration<? extends ProductRegistrationDefinition> registration : registrations) {
                Map<String, Product> regMap = this.getProductMapForRegistration(registration);
                allProductMap.putAll(regMap);
            }
        }
        CustomerProductSource source = new CustomerProductSourceImpl(allProductMap, customer);
        return source;
    }

    /**
     * Check licence merge info. This method can check the overlap of customer licence information betweeen EAC and Atypon/Erights. If this feature is enabled,
     * it will log warnings if the licence information is out of sync, and log debug messages otherwise
     * 
     * @param customerRegDto
     *            the customer reg dto
     */
    private void checkLicenceMergeInfo(CustomerRegistrationsDto customerRegDto) {
        if (licenceMergeInfo) {
            CustomerLicenceMergeInfoImpl mergeInfo = new CustomerLicenceMergeInfoImpl(customerRegDto);
            if (mergeInfo.isFullyMatched() == false) {
                LOG.warn(mergeInfo);
            } else {
                // no need to log that everything is in synch unless debug is switched on.
                if (LOG.isDebugEnabled()) {
                    LOG.debug(mergeInfo);
                }
            }
        }
    }

    private ActivationCodeSource getActivationCodeSource(final List<Registration<? extends ProductRegistrationDefinition>> registrations) {
        final Map<String,String> activationCodeMap = new HashMap<String,String>();
        if(registrations != null){
            for(Registration<? extends ProductRegistrationDefinition> registration : registrations){
                String id = registration.getId();
                if(id != null && (registration instanceof ActivationCodeRegistration)){
                    ActivationCodeRegistration acReg = (ActivationCodeRegistration)registration;
                    ActivationCode ac = acReg.getActivationCode();
                    if(ac != null){
                        activationCodeMap.put(id, ac.getCode());
                    }
                }
            }
        }
        ActivationCodeSource source = new ActivationCodeSource() {
          
			@Override
			public String getActivationCode(String eRightsLicenceId) {
				// TODO Auto-generated method stub
				return null;
			}
        };
        return source;
    }

    private Map<String, CustomerProductSource> getCustomerProductSources(final Customer customer,
            List<Registration<? extends ProductRegistrationDefinition>> registrations) {

        final Map<String, CustomerProductSource> result = new HashMap<String, CustomerProductSource>();
        if (registrations != null) {
            for (Registration<? extends ProductRegistrationDefinition> registration : registrations) {
                Map<String, Product> regMap = this.getProductMapForRegistration(registration);
                CustomerProductSource source = new CustomerProductSourceImpl(regMap, customer, registration);

                addToProductSourceMap(result, registration.getId(), source);
                
                Set<LinkedRegistration> linkedRegs = registration.getLinkedRegistrations();
                if (linkedRegs != null) {
                    for (LinkedRegistration linkedReg : linkedRegs) {
                    	ProductRegistration linkReg = new ProductRegistration();
                    	Product linkedProduct = new RegisterableProduct() ;
                    	linkedProduct.setProductName(linkedReg.getLinkedProduct().getProductName());
                    	linkedProduct.setId(linkedReg.getLinkedProduct().getId());
                    	ProductRegistrationDefinition regDef = new ProductRegistrationDefinition() ;
                    	regDef.setProduct(linkedProduct);
                    	linkReg.setRegistrationDefinition(regDef);
                    	CustomerProductSource linkSource = new CustomerProductSourceImpl(regMap, customer, linkReg);
                        addToProductSourceMap(result, linkedReg.getLinkedProduct().getId(), linkSource);
                        
                    }
                }
            }
        }
        
        return result;
    }

    private void addToProductSourceMap(Map<String, CustomerProductSource> productSourceMap, String eRightsLicenceId, CustomerProductSource productSource) {
        if (eRightsLicenceId != null) {
            productSourceMap.put(eRightsLicenceId, productSource);
        }
    }

    private Map<String, Product> getProductMapForRegistration(final Registration<? extends ProductRegistrationDefinition> registration) {
        Map<String, Product> result = new HashMap<String, Product>();

        Assert.notNull(registration);
        String registrationId = registration.getId();
        RegistrationDefinition rd = registration.getRegistrationDefinition();
        if(rd != null){
            //product de-duplication(chnages done for remove compilation error)
            RegisterableProduct rp = (RegisterableProduct)rd.getProduct();
            if(rp != null){
            	insert(registrationId, result, rp);
                //product de-duplication(chnages done for remove compilation error)
                if (registration.getLinkedRegistrations() != null) {
                    for (LinkedRegistration ls : registration.getLinkedRegistrations()) {
                    	LinkedProduct lp = new LinkedProduct() ;
                    	lp.setId(ls.getLinkedProduct().getId());
                    	lp.setProductName(ls.getLinkedProduct().getProductName());
                    	insert(lp.getId(), result, lp);
                    }
                }
            }
        }
        return result;
    }

	private void insert(String registrationId, Map<String, Product> result, Product product) {
		String eRightsId = product.getId();
		if (result.containsKey(eRightsId)) {
			String msg = String.format("For RegistrationId[%s] eRightsProductId[%s]", registrationId, eRightsId);
			LOG.warn(msg);
		}
	    result.put(eRightsId, product);
	}
    
	@Override
	public final List<ProductEntitlementInfoDto> getProductEntitlements(List<LicenceDto> licenses) {
        List<ProductEntitlementInfoDto> result = new ArrayList<ProductEntitlementInfoDto>();
        
        for (LicenceDto licenceDto : licenses) {
            
        	ProductEntitlementInfoDto ent = this.productEntitlementSource.getProductEntitlementDetails(licenceDto);

            // the ProductEntitlementInfo could be null if it was filtered out because the licence was inactive
            if (ent != null) {
                result.add(ent);
            }
            	
        }
                
           
        return result;
    }
}
