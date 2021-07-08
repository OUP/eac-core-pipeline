package com.oup.eac.web.controllers.context;

import javax.servlet.http.HttpServletRequest;

import com.oup.eac.integration.facade.ErightsFacade;
import com.oup.eac.service.DomainSkinResolverService;
import com.oup.eac.service.ProductService;
import com.oup.eac.service.RegistrationDefinitionService;
import com.oup.eac.web.controllers.authentication.AuthenticationWorkFlow;

public class UrlRequestContext extends BaseDirectRequestContext {

	UrlRequestContext(final ProductService productService, final DomainSkinResolverService domainSkinResolverService,
			final RegistrationDefinitionService registrationDefinitionService, final ErightsFacade erightsFacade) {
		super(productService, domainSkinResolverService, registrationDefinitionService, erightsFacade);
	}

	@Override
	public void loadRegisterableProductIfAvailable(final HttpServletRequest request, final RequestContext requestContext) {
		AuthenticationWorkFlow.initRequestContext(request, getDomainSkinResolverService(), requestContext);
		AuthenticationWorkFlow.loadRegisterableProductIfAvailable(request, getProductService(), requestContext);
	}

	@Override
	public boolean isProductRegistrationDefined(final HttpServletRequest request, final RequestContext requestContext) {
		return AuthenticationWorkFlow.isProductRegistrationDefined(request, requestContext, getRegistrationDefinitionService());
	}

	@Override
	public boolean isLandingPageMissing(final RequestContext requestContext) {
		return false;
	}

}
