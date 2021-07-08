package com.oup.eac.common.utils.lang;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;

public class LocaleUtils {

    public static final List<Locale> getOrderedLocales(Locale customerLocale) {
        List<Locale> localesList = new ArrayList<Locale>(Arrays.asList(SimpleDateFormat.getAvailableLocales()));
        
        if(customerLocale != null && !localesList.contains(customerLocale)) {
            localesList.add(customerLocale);
        }
        
        Collections.sort(localesList, new Comparator<Locale>() {

            @Override
            public int compare(Locale o1, Locale o2) {
                return o1.toString().compareTo(o2.toString());
            }
        });

        return localesList;
    }

    /**
     * We don't allow locales with variants.
     * 
     * @param locale
     * @return true if the locale is valid, false otherwise.
     */
    public static boolean isValid(Locale locale) {
        boolean valid = true;
        String language = locale.getLanguage();
        if (StringUtils.isNotBlank(language)) {
        	valid &= language.length() == 2;
        	if(valid){
        		valid &= Character.isLowerCase(language.charAt(0));
        		valid &= Character.isLowerCase(language.charAt(1));
        	}
        }else{
        	valid = false;
        }
        String country = locale.getCountry();
        if (StringUtils.isNotEmpty(country)) {
        	valid &= country.length() == 2;
        	if(valid){
        		valid &= Character.isUpperCase(country.charAt(0));
        		valid &= Character.isUpperCase(country.charAt(1));
        	}
        }
        String variant = locale.getVariant();
        if(StringUtils.isNotEmpty(variant)){
        	valid = false;
        }
        return valid;
    }
    
    public static boolean isLessSpecificThan(Locale locale1, Locale locale2){
    	if(locale1 == null){
    		return true;
    	}else{
    		return locale2.toString().startsWith(locale1.toString());
    	}
    }
    
}
