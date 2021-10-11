package com.oup.eac.admin.flows;

import java.util.Arrays;
import java.util.List;

import javax.naming.NamingException;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.webflow.core.collection.LocalAttributeMap;
import org.springframework.webflow.core.collection.MutableAttributeMap;
import org.springframework.webflow.test.MockExternalContext;
import org.springframework.webflow.test.MockParameterMap;

import com.oup.eac.admin.beans.CustomerBean;
import com.oup.eac.domain.AdminUser;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.ExternalCustomerId;
import com.oup.eac.domain.ExternalSystem;
import com.oup.eac.domain.ExternalSystemIdType;
import com.oup.eac.domain.Permission;
import com.oup.eac.domain.Role;
import com.oup.eac.dto.CustomerDto;
import com.oup.eac.dto.HashedLoginPasswordCredentialDto;

public class CreateOrEditCustomerFlowTest /* extends AbstractFlowTest */ {
	/*
	
	*//**
		 * A logger instance that is used in this class
		 */
	/*
	 * private static final Logger LOGGER =
	 * Logger.getLogger(CreateOrEditCustomerFlowTest.class);
	 * 
	 * private AdminUser adminUser;
	 * 
	 * private Customer customer;
	 * 
	 * public CreateOrEditCustomerFlowTest() throws NamingException { super(); }
	 * 
	 * @Override protected String getFlowPath() { return
	 * "src/main/webapp/WEB-INF/flows/customer/create/create-flow.xml"; }
	 * 
	 * @Override public String[] getBeansToInject() { return new
	 * String[]{"customerService","customerAction","externalIdService"}; }
	 * 
	 * public void _setUp() throws Exception { super.setUp(); if
	 * (LOGGER.isDebugEnabled()) { LOGGER.debug("start setUp()"); }
	 * 
	 * Permission createCustomer =
	 * sampleDataCreator.createPermission("CREATE_CUSTOMER"); Role sysAdmin =
	 * sampleDataCreator.createRole("SYSTEM_ADMIN");
	 * sysAdmin.getPermissions().add(createCustomer);
	 * sampleDataCreator.createRolePermissions(sysAdmin);
	 * 
	 * adminUser = sampleDataCreator.createAdminUser();
	 * adminUser.getRoles().add(sysAdmin);
	 * sampleDataCreator.createUserRoles(adminUser);
	 * 
	 * authenticate(adminUser);
	 * 
	 * customer = sampleDataCreator.createCustomer();
	 * 
	 * fakeErightsFacade.addCustomerDto(new CustomerDto( customer.getUsername(),
	 * Arrays.asList(new String[]{}), 1, new
	 * HashedLoginPasswordCredentialDto(customer.getUsername()), false,
	 * customer.getUserType(), customer.getId(), customer.getEmailAddress(),
	 * customer.getFirstName(), customer.getFamilyName(),
	 * customer.getEmailVerificationState(), customer.getTimeZone(),
	 * customer.getLocale(), customer.getExternalIds(), customer.isResetPassword()
	 * ));
	 * 
	 * ExternalSystem extSys =
	 * sampleDataCreator.createExternalSystem("Test System 1","Test System 1 Decs");
	 * ExternalSystemIdType extSysIdType =
	 * sampleDataCreator.createExternalSystemIdType(extSys,
	 * "External System Id Type", "ISBN"); loadAllDataSets(); }
	 * 
	 * @Test public void _testStartCreateCustomerFlow() {
	 * 
	 * MockExternalContext context = new MockExternalContext(); startFlow(context);
	 * 
	 * assertCurrentStateEquals("createOrEditCustomer"); CustomerBean customerBean =
	 * (CustomerBean)getRequiredFlowAttribute("customerBean");
	 * assertEquals("Check blank customer", null,
	 * customerBean.getCustomer().getId());
	 * 
	 * customerBean.getCustomer().setEmailAddress("myemail@mail.com");
	 * customerBean.getCustomer().setUsername("new-user");
	 * customerBean.setGeneratePassword(true);
	 * 
	 * this.getFlowScope().put("customerBean", customerBean);
	 * 
	 * context.setEventId("save");
	 * 
	 * resumeFlow(context);
	 * 
	 * assertFlowExecutionEnded();
	 * 
	 * assertEquals("Check redirect requested", "saveSuccess",
	 * getFlowExecutionOutcome().getId()); }
	 * 
	 * @Test public void _testStartEditCustomerFlow() { MutableAttributeMap input =
	 * new LocalAttributeMap(); input.put("customerId", customer.getId());
	 * MockExternalContext context = new MockExternalContext();
	 * 
	 * startFlow(input, context);
	 * 
	 * assertCurrentStateEquals("createOrEditCustomer"); CustomerBean customerBean =
	 * (CustomerBean)getRequiredFlowAttribute("customerBean"); Boolean editMode =
	 * (Boolean)getRequiredFlowAttribute("editMode");
	 * 
	 * assertTrue("Check edit mode", editMode); assertEquals("Check same customer",
	 * customer.getId(), customerBean.getCustomer().getId());
	 * 
	 * //Add external id
	 * 
	 * List<ExternalSystem> externalSystems =
	 * (List<ExternalSystem>)getRequiredViewAttribute("externalSystems");
	 * List<ExternalSystemIdType> externalSystemIdTypes =
	 * (List<ExternalSystemIdType>)getRequiredViewAttribute("externalSystemIdTypes")
	 * ;
	 * 
	 * customerBean.setExternalSystem(externalSystems.get(0));
	 * customerBean.setExternalSystemIdType(externalSystemIdTypes.get(0));
	 * customerBean.setExternalId("123456");
	 * 
	 * this.getFlowScope().put("customerBean", customerBean);
	 * 
	 * context.setEventId("addExternalId"); resumeFlow(context);
	 * 
	 * customerBean = (CustomerBean)getRequiredFlowAttribute("customerBean");
	 * 
	 * ExternalCustomerId extId = customerBean.getExternalIds().iterator().next();
	 * 
	 * assertEquals("Check correct external system", externalSystems.get(0),
	 * extId.getExternalSystemIdType().getExternalSystem());
	 * assertEquals("Check correct external system id type",
	 * externalSystemIdTypes.get(0), extId.getExternalSystemIdType());
	 * assertEquals("Check correct external id", "123456", extId.getExternalId());
	 * 
	 * 
	 * //Add new request parameter and try and remove external id MockParameterMap
	 * params = new MockParameterMap(); params.put("removalIndex", "0"); context =
	 * new MockExternalContext(params); context.setEventId("removeExternalId");
	 * resumeFlow(context);
	 * 
	 * customerBean = (CustomerBean)getRequiredFlowAttribute("customerBean");
	 * 
	 * assertEquals("Check no external ids", 0,
	 * customerBean.getExternalIds().size());
	 * 
	 * //Attempt to change password customerBean.setChangePassword(true);
	 * customerBean.setPassword("aNewPassword");
	 * 
	 * this.getFlowScope().put("customerBean", customerBean);
	 * 
	 * context.setEventId("edit"); resumeFlow(context);
	 * 
	 * String statusMsgKey =
	 * (String)this.getFlowExecution().getFlashScope().get("statusMessageKey");
	 * assertEquals("Check correct message returned",
	 * "status.customer.update.success", statusMsgKey);
	 * 
	 * assertFlowExecutionEnded(); }
	 */}
