/**
 * 
 */
package com.oup.eac.common.mock;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.mail.Session;
import javax.naming.NamingException;

import org.junit.Rule;
import org.junit.rules.TestName;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

/**
 * @author harlandd
 * 
 */
@ContextConfiguration(locations = { "classpath*:/eac/eac*-beans.xml", "classpath*:/eac/test.eac*-beans.xml" })
public abstract class AbstractMockTest extends AbstractJUnit4SpringContextTests {

    @Rule
    public TestName name = new TestName();    
    
    /**
     * Construct new test, binding java:/Mail to jndi context.
     * 
     * @throws NamingException
     *             the exception
     */
    public AbstractMockTest() throws NamingException {
        SimpleNamingContextBuilder builder = SimpleNamingContextBuilder.emptyActivatedContextBuilder();
        builder.bind("java:/Mail", Session.getInstance(new Properties()));
    }

    private List<Object> mocks = new ArrayList<Object>();

    /**
     * Sets the mocks.
     * 
     * @param objects
     *            the mocks.
     */
    protected final void setMocks(final Object... objects) {
        mocks.addAll(Arrays.asList(objects));
    }

    /**
     * Sets the mocks.
     * 
     * @param objects
     *            the mocks.
     */
    protected final void addMock(final Object object) {
        mocks.add(object);
    }

    /**
     * Get the mocks.
     * 
     * @return the mocks
     */
    protected final Object[] getMocks() {
        return mocks.toArray();
    }
    
    protected void replayMocks() {
    	replay(getMocks());
    }
    
    protected void verifyMocks() {
    	verify(getMocks());
    }
    
    protected <T> T createAMock(Class<T> aClass) {
    	return createMock(aClass);
    }
    
    protected <T> T createAndAddMock(Class<T> aClass) {
    	T mock = createMock(aClass);
    	mocks.add(mock);
    	return mock;
    }
    
    protected InputStream getFile() throws IOException {
        String fileName = getClass().getSimpleName()+ "_" + name.getMethodName() + ".txt";
        InputStream input = getClass().getResourceAsStream(fileName);
        if(input == null) {
            fail(fileName + " does not exist!");
        }
        return input;
    }
    
}
