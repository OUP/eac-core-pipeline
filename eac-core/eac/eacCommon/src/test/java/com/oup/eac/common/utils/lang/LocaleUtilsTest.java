package com.oup.eac.common.utils.lang;

import java.util.Locale;

import org.junit.Assert;
import org.junit.Test;

public class LocaleUtilsTest {

	@Test
	public void testIsLessSpecificThan(){
		Assert.assertTrue(LocaleUtils.isLessSpecificThan(null, Locale.ENGLISH));
		Assert.assertTrue(LocaleUtils.isLessSpecificThan(Locale.ENGLISH, Locale.ENGLISH));
		Assert.assertTrue(LocaleUtils.isLessSpecificThan(Locale.ENGLISH, Locale.UK));
		Assert.assertTrue(LocaleUtils.isLessSpecificThan(Locale.FRENCH, Locale.CANADA_FRENCH));
		Assert.assertTrue(LocaleUtils.isLessSpecificThan(Locale.FRENCH, Locale.FRANCE));
		Assert.assertTrue(LocaleUtils.isLessSpecificThan(Locale.FRENCH, Locale.FRENCH));
	}
	
	@Test
	public void testIsNotLessSpecificThan(){
		Assert.assertFalse(LocaleUtils.isLessSpecificThan(Locale.UK, Locale.ENGLISH));
		Assert.assertFalse(LocaleUtils.isLessSpecificThan(Locale.FRANCE, Locale.FRENCH));
		Assert.assertFalse(LocaleUtils.isLessSpecificThan(Locale.CANADA_FRENCH, Locale.FRENCH));
	}
	
	@Test
	public void testIsValid(){
		Assert.assertTrue(LocaleUtils.isValid(new Locale("ab")));		
		Assert.assertTrue(LocaleUtils.isValid(new Locale("AB")));
		Assert.assertTrue(LocaleUtils.isValid(new Locale("ab","CD")));
		Assert.assertTrue(LocaleUtils.isValid(new Locale("ab","cd")));
		Assert.assertTrue(LocaleUtils.isValid(new Locale("AB","cd")));
		Assert.assertTrue(LocaleUtils.isValid(new Locale("AB","CD")));
		Assert.assertTrue(LocaleUtils.isValid(new Locale("ab","")));
		Assert.assertTrue(LocaleUtils.isValid(new Locale("ab","","")));
	}
	
	@Test
	public void testIsNotValid(){
		Assert.assertFalse(LocaleUtils.isValid(new Locale(" ")));
		Assert.assertFalse(LocaleUtils.isValid(new Locale("ab"," ")));
		Assert.assertFalse(LocaleUtils.isValid(new Locale("en","GB","SCO")));
		Assert.assertFalse(LocaleUtils.isValid(new Locale("en","GB","   ")));		
	}
}


