package com.oup.eac.web.tags;

import javax.servlet.http.HttpServletRequest;

import com.oup.eac.domain.UrlSkin;
import com.oup.eac.domain.UrlSkinInfo;

public class SkinUrlTag extends BaseSkinTag {

    @Override
    public final String getValue(final HttpServletRequest request) {
        String result = null;
        UrlSkinInfo skin = getUrlSkin(request);
        if (skin != null) {
            result = skin.getUrl();
        }
        return result;
    }

    @Override
    public final String getDefaultValue(final UrlSkin defaultUrlSkin) {
        return defaultUrlSkin.getUrl();
    }

}
