package com.oup.eac.service.impl;

import java.io.StringWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.internet.InternetAddress;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import au.com.bytecode.opencsv.CSVWriter;

import com.oup.eac.common.utils.email.EmailUtils;
import com.oup.eac.common.utils.email.VelocityUtils;
import com.oup.eac.data.DivisionDao;
import com.oup.eac.domain.MailCriteria;
import com.oup.eac.domain.utils.audit.AuditLogger;
import com.oup.eac.service.EmailService;
import com.oup.eac.service.OrgUnitUsageReportService;
import com.oup.eac.service.ServiceLayerException;

/**
 * Data Export Service.
 * 
 */
@Service(value="orgUnitUsageReportService")
public class OrgUnitUsageReportServiceImpl implements OrgUnitUsageReportService {

    private static final String PARAM_FROM = "from";
    private static final String PARAM_TO = "to";

    private static final String MONTH_FORMAT = "MMM-YYYY";
    private static final String TIME_FORMAT = "ddMMMYYYYHHmmss";

    private static final Logger LOG = Logger.getLogger(OrgUnitUsageReportServiceImpl.class);

    private static final String EMAIL_TEMPLATE_FOR_ORG_UNIT_REPORT = "com/oup/eac/service/velocity/orgUnitDataExportEmail.vm";

    private static final String ORG_UNIT_USAGE_REPORT_SUBJECT = "EAC Org Unit Usage Report";

    private static final String ORG_UNIT_USAGE_REPORT_ATTACHMENT_PREFIX = "EacOrgUnitUsageReport";

    private static final Object DOT_CSV = ".csv";

    private static final String COL_ORG_UNIT = "Organisational Unit";
    private static final String COL_NUMBER   = "# Logins";
    private static final String COL_PERCENTAGE   = "% Logins";
    private static String[] REPORT_COL_HEADERS = { COL_ORG_UNIT, COL_NUMBER, COL_PERCENTAGE };

    private EmailService emailService;
    private VelocityEngine velocityEngine;
    private final DivisionDao orgUnitDao;

    @Autowired
    public OrgUnitUsageReportServiceImpl(EmailService emailService, VelocityEngine velocityEngine,
            DivisionDao orgUnitDao) {
        super();
        Assert.notNull(emailService);
        Assert.notNull(velocityEngine);
        Assert.notNull(orgUnitDao);
        this.emailService = emailService;
        this.velocityEngine = velocityEngine;
        this.orgUnitDao = orgUnitDao;
    }
    
    @Override
    public void emailOrgUnitUsageReport(DateTime fromTime, DateTime toTime, String emailTo, String emailBcc) throws ServiceLayerException{
        List<String[]> usageReportData = this.getOrgUnitUsageReport(fromTime, toTime);
        String usageReportAttachmentDataAsCsv = this.getUsageReportAttachmentDataAsCsv(usageReportData);
        emailOrgUnitUsageReport(emailTo, emailBcc, usageReportAttachmentDataAsCsv, fromTime, toTime);
    }
    
    protected String getUsageReportAttachmentDataAsCsv(List<String[]> usageReportData) {
        if(usageReportData == null || usageReportData.isEmpty()){
            return null;
        }else{
            StringWriter csvStringWriter = new StringWriter();
            CSVWriter csvWriter = new CSVWriter(csvStringWriter);
            csvWriter.writeAll(usageReportData);
            String csv = csvStringWriter.toString();
            LOG.debug(csv);
            return csv;
        }
    }
    
    /**
     * Email user data by owner and date range to recipient.
     * 
     * @see com.oup.eac.service.DataExportService#eMailDataExport(java.lang.String, java.util.Date, java.util.Date, java.lang.String)
     * @param productOwner
     *            division owner
     * @param fromDT
     *            starting export period
     * @param toDT
     *            ending export period
     * @param emailTo
     *            recipient
     * @throws ServiceLayerException
     *             service layer exception
     */
    protected final void emailOrgUnitUsageReport(final String emailTo, final String eacEmailBcc, String usageReportAttachmentDataAsCsv, DateTime fromTime, DateTime toTime) throws ServiceLayerException {

        DateTimeFormatter monthFmtr = DateTimeFormat.forPattern(MONTH_FORMAT);
        DateTimeFormatter timeFmtr = DateTimeFormat.forPattern(TIME_FORMAT);
        String monthFrom = monthFmtr.print(fromTime);
        String monthTo   = monthFmtr.print(toTime);
        
        // send email
        try {
            String emailText = getEmailText(monthFrom, monthTo);

            final MailCriteria mailCriteria = new MailCriteria();
            mailCriteria.addToAddress(new InternetAddress(emailTo));
            if (StringUtils.isNotBlank(eacEmailBcc)) {
                mailCriteria.addBccAddress(new InternetAddress(eacEmailBcc));
            }
            mailCriteria.setFrom(EmailUtils.getInternetAddressFrom());
            mailCriteria.setSubject(ORG_UNIT_USAGE_REPORT_SUBJECT);
            boolean isEmpty = StringUtils.isBlank(usageReportAttachmentDataAsCsv);
            mailCriteria.setText(emailText);
            if (!isEmpty) {
                String attachmentFileName = String.format("%s_%s%s", ORG_UNIT_USAGE_REPORT_ATTACHMENT_PREFIX, monthFrom, DOT_CSV);
                mailCriteria.setAttachmentName(attachmentFileName);
                mailCriteria.setAttachment(usageReportAttachmentDataAsCsv.getBytes());
            }
            emailService.sendMail(mailCriteria);
            
            LOG.debug(mailCriteria.toString());
            
            AuditLogger.logSystemEvent(ORG_UNIT_USAGE_REPORT_SUBJECT, timeFmtr.print(fromTime), timeFmtr.print(toTime), emailTo);
        } catch (Exception e) {
            throw new ServiceLayerException("Unable to send email ", e);
        }
    }
    
    protected List<String[]> getOrgUnitUsageReport(DateTime fromTime, DateTime toTime) {
        List<String[]> usageData = null;
        try {
            usageData = this.orgUnitDao.getOrgUnitUsage(fromTime, toTime);

            if (usageData == null) {
                usageData = new ArrayList<String[]>();
            }
            LOG.info("Got " + usageData.size() + " orgUnit usage data rows");
            if (usageData.isEmpty() == false) {
                usageData.add(0, REPORT_COL_HEADERS);
            }
        } catch (SQLException sqlex) {
            LOG.warn("problem getting org unit data", sqlex);
        }
        return usageData;
    }

    /**
     * @param fromDT
     *            export start date
     * @param toDT
     *            export end date
     * @return Email body text
     */
    private String getEmailText(final String from, String to) {
        final Map<String, Object> templateParams = new HashMap<String, Object>();        
        templateParams.put(PARAM_FROM, from);
        templateParams.put(PARAM_TO, to);
        return VelocityUtils.mergeTemplateIntoString(velocityEngine, EMAIL_TEMPLATE_FOR_ORG_UNIT_REPORT, templateParams);
    }


    /**
     * Get email service.
     * 
     * @return email service
     */
    public final EmailService getEmailService() {
        return emailService;
    }

    /**
     * Set email service.
     * 
     * @param emailService
     *            email service
     */
    public final void setEmailService(final EmailService emailService) {
        this.emailService = emailService;
    }

    /**
     * Get velocity engine.
     * 
     * @return velocity engine
     */
    public final VelocityEngine getVelocityEngine() {
        return velocityEngine;
    }

    /**
     * Set velocity engine.
     * 
     * @param velocityEngine
     *            the engine
     */
    public final void setVelocityEngine(final VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }

   

}
