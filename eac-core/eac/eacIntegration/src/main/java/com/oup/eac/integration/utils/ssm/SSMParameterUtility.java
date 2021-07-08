package com.oup.eac.integration.utils.ssm;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.AmazonWebServiceResponse;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.DefaultRequest;
import com.amazonaws.Request;
import com.amazonaws.Response;
import com.amazonaws.auth.AWS4Signer;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.http.AmazonHttpClient;
import com.amazonaws.http.ExecutionContext;
import com.amazonaws.http.HttpMethodName;
import com.amazonaws.http.HttpResponse;
import com.amazonaws.http.HttpResponseHandler;
import com.amazonaws.util.IOUtils;
import com.amazonaws.util.json.JSONArray;
import com.amazonaws.util.json.JSONException;
import com.amazonaws.util.json.JSONObject;
import com.oup.eac.common.utils.json.GsonUtil;
import com.oup.eac.common.utils.spring.ApplicationContextSupport;


public class SSMParameterUtility {
	
    private static  String AWS_SSM_URL;
    private static  String AWS_SSM_PARAMS;
	private static  String AWS_SSM_PARAMS_PREFIX;
	private static  String AWS_SSM_REGION;
	private static  String AWS_SSM_SERVICE;
	private static  String AWS_SSM_ACCESS_KEY_ID;
	private static  String AWS_SSM_SECRET_KEY; 
	private static  Map<String, String> SSM_PARAM_MAP;
	
	
	
	public enum SSMPARAMETERNAMES {
		CES_ENV_NAME
	    ,EAC_TRUSTED_SYSTEM_USERNAME
	    ,EAC_TRUSTED_SYSTEM_PASSWORD
	    ,CES_API_KEY,ENCRYPT_PUBLIC_KEY
	    ,AUTHENTICATE_USER_URL
	    ,CES_API_GATEWAY_ID
	    ,CEB_USER_DATASYNC_ENDPOINT
	    ,DATASYNC_SQS_NAME                     
	}
	
	public static String getParameterValue(String parameterName) throws Exception {
		SSMParameterUtility ssmParameterUtility = (SSMParameterUtility)ApplicationContextSupport.getBean("ssmParameterUtility");
		if(SSM_PARAM_MAP == null){
			SSM_PARAM_MAP = ssmParameterUtility.getSSMParameters();
		}
		return SSM_PARAM_MAP.get(parameterName);
	}
	
	//@Cacheable(value=CacheConstants.CACHE_NAME_SSMPARAMETER_LIST, key="T(com.oup.eac.common.cache.CacheKeygen).generateKey('')")
	public Map<String,String> getSSMParameters() throws Exception {
		Map<String,String> ssmParameters = new HashMap<String, String>();
		//Instantiate the request
		Request<Void> request = new DefaultRequest<Void>(AWS_SSM_SERVICE); //Request to ElasticSearch
		request.setHttpMethod(HttpMethodName.POST);
		request.addHeader("Content-Type","application/x-amz-json-1.1");
		request.addHeader("X-Amz-Target", "AmazonSSM.GetParameters");
		request.setEndpoint(URI.create(AWS_SSM_URL));
		request.setContent(new ByteArrayInputStream(createSSMRequestBody().getBytes("UTF8")));
		AWSCredentials credentials = new BasicAWSCredentials(AWS_SSM_ACCESS_KEY_ID,AWS_SSM_SECRET_KEY);
		//Sign it...
		AWS4Signer signer = new AWS4Signer();
		signer.setRegionName(AWS_SSM_REGION);
		signer.setServiceName(request.getServiceName());
		signer.sign(request, credentials);

		//Execute it and get the response...
		AmazonHttpClient httpClient = new AmazonHttpClient(new ClientConfiguration());
		Response<SSMParameterResponse> rsp = httpClient.execute(request, new SSMParameterResponseHandler(), new StringErrorResponseHandler(), new ExecutionContext(true));
		
		for(SSMParameterDescription param : rsp.getAwsResponse().getParameters()){
			ssmParameters.put(param.getName().replaceAll(AWS_SSM_PARAMS_PREFIX, ""), param.getValue());
		}
		return ssmParameters;
	  }

	private String createSSMRequestBody() throws JSONException {
		JSONArray names = new JSONArray();
		String[] params = AWS_SSM_PARAMS.split(",");
		for (String param : params){
			names.put(AWS_SSM_PARAMS_PREFIX+param);
		}
		
		JSONObject obj = new JSONObject();
		obj.put("Names", names);
		obj.put("WithDecryption", true);
		return obj.toString();
	}

	public class StringResponseHandler implements HttpResponseHandler<AmazonWebServiceResponse<String>> {

	    @Override
	    public AmazonWebServiceResponse<String> handle(com.amazonaws.http.HttpResponse response) throws IOException {

	        AmazonWebServiceResponse<String> awsResponse = new AmazonWebServiceResponse<>();

	        //putting response string in the result, available outside the handler
	        awsResponse.setResult((String) IOUtils.toString(response.getContent()));

	        return awsResponse;
	    }

	    @Override
	    public boolean needsConnectionLeftOpen() {
	        return false;
	    }

	}
	
	public class SSMParameterResponseHandler implements HttpResponseHandler<AmazonWebServiceResponse<SSMParameterResponse>> {

	    @Override
	    public AmazonWebServiceResponse<SSMParameterResponse> handle(com.amazonaws.http.HttpResponse response) throws IOException {

	        AmazonWebServiceResponse<SSMParameterResponse> awsResponse = new AmazonWebServiceResponse<>();

	        //putting response string in the result, available outside the handler
	        awsResponse.setResult((SSMParameterResponse)GsonUtil.jsonToObject((String) IOUtils.toString(response.getContent()), new SSMParameterResponse()));

	        return awsResponse;
	    }

	    @Override
	    public boolean needsConnectionLeftOpen() {
	        return false;
	    }

	}
	


	public class StringErrorResponseHandler implements HttpResponseHandler<AmazonServiceException>{

		@Override
		public AmazonServiceException handle(HttpResponse response)
				throws Exception {
			String text = IOUtils.toString(response.getContent());
			AmazonServiceException exception = new AmazonServiceException(text);
			return exception;
		}

		@Override
		public boolean needsConnectionLeftOpen() {
			return false;
		}

	}



	public static String getAWS_SSM_URL() {
		return AWS_SSM_URL;
	}

	public static void setAWS_SSM_URL(String aWS_SSM_URL) {
		AWS_SSM_URL = aWS_SSM_URL;
	}

	public static String getAWS_SSM_PARAMS() {
		return AWS_SSM_PARAMS;
	}

	public static void setAWS_SSM_PARAMS(String aWS_SSM_PARAMS) {
		AWS_SSM_PARAMS = aWS_SSM_PARAMS;
	}

	public static String getAWS_SSM_PARAMS_PREFIX() {
		return AWS_SSM_PARAMS_PREFIX;
	}

	public static void setAWS_SSM_PARAMS_PREFIX(String aWS_SSM_PARAMS_PREFIX) {
		AWS_SSM_PARAMS_PREFIX = "/"+aWS_SSM_PARAMS_PREFIX+"/";
	}

	public static String getAWS_SSM_REGION() {
		return AWS_SSM_REGION;
	}

	public static void setAWS_SSM_REGION(String aWS_SSM_REGION) {
		AWS_SSM_REGION = aWS_SSM_REGION;
	}

	public static String getAWS_SSM_SERVICE() {
		return AWS_SSM_SERVICE;
	}

	public static void setAWS_SSM_SERVICE(String aWS_SSM_SERVICE) {
		AWS_SSM_SERVICE = aWS_SSM_SERVICE;
	}

	public static String getAWS_SSM_ACCESS_KEY_ID() {
		return AWS_SSM_ACCESS_KEY_ID;
	}

	public static void setAWS_SSM_ACCESS_KEY_ID(String aWS_SSM_ACCESS_KEY_ID) {
		AWS_SSM_ACCESS_KEY_ID = aWS_SSM_ACCESS_KEY_ID;
	}

	public static String getAWS_SSM_SECRET_KEY() {
		return AWS_SSM_SECRET_KEY;
	}

	public static void setAWS_SSM_SECRET_KEY(String aWS_SSM_SECRET_KEY) {
		AWS_SSM_SECRET_KEY = aWS_SSM_SECRET_KEY;
	}
	
	
}
