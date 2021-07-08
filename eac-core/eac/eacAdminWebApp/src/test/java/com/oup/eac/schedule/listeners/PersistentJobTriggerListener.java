package com.oup.eac.schedule.listeners;

import junit.framework.Assert;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.Trigger;

import com.oup.eac.schedule.PersistentJob;

/**
 * Quartz Trigger Listener used to test a job which uses the database for job persistence.
 * 
 * @author David Hay
 *
 */
public class PersistentJobTriggerListener extends BaseTriggerListener {

    public static final Object LOCK = new Object();
    
    private boolean first = true;
    
    @Override
    public String getName(){
        return "PersistentJobTestTriggerListener";
    }
    
    @Override
    protected void complete(Trigger trg, JobExecutionContext context) {
        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        String value = jobDataMap.getString(PersistentJob.COUNTER);
        int newValue = Integer.parseInt(value);
        int temp = counter;
        if(!first){
            Assert.assertEquals(temp + 1, newValue);
        }
        counter = newValue;
        first = false;
    }
}
