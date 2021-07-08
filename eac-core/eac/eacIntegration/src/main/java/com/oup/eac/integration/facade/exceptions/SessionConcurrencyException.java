/**
 * 
 */
package com.oup.eac.integration.facade.exceptions;

/**
 * @author harlandd
 * 
 */
public class SessionConcurrencyException extends ErightsException {

    /**
     * Create an InvalidCredentialsException with a message and a code.
     * 
     * @param errorMessage
     *            The error message
     * @param code
     *            The erights error code returned
     */
    public SessionConcurrencyException(final String errorMessage, final String code) {
        super(errorMessage, code);
    }
}
