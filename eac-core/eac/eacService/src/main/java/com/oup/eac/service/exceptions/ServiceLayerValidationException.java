package com.oup.eac.service.exceptions;

import com.oup.eac.dto.Message;
import com.oup.eac.service.ServiceLayerException;
/**
 * A ServiceLayerException for general validation errors.
 * @author David Hay
 */
public class ServiceLayerValidationException extends ServiceLayerException {

	private String value;
	
	public ServiceLayerValidationException() {
        super();
    }

    public ServiceLayerValidationException(String message, Message... messages) {
        super(message, messages);
    }

    public ServiceLayerValidationException(String message, Throwable e, Message... messages) {
        super(message, e, messages);
    }

    public ServiceLayerValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceLayerValidationException(String message) {
        super(message);
    }
    
    public ServiceLayerValidationException(String message, String value) {
    	super(message);
    	this.value=value;
    }

    public String getValue(){
    	return value;
    }
}
