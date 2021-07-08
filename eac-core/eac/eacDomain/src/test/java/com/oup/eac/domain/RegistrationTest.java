package com.oup.eac.domain;

import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.oup.eac.domain.utils.audit.TestingAppender;

public class RegistrationTest {

    private TestingAppender testingAppender;
    private ProductRegistration registration;
    
    @Before
    public void onSetup() {
        testingAppender = new TestingAppender();
        ProductRegistration.LOG.addAppender(testingAppender);
        registration = new ProductRegistration();
        registration.setId("IDABC");
    }

    @Test
    public void testSetErightsLicenceIdFirstTime() {
        registration.setId("12345");
        Assert.assertTrue(testingAppender.getMessages().isEmpty());
    }
    @Ignore
    @Test
    public void testSetErightsLicenceIdSecondTime() {
        registration.setId("12345");
        registration.setId("54321");
        List<LoggingEvent> messages = testingAppender.getMessages();
        Assert.assertEquals(1, messages.size());
        Assert.assertEquals(Level.ERROR, messages.get(0).getLevel());
        Assert.assertEquals("Orphan Licence : RegistationId[IDABC] : overwriting old erightsLicenceId[12345] with new erightsLicenceId[54321]", (String)messages.get(0).getMessage());
    }
}
