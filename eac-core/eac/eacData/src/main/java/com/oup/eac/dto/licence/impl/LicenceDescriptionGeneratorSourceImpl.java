package com.oup.eac.dto.licence.impl;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import com.oup.eac.dto.licence.LicenceDescriptionGenerator;
import com.oup.eac.dto.licence.LicenceDescriptionGeneratorSource;

@Component("licenceDescriptionGeneratorSource")
public class LicenceDescriptionGeneratorSourceImpl implements LicenceDescriptionGeneratorSource {

    private MessageSource messageSource;
    private String dateStyle;
    private String dateTimeStyle;
    private boolean atyponExpiryBugFixed;

    @Autowired
    public LicenceDescriptionGeneratorSourceImpl(final MessageSource messageSource,
            @Value("${formats.date.style}") final String dateStyle,
            @Value("${formats.date.time.style}") final String dateTimeStyle,
            @Value("${licence.description.atypon.bug.fixed}") final boolean atyponExpiryBugFixed) {
        this.messageSource = messageSource;
        this.dateStyle = dateStyle;
        this.dateTimeStyle = dateTimeStyle;
        this.atyponExpiryBugFixed = atyponExpiryBugFixed;
    }

    @Override
    public LicenceDescriptionGenerator getLicenceDescriptionGenerator(Locale locale, String timeZoneID,
            boolean generateHtml) {
        return new LicenceDescriptionGeneratorImpl(messageSource, dateStyle, dateTimeStyle, locale, timeZoneID,
                generateHtml, atyponExpiryBugFixed);
    }

}
