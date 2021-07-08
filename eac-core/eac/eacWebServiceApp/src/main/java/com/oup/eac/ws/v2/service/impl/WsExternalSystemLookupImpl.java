package com.oup.eac.ws.v2.service.impl;

import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.util.Assert;

import com.oup.eac.domain.ExternalSystem;
import com.oup.eac.integration.facade.exceptions.DivisionNotFoundException;
import com.oup.eac.integration.facade.exceptions.ErightsException;
import com.oup.eac.service.ExternalIdService;
import com.oup.eac.ws.v2.ex.WebServiceValidationException;
import com.oup.eac.ws.v2.service.WsExternalSystemLookup;

public class WsExternalSystemLookupImpl extends BaseWsLookupImpl implements WsExternalSystemLookup {

    private ExternalIdService externalIdService;

    public WsExternalSystemLookupImpl(ExternalIdService externalIdService) {
    	Assert.notNull(externalIdService);
        this.externalIdService = externalIdService;
    }

    @Override
    public ExternalSystem lookupExternalSystemByName(String externalSystemName) throws WebServiceValidationException, AccessDeniedException {
        ExternalSystem es = new ExternalSystem();
		try {
			es = this.externalIdService.getExternalSystemByName(externalSystemName);
		} catch (ErightsException e) {
			throw new WebServiceValidationException("The systemId does not exist : " + externalSystemName);
			//e.printStackTrace();
		}
        if (es == null) {
            throw new WebServiceValidationException("The systemId does not exist : " + externalSystemName);
        }
        return es;
    }

    @Override
    public void validateExternalSystem(String systemId) throws WebServiceValidationException, AccessDeniedException, ErightsException {
        if(null != systemId){
            if (StringUtils.isBlank(systemId) == false) {
                @SuppressWarnings("unused")
                ExternalSystem es = this.lookupExternalSystemByName(systemId);
            }else {
                throw new WebServiceValidationException("systemId cannot be blank");
            }
            
        }
        
    }
    
    
    @Override
    public void validateMultipleExternalSystem(Set<String> systemIdSet) throws WebServiceValidationException, AccessDeniedException, ErightsException {
       for(String systemId : systemIdSet){
           validateExternalSystem(systemId);
       }
        
    }

}
