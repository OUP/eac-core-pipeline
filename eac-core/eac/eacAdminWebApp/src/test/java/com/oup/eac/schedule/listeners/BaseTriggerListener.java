package com.oup.eac.schedule.listeners;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Seconds;
import org.quartz.JobExecutionContext;
import org.quartz.Trigger;
import org.quartz.TriggerListener;

/**
 * Base class for Quartz Trigger Listeners used in testing.
 * @author David Hay
 *
 */
public abstract class BaseTriggerListener implements TriggerListener {

    private final Logger log = Logger.getLogger(getClass().getSimpleName());
    
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss:SSS");
    
    protected int counter = 0;
    
    protected String triggerName;

    @Override
    public abstract String getName();

    @Override
    public void triggerFired(Trigger trg, JobExecutionContext context) {
    }

    @Override
    public void triggerMisfired(Trigger arg0) {
    }

    @Override
    public boolean vetoJobExecution(Trigger trg, JobExecutionContext context) {
        return false;
    }

    protected void checkFireTime(Date expected, Date actual) {
        // we should not be seeing any late jobs as we are using a clean quartz DB for testing.
        DateTime exp = new DateTime(expected);
        DateTime act = new DateTime(actual);
        Seconds secs = Seconds.secondsBetween(exp, act);
        int diff = secs.getSeconds();
        if(diff > 0){
            log.warn("trigger event is late by " + diff + " seconds");
        }
    }

    @Override
    public synchronized final void triggerComplete(Trigger trg, JobExecutionContext context, int triggerInstructionCode) {
         
        if(trg.getName().equals(triggerName) == false){
            return;
        }
        Date expected = context.getScheduledFireTime();
        Date actual = context.getFireTime();
        checkFireTime(expected,actual);
        
        complete(trg,context);
        debugCounter("triggerComplete");
    }

    protected abstract void complete(Trigger trg, JobExecutionContext context);

    protected void debugCounter(String methodName){
        synchronized (sdf){
            String time = sdf.format(new Date());
            String msg = String.format("TriggerListener [%s] [%s] [%s] COUNTER IS [%d] [%s]%n",
                    this.getClass().getSimpleName(),
                    Integer.toHexString(this.hashCode()),
                    methodName,
                    counter,
                    time);
            log.debug(msg);
        }
    }
    

    public synchronized int getCounter() {        
        debugCounter("getCounter");
        int result = this.counter;
        return result;
    }

    public String getTriggerName() {
        return triggerName;
    }

    public void setTriggerName(String triggerName) {
        this.triggerName = triggerName;
    }
    
    
}
