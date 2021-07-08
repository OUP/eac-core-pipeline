package com.oup.eac.schedule;

import java.util.Properties;

import javax.mail.Session;
import javax.naming.NamingException;

import junit.framework.Assert;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerMetaData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;
import org.springframework.scheduling.quartz.LocalDataSourceJobStore;

/**
 * Base class for Quartz Integration Tests.
 * 
 * @author David Hay
 *
 */
public abstract class BaseQuartzTest {

    @Autowired
    @Qualifier("scheduler")
    protected Scheduler sched;
    
    public BaseQuartzTest() {
        try {            
            SimpleNamingContextBuilder builder = SimpleNamingContextBuilder.emptyActivatedContextBuilder();
            builder.bind("java:/Mail", Session.getInstance(new Properties()));
        } catch (NamingException ne) {
            throw new RuntimeException(ne);
        }
    }

    protected final void checkScheduler() throws SchedulerException {
        SchedulerMetaData md = sched.getMetaData();
        String summary = md.getSummary();
        System.out.println(summary);
        Class<?> jobStoreClass = md.getJobStoreClass();
        Assert.assertEquals(LocalDataSourceJobStore.class, jobStoreClass);
        Assert.assertFalse(sched.isStarted());
    }


}
