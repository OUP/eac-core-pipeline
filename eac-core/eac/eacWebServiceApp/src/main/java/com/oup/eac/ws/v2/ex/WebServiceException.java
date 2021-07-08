package com.oup.eac.ws.v2.ex;

/**
 * The Class WebServiceLayerException.
 */
public class WebServiceException extends Exception {

    /**
     * Instantiates a new web service layer exception.
     *
     * @param message the message
     * @param cause the cause
     */
    public WebServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new web service layer exception.
     *
     * @param message the message
     */
    public WebServiceException(String message) {
        super(message);
    }

    
}
