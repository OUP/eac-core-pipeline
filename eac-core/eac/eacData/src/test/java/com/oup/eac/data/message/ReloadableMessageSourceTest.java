package com.oup.eac.data.message;

import java.util.Locale;
import java.util.UUID;

import javax.annotation.PostConstruct;

import junit.framework.Assert;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.NoSuchMessageException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.synyx.messagesource.InitializableMessageSource;

import com.oup.eac.data.message.util.DbMessageSourceUtils;

/**
 * Tests the periodic reloading of localized messages from a HSQL database.
 * 
 * @author David Hay
 * 
 */
//@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:/eac/eac*-beans.xml", "classpath*:/eac/test.eac*-beans.xml" })
public class ReloadableMessageSourceTest extends BaseMessageSourceTest {
	/*
	 * 
	 * private static final Logger LOG =
	 * Logger.getLogger(ReloadableMessageSourceTest.class);
	 * 
	 * private static final boolean SHOW_DB_CONSOLE = false;
	 * 
	 * @Autowired(required=true)
	 * 
	 * @Qualifier("dataSource") private BasicDataSource dataSource;
	 * 
	 * @Autowired(required=true)
	 * 
	 * @Qualifier("test.dbMessageSource") private InitializableMessageSource
	 * testMessageSource;
	 * 
	 * @Autowired(required=true)
	 * 
	 * @Qualifier("messageSource") private InitializableMessageSource messageSource;
	 * 
	 * private DbMessageSourceUtils messageUtils;
	 * 
	 * private static final Locale TEST_LOCALE = new Locale("ru", "RU");
	 * 
	 * private static final String TEST_CODE = "test.reload";
	 * 
	 * @Value("${db.message.source.reloader.delay}") private long delayMillis;
	 * 
	 * private static final String EXISTING_CODE = "error.msg1"; private static
	 * final Locale EXISTING_LOCALE = new Locale("en"); private static final String
	 * EXISTING_MESSAGE = "error message for English";
	 * 
	 * @PostConstruct public void init() { this.messageUtils = new
	 * DbMessageSourceUtils(this.dataSource); try { if (SHOW_DB_CONSOLE) { String[]
	 * args = new String[] { "--url", dataSource.getUrl(), "--noexit" };
	 * org.hsqldb.util.DatabaseManagerSwing.main(args); } } catch (Exception ex) {
	 * ex.printStackTrace(); } finally {
	 * 
	 * } }
	 * 
	 * @Test public void testNonTestMessageSources() {
	 * Assert.assertNotNull(this.messageSource); }
	 * 
	 * @Test public void testReadFromMessageSource() {
	 * checkMessageSource(this.testMessageSource); }
	 * 
	 * @Test public void testReloading() { // read // assert null // gen 1 //
	 * persist 1 // wait // read // assert 1 // gen 2 // persist 2 // wait // read
	 * // assert 2
	 * 
	 * try { getMessage(); Assert.fail("we did not expect to find a message here");
	 * } catch (NoSuchMessageException me) {
	 * Assert.assertTrue(me.getMessage().indexOf(TEST_CODE) >= 0); }
	 * 
	 * String gen1 = generateText(); persist(gen1);// insert sleep(getDelay());//
	 * wait until realoader has done its stuff String message2 = getMessage();
	 * Assert.assertEquals(gen1, message2);
	 * 
	 * String gen2 = generateText(); persist(gen2);// update sleep(getDelay());//
	 * wait until realoader has done its stuff again String message3 = getMessage();
	 * Assert.assertEquals(gen2, message3);
	 * 
	 * }
	 * 
	 * @Test public void testReloadingNoChange() { checkExisting();
	 * sleep(getDelay());// gives reloader a chance to work checkExisting(); }
	 * 
	 * private long getDelay() { return this.delayMillis + 1000; }
	 * 
	 * private void persist(String message) { messageUtils.persist("test",
	 * TEST_LOCALE, TEST_CODE, message); }
	 * 
	 * private String getMessage() { String message =
	 * testMessageSource.getMessage(TEST_CODE, new Object[0], TEST_LOCALE); return
	 * message; }
	 * 
	 * private String generateText() { String msg = "test message for " +
	 * UUID.randomUUID().toString(); return msg; }
	 * 
	 * private void sleep(long millis) { LOG.debug("sleeping for " + (millis / 1000)
	 * + "secs"); try { Thread.sleep(millis); } catch (Exception ex) { // ignore }
	 * LOG.debug("slept for " + (millis / 1000) + "secs"); }
	 * 
	 * private void checkExisting() { String message =
	 * this.testMessageSource.getMessage(EXISTING_CODE, new Object[0],
	 * EXISTING_LOCALE); Assert.assertEquals(EXISTING_MESSAGE, message); }
	 * 
	 * 
	 */}
