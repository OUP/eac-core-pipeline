package com.oup.eac.admin.beans;

import com.oup.eac.domain.Customer;
import com.oup.eac.domain.ProductRegistrationDefinition;
import com.oup.eac.domain.RegistrationDefinition;
import com.oup.eac.dto.EnforceableProductDto;

public class AddRegistrationBean {

	private RegistrationDefinition registrationDefinition;
	private Customer customer;
	private boolean activate = true;
	private boolean sendEmail = true;
	private boolean validate = true;
	private boolean isAdmin=false;
	
	private ProductRegistrationDefinition productRegistrationDefinition ;
	
	private EnforceableProductDto product ; 
	
	public RegistrationDefinition getRegistrationDefinition() {
		return registrationDefinition;
	}

	public void setRegistrationDefinition(final RegistrationDefinition registrationDefinition) {
		this.registrationDefinition = registrationDefinition;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(final Customer customer) {
		this.customer = customer;
	}

	public boolean isActivate() {
		return activate;
	}

	public void setActivate(final boolean activate) {
		this.activate = activate;
	}

	public boolean isSendEmail() {
		return sendEmail;
	}

	public void setSendEmail(final boolean sendEmail) {
		this.sendEmail = sendEmail;
	}

	public boolean isValidate() {
		return validate;
	}

	public void setValidate(boolean validate) {
		this.validate = validate;
	}

	public boolean getIsAdmin(){
		return isAdmin;
	}
	
	public void setIsAdmin(boolean isAdmin){
		System.out.println("Inside the isAdmin of AddRegistrationBean   "+isAdmin);
		this.isAdmin=isAdmin;
	}
	
	
	
	public EnforceableProductDto getProduct() {
		return product;
	}

	public void setProduct(EnforceableProductDto product) {
		this.product = product;
	}
	
	
	
	public ProductRegistrationDefinition getProductRegistrationDefinition() {
		return productRegistrationDefinition;
	}

	public void setProductRegistrationDefinition(
			ProductRegistrationDefinition productRegistrationDefinition) {
		this.productRegistrationDefinition = productRegistrationDefinition;
	}

	@Override
	public String toString() {
		return "AddRegistrationBean [registrationDefinition=" + registrationDefinition + ", customer=" + customer + ", activate=" + activate + ", sendEmail="
				+ sendEmail + ", validate=" + validate + "]";
	}
	
}
