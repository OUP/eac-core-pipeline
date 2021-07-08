package com.oup.eac.service;

import com.oup.eac.dto.Message;

public class InvalidCredentialsServiceLayerException extends ServiceLayerException {

    /**
     * Default constructor.
     */
    public InvalidCredentialsServiceLayerException() {
        super();
    }

    /**
     * @param message
     *            the message Constructs a new exception with the specified detail message.
     */
    public InvalidCredentialsServiceLayerException(final String message) {
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
    public InvalidCredentialsServiceLayerException(final String message, final Message... messages) {
        super(message, messages);
    }

    /**
     * @param message
     *            the message
     * @param cause
     *            the cause Constructs a new exception with the specified detail message and cause.
     */
    public InvalidCredentialsServiceLayerException(final String message, final Throwable cause) {
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
    public InvalidCredentialsServiceLayerException(final String message, final Throwable cause, final Message... messages) {
        super(message, cause, messages);
    }
}
