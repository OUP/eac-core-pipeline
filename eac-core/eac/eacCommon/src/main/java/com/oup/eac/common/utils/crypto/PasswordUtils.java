package com.oup.eac.common.utils.crypto;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.omg.CORBA.UserException;
import org.springframework.context.MessageSource;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.kms.AWSKMSClient;
import com.amazonaws.services.kms.model.DecryptRequest;
import com.amazonaws.services.kms.model.EncryptRequest;
import com.amazonaws.services.kms.model.InvalidCiphertextException;
import com.oup.eac.common.utils.AwsClientConfiguration;
import com.oup.eac.common.utils.EACSettings;

/**
 * @author David Hay
 */
public abstract class PasswordUtils {
	/**
	 * A password is valid
	 * <ul>
	 * <li>if it is at least 6 characters in length</li>
	 * <li>and it is at most 15 characters in length</li> ------ Remvoded for
	 * CR16 on 18-Apr-2013 by Chirag Joshi
	 * <li>and it contains NO whitespace characters
	 * <em>- added 13/Dec/2011 by David Hay</em></li>
	 * <li>and it contains at least 1 digit</li>------ Remvoded for CR16 on
	 * 18-Apr-2013 by Chirag Joshi
	 * <li>and it contains at least 1 lowercase letter in the range a-z</li>
	 * <li>and it contains at least l uppercase letter in the range A-Z</li>
	 * </ul>
	 */
	public static Pattern PASSWORD_PATTERN;
	public static final String INVALID_PASSWORD_MSG_CODE = "error.password.strength";
	public static final String DEFAULT_INVALID_PASSWORD_MSG = "The password is not valid";
	private static final Logger LOG = Logger.getLogger(PasswordUtils.class);
	private static AWSKMSClient kms = null;

	public static boolean isPasswordValid(String candidatePassword) {
		boolean isValid = false;
		try {
			String passwordRegex = EACSettings
					.getProperty(EACSettings.PASSWORD_POLICY_REGEX);
			PASSWORD_PATTERN = Pattern.compile(passwordRegex);
			Matcher matcher = PASSWORD_PATTERN.matcher(candidatePassword);
			isValid = matcher.find();
		} catch (PatternSyntaxException pse) {
			LOG.error("Invalid Password Regex.");
			throw new IllegalArgumentException("invalid password Regex.", pse);
		}
		return isValid;
	}

	public static String getInvalidPasswordMessage(MessageSource src,
			Object... args) {
		String message = src.getMessage(INVALID_PASSWORD_MSG_CODE,
				(Object[]) args, DEFAULT_INVALID_PASSWORD_MSG, (Locale) null);
		return message;
	}

	public static AWSKMSClient getKMSCredentials() {
		if (kms == null) {
			synchronized (AWSKMSClient.class) {
				if (kms == null) {
					ClientConfiguration clientConfig = AwsClientConfiguration
							.getClientConfiguration();
					kms = new AWSKMSClient(
							new BasicAWSCredentials(
									EACSettings
											.getProperty(EACSettings.AWS_KMS_PASSWORD_ACCESSKEY),
									EACSettings
											.getProperty(EACSettings.AWS_KMS_PASSWORD_SECRETKEY)),
							clientConfig)
							.withEndpoint(EACSettings
									.getProperty(EACSettings.AWS_KMS_PASSWORD_ENDPOINT));
				}
			}
		}
		return kms;
	}

	/**
	 * getDecryptedCode.
	 * 
	 * @param encryted
	 * @return String
	 * @throws UserException
	 */
	public static String getKMSDecryptedCode(final String encryted)
			throws Exception {
		try {
			AWSKMSClient kms = getKMSCredentials();
			byte[] c = Base64.decodeBase64(encryted.getBytes());
			final ByteBuffer ciphertext2 = ByteBuffer.wrap(c);
			// Decrypt the data
			final DecryptRequest decReq1 = new DecryptRequest();
			decReq1.setCiphertextBlob(ciphertext2);
			final ByteBuffer decrypted = kms.decrypt(decReq1).getPlaintext();
			final String decryptedStr = new String(decrypted.array(),
					StandardCharsets.UTF_8);

			return decryptedStr;
		} catch (InvalidCiphertextException e) {
			LOG.error(getStackTrace(e));
			throw new Exception("Password is corrupted");

		} catch (Exception e) {
			LOG.error(getStackTrace(e));
			throw new Exception(e.getMessage());
		}

	}

	/**
	 * getEncryptedCode.
	 * 
	 * @param plainText
	 * @return String
	 * @throws Exception 
	 */
	public static String getKMSEncryptedCode(final String plainText)
			throws Exception {

		final String keyId = EACSettings
				.getProperty(EACSettings.AWS_KMS_PASSWORD_KEYID);

		try {
			AWSKMSClient kms = getKMSCredentials();
			final byte[] plaintextBytes = plainText
					.getBytes(StandardCharsets.UTF_8);

			// Encrypt the data
			final EncryptRequest encReq = new EncryptRequest();
			encReq.setKeyId(keyId);
			encReq.setPlaintext(ByteBuffer.wrap(plaintextBytes));
			final ByteBuffer ciphertext = kms.encrypt(encReq)
					.getCiphertextBlob();

			byte[] b = ciphertext.array();
			String encrypted = Base64.encodeBase64String(b);

			return encrypted;

		} catch (AmazonServiceException e) {

			LOG.error(getStackTrace(e));
			throw new Exception(e.getMessage());

		}
	}
	
	public static String getStackTrace(final Throwable e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String sStackTrace = sw.toString();
        return sStackTrace;
    }

}
