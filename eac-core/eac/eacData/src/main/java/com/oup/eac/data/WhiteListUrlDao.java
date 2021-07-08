package com.oup.eac.data;

import java.util.List;

import com.oup.eac.domain.WhiteListUrl;

public interface WhiteListUrlDao extends BaseDao<WhiteListUrl, String> {

	List<WhiteListUrl> getAllUrls();
}