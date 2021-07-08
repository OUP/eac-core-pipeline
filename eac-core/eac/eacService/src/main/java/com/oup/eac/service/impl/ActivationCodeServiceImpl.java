package com.oup.eac.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.mail.internet.InternetAddress;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.hibernate.Hibernate;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cloudsearchdomain.AmazonCloudSearchDomainClient;
import com.amazonaws.services.cloudsearchdomain.model.Hit;
import com.amazonaws.services.cloudsearchdomain.model.Hits;
import com.constants.SearchDomainFields;
import com.oup.eac.cloudSearch.search.impl.AmazonCloudSearchServiceImpl;
import com.oup.eac.cloudSearch.util.CloudSearchUtils;
import com.oup.eac.cloudSearch.util.CloudSearchUtils.DomainType;
import com.oup.eac.common.utils.MessageTextSource;
import com.oup.eac.common.utils.activationcode.ActivationCodeGenerator;
import com.oup.eac.common.utils.activationcode.EacNumericActivationCode;
import com.oup.eac.common.utils.email.EmailUtils;
import com.oup.eac.common.utils.email.VelocityUtils;
import com.oup.eac.common.utils.exception.ExceptionUtil;
import com.oup.eac.common.utils.lang.EACStringUtils;
import com.oup.eac.data.ActivationCodeBatchDao;
import com.oup.eac.data.ActivationCodeDao;
import com.oup.eac.data.RegistrationDefinitionDao;
import com.oup.eac.domain.ActivationCode;
import com.oup.eac.domain.ActivationCodeBatch;
import com.oup.eac.domain.ActivationCodeBatch.ActivationCodeFormat;
import com.oup.eac.domain.ActivationCodeRegistration;
import com.oup.eac.domain.ActivationCodeRegistrationDefinition;
import com.oup.eac.domain.ActivationCodeSearchDto;
import com.oup.eac.domain.AdminUser;
import com.oup.eac.domain.ConcurrentLicenceTemplate;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.EacGroups;
import com.oup.eac.domain.ExternalProductId;
import com.oup.eac.domain.LicenceTemplate;
import com.oup.eac.domain.MailCriteria;
import com.oup.eac.domain.Product;
import com.oup.eac.domain.Product.ProductState;
import com.oup.eac.domain.ProductPageDefinition;
import com.oup.eac.domain.ProductRegistrationDefinition;
import com.oup.eac.domain.RegisterableProduct;
import com.oup.eac.domain.RollingLicenceTemplate;
import com.oup.eac.domain.UsageLicenceTemplate;
import com.oup.eac.domain.paging.Paging;
import com.oup.eac.domain.paging.PagingCriteria;
import com.oup.eac.domain.search.AmazonSearchFields;
import com.oup.eac.domain.search.AmazonSearchRequest;
import com.oup.eac.domain.search.AmazonSearchResponse;
import com.oup.eac.domain.utils.audit.AuditLogger;
import com.oup.eac.dto.ActivationCodeBatchDto;
import com.oup.eac.dto.ActivationCodeBatchSearchCriteria;
import com.oup.eac.dto.ActivationCodeReportDto;
import com.oup.eac.dto.ActivationCodeSearchCriteria;
import com.oup.eac.dto.EnforceableProductDto;
import com.oup.eac.dto.GuestRedeemActivationCodeDto;
import com.oup.eac.dto.LicenceDetailDto;
import com.oup.eac.dto.Message;
import com.oup.eac.dto.ProductGroupDto;
import com.oup.eac.dto.RollingLicenceDetailDto;
import com.oup.eac.dto.StandardConcurrentLicenceDetailDto;
import com.oup.eac.dto.UsageLicenceDetailDto;
import com.oup.eac.integration.erights.AddedInPool;
import com.oup.eac.integration.erights.GetActivationCodeBatchByBatchIdResponse;
import com.oup.eac.integration.facade.ErightsFacade;
import com.oup.eac.integration.facade.exceptions.ErightsException;
import com.oup.eac.integration.facade.exceptions.GroupNotFoundException;
import com.oup.eac.integration.facade.exceptions.LicenseNotFoundException;
import com.oup.eac.integration.facade.exceptions.ProductNotFoundException;
import com.oup.eac.integration.facade.exceptions.UserNotFoundException;
import com.oup.eac.service.ActivationCodeService;
import com.oup.eac.service.EacGroupService;
import com.oup.eac.service.EmailService;
import com.oup.eac.service.ProductService;
import com.oup.eac.service.ServiceLayerException;
import com.oup.eac.service.exceptions.ActivationCodeServiceLayerException;
import com.oup.eac.service.exceptions.ServiceLayerValidationException;
import com.oup.eac.service.util.ConvertSearch;

@Service(value="activationCodeService")
public class ActivationCodeServiceImpl implements ActivationCodeService {
	
	private static final Logger LOGGER = Logger.getLogger(ActivationCodeServiceImpl.class);
   
	private final ActivationCodeBatchDao activationCodeBatchDao;
	
	private final ActivationCodeDao activationCodeDao;
	
	private final RegistrationDefinitionDao registrationDefinitionDao;
	
	private final ErightsFacade erightsFacade;
	
	private final ProductService productService;
	
	private final EacGroupService eacGrpService;
	
	private final MessageSource messageSource;
	
	private final VelocityEngine velocityEngine;
	
	private final EmailService emailService;
	
	private static String AWS_ACCESSKEY;

	private static String AWS_SECRETKEY;
	
	private static String CODE_FORMAT = ActivationCodeFormat.EAC_NUMERIC.toString();
	
	private static final String EMAIL_TEMPLATE = "com/oup/eac/service/velocity/activationCodeBatch.vm";

	private static final String EXPORT_EMAIL_SUBJECT = "Activation Codes for ";
	
	private static final String UTF8_ENCODING = "UTF-8";
	
	private static String ADMIN_NAME = null ;

	static {
		ResourceBundle resourceBundle = ResourceBundle.getBundle("acesCloudSearch");
		//erightsUsername = resourceBundle.getString("erights.conn.username");
		//erightsPassword = resourceBundle.getString("erights.conn.password");
		AWS_ACCESSKEY = resourceBundle.getString("aws.searchdoc.accesskey");
		AWS_SECRETKEY = resourceBundle.getString("aws.searchdoc.secretkey");
	}
	
	@Autowired
	public ActivationCodeServiceImpl(final ActivationCodeBatchDao activationCodeBatchDao, final ActivationCodeDao activationCodeDao, 
			final RegistrationDefinitionDao registrationDefinitionDao, final ErightsFacade erightsFacade,
			final ProductService productService, final EacGroupService eacGroupService,
			final MessageSource messageSource, final VelocityEngine velocityEngine, final EmailService emailService) {
		super();
		Assert.notNull(activationCodeBatchDao);
		Assert.notNull(activationCodeDao);
		Assert.notNull(registrationDefinitionDao);
		Assert.notNull(erightsFacade);
		Assert.notNull(productService);
		Assert.notNull(eacGroupService);
		Assert.notNull(messageSource);
		Assert.notNull(velocityEngine);
		Assert.notNull(emailService);
		this.activationCodeBatchDao = activationCodeBatchDao;
		this.activationCodeDao = activationCodeDao;
		this.registrationDefinitionDao = registrationDefinitionDao;
		this.erightsFacade = erightsFacade;
		this.productService = productService;
		this.eacGrpService = eacGroupService;
		this.messageSource = messageSource ;
		this.velocityEngine = velocityEngine ;
		this.emailService = emailService ;
	}
	
    @PreAuthorize("hasRole('ROLE_LIST_ACTIVATION_CODE') or T(com.oup.eac.domain.utils.audit.EacApp).isWebServices()")
    public final ActivationCode getActivationCodeByCode(final String code) throws ServiceLayerException {
    	ActivationCodeBatchDto activationCodeBatchDto = new ActivationCodeBatchDto();
    	try {
			activationCodeBatchDto = erightsFacade.getActivationCodeBatchByActivationCode(code);
		} catch (AccessDeniedException | ErightsException e) {
			throw new ServiceLayerException(e.getMessage());
		}
    	
    	ActivationCode activationCodes = new ActivationCode();
    	ActivationCodeBatch activationCodeBatch = new ActivationCodeBatch();
    	
    	List<ActivationCode> activationCodeList = new ArrayList<ActivationCode>();
    	List<String> codes = activationCodeBatchDto.getActivationCodes();
    	for (String singleCode : codes) {
    		ActivationCode activationCode = new ActivationCode();
    		activationCode.setCode(singleCode);
    		activationCodeList.add(activationCode);
    	}
    	
    	LicenceDetailDto licenceDetailDto = activationCodeBatchDto.getLicenceDetailDto();
    	LicenceTemplate lt = getLicenseTemplate(licenceDetailDto);    	
    	activationCodeBatch.setLicenceTemplate(lt); 
    	
    	ActivationCodeRegistrationDefinition activationCodeRegistrationDefinition = new ActivationCodeRegistrationDefinition();
    	activationCodeRegistrationDefinition.setLicenceTemplate(lt);
    	  
    	if(activationCodeBatchDto.getProductId()!=null){
    		EnforceableProductDto enfoProduct = new EnforceableProductDto();
			try {
				enfoProduct = erightsFacade.getProduct(activationCodeBatchDto.getProductId());
			} catch (ErightsException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		Product product = convertEnforceableProductToProduct(enfoProduct);
    		activationCodeRegistrationDefinition.setProduct(product);
    	}
    	
    	if (activationCodeBatchDto.getCodeFormat() == CODE_FORMAT) {
    		activationCodeBatch.setActivationCodeFormat(ActivationCodeFormat.EAC_NUMERIC);	
    	}   	   	
    	activationCodeBatch.setActivationCodes(activationCodeList);
    	activationCodeBatch.setBatchId(activationCodeBatchDto.getBatchId());  	     	    	
    	activationCodeBatch.setActivationCodeRegistrationDefinition(activationCodeRegistrationDefinition);
    	activationCodes.setActivationCodeBatch(activationCodeBatch);
    	activationCodes.setAllowedUsage(activationCodeBatchDto.getAllowedUsages());
    	activationCodes.setCode(code);
    	activationCodes.setId(activationCodeBatchDto.getBatchId());
    	//activationCodes.setRegistrations(registrations);
    	return activationCodes;
    	
    	//De-duplication ACB
        //return activationCodeDao.getActivationCodeByCode(activationCode.getCode());       	
        //return activationCodeDao.getActivationCodeByCode(code);
    }    
    
    public final ActivationCodeBatchDto getActivationCodeByCode(ActivationCode activationCode)  {
    	ActivationCodeBatchDto activationCodeBatchDto = new ActivationCodeBatchDto();
    	try {
			activationCodeBatchDto = erightsFacade.getActivationCodeBatchByActivationCode(activationCode.getCode());
		} catch (AccessDeniedException | ErightsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	/*ActivationCode activationCodes = new ActivationCode();
    	ActivationCodeBatch activationCodeBatch = new ActivationCodeBatch();
    	
    	List<ActivationCode> activationCodeList = new ArrayList<ActivationCode>();
    	for(String code: activationCodeBatchDto.getActivationCodes()){
    		ActivationCode actCode = new ActivationCode();
    		actCode.setCode(code);
    		activationCodeList.add(actCode);
    	}
    	//activationCodeList.add((ActivationCode) activationCodeBatchDto.getActivationCodes());
    	
    	LicenceDetailDto licenceDetailDto = activationCodeBatchDto.getLicenceDetailDto(); 
    	LicenceTemplate licenceTemplate = getLicenseTemplate(licenceDetailDto);
    	activationCodeBatch.setLicenceTemplate(licenceTemplate); 
    	
    	ActivationCodeRegistrationDefinition activationCodeRegistrationDefinition = new ActivationCodeRegistrationDefinition();
    	activationCodeRegistrationDefinition.setLicenceTemplate(licenceTemplate);
    	
    	if (activationCodeBatchDto.getCodeFormat() == CODE_FORMAT) {
    		activationCodeBatch.setActivationCodeFormat(ActivationCodeFormat.EAC_NUMERIC);	
    	}    	   	
    	activationCodeBatch.setActivationCodes(activationCodeList);
    	activationCodeBatch.setBatchId(activationCodeBatchDto.getBatchId());  	     	    	
    	activationCodeBatch.setActivationCodeRegistrationDefinition(activationCodeRegistrationDefinition);
    	activationCodes.setActivationCodeBatch(activationCodeBatch);
    	activationCodes.setAllowedUsage(activationCodeBatchDto.getAllowedUsages());
    	activationCodes.setCode(activationCode.getCode());
    	activationCodes.setId(activationCodeBatchDto.getBatchId());*/
    	//activationCodes.setRegistrations(registrations);
    	return activationCodeBatchDto;
    	
    	//De-duplication ACB
        //return activationCodeDao.getActivationCodeByCode(activationCode.getCode());
    }
    
    public final ActivationCode getActivationCodeAndDefinitionByCode(final String code) {
    	return activationCodeDao.getActivationCodeAndDefinitionByCode(code);
    }
    
    @Override
	public ActivationCode getActivationCodeAndDefinitionByCodeForEacGroup(String code) {
    	return activationCodeDao.getActivationCodeAndDefinitionByCodeForEacGroup(code);
	}
    
    @Override
    public final List<ActivationCodeRegistration> getRedeemActivationCodeInfo(ActivationCodeSearchDto activationCodeSearchDto, String systemId){
    	
    	List<ActivationCodeRegistration> registrations = new ArrayList<>();
		
		AmazonSearchRequest request = new AmazonSearchRequest();
    	request.setResultsPerPage(1000);
    	
    	List<AmazonSearchFields> searchFieldsList = new ArrayList<AmazonSearchFields>();    	
    	
    	if(activationCodeSearchDto != null && activationCodeSearchDto.getCode()!=null){
    		AmazonSearchFields searchField = new AmazonSearchFields();
    		searchField.setName(SearchDomainFields.ACTIVATION_CODE);
    		searchField.setValue(activationCodeSearchDto.getCode());
    		searchField.setType("String");
    		searchFieldsList.add(searchField);
    	}
    	
    	List<String> searchResultFieldsList = new ArrayList<String>();
		searchResultFieldsList.add(SearchDomainFields.LICENSE_CREATED_DATE);
		searchResultFieldsList.add(SearchDomainFields.LICENSE_USER_ID);
		
		HashMap<String, String> hMap1 = new HashMap<String, String>();
		hMap1.put(SearchDomainFields.LICENSE_USER_ID, "asc");
		request.setSortFields(hMap1);
		request.setSearchResultFieldsList(searchResultFieldsList);
    	request.setSearchFieldsList(searchFieldsList);
    	
    	AmazonCloudSearchServiceImpl cloudSearch = new AmazonCloudSearchServiceImpl();
    	AmazonSearchResponse response = cloudSearch.searchAllLicenses(request);
    	
    	ConvertSearch convert = new ConvertSearch();
    	registrations = convert.convertLicense(response, systemId);
    	
    	return registrations;
    	
    }
    
    @Override
    public final List<ActivationCodeSearchDto> searchActivationCodeByCode(final String systemId, final String code, final boolean likeSearch) {
    	
    	AuditLogger.logEvent("Search ActivationCodeBatchByCode", "code:"+code);
    	//cloudsearch
    	List<ActivationCodeSearchDto> activationCodes = new ArrayList<>();
    	AmazonSearchRequest request = new AmazonSearchRequest();
    	request.setResultsPerPage(1000);
    	
    	List<AmazonSearchFields> searchFieldsList = new ArrayList<AmazonSearchFields>();    	
    	
    	if(code != null){
    		AmazonSearchFields searchField = new AmazonSearchFields();
    		searchField.setName(SearchDomainFields.CLAIM_TICKET_ACTIVATIONCODE);
    		searchField.setValue(code);
    		searchField.setType("String");
    		searchFieldsList.add(searchField);
    	}
    	
    	List<String> searchResultFieldsList = new ArrayList<String>();
		searchResultFieldsList.add(SearchDomainFields.CLAIM_TICKET_ACTIVATIONCODE);
		searchResultFieldsList.add(SearchDomainFields.CLAIM_TICKET_PRODUCTID);
		searchResultFieldsList.add(SearchDomainFields.CLAIM_TICKET_PRODUCTGROUPID);
		searchResultFieldsList.add(SearchDomainFields.CLAIM_TICKET_ALLOWEDUSAGES);
		searchResultFieldsList.add(SearchDomainFields.CLAIM_TICKET_ACTUAL_USAGES);
		searchResultFieldsList.add(SearchDomainFields.CLAIM_TICKET_STARTDATE);
		searchResultFieldsList.add(SearchDomainFields.CLAIM_TICKET_ENDDATE);
		
		HashMap<String, String> hMap1 = new HashMap<String, String>();
		hMap1.put(SearchDomainFields.CLAIM_TICKET_ACTIVATIONCODE, "asc");
		request.setSortFields(hMap1);
		request.setSearchResultFieldsList(searchResultFieldsList);
    	request.setSearchFieldsList(searchFieldsList);
    	
    	AmazonCloudSearchServiceImpl cloudSearch = new AmazonCloudSearchServiceImpl();
    	AmazonSearchResponse response = cloudSearch.searchActivationCode(request, likeSearch);
    	
    	ConvertSearch convert = new ConvertSearch();
    	activationCodes = convert.convertActivationCodeSearch(response, systemId);
    	
		return activationCodes;
//    	return activationCodeDao.searchActivationCodeByCode(code, likeSearch);
    }
	
    @Override
	public final GetActivationCodeBatchByBatchIdResponse checkActivationCodeBatchExistsByBatchId(final String batchId) 
			throws ProductNotFoundException, UserNotFoundException, LicenseNotFoundException, 
			AccessDeniedException, GroupNotFoundException, ErightsException, ServiceLayerException {				
		GetActivationCodeBatchByBatchIdResponse response = erightsFacade.checkActivationCodeBatchExists(batchId);
		return response;
    }
    
	@Override
	public final ActivationCodeBatch getActivationCodeBatchByBatchId(final String batchId, final boolean activationCodeRequired) 
			throws ProductNotFoundException, UserNotFoundException, LicenseNotFoundException, 
			AccessDeniedException, GroupNotFoundException, ErightsException, ServiceLayerException {		
				
		ActivationCodeBatch activationCodeBatch = new ActivationCodeBatch();
		ActivationCodeBatchDto activationCodeBatchDto = erightsFacade.getActivationCodeBatch(batchId,activationCodeRequired);
		
		DateTime startDate = null;
		DateTime endDate = null;
		DateTime createdDate = null;
		DateTime updatedDate = null;		
		
		if (activationCodeBatchDto.getValidFrom() != null) {
			startDate = new DateTime(activationCodeBatchDto.getValidFrom().toString());
		}
		if (activationCodeBatchDto.getValidTo() != null) {
			endDate =  new DateTime(activationCodeBatchDto.getValidTo().toString());
		}
		if (activationCodeBatchDto.getCreatedDate() != null) {
			createdDate = new DateTime(activationCodeBatchDto.getCreatedDate().toString());
		}
		if (activationCodeBatchDto.getUpdatedDate() != null) {
			updatedDate =  new DateTime(activationCodeBatchDto.getUpdatedDate().toString());
		}	 
		
		if (activationCodeBatchDto.getCodeFormat() == CODE_FORMAT) {
    		activationCodeBatch.setActivationCodeFormat(ActivationCodeFormat.EAC_NUMERIC);	
    	}
		
		LicenceDetailDto licenceDetailDto = activationCodeBatchDto.getLicenceDetailDto();
		LicenceTemplate licenceTemplate = getLicenseTemplate(licenceDetailDto);		
		List<String> codes = activationCodeBatchDto.getActivationCodes();
    	
    	List<ActivationCode> activationCodeList = new ArrayList<ActivationCode>();
    	for (String code : codes) {
    		ActivationCode activationCode = new ActivationCode();
    		activationCode.setCode(code);
    		activationCodeList.add(activationCode);
    	}    	
    	    	
    	EnforceableProductDto product = null;
    	if (activationCodeBatchDto.getProductId() != null) {
    		product = erightsFacade.getProduct(activationCodeBatchDto.getProductId()); 
    	}
    	 
    	ProductGroupDto eacGroup = null;
    	if (activationCodeBatchDto.getProductGroupId() != null) {
    		eacGroup = erightsFacade.getProductGroup(activationCodeBatchDto.getProductGroupId(), 
        			null);    		   		
    	}   
    	
    	ActivationCodeRegistrationDefinition activationCodeRegistrationDefinition = null ;
    	if (product != null) {
    		Product eacProduct = new RegisterableProduct() ;
        	eacProduct.setId(product.getProductId());
        	activationCodeRegistrationDefinition = registrationDefinitionDao.getRegistrationDefinitionByProduct(ActivationCodeRegistrationDefinition.class, eacProduct);
    	}
    	
    	if (activationCodeRegistrationDefinition == null ){
    		activationCodeRegistrationDefinition = new ActivationCodeRegistrationDefinition() ;
    	}
    	
    	if (licenceTemplate != null) {
    		activationCodeRegistrationDefinition.setLicenceTemplate(licenceTemplate);
    		if (product != null) {
    			EnforceableProductDto enforceableProductDto = 
    					productService.getEnforceableProductByErightsId(product.getProductId());
    			Product eacProduct = new RegisterableProduct() ;
    			//productService.getProductById(enforceableProductDto.getProductId());
    			eacProduct.setId(enforceableProductDto.getProductId());
    			eacProduct.setProductName(enforceableProductDto.getName());
    			activationCodeRegistrationDefinition.setProduct(eacProduct);
    		} else if (eacGroup != null) {    			
    			EacGroups productGroups = eacGrpService.getEacGroupByName(eacGroup.getProductGroupName());    			
        		activationCodeRegistrationDefinition.setEacGroup(productGroups);
    		}
    		
    	}
    	
    	//activationCodeRegistrationDefinition.setActivationCodeBatchs(activationCodeBatchs);
    	//activationCodeRegistrationDefinition.setConfirmationEmailEnabled(activationCodeBatchDto.get);   	
    	//activationCodeRegistrationDefinition.setPageDefinition(ProductPageDefinition.PageDefinitionType.PRODUCT_PAGE_DEFINITION);   	
    	//activationCodeRegistrationDefinition.setRegistrationActivation(registrationActivation);
    	activationCodeRegistrationDefinition.setVersion(activationCodeBatchDto.getVersion());  	    	
    	
		activationCodeBatch.setActivationCodeRegistrationDefinition(activationCodeRegistrationDefinition);
    	if (activationCodeList != null) {
    		activationCodeBatch.setActivationCodes(activationCodeList);
    	}
		if (batchId != null) {
			activationCodeBatch.setBatchId(batchId);    		
		}
		if (activationCodeBatchDto.getCodePrefix() != null) {
			activationCodeBatch.setCodePrefix(activationCodeBatchDto.getCodePrefix());	
		}
		if (activationCodeBatchDto.getNumberOfTokens() != 0) {
			activationCodeBatch.setNumberOfTokens(activationCodeBatchDto.getNumberOfTokens());
		}
		if (createdDate != null) {
			activationCodeBatch.setCreatedDate(createdDate);
		}
		if (endDate != null) {
			activationCodeBatch.setEndDate(endDate);
		}
		if ( activationCodeBatchDto.getCurrentBatchId() != null) {
			activationCodeBatch.setId(activationCodeBatchDto.getCurrentBatchId());
		}
		if (licenceTemplate != null) {
			activationCodeBatch.setLicenceTemplate(licenceTemplate);
		}
		if (startDate != null) {
			activationCodeBatch.setStartDate(startDate);
		}
		if (updatedDate != null) {
			activationCodeBatch.setUpdatedDate(updatedDate);	
		}
		if ("" + activationCodeBatchDto.getVersion() != null) {
			activationCodeBatch.setVersion(activationCodeBatchDto.getVersion());
		}
			
		if(activationCodeBatchDto.getAddedInPool() != null) {
			activationCodeBatch.setAddedInPool(activationCodeBatchDto.getAddedInPool());
		}
		return activationCodeBatch;
		
		//De-duplication
		//return activationCodeBatchDao.getActivationCodeBatchByBatchId(batchId);		
	}
	
	@Override
    public final List<ActivationCode> getActivationCodesByBatchId(final String batchId) throws ServiceLayerException {
		List<ActivationCode> activationCodes = new ArrayList<ActivationCode>();
		ActivationCodeBatchDto batchDto = new ActivationCodeBatchDto(); 
		try {
			batchDto = erightsFacade.getActivationCodeBatch(batchId,true);
		} catch (AccessDeniedException | ErightsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		List<String> codes = batchDto.getActivationCodes();
		for (String code : codes) {
			ActivationCode activationCode = new ActivationCode();
			activationCode.setCode(code);
			activationCodes.add(activationCode);
		}
		
		return activationCodes;
		
		//de-duplication
		//return activationCodeDao.getActivationCodeByBatch(activationCodeBatchDao.loadEntity(batchId));
    }

    @Override
    @PreAuthorize("hasRole('ROLE_READ_ACTIVATION_CODE_BATCH')")
    public final byte[] getActivationCodeByBatch(final String batchId, final boolean format) throws ServiceLayerException {
        try{
            return getActivationCodeFile(getActivationCodesByBatchId(batchId), format);
        }catch(IOException io){
            throw new ServiceLayerException("problem getting activation codes", io);
        }
	}
    
    public final byte[] getActivationCodeBatchFileAsync(ActivationCodeBatchDto activationCodeBatchDto) throws ServiceLayerException {
        try{
            return getActivationCodeFileAsync(activationCodeBatchDto);
        }catch(IOException io){
            throw new ServiceLayerException("problem getting activation codes", io);
        }
	}
	
	private final byte[] getActivationCodeFile(final List<ActivationCode> activationCodes, final boolean format) throws UnsupportedEncodingException   {
		StringBuilder stringBuilder = new StringBuilder();
		for(ActivationCode activationCode : activationCodes) {
			if(stringBuilder.length() > 0) {
				stringBuilder.append("\r\n");
			}
			if(format) {
				stringBuilder.append(EACStringUtils.format(activationCode.getCode(), 4, '-') );
			} else {
				stringBuilder.append(activationCode.getCode());
			}
		}
		return stringBuilder.toString().getBytes("UTF-8");
	}
	
	private final byte[] getActivationCodeFileAsync(ActivationCodeBatchDto activationCodeBatchDto) throws UnsupportedEncodingException   {
		StringBuilder stringBuilder = new StringBuilder();
		for(String activationCode : activationCodeBatchDto.getActivationCodes()) {
			if(stringBuilder.length() > 0) {
				stringBuilder.append("\r\n");
			}
			
			stringBuilder.append(activationCode);
		}
		return stringBuilder.toString().getBytes(UTF8_ENCODING);
	}
	/*
	public final List<ActivationCodeBatch> getActivationCodeBatchByDivision(final List<DivisionAdminUser> divisionAdminUsers) {
		return activationCodeBatchDao.getActivationCodeBatchByDivision(divisionAdminUsers);
	}
	 */
	@Override
	public final ActivationCodeBatch createDefaultActivationCodeBatch()
			throws ServiceLayerException {
		return new ActivationCodeBatch();
	}

	@Override
	@PreAuthorize("hasRole('ROLE_READ_ACTIVATION_CODE_BATCH')")
	public final ActivationCodeBatch getActivationCodeBatchById(final String id) {
		return activationCodeBatchDao.getEntity(id);
	}

	private final ActivationCodeBatchDto saveActivationTokens(final ActivationCodeBatch activationCodeBatch, final int numTokens, final int allowedUsage, final String productId, final String productGroupId) throws AccessDeniedException, ErightsException {
		
		//int batchSize = Integer.parseInt(EACSettings.getProperty(EACSettings.HIBERNATE_JDBC_BATCH_SIZE));
		ActivationCodeBatchDto activationCodeBatchDto = new ActivationCodeBatchDto(activationCodeBatch,numTokens,allowedUsage,productId,productGroupId);
		
		activationCodeBatchDto = erightsFacade.createActivationCodeBatch(activationCodeBatchDto) ;
		
		return activationCodeBatchDto;
	}
	
	private final void saveActivationTokensAsync(final ActivationCodeBatch activationCodeBatch, final int numTokens, final int allowedUsage, final String productId, final String productGroupId, String adminEmail, String adminName) throws AccessDeniedException, ErightsException, ServiceLayerException {
		
		ActivationCodeBatchDto activationCodeBatchDto = new ActivationCodeBatchDto(activationCodeBatch,numTokens,allowedUsage,productId,productGroupId);
		byte[] activationCodeFileUnformatted = null ;
		byte[] activationCodeFileFormatted = null ;
		try {
		activationCodeBatchDto = erightsFacade.createActivationCodeBatch(activationCodeBatchDto) ;
		} catch (ProductNotFoundException e1) {
			String msg = String.format("Product not found Exception");
            LOGGER.error(msg, e1);
			try {
				activationCodeFileUnformatted = e1.getMessage().getBytes(UTF8_ENCODING) ;
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				LOGGER.error(ExceptionUtil.getStackTrace(e));
			}
		} catch (UserNotFoundException e1) {
			String msg = String.format("User not found Exception");
            LOGGER.error(msg, e1);
			try {
				activationCodeFileUnformatted = e1.getMessage().getBytes(UTF8_ENCODING) ;
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				LOGGER.error(ExceptionUtil.getStackTrace(e));
			}
		} catch (LicenseNotFoundException e1) {
			String msg = String.format("License not found Exception");
            LOGGER.error(msg, e1);
			try {
				activationCodeFileUnformatted = e1.getMessage().getBytes(UTF8_ENCODING) ;
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				LOGGER.error(ExceptionUtil.getStackTrace(e));
			}
		} catch (AccessDeniedException e1) {
			String msg = String.format("Access denied Exception");
            LOGGER.error(msg, e1);
			try {
				activationCodeFileUnformatted = e1.getMessage().getBytes(UTF8_ENCODING) ;
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				LOGGER.error(ExceptionUtil.getStackTrace(e));
			}
		} catch (GroupNotFoundException e1) {
			String msg = String.format("Group not found Exception");
            LOGGER.error(msg, e1);
			try {
				activationCodeFileUnformatted = e1.getMessage().getBytes(UTF8_ENCODING) ;
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				LOGGER.error(ExceptionUtil.getStackTrace(e));
			}
		} catch (ErightsException e1) {
			String msg = String.format("unexpected atypon error");
            LOGGER.error(msg, e1);
			try {
				activationCodeFileUnformatted = e1.getMessage().getBytes(UTF8_ENCODING) ;
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				LOGGER.error(ExceptionUtil.getStackTrace(e));
			}
		}
		if (activationCodeBatchDto != null && activationCodeBatchDto.getActivationCodes() != null && activationCodeBatchDto.getActivationCodes().size() > 0 ) {
			activationCodeFileUnformatted = getActivationCodeBatchFileAsync(activationCodeBatchDto) ;
		}
		try {
			activationCodeFileFormatted = getActivationCodeFileFormatted(activationCodeBatchDto.getActivationCodes(), true);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
			LOGGER.error(ExceptionUtil.getStackTrace(e1));
		}
		String fileName = EACStringUtils.removeNonAlphanumericAndWhitespace(activationCodeBatch.getBatchId()) ;
		final String tempFileLocation = System.getProperty("java.io.tmpdir")+File.separator;
		try {
			ADMIN_NAME = adminName ;
			String fileSuffix = ".txt";
            MailCriteria mailCriteria = new MailCriteria();
            mailCriteria.setFrom(EmailUtils.getInternetAddressFrom());
            mailCriteria.addToAddress(new InternetAddress(adminEmail));
            mailCriteria.setSubject(EXPORT_EMAIL_SUBJECT + fileName);
            mailCriteria.setText(getEmailText());            
            
            
            mailCriteria.setAttachmentName(fileName +".zip");
            byte[] csvDataUnformatted = activationCodeFileUnformatted;
            byte[] csvDataFormatted = activationCodeFileFormatted;
            
            // Create directory if it doesn't exists
            File tempDir = new File(tempFileLocation);
            if (!tempDir.exists()) tempDir.mkdirs();
            
            zipIt(csvDataUnformatted, csvDataFormatted, fileName  + fileSuffix, fileName + "_formatted" + fileSuffix, tempFileLocation + fileName +".zip"); 
            File zipOutputfile = new File(tempFileLocation + fileName +".zip");
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
	
	private final void saveActivationTokens(final ActivationCodeBatch activationCodeBatch, final int allowedUsage, 
			final int actualUsage, String activationCode) throws ProductNotFoundException, UserNotFoundException, 
			LicenseNotFoundException, AccessDeniedException, GroupNotFoundException, ErightsException {
            ActivationCode activationCodeObj = new ActivationCode(activationCodeBatch, activationCode, Integer.valueOf(allowedUsage));
            activationCodeObj.setActualUsage(actualUsage);
            activationCodeBatch.addActivationCode(activationCodeObj);
            /*ActivationCodeBatchDto activationCodeBatchDto = new ActivationCodeBatchDto(activationCodeBatch, 
            		activationCodeBatch.getNumberOfTokens(), 
            		allowedUsage, 
            		actualUsage, 
            		Integer.parseInt(activationCodeBatch.getActivationCodeRegistrationDefinition().getProduct().getId()), 
            		Integer.parseInt(activationCodeBatch.getActivationCodeRegistrationDefinition().getEacGroup().getId()), 
            		null, 
            		activationCodeBatch.getCreatedDate().toLocalDate(), 
            		activationCodeBatch.getUpdatedDate().toLocalDate(), 
            		activationCodeBatch.getCodePrefix(), 
            		activationCodeBatch.getVersion());
            erightsFacade.updateActivationCodeBatch(activationCodeBatchDto);*/
            //activationCodeDao.save(activationCodeObj);
            //activationCodeDao.flush();
    }
	
	private final String getNextValidToken(final ActivationCodeGenerator activationCodeGenerator, final ActivationCodeBatch activationCodeBatch) {
		String token = activationCodeGenerator.createActivationCode(activationCodeBatch.getCodePrefix());
		while(tokenExists(token)) {
		    LOGGER.debug("Duplicate token generated: " + token + " - will retry...");
		    token = activationCodeGenerator.createActivationCode(activationCodeBatch.getCodePrefix());
		}
		return token;
	}
	
	private final boolean tokenExists(final String token) {
		List<ActivationCode> activationCodes = new ArrayList<ActivationCode>();
		ActivationCode activationCode = new ActivationCode();
		ActivationCodeBatchDto batchDto = new ActivationCodeBatchDto(); 
		try {
			erightsFacade.getActivationCodeBatchByActivationCode(token);
		} catch (AccessDeniedException | ErightsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		List<String> codes = batchDto.getActivationCodes();
		for (String code : codes) {
			activationCode.setCode(code);
			activationCodes.add(activationCode);
		}
		
		if (activationCodes == null) {
			return true;
		} else {
			return false;
		}
		
		//De-duplication
		//return !activationCodeDao.checkActivationCodeIsUnique(token);
	}

	@Override
	public final boolean isActivationCodeExists(final String code){
	    return tokenExists(code);
	}
	private final ActivationCodeGenerator getActivationCodeGenerator(final ActivationCodeFormat activationCodeFormat) {
		switch (activationCodeFormat) {
			case EAC_NUMERIC:
			    return new EacNumericActivationCode(); 
			default:
				throw new IllegalArgumentException("Unknown activationCodeFormat :" + activationCodeFormat);
		}
	}
	
	@Override
	@PreAuthorize("hasRole('ROLE_CREATE_ACTIVATION_CODE_BATCH')")
	public final ActivationCodeBatch saveActivationCodeBatch(final ActivationCodeBatch activationCodeBatch, 
												final ActivationCodeRegistrationDefinition activationCodeRegistrationDefinition,
												final int numTokens, 
												final int allowedUsage) throws ServiceLayerException, AccessDeniedException, ErightsException {
		activationCodeBatch.setActivationCodeRegistrationDefinition(activationCodeRegistrationDefinition);
		//de-duplication of ACB 
		/*if(activationCodeRegistrationDefinition.getEacGroup() != null){
			registrationDefinitionDao.saveOrUpdate(activationCodeRegistrationDefinition);
		}*/
		//activationCodeBatchDao.saveOrUpdate(activationCodeBatch);
		ActivationCodeBatchDto activationCodeBatchDto = null;
		List<ActivationCode> activationCodes = new ArrayList<ActivationCode>();
			if(activationCodeRegistrationDefinition.getProduct()!= null && activationCodeRegistrationDefinition.getProduct().getId() != null){
				activationCodeBatchDto = saveActivationTokens(activationCodeBatch, numTokens, allowedUsage, activationCodeRegistrationDefinition.getProduct().getId(), null);
			} else {
				/*ProductGroupDto productGroup = erightsFacade.getProductGroup(
						activationCodeRegistrationDefinition.getEacGroup().getId(), null);*/
				activationCodeBatchDto = saveActivationTokens(activationCodeBatch, numTokens, allowedUsage,null,activationCodeRegistrationDefinition.getEacGroup().getId());
				
			}		
	    if(activationCodeRegistrationDefinition.getEacGroup() != null){
	    	AuditLogger.logEvent("Saved ActivationCodeBatch", "batchId:"+activationCodeBatch.getBatchId(), AuditLogger.eacGroup(activationCodeRegistrationDefinition.getEacGroup()), "numTokens:"+numTokens, "allowedUsage:"+allowedUsage);
	    }else{
	    	AuditLogger.logEvent("Saved ActivationCodeBatch", "batchId:"+activationCodeBatch.getBatchId(), AuditLogger.product(activationCodeRegistrationDefinition.getProduct()), "numTokens:"+numTokens, "allowedUsage:"+allowedUsage);
	    }
	    for(String actCode : activationCodeBatchDto.getActivationCodes()){
	    	ActivationCode activationCode = new ActivationCode();
	    	activationCode.setCode(actCode);
	    	activationCodes.add(activationCode);
	    }
	    activationCodeBatch.setActivationCodes(activationCodes);
	    return activationCodeBatch;
	}
	
	@Override
    public final ActivationCodeBatch saveActivationCodeBatch(final ActivationCodeBatch activationCodeBatch,
                                                final ActivationCodeRegistrationDefinition activationCodeRegistrationDefinition,
                                                final int allowedUsage, final int actualUsage,
                                                String activationCode) throws ServiceLayerException {
	    
        activationCodeRegistrationDefinition.addActivationCodeBatch(activationCodeBatch);

        registrationDefinitionDao.saveOrUpdate(activationCodeRegistrationDefinition);
        try {
			saveActivationTokens(activationCodeBatch, allowedUsage, actualUsage,activationCode);
		} catch (AccessDeniedException | ErightsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        if(activationCodeRegistrationDefinition.getEacGroup() != null){
            AuditLogger.logEvent("Saved ActivationCodeBatch", "batchId:"+activationCodeBatch.getBatchId(), AuditLogger.eacGroup(activationCodeRegistrationDefinition.getEacGroup()), "allowedUsage:"+allowedUsage);
        }else{
            AuditLogger.logEvent("Saved ActivationCodeBatch", "batchId:"+activationCodeBatch.getBatchId(), AuditLogger.product(activationCodeRegistrationDefinition.getProduct()), "allowedUsage:"+allowedUsage);
        }
        return activationCodeBatch;
    }
	
	@Override
	@PreAuthorize("hasRole('ROLE_LIST_ACTIVATION_CODE_BATCH')")
	public final Paging<ActivationCodeBatch> searchActivationCodeBatches(final ActivationCodeBatchSearchCriteria searchCriteria, final PagingCriteria pagingCriteria) {
	   
		AmazonSearchRequest request = new AmazonSearchRequest();
		request.setResultsPerPage(pagingCriteria.getItemsPerPage());    	
    	List<AmazonSearchFields> searchFieldsList = new ArrayList<AmazonSearchFields>(); 
    	List<ActivationCodeBatch> batches = null;
    	String startDate = null;
		String endDate = null;
		DateTime sDate = null;
		DateTime eDate = null;
    	
    	if(searchCriteria.getBatchId() != null){
    		AmazonSearchFields searchField = new AmazonSearchFields();
    		searchField.setName(SearchDomainFields.CLAIM_TICKET_BATCHNAME);
    		String batchId = searchCriteria.getBatchId().replace("'", "");
    		searchField.setValue(batchId);
    		searchField.setType("String");
    		searchFieldsList.add(searchField);
    	}
    	//need changes after productgroupsearch widget changes
    	if(searchCriteria.getRegisterableProduct() != null){
    		AmazonSearchFields searchField = new AmazonSearchFields();
    		searchField.setName(SearchDomainFields.CLAIM_TICKET_PRODUCTID); 			
			searchField.setValue(searchCriteria.getRegisterableProduct().getId());
    		searchField.setType("String");
    		searchFieldsList.add(searchField);
    	}
    	//need changes after productgroupsearch widget changes
    	if(searchCriteria.getEacGroupId() != null){
    		AmazonSearchFields searchField = new AmazonSearchFields();
    		searchField.setName(SearchDomainFields.CLAIM_TICKET_PRODUCTGROUPID);
    		searchField.setValue(searchCriteria.getEacGroupId());
    		searchField.setType("String");
    		searchFieldsList.add(searchField);
    	}
    	if(searchCriteria.getCode() != null){
    		AmazonSearchFields searchField = new AmazonSearchFields();
    		searchField.setName(SearchDomainFields.CLAIM_TICKET_ACTIVATIONCODE);
    		searchField.setValue(searchCriteria.getCode());
    		searchField.setType("String");
    		searchFieldsList.add(searchField);
    	}
    	if(searchCriteria.getLicenceTemplate() != null && !searchCriteria.getLicenceTemplate().getSimpleName().equals("Any")){
    		AmazonSearchFields searchField = new AmazonSearchFields();
    		searchField.setName(SearchDomainFields.CLAIM_TICKET_LICENSETYPE);
    		if(searchCriteria.getLicenceTemplate().getSimpleName().equals(ConcurrentLicenceTemplate.class.getSimpleName())) {
    			searchField.setValue(LicenceTemplate.LicenceType.CONCURRENT.toString());
    		}
    		if(searchCriteria.getLicenceTemplate().getSimpleName().equals(UsageLicenceTemplate.class.getSimpleName())) {
    			searchField.setValue(LicenceTemplate.LicenceType.USAGE.toString());
    		}
    		if(searchCriteria.getLicenceTemplate().getSimpleName().equals(RollingLicenceTemplate.class.getSimpleName())) {
    			searchField.setValue(LicenceTemplate.LicenceType.ROLLING.toString());
    		}
    		searchField.setType("String");
    		searchFieldsList.add(searchField);
    	}
    	if(searchCriteria.getBatchDate() != null){
    		//start date
    		AmazonSearchFields searchField = new AmazonSearchFields();
    		searchField.setName(SearchDomainFields.CLAIM_TICKET_STARTDATE);
    		//searchField.setValue(searchCriteria.getLicenceTemplate().getSimpleName());
    		searchField.setType("String");
    		searchFieldsList.add(searchField);
    	}
    	
    	if(searchCriteria.getBatchDate() != null){
    		AmazonSearchFields searchField = new AmazonSearchFields();
    		searchField.setName(SearchDomainFields.CLAIM_TICKET_ENDDATE);
    		//searchField.setValue(searchCriteria.getLicenceTemplate().toString());
    		searchField.setType("String");
    		searchFieldsList.add(searchField);
    	}
    	
    	List<String> searchResultFieldsList = new ArrayList<String>();
		searchResultFieldsList.add(SearchDomainFields.CLAIM_TICKET_BATCHNAME);
		searchResultFieldsList.add(SearchDomainFields.CLAIM_TICKET_PRODUCTID);
		searchResultFieldsList.add(SearchDomainFields.CLAIM_TICKET_PRODUCTGROUPID);
		searchResultFieldsList.add(SearchDomainFields.CLAIM_TICKET_ACTIVATIONCODE);
		searchResultFieldsList.add(SearchDomainFields.CLAIM_TICKET_STARTDATE);
		searchResultFieldsList.add(SearchDomainFields.CLAIM_TICKET_ENDDATE);
		searchResultFieldsList.add(SearchDomainFields.CLAIM_TICKET_LICENSETYPE);
		
		HashMap<String, String> hMap1 = new HashMap<String, String>();
		hMap1.put(pagingCriteria.getSortColumn(), "asc");
				
		request.setSortFields(hMap1);
		request.setSearchResultFieldsList(searchResultFieldsList);		
    	request.setSearchFieldsList(searchFieldsList);
    	int itemsPerPage = pagingCriteria.getItemsPerPage();
    	request.setStartAt((pagingCriteria.getRequestedPage()-1)*itemsPerPage);
    	//request.setStartAt((pagingCriteria.getRequestedPage()-1)*10);
    	AmazonCloudSearchServiceImpl cloudSearch = new AmazonCloudSearchServiceImpl();
    	AmazonSearchResponse response = cloudSearch.searchActivationCodeBatch(request, searchCriteria);
    	
    	ConvertSearch convert = new ConvertSearch();
    	try {
			batches = convert.convertActivationCodeBatch(response);
		} catch (InstantiationException | IllegalAccessException | UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			LOGGER.error(ExceptionUtil.getStackTrace(e1));
		}
    	
    	
    	/*if (searchCriteria.getBatchDate() != null) {
    		for (Map<String , String> field: fields) {
        		if(field.get(SearchDomainFields.CLAIM_TICKET_STARTDATE) != null) {
    				startDate = field.get(SearchDomainFields.CLAIM_TICKET_STARTDATE);
    				startDate = startDate.replaceAll("\\[", "").replaceAll("\\]","");	
    				sDate = new DateTime(startDate);
    			} 			
    			if(field.get(SearchDomainFields.CLAIM_TICKET_ENDDATE) != null) {
    				endDate = field.get(SearchDomainFields.CLAIM_TICKET_ENDDATE);
    				endDate = endDate.replaceAll("\\[", "").replaceAll("\\]","");	
    				eDate = new DateTime(endDate);    				
    			}
    			
    			if (field.get(SearchDomainFields.CLAIM_TICKET_STARTDATE) == null && field.get(SearchDomainFields.CLAIM_TICKET_ENDDATE) == null) {
    				try {
						batches = convert.convertActivationCodeBatch(response, searchCriteria);
					} catch (InstantiationException | IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
    			}
    			
    			if (field.get(SearchDomainFields.CLAIM_TICKET_STARTDATE) != null && field.get(SearchDomainFields.CLAIM_TICKET_ENDDATE) != null) {      			
        			//compare date with time
        			if (((sDate.toYearMonthDay()).isBefore(validDate.toYearMonthDay()) || (sDate.toYearMonthDay()).equals(validDate.toYearMonthDay())) 
        					&& (((eDate.toYearMonthDay()).isAfter(validDate.toYearMonthDay())) || (eDate.toYearMonthDay()).equals(validDate.toYearMonthDay()))) {
        				try {
							batches = convert.convertActivationCodeBatch(response, searchCriteria);
						} catch (InstantiationException e) {
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						}
        			}
        			
        			//compare only date
        			if (((sDate.toYearMonthDay()).isBefore(validDate.toYearMonthDay()) || (sDate.toYearMonthDay()).equals(validDate.toYearMonthDay())) 
        					&& (((eDate.toYearMonthDay()).isAfter(validDate.toYearMonthDay())) || (eDate.toYearMonthDay()).equals(validDate.toYearMonthDay()))) {
        				try {
							batches = convert.convertActivationCodeBatch(response, searchCriteria);
						} catch (InstantiationException e) {
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						}
        			}
    			}   
    			
        	}
    	} else {
    		try {
				batches = convert.convertActivationCodeBatch(response, searchCriteria);
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
    	}*/
    	  	   	
		/*AuditLogger.logEvent("Search ActivationCodeBatch", "ActivationCodeBatch:" + searchCriteria.getBatchId(), 
				AuditLogger.activationCodeBatchSearchCriteria(searchCriteria));*/
		return Paging.valueOf(pagingCriteria, batches, response.getResultsFound());
    
	}
	
    @Override
    public final ActivationCodeBatch saveActivationCodeBatchWithTemplate(final ActivationCodeBatch acBatch, final EnforceableProductDto enforceableProduct, LicenceTemplate licTemplate, 
    														final int numTokens, int allowedUsage) throws ServiceLayerException {  
    	validateLicenceTemplate(licTemplate);
    	ActivationCodeBatch activationCodeBatch = null;
    	//De-duplication
        //this.licTemplateDao.save(licTemplate);  
        acBatch.setLicenceTemplate(licTemplate);
        ActivationCodeRegistrationDefinition regDef = new ActivationCodeRegistrationDefinition();
        ProductRegistrationDefinition prodRegDef = new ProductRegistrationDefinition();
        Set<ProductRegistrationDefinition> prodRegDefs = new HashSet<ProductRegistrationDefinition>();
        ProductPageDefinition prodPageDef = new ProductPageDefinition();
        ActivationCodeBatchDto activationCodeBatchDto = new ActivationCodeBatchDto();      
        Set<ActivationCodeBatch> acBatches = new HashSet<ActivationCodeBatch> ();
        prodRegDef.setLicenceTemplate(licTemplate);
        
        Product product = convertEnforceableProductToProduct(enforceableProduct);
        prodRegDef.setProduct(product);
        //prodRegDef.setRegistrationActivation(registrationActivation);
        prodRegDef.setVersion(product.getVersion());
        
        prodRegDefs.add(prodRegDef);
        
        //prodPageDef.setDivision(product.getDivision());
        prodPageDef.setId(enforceableProduct.getProductId());
        prodPageDef.setName(enforceableProduct.getName());
        //prodPageDef.setPageComponents(pageComponents);
        //prodPageDef.setPreamble(preamble);
        prodPageDef.setRegistrationDefinitions(prodRegDefs);
        //prodPageDef.setTitle(product.get);
        
        regDef.setActivationCodeBatchs(acBatches);
        /*regDef.setConfirmationEmailEnabled(activationCodeBatchDto.g);*/
        //regDef.setEacGroup(eacGroup);
        regDef.setLicenceTemplate(licTemplate);
        regDef.setPageDefinition(prodPageDef);
        regDef.setProduct(product);
        //regDef.setRegistrationActivation(registrationActivation);
        regDef.setVersion(activationCodeBatchDto.getVersion());
        
        
        if(product.getState() != null && (product.getState() == ProductState.REMOVED || product.getState() == ProductState.RETIRED)){
        	throw new ActivationCodeServiceLayerException("Failed to Create Batch, the product state is invalid");
        }
        //Session session = this.registrationDefinitionDao.getSessionFromDao();
        //session.refresh(regDef, LockOptions.UPGRADE);
        Hibernate.initialize(regDef.getProduct());
        Hibernate.initialize(regDef.getProduct());
        
        try {
			activationCodeBatch = this.saveActivationCodeBatch(acBatch, regDef, numTokens, allowedUsage);
		} catch (AccessDeniedException e){
			e.printStackTrace();
		}
        catch (ErightsException e) {
			if(e.getErrorCode()==2051) {
				throw new ActivationCodeServiceLayerException("The Product for Id "+product.getId()+" is not associated with an activation code registration definition");
			}
			else if(e.getErrorCode()==1011) {
				throw new ActivationCodeServiceLayerException("The Batch Id is already in use");
			}
			else if(e.getErrorCode() == 2009) {
				throw new ActivationCodeServiceLayerException("The Product for Id ["+ product.getId() +"] cannot be found.");
			}
			else if (e.getErrorCode() == 3055) {
				if (e.getMessage().contains("product state is invalid")) {
					throw new ActivationCodeServiceLayerException("Failed to Create Batch, the product state is invalid");
				} else {
					throw new ActivationCodeServiceLayerException(e.getMessage());
				}
			}
			throw new ActivationCodeServiceLayerException(e.getMessage());
		}
        return activationCodeBatch;
    }
    
    private void validateLicenceTemplate(final LicenceTemplate licenceTemplate) throws ServiceLayerValidationException {
    	switch (licenceTemplate.getLicenceType()) {
    	case CONCURRENT:
    		ConcurrentLicenceTemplate concurrentLicenceTemplate = (ConcurrentLicenceTemplate) licenceTemplate;
    		int userConcurrency = concurrentLicenceTemplate.getUserConcurrency();
    		int totalConcurrency = concurrentLicenceTemplate.getTotalConcurrency();
    		if (userConcurrency == 0 || userConcurrency < -1) {
    			throw new ServiceLayerValidationException("The user concurrency must be set to -1 or a value greater than 0");
    		}
    		if (totalConcurrency == 0 || totalConcurrency < -1) {
    			throw new ServiceLayerValidationException("The total concurrency must be set to -1 or a value greater than 0");
    		}
    		break;
    	case ROLLING:
    		RollingLicenceTemplate rollingLicenceTemplate = (RollingLicenceTemplate) licenceTemplate;
    		int timePeriod = rollingLicenceTemplate.getTimePeriod();
    		if (timePeriod < 1) {
    			throw new ServiceLayerValidationException("The time period must be set to a value greater than 0");
    		}
    		break;
    	case USAGE:
    		UsageLicenceTemplate usageLicenceTemplate = (UsageLicenceTemplate) licenceTemplate;
    		int allowedUsages = usageLicenceTemplate.getAllowedUsages();
    		if (allowedUsages < 1) {
    			throw new ServiceLayerValidationException("The number of allowed usages must be set to a value greater than 0");
    		}
    		break;
    	default:
    		throw new ServiceLayerValidationException("Unsupported licence type. Concurrent, Rolling and Usage licences are supported.");
    	}
    }
    
    public final void updateActivationCode(final ActivationCode activationCode) {
    	activationCodeDao.update(activationCode);
    }
    
    //not used any where 
    public final ActivationCode getActivationCodeAndProductByCode(final String activationCode) {
    	return activationCodeDao.getActivationCodeAndProductByCode(activationCode);
    }
    
    public final ActivationCode getActivationCodeWithDetails(final String id) throws ProductNotFoundException, UserNotFoundException, 
    LicenseNotFoundException, AccessDeniedException, GroupNotFoundException, ErightsException, ServiceLayerException {
    	ActivationCodeBatchDto activationCodeBatchDto = new ActivationCodeBatchDto();
    	ActivationCode activationCode = new ActivationCode(); 
    	
		try {
			activationCodeBatchDto = erightsFacade.getActivationCodeDetailsByActivationCode(id);
		} catch (ErightsException e) {
			if(e.getErrorCode().equals(1123) || e.getErrorCode().equals(2004))
				throw new ServiceLayerException("This is not a valid activation code");
			}
		LicenceDetailDto licenceDetailDto = activationCodeBatchDto.getLicenceDetailDto();
		LicenceTemplate licenceTemplate = getLicenseTemplate(licenceDetailDto);
		
		Customer customer = new Customer();
		if (activationCodeBatchDto.getActivationCodeRegistration() != null && 
				activationCodeBatchDto.getActivationCodeRegistration().getCustomer() != null) {
			customer = activationCodeBatchDto.getActivationCodeRegistration().getCustomer();
		}
		 
		ActivationCodeRegistration activationCodeRegistration = new ActivationCodeRegistration();
				//activationCodeBatchDto.getActivationCodeRegistration();
		ActivationCodeRegistrationDefinition activationCodeRegistrationDefinition = 
				new ActivationCodeRegistrationDefinition();
		Set<ActivationCodeRegistration> activationCodeRegistrations = 
				new HashSet<ActivationCodeRegistration>();
		EnforceableProductDto enforceableProductDto = null ;
		EacGroups eacGroups = null ;
		if (activationCodeBatchDto.getProductGroupId() != null) {
			ProductGroupDto productGroupDto = 
					eacGrpService.getProductGroupDtoByErightsId(activationCodeBatchDto.getProductGroupId());
			eacGroups = eacGrpService.getEacGroupByProductGroupDto(productGroupDto);
			activationCodeRegistrationDefinition.setEacGroup(eacGroups);
		} else {
			enforceableProductDto = 
					productService.getEnforceableProductByErightsId(activationCodeBatchDto.getProductId());
			Product product = new RegisterableProduct();
			product.setId(enforceableProductDto.getProductId());
			//activationCodeRegistrationDefinition = registrationDefinitionDao.getActivationCodeRegistrationDefinitionByProduct(product);
			Set<ExternalProductId> externalProductIds = null;
			if(enforceableProductDto.getExternalIds()!=null && enforceableProductDto.getExternalIds().size()>0){
				externalProductIds = new HashSet<ExternalProductId>();
				List<ExternalProductId> externalProductIdList = enforceableProductDto.getExternalIds();
				if (externalProductIdList != null) {
					for (ExternalProductId externalProductId : externalProductIdList) {
						externalProductIds.add(externalProductId);
					}
				}
			}			
			product.setId(enforceableProductDto.getProductId());
			product.setProductName(enforceableProductDto.getName());
			product.setExternalIds(externalProductIds);
			product.setEmail(enforceableProductDto.getAdminEmail());
			product.setHomePage(enforceableProductDto.getHomePage());
			product.setLandingPage(enforceableProductDto.getLandingPage());
			product.setServiceLevelAgreement(enforceableProductDto.getSla());
			product.setActivationStrategy(enforceableProductDto.getActivationStrategy());
			//product.setState("");
			activationCodeRegistrationDefinition.setProduct(product);
		}
		//activationCodeRegistrationDefinition.setRegistrationActivation(registrationActivation);
		activationCodeRegistrationDefinition.setLicenceTemplate(licenceTemplate);
		activationCodeRegistrationDefinition.setVersion(activationCodeBatchDto.getVersion());
		
		activationCode.setCode(id);
		activationCodeRegistration.setActivationCode(activationCode); 	
		activationCodeRegistration.setCustomer(customer);
		activationCodeRegistration.setRegistrationDefinition(activationCodeRegistrationDefinition);
		activationCodeRegistrations.add(activationCodeRegistration);
		activationCodeRegistrations.addAll(activationCodeBatchDto.getActivationCodeRegistrations());
		
		ActivationCodeBatch activationCodeBatch = getActivationCodeBatchDetailsByCode(id,enforceableProductDto,eacGroups);
		if (enforceableProductDto != null ){
			activationCodeBatch.getActivationCodeRegistrationDefinition().setConfirmationEmailEnabled(enforceableProductDto.getConfirmationEmailEnabled());
		}
		activationCode.setActivationCodeBatch(activationCodeBatch);
		activationCode.setActualUsage(activationCodeBatchDto.getActualUsage());
		activationCode.setAllowedUsage(activationCodeBatchDto.getAllowedUsages());
		activationCode.setCode(id);
		activationCode.setRegistrations(activationCodeRegistrations);
		activationCode.setVersion(activationCodeBatchDto.getVersion());
    	
    	return activationCode;
    	//De-duplication
    	//return activationCodeDao.getActivationCodeWithDetails(id);
    }
    
    private ActivationCodeBatch getActivationCodeBatchDetailsByCode(
			final String activationCode, EnforceableProductDto product,
			EacGroups eacGroup) throws ProductNotFoundException,
			UserNotFoundException, LicenseNotFoundException,
			AccessDeniedException, GroupNotFoundException, ErightsException {

		ActivationCodeBatch activationCodeBatch = new ActivationCodeBatch();
		ActivationCodeBatchDto activationCodeBatchDto = erightsFacade
				.getActivationCodeBatchByActivationCode(activationCode);

		DateTime startDate = null;
		DateTime endDate = null;
		DateTime createdDate = null;
		DateTime updatedDate = null;

		if (activationCodeBatchDto.getValidFrom() != null) {
			startDate = new DateTime(activationCodeBatchDto.getValidFrom()
					.toString());
		}
		if (activationCodeBatchDto.getValidTo() != null) {
			endDate = new DateTime(activationCodeBatchDto.getValidTo()
					.toString());
		}
		if (activationCodeBatchDto.getCreatedDate() != null) {
			createdDate = new DateTime(activationCodeBatchDto.getCreatedDate()
					.toString());
		}
		if (activationCodeBatchDto.getUpdatedDate() != null) {
			updatedDate = new DateTime(activationCodeBatchDto.getUpdatedDate()
					.toString());
		}

		if (activationCodeBatchDto.getCodeFormat() == CODE_FORMAT) {
			activationCodeBatch
					.setActivationCodeFormat(ActivationCodeFormat.EAC_NUMERIC);
		}

		LicenceDetailDto licenceDetailDto = activationCodeBatchDto
				.getLicenceDetailDto();
		LicenceTemplate licenceTemplate = getLicenseTemplate(licenceDetailDto);
		List<String> codes = activationCodeBatchDto.getActivationCodes();

		List<ActivationCode> activationCodeList = new ArrayList<ActivationCode>();
		for (String code : codes) {
			ActivationCode activationCodes = new ActivationCode();
			activationCodes.setCode(code);
			activationCodeList.add(activationCodes);
		}

		ActivationCodeRegistrationDefinition activationCodeRegistrationDefinition = null;
		if (product != null) {
			Product eacProduct = new RegisterableProduct();
			eacProduct.setId(product.getProductId());
			activationCodeRegistrationDefinition = registrationDefinitionDao
					.getRegistrationDefinitionByProduct(
							ActivationCodeRegistrationDefinition.class,
							eacProduct);
		}

		if (activationCodeRegistrationDefinition == null) {
			activationCodeRegistrationDefinition = new ActivationCodeRegistrationDefinition();
		}

		if (licenceTemplate != null) {
			activationCodeRegistrationDefinition
					.setLicenceTemplate(licenceTemplate);
			if (product != null) {
				Product eacProduct = new RegisterableProduct();
				eacProduct.setId(product.getProductId());
				eacProduct.setProductName(product.getName());
				eacProduct.setActivationStrategy(product.getActivationStrategy());
				//eacProduct.setExternalIds(product.getExternalIds());
				eacProduct.setEmail(product.getAdminEmail());
				eacProduct.setHomePage(product.getHomePage());
				eacProduct.setLandingPage(product.getLandingPage());
				eacProduct.setServiceLevelAgreement(product.getSla());
				eacProduct.setProductUrls(product.getUrls());
				eacProduct.setValidatorEmail(product.getValidatorEmail());
				activationCodeRegistrationDefinition.setProduct(eacProduct);
			} else if (eacGroup != null) {
				activationCodeRegistrationDefinition.setEacGroup(eacGroup);
			}

		}

		activationCodeRegistrationDefinition.setVersion(activationCodeBatchDto
				.getVersion());

		activationCodeBatch
				.setActivationCodeRegistrationDefinition(activationCodeRegistrationDefinition);
		if (activationCodeList != null) {
			activationCodeBatch.setActivationCodes(activationCodeList);
		}
		if (activationCodeBatchDto.getBatchId() != null) {
			activationCodeBatch.setBatchId(activationCodeBatchDto.getBatchId());
		}
		if (activationCodeBatchDto.getCodePrefix() != null) {
			activationCodeBatch.setCodePrefix(activationCodeBatchDto
					.getCodePrefix());
		}
		if (activationCodeBatchDto.getNumberOfTokens() != 0) {
			activationCodeBatch.setNumberOfTokens(activationCodeBatchDto
					.getNumberOfTokens());
		}
		if (createdDate != null) {
			activationCodeBatch.setCreatedDate(createdDate);
		}
		if (endDate != null) {
			activationCodeBatch.setEndDate(endDate);
		}
		if (activationCodeBatchDto.getCurrentBatchId() != null) {
			activationCodeBatch.setId(activationCodeBatchDto
					.getCurrentBatchId());
		}
		if (licenceTemplate != null) {
			activationCodeBatch.setLicenceTemplate(licenceTemplate);
		}
		if (startDate != null) {
			activationCodeBatch.setStartDate(startDate);
		}
		if (updatedDate != null) {
			activationCodeBatch.setUpdatedDate(updatedDate);
		}
		if ("" + activationCodeBatchDto.getVersion() != null) {
			activationCodeBatch.setVersion(activationCodeBatchDto.getVersion());
		}

		return activationCodeBatch;

		
	}
    public final Paging<ActivationCodeReportDto> getActivationCodeSearch(final ActivationCodeSearchCriteria criteria, final PagingCriteria pagingCriteria, final AdminUser adminUser) {
            	
    	AmazonSearchRequest request = new AmazonSearchRequest();
    	request.setResultsPerPage(pagingCriteria.getItemsPerPage());
    	//request.setStartAt(pagingCriteria.getFirstResult());
    	
    	List<AmazonSearchFields> searchFieldsList = new ArrayList<AmazonSearchFields>();    	
    	
    	if(criteria.getCode() != null){
    		AmazonSearchFields searchField = new AmazonSearchFields();
    		searchField.setName(SearchDomainFields.CLAIM_TICKET_ACTIVATIONCODE);
    		searchField.setValue(criteria.getCode());
    		searchField.setType("String");
    		searchFieldsList.add(searchField);
    	}
    	if(criteria.getProductId() != null){
    		AmazonSearchFields searchField = new AmazonSearchFields();
    		searchField.setName(SearchDomainFields.CLAIM_TICKET_PRODUCTID);
    		
    		//fetch productid from eacid  				
			Hits hitsResult = new CloudSearchUtils(DomainType.PRODUCT).searchDocument(SearchDomainFields.PRODUCT_PRODUCTID, "'" + criteria.getProductId() + "'");
			if (hitsResult.getFound() != 0) {				
				Hit result = hitsResult.getHit().get(0);
				Map<String, List<String>> keys = result.getFields();				
				String productId = keys.get(SearchDomainFields.PRODUCT_PRODUCTID).get(0);	    		
	    		searchField.setValue(productId);
	    		searchField.setType("String");
	    		searchFieldsList.add(searchField);
			}		
    	}
    	if(criteria.getEacGroupId() != null){
    		AmazonSearchFields searchField = new AmazonSearchFields();
    		searchField.setName(SearchDomainFields.CLAIM_TICKET_PRODUCTGROUPID);
    		searchField.setValue(criteria.getEacGroupId());
    		searchField.setType("String");
    		searchFieldsList.add(searchField);
    	}
    	if(criteria.getActivationCodeState() != null && criteria.getActivationCodeState().toString() != "ALL"){
    		AmazonSearchFields searchField = new AmazonSearchFields();
    		searchField.setName(SearchDomainFields.CLAIM_TICKET_AVAILABLESTATE);
    		searchField.setValue(criteria.getActivationCodeState().toString());
    		searchField.setType("String");
    		searchFieldsList.add(searchField);
    	}     	
    	
    	List<String> searchResultFieldsList = new ArrayList<String>();
		searchResultFieldsList.add(SearchDomainFields.CLAIM_TICKET_ACTIVATIONCODE);
		searchResultFieldsList.add(SearchDomainFields.CLAIM_TICKET_PRODUCTID);
		//searchResultFieldsList.add(SearchDomainFields.PRODUCT_PRODUCTNAME);
		searchResultFieldsList.add(SearchDomainFields.CLAIM_TICKET_PRODUCTGROUPID);
		searchResultFieldsList.add(SearchDomainFields.CLAIM_TICKET_AVAILABLESTATE);
		
		HashMap<String, String> hMap1 = new HashMap<String, String>();
		hMap1.put(pagingCriteria.getSortColumn(), "asc");
				
		request.setSortFields(hMap1);
		request.setSearchResultFieldsList(searchResultFieldsList);
		
    	request.setSearchFieldsList(searchFieldsList);
    	request.setStartAt((pagingCriteria.getRequestedPage()-1)*10);
    	
    	AmazonCloudSearchServiceImpl cloudSearch = new AmazonCloudSearchServiceImpl();
    	AmazonSearchResponse response = cloudSearch.searchActivationCode(request);     			  	
    	ConvertSearch convert = new ConvertSearch();
    	List<ActivationCodeReportDto> dtos = convert.convertActivationCode(response);    	
    	    	   	
		AuditLogger.logEvent("Search ActivationCode", "ActivationCode:" + criteria.getCode(), AuditLogger.activationCodeSearchCriteria(criteria));
		return Paging.valueOf(pagingCriteria, dtos, response.getResultsFound());
    
    }
    
    @Override
    public void validateActivationCode(final ActivationCode activationCode) throws ServiceLayerException {
    	Calendar cal = Calendar.getInstance();
    	Date date = cal.getTime();    	
    	DateTime dateTime = new DateTime(date);
        if(activationCode.getAllowedUsage() != null && activationCode.isUsageLimitReached()) {
            throw new ServiceLayerException("Activation code: " + activationCode.getCode() + " Allowed usage is: " + activationCode.getAllowedUsage() + 
            " Actual usage is: " + activationCode.getActualUsage(), 
            new Message("error.problem.activating.token","There was a problem with your activation code."));
        }
        if (activationCode.getActivationCodeBatch().getStartDate() != null && activationCode.getActivationCodeBatch().getStartDate().toDateTime().isAfter(dateTime)) {
            throw new ServiceLayerException("The activation code: " + activationCode.getCode() + " may only be consumed after " + activationCode.getActivationCodeBatch().getStartDate(), new Message("error.problem.activating.token",
            "There was a problem with your activation code."));
        }
        if (activationCode.getActivationCodeBatch().getEndDate() != null && activationCode.getActivationCodeBatch().getEndDate().toDateTime().isBefore(dateTime)) {
            throw new ServiceLayerException("The activation code: " + activationCode.getCode() + " may only be consumed before " + activationCode.getActivationCodeBatch().getEndDate().toDateMidnight(), new Message("error.problem.activating.token",
            "There was a problem with your activation code."));
        }
    }

    @Override
    public ActivationCodeBatch getActivationCodeBatchByDbId(String id) {
        return this.activationCodeBatchDao.getActivationCodeBatchByBatchDbId(id);
    }

    @Override
    public void updateActivationCodeBatch(ActivationCodeBatch activationCodeBatch,String currentBatchId) 
    		throws ServiceLayerException, ErightsException {
        //make sure that both the licence template and the batch are marked as updated in db. 
    	ActivationCodeBatchDto activationCodeBatchDto = null;
        
			activationCodeBatchDto = erightsFacade.getActivationCodeBatch(currentBatchId,false);
			
			Product product = activationCodeBatch.getActivationCodeRegistrationDefinition().getProduct();
	        EacGroups eacGroup = activationCodeBatch.getActivationCodeRegistrationDefinition().getEacGroup();
	       	        
	        if(eacGroup != null){
	        	ProductGroupDto productGroup = erightsFacade.getProductGroup(null, eacGroup.getGroupName());
				activationCodeBatchDto.setProductGroupId(productGroup.getProductGroupId());
				//De-duplication
				//registrationDefinitionDao.saveOrUpdate(activationCodeBatch.getActivationCodeRegistrationDefinition());
			} else {
				activationCodeBatchDto.setProductGroupId(null);
			}
	    	DateTime now = new DateTime();
	        activationCodeBatch.setUpdatedDate(now);
	        activationCodeBatch.getLicenceTemplate().setUpdatedDate(now);
	        //De-duplication
	        //this.activationCodeBatchDao.update(activationCodeBatch);
	  
	        activationCodeBatchDto.mergeActivationCodebatchChanges(activationCodeBatch);
			erightsFacade.updateActivationCodeBatch(activationCodeBatchDto);
	        if(product != null){
	        	AuditLogger.logEvent("Updated ActivationCodeBatch", "batchId:"+activationCodeBatch.getBatchId(), AuditLogger.product(product));
	        }else{
	        	AuditLogger.logEvent("Updated ActivationCodeBatch", "batchId:"+activationCodeBatch.getBatchId(), AuditLogger.eacGroup(eacGroup));
	        }      
        
    }
    
    @Override
    public boolean hasActivationCodeBatchBeenUsed(String activationCodeBatchDbId) 
    		throws ServiceLayerException, ProductNotFoundException, UserNotFoundException, LicenseNotFoundException, 
    		AccessDeniedException, GroupNotFoundException, ErightsException {
		ActivationCodeBatchDto activationCodeBatchDto = erightsFacade
				.getActivationCodeBatch(activationCodeBatchDbId, false);
		// activationCodeBatchDto.getAllowedUsages();
		// ActivationCodeBatchDto acbDto =
		// erightsFacade.getActivationCodeDetailsByActivationCode(activationCodeBatchDto.getActivationCodes())
		// ;

		List<String> codes = activationCodeBatchDto.getActivationCodes();
		if (activationCodeBatchDto.getAddedInPool().equalsIgnoreCase(
			AddedInPool.NONE.toString())) {
				
			int used = 0;

			for (String code : codes) {
				ActivationCodeBatchDto acbDto = erightsFacade
						.getActivationCodeDetailsByActivationCode(code);
				if (acbDto.getActualUsage() > 0) {
					used = used + 1;
				}

			}
			if (used <= 0) {
				return false;
			} else {
				return true;
			}
		} else {
			return true;
		}

		/*
		 * ActivationCodeRegistration activationCodeRegistration =
		 * activationCodeBatchDto.getActivationCodeRegistration(); if
		 * (activationCodeRegistration != null) { return false; } return true;
		 */
		// de-duplication ACB
		// return
		// this.activationCodeBatchDao.isBatchUsed(activationCodeBatchDbId);
	}

    @Override
    @PreAuthorize("hasRole('ROLE_DELETE_ACTIVATION_CODE_BATCH')")
    public void deleteUnusedActivationCodeBatch(String activationCodeBatchId) throws ServiceLayerException, 
    ProductNotFoundException, UserNotFoundException, LicenseNotFoundException, AccessDeniedException, 
    GroupNotFoundException, ErightsException {
        /*ActivationCodeBatch batch = getActivationCodeBatchByDbId(activationCodeBatchId);
        boolean used = hasActivationCodeBatchBeenUsed(batch.getBatchId());
        if (used) {
            throw new ServiceLayerException(String.format("Used batch [%s] cannot be deleted.", batch.getBatchId()));
        }*/
        try {
			erightsFacade.deleteActivationCodeBatch(activationCodeBatchId);
		} catch (AccessDeniedException | ErightsException e) {
			// TODO Auto-generated catch block
			throw new ServiceLayerException(e.getMessage());
		}
        //de-duplication
        /*boolean archived = this.activationCodeBatchDao.archiveBatch(batch);
        if (!archived) {
            throw new ServiceLayerException(String.format("batch [%s] cannot be deleted.", batch.getBatchId()));
        }*/
        AuditLogger.logEvent("Deleted ActivationCodeBatch", "batchId:"+activationCodeBatchId, AuditLogger.activationCode(activationCodeBatchId));
    }

    @Override
    public int getNumberOfTokensInBatch(String activationCodeBatchId) throws ServiceLayerException {
        ActivationCodeBatchDto activationCodeBatchDto = new ActivationCodeBatchDto();
    	try {
    		activationCodeBatchDto = erightsFacade.getActivationCodeBatch(activationCodeBatchId,false);
    		
		} catch (AccessDeniedException | ErightsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	int noOfTokens = activationCodeBatchDto.getNumberOfTokens();
    	
    	return noOfTokens;
    	//De-duplication
    	//return this.activationCodeBatchDao.getNumberOfTokensInBatch(activationCodeBatchId);
    }

    @Override
    public boolean doesArchivedBatchExist(String batchName)  {
        return this.activationCodeBatchDao.doesArchivedBatchExist(batchName);
        
    }

	@Override
	public ActivationCode getActivationCodeFullDetails(String activationCode) {
		return activationCodeDao.getActivationCodeFullDetails(activationCode);
	}
	
	/**
	 * @return
	 */
	public AmazonCloudSearchDomainClient createClient() {
		AWSCredentials awsCredentials = new BasicAWSCredentials(AWS_ACCESSKEY, AWS_SECRETKEY);
		AmazonCloudSearchDomainClient cs = new AmazonCloudSearchDomainClient(awsCredentials).withRegion(Regions.EU_WEST_1);
		return cs;
	}
	
	public LicenceTemplate getLicenseTemplate(LicenceDetailDto licenceDetailDto) {
		LicenceTemplate licenceTemplate = null;		
		RollingLicenceDetailDto rollingLicenceDetailDto = null;
		UsageLicenceDetailDto usageLicenceDetailDto = null;
		StandardConcurrentLicenceDetailDto standardConcurrentLicenceDetailDto = null;		
		RollingLicenceTemplate rollingLicenceTemplate = new RollingLicenceTemplate();
		UsageLicenceTemplate usageLicenceTemplate = new UsageLicenceTemplate();
		ConcurrentLicenceTemplate concurrentLicenceTemplate = new ConcurrentLicenceTemplate();
				
		if (licenceDetailDto != null && licenceDetailDto.getClass().getSimpleName().toString().equals(
				RollingLicenceDetailDto.class.getSimpleName().toString())) {			
			rollingLicenceDetailDto =(RollingLicenceDetailDto) licenceDetailDto; 
			rollingLicenceTemplate.setBeginOn(rollingLicenceDetailDto.getBeginOn());
			rollingLicenceTemplate.setEndDate(rollingLicenceDetailDto.getEndDate());
			rollingLicenceTemplate.setStartDate(rollingLicenceDetailDto.getStartDate());
			rollingLicenceTemplate.setTimePeriod(rollingLicenceDetailDto.getTimePeriod());
			rollingLicenceTemplate.setUnitType(rollingLicenceDetailDto.getUnitType());
			licenceTemplate = rollingLicenceTemplate;			
		}
		if (licenceDetailDto != null && licenceDetailDto.getClass().getSimpleName().toString().equals(
				UsageLicenceDetailDto.class.getSimpleName().toString())) {
			usageLicenceDetailDto = (UsageLicenceDetailDto) licenceDetailDto; 
			
			usageLicenceTemplate.setAllowedUsages(usageLicenceDetailDto.getAllowedUsages());
			usageLicenceTemplate.setStartDate(usageLicenceDetailDto.getStartDate());
			usageLicenceTemplate.setEndDate(usageLicenceDetailDto.getEndDate());
			//usageLicenceDetailDto.getUsagesRemaining()
			licenceTemplate = usageLicenceTemplate;
		}
		if (licenceDetailDto != null && licenceDetailDto.getClass().getSimpleName().toString().equals(
				StandardConcurrentLicenceDetailDto.class.getSimpleName().toString())) {
			standardConcurrentLicenceDetailDto = (StandardConcurrentLicenceDetailDto) licenceDetailDto;
			
			concurrentLicenceTemplate.setStartDate(standardConcurrentLicenceDetailDto.getStartDate());
			concurrentLicenceTemplate.setEndDate(standardConcurrentLicenceDetailDto.getEndDate());
			concurrentLicenceTemplate.setTotalConcurrency(standardConcurrentLicenceDetailDto.getTotalConcurrency());
			concurrentLicenceTemplate.setUserConcurrency(standardConcurrentLicenceDetailDto.getUserConcurrency());
			
			licenceTemplate = concurrentLicenceTemplate;
			
		}
		return licenceTemplate;
	}
	
	private Product convertEnforceableProductToProduct(EnforceableProductDto enfoProduct) {
		RegisterableProduct regProduct = new RegisterableProduct();
		regProduct.setProductName(enfoProduct.getName());
		regProduct.getExternalIds().addAll(enfoProduct.getExternalIds());
		regProduct.setEmail(enfoProduct.getAdminEmail());
		
		/*for(LinkedProductNew linked : enfoProduct.getLinkedProducts())
			regProduct.setLinkedProduct(linked);*/
		regProduct.setId(enfoProduct.getProductId());
		return regProduct;
	}
	
	@Override
	public List<GuestRedeemActivationCodeDto> guestRedeemActivationCode(String activationCode) throws ErightsException {
		return erightsFacade.guestRedeemActivationCode( activationCode ) ;
	}
    
	@Override
	public final ActivationCodeBatch saveActivationCodeBatchAsync(
			final ActivationCodeBatch activationCodeBatch,
			final ActivationCodeRegistrationDefinition activationCodeRegistrationDefinition,
			final int numTokens, final int allowedUsage, final String productGroupId,
			final String adminEmail, final String adminName)
			throws ServiceLayerException, AccessDeniedException,
			ErightsException {
		activationCodeBatch
				.setActivationCodeRegistrationDefinition(activationCodeRegistrationDefinition);
		// de-duplication of ACB
		/*
		 * if(activationCodeRegistrationDefinition.getEacGroup() != null){
		 * registrationDefinitionDao
		 * .saveOrUpdate(activationCodeRegistrationDefinition); }
		 */
		// activationCodeBatchDao.saveOrUpdate(activationCodeBatch);

		if (activationCodeRegistrationDefinition.getProduct() != null
				&& activationCodeRegistrationDefinition.getProduct().getId() != null) {
			saveActivationTokensAsync(activationCodeBatch, numTokens, allowedUsage,
					activationCodeRegistrationDefinition.getProduct().getId(),
					null,adminEmail,adminName);
		} else {

			saveActivationTokensAsync(activationCodeBatch, numTokens, allowedUsage,
					null, productGroupId, adminEmail,adminName);

		}
		if (activationCodeRegistrationDefinition.getEacGroup() != null) {
			AuditLogger.logEvent("Saved ActivationCodeBatch", "batchId:"
					+ activationCodeBatch.getBatchId(), AuditLogger
					.eacGroup(activationCodeRegistrationDefinition
							.getEacGroup()), "numTokens:" + numTokens,
					"allowedUsage:" + allowedUsage);
		} else {
			AuditLogger.logEvent("Saved ActivationCodeBatch", "batchId:"
					+ activationCodeBatch.getBatchId(),
					AuditLogger.product(activationCodeRegistrationDefinition
							.getProduct()), "numTokens:" + numTokens,
					"allowedUsage:" + allowedUsage);
		}
		return activationCodeBatch;
	}
	private String getEmailText() {
        final Map<String, Object> templateParams = new HashMap<String, Object>();
        templateParams.put("resource", getResource(Locale.getDefault()));
        templateParams.put("adminName", ADMIN_NAME);
        return VelocityUtils.mergeTemplateIntoString(velocityEngine, EMAIL_TEMPLATE, templateParams);
    }
	
	private MessageTextSource getResource(final Locale locale) {
        MessageTextSource result = new MessageTextSource(this.messageSource, locale);
        return result;
    }
	
	private void zipIt(byte[] unformattedData, byte[] formattedData, String unformatedEntryFile, String formatedEntryFile, String outputFile){
        try{
            FileOutputStream fos = new FileOutputStream(outputFile);
            ZipOutputStream zos = new ZipOutputStream(fos);
    
            ZipEntry ze= new ZipEntry(unformatedEntryFile);
            zos.putNextEntry(ze);
    
            zos.write(unformattedData);
            
            ZipEntry ze1= new ZipEntry(formatedEntryFile);
            zos.putNextEntry(ze1);
    
            zos.write(formattedData);
            zos.closeEntry();
            //remember close it            
            zos.close();
        }catch(IOException ex){
            ex.printStackTrace();
            LOGGER.error(ex.getMessage(), ex);
        }
    }
	
	public final ActivationCode getActivationCodeWithDetailsWS(final String id) throws ProductNotFoundException, UserNotFoundException, 
    LicenseNotFoundException, AccessDeniedException, GroupNotFoundException, ErightsException, ServiceLayerException {
    	ActivationCodeBatchDto activationCodeBatchDto = new ActivationCodeBatchDto();
    	ActivationCode activationCode = new ActivationCode(); 
    	
		
		activationCodeBatchDto = erightsFacade.getActivationCodeBatchByActivationCode(id);
		
		LicenceDetailDto licenceDetailDto = activationCodeBatchDto.getLicenceDetailDto();
		LicenceTemplate licenceTemplate = getLicenseTemplate(licenceDetailDto);
		
		Customer customer = new Customer();
		if (activationCodeBatchDto.getActivationCodeRegistration() != null && 
				activationCodeBatchDto.getActivationCodeRegistration().getCustomer() != null) {
			customer = activationCodeBatchDto.getActivationCodeRegistration().getCustomer();
		}
		 
		ActivationCodeRegistration activationCodeRegistration = new ActivationCodeRegistration();
				//activationCodeBatchDto.getActivationCodeRegistration();
		ActivationCodeRegistrationDefinition activationCodeRegistrationDefinition = 
				new ActivationCodeRegistrationDefinition();
		Set<ActivationCodeRegistration> activationCodeRegistrations = 
				new HashSet<ActivationCodeRegistration>();
		EnforceableProductDto enforceableProductDto = null ;
		EacGroups eacGroups = null ;
		Product product = null ;
		if (activationCodeBatchDto.getProductGroupId() != null) {
			ProductGroupDto productGroupDto = 
					eacGrpService.getProductGroupDtoByErightsId(activationCodeBatchDto.getProductGroupId());
			eacGroups = eacGrpService.getEacGroupByProductGroupDto(productGroupDto);
			activationCodeRegistrationDefinition.setEacGroup(eacGroups);
		} else {
			enforceableProductDto = 
					productService.getEnforceableProductByErightsId(activationCodeBatchDto.getProductId());
			product = new RegisterableProduct();
			product.setId(enforceableProductDto.getProductId());
			//activationCodeRegistrationDefinition = registrationDefinitionDao.getActivationCodeRegistrationDefinitionByProduct(product);
			Set<ExternalProductId> externalProductIds = null;
			if(enforceableProductDto.getExternalIds()!=null && enforceableProductDto.getExternalIds().size()>0){
				externalProductIds = new HashSet<ExternalProductId>();
				List<ExternalProductId> externalProductIdList = enforceableProductDto.getExternalIds();
				if (externalProductIdList != null) {
					for (ExternalProductId externalProductId : externalProductIdList) {
						externalProductIds.add(externalProductId);
					}
				}
			}	
			product.setActivationStrategy(enforceableProductDto.getActivationStrategy());
			product.setProductUrls(enforceableProductDto.getUrls());
			product.setId(enforceableProductDto.getProductId());
			product.setProductName(enforceableProductDto.getName());
			product.setExternalIds(externalProductIds);
			product.setEmail(enforceableProductDto.getAdminEmail());
			product.setHomePage(enforceableProductDto.getHomePage());
			product.setLandingPage(enforceableProductDto.getLandingPage());
			product.setServiceLevelAgreement(enforceableProductDto.getSla());
			product.setValidatorEmail(enforceableProductDto.getValidatorEmail());
			//product.setState("");
			activationCodeRegistrationDefinition.setProduct(product);
		}
		//activationCodeRegistrationDefinition.setRegistrationActivation(registrationActivation);
		activationCodeRegistrationDefinition.setLicenceTemplate(licenceTemplate);
		activationCodeRegistrationDefinition.setVersion(activationCodeBatchDto.getVersion());
		
		activationCode.setCode(id);
		activationCodeRegistration.setActivationCode(activationCode); 	
		activationCodeRegistration.setCustomer(customer);
		activationCodeRegistration.setRegistrationDefinition(activationCodeRegistrationDefinition);
		activationCodeRegistrations.add(activationCodeRegistration);
		//activationCodeRegistrations.addAll(activationCodeBatchDto.getActivationCodeRegistrations());
		
		ActivationCodeBatch activationCodeBatch = getActivationCodeBatchDetailsByCodeWS(activationCodeBatchDto,product,eacGroups);
		if (enforceableProductDto != null ){
			activationCodeBatch.getActivationCodeRegistrationDefinition().setConfirmationEmailEnabled(enforceableProductDto.getConfirmationEmailEnabled());
		}
		activationCode.setActivationCodeBatch(activationCodeBatch);
		activationCode.setActualUsage(activationCodeBatchDto.getActualUsage());
		activationCode.setAllowedUsage(activationCodeBatchDto.getAllowedUsages());
		activationCode.setCode(id);
		activationCode.setRegistrations(activationCodeRegistrations);
		activationCode.setVersion(activationCodeBatchDto.getVersion());
    	
    	return activationCode;
    	//De-duplication
    	//return activationCodeDao.getActivationCodeWithDetails(id);
    }
	
	private ActivationCodeBatch getActivationCodeBatchDetailsByCodeWS(
			ActivationCodeBatchDto activationCodeBatchDto, Product product, EacGroups eacGroup) {

		ActivationCodeBatch activationCodeBatch = new ActivationCodeBatch();
		/*ActivationCodeBatchDto activationCodeBatchDto = erightsFacade
				.getActivationCodeBatchByActivationCode(activationCode);*/

		DateTime startDate = null;
		DateTime endDate = null;
		DateTime createdDate = null;
		DateTime updatedDate = null;

		if (activationCodeBatchDto.getValidFrom() != null) {
			startDate = new DateTime(activationCodeBatchDto.getValidFrom()
					.toString());
		}
		if (activationCodeBatchDto.getValidTo() != null) {
			endDate = new DateTime(activationCodeBatchDto.getValidTo()
					.toString());
		}
		if (activationCodeBatchDto.getCreatedDate() != null) {
			createdDate = new DateTime(activationCodeBatchDto.getCreatedDate()
					.toString());
		}
		if (activationCodeBatchDto.getUpdatedDate() != null) {
			updatedDate = new DateTime(activationCodeBatchDto.getUpdatedDate()
					.toString());
		}

		if (activationCodeBatchDto.getCodeFormat() == CODE_FORMAT) {
			activationCodeBatch
					.setActivationCodeFormat(ActivationCodeFormat.EAC_NUMERIC);
		}

		LicenceDetailDto licenceDetailDto = activationCodeBatchDto
				.getLicenceDetailDto();
		LicenceTemplate licenceTemplate = getLicenseTemplate(licenceDetailDto);
		List<String> codes = activationCodeBatchDto.getActivationCodes();

		List<ActivationCode> activationCodeList = new ArrayList<ActivationCode>();
		for (String code : codes) {
			ActivationCode activationCodes = new ActivationCode();
			activationCodes.setCode(code);
			activationCodeList.add(activationCodes);
		}

		ActivationCodeRegistrationDefinition activationCodeRegistrationDefinition = null;
		if (product != null) {
			Product eacProduct = new RegisterableProduct();
			eacProduct.setId(product.getId());
			activationCodeRegistrationDefinition = registrationDefinitionDao
					.getRegistrationDefinitionByProduct(
							ActivationCodeRegistrationDefinition.class,
							eacProduct);
		}

		if (activationCodeRegistrationDefinition == null) {
			activationCodeRegistrationDefinition = new ActivationCodeRegistrationDefinition();
		}

		if (licenceTemplate != null) {
			activationCodeRegistrationDefinition
					.setLicenceTemplate(licenceTemplate);
			if (product != null) {
				activationCodeRegistrationDefinition.setProduct(product);
			} else if (eacGroup != null) {
				activationCodeRegistrationDefinition.setEacGroup(eacGroup);
			}

		}

		activationCodeRegistrationDefinition.setVersion(activationCodeBatchDto
				.getVersion());

		activationCodeBatch
				.setActivationCodeRegistrationDefinition(activationCodeRegistrationDefinition);
		if (activationCodeList != null) {
			activationCodeBatch.setActivationCodes(activationCodeList);
		}
		if (activationCodeBatchDto.getBatchId() != null) {
			activationCodeBatch.setBatchId(activationCodeBatchDto.getBatchId());
		}
		if (activationCodeBatchDto.getCodePrefix() != null) {
			activationCodeBatch.setCodePrefix(activationCodeBatchDto
					.getCodePrefix());
		}
		if (activationCodeBatchDto.getNumberOfTokens() != 0) {
			activationCodeBatch.setNumberOfTokens(activationCodeBatchDto
					.getNumberOfTokens());
		}
		if (createdDate != null) {
			activationCodeBatch.setCreatedDate(createdDate);
		}
		if (endDate != null) {
			activationCodeBatch.setEndDate(endDate);
		}
		if (activationCodeBatchDto.getCurrentBatchId() != null) {
			activationCodeBatch.setId(activationCodeBatchDto
					.getCurrentBatchId());
		}
		if (licenceTemplate != null) {
			activationCodeBatch.setLicenceTemplate(licenceTemplate);
		}
		if (startDate != null) {
			activationCodeBatch.setStartDate(startDate);
		}
		if (updatedDate != null) {
			activationCodeBatch.setUpdatedDate(updatedDate);
		}
		if ("" + activationCodeBatchDto.getVersion() != null) {
			activationCodeBatch.setVersion(activationCodeBatchDto.getVersion());
		}

		return activationCodeBatch;
	}
	
	private final byte[] getActivationCodeFileFormatted(final List<String> activationCodes, final boolean format) throws UnsupportedEncodingException   {
		StringBuilder stringBuilder = new StringBuilder();
		for(String activationCode : activationCodes) {
			if(stringBuilder.length() > 0) {
				stringBuilder.append("\r\n");
			}
			if(format) {
				stringBuilder.append(EACStringUtils.format(activationCode, 4, '-') );
			} else {
				stringBuilder.append(activationCode);
			}
		}
		return stringBuilder.toString().getBytes("UTF-8");
	}

}
