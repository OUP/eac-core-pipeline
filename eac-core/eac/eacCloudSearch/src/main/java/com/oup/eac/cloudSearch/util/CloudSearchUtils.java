package com.oup.eac.cloudSearch.util;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cloudsearchdomain.AmazonCloudSearchDomainClient;
import com.amazonaws.services.cloudsearchdomain.model.Hits;
import com.amazonaws.services.cloudsearchdomain.model.QueryParser;
import com.amazonaws.services.cloudsearchdomain.model.SearchRequest;
import com.amazonaws.services.cloudsearchdomain.model.SearchResult;
import com.oup.eac.common.utils.EACSettings;

public class CloudSearchUtils {
	
	AmazonCloudSearchDomainClient cloudSearchClient;
	
	public static enum DomainType {
		USER, PRODUCT, PRODUCTGROUP, ACTIVATIONCODE, LICENSE, GROUP, USERGROUPMEMBERSHIP;
	}
	
	private static String AWS_ACCESSKEY;

	private static String AWS_SECRETKEY;

	private static String SEARCH_USER_ENDPOINT;
	
	private static String SEARCH_PRODUCT_ENDPOINT;
	
	private static String SEARCH_PRODUCTGROUP_ENDPOINT;
	
	private static String SEARCH_ACTIVATIONCODE_ENDPOINT;
	
	private static String SEARCH_LICENSE_ENDPOINT;
	
	private static String SEARCH_GROUP_ENPOINT ;
	
	private static String SEARCH_USER_GROUP_MEMBERSHIP ;
	
	private static String BRITANICO_GROUP_ID; 

	static {
		AWS_ACCESSKEY = EACSettings.getProperty("aws.searchdoc.accesskey");
		AWS_SECRETKEY = EACSettings.getProperty("aws.searchdoc.secretkey");
		SEARCH_USER_ENDPOINT = EACSettings.getProperty("aws.user.search.endpoint");
		SEARCH_PRODUCT_ENDPOINT = EACSettings.getProperty("aws.product.search.endpoint");
		SEARCH_PRODUCTGROUP_ENDPOINT = EACSettings.getProperty("aws.productgroup.search.endpoint");
		SEARCH_ACTIVATIONCODE_ENDPOINT = EACSettings.getProperty("aws.activationcode.search.endpoint");
		SEARCH_LICENSE_ENDPOINT = EACSettings.getProperty("aws.license.search.endpoint");
		SEARCH_GROUP_ENPOINT = EACSettings.getProperty("aws.group.search.endpoint");
		SEARCH_USER_GROUP_MEMBERSHIP = EACSettings.getProperty("aws.usergroupmembership.search.endpoint");
		BRITANICO_GROUP_ID = EACSettings.getProperty("britanico.group.id");
	}
	
	public CloudSearchUtils(DomainType domainType) {
		AWSCredentials awsCredentials = new BasicAWSCredentials(AWS_ACCESSKEY, AWS_SECRETKEY);
		cloudSearchClient = new AmazonCloudSearchDomainClient( awsCredentials).withRegion(Regions.EU_WEST_1);
		if (domainType == DomainType.USER) {
			cloudSearchClient.setEndpoint(SEARCH_USER_ENDPOINT);
		} else if (domainType == DomainType.PRODUCT) {
			cloudSearchClient.setEndpoint(SEARCH_PRODUCT_ENDPOINT);
		} else if (domainType == DomainType.PRODUCTGROUP) {
			cloudSearchClient.setEndpoint(SEARCH_PRODUCTGROUP_ENDPOINT);
		} else if (domainType == DomainType.ACTIVATIONCODE) {
			cloudSearchClient.setEndpoint(SEARCH_ACTIVATIONCODE_ENDPOINT);
		} else if (domainType == DomainType.LICENSE ) {
			cloudSearchClient.setEndpoint(SEARCH_LICENSE_ENDPOINT);
		}  else if (domainType == DomainType.GROUP) {
			cloudSearchClient.setEndpoint(SEARCH_GROUP_ENPOINT);
		} else if (domainType == DomainType.USERGROUPMEMBERSHIP ) {
			cloudSearchClient.setEndpoint(SEARCH_USER_GROUP_MEMBERSHIP);
		}
	}

	public AmazonCloudSearchDomainClient getCloudSearchClient() {
		return cloudSearchClient;
	}

	public void setCloudSearchClient(AmazonCloudSearchDomainClient cloudSearchClient) {
		this.cloudSearchClient = cloudSearchClient;
	}
	
    public final Hits searchDocument(final String field, final String value) {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setQueryParser(QueryParser.Structured);
        searchRequest.setQuery(field + ":" + value);
		SearchResult searchResult = cloudSearchClient.search(searchRequest);
        Hits hitsResult = searchResult.getHits();
        return hitsResult;
    }
    
    public String getBritanicoGroupId(){
    	return BRITANICO_GROUP_ID;
    }
}
