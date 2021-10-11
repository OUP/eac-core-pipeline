package com.oup.eac.admin.rbac;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ContextConfiguration;

import com.oup.eac.common.utils.security.SecurityContextUtils;
import com.oup.eac.domain.AdminUser;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.LicenceTemplate;
import com.oup.eac.domain.Permission;
import com.oup.eac.domain.ProductPageDefinition;
import com.oup.eac.domain.RegisterableProduct;
import com.oup.eac.domain.RegisterableProduct.RegisterableType;
import com.oup.eac.domain.RegistrationActivation;
import com.oup.eac.domain.RegistrationDefinition;
import com.oup.eac.domain.Role;
import com.oup.eac.domain.User;
import com.oup.eac.service.CustomerService;
import com.oup.eac.service.RegistrationDefinitionService;

//@ContextConfiguration(locations = { "classpath*:/eac/web.eac*-beans.xml", "classpath*:/eac/test.web.eac*-beans.xml" })
public class RBACTest /* extends AbstractDBTest */ {
	/*
	 * 
	 * @Autowired private ApplicationContext applicationContext;
	 * 
	 * 
	 * @Autowired private UserDetailsService userDetailsService;
	 * 
	 * @Autowired private AuthenticationProvider eacAuthenticationProvider;
	 * 
	 * @Autowired private CustomerService customerService;
	 * 
	 * @Autowired private RegistrationDefinitionService
	 * registrationDefinitionService;
	 * 
	 * private AdminUser systemAdmin;
	 * 
	 * private AdminUser divisonAdmin;
	 * 
	 * private AdminUser productionAdmin;
	 * 
	 * private AdminUser fieldRepAdmin;
	 * 
	 * @Before public void setUp() throws Exception {
	 * 
	 * Permission createRegistrationDefinition =
	 * getSampleDataCreator().createPermission("CREATE_REGISTRATION_DEFINITION");
	 * Permission createCustomer =
	 * getSampleDataCreator().createPermission("CREATE_CUSTOMER"); Permission
	 * listCustomer = getSampleDataCreator().createPermission("LIST_CUSTOMER");
	 * Permission updateCustomer =
	 * getSampleDataCreator().createPermission("UPDATE_CUSTOMER"); Permission
	 * createActivationCodeBatch =
	 * getSampleDataCreator().createPermission("CREATE_ACTIVATION_CODE_BATCH");
	 * 
	 * Role sysAdmin = getSampleDataCreator().createRole("SYSTEM_ADMIN"); Role
	 * divAdmin = getSampleDataCreator().createRole("DIVISION_ADMIN"); Role
	 * prodAdmin = getSampleDataCreator().createRole("PRODUCTION_ADMIN"); Role
	 * fieldRep = getSampleDataCreator().createRole("FIELD_REP");
	 * 
	 * sysAdmin.getPermissions().add(createRegistrationDefinition);
	 * sysAdmin.getPermissions().add(createCustomer);
	 * sysAdmin.getPermissions().add(listCustomer);
	 * sysAdmin.getPermissions().add(updateCustomer);
	 * sysAdmin.getPermissions().add(createActivationCodeBatch);
	 * 
	 * divAdmin.getPermissions().add(createCustomer);
	 * divAdmin.getPermissions().add(listCustomer);
	 * divAdmin.getPermissions().add(updateCustomer);
	 * divAdmin.getPermissions().add(createActivationCodeBatch);
	 * 
	 * prodAdmin.getPermissions().add(createCustomer);
	 * prodAdmin.getPermissions().add(listCustomer);
	 * prodAdmin.getPermissions().add(updateCustomer);
	 * 
	 * fieldRep.getPermissions().add(createCustomer);
	 * fieldRep.getPermissions().add(listCustomer);
	 * fieldRep.getPermissions().add(updateCustomer);
	 * 
	 * getSampleDataCreator().createRolePermissions(sysAdmin);
	 * getSampleDataCreator().createRolePermissions(divAdmin);
	 * getSampleDataCreator().createRolePermissions(prodAdmin);
	 * getSampleDataCreator().createRolePermissions(fieldRep);
	 * 
	 * systemAdmin = getSampleDataCreator().createAdminUser();
	 * systemAdmin.getRoles().add(sysAdmin);
	 * 
	 * getSampleDataCreator().createUserRoles(systemAdmin);
	 * 
	 * //System admin should have access to all divisions
	 * 
	 * divisonAdmin = getSampleDataCreator().createAdminUser();
	 * divisonAdmin.getRoles().add(divAdmin);
	 * 
	 * getSampleDataCreator().createUserRoles(divisonAdmin);
	 * 
	 * productionAdmin = getSampleDataCreator().createAdminUser();
	 * productionAdmin.getRoles().add(prodAdmin);
	 * 
	 * getSampleDataCreator().createUserRoles(productionAdmin);
	 * 
	 * 
	 * fieldRepAdmin = getSampleDataCreator().createAdminUser();
	 * fieldRepAdmin.getRoles().add(prodAdmin);
	 * 
	 * getSampleDataCreator().createUserRoles(fieldRepAdmin);
	 * 
	 * getSampleDataCreator().createAdminUser();
	 * 
	 * loadAllDataSets(); }
	 * 
	 * @Test public void testSystemAdmin() throws Exception { Authentication
	 * authentication = authenticate(systemAdmin);
	 * assertEquals("Check correct number of roles", 1,
	 * ((User)SecurityContextUtils.getCurrentUser()).getRoles().size());
	 * assertEquals("Check correct number of roles and permissions", 7,
	 * authentication.getAuthorities().size()); }
	 * 
	 * @Test public void testCreateCustomer() throws Exception {
	 * authenticate(systemAdmin); Customer cust = SampleDataFactory.createCustomer()
	 * ; cust.setId(null); customerService.saveCustomer(cust, false);
	 * 
	 * Customer cust1 = SampleDataFactory.createCustomer() ; cust1.setId(null);
	 * authenticate(divisonAdmin); customerService.saveCustomer(cust1, false);
	 * 
	 * Customer cust2 = SampleDataFactory.createCustomer() ; cust2.setId(null);
	 * authenticate(productionAdmin); customerService.saveCustomer(cust2, false);
	 * 
	 * Customer cust3 = SampleDataFactory.createCustomer() ; cust3.setId(null);
	 * authenticate(fieldRepAdmin); customerService.saveCustomer(cust3, false); }
	 * 
	 *//**
		 * Only SYSTEM_ADMIN should be able to run this.
		 * 
		 * @throws Exception
		 */
	/*
	 * @Test public void testCreatRegistrationDefinition() throws Exception {
	 * 
	 * RegisterableProduct product =
	 * SampleDataFactory.createRegisterableProduct(12345,"product 12345",
	 * RegisterableType.SELF_REGISTERABLE); LicenceTemplate licenceTemplate =
	 * SampleDataFactory.createRollingLicenceTemplate(); RegistrationActivation
	 * registrationActivation =
	 * SampleDataFactory.createInstantRegistrationActivation();
	 * ProductPageDefinition pageDefinition =
	 * SampleDataFactory.createProductPageDefinition(); RegistrationDefinition rd =
	 * SampleDataFactory.createProductRegistrationDefinition(product,
	 * licenceTemplate, registrationActivation, pageDefinition);
	 * 
	 * try { authenticate(divisonAdmin);
	 * registrationDefinitionService.saveRegistrationDefinition(rd);
	 * assertTrue("Check correct exception thrown", false); } catch
	 * (AccessDeniedException ade) { assertTrue("Check correct exception thrown",
	 * true); }
	 * 
	 * try { authenticate(productionAdmin);
	 * registrationDefinitionService.saveRegistrationDefinition(rd);
	 * assertTrue("Check correct exception thrown", false); } catch
	 * (AccessDeniedException ade) { assertTrue("Check correct exception thrown",
	 * true); }
	 * 
	 * try { authenticate(fieldRepAdmin);
	 * registrationDefinitionService.saveRegistrationDefinition(rd);
	 * assertTrue("Check correct exception thrown", false); } catch
	 * (AccessDeniedException ade) { assertTrue("Check correct exception thrown",
	 * true); } }
	 * 
	 *//**
		 * Authenticate user and store them in security context.
		 * 
		 * @param adminUser
		 */
	/*
	 * private Authentication authenticate(final AdminUser adminUser) {
	 * Authentication auth = eacAuthenticationProvider.authenticate(new
	 * UsernamePasswordAuthenticationToken(adminUser.getUsername(),
	 * adminUser.getPassword()));
	 * SecurityContextHolder.getContext().setAuthentication(auth); return auth; }
	 */}
