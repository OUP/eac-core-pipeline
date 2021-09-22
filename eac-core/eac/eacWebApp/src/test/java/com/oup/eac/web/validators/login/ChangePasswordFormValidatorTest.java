package com.oup.eac.web.validators.login;

import javax.naming.NamingException;

import org.easymock.EasyMock;
import org.junit.Test;

import com.oup.eac.common.utils.crypto.PasswordUtils;
import com.oup.eac.dto.ChangePasswordDto;

public class ChangePasswordFormValidatorTest /* extends AbstractValidatorTest */ {
	/*
	 * 
	 * private ChangePasswordFormValidator validator;
	 * 
	 * public ChangePasswordFormValidatorTest() throws NamingException { super(); }
	 * 
	 * @Override public void setUp() { super.setUp(); validator = new
	 * ChangePasswordFormValidator(); }
	 * 
	 * @Test public void testNoNewPassword() { ChangePasswordDto changePasswordDto =
	 * new ChangePasswordDto();
	 * 
	 * errors.rejectValue(EasyMock.eq("passwordcheck"),
	 * EasyMock.eq("error.not-specified"), eqLabels(new Object[] {
	 * "label.newpassword" }), EasyMock.eq("New Password " + "is required."));
	 * 
	 * replayMocks();
	 * 
	 * validator.validate(changePasswordDto, errors);
	 * 
	 * verifyMocks(); }
	 * 
	 * @Test public void testInvalidNewPassword() { ChangePasswordDto
	 * changePasswordDto = new ChangePasswordDto();
	 * changePasswordDto.setNewPassword("newpassword");
	 * 
	 * errors.rejectValue(EasyMock.eq("passwordcheck"),
	 * EasyMock.eq(PasswordUtils.INVALID_PASSWORD_MSG_CODE), eqLabels(new Object[] {
	 * "label.password" }), EasyMock.eq("Password is too easy."));
	 * 
	 * replayMocks();
	 * 
	 * validator.validate(changePasswordDto, errors);
	 * 
	 * verifyMocks(); }
	 * 
	 * @Test public void testNoConfirmPassword() { ChangePasswordDto
	 * changePasswordDto = new ChangePasswordDto();
	 * changePasswordDto.setNewPassword("Test1%Test");
	 * 
	 * errors.rejectValue(EasyMock.eq("confirmNewPassword"),
	 * EasyMock.eq("error.not-specified"), eqLabels( new Object[] {
	 * "label.confirmnewpassword" }),
	 * EasyMock.eq("Confirm New Password is required."));
	 * 
	 * replayMocks();
	 * 
	 * validator.validate(changePasswordDto, errors);
	 * 
	 * verifyMocks(); }
	 * 
	 * @Test public void testPasswordConfirmPasswordNotSame() { ChangePasswordDto
	 * changePasswordDto = new ChangePasswordDto();
	 * changePasswordDto.setNewPassword("Test1%Test");
	 * changePasswordDto.setConfirmNewPassword("Test1%Test1");
	 * 
	 * errors.reject(EasyMock.eq("error.must.be.same"), eqLabels(new Object[] {
	 * "label.newpassword", "label.confirmnewpassword" }),
	 * EasyMock.eq("New Password and Confirm New Password must be the same"));
	 * 
	 * replayMocks();
	 * 
	 * validator.validate(changePasswordDto, errors);
	 * 
	 * verifyMocks(); }
	 * 
	 */}
