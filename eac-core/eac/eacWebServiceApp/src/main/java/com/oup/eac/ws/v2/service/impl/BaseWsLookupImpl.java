package com.oup.eac.ws.v2.service.impl;

import org.apache.commons.lang.StringUtils;

import com.oup.eac.ws.v2.ex.WebServiceValidationException;

public abstract class BaseWsLookupImpl implements WebServiceMessages {
    
    private boolean anyBlank(String... values) {        
        if(values == null){
            return false;
        }
        for(String value : values){
            if(StringUtils.isBlank(value)){
                return true;
            }
        }
        return false;
    }
    
    protected void checkNotBlank(String msg, String... values) throws WebServiceValidationException {
        if (anyBlank(values)) {
            throw new WebServiceValidationException(msg);
        }
    }


}
