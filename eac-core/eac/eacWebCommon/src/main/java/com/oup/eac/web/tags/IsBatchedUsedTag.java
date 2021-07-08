package com.oup.eac.web.tags;

import com.amazonaws.services.cloudfront.model.AccessDeniedException;
import com.oup.eac.integration.facade.exceptions.ErightsException;
import com.oup.eac.integration.facade.exceptions.GroupNotFoundException;
import com.oup.eac.integration.facade.exceptions.LicenseNotFoundException;
import com.oup.eac.integration.facade.exceptions.ProductNotFoundException;
import com.oup.eac.integration.facade.exceptions.UserNotFoundException;
import com.oup.eac.service.ActivationCodeService;
import com.oup.eac.service.ServiceLayerException;

public class IsBatchedUsedTag extends BaseGetValueTag {

    private String batchId;
    
    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }


    @Override
    public Object getValue() throws ServiceLayerException, ProductNotFoundException, UserNotFoundException, 
    LicenseNotFoundException, AccessDeniedException, GroupNotFoundException, ErightsException {
        ActivationCodeService activationCodeService = getService("activationCodeService", ActivationCodeService.class);
        Boolean value = activationCodeService.hasActivationCodeBatchBeenUsed(batchId);
        return value;
    }

}
