package com.oup.eac.admin.binding;

import java.text.ParseException;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import com.oup.eac.domain.ExternalSystemIdType;
import com.oup.eac.service.ExternalIdService;

@Component (value="externalSystemIdTypeFormatter")
public class ExternalSystemIdTypeFormatter implements Formatter<ExternalSystemIdType> {

	@Autowired
    private ExternalIdService externalIdService;
    
    @Override
    public String print(ExternalSystemIdType externalSystemIdType, Locale locale) {
        return externalSystemIdType.getName();
    }

    @Override
    public ExternalSystemIdType parse(String externalSystemIdTypeId, Locale locale) throws ParseException {
        return externalIdService.getExternalSystemIdTypeById(externalSystemIdTypeId);
    }
}