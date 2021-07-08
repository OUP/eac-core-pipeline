package com.oup.eac.ws.rest.v1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class OupUserEnrollWS implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 12343L;

	/** The auth profile. */
	protected List<OupAuthProfileWS> authProfile;

	/** The suspended. */
	protected String suspended;
	/** The first name. */
	protected String firstName;

	/** The last name. */
	protected String lastName;
	
	protected String isTnCAccepted;
	protected String concurrency;
	protected String privacyPolicyAccepted;
	public List<OupAuthProfileWS> getAuthProfile() {
		if(authProfile == null){
			authProfile =new ArrayList<OupAuthProfileWS>();
		}
		return authProfile;
	}
	public void setAuthProfile(List<OupAuthProfileWS> authProfile) {
		this.authProfile = authProfile;
	}
	public String getSuspended() {
		return suspended;
	}
	public void setSuspended(String suspended) {
		this.suspended = suspended;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getIsTnCAccepted() {
		return isTnCAccepted;
	}
	public void setIsTnCAccepted(String isTnCAccepted) {
		this.isTnCAccepted = isTnCAccepted;
	}
	public String getConcurrency() {
		return concurrency;
	}
	public void setConcurrency(String concurrency) {
		this.concurrency = concurrency;
	}
	public String getPrivacyPolicyAccepted() {
		return privacyPolicyAccepted;
	}
	public void setPrivacyPolicyAccepted(String privacyPolicyAccepted) {
		this.privacyPolicyAccepted = privacyPolicyAccepted;
	}
	
	
}
