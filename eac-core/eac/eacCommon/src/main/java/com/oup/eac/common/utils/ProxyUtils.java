package com.oup.eac.common.utils;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class ProxyUtils {
	private static Logger logger = Logger
			.getLogger(ProxyUtils.class.getName());
	

	public static void setProxy() {
		logger.debug("is proxy enabled :: " + isProxyEnabled());
		if(!isProxyEnabled()) {
			return;
		} else {
			Properties props = System.getProperties();
			props.put("https.proxyHost", EACSettings.getProperty(EACSettings.HTTPS_PROXY_HOST));
			props.put("https.proxyPort", EACSettings.getProperty(EACSettings.HTTPS_PROXY_PORT));
			props.put("http.proxyHost", EACSettings.getProperty(EACSettings.HTTP_PROXY_HOST));
			props.put("http.proxyPort", EACSettings.getProperty(EACSettings.HTTP_PROXY_PORT));
		}
	}
	
	public static InetSocketAddress getProxyInetSocketAddress() {
		InetSocketAddress address = new InetSocketAddress(EACSettings.getProperty(EACSettings.HTTPS_PROXY_HOST),
				Integer.parseInt(EACSettings.getProperty(EACSettings.HTTPS_PROXY_PORT)));
		return address;
	}
	
	public static Proxy getHTTPProxy() {
		Proxy proxy = new Proxy(Proxy.Type.HTTP,getProxyInetSocketAddress());
		return proxy;
	}
	
	public static boolean isProxyEnabled() {
		String strIsProxyEnabled = EACSettings.getProperty(EACSettings.IS_PROXY_ENABLED);
		if(StringUtils.isNotBlank(strIsProxyEnabled)) {
			return Boolean.parseBoolean(EACSettings.getProperty(EACSettings.IS_PROXY_ENABLED));
		} else {
			return false;
		}
	}
}
