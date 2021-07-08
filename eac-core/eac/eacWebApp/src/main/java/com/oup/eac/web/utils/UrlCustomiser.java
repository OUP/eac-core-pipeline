package com.oup.eac.web.utils;

import javax.servlet.http.HttpServletRequest;

/**
 * The Interface UrlCustomiser.
 */
public interface UrlCustomiser {

	/**
	 * Customerise url.
	 *
	 * @param url the url
	 * @param request the request
	 * @return the string
	 */
	public String customiseUrl(String url, HttpServletRequest request);
		
}
