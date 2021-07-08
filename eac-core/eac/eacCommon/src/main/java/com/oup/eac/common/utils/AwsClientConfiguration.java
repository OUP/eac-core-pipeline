package com.oup.eac.common.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.log4j.Logger;

import com.amazonaws.ClientConfiguration;

public class AwsClientConfiguration {
	
	private static Logger logger = Logger
			.getLogger(AwsClientConfiguration.class.getName());
	
	private static ClientConfiguration clientConfig = new ClientConfiguration();
	
	public static ClientConfiguration getClientConfiguration(){
		boolean isProxyEnabled = Boolean.parseBoolean(EACSettings.getProperty(EACSettings.AWS_CLIENT_CONFIG_PROXY_ENABLE));
		
		if(!isProxyEnabled)
			return clientConfig;
		try {
			clientConfig.setProxyHost(EACSettings.getProperty(EACSettings.AWS_CLIENT_CONFIG_PROXY_HOST));
			Integer proxyPort = Integer.parseInt(EACSettings.getProperty(EACSettings.AWS_CLIENT_CONFIG_PROXY_PORT));
	     	clientConfig.setProxyPort(proxyPort);
	     	clientConfig.setProxyUsername(EACSettings.getProperty(EACSettings.AWS_CLIENT_CONFIG_PROXY_USER));
	     	clientConfig.setProxyPassword(EACSettings.getProperty(EACSettings.AWS_CLIENT_CONFIG_PROXY_PASSWORD));
		} catch(Exception ex){
			logger.error(getStackTrace(ex));
		}
		return clientConfig;
	}
	
	private static String getStackTrace(Throwable t) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		t.printStackTrace(pw);
		return  sw.toString(); // stack trace as a string
	}
}
