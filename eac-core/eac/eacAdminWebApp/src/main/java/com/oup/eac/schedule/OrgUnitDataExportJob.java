package com.oup.eac.schedule;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.oup.eac.service.OrgUnitUsageReportService;

public class OrgUnitDataExportJob extends QuartzJobBean implements StatefulJob {
    
    private static final Logger LOG = Logger.getLogger(OrgUnitDataExportJob.class);
    
    private String emailTo = null;
    private String eacEmailBcc = null;
    private String orgUnitReportServiceName = null;
    private ApplicationContext applicationContext = null;
    
    /**
     * Default constructor.
     */
    public OrgUnitDataExportJob() {
        super();
    }

    /**
     * Constructor.
     * 
     * @param emailTo
     *            recipient
     * @param productOwner
     *            product owner.
     * @param dataExportService
     *            service
     */
    public OrgUnitDataExportJob(final String emailTo, final String eacEmailBcc, String orgUnitReportServiceName) {
        super();
        this.emailTo = emailTo;
        this.eacEmailBcc = eacEmailBcc;
        this.orgUnitReportServiceName = orgUnitReportServiceName;
    }

    /**
     * Run the data export job for the product owner.
     * 
     * @see org.springframework.scheduling.quartz.QuartzJobBean#executeInternal(org.quartz.JobExecutionContext)
     * @param context
     *            job context
     * @throws JobExecutionException
     *             job exception
     */
    @Override
    protected final void executeInternal(final JobExecutionContext context) throws JobExecutionException {
        JobDataMap map = context.getJobDetail().getJobDataMap();
        try {
            DateTime dateTo = new DateTime(context.getScheduledFireTime());
            DateTime dateFrom = JobUtils.getFromDate(context); 
            if (dateFrom == null) {
                dateFrom = dateTo.minusMonths(1);
            }
            getOrgUnitReportService().emailOrgUnitUsageReport(dateFrom, dateTo, emailTo, this.eacEmailBcc);
            JobUtils.updateLastSuccessDate(map, dateTo);
        } catch (Exception e) {
            LOG.error("Unable to run data export job", e);
        }
    }

    /**
     * Get email to.
     * 
     * @return email to
     */
    public final String getEmailTo() {
        return emailTo;
    }

    /**
     * Set email to.
     * 
     * @param emailTo
     *            email recipient
     */
    public final void setEmailTo(final String emailTo) {
        this.emailTo = emailTo;
    }

    /**
	 * @return the eacEmailBcc
	 */
	public final String getEacEmailBcc() {
		return eacEmailBcc;
	}

	/**
	 * @param eacEmailBcc the eacEmailBcc to set
	 */
	public final void setEacEmailBcc(String eacEmailBcc) {
		this.eacEmailBcc = eacEmailBcc;
	}

    /**
     * Get data export service.
     * 
     * @return service
     */
    private OrgUnitUsageReportService getOrgUnitReportService() {
        OrgUnitUsageReportService service = this.applicationContext.getBean(this.orgUnitReportServiceName, OrgUnitUsageReportService.class);
        return service;
    }
    
    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public String getOrgUnitReportServiceName() {
        return orgUnitReportServiceName;
    }

    public void setOrgUnitReportServiceName(String orgUnitReportServiceName) {
        this.orgUnitReportServiceName = orgUnitReportServiceName;
    }
    

}
