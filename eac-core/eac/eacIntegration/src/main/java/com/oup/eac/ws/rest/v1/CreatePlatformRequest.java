package com.oup.eac.ws.rest.v1;

import java.io.Serializable;


public class CreatePlatformRequest implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3073476159564241789L;
	private String code;
	private String name;
	private String description;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}