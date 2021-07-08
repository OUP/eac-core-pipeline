package com.oup.eac.schedule;

import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Seconds;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.oup.eac.common.date.utils.DateUtils;
import com.oup.eac.service.DataExportService;

/**
 * Data Export Scheduled Job. Note only one instance should active to run in the environment.
 * 
 * Use http://www.cronmaker.com/ for defining/validation cron triggers.
 * 
 * This class used to be in the eacWebServiceApp project.
 * 
 */
public class DataExportJob extends QuartzJobBean implements StatefulJob {
    private static final Logger LOG = Logger.getLogger(DataExportJob.class);
    
    public static final String LAST_SUCCESS_REPORT_DATE = "LAST_SUCCESS_REPORT_DATE";

    private String emailTo = null;
    private String eacEmailBcc = null;
    private String divisionType = null;
    private String dataExportServiceName = null;
    private ApplicationContext applicationContext = null;
    
    /**
     * Default constructor.
     */
    public DataExportJob() {
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
    public DataExportJob(final String emailTo, final String eacEmailBcc, final String divisionType, String dataExportServiceName) {
        super();
        this.emailTo = emailTo;
        this.eacEmailBcc = eacEmailBcc;
        this.divisionType = divisionType;
        this.dataExportServiceName = dataExportServiceName;
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
        LOG.info("Execute data export job for: " + divisionType + " to:" + emailTo);
        JobDataMap map = context.getJobDetail().getJobDataMap();
        final DateTime dateTo = new DateTime(context.getScheduledFireTime());
        final DateTime dateFrom = getFromDate(dateTo, context);
        LOG.info("Export period from: " + dateFrom.toString() + " to: " + dateTo.toString());
        try {
            getDataExportService().eMailDataExport(divisionType,dateFrom, dateTo, emailTo, eacEmailBcc);
            map.put(LAST_SUCCESS_REPORT_DATE, dateTo.toString(DateUtils.SHORT_DATE_TIME_PATTERN));
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
     * @return the divisionType
     */
    public final String getDivisionType() {
        return divisionType;
    }

    /**
     * @param divisionType the divisionType to set
     */
    public final void setDivisionType(final String divisionType) {
        this.divisionType = divisionType;
    }

    /**
     * Get data export service.
     * 
     * @return service
     */
    private DataExportService getDataExportService() {
        DataExportService service = this.applicationContext.getBean(this.dataExportServiceName, DataExportService.class);
        return service;
    }
    
    /**
     * Calculate previous execution time. 
     * Uses the previous execution time is one is available otherwise calculates it using gap to next execution time.
     * @param toDate
     *            scheduled execution date time
     * @param context
     *            the jobExecutionContext
     * @return previous execution date time
     */
    private DateTime getFromDate(final DateTime toDate, final JobExecutionContext context) {
        final DateTime result;
        
        JobDataMap map = context.getJobDetail().getJobDataMap();
        String dateString = map.getString(LAST_SUCCESS_REPORT_DATE);
        if(StringUtils.isBlank(dateString)) {
            Date nextDate = context.getNextFireTime();
            final DateTime nextDT = new DateTime(nextDate.getTime());
            final Seconds secs = Seconds.secondsBetween(toDate, nextDT);
            final DateTime prevDT = toDate.minusSeconds(secs.getSeconds());
            result = prevDT;
        } else {
        	result = DateUtils.parseToShortDateTime(dateString, Locale.getDefault());
        }

        return result;
    }
    
    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public void setDataExportServiceName(String dataExportServiceName) {
        this.dataExportServiceName = dataExportServiceName;
    }

    public String getDataExportServiceName() {
        return dataExportServiceName;
    }
    
}
