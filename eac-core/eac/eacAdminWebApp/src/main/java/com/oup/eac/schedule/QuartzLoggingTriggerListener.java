package com.oup.eac.schedule;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.Trigger;
import org.quartz.TriggerListener;

import com.oup.eac.domain.QuartzLogEntry;
import com.oup.eac.service.QuartzLoggingService;

public class QuartzLoggingTriggerListener implements TriggerListener {

    private static final Logger LOG = Logger.getLogger(QuartzLoggingTriggerListener.class);
    
    private final QuartzLoggingService quartzLoggingService;
    
    public QuartzLoggingTriggerListener(QuartzLoggingService quartzLoggingService){
        this.quartzLoggingService = quartzLoggingService;
    }
    
    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public void triggerComplete(Trigger trg, JobExecutionContext ctx, int triggerInstructionCode) {
        log("triggerComplete",trg,ctx,triggerInstructionCode);
        
        QuartzLogEntry entry = getLogEntry(trg,ctx,triggerInstructionCode);
        this.quartzLoggingService.logQuartzEntry(entry);
    }

    private QuartzLogEntry getLogEntry(Trigger trg, JobExecutionContext ctx, int triggerInstructionCode) {
        QuartzLogEntry entry = new QuartzLogEntry();
        JobDetail jobDetail = ctx.getJobDetail();
        
        entry.setJobName(jobDetail.getName());
        entry.setJobGroup(jobDetail.getGroup());
        entry.setTriggerName(trg.getName());
        entry.setTriggerGroup(trg.getGroup());
     
        entry.setActualFireTime(new DateTime(ctx.getFireTime()));
        entry.setSchedFireTime(new DateTime(ctx.getScheduledFireTime()));
        entry.setJobRunTime(ctx.getJobRunTime());
        entry.setNextFireTime(new DateTime(ctx.getNextFireTime()));
        
        entry.setTriggerInstructionCode(triggerInstructionCode);
        entry.setRefireCount(ctx.getRefireCount());
        
        String hostName = "undef";
        String hostAddress = "undef";
        try {
            InetAddress addr = InetAddress.getLocalHost();
            hostName = addr.getCanonicalHostName();
            hostAddress = addr.getHostAddress();
        } catch (UnknownHostException e) {
            LOG.warn("Failed to get local host info",e);
        }
        entry.setHostName(hostName);
        entry.setHostAddress(hostAddress);
        entry.setJobClassName(jobDetail.getJobClass().getName());
        return entry;
    }

    @Override
    public void triggerFired(Trigger trg, JobExecutionContext ctx) {
        log("triggerFired",trg,ctx);
    }

    @Override
    public void triggerMisfired(Trigger trg) {
        log("triggerMisFired",trg);
    }

    @Override
    public boolean vetoJobExecution(Trigger arg0, JobExecutionContext arg1) {
        return false;
    }
    
    private static void log(String prefix, Object... args){
        if(LOG.isTraceEnabled()){
            String prettyArgs = args == null ? "" : Arrays.toString(args);
            String msg = String.format("%s : %s",prefix,prettyArgs);
            LOG.trace(msg);
        }
    }
    
    @PostConstruct
    public void init(){
        LOG.debug("The QuartzLoggingTriggerListener has been created");
    }


}
