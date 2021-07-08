package com.oup.eac.ws.mapping;

import java.io.IOException;
import java.util.Properties;

import javax.mail.Session;
import javax.naming.NamingException;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import junit.framework.Assert;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;
import org.springframework.oxm.castor.CastorMarshaller;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:/eac-web-services-servlet.xml", "classpath*:/eac/test.eac*-beans.xml" })
public class CastorUnMarshallingTest {

	@Autowired
	@Qualifier("castorMarshallerV1")
	private CastorMarshaller cmV1;
	
	@Autowired
	@Qualifier("castorMarshallerV2")
	private CastorMarshaller cmV2;
	
    public CastorUnMarshallingTest() {
        SimpleNamingContextBuilder builder;
        try {
            builder = SimpleNamingContextBuilder.emptyActivatedContextBuilder();
            builder.bind("java:/Mail", Session.getInstance(new Properties()));
        } catch (NamingException e) {
            throw new RuntimeException("problem with jndi",e);
        }
    }
	
    private String getData(String name) throws IOException{
        Resource res = new ClassPathResource(name);
    	String result = IOUtils.toString(res.getInputStream());
    	return result;    	
    }
    
    private Source getSource(String resourceName) throws IOException{
    	String data = getData(resourceName);
    	Source src = new StreamSource(IOUtils.toInputStream(data));
    	return src;
    }
    
    @Test
    public void testSpringCMv2() throws IOException{
    	Source source = getSource("mapping/userNameRequestV2.xml");
		Object o = this.cmV2.unmarshal(source);		
		Assert.assertEquals("com.oup.eac.ws.v2.binding.userdata.UserNameRequest", o.getClass().getName());
    }
    
    @Test
    public void testSpringCMv1() throws IOException{
    	Source source = getSource("mapping/userNameRequestV1.xml");
		Object o = this.cmV1.unmarshal(source);
		Assert.assertEquals("com.oup.eac.ws.v1.userdata.binding.UserNameRequest", o.getClass().getName());
    }
}
