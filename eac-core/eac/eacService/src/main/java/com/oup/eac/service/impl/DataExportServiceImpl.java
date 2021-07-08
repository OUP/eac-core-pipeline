package com.oup.eac.service.impl;

import java.io.StringWriter;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.internet.InternetAddress;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import au.com.bytecode.opencsv.CSVWriter;

import com.oup.eac.cloudSearch.util.OupIdMappingUtil;
import com.oup.eac.common.utils.EACSettings;
import com.oup.eac.common.utils.email.EmailUtils;
import com.oup.eac.common.utils.email.VelocityUtils;
import com.oup.eac.data.AnswerDao;
import com.oup.eac.data.ElementDao;
import com.oup.eac.data.QuestionDao;
import com.oup.eac.data.RegistrationDao;
import com.oup.eac.domain.Answer;
import com.oup.eac.domain.Answer.AnswerType;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.MailCriteria;
import com.oup.eac.domain.ProductRegistrationDefinition;
import com.oup.eac.domain.ProductSpecificAnswer;
import com.oup.eac.domain.Question;
import com.oup.eac.domain.RegisterableProduct;
import com.oup.eac.domain.Registration;
import com.oup.eac.domain.utils.audit.AuditLogger;
import com.oup.eac.service.DataExportService;
import com.oup.eac.service.EmailService;
import com.oup.eac.service.ServiceLayerException;

/**
 * Data Export Service.
 * 
 */
@Service(value="dataExportService")
public class DataExportServiceImpl implements DataExportService {

    private static final Logger LOG = Logger.getLogger(DataExportServiceImpl.class);

    private static final String DATE_COL = "date";
    private static final String PRODUCT_COL = "product";
    private static final String DATA_EXPORT = "Data Export";
    private static final String LASTNAME_COL = "lastname";
    private static final String FIRSTNAME_COL = "firstname";
    private static final String EMAIL_COL = "email";
    private static final String EXPORT_DATE_FORMAT = "dd/MM/yyyy HH:mm:ss z";
    
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat(EXPORT_DATE_FORMAT);
    private static final int productIdIndex = 0 ;
    private static final int productNameIndex = 1 ;
    private static final int customerIdIndex = 2 ;
    private static final int customerUserNameIndex = 3 ;
    private static final int customerFirstNameIndex = 4 ;
    private static final int customerLastNameIndex = 5 ;
    private static final int validatorEmailIndex = 6 ;
    private static final int emailVerificationIndex = 7 ;
    private static final int localeIndex = 8 ;
    private static final int customertLastLoginIndex = 13 ;
    private static final int customerCreateddateIndex = 14 ;
    private static final int regCreatedDateIndex = 15 ;
    
    private static final int ansPgDefIdIndex = 2 ;
    private static final int ansTextIndex = 0 ;
    private static final int ansQuestionIndex = 1 ;

    private static final String EXPORT_EMAIL_SUBJECT = "EAC Data Export";
    private static final String EMAIL_TEMPLATE = "com/oup/eac/service/velocity/dataExportEmail.vm";
    private static final String EMAIL_TEXT_DATE_FORMAT = "EE d MMM yyyy HH:mm z";

    private RegistrationDao registrationDao = null;
    private ElementDao elementDao = null;
    private QuestionDao questionDao;
    private EmailService emailService = null;
    private VelocityEngine velocityEngine = null;
    private final AnswerDao answerDao;

    /**
     * Constructor.
     * 
     * @param registrationDao
     *            registration dao
     * @param elementDao
     *            element dao
     * @param emailService
     *            email service
     * @param velocityEngine
     *            velocity engine
     */
    @Autowired
    public DataExportServiceImpl(final RegistrationDao registrationDao, final ElementDao elementDao, final EmailService emailService,
            final VelocityEngine velocityEngine, final QuestionDao questionDao, final AnswerDao answerDao) {
        super();
        Assert.notNull(registrationDao);
        Assert.notNull(elementDao);
        Assert.notNull(emailService);
        Assert.notNull(velocityEngine);
        Assert.notNull(questionDao);
        Assert.notNull(answerDao);
        this.registrationDao = registrationDao;
        this.elementDao = elementDao;
        this.emailService = emailService;
        this.velocityEngine = velocityEngine;
        this.questionDao = questionDao;
        this.answerDao = answerDao;
    }

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
     */
    @Override
    public final List<String[]> getUserDataByOwnerAndDateRange(final String divisionType, final DateTime fromDT, final DateTime toDT) {
       
    	final List<String[]> data = new ArrayList<String[]>();        
    	try{
	    	String ER_DB_NAME_WITH_SCHEMA = EACSettings.getProperty("rs.db.name") + "." + EACSettings.getProperty("rs.db.schema")  ;
	        final List<Object[]> registrations = registrationDao.getRegistrationsByDateAndDivision(fromDT, toDT, divisionType, ER_DB_NAME_WITH_SCHEMA);
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
	    				oupUserMap.putAll(OupIdMappingUtil.getOupIdsFromErightsIds(erightUserList, OupIdMappingUtil.Entity.USER.name()));
	    				erightUserList.clear();
	    			}
	    			if(erightProductList.size()%1000 == 0){
	    				oupProductMap.putAll(OupIdMappingUtil.getOupIdsFromErightsIds(erightProductList, OupIdMappingUtil.Entity.PRODUCT.name()));
	    				erightProductList.clear();
	    			}
	    		}
	            oupUserMap.putAll(OupIdMappingUtil.getOupIdsFromErightsIds(erightUserList, OupIdMappingUtil.Entity.USER.name()));
	            oupProductMap.putAll(OupIdMappingUtil.getOupIdsFromErightsIds(erightProductList, OupIdMappingUtil.Entity.PRODUCT.name()));
	            
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
    	} catch(Exception e){
    		LOG.error(e.getMessage(), e);
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
     * @throws ServiceLayerException
     *             service layer exception
     */
    @Override
    public final void eMailDataExport(final String divisionType, final DateTime fromDT, final DateTime toDT, final String emailTo, final String eacEmailBcc) throws ServiceLayerException {
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
                mailCriteria.setFrom(EmailUtils.getInternetAddressFrom());
                mailCriteria.setSubject(EXPORT_EMAIL_SUBJECT);
                mailCriteria.setText(getEmailText(fromDT, toDT));
                mailCriteria.setAttachmentName(getExportFileName(divisionType.toString()));
                //mailCriteria.setAttachmentName(getExportFileName());
                mailCriteria.setAttachment(csv.getBytes(Charset.forName("UTF-8")));

                emailService.sendMail(mailCriteria);

                //AuditLogger.logSystemEvent(DATA_EXPORT, data.size() + " registrations", divisionType.toString(), emailTo);
                AuditLogger.logSystemEvent(DATA_EXPORT, data.size() + " registrations", emailTo);
            } catch (Exception e) {
                throw new ServiceLayerException("Unable to send email", e);
            }
        } else {
            //AuditLogger.logSystemEvent(DATA_EXPORT, "0 registrations", divisionType.toString(), emailTo);
        	AuditLogger.logSystemEvent(DATA_EXPORT, "0 registrations", emailTo);            
        }
    }

    /**
     * @param fromDT
     *            export start date
     * @param toDT
     *            export end date
     * @return Email body text
     */
    private String getEmailText(final DateTime fromDT, final DateTime toDT) {
        final Map<String, Object> templateParams = new HashMap<String, Object>();
        templateParams.put("dateFrom", fromDT.toString(EMAIL_TEXT_DATE_FORMAT));
        templateParams.put("dateTo", toDT.toString(EMAIL_TEXT_DATE_FORMAT));
        return VelocityUtils.mergeTemplateIntoString(velocityEngine, EMAIL_TEMPLATE, templateParams);
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
     */
    private String[] getHeaders() {
        final List<String> headers = new ArrayList<String>();
        // static data
        headers.add(DATE_COL);
        headers.add(PRODUCT_COL);
        headers.add(EMAIL_COL);
        headers.add(FIRSTNAME_COL);
        headers.add(LASTNAME_COL);

        // dynamic data
        final List<Question> questions = questionDao.findAll();
        Collections.sort(questions, new Comparator<Question>() {
            // sort by export name
            public int compare(final Question question1, final Question question2) {
            	if(question1.getDescription() == null) {
            		if(question2.getDescription() == null) {
            			return 0;
            		}
            		return 1;
            	}
        		if(question2.getDescription() == null) {
        			return -1;
        		}            	
                return question1.getDescription().compareTo(question2.getDescription());
            }
        });
        for (Question question : questions) {
            if (StringUtils.isNotBlank(question.getDescription()) && !headers.contains(question.getDescription())) {
                headers.add(question.getDescription());
            } else{
                LOG.warn("No export name defined for question id:" + question.getId());
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
     */
    private Map<String, String> getRegistrationData(final Object[] registration) {
    	final Map<String, String> data = new HashMap<String, String>();
    	
        data.put(PRODUCT_COL, (registration[productNameIndex] != null) ? registration[productNameIndex].toString() : null);
        data.put(FIRSTNAME_COL, registration[customerFirstNameIndex] != null ? registration[customerFirstNameIndex].toString() : null);
        data.put(LASTNAME_COL, registration[customerLastNameIndex] != null ? registration[customerLastNameIndex].toString() : null);
        data.put(EMAIL_COL, registration[validatorEmailIndex] != null ? registration[validatorEmailIndex].toString() : null);
        data.put(DATE_COL, registration[customerCreateddateIndex] != null ? dateFormat.format(registration[customerCreateddateIndex]) : null); 

        final List<Object[]> answers = answerDao.getAnswerDetailsByCustomerAndProduct(registration[customerIdIndex].toString(), registration[productIdIndex].toString(), false);
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


    /**
     * Get registration dao.
     * 
     * @return registrationDao
     */
    public final RegistrationDao getRegistrationDao() {
        return registrationDao;
    }

    /**
     * Set registration dao.
     * 
     * @param registrationDao
     *            registration dao.
     */
    public final void setRegistrationDao(final RegistrationDao registrationDao) {
        this.registrationDao = registrationDao;
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

    /**
     * Get element dao.
     * 
     * @return element dao
     */
    public final ElementDao getElementDao() {
        return elementDao;
    }

    /**
     * Set element dao.
     * 
     * @param elementDao
     *            element dao
     */
    public final void setElementDao(final ElementDao elementDao) {
        this.elementDao = elementDao;
    }

}
