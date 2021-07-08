package com.oup.eac.ws.v2.service.impl;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.Assert;

import com.oup.eac.domain.Customer;
import com.oup.eac.dto.ExternalIdDto;
import com.oup.eac.service.ExternalIdService;
import com.oup.eac.service.ServiceLayerException;
import com.oup.eac.service.exceptions.ServiceLayerValidationException;
import com.oup.eac.ws.v2.binding.access.SetExternalUserIdsRequest;
import com.oup.eac.ws.v2.binding.access.SetExternalUserIdsResponse;
import com.oup.eac.ws.v2.binding.common.ErrorStatus;
import com.oup.eac.ws.v2.binding.common.ExternalIdentifier;
import com.oup.eac.ws.v2.binding.common.WsUserId;
import com.oup.eac.ws.v2.ex.WebServiceException;
import com.oup.eac.ws.v2.ex.WebServiceValidationException;
import com.oup.eac.ws.v2.service.ExternalUserIdsAdapter;
import com.oup.eac.ws.v2.service.WsCustomerLookup;
import com.oup.eac.ws.v2.service.utils.ErrorStatusUtils;
import com.oup.eac.ws.v2.service.utils.IdUtils;

public class ExternalUserIdsAdapterImpl implements ExternalUserIdsAdapter {

    private WsCustomerLookup customerLookup;
    private ExternalIdService externalIdService;

    public ExternalUserIdsAdapterImpl(WsCustomerLookup customerLookup, ExternalIdService externalIdService) {
        super();
        Assert.notNull(customerLookup);
        Assert.notNull(externalIdService);
        this.customerLookup = customerLookup;
        this.externalIdService = externalIdService;
    }

    @Override
    @PreAuthorize("hasRole('ROLE_WS_SET_EXTERNAL_USER_IDS')")
    public SetExternalUserIdsResponse setExternalUserIds(SetExternalUserIdsRequest request) throws WebServiceException {
        SetExternalUserIdsResponse response = new SetExternalUserIdsResponse();
        try {
            String systemId = request.getSystemId();
            WsUserId wsUserId = request.getWsUserId();
            Customer customer = customerLookup.getCustomerByWsUserId(wsUserId);
            ExternalIdentifier[] externals = request.getExternal();
            IdUtils.validateSetExternalIds(systemId, externals);
            List<ExternalIdDto> dtos = IdUtils.getExternalIdDtos(externals);
            
            externalIdService.saveExternalCustomerIdsForSystem(customer, systemId, dtos);
            
        } catch (WebServiceValidationException wsve) {
            ErrorStatus errStatus = ErrorStatusUtils.getClientErrorStatus(wsve.getMessage());
            response.setErrorStatus(errStatus);
        } catch (ServiceLayerValidationException slve) {
            ErrorStatus errStatus = ErrorStatusUtils.getClientErrorStatus(slve.getMessage());
            response.setErrorStatus(errStatus);
        } catch (ServiceLayerException sle) {
            throw new WebServiceException("problem setting external user ids", sle);
        }
        return response;
    }

}
