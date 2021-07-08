package com.oup.eac.ws.v2.ex;

/**
 * The Class WebServiceLayerException.
 */
public class WebServiceValidationException extends WebServiceException {

    /**
     * Instantiates a new web service layer exception.
     *
     * @param message the message
     * @param cause the cause
     */
    public WebServiceValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new web service layer exception.
     *
     * @param message the message
     */
    public WebServiceValidationException(String message) {
        super(message);
    }

    
}
