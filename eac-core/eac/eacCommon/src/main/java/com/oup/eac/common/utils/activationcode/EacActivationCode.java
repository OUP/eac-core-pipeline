package com.oup.eac.common.utils.activationcode;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.lang.RandomStringUtils;

/**
 * Activation Code Generator.
 * 
 * Tokens in the following format:
 * <code>:=<prefix>-<body><checkdigit>
 * <prefix>:=4 char product prefix
 * <body>:=10 chars not containing 1, I, 2, Z, 0, O, 8, B or special chars (!,$# etc)
 * <checkdigit>:=check digit validating body 
 * 
 * e.g. CAR1-4A367C7D9E1
 * <prefix>     -   CAR1
 * <body>       -   4A367C7D9E
 * <checkdigit> -   1
 * 
 */
public class EacActivationCode implements ActivationCodeGenerator, ActivationCodeValidator {
    private static final String VALID_CHARS = "34679ACDEFGHJKLMNPQRTUVWXY";

    /**
     * Create activation codes.
     * @param prefix 4 char prefix.
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
     * @param prefix 4 char prefix.
     * @return Set of codes.
     */
    public String createActivationCode(final String prefix) {
        if (prefix == null || !(prefix.length() == 4)) {
        	throw new AssertionError("Prefix must be 4 characters long and is: " + prefix);
        }
        final String body = RandomStringUtils.random(10, VALID_CHARS);
        return prefix + "-" + body + createCheckChar(body);
    }

    /**
     * Check if code is valid format.
     * @param code activation code
     * @return true - valid
     */
    public boolean isValid(final String code) {
        boolean valid = code != null;
        valid = valid && code.length() == 16;
        valid = valid && isValidCharSet(code);
        valid = valid && isValidCheckChar(code);
        return valid;
    }

    /**
     * Create check digit for body of code.
     * @param body code without prefix and check digit.
     * @return check digit
     */
    private String createCheckChar(final String body) {
        int total = 0;
        for (int i = 0; i < body.length(); i++) {
            total += body.charAt(i);
        }
        return String.valueOf(getDigitToMakeDivisible(9, total));
    }

    /**
     * Get digit which when added to total will become divisible by supplied value.
     * @param divisibleBy supplied divisible by number
     * @param total number to make divisible by
     * @return digit when added to total makes divisible by
     */
    private int getDigitToMakeDivisible(final int divisibleBy, final int total) {
        int fouthDigit = 0;
        for (; (total + fouthDigit) % divisibleBy != 0; fouthDigit++) {
        }
        return fouthDigit;
    }

    /**
     * Determine if check char is valid
     * @param code full activation code
     * @return true - valid
     */
    private boolean isValidCheckChar(final String code) {
        final String checkChar = code.substring(code.length() - 1);
        final String body = code.substring(code.indexOf('-') + 1, code.length() - 1);        
        return checkChar.equals(createCheckChar(body));
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
     * @param args arg1 prefix, arg2 number to create.
     */
    public static void main(final String[] args) {
        if (args != null && args.length == 2) {
            final EacActivationCode gen = new EacActivationCode();
            final Set<String> codes = gen.createActivationCodes(args[0], Integer.parseInt(args[1]));
            for (final Iterator<String> iterator = codes.iterator(); iterator.hasNext();) {
                System.out.println(iterator.next());
            }
        } else {
            System.out.println("Arguments required:\narg1 - prefix\narg2 - number to create");
        }
    }
}
