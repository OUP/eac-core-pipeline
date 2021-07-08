package com.oup.eac.service;

import com.oup.eac.dto.ReportCriteria;

public interface ReportService {
    
    long getReportSize(final ReportCriteria reportCriteria) throws ServiceLayerException;

    void createReport(final ReportCriteria reportCriteria, final String email) throws ServiceLayerException;
}
