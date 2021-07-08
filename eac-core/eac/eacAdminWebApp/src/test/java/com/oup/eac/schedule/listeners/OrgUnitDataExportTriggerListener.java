package com.oup.eac.schedule.listeners;

import java.util.Date;

import org.quartz.JobExecutionContext;
import org.quartz.Trigger;

/**
 * Quartz Trigger Listener used to test the OrgUnitDataExport
 * 
 * @author David Hay
 *
 */
public class OrgUnitDataExportTriggerListener extends BaseTriggerListener {
    @Override
    public String getName() {
        return "OrgUnitDataExportQuartzTestTriggerListener";
    }

    @Override
    protected synchronized void complete(Trigger trg, JobExecutionContext context) {
        if(trg.getName().equals(triggerName) == false){
            return;
        }
        Date expected = context.getScheduledFireTime();
        Date actual = context.getFireTime();
        checkFireTime(expected, actual);
        this.counter = counter + 1;
        debugCounter("onComplete");
    }

}
