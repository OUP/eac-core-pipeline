package com.oup.eac.integration.facade.impl;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.oup.eac.common.utils.EACSettings;
import com.oup.eac.common.utils.ssl.SSLUtilities;
import com.oup.eac.integration.facade.ErightsFacade;
import com.oup.eac.integration.facade.exceptions.ErightsSessionNotFoundException;


@ContextConfiguration(locations = { "classpath*:/eac/eac*-beans.xml", "classpath*:/eac/test.eac*-beans.xml" })
public class ErightsFacadeImplToolsTest extends AbstractJUnit4SpringContextTests {

	//private static String WEB_SERVICE_URL = "http://rs.dev.access.oup.com/oup/ws/OUPRightAccessService?wsdl";
	private static String WEB_SERVICE_URL = "http://rightsuite-elb.support-dev.support-dev.access.the-infra.com/oup/ws/OUPRightAccessService?wsdl";
	
	@Autowired
	private ErightsFacade erightsFacade;
	
	@Before
	public void setUp() throws Exception {
		 System.setProperty(EACSettings.ERIGHTS_WEBSERVICE_URL, WEB_SERVICE_URL);
	}
	
	@Test
	public void logout() throws Exception {
		String cookie = "hFiCspH8hCwx2FTwUmbQ7EKJx2B9d7dFxxz5h-18x2dQAy36jiDhMeUhUN2NAoIZQx3Dx3DAkEcnDwHuDlPTZaC0Hhx2B3wx3Dx3D-V6qX9TSWnsCMoJQ59x2F5SDAx3Dx3D-uBziJBUqnARjyvEZsUkILAx3Dx3D";
		try {
			erightsFacade.logout(cookie);
		} catch( ErightsSessionNotFoundException e) {
			e.printStackTrace();
		}
		
	}
}
