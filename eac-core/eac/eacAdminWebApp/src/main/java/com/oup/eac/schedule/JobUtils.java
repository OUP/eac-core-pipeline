package com.oup.eac.schedule;

import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Seconds;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;

import com.oup.eac.common.date.utils.DateUtils;

public class JobUtils {
    
    public static final String LAST_SUCCESS_REPORT_DATE = "LAST_SUCCESS_REPORT_DATE";

    public static DateTime deduceLastFireTime(final JobExecutionContext context){
        Date nextDate = context.getNextFireTime();
        DateTime currentDT = new DateTime(context.getFireTime());
        DateTime nextDT = new DateTime(nextDate.getTime());
        Seconds secs = Seconds.secondsBetween(currentDT, nextDT);
        DateTime prevDT = currentDT.minusSeconds(secs.getSeconds());
        DateTime result = prevDT;
        return result;
    }
    
    public static DateTime getFromDate(final JobExecutionContext context) {
        DateTime result = null;
        JobDataMap map = context.getJobDetail().getJobDataMap();
        String dateString = map.getString(LAST_SUCCESS_REPORT_DATE);
        if(StringUtils.isNotBlank(dateString)) {
            result = DateUtils.parseToShortDateTime(dateString, Locale.getDefault());
        }
        return result;
    }
    
    public static void updateLastSuccessDate(JobDataMap map, DateTime lastSuccessDate){
        map.put(LAST_SUCCESS_REPORT_DATE, lastSuccessDate.toString(DateUtils.SHORT_DATE_TIME_PATTERN));
    }
}
