package com.oup.eac.web.filter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Integration test for http HEAD support against locally running EAC.
 * 
 * @author David Hay
 *
 */

public class HeadTester {

    @Test
    public void testGet() throws ClientProtocolException, IOException {
        
        DefaultHttpClient httpclient = new DefaultHttpClient();        
        HttpGet head = new HttpGet("http://localhost:8080/eac/profile.htm");
        HttpResponse resp = httpclient.execute(head);
        InputStream is = resp.getEntity().getContent();
        ByteArrayOutputStream baos  = new ByteArrayOutputStream();
        
        int read;
        byte[] buffer = new byte[1024];
        while((read = is.read(buffer)) > 0 ){
            baos.write(buffer,0 ,read);
        }
        baos.flush();
        String res = baos.toString();
        System.out.println(res);
        Assert.assertEquals("HTTP", resp.getStatusLine().getProtocolVersion().getProtocol());
        Assert.assertEquals(1, resp.getStatusLine().getProtocolVersion().getMajor());
        Assert.assertEquals(1, resp.getStatusLine().getProtocolVersion().getMinor());
        Assert.assertEquals("OK", resp.getStatusLine().getReasonPhrase());
        Assert.assertEquals(200, resp.getStatusLine().getStatusCode());
    }
    
    @Test
    public void testHead() throws ClientProtocolException, IOException {
        
        DefaultHttpClient httpclient = new DefaultHttpClient();        
        HttpHead head = new HttpHead("http://localhost:8080/eac/profile.htm");
        HttpResponse resp = httpclient.execute(head);
        Header[] headers = resp.getAllHeaders();
        System.out.println("HEADERS " + resp.getAllHeaders().length);
        for(int i=0;i<headers.length;i++){
            Header h =  headers[i];
            System.out.printf("%d %s -> %s%n",i,h.getName(), h.getValue());
        }
        Assert.assertEquals("HTTP", resp.getStatusLine().getProtocolVersion().getProtocol());
        Assert.assertEquals(1, resp.getStatusLine().getProtocolVersion().getMajor());
        Assert.assertEquals(1, resp.getStatusLine().getProtocolVersion().getMinor());
        Assert.assertEquals("OK", resp.getStatusLine().getReasonPhrase());
        Assert.assertEquals(200, resp.getStatusLine().getStatusCode());
    }
    
    @Test
    public void testHeadGoogle() throws ClientProtocolException, IOException {
        
        DefaultHttpClient httpclient = new DefaultHttpClient();        
        HttpHead head = new HttpHead("http://www.google.co.uk");
        HttpResponse resp = httpclient.execute(head);
        Header[] headers = resp.getAllHeaders();
        System.out.println("HEADERS " + resp.getAllHeaders().length);
        for(int i=0;i<headers.length;i++){
            Header h =  headers[i];
            System.out.printf("%d %s -> %s%n",i,h.getName(), h.getValue());
        }
        Assert.assertEquals("HTTP", resp.getStatusLine().getProtocolVersion().getProtocol());
        Assert.assertEquals(1, resp.getStatusLine().getProtocolVersion().getMajor());
        Assert.assertEquals(1, resp.getStatusLine().getProtocolVersion().getMinor());
        Assert.assertEquals("OK", resp.getStatusLine().getReasonPhrase());
        Assert.assertEquals(200, resp.getStatusLine().getStatusCode());
        
    }
}
