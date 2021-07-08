package com.oup.eac.admin.controllers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.oup.eac.admin.actions.CustomerAction;
import com.oup.eac.admin.beans.CustomerBean;
import com.oup.eac.admin.validators.NonOrcsUserValidator;
import com.oup.eac.common.RuntimeContext;
import com.oup.eac.common.utils.spring.ApplicationContextSupport;
import com.oup.eac.common.utils.username.UsernameValidator;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.Customer.CustomerType;
import com.oup.eac.domain.Password;


@Controller
@RequestMapping("/orcsUser.htm")
public class NonOrcUserCreationController {

	private static final Logger LOG = Logger.getLogger(NonOrcUserCreationController.class);
	@Autowired
	CustomerAction customerAction;
	@Autowired
	UsernameValidator usernameValidator;
	
	@Autowired
	NonOrcsUserValidator nonOrcsUserValidator;
	
   private static RuntimeContext runtimeContext;
	static{
	
	}
	public NonOrcUserCreationController() {
		 runtimeContext = (RuntimeContext) ApplicationContextSupport.getBean("runtimeContext");
	}

	private RuntimeContext getRuntimeContext() {
	        return runtimeContext;
	    }
	

	public UsernameValidator getUsernameValidator() {
		return usernameValidator;
	}

	public void setUsernameValidator(UsernameValidator usernameValidator) {
		this.usernameValidator = usernameValidator;
	}
	
	 

	@RequestMapping(method = RequestMethod.GET)
	public void createCustomer(){
		System.out.println("Inside the NonOrcUserCreationController START----");
		List<CustomerBean> lstOfCustomerBean=new ArrayList<CustomerBean>();
		CustomerBean customerBean=null;
		Customer customer=null;
		runtimeContext=getRuntimeContext();
		String strUser=new String("nonOrcs.user");
		String value=null;
		String[] valAfterSplit=null;
		String noOfUser=runtimeContext.getProperty(String.valueOf("nonOrcs.user.migration.numberOfUser"));
		for(int i=1;i<=Integer.valueOf(noOfUser);i++){
			strUser.concat(String.valueOf(i));
			System.out.println("The strUser is "+strUser);
			value=runtimeContext.getProperty(String.valueOf(strUser+String.valueOf(i)));
			valAfterSplit=value.split("::");
			for (int j = 0; j < valAfterSplit.length; j++) {
				customerBean=new CustomerBean();
				customer=customerBean.getCustomer();
				System.out.println("The Value is "+valAfterSplit[j]);
				customer.setUsername(valAfterSplit[j]);
				customer.setPassword(new Password(valAfterSplit[j+1],false));
				customerBean.setPassword(customer.getPassword());
				customerBean.setPasswordAgain(customer.getPassword());
				customer.setCustomerType(CustomerType.SHARED);//j+2
				customer.setFirstName(valAfterSplit[j+3]);
				customer.setFamilyName(valAfterSplit[j+4]);
				customer.setEmailAddress(valAfterSplit[5]);
				customer.setLocale(new Locale("en","GB"));
				customer.setTimeZone("Europe/London");
				customer.setEnabled(true);
				lstOfCustomerBean.add(customerBean);
				break;
				
			}
			
		}
		
		System.out.println("The lstOfCustomerBean size is  "+lstOfCustomerBean.size());
		String strRet=null;
		Boolean flagToCreate=runtimeContext.getBoolProperty("nonOrcs.user.migration.flag");
		Boolean validateRequired=runtimeContext.getBoolProperty("nonOrcs.user.migration.validation.required");
		List<String> lstOfError=new ArrayList<String>();
		String msg=null;
		if(flagToCreate){
			for (Iterator iterator = lstOfCustomerBean.iterator(); iterator
					.hasNext();) {
				CustomerBean customerBean2 = (CustomerBean) iterator.next();
				if(validateRequired){
					nonOrcsUserValidator.validate(customerBean2, lstOfError);	
				}
				System.out.println("The LstOfUser   "+lstOfError.isEmpty()+" the size is "+lstOfError.size());
				if(lstOfError.isEmpty()){
					strRet=customerAction.saveOrcsCustomer(customerBean2);	
					LOG.info("Customer Created "+customerBean2.getCustomer().getUsername());
				}else{
					LOG.error("Customer not created "+customerBean2.getCustomer().getUsername());
					for (Iterator iterator2 = lstOfError.iterator(); iterator2
							.hasNext();) {
						msg = (String) iterator2.next();
						LOG.error(msg);
					}
				}
				
				
			}
		}
		System.out.println("Inside the NonOrcUserCreationController SUCCESS----"+strRet);
	}
}
