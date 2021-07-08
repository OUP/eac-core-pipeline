package com.oup.eac.ws.v2.service.impl;

import com.oup.eac.ws.v2.binding.access.UpdateUserAccountRequest;
import com.oup.eac.ws.v2.binding.access.UpdateUserAccountResponse;
import com.oup.eac.ws.v2.service.UpdateUserAccountAdapter;

public class DummyUpdateUserAccountAdapter implements UpdateUserAccountAdapter{

    @Override
    public UpdateUserAccountResponse updateUserAccount(UpdateUserAccountRequest request) {
        UpdateUserAccountResponse result = new UpdateUserAccountResponse();        
        return result;
    }
}
