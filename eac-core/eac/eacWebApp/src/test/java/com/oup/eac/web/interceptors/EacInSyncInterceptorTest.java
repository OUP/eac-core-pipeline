package com.oup.eac.web.interceptors;

import java.util.Arrays;
import java.util.List;

import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.web.servlet.ModelAndView;

import com.oup.eac.web.controllers.helpers.SessionHelper;

public class EacInSyncInterceptorTest /* extends AbstractMockTest */ {
	/*
	 * 
	 * public EacInSyncInterceptorTest() throws NamingException { super(); }
	 * 
	 * private EacInSyncInterceptor interceptor; private MockHttpServletRequest
	 * request; private MockHttpServletResponse response; private MockHttpSession
	 * session; private Object handler; private RequestDispatcher rd;
	 * 
	 * @Before public void setup() { interceptor = new EacInSyncInterceptor();
	 * interceptor.setEnabled(true); Assert.assertTrue(interceptor.isEnabled());
	 * List<String> ignoredPaths = Arrays.asList("/allow1","/allow2");
	 * interceptor.setIgnoredPaths(ignoredPaths); Assert.assertEquals(ignoredPaths,
	 * interceptor.getIgnoredPaths());
	 * 
	 * rd = EasyMock.createMock(RequestDispatcher.class); setMocks(rd);
	 * 
	 * request = new MockHttpServletRequest() {
	 * 
	 * @Override public RequestDispatcher getRequestDispatcher(final String path) {
	 * Assert.assertEquals(EacInSyncInterceptor.OUT_OF_SYNCH_ERROR_JSP,path); return
	 * rd; } };
	 * 
	 * response = new MockHttpServletResponse(); session = new MockHttpSession();
	 * request.setSession(session);
	 * 
	 * }
	 * 
	 * 
	 * @Test public void testPassGetRequest() throws Exception {
	 * request.setMethod("GET"); boolean canProceed = interceptor.preHandle(request,
	 * response, handler); Assert.assertTrue(canProceed);
	 * 
	 * ModelAndView modelAndView = new ModelAndView();
	 * 
	 * Assert.assertNull(SessionHelper.getInSyncToken(session));
	 * interceptor.postHandle(request, response, handler, modelAndView);
	 * 
	 * String token = SessionHelper.getInSyncToken(session);
	 * Assert.assertNotNull(token); }
	 * 
	 * @Test public void testPassPostRequestToIgnoredPath() throws Exception {
	 * request.setMethod("POST"); request.setRequestURI("/server/allow1"); boolean
	 * canProceed = interceptor.preHandle(request, response, handler);
	 * Assert.assertTrue(canProceed); }
	 * 
	 * @Test public void testFailPostRequestWithNoSynchToken1() throws Exception {
	 * request.setMethod("POST"); request.setRequestURI("/server/protected");
	 * checkForward(); }
	 * 
	 * private void checkForward() throws Exception { rd.forward(request, response);
	 * EasyMock.expectLastCall(); replayMocks(); boolean canProceed =
	 * interceptor.preHandle(request, response, handler);
	 * Assert.assertFalse(canProceed); verifyMocks(); }
	 * 
	 * 
	 * @Test public void testFailPostRequestWithNoSynchToken2() throws Exception {
	 * interceptor.setIgnoredPaths(null); request.setMethod("POST");
	 * request.setRequestURI("/server/protected");
	 * 
	 * checkForward(); }
	 * 
	 * @Test public void testPassPostRequestWithDisabledInteceptor() throws
	 * Exception { interceptor.setIgnoredPaths(null); interceptor.setEnabled(false);
	 * request.setMethod("POST"); request.setRequestURI("/server/protected");
	 * boolean canProceed = interceptor.preHandle(request, response, handler);
	 * Assert.assertTrue(canProceed); }
	 * 
	 * @Test public void testPassPostRequestWithSynchToken1() throws Exception {
	 * SessionHelper.setEacInSyncToken(session); request.setMethod("POST"); String
	 * token = SessionHelper.getInSyncToken(session);
	 * request.setParameter(SessionHelper.EAC_INSYNC_TOKEN,token);
	 * request.setQueryString("     "); request.setRequestURI("/server/protected");
	 * boolean canProceed = interceptor.preHandle(request, response, handler);
	 * Assert.assertTrue(canProceed); }
	 * 
	 * @Test public void testPassPostRequestWithSynchToken2() throws Exception {
	 * SessionHelper.setEacInSyncToken(session); request.setMethod("POST"); String
	 * token = SessionHelper.getInSyncToken(session);
	 * request.setParameter(SessionHelper.EAC_INSYNC_TOKEN,token);
	 * request.setRequestURI("/server/protected");
	 * request.setQueryString("name1=value1"); boolean canProceed =
	 * interceptor.preHandle(request, response, handler);
	 * Assert.assertTrue(canProceed); }
	 * 
	 * @Test public void testPassPostRequestWithSynchToken3() throws Exception {
	 * SessionHelper.setEacInSyncToken(session); request.setMethod("POST"); String
	 * token = SessionHelper.getInSyncToken(session);
	 * request.setParameter(SessionHelper.EAC_INSYNC_TOKEN,token);
	 * request.setRequestURI("/server/protected"); request.setQueryString(null);
	 * boolean canProceed = interceptor.preHandle(request, response, handler);
	 * Assert.assertTrue(canProceed); }
	 * 
	 * @Test public void testFailPostRequestWithOutOfSynchToken1() throws Exception
	 * { SessionHelper.setEacInSyncToken(session); request.setMethod("POST");
	 * request.setParameter(SessionHelper.EAC_INSYNC_TOKEN,"badToken");
	 * request.setRequestURI("/server/protected");
	 * request.setQueryString("name1=value1"); checkForward(); }
	 * 
	 * @Test public void testFailPostRequestWithOutOfSynchToken2() throws Exception
	 * { SessionHelper.setEacInSyncToken(session); request.setMethod("POST");
	 * request.setParameter(SessionHelper.EAC_INSYNC_TOKEN,"badToken");
	 * request.setRequestURI("/server/protected"); request.setQueryString("    ");
	 * checkForward(); }
	 * 
	 * @Test public void testShouldSkipTokenRegeneration() throws Exception {
	 * interceptor.setSkipTokenRegenerationPaths(Arrays.asList(
	 * "/server/dontResetToken"));
	 * 
	 * request.setMethod("GET"); request.setRequestURI("/server/initialPath");
	 * 
	 * interceptor.preHandle(request, response, handler);
	 * interceptor.postHandle(request, response, handler, new ModelAndView()); //
	 * Will generate the initial token String initialToken =
	 * SessionHelper.getInSyncToken(session); Assert.assertNotNull(initialToken);
	 * 
	 * request.setMethod("GET"); request.setRequestURI("/server/dontResetToken");
	 * 
	 * interceptor.preHandle(request, response, handler);
	 * interceptor.postHandle(request, response, handler, new ModelAndView()); //
	 * Should skip token regeneration String existingToken =
	 * SessionHelper.getInSyncToken(session); Assert.assertEquals(initialToken,
	 * existingToken); }
	 */}
