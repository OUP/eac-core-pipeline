package com.oup.eac.web.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import com.oup.eac.domain.RollingLicenceTemplate.RollingBeginOn;
import com.oup.eac.domain.RollingLicenceTemplate.RollingUnitType;
import com.oup.eac.dto.LicenceDetailDto.LicenceType;
import com.oup.eac.dto.LicenceDto;
import com.oup.eac.dto.RollingLicenceDetailDto;

/**
 * A tag to calculate the created date of a rolling (from creation) licence.
 * 
 * @author David Hay
 * 
 */
public class CalcCreatedDateTimeTag extends SimpleTagSupport {

    @SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger(CalcCreatedDateTimeTag.class);

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
            if (licenceDto.getLicenceDetail().getLicenceType() == LicenceType.ROLLING) {
                RollingLicenceDetailDto rolling = (RollingLicenceDetailDto) licenceDto.getLicenceDetail();
                if (rolling.getBeginOn() == RollingBeginOn.CREATION) {
                    DateTime createTime = null;
                    DateTime expiry = licenceDto.getExpiryDateAndTime();
                    int units = rolling.getTimePeriod();
                    RollingUnitType type = rolling.getUnitType();

                    switch (type) {
                    case YEAR:
                        createTime = expiry.minusYears(units);
                        break;
                    case MONTH:
                        createTime = expiry.minusMonths(units);
                        break;
                    case WEEK:
                        createTime = expiry.minusWeeks(units);
                        break;
                    case DAY:
                        createTime = expiry.minusDays(units);
                        break;
                    case HOUR:
                        createTime = expiry.minusHours(units);
                        break;
                    case MINUTE:
                        createTime = expiry.minusMinutes(units);
                        break;
                    case SECOND:
                        createTime = expiry.minusSeconds(units);
                        break;
                    case MILLISECOND:
                        createTime = expiry.minusMillis(units);
                        break;
                    }
                    if (createTime != null) {
                        pageContext.setAttribute(var, createTime);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
