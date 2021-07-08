package com.oup.eac.web.controllers.registration;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.oup.eac.domain.Customer;
import com.oup.eac.domain.ProductRegistrationDefinition;
import com.oup.eac.domain.Registration;
import com.oup.eac.dto.EnforceableProductDto;
import com.oup.eac.dto.TokenDto;
import com.oup.eac.service.CustomerService;
import com.oup.eac.service.DomainSkinResolverService;
import com.oup.eac.service.ProductService;
import com.oup.eac.service.RegistrationDefinitionService;
import com.oup.eac.service.RegistrationService;
import com.oup.eac.service.WhiteListUrlService;
import com.oup.eac.web.controllers.authentication.AuthenticationWorkFlow;
import com.oup.eac.web.controllers.context.RequestContext;
import com.oup.eac.web.controllers.helpers.SessionHelper;

@Controller
public class ResendConfirmationEmailController {

	private final CustomerService customerService;
	private final ProductService productService;
	private final RegistrationService registrationService;
	private final RegistrationDefinitionService registrationDefinitionService;
	private final DomainSkinResolverService domainSkinResolverService;
	private final WhiteListUrlService whiteListUrlService;

	@Autowired
	public ResendConfirmationEmailController(final CustomerService customerService, 
			final ProductService productService,
			final RegistrationService registrationService, 
			final RegistrationDefinitionService registrationDefinitionService,
			final DomainSkinResolverService domainSkinResolverService, 
			final WhiteListUrlService whiteListUrlService) {
		this.customerService = customerService;
		this.productService = productService;
		this.registrationService = registrationService;
		this.registrationDefinitionService = registrationDefinitionService;
		this.domainSkinResolverService = domainSkinResolverService;
		this.whiteListUrlService = whiteListUrlService;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/resendConfirmationEmail.htm")
	public void resendEmail(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
		RequestContext requestContext = new RequestContext();
		if (canResend(request, requestContext)) {
			Customer customer = requestContext.getCustomer();
			ProductRegistrationDefinition registrationDefinition = requestContext.getProductRegistrationDefinition();
			Registration<ProductRegistrationDefinition> registration = requestContext.getProductRegistration();
			String originalUrl = SessionHelper.getForwardUrl(request.getSession(), false);
			Locale locale = SessionHelper.getLocale(request);
			EnforceableProductDto enfoProduct = new EnforceableProductDto();
			enfoProduct.setName(registrationDefinition.getProduct().getProductName());			
			
			TokenDto token = new TokenDto(registration.getId(), customer.getId(), originalUrl,customer.getUsername() ,enfoProduct.getName());

			registrationService.emailSelfLicenceActivationNew(customer, enfoProduct, locale, token);
		}
		IOUtils.closeQuietly(response.getOutputStream());  // The client doesn't care about the content of the response
	}

	private boolean canResend(final HttpServletRequest request, final RequestContext requestContext) {
		final boolean validRequest = AuthenticationWorkFlow.isUserRequestValid(
				request, requestContext, whiteListUrlService);
		AuthenticationWorkFlow.initRequestContext(request, domainSkinResolverService, requestContext);
		AuthenticationWorkFlow.loadRegisterableProductIfAvailable(request, productService, requestContext);
		final boolean loggedIn = AuthenticationWorkFlow.isPrimaryDomainSessionAvailable(request, requestContext)
				&& AuthenticationWorkFlow.isPrimaryDomainSessionValid(request, customerService, requestContext);
		final boolean productRegistrationDefined = AuthenticationWorkFlow.isProductRegistrationDefined(request, requestContext, registrationDefinitionService);
		final boolean customerRegistered = AuthenticationWorkFlow.isCustomerRegistered(registrationService, request, requestContext);

		return validRequest && loggedIn && productRegistrationDefined && customerRegistered;
	}
}
