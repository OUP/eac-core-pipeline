package com.oup.eac.service;

import java.util.List;

import com.oup.eac.domain.UrlSkin;

public interface UrlSkinService {
	
	UrlSkin getUrlSkinById(final String id);

	List<UrlSkin> getAllUrlSkinsOrderedBySiteName();
	
	void saveUrlSkin(final UrlSkin urlSkin);
	
	void deleteUrlSkin(final UrlSkin urlSkin);
}
