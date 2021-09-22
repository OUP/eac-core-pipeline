package com.oup.eac.data;

import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.hibernate.SessionFactory;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.oup.eac.domain.AccountRegistrationDefinition;
import com.oup.eac.domain.ActivationCode;
import com.oup.eac.domain.ActivationCodeBatch;
import com.oup.eac.domain.ActivationCodeRegistrationDefinition;
import com.oup.eac.domain.AdminUser;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.LicenceTemplate;
import com.oup.eac.domain.ProductPageDefinition;
import com.oup.eac.domain.ProductRegistrationDefinition;
import com.oup.eac.domain.RegisterableProduct;
import com.oup.eac.domain.RegisterableProduct.RegisterableType;
import com.oup.eac.domain.Registration;
import com.oup.eac.domain.StandardLicenceTemplate;
import com.oup.eac.domain.paging.PagingCriteria;
import com.oup.eac.domain.paging.PagingCriteria.SortDirection;
import com.oup.eac.data.util.SampleDataFactory;
import com.oup.eac.dto.RegistrationDefinitionSearchCriteria;

/**
 * Registration hibernate test.
 */
public class RegistrationDefinitionHibernateDaoIntegrationTest /* extends AbstractDBTest */ {
	/*
	 * 
	 * @Autowired private SessionFactory sessionFactory;
	 * 
	 * @Autowired private RegistrationDefinitionDao registrationDefinitionDao;
	 * 
	 * private RegisterableProduct malaysiaProduct1;
	 * 
	 * private RegisterableProduct malaysiaProduct2;
	 * 
	 * private RegisterableProduct malaysiaProduct3;
	 * 
	 * private RegisterableProduct malaysiaProduct4;
	 * 
	 * private RegisterableProduct anzProduct1;
	 * 
	 * private RegisterableProduct anzProduct2;
	 * 
	 * private ProductRegistrationDefinition malaysiaRegistrationDefinition1;
	 * 
	 * private ActivationCodeRegistrationDefinition
	 * malaysiaActivationCodeRegistrationDefinition1;
	 * 
	 * private ActivationCodeRegistrationDefinition
	 * malaysiaActivationCodeRegistrationDefinition2;
	 * 
	 * private ActivationCodeRegistrationDefinition
	 * malaysiaActivationCodeRegistrationDefinition3;
	 * 
	 * private ActivationCodeRegistrationDefinition
	 * anzActivationCodeRegistrationDefinition1;
	 * 
	 * private ActivationCodeRegistrationDefinition
	 * anzActivationCodeRegistrationDefinition2;
	 * 
	 * private AdminUser adminUser;
	 * 
	 * private AdminUser adminUser2;
	 * 
	 * private RegisterableProduct malaysiaProductNoLicTemp;
	 * 
	 * private ProductRegistrationDefinition
	 * malaysiaRegistrationDefinitionNoLicTemp;
	 * 
	 * private AccountRegistrationDefinition accountRegDef;
	 * 
	 * private Registration<ProductRegistrationDefinition> activatedReg; private
	 * Registration<ActivationCodeRegistrationDefinition>
	 * activatedRegActivationCode; private
	 * Registration<ProductRegistrationDefinition> nonActivatedReg;
	 * 
	 * private Customer customerForActivatedReg; private Customer
	 * customerForNonActivatedReg;
	 * 
	 * private ProductPageDefinition malaysiaProductPageDefinition;
	 * 
	 * private ActivationCodeRegistrationDefinition acRegDef; private
	 * ActivationCodeBatch acb; private ActivationCode ac; private
	 * RegisterableProduct productAC;
	 * 
	 *//**
		 * @throws Exception Sets up data ready for test.
		 */
	/*
	 * @Before public final void setUp() throws Exception {
	 * getSampleDataCreator().createCustomer(); StandardLicenceTemplate
	 * standardLicenceTemplate =
	 * getSampleDataCreator().createStandardLicenceTemplate();
	 * 
	 * adminUser = getSampleDataCreator().createAdminUser(); adminUser2 =
	 * getSampleDataCreator().createAdminUser();
	 * 
	 * 
	 * malaysiaProduct4 =
	 * getSampleDataCreator().createRegisterableProduct(Integer.valueOf(4)
	 * ,"malaysia 4", RegisterableType.SELF_REGISTERABLE);
	 * 
	 * //ExternalSystem externalSystem =
	 * getSampleDataCreator().createExternalSystem("abcdef", "abcdef-desc"); //
	 * ExternalSystemIdType externalSystemIdType =
	 * getSampleDataCreator().createExternalSystemIdType(externalSystem, "abcdef",
	 * "abcdef-desc"); //ExternalProductId externalProductId =
	 * getSampleDataCreator().createExternalProductId(malaysiaProduct4, "12345",
	 * externalSystemIdType); //malaysiaProduct4.setExternalIds(new
	 * HashSet<ExternalProductId>(Arrays.asList(externalProductId)));
	 * malaysiaProduct1 =
	 * getSampleDataCreator().createRegisterableProduct(Integer.valueOf(1)
	 * ,"malaysia 1", RegisterableType.SELF_REGISTERABLE);
	 * 
	 * malaysiaProduct2 =
	 * getSampleDataCreator().createRegisterableProduct(Integer.valueOf(2)
	 * ,"malaysia 2", RegisterableType.SELF_REGISTERABLE);
	 * 
	 * anzProduct2 =
	 * getSampleDataCreator().createRegisterableProduct(Integer.valueOf(6),"anz 2"
	 * ,RegisterableType.SELF_REGISTERABLE);
	 * 
	 * malaysiaProduct3 =
	 * getSampleDataCreator().createRegisterableProduct(Integer.valueOf(3)
	 * ,"malaysia 3", RegisterableType.SELF_REGISTERABLE);
	 * 
	 * anzProduct1 =
	 * getSampleDataCreator().createRegisterableProduct(Integer.valueOf(5),"anz 1",
	 * RegisterableType.SELF_REGISTERABLE);
	 * 
	 * //SelfRegistrationActivation selfLicenceActivation =
	 * getSampleDataCreator().createSelfRegistrationActivation(); //
	 * InstantRegistrationActivation instantLicenceActivation =
	 * getSampleDataCreator().createInstantRegistrationActivation();
	 * malaysiaProductPageDefinition =
	 * getSampleDataCreator().createProductPageDefinition();
	 * malaysiaRegistrationDefinition1 =
	 * getSampleDataCreator().createProductRegistrationDefinition(malaysiaProduct1,
	 * standardLicenceTemplate, null, malaysiaProductPageDefinition);
	 * 
	 * malaysiaActivationCodeRegistrationDefinition1 =
	 * getSampleDataCreator().createActivationCodeRegistrationDefinition(
	 * malaysiaProduct2, standardLicenceTemplate, null, null);
	 * malaysiaActivationCodeRegistrationDefinition2 =
	 * getSampleDataCreator().createActivationCodeRegistrationDefinition(
	 * malaysiaProduct3, standardLicenceTemplate, null, null);
	 * malaysiaActivationCodeRegistrationDefinition3 =
	 * getSampleDataCreator().createActivationCodeRegistrationDefinition(
	 * malaysiaProduct4, standardLicenceTemplate, null, null);
	 * 
	 * anzActivationCodeRegistrationDefinition1 =
	 * getSampleDataCreator().createActivationCodeRegistrationDefinition(
	 * anzProduct1, standardLicenceTemplate, null, null);
	 * anzActivationCodeRegistrationDefinition2 =
	 * getSampleDataCreator().createActivationCodeRegistrationDefinition(
	 * anzProduct2, standardLicenceTemplate, null, null);
	 * 
	 * malaysiaProductNoLicTemp =
	 * getSampleDataCreator().createRegisterableProduct(Integer.valueOf(1111),
	 * "malaysia temp",RegisterableType.SELF_REGISTERABLE);
	 * 
	 * malaysiaRegistrationDefinitionNoLicTemp =
	 * getSampleDataCreator().createProductRegistrationDefinition(
	 * malaysiaProductNoLicTemp, null, null, malaysiaProductPageDefinition);
	 * 
	 * accountRegDef = getSampleDataCreator().createAccountRegistrationDefinition(
	 * malaysiaProductNoLicTemp, null, null);
	 * 
	 * customerForActivatedReg = getSampleDataCreator().createCustomer();
	 * customerForActivatedReg.setFirstName("ACT_CUST"); customerForNonActivatedReg
	 * = getSampleDataCreator().createCustomer();
	 * customerForNonActivatedReg.setFirstName("NON_ACT_CUST");
	 * 
	 * this.activatedReg =
	 * SampleDataFactory.createProductRegistration(customerForActivatedReg,
	 * malaysiaRegistrationDefinition1, new DateTime(), new DateTime());
	 * this.activatedReg.setCompleted(true); this.activatedReg.setActivated(true);
	 * 
	 * 
	 * 
	 * this.nonActivatedReg =
	 * SampleDataFactory.createProductRegistration(customerForNonActivatedReg,
	 * malaysiaRegistrationDefinition1, new DateTime(), new DateTime());
	 * this.nonActivatedReg.setActivated(false);
	 * 
	 * this.productAC = getSampleDataCreator().createRegisterableProduct(501," ",
	 * RegisterableType.SELF_REGISTERABLE);
	 * 
	 * this.acRegDef =
	 * getSampleDataCreator().createActivationCodeRegistrationDefinition(productAC,
	 * standardLicenceTemplate, null, malaysiaProductPageDefinition);
	 * 
	 * //this.acb =
	 * getSampleDataCreator().createActivationCodeBatch(standardLicenceTemplate,
	 * ActivationCodeFormat.EAC_NUMERIC, acRegDef, new DateTime(), new
	 * DateTime().plus(Period.years(1))); // this.ac =
	 * getSampleDataCreator().createActivationCode(acb, new EacActivationCode());
	 * 
	 * this.activatedRegActivationCode =
	 * SampleDataFactory.createActivationCodeRegistration(customerForActivatedReg,
	 * acRegDef, ac); this.activatedRegActivationCode.setCompleted(true);
	 * this.activatedRegActivationCode.setActivated(true);
	 * 
	 * //getSampleDataCreator().loadRegistration(activatedReg);
	 * //getSampleDataCreator().loadRegistration(nonActivatedReg);
	 * //getSampleDataCreator().loadRegistration(activatedRegActivationCode);
	 * loadAllDataSets(); }
	 * 
	 *//**
		 * Tests getting a data export data.
		 */
	/*
	 * @Test public final void testRegistrationsByDateAndOwner() {
	 * 
	 * List<Map<String,Object>> x = simpleJdbcTemplate.
	 * queryForList("select id, page_definition_type from page_definition"); for
	 * (Map<String,Object> y : x) { for (String key : y.keySet()){ Object o =
	 * y.get(key); System.out.println("Value:" + o); } }
	 * 
	 * x = simpleJdbcTemplate.
	 * queryForList("select id, registration_definition_type, page_definition_id from registration_definition"
	 * ); for (Map<String,Object> y : x) { for (String key : y.keySet()){ Object o =
	 * y.get(key); System.out.println("Value:" + o); } }
	 * 
	 * ProductRegistrationDefinition productRegistrationDefinition =
	 * registrationDefinitionDao.getRegistrationDefinitionByProduct(
	 * ProductRegistrationDefinition.class, malaysiaProduct1);
	 * assertEquals(malaysiaRegistrationDefinition1.getId(),
	 * productRegistrationDefinition.getId());
	 * assertEquals(PageDefinitionType.PRODUCT_PAGE_DEFINITION,
	 * productRegistrationDefinition.getPageDefinition().getPageDefinitionType()); }
	 * 
	 * @Test public final void testActivationCodeRegistrations() {
	 * List<ActivationCodeRegistrationDefinition> registrationDefinitions =
	 * registrationDefinitionDao.getActivationCodeRegistrationDefinitionsByAdminUser
	 * (adminUser);
	 * 
	 * Assert.assertEquals("Check correct number of activation code registrations",
	 * 6, registrationDefinitions.size());
	 * 
	 * Assert.assertEquals("Check correct name", "anz 1",
	 * registrationDefinitions.get(1).getProduct().getProductName());
	 * 
	 * Assert.assertEquals("Check correct name", "malaysia 4",
	 * registrationDefinitions.get(5).getProduct().getProductName()); }
	 * 
	 * @Test public final void
	 * testLicenceTemplateLoadingAvicationCodeRegistrationDefinition() {
	 * clearSession();// NO MORE LAZY LOADING ActivationCodeRegistrationDefinition
	 * acRegDef = this.registrationDefinitionDao.getRegistrationDefinitionByProduct(
	 * ActivationCodeRegistrationDefinition.class, this.anzProduct1);
	 * LicenceTemplate lt1 = acRegDef.getLicenceTemplate();
	 * Assert.assertNotNull(lt1);
	 * Assert.assertEquals(this.anzActivationCodeRegistrationDefinition1.getId(),
	 * acRegDef.getId()); }
	 * 
	 * @Test public final void
	 * testLicenceTemplateLoadingProductRegistrationDefinition() { clearSession();//
	 * NO MORE LAZY LOADING ProductRegistrationDefinition profRegDef =
	 * this.registrationDefinitionDao.getRegistrationDefinitionByProduct(
	 * ProductRegistrationDefinition.class, this.malaysiaProduct1); LicenceTemplate
	 * lt2 = profRegDef.getLicenceTemplate(); Assert.assertNotNull(lt2);
	 * Assert.assertEquals(this.malaysiaRegistrationDefinition1.getId(),profRegDef.
	 * getId()); }
	 * 
	 * @Test public final void
	 * testLicenceTemplateLoadingProductRegistrationDefinitionNoLicenceTemplate() {
	 * clearSession();// NO MORE LAZY LOADING ProductRegistrationDefinition
	 * profRegDefNoLicTemp =
	 * this.registrationDefinitionDao.getRegistrationDefinitionByProduct(
	 * ProductRegistrationDefinition.class, this.malaysiaProductNoLicTemp);
	 * LicenceTemplate lt3 = profRegDefNoLicTemp.getLicenceTemplate();
	 * Assert.assertNull(lt3);
	 * Assert.assertEquals(this.malaysiaRegistrationDefinitionNoLicTemp.getId(),
	 * profRegDefNoLicTemp.getId()); }
	 * 
	 * @Test public final void
	 * testLicenceTemplateLoadingAccountRegistrationDefinition() { clearSession();//
	 * NO MORE LAZY LOADING AccountRegistrationDefinition accountRegDefNoLicTemp =
	 * this.registrationDefinitionDao.getRegistrationDefinitionByProduct(
	 * AccountRegistrationDefinition.class, this.malaysiaProductNoLicTemp);
	 * Assert.assertNotNull(accountRegDefNoLicTemp);
	 * Assert.assertEquals(this.accountRegDef.getId(),accountRegDefNoLicTemp.getId()
	 * );
	 * 
	 * }
	 * 
	 * private void clearSession(){ sessionFactory.getCurrentSession().clear(); }
	 * 
	 * @Test public void
	 * testGetProductRegistrationDefinitionByCustomerAndRegistrationId(){ //the
	 * customer and registration match and the registration is activated
	 * ProductRegistrationDefinition regDef1 = this.registrationDefinitionDao.
	 * getProductRegistrationDefinitionFromCustomerAndRegistrationId(
	 * customerForActivatedReg, activatedReg.getId());
	 * 
	 * //NO MORE LAZY LOADING clearSession();
	 * 
	 * Assert.assertEquals(this.malaysiaRegistrationDefinition1.getId(),
	 * regDef1.getId()); Assert.assertEquals(malaysiaProductPageDefinition.getId(),
	 * regDef1.getPageDefinition().getId());
	 * 
	 * //the registration is not activated ProductRegistrationDefinition regDef2 =
	 * this.registrationDefinitionDao.
	 * getProductRegistrationDefinitionFromCustomerAndRegistrationId(
	 * customerForNonActivatedReg, nonActivatedReg.getId());
	 * Assert.assertNull(regDef2);
	 * 
	 * //the customer and registration do not match ProductRegistrationDefinition
	 * regDef3 = this.registrationDefinitionDao.
	 * getProductRegistrationDefinitionFromCustomerAndRegistrationId(
	 * customerForNonActivatedReg, activatedReg.getId());
	 * Assert.assertNull(regDef3); }
	 * 
	 * @Test public void
	 * testGetActivationCodeRegistrationDefinitionByCustomerAndRegistrationId(){
	 * //the customer and registration match and the registration is activated
	 * ProductRegistrationDefinition regDef1 = this.registrationDefinitionDao.
	 * getProductRegistrationDefinitionFromCustomerAndRegistrationId(
	 * customerForActivatedReg, activatedRegActivationCode.getId());
	 * 
	 * Assert.assertNotNull(regDef1);
	 * Assert.assertEquals(RegistrationDefinitionType.ACTIVATION_CODE_REGISTRATION,
	 * regDef1.getRegistrationDefinitionType()); Assert.assertTrue(regDef1
	 * instanceof ActivationCodeRegistrationDefinition);
	 * ActivationCodeRegistrationDefinition codeRegDef =
	 * (ActivationCodeRegistrationDefinition)regDef1;
	 * 
	 * //NO MORE LAZY LOADING clearSession();
	 * 
	 * Assert.assertEquals(this.acRegDef.getId(), codeRegDef.getId());
	 * Assert.assertEquals(malaysiaProductPageDefinition.getId(),
	 * codeRegDef.getPageDefinition().getId());
	 * Assert.assertEquals(this.productAC.getId(), codeRegDef.getProduct().getId());
	 * 
	 * 
	 * }
	 * 
	 * @Test public void shouldFindRegistrationDefinitionsByProductName() {
	 * List<RegistrationDefinition> regDefs =
	 * registrationDefinitionDao.searchRegistrationDefinitions(where().
	 * productName("malaysia 1").adminUser(adminUser), pagingCriteria());
	 * 
	 * assertThat(regDefs.size(), equalTo(1)); ProductRegistrationDefinition
	 * prodRegDef = (ProductRegistrationDefinition) regDefs.get(0);
	 * assertThat(prodRegDef.getProduct().getProductName(), equalTo("malaysia 1"));
	 * 
	 * regDefs =
	 * registrationDefinitionDao.searchRegistrationDefinitions(where().productName(
	 * "malAysIa").adminUser(adminUser), pagingCriteria());
	 * 
	 * assertThat(regDefs.size(), equalTo(5)); }
	 * 
	 * @Test public void
	 * shouldNotFindRegistrationDefinitionsByProductNameWhenDifferentAdminUser() {
	 * List<RegistrationDefinition> regDefs =
	 * registrationDefinitionDao.searchRegistrationDefinitions(where().
	 * productName("malaysia 1").adminUser(adminUser2), pagingCriteria());
	 * 
	 * assertThat(regDefs.size(), equalTo(0)); }
	 * 
	 * @Test public void shouldFindRegistrationDefinitionsByProductId() { String id
	 * = malaysiaProduct1.getId(); List<RegistrationDefinition> regDefs =
	 * registrationDefinitionDao.searchRegistrationDefinitions(where().productId(id)
	 * .adminUser(adminUser), pagingCriteria());
	 * 
	 * assertThat(regDefs.size(), equalTo(1)); ProductRegistrationDefinition
	 * prodRegDef = (ProductRegistrationDefinition) regDefs.get(0);
	 * assertThat(prodRegDef.getProduct().getProductName(), equalTo("malaysia 1"));
	 * }
	 * 
	 * @Test public void shouldFindRegistrationDefinitionsByExternalId() {
	 * List<RegistrationDefinition> regDefs =
	 * registrationDefinitionDao.searchRegistrationDefinitions(where().externalId(
	 * "12345").adminUser(adminUser), pagingCriteria());
	 * 
	 * assertThat(regDefs.size(), equalTo(1)); ProductRegistrationDefinition
	 * prodRegDef = (ProductRegistrationDefinition) regDefs.get(0);
	 * assertThat(prodRegDef.getProduct().getProductName(), equalTo("malaysia 4"));
	 * }
	 * 
	 * @Test public void shouldFindRegistrationDefinitionsByProductNameAndDivision()
	 * { List<RegistrationDefinition> regDefs =
	 * registrationDefinitionDao.searchRegistrationDefinitions(where().
	 * productName("anz 1").adminUser(adminUser), pagingCriteria());
	 * 
	 * assertThat(regDefs.size(), equalTo(1)); ProductRegistrationDefinition
	 * prodRegDef = (ProductRegistrationDefinition) regDefs.get(0);
	 * assertThat(prodRegDef.getProduct().getProductName(), equalTo("anz 1")); }
	 * 
	 * @Test public void shouldFindNothing() { List<RegistrationDefinition> regDefs
	 * =
	 * registrationDefinitionDao.searchRegistrationDefinitions(where().productName(
	 * "foobar").adminUser(adminUser), pagingCriteria());
	 * 
	 * assertThat(regDefs.size(), equalTo(0)); }
	 * 
	 * @Test public void shouldReturnCountOfRegistrationDefinitions() { int count =
	 * registrationDefinitionDao.countSearchRegistrationDefinitions(where().
	 * adminUser(adminUser)); assertThat(count, equalTo(6)); }
	 * 
	 * @Test public void shouldReturnCountOfActivationCodeRegistrationDefinitions()
	 * { int count =
	 * registrationDefinitionDao.countSearchRegistrationDefinitions(where()
	 * .registrationDefinitionType(RegistrationDefinitionType.
	 * ACTIVATION_CODE_REGISTRATION).adminUser(adminUser)); assertThat(count,
	 * equalTo(4)); }
	 * 
	 * private RegistrationDefinitionSearchCriteria where() { return new
	 * RegistrationDefinitionSearchCriteria(); }
	 * 
	 * private PagingCriteria pagingCriteria() { return new PagingCriteria(100, 1,
	 * SortDirection.ASC, null); }
	 * 
	 */}
