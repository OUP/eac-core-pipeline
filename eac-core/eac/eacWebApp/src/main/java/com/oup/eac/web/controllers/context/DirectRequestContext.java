package com.oup.eac.web.controllers.context;

import javax.servlet.http.HttpServletRequest;

public interface DirectRequestContext {

	/**
	 * Load registerable product if available.
	 * 
	 * @param request
	 *            the request
	 * @param requestContext
	 *            the request context
	 */
	void loadRegisterableProductIfAvailable(HttpServletRequest request, RequestContext requestContext);

	/**
	 * Checks if is product registration defined.
	 * 
	 * @param request
	 *            the request
	 * @param requestContext
	 *            the request context
	 * @return true, if is product registration defined
	 */
	boolean isProductRegistrationDefined(HttpServletRequest request, RequestContext requestContext);

	/**
	 * Checks if is landing page missing.
	 * 
	 * @param requestContext
	 *            the request context
	 * @return true, if is landing page missing
	 */
	boolean isLandingPageMissing(RequestContext requestContext);
}
