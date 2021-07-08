package com.oup.eac.common.utils.lang;

import org.apache.commons.lang.StringUtils;

/**
 * Eac string utils
 * 
 * @author harlandd
 *
 */
public class EACStringUtils {

	private EACStringUtils() {
		
	}
	
	public static boolean isStringArrayBlank(final String[] strArray) {
		if(strArray == null || strArray.length == 0) {
			return true;
		}
		for(String str : strArray) {
			if(StringUtils.isBlank(str)) {
				return true;
			}
		}
		return false;
	}
	
	
	public static String safeConvertToDelimitedString(final String[] strArray , final char delimiter) {
		if(strArray == null || strArray.length == 0) {
			return null;
		}
		StringBuilder stringBuilder = new StringBuilder();
		for(String str : strArray) {
			if(StringUtils.isNotBlank(str)) {
				if(stringBuilder.length() > 0) {
					stringBuilder.append(delimiter);
				}
				stringBuilder.append(str);
			}
		}
		return stringBuilder.toString();
	}
	
	/**
	 * Removes all non-alphanumeric and whitespace characters from a string
	 * 
	 * @param str the string to check
	 * @return the supplied string with all alphanumeric and whitespace characters removed 
	 */
	public static String removeNonAlphanumericAndWhitespace(final String str) {
		String strConverted = str.replaceAll("[^a-zA-Z0-9]","") ;
		if ( strConverted != null && !strConverted.isEmpty()) {
			return strConverted ;
		} else {
			return String.valueOf(System.currentTimeMillis()) ;
		}
	}
	
	/**
	 * Formats strings by adding a pad character at specified interval
	 * 
	 * @param str the string to have pad character added
	 * @param padCharInterval the pad interval
	 * @param padChar the pad character
	 * @return the string plus additional pad characters
	 */
	public static String format(final String str, final int padCharInterval, final char padChar) {
		if(StringUtils.isBlank(str)) {
			return str;
		}
		StringBuilder origStr = new StringBuilder(str);
		StringBuilder newStr = new StringBuilder();
		while(origStr.length() > 0) {
			if(newStr.length() > 0) {
				newStr.append(padChar);
			}
			if(origStr.length() >= padCharInterval) {
				newStr.append(origStr.substring(0, padCharInterval));
			} else {
				newStr.append(origStr.toString());
			}
			origStr.delete(0, padCharInterval);
		}
		return newStr.toString();
	}
	
}
