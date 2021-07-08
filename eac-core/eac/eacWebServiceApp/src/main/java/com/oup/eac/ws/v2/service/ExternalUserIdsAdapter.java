package com.oup.eac.ws.v2.service;

import com.oup.eac.ws.v2.binding.access.SetExternalUserIdsRequest;
import com.oup.eac.ws.v2.binding.access.SetExternalUserIdsResponse;
import com.oup.eac.ws.v2.ex.WebServiceException;

public interface ExternalUserIdsAdapter {

    /**
     * Sets the external user ids.
     *
     * @param request the request
     * @return the sets the external user ids response
     * @throws WebServiceException the web service exception
     */
    SetExternalUserIdsResponse setExternalUserIds(SetExternalUserIdsRequest request) throws WebServiceException;
}
