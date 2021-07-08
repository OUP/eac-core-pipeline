package com.oup.eac.web.validators.registration;

import javax.naming.NamingException;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;

import com.oup.eac.common.utils.EACSettings;
import com.oup.eac.common.utils.crypto.PasswordUtils;
import com.oup.eac.common.utils.username.UsernameValidator;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.util.SampleDataFactory;
import com.oup.eac.dto.AccountRegistrationDto;
import com.oup.eac.service.CustomerService;
import com.oup.eac.web.validators.AbstractValidatorTest;

public class AccountRegistrationFormValidatorTest extends AbstractValidatorTest {

    private AccountRegistrationFormValidator validator;
    private CustomerService customerService;
    private UsernameValidator usernameValidator;

    public AccountRegistrationFormValidatorTest() throws NamingException {
        super();
    }

    @Override
    public void setUp() {
        super.setUp();
        customerService = EasyMock.createMock(CustomerService.class);
        usernameValidator = EasyMock.createMock(UsernameValidator.class);
        addMock(customerService);
        addMock(usernameValidator);
        validator = new AccountRegistrationFormValidator(customerService, usernameValidator);
    }

    @Test
    public void testValidateNoValues() throws Exception {
        AccountRegistrationDto accountRegistrationDto = new AccountRegistrationDto();

        errors.rejectValue(EasyMock.eq("firstName"), EasyMock.eq("error.not-specified"), eqLabels(new Object[] { "title.firstname" }),
                EasyMock.eq("First Name is required."));
        errors.rejectValue(EasyMock.eq("familyName"), EasyMock.eq("error.not-specified"), eqLabels(new Object[] { "title.familyname" }),
                EasyMock.eq("Surname is required."));
        errors.rejectValue(EasyMock.eq("email"), EasyMock.eq("error.not-specified"), eqLabels(new Object[] { "title.email" }),
                EasyMock.eq("Email address is required."));
        errors.rejectValue(EasyMock.eq("username"), EasyMock.eq("error.not-specified"), eqLabels(new Object[] { "title.username" }),
                EasyMock.eq("Username is required."));
        errors.rejectValue(EasyMock.eq("passwordcheck"), EasyMock.eq("error.not-specified"), eqLabels(new Object[] { "title.password" }),
                EasyMock.eq("Password is required."));
        errors.rejectValue(EasyMock.eq("confirmPassword"), EasyMock.eq("error.not-specified"), eqLabels(new Object[] { "title.confirmpassword" }),
                EasyMock.eq("Confirm Password is required."));
        replayMocks();

        validator.validate(accountRegistrationDto, errors);

        verifyMocks();

    }

    @Test
    public void testValidateOnlyUsernameCustomerAlreadyExists() throws Exception {
        AccountRegistrationDto accountRegistrationDto = new AccountRegistrationDto();

        Customer customer = SampleDataFactory.createCustomer();
        customer.setUsername("test@test.com");

        accountRegistrationDto.setUsername(customer.getUsername());

        EasyMock.expect(usernameValidator.isValid("test@test.com")).andReturn(true);
        EasyMock.expect(customerService.getCustomerByUsername(customer.getUsername())).andReturn(customer);
        
        errors.rejectValue(EasyMock.eq("username"), EasyMock.eq("error.username.taken"),
                eqLabels(new Object[] { EACSettings.getProperty(EACSettings.EAC_LOGIN_URL) }),
                EasyMock.eq("This username is already taken. Please try another."));
        errors.rejectValue(EasyMock.eq("passwordcheck"), EasyMock.eq("error.not-specified"), eqLabels(new Object[] { "title.password" }),
                EasyMock.eq("Password is required."));
        errors.rejectValue(EasyMock.eq("confirmPassword"), EasyMock.eq("error.not-specified"), eqLabels(new Object[] { "title.confirmpassword" }),
                EasyMock.eq("Confirm Password is required."));
        errors.rejectValue(EasyMock.eq("familyName"), EasyMock.eq("error.not-specified"), eqLabels(new Object[] { "title.familyname" }),
                EasyMock.eq("Surname is required."));
        errors.rejectValue(EasyMock.eq("firstName"), EasyMock.eq("error.not-specified"), eqLabels(new Object[] { "title.firstname" }),
                EasyMock.eq("First Name is required."));
        errors.rejectValue(EasyMock.eq("email"), EasyMock.eq("error.not-specified"), eqLabels(new Object[] { "title.email" }),
                EasyMock.eq("Email address is required."));
        
        replayMocks();

        try {
            validator.validate(accountRegistrationDto, errors);
        } catch ( RuntimeException ex ) {
            Assert.fail("unexpected exception");
        }

        verifyMocks();

    }

    @Test
    public void testValidateOnlyValidUsername() throws Exception {
        AccountRegistrationDto accountRegistrationDto = new AccountRegistrationDto();

        accountRegistrationDto.setUsername("jsmith");

        EasyMock.expect(usernameValidator.isValid("jsmith")).andReturn(true);
        EasyMock.expect(customerService.getCustomerByUsername("jsmith")).andReturn(null);
        errors.rejectValue(EasyMock.eq("passwordcheck"), EasyMock.eq("error.not-specified"), eqLabels(new Object[] { "title.password" }),
                EasyMock.eq("Password is required."));
        errors.rejectValue(EasyMock.eq("confirmPassword"), EasyMock.eq("error.not-specified"), eqLabels(new Object[] { "title.confirmpassword" }),
                EasyMock.eq("Confirm Password is required."));
        errors.rejectValue(EasyMock.eq("familyName"), EasyMock.eq("error.not-specified"), eqLabels(new Object[] { "title.familyname" }),
                EasyMock.eq("Surname is required."));
        errors.rejectValue(EasyMock.eq("firstName"), EasyMock.eq("error.not-specified"), eqLabels(new Object[] { "title.firstname" }),
                EasyMock.eq("First Name is required."));
        errors.rejectValue(EasyMock.eq("email"), EasyMock.eq("error.not-specified"), eqLabels(new Object[] { "title.email" }),
                EasyMock.eq("Email address is required."));
        replayMocks();

        validator.validate(accountRegistrationDto, errors);

        verifyMocks();

    }

    @Test
    public void testValidateInvalidEmailAddress() throws Exception {
        AccountRegistrationDto accountRegistrationDto = new AccountRegistrationDto();

        accountRegistrationDto.setEmail("test");

        errors.rejectValue(EasyMock.eq("email"), EasyMock.eq("error.must.be.valid.email"), eqLabels(new Object[] { "title.email" }),
                EasyMock.eq("Email address must be a valid email address."));
        errors.rejectValue(EasyMock.eq("username"), EasyMock.eq("error.not-specified"), eqLabels(new Object[] { "title.username" }),
                EasyMock.eq("Username is required."));
        errors.rejectValue(EasyMock.eq("passwordcheck"), EasyMock.eq("error.not-specified"), eqLabels(new Object[] { "title.password" }),
                EasyMock.eq("Password is required."));
        errors.rejectValue(EasyMock.eq("confirmPassword"), EasyMock.eq("error.not-specified"), eqLabels(new Object[] { "title.confirmpassword" }),
                EasyMock.eq("Confirm Password is required."));
        errors.rejectValue(EasyMock.eq("familyName"), EasyMock.eq("error.not-specified"), eqLabels(new Object[] { "title.familyname" }),
                EasyMock.eq("Surname is required."));
        errors.rejectValue(EasyMock.eq("firstName"), EasyMock.eq("error.not-specified"), eqLabels(new Object[] { "title.firstname" }),
                EasyMock.eq("First Name is required."));
        replayMocks();

        validator.validate(accountRegistrationDto, errors);

        verifyMocks();
    }

    @Test
    public void testValidatePasswordsNotTheSame() throws Exception {
        AccountRegistrationDto accountRegistrationDto = new AccountRegistrationDto();

        accountRegistrationDto.setUsername("test@test.com");
        accountRegistrationDto.setPassword("password");
        accountRegistrationDto.setConfirmPassword("confirmPassword");

        EasyMock.expect(usernameValidator.isValid("test@test.com")).andReturn(true);
        EasyMock.expect(customerService.getCustomerByUsername("test@test.com")).andReturn(null);
        errors.rejectValue(EasyMock.eq("passwordcheck"), EasyMock.eq(PasswordUtils.INVALID_PASSWORD_MSG_CODE), eqLabels(new Object[] { "title.password" }),
                EasyMock.eq("Password is too easy."));
        errors.reject(EasyMock.eq("error.must.be.same"), eqLabels(new Object[] { "title.password", "title.confirmpassword" }),
                EasyMock.eq("Password and Confirm Password must be the same"));
        errors.rejectValue(EasyMock.eq("familyName"), EasyMock.eq("error.not-specified"), eqLabels(new Object[] { "title.familyname" }),
                EasyMock.eq("Surname is required."));
        errors.rejectValue(EasyMock.eq("firstName"), EasyMock.eq("error.not-specified"), eqLabels(new Object[] { "title.firstname" }),
                EasyMock.eq("First Name is required."));
        errors.rejectValue(EasyMock.eq("email"), EasyMock.eq("error.not-specified"), eqLabels(new Object[] { "title.email" }),
                EasyMock.eq("Email address is required."));
        replayMocks();

        validator.validate(accountRegistrationDto, errors);

        verifyMocks();

    }

    @Test
    public void testValidatePasswordAllDataCorrectAndPasswordIsStrong() throws Exception {
        AccountRegistrationDto accountRegistrationDto = new AccountRegistrationDto();

        accountRegistrationDto.setUsername("jsmith");
        accountRegistrationDto.setPassword("Pa$$w0rd");
        accountRegistrationDto.setConfirmPassword("Pa$$w0rd");
        accountRegistrationDto.setFamilyName("familyName");
        accountRegistrationDto.setFirstName("firstName");
        accountRegistrationDto.setTitle("title");
        accountRegistrationDto.setEmail("test@test.com");

        EasyMock.expect(usernameValidator.isValid("jsmith")).andReturn(true);
        EasyMock.expect(customerService.getCustomerByUsername("jsmith")).andReturn(null);
        replayMocks();

        validator.validate(accountRegistrationDto, errors);

        verifyMocks();

    }

}
