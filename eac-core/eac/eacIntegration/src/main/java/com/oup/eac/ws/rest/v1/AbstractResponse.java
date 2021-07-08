package com.oup.eac.ws.rest.v1;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Abstract Response
 * 
 */
public class AbstractResponse implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 12344L;

    /** The status. */
    protected String status;

    /** The error message. */
    protected String errorMessage;

    /** The error messages. */
    protected Map<String, String> errorMessages;

    /**
     * Gets the error messages.
     *
     * @return the error messages
     */
    public Map<String, String> getErrorMessages() {
        if (errorMessages == null) {
            errorMessages = new HashMap<String, String>();
        }
        return errorMessages;
    }

    /**
     * Sets the error messages.
     *
     * @param errorMessagesMap
     *            the error messages map
     */
    public void setErrorMessages(Map<String, String> errorMessagesMap) {
        this.errorMessages = errorMessagesMap;
    }

    /**
     * Gets the value of the status property.
     * 
     * @return possible object is String
     * 
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *            allowed object is String
     * 
     */
    public void setStatus(String value) {
        this.status = value;
    }

    /**
     * Gets the value of the errorMessage property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getErrorMessage() {
        return errorMessage;
    }

}
