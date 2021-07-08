package com.oup.eac.service.merge;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Session;
import javax.naming.NamingException;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.oup.eac.common.utils.EACSettings;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.paging.Paging;
import com.oup.eac.domain.paging.PagingCriteria;
import com.oup.eac.domain.paging.PagingCriteria.SortDirection;
import com.oup.eac.dto.CustomerSearchCriteria;
import com.oup.eac.integration.facade.ErightsFacade;
import com.oup.eac.service.CustomerService;
import com.oup.eac.service.RegistrationService;
import com.oup.eac.service.ServiceLayerException;

@ContextConfiguration(locations = { "classpath*:/eac/eac*-beans.xml", "classpath*:/eac/merge.beans.xml" })
@DirtiesContext
public abstract class BaseMergeTest extends AbstractJUnit4SpringContextTests {


	@Value("${merge.env}")
	protected String mergeEnv;
	
	@Value("${merge.driverClassName}")
	protected String mergeDriverClassName;
	
	@Value("${merge.username}")
	protected String mergeUserName;
	
	@Value("${merge.password}")
	protected String mergePassword;
			
	@Value("${merge.url}")
	protected String mergeUrl;

	@Value("${merge.atypon.wsdl.url}")
	protected String mergeAtyponWsdlUrl;

	@Value("${merge.atypon.ws.user}")
	protected String mergeAtyponWsUser;

	@Value("${merge.atypon.ws.password}")
	protected String mergeAtyponWsPassword;

    public static final String LOG_DIRECTORY = "C:/EAC_MERGE_LOGS";

    private static final int MAX_CUSTOMERS = 10000;

    @Autowired
    protected RegistrationService registrationService;

    @Autowired
    protected CustomerService customerService;

    @Autowired
    protected ErightsLicenceIdService eRightsLicenceIdService;

    @Autowired
    @Qualifier("dataSource")
    protected DataSource datSource;

    @Autowired
    @Qualifier("erightsFacade")
    protected ErightsFacade eRights;

    private SimpleDateFormat sdf = new SimpleDateFormat("EE_ddMMMyyyy_HHmmSS");

    private static class NullHostnameVerifier implements HostnameVerifier {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    public BaseMergeTest() {
        try {
            SimpleNamingContextBuilder builder = SimpleNamingContextBuilder.emptyActivatedContextBuilder();
            builder.bind("java:/Mail", Session.getInstance(new Properties()));
            HttpsURLConnection.setDefaultHostnameVerifier(new NullHostnameVerifier());
        } catch (NamingException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Before
    public void setup(){
        System.setProperty(EACSettings.ERIGHTS_WEBSERVICE_URL, this.mergeAtyponWsdlUrl);
        System.setProperty(EACSettings.WS_SECURITY_USERNAME, this.mergeAtyponWsUser);
        System.setProperty(EACSettings.WS_SECURITY_PASSWORD, this.mergeAtyponWsPassword);
    }

    protected void close(PrintWriter pw) {
        if (pw != null) {
            pw.flush();
            pw.close();
        }
    }

    public class PrintWriterInfo{
    	PrintWriter pw;
    	String filename;
    }
    
    protected PrintWriterInfo getPrintWriter(String prefix, Date now) throws IOException {
        String filename = String.format("%s_%s_%s.txt", sdf.format(now), this.mergeEnv, prefix);
        File directory = new File(LOG_DIRECTORY);
        if(directory.exists() == false){
        	boolean created = directory.mkdir();
        	Assert.assertTrue(created);
        }
        File f = new File(directory, filename);
        FileWriter fw = new FileWriter(f);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter pw = new PrintWriter(bw);
        PrintWriterInfo result = new PrintWriterInfo();
        result.pw = pw;
        result.filename = f.getAbsolutePath();
        return result;
    }

    protected List<Customer> getCustomers() throws ServiceLayerException {
		Paging<Customer> firstPage = customerService.searchCustomers(new CustomerSearchCriteria().username(""), new PagingCriteria(MAX_CUSTOMERS, 1, SortDirection.ASC, null));
        return firstPage.getItems();
    }
    
	@Test
	public void testShowConfig(){
		System.out.printf("merge : env %s%n",this.mergeEnv);

		System.out.printf("merge : db driverClassName %s%n",this.mergeDriverClassName);
		System.out.printf("merge : db url             %s%n",this.mergeUrl);
		System.out.printf("merge : db username        %s%n",this.mergeUserName);
		System.out.printf("merge : db password        %s%n",this.mergePassword);
		
		System.out.printf("merge : atypon wsdl url    %s%n",this.mergeAtyponWsdlUrl);
		System.out.printf("merge : atypon user        %s%n",this.mergeAtyponWsUser);
		System.out.printf("merge : atypon password    %s%n",this.mergeAtyponWsPassword);
	}

}
