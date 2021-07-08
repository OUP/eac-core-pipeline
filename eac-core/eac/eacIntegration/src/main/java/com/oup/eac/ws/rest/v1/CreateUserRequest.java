package com.oup.eac.ws.rest.v1;

import java.io.Serializable;

public class CreateUserRequest implements Serializable {
	
private static final long serialVersionUID = 1L;
	
	protected OupUserEnrollWS user;
	public OupUserEnrollWS getUser() {
		return user;
	}
	public void setUser(OupUserEnrollWS user) {
		this.user = user;
	}

}
