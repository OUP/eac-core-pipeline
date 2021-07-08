package com.oup.eac.admin.binding;

import java.text.ParseException;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import com.oup.eac.domain.ExternalSystem;
import com.oup.eac.service.ExternalIdService;

@Component (value="externalSystemFormatter")
public class ExternalSystemFormatter implements Formatter<ExternalSystem> {

	@Autowired
    private ExternalIdService externalIdService;
    
    @Override
    public String print(ExternalSystem object, Locale locale) {
        return object.getId();
    }

    @Override
    public ExternalSystem parse(String externalSystemId, Locale locale) throws ParseException {
    	return externalIdService.getExternalSystemById(externalSystemId);
    }
}
