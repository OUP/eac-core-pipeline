package com.oup.eac.service.impl;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;

import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.oup.eac.data.ActivationCodeDao;
import com.oup.eac.data.AnswerDao;
import com.oup.eac.data.LinkedRegistrationDao;
import com.oup.eac.data.RegistrationDao;
import com.oup.eac.data.RegistrationDefinitionDao;
import com.oup.eac.domain.Customer;
import com.oup.eac.dto.LicenceDto;
import com.oup.eac.dto.licence.LicenceDescriptionGeneratorSource;
import com.oup.eac.integration.facade.ErightsFacade;
import com.oup.eac.service.ActivationCodeService;
import com.oup.eac.service.EmailService;
import com.oup.eac.service.LicenceService;
import com.oup.eac.service.PageDefinitionService;
import com.oup.eac.service.ProductService;
import com.oup.eac.service.RegistrationDefinitionService;
import com.oup.eac.service.RegistrationService;
import com.oup.eac.service.exceptions.ServiceLayerValidationException;

@Service(value = "asyncEmailServiceForRedeemProductGroup")
public class AsyncEmailServiceForRedeemProductGroup implements Runnable{
	private static final Logger LOG = Logger.getLogger(AsyncEmailServiceForRedeemProductGroup.class);
	
	private static RegistrationService registrationServiceImpl ;
	private List<LicenceDto> licenses ;
	private Customer customer ;
	private Locale locale ;
	@Autowired
	public AsyncEmailServiceForRedeemProductGroup(RegistrationService registrationServiceImpl) {
		this.registrationServiceImpl = registrationServiceImpl;		
	}
	public AsyncEmailServiceForRedeemProductGroup(
			List<LicenceDto> licenses,
			Customer customer,
			Locale locale) {
		this.customer = customer ;
		this.locale = locale ;
		this.licenses = licenses ;
	}
	
	public AsyncEmailServiceForRedeemProductGroup() {
		super();
		
	}

	

	@Override
	public void run() {
		try {
			registrationServiceImpl.sendRedeemEmailForGroup(licenses,
					customer, locale);
		} catch (ServiceLayerValidationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LOG.error(e);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LOG.error(e);
		}
		
	}
}
