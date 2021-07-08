package com.oup.eac.schedule;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * A quartz job which stores some persistent state in the database between invocations.
 * 
 * @author David Hay
 *
 */
public class PersistentJob extends QuartzJobBean implements StatefulJob {
    private static final Logger LOG = Logger.getLogger(PersistentJob.class);
    
    public static final String COUNTER = "counter";
    /**
     * Default constructor.
     */
    public PersistentJob() {
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
        LOG.debug("Simple Job Executed by Quartz");
        
        JobDataMap map = context.getJobDetail().getJobDataMap();
        String counter = map.getString(COUNTER);
        LOG.debug(String.format("The value of the Persistent counter is [%s]",counter));
        if(StringUtils.isEmpty(counter)){
            counter = "0";
        }
        int value = Integer.parseInt(counter);
        value++;
        counter = String.valueOf(value);
        
        map.put(COUNTER, counter);
        context.getJobDetail().setJobDataMap(map);//this should update the persistent job store
        String msg = String.format("The new value of the counter is [%s]",counter); 
        LOG.debug(msg);        
    }

}
