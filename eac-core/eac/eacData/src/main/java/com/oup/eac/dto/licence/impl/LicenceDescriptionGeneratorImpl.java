package com.oup.eac.dto.licence.impl;

import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.context.MessageSource;

import com.oup.eac.domain.RollingLicenceTemplate.RollingBeginOn;
import com.oup.eac.domain.RollingLicenceTemplate.RollingUnitType;
import com.oup.eac.dto.LicenceDetailDto;
import com.oup.eac.dto.LicenceDetailDto.LicenceType;
import com.oup.eac.dto.LicenceDto;
import com.oup.eac.dto.RollingLicenceDetailDto;
import com.oup.eac.dto.StandardConcurrentLicenceDetailDto;
import com.oup.eac.dto.UsageLicenceDetailDto;
import com.oup.eac.dto.licence.LicenceDescriptionGenerator;

public class LicenceDescriptionGeneratorImpl implements LicenceDescriptionGenerator {

    private static final String MSG_LABEL_ROLLING_FROM_TO = "label.rolling.from.to";
    private static final String MSG_LABEL_ROLLING_END = "label.rolling.end";
    private static final String MSG_LABEL_ROLLING_START = "label.rolling.start";
    private static final String MSG_LABEL_ROLLING_FIRST_USE = "label.rolling.first.use";
    private static final String MSG_LABEL_ROLLING_REGISTRATION = "label.rolling.registration";
    private static final String MSG_LABEL_ROLLING_ACCESS_PERIOD = "label.rolling.access.period";
    private static final String MSG_PROFILE_LICENCE_USAGE = "profile.licence.usage";
    private static final String MSG_PROFILE_LICENCE_CONCURRENT = "profile.licence.concurrent";
    private static final String MSG_TITLE_LICENCE_EXPIRES = "title.licence.expires";
    private static final String MSG_TITLE_LICENCE_START = "title.licence.start";
    // private static final String MSG_PROFILE_LICENCE_ROLLING =
    // "profile.licence.rolling";
    private static final String MSG_TITLE_LICENCE_ROLLING_BEGIN_ON = "title.licence.rolling.begin.on.";
    private static final String MSG_LABEL_ROLLING_SUBJECT_TO = "label.rolling.subject.to";

    private final String mStart;
    private final String spacer;
    private final String mExpires;

    private final Locale locale;
    private final MessageSource messageSource;
    private final DateTimeFormatter dateFormatter;
    private final DateTimeFormatter dateTimeFormatter;
    private final boolean generateHtml;
    private final boolean atyponExpiryBugFixed;

    public LicenceDescriptionGeneratorImpl(MessageSource messageSource, String dateStyle, String dateTimeStyle,
            Locale locale, String timeZoneID, boolean generateHtml, boolean atyponExpiryBugFixed) {

        this.generateHtml = generateHtml;
        if (this.generateHtml) {
            spacer = "&nbsp;";
        } else {
            spacer = " ";
        }
        
        DateTimeZone tz = getTimeZone(timeZoneID);
        this.dateTimeFormatter = DateTimeFormat.forStyle(dateTimeStyle).withLocale(locale).withZone(tz);
        this.dateFormatter = DateTimeFormat.forStyle(dateStyle).withLocale(locale).withZone(tz);

        this.messageSource = messageSource;
        this.locale = locale == null ? Locale.getDefault() : locale;

        this.mStart = getMessage(MSG_TITLE_LICENCE_START);
        this.mExpires = getMessage(MSG_TITLE_LICENCE_EXPIRES);
        this.atyponExpiryBugFixed = atyponExpiryBugFixed;
    }
    
    private DateTimeZone getTimeZone(String timeZoneID) {
        DateTimeZone result = DateTimeZone.UTC;
        if (StringUtils.isNotBlank(timeZoneID)) {
            try {
                DateTimeZone tz = DateTimeZone.forID(timeZoneID);
                result = tz;
            } catch (Exception ex) {
                //ignore, use UTC
            }
        }
        return result;
    }

    private String getMessage(String code, Object... args) {
        String result = messageSource.getMessage(code, args, locale);
        return result;
    }

    @Override
    public String getLicenceDescription(LicenceDto licenceDto, DateTime now) {
        if (licenceDto == null) {
            return "";
        }
        LicInfo licInfo = new LicInfo(licenceDto, now);
        String result = licInfo.getDescription();
        return result;
    }

    private class LicInfo {
        private final DateTime now;
        private final String startDate;
        private final String endDate;
        private final String endDateTime;
        private final String startDateTime;
        private final LicenceDto licenceDto;
        private final LicenceDetailDto detail;

        public LicInfo(LicenceDto licenceDto, DateTime now) {
            this.now = now;
            this.licenceDto = licenceDto;
            this.detail = licenceDto.getLicenceDetail();
            if (licenceDto.getStartDate() != null) {
                if (licenceDto.getStartDateTime() == null) {
                    licenceDto.setStartDateTime(licenceDto.getStartDate().toDateTimeAtStartOfDay());
                }
                startDate =         dateFormatter.print(licenceDto.getStartDate());
                startDateTime = dateTimeFormatter.print(licenceDto.getStartDateTime());
            } else {
                startDate = null;
                startDateTime = null;
            }
            if (licenceDto.getEndDate() != null) {
                if (licenceDto.getEndDateTime() == null) {
                    licenceDto.setEndDateTime(licenceDto.getEndDate().toDateTimeAtStartOfDay());
                }
                endDate =     dateFormatter.print(licenceDto.getEndDate());
                endDateTime = dateTimeFormatter.print(licenceDto.getEndDateTime());
            } else {
                endDate = null;
                endDateTime = null;
            }
        }

        public String getDescription() {
            StringBuilder sb = new StringBuilder();
            try{
                if (detail != null) {
                    switch (detail.getLicenceType()) {
                    case ROLLING:
                        processingRolling(sb, now);
                        break;
                    case CONCURRENT:
                        StandardConcurrentLicenceDetailDto conDetail = (StandardConcurrentLicenceDetailDto) detail;
                        if (conDetail.getUserConcurrency() > 1) {
                            String line2 = getMessage(MSG_PROFILE_LICENCE_CONCURRENT, conDetail.getUserConcurrency());
                            addWrapped(sb, line2);
                        }
                        addStartDate(sb, startDate);
                        addEndDate(sb, endDate);
                        break;
                    case USAGE:
                        UsageLicenceDetailDto usageDetail = (UsageLicenceDetailDto) detail;

                        int allowed = usageDetail.getAllowedUsages();
                        int remaining = usageDetail.getUsagesRemaining();
                        String mLine2 = getMessage(MSG_PROFILE_LICENCE_USAGE, allowed, remaining);

                        addWrapped(sb, mLine2);
                        addStartDate(sb, startDate);
                        addEndDate(sb, endDate);
                        break;
                    case STANDARD:
                        addStartDate(sb, startDate);
                        addEndDate(sb, endDate);
                        break;
                    }
                }
            } catch (RuntimeException ex) {
                throw new RuntimeException(getErrorDescription(this.licenceDto, locale), ex);
            }
            String result = sb.toString();
            return result;
        }

        private void processingRolling(StringBuilder sb, DateTime now) {
            RollingLicenceDetailDto rollingDetail = (RollingLicenceDetailDto) detail;
            
            String unitTypeValue = getMessage(rollingDetail.getUnitType().name());
            String period = String.format("%d%s%s", rollingDetail.getTimePeriod(), spacer, unitTypeValue);

            String msg = "";
            switch (rollingDetail.getBeginOn()) {
            case FIRST_USE:
                msg = getRollingFirstUse(rollingDetail, period);
                break;
            case CREATION:
                msg = getRollingFromCreation(rollingDetail, period);                
                break;
            }
            sb.append(msg);
        }

        private String getRollingFromCreation(RollingLicenceDetailDto rollingDetail, String period) {
            String msg = null;
            String tactical = getRollingAccessPeriod(true, period, startDate, endDate, null);
            
            if (!atyponExpiryBugFixed
                    && (licenceDto.getStartDateTime() != null && licenceDto.getStartDateTime().isAfter(now))) {
                msg = tactical;
            } else if (licenceDto.getExpiryDateAndTime() != null) {
                DateTime createdDateTime = getCreatedDateTime(); // may not
                                                                 // be
                                                                 // correct
                if (startDate == null && endDate == null) {
                    msg = getAccessFromToDateTimeStyle(createdDateTime, licenceDto.getExpiryDateAndTime());

                } else if (startDate == null && endDate != null) {
                    
                    if (licenceDto.getEndDateTime().isAfter(licenceDto.getExpiryDateAndTime()) == false) {
                        
                        msg = tactical;// because we can't deduce the
                                       // creation
                                       // date correctly. We know expiry
                                       // date
                                       // though.
                    } else {
                        msg = getAccessFromToDateTimeStyle(createdDateTime, licenceDto.getExpiryDateAndTime());
                    }

                } else if (startDate != null && endDate == null) {
                    DateTime dt1;
                    if (licenceDto.getStartDateTime().isAfter(createdDateTime)) {
                        dt1 = licenceDto.getStartDateTime();
                    } else {
                        dt1 = createdDateTime;
                    }
                    msg = getAccessFromToDateTimeStyle(dt1, licenceDto.getExpiryDateAndTime());

                } else if (startDate != null && endDate != null) {
                    if (licenceDto.getEndDateTime().isAfter(licenceDto.getExpiryDateAndTime())) {
                        DateTime dt1;
                        if (licenceDto.getStartDateTime().isAfter(createdDateTime)) {
                            dt1 = licenceDto.getStartDateTime();
                        } else {
                            dt1 = createdDateTime;
                        }
                        msg = getAccessFromToDateTimeStyle(dt1, licenceDto.getExpiryDateAndTime());
                    } else {
                        //We can't work out the creation date so just use the start date
                        msg = getAccessFromToDateTimeStyle(licenceDto.getStartDateTime(), licenceDto.getEndDateTime());
                    }
                }
            } else {
                if (startDate == null && endDate == null) {
                    msg = tactical;
                } else if (startDate == null && endDate != null) {
                    msg = tactical;
                } else if (startDate != null && endDate == null) {
                    msg = tactical;
                } else {
                    //start date != null && end date != null
                    msg = tactical;
                }
            }
            return msg;
        }

        private String getRollingFirstUse(RollingLicenceDetailDto rollingDetail, String period) {
            String msg;
            if (rollingDetail.getFirstUse() != null) {
                msg = getAccessFromToDateTimeStyle(rollingDetail.getFirstUse(), licenceDto.getExpiryDateAndTime());
            } else {
                if (licenceDto.getExpiryDateAndTime() != null) {
                    boolean startDateInFuture = licenceDto.getStartDateTime() != null && licenceDto.getStartDateTime().isAfter(now);
                    String expiryDate = dateFormatter.print(licenceDto.getExpiryDateAndTime());
                    if (startDateInFuture) {
                        msg = getRollingAccessPeriod(false, period, startDate, expiryDate, null);
                    } else {
                        msg = getRollingAccessPeriod(false, period, null, expiryDate, null);
                    }
                } else {
                    msg = getRollingAccessPeriod(false, period, startDate, endDate, null);
                }
            }
            return msg;
        }

        private String getRollingAccessPeriod(boolean fromCreation, String period, String startDate, String endDate,
                String firstUseDate) {
            StringBuilder sb = new StringBuilder();

            String msg = getMessage(MSG_LABEL_ROLLING_ACCESS_PERIOD, period);
            sb.append(msg);
            sb.append(spacer);

            if (fromCreation) {
                msg = getMessage(MSG_LABEL_ROLLING_REGISTRATION);                
                sb.append(msg);
            } else {
                if (StringUtils.isNotBlank(firstUseDate)) {                    
                    sb.append(firstUseDate);
                } else {
                    msg = getMessage(MSG_LABEL_ROLLING_FIRST_USE);
                    sb.append(msg);
                }
            }
            if (StringUtils.isNotBlank(startDate) || StringUtils.isNotBlank(endDate)) {
                
                String subjectTo = getMessage(MSG_LABEL_ROLLING_SUBJECT_TO);
                sb.append(spacer);
                sb.append(subjectTo);
                
                if (StringUtils.isNotEmpty(startDate)) {
                    msg = getMessage(MSG_LABEL_ROLLING_START);
                    sb.append(spacer);
                    sb.append(msg);
                    sb.append(spacer);
                    sb.append(startDate);
                }
                if (StringUtils.isNotEmpty(endDate)) {
                    msg = getMessage(MSG_LABEL_ROLLING_END);
                    sb.append(spacer);
                    sb.append(msg);
                    sb.append(spacer);
                    sb.append(endDate);
                }
            }
            String result = sb.toString();
            return result;
        }


        private void addWrapped(StringBuilder sb, String message) {
            if (generateHtml) {
                sb.append("<span class=\"licenceDetail\">");
            }            
            sb.append(message);
            if (generateHtml) {
                sb.append("</span>");
            }
        }

        private void addStartDate(StringBuilder sb, String startDate) {
            String prefix = (sb.length() == 0 || generateHtml) ? "" : spacer;
            if (startDate != null) {
                String msg = String.format("%s%s%s%s", prefix, mStart, spacer, startDate);
                addWrapped(sb, msg);
            }
        }

        private void addEndDate(StringBuilder sb, String endDate) {
            String prefix = (sb.length() == 0 || generateHtml) ? "" : spacer;
            if (endDate != null) {
                String msg = String.format("%s%s%s%s", prefix, mExpires, spacer, endDate);
                addWrapped(sb, msg);
            }
        }

        private String getAccessFromToDateStyle(DateTime dateTime1, DateTime dateTime2) {
            String dt1 = dateFormatter.print(dateTime1);
            String dt2 = dateFormatter.print(dateTime2);
            String result = getRollingLabelFromTo(dt1, dt2);
            return result;
        }

        private String getAccessFromToDateTimeStyle(DateTime dateTime1, DateTime dateTime2) {
            String dt1 = dateTimeFormatter.print(dateTime1);
            String dt2 = dateTimeFormatter.print(dateTime2);
            String result = getRollingLabelFromTo(dt1, dt2);
            return result;
        }

        private String getRollingLabelFromTo(String dt1, String dt2) {
            String result = getMessage(MSG_LABEL_ROLLING_FROM_TO, dt1, dt2);
            return result;
        }

        private DateTime getCreatedDateTime() {
            DateTime createTime = null;
            if (this.licenceDto.getLicenceDetail().getLicenceType() == LicenceType.ROLLING) {
                RollingLicenceDetailDto rolling = (RollingLicenceDetailDto) this.detail;
                if (rolling.getBeginOn() == RollingBeginOn.CREATION) {

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
                }
            }
            return createTime;
        }

    }
    
    public static String getErrorDescription(LicenceDto dto, Locale locale) {
        StringBuilder sb = new StringBuilder();
        sb.append("problem getting licence description for ");
        sb.append("licDto[");
        sb.append(ToStringBuilder.reflectionToString(dto));
        if (dto != null) {
            sb.append("]licDetail[");
            sb.append(ToStringBuilder.reflectionToString(dto.getLicenceDetail()));
        }
        sb.append("]");
        sb.append("locale[");
        sb.append(ToStringBuilder.reflectionToString(locale));
        sb.append("]");
        String result = sb.toString();
        return result;
    }

}
