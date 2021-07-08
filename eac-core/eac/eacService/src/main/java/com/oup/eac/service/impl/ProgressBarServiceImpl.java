package com.oup.eac.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.oup.eac.data.ProgressBarDao;
import com.oup.eac.domain.AccountRegistrationDefinition;
import com.oup.eac.domain.AccountValidated;
import com.oup.eac.domain.ActivationCodeRegistration;
import com.oup.eac.domain.ActivationState;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.ProductRegistration;
import com.oup.eac.domain.ProductRegistrationDefinition;
import com.oup.eac.domain.ProgressBar;
import com.oup.eac.domain.ProgressBarContext;
import com.oup.eac.domain.ProgressState;
import com.oup.eac.domain.RegisterableProduct;
import com.oup.eac.domain.Registration;
import com.oup.eac.domain.RegistrationActivation.ActivationStrategy;
import com.oup.eac.domain.RegistrationDefinition.RegistrationDefinitionType;
import com.oup.eac.domain.RegistrationState;
import com.oup.eac.domain.RegistrationType;
import com.oup.eac.domain.TokenState;
import com.oup.eac.domain.UserState;
import com.oup.eac.dto.CustomerRegistrationsDto;
import com.oup.eac.dto.EnforceableProductDto;
import com.oup.eac.integration.facade.ErightsFacade;
import com.oup.eac.integration.facade.exceptions.ErightsException;
import com.oup.eac.service.ProgressBarService;
import com.oup.eac.service.RegistrationDefinitionService;
import com.oup.eac.service.RegistrationService;
import com.oup.eac.service.ServiceLayerException;

@Service(value="progressBarService")
public class ProgressBarServiceImpl implements ProgressBarService {
	
	private static final Logger LOGGER = Logger.getLogger(ProgressBarServiceImpl.class);

	private final ProgressBarDao progressBarDao;
	
	private final RegistrationDefinitionService registrationDefinitionService;
	
	private final RegistrationService registrationService;
	
	private final ErightsFacade erightsFacade;
	
	@Autowired
	public ProgressBarServiceImpl(final ProgressBarDao progressBarDao, final RegistrationDefinitionService registrationDefinitionService,
								final RegistrationService registrationService, final ErightsFacade erightsFacade) {
		super();
		Assert.notNull(progressBarDao);
		Assert.notNull(registrationDefinitionService);
		Assert.notNull(registrationService);
		this.progressBarDao = progressBarDao;
		this.registrationDefinitionService = registrationDefinitionService;
		this.registrationService = registrationService;
		this.erightsFacade = erightsFacade;
	}
	
	@Override
	public final ProgressBar getProgressBar(final ProgressBarContext context) {
		RegisterableProduct registerableProduct = context.getRegisterableProduct();

		if(registerableProduct == null) {
			return null;
		}
		EnforceableProductDto enforceableProductDto  = null;
		
		try {
			enforceableProductDto = erightsFacade.getProduct(registerableProduct.getId());
		} catch (ErightsException e) {
			e.printStackTrace();
			return null;
		}
		ProductRegistrationDefinition definition = getProductRegistrationDefinition(registerableProduct);
		if(definition == null) {
			return null;
		}
		
		AccountRegistrationDefinition accountDefinition = getAccountRegistrationDefinition(registerableProduct);
		
		ProgressState progressState = getProgressState(context, definition, accountDefinition, enforceableProductDto);
		
		if(LOGGER.isDebugEnabled()) {
			LOGGER.debug(progressState);
		}
		
		ProgressBar progressBar = progressBarDao.getProgressBar(progressState, context.getPage());
		
		return progressBar;
	}
	
	public final ProgressState getProgressState(final ProgressBarContext context, 
												final ProductRegistrationDefinition definition, 
												final AccountRegistrationDefinition accountDefinition, 
												final EnforceableProductDto enforceableProductDto) {
		ProgressState progressState = new ProgressState();
		
		
		setUserState(context.getCustomer(), progressState);
		setAccountValidatedState(accountDefinition, progressState);
		
		if(definition.getRegistrationDefinitionType() == RegistrationDefinitionType.ACTIVATION_CODE_REGISTRATION) {
			progressState.setRegistrationType(RegistrationType.TOKEN);
			if(context.getActivationCode() == null) {
				progressState.setTokenState(TokenState.NO);
			} else {
				if(context.getCustomer() == null) {
					progressState.setTokenState(TokenState.DIRECT);
				} else {
					progressState.setTokenState(TokenState.YES);
				}
			}
		} else {
			progressState.setRegistrationType(RegistrationType.REGULAR);
			progressState.setTokenState(TokenState.NA);
		}
		if(enforceableProductDto.getActivationStrategy().toString() == ActivationStrategy.INSTANT.toString())
			progressState.setActivationStrategy(ActivationStrategy.INSTANT);
		if(enforceableProductDto.getActivationStrategy().toString() == ActivationStrategy.SELF.toString())
			progressState.setActivationStrategy(ActivationStrategy.SELF);
		if(enforceableProductDto.getActivationStrategy().toString() == ActivationStrategy.VALIDATED.toString())
			progressState.setActivationStrategy(ActivationStrategy.VALIDATED);
		
		if(context.getRegistrationId() == null) {
			progressState.setActivationState(ActivationState.UNKNOWN);
			progressState.setRegistrationState(RegistrationState.UNKNOWN);
			return progressState;
		}
		CustomerRegistrationsDto custReg = null;
		Registration<?> registration = null; 
		try {
			custReg = registrationService.getEntitlementsForCustomerRegistrations(context.getCustomer(),null, true);
		} catch (ServiceLayerException e) {
			e.printStackTrace();
		}
		List<Registration<? extends ProductRegistrationDefinition>> registrations = custReg.getRegistrations();
		for(Registration<?> reg : registrations){
			if(reg.getId() == context.getRegistrationId().toString())
				registration = reg;
		}
		
			if(enforceableProductDto.getRegistrationDefinitionType().equals(RegistrationDefinitionType.ACTIVATION_CODE_REGISTRATION))
				registration = new ActivationCodeRegistration();
			else 
				registration = new ProductRegistration();
		
		if(registration.getRegistrationType() == Registration.RegistrationType.ACTIVATION_CODE) {
			ActivationCodeRegistration activationCodeRegistration = (ActivationCodeRegistration)registration;
			if(activationCodeRegistration.getActivationCode() == null) {
				progressState.setTokenState(TokenState.NO);
			} else {
				progressState.setTokenState(TokenState.YES);
			}
		}
		
		if(registration.isCompleted()) {
			progressState.setRegistrationState(RegistrationState.COMPLETE);
			if(registration.isAwaitingValidation()) {
				if(registration.isActivated()) {
					progressState.setActivationState(ActivationState.AWAITING);
				} else {
					if(registration.isDenied()) {
						progressState.setActivationState(ActivationState.DENIED);
					} else {
						progressState.setActivationState(ActivationState.AWAITING);
					}					
				}
			} else {
				progressState.setActivationState(ActivationState.UNKNOWN);
			}
		} else {
			progressState.setActivationState(ActivationState.NONE);
			progressState.setRegistrationState(RegistrationState.NONE);
		}	
		
		return progressState;
	}

    private void setUserState(final Customer customer, final ProgressState progressState) {
        boolean isLoggedIn = customer != null && customer.getCreatedDate() != null;
		if(isLoggedIn) {
			progressState.setUserState(UserState.LOGGEDIN);
		} else {
			progressState.setUserState(UserState.NEW);
		}
    }
    
    private void setAccountValidatedState(final AccountRegistrationDefinition accountDefinition, final ProgressState progressState) {
        if(accountDefinition.isValidationRequired()) {
            progressState.setAccountValidated(AccountValidated.VALIDATED);
        } else {
            progressState.setAccountValidated(AccountValidated.NON_VALIDATED);
        }
    }    
	
    public final ProductRegistrationDefinition getProductRegistrationDefinition(final RegisterableProduct registerableProduct) {
    	try {
    		return registrationDefinitionService.getProductRegistrationDefinitionByProduct(registerableProduct);
    	} catch (ServiceLayerException e) {
    		return null;
		}
    }
    
    public final AccountRegistrationDefinition getAccountRegistrationDefinition(final RegisterableProduct registerableProduct) {
        try {
            return registrationDefinitionService.getAccountRegistrationDefinitionByProduct(registerableProduct);
        } catch (ServiceLayerException e) {
            return null;
        }
    }
    
   
}
