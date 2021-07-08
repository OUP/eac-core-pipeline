package com.oup.eac.web.utils.impl;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.oup.eac.common.utils.url.QueryParser;
import com.oup.eac.web.controllers.helpers.SessionHelper;
import com.oup.eac.web.utils.UrlCustomiser;

public class ELTUrlCustomiser implements UrlCustomiser {

    private static final Logger LOG = Logger.getLogger(ELTUrlCustomiser.class);
    public static final String POST_PARAM_ELT_COUNTRY = "cc";
    public static final String POST_PARAM_ELT_LANG = "selLanguage";
    public static final String COUNTRY_GLOBAL = "global";


    @Override
    public String customiseUrl(String url, HttpServletRequest request) {
        String result = url;
        QueryParser parser = new QueryParser(url);

        parser.removeParam(POST_PARAM_ELT_COUNTRY);
        parser.removeParam(POST_PARAM_ELT_LANG);
        Locale current = SessionHelper.getLocale(request);

        String lang = current.getLanguage().toLowerCase();
        parser.replaceParameter(POST_PARAM_ELT_LANG, lang);
        String country = current.getCountry().toLowerCase();
        if (StringUtils.isNotBlank(country)) {
            parser.replaceParameter(POST_PARAM_ELT_COUNTRY, country);
        } else {
            parser.replaceParameter(POST_PARAM_ELT_COUNTRY, COUNTRY_GLOBAL);
        }
        result = parser.getUrl();
        if (LOG.isDebugEnabled()) {
            if (result.equals(url) == false) {
                LOG.debug("customiseUrl : " + result);
            }
        }
        return result;
    }
}
