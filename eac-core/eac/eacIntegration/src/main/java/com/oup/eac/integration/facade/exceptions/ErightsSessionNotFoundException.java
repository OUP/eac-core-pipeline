/**
 * 
 */
package com.oup.eac.integration.facade.exceptions;

/**
 * @author harlandd
 * 
 */
public class ErightsSessionNotFoundException extends ErightsException {

    /**
     * Create an SessionNotFoundException.
     * 
     * @param errorMessage
     *            The error message
     */
    public ErightsSessionNotFoundException(final String errorMessage) {
        super(errorMessage);
    }

    /**
     * Create an SessionNotFoundException with a message and a code.
     * 
     * @param errorMessage
     *            The error message
     * @param code
     *            The erights error code returned
     */
    public ErightsSessionNotFoundException(final String errorMessage, final String code) {
        super(errorMessage, code);
    }
}
