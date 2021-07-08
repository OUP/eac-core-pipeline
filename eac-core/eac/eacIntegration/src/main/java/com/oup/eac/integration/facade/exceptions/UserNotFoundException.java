package com.oup.eac.integration.facade.exceptions;

public class UserNotFoundException extends ErightsException {

    public UserNotFoundException(final String errorMessage) {
        super(errorMessage);
    }
    
    public UserNotFoundException(final String errorMessage, final String code, final Integer errorCode) {
        super(errorMessage, code, errorCode);
    }
}
