package com.oup.eac.web.locale;

import java.text.Collator;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.stereotype.Component;


/**
 * A cached source of LocaleDropDowns which generated Locale specific descriptions.
 * The cache key is the Locale.
 * 
 * @author David Hay
 *
 */
@Component
public class LocaleDropDownSourceImpl implements LocaleDropDownSource {

    
    /**
     * {@inheritDoc}
     */
    @Override
    public final Map<String, String> getLocaleDropDown(final Locale displayLocale, final Locale usersLocale) {
        //LinkedHashMap : iteration order will be insertion order
        Map<String, String> result = new LinkedHashMap<String, String>();
        
        Map<String, String> cachedLocales = getLocaleDropDownCached(displayLocale, usersLocale);
        
        List<Locale> localesToAdd = new ArrayList<Locale>();
        
        //If the display locale is a non-standard locale and missing from the list then add it to the list
        if(!cachedLocales.containsKey(displayLocale.toString())) {
            localesToAdd.add(displayLocale);
        }
        
        //If the customers locale is a non-standard locale and missing from the list then add it to the list
        if(usersLocale != null && (!cachedLocales.containsKey(usersLocale.toString()))) {
            localesToAdd.add(usersLocale);
        }
        
        for (Locale locale : localesToAdd) {
            result.put(locale.toString(), locale.getDisplayName(displayLocale));
        }        
        
        result.putAll(cachedLocales);
        
        return result;
    }
    
    /**
     * {@inheritDoc}
     */
   
    private final Map<String, String> getLocaleDropDownCached(final Locale displayLocale, final Locale usersLocale) {
        //LinkedHashMap : iteration order will be insertion order
        Map<String, String> result = new LinkedHashMap<String, String>();
        
        //get all locales
        List<Locale> locales = Arrays.asList(SimpleDateFormat.getAvailableLocales());
        
        //get a 'collator' which compares based on Locale 
        final Collator collator = Collator.getInstance(displayLocale);
        
        //will sort Locales based on the displayName which is compared using the displayLocale
        Comparator<Locale> sorter = new Comparator<Locale>() {

            @Override
            public int compare(final Locale o1, final Locale o2) {
                return collator.compare(o1.getDisplayName(displayLocale), o2.getDisplayName(displayLocale));
            }
        };
        //sort the locales
        Collections.sort(locales, sorter);
        //put the locales into the map in order of the display names
        for (Locale locale : locales) {
            result.put(locale.toString(), locale.getDisplayName(displayLocale));
        }
        return result;
    }
    
}
