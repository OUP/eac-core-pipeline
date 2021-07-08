package com.oup.eac.service;

import com.oup.eac.domain.QuartzLogEntry;

/**
 * A service which creates entries in a Quartz Job history table.
 * 
 * @author David Hay
 *
 */
public interface QuartzLoggingService {

	public void logQuartzEntry(QuartzLogEntry quartzLogEntry);
	
}
