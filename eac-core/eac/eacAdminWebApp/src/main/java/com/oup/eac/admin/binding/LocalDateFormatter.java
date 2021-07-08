package com.oup.eac.admin.binding;

import java.text.ParseException;
import java.util.Locale;

import org.joda.time.LocalDate;
import org.springframework.format.Formatter;

import com.oup.eac.common.date.utils.DateUtils;

public class LocalDateFormatter implements Formatter<LocalDate> {

    @Override
    public String print(LocalDate localDate, Locale locale) {
    	return localDate.toString(DateUtils.DATE_FORMAT);
    }

    @Override
    public LocalDate parse(String dateString, Locale locale) throws ParseException {
        return DateUtils.safeParseLocalDate(dateString, locale);
    }
}
