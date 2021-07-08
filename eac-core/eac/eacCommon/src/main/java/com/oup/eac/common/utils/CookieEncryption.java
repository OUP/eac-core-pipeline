package com.oup.eac.common.utils;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

import org.apache.log4j.Logger;

import org.apache.commons.codec.binary.Base64;

/**
 * Utility to encode and decode values for eRights cookie. Based on sample in Atypon Adapter Installation Guide. Note no padding mode set for encryption in
 * guide.
 * 
 */
public class CookieEncryption {
    private static final Logger LOG = Logger.getLogger(CookieEncryption.class);

    private static final String ENCRYPT_ALG = "DESede";
    private static final int LINE_LENGTH = 32;
    private static Base64 base64;
    private static byte[] linebreak = {}; // Remove Base64 encoder default
    // linebreak

    static {
        base64 = new Base64(LINE_LENGTH, linebreak, true);
    }

    /**
     * Constructor.
     */
    public CookieEncryption() {
        super();
    }

    /**
     * Encode token.
     * 
     * @param tokenKey
     *            key
     * @param plainValue
     *            value
     * @return encrypted value
     */
    public final String encodeToken(final String tokenKey, final String plainValue) {
        String encryptedValue = null;
        LOG.debug("Encode Key:" + tokenKey + " Value:" + plainValue);
        try {
            final SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ENCRYPT_ALG);

            final DESedeKeySpec keySpec = new DESedeKeySpec(base64.decode(tokenKey));
            final Key key = keyFactory.generateSecret(keySpec);
            final Cipher des = Cipher.getInstance(ENCRYPT_ALG);
            des.init(Cipher.ENCRYPT_MODE, key);
            final byte[] encrypted = des.doFinal(plainValue.getBytes());
            encryptedValue = base64.encodeToString(encrypted);

            LOG.debug("Encode Key:" + tokenKey + " Value:" + encryptedValue);

        } catch (Exception e) {
            LOG.error("Unable to encode token", e);
        }
        return encryptedValue;
    }

    /**
     * Decode token.
     * 
     * @param tokenKey
     *            key
     * @param encryptedValue
     *            value
     * @return plain value
     */
    public final String decodeToken(final String tokenKey, final String encryptedValue) {
        String plainValue = null;
        LOG.debug("Decode Key:" + tokenKey + " Value:" + encryptedValue);
        try {
            final SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ENCRYPT_ALG);
            final DESedeKeySpec keySpec = new DESedeKeySpec(base64.decode(tokenKey));
            final Key key = keyFactory.generateSecret(keySpec);
            final Cipher des = Cipher.getInstance(ENCRYPT_ALG);
            des.init(Cipher.DECRYPT_MODE, key);
            final byte[] decrypted = des.doFinal(base64.decode(encryptedValue));
            plainValue = new String(decrypted);
            LOG.debug("Decode Key:" + tokenKey + " Value:" + plainValue);
        } catch (Exception e) {
            LOG.error("Unable to decode token", e);
        }
        return plainValue;
    }

    /**
     * Generate key.
     * 
     * @return Base 64 encoded key
     */
    public final String generateKey() {
        String keyValue = null;
        final SecureRandom random = new SecureRandom();
        KeyGenerator keygen = null;
        try {
            keygen = KeyGenerator.getInstance("DESede");
            keygen.init(random);
            final Key key = keygen.generateKey();
            keyValue = base64.encodeToString(key.getEncoded());
            LOG.debug("Key:" + keyValue);
        } catch (NoSuchAlgorithmException e) {
            LOG.error("Unable to generate key", e);
        }
        return keyValue;
    }
}
