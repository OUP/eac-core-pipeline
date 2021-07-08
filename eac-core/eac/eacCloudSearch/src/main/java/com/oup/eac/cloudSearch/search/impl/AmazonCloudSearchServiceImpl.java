package com.oup.eac.cloudSearch.search.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import com.amazonaws.services.cloudsearchdomain.AmazonCloudSearchDomainClient;
import com.amazonaws.services.cloudsearchdomain.model.Bucket;
import com.amazonaws.services.cloudsearchdomain.model.BucketInfo;
import com.amazonaws.services.cloudsearchdomain.model.Hit;
import com.amazonaws.services.cloudsearchdomain.model.Hits;
import com.amazonaws.services.cloudsearchdomain.model.QueryParser;
import com.amazonaws.services.cloudsearchdomain.model.SearchRequest;
import com.amazonaws.services.cloudsearchdomain.model.SearchResult;
import com.constants.SearchDomainFields;
import com.oup.eac.cloudSearch.search.SearchServiceHandler;
import com.oup.eac.cloudSearch.util.CloudSearchUtils;
import com.oup.eac.cloudSearch.util.CloudSearchUtils.DomainType;
import com.oup.eac.domain.ActivationCodeState;
import com.oup.eac.domain.RollingLicenceTemplate.RollingBeginOn;
import com.oup.eac.domain.RollingLicenceTemplate.RollingUnitType;
import com.oup.eac.domain.search.AmazonSearchFields;
import com.oup.eac.domain.search.AmazonSearchRequest;
import com.oup.eac.domain.search.AmazonSearchResponse;
import com.oup.eac.dto.ActivationCodeBatchSearchCriteria;
import com.oup.eac.dto.EnforceableProductDto;
import com.oup.eac.dto.LicenceDetailDto;
import com.oup.eac.dto.LicenceDetailDto.LicenceType;
import com.oup.eac.dto.LicenceDto;
import com.oup.eac.dto.RollingLicenceDetailDto;
import com.oup.eac.dto.StandardConcurrentLicenceDetailDto;
import com.oup.eac.dto.UsageLicenceDetailDto;

public class AmazonCloudSearchServiceImpl implements SearchServiceHandler{

	private static final Logger logger = Logger.getLogger(AmazonCloudSearchServiceImpl.class);
	
	private static List<String> batches = new ArrayList<String>();
	
	private static int resultCount = 0;
		
	private static String restrictedUser = "(not lastname:'TRUSTED_SYSTEM')";
	
	@Override
	public AmazonSearchResponse searchUser(AmazonSearchRequest searchRequest, boolean isArchivedRequired, boolean filterOutTrusted) {

		AmazonSearchResponse restResponse = new AmazonSearchResponse();
		SearchRequest request=new SearchRequest();
		SearchResult response = new SearchResult();
		AmazonCloudSearchDomainClient client = new CloudSearchUtils(DomainType.USER).getCloudSearchClient();
		if(searchRequest.getSearchFieldsList().isEmpty()){
			request = new SearchRequest()
					.withQuery("(and matchall "
								+ SearchDomainFields.USER_ARCHIVESTATUS
								+ ":'ACTIVE'"+restrictedUser+")")
					.withSort("createddate desc")
					.withQueryParser(QueryParser.Structured)
					.withStart((long) searchRequest.getStartAt())
					.withSize((long) searchRequest.getResultsPerPage());
			response = client.search(request);
			System.out.println(request.getQuery());
		} else {
			StringBuilder queryStr = new StringBuilder("(and ");
			String createdDateFrom =  null;
			String createdDateTo= null;
			int count =0;
			for(AmazonSearchFields searchField: searchRequest.getSearchFieldsList()) {
				if(searchField.getName()=="createddatefrom"){
					createdDateFrom = searchField.getValue();
				}if(searchField.getName()=="createddateto"){
					createdDateTo = searchField.getValue();
				}
			}
			for(AmazonSearchFields searchField: searchRequest.getSearchFieldsList()) {
				if(null != searchField.getValue() && !searchField.getValue().equals("") && !searchField.getName().equals(SearchDomainFields.USER_USERID) &&
						!searchField.getName().equals("createddateto") && !searchField.getName().equals("createddatefrom")) {

					queryStr.append(prepareSearchString(searchField));

				}
				else if (searchField.getName().equals(SearchDomainFields.USER_USERID)){
					queryStr.append(" (or "); 
					for(String s : searchField.getValue().split(",")){
						queryStr.append("userid:'"+s.replace(" ", "")+"' ");
					}
					queryStr.append(") ");
				}
				else if(searchField.getName().equals("createddatefrom") || searchField.getName().equals("createddateto")){
					if(count==0){
						if(createdDateFrom!=null){
							queryStr.append("createddate:['");
							String startDate = StringUtils.substringBefore(createdDateFrom, ".");
							startDate = startDate+".00Z";
							queryStr.append(startDate);
						}if(createdDateTo!=null && createdDateFrom!=null){
							queryStr.append("','");
							String endDate = StringUtils.substringBefore(createdDateTo, ".");
							endDate = endDate+".00Z";
							queryStr.append(endDate);
							queryStr.append("']");
						}if(createdDateTo==null){
							queryStr.append("',}");
						}if(createdDateFrom==null && createdDateTo!=null){
							queryStr.append("createddate:{,'");
							String endDate = StringUtils.substringBefore(createdDateTo, ".");
							endDate = endDate+".00Z";
							queryStr.append(endDate);
							queryStr.append("']");
						}
						count++;
					}
				}
			}
			if (isArchivedRequired) {
				queryStr.append(" (not field="+SearchDomainFields.USER_ARCHIVESTATUS+" 'DELETED') ");
			} else {
				queryStr.append(" "+SearchDomainFields.USER_ARCHIVESTATUS+":'ACTIVE'");
			}
			if(filterOutTrusted){
				queryStr.append(" "+restrictedUser+" ");
			}
			queryStr.append(")");
			logger.debug("search Query: "+ queryStr);
			request = new SearchRequest().withQuery(queryStr.toString()).withSort("createddate desc").
					withQueryParser(QueryParser.Structured).withStart((long) searchRequest.getStartAt()).
					withSize((long) searchRequest.getResultsPerPage());
			response = client.search(request);
		}
		Hits hits = response.getHits();
		Long count = hits.getFound();

		List<Map<String, String>> resultFieldList= new ArrayList<Map<String,String>>();
		for (Hit hit : hits.getHit()) {
			Map<String, String> resultFields = new HashMap<String, String>();
			Map<String, List<String>> fields= hit.getFields();
			if(fields.get("username")!=null){
				List<String> username = fields.get("username");
				resultFields.put("username", username.toString());
			}
			if(fields.get("emailaddress")!=null){
				List<String> emailaddress = fields.get("emailaddress");
				resultFields.put("emailaddress", emailaddress.toString());
			}
			if(fields.get("userid")!=null){
				List<String> userid = fields.get("userid");
				resultFields.put("userid", userid.toString());
			}
			if(fields.get("createddate")!=null){
				List<String> createddate = fields.get("createddate");
				resultFields.put("createddate", createddate.toString());
			}
			if(fields.get("firstname")!=null){
				List<String> firstname=fields.get("firstname");
				resultFields.put("firstname",firstname.toString());
			}
			if(fields.get("lastname")!=null){
				List<String> lastname =fields.get("lastname");
				resultFields.put("lastname",lastname.toString());
			}
			if(fields.get("licensecount")!=null){
				List<String> licenses =fields.get("licensecount");
				resultFields.put("licensecount",licenses.toString());
			}
			if(fields.get("lastlogin") != null) {
				List<String> lastlogin =fields.get("lastlogin");
				resultFields.put("lastlogin",lastlogin.toString());
			}

			resultFieldList.add(resultFields);
		}
		restResponse.setResultFields(resultFieldList);
		restResponse.setResultsFound((int) (long) count);

		/*if(logger.isDebugEnabled()){ logger.debug("Returning rest response :" +restResponse); */
		return restResponse;
	}
	
	@Override
	public AmazonSearchResponse searchUserByUserIdList(List<String> userIdList) {

		AmazonSearchResponse restResponse = new AmazonSearchResponse();
		SearchRequest request=new SearchRequest();
		SearchResult response = new SearchResult();
		AmazonCloudSearchDomainClient client = new CloudSearchUtils(DomainType.USER).getCloudSearchClient();
		request = new SearchRequest()
		.withQuery("(and matchall "
					+ SearchDomainFields.USER_ARCHIVESTATUS
					+ ":'ACTIVE')")
		.withSort("createddate desc")
		.withQueryParser(QueryParser.Structured)
		.withStart((long) 0)
		.withSize((long)1000);
		response = client.search(request);
		
		
		Hits hits = response.getHits();
		Long count = hits.getFound();

		List<Map<String, String>> resultFieldList= new ArrayList<Map<String,String>>();
		for (Hit hit : hits.getHit()) {
			Map<String, String> resultFields = new HashMap<String, String>();
			Map<String, List<String>> fields= hit.getFields();
			if(fields.get("username")!=null){
				List<String> username = fields.get("username");
				resultFields.put("username", username.toString());
			}
			if(fields.get("emailaddress")!=null){
				List<String> emailaddress = fields.get("emailaddress");
				resultFields.put("emailaddress", emailaddress.toString());
			}
			if(fields.get("userid")!=null){
				List<String> userid = fields.get("userid");
				resultFields.put("userid", userid.toString());
			}
			if(fields.get("createddate")!=null){
				List<String> createddate = fields.get("createddate");
				resultFields.put("createddate", createddate.toString());
			}
			if(fields.get("firstname")!=null){
				List<String> firstname=fields.get("firstname");
				resultFields.put("firstname",firstname.toString());
			}
			if(fields.get("lastname")!=null){
				List<String> lastname =fields.get("lastname");
				resultFields.put("lastname",lastname.toString());
			}
			if(fields.get("licensecount")!=null){
				List<String> licenses =fields.get("licensecount");
				resultFields.put("licensecount",licenses.toString());
			}
			if(fields.get("lastlogin") != null) {
				List<String> lastlogin =fields.get("lastlogin");
				resultFields.put("lastlogin",lastlogin.toString());
			}

			resultFieldList.add(resultFields);
		}
		restResponse.setResultFields(resultFieldList);
		restResponse.setResultsFound((int) (long) count);

		if(logger.isDebugEnabled()){ logger.debug("Returning rest response :" +restResponse); }; 
		return restResponse;
		//return null ;
	}
	
	/**
	 * searchActivationCode
	 * @param searchRequest
	 * @param likeSearch
	 * @return
	 * AmazonSearchResponse
	 * @author Developed by TCS
	 */ 
	@Override
	public AmazonSearchResponse searchActivationCode(AmazonSearchRequest searchRequest, boolean likeSearch) {

		AmazonSearchResponse restResponse = new AmazonSearchResponse();
		SearchRequest request=new SearchRequest();
		SearchResult response = new SearchResult();
		AmazonCloudSearchDomainClient client = new CloudSearchUtils(DomainType.ACTIVATIONCODE).getCloudSearchClient();
		AmazonSearchFields searchField = searchRequest.getSearchFieldsList().get(0);
		
			StringBuilder queryStr = new StringBuilder("( ");
			if(likeSearch){
				queryStr.append("prefix field=" + searchField.getName() + " '" + searchField.getValue().trim() + "'");
			} else {
				queryStr.append("and " + searchField.getName() + " :'"+searchField.getValue().trim()+"'");
			}
			queryStr.append(" )");
			System.out.println("queryStr : " + queryStr);
			request = new SearchRequest().withQuery(queryStr.toString()).withQueryParser(QueryParser.Structured).withSize((long) searchRequest.getResultsPerPage());
			response = client.search(request);
		
		Hits hits = response.getHits();
		Long count = hits.getFound();

		List<Map<String, String>> resultFieldList= new ArrayList<Map<String,String>>();
		for (Hit hit : hits.getHit()) {
			Map<String, String> resultFields = new HashMap<String, String>();
			Map<String, List<String>> fields= hit.getFields();
			if(fields.get(SearchDomainFields.CLAIM_TICKET_ACTIVATIONCODE) != null){
				List<String> activationCode = fields.get(SearchDomainFields.CLAIM_TICKET_ACTIVATIONCODE);
				resultFields.put(SearchDomainFields.CLAIM_TICKET_ACTIVATIONCODE, activationCode.toString());
			}
			if(fields.get(SearchDomainFields.CLAIM_TICKET_PRODUCTID) != null){
				List<String> productId = fields.get(SearchDomainFields.CLAIM_TICKET_PRODUCTID);
				resultFields.put(SearchDomainFields.CLAIM_TICKET_PRODUCTID, productId.toString());
			}
			if(fields.get(SearchDomainFields.CLAIM_TICKET_PRODUCTGROUPID) != null){
				List<String> productGroupId = fields.get(SearchDomainFields.CLAIM_TICKET_PRODUCTGROUPID);
				resultFields.put(SearchDomainFields.CLAIM_TICKET_PRODUCTGROUPID, productGroupId.toString());
			}
			if(fields.get(SearchDomainFields.CLAIM_TICKET_AVAILABLESTATE) != null){
				List<String> availableState = fields.get(SearchDomainFields.CLAIM_TICKET_AVAILABLESTATE);
				resultFields.put(SearchDomainFields.CLAIM_TICKET_AVAILABLESTATE, availableState.toString());
			}
			if(fields.get(SearchDomainFields.CLAIM_TICKET_ALLOWEDUSAGES) != null){
				List<String> allowedUsages = fields.get(SearchDomainFields.CLAIM_TICKET_ALLOWEDUSAGES);
				resultFields.put(SearchDomainFields.CLAIM_TICKET_ALLOWEDUSAGES, allowedUsages.toString());
			}
			if(fields.get(SearchDomainFields.CLAIM_TICKET_ACTUAL_USAGES) != null){
				List<String> actualUsages = fields.get(SearchDomainFields.CLAIM_TICKET_ACTUAL_USAGES);
				resultFields.put(SearchDomainFields.CLAIM_TICKET_ACTUAL_USAGES, actualUsages.toString());
			}
			if(fields.get(SearchDomainFields.CLAIM_TICKET_STARTDATE) != null){
				List<String> startDate = fields.get(SearchDomainFields.CLAIM_TICKET_STARTDATE);
				resultFields.put(SearchDomainFields.CLAIM_TICKET_STARTDATE, startDate.toString());
			}
			if(fields.get(SearchDomainFields.CLAIM_TICKET_ENDDATE) != null){
				List<String> endDate = fields.get(SearchDomainFields.CLAIM_TICKET_ENDDATE);
				resultFields.put(SearchDomainFields.CLAIM_TICKET_ENDDATE, endDate.toString());
			}
			resultFieldList.add(resultFields);
		}
		restResponse.setResultFields(resultFieldList);
		restResponse.setResultsFound((int) (long) count);
		return restResponse;
	}
	
	/**
	 * searchActivationCode
	 * @param searchRequest
	 * @return
	 * AmazonSearchResponse
	 * @author Developed by TCS
	 */ 
	@Override
	public AmazonSearchResponse searchActivationCode(AmazonSearchRequest searchRequest) {

		AmazonSearchResponse restResponse = new AmazonSearchResponse();
		SearchRequest request=new SearchRequest();
		SearchResult response = new SearchResult();
		AmazonCloudSearchDomainClient client = new CloudSearchUtils(DomainType.ACTIVATIONCODE).getCloudSearchClient();
		if(searchRequest.getSearchFieldsList().isEmpty()){
			request = new SearchRequest().withQuery("matchall").withQueryParser(QueryParser.Structured).withStart((long) searchRequest.getStartAt());
			response = client.search(request);
		}
		else{
			StringBuilder queryStr = new StringBuilder("(and ");
			String activationCode = null;
			String productId = null;
			String productGroupId = null;
			String availableState = null;
			for(AmazonSearchFields searchField: searchRequest.getSearchFieldsList()) {
				if (searchField.getName() == SearchDomainFields.CLAIM_TICKET_ACTIVATIONCODE) {
					activationCode = searchField.getValue();
				} if(searchField.getName() == SearchDomainFields.CLAIM_TICKET_PRODUCTID) {				
					productId = searchField.getValue();
				} if(searchField.getName() == SearchDomainFields.CLAIM_TICKET_PRODUCTGROUPID) {
					productGroupId = searchField.getValue();
				} if((searchField.getName() == SearchDomainFields.CLAIM_TICKET_AVAILABLESTATE) && (searchField.getValue() != "ALL")) {					
					if (searchField.getValue().equals(ActivationCodeState.USED.toString())) {
						availableState = "UNAVAILABLE";
						searchField.setValue(availableState);
					} else {
						availableState = searchField.getValue();
					}
				}
			}
			for(AmazonSearchFields searchField: searchRequest.getSearchFieldsList()) {
				if(null != searchField.getValue() && !searchField.getValue().equals("")) {
					queryStr.append(prepareSearchString(searchField));
				}
			}
			queryStr.append(")");
			System.out.println("queryStr : " + queryStr);
			request = new SearchRequest().withQuery(queryStr.toString()).withQueryParser(QueryParser.Structured).withStart((long) searchRequest.getStartAt());
			response = client.search(request);
		}
		Hits hits = response.getHits();
		Long count = hits.getFound();

		List<Map<String, String>> resultFieldList= new ArrayList<Map<String,String>>();
		for (Hit hit : hits.getHit()) {
			Map<String, String> resultFields = new HashMap<String, String>();
			Map<String, List<String>> fields= hit.getFields();
			if(fields.get(SearchDomainFields.CLAIM_TICKET_ACTIVATIONCODE) != null){
				List<String> activationCode = fields.get(SearchDomainFields.CLAIM_TICKET_ACTIVATIONCODE);
				resultFields.put(SearchDomainFields.CLAIM_TICKET_ACTIVATIONCODE, activationCode.toString());
			}
			if(fields.get(SearchDomainFields.CLAIM_TICKET_PRODUCTID) != null){
				List<String> productId = fields.get(SearchDomainFields.CLAIM_TICKET_PRODUCTID);
				resultFields.put(SearchDomainFields.CLAIM_TICKET_PRODUCTID, productId.toString());
			}
			if(fields.get(SearchDomainFields.CLAIM_TICKET_PRODUCTGROUPID) != null){
				List<String> productGroupId = fields.get(SearchDomainFields.CLAIM_TICKET_PRODUCTGROUPID);
				resultFields.put(SearchDomainFields.CLAIM_TICKET_PRODUCTGROUPID, productGroupId.toString());
			}
			if(fields.get(SearchDomainFields.CLAIM_TICKET_AVAILABLESTATE) != null){
				List<String> availableState = fields.get(SearchDomainFields.CLAIM_TICKET_AVAILABLESTATE);
				resultFields.put(SearchDomainFields.CLAIM_TICKET_AVAILABLESTATE, availableState.toString());
			}
			if(fields.get(SearchDomainFields.CLAIM_TICKET_ALLOWEDUSAGES) != null){
				List<String> allowedUsages = fields.get(SearchDomainFields.CLAIM_TICKET_ALLOWEDUSAGES);
				resultFields.put(SearchDomainFields.CLAIM_TICKET_ALLOWEDUSAGES, allowedUsages.toString());
			}
			if(fields.get(SearchDomainFields.CLAIM_TICKET_STARTDATE) != null){
				List<String> startDate = fields.get(SearchDomainFields.CLAIM_TICKET_STARTDATE);
				resultFields.put(SearchDomainFields.CLAIM_TICKET_STARTDATE, startDate.toString());
			}
			if(fields.get(SearchDomainFields.CLAIM_TICKET_ENDDATE) != null){
				List<String> endDate = fields.get(SearchDomainFields.CLAIM_TICKET_ENDDATE);
				resultFields.put(SearchDomainFields.CLAIM_TICKET_ENDDATE, endDate.toString());
			}
			resultFieldList.add(resultFields);
		}
		restResponse.setResultFields(resultFieldList);
		restResponse.setResultsFound((int) (long) count);
		return restResponse;
	}
	
	/* (non-Javadoc)
	 * @see com.oup.eac.cloudSearch.search.SearchServiceHandler#searchActivationCodeBatch(com.oup.eac.domain.search.AmazonSearchRequest)
	 */
	@SuppressWarnings("deprecation")
	@Override
	public AmazonSearchResponse searchActivationCodeBatch(AmazonSearchRequest searchRequest, 
			ActivationCodeBatchSearchCriteria searchCriteria) {

	AmazonSearchResponse restResponse = new AmazonSearchResponse();
		SearchRequest request=new SearchRequest();
		SearchResult response = new SearchResult();
		AmazonCloudSearchDomainClient client = new CloudSearchUtils(DomainType.ACTIVATIONCODE).getCloudSearchClient();
		List<String> batchList = new ArrayList<String>();
		Set<String> batchIdSet = new HashSet<String>();
		List<Hit> validHits = new ArrayList<Hit>();
		int startAt = searchRequest.getStartAt();
		if (searchRequest.getStartAt() == 0) {
			batches.clear();
			resultCount = 0;
		}
		int batchSize = batches.size();
		if (startAt < batches.size()) {			
			for (int i = batchSize-1; i >= startAt; i--) {
				batches.remove(i);
			}
		}
		if (searchRequest.getSearchFieldsList().isEmpty()) {
			request = new SearchRequest().withQuery("matchall").withQueryParser(QueryParser.Structured).withStart((long) searchRequest.getStartAt());
			response = client.search(request);
		} else {
			StringBuilder queryStr = new StringBuilder("(and ");
			String batchId = null;
			String productId = null;
			String productGroupId = null;
			String activationCode = null;
			String validDate = null;
			String startDate = null;
			String endDate = null;
			String licenseType = null;		
			
			for(AmazonSearchFields searchField: searchRequest.getSearchFieldsList()) {
				if (searchField.getName() == SearchDomainFields.CLAIM_TICKET_BATCHNAME) {
					batchId = searchField.getValue();
				} 
				if(searchField.getName() == SearchDomainFields.CLAIM_TICKET_PRODUCTID) {
					productId = searchField.getValue();
				} 
				if(searchField.getName() == SearchDomainFields.CLAIM_TICKET_PRODUCTGROUPID) {
					productGroupId = searchField.getValue();
				} 
				if (searchField.getName() == SearchDomainFields.CLAIM_TICKET_ACTIVATIONCODE) {
					activationCode = searchField.getValue();
				} 
				if(searchField.getName() == SearchDomainFields.CLAIM_TICKET_STARTDATE) {
					startDate = searchField.getValue();
				} 
				if(searchField.getName() == SearchDomainFields.CLAIM_TICKET_ENDDATE) {
					endDate = searchField.getValue();
				}
				if(searchField.getName() == SearchDomainFields.CLAIM_TICKET_LICENSETYPE) {
					licenseType = searchField.getValue();					
				}
			}
			for(AmazonSearchFields searchField: searchRequest.getSearchFieldsList()) {
				if(null != searchField.getValue() && !searchField.getValue().equals("")) {
					queryStr.append(prepareSearchString(searchField));
				}
			}
			queryStr.append(")");
			System.out.println("query : " + queryStr);
			request = new SearchRequest().withQuery(queryStr.toString()).withQueryParser(QueryParser.Structured).withStart((long) searchRequest.getStartAt());
			request.setPartial(true);
			response = client.search(request);
			Hits hits = response.getHits();
			Long size = hits.getFound();
			long batchCount = 0;
			if (size > 10000) {
				//size = (long) 10000;
				if (size%10000 == 0) {
					batchCount = 1;
				} else {
					batchCount = (size/10000) + 1;
				}
			} else {
				batchCount = 1;
			}
			String withCursor = "initial";
			for (int i=0; i < batchCount; i++) {
				
			request = new SearchRequest().withQuery(queryStr.toString()).withQueryParser(QueryParser.Structured).withCursor(withCursor)
					/*withStart((long) searchRequest.getStartAt())*/.withSize(10000L);
			response = client.search(request);
			withCursor = response.getHits().getCursor();
			hits = response.getHits();
			
			for (Hit hit : hits.getHit()) {
				String name = null;
				Map<String, List<String>> fields = hit.getFields();
				if(fields.get(SearchDomainFields.CLAIM_TICKET_BATCHNAME) != null){
					name = fields.get(SearchDomainFields.CLAIM_TICKET_BATCHNAME).toString();								
				}
				if(batchIdSet.size() == 0 || (batchIdSet.size() > 0 && !batchIdSet.contains(name))) {
					batchIdSet.add(name);
					validHits.add(hit);
				}
			}
		}
			resultCount = validHits.size();
	}
		

		DateTime validDate = searchCriteria.getBatchDate();
		String startDate = null;
		String endDate = null;
		DateTime sDate = null;
		DateTime eDate = null;
		
		/*Hits hits = response.getHits();
		Long count = hits.getFound();
		List<Map<String, String>> resultFieldList= new ArrayList<Map<String,String>>();	
		int searchCount = 0;
		if (searchRequest.getStartAt() == 0) {
			for (Hit hit : hits.getHit()) {		
				searchCount = searchCount + 1;
				Map<String, List<String>> fields = hit.getFields();
				List<String> batchId = null;			
				if(fields.get(SearchDomainFields.CLAIM_TICKET_BATCHNAME) != null){
					batchId = fields.get(SearchDomainFields.CLAIM_TICKET_BATCHNAME);								
				}
				
				if(batchList != null && !batchList.contains(batchId.toString())) {	
					if (searchCriteria.getBatchDate() != null 
							&& fields.get(SearchDomainFields.CLAIM_TICKET_STARTDATE) != null 
							&& fields.get(SearchDomainFields.CLAIM_TICKET_ENDDATE) != null) {
						
						startDate = fields.get(SearchDomainFields.CLAIM_TICKET_STARTDATE).get(0);
	    				startDate = startDate.replaceAll("\\[", "").replaceAll("\\]","");	
	    				sDate = new DateTime(startDate);
	    				
	    				endDate = fields.get(SearchDomainFields.CLAIM_TICKET_ENDDATE).get(0);
	    				endDate = endDate.replaceAll("\\[", "").replaceAll("\\]","");	
	    				eDate = new DateTime(endDate);
	    				
	    				if (((sDate.toYearMonthDay()).isBefore(validDate.toYearMonthDay()) || (sDate.toYearMonthDay()).equals(validDate.toYearMonthDay())) 
	        					&& (((eDate.toYearMonthDay()).isAfter(validDate.toYearMonthDay())) || (eDate.toYearMonthDay()).equals(validDate.toYearMonthDay()))) {
	    					
	    					batchList.add(batchId.toString());
	    					resultCount = batchList.size();
	        			}
					} else {
						batchList.add(batchId.toString());
						resultCount = batchList.size();
					}					
				}
			}
		}*/
		
		restResponse.setResultsFound(resultCount);
		List<Map<String, String>> resultFieldList= new ArrayList<Map<String,String>>();	
		for (Hit hit : validHits) {
			
			Map<String, String> resultFields = new HashMap<String, String>();
			Map<String, List<String>> fields= hit.getFields();
			List<String> batchId = null;
			
			if(fields.get(SearchDomainFields.CLAIM_TICKET_BATCHNAME) != null){
				batchId = fields.get(SearchDomainFields.CLAIM_TICKET_BATCHNAME);
				resultFields.put(SearchDomainFields.CLAIM_TICKET_BATCHNAME, batchId.toString());				
			}
			if(fields.get(SearchDomainFields.CLAIM_TICKET_PRODUCTID) != null){
				List<String> productId = fields.get(SearchDomainFields.CLAIM_TICKET_PRODUCTID);
				resultFields.put(SearchDomainFields.CLAIM_TICKET_PRODUCTID, productId.toString());
			}
			if(fields.get(SearchDomainFields.CLAIM_TICKET_PRODUCTGROUPID) != null){
				List<String> productGroupId = fields.get(SearchDomainFields.CLAIM_TICKET_PRODUCTGROUPID);
				resultFields.put(SearchDomainFields.CLAIM_TICKET_PRODUCTGROUPID, productGroupId.toString());
			} 
			if(fields.get(SearchDomainFields.CLAIM_TICKET_ACTIVATIONCODE) != null){
				List<String> activationCode = fields.get(SearchDomainFields.CLAIM_TICKET_ACTIVATIONCODE);
				resultFields.put(SearchDomainFields.CLAIM_TICKET_ACTIVATIONCODE, activationCode.toString());
			}
			if(fields.get(SearchDomainFields.CLAIM_TICKET_STARTDATE) != null){
				List<String> startDates = fields.get(SearchDomainFields.CLAIM_TICKET_STARTDATE);
				resultFields.put(SearchDomainFields.CLAIM_TICKET_STARTDATE, startDates.toString());
			} 
			if(fields.get(SearchDomainFields.CLAIM_TICKET_ENDDATE) != null){
				List<String> endDates = fields.get(SearchDomainFields.CLAIM_TICKET_ENDDATE);
				resultFields.put(SearchDomainFields.CLAIM_TICKET_ENDDATE, endDates.toString());
			} 
			if(fields.get(SearchDomainFields.CLAIM_TICKET_CREATEDDATE) != null){
				List<String> createdDate = fields.get(SearchDomainFields.CLAIM_TICKET_CREATEDDATE);
				resultFields.put(SearchDomainFields.CLAIM_TICKET_CREATEDDATE, createdDate.toString());
			}
			if(fields.get(SearchDomainFields.CLAIM_TICKET_LICENSETYPE) != null){
				List<String> licenseType = fields.get(SearchDomainFields.CLAIM_TICKET_LICENSETYPE);
				resultFields.put(SearchDomainFields.CLAIM_TICKET_LICENSETYPE, licenseType.toString());
			}
			
			if(batches != null && !batches.contains(batchId.toString())) {
				if (searchCriteria.getBatchDate() != null 
						&& fields.get(SearchDomainFields.CLAIM_TICKET_STARTDATE) != null 
						&& fields.get(SearchDomainFields.CLAIM_TICKET_ENDDATE) != null) {
					
					startDate = fields.get(SearchDomainFields.CLAIM_TICKET_STARTDATE).get(0);
    				startDate = startDate.replaceAll("\\[", "").replaceAll("\\]","");	
    				sDate = new DateTime(startDate);
    				
    				endDate = fields.get(SearchDomainFields.CLAIM_TICKET_ENDDATE).get(0);
    				endDate = endDate.replaceAll("\\[", "").replaceAll("\\]","");	
    				eDate = new DateTime(endDate);
    				
    				if (((sDate.toYearMonthDay()).isBefore(validDate.toYearMonthDay()) || (sDate.toYearMonthDay()).equals(validDate.toYearMonthDay())) 
        					&& (((eDate.toYearMonthDay()).isAfter(validDate.toYearMonthDay())) || (eDate.toYearMonthDay()).equals(validDate.toYearMonthDay()))) {
        				try {
        					resultFieldList.add(resultFields);
        					batches.add(batchId.toString());
        					if(resultFieldList.size() == searchRequest.getResultsPerPage() || batches.size() == batchList.size()) {
        						break;
        					}
						} catch (Exception e) {
							e.printStackTrace();
						}
        			}
				} else {
					resultFieldList.add(resultFields);
					batches.add(batchId.toString());
					if(resultFieldList.size() == searchRequest.getResultsPerPage() || batches.size() == batchList.size()) {
						break;
					}
				}
			}			
		}		
		restResponse.setResultFields(resultFieldList);
		//restResponse.setResultsFound((int) (long) count);
		//restResponse.setResultsFound(resultCount);
		return restResponse;
	}
	
	@Override
	public AmazonSearchResponse searchProductGroups(AmazonSearchRequest searchRequest) {
		AmazonSearchResponse restResponse = new AmazonSearchResponse();
		SearchRequest request=new SearchRequest();
		SearchResult response = new SearchResult();
		AmazonCloudSearchDomainClient client = new CloudSearchUtils(DomainType.PRODUCTGROUP).getCloudSearchClient();
		if(searchRequest.getSearchFieldsList().isEmpty()){
			request = new SearchRequest().withQuery("(and matchall)").withQueryParser(QueryParser.Structured).withStart((long) searchRequest.getStartAt()).withSize((long) searchRequest.getResultsPerPage());
			response = client.search(request);
			System.out.println(request.getQuery());
		} else {
			StringBuilder queryStr = new StringBuilder("(and ");
			for(AmazonSearchFields searchField: searchRequest.getSearchFieldsList()) {
				if(null != searchField.getValue() && !searchField.getValue().equals("") && !searchField.getName().equals(SearchDomainFields.PRODUCT_GRP_PRODUCTGROUPID)) {
					queryStr.append(prepareSearchString(searchField));
				} else if (searchField.getName().equals(SearchDomainFields.PRODUCT_GRP_PRODUCTGROUPID)){
					queryStr.append(" (or "); 
					for(String s : searchField.getValue().split(",")){
						queryStr.append(SearchDomainFields.PRODUCT_GRP_PRODUCTGROUPID+":'"+s.trim()+"' ");
					}
					queryStr.append(") ");
				}
			}
			queryStr.append(" )");
			System.out.println(queryStr);
			request = new SearchRequest().withQuery(queryStr.toString()).withQueryParser(QueryParser.Structured).withStart((long) searchRequest.getStartAt()).withSize((long) searchRequest.getResultsPerPage());;
			response = client.search(request);
		}
		Hits hits = response.getHits();
		Long count = hits.getFound();

		List<Map<String, String>> resultFieldList= new ArrayList<Map<String,String>>();
		for (Hit hit : hits.getHit()) {
			Map<String, String> resultFields = new HashMap<String, String>();
			Map<String, List<String>> fields= hit.getFields();
			if(fields.get(SearchDomainFields.PRODUCT_GRP_PRODUCTGROUPID)!=null){
				List<String> username = fields.get(SearchDomainFields.PRODUCT_GRP_PRODUCTGROUPID);
				resultFields.put(SearchDomainFields.PRODUCT_GRP_PRODUCTGROUPID, username.toString());
			}
			if(fields.get(SearchDomainFields.PRODUCT_GRP_PRODUCTGROUPNAME)!=null){
				List<String> emailaddress = fields.get(SearchDomainFields.PRODUCT_GRP_PRODUCTGROUPNAME);
				resultFields.put(SearchDomainFields.PRODUCT_GRP_PRODUCTGROUPNAME, emailaddress.toString());
			}
			resultFieldList.add(resultFields);
		}
		
		restResponse.setResultFields(resultFieldList);
		restResponse.setResultsFound(count.intValue());
		return restResponse;
	}
	
	@Override
	public AmazonSearchResponse searchProduct(AmazonSearchRequest searchRequest) {
		
		AmazonSearchResponse cloudResponse = new AmazonSearchResponse();
		SearchRequest request=new SearchRequest();
		SearchResult response = new SearchResult();
		AmazonCloudSearchDomainClient client = new CloudSearchUtils(DomainType.PRODUCT).getCloudSearchClient();
		if(searchRequest.getSearchFieldsList().isEmpty()){
			request = new SearchRequest().withQuery("matchall").withQueryParser(QueryParser.Structured).withStart((long) searchRequest.getStartAt()).withSize((long)searchRequest.getResultsPerPage());
			response = client.search(request);
			System.out.println(request.getQuery());
		}
		else{
			StringBuilder queryStr = new StringBuilder("(and ");
			for(AmazonSearchFields searchField: searchRequest.getSearchFieldsList()) {
					if (searchField.getName().equals(SearchDomainFields.PRODUCT_DIVISIONID) ) {
						String [] searchValues = searchField.getValue().replace("[", "").replace("]", "").replace(" ", "").split(",") ;
						queryStr.append(" (or "); 
						for (String value : searchValues) {
							queryStr.append(searchField.getName() + ":'" + value + "' " ) ;
						}
						queryStr.append(" ) "); 
					} else if (searchField.getName().equals(SearchDomainFields.PRODUCT_STATE) ) {
						String [] searchValues = searchField.getValue().replace("[", "").replace("]", "").replace(" ", "").split(",") ;
						queryStr.append(" (or "); 
						for (String value : searchValues) {
							queryStr.append(searchField.getName() + ":'" + value + "' " ) ;
						}
						queryStr.append(" ) "); 
					} else if(searchField.getName().equals(SearchDomainFields.PRODUCT_PRODUCTID)) {
						queryStr.append(" (or "); 
						for(String s : searchField.getValue().split(",")){
							queryStr.append("productid:'"+s+"' ");
						}
						queryStr.append(") ");
					} else if(searchField.getName().equals(SearchDomainFields.PRODUCT_PLATFORMS)) {
						queryStr.append(" (or "); 
						for(String s : searchField.getValue().split(",")){
							queryStr.append(SearchDomainFields.PRODUCT_PLATFORMS +":'"+s+"' ");
						}
						queryStr.append(") ");
					} else {
						queryStr.append(prepareSearchString(searchField)); 
					} 
			}
			queryStr.append(")");
			System.out.println(queryStr);
			if(searchRequest.getResultsPerPage()!=null && searchRequest.getResultsPerPage() > 0)
				request = new SearchRequest().withQuery(queryStr.toString()).withSort(SearchDomainFields.PRODUCT_PRODUCTNAME +" asc").withQueryParser(QueryParser.Structured).withStart((long) searchRequest.getStartAt()).withSize((long)searchRequest.getResultsPerPage());
			else
				request = new SearchRequest().withQuery(queryStr.toString()).withSort(SearchDomainFields.PRODUCT_PRODUCTNAME +" asc").withQueryParser(QueryParser.Structured).withStart((long) searchRequest.getStartAt());
			response = client.search(request);
		}
		Hits hits = response.getHits();
		Long count = hits.getFound();

		List<Map<String, String>> resultFieldList= new ArrayList<Map<String,String>>();
		for (Hit hit : hits.getHit()) {
			Map<String, String> resultFields = new HashMap<String, String>();
			Map<String, List<String>> fields= hit.getFields();
			if(fields.get(SearchDomainFields.PRODUCT_PRODUCTNAME)!= null){
				List<String> productname = fields.get(SearchDomainFields.PRODUCT_PRODUCTNAME);
				resultFields.put(SearchDomainFields.PRODUCT_PRODUCTNAME, productname.toString());
			}
			if(fields.get(SearchDomainFields.PRODUCT_REGISTRABLETYPE) != null){
				List<String> registrabletype = fields.get(SearchDomainFields.PRODUCT_REGISTRABLETYPE);
				resultFields.put(SearchDomainFields.PRODUCT_REGISTRABLETYPE, registrabletype.toString());
			}
			if(fields.get(SearchDomainFields.PRODUCT_ACTIVATION_METHOD)!= null){
				List<String> activationmethod = fields.get(SearchDomainFields.PRODUCT_ACTIVATION_METHOD);
				resultFields.put(SearchDomainFields.PRODUCT_ACTIVATION_METHOD, activationmethod.toString());
			}
			if(fields.get(SearchDomainFields.PRODUCT_DIVISIONID)!= null){
				List<String> divisionids=fields.get(SearchDomainFields.PRODUCT_DIVISIONID);
				resultFields.put(SearchDomainFields.PRODUCT_DIVISIONID,divisionids.toString());
			}
			if(fields.get(SearchDomainFields.PRODUCT_PRODUCTGROUPIDS)!=null){
				List<String> divisionname=fields.get(SearchDomainFields.PRODUCT_PRODUCTGROUPIDS);
				resultFields.put(SearchDomainFields.PRODUCT_PRODUCTGROUPIDS,divisionname.toString());
			}
			if(fields.get(SearchDomainFields.PRODUCT_PRODUCTID)!=null){
				List<String> productId=fields.get(SearchDomainFields.PRODUCT_PRODUCTID);
				resultFields.put(SearchDomainFields.PRODUCT_PRODUCTID,productId.toString());
			}

			resultFieldList.add(resultFields);
		}

		cloudResponse.setResultFields(resultFieldList);
		cloudResponse.setResultsFound(count.intValue());

		/*if(logger.isDebugEnabled()){ logger.debug("Returning rest response :" +restResponse); */
		return cloudResponse;
	}
	
	@Override
	public AmazonSearchResponse searchAllLicenses(
			AmazonSearchRequest searchRequest) {

		AmazonSearchResponse cloudResponse = new AmazonSearchResponse();
		SearchRequest request = new SearchRequest();
		SearchResult response = new SearchResult();
		AmazonCloudSearchDomainClient client = new CloudSearchUtils(
				DomainType.LICENSE).getCloudSearchClient();

		StringBuilder queryStr = new StringBuilder("(and ");
		for (AmazonSearchFields searchField : searchRequest
				.getSearchFieldsList()) {
			queryStr.append(prepareSearchString(searchField));
		}
		// queryStr.append("(and "+SearchDomainFields.LICENSE_ARCHIVE_STATUS+":'ACTIVE' )");
		queryStr.append(")");

		System.out.println(queryStr);
		request = new SearchRequest().withQuery(queryStr.toString())
				.withQueryParser(QueryParser.Structured)
				.withStart((long) searchRequest.getStartAt())
				.withSize((long) searchRequest.getResultsPerPage());
		response = client.search(request);

		Hits hits = response.getHits();
		Long count = hits.getFound();
		System.out.println("results found:-" + count);

		List<Map<String, String>> resultFieldList = new ArrayList<Map<String, String>>();
		for (Hit hit : hits.getHit()) {
			Map<String, String> resultFields = new HashMap<String, String>();
			Map<String, List<String>> fields = hit.getFields();

			if (fields.get(SearchDomainFields.LICENSE_LICENSE_ID) != null) {
				List<String> licenseId = fields
						.get(SearchDomainFields.LICENSE_LICENSE_ID);
				resultFields.put(SearchDomainFields.LICENSE_LICENSE_ID,
						licenseId.toString());
			}
			if (fields.get(SearchDomainFields.LICENSE_CREATED_DATE) != null) {
				List<String> createdDate = fields
						.get(SearchDomainFields.LICENSE_CREATED_DATE);
				resultFields.put(SearchDomainFields.LICENSE_CREATED_DATE,
						createdDate.toString());
			}
			if (fields.get(SearchDomainFields.LICENSE_USER_ID) != null) {
				List<String> userId = fields
						.get(SearchDomainFields.LICENSE_USER_ID);
				resultFields.put(SearchDomainFields.LICENSE_USER_ID,
						userId.toString());
			}

			resultFieldList.add(resultFields);
		}

		cloudResponse.setResultFields(resultFieldList);
		cloudResponse.setResultsFound(count.intValue());

		/*
		 * if(logger.isDebugEnabled()){ logger.debug("Returning rest response :"
		 * +restResponse);
		 */
		return cloudResponse;
	}
	
	@Override
	public AmazonSearchResponse searchLicense(AmazonSearchRequest searchRequest) {

		AmazonSearchResponse cloudResponse = new AmazonSearchResponse();
		SearchRequest request=new SearchRequest();
		SearchResult response = new SearchResult();
		AmazonCloudSearchDomainClient client = new CloudSearchUtils(DomainType.LICENSE).getCloudSearchClient();
		if(searchRequest.getSearchFieldsList().isEmpty()){
			request = new SearchRequest().withQuery("(and matchall "+
					SearchDomainFields.LICENSE_ARCHIVE_STATUS+":'ACTIVE')").withQueryParser(QueryParser.Structured).withStart((long) searchRequest.getStartAt()).
					withSize((long) searchRequest.getResultsPerPage());
			response = client.search(request);
			System.out.println(request.getQuery());
		}
		else{
			StringBuilder queryStr = new StringBuilder("(and ");
			for(AmazonSearchFields searchField: searchRequest.getSearchFieldsList()) {
				queryStr.append(prepareSearchString(searchField)); 
			}
			queryStr.append("(and "+SearchDomainFields.LICENSE_ARCHIVE_STATUS+":'ACTIVE' )");
			queryStr.append(")");
			

			System.out.println(queryStr);
			request = new SearchRequest().withQuery(queryStr.toString()).withQueryParser(QueryParser.Structured).withStart((long) searchRequest.getStartAt()).withSize((long)searchRequest.getResultsPerPage());
			response = client.search(request);
		}
		Hits hits = response.getHits();
		Long count = hits.getFound();
		System.out.println("results found:-" +count);

		List<Map<String, String>> resultFieldList= new ArrayList<Map<String,String>>();
		for (Hit hit : hits.getHit()) {
			Map<String, String> resultFields = new HashMap<String, String>();
			System.out.println(hit);
			Map<String, List<String>> fields= hit.getFields();
			if(fields.get(SearchDomainFields.LICENSE_LICENSE_ID)!= null){
				List<String> licenseId = fields.get(SearchDomainFields.LICENSE_LICENSE_ID);
				resultFields.put(SearchDomainFields.LICENSE_LICENSE_ID, licenseId.toString());
			}
			resultFieldList.add(resultFields);
		}

		cloudResponse.setResultFields(resultFieldList);
		cloudResponse.setResultsFound(count.intValue());

		/*if(logger.isDebugEnabled()){ logger.debug("Returning rest response :" +restResponse); */
		return cloudResponse;
	}
	
	private String prepareSearchString(AmazonSearchFields searchField){
		String query = null;
		if(searchField.getValue().contains("@")){
			query =  "(or (  prefix field=" + searchField.getName() + " '" + searchField.getValue().replace("@", " ") + "')" + "( and " + searchField.getName() + " :'" + searchField.getValue().replace("@", " ") + "'))";
		} else if (searchField.getType().equals("int") || searchField.getType().equals("Integer")) {
			query = "(or ( and " + searchField.getName() + " :'" + searchField.getValue() + "'))";
		} else{
			query = "(or (  prefix field=" + searchField.getName() + " '" + searchField.getValue() + "')" 
					+ "( and " + searchField.getName() + " :'" + searchField.getValue() + "'))";
		}
		return query.replace("\\","") ;
	}
	
	@Override
	public AmazonSearchResponse searchProductsDivisions(AmazonSearchRequest searchRequest, List<Integer> divisionIds) {
		
		AmazonSearchResponse cloudResponse = new AmazonSearchResponse();
		SearchRequest request=new SearchRequest();
		SearchResult response = new SearchResult();
		String facate = "{\"" + SearchDomainFields.PRODUCT_DIVISIONID + "\":{\"sort\":\"bucket\",\"size\" : "+ divisionIds.size()+"}}" ;
		AmazonCloudSearchDomainClient client = new CloudSearchUtils(DomainType.PRODUCT).getCloudSearchClient();
		if(searchRequest.getSearchFieldsList().isEmpty()){
			request = new SearchRequest().withQuery("matchall").withQueryParser(QueryParser.Structured).withStart((long) searchRequest.getStartAt()).withSize((long)searchRequest.getResultsPerPage()).withFacet(facate);
			System.out.println(request.getQuery());
			response = client.search(request);
		}
		List<Map<String, String>> resultFieldList= new ArrayList<Map<String,String>>();
		Hits hits = response.getHits();
		Map<String,BucketInfo> divisionsList = response.getFacets() ;
		String divisionIdDomain = SearchDomainFields.PRODUCT_DIVISIONID ;
		BucketInfo divisionBucket = divisionsList.get(divisionIdDomain) ;
		List<Bucket> divisionBuckets = divisionBucket.getBuckets() ;
		for (Bucket bucket : divisionBuckets) {
			Map<String, String> resultFields = new HashMap<String, String>();
			if (bucket.getCount() > 0) {
				resultFields.put(SearchDomainFields.PRODUCT_DIVISIONID, bucket.getValue()) ;
				resultFieldList.add(resultFields) ;
			}
		}
		cloudResponse.setResultFields(resultFieldList);
		/*if(logger.isDebugEnabled()){ logger.debug("Returning rest response :" +restResponse); */
		return cloudResponse;
	}

	@Override
	public AmazonSearchResponse searchGroup(AmazonSearchRequest searchRequest) {
		AmazonSearchResponse cloudResponse = new AmazonSearchResponse();
		SearchRequest request=new SearchRequest();
		SearchResult response = new SearchResult();
		AmazonCloudSearchDomainClient client = new CloudSearchUtils(DomainType.GROUP).getCloudSearchClient();
		if(searchRequest.getSearchFieldsList().isEmpty()){
			request = new SearchRequest().withQuery("matchall").withQueryParser(QueryParser.Structured).withStart((long) searchRequest.getStartAt()).withSize((long)searchRequest.getResultsPerPage());
			response = client.search(request);
			System.out.println(request.getQuery());
		}
		else{
			StringBuilder queryStr = new StringBuilder("(and ");
			for(AmazonSearchFields searchField: searchRequest.getSearchFieldsList()) {
					if (searchField.getName().equals(SearchDomainFields.GROUP_ARCHIVESTATUS) ) {
						String [] searchValues = searchField.getValue().replace("[", "").replace("]", "").replace(" ", "").split(",") ;
						queryStr.append(" (or "); 
						for (String value : searchValues) {
							queryStr.append(searchField.getName() + ":'" + value + "' " ) ;
						}
						queryStr.append(" ) "); 
					} else if (searchField.getName().equals(SearchDomainFields.GROUP_COUNTRYCODE) ) {
						String [] searchValues = searchField.getValue().replace("[", "").replace("]", "").replace(" ", "").split(",") ;
						queryStr.append(" (or "); 
						for (String value : searchValues) {
							queryStr.append(searchField.getName() + ":'" + value + "' " ) ;
						}
						queryStr.append(" ) "); 
					} else if(searchField.getName().equals(SearchDomainFields.GROUP_CREATEDDATE)) {
						queryStr.append(" (or "); 
						for(String s : searchField.getValue().split(",")){
							queryStr.append(searchField.getName() + ":'" + s + "' " ) ;
						}
						queryStr.append(") ");
					} else if (searchField.getName().equals(SearchDomainFields.GROUP_CURRICULUMTYPE) ) {
						String [] searchValues = searchField.getValue().replace("[", "").replace("]", "").replace(" ", "").split(",") ;
						queryStr.append(" (or "); 
						for (String value : searchValues) {
							queryStr.append(searchField.getName() + ":'" + value + "' " ) ;
						}
						queryStr.append(" ) "); 
					} else if(searchField.getName().equals(SearchDomainFields.GROUP_GROUPID)) {
						queryStr.append(" (or "); 
						for(String s : searchField.getValue().split(",")){
							queryStr.append(searchField.getName() + ":'" + s + "' " ) ;
						}
						queryStr.append(") ");
					} else if (searchField.getName().equals(SearchDomainFields.GROUP_GROUPNAME) ) {
						String [] searchValues = searchField.getValue().replace("[", "").replace("]", "").replace(" ", "").split(",") ;
						queryStr.append(" (or "); 
						for (String value : searchValues) {
							queryStr.append(searchField.getName() + ":'" + value + "' " ) ;
						}
						queryStr.append(" ) "); 
					} else if(searchField.getName().equals(SearchDomainFields.GROUP_GROUPTYPE)) {
						queryStr.append(" (or "); 
						for(String s : searchField.getValue().split(",")){
							queryStr.append(searchField.getName() + ":'" + s + "' " ) ;
						}
						queryStr.append(") ");
					} else if(searchField.getName().equals(SearchDomainFields.GROUP_GROUPUNIQUEID)) {
						queryStr.append(" (or "); 
						for(String s : searchField.getValue().split(",")){
							queryStr.append(searchField.getName() + ":'" + s + "' " ) ;
						}
						queryStr.append(") ");
					} else if (searchField.getName().equals(SearchDomainFields.GROUP_LICENSES) ) {
						String [] searchValues = searchField.getValue().replace("[", "").replace("]", "").replace(" ", "").split(",") ;
						queryStr.append(" (or "); 
						for (String value : searchValues) {
							queryStr.append(searchField.getName() + ":'" + value + "' " ) ;
						}
						queryStr.append(" ) "); 
					} else if(searchField.getName().equals(SearchDomainFields.GROUP_PARENTIDS)) {
						queryStr.append(" (or "); 
						for(String s : searchField.getValue().split(",")){
							queryStr.append(searchField.getName() + ":'" + s + "' " ) ;
						}
						queryStr.append(") ");
					} else if (searchField.getName().equals(SearchDomainFields.GROUP_PRIMARYEMAILADDRESS) ) {
						String [] searchValues = searchField.getValue().replace("[", "").replace("]", "").replace(" ", "").split(",") ;
						queryStr.append(" (or "); 
						for (String value : searchValues) {
							queryStr.append(searchField.getName() + ":'" + value + "' " ) ;
						}
						queryStr.append(" ) "); 
					} else if(searchField.getName().equals(SearchDomainFields.GROUP_REGISTRATIONSTATUS)) {
						queryStr.append(" (or "); 
						for(String s : searchField.getValue().split(",")){
							queryStr.append(searchField.getName() + ":'" + s + "' " ) ;
						}
						queryStr.append(") ");
					} else {
						queryStr.append(prepareSearchString(searchField)); 
					} 
			}
			queryStr.append(")");
			System.out.println(queryStr);
			if(searchRequest.getResultsPerPage()!=null && searchRequest.getResultsPerPage() > 0)
				request = new SearchRequest().withQuery(queryStr.toString()).withSort(SearchDomainFields.GROUP_GROUPNAME +" asc").withQueryParser(QueryParser.Structured).withStart((long) searchRequest.getStartAt()).withSize((long)searchRequest.getResultsPerPage());
			else
				request = new SearchRequest().withQuery(queryStr.toString()).withSort(SearchDomainFields.GROUP_GROUPNAME +" asc").withQueryParser(QueryParser.Structured).withStart((long) searchRequest.getStartAt());
			response = client.search(request);
		}
		Hits hits = response.getHits();
		Long count = hits.getFound();

		List<Map<String, String>> resultFieldList= new ArrayList<Map<String,String>>();
		for (Hit hit : hits.getHit()) {
			Map<String, String> resultFields = new HashMap<String, String>();
			Map<String, List<String>> fields= hit.getFields();
			if(fields.get(SearchDomainFields.GROUP_ARCHIVESTATUS)!= null){
				List<String> archiveStatus = fields.get(SearchDomainFields.GROUP_ARCHIVESTATUS);
				resultFields.put(SearchDomainFields.GROUP_ARCHIVESTATUS, archiveStatus.toString());
			}
			if(fields.get(SearchDomainFields.GROUP_COUNTRYCODE) != null){
				List<String> countryCode = fields.get(SearchDomainFields.GROUP_COUNTRYCODE);
				resultFields.put(SearchDomainFields.GROUP_COUNTRYCODE, countryCode.toString());
			}
			if(fields.get(SearchDomainFields.GROUP_CREATEDDATE)!= null){
				List<String> createdDate = fields.get(SearchDomainFields.GROUP_CREATEDDATE);
				resultFields.put(SearchDomainFields.GROUP_CREATEDDATE, createdDate.toString());
			}
			if(fields.get(SearchDomainFields.GROUP_CURRICULUMTYPE)!= null){
				List<String> curriculum=fields.get(SearchDomainFields.GROUP_CURRICULUMTYPE);
				resultFields.put(SearchDomainFields.GROUP_CURRICULUMTYPE,curriculum.toString());
			}
			if(fields.get(SearchDomainFields.GROUP_GROUPID)!=null){
				List<String> groupID = fields.get(SearchDomainFields.GROUP_GROUPID);
				resultFields.put(SearchDomainFields.GROUP_GROUPID,groupID.toString());
			}
			if(fields.get(SearchDomainFields.GROUP_GROUPNAME)!=null){
				List<String> groupName=fields.get(SearchDomainFields.GROUP_GROUPNAME);
				resultFields.put(SearchDomainFields.GROUP_GROUPNAME,groupName.toString());
			}
			if(fields.get(SearchDomainFields.GROUP_GROUPTYPE)!= null){
				List<String> groupType = fields.get(SearchDomainFields.GROUP_GROUPTYPE);
				resultFields.put(SearchDomainFields.GROUP_GROUPTYPE, groupType.toString());
			}
			if(fields.get(SearchDomainFields.GROUP_GROUPUNIQUEID) != null){
				List<String> groupUniqueId = fields.get(SearchDomainFields.GROUP_GROUPUNIQUEID);
				resultFields.put(SearchDomainFields.GROUP_GROUPUNIQUEID, groupUniqueId.toString());
			}
			if(fields.get(SearchDomainFields.GROUP_LICENSES)!= null){
				List<String> licenses = fields.get(SearchDomainFields.GROUP_LICENSES);
				resultFields.put(SearchDomainFields.GROUP_LICENSES, licenses.toString());
			}
			if(fields.get(SearchDomainFields.GROUP_PARENTIDS)!= null){
				List<String> parentsIds=fields.get(SearchDomainFields.GROUP_PARENTIDS);
				resultFields.put(SearchDomainFields.GROUP_PARENTIDS,parentsIds.toString());
			}
			if(fields.get(SearchDomainFields.GROUP_PRIMARYEMAILADDRESS)!=null){
				List<String> emailAddress = fields.get(SearchDomainFields.GROUP_PRIMARYEMAILADDRESS);
				resultFields.put(SearchDomainFields.GROUP_PRIMARYEMAILADDRESS,emailAddress.toString());
			}
			if(fields.get(SearchDomainFields.GROUP_REGISTRATIONSTATUS)!=null){
				List<String> registrationStatus =fields.get(SearchDomainFields.GROUP_REGISTRATIONSTATUS);
				resultFields.put(SearchDomainFields.GROUP_REGISTRATIONSTATUS,registrationStatus.toString());
			}

			resultFieldList.add(resultFields);
		}

		cloudResponse.setResultFields(resultFieldList);
		cloudResponse.setResultsFound(count.intValue());

		return cloudResponse;
	}

	@Override
	public AmazonSearchResponse searchUserGroupMembership(
			AmazonSearchRequest searchRequest) {
		AmazonSearchResponse cloudResponse = new AmazonSearchResponse();
		SearchRequest request=new SearchRequest();
		SearchResult response = new SearchResult();
		AmazonCloudSearchDomainClient client = new CloudSearchUtils(DomainType.USERGROUPMEMBERSHIP).getCloudSearchClient();
		if(searchRequest.getSearchFieldsList().isEmpty()){
			request = new SearchRequest().withQuery("matchall").withQueryParser(QueryParser.Structured).withStart((long) searchRequest.getStartAt()).withSize((long)searchRequest.getResultsPerPage());
			response = client.search(request);
			System.out.println(request.getQuery());
		}
		else{
			StringBuilder queryStr = new StringBuilder("(and ");
			for(AmazonSearchFields searchField: searchRequest.getSearchFieldsList()) {
					if (searchField.getName().equals(SearchDomainFields.USER_GRP_ARCHIVESTATUS) ) {
						String [] searchValues = searchField.getValue().replace("[", "").replace("]", "").replace(" ", "").split(",") ;
						queryStr.append(" (or "); 
						for (String value : searchValues) {
							queryStr.append(searchField.getName() + ":'" + value + "' " ) ;
						}
						queryStr.append(" ) "); 
					} else if (searchField.getName().equals(SearchDomainFields.USER_GRP_DISMISSFLAG) ) {
						String [] searchValues = searchField.getValue().replace("[", "").replace("]", "").replace(" ", "").split(",") ;
						queryStr.append(" (or "); 
						for (String value : searchValues) {
							queryStr.append(searchField.getName() + ":'" + value + "' " ) ;
						}
						queryStr.append(" ) "); 
					} else if(searchField.getName().equals(SearchDomainFields.USER_GRP_GROUPID)) {
						queryStr.append(" (or "); 
						for(String s : searchField.getValue().split(",")){
							queryStr.append(searchField.getName() + ":'" + s + "' " ) ;
						}
						queryStr.append(") ");
					} else if (searchField.getName().equals(SearchDomainFields.USER_GRP_INVITATIONEXPIRYTIMESTAMP) ) {
						String [] searchValues = searchField.getValue().replace("[", "").replace("]", "").replace(" ", "").split(",") ;
						queryStr.append(" (or "); 
						for (String value : searchValues) {
							queryStr.append(searchField.getName() + ":'" + value + "' " ) ;
						}
						queryStr.append(" ) "); 
					} else if(searchField.getName().equals(SearchDomainFields.USER_GRP_INVITATIONSTATUS)) {
						queryStr.append(" (or "); 
						for(String s : searchField.getValue().split(",")){
							queryStr.append(searchField.getName() + ":'" + s + "' " ) ;
						}
						queryStr.append(") ");
					} else if (searchField.getName().equals(SearchDomainFields.USER_GRP_INVITATIONTIMESTAMP) ) {
						String [] searchValues = searchField.getValue().replace("[", "").replace("]", "").replace(" ", "").split(",") ;
						queryStr.append(" (or "); 
						for (String value : searchValues) {
							queryStr.append(searchField.getName() + ":'" + value + "' " ) ;
						}
						queryStr.append(" ) "); 
					} else if(searchField.getName().equals(SearchDomainFields.USER_GRP_ROLENAME)) {
						queryStr.append(" (or "); 
						for(String s : searchField.getValue().split(",")){
							queryStr.append(searchField.getName() + ":'" + s + "' " ) ;
						}
						queryStr.append(") ");
					} else if(searchField.getName().equals(SearchDomainFields.USER_GRP_REMINDERTIMESTAMP)) {
						queryStr.append(" (or "); 
						for(String s : searchField.getValue().split(",")){
							queryStr.append(searchField.getName() + ":'" + s + "' " ) ;
						}
						queryStr.append(") ");
					} else if (searchField.getName().equals(SearchDomainFields.USER_GRP_USERID) ) {
						String [] searchValues = searchField.getValue().replace("[", "").replace("]", "").replace(" ", "").split(",") ;
						queryStr.append(" (or "); 
						for (String value : searchValues) {
							queryStr.append(searchField.getName() + ":'" + value + "' " ) ;
						}
						queryStr.append(" ) "); 
					} else {
						queryStr.append(prepareSearchString(searchField)); 
					} 
			}
			queryStr.append(")");
			System.out.println(queryStr);
			request = new SearchRequest().withQuery(queryStr.toString()).withQueryParser(QueryParser.Structured).withStart((long) searchRequest.getStartAt()).withSize((long) searchRequest.getResultsPerPage());
			response = client.search(request);
		}
		Hits hits = response.getHits();
		Long count = hits.getFound();

		List<Map<String, String>> resultFieldList= new ArrayList<Map<String,String>>();
		for (Hit hit : hits.getHit()) {
			Map<String, String> resultFields = new HashMap<String, String>();
			Map<String, List<String>> fields= hit.getFields();
			if(fields.get(SearchDomainFields.USER_GRP_ARCHIVESTATUS)!= null){
				resultFields.put(SearchDomainFields.USER_GRP_ARCHIVESTATUS, fields.get(SearchDomainFields.USER_GRP_ARCHIVESTATUS).toString());
			}
			if(fields.get(SearchDomainFields.USER_GRP_DISMISSFLAG)!= null){
				resultFields.put(SearchDomainFields.USER_GRP_DISMISSFLAG, fields.get(SearchDomainFields.USER_GRP_DISMISSFLAG).toString());
			}
			if(fields.get(SearchDomainFields.USER_GRP_GROUPID)!= null){
				resultFields.put(SearchDomainFields.USER_GRP_GROUPID, fields.get(SearchDomainFields.USER_GRP_GROUPID).toString());
			}
			if(fields.get(SearchDomainFields.USER_GRP_INVITATIONEXPIRYTIMESTAMP)!= null){
				resultFields.put(SearchDomainFields.USER_GRP_INVITATIONEXPIRYTIMESTAMP, fields.get(SearchDomainFields.USER_GRP_INVITATIONEXPIRYTIMESTAMP).toString());
			}
			if(fields.get(SearchDomainFields.USER_GRP_INVITATIONSTATUS)!= null){
				resultFields.put(SearchDomainFields.USER_GRP_INVITATIONSTATUS, fields.get(SearchDomainFields.USER_GRP_INVITATIONSTATUS).toString());
			}
			if(fields.get(SearchDomainFields.USER_GRP_INVITATIONTIMESTAMP)!= null){
				resultFields.put(SearchDomainFields.USER_GRP_INVITATIONTIMESTAMP, fields.get(SearchDomainFields.USER_GRP_INVITATIONTIMESTAMP).toString());
			}
			if(fields.get(SearchDomainFields.USER_GRP_ROLENAME)!= null){
				resultFields.put(SearchDomainFields.USER_GRP_ROLENAME, fields.get(SearchDomainFields.USER_GRP_ROLENAME).toString());
			}
			if(fields.get(SearchDomainFields.USER_GRP_REMINDERTIMESTAMP)!= null){
				resultFields.put(SearchDomainFields.USER_GRP_REMINDERTIMESTAMP, fields.get(SearchDomainFields.USER_GRP_REMINDERTIMESTAMP).toString());
			}
			if(fields.get(SearchDomainFields.USER_GRP_USERID)!= null){
				resultFields.put(SearchDomainFields.USER_GRP_USERID, fields.get(SearchDomainFields.USER_GRP_USERID).toString());
			}
			resultFieldList.add(resultFields);
		}

		cloudResponse.setResultFields(resultFieldList);
		cloudResponse.setResultsFound(count.intValue());

		return cloudResponse;
	} 
	
	public boolean searchUserGroupMembershipForBritanico(
            String userId) {
        SearchRequest request=new SearchRequest();
        SearchResult response = new SearchResult();
        CloudSearchUtils cloudSearchUtils = new CloudSearchUtils(DomainType.USERGROUPMEMBERSHIP);
        AmazonCloudSearchDomainClient client = cloudSearchUtils.getCloudSearchClient();
        String britanicoId = cloudSearchUtils.getBritanicoGroupId();
        if (britanicoId == null || britanicoId.trim().equals("")) {
            return false;
        }       
            StringBuilder queryStr = new StringBuilder("(and userid:'"
                    + userId
                    + "' groupid:'"
                    + britanicoId
                    + "' archivestatus:'ACTIVE')");
            System.out.println(queryStr);
            request = new SearchRequest().withQuery(queryStr.toString()).withQueryParser(QueryParser.Structured).withStart(0L).withSize(1000L);
            response = client.search(request);
           
            return response.getHits().getFound() > 0 ? true : false;
    } 
	
	@Override
	public List<LicenceDto> searchAllLicensesForBritanico(
			String userId) {
		
		List<LicenceDto> listOfLicenseDTO = new ArrayList<LicenceDto>();
		List<String> productIdLst=null;
		SearchRequest request = new SearchRequest();
		SearchResult response = new SearchResult();
		AmazonCloudSearchDomainClient client = new CloudSearchUtils(
				DomainType.LICENSE).getCloudSearchClient();

		StringBuilder queryStr = new StringBuilder("(and userid:'"
				+ userId
				+ "')");

		System.out.println(queryStr);
		request = new SearchRequest().withQuery(queryStr.toString())
				.withQueryParser(QueryParser.Structured)
				.withStart(0L)
				.withSize(1000L);
		response = client.search(request);

		Hits hits = response.getHits();
	
		for (Hit hit : hits.getHit()) {
			Map<String, List<String>> fields = hit.getFields();
			productIdLst = new ArrayList<String>();
			String lId = "";
			DateTime lendDate = null;
			DateTime lstartDate = null;
			DateTime lcreatedDate = null;
			DateTime lexpiryDate = null;

			if (fields.get(SearchDomainFields.LICENSE_LICENSE_ID) != null) {
				List<String> licenseId = fields
						.get(SearchDomainFields.LICENSE_LICENSE_ID);
				lId = licenseId.get(0).toString();
			}
			List<String> createdDate = fields
					.get(SearchDomainFields.LICENSE_CREATED_DATE);
			if (fields.get(SearchDomainFields.LICENSE_CREATED_DATE) != null) {
				lcreatedDate = DateTime.parse(createdDate.get(0).toString());
			}
			List<String> startDate = fields
					.get(SearchDomainFields.LICENSE_LICENSE_START_DATE);
			if (fields.get(SearchDomainFields.LICENSE_LICENSE_START_DATE) != null) {
				lstartDate = DateTime.parse(startDate.get(0).toString());
			}
			List<String> endDate = fields
					.get(SearchDomainFields.LICENSE_LICENSE_END_DATE);
			if (fields.get(SearchDomainFields.LICENSE_LICENSE_END_DATE) != null) {
				lendDate = DateTime.parse(endDate.get(0).toString());
			}
			List<String> expiryDate = fields
					.get(SearchDomainFields.LICENSE_LICENSE_EXPIRY_DATE);
			if (fields.get(SearchDomainFields.LICENSE_LICENSE_EXPIRY_DATE) != null) {
				lexpiryDate = DateTime.parse(expiryDate.get(0).toString());
			}

			LicenceDto licenceDto = new LicenceDto(lId, lexpiryDate, false,
					true, true, false, false);

			if(lstartDate!=null)
			licenceDto.setStartDate(new LocalDate(lstartDate));
			if(lendDate!=null)
			licenceDto.setEndDate(new LocalDate(lendDate));
			if(lcreatedDate!=null)
			licenceDto.setCreatedDate(lcreatedDate);

			if (fields.get(SearchDomainFields.ACTIVATION_CODE) != null) {
				List<String> activationCode = fields
						.get(SearchDomainFields.ACTIVATION_CODE);
				licenceDto.setActivationCode(activationCode.get(0).toString());
			}

			if (fields.get(SearchDomainFields.PRODUCT_PRODUCTID) != null) {
				List<String> productId = fields
						.get(SearchDomainFields.PRODUCT_PRODUCTID);
				productIdLst.add(productId.get(0).toString());
				licenceDto.setProducts(getProductDetails(productId.get(0)
						.toString()));
				licenceDto.setProductIds(productIdLst);
			}

			if (fields.get(SearchDomainFields.LICENSE_LICENSE_TYPE) != null) {
				List<String> licenseType = fields
						.get(SearchDomainFields.LICENSE_LICENSE_TYPE);
				if (licenseType.get(0).toString()
						.equals(LicenceType.ROLLING.toString())) {
					List<String> timePeriod = fields
							.get(SearchDomainFields.LICENSE_TIME_PERIOD);
					int tp = (int) Integer.parseInt(timePeriod.get(0));
					List<String> unitType = fields
							.get(SearchDomainFields.LICENSE_UNIT_TYPE);
					List<String> beginOn = fields
							.get(SearchDomainFields.LICENSE_BEGIN_ON);

					RollingLicenceDetailDto rollingDto = new RollingLicenceDetailDto(
							RollingBeginOn.valueOf(beginOn.get(0)),
							RollingUnitType.valueOf(unitType.get(0)), tp,
							DateTime.parse(createdDate.get(0).toString()));
					if (lstartDate != null)
						rollingDto.setStartDate(new LocalDate(lstartDate));
					if (lendDate != null)
						rollingDto.setEndDate(new LocalDate(lendDate));
					licenceDto.setLicenceDetail((LicenceDetailDto) rollingDto);
				} else if (licenseType.get(0).toString()
						.equals(LicenceType.USAGE.toString())) {

					UsageLicenceDetailDto usageDto = new UsageLicenceDetailDto();
					List<String> allowedUsage = fields
							.get(SearchDomainFields.CLAIM_TICKET_ALLOWEDUSAGES);
					usageDto.setAllowedUsages((int) Integer
							.parseInt(allowedUsage.get(0)));
					if (lstartDate != null)
						usageDto.setStartDate(new LocalDate(lstartDate));
					if (lendDate != null)
						usageDto.setEndDate(new LocalDate(lendDate));
					licenceDto.setLicenceDetail((LicenceDetailDto) usageDto);

				} else if (licenseType.get(0).toString()
						.equals(LicenceType.CONCURRENT.toString())) {
					StandardConcurrentLicenceDetailDto standardConcurrentLicenceDetailDto = new StandardConcurrentLicenceDetailDto(
							1, 1);

					if (lstartDate != null)
						standardConcurrentLicenceDetailDto
								.setStartDate(new LocalDate(lstartDate));
					if (lendDate != null)
						standardConcurrentLicenceDetailDto
								.setEndDate(new LocalDate(lendDate));
					licenceDto
							.setLicenceDetail((LicenceDetailDto) standardConcurrentLicenceDetailDto);
				}
			}
			listOfLicenseDTO.add(licenceDto);
		}

		return listOfLicenseDTO;
	}
	
	private EnforceableProductDto getProductDetails(String productId){
		SearchRequest request = new SearchRequest();
		SearchResult response = new SearchResult();
		AmazonCloudSearchDomainClient client = new CloudSearchUtils(
				DomainType.PRODUCT).getCloudSearchClient();

		StringBuilder queryStr = new StringBuilder("(and productid:'"
				+ productId
				+ "')");

		System.out.println(queryStr);
		request = new SearchRequest().withQuery(queryStr.toString())
				.withQueryParser(QueryParser.Structured)
				.withStart(0L)
				.withSize(1000L);
		response = client.search(request);

		Hits hits = response.getHits();
		EnforceableProductDto epd = new EnforceableProductDto();
		for (Hit hit : hits.getHit()) {
			Map<String, List<String>> fields= hit.getFields();
			if(fields.get(SearchDomainFields.PRODUCT_PRODUCTNAME)!= null){
				List<String> productname = fields.get(SearchDomainFields.PRODUCT_PRODUCTNAME);
				epd.setName(productname.get(0));
			}
			epd.setProductId(productId);
		}
		return epd;
	}
}
