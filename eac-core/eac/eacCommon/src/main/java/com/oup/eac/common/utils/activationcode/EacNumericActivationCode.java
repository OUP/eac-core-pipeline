package com.oup.eac.common.utils.activationcode;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.lang.RandomStringUtils;

/**
 * Activation Code Generator.
 * 
 * Tokens in the following format:
 * 12 numeric random digits
 * 
 * e.g. 001236547895
 * 
 */
public class EacNumericActivationCode implements ActivationCodeGenerator, ActivationCodeValidator {
    private static final String VALID_CHARS = "0123456789";

    /**
     * Create activation codes.
     * @param prefix not used.
     * @param numTokens number to create.
     * @return Set of codes.
     */
    public Set<String> createActivationCodes(final String prefix, final int numTokens) {
        final Set<String> tokens = new HashSet<String>();
        while (tokens.size() < numTokens) {
            tokens.add(createActivationCode(prefix));
        }
        return tokens;
    }

    /**
     * Create activation code.
     * 
     * @param prefix not used.
     * @return Set of codes.
     */
    public String createActivationCode(final String prefix) {
        return RandomStringUtils.random(12, VALID_CHARS);
    }

    /**
     * Check if code is valid format.
     * @param code activation code
     * @return true - valid
     */
    public boolean isValid(final String code) {
        boolean valid = code != null;
        valid = valid && code.length() == 12;
        valid = valid && isValidCharSet(code);
        return valid;
    }

    /**
     * Determine if all chars are in supported list
     * @param code full activation code
     * @return true - valid
     */
    private boolean isValidCharSet(final String code) {
        boolean valid = true;
        final String body = code.substring(code.indexOf('-') + 1, code.length() - 1);        
        for (int i = 0; valid && i < body.length(); i++) {
            valid = VALID_CHARS.indexOf(body.charAt(i)) != -1;
        }
        return valid;
    }
    
    /**
     * Main run method to create tokens.
     * @param args arg1 number to create.
     */
    public static void main(final String[] args) {
        if (args != null && args.length == 1) {
            final EacNumericActivationCode gen = new EacNumericActivationCode();
            final Set<String> codes = gen.createActivationCodes(null, Integer.parseInt(args[0]));
            for (final Iterator<String> iterator = codes.iterator(); iterator.hasNext();) {
                System.out.println(iterator.next());
            }
        } else {
            System.out.println("Arguments required:\narg1 - number to create");
        }
    }
}
