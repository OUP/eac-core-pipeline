package com.oup.eac.service.exceptions;


public class NoRegisterableProductFoundException extends Exception {

    /**
     * Default constructor.
     */
    public NoRegisterableProductFoundException() {
        super();
    }

    /**
     * @param message
     *            the message Constructs a new exception with the specified detail message.
     */
    public NoRegisterableProductFoundException(final String message) {
        super(message);
    }

}
