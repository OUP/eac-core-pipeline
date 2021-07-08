package com.oup.eac.common.utils.crypto;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

/**
 * Simple AES encryption and decryption utility class.
 * 
 */
public final class SimpleCipher {
    private static final String AES_CIPHER = "AES/ECB/PKCS5Padding";
    private static final String PROVIDER = "SunJCE";
    private static final String AES = "AES";
    private static final int LINE_LENGTH = 32;
    private static final String UTF8 = "UTF-8";
    /**
     * A simple text cipher to encrypt/decrypt a string.
     */
    private static byte[] linebreak = {}; // Remove Base64 encoder default linebreak
    private static String secret = "oupTh3M4TR1Xeac1"; // secret key length must be 16
    private static SecretKey key;
    private static Cipher cipher;
    private static Base64 coder;
    private static Cipher rsaCipher;

    static {
        try {
            key = getSecretKey(secret);
            cipher = Cipher.getInstance(AES_CIPHER, PROVIDER);
            coder = new Base64(LINE_LENGTH, linebreak, true);
            rsaCipher = Cipher.getInstance("RSA");
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    /**
     * Hidden construction.
     */
    private SimpleCipher() {

    }

    /**
     * Encrypt.
     * 
     * @param plainText
     *            plain text string
     * @return encrypted text
     * @throws Exception
     *             encryption exception
     */
    private static synchronized String encrypt(final String plainText, final SecretKey secretKey) throws Exception {
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] cipherText = cipher.doFinal(getBytes(plainText));
        return getString(coder.encode(cipherText));
    }

    /**
     * Decrypt.
     * 
     * @param codedText
     *            encrypted text
     * @return plain text
     * @throws Exception
     *             decryption exception
     */
    private static synchronized String decrypt(final String codedText, SecretKey secretKey) throws Exception {
        byte[] encypted = coder.decode(getBytes(codedText));
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decrypted = cipher.doFinal(encypted);
        return getString(decrypted);
    }

    public static synchronized String encrypt(final String plainText) throws Exception {
        return encrypt(plainText, key);
    }
    
    public static synchronized String decrypt(final String codedText) throws Exception {
        return decrypt(codedText, key);
    }
    
    public static synchronized String encrypt(final String plainText, String secret) throws Exception {
        SecretKey secretKey = getSecretKey(secret);
        return encrypt(plainText, secretKey);
    }
    
    private static SecretKey getSecretKey(final String secret) {
        if(secret.length() != 16){
            throw new IllegalArgumentException("The length of the AES shared secret must be 16");
        }
        SecretKey result = new SecretKeySpec(secret.getBytes(), AES);
        return result;
    }

    public static synchronized String decrypt(final String codedText, String secret) throws Exception {
        SecretKey secretKey = getSecretKey(secret);
        return decrypt(codedText, secretKey);
    }
    
    private static String getString(byte[] bytes) throws UnsupportedEncodingException{
        return new String(bytes,UTF8);
    }
    private static byte[] getBytes(String data) throws UnsupportedEncodingException{
        return data.getBytes(UTF8);
    }
    
	/**
	 * Method will return encrypted value of given plainText using given public key
	 * @param plainText
	 * @param publicKeyInString
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * @throws InvalidKeyException
	 * @throws UnsupportedEncodingException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	public static synchronized String encryptTextRSA(final String plainText,
			String publicKeyInString) throws NoSuchAlgorithmException,
			InvalidKeySpecException, InvalidKeyException,
			UnsupportedEncodingException, IllegalBlockSizeException,
			BadPaddingException {
		String cipherStr = null;
		KeyFactory keyFactory = null;
		X509EncodedKeySpec keySpec = null;

		byte[] keyBytes = Base64.decodeBase64(publicKeyInString.getBytes());
		keySpec = new X509EncodedKeySpec(keyBytes);
		keyFactory = KeyFactory.getInstance("RSA");
		PublicKey publicKey = keyFactory.generatePublic(keySpec);
		rsaCipher.init(Cipher.ENCRYPT_MODE, publicKey);
		cipherStr = new String(Base64.encodeBase64(rsaCipher.doFinal(plainText
				.getBytes())), "UTF-8");
		return cipherStr;
	}
}
