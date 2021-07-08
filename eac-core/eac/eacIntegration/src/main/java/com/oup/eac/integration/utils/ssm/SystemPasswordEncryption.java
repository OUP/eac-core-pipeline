package com.oup.eac.integration.utils.ssm;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;

import com.oup.eac.integration.utils.ssm.SSMParameterUtility.SSMPARAMETERNAMES;

public final class SystemPasswordEncryption {    

    private static Cipher rsaCipher;    
    private static PublicKey publicKey;
    
    //initialization
    static {
        try {
        	// publicKeyValue needs to be stored in property file or configurable
            String publicKeyValue = SSMParameterUtility.getParameterValue(SSMPARAMETERNAMES.ENCRYPT_PUBLIC_KEY.name());
            rsaCipher = Cipher.getInstance("RSA");            
            byte[] publicBytes = Base64.decodeBase64(publicKeyValue);
    		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);
    		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    		publicKey = keyFactory.generatePublic(keySpec);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
    
    // This method will encrypt using public key
    public static String encryptText(String msg) {
    	try {
			rsaCipher.init(Cipher.ENCRYPT_MODE, publicKey);
			return Base64.encodeBase64String(rsaCipher.doFinal(msg.getBytes("UTF-8")));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
    
    // for testing 
    public static void main(String[] args){
    	try {
			System.out.println(encryptText("k3c5tf2g"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
  }
