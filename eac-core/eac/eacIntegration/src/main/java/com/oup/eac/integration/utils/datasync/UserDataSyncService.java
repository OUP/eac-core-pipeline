package com.oup.eac.integration.utils.datasync;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.amazonaws.services.sqs.model.MessageAttributeValue;
import com.oup.eac.common.utils.json.GsonUtil;
import com.oup.eac.dto.CustomerDto;
import com.oup.eac.dto.LoginPasswordCredentialDto;
import com.oup.eac.integration.facade.ErightsFacade;
import com.oup.eac.integration.rest.template.AcesRestTemplate;
import com.oup.eac.integration.utils.kms.KMSUtility;
import com.oup.eac.integration.utils.sqs.SqsUtility;

public class UserDataSyncService {

	@Autowired
	AcesRestTemplate acesRestTemplate;

	@Autowired
	SqsUtility sqsUtility;
	
	@Autowired
	ErightsFacade erightsFacade; 

	private Logger logger = Logger.getLogger(UserDataSyncService.class);

	public void syncUser(String userName,String userId,String password,CustomerDto customerDto) {
		LoginPasswordCredentialDto loginPasswordCredentialDto = null;
		String encryptedPassword = "";
		Boolean sendToSQS = false;
		try { 
			//send the encrypted password with null and empty checks
			if(password != null && !password.equalsIgnoreCase("")){
				encryptedPassword = KMSUtility.getEncryptedCode(password);
			}else if (customerDto !=null && customerDto.getLoginPasswordCredential().getPassword() != null
								&& !customerDto.getLoginPasswordCredential().getPassword().equalsIgnoreCase("")){
				encryptedPassword = KMSUtility.getEncryptedCode(customerDto.getLoginPasswordCredential().getPassword());
			}
			/*when data sync is happening due to change password by user name call*/
			if(userName != null && !userName.equalsIgnoreCase("")){
				customerDto = erightsFacade.getUserAccountByUsername(userName);
				loginPasswordCredentialDto = new LoginPasswordCredentialDto(userName,encryptedPassword);
				customerDto.setLoginPasswordCredential(loginPasswordCredentialDto);
			/*when data sync is happening due to change password by user id call*/
			}else if(userId !=null && !userId.equalsIgnoreCase("")){
				customerDto = erightsFacade.getUserAccount(userId);
				loginPasswordCredentialDto = new LoginPasswordCredentialDto(customerDto.getLoginPasswordCredential().getUsername(),encryptedPassword);
				customerDto.setLoginPasswordCredential(loginPasswordCredentialDto);				
			}else{
				loginPasswordCredentialDto = new LoginPasswordCredentialDto(customerDto.getLoginPasswordCredential().getUsername(),encryptedPassword);
				customerDto.setLoginPasswordCredential(loginPasswordCredentialDto);
			}
			
			DataSyncResponse dataSyncResponse=acesRestTemplate.postUserDataSync(customerDto);
			
			if (dataSyncResponse != null && !"success".equalsIgnoreCase(dataSyncResponse.getStatus())){
				sendToSQS = true;
				throw new Exception(" API cals failed for user data sync userName::"+userName+"userId ::"+userId);
			}			
		} catch(Exception e) {
			logger.error("Error while claing the sync data user ", e);
			if(sendToSQS){
				/*retry the same request message via sqs operation*/
				postSQSMessage(customerDto);
			}
		}
		
	}
	
	private void postSQSMessage(CustomerDto customerDto) {
		Map<String, MessageAttributeValue> messageAttributesMap = new HashMap<String, MessageAttributeValue>();
		try{
			String customerMetaDataJson = GsonUtil.objectToJsonWithDateFormat(customerDto);
			
			MessageAttributeValue messageAttributeValue = new MessageAttributeValue();
			messageAttributeValue.setDataType("String");
			messageAttributeValue.setStringValue("USER_DATA_CHANGED");
			messageAttributesMap.put("USER_DATA_CHANGED", messageAttributeValue);
			sqsUtility.setValuesInQueue(messageAttributesMap,customerMetaDataJson);
			
		}catch(Exception e) {
			logger.error("Error while claing the sync data user ", e);
		}

	}	


}
