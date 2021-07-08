package com.oup.eac.domain;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class UrlSkinTest {

    private UrlSkin skin;

    @Before
    public void setup() {
        skin = new UrlSkin();

        skin.setId("ID");
        skin.setContactPath("CONTACT_PATH");
        skin.setSiteName("SITE_NAME");
        skin.setSkinPath("SKIN_PATH");
        skin.setUrl("URL");
        skin.setUrlCustomiserBean("BEAN_NAME");
    }

    @Test
    public void testToString() {
        Assert.assertEquals(
                "UrlSkin [url=URL, skinPath=SKIN_PATH, siteName=SITE_NAME, contactPath=CONTACT_PATH, urlCustomeriserBean=BEAN_NAME]",
                skin.toString());
    }

    @Test
    public void testEqualsAndHashCode() {
        UrlSkin skin2 = new UrlSkin();

        skin2.setId("ID");
        skin2.setContactPath("CONTACT_PATH");
        skin2.setSiteName("SITE_NAME");
        skin2.setSkinPath("SKIN_PATH");
        skin2.setUrl("URL");
        skin2.setUrlCustomiserBean("BEAN_NAME_2");

        Assert.assertFalse(skin.equals(skin2));

        UrlSkin skin3 = new UrlSkin();

        skin3.setId("ID");
        skin3.setContactPath("CONTACT_PATH");
        skin3.setSiteName("SITE_NAME");
        skin3.setSkinPath("SKIN_PATH");
        skin3.setUrl("URL");
        skin3.setUrlCustomiserBean("BEAN_NAME");

        Assert.assertEquals(skin, skin3);

        Assert.assertEquals(skin.hashCode(), skin3.hashCode());

    }
}
