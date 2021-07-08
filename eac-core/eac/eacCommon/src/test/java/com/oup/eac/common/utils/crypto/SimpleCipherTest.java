/**
 * 
 */
package com.oup.eac.common.utils.crypto;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test the SimpleCipher and make sure it round trips successfully.
 * 
 * @author packardi
 * 
 */
public class SimpleCipherTest {

    /**
     * Test encrypting and decrypting using SimpleCipher.
     * 
     * @throws Exception
     *             the exception
     */
    @Test
    public final void encrypt() throws Exception {

        String original = "This is some plain text.";
        String encrypted = SimpleCipher.encrypt(original);
        String decrypted = SimpleCipher.decrypt(encrypted);

        assertEquals("Check same after encrypt/decrypt", original, decrypted);
    }
    
    @Test
    public final void encryptWithSecret() throws Exception {
        String secret = "abcdEFGHijklMNOP";
        String original1 = "This is some plain text.";
        String original2 = "This cat sat on the mat.";
        
        String enc1 = encrypt(original1, secret);
        String enc2 = encrypt(original2, secret);
        Assert.assertEquals("hFtkuzOm90u5rwz5uav6pyzueV0WIJtO9z8XwwC0njc",enc1);
        Assert.assertEquals("y2upIz3p7A-HuGfTZ1C2YOs4KicioT6PcZGEC_WVhWY",enc2);
    }
    
    @Test
    public final void encryptBlankWithSecret() throws Exception {
        String secret = "abcdEFGHijklMNOP";
        Assert.assertEquals("dHZTSl8FwvgC2nOK7Dl4zw",SimpleCipher.encrypt("",secret));
        Assert.assertEquals("",SimpleCipher.decrypt("",secret));
    }

    private String encrypt(String original, String secret) throws Exception{
        String encrypted = SimpleCipher.encrypt(original,secret);
        System.out.println("plain text     : " + original);
        System.out.println("encrypted text : " + encrypted);
        String decrypted = SimpleCipher.decrypt(encrypted,secret);       
        assertEquals("Check same after encrypt/decrypt", original, decrypted);
        return encrypted;
    }
}
