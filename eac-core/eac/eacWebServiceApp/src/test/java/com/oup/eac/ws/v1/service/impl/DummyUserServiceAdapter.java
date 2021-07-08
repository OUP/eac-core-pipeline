package com.oup.eac.ws.v1.service.impl;

import com.oup.eac.service.ServiceLayerException;
import com.oup.eac.ws.v1.service.UserServiceAdapter;
import com.oup.eac.ws.v1.userdata.binding.RegistrationInformation;
import com.oup.eac.ws.v1.userdata.binding.RegistrationInformationResponse;
import com.oup.eac.ws.v1.userdata.binding.User;
import com.oup.eac.ws.v1.userdata.binding.UserNameResponse;

public class DummyUserServiceAdapter implements UserServiceAdapter{

    @Override
    public UserNameResponse getUserName(String sessionToken) throws ServiceLayerException {
        if(sessionToken.equals("npe")){
            throw new NullPointerException(sessionToken);
        }
        if(sessionToken.equals("sle")){
            throw new ServiceLayerException(sessionToken);
        }
        UserNameResponse response = new UserNameResponse();
        response.setUserName(sessionToken);
        return response;
    }

	@Override
	public RegistrationInformationResponse getRegistrationInformation(
			String sessionToken) throws ServiceLayerException {
	    RegistrationInformationResponse resp = new RegistrationInformationResponse();
	    User customer = new User();
	    customer.setEmailAddress("david.hay@oup.com");
	    customer.setFirstName("David");
	    customer.setLastName("Hay");
	    customer.setUserName("davidhay");
        resp.setUser(customer);
        
        RegistrationInformation[] regInfo = new RegistrationInformation[2];
        RegistrationInformation regInfo1 = new RegistrationInformation();
        RegistrationInformation regInfo2 = new RegistrationInformation();
        regInfo[0] = regInfo1;
        regInfo[1] = regInfo2;
        
        regInfo1.setRegistrationKey("regKey1");
        regInfo1.setRegistrationValue("regValue1");
        
        regInfo2.setRegistrationKey("regKey2");
        regInfo2.setRegistrationValue("regValue2");

        resp.setRegistrationInformation(regInfo);
	    return resp;
	}

}
