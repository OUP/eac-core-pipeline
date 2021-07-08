package com.oup.eac.integration.rest.template;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.oup.eac.common.utils.json.GsonUtil;
import com.oup.eac.integration.utils.authenticatication.AuthenticateResponse;
import com.oup.eac.integration.utils.datasync.DataSyncResponse;
import com.oup.eac.integration.utils.ssm.SSMParameterUtility;
import com.oup.eac.integration.utils.ssm.SSMParameterUtility.SSMPARAMETERNAMES;


public class AcesRestTemplate {

	private static Logger logger = Logger.getLogger(AcesRestTemplate.class);
	
	public AuthenticateResponse postForAuthenticateUser(Object request)
			throws Exception {
		ResponseEntity<String> respEntity =null;
		AuthenticateResponse resp = null;
		try{

			String 	apiUrl = SSMParameterUtility.getParameterValue(SSMPARAMETERNAMES.AUTHENTICATE_USER_URL.name());
			
			HttpEntity entityReq = getOpenRestEntity(request);
			
			RestTemplate restTemplate = getRequestTemplateEntity();
			
			respEntity = restTemplate.exchange(apiUrl, HttpMethod.POST, entityReq, String.class);

			resp = (AuthenticateResponse)GsonUtil.jsonToObject(respEntity.getBody(), new AuthenticateResponse());
			
		}catch(Exception e){
			logger.error("error in postForAuthenticateUser method: ",e);
		    resp = new AuthenticateResponse();
			resp.setStatus("error");
			resp.setMessage(e.getMessage());
			return resp;
		}
		return resp;
	}
	
	public DataSyncResponse postUserDataSync(Object request)
			throws Exception {
		ResponseEntity<String> respEntity =null;
		DataSyncResponse resp = null;
		try{

			String 	apiUrl = SSMParameterUtility.getParameterValue(SSMPARAMETERNAMES.CEB_USER_DATASYNC_ENDPOINT.name());
			
			HttpEntity entityReq = getOpenRestEntity(request);
			
			RestTemplate restTemplate = getRequestTemplateEntity();
			
			respEntity = restTemplate.exchange(apiUrl, HttpMethod.PUT, entityReq, String.class);	

			resp = (DataSyncResponse)GsonUtil.jsonToObject(respEntity.getBody(), new DataSyncResponse());
			
		}catch(Exception e){
			logger.error("error in postForAuthenticateUser method: ",e);
		    resp = new DataSyncResponse();
			resp.setStatus("error");
			resp.setMessage(e.getMessage());
			return resp;
		}
		return resp;
	}
	
	private HttpEntity getOpenRestEntity(Object request){
		HttpEntity entityReq = null;
		try{
			String  apiKey = SSMParameterUtility.getParameterValue(SSMPARAMETERNAMES.CES_API_KEY.name());
			HttpHeaders headers = new HttpHeaders();		
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("X-API-KEY",apiKey);
			String req = GsonUtil.objectToJsonWithDateFormat(request);
			entityReq = new HttpEntity(req,headers);
		
		}catch(Exception e){
			logger.error("error in getRestOpenHeader method: ",e);
		}
		return entityReq;
	}
	
	private RestTemplate getRequestTemplateEntity(){
		RestTemplate restTemplate = null;
		try{
			restTemplate = new RestTemplate(new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()));
			List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
			interceptors.add(new RequestResponseLoggingInterceptor());
			restTemplate.setInterceptors(interceptors);
		
		}catch(Exception e){
			logger.error("error in getRequestTemplateEntity method: ",e);
		}
		return restTemplate;
	}

}