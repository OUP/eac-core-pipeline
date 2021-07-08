package com.oup.eac.common.utils.crypto;

import junit.framework.TestCase;

import org.junit.Test;

public class PasswordGeneratorTest extends TestCase {

    final String PASSWORD_PATTERN="([a-z])([A-Z])([!@#$%])([0-9]){6,15}";
    /**
     * Generate some random passwords and ensure they all
     * conform to the password pattern.
     */
    @Test
    public final void testGeneratedPasswordIsValid() {
        
    	String password = PasswordGenerator.createPassword(PASSWORD_PATTERN);
        System.out.println("Checking password strength for generated password " + password);
        boolean matcher = PASSWORD_PATTERN.matches(password);
        assertEquals(matcher, false);
    }
}
