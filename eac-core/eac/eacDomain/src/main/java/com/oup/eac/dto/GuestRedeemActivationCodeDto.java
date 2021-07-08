package com.oup.eac.dto;

import java.io.Serializable;
import java.util.List;

import com.oup.eac.domain.ExternalProductId;

public class GuestRedeemActivationCodeDto implements Serializable{
	private String activationCode;
	private String productId ;
	private String productName ;
	private List<ExternalProductId> externalProductId = null ;
	
	public List<ExternalProductId> getExternalProductId() {
		return externalProductId;
	}
	public void setExternalProductId(List<ExternalProductId> externalProductId) {
		this.externalProductId = externalProductId;
	}
	public String getActivationCode() {
		return activationCode;
	}
	public void setActivationCode(String activationCode) {
		this.activationCode = activationCode;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	
}
