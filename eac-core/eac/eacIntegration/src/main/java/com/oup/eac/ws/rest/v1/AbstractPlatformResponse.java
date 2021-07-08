package com.oup.eac.ws.rest.v1;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.oup.eac.integration.erights.UserAccountResponseSTATUS;


public class AbstractPlatformResponse implements Serializable {
	
	/** The Constant serialVersionUID. */
    private static final long serialVersionUID = 12344L;

    /** The status. */
    //protected String status;
    protected String status;

	/** The error message. */
    protected String errorMessage;

	/** The error messages. */
    protected Map<String, String> errorMessages;
    
    /** The error codes. */
    protected String errorCode;
    
    /**
     * Gets the value of the status property.
     * 
     * @return
     *     possible object is
     *     {@link PlatformSTATUS }
     *     
     */
    
    public String getStatus() {
		return status;
	}
    
    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link PlatformSTATUS }
     *     
     */
	public void setStatus(String status) {
		this.status = status;
	}


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
     * Gets the value of the errorCode property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * Sets the value of the errorCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setErrorCode(String value) {
        this.errorCode = value;
    }
    
    public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}
