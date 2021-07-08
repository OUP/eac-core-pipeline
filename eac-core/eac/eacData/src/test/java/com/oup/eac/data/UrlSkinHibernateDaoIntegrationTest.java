package com.oup.eac.data;

import static org.junit.Assert.assertEquals;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.oup.eac.domain.UrlSkin;

/**
 * Integration test of UrlSkinHibernateDao
 * 
 * @see com.oup.eac.data.impl.hibernate.UrlSkinHibernateDao 
 * 
 * @author David Hay 
 */
public class UrlSkinHibernateDaoIntegrationTest extends AbstractDBTest {

    @Autowired
    private UrlSkinDao urlSkinDao;

    private UrlSkin skin1;
    private UrlSkin skin2;
    private UrlSkin skin3;
    private UrlSkin skin4;

    /**
     * @throws Exception
     *             Sets up data ready for test.
     */
    @Before
    public final void setUp() throws Exception {
        URL url1 = new URL("http://www.gooogle.co.uk/path1");
        URL url2 = new URL("http://www.gooogle.co.uk/path2");
        URL url3 = new URL("http://oup.com/path1");
        URL url4 = new URL("http://oup.com/path2");
        
        String skinPath1 = "css1";
        String skinPath2 = "css2";
        String skinPath3 = "css3";
        String skinPath4 = "css4";
        
        skin1 = getSampleDataCreator().createUrlSkin(url1, skinPath1);
        skin2 = getSampleDataCreator().createUrlSkin(url2, skinPath2);
        skin3 = getSampleDataCreator().createUrlSkin(url3, skinPath3);
        skin4 = getSampleDataCreator().createUrlSkin(url4, skinPath4);
        loadAllDataSets();
    }

    /**
     * Test getting an answer and comparing it to the expected answer.
     * 
     * @throws Exception
     *             the exception
     */
    @Test
    public final void testGetUrlSkin() throws Exception {
        UrlSkin resSkin = urlSkinDao.getEntity(skin1.getId());
        checkSame(skin1,resSkin);
    }

    private void checkSame(UrlSkin skin1, UrlSkin skin2){
        assertEquals(skin1.getUrl(), skin2.getUrl());
        assertEquals(skin1.getSkinPath(), skin2.getSkinPath());        
        assertEquals(skin1.getId(), skin2.getId());
        assertEquals(skin1.getVersion(), skin2.getVersion());
    }
    /**
     * Test that we get a null response when we try and look up and entry that does not exist.
     * 
     * @throws Exception
     *             the exception
     */
    @Test
    public final void testUrlSkinNotFound() {
        UrlSkin resSkin = urlSkinDao.getEntity("999");
        Assert.assertNull(resSkin);
    }

    @Test
    public void testFindAll(){
        List<UrlSkin> skins = urlSkinDao.findAll();
        Assert.assertEquals(4, skins.size());
        Map<String,UrlSkin> map = new HashMap<String,UrlSkin>();
        for(UrlSkin skin : skins){
            map.put(skin.getId(), skin);
        }
        checkSame(skin1,map.get(skin1.getId()));
        checkSame(skin2,map.get(skin2.getId()));
        checkSame(skin3,map.get(skin3.getId()));
        checkSame(skin4,map.get(skin4.getId()));
    }
    
    /**
     * Tests that we can insert a new UrlSkin
     */
    @Test
    public final void testSaveUrlSkin() throws Exception {
        assertEquals(4, countRowsInTable("url_skin"));

        UrlSkin skin = new UrlSkin();
        
        skin.setUrl("http://oup.co.uk/p1/p2/p3");
        skin.setSkinPath("/some/path/to/file.css");
        urlSkinDao.save(skin);
        urlSkinDao.flush();
        urlSkinDao.clear();

        assertEquals(5, countRowsInTable("url_skin"));
    }
    
    /**
     * Tests that we can update a UrlSkin
     */
    @Test
    public final void testUpdateUrlSkin() throws Exception {
    	
        String random = UUID.randomUUID().toString();
    	String skinPath = random;
    	
        UrlSkin resSkin = urlSkinDao.getEntity(skin1.getId());
        
        resSkin.setSkinPath(skinPath);
        
        long v1 = resSkin.getVersion();
        urlSkinDao.update(resSkin);
        urlSkinDao.flush();
        UrlSkin updated =  urlSkinDao.getEntity(skin1.getId());
        assertEquals(skinPath,updated.getSkinPath());
        long v2 = updated.getVersion();
        Assert.assertTrue(v1 != v2);
        
    }
    

    /**
     * Tests that we can insert a new UrlSkin
     */
    @Test
    public final void testSaveUrlSkinWithEmptyResourcePath() throws Exception {
        assertEquals(4, countRowsInTable("url_skin"));

        String url = "http://oup.co.uk";
        String skinPath= "/some/path/to/file.css";
        UrlSkin skin = new UrlSkin();
        skin.setUrl(url);
        skin.setSkinPath(skinPath);
        
        Assert.assertNull(skin.getId());
        urlSkinDao.save(skin);
        Assert.assertNotNull(skin.getId());
        
        urlSkinDao.flush();
        urlSkinDao.clear();

        UrlSkin loaded = urlSkinDao.getEntity(skin.getId());
        assertEquals(url,loaded.getUrl());
        assertEquals(skinPath,loaded.getSkinPath());
        assertEquals(5, countRowsInTable("url_skin"));
    }
    
    /**
     * Tests that we can insert a new UrlSkin
     */
    @Test
    public final void testSaveUrlSkinWithNullResourcePath() throws Exception {
        assertEquals(4, countRowsInTable("url_skin"));

        String url = "http://oup.co.uk"; 
        String skinPath = "/some/path/to/file.css";
        UrlSkin skin = new UrlSkin();
        skin.setUrl(url);
        skin.setSkinPath(skinPath);
        
        Assert.assertNull(skin.getId());
        urlSkinDao.save(skin);
        Assert.assertNotNull(skin.getId());
        
        urlSkinDao.flush();
        urlSkinDao.clear();

        UrlSkin loaded = urlSkinDao.getEntity(skin.getId());
        assertEquals(url,loaded.getUrl());
        assertEquals(skinPath,loaded.getSkinPath());
        assertEquals(5, countRowsInTable("url_skin"));
    }

}
