package com.oup.eac.integration.utils.datasync;

import org.apache.log4j.Logger;

import com.oup.eac.common.utils.spring.ApplicationContextSupport;
import com.oup.eac.dto.CustomerDto;

/**
* <h1>User Data Synchronizer between EAC and CEB!</h1>
* The UserDataSychronisable program implements functionality which are thread capable
* and will make calls to CEB rest service to update user metadata and password for a user.
* <p>
* @author  Subhasis Das
* @version 1.0
* @since   2020-10-30 
*/
public class UserDataSychronisable implements Runnable{
	
	private static final Logger LOGGER = Logger.getLogger(UserDataSychronisable.class);
	
	private String userName;
	private String userId;
	private String password;
	private CustomerDto customerDto;
	public UserDataSychronisable(String userName,String userId,
			String password,CustomerDto customerDto){
		this.userName = userName;
		this.userId = userId;
		this.password = password;
		this.customerDto = customerDto;
	}
	
	public UserDataSychronisable() {
		super();
	}
	
	protected UserDataSyncService userDataSyncService = (UserDataSyncService) ApplicationContextSupport.getBean("userDataSyncService");
	
	@Override
	public void run() {		
		try {
			userDataSyncService.syncUser(userName, userId, password, customerDto);			
		} catch (Exception e) {
			LOGGER.error("User Data Synchronization failded due to ",e);
		}
	}

}
