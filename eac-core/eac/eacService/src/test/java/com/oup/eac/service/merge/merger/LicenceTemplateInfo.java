package com.oup.eac.service.merge.merger;

import java.text.SimpleDateFormat;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.joda.time.LocalDate;

import com.oup.eac.domain.ConcurrentLicenceTemplate;
import com.oup.eac.domain.LicenceTemplate;
import com.oup.eac.domain.RollingLicenceTemplate;
import com.oup.eac.domain.UsageLicenceTemplate;
import com.oup.eac.dto.LicenceDetailDto;
import com.oup.eac.dto.LicenceDto;
import com.oup.eac.dto.RollingLicenceDetailDto;
import com.oup.eac.dto.StandardConcurrentLicenceDetailDto;
import com.oup.eac.dto.UsageLicenceDetailDto;

/**
 * Used as a canonical representation of a licence template. Can be constructed from a LicenceTemplate ( from the eac db ) OR the LicenceDto ( from atypon ws )
 * Used in comparing licence information held in eac db and atypon ws.
 * 
 * @author David Hay
 * 
 */
public class LicenceTemplateInfo implements Comparable<LicenceTemplateInfo> {

    public static final SimpleDateFormat SDF = new SimpleDateFormat("dd-MMM-yyyy");

    private final String licenceType;// 1
    private final String startDate;// 2
    private final String endDate;// 3
    private final String totalConcurrency;// 4
    private final String userConcurrency;// 5
    private final String beginOn;// 6
    private final String timePeriod;// 7
    private final String unitType;// 8
    private final String allowedUsages;// 9
    private final String description;// derived - the canonical representation.
    private final String source;

    public LicenceTemplateInfo(LicenceTemplate lt) {
        this.licenceType = lt.getLicenceType().name();
        this.startDate = formatLocalDate(lt.getStartDate());
        this.endDate = formatLocalDate(lt.getEndDate());

        if (lt instanceof ConcurrentLicenceTemplate) {
            ConcurrentLicenceTemplate con = (ConcurrentLicenceTemplate) lt;
            this.totalConcurrency = convert(con.getTotalConcurrency());
            this.userConcurrency = convert(con.getUserConcurrency());
        } else {
            this.totalConcurrency = "_";
            this.userConcurrency = "_";
        }

        if (lt instanceof RollingLicenceTemplate) {
            RollingLicenceTemplate roll = (RollingLicenceTemplate) lt;
            this.beginOn = roll.getBeginOn() == null ? "_" : roll.getBeginOn().name();
            this.timePeriod = convert(roll.getTimePeriod());
            this.unitType = roll.getUnitType() == null ? "_" : roll.getUnitType().name();
        } else {
            this.beginOn = "_";
            this.timePeriod = "_";
            this.unitType = "_";
        }

        if (lt instanceof UsageLicenceTemplate) {
            UsageLicenceTemplate usage = (UsageLicenceTemplate) lt;
            this.allowedUsages = convert(usage.getAllowedUsages());
        } else {
            this.allowedUsages = "_";
        }
        this.description = getDescription();
        this.source = String.format("source = (LicenceTemplate : %s",lt.getId());
    }

    public LicenceTemplateInfo(LicenceDto dto) {
    	LicenceDetailDto detail = dto.getLicenceDetail();
        this.licenceType = detail == null ? "CONCURRENT" : detail.getLicenceType().name();
        this.startDate = formatLocalDate(dto.getStartDate());
        this.endDate = formatLocalDate(dto.getEndDate());

        if (detail instanceof StandardConcurrentLicenceDetailDto) {
            StandardConcurrentLicenceDetailDto con = (StandardConcurrentLicenceDetailDto) detail;
            this.totalConcurrency = convert(con.getTotalConcurrency());
            this.userConcurrency = convert(con.getUserConcurrency());
        } else {
            this.totalConcurrency = "_";
            this.userConcurrency = "_";
        }

        if (detail instanceof RollingLicenceDetailDto) {
            RollingLicenceDetailDto roll = (RollingLicenceDetailDto) detail;
            this.beginOn = roll.getBeginOn() == null ? "_" : roll.getBeginOn().name();
            this.timePeriod = convert(roll.getTimePeriod());
            this.unitType = roll.getUnitType() == null ? "_" : roll.getUnitType().name();
        } else {
            this.beginOn = "_";
            this.timePeriod = "_";
            this.unitType = "_";
        }

        if (detail instanceof UsageLicenceDetailDto) {
            UsageLicenceDetailDto usage = (UsageLicenceDetailDto) detail;
            this.allowedUsages = convert(usage.getAllowedUsages());
        } else {
            this.allowedUsages = "_";
        }
        this.description = getDescription();
        this.source = String.format("source = (LicenceDto : %s",dto.getLicenseId());
    }

    public String getDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.licenceType);// 1
        sb.append(this.startDate);// 2
        sb.append(this.endDate);// 3
        sb.append(this.totalConcurrency);// 4
        sb.append(this.userConcurrency);// 5
        sb.append(this.beginOn);// 6
        sb.append(this.timePeriod);// 7
        sb.append(this.unitType);// 8
        sb.append(this.allowedUsages);// 9
        String result = sb.toString();
        return result;
    }

    @Override
    public String toString() {
        return this.description + "/" + this.source;
    }

    @Override
    public int hashCode() {
        return description.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        LicenceTemplateInfo rhs = (LicenceTemplateInfo) obj;
        return new EqualsBuilder().append(this.description, rhs.description).isEquals();
    }

    @Override
    public int compareTo(LicenceTemplateInfo other) {
        return this.description.compareTo(other.description);
    }

    private String convert(int value) {
        return String.valueOf(value);
    }

    private String formatLocalDate(LocalDate localDate) {
        if (localDate == null) {
            return "_";
        } else {
            return SDF.format(localDate.toDateMidnight().toDate());
        }
    }

}
