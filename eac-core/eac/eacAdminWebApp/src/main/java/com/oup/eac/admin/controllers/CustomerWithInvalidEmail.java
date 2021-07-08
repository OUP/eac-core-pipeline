package com.oup.eac.admin.controllers;

import java.net.IDN;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.oup.eac.common.utils.email.InternationalEmailAddress;
import com.oup.eac.domain.Customer;
import com.oup.eac.service.CustomerService;

@Controller
@RequestMapping("/mvc/invalidEmail.htm")
public class CustomerWithInvalidEmail {

	
	@Autowired
	private CustomerService customerService; 
	private static final Logger LOG = Logger.getLogger(CustomerWithInvalidEmail.class);
	private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$", Pattern.CASE_INSENSITIVE);	
	private static final String AT_SYMBOL = "@";
	
	@RequestMapping(method = RequestMethod.GET)
	public void findInvalidEmailAddress(){ 
		try{
		 List<Customer> customer = customerService.getCustomerInvalidEmailAddress(); 
//				 customerService.getCustomerInvalidEmailAddress();
		 for (Iterator iterator = customer.iterator(); iterator.hasNext();) {
			Customer customer2 = (Customer) iterator.next();
			validateTransformEmail(customer2);
			customerService.saveCleansedEmail(customer2);
			LOG.info("The Customer Id is ::  "+customer2.getId()+" the email address is "+customer2.getEmailAddress());
		}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	private void validateTransformEmail(final Customer customer2){

		String emailAddress=customer2.getEmailAddress();
		boolean valid = emailAddress != null;
		final int atPosition = emailAddress.indexOf(AT_SYMBOL);
        valid = atPosition > 0;
        final String localPart = emailAddress.substring(0, atPosition);
        final String domain = emailAddress.substring(atPosition + 1);
        LOG.info("The Value of email in DB  "+emailAddress);
        // handle IDNs by punycode conversion to ASCII Compatible Encoding (ACE)
        // before performing regex check
        try {
            final String localPartAce = IDN.toASCII(localPart);
            String domainAce = IDN.toASCII(domain);
            while (domainAce.endsWith(".")){
                domainAce = IDN.toASCII(domainAce);
            }            
            final String email = localPartAce + AT_SYMBOL + domainAce;
            LOG.info("Internationalized Adderess "+InternationalEmailAddress.convertToAscii(email));
            final Matcher matcher = EMAIL_PATTERN.matcher(InternationalEmailAddress.convertToAscii(email));
            valid =  matcher.find();
            if(valid){
            	customer2.setEmailAddress(InternationalEmailAddress.convertToAscii(email));
            }
        } catch (Exception e) {                
            LOG.warn("Unable to convert email address", e);
            valid = false;
        }
    
	}
}
