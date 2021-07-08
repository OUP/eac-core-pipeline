/**
 * 
 */
package com.oup.eac.common.utils.url;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * Provides utilities for manipulating urls.
 * 
 * @author harlandd
 * 
 */
public final class URLUtils {

	private static final Logger LOGGER = Logger.getLogger(URLUtils.class);

	private static final String QUESTION_MARK = "?";
	private static final String EQUALS = "=";
	private static final String AMPERSAND = "&";
	private static final String UTF_8 = "UTF-8";

	/**
	 * Private constructor for utility class.
	 */
	private URLUtils() {
	}

	/**
	 * Encode url parts rather than as a whole.
	 * 
	 * @param urlString
	 * @return url encoded url string
	 * @throws MalformedURLException
	 * @throws UnsupportedEncodingException
	 * @throws URISyntaxException
	 */
	public static String safeEncode(final String urlString)
			throws MalformedURLException, UnsupportedEncodingException,
			URISyntaxException {
		String cleanUrl = urlString;
		if (urlString != null) {
			final URL url = new URL(URLDecoder.decode(urlString, UTF_8));
			final URI uri = new URI(url.getProtocol(), url.getHost(),
					url.getPath(), url.getQuery(), url.getRef());
			cleanUrl = uri.toASCIIString();
		}
		return cleanUrl;
	}

	/**
	 * Tests a url is relative or absolute
	 * 
	 * @param urlString
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws URISyntaxException
	 */
	public static boolean isRelativeURL(final String urlString)
			throws InvalidURLException {
		try {
			URI uri = new URI(URLDecoder.decode(urlString, UTF_8));
			return (uri.getHost() == null);
		} catch (Exception e) {
			throw new InvalidURLException(e.getMessage(), e);
		}
	}

	/**
	 * Tests a url is valid. URI finds whitespace invalid but this method allows
	 * it to pass.
	 * 
	 * @param urlString
	 * @return is url valid or not
	 */
	@SuppressWarnings("unused")
	public static void validateUrl(String urlString) throws InvalidURLException {
		if (StringUtils.isBlank(urlString)) {
			throw new InvalidURLException("Url can not be blank.");
		}
		try {
			new URI(URLDecoder.decode(urlString, UTF_8));
		} catch (UnsupportedEncodingException e) {
			throw new InvalidURLException(e.getMessage(), e);
		} catch (URISyntaxException e) {
			checkURISyntaxException(e);
		}
	}

	@SuppressWarnings("unused")
	public static final boolean isValidURL(final String uri) {
		if (StringUtils.isBlank(uri)) {
			return false;
		}
		try {
			new URL(uri);
			return true;
		} catch (MalformedURLException e) {
			LOGGER.error(e.getMessage(), e);
			return false;
		}
	}

	public static final boolean isValidURI(final String uri) {
		return isValidURI(uri, false);
	}

	@SuppressWarnings("unused")
	public static final boolean isValidURI(final String uri,
			final boolean ignoreBlank) {
		if (ignoreBlank && StringUtils.isBlank(uri)) {
			return true;
		}
		if (StringUtils.isBlank(uri) || uri.charAt(0) != '/') {
			return false;
		}
		try {
			new URI(uri);
			return true;
		} catch (URISyntaxException e) {
			LOGGER.error(e.getMessage(), e);
			return false;
		}
	}

	protected static void checkURISyntaxException(URISyntaxException e)
			throws InvalidURLException {
		int index = e.getIndex();
		if (index < 0 || index >= e.getInput().length()) {
			// Index is unknown so exception is because of something other than
			// a character.
			throw new InvalidURLException(e.getMessage(), e);
		}
		String param = e.getInput().substring(index, index + 1);
		if (StringUtils.isNotBlank(param)) {
			throw new InvalidURLException("Invalid character: " + param
					+ " found in URL: " + e.getInput(), e);
		}
	}

	/**
	 * Append parameters to a url.
	 * 
	 * @param url
	 *            The url
	 * @param params
	 *            The parameters to append
	 * @return The url with the appended parameters
	 */
	public static String appendParams(final String url,
			final Map<String, String> params) {
		StringBuilder newUrl = new StringBuilder(url);
		for (Entry<String, String> entry : params.entrySet()) {
			appendParamValue(newUrl, entry);
		}
		return newUrl.toString();
	}

	/**
	 * Appends params to a url adding ? and & where required.
	 * 
	 * @param url
	 *            The url to append param values to
	 * @param entry
	 *            The param names and values to be appended
	 */
	private static void appendParamValue(final StringBuilder url,
			final Entry<String, String> entry) {
		if (url.indexOf(QUESTION_MARK) > -1) {
			url.append(AMPERSAND);
		} else {
			url.append(QUESTION_MARK);
		}
		url.append(entry.getKey()).append(EQUALS).append(entry.getValue());
	}

	/**
	 * @param paramUrl
	 * @param whiteListUrls
	 * @return
	 */
	// method to validate url using getAllUrls
	public static final boolean checkValidUrl(final String paramUrl,
			final List<String> whiteListUrls) { 
		if (whiteListUrls != null && whiteListUrls.size() > 0) {
			for (String url : whiteListUrls) {
				if (paramUrl.startsWith(url)) {
					return true;
				}
			}
		}
		return false;
	}

}
