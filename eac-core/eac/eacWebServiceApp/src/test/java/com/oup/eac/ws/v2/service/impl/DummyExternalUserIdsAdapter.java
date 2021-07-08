package com.oup.eac.ws.v2.service.impl;

import com.oup.eac.ws.v2.binding.access.SetExternalUserIdsRequest;
import com.oup.eac.ws.v2.binding.access.SetExternalUserIdsResponse;
import com.oup.eac.ws.v2.ex.WebServiceException;
import com.oup.eac.ws.v2.service.ExternalUserIdsAdapter;

public class DummyExternalUserIdsAdapter implements ExternalUserIdsAdapter {

    @Override
    public SetExternalUserIdsResponse setExternalUserIds(SetExternalUserIdsRequest request) throws WebServiceException {
        SetExternalUserIdsResponse response = new SetExternalUserIdsResponse();
        return response;
    }

}
