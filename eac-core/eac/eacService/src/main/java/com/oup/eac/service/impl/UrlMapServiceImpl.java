package com.oup.eac.service.impl;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.oup.eac.common.utils.UrlMap;
import com.oup.eac.data.UrlSkinDao;
import com.oup.eac.domain.UrlSkin;
import com.oup.eac.service.UrlMapService;

/**
 * An implementation of the UrlMap service which uses the urlSkinDao and
 * caching.
 * 
 * @author David Hay
 * 
 */
@Service(value = "urlMapService")
public class UrlMapServiceImpl implements UrlMapService {

    private static final Logger LOG = Logger.getLogger(UrlMapServiceImpl.class);

    private final UrlSkinDao urlSkinDao;

    /**
     * Instantiates a new url skin map service impl.
     *
     * @param dao the dao
     */
    @Autowired
    public UrlMapServiceImpl(final UrlSkinDao dao) {
        Assert.notNull(dao);
        this.urlSkinDao = dao;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final UrlMap<UrlSkin> getUrlMap() {

        Map<String, UrlSkin> map = new HashMap<String, UrlSkin>();
        List<UrlSkin> urlSkins = urlSkinDao.findAll();
        if (LOG.isDebugEnabled()) {
            int numRead = (urlSkins == null) ? 0 : urlSkins.size();
            LOG.debug(String.format("Reloaded %d UrlMappings from UrlSkinDAO", numRead));
        }
        if (urlSkins != null) {
            for (UrlSkin urlSkin : urlSkins) {
                String url = urlSkin.getUrl();
                map.put(url, urlSkin);
            }
        }
        UrlMap<UrlSkin> result = null;
        try {
            result = new UrlMap<UrlSkin>(map);
        } catch (MalformedURLException ex) {
            LOG.error("cannot create UrlMap because of bad urls", ex);
        }
        return result;
    }
}
