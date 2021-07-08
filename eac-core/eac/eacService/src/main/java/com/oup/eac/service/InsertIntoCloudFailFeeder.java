package com.oup.eac.service;

import java.util.List;
import java.util.Map;

public interface InsertIntoCloudFailFeeder {


	/**
	 * @param requestBody
	 * @return
	 */
	List<String> insertToFailFeeder(Map<String, String> requestBody);

}
