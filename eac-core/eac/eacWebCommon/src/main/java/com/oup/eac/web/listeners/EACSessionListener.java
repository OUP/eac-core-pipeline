package com.oup.eac.web.listeners;

import java.util.Collections;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import com.oup.eac.domain.BaseDomainObject;

public class EACSessionListener implements HttpSessionListener {
    
    private static final Logger LOGGER = Logger.getLogger(EACSessionListener.class);
    
    private static final String SESSION_KEY_VALUE = "Session key: %s\tvalue: %s";
    
    private static final String DATE_TIME_PATTERN = "dd/MM/yyyy HH:mm:ss";
 
    public void sessionCreated(HttpSessionEvent event) {

    }
 
    public void sessionDestroyed(HttpSessionEvent event) {
        HttpSession session = event.getSession(); 
        if(session != null) {
            if(LOGGER.isDebugEnabled()) LOGGER.debug(getSessionValues(session));
        }
    }
    
    @SuppressWarnings("unchecked")
    private String getSessionValues(HttpSession session) {
        StringBuilder sessionString = new StringBuilder("Session Destruction:\n");
        sessionString.append("Session Id: ").append(session.getId()).append("\n");
        sessionString.append("Session Creation time: ").append(new DateTime(session.getCreationTime()).toString(DATE_TIME_PATTERN)).append("\n");
        sessionString.append("Session Last Accessed time: ").append(new DateTime(session.getLastAccessedTime()).toString(DATE_TIME_PATTERN)).append("\n");
        for(Object o : Collections.list(session.getAttributeNames())) {
            String key = (String)o;
            sessionString.append(String.format(SESSION_KEY_VALUE, key, getValue(session.getAttribute(key)))).append("\n");
        }
        return sessionString.toString();
    }
    
    private String getValue(Object o) {
        if(o == null) {
            return null;
        }
        if(o instanceof BaseDomainObject) {
            StringBuilder value = new StringBuilder();
            value.append(o.getClass().getSimpleName()).append(" id: ").append(((BaseDomainObject)o).getId());
            return value.toString();
        }
        return o.toString();
    }
}
