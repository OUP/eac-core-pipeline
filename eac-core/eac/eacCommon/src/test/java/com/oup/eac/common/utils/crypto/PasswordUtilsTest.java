package com.oup.eac.common.utils.crypto;

import javax.naming.NamingException;

import junit.framework.Assert;

import org.junit.Ignore;
import org.junit.Test;

import com.oup.eac.common.mock.AbstractMockTest;

/**
 * @author David Hay
 */

public class PasswordUtilsTest extends AbstractMockTest{
  
	public PasswordUtilsTest() throws NamingException {
		super();
	}

	@Test
    public void testEmptyIsInvalid(){
        boolean isValid = PasswordUtils.isPasswordValid("");
        Assert.assertFalse(isValid);
    }
    
    @Test
    public void testNullIsInvalid(){
        boolean isValid = PasswordUtils.isPasswordValid("");
        Assert.assertFalse(isValid);
    }
    
    @Test
    public void testWhitespaceIsInvalid(){
        boolean isValid = PasswordUtils.isPasswordValid("    ");
        Assert.assertFalse(isValid);
    }
    
    @Test
    public void testTooShortIsInvalid(){
        //min length is 5
        String password = "aBc1";
        Assert.assertTrue(password.length() == 4);
        boolean isValid = PasswordUtils.isPasswordValid(password);
        Assert.assertFalse(isValid);
    }
    
    @Test
    @Ignore // Ignoring because it's not supported now
    public void testTooLongIsInvalid(){
        //max length is 15
        String password = "1111aaaaPPPP1111";
        Assert.assertTrue(password.length() == 16);
        boolean isValid = PasswordUtils.isPasswordValid(password);
        Assert.assertFalse(isValid);
    }
    
    @Test
    @Ignore // Ignoring because it's not supported now
    public void testNoNumbersIsInvalid(){
        boolean isValid = PasswordUtils.isPasswordValid("abcABC");
        Assert.assertFalse(isValid);
    }
    
    @Test
    public void testNoUpperCaseIsInvalid(){
        boolean isValid = PasswordUtils.isPasswordValid("abc111");
        Assert.assertFalse(isValid);
    }
    
    @Test
    public void testNoLowerCaseIsInvalid(){
        boolean isValid = PasswordUtils.isPasswordValid("111AAA");
        Assert.assertFalse(isValid);
    }
    
    @Test
    public void testValidMinLength(){
        //password must be min 6 characters
        String password = "aaBB11";
        Assert.assertTrue(password.length() == 6);
        boolean isValid = PasswordUtils.isPasswordValid(password);
        Assert.assertTrue(isValid);
    }
    
    @Test
    public void testValidMaxLength(){
        //password must be min 6 character, so no max limit.
        String password = "aaBB11aabbCC123";
        Assert.assertTrue(password.length() == 15);
        boolean isValid = PasswordUtils.isPasswordValid(password);
        Assert.assertTrue(isValid);
    }
    
    @Test
    public void testNonAlphaNumericCharacterType(){
        //password must be between 6 and 15
        String password = "aaBB11aabbCC12_";
        Assert.assertTrue(password.length() == 15);
        boolean isValid = PasswordUtils.isPasswordValid(password);
        Assert.assertTrue(isValid);
    }
    
    @Test
    public void testUnicodeCharacterType(){
        //password must be between 6 and 15
        String password = "\u00e7\u00e8\u00e91aB";
        Assert.assertTrue(password.length() == 6);
        boolean isValid = PasswordUtils.isPasswordValid(password);
        Assert.assertTrue(isValid);
    }
    
    @Test
    public void testPasswordWithWhitespace(){
        //password must be between 6 and 15
        String password = "aaBB11aabbCC12 ";
        Assert.assertTrue(password.length() == 15);
        boolean isValid = PasswordUtils.isPasswordValid(password);
        Assert.assertFalse(isValid);
    }
    
    @Test
    public void testPasswordWithPunctuation(){
        //password must be between 6 and 15
        String password = "!\"£$%^&*()";
        Assert.assertTrue(password.length() == 10);
        boolean isValid = PasswordUtils.isPasswordValid(password);
        Assert.assertFalse(isValid);
    }

}
