package com.oup.eac.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.oup.eac.domain.UrlSkin;
import com.oup.eac.service.DefaultUrlSkinService;

@Component("defaultUrlSkinService")
public class DefaultUrlSkinServiceImpl implements DefaultUrlSkinService {

    @SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger(DefaultUrlSkinServiceImpl.class);

    private UrlSkin defaultUrlSkin;

    /**
     * Instantiates a new default url skin service impl.
     * 
     * @param url
     *            the url
     * @param skinPath
     *            the skin path
     * @param contactPath
     *            the contact path
     * @param siteName
     *            the site name
     */
    @Autowired
    public DefaultUrlSkinServiceImpl(
            @Value("${url.skin.default.url}") final String url,
            @Value("${url.skin.default.skin.path}") final String skinPath,
            @Value("${url.skin.default.contact.path}") final String contactPath,
            @Value("${url.skin.default.site.name}") final String siteName) {

        this.defaultUrlSkin = new UrlSkin();
        this.defaultUrlSkin.setUrl(url);
        this.defaultUrlSkin.setContactPath(contactPath);
        this.defaultUrlSkin.setSiteName(siteName);
        this.defaultUrlSkin.setSkinPath(skinPath);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UrlSkin getDefaultUrlSkin() {
        return this.defaultUrlSkin;
    }

}
