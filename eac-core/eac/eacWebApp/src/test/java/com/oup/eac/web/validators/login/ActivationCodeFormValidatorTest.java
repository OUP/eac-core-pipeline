package com.oup.eac.web.validators.login;

import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import org.easymock.EasyMock;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;

import com.oup.eac.domain.ActivationCode;
import com.oup.eac.domain.ActivationCodeBatch;
import com.oup.eac.dto.ActivationCodeBatchDto;
import com.oup.eac.dto.Message;
import com.oup.eac.service.ActivationCodeService;
import com.oup.eac.service.ServiceLayerException;

public class ActivationCodeFormValidatorTest/* extends AbstractValidatorTest */ {
	/*
	 * 
	 * private ActivationCodeFormValidator validator; private ActivationCodeService
	 * activationCodeService;
	 * 
	 * public ActivationCodeFormValidatorTest() throws NamingException { super(); }
	 * 
	 * @Override public void setUp() { super.setUp(); activationCodeService =
	 * createAndAddMock(ActivationCodeService.class); validator = new
	 * ActivationCodeFormValidator(activationCodeService); }
	 * 
	 * @Test public void testBlankCode() { ActivationCode activationCode = new
	 * ActivationCode();
	 * 
	 * errors.rejectValue(EasyMock.eq("code"), EasyMock.eq("error.not-specified"),
	 * eqLabels(new Object[] { "label.redeemcode" }),
	 * EasyMock.eq("Activation Code is required."));
	 * 
	 * replayMocks();
	 * 
	 * validator.validate(activationCode, errors);
	 * 
	 * verifyMocks(); }
	 * 
	 * @Test public void testInvalidCode() { ActivationCode activationCode = new
	 * ActivationCode(); activationCode.setCode("code");
	 * 
	 * EasyMock.expect(activationCodeService.getActivationCodeByCode(activationCode)
	 * ).andReturn(new ActivationCodeBatchDto()); replayMocks(); }
	 * 
	 * @Test public void testServiceLayerException_AllowedUsageNUll() {
	 * ActivationCode activationCode = new ActivationCode();
	 * activationCode.setCode("code");
	 * 
	 * try { activationCodeService.validateActivationCode(activationCode); } catch
	 * (ServiceLayerException e) { Assert.assertEquals(("Activation code: " +
	 * activationCode.getCode() + " Allowed usage is: " +
	 * activationCode.getAllowedUsage() + " Actual usage is: " +
	 * activationCode.getActualUsage()), e.getMessage()); }
	 * 
	 * }
	 * 
	 * @Test public void testServiceLayerException_startDateAfterNow() {
	 * ActivationCode activationCode = new ActivationCode(); ActivationCodeBatchDto
	 * dto = new ActivationCodeBatchDto(); List<String> codes = new
	 * ArrayList<String>(); codes.add("code"); dto.setActivationCodes(codes);
	 * activationCode.setCode("code"); ActivationCodeBatch activationCodeBatch = new
	 * ActivationCodeBatch(); activationCodeBatch.setStartDate(new
	 * DateTime().plusMonths(1));
	 * activationCode.setActivationCodeBatch(activationCodeBatch); try {
	 * activationCodeService.validateActivationCode(activationCode); } catch
	 * (ServiceLayerException e) { Assert.assertEquals("The activation code: " +
	 * activationCode.getCode() + " may only be consumed after " +
	 * activationCode.getActivationCodeBatch().getStartDate(), new
	 * Message("error.problem.activating.token",
	 * "There was a problem with your activation code."), e.getMessages()); }
	 * 
	 * EasyMock.expect(activationCodeService.getActivationCodeByCode(activationCode)
	 * ).andReturn(dto);
	 * activationCodeService.validateActivationCode(activationCode);
	 * EasyMock.expectLastCall().andThrow(new
	 * ServiceLayerException("The activation code: " + activationCode.getCode() +
	 * " may only be consumed after " +
	 * activationCode.getActivationCodeBatch().getStartDate(), new
	 * Message("error.problem.activating.token",
	 * "There was a problem with your activation code.")));
	 * errors.rejectValue(EasyMock.eq("code"),
	 * EasyMock.eq("error.problem.activating.token"), eqLabels(new Object[0]),
	 * EasyMock.eq("There was a problem with your activation code."));
	 * 
	 * replayMocks(); validator.validate(activationCode, errors); verifyMocks();
	 * 
	 * }
	 * 
	 * @Test public void testServiceLayerException_endDateBeforeNow(){
	 * ActivationCode activationCode = new ActivationCode(); ActivationCodeBatchDto
	 * dto = new ActivationCodeBatchDto(); List<String> codes = new
	 * ArrayList<String>(); codes.add("code"); dto.setActivationCodes(codes);
	 * activationCode.setCode("code"); ActivationCodeBatch activationCodeBatch = new
	 * ActivationCodeBatch(); activationCodeBatch.setStartDate(new
	 * DateTime().minusMonths(1)); activationCodeBatch.setEndDate(new
	 * DateTime().minusDays(1));
	 * activationCode.setActivationCodeBatch(activationCodeBatch);
	 * 
	 * try { activationCodeService.validateActivationCode(activationCode); } catch
	 * (ServiceLayerException e) { Assert.assertEquals("The activation code: " +
	 * activationCode.getCode() + " may only be consumed after " +
	 * activationCode.getActivationCodeBatch().getStartDate(), new
	 * Message("error.problem.activating.token",
	 * "There was a problem with your activation code."), e.getMessages()); }
	 * 
	 * EasyMock.expect(activationCodeService.getActivationCodeByCode(activationCode)
	 * ).andReturn(dto);
	 * activationCodeService.validateActivationCode(activationCode);
	 * EasyMock.expectLastCall().andThrow(new
	 * ServiceLayerException("The activation code: " + activationCode.getCode() +
	 * " may only be consumed after " +
	 * activationCode.getActivationCodeBatch().getStartDate(), new
	 * Message("error.problem.activating.token",
	 * "There was a problem with your activation code.")));
	 * errors.rejectValue(EasyMock.eq("code"),
	 * EasyMock.eq("error.problem.activating.token"), eqLabels(new Object[0]),
	 * EasyMock.eq("There was a problem with your activation code."));
	 * 
	 * replayMocks(); validator.validate(activationCode, errors); verifyMocks();
	 * 
	 * }
	 */}
