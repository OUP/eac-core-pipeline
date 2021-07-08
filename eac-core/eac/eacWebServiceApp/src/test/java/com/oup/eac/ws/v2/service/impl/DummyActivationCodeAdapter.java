package com.oup.eac.ws.v2.service.impl;

import java.util.Locale;

import com.oup.eac.ws.v2.binding.access.GuestRedeemActivationCodeResponse;
import com.oup.eac.ws.v2.binding.access.RedeemActivationCodeResponse;
import com.oup.eac.ws.v2.binding.access.SearchActivationCodeResponse;
import com.oup.eac.ws.v2.binding.access.ValidateActivationCodeResponse;
import com.oup.eac.ws.v2.binding.access.ValidateActivationCodeResponseSequence;
import com.oup.eac.ws.v2.binding.access.ValidateActivationCodeResponseSequenceItem;
import com.oup.eac.ws.v2.binding.common.GuestProductEntitlement;
import com.oup.eac.ws.v2.binding.common.ProductEntitlement;
import com.oup.eac.ws.v2.binding.common.WsUserId;
import com.oup.eac.ws.v2.ex.WebServiceException;
import com.oup.eac.ws.v2.service.ActivationCodeAdapter;

public class DummyActivationCodeAdapter extends BaseDummyAdapter implements ActivationCodeAdapter {

    @Override
    public RedeemActivationCodeResponse redeemActivationCode(String systemId, WsUserId wsUserId, String activationCode, Locale locale) throws WebServiceException {
        // this allows us to simulate service layer exceptions
        String id = getId(wsUserId);
        if (id.startsWith("serviceLayerException")) {
            throw new WebServiceException("SLE Message");
        }
        if (id.startsWith("runtimeException")) {
            throw new NullPointerException("NPE Message");
        }
        RedeemActivationCodeResponse result = new RedeemActivationCodeResponse();
        ProductEntitlement[] ents = getEntitlements();
        result.setEntitlement(ents);
        result.setErrorStatus(null);
        return result;
    }

	@Override
	public ValidateActivationCodeResponse validateActivationCode(String systemId, String activationCode) throws WebServiceException {
	    ValidateActivationCodeResponse response = new ValidateActivationCodeResponse();
	    ValidateActivationCodeResponseSequence seq = new ValidateActivationCodeResponseSequence();
	    ValidateActivationCodeResponseSequence[] seqArr = { seq };
	    ValidateActivationCodeResponseSequenceItem item = new ValidateActivationCodeResponseSequenceItem();
	    ValidateActivationCodeResponseSequenceItem[] items = { item };
	    item.setActivationCode(activationCode);
	    item.setProduct(getProductEntitlemResult().getProduct(0));//a product details record
	    seq.setValidateActivationCodeResponseSequenceItem(items);
        response.setValidateActivationCodeResponseSequence(seqArr);
        return response;
	}

	@Override
	public SearchActivationCodeResponse searchActivationCode(String systemId,
			String activationCode, boolean likeSearch)
			throws WebServiceException {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
    public GuestRedeemActivationCodeResponse guestRedeemActivationCode(String activationCode) throws WebServiceException {
        // this allows us to simulate service layer exceptions
        GuestRedeemActivationCodeResponse result = new GuestRedeemActivationCodeResponse();
        GuestProductEntitlement[] ents = getGuestEntitlements();
        result.setEntitlement(ents);
        result.setErrorStatus(null);
        return result;
    }
    
}
