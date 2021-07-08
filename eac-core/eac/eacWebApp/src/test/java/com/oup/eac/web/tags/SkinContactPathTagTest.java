package com.oup.eac.web.tags;

import com.oup.eac.domain.UrlSkin;

public class SkinContactPathTagTest extends BaseSkinTagTest {

    public String getDefaultValue() {
        return DEF_CONTACT_PATH;
    }

    public String getSessionValue() {
        return CONTACT_PATH;
    }

    public BaseSkinTag getTag() {
        return new SkinContactUsTag();
    }

    @Override
    public void setNullSkinValue(UrlSkin sessionUrlSkin) {
        sessionUrlSkin.setContactPath(null);
    }

}
