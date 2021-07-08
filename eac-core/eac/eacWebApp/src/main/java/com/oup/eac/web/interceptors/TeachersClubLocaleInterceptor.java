package com.oup.eac.web.interceptors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.oup.eac.common.utils.lang.LocaleUtils;
import com.oup.eac.web.controllers.helpers.RequestHelper;
import com.oup.eac.web.controllers.helpers.SessionHelper;

/**
 * The Class TeachersClubLocaleInterceptor.
 */
public class TeachersClubLocaleInterceptor extends HandlerInterceptorAdapter {

    /** The Constant LOG. */
    private static final Logger LOG = Logger.getLogger(TeachersClubLocaleInterceptor.class);

    /** The Constant DEFAULT_PARAM_NAME_LANG. */
    public static final String DEFAULT_PARAM_NAME_LANG = "selLanguage";

    /** The Constant DEFAULT_PARAM_NAME_COUNTRY. */
    public static final String DEFAULT_PARAM_NAME_COUNTRY = "cc";

    /** The Constant DEFAULT_COUNTRY_CODES_TO_IGNORE. */
    public static final List<String> DEFAULT_COUNTRY_CODES_TO_IGNORE = Arrays.asList("global");

    /** The param name lang. */
    private String paramNameLang = DEFAULT_PARAM_NAME_LANG;

    /** The param name country. */
    private String paramNameCountry = DEFAULT_PARAM_NAME_COUNTRY;

    /** The country codes to ignore. */
    private List<String> countryCodesToIgnore = DEFAULT_COUNTRY_CODES_TO_IGNORE;

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {
        if (RequestHelper.isGetRequest(request)) {
            String country = request.getParameter(paramNameCountry);
            String lang = request.getParameter(paramNameLang);
            if (isLanguageValid(lang)) {
                Locale locale;
                String locLang = lang.toLowerCase();
                if (isCountryValid(country)) {
                    locale = new Locale(locLang, country.toUpperCase());
                } else {
                    locale = new Locale(locLang);
                }
                boolean isValid = LocaleUtils.isValid(locale);
                if (isValid) {
                    SessionHelper.setLocale(request, response, locale);
                } else {
                    String msg = String.format("The locale [%s] is not valid", locale.toString());
                    LOG.warn(msg);
                }
            }
        }
        return true;
    }

    /**
     * Checks if is country valid.
     * 
     * @param country
     *            the country
     * @return true, if is country valid
     */
    private boolean isCountryValid(final String country) {
        if (country == null) {
            return false;
        }
        return (!this.countryCodesToIgnore.contains(country.toLowerCase())) && isValid(country);
    }

    /**
     * Checks if is language valid.
     * 
     * @param lang
     *            the lang
     * @return true, if is language valid
     */
    private boolean isLanguageValid(final String lang) {
        return isValid(lang);
    }

    /**
     * Checks if is valid.
     * 
     * @param value
     *            the value
     * @return true, if is valid
     */
    private boolean isValid(final String value) {
        return StringUtils.isNotBlank(value) && value.length() == 2;
    }

    /**
     * Gets the param name lang.
     * 
     * @return the param name lang
     */
    public final String getParamNameLang() {
        return paramNameLang;
    }

    /**
     * Sets the param name lang.
     * 
     * @param paramNameLangP
     *            the new param name lang
     */
    public final void setParamNameLang(final String paramNameLangP) {
        this.paramNameLang = paramNameLangP;
    }

    /**
     * Gets the param name country.
     * 
     * @return the param name country
     */
    public final String getParamNameCountry() {
        return paramNameCountry;
    }

    /**
     * Sets the param name country.
     * 
     * @param paramNameCountryP
     *            the new param name country
     */
    public final void setParamNameCountry(final String paramNameCountryP) {
        this.paramNameCountry = paramNameCountryP;
    }

    /**
     * Gets the country codes to ignore.
     * 
     * @return the country codes to ignore
     */
    public final List<String> getCountryCodesToIgnore() {
        return countryCodesToIgnore;
    }

    /**
     * Sets the country codes to ignore.
     * 
     * @param countryCodesToIgnoreP
     *            the new country codes to ignore
     */
    public final void setCountryCodesToIgnore(final List<String> countryCodesToIgnoreP) {
        this.countryCodesToIgnore = new ArrayList<String>();
        if (countryCodesToIgnoreP != null) {
            for (String code : countryCodesToIgnoreP) {
                if (code != null) {
                    countryCodesToIgnore.add(code.toLowerCase());
                }
            }
        }
    }

}
