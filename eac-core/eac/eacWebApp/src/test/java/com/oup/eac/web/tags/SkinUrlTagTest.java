package com.oup.eac.web.tags;

import com.oup.eac.domain.UrlSkin;

public class SkinUrlTagTest extends BaseSkinTagTest {

    public String getDefaultValue() {
        return DEF_URL;
    }

    public String getSessionValue() {
        return URL;
    }

    public BaseSkinTag getTag() {
        return new SkinUrlTag();
    }

    @Override
    public void setNullSkinValue(UrlSkin sessionUrlSkin) {
        sessionUrlSkin.setUrl(null);
    }

}
