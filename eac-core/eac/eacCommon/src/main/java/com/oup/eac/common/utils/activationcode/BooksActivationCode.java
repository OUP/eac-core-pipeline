package com.oup.eac.common.utils.activationcode;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

/**
 * Activation Code Generator.
 * 
 * Tokens in 4 blocks of 4 chars
 * Block 1 - 4 char prefix
 * Block 2 - 4 random numbers with total divisible by 4 
 * Block 3 - 4 random numbers with total divisible by 5 
 * Block 4 - 4 random numbers with total divisible by 6
 * 
 * e.g. XYZ9-6563-4678-4785
 * Block 1 - XYZ9
 * Block 2 - 6+5+6+3=20 which is divisible by 4 
 * Block 3 - 4+6+7+8=25 which is divisible by 5 
 * Block 4 - 4+7+8+5=24 which is divisible by 6
 * 
 * Note: this format is currently used by oup.com eracs.
 * 
 */
public class BooksActivationCode implements ActivationCodeGenerator {
    final transient private Random random = new Random(System.currentTimeMillis());
    
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
     * @param prefix 4 char prefix.
     * @return Set of codes.
     */
    public String createActivationCode(final String prefix) {
        assert(prefix != null && prefix.length() == 4);
        return prefix + "-" + createBlock(4) + "-" + createBlock(5) + "-" + createBlock(6);
    }

    /**
     * Create block.
     * @param divisibleBy total to be divisible by.
     * @return block.
     */
    private String createBlock(final int divisibleBy) {
        final int threeDigits = random.nextInt(1000);
        final int fourthDigit = getFourthDigit(divisibleBy, getTotal(threeDigits));
        return String.format("%04d", (threeDigits * 10) + fourthDigit);
    }

    /**
     * Total of all digits.
     * @param number number.
     * @return total.
     */
    private int getTotal(final int number) {
        int total = 0;
        int numPart = number;
        while (numPart > 0) {
            total += numPart % 10;
            numPart /= 10;
        }
        return total;
    }
    
    /**
     * Number to make total divisible by value.
     * @param divisibleBy value.
     * @param total total.
     * @return digit.
     */
    private int getFourthDigit(final int divisibleBy, final int total) {
        int fouthDigit = 0;
        for (; (total + fouthDigit) % divisibleBy != 0; fouthDigit++) {}
        return fouthDigit;
    }

    /**
     * Main run method to create tokens.
     * @param args arg1 prefix, arg2 number to create.
     */
    public static void main(final String[] args) {
        if (args != null && args.length == 2) {
            final BooksActivationCode gen = new BooksActivationCode();
            final Set<String> codes = gen.createActivationCodes(args[0], Integer.parseInt(args[1]));
            for (final Iterator<String> iterator = codes.iterator(); iterator.hasNext();) {
               System.out.println(iterator.next());                
            }
        } else {
            System.out.println("Arguments required:\narg1 - prefix\narg2 - number to create");
        }
    }
}
