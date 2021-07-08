package com.oup.eac.ws.v2.service.impl;

import java.util.Locale;

import com.oup.eac.service.ServiceLayerException;
import com.oup.eac.ws.v2.binding.access.AuthenticateResponse;
import com.oup.eac.ws.v2.binding.access.AuthenticateResponseSequence;
import com.oup.eac.ws.v2.binding.access.ChangePasswordResponse;
import com.oup.eac.ws.v2.binding.access.LogoutResponse;
import com.oup.eac.ws.v2.binding.access.ResetPasswordResponse;
import com.oup.eac.ws.v2.binding.common.CredentialName;
import com.oup.eac.ws.v2.binding.common.ErrorStatus;
import com.oup.eac.ws.v2.binding.common.Identifiers;
import com.oup.eac.ws.v2.binding.common.RegistrationInformation;
import com.oup.eac.ws.v2.binding.common.User;
import com.oup.eac.ws.v2.binding.common.WsUserId;
import com.oup.eac.ws.v2.binding.userdata.RegistrationInformationResponse;
import com.oup.eac.ws.v2.binding.userdata.RegistrationInformationResponseSequence;
import com.oup.eac.ws.v2.binding.userdata.UserNameResponse;
import com.oup.eac.ws.v2.service.UserServiceAdapter;
import com.oup.eac.ws.v2.service.util.TestIdUtils;
import com.oup.eac.ws.v2.service.utils.ErrorStatusUtils;
import com.oup.eac.ws.v2.service.utils.LocaleUtils;

public class DummyUserServiceAdapter extends BaseDummyAdapter implements UserServiceAdapter {

    @Override
    public UserNameResponse getUserName(WsUserId wsUserId) {
        String sessionToken = wsUserId.getSessionToken();
        if (sessionToken.equals("npe")) {
            throw new NullPointerException(sessionToken);
        }
        UserNameResponse resp = new UserNameResponse();
        if (sessionToken.equals("sle")) {
            ErrorStatus errorStatus = ErrorStatusUtils.getServerErrorStatus(sessionToken);            
            resp.setErrorStatus(errorStatus);
            return resp;
        }
        resp.setUserName(sessionToken);
        if(sessionToken.equals("INVALID")){
            ErrorStatus errorStatus = ErrorStatusUtils.getServerErrorStatus("this response will be converted into an invalid xml response");
            resp.setErrorStatus(errorStatus);
        }
        return resp;
    }

    private User getUser() {
        User user = new User();
        user.setEmailAddress("david.hay@oup.com");
        user.setFirstName("David");
        user.setLastName("Hay");
        CredentialName cred = new CredentialName();
        cred.setUserName("davidhay");
        user.setCredentialName(cred);
        Identifiers ids = TestIdUtils.getIds("internal1","extUserId1", "SYSTEM1","ISBN");
        user.setUserIds(ids);
        user.setLocale(LocaleUtils.getLocaleType(Locale.CANADA_FRENCH));
        return user;
    }

    @Override
    public RegistrationInformationResponse getRegistrationInformation(String systemId, WsUserId wsUserId) throws ServiceLayerException {
        RegistrationInformationResponse resp = new RegistrationInformationResponse();

        RegistrationInformationResponseSequence seq = new RegistrationInformationResponseSequence();
        resp.setRegistrationInformationResponseSequence(seq);
        User customer = getUser();
        seq.setUser(customer);

        RegistrationInformation[] regInfo = new RegistrationInformation[2];
        RegistrationInformation regInfo1 = new RegistrationInformation();
        RegistrationInformation regInfo2 = new RegistrationInformation();
        regInfo[0] = regInfo1;
        regInfo[1] = regInfo2;

        regInfo1.setRegistrationKey("regKey1");
        regInfo1.setRegistrationValue("regValue1");

        regInfo2.setRegistrationKey("regKey2");
        regInfo2.setRegistrationValue("regValue2");

        seq.setRegistrationInformation(regInfo);
        return resp;
    }

    @Override
    public AuthenticateResponse authenticate(String username, String password) {
        AuthenticateResponse resp = new AuthenticateResponse();
        if (username.equals("npe")) {
            throw new NullPointerException("npe for authenticate");
        } else {
            User user = getUser();
            AuthenticateResponseSequence seq  = new AuthenticateResponseSequence();
            seq.setSessionToken("SESSIONTOKEN123");
            seq.setUser(user);
            resp.setAuthenticateResponseSequence(seq);
        }
        return resp;
    }

    @Override
    public LogoutResponse logout(String sessionToken) {
        LogoutResponse resp = new LogoutResponse();
        if (sessionToken.equals("npe")) {
            throw new NullPointerException("npe for logout");
        }
        return resp;
    }

    @Override
    public ChangePasswordResponse changePassword(WsUserId wsUserId, String newPassword) {
        ChangePasswordResponse resp = new ChangePasswordResponse();
        if (getId(wsUserId).equals("npe")) {
            throw new NullPointerException("npe for changepassword");
        }
        return resp;
    }

	@Override
	public ResetPasswordResponse resetPassword(WsUserId wsUserId,
			String wsUsername) {
		ResetPasswordResponse resp = new ResetPasswordResponse();
        String id = getId(wsUserId);
        if (id.equals("npe")) {
            throw new NullPointerException("npe for resetpassword");
        }else{
            if(id.equals("empty")){
                resp.setNewPassword(null);
            }else{
                resp.setNewPassword(id);
            }
        }
        return resp;
	}

}
