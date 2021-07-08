package com.oup.eac.integration.rest.template;

import java.util.Collections;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.oup.eac.common.utils.ProxyUtils;

public class EacRestTemplate {

	// Version constants
	public static final String API_VERSION_BASE_NO_ACCEPT_HEADER = "*/*";
	public static final String API_VERSION_BASE = "application/json";
	public static final String API_VERSION_0_1 = "application/x.oup-com.eps.v0.1+json";
	public static final String API_VERSION_0_2 = "application/x.oup-com.eps.v0.2+json";
	public static final String API_VERSION_LATEST = "application/x.oup-com.eps+json";
	protected static final String API_VERSION_INVALID = "invalidVersion";
	
	public static final String PROXY_HOST = "ouparray.oup.com";
	public static final Integer PROXY_PORT = 8080;

	/**
	 * getRestTemplate.
	 * 
	 * @return
	 */
	
	public RestTemplate getRestTemplate(String apiVersion) {

		RestTemplate restTemplate = new RestTemplate(
				new BufferingClientHttpRequestFactory(
						simpleClientHttpRequestFactory()));
		ClientHttpRequestInterceptor interceptor = new JsonMimeInterceptor(
				apiVersion);
		restTemplate.setInterceptors(Collections.singletonList(interceptor));
		return restTemplate;
	}

	public <T> T postForObject(String apiVersion, String url, Object request,
			Class<T> responseType, Object... uriVariables)
			throws RestClientException {

		RestTemplate restTemplate = new RestTemplate(
				new BufferingClientHttpRequestFactory(
						simpleClientHttpRequestFactory()));
		
		ClientHttpRequestInterceptor interceptor = new JsonMimeInterceptor(
				apiVersion);
		restTemplate.setInterceptors(Collections.singletonList(interceptor));
		return restTemplate.postForObject(url, request, responseType);
	}
	
	public <T> T getForObject(String apiVersion, String url, Object request,
			Class<T> responseType, Object... uriVariables)
			throws RestClientException {

		RestTemplate restTemplate = new RestTemplate(
				new BufferingClientHttpRequestFactory(
						simpleClientHttpRequestFactory()));
		ClientHttpRequestInterceptor interceptor = new JsonMimeInterceptor(
				apiVersion);
		restTemplate.setInterceptors(Collections.singletonList(interceptor));
		return restTemplate.getForObject(url, responseType, request);
	}
	
	public <T> T putForObject(String apiVersion, String url, HttpEntity<?> requestEntity,
			Class<T> responseType, Object... uriVariables)
			throws RestClientException {
		ResponseEntity<T> response = null;
		try {
		RestTemplate restTemplate = new RestTemplate(
				new BufferingClientHttpRequestFactory(
						simpleClientHttpRequestFactory()));
		ClientHttpRequestInterceptor interceptor = new JsonMimeInterceptor(
				apiVersion);
		restTemplate.setInterceptors(Collections.singletonList(interceptor));
		response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, responseType, uriVariables) ;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response.getBody();
	}
	
	private SimpleClientHttpRequestFactory simpleClientHttpRequestFactory() {
		if(!ProxyUtils.isProxyEnabled()) {
			return new SimpleClientHttpRequestFactory();
		} else {  
			SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
	        factory.setProxy(ProxyUtils.getHTTPProxy());
	        return factory;
		}
	}
}