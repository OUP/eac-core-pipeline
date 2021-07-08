package com.oup.eac.ws.v2.service.impl;

import com.oup.eac.ws.v2.binding.access.CreateUserAccountResponse;
import com.oup.eac.ws.v2.binding.access.UserStatusType;
import com.oup.eac.ws.v2.binding.common.CreateUser;
import com.oup.eac.ws.v2.binding.common.ErrorStatus;
import com.oup.eac.ws.v2.binding.common.InternalIdentifier;
import com.oup.eac.ws.v2.binding.common.types.StatusCode;
import com.oup.eac.ws.v2.service.CreateUserAccountAdapter;

public class DummyCreateUserAccountAdapter implements CreateUserAccountAdapter{

    @Override
    public CreateUserAccountResponse createUserAccounts(CreateUser[] users) {
        CreateUserAccountResponse response = new CreateUserAccountResponse();
        UserStatusType[] userStatus = new UserStatusType[3];
        userStatus[0] = getUserStatus1();
        userStatus[1] = getUserStatus2();
        userStatus[2] = getUserStatus3();
        response.setUserStatus(userStatus);
        return response;
    }

    private UserStatusType getUserStatus1() {
        UserStatusType us = new UserStatusType();
        
        //SUCCESS
        InternalIdentifier internalid = new InternalIdentifier();
        internalid.setId("12312123123131231231231");        
        
        us.setUserId(internalid);
        return us;
    }
    
    private UserStatusType getUserStatus2() {
        UserStatusType us = new UserStatusType();
        
        //ERROR
        ErrorStatus es = new ErrorStatus();
        es.setStatusCode(StatusCode.CLIENT_ERROR);
        es.setStatusReason("reason 2");
        
        us.setErrorStatus(es);
        return us;
    }

    private UserStatusType getUserStatus3() {
        UserStatusType us = new UserStatusType();
        
        //ERROR
        ErrorStatus es = new ErrorStatus();
        es.setStatusCode(StatusCode.CLIENT_ERROR);
        es.setStatusReason("reason 3");
        
        us.setErrorStatus(es);
        return us;
    }

}
