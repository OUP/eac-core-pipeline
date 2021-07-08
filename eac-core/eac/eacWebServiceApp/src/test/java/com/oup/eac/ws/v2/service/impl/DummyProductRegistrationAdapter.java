package com.oup.eac.ws.v2.service.impl;

import com.oup.eac.ws.v2.binding.access.ProductRegistrationRequest;
import com.oup.eac.ws.v2.binding.access.ProductRegistrationResponse;
import com.oup.eac.ws.v2.ex.WebServiceException;
import com.oup.eac.ws.v2.service.ProductRegistrationAdapter;

public class DummyProductRegistrationAdapter implements ProductRegistrationAdapter{

    @Override
    public ProductRegistrationResponse createProductRegistration(ProductRegistrationRequest request) throws WebServiceException {
        ProductRegistrationResponse response = new ProductRegistrationResponse();
        return response;
    }

}
