package com.oup.eac.service;

import com.oup.eac.common.utils.UrlMap;
import com.oup.eac.domain.UrlSkin;

/**
 * A service which gets the UrlMap.
 * 
 * @author David Hay
 *
 */
public interface UrlMapService {

    /**
	 * Gets the url skin map.
	 *
	 * @return the url skin map
	 */
	UrlMap<UrlSkin> getUrlMap();
}
