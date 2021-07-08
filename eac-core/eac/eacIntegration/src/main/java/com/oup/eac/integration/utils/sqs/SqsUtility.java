package com.oup.eac.integration.utils.sqs;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.MessageAttributeValue;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.oup.eac.common.utils.EACSettings;
import com.oup.eac.integration.utils.authenticatication.AuthenticateUser;
import com.oup.eac.integration.utils.authenticatication.UserCredentials;
import com.oup.eac.integration.utils.ssm.SSMParameterUtility;
import com.oup.eac.integration.utils.ssm.SSMParameterUtility.SSMPARAMETERNAMES;
import com.oup.eac.integration.utils.ssm.SystemPasswordEncryption;

public class SqsUtility {


	private static Logger logger = Logger.getLogger(SqsUtility.class);



	@Autowired
	AuthenticateUser authenticateUser;


	/**Create SQS Connection.
	 * @return AmazonSQSClient
	 */
	private  AmazonSQSClient createConnection() {
		AmazonSQSClient sqs_client = null;
		try{
			Regions region = Regions.valueOf(EACSettings.getProperty(EACSettings.AWS_REGION));
			String trustedUsername = SSMParameterUtility.getParameterValue(SSMPARAMETERNAMES.EAC_TRUSTED_SYSTEM_USERNAME.name());
			String trustedPassword = SSMParameterUtility.getParameterValue(SSMPARAMETERNAMES.EAC_TRUSTED_SYSTEM_PASSWORD.name());
			String encryptedPassword = SystemPasswordEncryption.encryptText(trustedPassword);
			Map<String,String> resp = authenticateUser.getSessionCredentials(new UserCredentials(trustedUsername, encryptedPassword));
			String accessKeyId = resp.get("AccessKeyId");
			String accessSecretKey = resp.get("SecretAccessKey");
			String sessionToken = resp.get("SessionToken");


			BasicSessionCredentials sessionCredentials = new BasicSessionCredentials(
					accessKeyId,
					accessSecretKey,
					sessionToken);
			/** Proxy Configuration **/
			ClientConfiguration clientConfig = new ClientConfiguration();  	     	    
			sqs_client = new AmazonSQSClient(sessionCredentials, clientConfig).withRegion(region);    	    

		}catch(Exception ex){
			logger.error("AWS SQS Cleint creattion error", ex);
		}
		return sqs_client;
	}



	/**sqsCallToSetValuesInQueue.
	 * @param jsonString
	 * @param emailHostName
	 * @param path
	 * @param queueName
	 * @param event
	 * 
	 */
	public void setValuesInQueue(Map<String, MessageAttributeValue> messageAttributesMap,
			String queueMessageBody) {
		try{
			AmazonSQSClient sqs_client =createConnection(); 
			logger.info("Message requested for  " + queueMessageBody);
			String queueUrl  = sqs_client.getQueueUrl(SSMParameterUtility.getParameterValue(SSMPARAMETERNAMES.DATASYNC_SQS_NAME.name())).getQueueUrl();
			SendMessageRequest sendMsgRequest = 
					new SendMessageRequest().withQueueUrl(queueUrl)
												.withMessageAttributes(messageAttributesMap)
													.withMessageBody(queueMessageBody)
														.withDelaySeconds(5);
			SendMessageResult sendMessageResult = sqs_client.sendMessage(sendMsgRequest);
			logger.info("Messsage sent to queue with Message Id " + sendMessageResult.getMessageId());
		}catch(Exception e){
			logger.error("Messsage faild to send to queue in setValuesInQueue",e);
		}

	}



}
