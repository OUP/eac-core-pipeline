package com.oup.eac.ws.v2.service;

import com.oup.eac.ws.v2.binding.access.ProductRegistrationRequest;
import com.oup.eac.ws.v2.binding.access.ProductRegistrationResponse;
import com.oup.eac.ws.v2.ex.WebServiceException;

public interface ProductRegistrationAdapter {

    /**
     * Creates the product registration.
     *
     * @param request the request
     * @return the product registration response
     * @throws WebServiceException the web service exception
     */
    ProductRegistrationResponse createProductRegistration(ProductRegistrationRequest request) throws WebServiceException;
}
