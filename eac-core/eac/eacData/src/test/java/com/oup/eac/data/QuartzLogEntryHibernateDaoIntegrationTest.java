/*package com.oup.eac.data;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import junit.framework.Assert;

import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.joda.time.Seconds;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.oup.eac.domain.QuartzLogEntry;

*//**
 * Integration test of QuartzLogEntryHibernateDao
 * 
 * @see com.oup.eac.data.impl.hibernate.QuartzLogEntryHibernateDao
 * 
 * @author David Hay
 *//*
public class QuartzLogEntryHibernateDaoIntegrationTest extends AbstractDBTest {

    private static final String JOB_NAME_1 = "JN-1";
    private static final String JOB_NAME_2 = "JN-2";
    private static final String JOB_GROUP_1 = "J-G-1";
    private static final String JOB_GROUP_2 = "J-G-2";

    private static final String TRG_NAME_1 = "T-1";
    //private static final String TRG_NAME_2 = "T-2";
    private static final String TRG_GROUP_1 = "T-G-1";
    //private static final String TRG_GROUP_2 = "T-G-2";

    @Autowired
    private QuartzLogEntryDao quartzLogEntryDao;

    private QuartzLogEntry logEntry1;
    private QuartzLogEntry logEntry2;
    private QuartzLogEntry logEntry3;

    *//**
     * @throws Exception
     *             Sets up data ready for test.
     *//*
    @Before
    public final void setUp() throws Exception {

        DateTime sched1 = new DateTime(2011, 1, 1, 13, 0, 0, 0);
        DateTime sched2 = new DateTime(2011, 2, 2, 14, 0, 0, 0);
        DateTime sched3 = new DateTime(2011, 3, 2, 15, 0, 0, 0);

        int rfc1 = 1;
        int rfc2 = 2;
        int rfc3 = 3;

        int code1 = 11;
        int code2 = 22;
        int code3 = 33;

        Seconds delay = Seconds.seconds(60);
        Minutes next = Minutes.minutes(60);
        Seconds jobTime = Seconds.seconds(10);

        String hostName = "hostName";
        String hostAddress = "127.0.0.1";
        
        String jobClassName1 = "com.eac.Job1";
        String jobClassName2 = "com.eac.Job2";
        String jobClassName3 = "com.eac.Job3";
        this.logEntry1 = getSampleDataCreator().createQuartzLogEntry(TRG_NAME_1, TRG_GROUP_1, JOB_NAME_1, JOB_GROUP_1, sched1, delay, next, jobTime, rfc1,
                code1, hostName, hostAddress,jobClassName1);

        this.logEntry2 = getSampleDataCreator().createQuartzLogEntry(TRG_NAME_1, TRG_GROUP_1, JOB_NAME_1, JOB_GROUP_1, sched2, delay, next, jobTime, rfc2,
                code2, hostName, hostAddress,jobClassName2);

        this.logEntry3 = getSampleDataCreator().createQuartzLogEntry(TRG_NAME_1, TRG_GROUP_1, JOB_NAME_2, JOB_GROUP_2, sched3, delay, next, jobTime, rfc3,
                code3, hostName, hostAddress,jobClassName3);
        loadAllDataSets();
    }

    *//**
     * Test getting an answer and comparing it to the expected answer.
     * 
     * @throws Exception
     *             the exception
     *//*
    @Test
    public final void testGetQuartzLogEntry() throws Exception {
        QuartzLogEntry result = quartzLogEntryDao.getEntity(logEntry1.getId());
        checkSame(logEntry1, result);
    }

    private void checkSame(QuartzLogEntry log1, QuartzLogEntry log2) {
        assertEquals(log1.getJobName(), log2.getJobName());
        assertEquals(log1.getJobGroup(), log2.getJobGroup());
        assertEquals(log1.getTriggerName(), log2.getTriggerName());
        assertEquals(log1.getTriggerGroup(), log2.getTriggerGroup());

        assertEquals(log1.getRefireCount(), log2.getRefireCount());
        assertEquals(log1.getTriggerInstructionCode(), log2.getTriggerInstructionCode());

        assertEquals(log1.getSchedFireTime(), log2.getSchedFireTime());
        assertEquals(log1.getActualFireTime(), log2.getActualFireTime());
        assertEquals(log1.getNextFireTime(), log2.getNextFireTime());
        assertEquals(log1.getJobRunTime(), log2.getJobRunTime());
        assertEquals(log1.getHostAddress(), log2.getHostAddress());
        assertEquals(log1.getHostName(), log2.getHostName());

    }

    *//**
     * Test that we get a null result when we try and look up and entry that does not exist.
     * 
     * @throws Exception
     *             the exception
     *//*
    //@Test
    public final void testUrlSkinNotFound() {
        QuartzLogEntry result = quartzLogEntryDao.getEntity("999");
        Assert.assertNull(result);
    }

    //@Test
    public void testFindAll() {
        List<QuartzLogEntry> entries = quartzLogEntryDao.findAll();
        Assert.assertEquals(3, entries.size());
        Map<String, QuartzLogEntry> map = new HashMap<String, QuartzLogEntry>();
        for (QuartzLogEntry entry : entries) {
            map.put(entry.getId(), entry);
        }
        checkSame(logEntry1, map.get(logEntry1.getId()));
        checkSame(logEntry2, map.get(logEntry2.getId()));
        checkSame(logEntry3, map.get(logEntry3.getId()));
    }

    *//**
     * Tests that we can insert a new QuartzLogEntry
     *//*
    //@Test
    public final void testSaveQuartzLogEntry() throws Exception {
        assertEquals(3, countRowsInTable("quartz_log_entry"));

        QuartzLogEntry entry = new QuartzLogEntry();

        quartzLogEntryDao.save(entry);
        quartzLogEntryDao.flush();
        quartzLogEntryDao.clear();

        assertEquals(4, countRowsInTable("quartz_log_entry"));
    }

    *//**
     * Tests that we can update a UrlSkin
     *//*
    //@Test
    public final void testUpdateAnswer() throws Exception {

        String random = UUID.randomUUID().toString();
        String triggerName = random;

        QuartzLogEntry result = quartzLogEntryDao.getEntity(logEntry1.getId());
        result.setTriggerName(triggerName);
        
        long v1 = result.getVersion();
        quartzLogEntryDao.update(result);
        quartzLogEntryDao.flush();
        QuartzLogEntry updated = quartzLogEntryDao.getEntity(result.getId());
        assertEquals(triggerName, updated.getTriggerName());
        long v2 = updated.getVersion();
        Assert.assertTrue(v1 != v2);

    }

    *//**
     * Tests that we can insert a new QuartzLogEntry
     *//*
    //@Test
    public final void testSaveUrlSkinWithEmptyResourcePath() throws Exception {
        assertEquals(3, countRowsInTable("quartz_log_entry"));

        String jobName = "new job name";
        String jobGroup = "new job group";
        String trgName = "new trigger name";
        String trgGroup = "new trigger group";
        
        QuartzLogEntry entry = new QuartzLogEntry();
        entry.setJobName(jobName);
        entry.setJobGroup(jobGroup);
        entry.setTriggerName(trgName);
        entry.setTriggerGroup(trgGroup);

        Assert.assertNull(entry.getId());
        quartzLogEntryDao.save(entry);
        Assert.assertNotNull(entry.getId());

        quartzLogEntryDao.flush();
        quartzLogEntryDao.clear();

        QuartzLogEntry loaded = quartzLogEntryDao.getEntity(entry.getId());
        assertEquals(jobName, loaded.getJobName());
        assertEquals(jobGroup, loaded.getJobGroup());
        assertEquals(trgName, loaded.getTriggerName());
        assertEquals(trgGroup, loaded.getTriggerGroup());
        assertEquals(4, countRowsInTable("quartz_log_entry"));
    }

}
*/