package com.oup.eac.admin.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.oup.eac.admin.beans.CustomerBean;
import com.oup.eac.admin.beans.RegistrationStateBean;
import com.oup.eac.admin.validators.CustomerBeanValidator;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.ProductRegistrationDefinition;
import com.oup.eac.domain.Registration;
import com.oup.eac.domain.RollingLicenceTemplate.RollingBeginOn;
import com.oup.eac.domain.RollingLicenceTemplate.RollingUnitType;
import com.oup.eac.domain.entitlement.ProductEntitlementGroupDto;
import com.oup.eac.dto.CustomerRegistrationsDto;
import com.oup.eac.dto.LicenceDetailDto;
import com.oup.eac.dto.LicenceDto;
import com.oup.eac.dto.RollingLicenceDetailDto;
import com.oup.eac.dto.StandardConcurrentLicenceDetailDto;
import com.oup.eac.dto.UsageLicenceDetailDto;
import com.oup.eac.service.CustomerService;
import com.oup.eac.service.LicenceService;
import com.oup.eac.service.ProductService;
import com.oup.eac.service.RegistrationDefinitionService;
import com.oup.eac.service.RegistrationService;
import com.oup.eac.service.UserEntitlementsService;

@Controller
@RequestMapping("/mvc/customer/editRegistration.htm")
public class EditRegistrationController {

	@Autowired
	private ProductService productService;	
	@Autowired
	private RegistrationService registrationService;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private RegistrationDefinitionService registrationDefinitionService;
	@Autowired
	private UserEntitlementsService userEntitlementsService;
	@Autowired
	private CustomerBeanValidator customerBeanValidator;
	@Autowired
	private LicenceService licenceService;
	//private static final String FAILED_TO_ADD_REGISTRATION = "failedToAddRegistration";
	
	private static final String LICENSE_START_AND_END_DATE_FAILURE = "error.licence.endDate.beforeStartDate"   ;
	private static final String ERROR_STATUS_MESSAGE = "errorMessageKey" ;
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView showForm() {
		return new ModelAndView("registrationSummaryTile");
	}
	
	@ModelAttribute("customerBean")
	public CustomerBean loadCustomerBean(@RequestParam("registration_id") final String registrationId, 
			@RequestParam("customer_id") final String customerId) throws Exception {
		Customer customer = customerService.getCustomerByIdWs(customerId);
        CustomerRegistrationsDto registrationsDto = registrationService.getEntitlementsForCustomerRegistrations(customer, registrationId, true);
        List<ProductEntitlementGroupDto> groups = userEntitlementsService.getUserEntitlementGroups(registrationsDto, null);
		CustomerBean customerBean = new CustomerBean(customer,groups);
		return customerBean;
	}
	
    @ModelAttribute("rollingBeginOns")
    public RollingBeginOn[] getBeginOnValues() {
        return RollingBeginOn.values();
    }
    
    @ModelAttribute("rollingUnitTypes")
    public RollingUnitType[] getRollingUnitTypes() {
        return RollingUnitType.values();
    }
	
	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView updateRegistration(@ModelAttribute("customerBean") @Valid final CustomerBean customerBean, final BindingResult result,
			@RequestParam("registration_id") final String registrationId, @RequestParam("customer_id") final String customerId) 
			throws Exception {
		Customer customer = customerBean.getCustomer();
		if ( customerBean.getLicences().size() > 0 ) {
			
			List<String> errors = validateLicences(customerBean) ;
			if (errors != null && errors.size() > 0) {
				ModelAndView view = new ModelAndView("registrationSummaryTile") ;
				view.getModelMap().addAttribute(ERROR_STATUS_MESSAGE, errors) ;
				return view ;
			}
		}
		licenceService.updateLicencesForCustomer(customer, customerBean.getLicences());
		processRegistrationState(customerBean);
		return new ModelAndView("addRegistrationResult");
	}
	
	private void processRegistrationState(final CustomerBean customerBean) throws Exception {
		for (Registration<? extends ProductRegistrationDefinition> registration : customerBean.getRegistrations()) {
			RegistrationStateBean registrationStateBean = customerBean.getRegistrationState(registration.getId());
			
			if(null==registrationStateBean.getAllowDenySelected() || registrationStateBean.getAllowDenySelected().equals("") ){
				registrationStateBean.setValidate(null);
			}
			if (registrationStateBean.isActivate()) {
				registrationService.updateAllowRegistration(registration, registrationStateBean.isSendEmail(), null);
			} else if (registrationStateBean.getValidate() != null && !"".equals(String.valueOf(registrationStateBean.getValidate()))) {
				if (registrationStateBean.getValidate()) {
					registrationService.updateAllowRegistration(registration, registrationStateBean.isSendEmail(), null);
				} else {
					registrationService.updateDenyRegistration(registration, registrationStateBean.isSendEmail(), null);
				}
			}
		}
	}
	
	private List<String> validateLicences(final CustomerBean customerBean) {
		List<String> errors = new ArrayList<String>();
		for (LicenceDto licence : customerBean.getLicences()) {
			errors.addAll(validateLicenceDetailDto(licence.getLicenceDetail())) ;
			errors.addAll(validateDates(licence.getStartDate(), licence.getEndDate())) ;
			if (errors != null ){
				break ;
			}
		}
		return errors ;
	}
	
	private static List<String> validateDates(final LocalDate startDate, final LocalDate endDate) {
		List<String> errors = new ArrayList<String>();
		if (startDate != null && endDate != null) {
			if (endDate.isBefore(startDate)) {
				errors.add(LICENSE_START_AND_END_DATE_FAILURE) ;
			}
		}
		return errors;
	}
	
	private static List<String> validateLicenceDetailDto(final LicenceDetailDto licenceDetailDto) {
		List<String> errors = new ArrayList<String>();
		switch (licenceDetailDto.getLicenceType()) {
		case CONCURRENT:
			StandardConcurrentLicenceDetailDto standardConcurrentLicenceDetailDto = (StandardConcurrentLicenceDetailDto) licenceDetailDto;
			errors.addAll(validateTotalConcurrency(standardConcurrentLicenceDetailDto.getTotalConcurrency()+""));
			errors.addAll(validateTotalConcurrency(standardConcurrentLicenceDetailDto.getUserConcurrency()+""));
			break;
		case ROLLING:
			RollingLicenceDetailDto rollingLicenceDetailDto = (RollingLicenceDetailDto) licenceDetailDto;
			errors.addAll(validateTimePeriod(rollingLicenceDetailDto.getTimePeriod()+""));
			break;
		case USAGE:
			UsageLicenceDetailDto usageLicenceDetailDto = (UsageLicenceDetailDto) licenceDetailDto;
			errors.addAll(validateLicenceAllowedUsages(usageLicenceDetailDto.getAllowedUsages()+""));
			break;
		}
		return errors;
	}
	protected static List<String> validateTotalConcurrency(final String totalConcurrency) {
		List<String> errors = new ArrayList<String>();
		if (StringUtils.isBlank(totalConcurrency)) {
			errors.add("error.licence.noTotalConcurrency");
		} else {
			try {
				int concurrency = Integer.parseInt(totalConcurrency);
				if (concurrency == 0) {
					errors.add("error.licence.invalidTotalConcurrency");
				}
			} catch (NumberFormatException e) {
				errors.add("error.licence.invalidTotalConcurrency");
			}
		}
		return errors;
	}
	protected static List<String> validateTimePeriod(final String timePeriod) {
		List<String> errors = new ArrayList<String>();
		if (StringUtils.isBlank(timePeriod)) {
			errors.add("error.licence.noTimePeriod");
		} else {
			String message = "error.licence.invalidTimePeriod";
			try {
				int timePeriodInt = Integer.parseInt(timePeriod);
				if (timePeriodInt < 1 || timePeriodInt > 200000) {
					errors.add(message);
				}
			} catch (NumberFormatException e) {
				errors.add(message);
			}
		}
		return errors;
	}

	protected static List<String> validateLicenceAllowedUsages(final String licenceAllowedUsages) {
		List<String> errors = new ArrayList<String>();
		if (StringUtils.isBlank(licenceAllowedUsages)) {
			errors.add("error.licence.noAllowedUsages");
		} else {
			String message = "error.licence.invalidAllowedUsages";
			try {
				int licenceAllowedUsagesInt = Integer.parseInt(licenceAllowedUsages);
				if (licenceAllowedUsagesInt < 1 || licenceAllowedUsagesInt > 200000) {
					errors.add(message);
				}
			} catch (NumberFormatException e) {
				errors.add(message);
			}
		}
		return errors;
	}
}
