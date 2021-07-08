package com.oup.eac.web.locale;

import java.util.Locale;
import java.util.Map;

public interface LocaleDropDownSource {

    /**
     * Gets the Locale drop down with Locale specific descriptions.
     *
     * @param displayLocale the display locale
     * @return the locale drop down
     */
    Map<String, String> getLocaleDropDown(Locale displayLocale, Locale usersLocale);
}
