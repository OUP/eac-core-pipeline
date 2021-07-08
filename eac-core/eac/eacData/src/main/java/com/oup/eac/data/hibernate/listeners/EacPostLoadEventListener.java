package com.oup.eac.data.hibernate.listeners;


import org.apache.log4j.Logger;
import org.hibernate.event.PostLoadEvent;
import org.hibernate.event.PostLoadEventListener;

import com.oup.eac.domain.Customer;
import com.oup.eac.domain.Customer.CustomerType;

/**
 * We shouldn't have any SHARED users who have their resetPassword flag set to true.
 * 
 * This listener will log warnings if it finds any.
 * 
 * @author David Hay
 */
public class EacPostLoadEventListener implements PostLoadEventListener {

	private static final Logger LOG = Logger.getLogger(EacPostLoadEventListener.class);
	
	@Override
	public void onPostLoad(PostLoadEvent postLoadEvent) {
		if(postLoadEvent != null){
			Object entity = postLoadEvent.getEntity();
			if (entity instanceof Customer) {
				Customer cust = (Customer) entity;
				if (cust.getCustomerType() == CustomerType.SHARED && cust.isResetPassword()) {
					String msg = String.format("Customer with username[%s] has customerType[%s] and resetPassword[%b]", cust.getUsername(), cust.getCustomerType()/*, cust.isResetPassword()*/);
					LOG.warn(msg);
				}
			}
		}
	}

}
