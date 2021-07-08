package com.oup.eac.web.controllers.helpers.impl;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.util.Assert;

import com.oup.eac.domain.Customer;
import com.oup.eac.domain.Customer.CustomerType;
import com.oup.eac.domain.Product.ProductState;
import com.oup.eac.domain.RegisterableProduct;
import com.oup.eac.domain.RegisterableProduct.RegisterableType;
import com.oup.eac.web.controllers.authentication.AuthenticationWorkFlow.RegistrationNotAllowedReason;
import com.oup.eac.web.controllers.helpers.RegistrationNotAllowedMessageCodeSource;

/**
 * The Class RegistrationNotAllowedMessageCodeSourceImpl.
 * 
 * Note : did not use annotations for this class as annotations do not work well with maps where the key is not a String.
 * 
 * @author David Hay
 */
public class RegistrationNotAllowedMessageCodeSourceImpl implements RegistrationNotAllowedMessageCodeSource {

    private static final Logger LOG = Logger.getLogger(RegistrationNotAllowedMessageCodeSourceImpl.class);
    
    private Map<RegistrationNotAllowedMessageCodeConfig, String> config;
    private String productRegDefMessageCode;
    private String defaultMessageCode;
    
    /**
     * Instantiates a new registration not allowed message code source impl.
     *
     * @param config the config
     * @param productRegDefMessageCode the product reg def message code
     * @param defaultMessageCode the default message code
     */
    public RegistrationNotAllowedMessageCodeSourceImpl(Map<RegistrationNotAllowedMessageCodeConfig, String> config, String productRegDefMessageCode, String defaultMessageCode) {
        this.config = config;
        this.productRegDefMessageCode = productRegDefMessageCode;
        this.defaultMessageCode = defaultMessageCode;
    }
    
    /**
     * Instantiates a new registration not allowed message code source impl.
     * 
     * @param config the config
     */    
    @Override
    public String getMessageCode(RegistrationNotAllowedReason notAllowedReason, Customer customer,
            RegisterableProduct product, ProductState lifecycleState) {
        
        if (notAllowedReason == RegistrationNotAllowedReason.PRODUCT_REG_DEF) {
            return this.productRegDefMessageCode;
        }

        Assert.isTrue(product != null);
        Assert.isTrue(customer != null);
        CustomerType customerType = customer.getCustomerType();
        RegisterableType registerableType = product.getRegisterableType();
        
        RegistrationNotAllowedMessageCodeConfig key = new RegistrationNotAllowedMessageCodeConfig();
        key.setNotAllowedReason(notAllowedReason);
        key.setCustomerType(customerType);
        key.setRegisterableType(registerableType);
        key.setLifecycleState(lifecycleState);

        String result = config.get(key);
        
        if(result == null) {
            String message = String.format("Failed to find RegisrationDeniedMessageCode for [%s] using default[%s]",key,this.defaultMessageCode);
            LOG.error(message);
            result = this.defaultMessageCode;
        }
        
        return result;

    }

    public Map<RegistrationNotAllowedMessageCodeConfig, String> getConfig() {
        return config;
    }

    @Override
    public String getProductRegDefMessageCode() {
        return productRegDefMessageCode;
    }

    @Override
    public String getDefaultMessageCode() {
        return defaultMessageCode;
    }
    
}
