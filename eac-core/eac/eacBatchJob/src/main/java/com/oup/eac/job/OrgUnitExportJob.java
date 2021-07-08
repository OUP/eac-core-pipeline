package com.oup.eac.job;

import java.io.IOException;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.internet.InternetAddress;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import au.com.bytecode.opencsv.CSVWriter;

import com.oup.eac.dao.ReportDetailsDao;
import com.oup.eac.domain.MailCriteria;
import com.oup.eac.util.EmailService;


public class OrgUnitExportJob {

    
    private static final Logger LOG = Logger.getLogger(OrgUnitExportJob.class);

    private static final String PARAM_FROM = "from";
    private static final String PARAM_TO = "to";

    private static final String MONTH_FORMAT = "MMM-YYYY";
    private static final String TIME_FORMAT = "ddMMMYYYYHHmmss";

    private static final String EMAIL_TEMPLATE_FOR_ORG_UNIT_REPORT = "com/oup/eac/job/velocity/orgUnitDataExportEmail.vm";

    private static final String ORG_UNIT_USAGE_REPORT_SUBJECT = "EAC Org Unit Usage Report";

    private static final String ORG_UNIT_USAGE_REPORT_ATTACHMENT_PREFIX = "EacOrgUnitUsageReport";

    private static final Object DOT_CSV = ".csv";

    private static final String COL_ORG_UNIT = "Organisational Unit";
    private static final String COL_NUMBER   = "# Logins";
    private static final String COL_PERCENTAGE   = "% Logins";
    private static String[] REPORT_COL_HEADERS = { COL_ORG_UNIT, COL_NUMBER, COL_PERCENTAGE };

    private static EmailService emailService = new EmailService();
    private ReportDetailsDao reportDao = new ReportDetailsDao();
    
    public void emailOrgUnitUsageReport(DateTime fromTime, DateTime toTime, String emailTo, String emailBcc) throws Exception {
        List<String[]> usageReportData = this.getOrgUnitUsageReport(fromTime, toTime);
        String usageReportAttachmentDataAsCsv = this.getUsageReportAttachmentDataAsCsv(usageReportData);
        emailOrgUnitUsageReport(emailTo, emailBcc, usageReportAttachmentDataAsCsv, fromTime, toTime);
    }
    
    protected String getUsageReportAttachmentDataAsCsv(List<String[]> usageReportData) throws IOException {
        if(usageReportData == null || usageReportData.isEmpty()){
            return null;
        }else{
            StringWriter csvStringWriter = new StringWriter();
            CSVWriter csvWriter = new CSVWriter(csvStringWriter);
            csvWriter.writeAll(usageReportData);
            String csv = csvStringWriter.toString();
			csvWriter.close();
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
     * @throws Exception 
     * @throws ServiceLayerException
     *             service layer exception
     */
    protected final void emailOrgUnitUsageReport(final String emailTo, final String eacEmailBcc, String usageReportAttachmentDataAsCsv, DateTime fromTime, DateTime toTime) throws Exception {

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
            mailCriteria.setFrom(EmailService.getInternetAddressFrom());
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
            
            LOG.info(ORG_UNIT_USAGE_REPORT_SUBJECT+ timeFmtr.print(fromTime)+ timeFmtr.print(toTime)+ emailTo);
        } catch (Exception e) {
            LOG.error("Unable to send email ", e);
            throw e ;
        }
    }
    
    protected List<String[]> getOrgUnitUsageReport(DateTime fromTime, DateTime toTime) throws SQLException {
        List<String[]> usageData = null;
        usageData = reportDao.getOrgUnitUsage(fromTime, toTime);

        if (usageData == null) {
            usageData = new ArrayList<String[]>();
        }
        LOG.info("Got " + usageData.size() + " orgUnit usage data rows");
        if (usageData.isEmpty() == false) {
            usageData.add(0, REPORT_COL_HEADERS);
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
        VelocityEngine ve = new VelocityEngine();
        ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        ve.init();
        templateParams.put(PARAM_FROM, from);
        templateParams.put(PARAM_TO, to);
        return EmailService.mergeTemplateIntoString(ve, EMAIL_TEMPLATE_FOR_ORG_UNIT_REPORT, templateParams);
    }

}
