package com.oup.eac.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.oup.eac.data.QuartzLogEntryDao;
import com.oup.eac.domain.QuartzLogEntry;
import com.oup.eac.service.QuartzLoggingService;

/**
 * Implementation of Quartz Logging Service.
 * 
 * @author David Hay
 *
 */
@Service(value="quartzLoggingService")
public class QuartzLoggingServiceImpl implements QuartzLoggingService {

    @SuppressWarnings("unused")
    private Logger LOG = Logger.getLogger(QuartzLoggingServiceImpl.class);
    
	private final QuartzLogEntryDao quartzDao;

	@Autowired
	public QuartzLoggingServiceImpl(final QuartzLogEntryDao dao) {
		Assert.notNull(dao);
	    this.quartzDao = dao;
	}

    @Override
    public void logQuartzEntry(final QuartzLogEntry quartzLogEntry) {
        this.quartzDao.save(quartzLogEntry);
    }

}
