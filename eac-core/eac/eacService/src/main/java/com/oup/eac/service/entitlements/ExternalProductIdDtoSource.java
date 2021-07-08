package com.oup.eac.service.entitlements;

import java.util.List;

import com.oup.eac.domain.Product;
import com.oup.eac.domain.ProductRegistrationDefinition;
import com.oup.eac.domain.Registration;
import com.oup.eac.dto.ExternalProductIdDto;

public interface ExternalProductIdDtoSource {

    public ExternalProductIdDto getExternalProductId(List<Registration<? extends ProductRegistrationDefinition>> registrations, String systemId);

    public ExternalProductIdDto getExternalProductId(String systemId, List<Product> products);
    
    public ExternalProductIdDto getExternalProductId(List<Product> products);
    
    public ExternalProductIdDto getFullExternalProductId(List<Registration<? extends ProductRegistrationDefinition>> registrations);
    
}
