package com.oup.eac.service.impl.entitlements;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oup.eac.domain.LinkedProduct;
import com.oup.eac.domain.LinkedRegistration;
import com.oup.eac.domain.Product;
import com.oup.eac.domain.ProductRegistrationDefinition;
import com.oup.eac.domain.RegisterableProduct;
import com.oup.eac.domain.Registration;
import com.oup.eac.dto.ExternalProductIdDto;
import com.oup.eac.service.ExternalIdService;
import com.oup.eac.service.entitlements.ExternalProductIdDtoSource;

@Component("externalProductDtoSource")
public class ExternalProductDtoSourceImpl implements ExternalProductIdDtoSource {

    private final ExternalIdService externalIdService;

    @Autowired
    public ExternalProductDtoSourceImpl(ExternalIdService externalIdService) {
        this.externalIdService = externalIdService;
    }

    @Override
    public ExternalProductIdDto getExternalProductId(List<Registration<? extends ProductRegistrationDefinition>> registrations, String systemId) {
        List<Product> products = getProductIdsInRegistrations(registrations);
        ExternalProductIdDto result = getExternalProductId(systemId, products);
        return result;
    }
    
    @Override
    public ExternalProductIdDto getExternalProductId(String systemId, List<Product> products) {
        ExternalProductIdDto result = externalIdService.getExternalProductIds(products, systemId);
        return result;
    }

    @Override
    public ExternalProductIdDto getExternalProductId(List<Product> products) {
        ExternalProductIdDto result = externalIdService.getExternalProductIds(products);
        return result;
    }
    
    private List<Product> getProductIdsInRegistrations(List<Registration<? extends ProductRegistrationDefinition>> registrations){
        Set<Product> products = new HashSet<Product>();
        if(registrations != null){
            for(Registration<? extends ProductRegistrationDefinition> registration : registrations){
                //product de-duplication(chnages done for remove compilation error)
            	RegisterableProduct rp = (RegisterableProduct)registration.getRegistrationDefinition().getProduct(); 
                products.add(rp);
                //product de-duplication(chnages done for remove compilation error)
               /* Set<LinkedProduct> linked = null ;//rp.getLinkedProducts();
                if(linked != null && linked.isEmpty() == false){
                    products.addAll(linked);
                }*/
                if (registration.getLinkedRegistrations() != null && registration.getLinkedRegistrations().size() > 0) {
                	Set<LinkedRegistration> linkedReg = registration.getLinkedRegistrations() ;
                	for (LinkedRegistration linkReg: linkedReg) {
                		RegisterableProduct lp = new RegisterableProduct() ;
                		lp.setProductName(linkReg.getLinkedProduct().getProductName());
                		lp.setId(linkReg.getLinkedProduct().getId());
                		products.add(lp);
                	}
                }
            }
        }
        List<Product> result = new ArrayList<Product>(products);
        return result;
    }
    
    @Override
    public ExternalProductIdDto getFullExternalProductId(List<Registration<? extends ProductRegistrationDefinition>> registrations) {
        List<Product> products = getProductIdsInRegistrations(registrations);
        ExternalProductIdDto result = getAllExternalProductId(products);
        return result;
    }
    
    public List<Product> getFullExternalProducts(List<Registration<? extends ProductRegistrationDefinition>> registrations) {
        List<Product> products = getProductIdsInRegistrations(registrations); // get products from registration
        return products;
    }
    
    public ExternalProductIdDto getAllExternalProductId(List<Product> products) {
        ExternalProductIdDto result = externalIdService.getFullExternalProductIdDto(products);
        return result;
    }

}
