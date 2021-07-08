/**
 * 
 */
package com.oup.eac.integration.facade.exceptions;

/**
 * @author harlandd
 * 
 */
public class UserAlreadyExistsException extends ErightsException {

    public UserAlreadyExistsException(final String errorMessage) {
        super(errorMessage);
    }
}
