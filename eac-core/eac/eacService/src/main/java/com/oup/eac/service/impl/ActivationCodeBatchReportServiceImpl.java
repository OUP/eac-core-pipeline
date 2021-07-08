package com.oup.eac.service.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.mail.internet.InternetAddress;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import au.com.bytecode.opencsv.CSVWriter;

import com.oup.eac.cloudSearch.util.OupIdMappingUtil;
import com.oup.eac.common.utils.EACSettings;
import com.oup.eac.common.utils.MessageTextSource;
import com.oup.eac.common.utils.email.EmailUtils;
import com.oup.eac.common.utils.email.VelocityUtils;
import com.oup.eac.data.ActivationCodeBatchDao;
import com.oup.eac.domain.EacGroups;
import com.oup.eac.domain.MailCriteria;
import com.oup.eac.domain.Product;
import com.oup.eac.domain.utils.audit.AuditLogger;
import com.oup.eac.dto.ActivationCodeBatchReportCriteria;
import com.oup.eac.dto.ActivationCodeDto;
import com.oup.eac.integration.facade.ErightsFacade;
import com.oup.eac.integration.facade.exceptions.ErightsException;
import com.oup.eac.service.ActivationCodeBatchReportService;
import com.oup.eac.service.EacGroupService;
import com.oup.eac.service.EmailService;
import com.oup.eac.service.ProductService;

@Service("activationCodeBatchReportService")
public class ActivationCodeBatchReportServiceImpl implements ActivationCodeBatchReportService {
    
    private static final Logger LOGGER = Logger.getLogger(ActivationCodeBatchReportServiceImpl.class);
    
    private static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
    
    private static final String UTF8_ENCODING = "UTF-8";
    private static final String EMAIL_TEMPLATE = "com/oup/eac/service/velocity/report.vm";    
    private static final String EXPORT_EMAIL_SUBJECT = "Activation Code Report";
    
    private static final String BATCH_ID_COL = "batch id";
    private static final String BATCH_CREATED_DATE_COL = "batch created date";
    private static final String BATCH_START_DATE_COL = "batch start date";
    private static final String BATCH_END_DATE_COL = "batch end date";
    
    private static final String PRODUCT_GROUP_NAME_COL = "product group name";
    private static final String PRODUCT_ID_COL = "product id";
    private static final String PRODUCT_NAME_COL = "product name";
    
    private static final String LICENCE_TYPE_COL = "licence type";
    private static final String LICENCE_START_DATE_COL = "licence start date";
    private static final String LICENCE_END_DATE_COL = "licence end date";
    private static final String LICENCE_CONCURRENCY_COL = "licence concurrency";
    private static final String LICENCE_BEGIN_ON_COL = "licence begin on";
    private static final String LICENCE_PERIOD_COL = "licence period";
    private static final String LICENCE_UNITS_COL = "licence units";
    private static final String LICENCE_ALLOWED_USAGE_COL = "licence allowed usage";
    
    private static final String CODES_NUMBER_COL = "number of codes";
    private static final String CODES_ALLOWED_USAGE_COL = "allowed usages";
    private static final String CODES_ACTUAL_USAGE_COL = "actual usages";
    private static final String CODES_TOTAL_AVAILABLE_COL = "total available";
    private static final String CODES_TOTAL_UNAVAILABLE_COL = "total unavailable";
    
    private static final int BATCH_ID_INDEX = 1;
    private static final int BATCH_CREATED_DATE_INDEX = 2;
    private static final int BATCH_START_DATE_INDEX = 8;
    private static final int BATCH_END_DATE_INDEX = 9;
    
    private static final int PRODUCT_GROUP_NAME_INDEX = 12;
    private static final int PRODUCT_ID_INDEX = 11;
    private static final int PRODUCT_NAME_INDEX = 10;
    
    private static final int LICENCE_TYPE_INDEX = 13;
    private static final int LICENCE_START_DATE_INDEX = 14;
    private static final int LICENCE_END_DATE_INDEX = 15;
    private static final int LICENCE_CONCURRENCY_INDEX = 16;
    private static final int LICENCE_BEGIN_ON_INDEX = 18;
    private static final int LICENCE_PERIOD_INDEX = 19;
    private static final int LICENCE_UNITS_INDEX = 17;
    private static final int LICENCE_ALLOWED_USAGE_INDEX = 20;
    
    private static final int CODES_NUMBER_INDEX = 3;
    private static final int CODES_ALLOWED_USAGE_INDEX = 6;
    private static final int CODES_ACTUAL_USAGE_INDEX = 7;
    private static final int CODES_TOTAL_AVAILABLE_INDEX = 5;
    private static final int CODES_TOTAL_UNAVAILABLE_INDEX = 4;
    
    private static final String USAGE_LICENSE_TYPE = "Usage";
    private static final String CONCURRENT_LICENSE_TYPE = "Concurrent";
    private static final String ROLLING_LICENSE_TYPE = "Rolling";
    
    private static final String UNIT_TYPE_YEAR = "YEAR";
    private static final String UNIT_TYPE_MONTH = "MONTH";
    private static final String UNIT_TYPE_WEEK_OF_YEAR = "WEEK_OF_YEAR";
    private static final String UNIT_TYPE_DAY_OF_YEAR = "DAY_OF_YEAR";
    private static final String UNIT_TYPE_HOUR = "HOUR";
    private static final String UNIT_TYPE_MINUTE = "MINUTE";
    private static final String UNIT_TYPE_SECOND = "SECOND";
    private static final String UNIT_TYPE_MILLISECOND = "MILLISECOND";
    
    private static final String BEGINON_CREATION = "CREATION";
    private static final String BEGINON_FIRST_USE = "FIRST_USE";
    
    private final EmailService emailService;
    
    private final VelocityEngine velocityEngine;
    
    private final MessageSource messageSource;
    
    private final ActivationCodeBatchDao activationCodeBatchDao;
    
    private final ErightsFacade erightsFacade;
    
    private final ProductService productService;
    
    private final EacGroupService eacGroupService;
    
    @Autowired
    public ActivationCodeBatchReportServiceImpl(final EmailService emailService, 
    		final VelocityEngine velocityEngine, final MessageSource messageSource, 
    		final  ActivationCodeBatchDao activationCodeBatchDao, final ErightsFacade erightsFacade, 
    		final ProductService productService, final EacGroupService eacGroupService) {
        super();
        this.emailService = emailService;
        this.velocityEngine = velocityEngine;
        this.messageSource = messageSource;
        this.activationCodeBatchDao = activationCodeBatchDao;
        this.erightsFacade = erightsFacade;
        this.productService = productService;
        this.eacGroupService = eacGroupService;
    }
    
    @Override
    public void createActivationCodeReport(final ActivationCodeBatchReportCriteria reportCriteria, final String email) 
    {
    	AuditLogger.logEvent("Generated ActivationCodeBatchReport", "batchId:"+reportCriteria.getBatchId(), AuditLogger.activationCode(reportCriteria.getBatchId()));
    	
        List<String[]> data = getData(reportCriteria);
        final StringWriter csvStringWriter = new StringWriter();
        final CSVWriter csvWriter = new CSVWriter(csvStringWriter);
        csvWriter.writeAll(data);
        final String csv = csvStringWriter.toString();
        //added for testing: It should be remove.
        /*CSVWriter csvWriter1 = null;
        try{
            File file =new File("D:\\Users\\gaurang\\Softwares\\Wild fly\\wildfly-8.2.0.Final\\wildfly-8.2.0.Final\\standalone\\tmp\\test.csv");
            if(!file.exists()){
            	file.createNewFile();
            }
            csvWriter1 =  new CSVWriter(new FileWriter(file));
            csvWriter1.writeAll(data);
            
        }catch(Exception ex){
            ex.printStackTrace();
        }
        finally{
            if(csvWriter1!=null){
                try{csvWriter1.close();}catch(Exception ex){};
            }
        }*/
        //added for testing: It should be remove.                
        try {
            MailCriteria mailCriteria = new MailCriteria();
            mailCriteria.setFrom(EmailUtils.getInternetAddressFrom());
            mailCriteria.addToAddress(new InternetAddress(email));
            mailCriteria.setSubject(EXPORT_EMAIL_SUBJECT);
            mailCriteria.setText(getEmailText());
            mailCriteria.setAttachment(csv.getBytes(Charset.forName(UTF8_ENCODING)));
            mailCriteria.setAttachmentName(getExportFileName());
            emailService.sendMail(mailCriteria);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    } 
    
    @Override
    public long getActivationCodeReportSize(final ActivationCodeBatchReportCriteria reportCriteria) 
    		throws ErightsException { 
    	String ER_DB_NAME = EACSettings.getProperty("rs.db.name") ;
    	String ER_DB_SCHEMA = EACSettings.getProperty("rs.db.schema") ;
    	String ER_DB_NAME_WITH_SCHEMA = ER_DB_NAME + "." + ER_DB_SCHEMA  ;
    	try{
	    	if(reportCriteria.getProductId() != null){
	    		reportCriteria.setProductId(OupIdMappingUtil.getErightsIdFromOupId(reportCriteria.getProductId(), OupIdMappingUtil.Entity.PRODUCT.name()));
	    	}
	    	if(reportCriteria.getEacGroupId() != null){
	    		reportCriteria.setEacGroupId(OupIdMappingUtil.getErightsIdFromOupId(reportCriteria.getEacGroupId(), OupIdMappingUtil.Entity.PRODUCT_GROUP.name()));
	    	}
	    	return activationCodeBatchDao.getActivationCodeReportCount(reportCriteria, ER_DB_NAME_WITH_SCHEMA) ;
	    } catch (Exception e) {
			e.printStackTrace();
			LOGGER.error(e.getMessage(), e);
		}
		return 0;
    } 

    
    private final List<String[]> getData(final ActivationCodeBatchReportCriteria reportCriteria) {
    	String ER_DB_NAME = EACSettings.getProperty("rs.db.name") ;
    	String ER_DB_SCHEMA = EACSettings.getProperty("rs.db.schema") ;
    	String ER_DB_NAME_WITH_SCHEMA = ER_DB_NAME + "." + ER_DB_SCHEMA  ;
    	final List<String[]> data = new ArrayList<String[]>();        
        final List<Object[]> activationCodes = activationCodeBatchDao.getActivationCodeReport(reportCriteria, ER_DB_NAME_WITH_SCHEMA);
        if (!(activationCodes == null || activationCodes.isEmpty())) {
            LOGGER.info("Got " + activationCodes.size() + " activation code batches");
            Map<String,String> oupProductMap = new HashMap<String,String>();
            List<String> erightProductList = new ArrayList<String>();
            for (Object[] objArr : activationCodes) {
    			erightProductList.add(objArr[PRODUCT_ID_INDEX].toString());
    			if(erightProductList.size()%1000 == 0){
    				oupProductMap.putAll(OupIdMappingUtil.getOupIdsFromErightsIds(erightProductList, OupIdMappingUtil.Entity.PRODUCT.name()));
    				erightProductList.clear();
    			}
    		}
            oupProductMap.putAll(OupIdMappingUtil.getOupIdsFromErightsIds(erightProductList, OupIdMappingUtil.Entity.PRODUCT.name()));
            System.out.println(oupProductMap);
            final String[] headers = getHeaders();
            data.add(headers);
            Map<String, String> values = null;
            String[] customerData = null;
            for (Object[] activationCodeDto : activationCodes) {
            	activationCodeDto[PRODUCT_ID_INDEX] = oupProductMap.get(activationCodeDto[PRODUCT_ID_INDEX].toString());
                values = getActivationCodeData(activationCodeDto, reportCriteria);
                customerData = new String[headers.length];
                for (int i = 0; i < headers.length; i++) {
                    customerData[i] = StringUtils.defaultString(values.get(headers[i]));
                }
                data.add(customerData);
            }
        } else {
            LOGGER.info("No activation code batches for : " + reportCriteria);
        }
        return data;
    }    
    
    /**
     * @param fromDT
     *            export start date
     * @param toDT
     *            export end date
     * @return Email body text
     */
    private String getEmailText() {
        final Map<String, Object> templateParams = new HashMap<String, Object>();
        templateParams.put("resource", getResource(Locale.getDefault()));
        return VelocityUtils.mergeTemplateIntoString(velocityEngine, EMAIL_TEMPLATE, templateParams);
    }

    /**
     * @param productOwner
     *            product owner
     * @return file name
     */
    private String getExportFileName() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Activation-Code-Report-");
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
        headers.add(BATCH_ID_COL);
        headers.add(BATCH_CREATED_DATE_COL);
        headers.add(BATCH_START_DATE_COL);
        headers.add(BATCH_END_DATE_COL);
        headers.add(PRODUCT_GROUP_NAME_COL);
        headers.add(PRODUCT_ID_COL);
        headers.add(PRODUCT_NAME_COL);
        headers.add(LICENCE_TYPE_COL);
        headers.add(LICENCE_START_DATE_COL);
        headers.add(LICENCE_END_DATE_COL);
        headers.add(LICENCE_CONCURRENCY_COL);
        headers.add(LICENCE_BEGIN_ON_COL);
        headers.add(LICENCE_PERIOD_COL);
        headers.add(LICENCE_UNITS_COL);
        headers.add(LICENCE_ALLOWED_USAGE_COL);
        headers.add(CODES_NUMBER_COL);
        headers.add(CODES_ALLOWED_USAGE_COL);
        headers.add(CODES_ACTUAL_USAGE_COL);
        headers.add(CODES_TOTAL_AVAILABLE_COL);
        headers.add(CODES_TOTAL_UNAVAILABLE_COL);

        String[] headerArray = headers.toArray(new String[headers.size()]);
        LOGGER.info("Headers " + Arrays.toString(headerArray));
        return headerArray;
    }

    /**
     * Get customer registration values.
     * 
     * @param registration
     *            customer registration
     * @return map of column header and values.
     */
    Map<String, String> getActivationCodeData(final Object[] activationCodeDto, final ActivationCodeBatchReportCriteria reportCriteria) {
        final Map<String, String> data = new HashMap<String, String>();
        
        data.put(BATCH_ID_COL, activationCodeDto != null && activationCodeDto[BATCH_ID_INDEX] != null ? activationCodeDto[BATCH_ID_INDEX].toString() : null);
        data.put(BATCH_CREATED_DATE_COL, (activationCodeDto != null && activationCodeDto[BATCH_CREATED_DATE_INDEX] != null) ? DATE_TIME_FORMAT.format(activationCodeDto[BATCH_CREATED_DATE_INDEX]) : null);
        data.put(BATCH_START_DATE_COL, (activationCodeDto != null && activationCodeDto[BATCH_START_DATE_INDEX] != null) ? DATE_FORMAT.format(activationCodeDto[BATCH_START_DATE_INDEX]) : null);
        data.put(BATCH_END_DATE_COL, (activationCodeDto != null && activationCodeDto[BATCH_END_DATE_INDEX] != null) ? DATE_FORMAT.format(activationCodeDto[BATCH_END_DATE_INDEX]) : null);
        
        data.put(PRODUCT_GROUP_NAME_COL, activationCodeDto != null && activationCodeDto[PRODUCT_GROUP_NAME_INDEX] != null ? activationCodeDto[PRODUCT_GROUP_NAME_INDEX].toString() : null);
        data.put(PRODUCT_ID_COL, activationCodeDto != null && activationCodeDto[PRODUCT_ID_INDEX] != null ? activationCodeDto[PRODUCT_ID_INDEX].toString() : null);
        data.put(PRODUCT_NAME_COL, activationCodeDto != null && activationCodeDto[PRODUCT_NAME_INDEX] != null ? activationCodeDto[PRODUCT_NAME_INDEX].toString() : null);
        
        if(activationCodeDto != null && activationCodeDto[LICENCE_TYPE_INDEX] != null){
        	switch(activationCodeDto[LICENCE_TYPE_INDEX].toString()){
        	case "1":
        		data.put(LICENCE_TYPE_COL, CONCURRENT_LICENSE_TYPE);
        		data.put(LICENCE_CONCURRENCY_COL, (activationCodeDto != null && activationCodeDto[LICENCE_CONCURRENCY_INDEX] != null) ? activationCodeDto[LICENCE_CONCURRENCY_INDEX].toString() : null);
        		break;
        	case "2":
        		data.put(LICENCE_TYPE_COL, ROLLING_LICENSE_TYPE);
        		data.put(LICENCE_PERIOD_COL, (activationCodeDto != null && activationCodeDto[LICENCE_PERIOD_INDEX] != null) ? activationCodeDto[LICENCE_PERIOD_INDEX].toString() : null);
        		if(activationCodeDto != null && activationCodeDto[LICENCE_UNITS_INDEX] != null){
                	data.put(LICENCE_UNITS_COL, getLicenceUnit(activationCodeDto[LICENCE_UNITS_INDEX].toString()));
        		}
        		if(activationCodeDto != null && activationCodeDto[LICENCE_BEGIN_ON_INDEX] != null){
                	data.put(LICENCE_BEGIN_ON_COL, getBeginOn(activationCodeDto[LICENCE_BEGIN_ON_INDEX].toString()));
        		}
        		break;
        	case "6":
        		data.put(LICENCE_TYPE_COL, USAGE_LICENSE_TYPE);
        		data.put(LICENCE_ALLOWED_USAGE_COL, (activationCodeDto != null && activationCodeDto[LICENCE_ALLOWED_USAGE_INDEX] != null) ? activationCodeDto[LICENCE_ALLOWED_USAGE_INDEX].toString() : null);
        		break;
        	}
        }
        data.put(LICENCE_START_DATE_COL, (activationCodeDto != null && activationCodeDto[LICENCE_START_DATE_INDEX] != null) ? DATE_FORMAT.format(activationCodeDto[LICENCE_START_DATE_INDEX]) : null);
        data.put(LICENCE_END_DATE_COL, (activationCodeDto != null && activationCodeDto[LICENCE_END_DATE_INDEX] != null) ? DATE_FORMAT.format(activationCodeDto[LICENCE_END_DATE_INDEX]) : null);
        
        data.put(CODES_NUMBER_COL, (activationCodeDto != null && activationCodeDto[CODES_NUMBER_INDEX] != null) ? activationCodeDto[CODES_NUMBER_INDEX].toString() : null);
        data.put(CODES_ALLOWED_USAGE_COL, (activationCodeDto != null && activationCodeDto[CODES_ALLOWED_USAGE_INDEX] != null) ? activationCodeDto[CODES_ALLOWED_USAGE_INDEX].toString() : null);
        data.put(CODES_ACTUAL_USAGE_COL, (activationCodeDto != null && activationCodeDto[CODES_ACTUAL_USAGE_INDEX] != null) ? activationCodeDto[CODES_ACTUAL_USAGE_INDEX].toString() : null);
        data.put(CODES_TOTAL_AVAILABLE_COL, (activationCodeDto != null && activationCodeDto[CODES_TOTAL_AVAILABLE_INDEX] != null) ? activationCodeDto[CODES_TOTAL_AVAILABLE_INDEX].toString() : null);
        data.put(CODES_TOTAL_UNAVAILABLE_COL, (activationCodeDto != null && activationCodeDto[CODES_TOTAL_UNAVAILABLE_INDEX] != null) ? activationCodeDto[CODES_TOTAL_UNAVAILABLE_INDEX].toString() : null);

        LOGGER.info("Activation code batch details " + data.toString());
        return data;
    }   
    
    private String getName(String name) {
        StringBuilder stringBuilder = new StringBuilder(name.toLowerCase());
        stringBuilder.setCharAt(0, Character.toUpperCase(stringBuilder.charAt(0)));
        return stringBuilder.toString();
    }
    
    private MessageTextSource getResource(final Locale locale) {
        MessageTextSource result = new MessageTextSource(this.messageSource, locale);
        return result;
    }
    
    private String getLicenceUnit(String unitId){
    	switch(unitId){
    	case "1":
    		return UNIT_TYPE_YEAR;
    	case "2":
    		return UNIT_TYPE_MONTH;
    	case "3":
    		return UNIT_TYPE_WEEK_OF_YEAR;
    	case "6":
    		return UNIT_TYPE_DAY_OF_YEAR;
    	case "10":
    		return UNIT_TYPE_HOUR;
    	case "12":
    		return UNIT_TYPE_MINUTE;
    	case "13":
    		return UNIT_TYPE_SECOND;
    	case "14":
    		return UNIT_TYPE_MILLISECOND;
    	default:
    		return null;
    	}
    }
    
    private String getBeginOn(String beginOnId){
    	switch(beginOnId){
    	case "1":
    		return BEGINON_FIRST_USE;
    	case "2":
    		return BEGINON_CREATION;
    	default:
    		return null;
    	}
    }
    
}
