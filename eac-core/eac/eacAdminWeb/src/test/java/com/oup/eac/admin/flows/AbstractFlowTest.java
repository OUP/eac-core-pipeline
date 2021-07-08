package com.oup.eac.admin.flows;

import java.sql.Connection;
import java.util.Arrays;
import java.util.Properties;

import javax.mail.Session;
import javax.naming.NamingException;
import javax.sql.DataSource;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.ext.hsqldb.HsqldbDataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.webflow.config.FlowDefinitionResource;
import org.springframework.webflow.config.FlowDefinitionResourceFactory;
import org.springframework.webflow.execution.FlowExecutionListener;
import org.springframework.webflow.persistence.HibernateFlowExecutionListener;
import org.springframework.webflow.security.SecurityFlowExecutionListener;
import org.springframework.webflow.test.MockExternalContext;
import org.springframework.webflow.test.MockFlowBuilderContext;
import org.springframework.webflow.test.execution.AbstractXmlFlowExecutionTests;

import com.oup.eac.admin.webflow.ExceptionLoggingListener;
import com.oup.eac.common.RuntimeContext;
import com.oup.eac.common.utils.EACSettings;
import com.oup.eac.data.util.SampleDataCreator;
import com.oup.eac.domain.AdminUser;
import com.oup.eac.integration.facade.impl.FakeErightsFacadeImpl;
import com.oup.eac.service.impl.CustomerServiceImpl;

public abstract class AbstractFlowTest extends AbstractXmlFlowExecutionTests {

	/**
	 * A logger instance that is used in this class
	 */
	private static final Logger LOGGER = Logger.getLogger(CustomerServiceImpl.class);
	
	/**
	 * The Spring Application context that we will use to find beans that have
	 * been defined in spring configuration files
	 */
	protected ApplicationContext context;

	/**
	 * A mock implementation of the ExternalContext
	 */
	protected MockExternalContext mockExternalContext;
	
	/**
	 * locations of all spring configuration files that we want to use in this
	 * class
	 */
	private final String[] configLocations = new String[] {
			"classpath*:/eac/eac*-beans.xml", 
			"classpath*:/eac/test.eac*-beans.xml", 
			"classpath*:/eac/web.eac*-beans.xml", 
			"classpath*:/eac/test.web.eac*-beans.xml"};
	
	private static final String HSQLDB_HIBERNATE_DIALECT = "org.hibernate.dialect.HSQLDialect";
	
	private DataSource dataSource;
	
	private RuntimeContext runtimeContext;
	
	protected SampleDataCreator sampleDataCreator;
	
	protected HibernateFlowExecutionListener hibernateFlowExecutionListener;
	
	protected SecurityFlowExecutionListener securityFlowExecutionListener;
	
	protected ExceptionLoggingListener exceptionLoggingListener;
	
	protected FakeErightsFacadeImpl fakeErightsFacade;
	
	private GrantedAuthoritiesMapper grantedAuthoritiesMapper;
	
	public AbstractFlowTest() throws NamingException {
        SimpleNamingContextBuilder builder = SimpleNamingContextBuilder.emptyActivatedContextBuilder();
        builder.bind("java:/Mail", Session.getInstance(new Properties()));
    }
	
	protected void setUp() throws Exception {
		
		context = new ClassPathXmlApplicationContext(configLocations);
		Assert.assertNotNull(context);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("end setUp()");
		}
		
		runtimeContext  = (RuntimeContext)context.getBean("runtimeContext");
		dataSource = (DataSource)context.getBean("dataSource");
		hibernateFlowExecutionListener = (HibernateFlowExecutionListener)context.getBean("hibernateFlowExecutionListener");
		securityFlowExecutionListener = (SecurityFlowExecutionListener)context.getBean("securityFlowExecutionListener");
		exceptionLoggingListener = (ExceptionLoggingListener)context.getBean("exceptionLoggingExecutionListener");
		grantedAuthoritiesMapper = (GrantedAuthoritiesMapper)context.getBean("rbacAuthoritiesMapper");
		
		fakeErightsFacade = (FakeErightsFacadeImpl)context.getBean("fakeErightsFacade");
		
		this.setFlowExecutionListeners(new FlowExecutionListener[]{hibernateFlowExecutionListener, securityFlowExecutionListener, exceptionLoggingListener});
		
		sampleDataCreator = new SampleDataCreator();
	}
	
	@Override
	protected FlowDefinitionResource[] getModelResources(FlowDefinitionResourceFactory resourceFactory) {
	   return new FlowDefinitionResource[] {
	       resourceFactory.createResource("file:./src/main/webapp/WEB-INF/flows/common/parent/parent-flow.xml", null, "common/parent" ),
	       resourceFactory.createResource("file:./src/main/webapp/WEB-INF/flows/common/errors/errors-flow.xml", null, "common/errors")
	   };
	}
	
	@Override
	protected FlowDefinitionResource getResource(FlowDefinitionResourceFactory resourceFactory) {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("start getResource()");
		}
	
		return resourceFactory.createFileResource(getFlowPath());
	}
	
	protected abstract String getFlowPath();
	
	protected void configureFlowBuilderContext(MockFlowBuilderContext builderContext) {

		for (String name : context.getBeanDefinitionNames()) {
			if (Arrays.asList(getBeansToInject()).contains(name)) {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Bean being injected = " + name);
				}

				builderContext.registerBean(name, context.getBean(name));
			}
		}
	}
	
	public abstract String[] getBeansToInject();
	
	/**
	 * This method simulates the authentication process as we would expect in a
	 * container environment. It uses the supplied username and a collection of
	 * roles which are actually set in Spring's {@link SecurityContext}
	 * 
	 * @param username ,
	 *            name of the user that we want to set as User Principal
	 * @param roles ,
	 *            roles that have to be associated to this User during
	 *            authentication
	 */
	public void authenticate(AdminUser adminUser) throws Exception {

		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
				adminUser, null, grantedAuthoritiesMapper.mapAuthorities(adminUser.getAuthorities()));

		SecurityContextHolder.getContext().setAuthentication(token);

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("end authenticate()");
		}

	}
	
	
	/**
     * Loads all test data into the database for each test.
     * 
     * @throws Exception
     *             checked exception thrown by method
     */
    protected void loadAllDataSets() throws Exception {
        loadAllDataSets(DatabaseOperation.CLEAN_INSERT, this.sampleDataCreator);
    }

    /**
     * Loads all test data into the database for each test
     * where we clean the table and then insert test data.
     * 
     * @throws Exception
     *             checked exception thrown by method
     */
    protected final void loadAllDataSets(DatabaseOperation operation, SampleDataCreator creator) throws Exception {
        Connection connection = DataSourceUtils.getConnection(dataSource);
        try {
            operation.execute(getConnection(connection), creator.getAllDataSets());
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }
    }

    /**
     * Get a connection and set any properties required.
     * 
     * @param connection
     *            The connection to set properties on before returning
     * @return The connection
     * @throws Exception
     *             the exception
     **/
    private IDatabaseConnection getConnection(final Connection connection) throws Exception {
        DatabaseConnection databaseConnection = new DatabaseConnection(connection);
        if (runtimeContext.getProperty(EACSettings.HIBERNATE_DIALECT).equals(HSQLDB_HIBERNATE_DIALECT)) {
            databaseConnection.getConfig().setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new HsqldbDataTypeFactory());
        }
        return databaseConnection;
    }
    
    public void testDoNothing() {
    }
}
