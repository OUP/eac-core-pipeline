package com.oup.eac.admin.validators;

import static org.easymock.EasyMock.expect;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.validation.Errors;

import com.oup.eac.common.utils.EACSettings;
import com.oup.eac.common.utils.crypto.PasswordUtils;

@RunWith(PowerMockRunner.class)
@PrepareForTest(EACSettings.class)
public class PasswordValidatorHelperTest {
	
	private Errors mockErrors ;

	@Before
    public void setup() {
		mockErrors = EasyMock.createMock(Errors.class);
		 PowerMock.mockStatic(EACSettings.class);
		 
		}
	@Test
	public void shouldFailWhenPasswordEmpty() {
		mockErrors.reject("error.password.required");
		EasyMock.replay(mockErrors);
		
		PasswordValidatorHelper.validate("", "abc", mockErrors);
		
		EasyMock.verify(mockErrors);
	}
	
	@Test
	public void shouldFailWhenConfirmEmpty() {
		mockErrors.reject("error.password.again.required");
		EasyMock.replay(mockErrors);
		
		PasswordValidatorHelper.validate("abc", "", mockErrors);
		
		EasyMock.verify(mockErrors);
	}
	
	@Test
	public void shouldFailWhenPasswordAndConfirmDontMatch() {
		mockErrors.reject("error.passwords.dont.match");
		EasyMock.replay(mockErrors);
		
		PasswordValidatorHelper.validate("abc", "abcd", mockErrors);
		
		EasyMock.verify(mockErrors);
	}
	@Test
	public void shouldFailWhenPasswordTooEasy() {
		mockErrors.reject(PasswordUtils.INVALID_PASSWORD_MSG_CODE);
		expect(EACSettings.getProperty(EACSettings.PASSWORD_POLICY_REGEX)).andReturn("^(?=.*[a-z])(?=.*[A-Z])\\S{6,}$");
		PowerMock.expectLastCall();
		PowerMock.replay(EACSettings.class);
		EasyMock.replay(mockErrors);
		PasswordValidatorHelper.validate("abc", "abc", mockErrors);
		
		EasyMock.verify(mockErrors);
	}
}
