package com.oup.eac.service.util;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.web.util.HtmlUtils;

import com.amazonaws.services.cloudsearchdomain.model.Hit;
import com.amazonaws.services.cloudsearchdomain.model.Hits;
import com.constants.SearchDomainFields;
import com.oup.eac.cloudSearch.util.CloudSearchUtils;
import com.oup.eac.cloudSearch.util.CloudSearchUtils.DomainType;
import com.oup.eac.domain.ActivationCode;
import com.oup.eac.domain.ActivationCodeBatch;
import com.oup.eac.domain.ActivationCodeRegistration;
import com.oup.eac.domain.ActivationCodeRegistrationDefinition;
import com.oup.eac.domain.ActivationCodeSearchDto;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.EacGroups;
import com.oup.eac.domain.ExternalCustomerId;
import com.oup.eac.domain.ExternalProductId;
import com.oup.eac.domain.ExternalSystem;
import com.oup.eac.domain.ExternalSystemIdType;
import com.oup.eac.domain.GroupRestDto;
import com.oup.eac.domain.LicenceTemplate;
import com.oup.eac.domain.Product;
import com.oup.eac.domain.RegisterableProduct;
import com.oup.eac.domain.RegistrationActivation.ActivationStrategy;
import com.oup.eac.domain.UserGroupMembershipDto;
import com.oup.eac.domain.beans.ProductBean;
import com.oup.eac.domain.search.AmazonSearchResponse;
import com.oup.eac.dto.ActivationCodeReportDto;

public class ConvertSearch {

	public List<Customer> convertCustomer(AmazonSearchResponse response) {

		List<Map<String, String>> fields = response.getResultFields();
		List<Customer> customers = new ArrayList<Customer>();
		for (Map<String , String> field: fields){
			Customer customer = new Customer();
			if(field.get("username")!=null){
				String userName = field.get("username");
				userName =removeSquareBracket(userName);
				customer.setUsername(userName);
			}
			if(field.get("lastname")!=null){
				String lastName = field.get("lastname");
				lastName =removeSquareBracket(lastName);
				customer.setFamilyName(lastName);
			}
			if(field.get("firstname")!=null){
				String firstName = field.get("firstname");
				firstName =removeSquareBracket(firstName);
				customer.setFirstName(firstName);
			}
			/*if(field.get("lastname")!=null)
				customer.set(field.get("lastname"));*/
			if(field.get("emailaddress")!=null){
				String emailAddress = field.get("emailaddress");
				emailAddress =removeSquareBracket(emailAddress);
				customer.setEmailAddress(emailAddress);
			}
			if(field.get("licensecount")!=null){
				String licenseCount = field.get("licensecount");
				licenseCount=removeSquareBracket(licenseCount);
				customer.setLicenseCount(convertToInteger(licenseCount));
			}
			if(field.get("userid")!=null){
				String userId= field.get("userid"); 
				userId =removeSquareBracket(userId);
				customer.setId(userId);
			}
			if(field.get("createddate")!=null){
				String createdDate = field.get("createddate");
				createdDate =removeSquareBracket(createdDate);
				customer.setCreatedDate(DateTime.parse(createdDate));
			}
			
			
			if(field.get("lastlogin")!=null){
				String lastLoginDt = field.get("lastlogin");
				lastLoginDt = removeSquareBracket(lastLoginDt);
				customer.setLastLoginDateTime(DateTime.parse(lastLoginDt));
			}
			customers.add(customer);
		}
		return customers;
	}
	
	public List<ProductBean> convertProduct(AmazonSearchResponse response, final Map<String,String> adminDivisionList) {
		List<Map<String, String>> fields = response.getResultFields();
		List<ProductBean> products = new ArrayList<ProductBean>();
		for (Map<String , String> field: fields){
			ProductBean product = new ProductBean();
			if(field.get("eacid")!=null){
				String eacId = field.get("eacid");
				eacId =removeSquareBracket(eacId);
				product = new ProductBean(eacId) ;
			}
			if(field.get("productname")!=null){
				String productName = field.get("productname");
				System.out.println("name              " + productName);
				productName =removeSquareBracket(productName);
				product.setProductName(productName);
			}
			if(field.get("activationmethod")!=null){
				String activationMethod = field.get("activationmethod");
				activationMethod =removeSquareBracket(activationMethod);
				if(activationMethod.equalsIgnoreCase(ActivationStrategy.INSTANT.toString())){
					activationMethod = "InstantRegistrationActivation" ;
				} else if(activationMethod.equalsIgnoreCase(ActivationStrategy.SELF.toString())){
					activationMethod = "SelfRegistrationActivation" ;
				} else if(activationMethod.equalsIgnoreCase(ActivationStrategy.VALIDATED.toString())){
					activationMethod = "ValidatedRegistrationActivation" ;
				}  
				product.setActivationStrategy(activationMethod);
				//product.setAc
			}
			if(field.get("registrabletype")!=null){
				String registrableType = field.get("registrabletype");
				registrableType =removeSquareBracket(registrableType);
				product.setRegistrationType(registrableType);
			}
			if(field.get("productid")!=null){
				String productId = removeSquareBracket(field.get("productid"));
				
				product.setProductId(productId);
			}
			if(field.get("divisionid")!=null){
				String divisionid= field.get("divisionid"); 
				divisionid =removeSquareBracket(divisionid);
				product.setDivisionType(adminDivisionList.get(divisionid));
			}
			if(field.get(SearchDomainFields.PRODUCT_PRODUCTGROUPIDS)!=null){
				String productGroupIds= field.get(SearchDomainFields.PRODUCT_PRODUCTGROUPIDS); 
				productGroupIds =removeSquareBracket(productGroupIds);
				Set<String> groupIds = new HashSet<>();
				for(String s:productGroupIds.split(",")){
					groupIds.add(s);
				}
				product.setProductGroupIds(groupIds);
			}
			products.add(product);
		}
		return products;
	}
	
	/**
	 * convertLicense
	 * @param response
	 * @param systemId
	 * @return
	 * List<ActivationCodeRegistration>
	 * @author Developed by TCS 
	 */
	public List<ActivationCodeRegistration> convertLicense(AmazonSearchResponse response, String systemId) {
		
		List<Map<String, String>> fields = response.getResultFields();
		List<ActivationCodeRegistration> registrations = new ArrayList<>();
		ActivationCodeRegistration registration = null;
		
		for (Map<String , String> field: fields){
			registration = new ActivationCodeRegistration();
			String userId = null;
			String redeemDate = null;
			Customer customer = null;
			
			if(field.get(SearchDomainFields.LICENSE_USER_ID) != null) {
				
				userId = field.get(SearchDomainFields.LICENSE_USER_ID);
				userId = removeSquareBracket(userId);
				
				Hits hitsResult = new CloudSearchUtils(DomainType.USER)
				.searchDocument(SearchDomainFields.USER_USERID, "'" + userId + "'");
				customer = getCustomerFromHits(hitsResult, systemId);
				
			}
			
			if(field.get(SearchDomainFields.LICENSE_CREATED_DATE) != null) {
				redeemDate = field.get(SearchDomainFields.LICENSE_CREATED_DATE);
				redeemDate = removeSquareBracket(redeemDate);
				registration.setCreatedDate(new DateTime(redeemDate, DateTimeZone.UTC));
			}
			if(customer !=null && customer.getId() != null){
				registration.setCustomer(customer);
				registrations.add(registration);
			}
		}
		return registrations;
	}
	
	/**
	 * convertActivationCodeSearch
	 * @param response
	 * @param systemId
	 * @return
	 * List<ActivationCodeSearchDto>
	 * @author Developed by TCS
	 */
	public List<ActivationCodeSearchDto> convertActivationCodeSearch(AmazonSearchResponse response,String systemId) {

		List<Map<String, String>> fields = response.getResultFields();
		List<ActivationCodeSearchDto> activationCodes = new ArrayList<ActivationCodeSearchDto>();
		ActivationCodeSearchDto activationCode;		
		for (Map<String , String> field: fields){
			activationCode = new ActivationCodeSearchDto();
			String code = null;
			String productId = null;
			String productGroupId = null;
			String availableState = null;		
			Integer allowedUsage = 0;
	    	Integer actualUsage = 0;
			
			if(field.get(SearchDomainFields.CLAIM_TICKET_ACTIVATIONCODE) != null) {
				code = field.get(SearchDomainFields.CLAIM_TICKET_ACTIVATIONCODE);
				code = removeSquareBracket(code);
				activationCode.setCode(code);
			}
			if(field.get(SearchDomainFields.CLAIM_TICKET_ALLOWEDUSAGES) != null) {
				String allowedUsags = field.get(SearchDomainFields.CLAIM_TICKET_ALLOWEDUSAGES);
				allowedUsags = removeSquareBracket(allowedUsags);
				allowedUsage = convertToInteger(allowedUsags);
				activationCode.setAllowedUsage(allowedUsage);
			}
			/*if(field.get(SearchDomainFields.CLAIM_TICKET_AVAILABLESTATE) != null) {
				availableState = field.get(SearchDomainFields.CLAIM_TICKET_AVAILABLESTATE);
				availableState = availableState.replaceAll("\\[", "").replaceAll("\\]","");
				if (availableState.equals("AVAILABLE")) {
					actualUsage = allowedUsage - 1;					
				} else if (availableState == "UNAVAILABLE") {
					actualUsage = allowedUsage;
				}				
			}*/
			if(field.get(SearchDomainFields.CLAIM_TICKET_ACTUAL_USAGES) != null) {
				String actualUsags = field.get(SearchDomainFields.CLAIM_TICKET_ACTUAL_USAGES);
				actualUsags = removeSquareBracket(actualUsags);
				actualUsage = convertToInteger(actualUsags);
				activationCode.setActualUsage(actualUsage);			
			}
			ActivationCodeBatch activationCodeBatch = new ActivationCodeBatch();
			if(field.get(SearchDomainFields.CLAIM_TICKET_STARTDATE) != null) {
				String startDate = field.get(SearchDomainFields.CLAIM_TICKET_STARTDATE);
				startDate = removeSquareBracket(startDate);
				activationCodeBatch.setStartDate(new DateTime(startDate, DateTimeZone.UTC));
			}			
			if(field.get(SearchDomainFields.CLAIM_TICKET_ENDDATE) != null) {
				String endDate = field.get(SearchDomainFields.CLAIM_TICKET_ENDDATE);
				endDate = removeSquareBracket(endDate);
				activationCodeBatch.setEndDate(new DateTime(endDate, DateTimeZone.UTC));
			}
			activationCode.setActivationCodeBatch(activationCodeBatch);
			List<Product> products = null;
			// For product Group scenario
			/*
			if(field.get(SearchDomainFields.CLAIM_TICKET_PRODUCTGROUPID) != null) {
				productGroupId = field.get(SearchDomainFields.CLAIM_TICKET_PRODUCTGROUPID);
				productGroupId = productGroupId.replaceAll("\\[", "").replaceAll("\\]","");
				Hits hitsResult = new CloudSearchUtils(DomainType.PRODUCT)
				.searchDocument(SearchDomainFields.PRODUCT_PRODUCTGROUPIDS, "'" + productGroupId + "'");
				if (hitsResult.getFound() != 0) {
					products = getProductsFromHits(hitsResult, systemId);
				}
			} else
			*/
			if(field.get(SearchDomainFields.CLAIM_TICKET_PRODUCTID) != null) {
				productId = field.get(SearchDomainFields.CLAIM_TICKET_PRODUCTID);
				productId = removeSquareBracket(productId);
				Hits hitsResult = new CloudSearchUtils(DomainType.PRODUCT).searchDocument(SearchDomainFields.PRODUCT_PRODUCTID, "'" + productId + "'");
				products = getProductsFromHits(hitsResult, systemId);
			}
			if(products != null && products.size()>0){
				activationCode.setProduct(products.get(0));
				activationCodes.add(activationCode);					
			}
		}
		return activationCodes;
	}
	
	/**
	 * getCustomerFromHits
	 * @param hits
	 * @param systemId
	 * @return
	 * Customer
	 * @author Developed by TCS
	 */
	private Customer getCustomerFromHits(Hits hits, String systemId) {

		Customer customer = new Customer();
		Set<ExternalCustomerId> externalCustomerIds = new HashSet<>();
		String eacId = null;

		if (hits != null && hits.getFound() > 0) {
			for (Hit hit : hits.getHit()) {
				Map<String, List<String>> keys = hit.getFields();
				eacId = keys.get(SearchDomainFields.USER_USERID).get(0);
				eacId = removeSquareBracket(eacId);

				if (keys.get(SearchDomainFields.USER_EXTERNALSYSTEMS) != null
						&& keys.get(SearchDomainFields.USER_EXTERNALSYSTEMS)
								.size() > 0) {
					for (String temp : keys
							.get(SearchDomainFields.USER_EXTERNALSYSTEMS)) {

						String[] externalId = temp.split("~");
						if (systemId == null
								|| (systemId != null && systemId
										.equalsIgnoreCase(externalId[1]))) {
							ExternalCustomerId externalCustomerId = new ExternalCustomerId();
							ExternalSystemIdType externalSystemIdType = new ExternalSystemIdType();
							ExternalSystem externalSystem = new ExternalSystem();

							externalSystem.setName(externalId[1]);
							externalSystemIdType
									.setExternalSystem(externalSystem);
							externalSystemIdType.setName(externalId[2]);
							externalCustomerId.setExternalId(externalId[0]);

							externalCustomerId
									.setExternalSystemIdType(externalSystemIdType);

							externalCustomerIds.add(externalCustomerId);
						}
					}
				}
			}
		}

		customer.setId(eacId);
		customer.setExternalIds(externalCustomerIds);
		return customer;
	}
	
	/**
	 * adds External Id in the product if systemId is null or same as in product
	 * getProductsFromHits
	 * @param hits
	 * @param systemId
	 * @return
	 * List<Product>
	 * @author Developed by TCS
	 */
	private List<Product> getProductsFromHits(Hits hits, String systemId) {

		List<Product> products = new ArrayList<>();

		if (hits != null && hits.getFound() > 0) {
			for (Hit hit : hits.getHit()) {
				Map<String, List<String>> keys = hit.getFields();
				if (keys.get(SearchDomainFields.PRODUCT_PRODUCTID) != null) {
					String productId = keys.get(
							SearchDomainFields.PRODUCT_PRODUCTID).get(0);
					Set<ExternalProductId> productExternalIds = new HashSet<>();
					if (keys.get(SearchDomainFields.PRODUCT_EXTERNALSYSTEMS) != null
							&& keys.get(
									SearchDomainFields.PRODUCT_EXTERNALSYSTEMS)
									.size() > 0) {
						for (String temp : keys
								.get(SearchDomainFields.PRODUCT_EXTERNALSYSTEMS)) {

							String[] externalId = temp.split("~");
							if (systemId != null && systemId
											.equalsIgnoreCase(externalId[1])) {
								ExternalProductId externalProductId = new ExternalProductId();
								ExternalSystemIdType externalSystemIdType = new ExternalSystemIdType();
								ExternalSystem externalSystem = new ExternalSystem();

								externalSystem.setName(externalId[1]);
								externalSystemIdType
										.setExternalSystem(externalSystem);
								externalSystemIdType.setName(externalId[2]);
								externalProductId.setExternalId(externalId[0]);

								externalProductId
										.setExternalSystemIdType(externalSystemIdType);
								productExternalIds.add(externalProductId);
							}
						}
					}
					if (productId != null && !productId.equals("")) {
						Product product = new RegisterableProduct();
						product.setId(productId);
						if (productExternalIds.size() > 0) {
							product.setExternalIds(productExternalIds);
						}
						products.add(product);
					}
				}
			}
		}
		return products;
	}
	
	/**
	 * convertActivationCode
	 * @param response
	 * @return
	 * List<ActivationCodeReportDto>
	 * @author Developed by TCS
	 */ 
	public List<ActivationCodeReportDto> convertActivationCode(AmazonSearchResponse response) {

		List<Map<String, String>> fields = response.getResultFields();
		List<ActivationCodeReportDto> activationCodeReportDtos = new ArrayList<ActivationCodeReportDto>();
		ActivationCodeReportDto activationCodeReportDto;		
		for (Map<String , String> field: fields){
			ActivationCode activationCode = new ActivationCode();
			String code = null;
			String productId = null;
			String productGroupId = null;
			String availableState = null;
			String productName = null;
			String productGroupName = null;				
			Integer allowedUsage = 0;
	    	Integer actualUsage = 0;
			
			if(field.get(SearchDomainFields.CLAIM_TICKET_ACTIVATIONCODE) != null) {
				code = field.get(SearchDomainFields.CLAIM_TICKET_ACTIVATIONCODE);
				code = removeSquareBracket(code);
				activationCode.setCode(code);
			}
						
			if(field.get(SearchDomainFields.CLAIM_TICKET_PRODUCTID) != null) {
				productId = field.get(SearchDomainFields.CLAIM_TICKET_PRODUCTID);
				productId = removeSquareBracket(productId);				
				
				//ResourceBundle resourceBundle = ResourceBundle.getBundle("acesCloudSearch");
				//SEARCH_ENDPOINT = resourceBundle.getString("aws.product.search.endpoint");
								
				Hits hitsResult = new CloudSearchUtils(DomainType.PRODUCT).searchDocument(SearchDomainFields.PRODUCT_PRODUCTID, "'" + productId + "'");				//searchResult.getHits();					
				if (hitsResult.getFound() != 0) {
					Hit result = hitsResult.getHit().get(0);
					Map<String, List<String>> keys = result.getFields();				
					productName = keys.get(SearchDomainFields.PRODUCT_PRODUCTNAME).get(0);
				} 									
			}
			if(field.get(SearchDomainFields.CLAIM_TICKET_PRODUCTGROUPID) != null) {
				productGroupId = field.get(SearchDomainFields.CLAIM_TICKET_PRODUCTGROUPID);
				productGroupId = removeSquareBracket(productGroupId);
				
				/*ResourceBundle resourceBundle = ResourceBundle.getBundle("acesCloudSearch");
				SEARCH_ENDPOINT = resourceBundle.getString("aws.productgroup.search.endpoint");
				client.setEndpoint(SEARCH_ENDPOINT);*/
				/*client = new CloudSearchUtils(DomainType.PRODUCTGROUP).getCloudSearchClient();
								
				SearchRequest searchRequest = new SearchRequest();
				searchRequest.setQueryParser(QueryParser.Structured);
				searchRequest.setQuery(SearchDomainFields.PRODUCT_GRP_PRODUCTGROUPID + ":" + productGroupId);
				SearchResult searchResult = client.search(searchRequest);*/
				
				Hits hitsResult = new CloudSearchUtils(DomainType.PRODUCTGROUP).searchDocument(SearchDomainFields.PRODUCT_GRP_PRODUCTGROUPID, "'" + productGroupId + "'");				//searchResult.getHits();
				if (hitsResult.getFound() != 0) {
					Hit result = hitsResult.getHit().get(0);
					Map<String, List<String>> keys = result.getFields();				
					productGroupName = keys.get(SearchDomainFields.PRODUCT_GRP_PRODUCTGROUPNAME).get(0); 
				}					
				
			} 		
	    		
			if(field.get(SearchDomainFields.CLAIM_TICKET_ALLOWEDUSAGES) != null) {
				String allowedUsags = field.get(SearchDomainFields.CLAIM_TICKET_ALLOWEDUSAGES);
				allowedUsags = removeSquareBracket(allowedUsags);
				allowedUsage = convertToInteger(allowedUsags);
			}			
			
			if(field.get(SearchDomainFields.CLAIM_TICKET_AVAILABLESTATE) != null) {
				availableState = field.get(SearchDomainFields.CLAIM_TICKET_AVAILABLESTATE);
				availableState = removeSquareBracket(availableState);
				if (availableState.equals("AVAILABLE")) {					
					actualUsage = allowedUsage - 1;					
				} else if (availableState.equals("UNAVAILABLE")) {
					actualUsage = allowedUsage;
				}				
			}
			
			activationCodeReportDto = new ActivationCodeReportDto(null, code, productName, allowedUsage, 
					actualUsage, productGroupName);
			activationCodeReportDtos.add(activationCodeReportDto);					
		}
		return activationCodeReportDtos;
	}
	
/**
 * convertActivationCodeBatch
 * @param response
 * @return
 * List<ActivationCodeReportDto>
 * @author Developed by TCS
 * @throws IllegalAccessException 
 * @throws InstantiationException 
 * @throws UnsupportedEncodingException 
 */ 
public List<ActivationCodeBatch> convertActivationCodeBatch(AmazonSearchResponse response) throws InstantiationException, IllegalAccessException, UnsupportedEncodingException {
		
	List<Map<String, String>> fields = response.getResultFields();
		List<ActivationCodeBatch> activationCodeBatches = new ArrayList<ActivationCodeBatch>();
			
		for (Map<String , String> field: fields){
			ActivationCodeBatch activationCodeBatch = new ActivationCodeBatch(); 
			ActivationCode activationCode = new ActivationCode();
			ActivationCodeRegistrationDefinition regDef = new ActivationCodeRegistrationDefinition();
			List<ActivationCode> codes = new ArrayList<ActivationCode>();
			String batchId = null;
			String productId = null;
			String productGroupId = null;
			String code = null;
			String startDate = null;
			String endDate = null;
			String createdDate = null;
			String licenseType = null;
			String productName = null;
			String productGroupName = null;
			DateTime sDate = null;
			DateTime eDate = null;
			DateTime cDate = null;
			
			if(field.get(SearchDomainFields.CLAIM_TICKET_BATCHNAME) != null) {
				batchId = field.get(SearchDomainFields.CLAIM_TICKET_BATCHNAME);
				batchId = removeSquareBracket(batchId);
				batchId = HtmlUtils.htmlEscape(batchId) ;
				//batchId = URLEncoder.encode(batchId,"UTF-8") ;
				activationCodeBatch.setBatchId(batchId);
			}
						
			if(field.get(SearchDomainFields.CLAIM_TICKET_PRODUCTID) != null) {
				productId = field.get(SearchDomainFields.CLAIM_TICKET_PRODUCTID);
				productId = removeSquareBracket(productId);
				
				Hits hitsResult = new CloudSearchUtils(DomainType.PRODUCT).searchDocument(SearchDomainFields.PRODUCT_PRODUCTID, "'" + productId + "'");				//searchResult.getHits();					
				if (hitsResult.getFound() != 0) {
					Hit result = hitsResult.getHit().get(0);
					Map<String, List<String>> keys = result.getFields();				
					productName = keys.get(SearchDomainFields.PRODUCT_PRODUCTNAME).get(0);				
					Product product = new RegisterableProduct();
					product.setProductName(productName);
					regDef.setProduct(product); 
					activationCodeBatch.setActivationCodeRegistrationDefinition(regDef);
				}
			}
			if(field.get(SearchDomainFields.CLAIM_TICKET_PRODUCTGROUPID) != null) {
				productGroupId = field.get(SearchDomainFields.CLAIM_TICKET_PRODUCTGROUPID);
				productGroupId = removeSquareBracket(productGroupId);
				
				Hits hitsResult = new CloudSearchUtils(DomainType.PRODUCTGROUP).searchDocument(SearchDomainFields.PRODUCT_GRP_PRODUCTGROUPID, "'" + productGroupId + "'");				//searchResult.getHits();
				if (hitsResult.getFound() != 0) {
					Hit result = hitsResult.getHit().get(0);
					Map<String, List<String>> keys = result.getFields(); 			
					productGroupName = keys.get(SearchDomainFields.PRODUCT_GRP_PRODUCTGROUPNAME).get(0); 				
					EacGroups eacGrp = new EacGroups();
					eacGrp.setGroupName(productGroupName);
					eacGrp.setId(productGroupId);
					regDef.setEacGroup(eacGrp);
					activationCodeBatch.setActivationCodeRegistrationDefinition(regDef);
				}				
			}  	
			
			if(field.get(SearchDomainFields.CLAIM_TICKET_ACTIVATIONCODE) != null) {
				code = field.get(SearchDomainFields.CLAIM_TICKET_ACTIVATIONCODE);
				code = removeSquareBracket(code);
				activationCode.setCode(code);
				codes.add(activationCode);
				activationCodeBatch.setActivationCodes(codes);
			} 
			
			if(field.get(SearchDomainFields.CLAIM_TICKET_STARTDATE) != null) {
				startDate = field.get(SearchDomainFields.CLAIM_TICKET_STARTDATE);
				startDate = removeSquareBracket(startDate);	
				sDate = new DateTime(startDate);
				activationCodeBatch.setStartDate(sDate);
			} 
			
			if(field.get(SearchDomainFields.CLAIM_TICKET_ENDDATE) != null) {
				endDate = field.get(SearchDomainFields.CLAIM_TICKET_ENDDATE);
				endDate = removeSquareBracket(endDate);	
				eDate = new DateTime(endDate);
				activationCodeBatch.setEndDate(eDate);
			}
			
			if (field.get(SearchDomainFields.CLAIM_TICKET_CREATEDDATE) != null) {
				createdDate = field.get(SearchDomainFields.CLAIM_TICKET_CREATEDDATE);
				createdDate = removeSquareBracket(createdDate);
				cDate = new DateTime(createdDate);
				activationCodeBatch.setCreatedDate(cDate);
			}
			
			if(field.get(SearchDomainFields.CLAIM_TICKET_LICENSETYPE) != null) {
				com.oup.eac.domain.LicenceTemplate.LicenceType test = null;
				licenseType = field.get(SearchDomainFields.CLAIM_TICKET_LICENSETYPE);
				licenseType = removeSquareBracket(licenseType);	
				if (licenseType.equals("ROLLING")) {
					test = LicenceTemplate.LicenceType.ROLLING;
				} else if (licenseType.equals("CONCURRENT")) {
					test = LicenceTemplate.LicenceType.CONCURRENT;
				} else if (licenseType.equals("USAGE")) {
					test = LicenceTemplate.LicenceType.USAGE;
				}
				
				if (test != null) {
					LicenceTemplate licenceTemplate = test.getLicenceClass().newInstance();
					activationCodeBatch.setLicenceTemplate(licenceTemplate);
				}				
			}			
			activationCodeBatches.add(activationCodeBatch);		
		}
		return activationCodeBatches;
	}

	public List<EacGroups> convertProductGroups(AmazonSearchResponse response) {
		
		List<Map<String, String>> fields = response.getResultFields();
		List<EacGroups> productGroups = new ArrayList<EacGroups>();
		for (Map<String , String> field: fields){
			EacGroups productGroup = new EacGroups();
			if(field.get(SearchDomainFields.PRODUCT_GRP_PRODUCTGROUPID)!=null){
				String productGroupId = field.get(SearchDomainFields.PRODUCT_GRP_PRODUCTGROUPID);
				productGroupId = removeSquareBracket(productGroupId);
				productGroup.setId(productGroupId);
			}
			if(field.get(SearchDomainFields.PRODUCT_GRP_PRODUCTGROUPNAME)!=null){
				String productGroupName = field.get(SearchDomainFields.PRODUCT_GRP_PRODUCTGROUPNAME);
				productGroupName = removeSquareBracket(productGroupName);
				productGroup.setGroupName(productGroupName);
			}
			productGroups.add(productGroup);
		}
		return productGroups;
	}
	
	private int convertToInteger(String s){
		return Integer.parseInt(s.trim());
	}
	private String removeSquareBracket(String value) {
		String result = value;
		if (value != null && value.startsWith("[") && value.endsWith("]")) {
			result = value.substring(1, value.length()-1);
		}
		return result ;
	}
	
	/**
	 * convertGroupSearch
	 * @param response
	 * @return
	 * List<GroupRestDto>
	 * @author Developed by TCS
	 */
	public List<GroupRestDto> convertGroupSearch(AmazonSearchResponse response) {

		List<Map<String, String>> fields = response.getResultFields();
		List<GroupRestDto> groups = new ArrayList<GroupRestDto>();
		GroupRestDto groupRestDto;		
		for (Map<String , String> field: fields){
			groupRestDto = new GroupRestDto();
			String groupId = null;
			String groupName = null;		
			
			if(field.get(SearchDomainFields.GROUP_GROUPID) != null) {
				groupId = field.get(SearchDomainFields.GROUP_GROUPID);
				groupId = removeSquareBracket(groupId);
				groupRestDto.setGroupId(groupId);
			}
			if(field.get(SearchDomainFields.GROUP_GROUPNAME) != null) {
				groupName = field.get(SearchDomainFields.GROUP_GROUPNAME);
				groupName = removeSquareBracket(groupName);
				groupRestDto.setGroupName(groupName);
			}
			
			if(groupRestDto != null && groupId != null && groupName != null ){
				
				groups.add(groupRestDto);					
			}
		}
		return groups;
	}
	/**
	 * convertUserGroupMembershipSearch
	 * @param response
	 * @return
	 * List<UserGroupMembershipDto>
	 * @author Developed by TCS
	 */
	public List<UserGroupMembershipDto> convertUserGroupMembershipSearch(AmazonSearchResponse response) {

		List<Map<String, String>> fields = response.getResultFields();
		List<UserGroupMembershipDto> userGroupMemberships = new ArrayList<UserGroupMembershipDto>();
		UserGroupMembershipDto userGroupMembershipDto;		
		for (Map<String , String> field: fields){
			userGroupMembershipDto = new UserGroupMembershipDto();
			String groupId = null;
			String userId = null;		
			
			if(field.get(SearchDomainFields.USER_GRP_GROUPID) != null) {
				groupId = field.get(SearchDomainFields.USER_GRP_GROUPID);
				groupId = removeSquareBracket(groupId);
				userGroupMembershipDto.setGroupId(groupId);
			}
			if(field.get(SearchDomainFields.USER_GRP_USERID) != null) {
				userId = field.get(SearchDomainFields.USER_GRP_USERID);
				userId = removeSquareBracket(userId);
				userGroupMembershipDto.setUserId(userId);
			}
			
			if(userGroupMembershipDto != null && groupId != null && userId != null ){
				
				userGroupMemberships.add(userGroupMembershipDto);					
			}
		}
		return userGroupMemberships;
	}
}
