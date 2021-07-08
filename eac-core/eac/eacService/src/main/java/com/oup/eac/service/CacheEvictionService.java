package com.oup.eac.service;

import java.util.Map;

public interface CacheEvictionService {

	Map<String, String> evictCache(Map<String, String> requestBody);

}
