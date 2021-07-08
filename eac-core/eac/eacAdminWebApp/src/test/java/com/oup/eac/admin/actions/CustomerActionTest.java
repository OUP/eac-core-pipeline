package com.oup.eac.admin.actions;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.easymock.EasyMock;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.binding.message.Message;
import org.springframework.util.CollectionUtils;
import org.springframework.webflow.core.collection.MutableAttributeMap;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.test.MockRequestContext;

import com.oup.eac.admin.beans.CustomerBean;
import com.oup.eac.admin.validators.CustomerBeanValidator;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.Customer.CustomerType;
import com.oup.eac.domain.ExternalCustomerId;
import com.oup.eac.domain.ProductRegistration;
import com.oup.eac.domain.ProductRegistrationDefinition;
import com.oup.eac.domain.RegisterableProduct;
import com.oup.eac.domain.Registration;
import com.oup.eac.domain.entitlement.ProductDetailsDto;
import com.oup.eac.domain.entitlement.ProductEntitlementDto;
import com.oup.eac.domain.entitlement.ProductEntitlementGroupDto;
import com.oup.eac.dto.CustomerDto;
import com.oup.eac.dto.CustomerRegistrationsDto;
import com.oup.eac.dto.ExternalIdDto;
import com.oup.eac.dto.LicenceDto;
import com.oup.eac.service.CustomerService;
import com.oup.eac.service.ExternalIdService;
import com.oup.eac.service.RegistrationService;
import com.oup.eac.service.UserEntitlementsService;
import com.oup.eac.service.exceptions.ExternalIdTakenException;
import com.oup.eac.service.exceptions.UsernameExistsException;

public class CustomerActionTest {

	private CustomerService mockCustomerService;
	private RegistrationService mockRegistrationService;
	private UserEntitlementsService mockUserEntitlementsService;
	private CustomerBeanValidator mockCustomerBeanValidator;
	private ExternalIdService mockExternalIdService;
	private CustomerAction customerAction;

	@Before
	public void setUp() {
		mockCustomerService = EasyMock.createMock(CustomerService.class);
		mockRegistrationService = EasyMock.createMock(RegistrationService.class);
		mockUserEntitlementsService = EasyMock.createMock(UserEntitlementsService.class);
		mockCustomerBeanValidator = EasyMock.createMock(CustomerBeanValidator.class);
		mockExternalIdService = EasyMock.createMock(ExternalIdService.class);
		customerAction = new CustomerAction(mockCustomerService, mockRegistrationService, mockUserEntitlementsService, mockCustomerBeanValidator,mockExternalIdService);		
	}
	
	private Object[] getMocks(){
		return new Object[]{mockCustomerService, mockRegistrationService, mockUserEntitlementsService, mockCustomerBeanValidator,mockExternalIdService};
	}

	@Test
	public void shouldCreateEmptyCustomerBeanWhenCustomerIdIsNull() throws Exception {
		final String id = null;
		CustomerBean customerBean = customerAction.createCustomerBean(id);

		assertThat(customerBean, notNullValue());
		assertThat(customerBean.isChangePassword(), equalTo(true));
	}

	//below test cases is not required because we won't sort registration's
	@Ignore
	@Test
	public void shouldCreateCustomerBeanWhenCustomerIdSpecifiedWithSortedRegistrations() throws Exception {
		String id = "12345";
		Customer customer = new Customer();
		expect(mockCustomerService.getCustomerById(id)).andReturn(customer);
		CustomerRegistrationsDto registrationsDto = new CustomerRegistrationsDto(customer, null, null);
		expect(mockRegistrationService.getEntitlementsForCustomerRegistrations(customer, null, false)).andReturn(registrationsDto);

		ProductEntitlementGroupDto group1 = createProductEntitlementGroup(new DateTime(2012, 6, 1, 0, 0, 0, 0), "xyz");
		ProductEntitlementGroupDto group2 = createProductEntitlementGroup(new DateTime(2012, 6, 2, 0, 0, 0, 0), "xyz");
		ProductEntitlementGroupDto group3 = createProductEntitlementGroup(new DateTime(2012, 6, 2, 0, 0, 0, 0), "middle");
		ProductEntitlementGroupDto group4 = createProductEntitlementGroup(new DateTime(2012, 6, 3, 0, 0, 0, 0), "abc");
		ProductEntitlementGroupDto group5 = createProductEntitlementGroup(new DateTime(2012, 5, 3, 0, 0, 0, 0), "abc");

		expect(mockUserEntitlementsService.getUserEntitlementGroups(registrationsDto, null)).andReturn(asMutableList(group1, group2, group3, group4, group5));

		replay(mockCustomerService, mockRegistrationService, mockUserEntitlementsService);

		CustomerBean customerBean = customerAction.createCustomerBean(id);
		List<ProductEntitlementGroupDto> groups = customerBean.getProductEntitlementGroups();
		
		assertThat(CollectionUtils.isEmpty(groups), equalTo(false));
		assertThat(groups.size(), equalTo(5));
		assertThat(productName(groups.get(0)), equalTo("abc"));
		assertThat(createdDate(groups.get(0)), equalTo(new DateTime(2012, 5, 3, 0, 0, 0, 0)));
		assertThat(productName(groups.get(1)), equalTo("abc"));
		assertThat(createdDate(groups.get(1)), equalTo(new DateTime(2012, 6, 3, 0, 0, 0, 0)));
		assertThat(productName(groups.get(2)), equalTo("middle"));
		assertThat(productName(groups.get(3)), equalTo("xyz"));
		assertThat(createdDate(groups.get(3)), equalTo(new DateTime(2012, 6, 1, 0, 0, 0, 0)));
	}

	//below test cases is not required because we won't sort registration's
	@Ignore
    @Test
    //we do not filter out  entitlements without registrations now
    public void shouldCreateCustomerBeanWhenCustomerIdSpecifiedWithSortedRegistrations2() throws Exception {
        String id = "12345";
        Customer customer = new Customer();
        expect(mockCustomerService.getCustomerById(id)).andReturn(customer);
        CustomerRegistrationsDto registrationsDto = new CustomerRegistrationsDto(customer, null, null);
        expect(mockRegistrationService.getEntitlementsForCustomerRegistrations(customer, null, true)).andReturn(registrationsDto);

        ProductEntitlementGroupDto group1 = createProductEntitlementGroup(new DateTime(2012, 6, 1, 0, 0, 0, 0), "xyz");
        
        //groups without registrations should be filtered out
        ProductEntitlementGroupDto group2 = createProductEntitlementGroup(new DateTime(2011, 5, 31, 0, 0, 0, 0), "abc");
        group2.getEntitlement().setRegistration(null);
        
        expect(mockUserEntitlementsService.getUserEntitlementGroups(registrationsDto, null)).andReturn(asMutableList(group1, group2));

        replay(mockCustomerService, mockRegistrationService, mockUserEntitlementsService);

        CustomerBean customerBean = customerAction.createCustomerBean(id);
        List<ProductEntitlementGroupDto> groups = customerBean.getProductEntitlementGroups();
        
        assertThat(CollectionUtils.isEmpty(groups), equalTo(false));
        assertThat(groups.size(), equalTo(2));
        assertThat(productName(groups.get(0)), equalTo("abc"));
        Assert.assertNull(groups.get(0).getEntitlement().getRegistration());
        
        assertThat(productName(groups.get(1)), equalTo("xyz"));
        assertThat(createdDate(groups.get(1)), equalTo(new DateTime(2012, 6, 1, 0, 0, 0, 0)));

    }

    private ProductEntitlementGroupDto createProductEntitlementGroup(DateTime registrationCreatedDate, String productName) {
	
		RegisterableProduct product = new RegisterableProduct();
		product.setProductName(productName);
		ProductDetailsDto productDetailsDto = new ProductDetailsDto();
		productDetailsDto.setProduct(product);
		ProductEntitlementDto productEntitlementDto = new ProductEntitlementDto();
		productEntitlementDto.setProductList(Arrays.asList(productDetailsDto));
		LicenceDto licenceDto = new LicenceDto();
		double rand1 = (int)(Math.random() * 1000);
		double rand2 = (int)(Math.random() * 1000);
		licenceDto.setLicenseId(Double.toString(rand1));
		licenceDto.setProductIds(Arrays.asList(Double.toString(rand2)));
        productEntitlementDto.setLicence(licenceDto);
		Registration<? extends ProductRegistrationDefinition> registration = new ProductRegistration();
		registration.setCreatedDate(registrationCreatedDate);
		productEntitlementDto.setRegistration(registration);
		ProductEntitlementGroupDto productEntitlementGroupDto = new ProductEntitlementGroupDto();
		productEntitlementGroupDto.setEntitlement(productEntitlementDto);
		return productEntitlementGroupDto;
	}

	private <T> List<T> asMutableList(T... objects) {
		// Because Arrays.asList() returns an immutable list.
		List<T> mutableList = new ArrayList<T>();
		for (T object : objects) {
			mutableList.add(object);
		}
		return mutableList;
	}

	private String productName(ProductEntitlementGroupDto productEntitlementGroupDto) {
		return productEntitlementGroupDto.getProduct(0).getProductName();
	}

	private DateTime createdDate(ProductEntitlementGroupDto productEntitlementGroupDto) {
	    Registration<? extends ProductRegistrationDefinition> reg = productEntitlementGroupDto.getEntitlement().getRegistration();
	    if(reg == null){
	        return null;
	    }else{
	        return reg.getCreatedDate();
	    }
	}
	
	@Test
	public void testSaveCustomerOkay() throws Exception {
		MockRequestContext context = new MockRequestContext();
		MutableAttributeMap map = context.getFlowScope();
		Customer customer = new Customer();
		CustomerBean customerBean = new CustomerBean(customer, new ArrayList<ProductEntitlementGroupDto>());
		map.put("customerBean", customerBean);
		CustomerDto cDTO = new CustomerDto(customer) ;
        expect(this.mockCustomerService.saveCustomer(customer, false)).andReturn(cDTO); 
		
		
		EasyMock.replay(getMocks());
		Event result = this.customerAction.saveCustomer(context);
		EasyMock.verify(getMocks());
		Assert.assertEquals("success",result.getId());
	}
	
	@Test
	public void testSaveCustomerFail() throws Exception {
		MockRequestContext context = new MockRequestContext();
		MutableAttributeMap map = context.getFlowScope();
		Customer customer = new Customer();
		CustomerBean customerBean = new CustomerBean(customer, new ArrayList<ProductEntitlementGroupDto>());
		map.put("customerBean", customerBean);
		this.mockCustomerService.saveCustomer(customer, false);
		EasyMock.expectLastCall().andThrow(new UsernameExistsException());
		
		EasyMock.replay(getMocks());
		Event result = this.customerAction.saveCustomer(context);
		EasyMock.verify(getMocks());
		Assert.assertEquals("error",result.getId());
	}
	
	@Test
	public void testUpdateCustomerOkay() throws Exception {
		MockRequestContext context = new MockRequestContext();
		MutableAttributeMap map = context.getFlowScope();
		Customer customer = new Customer();
		List<LicenceDto> licences = new ArrayList<LicenceDto>();
		CustomerBean customerBean = new CustomerBean(customer, new ArrayList<ProductEntitlementGroupDto>());
		List<ExternalCustomerId> newExtIds = new ArrayList<ExternalCustomerId>();
		customerBean.setExternalIds(newExtIds);
		customerBean.setChangePassword(true);
		customerBean.setGeneratePassword(true);
		
		map.put("customerBean", customerBean);
		this.mockExternalIdService.checkExistingExternalCustomerIds(customer,newExtIds);
		EasyMock.expectLastCall();		
		
		this.mockCustomerService.updateCustomerAndLicences(customer,true, licences);
		EasyMock.expectLastCall();
		
		EasyMock.replay(getMocks());
		//Event res = this.customerAction.updateCustomer(context);
		//EasyMock.verify(getMocks());
		//Assert.assertEquals("success",res.getId());
	}
	
	@Test
	public void testUpdateCustomerFail() throws Exception {
		MockRequestContext context = new MockRequestContext();
		MutableAttributeMap map = context.getFlowScope();
		Customer customer = new Customer();
		CustomerBean customerBean = new CustomerBean(customer, new ArrayList<ProductEntitlementGroupDto>());
		List<ExternalCustomerId> newExtIds = new ArrayList<ExternalCustomerId>();
		customerBean.setExternalIds(newExtIds);
		customerBean.setChangePassword(true);
		customerBean.setGeneratePassword(true);
		
		map.put("customerBean", customerBean);
		this.mockExternalIdService.checkExistingExternalCustomerIds(customer,newExtIds);
		
		ExternalIdDto dto = new ExternalIdDto("SYS1","TYPE1","ID1");
		Throwable ex = new ExternalIdTakenException("",dto);
		EasyMock.expectLastCall().andThrow(ex);
		
		EasyMock.replay(getMocks());
		Event res = this.customerAction.updateExternalIds(context);
		EasyMock.verify(getMocks());
		Assert.assertEquals("error",res.getId());
		Message message = context.getMessageContext().getAllMessages()[0];
		Assert.assertEquals("The ExternalCustomerId : System Id[SYS1] Id Type[TYPE1] Id[ID1] has been associated with another customer",message.getText());
		
	}
	
	@Test
    public void testSaveCustomerWithPositiveConcurrencySuccess() throws Exception {
        MockRequestContext context = new MockRequestContext();
        MutableAttributeMap map = context.getFlowScope();
        Customer customer = new Customer();
        CustomerType ct = CustomerType.SPECIFIC_CONCURRENCY;
        customer.setCustomerType(ct);        
        CustomerBean customerBean = new CustomerBean(customer, new ArrayList<ProductEntitlementGroupDto>());
        customerBean.setConcurrencyValue("10");
        map.put("customerBean", customerBean);
        CustomerDto cDTO = new CustomerDto(customer) ;
        expect(this.mockCustomerService.saveCustomer(customer, false)).andReturn(cDTO); 
        
        EasyMock.replay(getMocks());
        Event result = this.customerAction.saveCustomer(context);
        EasyMock.verify(getMocks());
        Assert.assertEquals("success",result.getId());
    }
	
	
	@Test
    public void testSaveCustomerWithBlankConcurrencySuccess() throws Exception {
        MockRequestContext context = new MockRequestContext();
        MutableAttributeMap map = context.getFlowScope();
        Customer customer = new Customer();
        CustomerType ct = CustomerType.SPECIFIC_CONCURRENCY;
        customer.setCustomerType(ct);        
        CustomerBean customerBean = new CustomerBean(customer, new ArrayList<ProductEntitlementGroupDto>());        
        map.put("customerBean", customerBean);
        CustomerDto cDTO = new CustomerDto(customer) ;
        expect(this.mockCustomerService.saveCustomer(customer, false)).andReturn(cDTO);    
        EasyMock.replay(getMocks());
        Event result = this.customerAction.saveCustomer(context);
        EasyMock.verify(getMocks());
        Assert.assertEquals("success",result.getId());
    }
	
	@Test
    public void testSaveCustomerWithZeroConcurrencySuccess() throws Exception {
        MockRequestContext context = new MockRequestContext();
        MutableAttributeMap map = context.getFlowScope();
        Customer customer = new Customer();
        CustomerType ct = CustomerType.SPECIFIC_CONCURRENCY;
        customer.setCustomerType(ct);        
        CustomerBean customerBean = new CustomerBean(customer, new ArrayList<ProductEntitlementGroupDto>());
        customerBean.setConcurrencyValue("0");
        map.put("customerBean", customerBean);
        CustomerDto cDTO = new CustomerDto(customer) ;
        expect(this.mockCustomerService.saveCustomer(customer, false)).andReturn(cDTO); 
        
        EasyMock.replay(getMocks());
        Event result = this.customerAction.saveCustomer(context);
        EasyMock.verify(getMocks());
        Assert.assertEquals("success",result.getId());
    }
	
	@Test
    public void testSaveCustomerWithNegativeConcurrencySuccess() throws Exception {
	    	    
	    MockRequestContext context = new MockRequestContext();
        MutableAttributeMap map = context.getFlowScope();
        Customer customer = new Customer();
        CustomerType ct = CustomerType.SPECIFIC_CONCURRENCY;        
        customer.setCustomerType(ct);        
        CustomerBean customerBean = new CustomerBean(customer, new ArrayList<ProductEntitlementGroupDto>());
        customerBean.setConcurrencyValue("-1");
        map.put("customerBean", customerBean);
        
        Event result = this.customerAction.saveCustomer(context);

        Assert.assertEquals("error",result.getId());
        Message message = context.getMessageContext().getAllMessages()[0];
        Assert.assertEquals("Specific concurrency can not be negative.", message.getText());
      
    }

    @Test
    public void testSaveCustomerFaiLByNumberFormat() throws Exception {
        
        MockRequestContext context = new MockRequestContext();
        MutableAttributeMap map = context.getFlowScope();
        Customer customer = new Customer();
        CustomerType ct = CustomerType.SPECIFIC_CONCURRENCY;        
        customer.setCustomerType(ct);        
        CustomerBean customerBean = new CustomerBean(customer, new ArrayList<ProductEntitlementGroupDto>());
        customerBean.setConcurrencyValue("abcd");
        map.put("customerBean", customerBean);
        
        Event result = this.customerAction.saveCustomer(context);

        Assert.assertEquals("error",result.getId());
        Message message = context.getMessageContext().getAllMessages()[0];
        Assert.assertEquals("Concurrency value should be in numbers only.", message.getText());
       
    }
    
    @Test
    public void testSaveCustomerFaiLByNumberFormatTooLong() throws Exception {
        
        MockRequestContext context = new MockRequestContext();
        MutableAttributeMap map = context.getFlowScope();
        Customer customer = new Customer();
        CustomerType ct = CustomerType.SPECIFIC_CONCURRENCY;        
        customer.setCustomerType(ct);        
        CustomerBean customerBean = new CustomerBean(customer, new ArrayList<ProductEntitlementGroupDto>());
        customerBean.setConcurrencyValue("1234567890");
        map.put("customerBean", customerBean);
        
        Event result = this.customerAction.saveCustomer(context);

        Assert.assertEquals("error",result.getId());
        Message message = context.getMessageContext().getAllMessages()[0];
        Assert.assertEquals("Specific concurrency value is too long.", message.getText());
       
    }
    
    @Test
    public void testUpdateCustomerWithPositiveConcurrecnySuccess() throws Exception {
        MockRequestContext context = new MockRequestContext();
        MutableAttributeMap map = context.getFlowScope();
        Customer customer = new Customer();
        customer.setCustomerType(CustomerType.SPECIFIC_CONCURRENCY);
        List<LicenceDto> licences = new ArrayList<LicenceDto>();
        CustomerBean customerBean = new CustomerBean(customer, new ArrayList<ProductEntitlementGroupDto>());
        List<ExternalCustomerId> newExtIds = new ArrayList<ExternalCustomerId>();
        customerBean.setExternalIds(newExtIds);
        customerBean.setChangePassword(true);
        customerBean.setGeneratePassword(true);
        customerBean.setConcurrencyValue("10");
        
        map.put("customerBean", customerBean);
        this.mockExternalIdService.checkExistingExternalCustomerIds(customer,newExtIds);
        EasyMock.expectLastCall();      
        
        this.mockCustomerService.updateCustomerAndLicences(customer,true, licences);
        EasyMock.expectLastCall();
        
        EasyMock.replay(getMocks());
      //  Event res = this.customerAction.updateCustomer(context);
      //  EasyMock.verify(getMocks());
      //  Assert.assertEquals("success",res.getId());
    }
    
    @Test
    public void testUpdateCustomerWithZeroConcurrecnySuccess() throws Exception {
        MockRequestContext context = new MockRequestContext();
        MutableAttributeMap map = context.getFlowScope();
        Customer customer = new Customer();
        customer.setCustomerType(CustomerType.SPECIFIC_CONCURRENCY);
        List<LicenceDto> licences = new ArrayList<LicenceDto>();
        CustomerBean customerBean = new CustomerBean(customer, new ArrayList<ProductEntitlementGroupDto>());
        List<ExternalCustomerId> newExtIds = new ArrayList<ExternalCustomerId>();
        customerBean.setExternalIds(newExtIds);
        customerBean.setChangePassword(true);
        customerBean.setGeneratePassword(true);
        customerBean.setConcurrencyValue("0");
        
        map.put("customerBean", customerBean);
        this.mockExternalIdService.checkExistingExternalCustomerIds(customer,newExtIds);
        EasyMock.expectLastCall();  
        CustomerDto cDTO = new CustomerDto(customer) ;
        expect(this.mockCustomerService.saveCustomer(customer, true)).andReturn(cDTO);
        
        
        this.mockCustomerService.updateCustomerAndLicences(customer,false, licences);
        EasyMock.expectLastCall();
        
        EasyMock.replay(getMocks());
        Event res = this.customerAction.saveCustomer(context);
        Assert.assertEquals("success",res.getId());
    }
    
    @Test
    public void testUpdateCustomerWithBlankConcurrecnySuccess() throws Exception {
        MockRequestContext context = new MockRequestContext();
        MutableAttributeMap map = context.getFlowScope();
        Customer customer = new Customer();
        customer.setCustomerType(CustomerType.SPECIFIC_CONCURRENCY);
        List<LicenceDto> licences = new ArrayList<LicenceDto>();
        CustomerBean customerBean = new CustomerBean(customer, new ArrayList<ProductEntitlementGroupDto>());
        List<ExternalCustomerId> newExtIds = new ArrayList<ExternalCustomerId>();
        customerBean.setExternalIds(newExtIds);
        customerBean.setChangePassword(true);
        customerBean.setGeneratePassword(true);
        
        CustomerDto cDTO = new CustomerDto(customer) ;
        expect(this.mockCustomerService.saveCustomer(customer, true)).andReturn(cDTO);   
        
        map.put("customerBean", customerBean);
        this.mockExternalIdService.checkExistingExternalCustomerIds(customer,newExtIds);
        EasyMock.expectLastCall();      
        
        this.mockCustomerService.updateCustomerAndLicences(customer,true, licences);
        EasyMock.expectLastCall();
        
        EasyMock.replay(getMocks());
        Event res = this.customerAction.saveCustomer(context);
        Assert.assertEquals("success",res.getId());
    }
    
    @Test
    public void testUpdateCustomerWithNegativeConcurrecny() throws Exception {
        MockRequestContext context = new MockRequestContext();
        MutableAttributeMap map = context.getFlowScope();
        Customer customer = new Customer();
        customer.setCustomerType(CustomerType.SPECIFIC_CONCURRENCY);        
        CustomerBean customerBean = new CustomerBean(customer, new ArrayList<ProductEntitlementGroupDto>());
        List<ExternalCustomerId> newExtIds = new ArrayList<ExternalCustomerId>();
        customerBean.setExternalIds(newExtIds);
        customerBean.setChangePassword(true);
        customerBean.setGeneratePassword(true);
        customerBean.setConcurrencyValue("-25");
        
        map.put("customerBean", customerBean);
        CustomerDto cDTO = new CustomerDto(customer) ;
        expect(this.mockCustomerService.saveCustomer(customer, false)).andReturn(cDTO);    
        EasyMock.replay(getMocks());
		Event res = this.customerAction.saveCustomer(context);
		    
		Assert.assertEquals("error",res.getId());
        Message message = context.getMessageContext().getAllMessages()[0];
        Assert.assertEquals("Specific concurrency can not be negative.", message.getText());
    }
    
    @Test
    public void testUpdateCustomerFaiLByNumberFormat() throws Exception {
        MockRequestContext context = new MockRequestContext();
        MutableAttributeMap map = context.getFlowScope();
        Customer customer = new Customer();
        customer.setCustomerType(CustomerType.SPECIFIC_CONCURRENCY);        
        CustomerBean customerBean = new CustomerBean(customer, new ArrayList<ProductEntitlementGroupDto>());
        List<ExternalCustomerId> newExtIds = new ArrayList<ExternalCustomerId>();
        customerBean.setExternalIds(newExtIds);
        customerBean.setChangePassword(true);
        customerBean.setGeneratePassword(true);
        customerBean.setConcurrencyValue("abcd");
        
        map.put("customerBean", customerBean);
        CustomerDto cDTO = new CustomerDto(customer) ;
        expect(this.mockCustomerService.saveCustomer(customer, false)).andReturn(cDTO);    
        EasyMock.replay(getMocks());
        Event res = this.customerAction.saveCustomer(context);
        
        Assert.assertEquals("error",res.getId());
        Message message = context.getMessageContext().getAllMessages()[0];
        Assert.assertEquals("Concurrency value should be in numbers only.", message.getText());
    }
    
    @Test
    public void testUpdateCustomerFaiLByNumberFormatTooLong() throws Exception {
        MockRequestContext context = new MockRequestContext();
        MutableAttributeMap map = context.getFlowScope();
        Customer customer = new Customer();
        customer.setCustomerType(CustomerType.SPECIFIC_CONCURRENCY);        
        CustomerBean customerBean = new CustomerBean(customer, new ArrayList<ProductEntitlementGroupDto>());
        List<ExternalCustomerId> newExtIds = new ArrayList<ExternalCustomerId>();
        customerBean.setExternalIds(newExtIds);
        customerBean.setChangePassword(true);
        customerBean.setGeneratePassword(true);
        customerBean.setConcurrencyValue("1234567890");
        
        map.put("customerBean", customerBean);
        CustomerDto cDTO = new CustomerDto(customer) ;
        expect(this.mockCustomerService.saveCustomer(customer, false)).andReturn(cDTO);    
        EasyMock.replay(getMocks());
        
        Event res = this.customerAction.saveCustomer(context);
        
        Assert.assertEquals("error",res.getId());
        Message message = context.getMessageContext().getAllMessages()[0];
        Assert.assertEquals("Specific concurrency value is too long.", message.getText());
    }
    
    /*@Test
    public void testkillSessionOkay() throws Exception {
        MockRequestContext context = new MockRequestContext();
        MutableAttributeMap map = context.getFlowScope();
        Customer customer = new Customer();
        CustomerBean customerBean = new CustomerBean(customer, new ArrayList<ProductEntitlementGroupDto>());
        
        customer.setCustomerType(CustomerType.SELF_SERVICE);
        List<String> sessions = new ArrayList<String>();
        sessions.add("abcd");
        customer.setSessions(sessions);
        map.put("customerBean", customerBean);
        
        this.mockCustomerService.logout((Customer)EasyMock.anyObject(), (String)EasyMock.anyObject());
        EasyMock.expectLastCall();
        
        EasyMock.replay(getMocks());
        Event result = this.customerAction.killUserSession(context);
        EasyMock.verify(getMocks());
        Assert.assertEquals("success",result.getId());
        
    }
    
    @Test
    public void testkillSessionFailBySessionNotFound() throws Exception {
        MockRequestContext context = new MockRequestContext();
        MutableAttributeMap map = context.getFlowScope();
        Customer customer = new Customer();
        CustomerBean customerBean = new CustomerBean(customer, new ArrayList<ProductEntitlementGroupDto>());
        
        customer.setCustomerType(CustomerType.SELF_SERVICE);
        map.put("customerBean", customerBean);
        
        Event result = this.customerAction.killUserSession(context);        
        Assert.assertEquals("error",result.getId());        
        Message message = context.getMessageContext().getAllMessages()[0];
        Assert.assertEquals("User Session not found.", message.getText());
   
    }
    
    @Test
    public void testkillSessionFailByServiceLayer() throws Exception {
        MockRequestContext context = new MockRequestContext();
        MutableAttributeMap map = context.getFlowScope();
        Customer customer = new Customer();
        CustomerBean customerBean = new CustomerBean(customer, new ArrayList<ProductEntitlementGroupDto>());
        
        customer.setCustomerType(CustomerType.SELF_SERVICE);
        List<String> sessions = new ArrayList<String>();
        sessions.add("abcd");
        customer.setSessions(sessions);
        map.put("customerBean", customerBean);
        
        this.mockCustomerService.logout((Customer)EasyMock.anyObject(), (String)EasyMock.anyObject());
        EasyMock.expectLastCall().andThrow(new ServiceLayerException()); 
        
        EasyMock.replay(getMocks());
        Event result = this.customerAction.killUserSession(context);
        EasyMock.verify(getMocks());
        Assert.assertEquals("error",result.getId());
        Message message = context.getMessageContext().getAllMessages()[0];
        Assert.assertEquals("Error occurred while killing user session.", message.getText());
   
    }
    
    @Test
    public void testkillSessionFailByMultipleSession() throws Exception {
        MockRequestContext context = new MockRequestContext();
        MutableAttributeMap map = context.getFlowScope();
        Customer customer = new Customer();
        CustomerBean customerBean = new CustomerBean(customer, new ArrayList<ProductEntitlementGroupDto>());
        
        customer.setCustomerType(CustomerType.SELF_SERVICE);
        List<String> sessions = new ArrayList<String>();
        sessions.add("abcd");
        sessions.add("abc");
        
        customer.setSessions(sessions);
        map.put("customerBean", customerBean);
        
        
        Event result = this.customerAction.killUserSession(context);
        
        Assert.assertEquals("error",result.getId());
        Message message = context.getMessageContext().getAllMessages()[0];
        Assert.assertEquals("Error occurred while killing user session.", message.getText());
   
    }
    
    @Test
    public void testkillSessionFailByInvalidCustomerTypeShared() throws Exception {
        MockRequestContext context = new MockRequestContext();
        MutableAttributeMap map = context.getFlowScope();
        Customer customer = new Customer();
        CustomerBean customerBean = new CustomerBean(customer, new ArrayList<ProductEntitlementGroupDto>());
        
        customer.setCustomerType(CustomerType.SHARED);
        List<String> sessions = new ArrayList<String>();
        sessions.add("abcd");
        
        customer.setSessions(sessions);
        map.put("customerBean", customerBean);
        
        
        Event result = this.customerAction.killUserSession(context);
        
        Assert.assertEquals("error",result.getId());
        Message message = context.getMessageContext().getAllMessages()[0];
        Assert.assertEquals("Error occurred while killing user session.", message.getText());

   
    }
    
    @Test
    public void testkillSessionFailByInvalidCustomerTypeSpecificConcurrency() throws Exception {
        MockRequestContext context = new MockRequestContext();
        MutableAttributeMap map = context.getFlowScope();
        Customer customer = new Customer();
        CustomerBean customerBean = new CustomerBean(customer, new ArrayList<ProductEntitlementGroupDto>());
        
        customer.setCustomerType(CustomerType.SPECIFIC_CONCURRENCY);
        List<String> sessions = new ArrayList<String>();
        sessions.add("abcd");
        
        customer.setSessions(sessions);
        map.put("customerBean", customerBean);
        
        
        Event result = this.customerAction.killUserSession(context);
        
        Assert.assertEquals("error",result.getId());
        Message message = context.getMessageContext().getAllMessages()[0];
        Assert.assertEquals("Error occurred while killing user session.", message.getText());

   
    }*/
}
