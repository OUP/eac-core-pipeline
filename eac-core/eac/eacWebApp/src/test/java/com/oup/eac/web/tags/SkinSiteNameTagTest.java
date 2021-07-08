package com.oup.eac.web.tags;

import com.oup.eac.domain.UrlSkin;

public class SkinSiteNameTagTest extends BaseSkinTagTest {

    public String getDefaultValue() {
        return DEF_SITE_NAME;
    }

    public String getSessionValue() {
        return SITE_NAME;
    }

    public BaseSkinTag getTag() {
        return new SkinSiteNameTag();
    }

    @Override
    public void setNullSkinValue(UrlSkin sessionUrlSkin) {
        sessionUrlSkin.setSiteName(null);
    }

}
