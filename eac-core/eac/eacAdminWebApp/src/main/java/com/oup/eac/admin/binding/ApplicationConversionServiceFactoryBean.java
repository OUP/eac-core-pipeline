package com.oup.eac.admin.binding;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;

import com.oup.eac.domain.ExternalSystem;
import com.oup.eac.domain.ExternalSystemIdType;
import com.oup.eac.domain.Product;
import com.oup.eac.domain.RegistrationDefinition;

public class ApplicationConversionServiceFactoryBean extends FormattingConversionServiceFactoryBean {

    @Autowired
    private StringFormatter stringFormatter;
    
    @Autowired
    private RegistrationDefinitionFormatter registrationDefinitionFormatter;
    
    @Autowired
    private ExternalSystemFormatter externalSystemFormatter;
    
    @Autowired
    private ExternalSystemIdTypeFormatter externalSystemIdTypeFormatter;
    
    @Autowired
    private ProductFormatter productFormatter;
    
    @Override
    protected void installFormatters(FormatterRegistry registry) {
        super.installFormatters(registry);
        registry.addFormatterForFieldType(LocalDate.class, new LocalDateFormatter());
        registry.addFormatterForFieldType(DateTime.class, new DateTimeFormatter());
        registry.addFormatterForFieldType(Product.class, productFormatter);
        registry.addFormatterForFieldType(RegistrationDefinition.class, registrationDefinitionFormatter);
        registry.addFormatterForFieldType(ExternalSystem.class, externalSystemFormatter);
        registry.addFormatterForFieldType(ExternalSystemIdType.class, externalSystemIdTypeFormatter);
        registry.addFormatterForFieldType(String.class, stringFormatter);        
    }
    
}
