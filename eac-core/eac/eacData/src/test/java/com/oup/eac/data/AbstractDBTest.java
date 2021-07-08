package com.oup.eac.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Properties;

import javax.mail.Session;
import javax.sql.DataSource;

import org.apache.commons.io.IOUtils;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.ext.hsqldb.HsqldbDataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import com.oup.eac.common.RuntimeContext;
import com.oup.eac.common.utils.EACSettings;
import com.oup.eac.data.util.SampleDataCreator;

/**
 * @author harlandd Abstract class to be extended by all tests requiring database functionality.
 */
@ContextConfiguration(locations = { "classpath*:/eac/eac*-beans.xml", "classpath*:/eac/test.eac*-beans.xml" })
public abstract class AbstractDBTest extends AbstractTransactionalJUnit4SpringContextTests {

    private static final String HSQLDB_HIBERNATE_DIALECT = "org.hibernate.dialect.HSQLDialect";
    
    private static final String SQL_POSTFIX = ".sql";
    
    private static final char DIR = '/';
    
    private static final char PACKAGE_DELIMITER = '.';

    private SampleDataCreator sampleDataCreator = new SampleDataCreator();

    @Autowired
    private RuntimeContext runtimeContext;
    
    @Rule
    public TestName name = new TestName();

    @Autowired
    private DataSource dataSource;

    public AbstractDBTest() {
    	try {
    		SimpleNamingContextBuilder builder = SimpleNamingContextBuilder.emptyActivatedContextBuilder();
    		builder.bind("java:/Mail", Session.getInstance(new Properties()));
    	} catch (Exception e) {
			e.printStackTrace(System.out);
		}
    	
    	
    }
    
    /**
     * Loads all test data into the database for each test.
     * 
     * @throws Exception
     *             checked exception thrown by method
     */
    protected void loadAllDataSets() throws Exception {
        loadAllDataSets(DatabaseOperation.CLEAN_INSERT,this.sampleDataCreator);
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

    /**
     * @param dataSource
     *            the supplied dataSource
     */
    @Autowired
    public final void setDataSource(final DataSource dataSource) {
        super.setDataSource(dataSource);
        this.dataSource = dataSource;
    }

    /**
     * @return the sampleDataCreator
     */
    public final SampleDataCreator getSampleDataCreator() {
        return sampleDataCreator;
    }
    
    protected void assertTextSame(String message, String actual) throws IOException {
    	assertEquals(message, getFileText(), actual);
    }    
    
    protected void assertTextSame(String actual) throws IOException {
        String expected = getFileText();
    	assertEquals(expected, actual);
    }

	private String getFileText() throws IOException {
		String fileName = getFileName();
    	InputStream input = getInputStream(fileName);
    	if(input == null) {
    		fail(fileName + " does not exist!");
    	}
    	return IOUtils.toString(input, "UTF-8");
	}

	protected String getFileName() {
        String fileName = getClass().getSimpleName()+ "_" + name.getMethodName() + ".txt";
        return fileName;
    }
	
	protected InputStream getInputStream(String fileName) {
	    InputStream input = getClass().getResourceAsStream(fileName);
	    return input;
	}

	protected void runDbInitScript() {
		StringBuilder file = new StringBuilder();
		file.append(getClass().getPackage().getName().replace(PACKAGE_DELIMITER, DIR)).append(DIR).append(getClass().getSimpleName()).append(SQL_POSTFIX);
    	executeSqlScript(file.toString(), false);		
	}
	
}
