package com.oup.eac.ws.v2.service;

import com.oup.eac.ws.v2.binding.access.SetExternalProductIdsRequest;
import com.oup.eac.ws.v2.binding.access.SetExternalProductIdsResponse;
import com.oup.eac.ws.v2.ex.WebServiceException;

public interface ExternalProductIdsAdapter {

    /**
     * Sets the external product ids.
     *
     * @param request the request
     * @return the sets the external product ids response
     * @throws WebServiceException the web service exception
     */
    SetExternalProductIdsResponse setExternalProductIds(SetExternalProductIdsRequest request) throws WebServiceException;
}
