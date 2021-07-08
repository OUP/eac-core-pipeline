package com.oup.eac.service;

import java.util.List;

import org.springframework.security.access.AccessDeniedException;

import com.oup.eac.domain.Platform;
import com.oup.eac.integration.facade.exceptions.ErightsException;
import com.oup.eac.integration.facade.exceptions.PlatformNotFoundException;

public interface PlatformService {
	
	List<String> createPlatform(Platform platform) throws ErightsException;
	
	List<String> updatePlatform(Platform platform) throws ErightsException;
	
	List<String> deletePlatform(final Platform request) throws Exception;
	
	List<Platform> getAllPlatforms() throws ErightsException, PlatformNotFoundException, AccessDeniedException;

}
