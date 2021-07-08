package com.oup.eac.integration.utils.ssm;

import java.util.ArrayList;

public class SSMParameterResponse {
	ArrayList < Object > InvalidParameters = new ArrayList < Object > ();
	ArrayList < SSMParameterDescription > Parameters = new ArrayList < SSMParameterDescription > ();
	public ArrayList<Object> getInvalidParameters() {
		return InvalidParameters;
	}
	public void setInvalidParameters(ArrayList<Object> invalidParameters) {
		InvalidParameters = invalidParameters;
	}
	public ArrayList<SSMParameterDescription> getParameters() {
		return Parameters;
	}
	public void setParameters(ArrayList<SSMParameterDescription> parameters) {
		Parameters = parameters;
	}


}

class SSMParameterDescription {
	private String ARN;
	private float LastModifiedDate;
	private String Name;
	private String Type;
	private String Value;
	private float Version;


	// Getter Methods 

	public String getARN() {
		return ARN;
	}

	public float getLastModifiedDate() {
		return LastModifiedDate;
	}

	public String getName() {
		return Name;
	}

	public String getType() {
		return Type;
	}

	public String getValue() {
		return Value;
	}

	public float getVersion() {
		return Version;
	}

	// Setter Methods 

	public void setARN(String ARN) {
		this.ARN = ARN;
	}

	public void setLastModifiedDate(float LastModifiedDate) {
		this.LastModifiedDate = LastModifiedDate;
	}

	public void setName(String Name) {
		this.Name = Name;
	}

	public void setType(String Type) {
		this.Type = Type;
	}

	public void setValue(String Value) {
		this.Value = Value;
	}

	public void setVersion(float Version) {
		this.Version = Version;
	}

}
