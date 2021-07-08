package com.oup.eac.common.utils.activationcode;

import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.Set;

import org.junit.Test;

import com.oup.eac.common.utils.activationcode.BooksActivationCode;

public class BooksCodeGeneratorTest {
    private final BooksActivationCode gen = new BooksActivationCode(); 

    /**
     * Test code contains prefix.
     */
    @Test
    public void testPrefix() {
        Set<String> codes = gen.createActivationCodes("TEST", 10);
        for (Iterator<String> iterator = codes.iterator(); iterator.hasNext();) {
            assertTrue(iterator.next().startsWith("TEST"));            
        }
    }

    /**
     * Test number of codes is expected.
     */
    @Test
    public void testNumberOfCodesCreated() {
        int tests = 1;
        Set<String> codes = gen.createActivationCodes("TEST", tests);
        assertEquals(tests, codes.size());
        
        tests = 10;
        codes = gen.createActivationCodes("TEST", tests);
        assertEquals(tests, codes.size());

        tests = 10000;
        codes = gen.createActivationCodes("TEST", tests);
        assertEquals(tests, codes.size());
    }

    /**
     * Test codes in expected format.
     */
    @Test
    public void testBlockFormat() {
        Set<String> codes = gen.createActivationCodes("TEST", 10);
        for (Iterator<String> iterator = codes.iterator(); iterator.hasNext();) {
            String code = iterator.next();
            String[] blocks = code.split("-");
            assertTrue(blocks != null && blocks.length == 4);
            assertTrue(blocks[0].length() == 4);
            assertTrue(blocks[1].length() == 4);
            assertTrue(blockDivisibleBy(blocks[1], 4));
            assertTrue(blocks[2].length() == 4);
            assertTrue(blockDivisibleBy(blocks[2], 5));
            assertTrue(blocks[3].length() == 4);
            assertTrue(blockDivisibleBy(blocks[3], 6));
        }
    }

    /**
     * Check block value.
     * @param block value.
     * @param divisibleBy char total divisible by.
     * @return true/false.
     */
    private boolean blockDivisibleBy(String block, int divisibleBy) {
       int total = 0;
       for (int i = 0; i < 4; i++) {
           total += Integer.parseInt(String.valueOf(block.charAt(i)));
       }       
       return total % divisibleBy == 0;
    }
    
}
