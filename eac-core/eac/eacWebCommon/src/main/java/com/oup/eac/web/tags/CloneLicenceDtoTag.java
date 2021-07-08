package com.oup.eac.web.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import com.oup.eac.dto.LicenceDto;

/**
 * @author David Hay
 * Used for testing licenceDto.tag to allow testing of all logic paths.
 * 
 */
public class CloneLicenceDtoTag extends SimpleTagSupport {

    @SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger(CloneLicenceDtoTag.class);

    private LicenceDto old;

    private DateTime expiryDateTime;
    
    private String var;
    
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

    

    public LicenceDto getOld() {
        return old;
    }

    public void setOld(LicenceDto old) {
        this.old = old;
    }

    public DateTime getExpiryDateTime() {
        return expiryDateTime;
    }

    public void setExpiryDateTime(DateTime expiryDateTime) {
        this.expiryDateTime = expiryDateTime;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void doTag() throws JspException {

        PageContext pageContext = (PageContext) getJspContext();
        LicenceDto result = null;
        if (old != null) {
            result = new LicenceDto() ;// new LicenceDto(old.getErightsId(), expiryDateTime, old.isExpired(), old.isActive());
            result.setLicenceDetail(old.getLicenceDetail());
            result.setEnabled(old.isEnabled());
            result.setStartDate(old.getStartDate());
            result.setStartDateTime(old.getStartDateTime());
            result.setEndDate(old.getEndDate());
            result.setEndDateTime(old.getEndDateTime());
        }
        if (result != null) {
            pageContext.setAttribute(var, result);
        }
    }

}
