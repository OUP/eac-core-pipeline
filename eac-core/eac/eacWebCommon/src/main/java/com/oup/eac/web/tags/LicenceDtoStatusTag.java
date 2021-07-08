package com.oup.eac.web.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.log4j.Logger;

import com.oup.eac.dto.LicenceDto;
import com.oup.eac.dto.license.LicenceStatus;

/**
 * A tag to put the LicenceStatus into a scripting variable
 * 
 * @author David Hay
 * 
 */
public class LicenceDtoStatusTag extends SimpleTagSupport {

    @SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger(LicenceDtoStatusTag.class);

    private String var;

    private LicenceDto licenceDto;
    
    /**
     * @return the var
     */
    public final String getVar() {
        return var;
    }

    /**
     * @param varP
     *            the var to set
     */
    public final void setVar(final String varP) {
        this.var = varP;
    }

    
    
    public LicenceDto getLicenceDto() {
        return licenceDto;
    }

    public void setLicenceDto(LicenceDto licenceDto) {
        this.licenceDto = licenceDto;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void doTag() throws JspException {

        PageContext pageContext = (PageContext) getJspContext();
        try {
            LicenceStatus status = LicenceStatus.getLicenceStatus(licenceDto);
            pageContext.setAttribute(var, status);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
