package com.oup.eac.ws.v2.service;

import java.util.Set;

import org.springframework.security.access.AccessDeniedException;

import com.oup.eac.domain.ExternalSystem;
import com.oup.eac.integration.facade.exceptions.DivisionNotFoundException;
import com.oup.eac.integration.facade.exceptions.ErightsException;
import com.oup.eac.ws.v2.ex.WebServiceValidationException;

public interface WsExternalSystemLookup {
    public ExternalSystem lookupExternalSystemByName(String externalSystemName) throws WebServiceValidationException, AccessDeniedException, ErightsException;

    public void validateExternalSystem(String systemId) throws WebServiceValidationException, AccessDeniedException,  ErightsException;
    
    public void validateMultipleExternalSystem(Set<String> systemIdSet) throws WebServiceValidationException, AccessDeniedException, ErightsException;
}
