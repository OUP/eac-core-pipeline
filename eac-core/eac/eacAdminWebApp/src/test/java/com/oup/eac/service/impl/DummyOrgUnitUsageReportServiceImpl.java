package com.oup.eac.service.impl;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import com.oup.eac.service.OrgUnitUsageReportService;
import com.oup.eac.service.ServiceLayerException;

public class DummyOrgUnitUsageReportServiceImpl implements OrgUnitUsageReportService {

    
    private static final Logger LOG = org.apache.log4j.Logger.getLogger(DummyDataExportServiceImpl.class);
    
    
    @PostConstruct
    public void init(){
        LOG.debug("The DummyOrgUnitUsageReportService has been created");
    }

    @Override
    public void emailOrgUnitUsageReport(DateTime fromTime, DateTime toTime, String emailTo, String emailBcc)
            throws ServiceLayerException {
        String msg = String.format("**** emailOrgUnitUsageReport(%s,%s,%s,%s)",fromTime,toTime, emailTo, emailBcc);
        LOG.debug(msg);
    }

}
