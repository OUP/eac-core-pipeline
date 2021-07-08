package com.oup.eac.service;

import com.oup.eac.domain.ActivationCodeBatch;
import com.oup.eac.domain.ActivationCodeRegistrationDefinition;
import com.oup.eac.dto.ActivationCodeBatchReportCriteria;
import com.oup.eac.dto.ReportCriteria;

public interface AsyncReportService {

    void createReport(ReportCriteria reportCriteria, String email);
    
    void createReport(ActivationCodeBatchReportCriteria reportCriteria, String email);

	void createActivationCodeBatch(
			ActivationCodeBatch activationCodeBatch,
			ActivationCodeRegistrationDefinition activationCodeRegistrationDefinition,
			int numTokens, int allowedUsage, String productGroupId,
			String adminEmail, String adminName);
}
