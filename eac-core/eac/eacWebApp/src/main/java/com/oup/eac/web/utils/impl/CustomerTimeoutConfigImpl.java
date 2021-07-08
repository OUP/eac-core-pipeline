package com.oup.eac.web.utils.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.oup.eac.web.utils.CustomerTimeoutConfig;

@Component("webCustomerTimeoutConfig")
public class CustomerTimeoutConfigImpl implements CustomerTimeoutConfig {

	private final int customerTimeoutMins;

	@Autowired
	public CustomerTimeoutConfigImpl(@Value("${web.customer.timeout.mins}") final int customerTimeoutMins){
		this.customerTimeoutMins = customerTimeoutMins;
	}
	
	@Override
	public int getCustomerTimeoutMins() {
		return customerTimeoutMins;
	}
	
}
