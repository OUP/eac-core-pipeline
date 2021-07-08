package com.oup.eac.service.impl;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.junit.Test;

import com.oup.eac.common.utils.UrlMap;
import com.oup.eac.data.UrlSkinDao;
import com.oup.eac.domain.UrlSkin;

/**
 * Mock test for UrlMapServiceImpl.
 * 
 * @author David Hay
 */
public class UrlMapServiceImplMockTest {

    private static final String HOST_DOMAIN_1 = "oup1.com";
    private static final String HOST_DOMAIN_2 = "oup2.com";
    private static final String HTTP_PREFIX = "http://";
    private static final String BAD_HTTP_PREFIX = "htttp://";
    private UrlSkinDao mDao;

    private UrlMapServiceImpl sut;

    @Test
    public void testDbMiss() {

        this.mDao = EasyMock.createMock(UrlSkinDao.class);

        expect(mDao.findAll()).andReturn(null).once();

        replay(mDao);

        this.sut = new UrlMapServiceImpl(mDao);

        UrlMap<UrlSkin> urlMap = sut.getUrlMap();

        verify(mDao);

        Assert.assertEquals(0, urlMap.getSize());
    }

    @Test
    public void testDbHitWellFormed() {
        this.mDao = EasyMock.createMock(UrlSkinDao.class);

        List<UrlSkin> skins = new ArrayList<UrlSkin>();

        UrlSkin skin1 = new UrlSkin();
        skin1.setUrl(HTTP_PREFIX + HOST_DOMAIN_1);
        skin1.setSkinPath("css1");

        UrlSkin skin2 = new UrlSkin();
        skin2.setUrl(HTTP_PREFIX + HOST_DOMAIN_2);
        skin2.setSkinPath("css2");

        skins.add(skin1);
        skins.add(skin2);

        expect(mDao.findAll()).andReturn(skins).once();

        replay(mDao);

        this.sut = new UrlMapServiceImpl(mDao);

        UrlMap<UrlSkin> urlMap = this.sut.getUrlMap();

        verify(mDao);

        try {
            checkEquals("css1", urlMap.get("http://www.oup1.com"));
            checkEquals("css1", urlMap.get("http://oup1.com"));
            checkEquals("css2", urlMap.get("http://www.oup2.com"));
            checkEquals("css2", urlMap.get("http://oup2.com"));

            checkEquals("css1", urlMap.get("http://www.oup1.com/"));
            checkEquals("css1", urlMap.get("http://oup1.com/"));
            checkEquals("css2", urlMap.get("http://www.oup2.com/"));
            checkEquals("css2", urlMap.get("http://oup2.com/"));

            checkEquals("css1", urlMap.get("http://www.oup1.com/res1/"));
            checkEquals("css1", urlMap.get("http://oup1.com/res2/"));
            checkEquals("css2", urlMap.get("http://www.oup2.com/res3/"));
            checkEquals("css2", urlMap.get("http://oup2.com/res4/"));

        } catch (MalformedURLException ex) {
            ex.printStackTrace();
            Assert.fail("unexpected exception");
        }
    }

    @Test
    public void testDbHitMalformedUrlsAreIgnored() {
        this.mDao = EasyMock.createMock(UrlSkinDao.class);

        List<UrlSkin> skins = new ArrayList<UrlSkin>();

        UrlSkin skin1 = new UrlSkin();
        skin1.setUrl(BAD_HTTP_PREFIX + HOST_DOMAIN_1);
        skin1.setSkinPath("css1");

        UrlSkin skin2 = new UrlSkin();
        skin2.setUrl(HTTP_PREFIX + HOST_DOMAIN_2);
        skin2.setSkinPath("css2");

        skins.add(skin1);
        skins.add(skin2);

        expect(mDao.findAll()).andReturn(skins).once();

        replay(mDao);

        this.sut = new UrlMapServiceImpl(mDao);

        UrlMap<UrlSkin> urlMap = sut.getUrlMap();

        Assert.assertNull(urlMap);

        verify(mDao);

    }

    private void checkEquals(String css, UrlSkin skin) {
        Assert.assertEquals(css, skin.getSkinPath());
    }

}
