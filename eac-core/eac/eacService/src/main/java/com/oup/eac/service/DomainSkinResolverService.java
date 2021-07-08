package com.oup.eac.service;

import com.oup.eac.domain.UrlSkin;

/**
 * The Interface DomainSkinResolverService.
 */
public interface DomainSkinResolverService {

    /**
     * Gets the skin from domain.
     *
     * @param domain the domain
     * @return the skin from domain
     */
    UrlSkin getSkinFromDomain(String domain);

}
