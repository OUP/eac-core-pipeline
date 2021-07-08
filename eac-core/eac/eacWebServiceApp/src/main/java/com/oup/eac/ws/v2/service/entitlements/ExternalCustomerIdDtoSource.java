package com.oup.eac.ws.v2.service.entitlements;

import java.util.List;

import com.oup.eac.domain.ActivationCodeRegistration;
import com.oup.eac.dto.ExternalCustomerIdDto;

public interface ExternalCustomerIdDtoSource {

    List<ExternalCustomerIdDto> getExternalCustomersId(String systemId, List<ActivationCodeRegistration> registrations);
    
}
