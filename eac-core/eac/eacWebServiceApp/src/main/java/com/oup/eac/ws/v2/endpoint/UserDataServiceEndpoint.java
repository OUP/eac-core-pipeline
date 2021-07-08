package com.oup.eac.ws.v2.endpoint;

import org.springframework.ws.server.endpoint.annotation.RequestPayload;

import com.oup.eac.service.ServiceLayerException;
import com.oup.eac.ws.v2.binding.userdata.RegistrationInformationRequest;
import com.oup.eac.ws.v2.binding.userdata.RegistrationInformationResponse;
import com.oup.eac.ws.v2.binding.userdata.UserNameRequest;
import com.oup.eac.ws.v2.binding.userdata.UserNameResponse;

public interface UserDataServiceEndpoint {

    public UserNameResponse getUserName(@RequestPayload UserNameRequest request) throws ServiceLayerException;
    
    public RegistrationInformationResponse getRegistrationInformation(@RequestPayload RegistrationInformationRequest request) throws ServiceLayerException;
}