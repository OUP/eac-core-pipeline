package com.oup.eac.integration.facade.exceptions;

public class PasswordPolicyViolatedException extends ErightsException {
	
    public PasswordPolicyViolatedException(final String errorMessage) {
        super(errorMessage);
    }
    public PasswordPolicyViolatedException(final String errorMessage, final String code) {
        super(errorMessage, code);
    }
    
    
    /*@Override
    public  String getMessage() {
    	
        return super.getMessage();
    }*/
   
}
