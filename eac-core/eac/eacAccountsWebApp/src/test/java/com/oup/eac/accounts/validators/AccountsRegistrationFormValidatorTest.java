package com.oup.eac.accounts.validators;

import javax.naming.NamingException;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;

import com.oup.eac.common.utils.username.UsernameValidator;
import com.oup.eac.domain.Customer;
import com.oup.eac.dto.AccountRegistrationDto;
import com.oup.eac.service.CustomerService;

public class AccountsRegistrationFormValidatorTest /* extends AbstractValidatorTest */ {
	/*
	 * 
	 * private AccountsRegistrationFormValidator validator; private CustomerService
	 * customerService; private UsernameValidator usernameValidator;
	 * 
	 * public AccountsRegistrationFormValidatorTest() throws NamingException {
	 * super(); }
	 * 
	 * @Override public void setUp() { super.setUp(); customerService =
	 * EasyMock.createMock(CustomerService.class); usernameValidator =
	 * EasyMock.createMock(UsernameValidator.class); addMock(customerService);
	 * addMock(usernameValidator); validator = new
	 * AccountsRegistrationFormValidator(customerService, usernameValidator); }
	 * 
	 * @Test public void testValidateNoFirstNameValues() throws Exception {
	 * AccountRegistrationDto accountRegistrationDto = new AccountRegistrationDto();
	 * 
	 * errors.rejectValue(EasyMock.eq("firstName"),
	 * EasyMock.eq("error.not-specified"), eqLabels(new Object[] { "title.firstname"
	 * }), EasyMock.eq("First Name is required.")); replayMocks();
	 * validator.validate(accountRegistrationDto, errors); verifyMocks(); }
	 * 
	 * @Test public void testValidateNoFamilyNameValues() throws Exception {
	 * AccountRegistrationDto accountRegistrationDto = new AccountRegistrationDto();
	 * 
	 * accountRegistrationDto.setFirstName("firstName");
	 * errors.rejectValue(EasyMock.eq("familyName"),
	 * EasyMock.eq("error.not-specified"), eqLabels(new Object[] {
	 * "title.familyname" }), EasyMock.eq("Surname is required.")); replayMocks();
	 * validator.validate(accountRegistrationDto, errors); verifyMocks(); }
	 * 
	 * @Test public void testValidateNoUsernameValues() throws Exception {
	 * AccountRegistrationDto accountRegistrationDto = new AccountRegistrationDto();
	 * 
	 * accountRegistrationDto.setFirstName("firstName");
	 * accountRegistrationDto.setFamilyName("familyName");
	 * errors.rejectValue(EasyMock.eq("username"),
	 * EasyMock.eq("error.not-specified"), eqLabels(new Object[] { "title.username"
	 * }), EasyMock.eq("Username is required.")); replayMocks();
	 * validator.validate(accountRegistrationDto, errors); verifyMocks(); }
	 * 
	 * @Test public void testValidateNoEmailValues() throws Exception {
	 * AccountRegistrationDto accountRegistrationDto = new AccountRegistrationDto();
	 * 
	 * accountRegistrationDto.setFirstName("firstName");
	 * accountRegistrationDto.setFamilyName("familyName");
	 * accountRegistrationDto.setUsername("jsmith.test@test.com");
	 * EasyMock.expect(usernameValidator.isValid("jsmith.test@test.com")).andReturn(
	 * true);
	 * EasyMock.expect(customerService.getCustomerByUsername("jsmith.test@test.com")
	 * ).andReturn(null); errors.rejectValue(EasyMock.eq("email"),
	 * EasyMock.eq("error.not-specified"), eqLabels(new Object[] { "title.email" }),
	 * EasyMock.eq("Email address is required.")); replayMocks();
	 * validator.validate(accountRegistrationDto, errors); verifyMocks(); }
	 * 
	 * @Test public void testValidatePasswordAllDataCorrectAndPasswordIsStrong()
	 * throws Exception { AccountRegistrationDto accountRegistrationDto = new
	 * AccountRegistrationDto();
	 * 
	 * accountRegistrationDto.setUsername("jsmith");
	 * accountRegistrationDto.setPassword("Pa$$w0rd");
	 * accountRegistrationDto.setConfirmPassword("Pa$$w0rd");
	 * accountRegistrationDto.setFamilyName("familyName");
	 * accountRegistrationDto.setFirstName("firstName");
	 * accountRegistrationDto.setEmail("test@test.com");
	 * accountRegistrationDto.setReadOnly(true);
	 * 
	 * EasyMock.expect(usernameValidator.isValid("jsmith")).andReturn(true);
	 * EasyMock.expect(customerService.getCustomerByUsername("jsmith")).andReturn(
	 * null); replayMocks();
	 * 
	 * validator.validate(accountRegistrationDto, errors);
	 * 
	 * verifyMocks();
	 * 
	 * }
	 * 
	 * @Test public void testValidateOnlyUsernameCustomerAlreadyExists() throws
	 * Exception { AccountRegistrationDto accountRegistrationDto = new
	 * AccountRegistrationDto();
	 * 
	 * Customer customer = SampleDataFactory.createCustomer();
	 * customer.setUsername("test@test.com");
	 * 
	 * accountRegistrationDto.setUsername(customer.getUsername());
	 * accountRegistrationDto.setFamilyName("familyName");
	 * accountRegistrationDto.setFirstName("firstName");
	 * 
	 * EasyMock.expect(usernameValidator.isValid("test@test.com")).andReturn(true);
	 * EasyMock.expect(customerService.getCustomerByUsername(customer.getUsername())
	 * ).andReturn(customer);
	 * 
	 * errors.rejectValue(EasyMock.eq("username"),
	 * EasyMock.eq("error.username.taken.update"),
	 * EasyMock.eq("This username is already taken. Please try another."));
	 * replayMocks();
	 * 
	 * try { validator.validate(accountRegistrationDto, errors); } catch (
	 * RuntimeException ex ) { Assert.fail("unexpected exception"); } verifyMocks();
	 * }
	 * 
	 * @Test public void testValidateOnlyValidUsername() throws Exception {
	 * AccountRegistrationDto accountRegistrationDto = new AccountRegistrationDto();
	 * 
	 * accountRegistrationDto.setUsername("test@test.com");
	 * accountRegistrationDto.setPassword("Pa$$w0rd");
	 * accountRegistrationDto.setConfirmPassword("Pa$$w0rd");
	 * accountRegistrationDto.setFamilyName("familyName");
	 * accountRegistrationDto.setFirstName("firstName");
	 * accountRegistrationDto.setEmail("test@test.com");
	 * accountRegistrationDto.setReadOnly(true);
	 * 
	 * EasyMock.expect(usernameValidator.isValid("test@test.com")).andReturn(true);
	 * EasyMock.expect(customerService.getCustomerByUsername("test@test.com")).
	 * andReturn(null); replayMocks();
	 * 
	 * validator.validate(accountRegistrationDto, errors); verifyMocks(); }
	 * 
	 * @Test public void testValidateInvalidEmailAddress() throws Exception {
	 * AccountRegistrationDto accountRegistrationDto = new AccountRegistrationDto();
	 * 
	 * accountRegistrationDto.setUsername("test@test.com");
	 * accountRegistrationDto.setEmail("test");
	 * accountRegistrationDto.setFamilyName("familyName");
	 * accountRegistrationDto.setFirstName("firstName");
	 * EasyMock.expect(usernameValidator.isValid("test@test.com")).andReturn(true);
	 * EasyMock.expect(customerService.getCustomerByUsername("test@test.com")).
	 * andReturn(null);
	 * 
	 * errors.rejectValue(EasyMock.eq("email"),
	 * EasyMock.eq("error.must.be.valid.email"), eqLabels(new Object[] {
	 * "title.email" }),
	 * EasyMock.eq("Email address must be a valid email address.")); replayMocks();
	 * 
	 * validator.validate(accountRegistrationDto, errors);
	 * 
	 * verifyMocks(); }
	 * 
	 * @Test public void testValidatePasswordsNotTheSame() throws Exception {
	 * AccountRegistrationDto accountRegistrationDto = new AccountRegistrationDto();
	 * 
	 * accountRegistrationDto.setFamilyName("familyName");
	 * accountRegistrationDto.setFirstName("firstName");
	 * accountRegistrationDto.setUsername("test@test.com");
	 * accountRegistrationDto.setPassword("Passw0rd");
	 * accountRegistrationDto.setConfirmPassword("confirmPassword");
	 * accountRegistrationDto.setEmail("test@test.com");
	 * 
	 * EasyMock.expect(usernameValidator.isValid("test@test.com")).andReturn(true);
	 * EasyMock.expect(customerService.getCustomerByUsername("test@test.com")).
	 * andReturn(null);
	 * 
	 * errors.reject(EasyMock.eq("error.must.be.same"), eqLabels(new Object[] {
	 * "title.password", "title.confirmpassword" }),
	 * EasyMock.eq("Password and Confirm Password must be the same"));
	 * replayMocks();
	 * 
	 * validator.validate(accountRegistrationDto, errors);
	 * 
	 * verifyMocks();
	 * 
	 * }
	 * 
	 */}
