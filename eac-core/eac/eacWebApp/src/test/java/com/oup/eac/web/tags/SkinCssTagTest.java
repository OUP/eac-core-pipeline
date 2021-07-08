package com.oup.eac.web.tags;

import com.oup.eac.domain.UrlSkin;


public class SkinCssTagTest extends BaseSkinTagTest {

    public String getDefaultValue() {
        return DEF_SKIN_PATH;
    }

    public String getSessionValue() {
        return SKIN_PATH;
    }

    @Override
    public BaseSkinTag getTag() {
        return new SkinCssTag();
    }
    @Override
    public void setNullSkinValue(UrlSkin sessionUrlSkin) {
        sessionUrlSkin.setSkinPath(null);
    }
}
