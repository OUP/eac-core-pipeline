package com.oup.eac.common.date.utils;

import static junit.framework.Assert.assertEquals;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author David Hay
 * @author Ian Packard
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath*:/eac/eac*-beans.xml",
		"classpath*:/eac/test.eac*-beans.xml" })
public class DateUtilsTest {

	@Before
	public void setup(){
		Locale.setDefault(Locale.US);
		DateTimeZone.setDefault(DateTimeZone.forID("America/New_York"));
	}
	
    @Test
    public void testFormatDateTime1(){
        DateTime testTime = new DateTime(2011,12,9,15,10,54,30, DateTimeZone.UTC);
        
        String expectedParis = "vendredi 9 d\u00e9cembre 2011 16 h 10 CET";
        String expectedLondon = "Friday, December 9, 2011 3:10:54 PM GMT";
        
        String london = DateUtils.formatDateTime(testTime, Locale.ENGLISH, DateTimeZone.forID("Europe/London"));
        String paris  = DateUtils.formatDateTime(testTime, Locale.FRANCE, DateTimeZone.forID("Europe/Paris"));
        
        assertEquals(expectedLondon, london);
        assertEquals(expectedParis, paris);
    }
    
    
    @Test
    public void testFormatDateTime2(){
        DateTime testTime = new DateTime(2011,12,9,15,10,54,30);
        
        String result2 = DateUtils.formatDateTime(null, null, null);
        Assert.assertEquals(null, result2);
    }

    
    @Test
    public void testGetTimeZoneFromListOfIds() {
    	List<String> timeZoneIds = DateUtils.getOrderedTimeZoneIds();
    	
    	List<TimeZone> timeZones = DateUtils.getOrderedTimeZones();
    	
    	assertEquals("Check length same", timeZoneIds.size(), timeZones.size());
    	
    	assertEquals("Check last zulu", "Zulu", timeZoneIds.get(timeZoneIds.size()-1));
    }
    
    @Test
    public void testDaysBetween1() {
    	int daysBetween = DateUtils.getDaysBetween(new LocalDate(), new LocalDate().plusDays(5));
    	assertEquals("Check correct number of days", 5, daysBetween);
    }
    
    @Test
    public void testDaysBetween2() {
    	int daysBetween = DateUtils.getDaysBetween(new LocalDate(), null);
    	assertEquals("Check correct number of days", -1, daysBetween);
    }
    
    @Test
    public void testDaysBetween3() {
    	int daysBetween = DateUtils.getDaysBetween(null, new LocalDate());
    	assertEquals("Check correct number of days", -1, daysBetween);
    }
    
    @Test
    public void testDaysBetween4() {
    	int daysBetween = DateUtils.getDaysBetween(null, null);
    	assertEquals("Check correct number of days", -1, daysBetween);
    }
    
    @Test
    public void testSafeConvertLocalDateTimeXML() {
    	LocalDateTime now = new LocalDateTime();
    	
    	XMLGregorianCalendar dateTime = DateUtils.safeConvertLocalDateTime(now);
    	
    	LocalDateTime roundTrippedDateTime = DateUtils.safeConvertLocalDateTime(dateTime);
    	
    	assertEquals("Check same date/time", now, roundTrippedDateTime);
    	
    	//test null
    	Assert.assertNull(DateUtils.safeConvertLocalDateTime((XMLGregorianCalendar)null));
    	Assert.assertNull(DateUtils.safeConvertLocalDateTime((LocalDateTime)null));
    }
    
    /**
     * Checks that when we convert a LocalDate to a XMLGregorianCalendar that the time part of the XMLGregorianCalendar is the correct time in UTC.
     * For example, if we convert 30/APR/1969 to XMLGregorianCalendar at 4pm BST we should get 1969-04-30T16:00:00.000+01:00
     * (Before we would have got 1969-04-30T16:00:00.000+00:00)
     * 
     * @throws DatatypeConfigurationException
     */
    @Ignore
    @Test
    public void testSafeConvertLocalDate() throws DatatypeConfigurationException{
    	DateTime now = new DateTime();
    	GregorianCalendar calone = now.toGregorianCalendar();
    	calone.set(Calendar.MONTH, Calendar.APRIL);
    	calone.set(Calendar.YEAR, 1969);
    	calone.set(Calendar.DAY_OF_MONTH, 30);
    	
    	LocalDate ld = new LocalDate(1969,4,30);    	
    	XMLGregorianCalendar cal = DateUtils.safeConvertLocalDate(ld);
    	System.out.println(cal.toXMLFormat());
    	GregorianCalendar gregcal = cal.toGregorianCalendar();
    	
    	long diff = Math.abs(gregcal.getTimeInMillis() - calone.getTimeInMillis());
    	System.out.println("diff is " + diff);
    	Assert.assertTrue(diff < 5);

    	//null
    	XMLGregorianCalendar res = DateUtils.safeConvertLocalDate((LocalDate)null);
    	Assert.assertNull(res);
    }
    
    @Test
    public void testSafeConvertDateXML() throws DatatypeConfigurationException{
    	//test local date
    	DateTime dt = new DateTime(1969,4,30,9,16,15,558);
    	GregorianCalendar gcal = dt.toGregorianCalendar();
    	XMLGregorianCalendar xgcal = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcal);    	
    	LocalDate res = DateUtils.safeConvertDate(xgcal);
    	Assert.assertEquals(1969,res.getYear());
    	Assert.assertEquals(4,res.getMonthOfYear());
    	Assert.assertEquals(30,res.getDayOfMonth());
    	
    	//test null
    	Assert.assertNull(DateUtils.safeConvertDate(null));
    }
    
    @Test
    public void testSafeConvertDateTimeXML() throws DatatypeConfigurationException{
    	//test local date
    	DateTime dt = new DateTime(1969,4,30,9,16,15,558);
    	GregorianCalendar gcal = dt.toGregorianCalendar();
    	XMLGregorianCalendar xgcal = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcal);    	
    	DateTime res = DateUtils.safeConvertDateTime(xgcal);
    	Assert.assertEquals(1969,res.getYear());
    	Assert.assertEquals(4,res.getMonthOfYear());
    	Assert.assertEquals(30,res.getDayOfMonth());
    	Assert.assertEquals(9,res.getHourOfDay());
    	Assert.assertEquals(16,res.getMinuteOfHour());
    	Assert.assertEquals(15,res.getSecondOfMinute());
    	Assert.assertEquals(558,res.getMillisOfSecond());
    	
    	//test null
    	Assert.assertNull(DateUtils.safeConvertDateTime(null));
    }
    @Test
    public void testIsTimeZoneValid(){
    	Assert.assertTrue(DateUtils.isTimeZoneValid("Europe/London"));
    	Assert.assertTrue(DateUtils.isTimeZoneValid("UTC"));
    	Assert.assertTrue(DateUtils.isTimeZoneValid("Asia/Vladivostok"));
    	Assert.assertTrue(DateUtils.isTimeZoneValid("Atlantic/Reykjavik"));
    	Assert.assertTrue(DateUtils.isTimeZoneValid("-01:00"));
    	Assert.assertTrue(DateUtils.isTimeZoneValid("+01:00"));
    	
    	Assert.assertTrue(DateUtils.isTimeZoneValid(null));//this is for customers who have not set there timezoneid yet.
    	//Assert.assertTrue(DateUtils.isTimeZoneValid("europe/london"));
    	Assert.assertFalse(DateUtils.isTimeZoneValid(" Atlantic/Reykjavik"));
    	Assert.assertTrue(DateUtils.isTimeZoneValid("Etc/GMT+12"));
    	//Assert.assertTrue(DateUtils.isTimeZoneValid("etc/gmt+12"));
    	Assert.assertFalse(DateUtils.isTimeZoneValid("BOB"));
    	
    }
    
    @Test
    public void testParseToDate(){
    	DateTime dateTime = DateUtils.parseToDate("01/02/2012", Locale.ENGLISH);
    	Assert.assertEquals(2012,dateTime.getYear());
    	Assert.assertEquals(2,dateTime.getMonthOfYear());
    	Assert.assertEquals(1,dateTime.getDayOfMonth());
    }
    
    @Test
    public void testSafeParseLocalDate(){
    	LocalDate localDate = DateUtils.safeParseLocalDate("01/02/2012", Locale.ENGLISH);
    	Assert.assertEquals(2012,localDate.getYear());
    	Assert.assertEquals(2,localDate.getMonthOfYear());
    	Assert.assertEquals(1,localDate.getDayOfMonth());
    }
    
    @Test
    public void testParseToShortDateTime(){
    	DateTime dateTime = DateUtils.parseToShortDateTime("01/02/2012 15:16", Locale.ENGLISH);
    	Assert.assertEquals(2012,dateTime.getYear());
    	Assert.assertEquals(2,dateTime.getMonthOfYear());
    	Assert.assertEquals(1,dateTime.getDayOfMonth());
    	Assert.assertEquals(15,dateTime.getHourOfDay());
    	Assert.assertEquals(16,dateTime.getMinuteOfHour());
    }
    
    @Test
    public void testPrintAsDate(){
    	DateTime dt = new DateTime(1969,4,30,9,16,15,558);
    	String english = DateUtils.printAsDate(dt, Locale.UK);
    	Assert.assertEquals("30/04/1969",english);
    	String korea = DateUtils.printAsDate(dt, Locale.KOREA);
    	Assert.assertEquals("30/04/1969",korea);
    }
    
    @Test
    public void testPrintAsDateTime(){
    	DateTime dt = new DateTime(1969,4,30,9,16,15,558);
    	String english = DateUtils.printAsShortDateTime(dt, Locale.UK);
    	Assert.assertEquals("30/04/1969 09:16",english);
    	String korea = DateUtils.printAsShortDateTime(dt, Locale.KOREA);
    	Assert.assertEquals("30/04/1969 09:16",korea);
    }
    
    @Test
    public void testStyles() throws DatatypeConfigurationException{
    	
    	DateTimeZone.setDefault(DateTimeZone.forID("America/New_York"));
    	
    	DateTime time = new DateTime(1970,4,30,9,16,15,558, DateTimeZone.UTC);    	
    	String xml = DatatypeFactory.newInstance().newXMLGregorianCalendar(time.toGregorianCalendar()).toXMLFormat();
    	Assert.assertEquals("1970-04-30T09:16:15.558Z",xml);
    	
    	String res1 = DateTimeFormat.forStyle("M-").withLocale(Locale.FRANCE).withZone(DateTimeZone.forID("Europe/Paris")).print(time);
    	String res2 = DateTimeFormat.forStyle("M-").withLocale(Locale.UK).withZone(DateTimeZone.forID("Europe/London")).print(time);
    	String res3 = DateTimeFormat.forStyle("M-").withLocale(Locale.US).withZone(DateTimeZone.forID("America/New_York")).print(time);
    	
    	
    	String res4 = DateTimeFormat.forStyle("ML").withLocale(Locale.FRANCE).withZone(DateTimeZone.forID("Europe/Paris")).print(time);
    	String res5= DateTimeFormat.forStyle("ML").withLocale(Locale.UK).withZone(DateTimeZone.forID("Europe/London")).print(time);
    	String res6 = DateTimeFormat.forStyle("ML").withLocale(Locale.US).withZone(DateTimeZone.forID("America/New_York")).print(time);

    	System.out.println(res1);
    	System.out.println(res2);
    	System.out.println(res3);
    	
    	System.out.println(res4);
    	System.out.println(res5);
    	System.out.println(res6);
    	
    	Assert.assertEquals("30 avr. 1970",res1);
    	Assert.assertEquals("30-Apr-1970",res2);
    	Assert.assertEquals("Apr 30, 1970",res3);
    	Assert.assertEquals("30 avr. 1970 10:16:15 CET",res4);
    	Assert.assertEquals("30-Apr-1970 10:16:15 GMT",res5);
    	Assert.assertEquals("Apr 30, 1970 5:16:15 AM EDT",res6);
    	
    }
    
    @Test
    public void testDateForEmail(){
    	LocalDate time = new LocalDate(1969,4,30);
    	String result1 = DateUtils.formatDateFormEmail(time, null, null);
    	String result2 = DateUtils.formatDateFormEmail(time, Locale.getDefault(), DateTimeZone.getDefault().getID());    	
    	Assert.assertEquals(result1,result2);
    	
    	String result3 = DateUtils.formatDateFormEmail(time, Locale.FRANCE, "Europe/Paris");
    	Assert.assertEquals("30 avr. 1969",result3);    	
    	
    	String result4 = DateUtils.formatDateFormEmail(time, Locale.US, "America/New_York");
    	Assert.assertEquals("Apr 30, 1969",result4);
    	
    	String result5 = DateUtils.formatDateFormEmail(time, Locale.US, "Blah/Blah");
    	Assert.assertEquals("Apr 30, 1969",result5);
    	
    	String res = DateUtils.formatDateFormEmail(new LocalDate(2011,9,19), Locale.UK, "Europe/London");
    	Assert.assertEquals("19-Sep-2011",res);
    	
    	
    	String resA = DateUtils.formatDateFormEmail(null, Locale.UK, "Europe/London");
    	Assert.assertEquals(resA,null);
    }
    
    @Test
	public void testTimeZoneOffsets(){
		long millisInHour = 1000 * 60 * 60;
		java.util.TimeZone tz1 = java.util.TimeZone.getTimeZone("Etc/GMT-5");//east of GMT
		java.util.TimeZone tz2 = java.util.TimeZone.getTimeZone("GMT+5");//east of GMT
		java.util.TimeZone tz3 = java.util.TimeZone.getTimeZone("Etc/GMT-4");//east of GMT
		java.util.TimeZone tz4 = java.util.TimeZone.getTimeZone("GMT+4");//east of GMT
		
		java.util.TimeZone tz5 = java.util.TimeZone.getTimeZone("Etc/GMT+2");//west of GMT
		java.util.TimeZone tz6= java.util.TimeZone.getTimeZone("GMT-2");//west of GMT
		java.util.TimeZone tz7 = java.util.TimeZone.getTimeZone("Etc/GMT+1");//west of GMT
		java.util.TimeZone tz8 = java.util.TimeZone.getTimeZone("GMT-1");//west of GMT
		
		int offset1 = (int)(tz1.getOffset(System.currentTimeMillis()) / millisInHour);
		int offset2 = (int)(tz2.getOffset(System.currentTimeMillis()) / millisInHour);
		int offset3 = (int)(tz3.getOffset(System.currentTimeMillis()) / millisInHour);
		int offset4 = (int)(tz4.getOffset(System.currentTimeMillis()) / millisInHour);
		
		int offset5 = (int)(tz5.getOffset(System.currentTimeMillis()) / millisInHour);
		int offset6 = (int)(tz6.getOffset(System.currentTimeMillis()) / millisInHour);
		int offset7 = (int)(tz7.getOffset(System.currentTimeMillis()) / millisInHour);
		int offset8 = (int)(tz8.getOffset(System.currentTimeMillis()) / millisInHour);
		
		Assert.assertEquals(5, offset1);
		Assert.assertEquals(5, offset2);
		Assert.assertEquals(4, offset3);
		Assert.assertEquals(4, offset4);
		
		Assert.assertEquals(-2, offset5);
		Assert.assertEquals(-2, offset6);
		Assert.assertEquals(-1, offset7);
		Assert.assertEquals(-1, offset8);
	}

    @Test
    public void testIsProblemWithTimeZoneResourceFile() {
        Assert.assertFalse(DateUtils.isProblemWithTimeZoneResourceFile("utc"));
        Assert.assertFalse(DateUtils.isProblemWithTimeZoneResourceFile("etc/gmt+12"));  
        Assert.assertFalse(DateUtils.isProblemWithTimeZoneResourceFile("etc/gmt+13"));
        Assert.assertEquals(null,DateUtils.getJodaResourceInputStream("etc/gmt+13", 1, 1));
        Assert.assertTrue(DateUtils.isProblemWithValidTimeZoneResourceFile("etc/gmt+13"));//because gmt+13 is not really a valid timezone id (it's used here to simulate resource not found)
    }
    
    @Test
    public void testSafeConvertFromDateTime() throws DatatypeConfigurationException {
        DateTimeZone nyc = DateTimeZone.forID("America/New_York");        
        Assert.assertNotNull(nyc);
        DateTime t1 = new DateTime(2012,10,31,16,0,0,0,nyc);
        String pre = DatatypeFactory.newInstance().newXMLGregorianCalendar(t1.toGregorianCalendar()).toXMLFormat();
        Assert.assertEquals("2012-10-31T16:00:00.000-04:00",pre);
        
        XMLGregorianCalendar cal2 = DateUtils.safeConvertFromDateTime(t1);
        String result = cal2.toXMLFormat();
        Assert.assertEquals("2012-10-31T16:00:00.000Z",result);
    }
}
