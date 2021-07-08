package com.oup.eac.dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/**
 * @author harlandd Message for configuring message keys and args
 */
public class Message {

    public enum MessageType {
        MESSAGE, FIELD_MESSAGE;
    }

    private final String messageKey;

    private final String defaultMessage;

    private final List<Object> args = new ArrayList<Object>();

    /**
     * @param messageKey
     *            the messageKey
     */
    public Message(final String messageKey) {
        this(messageKey, null);
    }

    /**
     * @param messageKey
     *            the messageKey
     * @param defaultMessage
     *            the defaultMessage
     */
    public Message(final String messageKey, final String defaultMessage) {
        this(messageKey, defaultMessage, null);
    }

    /**
     * @param messageKey
     *            the messageKey
     * @param defaultMessage
     *            the defaultMessage
     * @param args
     *            the args
     */
    public Message(final String messageKey, final String defaultMessage, final Object[] args) {
        super();
        this.messageKey = messageKey;
        this.defaultMessage = defaultMessage;
        if(args != null) this.args.addAll(Arrays.asList(args));
    }

    /**
     * @return the messageKey
     */
    public final String getMessageKey() {
        return messageKey;
    }

    /**
     * @return the args
     */
    public final List<Object> getArgs() {
    	return Collections.unmodifiableList(args);
    }

    /**
     * @return the defaultMessage
     */
    public final String getDefaultMessage() {
        return defaultMessage;
    }

    /**
     * @return the message type
     */
    public MessageType getMessageType() {
        return MessageType.MESSAGE;
    }

}
