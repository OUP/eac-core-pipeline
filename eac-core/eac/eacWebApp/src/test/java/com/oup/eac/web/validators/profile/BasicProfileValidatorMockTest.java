package com.oup.eac.web.validators.profile;

import java.util.Locale;

import javax.naming.NamingException;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.oup.eac.common.mock.AbstractMockTest;
import com.oup.eac.common.utils.username.UsernameValidator;
import com.oup.eac.dto.BasicProfileDto;

public class BasicProfileValidatorMockTest extends AbstractMockTest {

    private BasicProfileValidator validator;
    private UsernameValidator usernameValidator;

    public BasicProfileValidatorMockTest() throws NamingException {
        super();
    }

    @Before
    public void setup() {
        this.usernameValidator = EasyMock.createMock(UsernameValidator.class);
        this.validator = new BasicProfileValidator(usernameValidator);
        setMocks(this.usernameValidator);
    }

    @Test
    public void testHappyPath() {
        BasicProfileDto target = new BasicProfileDto();
        target.setEmail("david.hay@oup.com");
        target.setUsername("bobbuilder");
        target.setFirstName("david");
        target.setFamilyName("hay");
        target.setTimezone("Europe/London");
        target.setUserLocale(Locale.UK);

        BindingResult errors = new BindException(target, "basicProfileDto");

        EasyMock.expect(this.usernameValidator.isValid("bobbuilder")).andReturn(true);
        replayMocks();
        this.validator.validate(target, errors);
        verifyMocks();
        Assert.assertFalse(errors.hasErrors());
    }

    @Test
    public void testBlankEmail() {
        BasicProfileDto target = new BasicProfileDto();
        target.setEmail("    ");
        target.setUsername("bobbuilder");
        target.setFirstName("david");
        target.setFamilyName("hay");
        target.setTimezone("Europe/London");
        target.setUserLocale(Locale.UK);

        BindingResult errors = new BindException(target, "basicProfileDto");

        EasyMock.expect(this.usernameValidator.isValid("bobbuilder")).andReturn(true);
        replayMocks();
        this.validator.validate(target, errors);
        verifyMocks();
        Assert.assertTrue(errors.hasErrors());
        Assert.assertEquals(1, errors.getFieldErrorCount());
        Assert.assertEquals(0, errors.getGlobalErrorCount());
        FieldError fe = errors.getFieldError("email");
        Assert.assertNotNull(fe);
        checkEmpty(fe);
    }

    private void checkEmpty(FieldError fe) {
        Assert.assertEquals("error.not-specified", fe.getCode());
    }

    @Test
    public void testInvalidEmail() {
        BasicProfileDto target = new BasicProfileDto();
        target.setEmail("test.user@@test.com");
        target.setUsername("bobbuilder");
        target.setFirstName("david");
        target.setFamilyName("hay");
        target.setTimezone("Europe/London");
        target.setUserLocale(Locale.UK);

        BindingResult errors = new BindException(target, "basicProfileDto");

        EasyMock.expect(this.usernameValidator.isValid("bobbuilder")).andReturn(true);
        replayMocks();
        this.validator.validate(target, errors);
        verifyMocks();
        Assert.assertTrue(errors.hasErrors());
        Assert.assertEquals(1, errors.getFieldErrorCount());
        Assert.assertEquals(0, errors.getGlobalErrorCount());
        FieldError fe = errors.getFieldError("email");
        Assert.assertNotNull(fe);
        Assert.assertEquals("error.must.be.valid.email", fe.getCode());
    }

    @Test
    public void testInvalidUsername() {
        BasicProfileDto target = new BasicProfileDto();
        target.setEmail("david.hay@oup.com");
        target.setUsername("bobbuilder");
        target.setFirstName("david");
        target.setFamilyName("hay");
        target.setTimezone("Europe/London");
        target.setUserLocale(Locale.UK);

        BindingResult errors = new BindException(target, "basicProfileDto");

        EasyMock.expect(this.usernameValidator.isValid("bobbuilder")).andReturn(false);
        replayMocks();
        this.validator.validate(target, errors);
        verifyMocks();
        Assert.assertTrue(errors.hasErrors());
        Assert.assertEquals(1, errors.getFieldErrorCount());
        Assert.assertEquals(0, errors.getGlobalErrorCount());
        FieldError fe = errors.getFieldError("username");
        Assert.assertNotNull(fe);
        Assert.assertEquals("error.must.be.valid.username", fe.getCode());
    }

    @Test
    public void testBlankUsername() {
        BasicProfileDto target = new BasicProfileDto();
        target.setEmail("david.hay@oup.com");
        target.setUsername("     ");
        target.setFirstName("david");
        target.setFamilyName("hay");
        target.setTimezone("Europe/London");
        target.setUserLocale(Locale.UK);

        BindingResult errors = new BindException(target, "basicProfileDto");

        replayMocks();
        this.validator.validate(target, errors);
        verifyMocks();
        Assert.assertTrue(errors.hasErrors());
        Assert.assertEquals(1, errors.getFieldErrorCount());
        Assert.assertEquals(0, errors.getGlobalErrorCount());
        FieldError fe = errors.getFieldError("username");
        Assert.assertNotNull(fe);
        checkEmpty(fe);

    }

    @Test
    public void testBlankFirstName() {
        BasicProfileDto target = new BasicProfileDto();
        target.setEmail("david.hay@oup.com");
        target.setUsername("bobbuilder");
        target.setFirstName("    ");
        target.setFamilyName("hay");
        target.setTimezone("Europe/London");
        target.setUserLocale(Locale.UK);

        BindingResult errors = new BindException(target, "basicProfileDto");

        EasyMock.expect(this.usernameValidator.isValid("bobbuilder")).andReturn(true);
        replayMocks();
        this.validator.validate(target, errors);
        verifyMocks();
        int count = errors.getErrorCount();
        Assert.assertEquals(0,count);
    }

    @Test
    public void testBlankFamilyName() {
        BasicProfileDto target = new BasicProfileDto();
        target.setEmail("david.hay@oup.com");
        target.setUsername("bobbuilder");
        target.setFirstName("david");
        target.setFamilyName("   ");
        target.setTimezone("Europe/London");
        target.setUserLocale(Locale.UK);

        BindingResult errors = new BindException(target, "basicProfileDto");

        EasyMock.expect(this.usernameValidator.isValid("bobbuilder")).andReturn(true);
        replayMocks();
        this.validator.validate(target, errors);
        verifyMocks();
        int count = errors.getErrorCount();
        Assert.assertEquals(0, count);
    }

    @Test
    public void testBlankLocale() {
        BasicProfileDto target = new BasicProfileDto();
        target.setEmail("david.hay@oup.com");
        target.setUsername("bobbuilder");
        target.setFirstName("david");
        target.setFamilyName("hay");
        target.setTimezone("Europe/London");
        target.setUserLocale(null);

        BindingResult errors = new BindException(target, "basicProfileDto");

        EasyMock.expect(this.usernameValidator.isValid("bobbuilder")).andReturn(true);
        replayMocks();
        this.validator.validate(target, errors);
        verifyMocks();
        Assert.assertFalse(errors.hasErrors());
        
    }

    @Test
    public void testBlankTimeZone() {
        BasicProfileDto target = new BasicProfileDto();
        target.setEmail("david.hay@oup.com");
        target.setUsername("bobbuilder");
        target.setFirstName("david");
        target.setFamilyName("hay");
        target.setTimezone("");
        target.setUserLocale(Locale.UK);

        BindingResult errors = new BindException(target, "basicProfileDto");

        EasyMock.expect(this.usernameValidator.isValid("bobbuilder")).andReturn(true);
        replayMocks();
        this.validator.validate(target, errors);
        verifyMocks();
        Assert.assertFalse(errors.hasErrors());
        
    }

    @Test
    public void testInvalidTimeZone() {
        BasicProfileDto target = new BasicProfileDto();
        target.setEmail("david.hay@oup.com");
        target.setUsername("bobbuilder");
        target.setFirstName("david");
        target.setFamilyName("hay");
        target.setTimezone("Europe/Oxford");
        target.setUserLocale(Locale.UK);

        BindingResult errors = new BindException(target, "basicProfileDto");

        EasyMock.expect(this.usernameValidator.isValid("bobbuilder")).andReturn(true);
        replayMocks();
        this.validator.validate(target, errors);
        verifyMocks();
        Assert.assertFalse(errors.hasErrors());
        
    }

    @Test
    public void testAssignable() {
        Assert.assertTrue(validator.supports(BasicProfileDto.class));
    }

}
