package com.oup.eac.common.utils.activationcode;

import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.Set;

import org.junit.Test;


public class EacNumericCodeGeneratorTest {
    private final EacNumericActivationCode gen = new EacNumericActivationCode(); 

    /**
     * Test code contains prefix.
     */
    @Test
    public void testPrefix() {
        Set<String> codes = gen.createActivationCodes("ABC1", 10);
        for (Iterator<String> iterator = codes.iterator(); iterator.hasNext();) {
            // numeric generator ignores prefix
            assertFalse(iterator.next().startsWith("ABC1"));            
        }
    }

    /**
     * Test number of codes is expected.
     */
    @Test
    public void testNumberOfCodesCreated() {
        int tests = 1;
        Set<String> codes = gen.createActivationCodes(null, tests);
        assertEquals(tests, codes.size());
        
        tests = 10;
        codes = gen.createActivationCodes(null, tests);
        assertEquals(tests, codes.size());

        tests = 10000;
        codes = gen.createActivationCodes(null, tests);
        assertEquals(tests, codes.size());
    }

    /**
     * Test codes in unexpected format.
     */
    @Test
    public void testInvalidFormat() {
        assertFalse(gen.isValid(null));
        assertFalse(gen.isValid(""));
        assertFalse(gen.isValid("1"));
        assertFalse(gen.isValid("ABC1-12345678901"));
        assertFalse(gen.isValid("4A367C7D9E9"));
    }

    /**
     * Test codes in expected format.
     */
    @Test
    public void testValidFormat() {
        assertTrue(gen.isValid("000000000001"));
        assertTrue(gen.isValid("123456789012"));
    }

}
