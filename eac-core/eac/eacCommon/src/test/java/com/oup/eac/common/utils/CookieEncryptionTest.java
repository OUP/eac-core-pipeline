/**
 * 
 */
package com.oup.eac.common.utils;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author douglass
 * 
 */
public class CookieEncryptionTest {

    /**
     * Test method for {@link com.oup.eac.common.utils.CookieEncryption#encodeToken(java.lang.String, java.lang.String)} .
     */
    @Test
    public void testEncodeToken() {
        CookieEncryption util = new CookieEncryption();
        String plainValue = "Semper ubi sub ubi";
        String key = util.generateKey();
        String encryptedValue = util.encodeToken(key, plainValue);
        assertNotNull(encryptedValue);

        String value = util.decodeToken(key, encryptedValue);
        assertEquals(plainValue, value);
    }

}
