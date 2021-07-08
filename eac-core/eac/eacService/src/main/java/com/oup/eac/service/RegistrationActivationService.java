package com.oup.eac.service;

import java.util.List;

import org.springframework.security.access.AccessDeniedException;

import com.oup.eac.domain.RegistrationActivation;
import com.oup.eac.integration.facade.exceptions.ErightsException;

public interface RegistrationActivationService {

	List<RegistrationActivation> getAllRegistrationActivationsOrderedByType() throws AccessDeniedException, ErightsException;
	
	void saveOrUpdateRegistrationActivation(final RegistrationActivation registrationActivation);
}
