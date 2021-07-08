/*package com.oup.eac.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import junit.framework.Assert;

import org.apache.commons.lang.LocaleUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.oup.eac.domain.Answer;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.Customer.CustomerType;
import com.oup.eac.domain.Division;
import com.oup.eac.domain.ExternalCustomerId;
import com.oup.eac.domain.ExternalSystem;
import com.oup.eac.domain.ExternalSystemIdType;
import com.oup.eac.domain.InstantRegistrationActivation;
import com.oup.eac.domain.Password;
import com.oup.eac.domain.ProductPageDefinition;
import com.oup.eac.domain.ProductRegistrationDefinition;
import com.oup.eac.domain.Question;
import com.oup.eac.domain.RegisterableProduct;
import com.oup.eac.domain.RegisterableProduct.RegisterableType;
import com.oup.eac.domain.Registration;
import com.oup.eac.domain.StandardLicenceTemplate;
import com.oup.eac.domain.paging.PagingCriteria;
import com.oup.eac.domain.paging.PagingCriteria.SortDirection;
import com.oup.eac.domain.util.SampleDataFactory;
import com.oup.eac.dto.CustomerSearchCriteria;

*//**
 * @author harlandd User dao hibernate test
 *//*
public class CustomerHibernateDaoIntegrationTest extends AbstractDBTest {

    private static final String TZ_ONE = "Europe/London";
    private static final String TZ_TWO = "Asia/Vladivostok";
    private static final String NO_SORT_COL = null;
    
    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private SessionFactory sessionFactory;
    
    private Customer customer1;
    private Customer customer2;
    private Customer customer3;
    private Customer customer4;
    private Customer customer5;

    private ExternalSystem sys1;
    private ExternalSystem sys2;
    
    private ExternalSystemIdType type1A;
    private ExternalSystemIdType type1B;
    private ExternalSystemIdType type2C;
    private ExternalSystemIdType type2D;
    
    private ExternalCustomerId extid1;
    private ExternalCustomerId extid2;
    private ExternalCustomerId extid3;
    private ExternalCustomerId extid4;
    private ExternalCustomerId extid5;
    private ExternalCustomerId extid6;
    private ExternalCustomerId extid7;
    private ExternalCustomerId extid8;

    private Locale localeWithVariant = LocaleUtils.toLocale("no_NO_NY");
    private Question q1;
    private Question q2;
    
    private Answer answer1;
    private Answer answer2;
	private Answer answer3;
	private Answer answer4;
	private Answer answer5;
    *//**
     * @throws Exception
     *             Sets up data ready for test.
     *//*
    @Before
    public final void setUp() throws Exception {
        customer1 = getSampleDataCreator().createCustomer();
        customer2 = SampleDataFactory.createCustomer();
        customer2.setFirstName("MALAYSIA");
        customer2.setFamilyName("MALAYSIA"+"-familyName");
        customer2.setEmailAddress("MALAYSIA"+"-email");
        customer2.setUsername("MALAYSIA"+"-username");
        customer2.setCreatedDate(new DateTime().minusDays(1));
       // customer2.setErightsId(new Integer(2));
        getSampleDataCreator().loadCustomer(customer2);
        customer3 = SampleDataFactory.createCustomer();
        customer3.setFirstName("ANZ");
        customer3.setFamilyName("ANZ-familyName");
        customer3.setEmailAddress("ANZ-email");
        customer3.setUsername("ANZ-username");
     //   customer3.setErightsId(new Integer(3));
        getSampleDataCreator().loadCustomer(customer3);
        customer4 = SampleDataFactory.createCustomer();
        customer4.setFirstName("CANADA");
        customer4.setFamilyName("CANADA-familyName1");
        customer4.setEmailAddress("CANADA-email1");
        customer4.setUsername("CANADA-username1");
        customer4.setCreatedDate(new DateTime().plusDays(2));
       // customer4.setErightsId(new Integer(4));
        customer4.setLocale(this.localeWithVariant);
        customer4.setTimeZone(TZ_ONE);
        
        getSampleDataCreator().loadCustomer(customer4);
        customer5 = SampleDataFactory.createCustomer();
        customer5.setFirstName("CANADA");
        customer5.setFamilyName("CANADA-familyName2");
        customer5.setEmailAddress("CANADA-email2");
        customer5.setUsername("CANADA-username2");
       // customer5.setErightsId(new Integer(5));
        customer5.setCreatedDate(new DateTime().minusDays(2));
        customer5.setLocale(Locale.CANADA_FRENCH);
        customer5.setTimeZone(TZ_TWO);
        getSampleDataCreator().loadCustomer(customer5);
        
        
        StandardLicenceTemplate standardLicenceTemplate = getSampleDataCreator().createStandardLicenceTemplate();    

        RegisterableProduct malaysiaProduct1 = getSampleDataCreator().createRegisterableProduct(1, "malaysia 1",RegisterableType.SELF_REGISTERABLE);
        InstantRegistrationActivation licenceActivation = getSampleDataCreator().createInstantRegistrationActivation();
        ProductPageDefinition productPageDefinition = getSampleDataCreator().createProductPageDefinition();
        ProductRegistrationDefinition malaysiaRegistrationDefinition1 = getSampleDataCreator().createProductRegistrationDefinition(malaysiaProduct1, standardLicenceTemplate, licenceActivation, productPageDefinition);

        RegisterableProduct malaysiaProduct2 = getSampleDataCreator().createRegisterableProduct(2, "malaysia 2",RegisterableType.SELF_REGISTERABLE);
        ProductRegistrationDefinition malaysiaRegistrationDefinition2 = getSampleDataCreator().createProductRegistrationDefinition(malaysiaProduct2, standardLicenceTemplate, licenceActivation, productPageDefinition);

        RegisterableProduct anzProduct1 = getSampleDataCreator().createRegisterableProduct(3, "malaysia 3",RegisterableType.SELF_REGISTERABLE);
        ProductRegistrationDefinition anzRegistrationDefinition1 = getSampleDataCreator().createProductRegistrationDefinition(anzProduct1, standardLicenceTemplate, licenceActivation, productPageDefinition);

        RegisterableProduct canadaProduct1 = getSampleDataCreator().createRegisterableProduct(4, "malaysia 4",RegisterableType.SELF_REGISTERABLE);
        ProductRegistrationDefinition canadaRegistrationDefinition1 = getSampleDataCreator().createProductRegistrationDefinition(canadaProduct1, standardLicenceTemplate, licenceActivation, productPageDefinition);
        
        Registration<ProductRegistrationDefinition> reg2 = getSampleDataCreator().createRegistration(customer2, malaysiaRegistrationDefinition1);
        customer2.getRegistrations().add(reg2);
        
        getSampleDataCreator().createRegistration(customer2, malaysiaRegistrationDefinition2);
        getSampleDataCreator().createRegistration(customer3, anzRegistrationDefinition1);
        getSampleDataCreator().createRegistration(customer4, canadaRegistrationDefinition1);
        getSampleDataCreator().createRegistration(customer5, canadaRegistrationDefinition1);
        
        sys1 = getSampleDataCreator().createExternalSystem("SYS1", "SYS1");
        sys2 = getSampleDataCreator().createExternalSystem("SYS2", "SYS2");
        
        type1A = getSampleDataCreator().createExternalSystemIdType(sys1,"A","A");
        type1B = getSampleDataCreator().createExternalSystemIdType(sys1,"B","B");
        type2C = getSampleDataCreator().createExternalSystemIdType(sys2,"C","C");
        type2D = getSampleDataCreator().createExternalSystemIdType(sys2,"D","D");
        
        extid1 = getSampleDataCreator().createExternalCustomerId(customer1,"C1-1A-1", type1A);
        extid2 = getSampleDataCreator().createExternalCustomerId(customer1,"C1-1A-2", type1A);
        extid3 = getSampleDataCreator().createExternalCustomerId(customer1,"C1-1B-1", type1B);
        extid4 = getSampleDataCreator().createExternalCustomerId(customer1,"C1-1B-2", type1B);
        
        extid5 = getSampleDataCreator().createExternalCustomerId(customer2,"C2-2C-1", type2C);
        extid6 = getSampleDataCreator().createExternalCustomerId(customer2,"C2-2C-2", type2C);
        extid7 = getSampleDataCreator().createExternalCustomerId(customer2,"C2-2D-1", type2D);
        extid8 = getSampleDataCreator().createExternalCustomerId(customer2,"C2-2D-2", type2D);
        customer2.getExternalIds().add(extid5);
        customer2.getExternalIds().add(extid6);
        customer2.getExternalIds().add(extid7);
        customer2.getExternalIds().add(extid8);
        
        q1 = getSampleDataCreator().createQuestion();
        q2 = getSampleDataCreator().createQuestion();
        
        answer1 = getSampleDataCreator().createAnswer(q1, this.customer1);
        answer2 = getSampleDataCreator().createAnswer(q2, this.customer1);
        
		answer3 = SampleDataFactory.createAnswer(customer2, q1);
		answer3.setAnswerText("foo");
		getSampleDataCreator().loadAnswer(answer3);
		answer4 = SampleDataFactory.createAnswer(customer2, q2);
		answer4.setAnswerText("bar");
		getSampleDataCreator().loadAnswer(answer4);
		answer5 = SampleDataFactory.createAnswer(customer3, q1);
		answer5.setAnswerText("foo");
		getSampleDataCreator().loadAnswer(answer5);
        
		customer2.getAnswers().add(answer3);
		customer2.getAnswers().add(answer4);
        loadAllDataSets();
    }

    *//**
     * @throws Exception
     *             test create user
     *//*
    @Test
    public final void testCreateCustomer() throws Exception {
        Customer user = new Customer("David", "Harland", "username", new Password("password", false), CustomerType.SELF_SERVICE);
        user.setUsername("username");
        customerDao.save(user);
        customerDao.flush();
        assertEquals(6, countRowsInTable(user.getClass().getSimpleName()));
    }

    *//**
     * test get user by id.
     *//*
    @Test
    public final void testGetCustomerById() {
        Customer user = customerDao.getEntity(customer1.getId());
        assertEquals(customer1.getFirstName(), user.getFirstName());
    }

    *//**
     * test get user by erights id.
     *//*
    @Test
    public final void testGetCustomerByErightsId() {
        Customer user = customerDao.getCustomerByErightsId(customer1.getErightsId(), 
                Arrays.asList(customer1.getCustomerType()));
        assertNotNull(user);
        assertEquals(customer1.getFirstName(), user.getFirstName());
    }

    *//**
     * test get user by username
     *//*
    @Test
    public final void testGetCustomerByUsername() {
        checkUsername(customer1.getUsername());
        checkUsername(customer1.getUsername().toLowerCase());
        checkUsername(customer1.getUsername().toUpperCase());        
        checkUsername(null);
        checkUsername("");
        checkUsername("  ");
    }
    
    private void checkUsername(String username){
        Customer user = customerDao.getCustomerByUsername(username);
        if(StringUtils.isBlank(username)){
            assertNull(user);
        }else{
            assertNotNull(user);
            assertEquals(customer1.getUsername(),user.getUsername());
        }
    }

    *//**
     * test update user.
     *//*
    @Test
    public final void testUpdateCustomer() {
        Customer user = customerDao.getEntity(customer1.getId());
        user.setFirstName("Fred");
        customerDao.update(user);

        Customer user2 = customerDao.getEntity(customer1.getId());
        assertEquals("Fred", user2.getFirstName());

    }

    
    @Test
    public void searchCustomers() throws Exception {

        
		List<Customer> customers = customerDao.searchCustomers(new CustomerSearchCriteria(), pagingCriteria(10, 1));
        
        assertEquals("Check number of record returned", 5, customers.size());
        
		customers = customerDao.searchCustomers(where().username("user"), pagingCriteria(10, 1));
        
        assertEquals("Check number of record returned", 5, customers.size());
        
		customers = customerDao.searchCustomers(where().username("MAL"), pagingCriteria(10, 1));
        
        assertEquals("Check number of record returned", 1, customers.size());
        
		customers = customerDao.searchCustomers(where().username("CAN"), pagingCriteria(10, 1));
        
        assertEquals("Check number of record returned", 2, customers.size());
        
		customers = customerDao.searchCustomers(where().username("CAN").email("email").firstName("AN").familyName("family"), pagingCriteria(10, 1));
        
        assertEquals("Check number of record returned", 2, customers.size());
        
		customers = customerDao.searchCustomers(where().username("CAN").familyName("familyName2"), pagingCriteria(10, 1));
        
        assertEquals("Check number of record returned", 1, customers.size());
        
		customers = customerDao.searchCustomers(where().createdDateTo(new DateTime()), pagingCriteria(2, 1));
        
        assertEquals("Check number of record returned", 2, customers.size());
        
		customers = customerDao.searchCustomers(where().createdDateTo(new DateTime()), pagingCriteria(2, 2));
        
        assertEquals("Check number of record returned", 2, customers.size());
        
		customers = customerDao.searchCustomers(where().createdDateFrom(new DateTime()), pagingCriteria(10, 1));
        
        assertEquals("Check number of record returned", 1, customers.size());
        
		customers = customerDao.searchCustomers(where().createdDateFrom(new DateTime()).createdDateTo(new DateTime().plusDays(2)), pagingCriteria(10, 1));
        
        assertEquals("Check number of record returned", 1, customers.size());
        
        //checks that the search is case insensitive
		customers = customerDao.searchCustomers(where().username("can"), pagingCriteria(10, 1));
        assertEquals("Check number of record returned", 2, customers.size());
        
		customers = customerDao.searchCustomers(where().registrationDataKeywords(new String[] { "foo" }), pagingCriteria(10, 1));

		assertEquals("Check number of records returned", 2, customers.size());

		customers = customerDao.searchCustomers(where().registrationDataKeywords(new String[] { "foo", "bar" }), pagingCriteria(10, 1));

		assertEquals("Check number of records returned", 1, customers.size());

		customers = customerDao.searchCustomers(where().registrationDataKeywords(new String[] { "BaR" }), pagingCriteria(10, 1));

		assertEquals("Check number of records returned", 1, customers.size());

		customers = customerDao.searchCustomers(where().email("CANADA-email2").registrationDataKeywords(new String[] { "foo", "bar" }), pagingCriteria(10, 1));

		assertEquals("Check number of records returned", 2, customers.size());

		customers = customerDao.searchCustomers(where().registrationDataKeywords(new String[] { "hello", "foo" }), pagingCriteria(10, 1));

		assertEquals("Check number of records returned", 0, customers.size());
		
		customers = customerDao.searchCustomers(where().externalId("C2-2D-2"), pagingCriteria(10, 1));
		
		assertEquals("Check number of records returned", 1, customers.size());
		
    }
    
    private PagingCriteria pagingCriteria(final int itemsPerPage, final int requestedPage) {
    	return new PagingCriteria(itemsPerPage, requestedPage, SortDirection.ASC, NO_SORT_COL);
    }
    
    @Test
    public void shouldReturnCountOfSearchedCustomers() {
    	int count = customerDao.countSearchCustomers(where().registrationDataKeywords(new String[] { "foo" }));
    	assertEquals("Check count of customers", 2, count);
    }
    
	public CustomerSearchCriteria where() {
		return new CustomerSearchCriteria();
	}

    @Test
    public void testLookupCustomerByExternalCustomerId(){
        checkExternalCustomerId(customer1, extid1);
        checkExternalCustomerId(customer1, extid2);
        checkExternalCustomerId(customer1, extid3);
        checkExternalCustomerId(customer1, extid4);
        
        checkExternalCustomerId(customer2, extid5);
        checkExternalCustomerId(customer2, extid6);
        checkExternalCustomerId(customer2, extid7);
        checkExternalCustomerId(customer2, extid8);
    }

    private void checkExternalCustomerId(Customer expected, ExternalCustomerId extid) {
        String systemId = extid.getExternalSystemIdType().getExternalSystem().getName();
        String typeId = extid.getExternalSystemIdType().getName();
        String externalId = extid.getExternalId();
        Customer result = this.customerDao.getCustomerByExternalCustomerId(systemId, typeId, externalId);
        Assert.assertEquals(expected.getId(),result.getId());
    }
    
    @Test
    public void testLocaleAndTimeZone(){

        Customer cust1 = this.customerDao.getEntity(this.customer1.getId());
        Assert.assertTrue(cust1.getLocale().equals(Locale.getDefault()));
        Assert.assertNull(cust1.getTimeZone());
        
        Customer cust4 = this.customerDao.getEntity(this.customer4.getId());
        Assert.assertTrue(this.localeWithVariant.equals(cust4.getLocale()));
        Assert.assertEquals(TZ_ONE, cust4.getTimeZone());

        Customer cust5 = this.customerDao.getEntity(this.customer5.getId());
        Assert.assertTrue(Locale.CANADA_FRENCH.equals(cust5.getLocale()));
        Assert.assertEquals(TZ_TWO, cust5.getTimeZone());
        
    }
    
    private void sessionClear() {
        Session session = sessionFactory.getCurrentSession();
        session.clear();
    }

    
    @Test
    public void testGetCustomerWithAnswersByErightsId() {
        Customer result = this.customerDao.getCustomerWithAnswersByErightsId(this.customer1.getErightsId());
        sessionClear();// no more lazy loading
        Answer ans1 = null;
        Answer ans2 = null;
        for (Answer a : result.getAnswers()) {
            if (a.getId().equals(this.answer1.getId())) {
                ans1 = a;
            } else if (a.getId().equals(this.answer2.getId())) {
                ans2 = a;
            }
        }
        Assert.assertNotNull(ans1);
        Assert.assertNotNull(ans2);

        Assert.assertEquals(answer1.getId(), ans1.getId());
        Assert.assertEquals(answer2.getId(), ans2.getId());

        Assert.assertEquals(q1.getId(), ans1.getQuestion().getId());
        Assert.assertEquals(q2.getId(), ans2.getQuestion().getId());
    }
    
    @Test
    public void testDelete() {
        //BEFORE
       Customer customer = this.customer2;
       Assert.assertTrue(doesCustomerExist(customer));
       Assert.assertFalse(customer.getAnswers().isEmpty());
       Assert.assertFalse(customer.getRegistrations().isEmpty());
       Assert.assertFalse(customer.getExternalIds().isEmpty());

       //DELETE
       customerDao.deleteCustomer(customer);
       
       //AFTER
       Assert.assertFalse(doesCustomerExist(customer));
    }
    
    boolean doesCustomerExist(Customer customer) {
        @SuppressWarnings("deprecation")
        int  count = this.simpleJdbcTemplate.queryForInt("select count(*) from customer where id = '" + customer.getId()+"'");
        return count == 1;
    }
    
    boolean doesAnswerExist(Answer ans) {
        @SuppressWarnings("deprecation")
        int  count = this.simpleJdbcTemplate.queryForInt("select count(*) from answer where id = '" + ans.getId()+"'");
        return count == 1;
    }
}
    */