package com.oup.eac.ws.v1.endpoint;

import javax.annotation.Resource;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.oup.eac.service.ServiceLayerException;
import com.oup.eac.ws.v1.service.UserServiceAdapter;
import com.oup.eac.ws.v1.userdata.binding.RegistrationInformationRequest;
import com.oup.eac.ws.v1.userdata.binding.RegistrationInformationResponse;
import com.oup.eac.ws.v1.userdata.binding.UserNameRequest;
import com.oup.eac.ws.v1.userdata.binding.UserNameResponse;

@Endpoint
public class UserDataServiceEndpoint {

	public static final String NAMESPACE = "http://eac.oup.com/eac-user-data-services";
	
    @Resource(name = "${ws.user.service.adapter.bean.name.v1}")
    private UserServiceAdapter userServiceAdapter;

    /**
     * @return UserServiceFacade
     */
    public final UserServiceAdapter getUserServiceAdapter() {
        return userServiceAdapter;
    }

    /**
     * @param userService
     *            user service facade
     */
    public final void setUserServiceAdapter(final UserServiceAdapter userService) {
        this.userServiceAdapter = userService;
    }
    
    /**
     * @param sessionToken
     *            session token
     * @return user name response
     * @throws ServiceLayerException
     *             the exception
     */
    @PayloadRoot(localPart="UserNameRequest", namespace=NAMESPACE)
    @ResponsePayload
    UserNameResponse getUserName(@RequestPayload UserNameRequest request) throws ServiceLayerException {
    	
        UserNameResponse response = null;

        String sessionToken = request.getSessionToken();
        response = userServiceAdapter.getUserName(sessionToken);

        return response;
    }

    /**
     * @param sessionToken
     *            session token
     * @return registration information response
     * @throws ServiceLayerException
     *             the exception
     */
    @PayloadRoot(localPart="RegistrationInformationRequest", namespace=NAMESPACE)
    @ResponsePayload
    RegistrationInformationResponse getRegistrationInformation(@RequestPayload RegistrationInformationRequest request) throws ServiceLayerException {
    
    	RegistrationInformationResponse response = null;

        String sessionToken = request.getSessionToken();
        response = userServiceAdapter.getRegistrationInformation(sessionToken);

        return response;
   	}
    
}
