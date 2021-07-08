package com.oup.eac.web.tags;

import com.oup.eac.domain.UrlSkin;

public class LogoutLandingUrlTag extends CurrentProductHomeUrlTag {

    private static final String DEFAULT_LOGGED_OUT_VIEW = "loggedOut.htm";

    @Override
    public String getDefaultValue(UrlSkin defaultUrlSkin) {
        return DEFAULT_LOGGED_OUT_VIEW;
    }

}
