package com.oup.eac.web.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.oup.eac.common.utils.activationcode.EacNumericActivationCode;
import com.oup.eac.domain.ActivationCode;
import com.oup.eac.domain.ActivationCodeBatch;
import com.oup.eac.domain.ActivationCodeBatch.ActivationCodeFormat;
import com.oup.eac.domain.ActivationCodeRegistrationDefinition;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.Division;
import com.oup.eac.domain.RegisterableProduct;
import com.oup.eac.domain.RegisterableProduct.RegisterableType;
import com.oup.eac.domain.RegistrationActivation;
import com.oup.eac.domain.StandardLicenceTemplate;
import com.oup.eac.web.controllers.authentication.ActivationCodeFormController;
import com.oup.eac.web.controllers.helpers.SessionHelper;


@ContextConfiguration(locations = {"classpath*:/eac/eac*-beans.xml", "classpath*:/eac/test.eac*-beans.xml", "classpath*:/eac/web.eac*-beans.xml", "classpath*:/eac/web.eac-servlet.xml"})
public class ActivationCodeFormControllerTest/* extends AbstractDbMessageTest */ {
	/*
	 * 
	 * private static final String SPRING_VALIDATION_BINDING_RESULT =
	 * "org.springframework.validation.BindingResult.";
	 * 
	 * private static final Logger LOGGER =
	 * Logger.getLogger(ActivationCodeFormControllerTest.class);
	 * 
	 * @Autowired private ActivationCodeFormController activationCodeFormController;
	 * 
	 * private MockHttpServletRequest httpServletRequest; private
	 * MockHttpServletResponse httpServletResponse; private MockHttpSession
	 * httpSession;
	 * 
	 * private ActivationCode activationCode1;
	 * 
	 * private Customer customer;
	 * 
	 * @Before public final void setUp() throws Exception { httpServletRequest = new
	 * MockHttpServletRequest(); httpServletResponse = new
	 * MockHttpServletResponse(); httpSession = new MockHttpSession();
	 * httpServletRequest.setSession(httpSession);
	 * 
	 * RegistrationActivation registrationActivation =
	 * getSampleDataCreator().createInstantRegistrationActivation(); Division
	 * division = getSampleDataCreator().createDivision("MALAYSIA");
	 * RegisterableProduct product =
	 * SampleDataFactory.createRegisterableProduct(1,"Product 1",
	 * RegisterableType.SELF_REGISTERABLE);
	 * product.setLandingPage("http://localhost/product1.html");
	 * getSampleDataCreator().loadRegisterableProduct(product);
	 * StandardLicenceTemplate licenceTemplate =
	 * getSampleDataCreator().createStandardLicenceTemplate();
	 * ActivationCodeRegistrationDefinition acrd =
	 * getSampleDataCreator().createActivationCodeRegistrationDefinition(product,
	 * licenceTemplate, registrationActivation, null); ActivationCodeBatch
	 * activationCodeBatch =
	 * getSampleDataCreator().createActivationCodeBatch(licenceTemplate,
	 * ActivationCodeFormat.EAC_NUMERIC, acrd, null, null); activationCode1 =
	 * getSampleDataCreator().createActivationCode(activationCodeBatch, new
	 * EacNumericActivationCode()); customer =
	 * getSampleDataCreator().createCustomer();
	 * SessionHelper.setCustomer(httpServletRequest, customer); loadAllDataSets(); }
	 * 
	 * @Ignore
	 * 
	 * @Test public void testValidActivationCode() throws Exception {
	 * httpServletRequest.setMethod("POST");
	 * SessionHelper.setForwardUrl(httpSession, "http://localhost/product1.html");
	 * httpServletRequest.setParameter("code", activationCode1.getCode());
	 * 
	 * ModelAndView modelAndView =
	 * activationCodeFormController.handleRequest(httpServletRequest,
	 * httpServletResponse); assertFalse(hasErrors(modelAndView, "activationCode"));
	 * assertEquals("http://localhost/product1.html", getView(modelAndView)); }
	 * 
	 * @Ignore
	 * 
	 * @Test public void testInValidActivationCode() throws Exception {
	 * httpServletRequest.setMethod("POST");
	 * SessionHelper.setForwardUrl(httpSession, "http://localhost/product1.html");
	 * httpServletRequest.setParameter("code", "1234");
	 * 
	 * ModelAndView modelAndView =
	 * activationCodeFormController.handleRequest(httpServletRequest,
	 * httpServletResponse); assertTrue(hasErrors(modelAndView, "activationCode"));
	 * assertEquals("activationCodeForm", getView(modelAndView)); }
	 * 
	 * 
	 * private String getView(ModelAndView modelAndView) { if(modelAndView.getView()
	 * instanceof RedirectView) { return
	 * ((RedirectView)modelAndView.getView()).getUrl(); } return
	 * modelAndView.getViewName(); }
	 * 
	 * private final boolean hasErrors(final ModelAndView modelAndView, final String
	 * key) { BeanPropertyBindingResult result =
	 * (BeanPropertyBindingResult)modelAndView.getModelMap().get(
	 * SPRING_VALIDATION_BINDING_RESULT + key); if(result != null &&
	 * result.hasErrors()) { for(ObjectError o : result.getAllErrors()) {
	 * LOGGER.info(o.toString()); } return true; } return false; }
	 * 
	 */}
