package com.oup.eac.ws.v1.service.impl;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashSet;
import java.util.Set;

import javax.naming.NamingException;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.easymock.IArgumentMatcher;
import org.junit.Before;
import org.junit.Test;

import com.oup.eac.domain.Answer;
import com.oup.eac.domain.Component;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.Element;
import com.oup.eac.domain.Question;
import com.oup.eac.dto.CustomerSessionDto;
import com.oup.eac.service.ServiceLayerException;
import com.oup.eac.service.exceptions.CustomerNotFoundServiceLayerException;
import com.oup.eac.ws.v1.service.UserServiceAdapter;
import com.oup.eac.ws.v1.userdata.binding.RegistrationInformation;
import com.oup.eac.ws.v1.userdata.binding.RegistrationInformationResponse;
import com.oup.eac.ws.v1.userdata.binding.User;
import com.oup.eac.ws.v1.userdata.binding.UserNameResponse;
import com.oup.eac.ws.v2.binding.common.WsUserId;
import com.oup.eac.ws.v2.ex.WebServiceValidationException;
import com.oup.eac.ws.v2.service.WsCustomerLookup;

public class CachingUserServiceAdapterMockTest/* extends AbstractMockTest */ {
	/*
	 * 
	 * 
	 * private UserServiceAdapter cachingUserServiceAdapter;
	 * 
	 * private Customer customer;
	 * 
	 * private Customer customerWithAnswers;
	 * 
	 * private Set<Answer> answers;
	 * 
	 * private CustomerSessionDto customerSessionDto;
	 * 
	 * private Answer answer, answer2, answer3;
	 * 
	 * private Question question, question2, question3;
	 * 
	 * private User userWithAnswers;
	 * 
	 * private static final String SESSION_KEY = "abcdefg";
	 * 
	 * 
	 * private WsCustomerLookup wsCustomerLookup;
	 * 
	 * public CachingUserServiceAdapterMockTest() throws NamingException { super();
	 * }
	 * 
	 * @Before public void setUp() throws Exception { this.customer = new
	 * Customer(); this.answers = new HashSet<Answer>() ; Component component =
	 * SampleDataFactory.createComponent("label.key"); question =
	 * SampleDataFactory.createQuestion(); Element element =
	 * SampleDataFactory.createElement(question);
	 * SampleDataFactory.createField(component, element, 1); question2 =
	 * SampleDataFactory.createQuestion(); Element element2 =
	 * SampleDataFactory.createElement(question2);
	 * SampleDataFactory.createField(component, element2, 2); question3 =
	 * SampleDataFactory.createQuestion(); Element element3 =
	 * SampleDataFactory.createElement(question3);
	 * SampleDataFactory.createField(component, element3, 3);
	 * 
	 * customer = SampleDataFactory.createCustomer(); customerWithAnswers =
	 * SampleDataFactory.createCustomer(); customerSessionDto = new
	 * CustomerSessionDto(); customerSessionDto.setCustomer(customer);
	 * customerSessionDto.setSession(SESSION_KEY);
	 * 
	 * userWithAnswers = convertCustomer(customerWithAnswers); answer =
	 * SampleDataFactory.createAnswer(customerWithAnswers, question); answer2 =
	 * SampleDataFactory.createAnswer(customerWithAnswers, question2); answer3 =
	 * SampleDataFactory.createAnswer(customerWithAnswers, question3);
	 * answer3.setAnswerText("     ");// blank answers are not returned
	 * answers.add(answer); answers.add(answer2); answers.add(answer3);
	 * wsCustomerLookup = EasyMock.createMock(WsCustomerLookup.class);
	 * 
	 * setMocks(wsCustomerLookup); cachingUserServiceAdapter = new
	 * CachingUserServiceAdapter(wsCustomerLookup); }
	 * 
	 * @Test public void testGetUsername() throws Exception {
	 * 
	 * expect(wsCustomerLookup.getCustomerByWsUserId(eqSessionKey(SESSION_KEY))).
	 * andReturn(customer);
	 * 
	 * replay(getMocks());
	 * 
	 * UserNameResponse userNameResponse =
	 * cachingUserServiceAdapter.getUserName(SESSION_KEY);
	 * 
	 * assertEquals(userNameResponse.getUserName(), customer.getFullName());
	 * 
	 * verify(getMocks()); }
	 * 
	 * @Test public void testGetUsernameCustomerNotFound() throws Exception {
	 * ServiceLayerException sle = new
	 * CustomerNotFoundServiceLayerException("not found");
	 * WebServiceValidationException ve = new WebServiceValidationException("oops",
	 * sle);
	 * expect(wsCustomerLookup.getCustomerByWsUserId(eqSessionKey(SESSION_KEY))).
	 * andThrow(ve);
	 * 
	 * replay(getMocks()); try { cachingUserServiceAdapter.getUserName(SESSION_KEY);
	 * Assert.fail("exception expected"); } catch (Exception ex) {
	 * Assert.assertEquals(sle, ex); } verify(getMocks()); }
	 * 
	 * @Test public void testGetRegistrationInformation() throws Exception {
	 * 
	 * expect(wsCustomerLookup.getCustomerByWsUserId(eqSessionKey(SESSION_KEY))).
	 * andReturn(customerWithAnswers);
	 * 
	 * expect(wsCustomerLookup.getCustomerWithAnswers(customerWithAnswers.getId())).
	 * andReturn(answers);
	 * 
	 * replay(getMocks());
	 * 
	 * RegistrationInformationResponse response =
	 * cachingUserServiceAdapter.getRegistrationInformation(SESSION_KEY);
	 * 
	 * assertNotNull(response); assertNotNull(response.getUser());
	 * 
	 * User responseUser = response.getUser();
	 * 
	 * Assert.assertEquals(userWithAnswers.getEmailAddress(),
	 * responseUser.getEmailAddress());
	 * Assert.assertEquals(userWithAnswers.getFirstName(),
	 * responseUser.getFirstName());
	 * Assert.assertEquals(userWithAnswers.getLastName(),
	 * responseUser.getLastName());
	 * Assert.assertEquals(userWithAnswers.getUserName(),
	 * responseUser.getUserName());
	 * 
	 * RegistrationInformation[] registrationInformations =
	 * response.getRegistrationInformation();
	 * 
	 * assertNotNull(registrationInformations); assertEquals(3,
	 * registrationInformations.length);
	 * 
	 * RegistrationInformation registrationInformation =
	 * getRegistrationInformationForKey(question.getDescription(),
	 * registrationInformations);
	 * 
	 * assertNotNull(registrationInformation);
	 * assertEquals(question.getDescription(),
	 * registrationInformation.getRegistrationKey());
	 * assertEquals(answer.getAnswerText(),
	 * registrationInformation.getRegistrationValue());
	 * 
	 * registrationInformation =
	 * getRegistrationInformationForKey(question2.getDescription(),
	 * registrationInformations);
	 * 
	 * assertNotNull(registrationInformation);
	 * assertEquals(question2.getDescription(),
	 * registrationInformation.getRegistrationKey());
	 * assertEquals(answer2.getAnswerText(),
	 * registrationInformation.getRegistrationValue());
	 * 
	 * registrationInformation =
	 * getRegistrationInformationForKey(question3.getDescription(),
	 * registrationInformations);
	 * 
	 * assertNotNull(registrationInformation);
	 * assertEquals(question3.getDescription(),
	 * registrationInformation.getRegistrationKey());
	 * assertEquals(answer3.getAnswerText(),
	 * registrationInformation.getRegistrationValue());
	 * 
	 * verify(getMocks()); }
	 * 
	 * @Test public void testGetRegistrationInformationCustomerNotFound() throws
	 * Exception {
	 * 
	 * ServiceLayerException sle = new
	 * CustomerNotFoundServiceLayerException("not found");
	 * WebServiceValidationException ve = new
	 * WebServiceValidationException("oops",sle);
	 * expect(wsCustomerLookup.getCustomerByWsUserId(eqSessionKey(SESSION_KEY))).
	 * andThrow(ve);
	 * 
	 * replay(getMocks());
	 * 
	 * try { cachingUserServiceAdapter.getRegistrationInformation(SESSION_KEY);
	 * Assert.fail("exception expected"); } catch (Exception ex) {
	 * Assert.assertEquals(sle, ex); }
	 * 
	 * verify(getMocks()); }
	 * 
	 * private final User convertCustomer(final Customer customer) { User user = new
	 * User(); user.setEmailAddress(customer.getEmailAddress());
	 * user.setFirstName(customer.getFirstName());
	 * user.setLastName(customer.getFamilyName());
	 * user.setUserName(customer.getUsername()); return user; }
	 * 
	 * private RegistrationInformation getRegistrationInformationForKey(final String
	 * key, RegistrationInformation[] registrationInformations) { for
	 * (RegistrationInformation registrationInformation : registrationInformations)
	 * { if (registrationInformation.getRegistrationKey().equals(key)) { return
	 * registrationInformation; } } return null; }
	 * 
	 * 
	 * protected WsUserId eqUsername(final String expectedUsername) {
	 * IArgumentMatcher matcher = new IArgumentMatcher() {
	 * 
	 * @Override public boolean matches(Object arg) { if (arg instanceof WsUserId ==
	 * false) { return false; } WsUserId wsUserId = (WsUserId)arg;
	 * 
	 * boolean result = expectedUsername.equals(wsUserId.getUserName()); return
	 * result; }
	 * 
	 * @Override public void appendTo(StringBuffer out) { out.append("eqUsername(");
	 * out.append(expectedUsername); out.append(")"); } };
	 * EasyMock.reportMatcher(matcher); return null; }
	 * 
	 * protected WsUserId eqSessionKey(final String expectedSessionKey) {
	 * IArgumentMatcher matcher = new IArgumentMatcher() {
	 * 
	 * @Override public boolean matches(Object arg) { if (arg instanceof WsUserId ==
	 * false) { return false; } WsUserId wsUserId = (WsUserId)arg;
	 * 
	 * boolean result = expectedSessionKey.equals(wsUserId.getSessionToken());
	 * return result; }
	 * 
	 * @Override public void appendTo(StringBuffer out) {
	 * out.append("eqSessionKey("); out.append(expectedSessionKey); out.append(")");
	 * } }; EasyMock.reportMatcher(matcher); return null; }
	 * 
	 */}
