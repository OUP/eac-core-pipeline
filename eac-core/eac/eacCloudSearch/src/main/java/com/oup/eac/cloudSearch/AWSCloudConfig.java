package com.oup.eac.cloudSearch;

import java.util.ResourceBundle;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.util.Base64;

public class AWSCloudConfig {
	
	private static String AWS_ACCESSKEY;
	
	private static String AWS_SECRETKEY;
	
	private static String SEARCH_ENDPOINT;
	
	private static String DOCUMENT_ENDPOINT;
	
	static {
		ResourceBundle resourceBundle = ResourceBundle.getBundle("acesCloudSearch");
		//erightsUsername = resourceBundle.getString("erights.conn.username");
		//erightsPassword = resourceBundle.getString("erights.conn.password");
		AWS_ACCESSKEY = resourceBundle.getString("aws.searchdoc.accesskey");
		AWS_SECRETKEY = resourceBundle.getString("aws.searchdoc.secretkey");
		SEARCH_ENDPOINT = resourceBundle.getString("aws.user.search.endpoint");
		DOCUMENT_ENDPOINT = resourceBundle.getString("aws.user.document.endpoint");
		SEARCH_ENDPOINT = resourceBundle.getString("aws.activationcode.search.endpoint");
		DOCUMENT_ENDPOINT = resourceBundle.getString("aws.activationcode.document.endpoint");
		
	}
	
	
	public static AmazonCloudSearchRequest getCloudSearchClient() {
		AWSCredentials awsCredentials = new BasicAWSCredentials(Base64.decode(AWS_ACCESSKEY).toString(), Base64.decode(AWS_SECRETKEY).toString());
		System.out.println("got access and secrest key as : "+ AWS_SECRETKEY+" access key as: "+ AWS_ACCESSKEY);
		AmazonCloudSearchRequest request = new AmazonCloudSearchRequest(awsCredentials);
		request.setSearchEndpoint(SEARCH_ENDPOINT);
		System.out.println("Request : "+ request.getSearchEndpoint());
		request.setDocumentEndpoint(DOCUMENT_ENDPOINT);
		System.out.println("Request : "+ request.getDocumentEndpoint());
		return request;
	}
	
	// TODO :- remove toString method
	@Override
	public String toString() {
		
		return "access key :" +AWS_ACCESSKEY +"\n secret key: " +AWS_SECRETKEY;
	}
	
	public static void main(String[] args){
		getCloudSearchClient();
	}
}
