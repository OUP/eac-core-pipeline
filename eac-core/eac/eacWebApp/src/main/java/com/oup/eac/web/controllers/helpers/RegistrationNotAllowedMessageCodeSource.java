package com.oup.eac.web.controllers.helpers;

import com.oup.eac.domain.Customer;
import com.oup.eac.domain.Product.ProductState;
import com.oup.eac.domain.RegisterableProduct;
import com.oup.eac.web.controllers.authentication.AuthenticationWorkFlow.RegistrationNotAllowedReason;

 /**
 * The Interface RegistrationNotAllowedMessageCodeSource.
 * 
 * @author David Hay
 */
public interface RegistrationNotAllowedMessageCodeSource {

    /**
     * Gets the message code.
     *
     * @param notAllowedReason the not allowed reason
     * @param customer the customer
     * @param product the product
     * @param lifecycleState the lifecycle state
     * @return the message code
     */
    String getMessageCode(RegistrationNotAllowedReason notAllowedReason, Customer customer, RegisterableProduct product, ProductState lifecycleState);

    /**
     * Gets the product reg def message code.
     *
     * @return the product reg def message code
     */
    String getProductRegDefMessageCode();

    /**
     * Gets the default message code.
     *
     * @return the default message code
     */
    String getDefaultMessageCode();
}
