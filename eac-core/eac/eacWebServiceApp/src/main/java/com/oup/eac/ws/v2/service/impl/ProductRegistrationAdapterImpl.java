package com.oup.eac.ws.v2.service.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.Assert;

import com.oup.eac.domain.Customer;
import com.oup.eac.domain.Product;
import com.oup.eac.domain.Product.ProductState;
import com.oup.eac.domain.Product.ProductType;
import com.oup.eac.domain.RegisterableProduct;
import com.oup.eac.dto.EnforceableProductDto;
import com.oup.eac.integration.facade.exceptions.ErightsException;
import com.oup.eac.service.ProductRegistrationService;
import com.oup.eac.service.ProductService;
import com.oup.eac.service.ServiceLayerException;
import com.oup.eac.service.exceptions.ServiceLayerValidationException;
import com.oup.eac.service.impl.ProductServiceImpl;
import com.oup.eac.ws.v2.binding.access.ProductRegistrationRequest;
import com.oup.eac.ws.v2.binding.access.ProductRegistrationResponse;
import com.oup.eac.ws.v2.binding.common.ErrorStatus;
import com.oup.eac.ws.v2.binding.common.Identifier;
import com.oup.eac.ws.v2.binding.common.RegistrationInformation;
import com.oup.eac.ws.v2.binding.common.WsUserId;
import com.oup.eac.ws.v2.ex.WebServiceException;
import com.oup.eac.ws.v2.ex.WebServiceValidationException;
import com.oup.eac.ws.v2.service.ProductRegistrationAdapter;
import com.oup.eac.ws.v2.service.WsCustomerLookup;
import com.oup.eac.ws.v2.service.WsProductLookup;
import com.oup.eac.ws.v2.service.utils.ErrorStatusUtils;

/**
 * The Class ProductRegistrationAdapterImpl.
 * Used to create product registrations from the EAC Web Service
 * @author David Hay
 */
public class ProductRegistrationAdapterImpl implements ProductRegistrationAdapter, WebServiceMessages {

    private static final Logger LOG = Logger.getLogger(ProductRegistrationAdapterImpl.class);
    
    private WsCustomerLookup customerLookup;

    private WsProductLookup productLookup;

    private ProductRegistrationService productRegistrationService;
    
    private ProductService productService ;

    public ProductRegistrationAdapterImpl(WsCustomerLookup customerLookup, WsProductLookup productLookup, ProductRegistrationService productRegistrationService, ProductService productService) {
    	Assert.notNull(customerLookup);
    	Assert.notNull(productLookup);
    	Assert.notNull(productRegistrationService);
    	Assert.notNull(productService);
        this.customerLookup = customerLookup;
        this.productLookup = productLookup;
        this.productRegistrationService = productRegistrationService;
        this.productService = productService ;
    }

    
    @Override
    @PreAuthorize("hasRole('ROLE_WS_PRODUCT_REGISTRATION')")
    public ProductRegistrationResponse createProductRegistration(ProductRegistrationRequest request) throws WebServiceException {
    	LOG.debug("start time : " + System.currentTimeMillis());
        ProductRegistrationResponse response = new ProductRegistrationResponse();
        Identifier productId = request.getProductId();
        Identifier userId = request.getUserId();
        RegistrationInformation[] registrationInformation = request.getRegistrationInformation();

        try {
            Customer customer = customerLookup.getCustomerByWsUserId(getWsUserId(userId));
            EnforceableProductDto product = getRegisterableProduct(productId);
            Map<String,String> registrationPageData = getRegistrationPageData(registrationInformation);
            this.productRegistrationService.createProductRegistration(product, customer, registrationPageData);
        } catch (WebServiceValidationException wsve) {
            ErrorStatus errorStatus = ErrorStatusUtils.getClientErrorStatus(wsve.getMessage());
            response.setErrorStatus(errorStatus);
        } catch (ServiceLayerValidationException slve) {
            ErrorStatus errorStatus = ErrorStatusUtils.getClientErrorStatus(slve.getMessage());
            response.setErrorStatus(errorStatus);
        } catch (ServiceLayerException sle) {
            throw new WebServiceException(ERR_PRODUCT_REGISTRATION_CREATION_UNEXPECTED, sle);
        }
        LOG.debug("start time : " + System.currentTimeMillis());
        return response;
    }

    private EnforceableProductDto getRegisterableProduct(Identifier productId) throws WebServiceValidationException{
    	EnforceableProductDto product = productLookup.lookupEnforceableProductByIdentifier(productId);
		/*try {
			product = productService.getEnforceableProductByErightsId(products.getId());
			
		} catch (ErightsException e) {
			// TODO Auto-generated catch block
			if(e.getErrorCode() == 2001){
				String msg = String.format(FMT_PRODUCT_FOR_ID_NOT_FOUND, products.getId());
				throw new WebServiceValidationException(msg);
			}
			throw new WebServiceValidationException(ERR_CANNOT_REGISTER_FOR_THIS_PRODUCT);
		}*/
/*        if ( !product.getRegisterableType().toString().equals(ProductType.REGISTERABLE.toString())) {
            throw new WebServiceValidationException(ERR_NOT_REGISTERABLE_PRODUCT);
        }*/
        if (product.getState().equals(ProductState.REMOVED.toString())) {
            String msg = String.format("Attempt was made to register for removed product (id=%s) (name=%s) with state : %s", product.getProductId(), product.getName(), product.getState()); 
            LOG.warn(msg);
            throw new WebServiceValidationException(ERR_CANNOT_REGISTER_FOR_THIS_PRODUCT);
        }
        //EnforceableProductDto result = (RegisterableProduct)product;
        return product;
    }


    /**
     * Gets the ws user id.
     *
     * @param userId the user id
     * @return the ws user id
     */
    private WsUserId getWsUserId(Identifier userId) {
        WsUserId id = new WsUserId();
        id.setUserId(userId);
        return id;
    }
    

    private Map<String,String> getRegistrationPageData(RegistrationInformation[] registrationInformation) throws WebServiceValidationException {
        //build up map of supplied registration information keeping track of duplicate registration keys
        Set<String> duplicateRegKeys = new HashSet<String>();
        Map<String, String> registrationPageData = new HashMap<String, String>();
        for (RegistrationInformation ri : registrationInformation) {
            String regKey = ri.getRegistrationKey();
            if (registrationPageData.containsKey(regKey)) {
                duplicateRegKeys.add(regKey);
            } else {
                registrationPageData.put(regKey, ri.getRegistrationValue());
            }
        }

        if (duplicateRegKeys.isEmpty() == false) {
            String msg = String.format(ERR_FMT_DUPLICATE_REGISTRATION_KEYS, duplicateRegKeys);
            throw new WebServiceValidationException(msg);
        }
        return registrationPageData;
    }

    

}
