package com.oup.eac.service.impl;

import java.net.MalformedURLException;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.oup.eac.common.utils.UrlMap;
import com.oup.eac.domain.UrlSkin;
import com.oup.eac.service.DomainSkinResolverService;
import com.oup.eac.service.UrlMapService;

/**
 * Domain Skin Resolver Service.
 * 
 * @author David Hay
 */
@Service(value = "domainSkinResolverService")
public class DomainSkinResolverServiceImpl implements DomainSkinResolverService {

    private static final Logger LOG = Logger.getLogger(DomainSkinResolverServiceImpl.class);

    private final UrlMapService urlMapService;

    /**
     * Constructor.
     * 
     * @param urlMapService
     *            the url skin map service
     */
    @Autowired
    public DomainSkinResolverServiceImpl(final UrlMapService urlMapService) {
        Assert.notNull(urlMapService);
        this.urlMapService = urlMapService;
    }

    /**
     * Initialisation method.
     */
    @PostConstruct
    public final void init() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("DomainSkinResolverSkinImpl initialised");
        }
    }

    @Override
    public final UrlSkin getSkinFromDomain(final String domain) {
        UrlSkin result = null;
        UrlMap<UrlSkin> urlSkinMap = urlMapService.getUrlMap();
        if (urlSkinMap != null) {
            try {
                result = urlSkinMap.get(domain);
            } catch (MalformedURLException ex) {
                LOG.warn("Cannot get skin from UrlMap because of bad url:" + domain != null ? domain : "null");
            }
        }        
        return result;
    }



}
