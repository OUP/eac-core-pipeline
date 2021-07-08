package com.oup.eac.integration.utils.authenticatication;

import java.util.HashMap;

public class AuthenticateResponse {

	private String status;
	private String message;
	private HashMap<String,String> data;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public HashMap<String, String> getData() {
		return data;
	}
	public void setData(HashMap<String, String> data) {
		this.data = data;
	}
}
