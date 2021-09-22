package com.oup.eac.web.validators.registration;

import javax.naming.NamingException;

import org.easymock.EasyMock;
import org.junit.Test;

import com.oup.eac.domain.Answer;
import com.oup.eac.domain.Component;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.Element;
import com.oup.eac.domain.Field;
import com.oup.eac.domain.Question;
import com.oup.eac.dto.ProductRegistrationDto;
import com.oup.eac.dto.RegistrationDto;

public class RegistrationFormValidatorTest /* extends AbstractValidatorTest */{
	/*
	 * 
	 * private RegistrationFormValidator validator;
	 * 
	 * public RegistrationFormValidatorTest() throws NamingException { super(); }
	 * 
	 * @Override public void setUp() { super.setUp(); validator = new
	 * RegistrationFormValidator(); }
	 * 
	 * 
	 * @Test public void testValidateNoAnswer() throws Exception { Component
	 * component = SampleDataFactory.createComponent("label.key"); Question question
	 * = SampleDataFactory.createQuestion(); Element element =
	 * SampleDataFactory.createElement(question);
	 * element.setTitleText("title text"); Field field =
	 * SampleDataFactory.createField(component, element, 1);
	 * SampleDataFactory.createTextField(element);
	 * 
	 * RegistrationDto registrationDto = new ProductRegistrationDto();
	 * registrationDto.addField(field); registrationDto.addComponent(component);
	 * 
	 * errors.rejectValue(EasyMock.eq("answers['" + question.getId() + "']"),
	 * EasyMock.eq("error.not-specified"), eqLabels(new Object[] {
	 * field.getElement().getTitleText() }),
	 * EasyMock.eq(field.getElement().getTitleText() + " is required."));
	 * replayMocks();
	 * 
	 * validator.validate(registrationDto, errors);
	 * 
	 * verifyMocks();
	 * 
	 * }
	 * 
	 * @Test public void testValidateWithInvalidAnswer() throws Exception {
	 * Component component = SampleDataFactory.createComponent("label.key");
	 * Question question = SampleDataFactory.createQuestion(); Element element =
	 * SampleDataFactory.createElement(question);
	 * SampleDataFactory.createTextField(element);
	 * element.setTitleText("title text"); element.setRegularExpression("^[a-z]+$");
	 * Field field = SampleDataFactory.createField(component, element, 1); Customer
	 * customer = SampleDataFactory.createCustomer(); Answer answer =
	 * SampleDataFactory.createAnswer(customer, question);
	 * answer.setAnswerText("answer1"); RegistrationDto registrationDto = new
	 * ProductRegistrationDto(); registrationDto.processAnswer(field, answer);
	 * registrationDto.addComponent(component);
	 * 
	 * errors.rejectValue(EasyMock.eq("answers['" + question.getId() + "']"),
	 * EasyMock.eq("error.regularexpression"), eqLabels(new Object[] {
	 * field.getElement().getTitleText() }),
	 * EasyMock.eq(field.getElement().getTitleText() + " is invalid"));
	 * 
	 * replayMocks();
	 * 
	 * validator.validate(registrationDto, errors);
	 * 
	 * verifyMocks();
	 * 
	 * }
	 * 
	 * @Test public void testValidateWithValidAnswer() throws Exception { Component
	 * component = SampleDataFactory.createComponent("label.key"); Question question
	 * = SampleDataFactory.createQuestion(); Element element =
	 * SampleDataFactory.createElement(question);
	 * SampleDataFactory.createTextField(element);
	 * element.setTitleText("title text"); element.setRegularExpression("^[a-z]+$");
	 * Field field = SampleDataFactory.createField(component, element, 1); Customer
	 * customer = SampleDataFactory.createCustomer(); Answer answer =
	 * SampleDataFactory.createAnswer(customer, question);
	 * answer.setAnswerText("answer"); RegistrationDto registrationDto = new
	 * ProductRegistrationDto(); registrationDto.processAnswer(field, answer);
	 * registrationDto.addComponent(component);
	 * 
	 * replayMocks();
	 * 
	 * validator.validate(registrationDto, errors);
	 * 
	 * verifyMocks();
	 * 
	 * }
	 * 
	 */}
