package com.oup.eac.integration.rest.template;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import com.oup.eac.common.utils.ProxyUtils;

public class JsonMimeInterceptor implements ClientHttpRequestInterceptor {

	public static final String ACCEPT = "Accept";
	public static final String CONTENT_TYPE = "Content-Type";
	public static final String CONTENT_TYPE_VALUE = "application/json;charset=UTF-8";

	String header;
	private static final Logger LOGGER = Logger
			.getLogger(JsonMimeInterceptor.class);

	public JsonMimeInterceptor(String header) {
		this.header = header;
	}

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body,
			ClientHttpRequestExecution execution) throws IOException {
		ProxyUtils.setProxy();
		HttpHeaders headers = request.getHeaders();
		headers.remove(ACCEPT);
		headers.add(ACCEPT, header);
		traceRequest(request, body);
		ClientHttpResponse response = execution.execute(request, body);
		HttpHeaders responseHeaders = response.getHeaders();
		responseHeaders.remove(CONTENT_TYPE);
		responseHeaders.add(CONTENT_TYPE, CONTENT_TYPE_VALUE);
		traceResponse(response);
		return response;

	}

	private void traceRequest(HttpRequest request, byte[] body)
			throws IOException {
		LOGGER.debug("===========================request begin================================================");
		LOGGER.debug("URI         : {}" + request.getURI());
		LOGGER.debug("Method      : {}" + request.getMethod());
		LOGGER.debug("Headers     : {}" + request.getHeaders());
		LOGGER.debug("Request body: {}" + new String(body, "UTF-8"));
		LOGGER.debug("==========================request end================================================");
	}

	private void traceResponse(ClientHttpResponse response) throws IOException {
		StringBuilder inputStringBuilder = new StringBuilder();
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(response.getBody(), "UTF-8"));
		String line = bufferedReader.readLine();
		while (line != null) {
			inputStringBuilder.append(line);
			inputStringBuilder.append('\n');
			line = bufferedReader.readLine();
		}
		LOGGER.debug("============================response begin==========================================");
		LOGGER.debug("Status code  : {}" + response.getStatusCode());
		LOGGER.debug("Status text  : {}" + response.getStatusText());
		LOGGER.debug("Headers      : {}" + response.getHeaders());
		LOGGER.debug("Response body: {}" + inputStringBuilder.toString());
		LOGGER.debug("=======================response end=================================================");
	}
}
