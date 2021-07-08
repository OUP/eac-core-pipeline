package com.oup.eac.integration.utils.kms;

import java.nio.charset.StandardCharsets;

import org.apache.log4j.Logger;
import org.apache.commons.codec.binary.Base64;

import java.nio.ByteBuffer;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.kms.AWSKMSClient;
import com.amazonaws.services.kms.model.DecryptRequest;
import com.amazonaws.services.kms.model.EncryptRequest;
import com.oup.eac.common.utils.AwsClientConfiguration;
import com.oup.eac.common.utils.EACSettings;
import com.oup.eac.integration.utils.sqs.SqsUtility;

public class KMSUtility {

	private static Logger logger = Logger.getLogger(SqsUtility.class);

	private static AWSKMSClient kms = null;

	public static AWSKMSClient getKMSCredentials() {
		if(kms == null){
			synchronized (AWSKMSClient.class) {
				if(kms == null){
					ClientConfiguration clientConfig = AwsClientConfiguration.getClientConfiguration();
					kms = new  AWSKMSClient(new BasicAWSCredentials(EACSettings.getProperty(EACSettings.AWS_KMS_PASSWORD_ACCESSKEY),
							EACSettings.getProperty(EACSettings.AWS_KMS_PASSWORD_SECRETKEY)), clientConfig)
					.withEndpoint(EACSettings.getProperty(EACSettings.AWS_KMS_PASSWORD_ENDPOINT));
				}
			}


		}
		return kms;
	}

	public static String getDecryptedCode(final String encryted){
		String decryptedStr = "";
		try {
			AWSKMSClient kms = getKMSCredentials();
			byte[] c = Base64.decodeBase64(encryted.getBytes());
			final ByteBuffer ciphertext2 = ByteBuffer.wrap(c);
			// Decrypt the data
			final DecryptRequest decReq1 = new DecryptRequest();
			decReq1.setCiphertextBlob(ciphertext2);
			final ByteBuffer decrypted = kms.decrypt(decReq1).getPlaintext();
			 decryptedStr = new String(decrypted.array(), StandardCharsets.UTF_8);
			
		} catch (Exception e) {
			logger.error("error in getDecryptedCode",e);
		}
		return decryptedStr;
	}

	public static String getEncryptedCode(final String plainText){

		final String keyId = EACSettings.getProperty(EACSettings.AWS_KMS_PASSWORD_KEYID);
		String encrypted = "";
		try {
			AWSKMSClient kms = getKMSCredentials();
			final byte[] plaintextBytes = plainText.getBytes(StandardCharsets.UTF_8);


			// Encrypt the data
			final EncryptRequest encReq = new EncryptRequest();
			encReq.setKeyId(keyId);
			encReq.setPlaintext(ByteBuffer.wrap(plaintextBytes));
			final ByteBuffer ciphertext = kms.encrypt(encReq).getCiphertextBlob();

			byte[] b = ciphertext.array();
			encrypted = Base64.encodeBase64String(b);

		} catch (AmazonServiceException e) {	    		
			logger.error("error in getDecryptedCode",e);
		}
		return encrypted;
	}
}
