package com.oup.eac.data.message;


import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.joda.time.Minutes;
import org.joda.time.Period;
import org.synyx.messagesource.InitializableMessageSource;

import com.oup.eac.domain.utils.audit.EacApp;

/**
 * Used to reload the localized messages from the database periodically.
 *  
 * @author David Hay
 *
 */
public class DbMessageSourceReloader {

    private static final Logger LOG = Logger.getLogger(DbMessageSourceReloader.class);
    
    private List<InitializableMessageSource> messageSources;
    
    private boolean first = true;
    
    @PostConstruct
    public void init(){
        String delayMsg = getDelayString();
        String msg = String.format("DbMessageSourceReloader initialised delay=[%s]",delayMsg);
        LOG.debug(msg);
    }
    
    private String description;
    
    
    private boolean enabled;
    
    //for debugging only - this does not control the realoader.
    private long delayMs;
    
    /**
     * This method is called periodically by the spring task scheduler.
     */
    public synchronized void onMessageSourceReload(){
        
        logMessage();
        if(!first){
            if(messageSources == null){
                LOG.warn("No Message Sources to initialise");
            }else{
                for(InitializableMessageSource messageSource : messageSources){
                    messageSource.initialize();
                }
            }
        }
        first = false;
    }
    
    public synchronized List<InitializableMessageSource> getMessageSources() {
        return messageSources;
    }

    public synchronized void setMessageSources(List<InitializableMessageSource> messageSources) {
        this.messageSources = messageSources;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public long getDelayMs() {
        return delayMs;
    }

    public void setDelayMs(long delayMs) {
        this.delayMs = delayMs;        
    }

    private String getDelayString() {
        Period p = new Period(this.delayMs);
        int minutes =  Minutes.standardMinutesIn(p).getMinutes();        
        String result = String.format("%d ms/%d minutes",delayMs,minutes);         
        return result;
    }

    private void logMessage(){
        if(LOG.isDebugEnabled() == false){
            return;
        }
        String delayString = getDelayString();
        String msg = String.format("DbMessageSourceReloader event : 1st[%s] desc[%s] delay=[%s]",first,description,delayString);
        LOG.debug(msg);
    }

    public String getEacType() {        
        return EacApp.getType().name();
    }
    
}
