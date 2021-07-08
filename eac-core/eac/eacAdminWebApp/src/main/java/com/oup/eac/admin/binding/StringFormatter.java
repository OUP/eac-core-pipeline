package com.oup.eac.admin.binding;

import java.text.ParseException;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

@Component (value="stringFormatter")
public class StringFormatter implements Formatter<String> {

    @Override
    public String print(String value, Locale locale) {
        return value;
    }

    @Override
    public String parse(String text, Locale locale) throws ParseException {
        if (StringUtils.isBlank(text)) {
        	return null;
        }
        return text.trim();
    }
}
