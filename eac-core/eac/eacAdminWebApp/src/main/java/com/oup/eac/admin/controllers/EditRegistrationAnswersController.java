package com.oup.eac.admin.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.oup.eac.domain.Customer;
import com.oup.eac.domain.ProductRegistrationDefinition;
import com.oup.eac.domain.Registration;
import com.oup.eac.dto.RegistrationDto;
import com.oup.eac.service.RegistrationService;
import com.oup.eac.service.ServiceLayerException;
import com.oup.eac.web.validators.registration.RegistrationFormValidator;

@Controller
@RequestMapping("/mvc/customer/editRegistrationAnswers.htm")
public class EditRegistrationAnswersController {

	@Autowired
	private RegistrationService registrationService;
	@Autowired
	private RegistrationFormValidator registrationFormValidator;

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView showForm() {
		return new ModelAndView("editRegistrationAnswers");
	}

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView updateAnswers(@ModelAttribute("registrationDto") @Valid final RegistrationDto registrationDto, final BindingResult result,
			@RequestParam("registration_id") final String registrationId, @RequestParam("customer_id") final String customerId) throws ServiceLayerException {

		ModelMap modelMap = new ModelMap();

		if (!result.hasErrors()) {
			Registration<? extends ProductRegistrationDefinition> registration = loadRegistration(registrationId, customerId);
			registrationService.updateRegistration(registrationDto, registration.getCustomer(), registration);
			modelMap.addAttribute("statusMessageKey", "status.registration.update.success");
		}

		modelMap.addAttribute("registrationDto", registrationDto);

		return new ModelAndView("editRegistrationAnswers", modelMap);
	}

	@ModelAttribute("registrationDto")
	public RegistrationDto loadRegistrationDto(@RequestParam("registration_id") final String registrationId, @RequestParam("customer_id") final String customerId) {
		Registration<? extends ProductRegistrationDefinition> registration = loadRegistration(registrationId, customerId);
		Customer customer = registration.getCustomer();
		RegistrationDto registrationDto = registrationService.getProductPageDefinitionByRegistrationDefinition(registration.getRegistrationDefinition(),
				customer, customer.getLocale());
		return registrationDto;
	}

	private Registration<? extends ProductRegistrationDefinition> loadRegistration(final String registrationId, final String customerId) {
		Registration<? extends ProductRegistrationDefinition> registration;
		try {
			registration = registrationService.getProductRegistration(registrationId, customerId);
		} catch (ServiceLayerException e) {
			throw new IllegalArgumentException("Invalid registration id: " + registrationId);
		}
		if (registration == null) {
			throw new IllegalArgumentException("Invalid registration id: " + registrationId);
		}
		return registration;
	}

	@InitBinder("registrationDto")
	public void initBinder(final WebDataBinder binder) {
		binder.setValidator(registrationFormValidator);
	}
}
