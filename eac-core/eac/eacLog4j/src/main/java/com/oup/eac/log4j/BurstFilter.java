package com.oup.eac.log4j;

import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.apache.log4j.spi.Filter;
import org.apache.log4j.spi.LoggingEvent;

/**
 * A filter for the SMTP Appender that decides whether LogEvents should be emailed or not.
 * 
 * Within EAC, any exception which prints its stackTrace directly to System.err has each line of its stack trace sent to Log4j as a separate LogEvent with Level.ERROR. (See Jboss's LoggerStream )  
 * These ERROR LogEvents get sent to the  SMTP Appender. 
 * 
 * This Filter can detect log events that are derived from 'ex.printStackTrace(System.err)' as they have a Logger with name 'STDERR'
 * We don't want every line in the stack trace creating a separate email.
 * We can identify the 2nd and subsequent stack trace LogEvents as they start with 'at' and ignore them.
 * 
 * The BurstDetector is used to keep track of the rate of LogEvents, if the rate is too high they will be ignored.
 * 
 * @author David Hay
 *
 */
public class BurstFilter extends Filter {

    private static final Logger LOG = Logger.getLogger(BurstFilter.class);
    private static final String INFO = "info";
            
    private BurstDetector detector;
    
    private long numberOfEvents;
    private long timeWindowMs;
    private String stderrLoggerName = "STDERR";
    private String stderrAtLinePrefix = "at";
    private String stderrFirstLineInfoText=" [PLEASE SEE LOG FILES FOR FULL DETAILS.]";
    private boolean stderrCheckEnabled;
    
    @Override
    public synchronized int decide(LoggingEvent event) {
        
        if(detector == null){
            detector = new BurstDetector(numberOfEvents, timeWindowMs);
        }
        
        if (stderrCheckEnabled && isStackTraceNonFirstLine(event)) {
            return DENY;
        }
        if(detector.increment()){
            LOG.debug("filter result : ACCEPT");
            return ACCEPT;
        }else {
            LOG.debug("filter result : DENY");
            return DENY;
        }
            
    }

    private boolean isStackTraceNonFirstLine(LoggingEvent event) {
        MDC.remove(INFO);
        if (stderrLoggerName != null && stderrAtLinePrefix != null) {
            if (stderrLoggerName.equalsIgnoreCase(event.getLoggerName())) {

                String msg = event.getMessage().toString().trim();
                if (msg.startsWith(stderrAtLinePrefix)) {
                    //not 1st line
                    return true;
                } else {
                    //1st line
                    MDC.put(INFO, stderrFirstLineInfoText);
                }
            }
        }
        return false;
    }

    public long getNumberOfEvents() {
        return numberOfEvents;
    }

    public void setNumberOfEvents(long numberOfEvents) {
        this.numberOfEvents = numberOfEvents;
    }

    public long getTimeWindowMs() {
        return timeWindowMs;
    }

    public void setTimeWindowMs(long timeWindowMs) {
        this.timeWindowMs = timeWindowMs;
    }

    public String getStderrLoggerName() {
        return stderrLoggerName;
    }

    public void setStderrLoggerName(String stderrLoggerName) {
        this.stderrLoggerName = stderrLoggerName;
    }

    public String getStderrAtLinePrefix() {
        return stderrAtLinePrefix;
    }

    public void setStderrAtLinePrefix(String atLinePrefix) {
        this.stderrAtLinePrefix = atLinePrefix;
    }

    public String getStderrFirstLineInfoText() {
        return stderrFirstLineInfoText;
    }

    public void setStderrFirstLineInfoText(String stderr1stLineInfoText) {
        this.stderrFirstLineInfoText = stderr1stLineInfoText;
    }

    public boolean isStderrCheckEnabled() {
        return stderrCheckEnabled;
    }

    public void setStderrCheckEnabled(boolean stderrCheckEnabled) {
        this.stderrCheckEnabled = stderrCheckEnabled;
    }
    
    

}
