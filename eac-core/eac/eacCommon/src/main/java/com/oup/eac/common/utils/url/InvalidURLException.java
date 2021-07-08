package com.oup.eac.common.utils.url;


public class InvalidURLException extends Exception {

	/*
     * Default constructor.
     */
    public InvalidURLException() {
        super();
    }

    /**
     * @param message
     *            the message Constructs a new exception with the specified detail message.
     */
    public InvalidURLException(final String message) {
        super(message);
    }

    /**
     * @param message
     *            the message
     * @param cause
     *            the cause Constructs a new exception with the specified detail message and cause.
     */
    public InvalidURLException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
