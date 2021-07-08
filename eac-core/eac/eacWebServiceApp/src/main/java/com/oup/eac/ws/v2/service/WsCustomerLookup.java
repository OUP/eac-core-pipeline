package com.oup.eac.ws.v2.service;

import java.util.Set;

import com.oup.eac.domain.Answer;
import com.oup.eac.domain.Customer;
import com.oup.eac.ws.v2.binding.common.WsUserId;
import com.oup.eac.ws.v2.ex.WebServiceValidationException;

/**
 * The Interface WsCustomerLookup.
 */
public interface WsCustomerLookup {

    /**
     * Gets the customer by ws user id.
     *
     * @param wsUserId the ws user id
     * @return the customer by ws user id
     * @throws WebServiceValidationException the web service validation exception
     */
    Customer getCustomerByWsUserId(WsUserId wsUserId) throws  WebServiceValidationException;
    
    
    /**
     * Gets the customer with answers.
     *
     * @param customerId the customer id
     * @return the customer with answers
     * @throws WebServiceValidationException the web service validation exception
     */
    Set<Answer> getCustomerWithAnswers(String customerId) throws WebServiceValidationException;


	void checkNotEmpty(WsUserId wsUserId) throws WebServiceValidationException;
}
