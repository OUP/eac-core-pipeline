package com.oup.eac.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.constants.SearchDomainFields;
import com.oup.eac.cloudSearch.search.impl.AmazonCloudSearchServiceImpl;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.Platform;
import com.oup.eac.domain.UserDetailsReport;
import com.oup.eac.domain.search.AmazonSearchFields;
import com.oup.eac.domain.search.AmazonSearchRequest;
import com.oup.eac.domain.search.AmazonSearchResponse;
import com.oup.eac.domain.utils.audit.AuditLogger;
import com.oup.eac.dto.CustomerSearchCriteria;
import com.oup.eac.integration.facade.ErightsRestFacade;
import com.oup.eac.integration.facade.exceptions.ErightsException;
import com.oup.eac.service.ServiceLayerException;
import com.oup.eac.service.UserDetailsReportService;
import com.oup.eac.service.util.ConvertSearch;
import com.oup.eac.ws.rest.v1.CreatePlatformResponse;
import com.oup.eac.ws.rest.v1.GenerateReportResponse;
import com.oup.eac.ws.rest.v1.UpdatePlatformResponse;

@Service("userDetailsReportService")
public class UserDetailsReportServiceImpl implements UserDetailsReportService {
	private final ErightsRestFacade erightsRestFacade;
	
	@Autowired
	public UserDetailsReportServiceImpl(ErightsRestFacade erightsRestFacade) {
		this.erightsRestFacade = erightsRestFacade ;
		
	}
	
	@Override
	public List<String> generateUserDetailsReport(UserDetailsReport userDetailsReport) {
		GenerateReportResponse response = erightsRestFacade.generateUserDetailsReport(userDetailsReport) ;
		List<String> responseMessages = new ArrayList<String>();
		if ( response.getStatus().equalsIgnoreCase("SCHEDULED")) {
			responseMessages.add(response.getStatus()) ;
		} else {
			if (response.getErrorMessage() != null ) {
				responseMessages.add(response.getErrorMessage()) ;
			} else {
				Map<String, String> map =  response.getErrorMessages();
				for(Entry<String, String> element : map.entrySet()) {
					responseMessages.add(element.getValue());
				}
			}
		}
		return responseMessages;
	}
	
	@Override
	public String getUserIdFromUsername(final CustomerSearchCriteria customerSearchCriteria){
		AmazonSearchRequest request = new AmazonSearchRequest();
    	
    	request.setStartAt(0);
    	request.setResultsPerPage(20);
    	
    	List<AmazonSearchFields> searchFieldsList = new ArrayList<AmazonSearchFields>();
    	List<Customer> customers = null;
    	if(customerSearchCriteria.getUsername() != null){
    		AmazonSearchFields searchField = new AmazonSearchFields();
    		searchField.setName(SearchDomainFields.USER_USERNAME);
    		searchField.setValue(customerSearchCriteria.getUsername());
    		searchField.setType("String");
    		searchFieldsList.add(searchField);
    	}
    	List<String> searchResultFieldsList = new ArrayList<String>();
		searchResultFieldsList.add(SearchDomainFields.USER_USERID);
		searchResultFieldsList.add(SearchDomainFields.USER_USERNAME);
		
		HashMap<String, String> hMap1 = new HashMap<String, String>();
		hMap1.put("createddate", "desc");
		
		request.setSortFields(hMap1);
		request.setSearchResultFieldsList(searchResultFieldsList);
		
    	request.setSearchFieldsList(searchFieldsList);
    	
    	System.out.println(request);
    	
    	AmazonCloudSearchServiceImpl cloudSearch = new AmazonCloudSearchServiceImpl();
    	AmazonSearchResponse response = null;
    	String userId = null ;
    	
		response = cloudSearch.searchUser(request,true, true);
		ConvertSearch convert = new ConvertSearch();
    	customers = convert.convertCustomer(response);
    	for (Customer customer : customers ) {
    		if (customer.getUsername().equalsIgnoreCase(customerSearchCriteria.getUsername())) {
    			userId = customer.getId() ;
    		}
    	}
		AuditLogger.logEvent("Search Customers for User Details Report", " Username:"+customerSearchCriteria.getUsername(), AuditLogger.customerSearchCriteria(customerSearchCriteria));
		return userId;
	}
}
