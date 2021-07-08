package com.oup.eac.ws.rest.v1;

import java.io.Serializable;


public class EnrolUserRequest implements Serializable{
	
private static final long serialVersionUID = 1L;
	
	protected OupUserEnrollWS user;
	protected OupGroupDetailsWS groupDetails;
	protected String requestorId;
	public OupUserEnrollWS getUser() {
		return user;
	}
	public void setUser(OupUserEnrollWS user) {
		this.user = user;
	}
	public OupGroupDetailsWS getGroupDetails() {
		return groupDetails;
	}
	public void setGroupDetails(OupGroupDetailsWS groupDetails) {
		this.groupDetails = groupDetails;
	}
	public String getRequestorId() {
		return requestorId;
	}
	public void setRequestorId(String requestorId) {
		this.requestorId = requestorId;
	}
	
	

}
