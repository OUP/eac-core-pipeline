package com.oup.eac.admin.binding;

import java.text.ParseException;
import java.util.Locale;

import org.joda.time.DateTime;
import org.springframework.format.Formatter;

import com.oup.eac.common.date.utils.DateUtils;

public class DateTimeFormatter implements Formatter<DateTime> {

    @Override
    public String print(DateTime dateTime, Locale locale) {
    	return DateUtils.printAsDate(dateTime, locale);
    }

    @Override
    public DateTime parse(String dateString, Locale locale) throws ParseException {
        return DateUtils.parseToDate(dateString, locale);
    }
}
