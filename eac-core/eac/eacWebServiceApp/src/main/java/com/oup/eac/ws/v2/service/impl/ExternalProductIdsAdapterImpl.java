package com.oup.eac.ws.v2.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.Assert;

import com.oup.eac.domain.ExternalProductId;
import com.oup.eac.domain.ExternalSystem;
import com.oup.eac.domain.ExternalSystemIdType;
import com.oup.eac.domain.Product;
import com.oup.eac.domain.Product.ProductState;
import com.oup.eac.domain.Product.ProductType;
import com.oup.eac.domain.RegisterableProduct;
import com.oup.eac.domain.RegistrationDefinition;
import com.oup.eac.dto.ExternalIdDto;
import com.oup.eac.integration.facade.exceptions.ErightsException;
import com.oup.eac.service.ExternalIdService;
import com.oup.eac.service.ProductService;
import com.oup.eac.service.RegistrationDefinitionService;
import com.oup.eac.service.ServiceLayerException;
import com.oup.eac.service.exceptions.ServiceLayerValidationException;
import com.oup.eac.ws.v2.binding.access.SetExternalProductIdsRequest;
import com.oup.eac.ws.v2.binding.access.SetExternalProductIdsResponse;
import com.oup.eac.ws.v2.binding.common.ErrorStatus;
import com.oup.eac.ws.v2.binding.common.ExternalIdentifier;
import com.oup.eac.ws.v2.binding.common.Identifier;
import com.oup.eac.ws.v2.ex.WebServiceException;
import com.oup.eac.ws.v2.ex.WebServiceValidationException;
import com.oup.eac.ws.v2.service.ExternalProductIdsAdapter;
import com.oup.eac.ws.v2.service.WsProductLookup;
import com.oup.eac.ws.v2.service.utils.ErrorStatusUtils;
import com.oup.eac.ws.v2.service.utils.IdUtils;

public class ExternalProductIdsAdapterImpl implements ExternalProductIdsAdapter {

   // private final ProductService productService;
    private final RegistrationDefinitionService registrationDefinitionService;
    private final ExternalIdService externalIdService;  
    //Added to convert Product in RegisterableProduct to support externalId and Error messages
    private final WsProductLookup productLookup;
    
    public static final String ERR_NOT_REGISTERABLE_PRODUCT = "The product is not a registerable product";
    public static final String ERR_CANNOT_REGISTER_FOR_THIS_PRODUCT = "You cannot register for this product via the EAC Web Service";

    public ExternalProductIdsAdapterImpl(ExternalIdService externalIdService, WsProductLookup productLookup
    		,RegistrationDefinitionService registrationDefinitionService){
        super();
       // Assert.notNull(productService);
        Assert.notNull(externalIdService);
        Assert.notNull(productLookup);
        Assert.notNull(registrationDefinitionService);
        this.registrationDefinitionService = registrationDefinitionService;
        //this.productService = productService;
        this.externalIdService = externalIdService;
        this.productLookup= productLookup;
    }

    @Override
    @PreAuthorize("hasRole('ROLE_WS_SET_EXTERNAL_PRODUCT_IDS')")
    public SetExternalProductIdsResponse setExternalProductIds(SetExternalProductIdsRequest request) throws WebServiceException {
        SetExternalProductIdsResponse response = new SetExternalProductIdsResponse();
        try {
        	String systemId = request.getSystemId();
            Identifier id = request.getProductId();
            //Product product;
            RegisterableProduct regProduct= new RegisterableProduct();
            
            if(id.getInternalId() != null){
            	String productId = id.getInternalId().getId();
            	regProduct.setId(productId);
            	checkOupIdPattern(productId);	
                //product = this.productService.getProductById(productId);
            	RegistrationDefinition regDef = registrationDefinitionService.getProductRegistrationDefinitionByProduct(regProduct);
                regProduct = getRegisterableProduct(id);
                if(regDef == null){
                    throw new WebServiceValidationException("Invalid Product Id");
                }
            }else{
                throw new WebServiceValidationException("Using External Product Id to identify product is not yet supported");
            }
            ExternalIdentifier[] externals = request.getExternal();
            IdUtils.validateSetExternalIds(systemId, externals);
            List<ExternalIdDto> dtos = IdUtils.getExternalIdDtos(externals);
           
           
            //added for atypon call
            Set<ExternalProductId> extPId =getExternalIds(request.getExternal());
            regProduct.setExternalIds(extPId);
            externalIdService.saveExternalProductIdsForSystem(regProduct, systemId, dtos,regProduct);
        } catch (WebServiceValidationException wsve) {
            ErrorStatus errStatus = ErrorStatusUtils.getClientErrorStatus(wsve.getMessage());
            response.setErrorStatus(errStatus);
        } catch (ServiceLayerValidationException slve) {
            ErrorStatus errStatus = ErrorStatusUtils.getClientErrorStatus(slve.getMessage());
            response.setErrorStatus(errStatus);
        } catch (ServiceLayerException sle) {
        	if (sle.getMessage().contains("Product is not found with ID")) {
        		ErrorStatus errStatus = ErrorStatusUtils.getClientErrorStatus("Invalid Product Id");
                response.setErrorStatus(errStatus);
        	} else {
        		throw new WebServiceException("problem setting external product ids", sle);
        	}
            
        }//Added to send response in status
        catch (ErightsException e) {
        	 ErrorStatus errStatus = ErrorStatusUtils.getClientErrorStatus(e.getMessage());
             response.setErrorStatus(errStatus);
		}
        return response;
    }
    private void checkOupIdPattern(String oupId) throws WebServiceValidationException {
		String pattern = "[{]?[a-fA-F0-9]{8}[-]?[a-fA-F0-9]{4}[-]?[a-fA-F0-9]{4}[-]?[a-fA-F0-9]{4}[-]?[a-fA-F0-9]{12}[}]?";
		if(!oupId.matches(pattern) && oupId.isEmpty() || oupId==""){
			throw new WebServiceValidationException("Invalid Product Id");
		}
		else if(!oupId.matches(pattern)){
			throw new WebServiceValidationException("Invalid Product Id");
		}
	}
    //Added to convert Erights object
    static Set<ExternalProductId>  getExternalIds(ExternalIdentifier[] externalId){
    	Set<ExternalProductId> externalProductId = new HashSet<ExternalProductId>();
    	for(ExternalIdentifier extId : externalId){
    		ExternalProductId result = getExternalProductId(extId);
    		externalProductId.add(result); 
    	}
		return externalProductId;
	}
    
    private static ExternalProductId getExternalProductId(ExternalIdentifier extId) {
    	ExternalProductId externalProductId = new ExternalProductId();
	    
	    ExternalSystemIdType externalSystemTypeId = new ExternalSystemIdType();
	    ExternalSystem extSys = new ExternalSystem();
	    extSys.setName(extId.getSystemId());
	    externalSystemTypeId.setExternalSystem(extSys);
	    externalSystemTypeId.setName(extId.getTypeId());
	    externalProductId.setExternalSystemIdType(externalSystemTypeId);
	    externalProductId.setExternalId(extId.getId());
	    return externalProductId;
	}
    //Added to get RegisterableProduct for externalId function
    private RegisterableProduct getRegisterableProduct(Identifier productId) throws WebServiceValidationException{
        Product product = productLookup.lookupProductByIdentifier(productId);
        
        if (product.getProductType() != ProductType.REGISTERABLE) {
            throw new WebServiceValidationException(ERR_NOT_REGISTERABLE_PRODUCT);
        }
        if (product.getState() == ProductState.REMOVED) {
            String msg = String.format("Attempt was made to register for removed product (id=%s) (name=%s) with state : %s", product.getId(), product.getProductName(), product.getState().name()); 
            throw new WebServiceValidationException(ERR_CANNOT_REGISTER_FOR_THIS_PRODUCT);
        }
        RegisterableProduct result = (RegisterableProduct)product;
        return result;
    }
    

}
