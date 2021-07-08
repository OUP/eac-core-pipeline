package com.oup.eac.domain.utils.audit;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;


public class TestingAppender extends AppenderSkeleton {

    //We are collecting all the log messages in to a list
    private List<LoggingEvent> messages = new ArrayList<LoggingEvent>();

    // This method is called when ever any logging happens
    // We are simply taking the log message and adding to our list
    @Override
    protected void append(LoggingEvent event) {
      messages.add(event);
    }

    //This method is called when the appender is closed.
    //Gives an opportunity to clean up resources
    public void close() {
      messages.clear();
    }

    public boolean requiresLayout() {
      return false;
    }

    public List<LoggingEvent> getMessages() {
      return messages;
    }

    public void clear() {
      messages.clear();
    }
    
  }
