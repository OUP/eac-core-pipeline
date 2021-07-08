package com.oup.eac.service.impl;

import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.mail.internet.InternetAddress;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import au.com.bytecode.opencsv.CSVWriter;

import com.oup.eac.cloudSearch.util.OupIdMappingUtil;
import com.oup.eac.common.utils.EACSettings;
import com.oup.eac.common.utils.MessageTextSource;
import com.oup.eac.common.utils.email.EmailUtils;
import com.oup.eac.data.AnswerDao;
import com.oup.eac.data.PageDefinitionDao;
import com.oup.eac.data.RegistrationDao;
import com.oup.eac.domain.Answer;
import com.oup.eac.domain.Answer.AnswerType;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.ExportType;
import com.oup.eac.domain.FTPCriteria;
import com.oup.eac.domain.Field;
import com.oup.eac.domain.MailCriteria;
import com.oup.eac.domain.OptionsTag;
import com.oup.eac.domain.PageComponent;
import com.oup.eac.domain.PageDefinition;
import com.oup.eac.domain.Product;
import com.oup.eac.domain.ProductPageDefinition;
import com.oup.eac.domain.ProductRegistrationDefinition;
import com.oup.eac.domain.ProductSpecificAnswer;
import com.oup.eac.domain.Question;
import com.oup.eac.domain.RegisterableProduct;
import com.oup.eac.domain.Registration;
import com.oup.eac.domain.Tag;
import com.oup.eac.domain.Tag.TagType;
import com.oup.eac.domain.TagOption;
import com.oup.eac.domain.utils.audit.AuditLogger;
import com.oup.eac.integration.facade.ErightsFacade;
import com.oup.eac.service.ELTDataExportService;
import com.oup.eac.service.EmailService;
import com.oup.eac.service.FTPService;
import com.oup.eac.service.PageDefinitionService;
import com.oup.eac.service.ProductService;
import com.oup.eac.service.RegistrationDefinitionService;
import com.oup.eac.service.ServiceLayerException;

/**
 * Data Export Service.
 * 
 */
@Service(value="eltDataExportService")
public class ELTDataExportServiceImpl implements ELTDataExportService {

    private static final Logger LOG = Logger.getLogger(ELTDataExportServiceImpl.class);
    
    private static final String UTF8_ENCODING = "UTF-8";
    
    private static final String ELT_DATA_EXPORT = "ELT Data Export";
    
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
    
    private static final int ansPgDefIdIndex = 2 ;
    private static final int ansTextIndex = 0 ;
    private static final int ansExportNameIndex = 1 ;
    
    private final FTPService ftpService;
    
    private final RegistrationDao registrationDao;
    
    private final MessageSource messageSource;
    
    private final PageDefinitionDao<ProductPageDefinition> productPageDefinitionDao;
    
    private final AnswerDao answerDao;
    
    private final EmailService emailService;
    
    @Autowired
    public ELTDataExportServiceImpl(final FTPService ftpService, final RegistrationDao registrationDao, final MessageSource messageSource, final PageDefinitionDao<ProductPageDefinition> productPageDefinitionDao, final AnswerDao answerDao,
    								final EmailService emailService) {
		super();
		Assert.notNull(ftpService);
		Assert.notNull(registrationDao);
		Assert.notNull(messageSource);
		Assert.notNull(productPageDefinitionDao);
		Assert.notNull(answerDao);
		Assert.notNull(emailService);
		this.ftpService = ftpService;
		this.registrationDao = registrationDao;
		this.messageSource = messageSource;
		this.productPageDefinitionDao = productPageDefinitionDao;
		this.answerDao = answerDao;
		this.emailService = emailService;
	}
    
	public final void ftpCMDPData(final String divisionType, final DateTime fromDT, final DateTime toDT, final String cmdpSupportEmail) throws ServiceLayerException {
		
        try {
        	final Map<String, Map<String, Map<String, String>>> pageDefinitionExportNameMapCache = new HashMap<String, Map<String,Map<String,String>>>();
        	
            // get data
        	final String[] headers = getHeaders(divisionType);
        	String ER_DB_NAME_WITH_SCHEMA = EACSettings.getProperty("rs.db.name") + "." + EACSettings.getProperty("rs.db.schema")  ;
        	long registrationCount = registrationDao.getRegistrationsByDateAndDivisionAndAccountCount(fromDT, toDT, divisionType, ER_DB_NAME_WITH_SCHEMA);
            
            
            final List<String[]> data = new ArrayList<String[]>();
            data.add(headers);
            
	        if (registrationCount != 0) {
	        	LOG.info(registrationCount + " registrations to process...");
	        	
	        	final int BATCH_SIZE = 1000;
	        	final int numberOfBatches = (int) Math.ceil(Double.valueOf(registrationCount) / Double.valueOf(BATCH_SIZE));
	        	for(int i = 0;i<numberOfBatches;i++) {
	        		final int batch = i + 1;
	        		final List<String[]> registrations = getUserDataByOwnerAndDateRange(divisionType, fromDT, toDT, headers, BATCH_SIZE, batch, pageDefinitionExportNameMapCache);
	        		data.addAll(registrations);
	        		LOG.info("Processed: " + (data.size() - 1)  + " of: " + (registrationCount) + " registrations");
	        	}
                AuditLogger.logSystemEvent(ELT_DATA_EXPORT, registrationCount + " registrations", divisionType.toString());
	        } else {
	            AuditLogger.logSystemEvent(ELT_DATA_EXPORT, "0 registrations", divisionType.toString());
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

            final String host = EACSettings.getProperty("elt.cmdp.host");
            final String username = EACSettings.getProperty("elt.cmdp.username");
            final String password = EACSettings.getDecryptedProperty("elt.cmdp.password");
            final String directory = EACSettings.getProperty("elt.cmdp.directory");
            
        	FTPCriteria ftpCriteria = FTPCriteria.valueOf(getFileName(), IOUtils.toInputStream(csv, UTF8_ENCODING), host, username, password, directory);
        	ftpService.ftp(ftpCriteria);
        	
        } catch (Exception e) {
        	sendFailureEmail(cmdpSupportEmail);
            throw new ServiceLayerException(e.getMessage(), e);
        }

    }
	
	private void sendFailureEmail(final String cmdpSupportEmail) {
		try {
			MailCriteria mailCriteria = new MailCriteria();
			mailCriteria.addToAddress(new InternetAddress(EACSettings.getProperty(EACSettings.EAC_ADMIN_EMAIL), "EAC CMDP Export"));
			if(StringUtils.isNotBlank(cmdpSupportEmail)) {
				mailCriteria.addCcAddress(new InternetAddress(cmdpSupportEmail, "CMDP Support"));
			}
			mailCriteria.setFrom(EmailUtils.getInternetAddressFrom());
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

	public final List<String[]> getUserDataByOwnerAndDateRange(final String divisionType, final DateTime fromDT, final DateTime toDT, final String[] headers, final int batchSize, int firstRecord, Map<String, Map<String, Map<String, String>>> pageDefinitionExportNameMapCache) {
		final List<String[]> data = new ArrayList<String[]>();    
    	String ER_DB_NAME_WITH_SCHEMA = EACSettings.getProperty("rs.db.name") + "." + EACSettings.getProperty("rs.db.schema")  ;
        final List<Object[]> registrations = registrationDao.getRegistrationsByDateAndDivisionAndAccount(fromDT, toDT, divisionType, batchSize, firstRecord, ER_DB_NAME_WITH_SCHEMA);
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
     */
    private Map<String, String> getRegistrationData(final Object[] registration, Map<String, Map<String, Map<String, String>>> pageDefinitionExportNameMapCache) {
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
        data.put(LAST_LOGIN_DATE_COL, registration[customertLastLoginIndex] != null ? registration[customertLastLoginIndex].toString() : null);
        data.put(CREATED_DATE_COL, registration[customerCreateddateIndex] != null ? sdf.format(registration[customerCreateddateIndex]) : null); 
        data.put(REGISTRATION_DATE_COL, (registration[regCreatedDateIndex] != null) ? sdf.format(registration[regCreatedDateIndex]) : null);  
        data.put(UPDATED_DATE_COL, (registration[customerCreateddateIndex] != null) ? sdf.format(registration[customerCreateddateIndex]) : null);  
//        data.put(STATUS_COL, registration[statusIndex] != null ? registration[statusIndex].toString() : null);
        data.put(STATUS_COL, "Activated");
        data.put(PREFERRED_LANGUAGE_COL, registration[localeIndex] != null ? registration[localeIndex].toString() : null);
        //data.put(RECEIVED_DATE_COL, registration.getCreatedDate().toString(EXPORT_DATE_FORMAT));

        
        //PageDefinition pageDefinition = registration.getRegistrationDefinition().getPageDefinition();        
        
        //Map<String, Map<String, String>> exportLabelMap = getTextMap(pageDefinition.getId(), pageDefinitionExportNameMapCache);
        
        // dynamic data
        final List<Object[]> answers = answerDao.getAnswerDetailsByCustomerAndProduct(data.get(USER_ID_COL), data.get(PRODUCT_ID_COL), true);
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
    
    private String getName(String name) {
        StringBuilder stringBuilder = new StringBuilder(name.toLowerCase());
        stringBuilder.setCharAt(0, Character.toUpperCase(stringBuilder.charAt(0)));
        return stringBuilder.toString();
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
    
    private Map<String, Map<String, String>> getTextMap(final String pageDefinitionId, Map<String, Map<String, Map<String, String>>> pageDefinitionExportNameMapCache) {
    	Map<String, Map<String, String>> map = pageDefinitionExportNameMapCache.get(pageDefinitionId);
    	if(map == null) {
    		map = getTextMap(pageDefinitionId);
    		pageDefinitionExportNameMapCache.put(pageDefinitionId, map);
    	}
    	return map;
    }
    
    
    private Map<String, Map<String, String>> getTextMap(final String pageDefinitionId) {
    	Map<String, Map<String, String>> map = new HashMap<String, Map<String,String>>();
    	MessageTextSource resource = getResource(Locale.getDefault());
    	ProductPageDefinition productPageDefinition = productPageDefinitionDao.getPageDefinitionById(pageDefinitionId);
    	for(PageComponent pageComponent : productPageDefinition.getPageComponents()) {
    		for(Field field : pageComponent.getComponent().getFields()) {
    			Question question = field.getElement().getQuestion();
    			Tag tag = field.getElement().getTag();
    			if(tag.getTagType() == TagType.SELECT || tag.getTagType() == TagType.MULTISELECT) {
    				Map<String, String> labelValuesMap = new HashMap<String, String>();
    				OptionsTag optionsTag = (OptionsTag)tag;
    				for(TagOption tagOption : optionsTag.getOptions()) {
    					labelValuesMap.put(tagOption.getValue(), resource.getString(tagOption.getLabel()));
    				}
    				map.put(question.getExportName(ExportType.CMDP), labelValuesMap);
    			}
    		}
    	}
    	return map;
    }
    
    private String translateExportName(String exportName) {
    	if(exportName.equals("marketing.pref")) {
    		return EMAILFLAG_COL;
    	}
    	return exportName;
    }
    
    private MessageTextSource getResource(final Locale locale) {
        MessageTextSource result = new MessageTextSource(this.messageSource, locale);
        return result;
    }
}
