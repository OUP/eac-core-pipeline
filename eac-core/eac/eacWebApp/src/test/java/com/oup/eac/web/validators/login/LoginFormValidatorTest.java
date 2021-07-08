package com.oup.eac.web.validators.login;

import javax.naming.NamingException;

import org.easymock.EasyMock;
import org.junit.Test;

import com.oup.eac.dto.LoginDto;
import com.oup.eac.web.validators.AbstractValidatorTest;

public class LoginFormValidatorTest extends AbstractValidatorTest {

	private LoginFormValidator validator;
	
	public LoginFormValidatorTest() throws NamingException {
		super();
	}

	@Override
	public void setUp() {
		super.setUp();
		validator = new LoginFormValidator();
	}

	@Test
	public void testValidateNoUsernameOrPassword() throws Exception {
		
		LoginDto loginDto = new LoginDto();
		
		errors.rejectValue(EasyMock.eq("username"), EasyMock.eq("error.not-specified"), eqLabels(new Object[] { "label.username" }), EasyMock.eq("Username is required."));
		replayMocks();
		
		validator.validate(loginDto, errors);
		
		verifyMocks();
		
	}
	
	@Test
	public void testValidateNoPassword() throws Exception {
		
		LoginDto loginDto = new LoginDto();
		loginDto.setUsername("username");
		
		errors.rejectValue(EasyMock.eq("password"), EasyMock.eq("error.not-specified"), eqLabels(new Object[] { "label.password" }), EasyMock.eq("Password is required."));
		replayMocks();
		
		validator.validate(loginDto, errors);
		
		verifyMocks();
		
	}
	
	@Test
	public void testValidate() throws Exception {
		
		LoginDto loginDto = new LoginDto();
		loginDto.setUsername("username");
		loginDto.setPassword("password");
		
		replayMocks();
		
		validator.validate(loginDto, errors);
		
		verifyMocks();
		
	}
	
	
}
