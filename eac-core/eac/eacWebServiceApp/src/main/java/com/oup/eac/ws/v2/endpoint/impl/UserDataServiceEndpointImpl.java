package com.oup.eac.ws.v2.endpoint.impl;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.oup.eac.service.ServiceLayerException;
import com.oup.eac.ws.v2.binding.common.WsUserId;
import com.oup.eac.ws.v2.binding.userdata.RegistrationInformationRequest;
import com.oup.eac.ws.v2.binding.userdata.RegistrationInformationResponse;
import com.oup.eac.ws.v2.binding.userdata.UserNameRequest;
import com.oup.eac.ws.v2.binding.userdata.UserNameResponse;
import com.oup.eac.ws.v2.endpoint.UserDataServiceEndpoint;
import com.oup.eac.ws.v2.service.UserServiceAdapter;

@Endpoint("userDataServiceEndpoint_v2")
public class UserDataServiceEndpointImpl implements UserDataServiceEndpoint {

    public static final String NAMESPACE = "http://eac.oup.com/2.0/eac-user-data-services";
    
    @Resource(name = "${ws.user.service.adapter.bean.name.v2}")
    private UserServiceAdapter userServiceAdapter;
    
    @Value("${ws.user.service.adapter.v2.enabled}")
    private boolean enabled;

    /**
     * {@inheritDoc}
     */
    @PayloadRoot(localPart="UserNameRequest", namespace=NAMESPACE)
    @ResponsePayload
    @Override
    public UserNameResponse getUserName(@RequestPayload UserNameRequest request) throws ServiceLayerException {
        checkEnabled();
        
        UserNameResponse response = null;

        WsUserId wsUserId = request.getWsUserId();
        response = userServiceAdapter.getUserName(wsUserId);

        return response;
    }

    /**
     * {@inheritDoc}
     */
    @PayloadRoot(localPart="RegistrationInformationRequest", namespace=NAMESPACE)
    @ResponsePayload
    @Override
    public RegistrationInformationResponse getRegistrationInformation(@RequestPayload RegistrationInformationRequest request) throws ServiceLayerException {
    
        checkEnabled();
        
        String systemId = request.getSystemId();
        WsUserId wsUserId = request.getWsUserId();
        RegistrationInformationResponse response = userServiceAdapter.getRegistrationInformation(systemId, wsUserId);

        return response;
    }
    
    /**
     * This allows us to prevent access to "UserDataServices V2 methods" in an online environment but allow access during off line unit tests.
     * @throws ServiceLayerException
     */
    private void checkEnabled() throws ServiceLayerException {
        if(!enabled){
            throw new ServiceLayerException("This operation has been disabled");
        }
    }

}
