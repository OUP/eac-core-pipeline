package com.oup.eac.admin.binding;

import java.text.ParseException;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import com.oup.eac.domain.RegistrationDefinition;
import com.oup.eac.service.RegistrationDefinitionService;
import com.oup.eac.service.ServiceLayerException;

@Component (value="registrationDefinitionFormatter")
public class RegistrationDefinitionFormatter implements Formatter<RegistrationDefinition> {

    @Autowired
    private RegistrationDefinitionService registrationDefinitionService;
    
    @Override
    public String print(RegistrationDefinition object, Locale locale) {
        return object.getProduct().getProductName();
    }

    @Override
    public RegistrationDefinition parse(String text, Locale locale) throws ParseException {
        try {
            return registrationDefinitionService.getRegistrationDefinitionById(text);
        } catch (ServiceLayerException e) {
            throw new ParseException("RegistrationDefinition could not be parsed from id " + text, 0);
        }
    }
}
