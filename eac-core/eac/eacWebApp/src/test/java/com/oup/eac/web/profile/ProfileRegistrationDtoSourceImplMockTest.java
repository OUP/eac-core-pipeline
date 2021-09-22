package com.oup.eac.web.profile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.naming.NamingException;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import com.oup.eac.domain.Customer;
import com.oup.eac.domain.ProductRegistration;
import com.oup.eac.domain.ProductRegistrationDefinition;
import com.oup.eac.domain.RegisterableProduct;
import com.oup.eac.domain.Registration;
import com.oup.eac.dto.CustomerRegistrationsDto;
import com.oup.eac.dto.LicenceDto;
import com.oup.eac.dto.profile.ProfileRegistrationDto;
import com.oup.eac.service.RegistrationService;
import com.oup.eac.service.ServiceLayerException;

public class ProfileRegistrationDtoSourceImplMockTest /* extends AbstractMockTest */ {
	/*
	 * 
	 * private static final String ID_ONE = "111"; private static final String
	 * ID_TWO = "222";
	 * 
	 * private RegistrationService registrationService; private
	 * ProfileRegistrationDtoSourceImpl sut; private Customer customer; private
	 * CustomerRegistrationsDto data;
	 * 
	 * private LicenceDto lic1; private LicenceDto lic2; private ProductRegistration
	 * reg1; private ProductRegistration reg2;
	 * 
	 * public ProfileRegistrationDtoSourceImplMockTest() throws NamingException {
	 * super(); }
	 * 
	 * @Before public void setup() { registrationService =
	 * EasyMock.createMock(RegistrationService.class); sut = new
	 * ProfileRegistrationDtoSourceImpl(registrationService);
	 * setMocks(registrationService); customer = new Customer();
	 * 
	 * reg1 = new ProductRegistration(); ProductRegistrationDefinition regDef1 = new
	 * ProductRegistrationDefinition(); RegisterableProduct prod1 = new
	 * RegisterableProduct(); prod1.setProductName("PRODUCT_ONE");
	 * regDef1.setProduct(prod1); reg1.setRegistrationDefinition(regDef1);
	 * reg1.setId(ID_ONE);
	 * 
	 * reg2 = new ProductRegistration(); ProductRegistrationDefinition regDef2 = new
	 * ProductRegistrationDefinition(); RegisterableProduct prod2 = new
	 * RegisterableProduct(); prod2.setProductName("PRODUCT_TWO");
	 * regDef2.setProduct(prod2); reg2.setRegistrationDefinition(regDef2);
	 * reg2.setId(ID_TWO);
	 * 
	 * @SuppressWarnings("unchecked") List<Registration<? extends
	 * ProductRegistrationDefinition>> regs = new ArrayList<Registration<? extends
	 * ProductRegistrationDefinition>>(); regs.add(reg1); regs.add(reg2); lic1 = new
	 * LicenceDto("1111", new DateTime(), false, true, true, false, false);
	 * lic1.setLicenseId(ID_ONE);
	 * 
	 * lic2 = new LicenceDto("1234", new DateTime(), false, true, true, false,
	 * false); lic2.setLicenseId(ID_TWO); List<LicenceDto> lics =
	 * Arrays.asList(lic1, lic2);
	 * 
	 * data = new CustomerRegistrationsDto(customer, regs, lics); }
	 * 
	 * @Test public void testHappyPath() throws ServiceLayerException {
	 * 
	 * EasyMock.expect(this.registrationService.
	 * getEntitlementsForCustomerRegistrations(customer, null
	 * ,true)).andReturn(data);
	 * 
	 * replayMocks(); List<ProfileRegistrationDto> result =
	 * sut.getProfileRegistrationDtos(customer); verifyMocks();
	 * 
	 * Assert.assertEquals(2, result.size());
	 * 
	 * ProfileRegistrationDto res1 = result.get(0); Assert.assertEquals(lic1,
	 * res1.getLicenceDto()); Assert.assertEquals(reg1, res1.getRegistration());
	 * 
	 * ProfileRegistrationDto res2 = result.get(1); Assert.assertEquals(lic2,
	 * res2.getLicenceDto()); Assert.assertEquals(reg2, res2.getRegistration()); }
	 * 
	 * @Test public void testNoRegistrationsOrLicences() throws
	 * ServiceLayerException {
	 * 
	 * CustomerRegistrationsDto data = new CustomerRegistrationsDto(this.customer,
	 * null, null); EasyMock.expect(this.registrationService.
	 * getEntitlementsForCustomerRegistrations(customer, null
	 * ,true)).andReturn(data);
	 * 
	 * replayMocks(); List<ProfileRegistrationDto> result =
	 * sut.getProfileRegistrationDtos(customer); verifyMocks();
	 * 
	 * Assert.assertEquals(0, result.size());
	 * 
	 * }
	 * 
	 * @Test public void testUnHappyPath() throws ServiceLayerException {
	 * 
	 * ServiceLayerException sle = new ServiceLayerException();
	 * EasyMock.expect(this.registrationService.
	 * getEntitlementsForCustomerRegistrations(customer, null ,true)).andThrow(sle);
	 * 
	 * replayMocks(); try { sut.getProfileRegistrationDtos(customer);
	 * Assert.fail("exception expected"); } catch (Exception ex) {
	 * Assert.assertEquals(sle, ex); } finally { verifyMocks(); } }
	 */}
