package com.oup.eac.ws.v2.service;

import com.oup.eac.ws.v2.binding.access.KillUserSessionRequest;
import com.oup.eac.ws.v2.binding.access.KillUserSessionResponse;
import com.oup.eac.ws.v2.ex.WebServiceException;

/**
 * The Interface KillUserSessionAdapter.
 */
public interface KillUserSessionAdapter {

    /**
     * Kills user session
     *
     * @param request the request
     * @return the kill user session response
     */
    public KillUserSessionResponse killUserSession(KillUserSessionRequest request) throws WebServiceException;
}
