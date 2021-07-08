package com.oup.eac.admin.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.oup.eac.admin.beans.AddRegistrationBean;
import com.oup.eac.domain.ActivationCodeRegistrationDefinition;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.InstantRegistrationActivation;
import com.oup.eac.domain.LinkedProductNew;
import com.oup.eac.domain.Product;
import com.oup.eac.domain.Product.ProductState;
import com.oup.eac.domain.ProductRegistrationDefinition;
import com.oup.eac.domain.RegisterableProduct;
import com.oup.eac.domain.Registration;
import com.oup.eac.domain.RegistrationActivation.ActivationStrategy;
import com.oup.eac.domain.RegistrationDefinition.RegistrationDefinitionType;
import com.oup.eac.domain.SelfRegistrationActivation;
import com.oup.eac.domain.ValidatedRegistrationActivation;
import com.oup.eac.dto.EnforceableProductDto;
import com.oup.eac.dto.RegistrationActivationDto;
import com.oup.eac.integration.facade.exceptions.ErightsException;
import com.oup.eac.service.CustomerService;
import com.oup.eac.service.ProductService;
import com.oup.eac.service.RegistrationDefinitionService;
import com.oup.eac.service.RegistrationService;
import com.oup.eac.service.ServiceLayerException;

@Controller
@RequestMapping("/mvc/customer/addRegistration.htm")
public class AddRegistrationController {

	@Autowired
	private ProductService productService;
	
	@Autowired
	private RegistrationService registrationService;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private RegistrationDefinitionService registrationDefinitionService;

	private static final List<ProductState> VALID_PRODUCT_STATES_FOR_ADD_REGISTRATION = Arrays.asList(ProductState.ACTIVE, ProductState.SUSPENDED, ProductState.RETIRED);
	private static final String FAILED_TO_ADD_REGISTRATION = "failedToAddRegistration";
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView showProductSelector() {
		return new ModelAndView("addRegistration");
	}

	@RequestMapping(method = RequestMethod.GET, params = { "product_id" })
	public ModelAndView showRegistrationDetails() {
		return new ModelAndView("addRegistrationDetails");
	}

	@RequestMapping(method = RequestMethod.POST, params = { "product_id", "customer_id" })
	public ModelAndView addRegistration(@ModelAttribute("addRegistrationBean") final AddRegistrationBean addRegistrationBean) throws Exception {
		ProductRegistrationDefinition prodRegDef = addRegistrationBean.getProductRegistrationDefinition();
		Customer customer = addRegistrationBean.getCustomer();
		String strategy = addRegistrationBean.getProduct().getActivationStrategy() ;
		if (strategy.equalsIgnoreCase(new SelfRegistrationActivation().getName())) {
			addRegistrationBean.getProduct().setActivationStrategy(ActivationStrategy.SELF.toString() );
		} else if (strategy.equalsIgnoreCase(new ValidatedRegistrationActivation().getName())) {
			addRegistrationBean.getProduct().setActivationStrategy(ActivationStrategy.VALIDATED.toString() );
		} else {
			addRegistrationBean.getProduct().setActivationStrategy(ActivationStrategy.INSTANT.toString() );
		}
		// Create the registration
		final Registration<?> registration;
        if(prodRegDef.getRegistrationDefinitionType() == RegistrationDefinitionType.ACTIVATION_CODE_REGISTRATION) {
        	registration = registrationService.saveActivationCodeRegistrationWithoutActivationCode(customer, (ActivationCodeRegistrationDefinition)prodRegDef);
        } else {
        	registration = registrationService.saveProductRegistration(customer, prodRegDef);
        }
        registration.setCompleted(true);
        
        // Activate the Registration
        Locale locale = customer.getLocale();
		String originalUrl = addRegistrationBean.getProduct().getLandingPage();
		ProductState state = null ;
		if(addRegistrationBean.getProduct().getState().equals(ProductState.ACTIVE.toString())){ 
			state = ProductState.ACTIVE ;
		} else if(addRegistrationBean.getProduct().getState().equals(ProductState.REMOVED.toString())){ 
			state = ProductState.REMOVED ;
		} else if(addRegistrationBean.getProduct().getState().equals(ProductState.RETIRED.toString())){ 
			state = ProductState.RETIRED ;
		} else if(addRegistrationBean.getProduct().getState().equals(ProductState.SUSPENDED.toString())){ 
			state = ProductState.SUSPENDED ;
		}
			
		
		if ( VALID_PRODUCT_STATES_FOR_ADD_REGISTRATION.contains(state) == false ) {
		    ModelAndView mav = new ModelAndView("addRegistrationResult");
		    mav.addObject(FAILED_TO_ADD_REGISTRATION, Boolean.TRUE);
		    return mav;
		}
		RegistrationActivationDto registrationActivationDto=new RegistrationActivationDto(customer, locale, originalUrl, registration, prodRegDef);
		registrationActivationDto.setIsAdmin(addRegistrationBean.getIsAdmin());
		registrationService.saveRegistrationActivation(registrationActivationDto,addRegistrationBean.getProduct());
				
		//ActivationStrategy activationStrategy = addRegistrationBean.getRegistrationDefinition().getRegistrationActivation().getActivationStrategy(locale);
		ActivationStrategy activationStrategy = null ;
		String actStrategy = addRegistrationBean.getProduct().getActivationStrategy() ;
		if (actStrategy.equals(ActivationStrategy.SELF.toString())) {
			activationStrategy = ActivationStrategy.SELF ;
		} else if (actStrategy.equals(ActivationStrategy.VALIDATED.toString())) {
			activationStrategy = ActivationStrategy.VALIDATED ;
		} else {
			activationStrategy = ActivationStrategy.INSTANT ;
		}
		// Validate the registration
		switch (activationStrategy) {
		case SELF:
			if (addRegistrationBean.isActivate()) {
				registrationService.updateAllowRegistration(registration, addRegistrationBean.isSendEmail(), null);
			}
			break;
		case VALIDATED:
			if (addRegistrationBean.isValidate()) {
				registrationService.updateAllowRegistration(registration, addRegistrationBean.isSendEmail(), null);
			}
			break;
		}
		
		return new ModelAndView("addRegistrationResult");
	}

	@ModelAttribute("addRegistrationBean")
	public AddRegistrationBean createAddRegistrationBean(
			@RequestParam(value = "product_id", required = false) final String productId,
			@RequestParam(value = "customer_id", required = false) final String customerId) 
					throws ServiceLayerException {

		AddRegistrationBean addRegistrationBean = new AddRegistrationBean();

		if (StringUtils.isNotBlank(productId)) {
			setRegistrationDefinition(addRegistrationBean, productId);
		}

		if (StringUtils.isNotBlank(customerId)) {
			setCustomer(addRegistrationBean, customerId);
		}

		return addRegistrationBean;
	}

	private void setRegistrationDefinition(final AddRegistrationBean addRegistrationBean, final String productId) throws ServiceLayerException {
		Product product = new RegisterableProduct();
		product.setId(productId);
		ProductRegistrationDefinition productRegistrationDefinition = registrationDefinitionService.getProductRegistrationDefinitionByProduct(product);
		//Product product = productService.getProductById(productId);

		if (productRegistrationDefinition == null) {
			throw new IllegalArgumentException("Invalid product id: " + productId);
		}
		//ProductRegistrationDefinition productRegistrationDefinition = registrationDefinitionService.getProductRegistrationDefinitionByProduct(product);
		addRegistrationBean.setProductRegistrationDefinition(productRegistrationDefinition);
		try {
			EnforceableProductDto enforceableProductDto = productService.getEnforceableProductByErightsId(product.getId());
			if (enforceableProductDto.getActivationStrategy().equals(ActivationStrategy.INSTANT.toString())) {
				enforceableProductDto.setActivationStrategy(new InstantRegistrationActivation().getName());
			} else if (enforceableProductDto.getActivationStrategy().equals(ActivationStrategy.SELF.toString())) {
				enforceableProductDto.setActivationStrategy(new SelfRegistrationActivation().getName());
			} else if (enforceableProductDto.getActivationStrategy().equals(ActivationStrategy.VALIDATED.toString())) {
				enforceableProductDto.setActivationStrategy(new ValidatedRegistrationActivation().getName());
			}
			List<LinkedProductNew> linkedProducts = new ArrayList<LinkedProductNew>() ;
			for(LinkedProductNew linkedId:enforceableProductDto.getLinkedProducts()) {
				EnforceableProductDto linkedProduct = productService.getEnforceableProductByErightsId(linkedId.getProductId()) ;
				linkedId.setName(linkedProduct.getName());
				linkedProducts.add(linkedId) ;
			}
			enforceableProductDto.setLinkedProducts(linkedProducts);
			addRegistrationBean.setProduct(enforceableProductDto);
		} catch (ErightsException e) {
			// TODO Auto-generated catch block
			throw new IllegalArgumentException(e.getMessage());
		};
	}

	private void setCustomer(final AddRegistrationBean addRegistrationBean, final String customerId) {
		Customer customer = customerService.getCustomerById(customerId);

		if (customer == null) {
			throw new IllegalArgumentException("Invalid customer id: " + customerId);
		}

		addRegistrationBean.setCustomer(customer);
	}
}
