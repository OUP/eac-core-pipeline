package com.oup.eac.common.utils.username;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.oup.eac.common.utils.username.impl.UsernameValidatorImpl;

public class UsernameValidatorTest {

    //private static final String INVALID_REGEX = "\\<.*?\\>";// NO ANGLE BRACKETS
    private static final String usernamePolicyRegex="^(?=^(?:(?!<.*?>).)*$)\\S{5,255}$";
    private UsernameValidator validator;

    @Before
    public void setup() {
        validator = new UsernameValidatorImpl(usernamePolicyRegex);
    }

   /* @Test
    public void testBadConfig() {
        checkBadConfig(0, 1, true, INVALID_REGEX);
        checkBadConfig(2, 1, true, INVALID_REGEX);
    }*/

   /* @Test
    @SuppressWarnings("unused")
    public void testBadPattern() {
        try {
            new UsernameValidatorImpl(usernamePolicyRegex, "{\"user_id\" : [0-9]*}");
            Assert.fail("exception expected");
        } catch (java.lang.Throwable th) {
            Assert.assertTrue(th instanceof java.lang.IllegalArgumentException);
            IllegalArgumentException ill = (IllegalArgumentException) th;
            Assert.assertEquals(PatternSyntaxException.class, ill.getCause().getClass());
        }
    }*/

   /* @SuppressWarnings("unused")
    private void checkBadConfig(final int min, final int max, final boolean isWhiteSpaceInvalid, final String invalidRegex) {
        try {
            new UsernameValidatorImpl(min, max, isWhiteSpaceInvalid, invalidRegex);
            Assert.fail("exception expected");
        } catch (java.lang.Throwable th) {
            Assert.assertTrue(th instanceof java.lang.IllegalArgumentException);
            Assert.assertTrue(th.getMessage().contains("[Assertion failed"));
        }
    }*/

    @Test
    public void testMinLength() {
        Assert.assertFalse(validator.isValid("bob"));
    }

    
    @Test
    public void testMaxLength() {
        Assert.assertFalse(validator.isValid("bobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrstbobBOBbobBOBabcdefghijklmnopqrst"));
    }

    @Test
    public void testMinLengthOkay() {
        Assert.assertTrue(validator.isValid("bobob"));
    }

    @Test
    public void testMaxLengthOkay() {
        Assert.assertTrue(validator.isValid("bobobBOBOB"));
    }

    @Test
    public void testWhitespace1() {
        Assert.assertFalse(validator.isValid("bob BOB"));
    }

    @Test
    public void testWhitespace2() {
        Assert.assertFalse(validator.isValid("bob\tBOB"));
    }

    @Test
    public void testWhitespace3() {
        Assert.assertFalse(validator.isValid("bob\nBOB"));
    }

    @Test
    public void testWhitespace4() {
        Assert.assertFalse(validator.isValid("bob\rBOB"));
    }

    @Test
    public void testWhitespaceLineFeed() {
    	char lineFeed = (char)10;
    	checkWhiteSpace(lineFeed);

    	char verticalTab = (char)11;
    	checkWhiteSpace(verticalTab);

    	char formFeed = (char)12;
    	checkWhiteSpace(formFeed);

    	char carRet = (char)13;
    	checkWhiteSpace(carRet);
    }


    @Test
    public void testNoWhitespace() {
        Assert.assertTrue(validator.isValid("NoWSpace"));
    }



    private void checkWhiteSpace(char c ){
    	StringBuffer sb = new StringBuffer();
    	sb.append("bob");
    	sb.append(c);
    	sb.append("bob");
    	int d= (int)c;
    	Assert.assertFalse("the character " + d + "is not identified as whitespace",validator.isValid(sb.toString()));
    }

    @Test
    public void testOkay() {
        Assert.assertTrue(validator.isValid("bobbuilder"));
    }

    @Test
    public void testInvalidWithAngleBrackets1() {
        Assert.assertFalse(validator.isValid("<script1>bob</script2>"));
    }

    @Test
    public void testInvalidWithAngleBrackets2() {
        Assert.assertFalse(validator.isValid("<script1>"));
    }

    /*@Test
    public void testValidWithAngleBrackets() {
        UsernameValidator validator1 = new UsernameValidatorImpl(usernamePolicyRegex, "");
        Assert.assertTrue(validator1.isValid("<script1>"));
    }*/

    @Test
    public void testInvalidWhenUsernameIsNull() {
    	Assert.assertFalse(validator.isValid(null));
    }
}