package com.oup.eac.service;

import org.joda.time.DateTime;

public interface OrgUnitUsageReportService {
    
    void emailOrgUnitUsageReport(DateTime fromTime, DateTime toTime, String emailTo, String emailBcc) throws ServiceLayerException;
        
}
