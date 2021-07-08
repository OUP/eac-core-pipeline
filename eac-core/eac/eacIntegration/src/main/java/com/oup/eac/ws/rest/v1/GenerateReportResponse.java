package com.oup.eac.ws.rest.v1;

import java.io.Serializable;

public class GenerateReportResponse extends AbstractResponse implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
