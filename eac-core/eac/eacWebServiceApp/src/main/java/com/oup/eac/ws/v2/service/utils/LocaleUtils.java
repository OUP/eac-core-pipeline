package com.oup.eac.ws.v2.service.utils;

import java.util.Locale;

import org.apache.commons.lang.StringUtils;

import com.oup.eac.ws.v2.binding.common.LocaleType;

public abstract class LocaleUtils {

    public static Locale getLocale(final LocaleType localeInfo) {
        if (localeInfo == null) {
            return null;
        }

        String lang = localeInfo.getLanguage();
        String country = localeInfo.getCountry();
        String variant = localeInfo.getVariant();

        final Locale result;
        if (country == null) {
            result = new Locale(lang);
        } else {
            if (variant == null) {
                result = new Locale(lang, country);
            } else {
                result = new Locale(lang, country, variant);
            }
        }
        return result;
    }

    public static LocaleType getLocaleType(final Locale locale) {
        if (locale == null) {
            return null;
        }
        final LocaleType result = new LocaleType();
        result.setLanguage(getValue(locale.getLanguage()));
        result.setCountry(getValue(locale.getCountry()));
        result.setVariant(getValue(locale.getVariant()));
        return result;
    }

    private static String getValue(String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        } else {
            return value;
        }
    }

}
