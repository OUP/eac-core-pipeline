package com.oup.eac.common.utils.lang;

import static org.junit.Assert.*;
import org.junit.Test;

public class EACStringUtilsTest {

	@Test
	public void testFormatThreeDigits() {
		String str = "123";
		String formatted = EACStringUtils.format(str, 4, '-');
		assertEquals("123", formatted);
	}	
	
	@Test
	public void testFormatEven() {
		String str = "123456789123";
		String formatted = EACStringUtils.format(str, 4, '-');
		assertEquals("1234-5678-9123", formatted);
	}
	
	@Test
	public void testFormatOdd() {
		String str = "1234567891234";
		String formatted = EACStringUtils.format(str, 4, '-');
		assertEquals("1234-5678-9123-4", formatted);
	}
	
	@Test
	public void testValidString() {
		String str = "asKdjbnaskd123bj";
		String corrected = EACStringUtils.removeNonAlphanumericAndWhitespace(str);
		assertEquals("asKdjbnaskd123bj", corrected);
	}
	
	@Test
	public void testStringWithWhiteSpace() {
		String str = " a sKdj bna skd123 bj  ";
		String corrected = EACStringUtils.removeNonAlphanumericAndWhitespace(str);
		assertEquals("asKdjbnaskd123bj", corrected);
	}
	
	@Test
	public void testStringWithNonValid() {
		String str = "a$sK^dj&bna**sk$d123bj9";
		String corrected = EACStringUtils.removeNonAlphanumericAndWhitespace(str);
		assertEquals("asKdjbnaskd123bj9", corrected);
	}
	
	@Test
	public void testStringWithWhiteSpaceAndNonValid() {
		String str = "a s%K$d^j b*na s(kd123 b)j9";
		String corrected = EACStringUtils.removeNonAlphanumericAndWhitespace(str);
		assertEquals("asKdjbnaskd123bj9", corrected);
	}
	
	@Test
	public void testStringArrayBlank() {
		String[] s = new String[2];
		assertTrue(EACStringUtils.isStringArrayBlank(s));
	}
	
	@Test
	public void testStringArrayBlankNull() {
		String[] s = null;
		assertTrue(EACStringUtils.isStringArrayBlank(s));
	}
	
	@Test
	public void testStringArrayBlankEmptyString() {
		String[] s = new String[] {""};
		assertTrue(EACStringUtils.isStringArrayBlank(s));
	}
	
	@Test
	public void testStringArrayBlankEmptyStrings() {
		String[] s = new String[] {"", "hello"};
		assertTrue(EACStringUtils.isStringArrayBlank(s));
	}
	
	@Test
	public void testStringArrayBlankStrings() {
		String[] s = new String[] {"test", "hello"};
		assertFalse(EACStringUtils.isStringArrayBlank(s));
	}
	
	@Test
	public void testSafeConvertToDelimitedStringNull() {
		String[] s = null;
		assertNull(EACStringUtils.safeConvertToDelimitedString(s, ','));
	}	
	
	@Test
	public void testSafeConvertToDelimitedStringEmpty() {
		String[] s = new String[2] ;
		assertEquals("", EACStringUtils.safeConvertToDelimitedString(s, ','));
	}
	
	@Test
	public void testSafeConvertToDelimitedStringSingle() {
		String[] s = new String[] {"test"};
		assertEquals("test", EACStringUtils.safeConvertToDelimitedString(s, ','));
	}	
	
	@Test
	public void testSafeConvertToDelimitedString() {
		String[] s = new String[] {"test", "hello"};
		assertEquals("test,hello", EACStringUtils.safeConvertToDelimitedString(s, ','));
	}	
}
