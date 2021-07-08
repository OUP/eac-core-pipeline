package com.oup.eac.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.oup.eac.data.RegistrationActivationDao;
import com.oup.eac.domain.InstantRegistrationActivation;
import com.oup.eac.domain.RegistrationActivation;
import com.oup.eac.domain.RegistrationActivation.ActivationStrategy;
import com.oup.eac.domain.SelfRegistrationActivation;
import com.oup.eac.domain.ValidatedRegistrationActivation;
import com.oup.eac.integration.facade.ErightsFacade;
import com.oup.eac.integration.facade.exceptions.ErightsException;
import com.oup.eac.service.RegistrationActivationService;

@Service("registrationActivationService")
public class RegistrationActivationServiceImpl implements RegistrationActivationService {
	private final ErightsFacade erightsFacade;
	private final RegistrationActivationDao registrationActivationDao;
	
	@Autowired
	public RegistrationActivationServiceImpl(final RegistrationActivationDao registrationActivationDao,final ErightsFacade erightsFacade) {
		this.registrationActivationDao = registrationActivationDao;
		this.erightsFacade = erightsFacade ;
	}

	@Override
	public List<RegistrationActivation> getAllRegistrationActivationsOrderedByType() throws AccessDeniedException, ErightsException {
		List<RegistrationActivation> registrationActivations = new ArrayList<RegistrationActivation>() ;
		InstantRegistrationActivation instantRegistrationActivation = new InstantRegistrationActivation() ;
		instantRegistrationActivation.setId(ActivationStrategy.INSTANT.toString());
		SelfRegistrationActivation selfRegistrationActivation = new SelfRegistrationActivation() ;
		selfRegistrationActivation.setId(ActivationStrategy.SELF.toString());
		registrationActivations.add(selfRegistrationActivation) ;
		registrationActivations.add(instantRegistrationActivation);
		List<String> validatorEmails = erightsFacade.getAllValidatorEmails();
		for (String validatorEmail : validatorEmails) {
			ValidatedRegistrationActivation validatedRegistrationActivation = new ValidatedRegistrationActivation() ;
			validatedRegistrationActivation.setId(validatorEmail);
			validatedRegistrationActivation.setValidatorEmail(validatorEmail);
			registrationActivations.add(validatedRegistrationActivation) ;
		}
		return registrationActivations ;
		//product de-duplication
		//return registrationActivationDao.findAllRegistrationActivationsOrderedByType();
	}

	@Override
	public void saveOrUpdateRegistrationActivation(final RegistrationActivation registrationActivation) {
		this.registrationActivationDao.saveOrUpdate(registrationActivation);
	}

	
}
