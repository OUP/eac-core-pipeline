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
import com.oup.eac.service.ELTDataExportService;
import com.oup.eac.service.ServiceLayerException;

/**
 * Data Export Scheduled Job. Note only one instance should active to run in the environment.
 * 
 * Use http://www.cronmaker.com/ for defining/validation cron triggers.
 * 
 * This class used to be in the eacWebServiceApp project.
 * 
 */
public class CMDPDataExportJob extends QuartzJobBean implements StatefulJob {
	
    private static final Logger LOG = Logger.getLogger(CMDPDataExportJob.class);
    public static final String LAST_SUCCESS_REPORT_DATE = "LAST_SUCCESS_REPORT_DATE";
    
    private ApplicationContext applicationContext;
    
    private String dataExportServiceName;
    
    private String divisionType;
    
    private String cmdpSupportEmail;
    
    /**
     * Default constructor.
     */
    public CMDPDataExportJob() {
        super();
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
    	ELTDataExportService dataExportService = getDataExportService();
        final DateTime dateTo = new DateTime(context.getScheduledFireTime());
        final DateTime dateFrom = getFromDate(dateTo, context);
        
        LOG.info("Generating ELT report from: " + dateFrom.toString(DateUtils.SHORT_DATE_TIME_PATTERN) + " to: " + dateTo.toString(DateUtils.SHORT_DATE_TIME_PATTERN));
        
    	try {
			dataExportService.ftpCMDPData(divisionType, dateFrom, dateTo, cmdpSupportEmail);
			map.put(LAST_SUCCESS_REPORT_DATE, dateTo.toString(DateUtils.SHORT_DATE_TIME_PATTERN));
		} catch (ServiceLayerException e) {
			LOG.error(e.getMessage(), e);
		}
    }

    private ELTDataExportService getDataExportService() {
    	ELTDataExportService service = this.applicationContext.getBean(this.dataExportServiceName, ELTDataExportService.class);
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

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

	/**
	 * @param dataExportServiceName the dataExportServiceName to set
	 */
	public final void setDataExportServiceName(String dataExportServiceName) {
		this.dataExportServiceName = dataExportServiceName;
	}

	/**
	 * @param divisionType the divisionType to set
	 */
	public final void setDivisionType(String divisionType) {
		this.divisionType = divisionType;
	}

	/**
	 * @param cmdpSupportEmail the cmdpSupportEmail to set
	 */
	public final void setCmdpSupportEmail(String cmdpSupportEmail) {
		this.cmdpSupportEmail = cmdpSupportEmail;
	}

}
