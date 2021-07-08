package com.oup.eac.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oup.eac.integration.facade.ErightsRestFacade;
import com.oup.eac.service.InsertIntoCloudFailFeeder;
import com.oup.eac.ws.rest.v1.AbstractResponse;

@Service("insertIntoCloudFailFeederService")
public class InsertIntoCloudFailFeederServiceImpl implements InsertIntoCloudFailFeeder {
	private final ErightsRestFacade erightsRestFacade;
	
	@Autowired
	public InsertIntoCloudFailFeederServiceImpl(ErightsRestFacade erightsRestFacade) {
		this.erightsRestFacade = erightsRestFacade ;
		
	}
	
	/* (non-Javadoc)
	 * @see com.oup.eac.service.InsertIntoCloudFailFeeder#insertToFailFeeder(java.util.Map)
	 */
	@Override
	public List<String> insertToFailFeeder(Map<String, String> requestBody) {
		AbstractResponse response = erightsRestFacade.insertFailFeeder(requestBody);
				List<String> responseMessages = new ArrayList<String>();
		if ( response.getStatus().equalsIgnoreCase("SUCCESS")) {
			responseMessages.add(response.getStatus()) ;
		} else {
			if (response.getErrorMessage() != null ) {
				responseMessages.add(response.getErrorMessage()) ;
			} else {
				Map<String, String> map =  response.getErrorMessages();
				for(Entry<String, String> element : map.entrySet()) {
					responseMessages.add(element.getValue());
				}
			}
		}
		return responseMessages;
	}
	
}
