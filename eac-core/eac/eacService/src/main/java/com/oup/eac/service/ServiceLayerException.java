package com.oup.eac.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.oup.eac.dto.Message;

public class ServiceLayerException extends Exception {

    private final List<Message> messages = new ArrayList<Message>();

    /**
     * Default constructor.
     */
    public ServiceLayerException() {
        super();
    }

    /**
     * @param message
     *            the message Constructs a new exception with the specified detail message.
     */
    public ServiceLayerException(final String message) {
        super(message);
    }

    /**
     * Create a service exception with a main message and nested messages.
     * 
     * @param message
     *            the message
     * @param messages
     *            the messages
     */
    public ServiceLayerException(final String message, final Message... messages) {
        super(message);
        this.messages.addAll(Arrays.asList(messages));
    }

    /**
     * @param message
     *            the message
     * @param cause
     *            the cause Constructs a new exception with the specified detail message and cause.
     */
    public ServiceLayerException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * 
     * @param message
     *            the message
     * @param e
     *            the cause
     * @param messages
     *            the nested messages
     */
    public ServiceLayerException(final String message, final Throwable e, final Message... messages) {
        super(message, e);
        this.messages.addAll(Arrays.asList(messages));
    }

    /**
     * Gets the messages set for this exception.
     * 
     * @return messages
     */
    public final List<Message> getMessages() {
        return Collections.unmodifiableList(messages);
        
    }
    
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(super.toString());
        addMessages(stringBuilder);
        return stringBuilder.toString();
    }
    
    private void addMessages(StringBuilder stringBuilder) {
        for(Message message : getMessages()) {
            if(stringBuilder.length() > 0) {
                stringBuilder.append("\n");
            }
            if(StringUtils.isNotBlank(message.getDefaultMessage())) {
            	stringBuilder.append("\tMessage: ").append(message.getDefaultMessage());
            }
            stringBuilder.append("\tMessageKey: ").append(message.getMessageKey());
        }
    }
}
