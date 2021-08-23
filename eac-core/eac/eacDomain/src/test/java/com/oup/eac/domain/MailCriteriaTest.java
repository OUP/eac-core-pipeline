package com.oup.eac.domain;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.oup.eac.common.utils.EACSettings;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath*:/eac/eac*-beans.xml",
        "classpath*:/eac/test.eac*-beans.xml" })
public class MailCriteriaTest {

    @Test
    public void testToString() throws UnsupportedEncodingException, AddressException {
      StringBuffer sb = new StringBuffer();
      sb.append("\n");
      sb.append("\nFrom: Oxford University Press <noreply@dev.access.oup.com>");
      sb.append("\nSent: Fri 14/12/2012 02:59");
      sb.append("\nTo Recipients: test@mailinator.com");
      sb.append("\nSubject: the subject");
      sb.append("\nMessage: ");
      sb.append("\n");
      sb.append("\nline1");
      sb.append("\nline2");
      String expected = sb.toString();

        Calendar cal = new GregorianCalendar();
        cal.set(Calendar.YEAR, 2012);
        cal.set(Calendar.MONTH, Calendar.DECEMBER);
        cal.set(Calendar.DAY_OF_MONTH, 14);
        cal.set(Calendar.DAY_OF_MONTH, 14);
        cal.set(Calendar.HOUR_OF_DAY, 14);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 30);
        Date sent = cal.getTime();
        
        /*MailCriteria mc = new MailCriteria(sent);
        InternetAddress internetAdd = new InternetAddress("noreply@dev.access.oup.com", EACSettings.getProperty(EACSettings.EMAIL_FROM_TITLE));
        mc.setFrom(internetAdd);
        mc.getToAddresses().add(new InternetAddress("test@mailinator.com"));
        mc.setText("line1\nline2");
        mc.setSubject("the subject");
        String result = mc.toString();
        
        Assert.assertEquals(expected, result);*/
    }

}
