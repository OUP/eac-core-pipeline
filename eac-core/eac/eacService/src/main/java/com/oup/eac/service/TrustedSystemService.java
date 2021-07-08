package com.oup.eac.service;

import java.util.List;

import com.oup.eac.domain.TrustedSystem;
import com.oup.eac.integration.facade.exceptions.ErightsException;

public interface TrustedSystemService {
	
	List<String> createTrustedSystem(TrustedSystem trustedSystem) throws ErightsException;
	
	List<String> updateTrustedSystem(TrustedSystem trustedSystem) throws ErightsException;
	
	List<String> deleteTrustedSystem(final TrustedSystem trustedSystem) throws Exception;

	List<TrustedSystem> fetchAllTrustedSystems() throws Exception ;

}
