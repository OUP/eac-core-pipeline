package com.oup.eac.ws.v2.service.impl;

import com.oup.eac.ws.v2.binding.access.SetExternalProductIdsRequest;
import com.oup.eac.ws.v2.binding.access.SetExternalProductIdsResponse;
import com.oup.eac.ws.v2.ex.WebServiceException;
import com.oup.eac.ws.v2.service.ExternalProductIdsAdapter;

public class DummyExternalProductIdsAdapter implements ExternalProductIdsAdapter {

    @Override
    public SetExternalProductIdsResponse setExternalProductIds(SetExternalProductIdsRequest request) throws WebServiceException {
        SetExternalProductIdsResponse response = new SetExternalProductIdsResponse();
        return response;
    }

}
