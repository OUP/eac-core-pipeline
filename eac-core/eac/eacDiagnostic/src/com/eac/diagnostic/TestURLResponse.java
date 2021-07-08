package com.eac.diagnostic;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import com.amazonaws.util.IOUtils;

public class TestURLResponse {

	public static void main(String[] args) throws MalformedURLException,SQLException, IOException {
		
		getResponseCode(null);

	}
	public static int getResponseCode(String urlString) throws MalformedURLException, IOException {
	    /*URL u = new URL("http://localhost:8080/site_monitor/oup/checkAuth/uat");*/ 
	    URL u = new URL("http://ec2-52-49-251-221.eu-west-1.compute.amazonaws.com:8080/site_monitor/oup/checkAuth/prod"); 
	    Proxy proxy=new Proxy(Proxy.Type.HTTP, new InetSocketAddress("ouparray.oup.com", 8080));
	    HttpURLConnection huc =  (HttpURLConnection)  u.openConnection(proxy); 
	    //HttpURLConnection huc =  (HttpURLConnection)  u.openConnection();
	    huc.setConnectTimeout(3000); //set timeout to 10 seconds
	    huc.setReadTimeout(3000); //set timeout to 10 seconds
	    huc.setRequestMethod("GET"); 
	    
	    huc.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.0; en-US; rv:1.9.1.2) Gecko/20090729 Firefox/3.5.2 (.NET CLR 3.5.30729)");
	    huc.connect(); 
	    InputStream in = huc.getInputStream();
	    
	    String encoding = huc.getContentEncoding();
	    encoding = encoding == null ? "UTF-8" : encoding;
	    String body = IOUtils.toString(in);
	    System.out.println(body);
	    return 1;
	}
	
	
}
