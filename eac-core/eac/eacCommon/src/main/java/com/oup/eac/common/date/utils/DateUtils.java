/**
 * 
 */
package com.oup.eac.common.date.utils;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.util.Assert;

import com.oup.eac.common.utils.EACSettings;

/**
 * @author harlandd
 * @author Ian Packard
 * 
 */
public final class DateUtils {

	private static Logger LOG = Logger.getLogger(DateUtils.class);
	
	public static final String SHORT_DATE_TIME_PATTERN = "dd/MM/yyyy HH:mm";
	public static final String DATE_FORMAT = "dd/MM/yyyy";
	private static final Map<String, String> jodaZoneInfoMap;
	private static final String JODA_RESOURCE_BASE = "org/joda/time/tz/data/";
	private static final String JODA_RESOURCE_ZONE_INFO_MAP = JODA_RESOURCE_BASE + "ZoneInfoMap";
    /**
     * Utility class.
     */
    private DateUtils() {
        
    }
    
    static {
        //holds ~570 time zones
        jodaZoneInfoMap = loadJodaZoneInfoMap();
    }

    /**
     * Convert a LocalDate (JodaTime) to an XMLGregorianCalendar.
     * 
     * @param localDate
     *            The LocalDate to convert
     * @return The calendar
     */
    public static XMLGregorianCalendar safeConvertLocalDate(final LocalDate localDate) {
    	XMLGregorianCalendar result = null;
    	if(localDate != null){
    	  try {
        	  DateTime dt = localDate.toDateTime(new LocalTime(), DateTimeZone.getDefault());
        	  result = DatatypeFactory.newInstance().newXMLGregorianCalendar(dt.toGregorianCalendar());
          } catch (Exception e) {
              LOG.error("unexpected exception",e);
              result = null;
          }
    	}
    	return result;
    }
    
    /**
     * Convert a LocalDateTime (JodaTime) to an XMLGregorianCalendar.
     * 
     * @param localDate
     *            The LocalDateTime to convert
     * @return The calendar
     */
	public static XMLGregorianCalendar safeConvertLocalDateTime(final LocalDateTime localDateTime) {
		XMLGregorianCalendar result = null;
		if (localDateTime != null) {
			try {
				result = DatatypeFactory.newInstance().newXMLGregorianCalendar(localDateTime.toDateTime().toGregorianCalendar());
			} catch (Exception e) {
				LOG.error("unexpected exception", e);
				result = null;
			}
		}
		return result;
	}
    
    /**
     * Convert dates received via a web service into joda time LocalDate.
     * 
     * @param date
     * 			The date received from the web service.
     * @return
     * 			The joda time based LocalDate
     */
    public static LocalDate safeConvertDate(final XMLGregorianCalendar date) {
    	if (date == null) return null;
    	return new LocalDate(date.toGregorianCalendar().getTime());
    }
    
    /**
     * Convert date and time received via a web service into joda time LocalDateTime.
     * 
     * @param date
     * 			The date and time received from the web service.
     * @return
     * 			The joda time based LocalDateTime
     */
    public static DateTime safeConvertDateTime(final XMLGregorianCalendar dateTime) {
    	if (dateTime == null) return null;
    	return new DateTime(dateTime.toGregorianCalendar().getTime());
    }
    
    public static LocalDateTime safeConvertLocalDateTime(final XMLGregorianCalendar dateTime) {
    	if (dateTime == null) return null;
    	return new LocalDateTime(dateTime.toGregorianCalendar().getTime());
    }
    /**
     * A utility class for formatting a date/time given a timeZone and a Locale.
     * @param utcTime The UTC time to format
     * @param locale the locale
     * @param timeZone the timeZone
     * @return the formatted output.
     */
    public static String formatDateTime(DateTime utcTime, Locale locale, DateTimeZone timeZone) {
		String result = null;
		if (utcTime != null) {
			try {
				DateTimeFormatter formatterLocale = DateTimeFormat.forStyle("FF").withLocale(locale).withZone(timeZone);
				DateTimeFormatter formatter = formatterLocale;
				DateTime time = utcTime.withZone(timeZone);
				result = formatter.print(time);
			} catch (Exception ex) {
				LOG.error("unexpected exception", ex);
				result = null;
			}
		}
		return result;
    }
    
    public static boolean isTimeZoneValid(String timeZoneID){
        boolean preCheck = isProblemWithTimeZoneResourceFile(timeZoneID);        
        if(preCheck ){
            return preCheck;
        }
        if (StringUtils.isBlank(timeZoneID)) {
			return true;
		}
		
		boolean result = false;
		try {		    
			DateTimeZone res = DateTimeZone.forID(timeZoneID);
			result = res != null;
		} catch (IllegalArgumentException ex) {
			LOG.warn(ex.getMessage());
			result = false;
		}
		return result;
    }
    
    public static int getDaysBetween(final LocalDate startDate, final LocalDate endDate) {
    	if (endDate == null) return -1;
    	if (startDate == null) return -1;
		Days d = Days.daysBetween(startDate, endDate);
		return d.getDays();	
	}
    
    
    
    /**
     * Get a list of ordered time zone ids.
     * 
     * @return
     */
    public static List<String> getOrderedTimeZoneIds() {
    	@SuppressWarnings("unchecked")
		List<String> tzIds = new ArrayList<String>(DateTimeZone.getAvailableIDs());
    	Collections.sort(tzIds);
    	return tzIds;
    }
    
    /**
     * Get list of time zones ordered by id.
     * 
     * @return
     */
    public static List<TimeZone> getOrderedTimeZones() {
    	List<String> ids = getOrderedTimeZoneIds();
    	List<TimeZone> tzs = new ArrayList<TimeZone>(ids.size()); 
    	for (String id : ids) {
    		tzs.add(DateTimeZone.forID(id).toTimeZone());
    	}
    	return tzs;
    }
    
    public static DateTime parseToDate(final String dateString, final Locale locale) {
    	return DateTimeFormat.forPattern(DATE_FORMAT).withLocale(locale).parseDateTime(dateString);
    }
    
    public static String printAsDate(final DateTime dateTime, final Locale locale) {
    	return DateTimeFormat.forPattern(DATE_FORMAT).withLocale(locale).print(dateTime);
    }    
    
    public static DateTime parseToShortDateTime(final String dateString, final Locale locale) {
    	return DateTimeFormat.forPattern(SHORT_DATE_TIME_PATTERN).withLocale(locale).parseDateTime(dateString);
    }
    
    public static String printAsShortDateTime(final DateTime dateTime, final Locale locale) {
    	return DateTimeFormat.forPattern(SHORT_DATE_TIME_PATTERN).withLocale(locale).print(dateTime);
    }
    
    public static LocalDate safeParseLocalDate(final String date, final Locale locale) {
    	return DateTimeFormat.forPattern(DATE_FORMAT).parseDateTime(date).toLocalDate();
    }
    
    
	public static String formatDateFormEmail(final LocalDate date, final Locale locale, final String timeZoneID) {
		String result = null;
		if (date != null) {
			String style = EACSettings.getProperty(EACSettings.EAC_EMAIL_DATE_STYLE);
			Locale loc = locale == null ? Locale.getDefault() : locale;
			DateTimeZone tz;
			if (isTimeZoneValid(timeZoneID)) {
				tz = DateTimeZone.forID(timeZoneID);
			} else {
				tz = DateTimeZone.getDefault();
			}
			result = DateTimeFormat.forStyle(style).withLocale(loc).withZone(tz).print(date);
		}
		return result;
	}
	
    /**
     * This helps us check JodaTime ResourceFiles can be loaded so we can avoid the ERRORs jodaTime normally logs when it cannot load the resource files.
     * If the JodaTime resource file cannot be loaded, a single error message is now logged rather than the many error messages logged currently.
     * See mantis ticket http://mantisoup.idm.fr/view.php?id=13351
     * @param timeZoneId
     * @return true if (the timezoneId is valid AND the timezone resource file cannot be found), false otherwise
     */
    public static boolean isProblemWithTimeZoneResourceFile(String timeZoneId) {         
        boolean result;
        if (StringUtils.isBlank(timeZoneId)) {
            result = false;
        } else {
            String validTimeZoneId = (String) jodaZoneInfoMap.get(timeZoneId);
            if (validTimeZoneId == null) {
                result = false;
            } else {
                result = isProblemWithValidTimeZoneResourceFile(validTimeZoneId);
            }
        }
        return result;
    }

    protected static boolean isProblemWithValidTimeZoneResourceFile(String validTimeZoneId) {
        boolean problem = false;
        int attempts = 1;
        int max = 5;
        String resourceName = JODA_RESOURCE_BASE + validTimeZoneId;
        do {
            InputStream res = getJodaResourceInputStream(resourceName, attempts, max);
            problem = res == null;
            attempts++;
        }while(problem && attempts <= max);
        
        if(problem) {
            String msg = String.format("JodaTimeProblem : cannot find resource file [%s] for valid TimeZoneId [%s] after [%d] attempts", resourceName, validTimeZoneId, max);
            LOG.error(msg);
        }
        return problem;
    }

    protected static InputStream getJodaResourceInputStream(String resourceName, int attempt, int max) {
        InputStream res = null;
        ClassLoader cl = org.joda.time.tz.ZoneInfoProvider.class.getClassLoader();        
        res = cl.getResourceAsStream(resourceName);
        if(res == null){
            String message = String.format("JodaTimeProblem : cannot find resource file [%s] attempt[%d/%d]",resourceName,attempt,max);
            LOG.info(message);
        }
        return res;
    }

    private static Map<String,String> loadJodaZoneInfoMap() {
        Map<String,String> map = new TreeMap<String,String>(String.CASE_INSENSITIVE_ORDER);
        InputStream in = getJodaResourceInputStream(JODA_RESOURCE_ZONE_INFO_MAP, 1, 1);
        Assert.isTrue(in != null);
        DataInputStream din = new DataInputStream(in);
        try {
            loadJodaZoneInfoMap(din, map);
        }catch(IOException ex){
            LOG.debug("unexpected problem", ex);
        } finally {
            try {
                if(din != null){
                    din.close();
                }
            } catch (IOException e) {
            }
        }
        return map;
    }
	
    private static void loadJodaZoneInfoMap(DataInputStream din, Map<String,String> zimap) throws IOException {
        // Read the string pool.
        int size = din.readUnsignedShort();
        String[] pool = new String[size];
        for (int i=0; i<size; i++) {
            pool[i] = din.readUTF().intern();
        }

        // Read the mappings.
        size = din.readUnsignedShort();
        for (int i=0; i<size; i++) {
            try {
                zimap.put(pool[din.readUnsignedShort()], pool[din.readUnsignedShort()]);
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new IOException("Corrupt zone info map");
            }
        }
    }

    public static XMLGregorianCalendar safeConvertFromDateTime(DateTime dateTime) {
        XMLGregorianCalendar result = null;
        if(dateTime != null){
          try {
              //We will assume the datepart is correct but the TimeZone is wrong.
              DateTime dateTimeUTC = dateTime.toLocalDateTime().toDateTime(DateTimeZone.UTC);
              result = DatatypeFactory.newInstance().newXMLGregorianCalendar(dateTimeUTC.toGregorianCalendar());
          } catch (Exception e) {
              LOG.error("unexpected exception",e);
              result = null;
          }
        }
        return result;
    }
    public static Date fromISO8601UTC(String dateStr) {
		  TimeZone tz = TimeZone.getTimeZone("UTC");
		  DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		  df.setTimeZone(tz);
		  
		  try {
		    return df.parse(dateStr);
		  } catch (ParseException e) {
		    e.printStackTrace();
		  }
		  
		  return null;
	}
    
}
