package com.oup.eac.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

/**
 * Represents a log entry for quartz jobs.
 * 
 * @author David Hay
 *
 */
@Entity
@Table(name="quartz_log_entry")
public class QuartzLogEntry extends BaseDomainObject {

    @Column(name="job_name")
    private String jobName;
    
    @Column(name="job_group")
    private String jobGroup;
    
    @Column(name="trg_name")
    private String triggerName;
    
    @Column(name="trg_group")
    private String triggerGroup;
    
    @Column(name="sch_fire_time")
    @Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
    private DateTime schedFireTime;
    
    @Column(name="act_fire_time")
    @Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
    private DateTime actualFireTime;
    
    @Column(name="nxt_fire_time")
    @Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
    private DateTime nextFireTime;

    @Column(name="job_run_time")
    private long jobRunTime;
    
    @Column(name="refire_count")
    private int refireCount;
    
    @Column(name="trg_instr_code")
    private int triggerInstructionCode;

    @Column(name="host_name")
    private String hostName;
    
    @Column(name="host_address")
    private String hostAddress;
    
    @Column(name="job_class_name")
    private String jobClassName;
    
    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    public String getTriggerName() {
        return triggerName;
    }

    public void setTriggerName(String triggerName) {
        this.triggerName = triggerName;
    }

    public String getTriggerGroup() {
        return triggerGroup;
    }

    public void setTriggerGroup(String triggerGroup) {
        this.triggerGroup = triggerGroup;
    }

    public DateTime getSchedFireTime() {
        return schedFireTime;
    }

    public void setSchedFireTime(DateTime schedFireTime) {
        this.schedFireTime = schedFireTime;
    }

    
    public DateTime getActualFireTime() {
        return actualFireTime;
    }

    public void setActualFireTime(DateTime actualFireTime) {
        this.actualFireTime = actualFireTime;
    }

    public DateTime getNextFireTime() {
        return nextFireTime;
    }

    public void setNextFireTime(DateTime nextFireTime) {
        this.nextFireTime = nextFireTime;
    }

    public long getJobRunTime() {
        return jobRunTime;
    }

    public void setJobRunTime(long jobRunTime) {
        this.jobRunTime = jobRunTime;
    }

    public int getRefireCount() {
        return refireCount;
    }

    public void setRefireCount(int refireCount) {
        this.refireCount = refireCount;
    }

    public int getTriggerInstructionCode() {
        return triggerInstructionCode;
    }

    public void setTriggerInstructionCode(int triggerInstructionCode) {
        this.triggerInstructionCode = triggerInstructionCode;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getHostAddress() {
        return hostAddress;
    }

    public void setHostAddress(String hostAddress) {
        this.hostAddress = hostAddress;
    }

    public String getJobClassName() {
        return jobClassName;
    }

    public void setJobClassName(String jobClassName) {
        this.jobClassName = jobClassName;
    }
    
}
