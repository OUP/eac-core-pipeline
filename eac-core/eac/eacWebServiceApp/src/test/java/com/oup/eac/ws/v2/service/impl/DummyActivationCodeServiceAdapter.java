package com.oup.eac.ws.v2.service.impl;

import com.oup.eac.ws.v2.binding.access.CreateActivationCodeBatchResponse;
import com.oup.eac.ws.v2.binding.common.ActivationCodeBatch;
import com.oup.eac.ws.v2.binding.common.ActivationCodeLicence;
import com.oup.eac.ws.v2.ex.WebServiceException;
import com.oup.eac.ws.v2.service.ActivationCodeServiceAdapter;

public class DummyActivationCodeServiceAdapter implements ActivationCodeServiceAdapter {

    @Override
    public CreateActivationCodeBatchResponse createActivationCodeBatch(ActivationCodeBatch activationCodeBatch, ActivationCodeLicence acticationCodeLicence)
            throws WebServiceException {
        CreateActivationCodeBatchResponse resp = new CreateActivationCodeBatchResponse();        
        resp.setErrorStatus(null);
        String[] codes = { "CODE1","CODE2" };
        resp.setActivationCode(codes);
        return resp;
    }

}
