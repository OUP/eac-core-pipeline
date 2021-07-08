package com.oup.eac.service.impl;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.oup.eac.data.AbstractDBTest;
import com.oup.eac.domain.AdminUser;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.RegisterableProduct;
import com.oup.eac.domain.RegisterableProduct.RegisterableType;
import com.oup.eac.domain.StandardLicenceTemplate;
import com.oup.eac.domain.util.SampleDataFactory;
import com.oup.eac.dto.CustomerWithLicencesAndProductsDto;
import com.oup.eac.dto.LicenceDto;
import com.oup.eac.integration.facade.ErightsFacade;
import com.oup.eac.service.CustomerService;

@Ignore
public class CustomerServiceImplForAdminTest extends AbstractDBTest {

	@Autowired
	private CustomerService customerService;
	
	@Autowired
	@Qualifier("fakeErightsFacade")
	private ErightsFacade fakeErightsFacade;
	
	private AdminUser adminUser;
	
	private AdminUser adminUser2;
	
	private Customer customer;
	
	private RegisterableProduct product1;
	private RegisterableProduct product2;
	private RegisterableProduct product3;
	private RegisterableProduct product4;
	
	private String licence1;
	private String licence2;
	private String licence3;
	private String licence4;
	private String licence5;
	private String licence6;
	
	/**
     * @throws Exception
     *             Sets up data ready for test.
     */
    @Before
    public final void setUp() throws Exception {
    	    	
    	
        //Malaysia products
    	product1 = getSampleDataCreator().createRegisterableProduct(12345,"malaysia 1", RegisterableType.SELF_REGISTERABLE);
    	product2 = getSampleDataCreator().createRegisterableProduct(67890,"malaysia 2", RegisterableType.SELF_REGISTERABLE);
    	
    	//Canada product
    	product3 = getSampleDataCreator().createRegisterableProduct(98765,"malaysia 3", RegisterableType.SELF_REGISTERABLE);
    	//Anz product
    	product4 = getSampleDataCreator().createRegisterableProduct(76543,"malaysia 4", RegisterableType.SELF_REGISTERABLE);
    	
    	//AdminUser1 has access to product1, product2, product3
    	//AdminUser2 has access to product1, product2, product4
    	
    	StandardLicenceTemplate licenceTemplate = SampleDataFactory.createStandardLicenceTemplate(); getSampleDataCreator().createStandardLicenceTemplate();
        getSampleDataCreator().loadStandardLicenceTemplate(licenceTemplate);
        
        customer = SampleDataFactory.createCustomer();
    	customer.setId("1");
    	getSampleDataCreator().loadCustomer(customer);
    	
        //Add some licences to the user for products defined
        //Access to adminUser1 and adminUser2
    	licence1 = fakeErightsFacade.addLicense(customer.getId(), licenceTemplate, Arrays.asList(new String[]{String.valueOf(product1.getErightsId())}), true);
    	licence2 = fakeErightsFacade.addLicense(customer.getId(), licenceTemplate, Arrays.asList(new String[]{String.valueOf(product2.getErightsId())}), true);
    	
    	//Access to adminuser1 only
    	licence3 = fakeErightsFacade.addLicense(customer.getId(), licenceTemplate, Arrays.asList(new String[]{String.valueOf(product3.getErightsId())}), true);

    	//Access to adminuser2 only
    	licence4 = fakeErightsFacade.addLicense(customer.getId(), licenceTemplate, Arrays.asList(new String[]{String.valueOf(product4.getErightsId())}), true);
    	
    	//Access by adminUser1 and 2
    	licence5 = fakeErightsFacade.addLicense(customer.getId(), licenceTemplate, Arrays.asList(new String[]{String.valueOf(product1.getErightsId()), String.valueOf(product3.getErightsId())}), true);
    	licence6 = fakeErightsFacade.addLicense(customer.getId(), licenceTemplate, Arrays.asList(new String[]{String.valueOf(product1.getErightsId()), String.valueOf(product4.getErightsId())}), true);
    	
    	//AdminUser has access to 5 licences
    	//AdminUser2 has access to 5 licences
        
        loadAllDataSets();
    }
    
    @Test
    public void getCustomerWithLicencesAndProductsByAdminUser() throws Exception {
    	CustomerWithLicencesAndProductsDto adminUser1Dto = customerService.getCustomerWithLicencesAndProductsByAdminUser(customer.getId(), adminUser);
    	CustomerWithLicencesAndProductsDto adminUser2Dto = customerService.getCustomerWithLicencesAndProductsByAdminUser(customer.getId(), adminUser2);
    	
    	assertEquals("Check correct number of licences for admin1", 6, adminUser1Dto.getLicences().size());
    	assertEquals("Check correct number of licences for admin2", 6, adminUser2Dto.getLicences().size());
    	
    	//Convert licences to ids
    	List<String> adminUser1DtoLicenceIds = new ArrayList<String>();
    	for (LicenceDto licenceDto : adminUser1Dto.getLicences()) {
    		adminUser1DtoLicenceIds.add(licenceDto.getLicenseId());
    	}
    	
    	List<String> adminUser2DtoLicenceIds = new ArrayList<String>();
    	for (LicenceDto licenceDto : adminUser2Dto.getLicences()) {
    		adminUser2DtoLicenceIds.add(licenceDto.getLicenseId());
    	}
    	
    	/*assertTrue("Check correct licences were hidden", !adminUser1DtoLicenceIds.contains(licence4));
    	assertTrue("Check correct licences were hidden", !adminUser2DtoLicenceIds.contains(licence3));*/
    }
}
