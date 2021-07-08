package com.oup.eac.ws.v2.service.impl;

import java.util.Locale;

import com.oup.eac.ws.v2.binding.access.GetUserEntitlementsResponse;
import com.oup.eac.ws.v2.binding.access.GetUserEntitlementsResponseSequence;
import com.oup.eac.ws.v2.binding.common.CredentialName;
import com.oup.eac.ws.v2.binding.common.Identifiers;
import com.oup.eac.ws.v2.binding.common.ProductEntitlement;
import com.oup.eac.ws.v2.binding.common.ProductEntitlementGroup;
import com.oup.eac.ws.v2.binding.common.User;
import com.oup.eac.ws.v2.binding.common.WsUserId;
import com.oup.eac.ws.v2.service.UserEntitlementsAdapter;
import com.oup.eac.ws.v2.service.util.TestIdUtils;
import com.oup.eac.ws.v2.service.utils.LocaleUtils;

/**
 * Creates a stock web service response so we can check that it gets converted to xml by castor.
 * 
 * @author David Hay.
 */
public class DummyUserEntitlementsAdapter extends BaseDummyAdapter implements UserEntitlementsAdapter {

    @Override
    public GetUserEntitlementsResponse getUserEntitlementGroups(String systemId, WsUserId wsUserId) {
        GetUserEntitlementsResponse response = new GetUserEntitlementsResponse();
        GetUserEntitlementsResponseSequence seq = new GetUserEntitlementsResponseSequence();
        response.setGetUserEntitlementsResponseSequence(seq);
        User user = new User();
        Identifiers ids = TestIdUtils.getIds("indUserId", "extUserId", "system1", "ISBN");
        user.setUserIds(ids);
        user.setEmailAddress("test.user@oup.com");
        user.setFirstName("test");
        user.setLastName("user");
        user.setLocale(LocaleUtils.getLocaleType(new Locale("ar")));
        CredentialName cred = new CredentialName();
        cred.setUserName("test-user");
        user.setCredentialName(cred);
        seq.setUser(user);

        ProductEntitlement[] ents = getEntitlements();
        ProductEntitlementGroup[] groups = new ProductEntitlementGroup[ents.length];
        for(int i=0;i<ents.length;i++){
            ProductEntitlement ent = ents[i];
            ProductEntitlementGroup group = new ProductEntitlementGroup();
            group.setEntitlement(ent);
            if(i == 1){
                ProductEntitlement[] linked = getLinkedEntitlements();
                group.setLinkedEntitlement(linked);
            }
            groups[i] = group;
        }
        seq.setEntitlementGroup(groups);
        return response;
    }

}
