package com.oup.eac.job;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.internet.InternetAddress;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.context.MessageSource;
import org.springframework.context.support.DelegatingMessageSource;

import au.com.bytecode.opencsv.CSVWriter;

import com.oup.eac.dao.ReportDetailsDao;
import com.oup.eac.domain.FTPCriteria;
import com.oup.eac.domain.MailCriteria;
import com.oup.eac.util.BatchJobProperties;
import com.oup.eac.util.EmailService;
import com.oup.eac.util.FTPService;
import com.oup.eac.util.OupIdUtility;
import com.oup.eac.util.SimpleCipher;



public class CMDPDataExportJob {

    private static final Logger LOG = Logger.getLogger(CMDPDataExportJob.class);
    
    private static final String UTF8_ENCODING = "UTF-8";
    
    private static final String FILE_NAME_PREFIX = "eac_tclub_reg_";
    
    private static final String EXPORT_DATE_FORMAT = "yyyyMMddHHmmss";
    
    private static final String FILE_NAME_POSTFIX= ".csv";
    
    private static final String FILE_NAME_DATE_FORMAT= "yyyyMMdd";
    
    private static final String PRODUCT_ID_COL = "[ProductId]";
    private static final String PRODUCT_NAME_COL = "[ProductName]";
    private static final String USER_ID_COL = "[UserId]";
    private static final String USERNAME_COL = "[Username]";    
    private static final String FIRSTNAME_COL = "[FirstName]";
    private static final String LASTNAME_COL = "[LastName]";
    private static final String EMAIL_COL = "[Email]";
    private static final String EMAIL_VERIFICATION_COL = "[EmailVerification]";
    private static final String LAST_LOGIN_DATE_COL = "[LastLoginDate]";
    private static final String CREATED_DATE_COL = "[CreatedDate]";
    private static final String REGISTRATION_DATE_COL = "[RegistrationDate]";
    private static final String UPDATED_DATE_COL = "[UpdatedDate]";
    private static final String STATUS_COL = "[Status]";
    private static final String ADDRESS1_COL = "[Address1]";
    private static final String ADDRESS2_COL = "[Address2]";
    private static final String ADDRESS3_COL = "[Address3]";
    private static final String CITY_COL = "[City]";
    private static final String POSTCODE_COL = "[Postcode]";
    private static final String INSTITUION_COL = "[Institution]";
    private static final String COUNTRY_COL = "[Country]";
    private static final String JOBTYPE_COL = "[JobType]";
    private static final String JOBTYPE_OTHER_COL = "[JobType Other]";
    private static final String EMAILFLAG_COL = "[EmailFlag]";
    private static final String INTERESTS_COL = "[Interests]";
    private static final String REFERRED_BY_COL = "[ReferredBy]";
    private static final String TANDC_COL = "[TANDC]";
    // private static final String RECEIVED_DATE_COL = "[ReceiveDate]";
    private static final String CAMPUS_COL = "[Campus]";
    private static final String DEPARTMENT_COL = "[Department]";
    private static final String INSTITUTION_TYPE_COL = "[InstitutionType]";
    private static final String PUBLIC_OR_PRIVATE_INSTITUTION_COL = "[PublicOrPrivateInstitution]";
    private static final String NUMBER_OF_STUDENTS_COL = "[NumberOfStudents]";
    private static final String TEACHING_STATUS_COL = "[TeachingStatus]";
    private static final String TEACHING_INTERESTS_COL = "[TeachingInterests]";
    private static final String EXAM_INTERESTS_COL = "[ExamInterests]";
    private static final String CLUBS_COL = "[Clubs]";
    private static final String PREFERRED_LANGUAGE_COL = "[PreferredLanguage]";
    private static final String MOBILE_COL = "[Mobile]";
    private static final String INSTITUTION_TYPE_KR_COL = "[InstitutionTypeKR]";
    private static final String TAIWAN_EXAM_INTERESTS_COL = "[ExamInterestsTW]";
    
    private static final SimpleDateFormat sdf = new SimpleDateFormat(EXPORT_DATE_FORMAT);
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
    private static final int updatedDateIndex = 16 ;
    private static final int ansPgDefIdIndex = 2 ;
    private static final int ansTextIndex = 0 ;
    private static final int ansExportNameIndex = 1 ;
    
    private final FTPService ftpService = new FTPService();
    
    private final MessageSource messageSource = new DelegatingMessageSource();
    
    private final EmailService emailService = new EmailService();
    
    private final ReportDetailsDao reportDao = new ReportDetailsDao();
    
    private final String divisionType = "ELT";
    
    private final String cmdpSupportEmail = BatchJobProperties.CMDP_SUPPORT_EMAIL;
   
    public CMDPDataExportJob() {
		super();
		
	}
    
	public final void ftpCMDPData( final DateTime fromDT, final DateTime toDT) throws Exception {
		
        try {
        	final Map<String, Map<String, Map<String, String>>> pageDefinitionExportNameMapCache = new HashMap<String, Map<String,Map<String,String>>>();
            // get data
        	final String[] headers = getHeaders(divisionType);
        	long registrationCount =0;
        	List<String> productIds = reportDao.getProductByDivision(divisionType);
        	ArrayList<String> productErightsIds = new ArrayList<String>();
        	LOG.info(productIds);
        	if(productIds!=null && productIds.size()>0){
        		Map<String, String> productIdsMap = OupIdUtility.getErightsIdsFromOupIds(productIds, false, OupIdUtility.Entity.PRODUCT.name());
        		Collection<String> productErightsIdsMap =  productIdsMap.values();
        		productErightsIds.addAll(productErightsIdsMap);
        		LOG.info(productErightsIds);
        		registrationCount = reportDao.getRegistrationsByDateAndDivisionAndAccountCount(fromDT, toDT, divisionType, productErightsIds);
        	}
            final List<String[]> data = new ArrayList<String[]>();
            data.add(headers);
            
	        if (registrationCount != 0) {
	        	LOG.info(registrationCount + " registrations to process...");
	        	
	        	final int BATCH_SIZE = 1000;
	        	final int numberOfBatches = (int) Math.ceil(Double.valueOf(registrationCount) / Double.valueOf(BATCH_SIZE));
	        	for(int i = 0;i<numberOfBatches;i++) {
	        		final int batch = i + 1;
	        		final List<String[]> registrations = getUserDataByOwnerAndDateRange(divisionType, fromDT, toDT, headers, BATCH_SIZE, batch, pageDefinitionExportNameMapCache, productErightsIds);
	        		data.addAll(registrations);
	        		LOG.info("Processed: " + (data.size() - 1)  + " of: " + (registrationCount) + " registrations");
	        	}
	        } 	
	        
	        // StringWriter default buffer size is 8MBytes. Giving 512Kbytes per 1000 registrations
	        int bufferSize = 8192;
	        if(registrationCount > 1000) {
	        	bufferSize = (int) Math.ceil(Double.valueOf(512) * Double.valueOf(registrationCount/1000));
	        }
            final StringWriter csvStringWriter = new StringWriter(bufferSize);
            final CSVWriter csvWriter = new CSVWriter(csvStringWriter);
            csvWriter.writeAll(data);
            final String csv = csvStringWriter.toString();
            //LOG.debug("Export Data : \n " + csv);
            csvWriter.close();

            final String host = BatchJobProperties.CMDP_HOST;
            final String username = BatchJobProperties.CMDP_USERNAME;
            final String password = SimpleCipher.decrypt(BatchJobProperties.CMDP_PASSWORD);
            final String directory = BatchJobProperties.CMDP_DIRECTORY;
            
        	FTPCriteria ftpCriteria = FTPCriteria.valueOf(getFileName(),new ByteArrayInputStream(csv.getBytes(UTF8_ENCODING)), host, username, password, directory);
        	ftpService.ftp(ftpCriteria);
        	
        } catch (Exception e) {
        	sendFailureEmail(cmdpSupportEmail);
            LOG.error(e.getMessage(), e);
            throw e ;
        }

    }
	
	
	public final List<String[]> getUserDataByOwnerAndDateRange(final String divisionType, final DateTime fromDT, final DateTime toDT, final String[] headers, final int batchSize, int firstRecord, Map<String, Map<String, Map<String, String>>> pageDefinitionExportNameMapCache, ArrayList<String> productErightsIds) throws Exception {
		final List<String[]> data = new ArrayList<String[]>();    
        final List<Object[]> registrations = reportDao.getRegistrationsByDateAndDivisionAndAccount(divisionType, fromDT, toDT, batchSize, firstRecord, productErightsIds);
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
        if (!(registrations == null || registrations.isEmpty())) {
            Map<String, String> values = null;
            String[] customerData = null;
            for (Object[] registration : registrations) {
            	registration[productIdIndex] = oupProductMap.get(registration[productIdIndex].toString());
            	registration[customerIdIndex] = oupUserMap.get(registration[customerIdIndex].toString());
                values = getRegistrationData(registration, pageDefinitionExportNameMapCache);
                customerData = new String[headers.length];
                for (int i = 0; i < headers.length; i++) {
                    customerData[i] = StringUtils.defaultString(values.get(headers[i])).trim();
                }
                data.add(customerData);
            }
        } 
        return data;
    }
    
	
	private void sendFailureEmail(final String cmdpSupportEmail) {
		try {
			MailCriteria mailCriteria = new MailCriteria();
			mailCriteria.addToAddress(new InternetAddress(BatchJobProperties.EAC_ADMIN_EMAIL, "EAC CMDP Export"));
			if(StringUtils.isNotBlank(cmdpSupportEmail)) {
				mailCriteria.addCcAddress(new InternetAddress(cmdpSupportEmail, "CMDP Support"));
			}
			mailCriteria.setFrom(EmailService.getInternetAddressFrom());
			mailCriteria.setSubject("EAC CMDP Export failure");
			mailCriteria.setText("An error has occurred while sending the CMDP report.\r\n\r\nRegards\r\n\r\nEAC Support");
			emailService.sendMail(mailCriteria);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
	}
	
	private String getFileName() {
		StringBuilder fileName = new StringBuilder(FILE_NAME_PREFIX);
		fileName.append(new DateTime().toString(FILE_NAME_DATE_FORMAT));
		fileName.append(FILE_NAME_POSTFIX);
		return fileName.toString();
	}

	
    
	
    /**
     * Get header columns.
     * 
     * @return column names.
     */
    private String[] getHeaders(String divisionType) {
        final List<String> headers = new ArrayList<String>();

        headers.add(PRODUCT_ID_COL);
        headers.add(PRODUCT_NAME_COL);
        headers.add(USER_ID_COL);
        headers.add(USERNAME_COL);        
        headers.add(FIRSTNAME_COL);
        headers.add(LASTNAME_COL);
        headers.add(EMAIL_COL);
        headers.add(EMAIL_VERIFICATION_COL);
        headers.add(LAST_LOGIN_DATE_COL);
        headers.add(CREATED_DATE_COL);
        headers.add(REGISTRATION_DATE_COL);
        headers.add(UPDATED_DATE_COL);
        headers.add(STATUS_COL);
        headers.add(ADDRESS1_COL);
        headers.add(ADDRESS2_COL);
        headers.add(ADDRESS3_COL);
        headers.add(CITY_COL);
        headers.add(POSTCODE_COL);
        headers.add(INSTITUION_COL);
        headers.add(COUNTRY_COL);
        headers.add(JOBTYPE_COL);
        headers.add(JOBTYPE_OTHER_COL);
        headers.add(EMAILFLAG_COL);
        headers.add(INTERESTS_COL);
        headers.add(REFERRED_BY_COL);
        headers.add(TANDC_COL);
        //headers.add(RECEIVED_DATE_COL);
        headers.add(CAMPUS_COL);
        headers.add(DEPARTMENT_COL);
        headers.add(INSTITUTION_TYPE_COL);
        headers.add(PUBLIC_OR_PRIVATE_INSTITUTION_COL);
        headers.add(NUMBER_OF_STUDENTS_COL);
        headers.add(TEACHING_STATUS_COL);
        headers.add(TEACHING_INTERESTS_COL);
        headers.add(EXAM_INTERESTS_COL);
        headers.add(CLUBS_COL);
        headers.add(PREFERRED_LANGUAGE_COL);
        headers.add(INSTITUTION_TYPE_KR_COL);
        headers.add(MOBILE_COL);
        headers.add(TAIWAN_EXAM_INTERESTS_COL);
        
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
    private Map<String, String> getRegistrationData(final Object[] registration, Map<String, Map<String, Map<String, String>>> pageDefinitionExportNameMapCache) throws SQLException {
        final Map<String, String> data = new HashMap<String, String>();

        // static customer data
//        final Customer customer = registration.getCustomer();
//        RegisterableProduct product = (RegisterableProduct)registration.getRegistrationDefinition().getProduct();        
        
        
        data.put(PRODUCT_ID_COL, registration[productIdIndex] != null ? registration[productIdIndex].toString() : null);
        data.put(PRODUCT_NAME_COL, (registration[productNameIndex] != null) ? registration[productNameIndex].toString() : null);
        data.put(USER_ID_COL, registration[customerIdIndex] != null ? registration[customerIdIndex].toString() : null);
        data.put(USERNAME_COL, registration[customerUserNameIndex] != null ? registration[customerUserNameIndex].toString() : null);
        data.put(FIRSTNAME_COL, registration[customerFirstNameIndex] != null ? registration[customerFirstNameIndex].toString() : null);
        data.put(LASTNAME_COL, registration[customerLastNameIndex] != null ? registration[customerLastNameIndex].toString() : null);
        data.put(EMAIL_COL, registration[validatorEmailIndex] != null ? registration[validatorEmailIndex].toString() : null);
        data.put(EMAIL_VERIFICATION_COL, registration[emailVerificationIndex] != null ? registration[emailVerificationIndex].toString() : null);        
        data.put(LAST_LOGIN_DATE_COL, registration[customertLastLoginIndex] != null ? sdf.format(registration[customertLastLoginIndex]) : null);
        data.put(CREATED_DATE_COL, registration[customerCreateddateIndex] != null ? sdf.format(registration[customerCreateddateIndex]) : null); 
        data.put(REGISTRATION_DATE_COL, (registration[regCreatedDateIndex] != null) ? sdf.format(registration[regCreatedDateIndex]) : null);  
        data.put(UPDATED_DATE_COL, (registration[updatedDateIndex] != null) ? sdf.format(registration[updatedDateIndex]) : null);  
//        data.put(STATUS_COL, registration[statusIndex] != null ? registration[statusIndex].toString() : null);
        data.put(STATUS_COL, "Activated");
        data.put(PREFERRED_LANGUAGE_COL, registration[localeIndex] != null ? registration[localeIndex].toString() : null);
        //data.put(RECEIVED_DATE_COL, registration.getCreatedDate().toString(EXPORT_DATE_FORMAT));

        
        //PageDefinition pageDefinition = registration.getRegistrationDefinition().getPageDefinition();        
        
        //Map<String, Map<String, String>> exportLabelMap = getTextMap(pageDefinition.getId(), pageDefinitionExportNameMapCache);
        
        // dynamic data
        final List<Object[]> answers = reportDao.getAnswerDetailsByCustomerAndProduct(data.get(USER_ID_COL), data.get(PRODUCT_ID_COL), true);
        if (answers != null && answers.size() > 0) {
        	Map<String, Map<String, String>> exportLabelMap = getTextMap(answers.get(0)[ansPgDefIdIndex].toString(), pageDefinitionExportNameMapCache);
            for (Object[] answer : answers) {
            		processAnswer(data, exportLabelMap, answer[ansTextIndex].toString(), answer[ansExportNameIndex].toString());
            }
        }
        
        /*if ( registration[ customerIdIndex ] != null ) {
        	customer.setId(registration[ customerIdIndex ].toString() );
        }
        if(  registration[productIdIndex] != null && registration[customerIdIndex] != null ) {
        	PageDefinition pageDefinition = null ;
        	ProductRegistrationDefinition pageRegDefinition = null ;
			try {
				product = productService.getProductById(registration[ productIdIndex ].toString()) ;
				if (product != null) {
					pageRegDefinition = registrationDefinitionService.getProductRegistrationDefinitionByProduct(product);
				}
			} catch (ServiceLayerException e) {
				LOG.error(e.getMessage(), e);
			}
        	if (pageRegDefinition != null ) {
        		pageDefinition = pageRegDefinition.getPageDefinition() ;
        	}
        	if (pageDefinition != null ) {
	            final List<Question> questions = pageDefinitionService.getPageDefinitionQuestions(pageDefinition);
	            if(questions != null && questions.size() > 0) {
	    	        final List<Answer> answers = answerDao.getCustomerAnswerByQuestions(questions, customer);
	    	        if (answers != null && answers.size() > 0) {
	    	            LOG.info("Got " + answers.size() + " answers");
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
        }*/
        LOG.debug("Customer registration " + data.toString());
        return data;
    }
   
	private void processAnswer(final Map<String, String> data, Map<String, Map<String, String>> exportLabelMap, String answer, String exportName) {
		//String exportName = answer.getQuestion().getExportName(ExportType.CMDP);
		Map<String, String> valuesMap = exportLabelMap.get(exportName);
		if(valuesMap == null) {
			data.put(translateExportName(exportName), translate(answer));
		} else {
			data.put(translateExportName(exportName), fixLabels(valuesMap, answer, exportName));
		}
	}
    
    private String translate(String optInoptOut) {
    	if(StringUtils.isBlank(optInoptOut)) {
    		return optInoptOut;
    	}
    	if(optInoptOut.equals("opt in")) {
    		return "Y";
    	} else if(optInoptOut.equals("opt out")) {
    		return "N";
    	} else {
    		return optInoptOut;
    	}
    }
    
    private String fixLabels(Map<String, String> valuesMap, String answers, String exportName) {
    	if(StringUtils.isBlank(answers)) {
    		return answers;
    	}
    	if(exportName.equals(COUNTRY_COL)) {
    		return answers.toUpperCase();
    	}
    	StringBuilder answersBuilder = new StringBuilder();
    	String[] parts = answers.trim().split(",");
    	for(String part : parts) {
    		if(answersBuilder.length() > 0) {
    			answersBuilder.append(", ");
    		}
    		answersBuilder.append(valuesMap.get(part));
    	}
    	return answersBuilder.toString();
    }
    
    private Map<String, Map<String, String>> getTextMap(final String pageDefinitionId, Map<String, Map<String, Map<String, String>>> pageDefinitionExportNameMapCache) throws SQLException {
    	Map<String, Map<String, String>> map = pageDefinitionExportNameMapCache.get(pageDefinitionId);
    	if(map == null) {
    		map = reportDao.getTextMap(pageDefinitionId,this.messageSource);
    		pageDefinitionExportNameMapCache.put(pageDefinitionId, map);
    	}
    	return map;
    }
    
    private String translateExportName(String exportName) {
    	if(exportName.equals("marketing.pref")) {
    		return EMAILFLAG_COL;
    	}
    	return exportName;
    }

    public static void main(String[] args){
    	CMDPDataExportJob job = new CMDPDataExportJob();
    	DateTime now = DateTime.parse("2018-09-24T12:28:00.412+05:30");
    	DateTime then = DateTime.parse("2018-09-24T12:28:59.412+05:30");
    	try {
			job.ftpCMDPData(now, then);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
