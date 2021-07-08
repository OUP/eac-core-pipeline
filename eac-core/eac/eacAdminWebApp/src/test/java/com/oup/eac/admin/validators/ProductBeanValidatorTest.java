package com.oup.eac.admin.validators;

import java.util.Arrays;

import org.apache.commons.lang.StringUtils;
import org.easymock.EasyMock;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.validation.Errors;

import com.oup.eac.domain.CountryMatchRegistrationActivation;
import com.oup.eac.domain.InstantRegistrationActivation;
import com.oup.eac.domain.RegisterableProduct.RegisterableType;
import com.oup.eac.domain.RegistrationActivation;
import com.oup.eac.domain.ValidatedRegistrationActivation;
import com.oup.eac.domain.beans.ProductBean;

public class ProductBeanValidatorTest {

	private final ProductBeanValidator validator = new ProductBeanValidator();

	@Test
	public void shouldRejectWhenProductIdEmpty() {
		ProductBean productBean = new ProductBean("");
		productBean.setProductName("productName");
		productBean.setRegistrationType(RegisterableType.SELF_REGISTERABLE + "");
		productBean.setEmail("somebody@somewhere.com");
		productBean.setDivisionId("14");
		Errors mockErrors = EasyMock.createMock(Errors.class);
		mockErrors.rejectValue("productId", "error.emptyProductId");

		EasyMock.replay(mockErrors);

		validator.validate(productBean, mockErrors);

		if (StringUtils.isBlank(productBean.getProductId())) {
			mockErrors.rejectValue("productId", "error.emptyProductId");
		}
		EasyMock.verify(mockErrors);
	}

	
	
	@Test
	public void shouldRejectWhenNoRegistrationType() {
		ProductBean productBean = new ProductBean("987654");
		//productBean.setAssetId("12345");
		productBean.setRegistrationType("");
		productBean.setProductName("productName");
		productBean.setEmail("somebody@somewhere.com");
		productBean.setDivisionId("14");
		Errors mockErrors = EasyMock.createMock(Errors.class);
		mockErrors.rejectValue("registrationType", "error.emptyRegistrationType");
		
		EasyMock.replay(mockErrors);
		
		validator.validate(productBean, mockErrors);
		
		EasyMock.verify(mockErrors);
	}

	@Test
	public void shouldRejectWhenInvalidHomePageUrl() {
		ProductBean productBean = new ProductBean("987654");
		//productBean.setAssetId("12345");
		productBean.setProductName("productName");
		productBean.setRegistrationType(RegisterableType.SELF_REGISTERABLE + "");
		productBean.setEmail("somebody@somewhere.com");
		productBean.setHomePage("foobar");
		productBean.setDivisionId("14");
		Errors mockErrors = EasyMock.createMock(Errors.class);
		mockErrors.rejectValue("homePage", "error.homePage.malformedUrl");

		EasyMock.replay(mockErrors);

		validator.validate(productBean, mockErrors);

		EasyMock.verify(mockErrors);
	}

	@Test
	public void shouldRejectWhenInvalidLandingPageUrl() {
		ProductBean productBean = new ProductBean("987654");
		productBean.setProductName("productName");
		productBean.setRegistrationType(RegisterableType.SELF_REGISTERABLE + "");
		productBean.setEmail("somebody@somewhere.com");
		productBean.setLandingPage("foobar");
		productBean.setDivisionId("14");
		Errors mockErrors = EasyMock.createMock(Errors.class);
		mockErrors.rejectValue("landingPage", "error.landingPage.malformedUrl");

		EasyMock.replay(mockErrors);

		validator.validate(productBean, mockErrors);

		EasyMock.verify(mockErrors);
	}

	@Test
	public void shouldRejectWhenEmailEmpty() {
		ProductBean productBean = new ProductBean("987654");
		productBean.setProductName("productName");
		productBean.setRegistrationType(RegisterableType.SELF_REGISTERABLE + "");
		productBean.setDivisionId("14");
		Errors mockErrors = EasyMock.createMock(Errors.class);
		mockErrors.rejectValue("email", "error.invalidEmail");

		EasyMock.replay(mockErrors);

		validator.validate(productBean, mockErrors);

		EasyMock.verify(mockErrors);
	}

	@Test
	public void shouldRejectWhenEmailInvalid() {
		ProductBean productBean = new ProductBean("987654");
		productBean.setProductName("productName");
		productBean.setRegistrationType(RegisterableType.SELF_REGISTERABLE + "");
		productBean.setEmail("foobar");
		productBean.setDivisionId("14");
		Errors mockErrors = EasyMock.createMock(Errors.class);
		mockErrors.rejectValue("email", "error.invalidEmail");

		EasyMock.replay(mockErrors);

		validator.validate(productBean, mockErrors);

		EasyMock.verify(mockErrors);
	}
	
	@Test
	public void shouldRejectWhenValidatorEmailEmpty() {
		ProductBean productBean = new ProductBean("987654");
		productBean.setProductName("productName");
		productBean.setRegistrationType(RegisterableType.SELF_REGISTERABLE + "");
		productBean.setEmail("somebody@somewhere.com");
		productBean.setRegistrationActivationId("newValidator");
		productBean.setDivisionId("14");
		Errors mockErrors = EasyMock.createMock(Errors.class);
		mockErrors.rejectValue("email", "error.validatorInvalidEmail");

		EasyMock.replay(mockErrors);

		validator.validate(productBean, mockErrors);

		EasyMock.verify(mockErrors);
	}
	
	@Test
	public void shouldRejectWhenValidatorEmailEmptyExistingValidator() {
		ProductBean productBean = new ProductBean("987654");
		productBean.setProductName("productName");
		productBean.setRegistrationType(RegisterableType.SELF_REGISTERABLE + "");
		productBean.setEmail("somebody@somewhere.com");
		productBean.setDivisionId("14");
		InstantRegistrationActivation instant = new InstantRegistrationActivation();
		instant.setId("12345");
		ValidatedRegistrationActivation validated = new ValidatedRegistrationActivation();
		validated.setId("99999");
		productBean.setRegistrationActivations(Arrays.asList(instant, validated));
		productBean.setRegistrationActivationId("99999");
		
		Errors mockErrors = EasyMock.createMock(Errors.class);
		mockErrors.rejectValue("email", "error.validatorInvalidEmail");
		
		EasyMock.replay(mockErrors);
		
		validator.validate(productBean, mockErrors);
		
		EasyMock.verify(mockErrors);
	}
	
	@Test
	public void shouldRejectWhenValidatorEmailInvalid() {
		ProductBean productBean = new ProductBean("987654");
		productBean.setProductName("productName");
		productBean.setRegistrationType(RegisterableType.SELF_REGISTERABLE + "");
		productBean.setEmail("somebody@somewhere.com");
		productBean.setRegistrationActivationId("newValidator");
		productBean.setValidator("blah");
		productBean.setDivisionId("14");
		Errors mockErrors = EasyMock.createMock(Errors.class);
		mockErrors.rejectValue("email", "error.validatorInvalidEmail");

		EasyMock.replay(mockErrors);

		validator.validate(productBean, mockErrors);

		EasyMock.verify(mockErrors);
	}
	
	@Test
	public void shouldNotRejectWhenValidatorEmailInvalidButNotNewRegistrationActivation() {
		ProductBean productBean = new ProductBean("987654");
		productBean.setProductName("productName");
		productBean.setRegistrationType(RegisterableType.SELF_REGISTERABLE + "");
		productBean.setEmail("somebody@somewhere.com");
		productBean.setValidator("blah");
		productBean.setDivisionId("14");
		Errors mockErrors = EasyMock.createMock(Errors.class);

		EasyMock.replay(mockErrors);

		validator.validate(productBean, mockErrors);

		EasyMock.verify(mockErrors);
	}
	
	@Test
	public void shouldRejectWhenNewCountryMatchActivationAndDescriptionNotSet() {
		ProductBean productBean = new ProductBean("987654");
		productBean.setProductName("productName");
		productBean.setRegistrationType(RegisterableType.SELF_REGISTERABLE + "");
		productBean.setEmail("somebody@somewhere.com");
		productBean.setRegistrationActivationId("newCountryMatch");
		productBean.setDescription("");
		productBean.setDivisionId("14");
		productBean.setUnmatchedRegistrationActivationId("123454");
		productBean.setMatchedRegistrationActivationId("54321");
		productBean.setLocaleList("zh_HK,en_AU");

		Errors mockErrors = EasyMock.createMock(Errors.class);
		mockErrors.rejectValue("description", "error.emptyCountryMatchDescription");

		EasyMock.replay(mockErrors);

		validator.validate(productBean, mockErrors);

		EasyMock.verify(mockErrors);
	}
	
	@Test
	public void shouldRejectWhenCountryMatchDescriptionEmptyExistingValidator() {
		ProductBean productBean = new ProductBean("987654");
		productBean.setProductName("productName");
		productBean.setRegistrationType(RegisterableType.SELF_REGISTERABLE + "");
		productBean.setEmail("somebody@somewhere.com");
		InstantRegistrationActivation instant = new InstantRegistrationActivation();
		instant.setId("12345");
		productBean.setDivisionId("14");
		CountryMatchRegistrationActivation countryMatch = new CountryMatchRegistrationActivation();
		countryMatch.setId("99999");
		productBean.setRegistrationActivations(Arrays.asList(instant, countryMatch));
		productBean.setRegistrationActivationId("99999");
		productBean.setDescription("");
		productBean.setUnmatchedRegistrationActivationId("12345");
		productBean.setMatchedRegistrationActivationId("54321");
		productBean.setLocaleList("zh_HK,en_AU");
		
		Errors mockErrors = EasyMock.createMock(Errors.class);
		mockErrors.rejectValue("description", "error.emptyCountryMatchDescription");
		
		EasyMock.replay(mockErrors);
		
		validator.validate(productBean, mockErrors);
		
		EasyMock.verify(mockErrors);
	}
	
	@Test
	public void shouldRejectWhenNewCountryMatchActivationAndUnmatchedNotSet() {
		ProductBean productBean = new ProductBean("987654");
		productBean.setProductName("productName");
		productBean.setRegistrationType(RegisterableType.SELF_REGISTERABLE + "");
		productBean.setEmail("somebody@somewhere.com");
		productBean.setRegistrationActivationId("newCountryMatch");
		productBean.setDescription("description");
		productBean.setUnmatchedRegistrationActivationId("");
		productBean.setMatchedRegistrationActivationId("54321");
		productBean.setLocaleList("zh_HK,en_AU");
		productBean.setDivisionId("14");
		Errors mockErrors = EasyMock.createMock(Errors.class);
		mockErrors.rejectValue("unmatchedRegistrationActivation", "error.emptyUnmatchedActivation");

		EasyMock.replay(mockErrors);

		validator.validate(productBean, mockErrors);

		EasyMock.verify(mockErrors);
	}
	
	@Test
	public void shouldRejectWhenNewCountryMatchActivationAndMatchedNotSet() {
		ProductBean productBean = new ProductBean("987654");
		productBean.setProductName("productName");
		productBean.setRegistrationType(RegisterableType.SELF_REGISTERABLE + "");
		productBean.setEmail("somebody@somewhere.com");
		productBean.setRegistrationActivationId("newCountryMatch");
		productBean.setDescription("description");
		productBean.setUnmatchedRegistrationActivationId("12345");
		productBean.setMatchedRegistrationActivationId("");
		productBean.setLocaleList("zh_HK,en_AU");
		productBean.setDivisionId("14");
		Errors mockErrors = EasyMock.createMock(Errors.class);
		mockErrors.rejectValue("matchedRegistrationActivation", "error.emptyMatchedActivation");
		
		EasyMock.replay(mockErrors);
		
		validator.validate(productBean, mockErrors);
		
		EasyMock.verify(mockErrors);
	}
	
	@Test
	public void shouldRejectWhenNewCountryMatchActivationAndLocaleListNotSet() {
		ProductBean productBean = new ProductBean("987654");
		productBean.setProductName("productName");
		productBean.setRegistrationType(RegisterableType.SELF_REGISTERABLE + "");
		productBean.setEmail("somebody@somewhere.com");
		productBean.setRegistrationActivationId("newCountryMatch");
		productBean.setDescription("description");
		productBean.setUnmatchedRegistrationActivationId("12345");
		productBean.setMatchedRegistrationActivationId("12345");
		productBean.setLocaleList("");
		productBean.setDivisionId("14");
		Errors mockErrors = EasyMock.createMock(Errors.class);
		mockErrors.rejectValue("localeList", "error.localeListInvalid");
		
		EasyMock.replay(mockErrors);
		
		validator.validate(productBean, mockErrors);
		
		EasyMock.verify(mockErrors);
	}
	
	@Test
	public void shouldRejectWhenNewCountryMatchActivationAndLocaleListStringInvalid1() {
		ProductBean productBean = new ProductBean("987654");
		productBean.setProductName("productName");
		productBean.setRegistrationType(RegisterableType.SELF_REGISTERABLE + "");
		productBean.setEmail("somebody@somewhere.com");
		productBean.setRegistrationActivationId("newCountryMatch");
		productBean.setDescription("description");
		productBean.setUnmatchedRegistrationActivationId("12345");
		productBean.setMatchedRegistrationActivationId("12345");
		productBean.setLocaleList("zh_,EN_AU");
		productBean.setDivisionId("14");
		Errors mockErrors = EasyMock.createMock(Errors.class);
		mockErrors.rejectValue("localeList", "error.localeListInvalid");
		
		EasyMock.replay(mockErrors);
		
		validator.validate(productBean, mockErrors);
		
		EasyMock.verify(mockErrors);
	}
	
	@Test
	public void shouldRejectWhenNewCountryMatchActivationAndLocaleListStringInvalid2() {
		ProductBean productBean = new ProductBean("987654");
		productBean.setProductName("productName");
		productBean.setRegistrationType(RegisterableType.SELF_REGISTERABLE + "");
		productBean.setEmail("somebody@somewhere.com");
		productBean.setRegistrationActivationId("newCountryMatch");
		productBean.setDescription("description");
		productBean.setUnmatchedRegistrationActivationId("12345");
		productBean.setMatchedRegistrationActivationId("12345");
		productBean.setLocaleList("zh_HKen_AU");
		productBean.setDivisionId("14");
		Errors mockErrors = EasyMock.createMock(Errors.class);
		mockErrors.rejectValue("localeList", "error.localeListInvalid");
		
		EasyMock.replay(mockErrors);
		
		validator.validate(productBean, mockErrors);
		
		EasyMock.verify(mockErrors);
	}
	
	
	@Test
	public void shouldRejectWhenNewCountryMatchActivationAndLocaleListStringInvalid3() {
		ProductBean productBean = new ProductBean("987654");
		productBean.setProductName("productName");
		productBean.setRegistrationType(RegisterableType.SELF_REGISTERABLE + "");
		productBean.setEmail("somebody@somewhere.com");
		productBean.setRegistrationActivationId("newCountryMatch");
		productBean.setDescription("description");
		productBean.setUnmatchedRegistrationActivationId("12345");
		productBean.setMatchedRegistrationActivationId("12345");
		productBean.setLocaleList("zh_HKen_AU,");
		productBean.setDivisionId("14");
		Errors mockErrors = EasyMock.createMock(Errors.class);
		mockErrors.rejectValue("localeList", "error.localeListInvalid");
		
		EasyMock.replay(mockErrors);
		
		validator.validate(productBean, mockErrors);
		
		EasyMock.verify(mockErrors);
	}
	
	@Test
	public void shouldRejectWhenNewCountryMatchActivationAndLocaleListNotValidLocales() {
		ProductBean productBean = new ProductBean("987654");
		productBean.setProductName("productName");
		productBean.setRegistrationType(RegisterableType.SELF_REGISTERABLE + "");
		productBean.setEmail("somebody@somewhere.com");
		productBean.setRegistrationActivationId("newCountryMatch");
		productBean.setDescription("description");
		productBean.setUnmatchedRegistrationActivationId("12345");
		productBean.setMatchedRegistrationActivationId("12345");
		productBean.setLocaleList("HK,en_AU,en_H6");
		productBean.setDivisionId("14");
		Errors mockErrors = EasyMock.createMock(Errors.class);
		mockErrors.rejectValue("localeList", "error.localeListInvalid");
		
		EasyMock.replay(mockErrors);
		
		validator.validate(productBean, mockErrors);
		
		EasyMock.verify(mockErrors);
	}
	
	
	
	
	@Test
	public void shouldNotRejectWhenNewCountryMatchActivationAndLocaleListStringIsValid1() {
		ProductBean productBean = new ProductBean("987654");
		productBean.setProductName("productName");
		productBean.setRegistrationType(RegisterableType.SELF_REGISTERABLE + "");
		productBean.setEmail("somebody@somewhere.com");
		productBean.setRegistrationActivationId("newCountryMatch");
		productBean.setDescription("description");
		productBean.setUnmatchedRegistrationActivationId("12345");
		productBean.setMatchedRegistrationActivationId("12345");
		productBean.setLocaleList("zh_HK,en_AU,tr_TR");
		productBean.setDivisionId("14");
		Errors mockErrors = EasyMock.createMock(Errors.class);
		
		EasyMock.replay(mockErrors);
		
		validator.validate(productBean, mockErrors);
		
		EasyMock.verify(mockErrors);
	}
	
	@Test
	public void shouldNotRejectWhenNewCountryMatchActivationAndLocaleListStringIsValid2() {
		ProductBean productBean = new ProductBean("987654");
		productBean.setProductName("productName");
		productBean.setRegistrationType(RegisterableType.SELF_REGISTERABLE + "");
		productBean.setEmail("somebody@somewhere.com");
		productBean.setRegistrationActivationId("newCountryMatch");
		productBean.setDescription("description");
		productBean.setUnmatchedRegistrationActivationId("12345");
		productBean.setMatchedRegistrationActivationId("12345");
		productBean.setLocaleList("zh_HK, en_AU,");
		productBean.setDivisionId("14");
		Errors mockErrors = EasyMock.createMock(Errors.class);
		
		EasyMock.replay(mockErrors);
		
		validator.validate(productBean, mockErrors);
		
		EasyMock.verify(mockErrors);
	}
	
	@Test
	public void shouldRejectWhenLocaleListGreaterThan255Characters() {
		ProductBean productBean = new ProductBean("987654");
		productBean.setProductName("productName");
		productBean.setRegistrationType(RegisterableType.SELF_REGISTERABLE + "");
		productBean.setEmail("somebody@somewhere.com");
		productBean.setRegistrationActivationId("newCountryMatch");
		productBean.setDescription("description");
		productBean.setUnmatchedRegistrationActivationId("12345");
		productBean.setMatchedRegistrationActivationId("12345");
		productBean.setLocaleList("HK, AU,HK, AU,HK, AU,HK, AU,HK, AU,HK, AU,HK, AU,HK, AU,HK, AU,HK, AU,HK, AU,HK, AU,HK, AU,HK, AU,HK, AU,HK, AU,HK, AU,HK, AU,HK, AU,HK, AU,HK, AU,HK, AU,HK, AU,HK, AU,HK, AU,HK, AU,HK, AU,HK, AU,HK, AU,HK, AU,HK, AU,HK, AU,HK, AU,HK, AU,HK, AU,HK, AU,HK, AU,");
		productBean.setDivisionId("14");
		Errors mockErrors = EasyMock.createMock(Errors.class);
		mockErrors.rejectValue("localeList", "error.localeListTooBig");
		
		EasyMock.replay(mockErrors);
		
		validator.validate(productBean, mockErrors);
		
		EasyMock.verify(mockErrors);
	}
	
	@Test
	public void shouldRejectWhenDescriptionGreaterThan100Characters() {
		ProductBean productBean = new ProductBean("987654");
		productBean.setProductName("productName");
		productBean.setRegistrationType(RegisterableType.SELF_REGISTERABLE + "");
		productBean.setEmail("somebody@somewhere.com");
		productBean.setRegistrationActivationId("newCountryMatch");
		productBean.setDescription("description sdklfj sdklfjsdklfj klsdfj klsdjklfgjdfklgjdfklgjdfkl gjdfgldfkgj dflkg dfklgj dfklgj kldfgj kldfgj kldfgj kldfg dfklg dfklgj dfklgh dgklhjgklhj  gfhj j ");
		productBean.setUnmatchedRegistrationActivationId("12345");
		productBean.setMatchedRegistrationActivationId("12345");
		productBean.setLocaleList("zh_HK,en_AU,zh_HK, ");
		productBean.setDivisionId("14");
		Errors mockErrors = EasyMock.createMock(Errors.class);
		mockErrors.rejectValue("description", "error.descriptionTooBig");
		
		EasyMock.replay(mockErrors);
		
		validator.validate(productBean, mockErrors);
		
		EasyMock.verify(mockErrors);
	}
	
	@Test
	public void shouldRejectWhenDescriptionAlreadyExists() {
		CountryMatchRegistrationActivation existing = new CountryMatchRegistrationActivation();
		existing.setDescription("Description");
		existing.setId("D36A9961-F63E-4CF0-B9FC-183B0408A05C");
		ProductBean productBean = new ProductBean("987654");
		productBean.setProductName("productName");
		productBean.setRegistrationType(RegisterableType.SELF_REGISTERABLE + "");
		productBean.setEmail("somebody@somewhere.com");
		productBean.setRegistrationActivationId("newCountryMatch");
		productBean.setDescription("description");
		productBean.setUnmatchedRegistrationActivationId("12345");
		productBean.setMatchedRegistrationActivationId("12345");
		productBean.setLocaleList("zh_HK, en_AU,zh_HK, ");
		productBean.setDivisionId("14");
		productBean.setRegistrationActivations(Arrays.<RegistrationActivation>asList(existing));
		
		Errors mockErrors = EasyMock.createMock(Errors.class);
		mockErrors.rejectValue("description", "error.descriptionAlreadyExists");
		
		EasyMock.replay(mockErrors);
		
		validator.validate(productBean, mockErrors);
		
		EasyMock.verify(mockErrors);
	}

	@Test
	public void shouldRejectWhenLinkedToSelfOnAdd() {
		ProductBean productBean = new ProductBean("987654");
		productBean.setRegistrationType(RegisterableType.SELF_REGISTERABLE + "");
		productBean.setEmail("somebody@somewhere.com");
		productBean.setProductName("abcdef");
		productBean.setLinkedProductsToAdd(productBean.getProductId());
		productBean.setDivisionId("14");
		Errors mockErrors = EasyMock.createMock(Errors.class);
		mockErrors.rejectValue("productId", "error.selfReferentialAssociation");

		EasyMock.replay(mockErrors);

		validator.validate(productBean, mockErrors);

		EasyMock.verify(mockErrors);
	}
	
	@Test
	public void shouldRejectWhenLinkedToSelfOnUpdate() {
		ProductBean productBean = new ProductBean("987654");
		productBean.setRegistrationType(RegisterableType.SELF_REGISTERABLE + "");
		productBean.setEmail("somebody@somewhere.com");
		productBean.setProductName("987654");
		productBean.setLinkedProductsToUpdate("987654,abcdef,PRE_PARENT|987654,12345,PRE_PARENT");
		productBean.setDivisionId("14");
		Errors mockErrors = EasyMock.createMock(Errors.class);
		mockErrors.rejectValue("productId", "error.selfReferentialAssociation");
		
		EasyMock.replay(mockErrors);
		
		validator.validate(productBean, mockErrors);
		
		EasyMock.verify(mockErrors);
	}
}
