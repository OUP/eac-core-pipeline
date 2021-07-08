package com.oup.eac.web.controllers.context;


import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.oup.eac.domain.Product;
import com.oup.eac.domain.RegisterableProduct;
import com.oup.eac.integration.facade.ErightsFacade;
import com.oup.eac.integration.facade.exceptions.ErightsException;
import com.oup.eac.integration.facade.exceptions.ProductNotFoundException;
import com.oup.eac.service.DomainSkinResolverService;
import com.oup.eac.service.ProductService;
import com.oup.eac.service.RegistrationDefinitionService;
import com.oup.eac.web.controllers.authentication.AuthenticationWorkFlow;

public class InternalProductIdRequestContext extends BaseDirectRequestContext {

	private static final Logger LOG = Logger.getLogger(InternalProductIdRequestContext.class);

	private final String prodId;

	InternalProductIdRequestContext(final ProductService productService, final DomainSkinResolverService domainSkinResolverService,
			final RegistrationDefinitionService registrationDefinitionService, final ErightsFacade erightsFacade, final String prodIdP) {
		super(productService, domainSkinResolverService, registrationDefinitionService, erightsFacade);
		prodId = prodIdP;
	}
	
	@Override
	public void loadRegisterableProductIfAvailable(final HttpServletRequest request, final RequestContext requestContext) {
		try {
			Product product = new RegisterableProduct(); 
			product.setId(prodId);
					//getProductService().getProductById(prodId);
			processProduct(product, null, request, requestContext);
		} catch (ProductNotFoundException e) {
			if (LOG.isInfoEnabled()) {
				String msg = String.format("Failed to find product with id [%s]", prodId);
				LOG.info(msg, e);
			}
			e.printStackTrace();
		} catch (ErightsException e) {
			if (LOG.isInfoEnabled()) {
				String msg = String.format("Failed to find product with id [%s]", prodId);
				LOG.info(msg, e);
			}
			e.printStackTrace();
		}
	}

	@Override
	public boolean isProductRegistrationDefined(final HttpServletRequest request, final RequestContext requestContext) {
		String msg = String.format("Could not load product registration definition for product with id: [%s]", prodId);
		return AuthenticationWorkFlow.isProductRegistrationDefined(request, requestContext, getRegistrationDefinitionService(), msg);
	}

	@Override
	public boolean isLandingPageMissing(final RequestContext requestContext) {
		return isNoLandingPage(requestContext);
	}

}
