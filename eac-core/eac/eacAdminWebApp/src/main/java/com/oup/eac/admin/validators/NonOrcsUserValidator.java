package com.oup.eac.admin.validators;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.oup.eac.admin.beans.CustomerBean;
import com.oup.eac.common.utils.crypto.PasswordUtils;
import com.oup.eac.common.utils.email.InternationalEmailAddress;

@Component
public class NonOrcsUserValidator{

	public void validate(Object target, List<String> errors) {
		// TODO Auto-generated method stub
		CustomerBean customerBean=(CustomerBean)target;
		if (StringUtils.isBlank(customerBean.getCustomer().getEmailAddress())) {
			errors.add("The Email Address is Blank");
		}
		if (!InternationalEmailAddress.isValid(customerBean.getCustomer().getEmailAddress())) {
			errors.add("The Email Address is Invalid");
		}
		if (!StringUtils.isBlank((customerBean.getPassword()))){
			if(null!=validate(customerBean.getPassword(), customerBean.getPasswordAgain())){
				errors.add(validate(customerBean.getPassword(), customerBean.getPasswordAgain()));
			}
		}else{
			errors.add("Password is Blank");
		}
	}
	
	private static String validate(final String password, final String confirmPassword) {
		if (StringUtils.isBlank(password)) {
			return "Password Required";
		}
		if (StringUtils.isBlank(confirmPassword)) {
			return "Password Again Required";
		}
		if (!StringUtils.equals(password, confirmPassword)) {
			return "Password Doesnot Match";
		}
		if (!PasswordUtils.isPasswordValid(password)) {
			System.out.println("Inside the Password check");
			return "The password is not valid";
		}
		return null;
	}

}
