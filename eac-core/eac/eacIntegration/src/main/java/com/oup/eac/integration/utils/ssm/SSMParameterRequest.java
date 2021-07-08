package com.oup.eac.integration.utils.ssm;

import java.util.ArrayList;

public class SSMParameterRequest {

	ArrayList<String> Names = new ArrayList<String>();
	private boolean WithDecryption;

	public boolean getWithDecryption() {
		return WithDecryption;
	}

	public ArrayList<String> getNames() {
		return Names;
	}

	public void setNames(ArrayList<String> names) {
		Names = names;
	}

	public void setWithDecryption(boolean WithDecryption) {
		this.WithDecryption = WithDecryption;
	}
}