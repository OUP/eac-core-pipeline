package com.oup.eac.ws.rest.v1;

import java.io.Serializable;


public class DeletePlatformRequest implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String code;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
