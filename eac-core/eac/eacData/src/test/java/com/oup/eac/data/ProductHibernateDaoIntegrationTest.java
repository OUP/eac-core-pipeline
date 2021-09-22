package com.oup.eac.data;

import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import junit.framework.Assert;

import org.hibernate.SessionFactory;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.oup.eac.domain.AdminUser;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.Customer.CustomerType;
import com.oup.eac.domain.ExternalProductId;
import com.oup.eac.domain.ExternalSystem;
import com.oup.eac.domain.ExternalSystemIdType;
import com.oup.eac.domain.LicenceTemplate;
import com.oup.eac.domain.LinkedProduct;
import com.oup.eac.domain.Password;
import com.oup.eac.domain.Product;
import com.oup.eac.domain.Product.ProductState;
import com.oup.eac.domain.ProductPageDefinition;
import com.oup.eac.domain.ProductRegistrationDefinition;
import com.oup.eac.domain.RegisterableProduct;
import com.oup.eac.domain.RegisterableProduct.RegisterableType;
import com.oup.eac.domain.Registration;
import com.oup.eac.domain.SelfRegistrationActivation;
import com.oup.eac.domain.StandardLicenceTemplate;
import com.oup.eac.domain.User.EmailVerificationState;
import com.oup.eac.data.util.SampleDataFactory;

public class ProductHibernateDaoIntegrationTest extends AbstractDBTest {

    private static final Set<ProductState> ACTIVE_STATE = new HashSet<ProductState>(Arrays.asList(ProductState.ACTIVE));
    
	@Autowired
    private AdminUserDao adminUserDao;
	
    @Autowired
    private ProductDao productDao;
    
    @Autowired
    private CustomerDao customerDao;

    private RegisterableProduct product;
    private RegisterableProduct product1;
    private LinkedProduct product2;
    private RegisterableProduct product4;
    private RegisterableProduct product10;
    
    private StandardLicenceTemplate standardLicenceTemplate;
    
    private ExternalSystem sys1;
    private ExternalSystem sys2;
    
    private ExternalSystemIdType type1A;
    private ExternalSystemIdType type1B;
    private ExternalSystemIdType type2C;
    private ExternalSystemIdType type2D;
    
    private ExternalProductId extid1;
    private ExternalProductId extid2;
    private ExternalProductId extid3;
    private ExternalProductId extid4;
    private ExternalProductId extid5;
    private ExternalProductId extid6;
    private ExternalProductId extid7;
    private ExternalProductId extid8;    
    
    private AdminUser adminUser;
    
    private Integer erightsId1 = Integer.valueOf(1234567891);
    private Integer erightsId2 = Integer.valueOf(1234567892);
    private Integer erightsId3 = Integer.valueOf(1234567893);
    private Integer erightsId4 = Integer.valueOf(1234567894);
    private Integer testErightsId = Integer.valueOf(123123);
    
    
    @Autowired
    private SessionFactory sessionFactory;

    private RegisterableProduct testProduct1;
    private RegisterableProduct testProduct2;    
    private Customer testCustomer;
    
    /**
     * Setup test by creating a standard licence template and a product definition. Load all datasets.
     * 
     * @throws Exception
     *             the exception
     */
    @Before
    public final void setUp() throws Exception {
    	        
        adminUser = new AdminUser();
    	adminUser.setFirstName("Admin");
    	adminUser.setFamilyName("User");
    	adminUser.setEmailAddress("adminuser@mailinator.com");
    	adminUser.setPassword(new Password("Passw0rd", false));
    	adminUser.setUsername("admin");
    	adminUser.setCreatedDate(new DateTime());
        UUID uuid = UUID.randomUUID();
        adminUser.setId(uuid.toString());
        adminUserDao.save(adminUser);
        adminUserDao.flush();
        adminUserDao.clear();
        
        this.testCustomer = new Customer();
        this.testCustomer.setFirstName("test");
        this.testCustomer.setFamilyName("customer");
        this.testCustomer.setEmailAddress("test.customer@mailinator.com");
        this.testCustomer.setCustomerType(CustomerType.SELF_SERVICE);
        this.testCustomer.setEmailVerificationState(EmailVerificationState.UNKNOWN);
        this.testCustomer.setUsername("testcustomer");
        this.testCustomer.setCreatedDate(new DateTime());
        UUID uuid1 = UUID.randomUUID();
        this.testCustomer.setId(uuid1.toString());
        customerDao.save(testCustomer);
        customerDao.flush();
        customerDao.clear();
                
        //standardLicenceTemplate = getSampleDataCreator().createStandardLicenceTemplate();
        
        
        getSampleDataCreator().createRegisterableProduct(erightsId1, "adminProd1", RegisterableType.SELF_REGISTERABLE);
        getSampleDataCreator().createRegisterableProduct(erightsId2, "adminProd2", RegisterableType.SELF_REGISTERABLE);
        getSampleDataCreator().createRegisterableProduct(erightsId3, "adminProd3", RegisterableType.ADMIN_REGISTERABLE);
        getSampleDataCreator().createRegisterableProduct(erightsId4, "adminProd4", RegisterableType.ADMIN_REGISTERABLE);
        
        
        LicenceTemplate licenceTemplate = getSampleDataCreator().createStandardLicenceTemplate();
        SelfRegistrationActivation registrationActivation = getSampleDataCreator().createSelfRegistrationActivation();
        ProductPageDefinition productPageDefinition = getSampleDataCreator().createProductPageDefinition();
        testProduct1 = getSampleDataCreator().createRegisterableProduct(testErightsId, "testProd1", RegisterableType.SELF_REGISTERABLE);
        @SuppressWarnings("unused")
        ProductRegistrationDefinition testPrd1 = getSampleDataCreator().createProductRegistrationDefinition(testProduct1, licenceTemplate, registrationActivation, productPageDefinition);
        
        testProduct2 = getSampleDataCreator().createRegisterableProduct(testErightsId, "testProd2", RegisterableType.SELF_REGISTERABLE);
        ProductRegistrationDefinition testPrd2 = getSampleDataCreator().createProductRegistrationDefinition(testProduct2, licenceTemplate, registrationActivation, productPageDefinition);
        @SuppressWarnings("unused")
        Registration<ProductRegistrationDefinition> testReg2 = getSampleDataCreator().createRegistration(testCustomer, testPrd2);
        
        //8 MALAYSIA, 2 ANZ, 3 CANADA
        product = getSampleDataCreator().createRegisterableProduct(Integer.valueOf(1), "Prodtest1", RegisterableType.SELF_REGISTERABLE);
        product1 = getSampleDataCreator().createRegisterableProduct(Integer.valueOf(2), "Prodtest2", RegisterableType.SELF_REGISTERABLE);
        product2 = getSampleDataCreator().createLinkedProduct(product1,Integer.valueOf(3));
        getSampleDataCreator().createRegisterableProduct(Integer.valueOf(4), "Prodtest4", RegisterableType.SELF_REGISTERABLE);
        product4 = getSampleDataCreator().createRegisterableProduct(Integer.valueOf(5), "Prodtest5", RegisterableType.SELF_REGISTERABLE);
        getSampleDataCreator().createRegisterableProduct(Integer.valueOf(6), "Prodtest6", RegisterableType.SELF_REGISTERABLE);
        getSampleDataCreator().createRegisterableProduct(Integer.valueOf(7), "Prodtest7", RegisterableType.SELF_REGISTERABLE);
        //SampleDataFactory.createRegisterableProduct(Integer.valueOf(8), RegisterableType.SELF_REGISTERABLE);
        getSampleDataCreator().createRegisterableProduct(Integer.valueOf(8), "Prodtest8", RegisterableType.SELF_REGISTERABLE);
        getSampleDataCreator().createRegisterableProduct(Integer.valueOf(9), "Prodtest9", RegisterableType.SELF_REGISTERABLE);
        getSampleDataCreator().createRegisterableProduct(Integer.valueOf(10), "Prodtest10", RegisterableType.SELF_REGISTERABLE);
        product10 = getSampleDataCreator().createRegisterableProduct(Integer.valueOf(11), "Prodtest11", RegisterableType.SELF_REGISTERABLE);
        getSampleDataCreator().createRegisterableProduct(Integer.valueOf(12), "Prodtest12", RegisterableType.SELF_REGISTERABLE);
        getSampleDataCreator().createRegisterableProduct(Integer.valueOf(13), "Prodtest13", RegisterableType.SELF_REGISTERABLE);
        
       // InstantRegistrationActivation licenceActivation = getSampleDataCreator().createInstantRegistrationActivation();
        getSampleDataCreator().createProductRegistrationDefinition(product, standardLicenceTemplate, null, productPageDefinition);
        
        /*sys1 = getSampleDataCreator().createExternalSystem("SYS1", "SYS1");
        sys2 = getSampleDataCreator().createExternalSystem("SYS2", "SYS2");
        
        type1A = getSampleDataCreator().createExternalSystemIdType(sys1,"A","A");
        type1B = getSampleDataCreator().createExternalSystemIdType(sys1,"B","B");
        type2C = getSampleDataCreator().createExternalSystemIdType(sys2,"C","C");
        type2D = getSampleDataCreator().createExternalSystemIdType(sys2,"D","D");
        
        extid1 = getSampleDataCreator().createExternalProductId(product1,"C1-1A-1", type1A);
        extid2 = getSampleDataCreator().createExternalProductId(product1,"C1-1A-2", type1A);
        extid3 = getSampleDataCreator().createExternalProductId(product1,"C1-1B-1", type1B);
        extid4 = getSampleDataCreator().createExternalProductId(product1,"C1-1B-2", type1B);
        
        extid5 = getSampleDataCreator().createExternalProductId(product2,"C2-2C-1", type2C);
        extid6 = getSampleDataCreator().createExternalProductId(product2,"C2-2C-2", type2C);
        extid7 = getSampleDataCreator().createExternalProductId(product2,"C2-2D-1", type2D);
        extid8 = getSampleDataCreator().createExternalProductId(product2,"C2-2D-2", type2D);*/
        

        loadAllDataSets();
    }
    
    @Test
    public final void testInsertRegisterableProduct() throws Exception {
        RegisterableProduct registerableProduct = SampleDataFactory.createRegisterableProduct(Integer.valueOf(12397), "Prodtest12397", RegisterableType.SELF_REGISTERABLE);
        productDao.saveProduct(registerableProduct);
        
        Assert.assertEquals(1, countRowsInTable(Product.class.getSimpleName().toLowerCase()));
    }
    
    @Test
    public final void testInsertLinkedProduct() throws Exception {
        LinkedProduct linkedProduct = SampleDataFactory.createLinkedProduct(product,  Integer.valueOf(12397));
        productDao.saveProduct(linkedProduct);
        
        Assert.assertEquals(1, countRowsInTable(Product.class.getSimpleName().toLowerCase()));
    }
    
    /**
     * Get a product definition by erights id. Check the correct Product was returned.
     * 
     * @throws Exception
     *             the exception
     */
   /* @Test
    public final void testGetRegiterableProductByErightsId() throws Exception {
        List<RegisterableProduct> results = productDao.getRegisterableProductsByErightsId(product.getErightsId(), ACTIVE_STATE);
        assertThat(results.size(), equalTo(1));
        assertEquals(product.getId(), results.get(0).getId());
        
        //product2 is not a registerable product
        List<RegisterableProduct> results2 = productDao.getRegisterableProductsByErightsId(product2.getErightsId(), ACTIVE_STATE);
        assertThat(results2.size(), equalTo(0));
        
    }*/
    
   /* @Test
    public void testLookupProductByExternalProductId(){
        checkExternalProductId(product1, extid1);
        checkExternalProductId(product1, extid2);
        checkExternalProductId(product1, extid3);
        checkExternalProductId(product1, extid4);
        
        checkExternalProductId(product2, extid5);
        checkExternalProductId(product2, extid6);
        checkExternalProductId(product2, extid7);
        checkExternalProductId(product2, extid8);
    }

    private void checkExternalProductId(Product expected, ExternalProductId extid) {
        String systemId = extid.getExternalSystemIdType().getExternalSystem().getName();
        String typeId = extid.getExternalSystemIdType().getName();
        String externalId = extid.getExternalId();
        Product result = this.productDao.getProductByExternalProductId(systemId, typeId, externalId);
        Assert.assertEquals(expected.getId(),result.getId());
    }
    
    @Test
    public void testGetProductByErightsId(){
        Product res1 = this.productDao.getProductByErightsId(this.product.getId()).get(0);
        Assert.assertTrue(res1 instanceof RegisterableProduct);
        Assert.assertEquals(res1.getId(), product.getId());
        
        Product res2 = this.productDao.getProductByErightsId(this.product1.getId()).get(0);
        Assert.assertTrue(res2 instanceof RegisterableProduct);
        Assert.assertEquals(res2.getId(), product1.getId());
        
        Product res3 = this.productDao.getProductByErightsId(this.product2.getId()).get(0);
        Assert.assertTrue(res3 instanceof LinkedProduct);
        Assert.assertEquals(res3.getId(), product2.getId());
        
        List<Product> res4 =  this.productDao.getProductByErightsId(null);
        Assert.assertTrue(res4.isEmpty());
        
        List<Product> res5 =  this.productDao.getProductByErightsId("12345");
        Assert.assertTrue(res5.isEmpty());
    }*/
    
   /* @Test
    public void testGetProductById() {
    	Product res1 = this.productDao.getProductById(this.product.getId());
        Assert.assertTrue(res1 instanceof RegisterableProduct);
        Assert.assertEquals(res1.getId(), product.getId());
    }
    */
    @Test
    public void testGetProductByNonExistentId() {
    	Product product = this.productDao.getProductById(UUID.randomUUID().toString());
    	assertNull(product);
    }
    
    /*@Test
    public void testGetProductsByErightsIds() {
    	List<Product> products = productDao.getProductsByErightsIds(Arrays.asList(new Integer[] { product.getErightsId(), product2.getErightsId() }));
    	
    	for (Product product : products) {
    		if (product instanceof LinkedProduct) {
    			Assert.assertEquals(product2.getId(), product.getId());
    		} else if (product instanceof RegisterableProduct) {
    			Assert.assertEquals(product.getId(), product.getId());
    		} else {
    			assertTrue("Check match all products", false);
    		}
    	}    
    }*/
    
  /*  @Test
    public void testGetProductAndExternalIdsByIdWithExternals(){
        String productId = this.product1.getId();
        Product res1 = this.productDao.getProductAndExternalIdsById(productId);
        Assert.assertNotNull("DAO returned null!",res1);
        sessionFactory.getCurrentSession().clear();//prevent any more lazy loading
        Assert.assertTrue(res1 instanceof RegisterableProduct);
        Assert.assertEquals(res1.getId(), product1.getId());
        Set<ExternalProductId> externals = res1.getExternalIds();
        Set<String> expected = new HashSet<String>(Arrays.asList(this.extid1.getId(),this.extid2.getId(),this.extid3.getId(), this.extid4.getId()));
        Set<String> actual = new HashSet<String>();
        for(ExternalProductId external : externals){
            actual.add(external.getId());
        }
        Assert.assertEquals(expected,actual);
    }*/
    
    /*@Test
    public void testGetProductAndExternalIdsByIdWithoutExternals(){
        String productId = this.product.getId();
        Product res1 = this.productDao.getProductAndExternalIdsById(productId);
        Assert.assertNotNull("DAO returned null!",res1);
        sessionFactory.getCurrentSession().clear();//prevent any more lazy loading
        Assert.assertTrue(res1 instanceof RegisterableProduct);
        Assert.assertEquals(res1.getId(), product.getId());
        Set<ExternalProductId> externals = res1.getExternalIds();        
        Assert.assertTrue(externals.isEmpty());
    }
    
    @Test
    public void testGetProductAndExternalIdsByErightsIdWithExternals(){
        Product res1 = this.productDao.getProductAndExternalIdsByErightsId(product1.getId()).get(0);
        Assert.assertNotNull("DAO returned null!",res1);
        sessionFactory.getCurrentSession().clear();//prevent any more lazy loading
        Assert.assertTrue(res1 instanceof RegisterableProduct);
        Assert.assertEquals(res1.getId(), product1.getId());
        Set<ExternalProductId> externals = res1.getExternalIds();
        Set<String> expected = new HashSet<String>(Arrays.asList(this.extid1.getId(),this.extid2.getId(),this.extid3.getId(), this.extid4.getId()));
        Set<String> actual = new HashSet<String>();
        for(ExternalProductId external : externals){
            actual.add(external.getId());
        }
        Assert.assertEquals(expected,actual);
    }
    
    @Test
    public void testGetProductAndExternalIdsByErightsIdWithoutExternals(){
        Product res1 = this.productDao.getProductAndExternalIdsByErightsId(product.getId()).get(0);
        Assert.assertNotNull("DAO returned null!",res1);
        sessionFactory.getCurrentSession().clear();//prevent any more lazy loading
        Assert.assertTrue(res1 instanceof RegisterableProduct);
        Assert.assertEquals(res1.getId(), product.getId());
        Set<ExternalProductId> externals = res1.getExternalIds();        
        Assert.assertTrue(externals.isEmpty());
    }*/
    
    /*
    @Test
    public void testProductByDivision() {
    	//Will match first 5 malaysia products
    	List<Product> products = this.productDao.getProductByDivision("MALAYSIA", null, null, 5, 1);
    	
    	assertEquals("Check correct number of products", 5, products.size());
    	
    	//Will match last 3 (second page)
    	products = this.productDao.getProductByDivision("MALAYSIA", null, null, 5, 2);
    	
    	assertEquals("Check correct number of products", 5, products.size());
    	
    	//Will match division and name (all records have "Name"
    	products = this.productDao.getProductByDivision("MALAYSIA", "Name", null, 15, 1);
    	
    	assertEquals("Check correct number of products", 10, products.size());
    	
    	//Will match division and name
    	products = this.productDao.getProductByDivision("MALAYSIA", "productName", null, 15, 1);
    	
    	assertEquals("Check correct number of products", 9, products.size());
    	
    	//Will match division and name
		products = this.productDao.getProductByDivision("MALAYSIA", "differentName", null, 15, 1);
		
    	assertEquals("Check correct number of products", 1, products.size());
    	
    	//Will match division, name and erights id
		products = this.productDao.getProductByDivision("MALAYSIA", "differentName", 8, 15, 1);
    	
    	assertEquals("Check correct number of products", 1, products.size());
    	
    	//Will not match division, name and erights id as erights id is incorrect
    	products = this.productDao.getProductByDivision("MALAYSIA", "productName", 8, 15, 1);
    	
    	assertEquals("Check correct number of products", 0, products.size());
    	
    	//Will match only anz
    	products = this.productDao.getProductByDivision("ANZ", null, null, 5, 1);
    	
    	assertEquals("Check correct number of products", 2, products.size());
    	
    	//Will match only canada
    	products = this.productDao.getProductByDivision("CANADA", null, null, 5, 1);
    	
    	assertEquals("Check correct number of products", 3, products.size());
    	
    	//Will match all
    	products = this.productDao.getProductByDivision(null, null, null, 15, 1);
    	
    	assertEquals("Check correct number of products", 15, products.size());
    	
    	//Will match all containing "Name"
    	products = this.productDao.getProductByDivision(null, "Name", null, 15, 1);
    	
    	assertEquals("Check correct number of products", 15, products.size());
    	
    	//Will match 1 containing "Name"
    	products = this.productDao.getProductByDivision(null, "Name", 6, 15, 1);
    	
    	assertEquals("Check correct number of products", 1, products.size());
    }*/

   /* @Test
    public void testGetProductsByErightsIdsAndAdminUser() {

    	List<Product> products = this.productDao.getProductsByErightsIds(Arrays.asList(new Integer[] { product.getErightsId(), product4.getErightsId()}));
    	
    	assertEquals("Check correct number of products", 2, products.size());
    	
    	products = this.productDao.getProductsByErightsIds(Arrays.asList(new Integer[] { product.getErightsId(), product4.getErightsId(), product10.getErightsId()}));
    	
    	assertEquals("Check correct number of products", 3, products.size());
    }
    */
   /* @Test
    public void testDeleteUnusedProductWithoutExternalIds() {
        boolean result = this.productDao.deleteUnusedProduct(this.testProduct1.getId());
        Assert.assertTrue(result);
    }*/
    
    @Test
    public void testDeleteUnusedProductWithExternalIds() {
        boolean result = this.productDao.deleteUnusedProduct(this.testProduct2.getId());
        Assert.assertFalse(result);
    }
}
