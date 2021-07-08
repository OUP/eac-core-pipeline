package com.oup.eac.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oup.eac.integration.facade.ErightsRestFacade;
import com.oup.eac.service.CacheEvictionService;

@Service("cacheEvictionService")
public class CacheEvictionServiceImpl implements CacheEvictionService {
	private final ErightsRestFacade erightsRestFacade;
	
	@Autowired
	public CacheEvictionServiceImpl(ErightsRestFacade erightsRestFacade) {
		this.erightsRestFacade = erightsRestFacade ;
		
	}
	
	@Override
	public Map<String, String> evictCache(Map<String, String> requestBody) {
		Map<String, String> response = erightsRestFacade.evictCache(requestBody);
		return response;
	}
	
}
