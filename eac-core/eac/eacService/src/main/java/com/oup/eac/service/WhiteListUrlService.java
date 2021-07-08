package com.oup.eac.service;

import java.util.List;

import com.oup.eac.domain.WhiteListUrl;

public interface WhiteListUrlService {
	
	
	WhiteListUrl getListUrl(final String url);

	List<String> getUrls();
	
	void saveWhiteListUrl(final WhiteListUrl whiteListUrl);
	
	void deleteWhiteListUrl(final WhiteListUrl whiteListUrl);

	List<WhiteListUrl> getAllUrls();
	
	boolean updateUrls(List<WhiteListUrl> toDelete, List<WhiteListUrl> toUpdate, List<WhiteListUrl> toAdd) throws ServiceLayerException;
	
}
