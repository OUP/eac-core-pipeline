package com.oup.eac.job;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.mail.internet.InternetAddress;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.joda.time.DateTime;

import au.com.bytecode.opencsv.CSVWriter;

import com.oup.eac.dao.ReportDetailsDao;
import com.oup.eac.domain.MailCriteria;
import com.oup.eac.util.BatchJobProperties;
import com.oup.eac.util.EmailService;
import com.oup.eac.util.OupIdUtility;

public class OxedMalaysiaDataExportJob {


    private static final Logger LOG = Logger.getLogger(OxedMalaysiaDataExportJob.class);

    private static final String DATE_COL = "date";
    private static final String PRODUCT_COL = "product";
    private static final String LASTNAME_COL = "lastname";
    private static final String FIRSTNAME_COL = "firstname";
    private static final String EMAIL_COL = "email";
    private static final String EXPORT_DATE_FORMAT = "dd/MM/yyyy HH:mm:ss z";
    private static final String EXPORT_DATE_FORMAT_OXED_MALAYSIA = "dd/MM/yyyy HH:mm:ss z";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat(EXPORT_DATE_FORMAT);
    private static final int productIdIndex = 0 ;
    private static final int productNameIndex = 1 ;
    private static final int customerIdIndex = 2 ;
    private static final int customerFirstNameIndex = 4 ;
    private static final int customerLastNameIndex = 5 ;
    private static final int validatorEmailIndex = 6 ;
    private static final int customerCreateddateIndex = 14 ;
    
    private static final int ansTextIndex = 0 ;
    private static final int ansQuestionIndex = 1 ;

    private static final String EXPORT_EMAIL_SUBJECT = "EAC Data Export";
    private static final String EMAIL_TEMPLATE = "com/oup/eac/job/velocity/dataExportEmail.vm";
    private static final String EMAIL_TEXT_DATE_FORMAT = "EE d MMM yyyy HH:mm z";

    private static ReportDetailsDao reportDao = new ReportDetailsDao();
    private EmailService emailService = new EmailService();


    /**
     * Get user data by owner and date range.
     * 
     * @see com.oup.eac.service.DataExportService#getUserDataByOwnerAndDateRangeCSV(java.lang.String, java.util.Date, java.util.Date)
     * @param productOwner
     *            division owner
     * @param fromDT
     *            starting export period
     * @param toDT
     *            ending export period
     * @return list of customer data
     * @throws Exception 
     */
    public final List<String[]> getUserDataByOwnerAndDateRange(final String divisionType, final DateTime fromDT, final DateTime toDT) throws Exception {
       
    	final List<String[]> data = new ArrayList<String[]>();    
    		
    	String ER_DB_NAME_WITH_SCHEMA = BatchJobProperties.ER_DB_NAME  ;
        final List<Object[]> registrations = reportDao.getRegistrationsByDateAndDivision(fromDT, toDT, divisionType, ER_DB_NAME_WITH_SCHEMA);
        if (!(registrations == null || registrations.isEmpty())) {
            LOG.info("Got " + registrations.size() + " registrations");

            final String[] headers = getHeaders();
            data.add(headers);
            Map<String, String> values = null;
            String[] customerData = null;
            
            Map<String,String> oupUserMap = new HashMap<String,String>();
            Map<String,String> oupProductMap = new HashMap<String,String>();
            List<String> erightUserList = new ArrayList<String>();
            List<String> erightProductList = new ArrayList<String>();
            for (int i = 0 ; i < registrations.size() ; i ++) {
            	Object[] objArr = registrations.get(i) ;
    			erightUserList.add(objArr[customerIdIndex].toString());
    			erightProductList.add(objArr[productIdIndex].toString());
    			if(erightUserList.size()%1000 == 0){
    				oupUserMap.putAll(OupIdUtility.getOupIdsFromErightsIds(erightUserList, OupIdUtility.Entity.USER.name()));
    				erightUserList.clear();
    			}
    			if(erightProductList.size()%1000 == 0){
    				oupProductMap.putAll(OupIdUtility.getOupIdsFromErightsIds(erightProductList, OupIdUtility.Entity.PRODUCT.name()));
    				erightProductList.clear();
    			}
    		}
            oupUserMap.putAll(OupIdUtility.getOupIdsFromErightsIds(erightUserList, OupIdUtility.Entity.USER.name()));
            oupProductMap.putAll(OupIdUtility.getOupIdsFromErightsIds(erightProductList, OupIdUtility.Entity.PRODUCT.name()));
            
            for (Object[] registration : registrations) {
            	registration[productIdIndex] = oupProductMap.get(registration[productIdIndex].toString());
            	registration[customerIdIndex] = oupUserMap.get(registration[customerIdIndex].toString());
                values = getRegistrationData(registration);
                customerData = new String[headers.length];
                for (int i = 0; i < headers.length; i++) {
                    customerData[i] = StringUtils.defaultString(values.get(headers[i]));
                }
                data.add(customerData);
            }
        } else {
            LOG.info("No registrations for period");
        }
        return data;
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
    public final void eMailDataExport(final String divisionType, final DateTime fromDT, final DateTime toDT, final String emailTo, final String eacEmailBcc) throws Exception {
    	// get data
        final List<String[]> data = getUserDataByOwnerAndDateRange(divisionType, fromDT, toDT);

        if (!(data == null || data.isEmpty())) {
            // build cvs
            final StringWriter csvStringWriter = new StringWriter();
            final CSVWriter csvWriter = new CSVWriter(csvStringWriter);
            csvWriter.writeAll(data);
            final String csv = csvStringWriter.toString();
            
            // send email
            try {
            	final MailCriteria mailCriteria = new MailCriteria();
                mailCriteria.addToAddress(new InternetAddress(emailTo));
                mailCriteria.addBccAddress(new InternetAddress(eacEmailBcc));
                mailCriteria.setFrom(EmailService.getInternetAddressFrom());
                mailCriteria.setSubject(EXPORT_EMAIL_SUBJECT);
                mailCriteria.setText(getEmailText(fromDT, toDT));
                mailCriteria.setAttachmentName(getExportFileName(divisionType.toString()));
                //mailCriteria.setAttachmentName(getExportFileName());
                mailCriteria.setAttachment(csv.getBytes(Charset.forName("UTF-8")));

                emailService.sendMail(mailCriteria);
                csvWriter.close();
                //AuditLogger.logSystemEvent(DATA_EXPORT, data.size() + " registrations", divisionType.toString(), emailTo);
                LOG.info((data.size() - 1) + " registrations "+ emailTo);
            } catch (Exception e) {
                LOG.error("Unable to send email", e);
                throw e ;
            }
        } else {
            //AuditLogger.logSystemEvent(DATA_EXPORT, "0 registrations", divisionType.toString(), emailTo);
        	LOG.info( "0 registrations "+ emailTo);            
        }
    }

    /**
     * @param fromDT
     *            export start date
     * @param toDT
     *            export end date
     * @return Email body text
     * @throws IOException 
     * @throws VelocityException 
     */
    private String getEmailText(final DateTime fromDT, final DateTime toDT) throws VelocityException, IOException {
        final Map<String, Object> templateParams = new HashMap<String, Object>();
        VelocityEngine ve = new VelocityEngine();
        ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        ve.init();
        templateParams.put("dateFrom", fromDT.toString(EMAIL_TEXT_DATE_FORMAT));
        templateParams.put("dateTo", toDT.toString(EMAIL_TEXT_DATE_FORMAT));
        return EmailService.mergeTemplateIntoString(ve, EMAIL_TEMPLATE, templateParams);
    }

    /**
     * @param productOwner
     *            product owner
     * @return file name
     */
    private String getExportFileName(final String productOwner) {
        final StringBuilder sb = new StringBuilder();        
        sb.append(productOwner);
        sb.append("-export-");
        sb.append(new Date().getTime());
        sb.append(".csv");
        return sb.toString();
    }

    /**
     * Get header columns.
     * 
     * @return column names.
     * @throws SQLException 
     */
    private String[] getHeaders() throws SQLException {
        final List<String> headers = new ArrayList<String>();
        // static data
        headers.add(DATE_COL);
        headers.add(PRODUCT_COL);
        headers.add(EMAIL_COL);
        headers.add(FIRSTNAME_COL);
        headers.add(LASTNAME_COL);

        // dynamic data
        final List<String> questions = reportDao.getAllQuestions();
        Collections.sort(questions, new Comparator<String>() {
            // sort by export name
            public int compare(final String question1, final String question2) {
            	if(question1 == null) {
            		if(question2 == null) {
            			return 0;
            		}
            		return 1;
            	}
        		if(question2 == null) {
        			return -1;
        		}            	
                return question1.compareTo(question2);
            }
        });
        for (String question : questions) {
            if (StringUtils.isNotBlank(question) && !headers.contains(question)) {
                headers.add(question);
            } else{
                LOG.warn("No export name defined for question :" + question);
            }
        }
        String[] headerArray = headers.toArray(new String[headers.size()]);
        LOG.info("Headers " + Arrays.toString(headerArray));
        return headerArray;
    }

    /**
     * Get customer registration values.
     * 
     * @param registration
     *            customer registration
     * @return map of column header and values.
     * @throws SQLException 
     */
    private Map<String, String> getRegistrationData(final Object[] registration) throws SQLException {
    	final Map<String, String> data = new HashMap<String, String>();
    	
    	SimpleDateFormat dateFormat_OXED_MALAYSIA = new SimpleDateFormat(EXPORT_DATE_FORMAT_OXED_MALAYSIA);
    	dateFormat_OXED_MALAYSIA.setTimeZone(TimeZone.getTimeZone("GMT"));
        data.put(PRODUCT_COL, (registration[productNameIndex] != null) ? registration[productNameIndex].toString() : null);
        data.put(FIRSTNAME_COL, registration[customerFirstNameIndex] != null ? registration[customerFirstNameIndex].toString() : null);
        data.put(LASTNAME_COL, registration[customerLastNameIndex] != null ? registration[customerLastNameIndex].toString() : null);
        data.put(EMAIL_COL, registration[validatorEmailIndex] != null ? registration[validatorEmailIndex].toString() : null);
        data.put(DATE_COL, registration[customerCreateddateIndex] != null ? dateFormat_OXED_MALAYSIA.format(registration[customerCreateddateIndex]) : null); 

        final List<Object[]> answers = reportDao.getAnswerDetailsByCustomerAndProduct(registration[customerIdIndex].toString(), registration[productIdIndex].toString(), false);
        if (answers != null && answers.size() > 0) {
            for (Object[] answer : answers) {
            	data.put(answer[ansQuestionIndex].toString(), answer[ansTextIndex].toString());
            }
        }
        /*
        // dynamic data
        final List<Answer> answers = answerDao.getCustomerAnswerByPageDefinition(registration.getRegistrationDefinition().getPageDefinition(), customer);
        if (answers != null) {
            LOG.info("Got " + answers.size() + " answers");
            for (Answer answer : answers) {
            	if(StringUtils.isNotBlank(answer.getQuestion().getDescription())) {
	            	if(answer.getAnswerType() == AnswerType.PRODUCT_SPECIFIC_ANSWER) {
	            		ProductSpecificAnswer productSpecificAnswer = answerDao.getCustomerProductSpecificAnswerByQuestion(answer.getQuestion(), customer, product);
	            		if(productSpecificAnswer != null) {
	            			data.put(answer.getQuestion().getDescription(), productSpecificAnswer.getAnswerText());
		            		}
	            	} else {
	            		data.put(answer.getQuestion().getDescription(), answer.getAnswerText());
	            	}
            	}
            }
        }*/
        LOG.info("Customer registration " + data.toString());
        return data;
    }


}
