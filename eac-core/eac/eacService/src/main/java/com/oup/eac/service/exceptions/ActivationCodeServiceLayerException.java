package com.oup.eac.service.exceptions;

import com.oup.eac.service.ServiceLayerException;


/**
 * The Class ActivationCodeServiceLayerException.
 */
public class ActivationCodeServiceLayerException extends ServiceLayerException {

    /**
     * Instantiates a new activation code exception.
     *
     * @param message the message
     * @param cause the cause
     */
    public ActivationCodeServiceLayerException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new activation code exception.
     *
     * @param message the message
     */
    public ActivationCodeServiceLayerException(String message) {
        super(message);
    }

}
