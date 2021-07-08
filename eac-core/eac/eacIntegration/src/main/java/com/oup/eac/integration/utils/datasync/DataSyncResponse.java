package com.oup.eac.integration.utils.datasync;

import java.util.HashMap;

public class DataSyncResponse {

	private String status;
	private String message;
	private HashMap<String,Object> data;
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
	public HashMap<String, Object> getData() {
		return data;
	}
	public void setData(HashMap<String, Object> data) {
		this.data = data;
	}
}
