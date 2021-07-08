package com.oup.eac.common.utils.crypto;

import com.mifmif.common.regex.Generex;
import com.oup.eac.common.utils.EACSettings;

/**
 * @author Vaibhav
 * 
 *         Class to generate random passwords.
 */
public final class PasswordGenerator {

    /**
     * Default constructor.
     */
    private PasswordGenerator() {

    }

    /**
     * @return random password at default length
     */
    public static String createPassword() {
    	String regex=EACSettings.getProperty(EACSettings.PASSWORD_POLICY_RESETFORGET_REGEX);
        return createPassword(regex);
    }

    public static String createPassword(final String regex) {
    	Generex generex = new Generex(regex);
        String password = generex.random();	
        return password;
    }

}