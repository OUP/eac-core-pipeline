package com.oup.eac.integration.rest.template;

import java.io.IOException;
import java.io.StringWriter;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
 
public class RequestResponseLoggingInterceptor implements ClientHttpRequestInterceptor {
 
	private static Logger log = Logger.getLogger(RequestResponseLoggingInterceptor.class);
	
	public static final String ACCEPT = "Accept";
	public static final String API_VERSION_BASE = "application/json";
 
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
    	HttpHeaders headers = request.getHeaders();
		headers.remove(ACCEPT);
		headers.add(ACCEPT,API_VERSION_BASE);
        logRequest(request, body);
        ClientHttpResponse response = execution.execute(request, body);
        logResponse(response);
        return response;
    }
 
    private void logRequest(HttpRequest request, byte[] body) throws IOException {
            log.info("===========================request begin================================================");
            log.info("URI         : {}"+ request.getURI());
            log.info("Method      : {}"+ request.getMethod());
            //log.info("Headers     : {}"+ request.getHeaders());
            //log.info("Request body: {}"+ new String(body, "UTF-8"));
            log.info("==========================request end================================================");
    }
 
    private void logResponse(ClientHttpResponse response) throws IOException {
    		StringWriter writer = new StringWriter();
    		IOUtils.copy(response.getBody(),writer);
            log.info("============================response begin==========================================");
            log.info("Status code  : {}"+ response.getStatusCode());
            log.info("Status text  : {}"+ response.getStatusText());
            //log.info("Headers      : {}"+ response.getHeaders());
            //log.info("Response body: {}"+ writer.toString());
            log.info("=======================response end=================================================");
    }
}
