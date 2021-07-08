package com.oup.eac.admin.beans;

import java.io.Serializable;

public class RegistrationStateBean implements Serializable {

	private static final long serialVersionUID = 1031358521560207643L;

	private boolean activate;
	private boolean sendEmail = true;
	private Boolean validate;
	private String allowDenySelected="";

	public boolean isActivate() {
		return activate;
	}

	public void setActivate(boolean activate) {
		this.activate = activate;
	}

	public boolean isSendEmail() {
		return sendEmail;
	}

	public void setSendEmail(boolean sendEmail) {
		this.sendEmail = sendEmail;
	}

	public Boolean getValidate() {
		return validate;
	}

	public void setValidate(Boolean validate) {
		this.validate = validate;
	}
	
	public String getAllowDenySelected() {
		return allowDenySelected;
	}

	public void setAllowDenySelected(String allowDenySelected) {
		this.allowDenySelected = allowDenySelected;
	}

	@Override
	public String toString() {
		return "RegistrationStateBean [activate=" + activate + ", sendEmail=" + sendEmail + ", validate=" + validate + "]";
	}

}
