package com.oup.eac.service.impl;

import java.util.concurrent.Executor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.oup.eac.domain.ActivationCodeBatch;
import com.oup.eac.domain.ActivationCodeRegistrationDefinition;
import com.oup.eac.dto.ActivationCodeBatchReportCriteria;
import com.oup.eac.dto.ReportCriteria;
import com.oup.eac.integration.facade.exceptions.ErightsException;
import com.oup.eac.service.ActivationCodeBatchReportService;
import com.oup.eac.service.ActivationCodeService;
import com.oup.eac.service.AsyncReportService;
import com.oup.eac.service.ReportService;
import com.oup.eac.service.ServiceLayerException;

@Service("asyncReportService")
public class AsyncReportServiceImpl implements AsyncReportService {
    
    @Autowired
    private ReportService reportService;
    
    @Autowired
    private ActivationCodeBatchReportService activationCodeBatchReportService;
    
    @Autowired
    private ActivationCodeService activationCodeService ;
    
    @Override
    public void createReport(final ReportCriteria reportCriteria, final String email) {
        Executor executor = new Executor() {
            @Override
            public void execute(Runnable runnable) {
                new Thread(runnable).start();
            }
        };
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
					reportService.createReport(reportCriteria, email);
				} catch (ServiceLayerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });
    }
    
    @Override
    public void createReport(final ActivationCodeBatchReportCriteria reportCriteria, final String email) {
        Executor executor = new Executor() 
        {
            @Override
            public void execute(Runnable runnable) {
                new Thread(runnable).start();
            }
        };
        executor.execute(new Runnable() 
        {
            @Override
            public void run() {
                activationCodeBatchReportService.createActivationCodeReport(reportCriteria, email);
            }
        });
    }
    @Override
    public void createActivationCodeBatch(final ActivationCodeBatch activationCodeBatch, 
			final ActivationCodeRegistrationDefinition activationCodeRegistrationDefinition,
			final int numTokens, 
			final int allowedUsage, final String productGroupId, final String adminEmail,final String adminName) {
        Executor executor = new Executor() 
        {
            @Override
            public void execute(Runnable runnable) {
                new Thread(runnable).start();
            }
        };
        executor.execute(new Runnable() 
        {
            @Override
            public void run() {
            	
				try {
					activationCodeService.saveActivationCodeBatchAsync(activationCodeBatch, activationCodeRegistrationDefinition, numTokens, 
							allowedUsage,productGroupId,adminEmail,adminName);
				} catch (AccessDeniedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ServiceLayerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ErightsException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });
    }
}