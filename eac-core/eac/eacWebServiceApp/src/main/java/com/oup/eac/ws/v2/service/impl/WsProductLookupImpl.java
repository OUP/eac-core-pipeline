package com.oup.eac.ws.v2.service.impl;

import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

import com.oup.eac.domain.Division;
import com.oup.eac.domain.Product;
import com.oup.eac.domain.RegisterableProduct;
import com.oup.eac.dto.EnforceableProductDto;
import com.oup.eac.integration.facade.exceptions.ErightsException;
import com.oup.eac.integration.facade.exceptions.ProductNotFoundException;
import com.oup.eac.service.DivisionService;
import com.oup.eac.service.ProductService;
import com.oup.eac.service.ServiceLayerException;
import com.oup.eac.ws.v2.binding.common.ExternalIdentifier;
import com.oup.eac.ws.v2.binding.common.Identifier;
import com.oup.eac.ws.v2.binding.common.InternalIdentifier;
import com.oup.eac.ws.v2.ex.WebServiceValidationException;
import com.oup.eac.ws.v2.service.WsProductLookup;

public class WsProductLookupImpl extends BaseWsLookupImpl implements WsProductLookup {

    private ProductService productService;
    
    private DivisionService divisionService;

    public WsProductLookupImpl(ProductService productService) {
    	Assert.notNull(productService);
        this.productService = productService;
    }
    
    public WsProductLookupImpl(ProductService productService, DivisionService divisionService) {
        Assert.notNull(productService);
        Assert.notNull(divisionService);
        this.productService = productService;
        this.divisionService=divisionService;
    }


    private String getMessageForInternalProductId(String id) {
        String result = String.format(FMT_PRODUCT_FOR_ID_NOT_FOUND, id);
        return result;
    }

    private String getMessageForErightsException(String id) {
        String result = String.format(ERR_CANNOT_REGISTER_FOR_THIS_PRODUCT, id);
        return result;
    }
    
    private String getMessageForExternalProductId(String systemId, String typeId, String externalId) {
        String result = String.format(FMT_PRODUCT_NOT_FOUND_FOR_EXTERNAL_ID, systemId, typeId, externalId);
        return result;
    }

    @Override
    public Product lookupProductByIdentifier(Identifier identifier) throws WebServiceValidationException {
    	RegisterableProduct result = null;
        InternalIdentifier intID = identifier.getInternalId();
        ExternalIdentifier extID = identifier.getExternalId();
        // the schema validation ensures that only 1 of intID or extID is not null.
        
        if (intID != null) {
           /* try {*/
        		checkOupIdPattern(intID.getId());	
                checkNotBlank("Invalid Product Id", intID.getId());
                Product product = new RegisterableProduct();
                product.setId(intID.getId());
                		//this.productService.getProductById(intID.getId());
                result = (RegisterableProduct) product ;
            /*} catch (ServiceLayerException sle) {
                throw new WebServiceValidationException(getMessageForInternalProductId(intID.getId()), sle);
            }*/
/*            if (result == null) {
            	throw new WebServiceValidationException("Invalid Product Id");
            }*/
        }
        if (extID != null) {
        	String systemId = extID.getSystemId();
            String typeId = extID.getTypeId();
            String externalId = extID.getId();
            checkNotBlank(ERR_MSG_EXTERNAL, systemId, typeId, externalId);
            
            try {
            	EnforceableProductDto enforceableProductDto = this.productService.getProductByExternalProductId(systemId, typeId, externalId);
            	Product product = new RegisterableProduct();
            	 product.setId(enforceableProductDto.getProductId());
            	result = (RegisterableProduct)product;
            			//(RegisterableProduct)this.productService.getProductById(enforceableProductDto.getProductId()) ;
			} catch (ServiceLayerException e) {
				// TODO Auto-generated catch block
				throw new WebServiceValidationException(getMessageForExternalProductId(systemId,typeId,externalId),e) ;
			}
           /* if (result == null) {
                throw new WebServiceValidationException(getMessageForExternalProductId(systemId,typeId,externalId));
            }*/
        }
        
        return result;
    }
    
    @Override
    public EnforceableProductDto lookupEnforceableProductByIdentifier(Identifier identifier) throws WebServiceValidationException {
        InternalIdentifier intID = identifier.getInternalId();
        ExternalIdentifier extID = identifier.getExternalId();
        // the schema validation ensures that only 1 of intID or extID is not null.
        EnforceableProductDto enforceableProductDto = null;
        if (intID != null) {
        	checkOupIdPattern(intID.getId());
        	try {
                checkNotBlank("The product id cannot be blank.",intID.getId());
                enforceableProductDto =this.productService.getEnforceableProductByErightsId(intID.getId());
        	} catch (ProductNotFoundException e) {
    			throw new WebServiceValidationException(getMessageForInternalProductId(intID.getId()), e) ;
			} catch (ErightsException e) {
				e.printStackTrace();
				throw new WebServiceValidationException(getMessageForErightsException(intID.getId()), e);
			}
        }
        if (extID != null) {
        	String systemId = extID.getSystemId();
            String typeId = extID.getTypeId();
            String externalId = extID.getId();
            checkNotBlank(ERR_MSG_EXTERNAL, systemId, typeId, externalId);
            try {
            	enforceableProductDto = this.productService.getProductByExternalProductId(systemId, typeId, externalId);
            } catch (ServiceLayerException e) {
    			// TODO Auto-generated catch block
    			throw new WebServiceValidationException(getMessageForExternalProductId(systemId,typeId,externalId),e) ;
    		}
        }
        return enforceableProductDto;
    }
    
    @Override
    public void checkOupIdPattern(String oupId) throws WebServiceValidationException {
		String pattern = "[{]?[a-fA-F0-9]{8}[-]?[a-fA-F0-9]{4}[-]?[a-fA-F0-9]{4}[-]?[a-fA-F0-9]{4}[-]?[a-fA-F0-9]{12}[}]?";
		if(!oupId.matches(pattern) && oupId.isEmpty()){
			throw new WebServiceValidationException(ERR_PRODUCTID_CANNOT_BE_BLANK);
		}
		else if(!oupId.matches(pattern)){
			String msg = String.format(FMT_PRODUCT_FOR_ID_NOT_FOUND, oupId);
			throw new WebServiceValidationException(msg);
		}
	}
    
    
    
    @Override
    public void validateProductOrgUnit(Set<String> productOrgUnitSet) throws WebServiceValidationException {
    	
    	/*
    	 * Gaurav Soni : Performance improvements CR
    	 * check for empty list
    	 * */
    	List<Division> divisions = null;
    	if(CollectionUtils.isNotEmpty(productOrgUnitSet)){
    		try {
    			divisions = this.divisionService.getAllDivisions();
    		} catch(ErightsException ex) {
    			ex.printStackTrace();
    		}
            Boolean flag=false;
            
            for(String pOrgUnit : productOrgUnitSet){
                for(int i=0; i<divisions.size() ; i++){
                    if (StringUtils.isBlank(pOrgUnit) == false) {
                        if(divisions.get(i).getDivisionType().equalsIgnoreCase(pOrgUnit)){
                            flag=true;
                            break;
                        }
                    }else {
                        throw new WebServiceValidationException("Product OrgUnit cannot be blank");
                    }              
                }
                if(flag==false){
                    throw new WebServiceValidationException("The OrgUnit does not exist : " + pOrgUnit);                    
                }
                flag=false;
            }
    	}
    }
    
    @Override
    public void validateLicenceState(String licenceState) throws WebServiceValidationException {      
        
        if(StringUtils.isBlank(licenceState) == false){
            if(licenceState.equalsIgnoreCase("active")==false && licenceState.equalsIgnoreCase("expired")==false){
                throw new WebServiceValidationException("The licenceState is invalid : "+licenceState + ". licenceState must be either 'active' or 'expired'.");
            }            
        }else if(licenceState != null){
            throw new WebServiceValidationException("licenceState cannot be blank. licenceState must be either 'active' or 'expired'.");
        }
    }    
    
}
