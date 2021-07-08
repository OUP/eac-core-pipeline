package com.oup.eac.web.controllers.context;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import com.oup.eac.integration.facade.ErightsFacade;
import com.oup.eac.service.DomainSkinResolverService;
import com.oup.eac.service.ProductService;
import com.oup.eac.service.RegistrationDefinitionService;
import com.oup.eac.service.WhiteListUrlService;
import com.oup.eac.web.controllers.authentication.AuthenticationWorkFlow;

public final class DirectRequestContextFactory {

	private static final String PARAM_NAME_SYSTEM_ID = "systemId";
	private static final String PARAM_NAME_TYPE_ID = "typeId";
	private static final String PARAM_NAME_PRODUCT_ID = "prodId";

	private final ProductService productService;
	private final DomainSkinResolverService domainSkinResolverService;
	private final RegistrationDefinitionService registrationDefinitionService;
	private final ErightsFacade erightsFacade;
	private final WhiteListUrlService whiteListUrlService;
	
	public DirectRequestContextFactory(final ProductService productService, 
			final DomainSkinResolverService domainSkinResolverService,
			final RegistrationDefinitionService registrationDefinitionService, 
			final ErightsFacade erightsFacade, 
			final WhiteListUrlService whiteListUrlService) {
		this.productService = productService;
		this.domainSkinResolverService = domainSkinResolverService;
		this.registrationDefinitionService = registrationDefinitionService;
		this.erightsFacade = erightsFacade;
		this.whiteListUrlService = whiteListUrlService;
	}

	/**
	 * Gets the direct request context.
	 * 
	 * @param request
	 *            the request
	 * @param requestContext
	 *            the request context
	 * @return the direct request context
	 */
	public DirectRequestContext getDirectRequestContext(final HttpServletRequest request, final RequestContext requestContext) {
		if (AuthenticationWorkFlow.isUserRequestValid(request, requestContext, whiteListUrlService)) {
			return new UrlRequestContext(productService, domainSkinResolverService, registrationDefinitionService, erightsFacade);
		}
		ParamContext prod = new ParamContext(request, PARAM_NAME_PRODUCT_ID);
		ParamContext system = new ParamContext(request, PARAM_NAME_SYSTEM_ID);
		ParamContext type = new ParamContext(request, PARAM_NAME_TYPE_ID);

		// if blank systemId or typeId parameters are supplied
		// - we don't ignore them and just assume that an internal product
		// id was being specified.

		if (prod.hasValue && system.hasValue && type.hasValue) {
			return new ExternalProductIdRequestContext(productService, domainSkinResolverService, registrationDefinitionService, erightsFacade, system.value, type.value,
					prod.value);
		} else if (prod.hasValue && !system.hasParam && !type.hasParam) {
			return new InternalProductIdRequestContext(productService, domainSkinResolverService, registrationDefinitionService, erightsFacade, prod.value);
		} else {
			return null;
		}
	}

	/**
	 * Used to hold information about a request parameter.
	 * 
	 * @author David Hay
	 * 
	 */
	private static class ParamContext {
		private final boolean hasParam;
		private final boolean hasValue;
		private final String value;

		/**
		 * Instantiates a new param context.
		 * 
		 * @param request
		 *            the request
		 * @param paramName
		 *            the param name
		 */
		ParamContext(final HttpServletRequest request, final String paramName) {
			hasParam = request.getParameterMap().containsKey(paramName);
			value = request.getParameter(paramName);
			hasValue = StringUtils.isNotBlank(value);
		}
	}
}
