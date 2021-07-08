package com.oup.eac.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.mail.internet.InternetAddress;

import org.apache.commons.io.IOUtils;
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
import com.oup.eac.data.ActivationCodeDao;
import com.oup.eac.data.AnswerDao;
import com.oup.eac.data.QuestionDao;
import com.oup.eac.data.RegistrationDao;
import com.oup.eac.domain.ActivationCode;
import com.oup.eac.domain.ActivationCodeBatch;
import com.oup.eac.domain.ActivationCodeRegistration;
import com.oup.eac.domain.Answer;
import com.oup.eac.domain.Answer.AnswerType;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.MailCriteria;
import com.oup.eac.domain.PageDefinition;
import com.oup.eac.domain.Product;
import com.oup.eac.domain.ProductRegistrationDefinition;
import com.oup.eac.domain.ProductSpecificAnswer;
import com.oup.eac.domain.Question;
import com.oup.eac.domain.RegisterableProduct;
import com.oup.eac.domain.Registration;
import com.oup.eac.domain.Registration.RegistrationType;
import com.oup.eac.domain.RollingLicenceTemplate.RollingBeginOn;
import com.oup.eac.domain.RollingLicenceTemplate.RollingUnitType;
import com.oup.eac.domain.utils.audit.AuditLogger;
import com.oup.eac.dto.ReportCriteria;
import com.oup.eac.dto.ReportCriteria.RegistrationState;
import com.oup.eac.integration.facade.ErightsFacade;
import com.oup.eac.integration.facade.exceptions.ErightsException;
import com.oup.eac.service.EmailService;
import com.oup.eac.service.PageDefinitionService;
import com.oup.eac.service.ProductService;
import com.oup.eac.service.RegistrationDefinitionService;
import com.oup.eac.service.ReportService;
import com.oup.eac.service.ServiceLayerException;

@Service("reportService")
public class ReportServiceImpl implements ReportService {
    
    private static final Logger LOGGER = Logger.getLogger(ReportServiceImpl.class);
    
    private static final String UTF8_ENCODING = "UTF-8";
    private static final String DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm:ss";
    private static final String EMAIL_TEMPLATE = "com/oup/eac/service/velocity/report.vm";
    private static final String EXPORT_EMAIL_SUBJECT = "Registration Report";
    private static final String PRODUCT_ID_COL = "product id";
    private static final String PRODUCT_NAME_COL = "name";
    
    private static final String CUSTOMER_ID_COL = "customer id";
    private static final String USERNAME_ID_COL = "username";
    private static final String FIRSTNAME_COL = "first name";
    private static final String SURNAME_COL = "surname";
    private static final String EMAIL_COL = "email address";
    private static final String EMAIL_VERIFICATION_COL = "email verification";
    private static final String LOCALE_COL = "locale";
    private static final String TIMZONE_COL = "time_zone";
    private static final String LAST_LOGIN_COL = "last login date";
    private static final String CREATED_COL = "created date";
    
    private static final String REG_DATE_COL = "registration date";
    private static final String REG_UPDATED_DATE_COL = "updated date";
    private static final String STATUS_COL = "status";
    private static final String REGISTRATION_TYPE_COL = "registration type";
    private static final String ACTIVATION_CODE_COL = "activation code";
    

    private final RegistrationDao registrationDao;
    
    private final EmailService emailService;
    
    private final AnswerDao answerDao;
    
    private final QuestionDao questionDao;
    
    private final VelocityEngine velocityEngine;
    
    private final MessageSource messageSource;
    
    private final ActivationCodeDao activationCodeDao;
    
    private final PageDefinitionService pageDefinitionService;
    
    private final ErightsFacade erightsFacade ; 
    
    private final RegistrationDefinitionService registrationDefinitionService ;
    
    private final ProductService productService ;
    
    @Autowired
    public ReportServiceImpl(final RegistrationDao registrationDao, final EmailService emailService, final AnswerDao answerDao, final QuestionDao questionDao, final VelocityEngine velocityEngine, final MessageSource messageSource, 
                                        final ActivationCodeDao activationCodeDao, final PageDefinitionService pageDefinitionService,  final ErightsFacade erightsFacade , 
                                        final RegistrationDefinitionService registrationDefinitionService,final ProductService productService) {
        super();
        this.registrationDao = registrationDao;
        this.emailService = emailService;
        this.answerDao = answerDao;
        this.questionDao = questionDao;
        this.velocityEngine = velocityEngine;
        this.messageSource = messageSource;
        this.activationCodeDao = activationCodeDao;
        this.pageDefinitionService = pageDefinitionService;
        this.erightsFacade = erightsFacade ;
        this.registrationDefinitionService = registrationDefinitionService ;
        this.productService = productService ;
    }

    @Override
    public void createReport(final ReportCriteria reportCriteria, final String email) throws ServiceLayerException 
    {
    	AuditLogger.logEvent("Generated RegistrationReport", "productId:"+reportCriteria.getProductId(), AuditLogger.registrationReport(reportCriteria));
    	
        String fileSuffix = ".csv";
        List<String[]> data = null ;
		try {
			data = getData(reportCriteria);
		} catch (ErightsException e1) {
			// TODO Auto-generated catch block
			LOGGER.error(e1.getMessage(), e1);
			throw new ServiceLayerException() ;
		}
        final StringWriter csvStringWriter = new StringWriter();
        final CSVWriter csvWriter = new CSVWriter(csvStringWriter);
        csvWriter.writeAll(data);
        final String csv = csvStringWriter.toString();   
        
        final String tempFileLocation = System.getProperty("java.io.tmpdir")+File.separator;
        try {
            MailCriteria mailCriteria = new MailCriteria();
            mailCriteria.setFrom(EmailUtils.getInternetAddressFrom());
            mailCriteria.addToAddress(new InternetAddress(email));
            mailCriteria.setSubject(EXPORT_EMAIL_SUBJECT);
            mailCriteria.setText(getEmailText());            
            
            String filename = getExportFileName(reportCriteria.getStartIndex(), data.size());
            mailCriteria.setAttachmentName(filename +".zip");
            byte[] csvData = csv.getBytes(Charset.forName(UTF8_ENCODING));
            
            // Create directory if it doesn't exists
            File tempDir = new File(tempFileLocation);
            if (!tempDir.exists()) tempDir.mkdirs();
            
            zipIt(csvData, filename  + fileSuffix, tempFileLocation + filename +".zip"); 
            File zipOutputfile = new File(tempFileLocation + filename +".zip");
            FileInputStream fis = new FileInputStream(zipOutputfile);
            byte[] zipData =  IOUtils.toByteArray(fis);
            
            mailCriteria.setAttachment(zipData);
            emailService.sendMail(mailCriteria);
            
            fis.close();
            
            if(zipOutputfile.exists()){
              boolean delFile = zipOutputfile.delete();
              LOGGER.info(zipOutputfile.getName() + " is deleted... Flag : " + delFile);             
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    } 
    
    @Override
    public long getReportSize(final ReportCriteria reportCriteria) throws ServiceLayerException {
    	if(reportCriteria.getProductId() != null){
    		reportCriteria.setProductId(OupIdMappingUtil.getErightsIdFromOupId(reportCriteria.getProductId(), OupIdMappingUtil.Entity.PRODUCT.name()));
    	}
    	return getReportCalculatedData(reportCriteria).size(); //registrationDao.getReportRegistrationsCount(reportCriteria, ER_DB_NAME_WITH_SCHEMA) ;
    } 

    private final List<Object[]> getReportCalculatedData(final ReportCriteria reportCriteria) throws ServiceLayerException{
    	String ER_DB_NAME = EACSettings.getProperty("rs.db.name") ;
    	String ER_DB_SCHEMA = EACSettings.getProperty("rs.db.schema") ;
    	String ER_DB_NAME_WITH_SCHEMA = ER_DB_NAME + "." + ER_DB_SCHEMA  ;
    	List<Object[]> fetchData = null;
    	try{
    		fetchData = registrationDao.getReportRegistrations(reportCriteria, ER_DB_NAME_WITH_SCHEMA);
    	} catch(Exception e){
    		LOGGER.error(e.getMessage(), e);
    		throw new ServiceLayerException() ;
    	}
    	boolean isActivated = false, reqActivated = reportCriteria.isActivated() ;
    	boolean isDenied = false, reqDenied = reportCriteria.isDenied() ;
    	boolean isPending = false, reqPending = reportCriteria.isPending() ;
    	boolean isExpired = false, reqExpired = reportCriteria.isExpired() ;
    	boolean isDisabled = false, reqDisabled = reportCriteria.isDisabled() ;
    	List<Object[]> actualData = new ArrayList<Object[]>() ;
    	for(Object[] reportData :fetchData ) {
    		isActivated = false ;
    		isDenied = false ;
    		isPending = false ;
    		isExpired = false ;
    		isDisabled = false ;
    		
    		isActivated = checkActivated(reportData) ;
    		if (reportData[13] != null ) {
    			isDenied = Integer.parseInt(reportData[13].toString()) == 0 ? false : true ;
    		} 
    		if (reportData[14] != null ){
    			isPending = Integer.parseInt(reportData[14].toString()) == 0 ? false : true ;
    		}
    		if (reportData[25] != null ) {
    			isDisabled = Integer.parseInt(reportData[25].toString()) == 0 ? true : false ;
    		}
    		isExpired = checkExpired(reportData) ;
    		if (reqPending && isPending) {
    			reportData[34] = RegistrationState.PENDING.toString() ;
    			actualData.add(reportData);
    			continue;
    		}
    		if (reqActivated && isActivated) {
    			reportData[34] = RegistrationState.ACTIVATED.toString() ;
    			actualData.add(reportData);
    			continue;
    		}
    		if (reqDenied && isDenied) {
    			reportData[34] = RegistrationState.DENIED.toString() ;
    			actualData.add(reportData);
    			continue;
    		}
    		if (reqExpired && isExpired) {
    			reportData[34] = RegistrationState.EXPIRED.toString() ;
    			actualData.add(reportData);
    			continue;
    		}
    		if (reqDisabled && isDisabled) {
    			reportData[34] = RegistrationState.DISABLED.toString() ;
    			actualData.add(reportData);
    			continue;
    		}
    	}
    	return actualData ;
    }
    private boolean checkExpired(Object[] reportData) {
    	int licenseType = Integer.parseInt(reportData[21].toString()) ; //At index 21 license type is set
    	Timestamp timeStamp = null ;
    	Date now = new Date() ;
    	switch(licenseType) {
    	case 1 : {
    		if (reportData[24] != null) {
    			timeStamp = (Timestamp)reportData[24] ;
    			Date endDate = new Date(timeStamp.getTime());
    			if (now.after(endDate)) {
    				return true ;
    			}
    		}
    		break ;
    	}
    	case 2 :{
    		String beginOn;
    		Date createdOrFirstUseDate = null ;
			Date startDate = null;
			Date endDate = null;
			String licensePeriodUnit;
			int licensePeriod;
			
			if (Integer.parseInt(reportData[28].toString()) == 1 ) {
    			beginOn = RollingBeginOn.FIRST_USE.toString() ;
    			if (reportData[31] != null) {
    				timeStamp = (Timestamp)reportData[31] ;
    				createdOrFirstUseDate = new Date(timeStamp.getTime()) ;
    			} else {
    				createdOrFirstUseDate = null ;
    			}
    			
    		} else {
    			beginOn = RollingBeginOn.CREATION.toString() ;
    			timeStamp = (Timestamp)reportData[19] ;
    			createdOrFirstUseDate = new Date(timeStamp.getTime()) ;
    		}
			if (reportData[23] != null ) {// At index 23 license start date is set
				timeStamp = (Timestamp)reportData[23] ;
				startDate = new Date(timeStamp.getTime());
			}
			if (reportData[24] != null ) {//// At index 24 license end date is set
				timeStamp = (Timestamp)reportData[24] ;
				endDate = new Date(timeStamp.getTime());
			}
			licensePeriod = Integer.parseInt(reportData[26].toString()) ;
			licensePeriodUnit = convertPeriodunit(Integer.parseInt(reportData[27].toString())) ;
			Date expiredDate = getRollingExpiryDate(beginOn, createdOrFirstUseDate, startDate, endDate, licensePeriodUnit, licensePeriod) ;
			if (expiredDate != null ) {
				if (now.after(expiredDate)) {
					return true ;
				}
			}
			break ;
    	}
    	case 6 :{
    		if (reportData[26] != null ) { // At index 26 license usage value set 
    			if (reportData[32] != null ) { // At index 32 license total usage used value set 
    				if (Integer.parseInt(reportData[32].toString()) == Integer.parseInt(reportData[26].toString())) {
    					return true ;
    				}
    			}
    		}
    		break ;
    	}
    	
    	default :
    		break ;
    	}
		return false;
	}

	private String convertPeriodunit(int periodUnit) {
		String returnValue ;
		switch(periodUnit){
		case 1 :
			returnValue = RollingUnitType.YEAR.toString() ;
			break ;
		case 2 :
			returnValue = RollingUnitType.MONTH.toString() ;
			break ;
		case 3 :
			returnValue = RollingUnitType.WEEK.toString() ;
			break ;
		case 5 :
			returnValue = RollingUnitType.DAY.toString() ;
			break ;
		case 10 :
			returnValue = RollingUnitType.HOUR.toString() ;
			break ;
		case 12 :
			returnValue = RollingUnitType.MINUTE.toString() ;
			break ;
		default :
			returnValue = null ;
			break ;
		}
		return returnValue ;
	}

	private boolean checkActivated(Object[] reportData) {
		int isEnabled = Integer.parseInt(reportData[25].toString()) ;// At index 25 license enable or not is set
		Date now = new Date() ;
		Timestamp timeStamp = null ;
		if(isEnabled != 1) {
			return false ;
		}
		if (reportData[22] != null ) { // At index 22 license paused date is set
			return false ;
		}
		if (reportData[23] != null ) {// At index 23 license start date is set
			timeStamp = (Timestamp)reportData[23] ;
			Date startDate = new Date(timeStamp.getTime());
			if (now.before(startDate)) {
				return false ;
			}
		}
		if (reportData[24] != null ) {//// At index 24 license end date is set
			timeStamp = (Timestamp)reportData[24] ;
			Date endDate = new Date(timeStamp.getTime());
			if (now.after(endDate)) {
				return false ;
			}
		}
		if (reportData[26] != null ) { // At index 26 license usage value set 
			if (reportData[32] != null ) { // At index 32 license total usage used value set 
				if (Integer.parseInt(reportData[32].toString()) == Integer.parseInt(reportData[26].toString())) {
					return false ;
				}
			}
		}
		if ( reportData[2] == null ) { // At index 2 product id set
			return false ;
		}
		if (reportData[29] != null ) { // At index 29 product suspended date is set
			return false ;
		}
		if ( reportData[30] != null ) { // At index 30 user suspended date is set
			return false ;
		}
		return true;
	}

	private final List<String[]> getData(final ReportCriteria reportCriteria) throws ErightsException, ServiceLayerException {
        final List<String[]> data = new ArrayList<String[]>();   
        //RegistrationReportsDto registrationReportsDto = erightsFacade.getRegistrationReports(reportCriteria) ;
        List<Object[]> datas = getReportCalculatedData(reportCriteria) ;
        Map<String,String> oupUserMap = new HashMap<String,String>();
        Map<String,String> oupProductMap = new HashMap<String,String>();
        List<String> erightUserList = new ArrayList<String>();
        List<String> erightProductList = new ArrayList<String>();
        for (int i = reportCriteria.getStartIndex()-1 ;i <= reportCriteria.getMaxResults() && i < datas.size() ; i ++) {
        	Object[] objArr = datas.get(i) ;
			erightUserList.add(objArr[3].toString());
			erightProductList.add(objArr[1].toString());
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
        
		//String[] registrationDatas =  registrationReportsDto.getReportFile().split("\\r?\\n");
		final String[] headers = getHeaders();
		data.add(headers);
		Map<String, String> values = null;
        String[] customerData = null;
		for(int i = reportCriteria.getStartIndex()-1 ;i <= reportCriteria.getMaxResults() && i < datas.size() ; i ++) {
			Object[] registarionData = datas.get(i) ;
			registarionData[1] = oupProductMap.get(registarionData[1].toString());
			registarionData[3] = oupUserMap.get(registarionData[3].toString());
			values = getRegistrationData(registarionData, reportCriteria);
			customerData = new String[headers.length];
            for (int j = 0; j < headers.length; j++) {
                customerData[j] = StringUtils.defaultString(values.get(headers[j]));
            }
            data.add(customerData);
		}
		
        /*if (!(values == null || values.isEmpty())) {
            LOGGER.info("Got " + values.size() + " registrations");

            final String[] headers = getHeaders();
            data.add(headers);
            Map<String, String> values = null;
            String[] customerData = null;
            for (Registration<?> registration : registrations) {
                values = getRegistrationData(registration, reportCriteria);
                customerData = new String[headers.length];
                for (int i = 0; i < headers.length; i++) {
                    customerData[i] = StringUtils.defaultString(values.get(headers[i]));
                }
                data.add(customerData);
            }
        } else {
            LOGGER.info("No registrations for : " + reportCriteria);
        }*/
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
    private String getExportFileName(int startIndex, int dataCount) {
        int endIndex = startIndex + dataCount - 2;  // As startIndex is starting from zero(0), so substrating 2 from endIndex as we have to remove 1 value for header also. 
        final StringBuilder sb = new StringBuilder();        
        sb.append("Registration-Report-");
        sb.append(startIndex + "_to_" + endIndex + "-");
        sb.append(new Date().getTime());        
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
        headers.add(PRODUCT_ID_COL);
        headers.add(PRODUCT_NAME_COL);
        headers.add(CUSTOMER_ID_COL);
        headers.add(USERNAME_ID_COL);
        headers.add(FIRSTNAME_COL);
        headers.add(SURNAME_COL);
        headers.add(EMAIL_COL);
        headers.add(EMAIL_VERIFICATION_COL);
        headers.add(LOCALE_COL);
        headers.add(TIMZONE_COL);
        headers.add(LAST_LOGIN_COL);
        headers.add(CREATED_COL);
        headers.add(REG_DATE_COL);
        headers.add(REG_UPDATED_DATE_COL);
        headers.add(STATUS_COL);
        headers.add(REGISTRATION_TYPE_COL);
        headers.add(ACTIVATION_CODE_COL);


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
                LOGGER.warn("No export name defined for question id:" + question.getId());
            }
        }
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
    private Map<String, String> getRegistrationData(final Object[] registrationData, final ReportCriteria reportCriteria) {
        final Map<String, String> data = new HashMap<String, String>();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT);
        int productIdIndex = 1 ;
        int productNameIndex = 2 ;
        int customerIdIndex = 3 ;
        int customerUserNameIndex = 4 ;
        int customertFirstNameIndex = 5 ;
        int customerLastNameIndex = 6 ;
        int validatorEmailIndex = 7 ;
        int emailVerificationIndex = 8 ;
        int localeIndex = 9 ;
        int timeZoneIndex = 10 ;
        int activationCodeIndex = 11 ;
        int registrationTypeIndex = 16 ;
        int customertLastLoginIndex = 17 ;
        int customerCreateddateIndex = 18 ;
        int regCreatedDateIndex = 19 ;
        int regUpdatedDate = 20 ;
        int statusIndex = 34 ;
        
        String activationCode = new String() ; 
        if (registrationData[ activationCodeIndex ] != null ) {
        	activationCode = "Batch id: " + registrationData[activationCodeIndex].toString() + " Code: " + registrationData[activationCodeIndex + 1 ].toString() ;
        }
        
        String status = registrationData[statusIndex].toString() ;
        /*if (status == null) {
        	status = RegistrationState.ACTIVATED.toString() ;
        }*/
        
        Product product = new RegisterableProduct() ;
        
        Customer customer = new Customer() ;
        if ( registrationData[ customerIdIndex ] != null ) {
        	customer.setId(registrationData[ customerIdIndex ].toString() );
        }
        
        data.put(PRODUCT_ID_COL, registrationData[productIdIndex] == null ? null : registrationData[productIdIndex].toString());
        data.put(PRODUCT_NAME_COL, registrationData[productNameIndex] == null ? null : registrationData[productNameIndex].toString());
        data.put(CUSTOMER_ID_COL, registrationData[customerIdIndex] == null ? null : registrationData[customerIdIndex].toString());
        data.put(USERNAME_ID_COL, registrationData[customerUserNameIndex] == null ? null : registrationData[customerUserNameIndex].toString());
        data.put(FIRSTNAME_COL, registrationData[customertFirstNameIndex] == null ? null : registrationData[customertFirstNameIndex].toString());
        data.put(SURNAME_COL, registrationData[customerLastNameIndex] == null ? null : registrationData[customerLastNameIndex].toString());
        data.put(EMAIL_COL, registrationData[validatorEmailIndex] == null ? null : registrationData[validatorEmailIndex].toString());
        data.put(EMAIL_VERIFICATION_COL, registrationData[emailVerificationIndex] == null ? null : registrationData[emailVerificationIndex].toString());
        data.put(LOCALE_COL, registrationData[localeIndex] == null ? null : registrationData[localeIndex].toString());
        data.put(TIMZONE_COL, registrationData[timeZoneIndex] == null ? null : registrationData[timeZoneIndex].toString());
        data.put(LAST_LOGIN_COL, registrationData[customertLastLoginIndex] == null ? null : sdf.format((Date)registrationData[customertLastLoginIndex]).toString());
        data.put(CREATED_COL, registrationData[customerCreateddateIndex] == null ? null : sdf.format((Date)registrationData[customerCreateddateIndex]).toString()); 
        data.put(REG_DATE_COL, registrationData[regCreatedDateIndex] == null ? null : sdf.format((Date)registrationData[regCreatedDateIndex]).toString());  
        data.put(REG_UPDATED_DATE_COL, registrationData[regUpdatedDate] == null ? null : sdf.format((Date)registrationData[regUpdatedDate]).toString());  
        data.put(STATUS_COL, status.isEmpty() ? null : status);
        data.put(REGISTRATION_TYPE_COL, registrationData[registrationTypeIndex] == null ? null : registrationData[registrationTypeIndex].toString());
        data.put(ACTIVATION_CODE_COL, registrationData[activationCodeIndex] == null ? null : activationCode);
        
        
        // dynamic data
        if(  registrationData[productIdIndex] != null && registrationData[customerIdIndex] != null ) {
        	PageDefinition pageDefinition = null ;
        	ProductRegistrationDefinition pageRegDefinition = null ;
			try {
				product.setId(registrationData[ productIdIndex ].toString());  
						//productService.getProductById(registrationData[ productIdIndex ].toString()) ;
				pageRegDefinition = registrationDefinitionService.getProductRegistrationDefinitionByProduct(product);
			} catch (ServiceLayerException e) {
				// TODO Auto-generated catch block
				LOGGER.error(e.getMessage(), e);
			}
        	if (pageRegDefinition != null ) {
        		pageDefinition = pageRegDefinition.getPageDefinition() ;
        	}
        	if (pageDefinition != null ) {
	            final List<Question> questions = pageDefinitionService.getPageDefinitionQuestions(pageDefinition);
	            if(questions != null && questions.size() > 0) {
	    	        final List<Answer> answers = answerDao.getCustomerAnswerByQuestions(questions, customer);
	    	        if (answers != null && answers.size() > 0) {
	    	            LOGGER.info("Got " + answers.size() + " answers");
	    	            for (Answer answer : answers) {
	    	                if(StringUtils.isNotBlank(answer.getQuestion().getDescription())) {
	    	                    if(answer.getAnswerType() == AnswerType.PRODUCT_SPECIFIC_ANSWER) {
	    	                        ProductSpecificAnswer productSpecificAnswer = answerDao.getCustomerProductSpecificAnswerByQuestion(answer.getQuestion(), customer, (RegisterableProduct)product);
	    	                        if(productSpecificAnswer != null) {
	    	                            data.put(answer.getQuestion().getDescription(), productSpecificAnswer.getAnswerText());
	    	                        }
	    	                    } else {
	    	                        data.put(answer.getQuestion().getDescription(), answer.getAnswerText());
	    	                    }
	    	                }
	    	            }
	    	        }
	            }
	        }
        }
        LOGGER.info("Customer registration " + data.toString());
        return data;
    }   
    
    private String getStatus(Registration<?> registration) {
    	StringBuilder status = new StringBuilder();
    	boolean pending = !registration.isDenied() && !registration.isActivated();
    	boolean activated = !registration.isDenied() && registration.isActivated();
    	boolean denied = registration.isDenied();
        if(pending) {
            addStatus(status, "Pending");
        }
        if(activated) {
            addStatus(status, "Activated");
        }
        if(denied) {
            addStatus(status, "Denied");
        }
        return status.toString();
    }
    
    private void addStatus(StringBuilder statusStr, String status) {
        if(statusStr.length() > 0) {
            statusStr.append(", ");
        }
        statusStr.append(status);
    }
    
    private String getName(String name) {
        StringBuilder stringBuilder = new StringBuilder(name.toLowerCase());
        stringBuilder.setCharAt(0, Character.toUpperCase(stringBuilder.charAt(0)));
        return stringBuilder.toString();
    }
    
    private String getActivationCode(Registration<?> registration) {
        if(registration.getRegistrationType() == RegistrationType.PRODUCT) {
            return null;
        }
        ActivationCode activationCode = activationCodeDao.getActivationCodeAndBatchBy((ActivationCodeRegistration)registration);
        if(activationCode == null) {
            return null;
        }
        ActivationCodeBatch activationCodeBatch = activationCode.getActivationCodeBatch();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Batch id: ").append(activationCodeBatch.getBatchId()).append(" Code: ").append(activationCode.getCode());
        return stringBuilder.toString();
    }
    
    private String getPreferredLanguage(final Locale locale) {
        StringBuilder preferredLanguage = new StringBuilder();
        if (locale != null) {
            preferredLanguage.append(locale.getLanguage());
            if (StringUtils.isNotBlank(locale.getCountry())) {
                preferredLanguage.append("_");
                preferredLanguage.append(locale.getCountry());
            }
        }
        return preferredLanguage.toString();
    }
    
    private MessageTextSource getResource(final Locale locale) {
        MessageTextSource result = new MessageTextSource(this.messageSource, locale);
        return result;
    }
    
    
    private void zipIt(byte[] data, String entryFile, String outputFile){

        try{
            
            FileOutputStream fos = new FileOutputStream(outputFile);
            ZipOutputStream zos = new ZipOutputStream(fos);
    
            ZipEntry ze= new ZipEntry(entryFile);
            zos.putNextEntry(ze);
    
            zos.write(data);
           
            zos.closeEntry();
            //remember close it            
            zos.close();
            
        }catch(IOException ex){
            ex.printStackTrace();
            LOGGER.error(ex.getMessage(), ex);
        }
    }
    
    public Date getRollingExpiryDate(String beginOn, Date createdOrFirstUseDate, Date startDate, Date endDate, final String licensePeriodUnit, final int licensePeriod) {
		if (RollingBeginOn.CREATION.toString().equalsIgnoreCase(beginOn)) {
			// CREATION
			return getRollingExpiryForCreation(createdOrFirstUseDate,startDate,endDate,licensePeriodUnit, licensePeriod);
		} else if (RollingBeginOn.FIRST_USE.toString().equalsIgnoreCase(beginOn)) {
			// BEGIN_ON
			return getRollingExpiryForFirstUse(createdOrFirstUseDate,startDate,endDate,licensePeriodUnit, licensePeriod);
		} else {
			return null;
		}
	}
	
	private Date getRollingExpiryForCreation(Date createdDate, Date startDate, Date endDate, final String licensePeriodUnit, final int licensePeriod) {

		// creation date required
		// startdate optional
		// end date optional
		// licence period required
		
		Date normalExpiryDate = addLicencePeriodToDate(createdDate,licensePeriodUnit, licensePeriod);

		if (startDate != null && endDate != null
		        && startDate.after(endDate)) {
		}

		if (startDate != null && startDate.after(normalExpiryDate)) {
			return null;
		} else if (endDate != null && endDate.before(createdDate)) {
			return endDate;
		} else if (endDate == null) {
			return normalExpiryDate;
		} else if (endDate.before(normalExpiryDate)) {
			return endDate;
		} else {
			return normalExpiryDate;
		}

	}
	
	private Date getRollingExpiryForFirstUse(Date firstUse, Date startDate, Date endDate, final String licensePeriodUnit, final int licensePeriod) {
		// FIRST_USE
		// start date optional
		// end date optional
		// first use date optional
		// licence period required
		Date now = new Date() ;
		if (firstUse == null) {
			// No
			if (endDate == null) {
				// No
				return null;
			} else {
				// Yes
				if (startDate == null) {
					// No
					return null;
				} else {
					// Yes
					// Start date in future?
					if (startDate.after(now)) {
						// Yes
						return endDateLessThanStartDatePlusLicencePeriod(startDate, endDate,licensePeriodUnit, licensePeriod);
					} else {
						// No
						return endDateLessThanNowPlusLicencePeriod(startDate, endDate,licensePeriodUnit, licensePeriod);
					}
				}
			}
		} else {
			// Yes
			if (endDate == null) {
				// No expiry date = first use + licence period
				return addLicencePeriodToDate(firstUse,licensePeriodUnit, licensePeriod);
			} else {
				// Yes
				return endDateAfterFirstUsePlusLicencePeriod(firstUse, endDate, licensePeriodUnit, licensePeriod) ;
			}
		}
	}
private Date addLicencePeriodToDate(final Date date, final String licensePeriodUnit, final int licensePeriod) {
		
		Calendar rollingPeriodCal = Calendar.getInstance();
		rollingPeriodCal.setTime(date);

		switch (licensePeriodUnit) {
		case "YEAR":
			rollingPeriodCal.add(Calendar.YEAR, licensePeriod);
			break;
		case "MONTH":
			rollingPeriodCal.add(Calendar.MONTH, licensePeriod);
			break;
		case "WEEK":
			rollingPeriodCal.add(Calendar.WEEK_OF_YEAR, licensePeriod);
			break;
		case "DAY":
			rollingPeriodCal.add(Calendar.DAY_OF_YEAR, licensePeriod);
			break;
		case "HOUR":
			rollingPeriodCal.add(Calendar.HOUR, licensePeriod);
			break;
		case "MINUTE":
			rollingPeriodCal.add(Calendar.MINUTE, licensePeriod);
			break;
		case "SECOND":
			rollingPeriodCal.add(Calendar.SECOND, licensePeriod);
			break;
		case "MILLISECOND":
			rollingPeriodCal.add(Calendar.MILLISECOND, licensePeriod);
			break;
		default:
			
		}
		return rollingPeriodCal.getTime();
	}
	
	private Date endDateLessThanStartDatePlusLicencePeriod(Date startDate, Date endDate,final String licensePeriodUnit, final int licensePeriod) {
		if (endDate.before(addLicencePeriodToDate(startDate, licensePeriodUnit, licensePeriod))) {
			// Yes
			return endDate;
		} else {
			// No
			return null;
		}
	}
	
	private Date endDateLessThanNowPlusLicencePeriod(Date startDate, Date endDate, final String licensePeriodUnit, final int licensePeriod) {
		Date now = new Date() ;
		if (endDate.before(addLicencePeriodToDate(now,licensePeriodUnit, licensePeriod))) {
			return endDate;
		} else {
			return null;
		}
	}
	
	private Date endDateAfterFirstUsePlusLicencePeriod(Date firstUse, Date endDate, final String licensePeriodUnit, final int licensePeriod) {
		Date firstUsePlusLicencePeriod = addLicencePeriodToDate(firstUse,licensePeriodUnit, licensePeriod);
		if (endDate.after(firstUsePlusLicencePeriod)) {
			// Yes
			return firstUsePlusLicencePeriod;
		} else {
			return endDate;
		}
	}
    
}
