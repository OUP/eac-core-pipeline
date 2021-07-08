package com.oup.eac.domain;

import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.oup.eac.domain.utils.audit.TestingAppender;

public class LinkedRegistrationTest {

    private TestingAppender testingAppender;
    private LinkedRegistration linkedRegistration;
    
    @Before
    public void onSetup() {
        testingAppender = new TestingAppender();
        LinkedRegistration.LOG.addAppender(testingAppender);
        linkedRegistration = new LinkedRegistration();
        linkedRegistration.setId("IDXYZ");
    }

    @Test
    public void testSetErightsLicenceIdFirstTime() {
        linkedRegistration.setErightsLicenceId(11111);
        Assert.assertTrue(testingAppender.getMessages().isEmpty());
    }

    @Test
    public void testSetErightsLicenceIdSecondTime() {
        linkedRegistration.setErightsLicenceId(11111);
        linkedRegistration.setErightsLicenceId(22222);
        List<LoggingEvent> messages = testingAppender.getMessages();
        Assert.assertEquals(1, messages.size());
        Assert.assertEquals(Level.ERROR, messages.get(0).getLevel());
        Assert.assertEquals("Orphan Licence : LinkedRegistationId[IDXYZ] : overwriting old erightsLicenceId[11111] with new erightsLicenceId[22222]", (String)messages.get(0).getMessage());
    }
}
