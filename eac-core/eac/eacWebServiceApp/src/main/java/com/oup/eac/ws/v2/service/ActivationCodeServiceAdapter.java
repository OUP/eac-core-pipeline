package com.oup.eac.ws.v2.service;

import com.amazonaws.services.cloudfront.model.AccessDeniedException;
import com.oup.eac.integration.facade.exceptions.ErightsException;
import com.oup.eac.integration.facade.exceptions.GroupNotFoundException;
import com.oup.eac.integration.facade.exceptions.LicenseNotFoundException;
import com.oup.eac.integration.facade.exceptions.ProductNotFoundException;
import com.oup.eac.integration.facade.exceptions.UserNotFoundException;
import com.oup.eac.ws.v2.binding.access.CreateActivationCodeBatchResponse;
import com.oup.eac.ws.v2.binding.common.ActivationCodeBatch;
import com.oup.eac.ws.v2.binding.common.ActivationCodeLicence;
import com.oup.eac.ws.v2.ex.WebServiceException;

public interface ActivationCodeServiceAdapter {

    public CreateActivationCodeBatchResponse createActivationCodeBatch(final ActivationCodeBatch activationCodeBatch, 
    		final ActivationCodeLicence acticationCodeLicence) throws WebServiceException, 
    		ProductNotFoundException, UserNotFoundException, LicenseNotFoundException, 
    		AccessDeniedException, GroupNotFoundException, ErightsException;
}
