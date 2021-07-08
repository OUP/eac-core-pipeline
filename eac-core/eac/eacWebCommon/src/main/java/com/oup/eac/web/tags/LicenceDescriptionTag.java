package com.oup.eac.web.tags;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.joda.time.DateTime;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.tags.RequestContextAwareTag;

import com.oup.eac.dto.LicenceDto;
import com.oup.eac.dto.licence.LicenceDescriptionGenerator;
import com.oup.eac.dto.licence.LicenceDescriptionGeneratorSource;

public class LicenceDescriptionTag extends RequestContextAwareTag {

    private static final String BEAN_NAME = "licenceDescriptionGeneratorSource";
    
    private String var;
    private LicenceDto licenceDto;
    private boolean generateHtml;
    
    @Override
    protected int doStartTagInternal() throws Exception {
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        Locale locale = (Locale)request.getAttribute("locale");
        String timeZone = (String)request.getAttribute("timeZone");
        
        LicenceDescriptionGeneratorSource genSource = getGeneratorSource();
        LicenceDescriptionGenerator generator = genSource.getLicenceDescriptionGenerator(locale, timeZone, generateHtml);
        DateTime now = new DateTime();
        String description = generator.getLicenceDescription(licenceDto, now);
        request.setAttribute(var, description);
        return SKIP_BODY;
    }

    protected LicenceDescriptionGeneratorSource getGeneratorSource() {
        ApplicationContext appContext = getRequestContext().getWebApplicationContext();
        LicenceDescriptionGeneratorSource generatorSource = appContext.getBean(BEAN_NAME, LicenceDescriptionGeneratorSource.class);
        return generatorSource;
    }

    public String getVar() {
        return var;
    }

    public void setVar(String var) {
        this.var = var;
    }

    public LicenceDto getLicenceDto() {
        return licenceDto;
    }

    public void setLicenceDto(LicenceDto licenceDto) {
        this.licenceDto = licenceDto;
    }

    public boolean isGenerateHtml() {
        return generateHtml;
    }

    public void setGenerateHtml(boolean generateHtml) {
        this.generateHtml = generateHtml;
    }

    
}

