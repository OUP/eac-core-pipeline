package com.oup.eac.common.utils;

import java.util.Locale;

import org.apache.log4j.Logger;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

public class MessageTextSource {

    private static Logger LOG = Logger.getLogger(MessageTextSource.class);
    
    private static final Object[] EMPTY_ARGS = new Object[0];

    private Locale locale;
    private MessageSource messageSource;

    public MessageTextSource(MessageSource messageSource, Locale locale) {
        this.messageSource = messageSource;
        this.locale = locale;
    }

    public String getString(String key) {
        String result;
        try{
            result = messageSource.getMessage(key, EMPTY_ARGS, locale);
        }catch(NoSuchMessageException ex){
            LOG.warn("No message found key " + key,ex);
            result = key;
        }
        return result;
    }

}
