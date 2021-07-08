package com.oup.eac.service.exceptions;

import com.oup.eac.dto.Message;
import com.oup.eac.service.ServiceLayerException;

public class LicenceNotFoundServiceLayerException extends ServiceLayerException {
	 /**
     * Default constructor.
     */
    public LicenceNotFoundServiceLayerException() {
        super();
    }

    /**
     * @param message
     *            the message Constructs a new exception with the specified detail message.
     */
    public LicenceNotFoundServiceLayerException(final String message) {
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
    public LicenceNotFoundServiceLayerException(final String message, final Message... messages) {
        super(message, messages);
    }

    /**
     * @param message
     *            the message
     * @param cause
     *            the cause Constructs a new exception with the specified detail message and cause.
     */
    public LicenceNotFoundServiceLayerException(final String message, final Throwable cause) {
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
    public LicenceNotFoundServiceLayerException(final String message, final Throwable cause, final Message... messages) {
        super(message, cause, messages);
    }
}
