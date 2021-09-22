package com.oup.eac.web.controllers.authentication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;

import com.oup.eac.domain.ErightsLicenceDecision;

@ContextConfiguration(locations = {"classpath*:/eac/eac*-beans.xml", "classpath*:/eac/test.eac*-beans.xml", "classpath*:/eac/web.eac*-beans.xml", "classpath*:/eac/web.eac-servlet.xml"})
public class AuthenticationWorkFlowTest /* extends AbstractDbMessageTest */ {
	/*
	 * 
	 * private MockHttpServletRequest request;
	 * 
	 * @Before public void setUp() { request = new MockHttpServletRequest();
	 * request.setParameter("licenseDenyTypes", "-510"); }
	 * 
	 * @Test public void testGetErightsLicenceDecisions() {
	 * Set<ErightsLicenceDecision> decisions =
	 * AuthenticationWorkFlow.getErightsLicenceDecisions(request); assertEquals(1,
	 * decisions.size()); ErightsLicenceDecision erightsLicenceDecision =
	 * decisions.iterator().next(); assertNotNull(erightsLicenceDecision);
	 * assertEquals(ErightsLicenceDecision.DENY_CONCURRENCY,
	 * erightsLicenceDecision); }
	 * 
	 * @Test public void testGetErightsLicenceDecisionsMultiple() {
	 * request.setParameter("licenseDenyTypes", "-510,5");
	 * Set<ErightsLicenceDecision> decisions =
	 * AuthenticationWorkFlow.getErightsLicenceDecisions(request); assertEquals(2,
	 * decisions.size()); int countDenyConcurreny = 0; int countDenyAccept= 0;
	 * for(ErightsLicenceDecision erightsLicenceDecision : decisions) {
	 * if(erightsLicenceDecision == ErightsLicenceDecision.DENY_CONCURRENCY) {
	 * countDenyConcurreny++; } if(erightsLicenceDecision ==
	 * ErightsLicenceDecision.DECISION_ACCEPT) { countDenyAccept++; } }
	 * assertEquals(countDenyConcurreny, 1); assertEquals(countDenyAccept, 1); }
	 * 
	 * @Test public void testGetErightsLicenceDecisionsNoReasons() {
	 * request.setParameter("licenseDenyTypes", ""); Set<ErightsLicenceDecision>
	 * decisions = AuthenticationWorkFlow.getErightsLicenceDecisions(request);
	 * assertEquals(0, decisions.size()); }
	 * 
	 * @Test public void testIsConcurrencyExceededNoReasons() {
	 * request.setParameter("licenseDenyTypes", "");
	 * assertFalse(AuthenticationWorkFlow.isConcurrencyExceeded(request)); }
	 * 
	 * @Test public void testIsConcurrencyExceeded() {
	 * request.setParameter("licenseDenyTypes", "-510,5");
	 * assertTrue(AuthenticationWorkFlow.isConcurrencyExceeded(request)); }
	 */}
