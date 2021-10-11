package com.oup.eac.admin.binding;

import static org.junit.Assert.assertEquals;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.binding.convert.ConversionService;
import org.springframework.test.context.ContextConfiguration;

import com.oup.eac.common.date.utils.DateUtils;
import com.oup.eac.domain.BaseDomainObject;
import com.oup.eac.domain.ExternalSystem;
import com.oup.eac.domain.ExternalSystemIdType;
import com.oup.eac.domain.InstantRegistrationActivation;
import com.oup.eac.domain.LicenceTemplate;
import com.oup.eac.domain.OUPBaseDomainObject;
import com.oup.eac.domain.Product;
import com.oup.eac.domain.ProductRegistrationDefinition;
import com.oup.eac.domain.RegisterableProduct;
import com.oup.eac.domain.RegisterableProduct.RegisterableType;
import com.oup.eac.domain.RegistrationDefinition;

//@ContextConfiguration(locations = {"classpath*:/eac/web.eac*-beans.xml", "classpath*:/eac/test.web.eac*-beans.xml"})
public class ConversionServiceTest /* extends AbstractDbMessageTest */ {
	/*
	 * 
	 * @Autowired private ConversionService defaultConversionService;
	 * 
	 * private RegisterableProduct product; private LicenceTemplate licenceTemplate;
	 * private InstantRegistrationActivation licenceActivation; private
	 * RegistrationDefinition registrationDefinition; private DateTime dateTime;
	 * private ExternalSystem externalSystem; private ExternalSystemIdType
	 * externalSystemIdType;
	 * 
	 * @Before public void setUp() throws Exception { product =
	 * getSampleDataCreator().createRegisterableProduct(12345,"productName",
	 * RegisterableType.SELF_REGISTERABLE); licenceTemplate =
	 * getSampleDataCreator().createStandardLicenceTemplate(); licenceActivation =
	 * getSampleDataCreator().createInstantRegistrationActivation();
	 * registrationDefinition =
	 * getSampleDataCreator().createActivationCodeRegistrationDefinition(product,
	 * licenceTemplate, licenceActivation, null); dateTime =
	 * DateUtils.parseToDate("01/02/2012", null); externalSystem =
	 * getSampleDataCreator().createExternalSystem("external system name",
	 * "external system description"); externalSystemIdType =
	 * getSampleDataCreator().createExternalSystemIdType(externalSystem,
	 * "external system id type name", "external system id type description");
	 * 
	 * loadAllDataSets(); }
	 * 
	 * @Test public void testFormatterToString() {
	 * assertEquals("Check correct product format", "productName",
	 * (String)defaultConversionService.executeConversion(product, String.class));
	 * assertEquals("Check correct registrationDefinition format", "productName",
	 * (String)defaultConversionService.executeConversion(registrationDefinition,
	 * String.class)); assertEquals("Check correct localDateTime format",
	 * DateUtils.printAsDate(dateTime, null),
	 * (String)defaultConversionService.executeConversion(dateTime, String.class));
	 * //assertEquals("Check correct empty string format", null,
	 * defaultConversionService.executeConversion("", String.class));
	 * assertEquals("Check correct null string format", null,
	 * (String)defaultConversionService.executeConversion(null, String.class));
	 * assertEquals("Check correct external system id format",
	 * externalSystem.getId(),
	 * (String)defaultConversionService.executeConversion(externalSystem,
	 * String.class)); assertEquals("Check correct external system id type format",
	 * "external system id type name",
	 * (String)defaultConversionService.executeConversion(externalSystemIdType,
	 * String.class)); }
	 * 
	 * @Test public void testFormatteFromString() {
	 * assertEquals("Check correct product from string", product.getId(),
	 * ((OUPBaseDomainObject)defaultConversionService.executeConversion(product.
	 * getId(), Product.class)).getId());
	 * assertEquals("Check correct registrationDefinition from string",
	 * registrationDefinition.getId(),
	 * ((BaseDomainObject)defaultConversionService.executeConversion(
	 * registrationDefinition.getId(),
	 * ProductRegistrationDefinition.class)).getId());
	 * assertEquals("Check correct dateTime from string", dateTime,
	 * ((DateTime)defaultConversionService.executeConversion(DateUtils.printAsDate(
	 * dateTime, null), DateTime.class))); }
	 */}
