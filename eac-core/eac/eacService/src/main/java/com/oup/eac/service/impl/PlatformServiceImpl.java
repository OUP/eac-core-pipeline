package com.oup.eac.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.oup.eac.domain.Platform;
import com.oup.eac.integration.facade.ErightsRestFacade;
import com.oup.eac.integration.facade.exceptions.ErightsException;
import com.oup.eac.integration.facade.exceptions.PlatformNotFoundException;
import com.oup.eac.service.PlatformService;
import com.oup.eac.ws.rest.v1.CreatePlatformResponse;
import com.oup.eac.ws.rest.v1.DeletePlatformRequest;
import com.oup.eac.ws.rest.v1.DeletePlatformResponse;
import com.oup.eac.ws.rest.v1.GetAllPlatformsResponse;
import com.oup.eac.ws.rest.v1.OupPlatform;
import com.oup.eac.ws.rest.v1.UpdatePlatformResponse;

@Service("platformService")
public class PlatformServiceImpl implements PlatformService {

	private final ErightsRestFacade erightsRestFacade;
	
	
	@Autowired
	public PlatformServiceImpl(ErightsRestFacade erightsRestFacade) {
		this.erightsRestFacade = erightsRestFacade ;
		
	}
	
	@Override
	public List<String> createPlatform(Platform platform) throws ErightsException {

		List<String> errorMessages = new ArrayList<String>();
		CreatePlatformResponse response = erightsRestFacade.createPlatform(platform);
		Map<String, String> map =  response.getErrorMessages();
		for(Entry<String, String> element : map.entrySet()) {
			errorMessages.add(element.getValue());
		}

		return errorMessages;
	}
	
	@Override
	public List<String> updatePlatform(Platform platform) throws ErightsException {
		List<String> errorMessages = new ArrayList<String>();
		UpdatePlatformResponse response = erightsRestFacade.updatePlatform(platform);
		Map<String, String> map =  response.getErrorMessages();
		for(Entry<String, String> element : map.entrySet()) {
			errorMessages.add(element.getValue());
		}

		return errorMessages;
		
	}

	@Override
	public List<String> deletePlatform(Platform request) throws Exception {
		List<String> errorMessages = new ArrayList<String>();
		DeletePlatformResponse response = erightsRestFacade
				.deletePlatform(request);
		if (response.getErrorMessage() != null) {
			errorMessages.add(response.getErrorMessage());
		}
		Map<String, String> map = response.getErrorMessages();
		if (map.size() > 0 && map != null) {
			for (Entry<String, String> element : map.entrySet()) {
				errorMessages.add(element.getValue());
			}
		}
		return errorMessages;
	}

	
	@Override
	public List<Platform> getAllPlatforms() throws ErightsException, PlatformNotFoundException, AccessDeniedException {
		GetAllPlatformsResponse response = 	erightsRestFacade.getAllPlatforms() ;
		
		List<OupPlatform> dto = response.getPlatforms() ;
			List<Platform> platformList = new ArrayList<Platform>();
			for(int i=0;i<dto.size();i++){
				Platform pt = new Platform();
				pt.setPlatformId(dto.get(i).getId());
				pt.setCode(dto.get(i).getCode());
				pt.setName(dto.get(i).getName());
				pt.setDescription(dto.get(i).getDescription());
				platformList.add(pt);
			}
			return platformList;
	}
	
}
