package com.oup.eac.service;

import com.oup.eac.dto.ActivationCodeBatchReportCriteria;
import com.oup.eac.integration.facade.exceptions.ErightsException;

public interface ActivationCodeBatchReportService {
    
    void createActivationCodeReport(final ActivationCodeBatchReportCriteria reportCriteria, final String email);
    
    long getActivationCodeReportSize(final ActivationCodeBatchReportCriteria reportCriteria) 
    		throws ErightsException;
}
