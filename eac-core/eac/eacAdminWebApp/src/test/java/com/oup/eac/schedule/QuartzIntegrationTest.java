package com.oup.eac.schedule;


import junit.framework.Assert;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.oup.eac.schedule.listeners.DataExportTriggerListener;
import com.oup.eac.schedule.listeners.OrgUnitDataExportTriggerListener;
import com.oup.eac.schedule.listeners.PersistentJobTriggerListener;

/**
 * This is the Quartz Integration Test class.
 * 
 * Please note : this is the ONLY test class which performs Quartz Tests.
 * 
 * If we have more that 1 quartz scheduler running against the same shared 
 * test database we will run into problems. We should only have 1 scheduler running
 * at any time on a given box.
 *  
 * To ensure we only have 1 scheduler running - we start/stop the scheduler within
 * a single test method. 
 * 
 * This guarantees that junit will not spawn 2 threads each with their own application context/scheduler 
 * accessing the shared test database simultaneously.
 * 
 * @author David Hay
 *
 */
@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:/eac/eac*-beans.xml", "classpath*:/eac/test.eac*-beans.xml" })
public class QuartzIntegrationTest extends BaseQuartzTest {

    @Autowired
    public PersistentJobTriggerListener pListener;

    @Autowired
    public DataExportTriggerListener dListener;

    @Autowired
    public OrgUnitDataExportTriggerListener orgUnitListener;

    private int dStart;
    private int pStart;
    private int orgUnitStart;
    
    private static final String[] LOGGERS = { "com.oup.eac.schedule" };

    @BeforeClass
    public static void beforeClass() {
        for (String logger : LOGGERS) {
            try {
                Logger.getLogger(logger).setLevel(Level.DEBUG);
            } catch (Exception ex) {
            }     
        }
    }

    @AfterClass
    public static void afterClass() {
        for (String logger : LOGGERS) {
            try {
                Logger.getLogger(logger).setLevel(Level.WARN);
            } catch (Exception ex) {
            }
        }
    }
    
    @Before
    public void setup() throws SchedulerException{
        checkScheduler();
        this.dStart = dListener.getCounter();
        this.pStart = pListener.getCounter();
        this.orgUnitStart = orgUnitListener.getCounter();
        sched.start();
        try {
            Thread.sleep(12500);// assuming the jobs go off every 3 seconds
        } catch (Exception ex) {
            Assert.fail("wait too short");
        }
    }
    
    @After
    public void tearDown() throws SchedulerException{
        sched.shutdown(false);
    }
    
    /**
     * This single test method executes all the Quartz tests.
     * @throws SchedulerException
     */
  /*  @Test
    public void testQuartsJobs() throws SchedulerException{
        checkDataExportJob();
        checkPersistentJob();
        checkOrgUnitJob();
    }*/

    /**
    * This method checks that a quartz job similar to the malaysian data export
    * has been triggered several times in line with it's cron trigger.
    */
    private void checkDataExportJob() throws SchedulerException{
        int end = dListener.getCounter();
        int diff = end - dStart;
        Assert.assertTrue(diff >=4 );
    }
    
    /**
     * This method checks that a quartz job has been triggered several times
     * in line with it's cron trigger and that it is correctly updating a counter in the jobdata stored
     * to the database.
     *  
     * @throws SchedulerException
     */
    private void checkPersistentJob() throws SchedulerException {
        int end = pListener.getCounter();
        int diff = end - pStart;
        Assert.assertTrue(diff >= 4);
        
    }
    
    /**
     * This method checks that a quartz job has been triggered several times
     * in line with it's cron trigger and that it is correctly updating a counter in the jobdata stored
     * to the database.
     *  
     * @throws SchedulerException
     */
    private void checkOrgUnitJob() throws SchedulerException {
        int end = orgUnitListener.getCounter();
        int diff = end - orgUnitStart;
        Assert.assertTrue(diff >= 4);
    }

}
