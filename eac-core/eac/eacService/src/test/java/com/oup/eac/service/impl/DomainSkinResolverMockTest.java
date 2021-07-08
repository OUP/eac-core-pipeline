package com.oup.eac.service.impl;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import com.oup.eac.common.utils.UrlMap;
import com.oup.eac.domain.UrlSkin;
import com.oup.eac.service.UrlMapService;

/**
 * Mock test for DomainSkinResolverServiceImpl.
 * 
 * @author David Hay
 */
public class DomainSkinResolverMockTest {

    private static final String HTTP_PREFIX = "http://";
    private static final String HOST_DOMAIN = "oup.com";
    private static final String PATH1 = "/path1";
    private static final String PATH2 = "/path2";
    private DomainSkinResolverServiceImpl sut;
    private UrlMapService mUrlMapService;

    @Before
    public void setup() {
        this.mUrlMapService = EasyMock.createMock(UrlMapService.class);
        this.sut = new DomainSkinResolverServiceImpl(this.mUrlMapService);
        
        Logger.getLogger(sut.getClass()).setLevel(Level.INFO);
        sut.init();
        Logger.getLogger(sut.getClass()).setLevel(Level.DEBUG);
        sut.init();

    }

    @Test
    public void testMapSourceFound() throws MalformedURLException {

        Map<String, UrlSkin> map = new HashMap<String, UrlSkin>();
        map.put(HTTP_PREFIX + HOST_DOMAIN + PATH1, getSkin("css1"));
        map.put(HTTP_PREFIX + HOST_DOMAIN + PATH2, getSkin("css2"));
        UrlMap<UrlSkin> urlMap = new UrlMap<UrlSkin>(map);

        expect(mUrlMapService.getUrlMap()).andReturn(urlMap).once();

        replay(mUrlMapService);

        UrlSkin skin = this.sut.getSkinFromDomain("http://oup.com/path1");

        verify(mUrlMapService);
        checkEquals("css1", skin);

    }

    /**
     * Check equals.
     * 
     * @param cssPath
     *            the css path
     * @param skin
     *            the skin
     */
    private void checkEquals(final String cssPath, final UrlSkin skin) {
        Assert.assertEquals(cssPath, skin.getSkinPath());
    }

    /**
     * Gets the skin.
     * 
     * @param cssPath
     *            the css path
     * @return the skin
     */
    private UrlSkin getSkin(final String cssPath) {
        UrlSkin result = new UrlSkin();
        result.setSkinPath(cssPath);
        return result;
    }

    @Test
    public void testMapSourceNotFound1() {

        expect(mUrlMapService.getUrlMap()).andReturn(null).once();

        replay(mUrlMapService);

        UrlSkin skin = this.sut.getSkinFromDomain("http://oup.com/path1");

        verify(mUrlMapService);
        Assert.assertEquals(null, skin);

    }
    
    @Test
    public void testMapSourceBadURL1() throws MalformedURLException {

        Map<String, UrlSkin> map = new HashMap<String, UrlSkin>();
        map.put(HTTP_PREFIX + HOST_DOMAIN + PATH1, getSkin("css1"));
        map.put(HTTP_PREFIX + HOST_DOMAIN + PATH2, getSkin("css2"));
        UrlMap<UrlSkin> urlMap = new UrlMap<UrlSkin>(map);

        expect(mUrlMapService.getUrlMap()).andReturn(urlMap).once();
        
        replay(mUrlMapService);

        UrlSkin skin = this.sut.getSkinFromDomain("htttp://oup.com/path1");

        verify(mUrlMapService);
        Assert.assertEquals(null, skin);

    }

    @Test
    public void testMapSourceBadURL2() throws MalformedURLException {

        Map<String, UrlSkin> map = new HashMap<String, UrlSkin>();
        map.put(HTTP_PREFIX + HOST_DOMAIN + PATH1, getSkin("css1"));
        map.put(HTTP_PREFIX + HOST_DOMAIN + PATH2, getSkin("css2"));
        UrlMap<UrlSkin> urlMap = new UrlMap<UrlSkin>(map);

        expect(mUrlMapService.getUrlMap()).andReturn(urlMap).once();
        
        replay(mUrlMapService);

        UrlSkin skin = this.sut.getSkinFromDomain(null);

        verify(mUrlMapService);
        Assert.assertEquals(null, skin);

    }

}
