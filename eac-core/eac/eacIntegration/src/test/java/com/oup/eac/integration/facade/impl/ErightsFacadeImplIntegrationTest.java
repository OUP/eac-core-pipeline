package com.oup.eac.integration.facade.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.oup.eac.common.utils.EACSettings;
import com.oup.eac.common.utils.ssl.SSLUtilities;
import com.oup.eac.domain.ConcurrentLicenceTemplate;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.Customer.CustomerType;
import com.oup.eac.domain.ErightsDenyReason;
import com.oup.eac.domain.ExternalProductId;
import com.oup.eac.domain.ExternalSystem;
import com.oup.eac.domain.ExternalSystemIdType;
import com.oup.eac.domain.LicenceTemplate;
import com.oup.eac.domain.LicenceTemplate.LicenceType;
import com.oup.eac.domain.Password;
import com.oup.eac.domain.Platform;
import com.oup.eac.domain.Product.ProductState;
import com.oup.eac.domain.RegistrationActivation.ActivationStrategy;
import com.oup.eac.domain.RegistrationDefinition.RegistrationDefinitionType;
import com.oup.eac.domain.RollingLicenceTemplate;
import com.oup.eac.domain.RollingLicenceTemplate.RollingBeginOn;
import com.oup.eac.domain.RollingLicenceTemplate.RollingUnitType;
import com.oup.eac.domain.StandardLicenceTemplate;
import com.oup.eac.domain.UsageLicenceTemplate;
import com.oup.eac.dto.AuthenticationResponseDto;
import com.oup.eac.dto.CustomerDto;
import com.oup.eac.dto.DivisionDto;
import com.oup.eac.dto.EnforceableProductDto;
import com.oup.eac.dto.EnforceableProductUrlDto;
import com.oup.eac.dto.LicenceDetailDto;
import com.oup.eac.dto.LicenceDto;
import com.oup.eac.dto.LoginPasswordCredentialDto;
import com.oup.eac.dto.RollingLicenceDetailDto;
import com.oup.eac.dto.StandardConcurrentLicenceDetailDto;
import com.oup.eac.dto.UsageLicenceDetailDto;
import com.oup.eac.integration.erights.AuthenticationResponseSTATUS;
import com.oup.eac.integration.erights.UserAccountResponseSTATUS;
import com.oup.eac.integration.facade.ErightsFacade;
import com.oup.eac.integration.facade.ErightsRestFacade;
import com.oup.eac.integration.facade.exceptions.ChildProductFoundException;
import com.oup.eac.integration.facade.exceptions.ErightsException;
import com.oup.eac.integration.facade.exceptions.ErightsSessionNotFoundException;
import com.oup.eac.integration.facade.exceptions.InvalidCredentialsException;
import com.oup.eac.integration.facade.exceptions.LicenseNotFoundException;
import com.oup.eac.integration.facade.exceptions.ParentProductNotFoundException;
import com.oup.eac.integration.facade.exceptions.PasswordPolicyViolatedException;
import com.oup.eac.integration.facade.exceptions.ProductNotFoundException;
import com.oup.eac.integration.facade.exceptions.SessionConcurrencyException;
import com.oup.eac.integration.facade.exceptions.UserLoginCredentialAlreadyExistsException;
import com.oup.eac.integration.facade.exceptions.UserNotFoundException;
import com.oup.eac.ws.rest.v1.DeletePlatformResponse;
import com.oup.eac.ws.rest.v1.GetAllPlatformsResponse;
import com.oup.eac.ws.rest.v1.OupPlatform;
import com.sun.xml.ws.util.ByteArrayBuffer;

@Ignore
@ContextConfiguration(locations = { "classpath*:/eac/eac*-beans.xml", "classpath*:/eac/test.eac*-beans.xml" })
public class ErightsFacadeImplIntegrationTest extends AbstractJUnit4SpringContextTests {

	private static Logger LOG = Logger.getLogger(ErightsFacadeImplIntegrationTest.class);

	private static final boolean UAT_TEST = Boolean.valueOf(System.getProperty("uat.test", "false"));
	
	private static String WEB_SERVICE_URL = "http://ec2-52-208-167-63.eu-west-1.compute.amazonaws.com:8080/acesWebService/services/access-service-v1.0.wsdl";
	//private static String WEB_SERVICE_URL = "http://ec2-52-208-167-63.eu-west-1.compute.amazonaws.com:8080/acesWebService/services/access-service-v1.0.wsdl";
	/*    private static String WEB_SERVICE_URL = "http://rs.dev.access.oup.com/oup/ws/OUPRightAccessService?wsdl";*/
	//private static String WEB_SERVICE_URL = "http://localhost:9002/services/OUPRightAccessService?wsdl";
	private static String PRODUCT_URL = "http://dev.access.oup.com/eacSampleSite/protected/release1/oxfordfajar/spm-success-addmath.jsp";
	private static final String EAC_COOKIE_NAME = "EAC_ERIGHTS";
	private static String ERIGHTS_PRODUCT_ID = UUID.randomUUID().toString();
	private static String ERIGHTS_ALL_PRODUCTS_ID = UUID.randomUUID().toString();

	private static final int USER_CONCURRENCY = 5;
	private static final String UNKNOWN_USER = UUID.randomUUID().toString();
	private static final String NON_EXISTANT_USER_ID = UUID.randomUUID().toString();
	private static final String INVALID_LICENCE_ID = UUID.randomUUID().toString();
	private static final String INVALID_USER_ID = UUID.randomUUID().toString();
	private String email;
	private String id;
	private Customer customer;
	private CustomerDto customerDto;
	private int divisionId ;
	/*private Platform platform_create ;
	private Platform platform_update ;*/
	private Customer customerNew;

	private String unique;

	@Autowired
	@Qualifier("rewindableErightsFacade")
	private ErightsFacade erightsFacade;
	
	@Autowired
	@Qualifier("rewindableErightsRestFacade")
	private ErightsRestFacade erightsRestFacade;

	/**
	 * Setup test, obtaining the service and creating a user.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Before
	public final void setUp() throws Exception {
		System.out.println("Setup-----------------------------");
		List<DivisionDto> divisionDtos = new ArrayList<DivisionDto>();
		DivisionDto div = new DivisionDto();
		// TODO uat wsdl has moved. UAT test will not work until new wsdl added.
		if (UAT_TEST) {
			ERIGHTS_PRODUCT_ID = "4301";
			PRODUCT_URL = "http://uatcw.oxfordfajar.net/resource-details.aspx?id=MTE4Mg==";
			// Tunnel must be active on localhost that forwards WS traffic via
			// EAC UAT server
			WEB_SERVICE_URL = "https://eac-dev-a-eacerigh-gmycvfloixgj-156402362.eu-west-1.elb.amazonaws.com/acesWebService/soap/services/access-service-v1.0.wsdl?wsdl";

			// ws.security.username=oupUAT
			// ws.security.password=T35t1nG
			System.setProperty(EACSettings.WS_SECURITY_USERNAME, "oupUAT");
			System.setProperty(EACSettings.WS_SECURITY_PASSWORD, "T35t1nG");
			div.setDivisionType("Test new division");
	        div.setId(new Random().nextInt(50));		
			divisionDtos.add(div );
			List<DivisionDto> divisionIds = erightsFacade.createDivision(divisionDtos);
			divisionId= divisionIds.get(0).getId();
		}
		
		
		System.setProperty(EACSettings.WS_SECURITY_USERNAME, "oupUAT");
		System.setProperty(EACSettings.WS_SECURITY_PASSWORD, "G4v_xULo_vYbLQFONMAxmg");
		unique = UUID.randomUUID().toString().replace("-", "");
		LOG.debug("Unique Key for test: " + unique);

		System.setProperty(EACSettings.ERIGHTS_WEBSERVICE_URL, WEB_SERVICE_URL);
		SSLUtilities.trustAllHostnames();
		SSLUtilities.trustAllHttpsCertificates();

		div.setDivisionType("Test new division");
        div.setId(new Random().nextInt(50));		
		divisionDtos.add(div );
		List<DivisionDto> divisionIds = erightsFacade.createDivision(divisionDtos);
		divisionId= divisionIds.get(0).getId();
		
		// Create some groups for testing
		/*parentGroup = new GroupDto("Parent Group");
		parentGroup = erightsFacade.createGroup(parentGroup);

		childGroup = new GroupDto("Child Group");
		childGroup.addParentGroupId(parentGroup.getErightsId());

		childGroup = erightsFacade.createGroup(childGroup);*/

		email = unique + "@mailinator.com";
		customer = new Customer();
		customer.setUsername(email);
		customer.setPassword(new Password("password", false));
		customer.setSuspended(false);
		customer.setEmailAddress(email);
		customerNew = new Customer();
		customerNew.setUsername(email);
		customerNew.setEmailAddress(email);
		customerNew.setPassword(new Password("passwordTest", false));

		//List<String> parentGroupIds = new ArrayList<String>();
		//parentGroupIds.add(String.valueOf(childGroup.getErightsId()));
		customerDto = new CustomerDto(customer);

		customerDto = erightsFacade.createUserAccount(customerDto);

		id = customerDto.getUserId();

		customer.setId(id);
		customerNew.setId(id);
	}

	@After
	public void tearDown() {
		if (erightsFacade instanceof RewindableErightsFacade) {
			((RewindableErightsFacade) erightsFacade).rewind();
		}
	}

	/**
	 * Create a user, authenticate and logout. Tests various error conditions
	 * along the way.
	 * 
	 * @throws Exception
	 *             the exception
	 */

	@Test
	public final void testCreateUserAccountAuthenticateAndLogout()
			throws Exception {
		try {
			Customer customer = new Customer();
			customer.setUsername(email);
			customer.setPassword(new Password("password", false));
			customer.setEmailAddress(email);
			customer.setSuspended(false);
			CustomerDto customerDto = new CustomerDto(customer);

			erightsFacade.createUserAccount(customerDto);
			assertTrue("Check that the correct exception was thrown", false);
		} catch (ErightsException e) {
			// all good
			if (e.getCode() != null && e.getCode().equals( UserAccountResponseSTATUS.USER_ALREADY_EXISTS.toString()))
				assertTrue("Check correct exception was thrown", true);
			else
				assertTrue("Check correct exception was thrown", false);
		}

		try {
			// authenticate using user we created
			AuthenticationResponseDto authDto = erightsFacade
					.authenticateUser(new LoginPasswordCredentialDto(email,
							"password"));
			assertEquals("Check user created id is same as authenticated id",
					id, authDto.getUserIds().get(0));
			// check the user id is returned correctly for the session key
			List<String> ids = erightsFacade.getCustomerIdsFromSession(authDto
					.getSessionKey());
			assertEquals("Check correct customer id returned for session key",
					id, ids.get(0));
			// Check we can log this session out
			erightsFacade.logout(authDto.getSessionKey());
			try {
				// check session not found after logout
				ids = erightsFacade.getCustomerIdsFromSession(authDto
						.getSessionKey());
				assertEquals(
						"Check correct customer id returned for session key",
						id, ids.get(0));
				assertTrue("Check an exception was thrown", false);
			} catch (ErightsException e) {
				assertEquals("Check correct error was thrown by erights",
						UserAccountResponseSTATUS.SESSION_NOT_FOUND
						.value(), e.getCode());
			}
		} catch (Exception e) {
			assertTrue("Check an exception was not thrown", false);
		}

		try {
			erightsFacade.authenticateUser(new LoginPasswordCredentialDto(
					email, "badpassword"));
			assertTrue("Check an exception was thrown", false);
		} catch (InvalidCredentialsException e) {
			// all good
			assertTrue("Check correct exception was thrown", true);
		}
	}

	/**
	 * Test changing the users password by id and username. Tests changing the
	 * password when user is logged in and logged out.
	 * 
	 * @throws Exception
	 *             the exception
	 */

	@SuppressWarnings("unused")
	@Test
	public final void testChangingPassword() throws Exception {
		// change password then try and login
		erightsFacade.changePasswordByUserId(id, "newpassword");

		// should fail with old password
		try {
			erightsFacade.authenticateUser(new LoginPasswordCredentialDto(
					email, "password"));
			assertTrue("Check the correct exception was thrown", false);
		} catch (InvalidCredentialsException e) {
			// all good
			assertTrue("Check correct exception was thrown", true);
		}

		// should be able to login using new password
		AuthenticationResponseDto authDto = erightsFacade
				.authenticateUser(new LoginPasswordCredentialDto(email,
						"newpassword"));
		AuthenticationResponseDto authDto2 = erightsFacade
				.authenticateUser(new LoginPasswordCredentialDto(email,
						"newpassword"));
		AuthenticationResponseDto authDto3 = erightsFacade
				.authenticateUser(new LoginPasswordCredentialDto(email,
						"newpassword"));
		AuthenticationResponseDto authDto4 = erightsFacade
				.authenticateUser(new LoginPasswordCredentialDto(email,
						"newpassword"));
		AuthenticationResponseDto authDto5 = erightsFacade
				.authenticateUser(new LoginPasswordCredentialDto(email,
						"newpassword"));

		// change password while logged in
		erightsFacade.changePasswordByUserId(id, "anothernewpassword");

		try {
			erightsFacade.authenticateUser(new LoginPasswordCredentialDto(
					email, "anothernewpassword"));
			assertTrue("Check a concurrency exception was thrown", false);
		} catch (ErightsException e) {
			assertEquals(
					"Check the correct exception was thrown",
					AuthenticationResponseSTATUS.FAILURE_SESSION_CONCURRENCY_EXCEEDED
					.value(), e.getCode());
		}

		erightsFacade.logout(authDto.getSessionKey());

		// check with the new password set while already logged in
		authDto = erightsFacade
				.authenticateUser(new LoginPasswordCredentialDto(email,
						"anothernewpassword"));

		erightsFacade.logout(authDto.getSessionKey());

		// change password then try and login
		erightsFacade.changePasswordByUsername(email, "passwordnew");

		// should fail with old password
		try {
			erightsFacade.authenticateUser(new LoginPasswordCredentialDto(
					email, "badpassword"));
			assertTrue("Check the correct exception was thrown", false);
		} catch (InvalidCredentialsException e) {
			// all good
			assertTrue("Check correct exception was thrown", true);
		}

		// should be able to login using new password
		authDto = erightsFacade
				.authenticateUser(new LoginPasswordCredentialDto(email,
						"passwordnew"));

		erightsFacade.logout(authDto.getSessionKey());

		// try changing password of unknown username
		try {
			erightsFacade.authenticateUser(new LoginPasswordCredentialDto(
					email, "badpassword"));
			assertTrue("Check the correct exception was thrown", false);
		} catch (InvalidCredentialsException e) {
			// all good
			assertTrue("Check correct exception was thrown", true);
		}

		// try changing password of unknown username
		try {
			erightsFacade.changePasswordByUserId(UNKNOWN_USER, "apassword");
			assertTrue("Check the correct exception was thrown", false);
		} catch (ErightsException e) {
			// all good
			assertEquals("Check correct exception was thrown",
					UserAccountResponseSTATUS.USER_NOT_FOUND.value(),
					e.getCode());
		}

		// try changing password of unknown username
		try {
			erightsFacade.changePasswordByUsername("abademail@me.com",
					"apassword");
			assertTrue("Check the correct exception was thrown", false);
		} catch (ErightsException e) {
			// all good
			assertEquals("Check correct exception was thrown",
					UserAccountResponseSTATUS.USER_NOT_FOUND.value(),
					e.getCode());
		}

	}


	@Test
	public void testGetUser() throws Exception {
		CustomerDto savedCustomerDto = erightsFacade.getUserAccount(id);

		assertEquals("Check concurrency is same", customerDto.getConcurrency(),
				savedCustomerDto.getConcurrency());

		// get groups from original customer, compare size against retrieved
		// customer
		/*assertEquals("Check correct number of groups", 1, savedCustomerDto
				.getGroupIds().size());*/

		// Check credentials are correct
		assertEquals("Check username", customerDto.getUsername(),
				savedCustomerDto.getUsername());
		assertEquals("Check credentials", customerDto
				.getLoginPasswordCredential().getUsername(), savedCustomerDto
				.getLoginPasswordCredential().getUsername());
		assertEquals("Check credentials", email, savedCustomerDto
				.getLoginPasswordCredential().getUsername());
		assertEquals("Check email address", customerDto.getEmailAddress(), 
				savedCustomerDto.getEmailAddress());

		// check concurrency
		assertEquals("Check username", new Integer(USER_CONCURRENCY),
				savedCustomerDto.getConcurrency());

		// Exception should be thrown with hashed password
		try {
			savedCustomerDto.getLoginPasswordCredential().getPassword();
			assertTrue("Check correct exception was thrown", false);
		} catch (IllegalStateException ise) {
			assertTrue("Check correct exception was thrown", true);
		}

		// We expect the customer to be the same as the one created in set up
		assertEquals("Check customer erights id is same",
				customer.getId(), savedCustomerDto.getUserId());

		// test correct exception thrown when user does not exist
		try {
			savedCustomerDto = erightsFacade.getUserAccount(INVALID_USER_ID);
			assertTrue("Check correct exception was thrown", false);
		} catch (UserNotFoundException unfe) {
			assertTrue("Check correct exception was thrown", true);
		}
	}


	@SuppressWarnings("unused")
	@Test
	public void testUpdatingUser() throws Exception {

		// Check user returned has correct username
		CustomerDto savedCustomerDto = erightsFacade.getUserAccount(id);
		assertEquals("Check customer username is same", customer.getUsername(),
				savedCustomerDto.getUsername());
		customer.setPassword(new Password("", true));
		savedCustomerDto.mergeCustomerChanges(customer,false);
	
		// Update without change should leave password untouched
		erightsFacade.updateUserAccount(savedCustomerDto);
		customerDto = erightsFacade.getUserAccount(id);

		AuthenticationResponseDto authResponse = erightsFacade
				.authenticateUser(new LoginPasswordCredentialDto(email,
						"password"));
		AuthenticationResponseDto resp1 = erightsFacade
				.authenticateUser(new LoginPasswordCredentialDto(email,
						"password"));
		AuthenticationResponseDto resp2 = erightsFacade
				.authenticateUser(new LoginPasswordCredentialDto(email,
						"password"));
		AuthenticationResponseDto resp3 = erightsFacade
				.authenticateUser(new LoginPasswordCredentialDto(email,
						"password"));
		AuthenticationResponseDto resp4 = erightsFacade
				.authenticateUser(new LoginPasswordCredentialDto(email,
						"password"));

		// Test sessions are not killed after update if credentials are not
		// changed
		try {
			erightsFacade.authenticateUser(new LoginPasswordCredentialDto(
					email, "password"));
			assertTrue("Check correct exception thrown", false);
		} catch (SessionConcurrencyException ee) {
			// Expected
		}

		// Update username only
		String newLogin = "newlogin-" + email;
		customer.setUsername(newLogin);
		// Set password on customer as hashed so it is not used during update
		customer.setPassword(new Password("thispasswordishashed", true));
		savedCustomerDto.mergeCustomerChanges(customer,true);
		erightsFacade.updateUserAccount(savedCustomerDto);

		// get updated user
		savedCustomerDto = erightsFacade.getUserAccount(id);

		assertEquals("Check username changed", newLogin, savedCustomerDto
				.getLoginPasswordCredential().getUsername());

		// Session should still not have been killed after change in credentials
		// (just dropped from cache)
		try {
			erightsFacade.authenticateUser(new LoginPasswordCredentialDto(
					newLogin, "password"));
			assertTrue("Check correct exception thrown", false);
		} catch (SessionConcurrencyException ee) {
			// Expected
		}

		erightsFacade.logout(authResponse.getSessionKey());

		// Login again with new username
		authResponse = erightsFacade
				.authenticateUser(new LoginPasswordCredentialDto(newLogin,
						"password"));

		customer.setUsername(email);
		customer.setPassword(new Password("newpassword", false));
		savedCustomerDto.mergeCustomerChanges(customer,true);
		erightsFacade.updateUserAccount(savedCustomerDto);

		// Session should still not have been killed after change in credentials
		// (just dropped from cache)
		try {
			erightsFacade.authenticateUser(new LoginPasswordCredentialDto(
					email, "newpassword"));
			assertTrue("Check correct exception thrown", false);
		} catch (SessionConcurrencyException ee) {
			// Expected
		}

		erightsFacade.logout(authResponse.getSessionKey());

		erightsFacade.authenticateUser(new LoginPasswordCredentialDto(email,
				"newpassword"));

		savedCustomerDto = erightsFacade.getUserAccount(id);

		// Test creating a user with credentials in use by another user
		String newEmail = UUID.randomUUID().toString().replace("-", "")
				+ "@mailinator.com";
		Customer newCustomer = new Customer();
		newCustomer.setUsername(newEmail);
		newCustomer.setPassword(new Password("password", false));
		newCustomer.setEmailAddress(newEmail);
		CustomerDto newCustomerDto = new CustomerDto(newCustomer);

		newCustomerDto = erightsFacade.createUserAccount(newCustomerDto);

		CustomerDto newSavedCustomerDto = erightsFacade
				.getUserAccount(newCustomerDto.getUserId());
		assertEquals("Check customer username is same", newEmail,
				newSavedCustomerDto.getUsername());

		assertEquals("Check username of credential", newEmail,
				newSavedCustomerDto.getLoginPasswordCredential().getUsername());
		 Customer cust = new Customer();
		 cust.setPassword(new Password("", true));
		newSavedCustomerDto.mergeCustomerChanges(customer,false);
		// Ensure login possible when updating user with untouched credential
		// (tests password is not used in HashedLoginPasswordCredential)
		//erightsFacade.updateUserAccount(newSavedCustomerDto);
		erightsFacade.authenticateUser(new LoginPasswordCredentialDto(newEmail,
				"password"));

		newCustomer.setUsername(customerDto.getLoginPasswordCredential()
				.getUsername());
		newCustomer.setPassword(new Password("password", false));
		newSavedCustomerDto.mergeCustomerChanges(newCustomer,true);

		try {
			erightsFacade.updateUserAccount(newSavedCustomerDto);
			assertTrue("Check correct exception thrown", false);
		} catch (UserLoginCredentialAlreadyExistsException ulcaee) {
			// This exception is thrown in two scenarios. If two login/password
			// credentials were passed in webservice (not currently supported
			// via eac)
			// Or if another user has the same username (which is tested here)
			assertTrue("Check correct exception thrown", true);
		}

		// test modification of unknown user
		try {
			Customer invalidCustomer = new Customer();
			invalidCustomer.setUsername("invalid Customer");
			invalidCustomer.setId(INVALID_USER_ID);
			invalidCustomer.setEmailAddress(email);
			invalidCustomer.setPassword(new Password("anypassword", false));
			CustomerDto invalidCustomerDto = new CustomerDto(invalidCustomer);
			erightsFacade.updateUserAccount(invalidCustomerDto);
			assertTrue("Check correct exception thrown", false);
		} catch (UserNotFoundException ere) {
			// Cannot update a user that doesn't exist
			assertTrue("Check correct exception thrown", true);
		}

		// Try updating customer, including suspending user
		String anotherEmail = UUID.randomUUID().toString().replace("-", "")
				+ "@mailinator.com";

		Customer customerToUpdate = new Customer();
		customerToUpdate.setId(savedCustomerDto.getUserId());
		customerToUpdate.setUsername(anotherEmail);
		customerToUpdate.setPassword(new Password(UUID.randomUUID().toString(), false));
		customerToUpdate.setSuspended(true);
		customerToUpdate.setEmailAddress(anotherEmail);

		CustomerDto customerToUpdateDto;
		
		// Tests process of updating user without initial trip to erights
		customerToUpdateDto = new CustomerDto(customerToUpdate);

		erightsFacade.updateUserAccount(customerToUpdateDto);
		customerToUpdate.setPassword(new Password(UUID.randomUUID().toString(), false));
		customerToUpdateDto = new CustomerDto(customerToUpdate);
		erightsFacade.updateUserAccount(customerToUpdateDto);

		// Check updated user
		CustomerDto customerUpdatedAndReturned = erightsFacade
				.getUserAccount(customerToUpdate.getId());

		assertEquals("Check username was updated", anotherEmail,
				customerUpdatedAndReturned.getUsername());
		assertTrue("Check suspension", customerUpdatedAndReturned.isSuspended());

		// User is now suspended. Should not be able to log in.
		try {
			erightsFacade.authenticateUser(new LoginPasswordCredentialDto(
					anotherEmail, "passwordnew"));
			assertTrue("Check correct exception thrown", false);
		} catch (ErightsException ee) {
			// Expected
		}
	}

	/**
	 * Test case that is designed to expose problem when dealing with multiple
	 * auth profiles. Erights currently throws exception during this test. It
	 * should work just fine.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testUpdatingCredentialWithoutIds() throws Exception {

		// Check user returned has correct username
		CustomerDto savedCustomerDto = erightsFacade.getUserAccount(id);
		customer.setUsername(email);
		customer.setPassword(new Password("newpasswor", false));
		savedCustomerDto.mergeCustomerChanges(customer,true);
		erightsFacade.updateUserAccount(savedCustomerDto);

		// Try updating customer
		String anotherEmail = UUID.randomUUID().toString().replace("-", "")
				+ "@mailinator.com";

		Customer customerToUpdate = new Customer();
		customerToUpdate.setUsername(anotherEmail);
		customerToUpdate.setEmailAddress(savedCustomerDto.getEmailAddress());
		customerToUpdate.setPassword(new Password("passwordnew", false));
		customerToUpdate.setId(savedCustomerDto.getUserId());

		CustomerDto customerToUpdateDto;

		customerToUpdateDto = new CustomerDto(customerToUpdate);

		erightsFacade.updateUserAccount(customerToUpdateDto);
	}

	@Test
	public void samePasswordTest() throws Exception {

		// Test updating a user with credentials in use by another user
		String newEmail = UUID.randomUUID().toString().replace("-", "")
				+ "@mailinator.com";
		Customer newCustomer = new Customer();
		newCustomer.setUsername(newEmail);
		newCustomer.setEmailAddress(newEmail);
		newCustomer.setPassword(new Password("password", false));
		CustomerDto newCustomerDto = new CustomerDto(newCustomer);

		newCustomerDto = erightsFacade.createUserAccount(newCustomerDto);

		CustomerDto savedCustomerDto = erightsFacade
				.getUserAccount(newCustomerDto.getUserId());

		// Try updating customer, including suspending user
		String anotherEmail = UUID.randomUUID().toString().replace("-", "")
				+ "@mailinator.com";

		Customer customerToUpdate = new Customer();
		customerToUpdate.setId(savedCustomerDto.getUserId());
		customerToUpdate.setEmailAddress(savedCustomerDto.getEmailAddress());
		customerToUpdate.setUsername(anotherEmail);
		customerToUpdate.setSuspended(true);
		customerToUpdate.setPassword(new Password("password", false));

		CustomerDto customerToUpdateDto = new CustomerDto(customerToUpdate);

		try {
			erightsFacade.updateUserAccount(customerToUpdateDto);
			assertTrue("Check correct exception was throwsn", false);
		} catch (PasswordPolicyViolatedException unfe) {
			// Expected
		}
	}

	@Test
	public void testDeleteingUser() throws Exception {

		// Get user
		// Check user returned has correct username
		CustomerDto savedCustomerDto = erightsFacade.getUserAccount(id);

		assertEquals("Check customer username is same", customer.getUsername(),
				savedCustomerDto.getUsername());

		// Delete user that exists,
		erightsFacade.deleteUserAccount(savedCustomerDto.getUserId());

		// Get user, confirm no user found
		try {
			erightsFacade.getUserAccount(id);
			assertTrue("Check correct exception was throwsn", false);
		} catch (UserNotFoundException unfe) {
			// Expected
		}

		// Delete user that doesn't exist, check exception thrown
		try {
			erightsFacade.deleteUserAccount(INVALID_USER_ID);
			assertTrue("Check correct exception was throwsn", false);
		} catch (UserNotFoundException unfe) {
			// Expected
		}
	}
	
	private LicenceTemplate createLicenceTemplate(LicenceType licenceType) {
		//LicenceType licenceType = productBean.getLicenceType();
		if (LicenceType.CONCURRENT == licenceType) return new ConcurrentLicenceTemplate();
		if (LicenceType.ROLLING == licenceType) return new RollingLicenceTemplate();
		if (LicenceType.USAGE == licenceType){ 
			UsageLicenceTemplate usageLicenceTemplate = new UsageLicenceTemplate();
			usageLicenceTemplate.setAllowedUsages(1);
			 return usageLicenceTemplate;
		}
		throw new IllegalStateException("Unsupported licence type: " + licenceType);
	}
	@Ignore
	@Test
	public void testCreateProduct() throws Exception {

		Platform platform_create = createAndGetPlatform();
		EnforceableProductDto enforceableProduct = new EnforceableProductDto(
				"Parent Product"+unique);
		enforceableProduct.setDivisionId(divisionId);
		enforceableProduct.setAdminEmail(email);
		enforceableProduct.setRegistrationDefinitionType("PRODUCT_REGISTRATION");
		EnforceableProductUrlDto url = new EnforceableProductUrlDto("protocol",
				"*pc17245.uk.oup.com", "path", "query", "fragment",
				"(index-of uri '/path/index1.html false)");
		EnforceableProductUrlDto url2 = new EnforceableProductUrlDto(
				"protocol2", "*pc17245.uk.oup.com2", "path2", "query2",
				"fragment2", "(index-of uri '/path/index2.html false)");
		enforceableProduct.addEnforceableProductUrl(url);
		enforceableProduct.addEnforceableProductUrl(url2);
		enforceableProduct.setState("ACTIVE");
		enforceableProduct.setPlatformList(Arrays.asList(platform_create));
		ExternalProductId externalProductId = new ExternalProductId();
		externalProductId.setExternalId("12345"+unique);
		ExternalSystemIdType externalSystemIdType = new ExternalSystemIdType();
		externalSystemIdType.setName("isbn");
		ExternalSystem extSys = new ExternalSystem();
		extSys.setName("orcs");
		externalSystemIdType.setExternalSystem(extSys);
		externalProductId.setExternalSystemIdType(externalSystemIdType);
		List<ExternalProductId> extP= new ArrayList<ExternalProductId>();
		extP.add(externalProductId);
		enforceableProduct.setExternalIds(extP);
		LicenceTemplate licetemplate =createLicenceTemplate(LicenceType.USAGE);
		LicenceDetailDto licenceDetailDto = ErightsObjectFactory.getLicenceTemplateDto(licetemplate);
		EnforceableProductDto returnedParentProduct = erightsFacade
				.createProduct(enforceableProduct, licenceDetailDto);

		EnforceableProductDto enforceableProduct2 = new EnforceableProductDto(
				"Child Product"+unique);
		enforceableProduct2.setDivisionId(divisionId);
		enforceableProduct2.setAdminEmail(email);
		enforceableProduct2.setRegistrationDefinitionType("PRODUCT_REGISTRATION");
		EnforceableProductUrlDto url3 = new EnforceableProductUrlDto(
				"protocol3", "*pc17245.uk.oup.com3", "path3", "query3",
				"fragment3", "(index-of uri '/path/index3.html false)");
		EnforceableProductUrlDto url4 = new EnforceableProductUrlDto(
				"protocol4", "*pc17245.uk.oup.com4", "path4", "query4",
				"fragment4", "(index-of uri '/path/index4.html false)");
		EnforceableProductUrlDto url5 = new EnforceableProductUrlDto(
				"protocol5", "*pc17245.uk.oup.com5", "path5", "query5",
				"fragment5", "(index-of uri '/path/index5.html false)");
		EnforceableProductUrlDto url6 = new EnforceableProductUrlDto(
				"protocol6", "*pc17245.uk.oup.com6", "path6", "query6",
				"fragment6", "(index-of uri '/path/index6.html false)");
		enforceableProduct2.addEnforceableProductUrl(url3);
		enforceableProduct2.addEnforceableProductUrl(url4);
		enforceableProduct2.addEnforceableProductUrl(url5);
		enforceableProduct2.addEnforceableProductUrl(url6);
		List<String> produtIds=new ArrayList<String>();
		produtIds.add(returnedParentProduct.getProductId());
		enforceableProduct2.setParentIds(produtIds);
		enforceableProduct2.setState("ACTIVE");
		licetemplate =createLicenceTemplate(LicenceType.USAGE);
		licenceDetailDto = ErightsObjectFactory.getLicenceTemplateDto(licetemplate);
		EnforceableProductDto returnedChildProduct = erightsFacade
				.createProduct(enforceableProduct2, licenceDetailDto);

		assertEquals("Check correct name returned", "Parent Product"+unique,
				returnedParentProduct.getName());
		assertEquals("Check correct platform returned", platform_create.getCode(),
				returnedParentProduct.getPlatformList().get(0).getCode());
		assertEquals("Check correct name returned", "Child Product"+unique,
				returnedChildProduct.getName());
		assertEquals("Check correct parent id returned",
				enforceableProduct2.getParentIds(),
				returnedChildProduct.getParentIds());
		assertEquals("Check correct number of resource items returned", 2,
				returnedParentProduct.getUrls().size());
		assertEquals("Check correct number of resource items returned", 4,
				returnedChildProduct.getUrls().size());

		for (EnforceableProductUrlDto urlDto : returnedChildProduct.getUrls()) {
			assertTrue("Check protocol",
					urlDto.getProtocol().contains("protocol"));
			assertTrue("Check host",
					urlDto.getHost().contains("pc17245.uk.oup.com"));
			assertTrue("Check path", urlDto.getPath().contains("path"));
			assertTrue("Check query", urlDto.getQuery().contains("query"));
			assertTrue("Check fragment",
					urlDto.getFragment().contains("fragment"));
		}

		EnforceableProductDto newReturnedParentProduct = erightsFacade
				.getProduct(returnedParentProduct.getProductId());
		EnforceableProductDto newReturnedChildProduct = erightsFacade
				.getProduct(returnedChildProduct.getProductId());

		assertEquals("Check correct name returned", "Parent Product"+unique,
				newReturnedParentProduct.getName());
		assertEquals("Check correct name returned", "Child Product"+unique,
				newReturnedChildProduct.getName());
		assertEquals("Check correct parent id returned",
				enforceableProduct2.getParentIds(),
				newReturnedChildProduct.getParentIds());
		assertEquals("Check correct number of resource items returned", 2,
				newReturnedParentProduct.getUrls().size());
		assertEquals("Check correct number of resource items returned", 4,
				newReturnedChildProduct.getUrls().size());

		for (EnforceableProductUrlDto urlDto : newReturnedChildProduct
				.getUrls()) {
			assertTrue("Check protocol",
					urlDto.getProtocol().contains("protocol"));
			assertTrue("Check host",
					urlDto.getHost().contains("pc17245.uk.oup.com"));
			assertTrue("Check path", urlDto.getPath().contains("path"));
			assertTrue("Check query", urlDto.getQuery().contains("query"));
			assertTrue("Check fragment",
					urlDto.getFragment().contains("fragment"));
		}

		try {
			List<String> produtIds1=new ArrayList<String>();
			produtIds1.add(UUID.randomUUID().toString());
			enforceableProduct2.setParentIds(produtIds1);
			returnedChildProduct = erightsFacade
					.createProduct(enforceableProduct2, licenceDetailDto);
			assertTrue("Check correct exception was throwsn", false);
		} catch (ParentProductNotFoundException ppnfe) {
			// Expected
		}
		
		/*newReturnedParentProduct.setPlatformList(new ArrayList<Platform>());*/
		erightsFacade.deleteProduct(newReturnedChildProduct.getProductId());
		erightsFacade.deleteProduct(newReturnedParentProduct.getProductId());
		//erightsRestFacade.deletePlatform(platform_create) ;

	}
	@Ignore
	@Test
	public void testUpdateProduct() throws Exception {

		Platform platform_update = createAndGetPlatform();
		EnforceableProductDto enforceableProduct = new EnforceableProductDto(
				"Parent Product" + unique);
		enforceableProduct.setDivisionId(divisionId);
		enforceableProduct.setAdminEmail(email);
		enforceableProduct.setRegistrationDefinitionType("PRODUCT_REGISTRATION");
		// note expression must be valid otherwise no match may be found by any
		// resource item
		EnforceableProductUrlDto url = new EnforceableProductUrlDto("protocol",
				"*pc17245.uk.oup.com", "path", "query", "fragment",
				"(index-of uri '/path/index.html false)");
		EnforceableProductUrlDto url2 = new EnforceableProductUrlDto("",
				"*pc17245.uk.oup.com", "/" + unique, "", "", "");
		enforceableProduct.addEnforceableProductUrl(url);
		enforceableProduct.addEnforceableProductUrl(url2);
		enforceableProduct.setState("ACTIVE");
		//enforceableProduct.setPlatformList(Arrays.asList(platform_create));
		ExternalProductId externalProductId = new ExternalProductId();
		externalProductId.setExternalId("121211"+UUID.randomUUID().toString().replace("-", ""));
		ExternalSystemIdType externalSystemIdType = new ExternalSystemIdType();
		externalSystemIdType.setName("isbn");
		ExternalSystem extSys = new ExternalSystem();
		extSys.setName("orcs");
		externalSystemIdType.setExternalSystem(extSys);
		externalProductId.setExternalSystemIdType(externalSystemIdType);
		List<ExternalProductId> extP= new ArrayList<ExternalProductId>();
		extP.add(externalProductId);
		enforceableProduct.setExternalIds(extP);
		LicenceTemplate licetemplate =createLicenceTemplate(LicenceType.USAGE);
		LicenceDetailDto licenceDetailDto = ErightsObjectFactory.getLicenceTemplateDto(licetemplate);
		EnforceableProductDto returnedParentProduct = erightsFacade
				.createProduct(enforceableProduct, licenceDetailDto);

		delay(10000);
		returnedParentProduct = erightsFacade.getProduct(returnedParentProduct.getProductId());
		List<String> prodIds = erightsFacade
				.getProductIdsByUrl("http://pc17245.uk.oup.com/" + unique);

		assertEquals("Check correct product returned",
				returnedParentProduct.getProductId(), prodIds.get(0));
		assertEquals("Check correct number of products returned", 1,
				prodIds.size());

		returnedParentProduct.setName("Updated Parent Product" + unique);
		extSys.setName("konakart");
		externalSystemIdType.setExternalSystem(extSys);
		externalProductId.setExternalSystemIdType(externalSystemIdType);
		returnedParentProduct.getExternalIds().add(externalProductId);
		returnedParentProduct.setState("ACTIVE");
		returnedParentProduct.getUrls().remove(0);
		returnedParentProduct.setPlatformList(Arrays.asList(platform_update));
		assertEquals("Check correct size of urls", 1, returnedParentProduct
				.getUrls().size());

		EnforceableProductUrlDto url3 = new EnforceableProductUrlDto("",
				"*pc17245.uk.oup.com", "/" + unique, "unique=" + unique, "",
				"");

		returnedParentProduct.addEnforceableProductUrl(url3);

		erightsFacade.updateProduct(returnedParentProduct);

		delay(3000);

		prodIds = erightsFacade.getProductIdsByUrl("http://pc17245.uk.oup.com/"
				+ unique);
		assertEquals("Check correct product returned",
				returnedParentProduct.getProductId(), prodIds.get(0));
		assertEquals("Check correct number of products returned", 1,
				prodIds.size());

		prodIds = erightsFacade.getProductIdsByUrl("http://pc17245.uk.oup.com/"
				+ unique + "?unique=" + unique);
		assertEquals("Check correct product returned",
				returnedParentProduct.getProductId(), prodIds.get(0));
		assertEquals("Check correct number of products returned", 1,
				prodIds.size());
		assertEquals("Check product is updatetd with correct platform",platform_update.getCode(),
				returnedParentProduct.getPlatformList().get(0).getCode());
		returnedParentProduct = erightsFacade.getProduct(returnedParentProduct
				.getProductId());

		assertEquals("Check correct name returned", "Updated Parent Product"
				+ unique, returnedParentProduct.getName());

		assertTrue("Check product is not suspended",
				!returnedParentProduct.isSuspended());

		prodIds = erightsFacade.getProductIdsByUrl("http://pc17245.uk.oup.com/"
				+ unique);

		assertEquals("Check correct number of products returned", 1,
				prodIds.size());
		
		returnedParentProduct.setState("RETIRED");
		returnedParentProduct.setSuspended(Boolean.TRUE);
		erightsFacade.updateProduct(returnedParentProduct);

		returnedParentProduct = erightsFacade.getProduct(returnedParentProduct
				.getProductId());

		assertTrue("Check product is suspended",
				returnedParentProduct.isSuspended());

		// Check suspended product does not return in getProductsByUrl
		try {
			erightsFacade.getProductIdsByUrl("http://pc17245.uk.oup.com/"
					+ unique);
			assertTrue("Check correct exception was throwsn", false);
		} catch (ErightsException ee) {
			assertTrue("Check correct exception thrown",
					ee.getCode().equals("NO_PRODUCTS_FOUND"));
		}

		returnedParentProduct.setState("ACTIVE");
		returnedParentProduct.setSuspended(Boolean.FALSE);;
		erightsFacade.updateProduct(returnedParentProduct);

		delay(3000);

		prodIds = erightsFacade.getProductIdsByUrl("http://pc17245.uk.oup.com/"
				+ unique);

		assertEquals("Check correct number of products returned", 1,
				prodIds.size());

		returnedParentProduct = erightsFacade.getProduct(returnedParentProduct
				.getProductId());

		assertTrue("Check product is not suspended",
				!returnedParentProduct.isSuspended());

		// Test updating product with invalid parent id
		try {
			/*List<String> parentIDs= new ArrayList<String>();
			parentIDs.add(UUID.randomUUID().toString());
			parentIDs.add(UUID.randomUUID().toString());
			returnedParentProduct.setParentIds(parentIDs);			
			erightsFacade.updateProduct(returnedParentProduct);*/
			erightsFacade.addLinkedProduct(returnedParentProduct.getProductId(), UUID.randomUUID().toString());
			assertTrue("Check correct exception was throwsn", false);
		} catch (ParentProductNotFoundException ppnfe) {
			// Expected
		}

		// Test updating invalid product
		try {
			EnforceableProductDto badProduct = new EnforceableProductDto(UUID.randomUUID().toString(),
					returnedParentProduct);
			badProduct.setName("Bad Product");
			badProduct.setState("ACTIVE");
			badProduct.setDivisionId(divisionId);
			badProduct.setAdminEmail(email);
			badProduct.setRegistrationDefinitionType("PRODUCT_REGISTRATION");
			LicenceDto licenceDto2 = new LicenceDto(UUID.randomUUID().toString());
			licenceDto2.setLicenceDetail(licenceDetailDto);
			badProduct.setLicenceDetail(licenceDto2);
			erightsFacade.updateProduct(badProduct);
			assertTrue("Check correct exception was thrown", false);
		} catch (ProductNotFoundException pnfe) {
			// Expected
		}
		/*erightsFacade.deleteProduct(newReturnedChildProduct.getProductId());*/
		erightsFacade.deleteProduct(returnedParentProduct.getProductId());
		//deletePlatform(platform_update) ;

	}

	@Ignore
	@Test
	public void testDeleteProduct() throws Exception {
		EnforceableProductDto enforceableProduct = new EnforceableProductDto(
				"Parent Product");

		EnforceableProductUrlDto url = new EnforceableProductUrlDto("protocol",
				"*pc17245.uk.oup.com", "path", "query", "fragment", "");
		EnforceableProductUrlDto url2 = new EnforceableProductUrlDto(
				"protocol2", "*pc17245.uk.oup.com2", "path2", "query2",
				"fragment2", "");
		enforceableProduct.addEnforceableProductUrl(url);
		enforceableProduct.addEnforceableProductUrl(url2);
		enforceableProduct.setState(ProductState.ACTIVE.toString());
		enforceableProduct.setDivisionId(1);
		enforceableProduct.setActivationStrategy(ActivationStrategy.INSTANT.toString());
		enforceableProduct.setSuspended(false);
		enforceableProduct.setAdminEmail("test@test.com");
		enforceableProduct.setConfirmationEmailEnabled(false);
		enforceableProduct.setRegistrationDefinitionType(RegistrationDefinitionType.PRODUCT_REGISTRATION.toString());
		LicenceTemplate licetemplate =createLicenceTemplate(LicenceType.USAGE);
		LicenceDetailDto licenceDetailDto = ErightsObjectFactory.getLicenceTemplateDto(licetemplate);
		EnforceableProductDto returnedParentProduct = erightsFacade
				.createProduct(enforceableProduct, licenceDetailDto);

		EnforceableProductDto enforceableProduct2 = new EnforceableProductDto(
				"Child Product");

		EnforceableProductUrlDto url3 = new EnforceableProductUrlDto(
				"protocol3", "*pc17245.uk.oup.com3", "path3", "query3",
				"fragment3", "(index-of uri '/path/index3.html false)");
		EnforceableProductUrlDto url4 = new EnforceableProductUrlDto(
				"protocol4", "*pc17245.uk.oup.com4", "path4", "query4",
				"fragment4", "");
		EnforceableProductUrlDto url5 = new EnforceableProductUrlDto(
				"protocol5", "*pc17245.uk.oup.com5", "path5", "query5",
				"fragment5", "");
		EnforceableProductUrlDto url6 = new EnforceableProductUrlDto(
				"protocol6", "*pc17245.uk.oup.com6", "path6", "query6",
				"fragment6", "");
		enforceableProduct2.addEnforceableProductUrl(url3);
		enforceableProduct2.addEnforceableProductUrl(url4);
		enforceableProduct2.addEnforceableProductUrl(url5);
		enforceableProduct2.addEnforceableProductUrl(url6);
		List<String> parentIds = new ArrayList<String>();
		parentIds.add(returnedParentProduct.getProductId());
		enforceableProduct2.setParentIds(parentIds);
		enforceableProduct2.setState(ProductState.ACTIVE.toString());
		enforceableProduct2.setDivisionId(1);
		enforceableProduct2.setActivationStrategy(ActivationStrategy.INSTANT.toString());
		enforceableProduct2.setSuspended(false);
		enforceableProduct2.setAdminEmail("test@test.com");
		enforceableProduct2.setConfirmationEmailEnabled(false);
		enforceableProduct2.setRegistrationDefinitionType(RegistrationDefinitionType.PRODUCT_REGISTRATION.toString());
		
		EnforceableProductDto returnedChildProduct = erightsFacade
				.createProduct(enforceableProduct2, licenceDetailDto);

		returnedParentProduct = erightsFacade.getProduct(returnedParentProduct
				.getProductId());

		// Try and remove parent product. Should throw error because it has
		// child product
		try {
			erightsFacade.deleteProduct(returnedParentProduct.getProductId());
			assertTrue("Check correct exception was thrown", false);
		} catch (ChildProductFoundException cpfe) {
			// Check returned child ids
			assertEquals("Check correct number of children", 1, cpfe
					.getChildIds().size());
			assertEquals("Check correct child id",
					returnedChildProduct.getProductId(), cpfe.getChildIds()
					.get(0));
		}

		// Add a licence between user and child product, check licences then
		// delete product and check licences were cleaned up
		ConcurrentLicenceTemplate concurrentLicenceTemplate = SampleDataFactory
				.createConcurrentLicenceTemplate();
		erightsFacade.addLicense(customerDto.getUserId(),
				concurrentLicenceTemplate, Arrays
				.asList(new String[] { returnedParentProduct
						.getProductId() }), false);

		List<LicenceDto> licences = erightsFacade.getLicensesForUserProduct(
				customerDto.getUserId(),
				returnedParentProduct.getProductId());
		assertEquals("Check correct number of licences", 1, licences.size());

		// Create a couple of licences for two different products and check both
		// are returned
		RollingLicenceTemplate rollingLicenceTemplate = SampleDataFactory
				.createRollingLicenceTemplate();
		erightsFacade.addLicense(customerDto.getUserId(),
				rollingLicenceTemplate, Arrays
				.asList(new String[] { returnedChildProduct
						.getProductId() }), false);

		licences = erightsFacade
				.getLicensesForUserProduct(customerDto.getUserId(),
						returnedChildProduct.getProductId());

		assertEquals("Check correct number of licences", 1, licences.size());

		/*licences = erightsFacade.getLicensesForUser(customerDto.getUserId());
		assertEquals("Check correct number of licences", 2, licences.size());*/

		// Delete child product
		erightsFacade.deleteProduct(returnedChildProduct.getProductId());

		// Check only 1 licence returned
		/*licences = erightsFacade.getLicensesForUser(customerDto.getUserId());
		assertEquals("Check correct number of licences", 1, licences.size());*/

		// Product removed so can't get licences by user/product
		try {
			licences = erightsFacade.getLicensesForUserProduct(
					customerDto.getUserId(),
					returnedChildProduct.getProductId());
			assertTrue("Check correct exception was thrown", false);
		} catch (ProductNotFoundException pnfe) {
			// Expected
		}

		// Try and get product, should fail as we just deleted it
		try {
			erightsFacade.getProduct(returnedChildProduct.getProductId());
			assertTrue("Check correct exception was thrown", false);
		} catch (ProductNotFoundException pnfe) {
			// Expected
		}

		// Try and delete same product. Should fail because it has been deleted
		// already
		try {
			erightsFacade.deleteProduct(returnedChildProduct.getProductId());
			assertTrue("Check correct exception was thrown", false);
		} catch (ProductNotFoundException pnfe) {
			// Expected
		}
	}

	/*@Ignore
	@Test
	public void testCreateGroup() throws Exception {

		// Get group that doesnt exist
		try {
			System.out.println("----------------------------WS Password:"+System.getProperty(EACSettings.WS_SECURITY_PASSWORD));
			erightsFacade.getGroup(983642);
			assertTrue("Check correct exception was throwsn", false);
		} catch (GroupNotFoundException gnfe) {
			// Expected
		}

		GroupDto testGroup = new GroupDto("Test Group");
		testGroup.addParentGroupId(2345);

		try {
			testGroup = erightsFacade.createGroup(testGroup);
			assertTrue("Check that the correct exception was thrown", false);
		} catch (ParentGroupNotFoundException pgnfe) {
			// Expected
		}

		// Check we can get the group created in setUp
		GroupDto newChildGroup = erightsFacade.getGroup(childGroup
				.getErightsId());

		assertEquals("Check group name is same", "Child Group",
				newChildGroup.getName());
	}*/

	@Ignore
	@Test
	public void testUpdateLicence() throws Exception {

		// Create a product to add licences against
		EnforceableProductDto aProduct = new EnforceableProductDto("A Product");

		EnforceableProductUrlDto url = new EnforceableProductUrlDto("protocol",
				"*pc17245.uk.oup.com", "path", "query", "fragment", null);
		EnforceableProductUrlDto url2 = new EnforceableProductUrlDto(
				"protocol2", "*pc17245.uk.oup.com2", "path2", "query2",
				"fragment2", "(index-of uri '/path/index2.html false)");
		aProduct.addEnforceableProductUrl(url);
		aProduct.addEnforceableProductUrl(url2);
		LicenceTemplate licetemplate =createLicenceTemplate(LicenceType.USAGE);
		LicenceDetailDto licenceDetailDto = ErightsObjectFactory.getLicenceTemplateDto(licetemplate);
		EnforceableProductDto aSavedProduct = erightsFacade
				.createProduct(aProduct, licenceDetailDto);

		EnforceableProductDto enforceableProduct2 = new EnforceableProductDto(
				"Child Product");

		EnforceableProductUrlDto url3 = new EnforceableProductUrlDto(
				"protocol3", "*pc17245.uk.oup.com3", "path3", "query3",
				"fragment3", "(index-of uri '/path/index3.html false)");
		EnforceableProductUrlDto url4 = new EnforceableProductUrlDto(
				"protocol4", "*pc17245.uk.oup.com4", "path4", "query4",
				"fragment4", "(index-of uri '/path/index4.html false)");
		EnforceableProductUrlDto url5 = new EnforceableProductUrlDto(
				"protocol5", "*pc17245.uk.oup.com5", "path5", "query5",
				"fragment5", "(index-of uri '/path/index5.html false)");
		EnforceableProductUrlDto url6 = new EnforceableProductUrlDto(
				"protocol6", "*pc17245.uk.oup.com6", "path6", "query6",
				"fragment6", "(index-of uri '/path/index6.html false)");
		enforceableProduct2.addEnforceableProductUrl(url3);
		enforceableProduct2.addEnforceableProductUrl(url4);
		enforceableProduct2.addEnforceableProductUrl(url5);
		enforceableProduct2.addEnforceableProductUrl(url6);
		List<String> parentIds = new ArrayList<String>();
		parentIds.add(aSavedProduct.getProductId());
		enforceableProduct2.setParentIds(parentIds);

		EnforceableProductDto returnedChildProduct = erightsFacade
				.createProduct(enforceableProduct2, licenceDetailDto);

		// Add some different licences usage/rolling/concurrent

		// Create a couple of licences for two different products and check both
		// are returned
		ConcurrentLicenceTemplate concurrentLicenceTemplate = SampleDataFactory
				.createConcurrentLicenceTemplate();
		String concurrentLicenceId = erightsFacade.addLicense(
				customerDto.getUserId(), concurrentLicenceTemplate,
				Arrays.asList(new String[] { aSavedProduct.getProductId() }),
				false);

		List<LicenceDto> licences = erightsFacade
				.getLicensesForUser(customerDto.getUserId(),concurrentLicenceId);

		assertEquals("Check correct number of licences for user", 1,
				licences.size());

		// Create a couple of licences for two different products and check both
		// are returned
		RollingLicenceTemplate rollingLicenceTemplate = SampleDataFactory
				.createRollingLicenceTemplate();
		String rollingLicenceId = erightsFacade.addLicense(
				customerDto.getUserId(), rollingLicenceTemplate,
				Arrays.asList(new String[] { aSavedProduct.getProductId() }),
				false);

		licences = erightsFacade.getLicensesForUserProduct(customerDto.getUserId(),aSavedProduct.getProductId());
		assertEquals("Check correct number of licences for user", 2,
				licences.size());

		// Create a couple of licences for two different products and check both
		// are returned
		UsageLicenceTemplate usageLicenceTemplate = SampleDataFactory
				.createUsageLicenceTemplate();
		String usageLicenceId = erightsFacade.addLicense(
				customerDto.getUserId(), usageLicenceTemplate,
				Arrays.asList(new String[] { aSavedProduct.getProductId() }),
				true);

		licences = erightsFacade.getLicensesForUserProduct(customerDto.getUserId(),aSavedProduct.getProductId());
		assertEquals("Check correct number of licences for user", 3,
				licences.size());

		for (LicenceDto licence : licences) {
			if (licence.getLicenseId().equals(usageLicenceId)) {
				assertTrue("Check licence is enabled", licence.isEnabled());
			} else {
				assertTrue("Check licence is disabled", !licence.isEnabled());
			}
		}

		// Expected values
		LocalDate standardConcurrentStartDate = null;
		LocalDate standardConcurrentEndDate = null;
		int standardConcurrentTotalCon = 5;
		int standardConcurrentUserCon = 10;

		LocalDate rollingEndDate = new LocalDate();

		LocalDate usageEndDate = null;
		int usageQuantityLimit = 25;

		// Update fields of licence and update
		for (LicenceDto licence : licences) {

			if (licence.getLicenseId().equals(concurrentLicenceId)) {

				StandardConcurrentLicenceDetailDto detail = (StandardConcurrentLicenceDetailDto) licence
						.getLicenceDetail();

				assertEquals("Check correct total concurrency", 1,
						detail.getTotalConcurrency());
				assertEquals("Check correct user concurrency", 1,
						detail.getUserConcurrency());

				assertEquals("Check correct start date",
						concurrentLicenceTemplate.getStartDate(),
						licence.getStartDate());
				assertEquals("Check correct end date",
						concurrentLicenceTemplate.getEndDate(),
						licence.getEndDate());
				assertEquals("Check correct product id",
						aSavedProduct.getProductId(), licence.getProductIds()
						.get(0));

				assertTrue("Check not active", !licence.isActive());
				assertTrue("Check not enabled", !licence.isEnabled());
				assertTrue("Check not expired", !licence.isExpired());

				// expires on end date if provided
				LocalDate expectedExpiry = concurrentLicenceTemplate
						.getEndDate();

				LOG.debug("Concurrent licence before update!");
				LOG.debug("Current date is: " + new LocalDate());
				LOG.debug("Start date is: " + licence.getStartDate());
				LOG.debug("End date is: " + licence.getEndDate());
				LOG.debug("Erights calculated date and time is: "
						+ licence.getExpiryDateAndTime());
				LOG.debug("Locally calculated date and time is: "
						+ expectedExpiry);

				LOG.debug("TimeZone is : "
						+ new DateTime().getZone().toString());

				assertEquals("Check expiry date and time", expectedExpiry,
						licence.getExpiryDateAndTime().toLocalDate());

				licence.setStartDate(standardConcurrentStartDate = new LocalDate()
				.plusDays(5));
				licence.setEndDate(standardConcurrentEndDate = new LocalDate()
				.plusDays(10));

				licence.getProductIds()
				.add(returnedChildProduct.getProductId());

				detail.setTotalConcurrency(standardConcurrentTotalCon);
				detail.setUserConcurrency(standardConcurrentUserCon);

				licence.setEnabled(true);

			} else if (licence.getLicenseId().equals(rollingLicenceId)) {
				RollingLicenceDetailDto detail = (RollingLicenceDetailDto) licence
						.getLicenceDetail();

				assertEquals("Check correct begin on",
						RollingBeginOn.CREATION.ordinal(), detail.getBeginOn()
						.ordinal());
				assertEquals("Check correct unit type",
						RollingUnitType.DAY.ordinal(), detail.getUnitType()
						.ordinal());

				assertEquals("Check correct start date",
						rollingLicenceTemplate.getStartDate(),
						licence.getStartDate());
				assertEquals("Check correct end date",
						rollingLicenceTemplate.getEndDate(),
						licence.getEndDate());
				assertEquals("Check correct product id",
						aSavedProduct.getProductId(), licence.getProductIds()
						.get(0));

				if (WEB_SERVICE_URL.contains("literatumonline")) {
					// Can only test first use when testing against real
					// erights. Fake erights always returns used licences.
					assertEquals("Check first use", null, detail.getFirstUse());
				}
				assertTrue("Check not active", !licence.isActive());
				assertTrue("Check not enabled", !licence.isEnabled());
				assertTrue("Check not expired", !licence.isExpired());

				// 5 rolling days takes you past 2 day validity period so
				// expires on end date.
				LocalDate expectedExpiry = usageLicenceTemplate.getEndDate();

				LOG.debug("Rolling licence before update!");
				LOG.debug("Current date is: " + new LocalDate());
				LOG.debug("Start date is: " + licence.getStartDate());
				LOG.debug("End date is: " + licence.getEndDate());
				LOG.debug("Erights calculated date and time is: "
						+ licence.getExpiryDateAndTime());
				LOG.debug("Locally calculated date and time is: "
						+ expectedExpiry);

				LOG.debug("TimeZone is : "
						+ new DateTime().getZone().toString());

				assertEquals("Check expiry date and time", expectedExpiry,
						licence.getExpiryDateAndTime().toLocalDate());

				licence.setStartDate(null);

				// Should expire if date set to now
				licence.setEndDate(rollingEndDate.plusDays(7));

				detail.setBeginOn(RollingBeginOn.CREATION);
				detail.setUnitType(RollingUnitType.YEAR);

				licence.setEnabled(true);
			} else {
				UsageLicenceDetailDto detail = (UsageLicenceDetailDto) licence
						.getLicenceDetail();

				assertEquals("Check correct quantity limit",
						usageLicenceTemplate.getAllowedUsages(),
						detail.getAllowedUsages());

				assertEquals("Check correct start date",
						usageLicenceTemplate.getStartDate(),
						licence.getStartDate());
				assertEquals("Check correct end date",
						usageLicenceTemplate.getEndDate(), licence.getEndDate());
				assertEquals("Check correct product id",
						aSavedProduct.getProductId(), licence.getProductIds()
						.get(0));

				assertTrue("Check active", licence.isActive());
				assertTrue("Check enabled", licence.isEnabled());
				assertTrue("Check not expired", !licence.isExpired());

				// expires on end date if provided
				LocalDate expectedExpiry = usageLicenceTemplate.getEndDate();

				LOG.debug("Usage licence before update!");
				LOG.debug("Current date is: " + new LocalDate());
				LOG.debug("Start date is: " + licence.getStartDate());
				LOG.debug("End date is: " + licence.getEndDate());
				LOG.debug("Erights calculated date and time is: "
						+ licence.getExpiryDateAndTime());
				LOG.debug("Locally calculated date and time is: "
						+ expectedExpiry);

				LOG.debug("TimeZone is : "
						+ new DateTime().getZone().toString());

				assertEquals("Check expiry date and time", expectedExpiry,
						licence.getExpiryDateAndTime().toLocalDate());

				licence.setStartDate(null);
				licence.setEndDate(usageEndDate = new LocalDate().plusDays(5));

				detail.setAllowedUsages(usageQuantityLimit);

				licence.setEnabled(false);
			}

			erightsFacade.updateLicence(customerDto.getUserId(), licence);
		}

		licences = erightsFacade.getLicensesForUser(customerDto.getUserId(),aSavedProduct.getProductId());
		assertEquals("Check correct number of licences for user", 3,
				licences.size());

		LicenceDto licenceForTest = null;

		// Get licence and check update
		for (LicenceDto licence : licences) {
			if (licence.getLicenseId().equals(concurrentLicenceId)) {

				StandardConcurrentLicenceDetailDto detail = (StandardConcurrentLicenceDetailDto) licence
						.getLicenceDetail();

				assertEquals("Check correct total concurrency",
						standardConcurrentTotalCon,
						detail.getTotalConcurrency());
				assertEquals("Check correct user concurrency",
						standardConcurrentUserCon, detail.getUserConcurrency());

				assertEquals("Check correct start date",
						standardConcurrentStartDate, licence.getStartDate());
				assertEquals("Check correct end date",
						standardConcurrentEndDate, licence.getEndDate());
				assertTrue("Check correct product id", licence.getProductIds()
						.contains(aSavedProduct.getProductId()));
				assertTrue("Check correct product id", licence.getProductIds()
						.contains(returnedChildProduct.getProductId()));

				LOG.debug("Concurrent licence after update!");
				LOG.debug("Current date is: " + new LocalDate());
				LOG.debug("Start date is: " + licence.getStartDate());
				LOG.debug("End date is: " + licence.getEndDate());
				LOG.debug("Erights calculated date and time is: "
						+ licence.getExpiryDateAndTime());

				LOG.debug("TimeZone is : "
						+ new DateTime().getZone().toString());

				assertEquals("Check expiry date and time",
						standardConcurrentEndDate, licence
						.getExpiryDateAndTime().toLocalDate());

				// Shouldnt be active as start date is time in future.
				assertTrue("Check active", !licence.isActive());
				assertTrue("Check enabled", licence.isEnabled());
				assertTrue("Check not expired", !licence.isExpired());

				// Use this licence in further tests
				licenceForTest = licence;

			} else if (licence.getLicenseId().equals(rollingLicenceId)) {
				RollingLicenceDetailDto detail = (RollingLicenceDetailDto) licence
						.getLicenceDetail();

				assertEquals("Check correct begin on",
						RollingBeginOn.CREATION.ordinal(), detail.getBeginOn()
						.ordinal());
				assertEquals("Check correct unit type",
						RollingUnitType.YEAR.ordinal(), detail.getUnitType()
						.ordinal());

				assertEquals("Check correct start date", null,
						licence.getStartDate());
				assertEquals("Check correct end date",
						rollingEndDate.plusDays(7), licence.getEndDate());
				assertEquals("Check correct product id",
						aSavedProduct.getProductId(), licence.getProductIds()
						.get(0));

				LocalDate expectedExpiry = new LocalDate().plusDays(7);

				LOG.debug("Rolling licence after update!");
				LOG.debug("Current date is: " + new LocalDate());
				LOG.debug("Start date is: " + licence.getStartDate());
				LOG.debug("End date is: " + licence.getEndDate());
				LOG.debug("Erights calculated date and time is: "
						+ licence.getExpiryDateAndTime());
				LOG.debug("Locally calculated date and time is: "
						+ expectedExpiry);

				LOG.debug("TimeZone is : "
						+ new DateTime().getZone().toString());

				// TODO fixme
				// assertEquals("Check expiry date and time", expectedExpiry,
				// licence.getExpiryDateAndTime().toLocalDate());
				// assertTrue("Check not active", !licence.isActive());
				// assertTrue("Check expired", licence.isExpired());

				assertTrue("Check enabled", licence.isEnabled());
			} else {
				UsageLicenceDetailDto detail = (UsageLicenceDetailDto) licence
						.getLicenceDetail();

				assertEquals("Check correct quantity limit",
						usageQuantityLimit, detail.getAllowedUsages());

				assertEquals("Check correct start date", null,
						licence.getStartDate());
				assertEquals("Check correct end date", usageEndDate,
						licence.getEndDate());
				assertEquals("Check correct product id",
						aSavedProduct.getProductId(), licence.getProductIds()
						.get(0));

				LOG.debug("Usage licence after update!");
				LOG.debug("Current date is: " + new LocalDate());
				LOG.debug("Start date is: " + licence.getStartDate());
				LOG.debug("End date is: " + licence.getEndDate());
				LOG.debug("Erights calculated date and time is: "
						+ licence.getExpiryDateAndTime());

				LOG.debug("TimeZone is : "
						+ new DateTime().getZone().toString());

				assertEquals("Check expiry date and time", usageEndDate,
						licence.getExpiryDateAndTime().toLocalDate());

				assertTrue("Check not active", !licence.isActive());
				assertTrue("Check not enabled", !licence.isEnabled());
				assertTrue("Check not expired", !licence.isExpired());
			}
		}

		String licenceId = licenceForTest.getLicenseId();

		// Check licence not found exception
		try {
			licenceForTest.setLicenseId(UUID.randomUUID().toString());
			erightsFacade.updateLicence(customerDto.getUserId(),
					licenceForTest);
			assertTrue("Check correct exception was thrown", false);
		} catch (LicenseNotFoundException lnfe) {
			// Expected
			licenceForTest.setLicenseId(licenceId);
		}

		// Check product not found exception
		try {
			licenceForTest.getProductIds().add(UUID.randomUUID().toString());
			erightsFacade.updateLicence(customerDto.getUserId(),
					licenceForTest);
			assertTrue("Check correct exception was thrown", false);
		} catch (ProductNotFoundException pnfe) {
			// Expected
			licenceForTest.getProductIds().remove(new Integer(9874343));
		}

		// Test update with empty product list
		try {
			licenceForTest.getProductIds().clear();
			erightsFacade.updateLicence(customerDto.getUserId(),
					licenceForTest);
			assertTrue("Check correct exception was thrown", false);
		} catch (ErightsException ee) {
			// Expected
		}

		// Check user not found exception
		try {
			erightsFacade.updateLicence(INVALID_USER_ID, licenceForTest);
			assertTrue("Check correct exception was thrown", false);
		} catch (UserNotFoundException unfe) {
			// Expected
		}
	}

	/**
	 * Delay to allow for any differences between EAC & Atypon time
	 * 
	 * @param delay
	 *            TODO
	 */
	private void delay(long delay) {
		try {
			Thread.sleep(delay);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@Test
	public void testRemoveLicence() throws Exception {

		EnforceableProductDto enforceableProduct = new EnforceableProductDto(
				"A Product"+unique);
		enforceableProduct.setDivisionId(divisionId);
		enforceableProduct.setAdminEmail(email);
		enforceableProduct.setRegistrationDefinitionType("PRODUCT_REGISTRATION");
		EnforceableProductUrlDto url = new EnforceableProductUrlDto("protocol",
				"*pc17245.uk.oup.com", "path", "query", "fragment",
				"(index-of uri '/path/index.html false)");
		EnforceableProductUrlDto url2 = new EnforceableProductUrlDto(
				"protocol2", "*pc17245.uk.oup.com2", "path2", "query2",
				"fragment2", "(index-of uri '/path/index2.html false)");
		enforceableProduct.addEnforceableProductUrl(url);
		enforceableProduct.addEnforceableProductUrl(url2);
		enforceableProduct.setState("ACTIVE");
		LicenceTemplate licetemplate =createLicenceTemplate(LicenceType.USAGE);
		LicenceDetailDto licenceDetailDto = ErightsObjectFactory.getLicenceTemplateDto(licetemplate);
		EnforceableProductDto returnedParentProduct = erightsFacade
				.createProduct(enforceableProduct, licenceDetailDto);

		// Add active licence
		ConcurrentLicenceTemplate concurrentLicenceTemplate = SampleDataFactory
				.createConcurrentLicenceTemplate();
		String erightsLicenceId = erightsFacade.addLicense(customerDto
				.getUserId(), concurrentLicenceTemplate,
				Arrays.asList(new String[] { returnedParentProduct
						.getProductId() }), true);

		List<LicenceDto> licences = erightsFacade
				.getLicensesForUser(customerDto.getUserId(),erightsLicenceId);

		// Check licence on user
		assertEquals("Check user has licence", erightsLicenceId, licences
				.get(0).getLicenseId());

		// Remove licence
		erightsFacade.removeLicence(customerDto.getUserId(),
				erightsLicenceId);

		// Check user has no licence
		try {
			licences = erightsFacade.getLicensesForUser(customerDto
					.getUserId(),erightsLicenceId);
			assertTrue("Check correct exception was thrown", false);
		} catch (LicenseNotFoundException lnfe) {
			// Expected
		}

		/*// Check user not found
		try {
			// Remove licence
			erightsFacade.removeLicence(INVALID_USER_ID, erightsLicenceId);
			assertTrue("Check correct exception was thrown", false);
		} catch (UserNotFoundException e) {
			// Expected
		}*/

		// Check licence not found
		try {
			// Remove licence
			erightsFacade.removeLicence(customerDto.getUserId(), UUID.randomUUID().toString());
			assertTrue("Check correct exception was thrown", false);
		} catch (LicenseNotFoundException lnfe) {

		}
	}

	/*@Ignore
	@Test
	public void testUpdateGroup() throws Exception {
		// Check we can get the group created in setUp
		GroupDto newChildGroup = erightsFacade.getGroup(childGroup
				.getErightsId());

		assertEquals("Check group name is same", "Child Group",
				newChildGroup.getName());

		// Check invalid parent group
		GroupDto badParentGroup = new GroupDto(3245345, "Bad Parent");
		try {
			// GroupDto newGroup = new
			// GroupDto(childGroup.getGroup(),Arrays.asList(new
			// Integer[]{3245345}));
			newChildGroup.addParentGroupId(badParentGroup.getErightsId());
			erightsFacade.updateGroup(newChildGroup);
			assertTrue("Check correct exception was thrown", false);
		} catch (ParentGroupNotFoundException pgnfe) {
			// Expected
		}

		newChildGroup.getParentIds().remove(badParentGroup.getErightsId());
		erightsFacade.updateGroup(newChildGroup);
		assertEquals("Check group name is same", "Child Group",
				newChildGroup.getName());

		newChildGroup.setName("Child Group Updated");
		newChildGroup.getParentIds().clear();

		assertTrue("Check 1 parent group",
				childGroup.getParentIds().size() == 1);

		erightsFacade.updateGroup(newChildGroup);
		GroupDto childGroupUpdated = erightsFacade.getGroup(newChildGroup
				.getErightsId());

		assertEquals("Check group name is same", "Child Group Updated",
				childGroupUpdated.getName());
		assertTrue("Check no groups after update", childGroupUpdated
				.getParentIds().size() == 0);

		// Check we can get the group created in setUp
		newChildGroup = erightsFacade.getGroup(childGroup.getErightsId());

		// Check no group to update
		try {
			newChildGroup.setErightsId(998247);
			erightsFacade.updateGroup(newChildGroup);
			assertTrue("Check correct exception was thrown", false);
		} catch (GroupNotFoundException gnfe) {
			// Expected
		}

		// Check general error
		try {
			newChildGroup.setErightsId(null);
			erightsFacade.updateGroup(newChildGroup);
			assertTrue("Check correct exception was thrown", false);
		} catch (ErightsException ee) {
			// Expected
		}
	}

	@Ignore
	@Test
	public void testGetGroupUsers() throws Exception {

		// Create a couple more users to be used by different groups
		Customer customer1 = new Customer();
		customer1.setUsername("user1-" + email);
		customer1.setPassword(new Password("password", false));
		customer1.setEmailAddress(email);

		Customer customer2 = new Customer();
		customer2.setUsername("user2-" + email);
		customer2.setPassword(new Password("password", false));
		customer2.setEmailAddress(email);

		Customer customer3 = new Customer();
		customer3.setUsername("user3-" + email);
		customer3.setPassword(new Password("password", false));
		customer3.setEmailAddress(email);

		// Add users to groups, 2 users for group 1, 1 user for group 2, 1 user
		// for parent group.
		GroupDto group1 = new GroupDto("Group1");
		group1.addParentGroupId(parentGroup.getErightsId());
		group1 = erightsFacade.createGroup(group1);

		GroupDto group2 = new GroupDto("Group2");
		group2 = erightsFacade.createGroup(group2);

		// Create a group with no users
		GroupDto group3 = new GroupDto("Group3 has no users");
		group3 = erightsFacade.createGroup(group3);

		List<String> parentGroupIds = new ArrayList<String>();
		parentGroupIds.add(String.valueOf(group1.getErightsId()));
		parentGroupIds.add(String.valueOf(group2.getErightsId()));

		CustomerDto customerDto1 = new CustomerDto(customer1, parentGroupIds);
		customerDto1 = erightsFacade.createUserAccount(customerDto1);

		parentGroupIds = new ArrayList<String>();
		parentGroupIds.add(String.valueOf(group1.getErightsId()));

		CustomerDto customerDto2 = new CustomerDto(customer2, parentGroupIds);
		customerDto2 = erightsFacade.createUserAccount(customerDto2);

		// Add customer 3 just to parent group
		parentGroupIds = new ArrayList<String>();
		parentGroupIds.add(String.valueOf(parentGroup.getErightsId()));

		CustomerDto customerDto3 = new CustomerDto(customer3, parentGroupIds);
		customerDto3 = erightsFacade.createUserAccount(customerDto3);

		// Parent group is parent of group 1, which has 2 users. Parent group
		// has 1 user. Parent group is also parent of child group which has 1
		// user
		// get group users with indirect parents should return 4 customers
		List<Integer> customerIds = erightsFacade.getGroupUsers(
				parentGroup.getErightsId(), true);

		assertEquals("Check correct number of customers returned for group", 4,
				customerIds.size());
		assertTrue("Check correct user returned",
				customerIds.contains(customerDto1.getUserId()));
		assertTrue("Check correct user returned",
				customerIds.contains(customerDto2.getUserId()));
		assertTrue("Check correct user returned",
				customerIds.contains(customerDto3.getUserId()));

		// Only 1 user returned for parent group when indirect parent flag is
		// false
		customerIds = erightsFacade.getGroupUsers(parentGroup.getErightsId(),
				false);

		assertEquals("Check correct number of customers returned for group", 1,
				customerIds.size());
		assertTrue("Check correct user returned",
				customerIds.contains(customerDto3.getUserId()));

		// Get group 1 users, should be 2
		customerIds = erightsFacade.getGroupUsers(group1.getErightsId(), false);

		assertEquals("Check correct number of customers returned for group", 2,
				customerIds.size());
		assertTrue("Check correct user returned",
				customerIds.contains(customerDto1.getUserId()));
		assertTrue("Check correct user returned",
				customerIds.contains(customerDto2.getUserId()));

		// Get group 2 users, should be 1
		customerIds = erightsFacade.getGroupUsers(group2.getErightsId(), false);

		assertEquals("Check correct number of customers returned for group", 1,
				customerIds.size());
		assertTrue("Check correct user returned",
				customerIds.contains(customerDto1.getUserId()));

		// Get users for group with no users, throws exception
		try {
			erightsFacade.getGroupUsers(group3.getErightsId(), false);
			assertTrue("Check correct exception was thrown", false);
		} catch (UserNotFoundException unfe) {
			// Expected
		}

		// Delete groups from user 1
		customerDto1 = erightsFacade
				.getUserAccount(customerDto1.getUserId());

		customerDto1.getGroupIds().clear();

		Customer customerToUpdate = new Customer();
		customerToUpdate.setId(customerDto1.getUserId());
		customerToUpdate.setEmailAddress(customerDto1.getEmailAddress());
		customerToUpdate.setUsername(customerDto1.getUsername());
		customerToUpdate.setSuspended(true);
		customerToUpdate.setPassword(new Password("password"+unique, false));
		CustomerDto customerToUpdateDto = new CustomerDto(customerToUpdate);

		erightsFacade.updateUserAccount(customerToUpdateDto);

		// Get group 2 after removing customer 1. Should have no users and throw
		// exception
		try {
			erightsFacade.getGroupUsers(group2.getErightsId(), false);
			assertTrue("Check correct exception was thrown", false);
		} catch (UserNotFoundException unfe) {
			// Expected
		}

		// Get customer id's for group 1 (should only have customer 2 now)
		customerIds = erightsFacade.getGroupUsers(group1.getErightsId(), false);
		assertEquals("Check correct number of customers returned for group 1",
				1, customerIds.size());

		try {
			erightsFacade.getGroupUsers(983427, false);
			assertTrue("Check correct exception was thrown", false);
		} catch (GroupNotFoundException gnfe) {
			// Expected
		}
	}

	@Ignore
	@Test
	public void testDeleteGroup() throws Exception {
		// Check we can get the group created in setUp
		GroupDto newChildGroup = erightsFacade.getGroup(childGroup
				.getErightsId());

		assertTrue("Check 1 parent group",
				newChildGroup.getParentIds().size() == 1);

		int parentGroupId = newChildGroup.getParentIds().get(0);

		// Check parent group cannot be deleted while it has a child
		try {
			erightsFacade.deleteGroup(parentGroupId);
			assertTrue("Check correct exception was thrown", false);
		} catch (Exception e) {
			// Expected
		}

		// Delete child group
		erightsFacade.deleteGroup(newChildGroup.getErightsId());

		// Try and delete a group that doesn't exist
		try {
			erightsFacade.deleteGroup(newChildGroup.getErightsId());
			assertTrue("Check correct exception was thrown", false);
		} catch (Exception e) {
			// Expected
		}
	}
*/

	@Test
	public void testDeactivateLicence() throws Exception {
		EnforceableProductDto enforceableProduct = new EnforceableProductDto(
				"A Product"+unique);
		enforceableProduct.setDivisionId(divisionId);
		enforceableProduct.setAdminEmail(email);
		enforceableProduct.setRegistrationDefinitionType("PRODUCT_REGISTRATION");
		EnforceableProductUrlDto url = new EnforceableProductUrlDto("protocol",
				"*pc17245.uk.oup.com", "path", "query", "fragment",
				"(index-of uri '/path/index.html false)");
		EnforceableProductUrlDto url2 = new EnforceableProductUrlDto(
				"protocol2", "*pc17245.uk.oup.com2", "path2", "query2",
				"fragment2", "(index-of uri '/path/index2.html false)");
		enforceableProduct.addEnforceableProductUrl(url);
		enforceableProduct.addEnforceableProductUrl(url2);
		enforceableProduct.setState("ACTIVE");
		LicenceTemplate licetemplate =createLicenceTemplate(LicenceType.USAGE);
		LicenceDetailDto licenceDetailDto = ErightsObjectFactory.getLicenceTemplateDto(licetemplate);
		EnforceableProductDto returnedParentProduct = erightsFacade
				.createProduct(enforceableProduct, licenceDetailDto);

		// Add active licence
		ConcurrentLicenceTemplate concurrentLicenceTemplate = SampleDataFactory
				.createConcurrentLicenceTemplate();

		String erightsLicenceId = erightsFacade.addLicense(customerDto
				.getUserId(), concurrentLicenceTemplate,
				Arrays.asList(new String[] { returnedParentProduct
						.getProductId() }), true);

		List<LicenceDto> licences = erightsFacade
				.getLicensesForUser(customerDto.getUserId(),erightsLicenceId);


		// Get licence
		assertEquals("Check correct number of licences", 1, licences.size());

		// Check activation flag
		assertTrue("Check licence is active", licences.get(0).isActive());

		// Deactivate licence
		erightsFacade.deactivateLicense(customerDto.getUserId(),
				erightsLicenceId);

		// Get licences
		licences = erightsFacade.getLicensesForUser(customerDto.getUserId(),erightsLicenceId);

		// Check activation flag. Should be deactivated
		assertTrue("Check licence is deactivated", !licences.get(0).isActive());

		// Deactivate already deactivated licence
		erightsFacade.deactivateLicense(customerDto.getUserId(),
				erightsLicenceId);

		try {
			// Deactivate licence
			erightsFacade.deactivateLicense(customerDto.getUserId(),
					INVALID_LICENCE_ID);
			assertTrue("Check correct exception was thrown", false);
		} catch (LicenseNotFoundException lnfe) {
			// Expected
		}

		try {
			// Deactivate licence
			erightsFacade.deactivateLicense(INVALID_USER_ID, erightsLicenceId);
			assertTrue("Check correct exception was thrown", false);
		} catch (UserNotFoundException unfe) {
			// Expected
		}
	}

	@Ignore
	@Test
	public void getLicencesByUserAndUserProduct() throws Exception {

		EnforceableProductDto aProduct = new EnforceableProductDto("A Product");

		EnforceableProductUrlDto url = new EnforceableProductUrlDto("protocol",
				"*pc17245.uk.oup.com", "path", "query", "fragment", null);
		EnforceableProductUrlDto url2 = new EnforceableProductUrlDto(
				"protocol2", "*pc17245.uk.oup.com2", "path2", "query2",
				"fragment2", null);
		aProduct.addEnforceableProductUrl(url);
		aProduct.addEnforceableProductUrl(url2);
		LicenceTemplate licetemplate =createLicenceTemplate(LicenceType.USAGE);
		LicenceDetailDto licenceDetailDto = ErightsObjectFactory.getLicenceTemplateDto(licetemplate);
		EnforceableProductDto aSavedProduct = erightsFacade
				.createProduct(aProduct, licenceDetailDto);

		EnforceableProductDto bProduct = new EnforceableProductDto("A Product");

		EnforceableProductUrlDto url3 = new EnforceableProductUrlDto(
				"protocol", "*pc17245.uk.oup.com", "path", "query", "fragment",
				null);
		EnforceableProductUrlDto url4 = new EnforceableProductUrlDto(
				"protocol2", "*pc17245.uk.oup.com2", "path2", "query2",
				"fragment2", null);
		bProduct.addEnforceableProductUrl(url3);
		bProduct.addEnforceableProductUrl(url4);

		EnforceableProductDto bSavedProduct = erightsFacade
				.createProduct(bProduct, licenceDetailDto);

		try {
			erightsFacade.getLicensesForUser(customerDto.getUserId(),bSavedProduct.getProductId());
			assertTrue("Check correct exception was thrown", false);
		} catch (LicenseNotFoundException lnfe) {
			// Expected
		}

		// Create a couple of licences for two different products and check both
		// are returned
		StandardLicenceTemplate standardLicenceTemplate = SampleDataFactory
				.createStandardLicenceTemplateValidForOneDay();
		String licenceId = erightsFacade.addLicense(
				customerDto.getUserId(), standardLicenceTemplate,
				Arrays.asList(new String[] { aSavedProduct.getProductId() }),
				false);

		List<LicenceDto> licences = erightsFacade
				.getLicensesForUser(customerDto.getUserId(),licenceId);

		assertEquals("Check correct number of licences for user", 1,
				licences.size());
		assertEquals("Check correct licence returned", licenceId,
				licences.get(0).getLicenseId());

		String licenceId2 = erightsFacade.addLicense(
				customerDto.getUserId(), standardLicenceTemplate,
				Arrays.asList(new String[] { bSavedProduct.getProductId() }),
				true);

		licences = erightsFacade.getLicensesForUserProduct(customerDto.getUserId(),bSavedProduct.getProductId());

		assertEquals("Check correct number of licences for user", 2,
				licences.size());
		// Cant guarentee the id is higher for licence 2
		// Collections.sort(licences, new Comparator<LicenceDto>() {
		//
		// @Override
		// public int compare(LicenceDto o1, LicenceDto o2) {
		// return o1.getErightsId().compareTo(o2.getErightsId());
		// }});

		boolean found = false;
		for (LicenceDto aLicence : licences) {
			if (aLicence.getLicenseId().equals(licenceId2)) {
				found = true;
				break;
			}
		}

		assertTrue("Check correct licence returned", found);

		// Check getting licences by user/product
		// by aSavedProduct
		licences = erightsFacade.getLicensesForUserProduct(
				customerDto.getUserId(), aSavedProduct.getProductId());
		assertEquals("Check correct number of licences for user", 1,
				licences.size());
		assertEquals("Check correct licence returned", licenceId,
				licences.get(0).getLicenseId());

		// by bSavedProduct
		licences = erightsFacade.getLicensesForUserProduct(
				customerDto.getUserId(), bSavedProduct.getProductId());
		assertEquals("Check correct number of licences for user", 1,
				licences.size());
		assertEquals("Check correct licence returned", licenceId2, licences
				.get(0).getLicenseId());

		// Try and get licences for non existent user
		try {
			erightsFacade.getLicensesForUser(UUID.randomUUID().toString(),"asdasd");
			assertTrue("Check correct exception was thrown", false);
		} catch (UserNotFoundException unfe) {
			// Expected
		}

		// Try and get licences for non existent user and product
		try {
			erightsFacade.getLicensesForUserProduct(UUID.randomUUID().toString(), UUID.randomUUID().toString());
			assertTrue("Check correct exception was thrown", false);
		} catch (UserNotFoundException unfe) {
			// Expected
		}

		// Try and get licences for valid user and non existent product
		try {
			erightsFacade.getLicensesForUserProduct(customerDto.getUserId(),
					UUID.randomUUID().toString());
			assertTrue("Check correct exception was thrown", false);
		} catch (ProductNotFoundException pnfe) {
			// Expected
		}
	}

	/**
	 * 
	 * @throws Exception
	 *             the exception
	 */


	@Test
	public final void testLogoutInvalidSession() throws Exception {
		try {
			// erightsFacade.logout("randomsessionkeythatdoesntexist"); //
			// internal error
			erightsFacade.logout("999999999999999999999999999999999999999999");
		} catch (ErightsException e) {
			assertEquals("Check correct exception thrown",
					AuthenticationResponseSTATUS.SESSION_NOT_FOUND.value(), e.getCode());
		}
	}

	/**
	 * 
	 * @throws Exception
	 *             the exception
	 */


	@Test
	public final void testAuthenticateUser() throws Exception {
		try {
			erightsFacade.authenticateUser(new LoginPasswordCredentialDto(
					"administrator", "Oxford123"));
			assertTrue("Check the correct exception was thrown", false);
		} catch (InvalidCredentialsException e) {
			// all good
			assertTrue("Check correct exception was thrown", true);
		}
		try {
			erightsFacade.authenticateUser(new LoginPasswordCredentialDto(
					"idontexist", "atthisplace"));
			assertTrue("Check the correct exception was thrown", false);
		} catch (InvalidCredentialsException e) {
			// all good
			assertTrue("Check correct exception was thrown", true);
		}
	}

	/**
	 * Test erights connection for admin. Should not be able to login as admin.
	 * 
	 * @throws Exception
	 *             the exception
	 */


	@Test
	public final void testErightsConnectionForAdmin() throws Exception {
		// System.setProperty(EACSettings.ERIGHTS_WEBSERVICE_URL,
		// "http://localhost:8088/mockoupRightAccessServiceSoapBinding?wsdl");

		// When using IP address, certificate checks must be reduced (if
		// System.setProperty(EACSettings.WS_HTTPS_TRUST_ALL_HOSTS, "true");
		// System.setProperty(EACSettings.WS_HTTPS_TRUST_ALL_CERTIFICATES,
		// "true");

		// System.setProperty(EACSettings.ERIGHTS_WEBSERVICE_URL,
		// "https://oup-eac.literatumonline.com/oup/ws/OUPRightAccessService?wsdl");

		// loop to hit the server 10 times (exposes connection pool issues)
		for (int i = 0; i < 10; i++) {
			try {
				erightsFacade.authenticateUser(new LoginPasswordCredentialDto(
						"administrator", "Oxford123"));
				assertTrue("Check that correct exception was thrown", false);
			} catch (InvalidCredentialsException ice) {
				assertTrue("Check that correct exception was thrown", true);
			}
		}
	}

	//  @Test
	// public void testGetProductsByURL() throws Exception {
	// System.setProperty(EACSettings.ERIGHTS_WEBSERVICE_URL,
	// "http://localhost:8088/mockoupRightAccessServiceSoapBinding?wsdl");

	// System.setProperty(EACSettings.ERIGHTS_WEBSERVICE_URL,
	// "https://129.41.23.206/oup/ws/OUPRightAccessService?wsdl");
	// When using IP address, certificate checks must be reduced (if
	// System.setProperty(EACSettings.WS_HTTPS_TRUST_ALL_HOSTS, "true");
	// System.setProperty(EACSettings.WS_HTTPS_TRUST_ALL_CERTIFICATES,
	// "true");

	// System.setProperty(EACSettings.ERIGHTS_WEBSERVICE_URL,
	// "https://oup-eac.literatumonline.com/oup/ws/OUPRightAccessService?wsdl");
	// System.setProperty(EACSettings.ERIGHTS_WEBSERVICE_URL,
	// "https://129.41.23.206/oup/ws/OUPRightAccessService?wsdl");
	//
	// erightsFacade.getProductIdsByUrl("http://localhost/eacSampleSite/book-details/spm-success-addmath.html?null");
	// assertTrue("Check that correct exception was thrown", false);
	// }

	/**
	 * Get products by url. Check when urls are known to exist and when they are
	 * known to fail.
	 * 
	 * PRODUCT_URL should match a product in erights for the test to pass.
	 * 
	 * @throws Exception
	 *             the exception
	 */

	@Ignore	
	@Test
	public final void testGetProductsByURL() throws Exception {
		List<String> ids = erightsFacade.getProductIdsByUrl(PRODUCT_URL);
		assertNotNull("Check some ids were returned", ids);
		assertEquals("Check correct number of ids was returned", 2, ids.size()); //Since size changes each time testcase runs
		ERIGHTS_PRODUCT_ID = UUID.randomUUID().toString();
		assertTrue("Check correct id returned for url",
				ids.contains(ERIGHTS_PRODUCT_ID));
		assertTrue("Check correct id returned for url",
				ids.contains(ERIGHTS_ALL_PRODUCTS_ID));  //Sicne ID changes every time

		try {
			erightsFacade
			.getProductIdsByUrl("thisurldoesnotexistmostlikely.com");
			assertTrue("Check the correct exception was thrown", false);
		} catch (ErightsException e) {
			// all good
			assertTrue("Check correct exception was thrown", true);
		}
	}

	/**
	 * Test activating licences. This is really setting "enabled" on the
	 * license.
	 */


	@Test
	public final void testActivateLicense() throws Exception {

		EnforceableProductDto enforceableProduct = new EnforceableProductDto(
				"A Product"+unique);
		enforceableProduct.setDivisionId(divisionId);
		enforceableProduct.setAdminEmail(email);
		enforceableProduct.setRegistrationDefinitionType("PRODUCT_REGISTRATION");
		EnforceableProductUrlDto url = new EnforceableProductUrlDto("protocol",
				"*pc17245.uk.oup.com", "path", "query", "fragment", null);
		EnforceableProductUrlDto url2 = new EnforceableProductUrlDto(
				"protocol2", "*pc17245.uk.oup.com2", "path2", "query2",
				"fragment2", null);
		enforceableProduct.addEnforceableProductUrl(url);
		enforceableProduct.addEnforceableProductUrl(url2);
		enforceableProduct.setState("ACTIVE");

		LicenceTemplate licetemplate =createLicenceTemplate(LicenceType.USAGE);
		LicenceDetailDto licenceDetailDto = ErightsObjectFactory.getLicenceTemplateDto(licetemplate);
		EnforceableProductDto returnedParentProduct = erightsFacade
				.createProduct(enforceableProduct, licenceDetailDto);

		// Add inactive licence
		ConcurrentLicenceTemplate concurrentLicenceTemplate = SampleDataFactory
				.createConcurrentLicenceTemplate();
		String erightsLicenceId = erightsFacade.addLicense(customerDto
				.getUserId(), concurrentLicenceTemplate,
				Arrays.asList(new String[] { returnedParentProduct
						.getProductId() }), false);

		List<LicenceDto> licences = erightsFacade
				.getLicensesForUser(customerDto.getUserId(),erightsLicenceId);

		assertTrue("Check licence is inactive", !licences.get(0).isActive());
		assertEquals("Check correct licence", erightsLicenceId, licences.get(0)
				.getLicenseId());

		// Activate licence
		erightsFacade.activateLicense(customerDto.getUserId(),
				erightsLicenceId);

		// Re-check licence is now active
		licences = erightsFacade.getLicensesForUser(customerDto.getUserId(),erightsLicenceId);

		assertTrue("Check licence is inactive", licences.get(0).isActive());
		assertEquals("Check correct licence", erightsLicenceId, licences.get(0)
				.getLicenseId());

		try {
			erightsFacade.activateLicense(id, INVALID_LICENCE_ID);
			assertTrue("Check the correct exception was thrown", false);
		} catch (LicenseNotFoundException e) {
			// Expected
		}

		try {
			erightsFacade.activateLicense(INVALID_USER_ID, erightsLicenceId);
			assertTrue("Check the correct exception was thrown", false);
		} catch (UserNotFoundException e) {
			// Expected
		}

	}


	@Test
	public final void testRollingFromCreationExpiryDateUsingVariousLicences()
			throws Exception {

		EnforceableProductDto enforceableProduct = new EnforceableProductDto(
				"A Product"+unique);
		enforceableProduct.setDivisionId(divisionId);
		enforceableProduct.setAdminEmail(email);
		enforceableProduct.setRegistrationDefinitionType("PRODUCT_REGISTRATION");
		EnforceableProductUrlDto url = new EnforceableProductUrlDto("protocol",
				"*pc17245.uk.oup.com", "path", "query", "fragment",
				"(index-of uri '/path/index1.html false)");
		enforceableProduct.addEnforceableProductUrl(url);
		enforceableProduct.setState("ACTIVE");
		LicenceTemplate licetemplate =createLicenceTemplate(LicenceType.USAGE);
		LicenceDetailDto licenceDetailDto = ErightsObjectFactory.getLicenceTemplateDto(licetemplate);
		EnforceableProductDto returnedEnforceableProduct = erightsFacade
				.createProduct(enforceableProduct, licenceDetailDto);

		String licence1Id = erightsFacade
				.addLicense(
						customer.getId(),
						SampleDataFactory
						.createRollingLicenceTemplateExpiresFiveDaysFromCreation(),
						Arrays.asList(new String[] { returnedEnforceableProduct
								.getProductId() }), true);

		List<LicenceDto> licences = erightsFacade.getLicensesForUserProduct(
				customerDto.getUserId(),
				returnedEnforceableProduct.getProductId());

		LicenceDto licence = findLicence(licences, licence1Id);

		// Should expire in 5 days
		assertEquals("Check correct expiry", new LocalDateTime().plusDays(5)
				.toLocalDate().toDateMidnight(), licence.getExpiryDateAndTime()
				.toLocalDate().toDateMidnight());
		assertTrue("Check not expired", !licence.isExpired());

		// **************

		String licence2Id = erightsFacade
				.addLicense(
						customer.getId(),
						SampleDataFactory
						.createRollingLicenceTemplateExpiresFiveDaysFromStartDate(),
						Arrays.asList(new String[] { returnedEnforceableProduct
								.getProductId() }), true);

		licences = erightsFacade.getLicensesForUserProduct(
				customerDto.getUserId(),
				returnedEnforceableProduct.getProductId());

		licence = findLicence(licences, licence2Id);
		// After atypon bug fixes now, expiry should be creation Date + time
		// period i.e todays date + 5
		assertEquals("Check correct expiry", new LocalDateTime().plusDays(5)
				.toLocalDate().toDateMidnight(), licence.getExpiryDateAndTime()
				.toLocalDate().toDateMidnight());
		assertTrue("Check not expired", !licence.isExpired());

		// **************

		String licence3Id = erightsFacade
				.addLicense(
						customer.getId(),
						SampleDataFactory
						.createRollingLicenceTemplateExpiresFiveDaysFromCreationWithMinusStartDate(),
						Arrays.asList(new String[] { returnedEnforceableProduct
								.getProductId() }), true);

		licences = erightsFacade.getLicensesForUserProduct(
				customerDto.getUserId(),
				returnedEnforceableProduct.getProductId());

		licence = findLicence(licences, licence3Id);

		// Should expire in 5 days
		assertEquals("Check correct expiry", new LocalDateTime().plusDays(5)
				.toLocalDate().toDateMidnight(), licence.getExpiryDateAndTime()
				.toLocalDate().toDateMidnight());
		assertTrue("Check not expired", !licence.isExpired());

		// **************

		String licence4Id = erightsFacade.addLicense(customer.getId(),
				SampleDataFactory
				.createRollingLicenceTemplateExpiresAtEndDate(), Arrays
				.asList(new String[] { returnedEnforceableProduct
						.getProductId() }), true);

		licences = erightsFacade.getLicensesForUserProduct(
				customerDto.getUserId(),
				returnedEnforceableProduct.getProductId());

		licence = findLicence(licences, licence4Id);

		// Should expire in 5 days
		assertEquals("Check correct expiry", SampleDataFactory
				.createRollingLicenceTemplateExpiresAtEndDate().getEndDate()
				.toDateMidnight(), licence.getExpiryDateAndTime().toLocalDate()
				.toDateMidnight());
		assertTrue("Check not expired", !licence.isExpired());

		// **************

		String licence5Id = erightsFacade.addLicense(customer.getId(),
				SampleDataFactory
				.createRollingLicenceTemplateNoStartOrEndDate(), Arrays
				.asList(new String[] { returnedEnforceableProduct
						.getProductId() }), true);

		licences = erightsFacade.getLicensesForUserProduct(
				customerDto.getUserId(),
				returnedEnforceableProduct.getProductId());

		licence = findLicence(licences, licence5Id);

		// Should expire in 5 days
		assertEquals("Check correct expiry", new LocalDateTime().plusDays(15)
				.toLocalDate().toDateMidnight(), licence.getExpiryDateAndTime()
				.toLocalDate().toDateMidnight());
		assertTrue("Check not expired", !licence.isExpired());

		// **************

		String licence6Id = erightsFacade
				.addLicense(
						customer.getId(),
						SampleDataFactory
						.createRollingLicenceTemplateWithPastStartDateAndNoEndDate(),
						Arrays.asList(new String[] { returnedEnforceableProduct
								.getProductId() }), true);

		licences = erightsFacade.getLicensesForUserProduct(
				customerDto.getUserId(),
				returnedEnforceableProduct.getProductId());

		licence = findLicence(licences, licence6Id);

		// Should expire in 5 days
		assertEquals("Check correct expiry", new LocalDateTime().plusDays(15)
				.toLocalDate().toDateMidnight(), licence.getExpiryDateAndTime()
				.toLocalDate().toDateMidnight());
		assertTrue("Check not expired", !licence.isExpired());

		// **************

		String licence7Id = erightsFacade
				.addLicense(
						customer.getId(),
						SampleDataFactory
						.createRollingLicenceTemplateWithFutureStartDateAndNoEndDate(),
						Arrays.asList(new String[] { returnedEnforceableProduct
								.getProductId() }), true);

		licences = erightsFacade.getLicensesForUserProduct(
				customerDto.getUserId(),
				returnedEnforceableProduct.getProductId());

		licence = findLicence(licences, licence7Id);

		// After atypon bug fixes now, expiry should be creation Date + time
		// period i.e todays date + 15
		assertEquals("Check correct expiry", new LocalDateTime().plusDays(15)
				.toLocalDate().toDateMidnight(), licence.getExpiryDateAndTime()
				.toLocalDate().toDateMidnight());
		assertTrue("Check not expired", !licence.isExpired());

		// **************

		String licence8Id = erightsFacade
				.addLicense(
						customer.getId(),
						SampleDataFactory
						.createRollingLicenceTemplateNoStartDateReturningEndDate(),
						Arrays.asList(new String[] { returnedEnforceableProduct
								.getProductId() }), true);

		licences = erightsFacade.getLicensesForUserProduct(
				customerDto.getUserId(),
				returnedEnforceableProduct.getProductId());

		licence = findLicence(licences, licence8Id);

		// Should expire in 5 days
		assertEquals("Check correct expiry", SampleDataFactory
				.createRollingLicenceTemplateNoStartDateReturningEndDate()
				.getEndDate().toDateMidnight(), licence.getExpiryDateAndTime()
				.toLocalDate().toDateMidnight());
		assertTrue("Check not expired", !licence.isExpired());

		// **************

		String licence9Id = erightsFacade
				.addLicense(
						customer.getId(),
						SampleDataFactory
						.createRollingLicenceTemplatePastStartDateReturningEndDate(),
						Arrays.asList(new String[] { returnedEnforceableProduct
								.getProductId() }), true);

		licences = erightsFacade.getLicensesForUserProduct(
				customerDto.getUserId(),
				returnedEnforceableProduct.getProductId());

		licence = findLicence(licences, licence9Id);

		// Should expire in 5 days
		assertEquals("Check correct expiry", SampleDataFactory
				.createRollingLicenceTemplatePastStartDateReturningEndDate()
				.getEndDate().toDateMidnight(), licence.getExpiryDateAndTime()
				.toLocalDate().toDateMidnight());
		assertTrue("Check not expired", !licence.isExpired());

	}

	@Ignore
	@Test
	public final void testRollingFromFirstUseExpiryDateUsingVariousLicences()
			throws Exception {

		String protectedResourcePath = "/protected/" + unique
				+ "/testProductForFirstUse.jsp";
		String protectedUrl = "http://dev.eac.uk.oup.com/eacSampleSite"
				+ protectedResourcePath;

		EnforceableProductDto enforceableProduct = new EnforceableProductDto(
				"Test product for first use");

		EnforceableProductUrlDto url = new EnforceableProductUrlDto(null, null,
				"*" + protectedResourcePath + "*", null, null, null);
		enforceableProduct.addEnforceableProductUrl(url);

		LicenceTemplate licetemplate =createLicenceTemplate(LicenceType.USAGE);
		LicenceDetailDto licenceDetailDto = ErightsObjectFactory.getLicenceTemplateDto(licetemplate);
		EnforceableProductDto returnedParentProduct = erightsFacade
				.createProduct(enforceableProduct, licenceDetailDto);

		String licence1Id = erightsFacade.addLicense(customer.getId(),
				SampleDataFactory
				.createRollingLicenceTemplateNullExpiryUntilFirstUse(),
				Arrays.asList(new String[] { returnedParentProduct
						.getProductId() }), true);

		List<LicenceDto> licences = erightsFacade.getLicensesForUserProduct(
				customerDto.getUserId(),
				returnedParentProduct.getProductId());

		LicenceDto licence = findLicence(licences, licence1Id);

		// Should expire in 5 days
		assertEquals("Check correct expiry", null,
				licence.getExpiryDateAndTime());
		assertTrue("Check not expired", !licence.isExpired());

		erightsFacade.deactivateLicense(customerDto.getUserId(), licence1Id);

		// **************

		String licence2Id = erightsFacade
				.addLicense(
						customer.getId(),
						SampleDataFactory
						.createRollingLicenceTemplateNullExpiryUntilFirstUseWithEndDate(),
						Arrays.asList(new String[] { returnedParentProduct
								.getProductId() }), true);

		licences = erightsFacade.getLicensesForUserProduct(
				customerDto.getUserId(),
				returnedParentProduct.getProductId());

		licence = findLicence(licences, licence2Id);

		// Should expire in 5 days
		assertEquals("Check correct expiry", null,
				licence.getExpiryDateAndTime());
		assertTrue("Check not expired", !licence.isExpired());

		erightsFacade.deactivateLicense(customerDto.getUserId(), licence2Id);

		// **************

		String licence3Id = erightsFacade
				.addLicense(
						customer.getId(),
						SampleDataFactory
						.createRollingLicenceTemplateNullExpiryUntilFirstUseWithEndDateAndPastStartDateExpiryIsNull(),
						Arrays.asList(new String[] { returnedParentProduct
								.getProductId() }), true);

		licences = erightsFacade.getLicensesForUserProduct(
				customerDto.getUserId(),
				returnedParentProduct.getProductId());

		licence = findLicence(licences, licence3Id);

		// Should expire in 5 days
		assertEquals("Check correct expiry", null,
				licence.getExpiryDateAndTime());
		assertTrue("Check not expired", !licence.isExpired());

		erightsFacade.deactivateLicense(customerDto.getUserId(), licence3Id);

		// **************

		String licence4Id = erightsFacade
				.addLicense(
						customer.getId(),
						SampleDataFactory
						.createRollingLicenceTemplateNullExpiryUntilFirstUseWithEndDateAndPastStartDateExpiryIsEndDate(),
						Arrays.asList(new String[] { returnedParentProduct
								.getProductId() }), true);

		licences = erightsFacade.getLicensesForUserProduct(
				customerDto.getUserId(),
				returnedParentProduct.getProductId());

		licence = findLicence(licences, licence4Id);

		// Should expire in 4 days
		assertEquals(
				"Check correct expiry",
				SampleDataFactory
				.createRollingLicenceTemplateNullExpiryUntilFirstUseWithEndDateAndPastStartDateExpiryIsEndDate()
				.getEndDate().toDateMidnight(), licence
				.getExpiryDateAndTime().toLocalDate().toDateMidnight());
		assertTrue("Check not expired", !licence.isExpired());

		erightsFacade.deactivateLicense(customerDto.getUserId(), licence4Id);

		// **************

		String licence5Id = erightsFacade.addLicense(customer.getId(),
				SampleDataFactory
				.createRollingLicenceTemplateNullExpiryUntilFirstUse(),
				Arrays.asList(new String[] { returnedParentProduct
						.getProductId() }), true);

		licences = erightsFacade.getLicensesForUserProduct(
				customerDto.getUserId(),
				returnedParentProduct.getProductId());

		licence = findLicence(licences, licence5Id);

		assertEquals("Check no first use", null,
				((RollingLicenceDetailDto) licence.getLicenceDetail())
				.getFirstUse());

		useLicense(customer, licence5Id, protectedUrl);

		licences = erightsFacade.getLicensesForUserProduct(
				customerDto.getUserId(),
				returnedParentProduct.getProductId());

		licence = findLicence(licences, licence5Id);

		assertTrue("Check no first use",
				((RollingLicenceDetailDto) licence.getLicenceDetail())
				.getFirstUse() != null);

		// Should expire in 5 days
		assertEquals("Check correct expiry", new LocalDate().toDateMidnight()
				.plusDays(5), licence.getExpiryDateAndTime().toLocalDate()
				.toDateMidnight());
		assertTrue("Check not expired", !licence.isExpired());

		erightsFacade.deactivateLicense(customerDto.getUserId(), licence5Id);

		// **************

		String licence6Id = erightsFacade
				.addLicense(
						customer.getId(),
						SampleDataFactory
						.createRollingLicenceTemplateNullExpiryUntilFirstUseWithEndDateAndPastStartDateExpiryIsNull(),
						Arrays.asList(new String[] { returnedParentProduct
								.getProductId() }), true);

		licences = erightsFacade.getLicensesForUserProduct(
				customerDto.getUserId(),
				returnedParentProduct.getProductId());

		licence = findLicence(licences, licence6Id);

		assertEquals("Check no first use", null,
				((RollingLicenceDetailDto) licence.getLicenceDetail())
				.getFirstUse());

		useLicense(customer, licence6Id, protectedUrl);

		licences = erightsFacade.getLicensesForUserProduct(
				customerDto.getUserId(),
				returnedParentProduct.getProductId());

		licence = findLicence(licences, licence6Id);

		assertTrue("Check no first use",
				((RollingLicenceDetailDto) licence.getLicenceDetail())
				.getFirstUse() != null);

		// Should expire in 5 days
		assertEquals("Check correct expiry", new LocalDate().toDateMidnight()
				.plusDays(5), licence.getExpiryDateAndTime().toLocalDate()
				.toDateMidnight());
		assertTrue("Check not expired", !licence.isExpired());

		erightsFacade.deactivateLicense(customerDto.getUserId(), licence6Id);

		// **************

		String licence7Id = erightsFacade
				.addLicense(
						customer.getId(),
						SampleDataFactory
						.createRollingLicenceTemplateNullExpiryUntilFirstUseWithEndDateAndPastStartDateExpiryIsEndDate(),
						Arrays.asList(new String[] { returnedParentProduct
								.getProductId() }), true);

		licences = erightsFacade.getLicensesForUserProduct(
				customerDto.getUserId(),
				returnedParentProduct.getProductId());

		licence = findLicence(licences, licence7Id);

		assertEquals("Check no first use", null,
				((RollingLicenceDetailDto) licence.getLicenceDetail())
				.getFirstUse());

		useLicense(customer, licence7Id, protectedUrl);

		licences = erightsFacade.getLicensesForUserProduct(
				customerDto.getUserId(),
				returnedParentProduct.getProductId());

		licence = findLicence(licences, licence7Id);

		assertTrue("Check no first use",
				((RollingLicenceDetailDto) licence.getLicenceDetail())
				.getFirstUse() != null);

		// Should expire in 5 days
		assertEquals(
				"Check correct expiry",
				SampleDataFactory
				.createRollingLicenceTemplateNullExpiryUntilFirstUseWithEndDateAndPastStartDateExpiryIsEndDate()
				.getEndDate().toDateMidnight(), licence
				.getExpiryDateAndTime().toLocalDate().toDateMidnight());
		assertTrue("Check not expired", !licence.isExpired());

		erightsFacade.deactivateLicense(customerDto.getUserId(), licence7Id);

		// **************

		String licence8Id = erightsFacade
				.addLicense(
						customer.getId(),
						SampleDataFactory
						.createRollingLicenceTemplateNullExpiryUntilFirstUseWithEndDateAndFutureStartDateExpiryIsEndDate(),
						Arrays.asList(new String[] { returnedParentProduct
								.getProductId() }), true);

		licences = erightsFacade.getLicensesForUserProduct(
				customerDto.getUserId(),
				returnedParentProduct.getProductId());

		licence = findLicence(licences, licence8Id);

		// Should expire in 5 days
		assertEquals(
				"Check correct expiry",
				SampleDataFactory
				.createRollingLicenceTemplateNullExpiryUntilFirstUseWithEndDateAndFutureStartDateExpiryIsEndDate()
				.getEndDate().toDateMidnight(), licence
				.getExpiryDateAndTime().toLocalDate().toDateMidnight());
		assertTrue("Check not expired", !licence.isExpired());

		// **************

		String licence9Id = erightsFacade
				.addLicense(
						customer.getId(),
						SampleDataFactory
						.createRollingLicenceTemplateNullExpiryUntilFirstUseWithEndDateAndFutureStartDateExpiryIsNull(),
						Arrays.asList(new String[] { returnedParentProduct
								.getProductId() }), true);

		licences = erightsFacade.getLicensesForUserProduct(
				customerDto.getUserId(),
				returnedParentProduct.getProductId());

		licence = findLicence(licences, licence9Id);

		// Should expire in 5 days
		assertEquals("Check correct expiry", null,
				licence.getExpiryDateAndTime());
		assertTrue("Check not expired", !licence.isExpired());

	}

	private LicenceDto findLicence(List<LicenceDto> licences, String erightsId) {
		for (LicenceDto licence : licences) {
			if (licence.getLicenseId().equals(erightsId)) {
				return licence;
			}
		}
		return null;
	}

	// @Test - Enable if required.
	public final void testRegisterLogoutLoginAndAccessContent()
			throws Exception {

		try {
			// authenticate using user we created
			AuthenticationResponseDto authDto = erightsFacade
					.authenticateUser(new LoginPasswordCredentialDto("email",
							"password"));
			assertEquals("Check user created id is same as authenticated id",
					id, authDto.getUserIds().get(0));
			// check the user id is returned correctly for the session key
			List<String> ids = erightsFacade.getCustomerIdsFromSession(authDto
					.getSessionKey());
			assertEquals("Check correct customer id returned for session key",
					id, ids.get(0));

			// Get product ids and licenses. Current user should not have any
			// yet
			ids = erightsFacade.getProductIdsByUrl(PRODUCT_URL);
			// List<Integer> licences =
			// erightsFacade.getLicensesForUserProduct(id,
			// ids.get(0));
			// assertEquals("Check no licenses were returned for this user", 0,
			// licences.size());

			// try and add a disabled licence to a user. This should work
			// create standard licence
			StandardLicenceTemplate standardLicenceTemplate = SampleDataFactory
					.createStandardLicenceTemplateValidForOneDay();
			String licenceId = erightsFacade.addLicense(id,
					standardLicenceTemplate, Arrays
					.asList(new String[] { ERIGHTS_PRODUCT_ID }), false);
			// int expectedLicenceSize = 1;
			// Check that one licence has been added
			// licences = erightsFacade.getLicensesForUserProduct(id,
			// ids.get(0));
			// Collections.sort(licences);
			// assertEquals("Check one valid licence is available for this user",
			// expectedLicenceSize, licences.size());
			// assertEquals("Check the correct licence id was returned",
			// licenceId, licences.get(0));

			testConnectToProtectedResourceWithoutLicence(authDto);

			// Now activate the licence
			erightsFacade.activateLicense(id, licenceId);

			// attempt to re-access content, should be possible now with licence
			testConnectToProtectedResourceWithLicence(authDto, PRODUCT_URL,
					HttpStatus.SC_OK);

			// Check we can log this session out
			erightsFacade.logout(authDto.getSessionKey());
			try {
				// check session not found after logout
				ids = erightsFacade.getCustomerIdsFromSession(authDto
						.getSessionKey());
				assertEquals(
						"Check correct customer id returned for session key",
						id, ids.get(0));
				assertTrue("Check an exception was thrown", false);
			} catch (ErightsException e) {
				assertEquals("Check correct error was thrown by erights",
						UserAccountResponseSTATUS.SESSION_NOT_FOUND
						.value(), e.getCode());
			}

			// relogin user
			authDto = erightsFacade
					.authenticateUser(new LoginPasswordCredentialDto("email",
							"password"));
			assertEquals("Check user created id is same as authenticated id",
					id, authDto.getUserIds().get(0));

			// Check correct number of licences
			// licences = erightsFacade.getLicensesForUserProduct(id,
			// ids.get(0));
			// Collections.sort(licences);
			// assertEquals("Check one valid licence is available for this user",
			// expectedLicenceSize, licences.size());
			// assertEquals("Check the correct licence id was returned",
			// licenceId, licences.get(0));

			// Thread.sleep(1000 * 60 * 5);
			// attempt to re-access content, should still be possible now with
			// licence
			testConnectToProtectedResourceWithLicence(authDto, PRODUCT_URL,
					HttpStatus.SC_OK);

			// Check we can log this session out
			erightsFacade.logout(authDto.getSessionKey());

		} catch (Exception e) {
			LOG.error("Check an exception was not thrown", e);
			assertTrue("Check an exception was not thrown", false);

		}
	}

	private void testConnectToProtectedResourceWithoutLicence(
			AuthenticationResponseDto authDto) throws Exception {

		HttpClient httpClient = new HttpClient();
		GetMethod method = new GetMethod(PRODUCT_URL);
		method.setFollowRedirects(false);
		method.setRequestHeader("Cookie",
				EAC_COOKIE_NAME + "=" + authDto.getSessionKey());

		// Execute the method.
		int statusCode = httpClient.executeMethod(method);
		LOG.info("Method Status Code: " + method.getStatusLine());
		assertEquals("Check redirect response",
				HttpStatus.SC_MOVED_TEMPORARILY, statusCode);

		Header locationHeader = method.getResponseHeader("Location");
		LOG.info("Location returned from request: " + locationHeader.getValue());
		assertTrue(
				"Check redirect contains "
						+ ErightsDenyReason.DENY_ALL_LICENSE_DENIED.name(),
						locationHeader
						.getValue()
						.contains(
								String.valueOf(ErightsDenyReason.DENY_ALL_LICENSE_DENIED
										.getReasonCode())));

		method.releaseConnection();
	}

	private void testConnectToProtectedResourceWithLicence(
			AuthenticationResponseDto authDto, String protectedResourceUrl,
			int expectedStatusCode) throws Exception {

		HttpClient httpClient = new HttpClient();
		GetMethod method = new GetMethod(protectedResourceUrl);
		method.setFollowRedirects(false);
		method.setRequestHeader("Cookie",
				EAC_COOKIE_NAME + "=" + authDto.getSessionKey());

		// Execute the method.
		int statusCode = httpClient.executeMethod(method);
		LOG.info("Method Status Code: " + method.getStatusLine());
		// assertEquals("Check redirect response", expectedStatusCode,
		// statusCode);

		// IOUtils.copy(method.getResponseBodyAsStream(),System.out);

		method.releaseConnection();
	}

	private void useLicense(Customer customer, String licenceId,
			String protectedResourceUrl) throws Exception {

		if (WEB_SERVICE_URL.contains("localhost")) {
			URL url = new URL(
					"http://localhost:9002/rest/useLicence?customerId="
							+ customer.getId() + "&licenceId="
							+ licenceId);

			URLConnection con = url.openConnection();

			BufferedInputStream bis = new BufferedInputStream(
					con.getInputStream(), 128);

			ByteArrayBuffer barb = new ByteArrayBuffer(128);
			// read the bytes one by one and append it into the ByteArrayBuffer
			// barb
			int current = 0;
			while ((current = bis.read()) != -1) {
				barb.write((byte) current);
			}

			String dateString = barb.toString();

			System.out.println("First use recorded as :" + dateString);
		} else {
			AuthenticationResponseDto authDto = erightsFacade
					.authenticateUser(new LoginPasswordCredentialDto(customer
							.getUsername(), customer.getPassword()));
			testConnectToProtectedResourceWithLicence(authDto,
					protectedResourceUrl, HttpStatus.SC_NOT_FOUND);
			erightsFacade.logout(authDto.getSessionKey());
		}
	}

	private static class SampleDataFactory {

		private static final int TIME_PERIOD = 5;
		private static final int ALLOWED_USAGES = 5;

		/**
		 * @return a StandardLicenceTemplate
		 */
		public static StandardLicenceTemplate createStandardLicenceTemplate() {
			String id = UUID.randomUUID().toString();
			StandardLicenceTemplate standardLicenceTemplate = new StandardLicenceTemplate();
			standardLicenceTemplate.setId(id);
			standardLicenceTemplate.setStartDate(new LocalDate());
			standardLicenceTemplate.setEndDate(new LocalDate());
			standardLicenceTemplate.setVersion(0);
			return standardLicenceTemplate;
		}

		/**
		 * @return a StandardLicenceTemplate
		 */
		public static StandardLicenceTemplate createStandardLicenceTemplateValidForOneDay() {
			String id = UUID.randomUUID().toString();
			StandardLicenceTemplate standardLicenceTemplate = new StandardLicenceTemplate();
			standardLicenceTemplate.setId(id);
			standardLicenceTemplate.setStartDate(new LocalDate());
			standardLicenceTemplate.setEndDate(new LocalDate().plusDays(1));
			standardLicenceTemplate.setVersion(0);
			return standardLicenceTemplate;
		}

		/**
		 * @return a ConcurrentLicenceTemplate
		 */
		public static ConcurrentLicenceTemplate createConcurrentLicenceTemplate() {
			String id = UUID.randomUUID().toString();
			ConcurrentLicenceTemplate concurrentLicenceTemplate = new ConcurrentLicenceTemplate();
			concurrentLicenceTemplate.setId(id);
			concurrentLicenceTemplate.setStartDate(new LocalDate());
			concurrentLicenceTemplate.setEndDate(new LocalDate().plusDays(2));
			concurrentLicenceTemplate.setVersion(0);
			concurrentLicenceTemplate.setTotalConcurrency(1);
			concurrentLicenceTemplate.setUserConcurrency(1);
			return concurrentLicenceTemplate;
		}

		/**
		 * Template for licences due to expire in 2 days.
		 * 
		 * @return a RollingLicenceTemplate
		 */
		public static RollingLicenceTemplate createRollingLicenceTemplate() {
			String id = UUID.randomUUID().toString();
			RollingLicenceTemplate rollingLicenceTemplate = new RollingLicenceTemplate();
			rollingLicenceTemplate.setId(id);
			rollingLicenceTemplate.setStartDate(new LocalDate());
			rollingLicenceTemplate.setEndDate(new LocalDate().plusDays(2));
			rollingLicenceTemplate.setVersion(0);
			rollingLicenceTemplate.setTimePeriod(TIME_PERIOD);
			rollingLicenceTemplate
			.setBeginOn(RollingLicenceTemplate.RollingBeginOn.CREATION);
			rollingLicenceTemplate
			.setUnitType(RollingLicenceTemplate.RollingUnitType.DAY);
			return rollingLicenceTemplate;
		}

		/**
		 * Template for licences that will expire 5 days after creation.
		 * 
		 * End date is set for now + 10 days. Rolling for 5 days.
		 * 
		 * @return a RollingLicenceTemplate
		 */
		public static RollingLicenceTemplate createRollingLicenceTemplateExpiresFiveDaysFromCreation() {
			String id = UUID.randomUUID().toString();
			RollingLicenceTemplate rollingLicenceTemplate = new RollingLicenceTemplate();
			rollingLicenceTemplate.setId(id);
			rollingLicenceTemplate.setStartDate(null);
			rollingLicenceTemplate.setEndDate(new LocalDate().plusDays(10));
			rollingLicenceTemplate.setVersion(0);
			rollingLicenceTemplate.setTimePeriod(TIME_PERIOD);
			rollingLicenceTemplate
			.setBeginOn(RollingLicenceTemplate.RollingBeginOn.CREATION);
			rollingLicenceTemplate
			.setUnitType(RollingLicenceTemplate.RollingUnitType.DAY);
			return rollingLicenceTemplate;
		}

		/**
		 * Template for licences that will expire 5 days after start date (7
		 * days after creation).
		 * 
		 * Start date is set for now + 2, End date is set for now + 10 days.
		 * Rolling for 5 days.
		 * 
		 * @return a RollingLicenceTemplate
		 */
		public static RollingLicenceTemplate createRollingLicenceTemplateExpiresFiveDaysFromStartDate() {
			String id = UUID.randomUUID().toString();
			RollingLicenceTemplate rollingLicenceTemplate = new RollingLicenceTemplate();
			rollingLicenceTemplate.setId(id);
			rollingLicenceTemplate.setStartDate(new LocalDate().plusDays(2));
			rollingLicenceTemplate.setEndDate(new LocalDate().plusDays(10));
			rollingLicenceTemplate.setVersion(0);
			rollingLicenceTemplate.setTimePeriod(TIME_PERIOD);
			rollingLicenceTemplate
			.setBeginOn(RollingLicenceTemplate.RollingBeginOn.CREATION);
			rollingLicenceTemplate
			.setUnitType(RollingLicenceTemplate.RollingUnitType.DAY);
			return rollingLicenceTemplate;
		}

		/**
		 * Template for licences that will expire 5 days after creation.
		 * 
		 * Start date is set for now - 2, End date is set for now + 10 days.
		 * Rolling for 5 days.
		 * 
		 * @return a RollingLicenceTemplate
		 */
		public static RollingLicenceTemplate createRollingLicenceTemplateExpiresFiveDaysFromCreationWithMinusStartDate() {
			String id = UUID.randomUUID().toString();
			RollingLicenceTemplate rollingLicenceTemplate = new RollingLicenceTemplate();
			rollingLicenceTemplate.setId(id);
			rollingLicenceTemplate.setStartDate(new LocalDate().minusDays(2));
			rollingLicenceTemplate.setEndDate(new LocalDate().plusDays(10));
			rollingLicenceTemplate.setVersion(0);
			rollingLicenceTemplate.setTimePeriod(TIME_PERIOD);
			rollingLicenceTemplate
			.setBeginOn(RollingLicenceTemplate.RollingBeginOn.CREATION);
			rollingLicenceTemplate
			.setUnitType(RollingLicenceTemplate.RollingUnitType.DAY);
			return rollingLicenceTemplate;
		}

		/**
		 * Template for licences that will expire on end date (end date will be
		 * lower than end date based on rolling calculation).
		 * 
		 * Start date is set for now - 2, End date is set for now + 10 days.
		 * Rolling for 15 days.
		 * 
		 * @return a RollingLicenceTemplate
		 */
		public static RollingLicenceTemplate createRollingLicenceTemplateExpiresAtEndDate() {
			String id = UUID.randomUUID().toString();
			RollingLicenceTemplate rollingLicenceTemplate = new RollingLicenceTemplate();
			rollingLicenceTemplate.setId(id);
			rollingLicenceTemplate.setStartDate(new LocalDate().minusDays(2));
			rollingLicenceTemplate.setEndDate(new LocalDate().plusDays(10));
			rollingLicenceTemplate.setVersion(0);
			rollingLicenceTemplate.setTimePeriod(15);
			rollingLicenceTemplate
			.setBeginOn(RollingLicenceTemplate.RollingBeginOn.CREATION);
			rollingLicenceTemplate
			.setUnitType(RollingLicenceTemplate.RollingUnitType.DAY);
			return rollingLicenceTemplate;
		}

		/**
		 * Template for licences that expire on creation date + licence period
		 * 
		 * Start date is null, End date is null. Rolling for 15 days.
		 * 
		 * @return a RollingLicenceTemplate
		 */
		public static RollingLicenceTemplate createRollingLicenceTemplateNoStartOrEndDate() {
			String id = UUID.randomUUID().toString();
			RollingLicenceTemplate rollingLicenceTemplate = new RollingLicenceTemplate();
			rollingLicenceTemplate.setId(id);
			rollingLicenceTemplate.setStartDate(null);
			rollingLicenceTemplate.setEndDate(null);
			rollingLicenceTemplate.setVersion(0);
			rollingLicenceTemplate.setTimePeriod(15);
			rollingLicenceTemplate
			.setBeginOn(RollingLicenceTemplate.RollingBeginOn.CREATION);
			rollingLicenceTemplate
			.setUnitType(RollingLicenceTemplate.RollingUnitType.DAY);
			return rollingLicenceTemplate;
		}

		/**
		 * Template for licences that expire on creation date + licence period
		 * but having start date in past.
		 * 
		 * Start date is now - 2 days, End date is null. Rolling for 15 days.
		 * 
		 * @return a RollingLicenceTemplate
		 */
		public static RollingLicenceTemplate createRollingLicenceTemplateWithPastStartDateAndNoEndDate() {
			String id = UUID.randomUUID().toString();
			RollingLicenceTemplate rollingLicenceTemplate = new RollingLicenceTemplate();
			rollingLicenceTemplate.setId(id);
			rollingLicenceTemplate.setStartDate(new LocalDate().minusDays(2));
			rollingLicenceTemplate.setEndDate(null);
			rollingLicenceTemplate.setVersion(0);
			rollingLicenceTemplate.setTimePeriod(15);
			rollingLicenceTemplate
			.setBeginOn(RollingLicenceTemplate.RollingBeginOn.CREATION);
			rollingLicenceTemplate
			.setUnitType(RollingLicenceTemplate.RollingUnitType.DAY);
			return rollingLicenceTemplate;
		}

		/**
		 * Template for licences that expire on start date + licence period and
		 * having start date in future.
		 * 
		 * Start date is now + 2 days, End date is null. Rolling for 15 days.
		 * 
		 * @return a RollingLicenceTemplate
		 */
		public static RollingLicenceTemplate createRollingLicenceTemplateWithFutureStartDateAndNoEndDate() {
			String id = UUID.randomUUID().toString();
			RollingLicenceTemplate rollingLicenceTemplate = new RollingLicenceTemplate();
			rollingLicenceTemplate.setId(id);
			rollingLicenceTemplate.setStartDate(new LocalDate().plusDays(2));
			rollingLicenceTemplate.setEndDate(null);
			rollingLicenceTemplate.setVersion(0);
			rollingLicenceTemplate.setTimePeriod(15);
			rollingLicenceTemplate
			.setBeginOn(RollingLicenceTemplate.RollingBeginOn.CREATION);
			rollingLicenceTemplate
			.setUnitType(RollingLicenceTemplate.RollingUnitType.DAY);
			return rollingLicenceTemplate;
		}

		/**
		 * Template for licences that expire on end date and do not have a start
		 * date.
		 * 
		 * Start date is null, End date is less than creation + rolling period
		 * (5 days). Rolling period is 6 days
		 * 
		 * @return a RollingLicenceTemplate
		 */
		public static RollingLicenceTemplate createRollingLicenceTemplateNoStartDateReturningEndDate() {
			String id = UUID.randomUUID().toString();
			RollingLicenceTemplate rollingLicenceTemplate = new RollingLicenceTemplate();
			rollingLicenceTemplate.setId(id);
			rollingLicenceTemplate.setStartDate(null);
			rollingLicenceTemplate.setEndDate(new LocalDate().plusDays(5));
			rollingLicenceTemplate.setVersion(0);
			rollingLicenceTemplate.setTimePeriod(6);
			rollingLicenceTemplate
			.setBeginOn(RollingLicenceTemplate.RollingBeginOn.CREATION);
			rollingLicenceTemplate
			.setUnitType(RollingLicenceTemplate.RollingUnitType.DAY);
			return rollingLicenceTemplate;
		}

		/**
		 * Template for licences that expire on end date and start date is in
		 * past.
		 * 
		 * Start date is now - 2 days, End date is less than creation + rolling
		 * period (5 days). Rolling period is 6 days
		 * 
		 * @return a RollingLicenceTemplate
		 */
		public static RollingLicenceTemplate createRollingLicenceTemplatePastStartDateReturningEndDate() {
			String id = UUID.randomUUID().toString();
			RollingLicenceTemplate rollingLicenceTemplate = new RollingLicenceTemplate();
			rollingLicenceTemplate.setId(id);
			rollingLicenceTemplate.setStartDate(new LocalDate().minusDays(2));
			rollingLicenceTemplate.setEndDate(new LocalDate().plusDays(5));
			rollingLicenceTemplate.setVersion(0);
			rollingLicenceTemplate.setTimePeriod(6);
			rollingLicenceTemplate
			.setBeginOn(RollingLicenceTemplate.RollingBeginOn.CREATION);
			rollingLicenceTemplate
			.setUnitType(RollingLicenceTemplate.RollingUnitType.DAY);
			return rollingLicenceTemplate;
		}

		/**
		 * Template for licences that will have null expiry until first use.
		 * 
		 * Start date is set to null, End date is set to null. Rolling for 5
		 * days.
		 * 
		 * @return a RollingLicenceTemplate
		 */
		public static RollingLicenceTemplate createRollingLicenceTemplateNullExpiryUntilFirstUse() {
			String id = UUID.randomUUID().toString();
			RollingLicenceTemplate rollingLicenceTemplate = new RollingLicenceTemplate();
			rollingLicenceTemplate.setId(id);
			rollingLicenceTemplate.setStartDate(null);
			rollingLicenceTemplate.setEndDate(null);
			rollingLicenceTemplate.setVersion(0);
			rollingLicenceTemplate.setTimePeriod(5);
			rollingLicenceTemplate
			.setBeginOn(RollingLicenceTemplate.RollingBeginOn.FIRST_USE);
			rollingLicenceTemplate
			.setUnitType(RollingLicenceTemplate.RollingUnitType.DAY);
			return rollingLicenceTemplate;
		}

		/**
		 * Template for licences that will have null expiry until first use,
		 * with an end date and no start date.
		 * 
		 * Start date is set to null, End date is set to now + 5. Rolling for 5
		 * days.
		 * 
		 * @return a RollingLicenceTemplate
		 */
		public static RollingLicenceTemplate createRollingLicenceTemplateNullExpiryUntilFirstUseWithEndDate() {
			String id = UUID.randomUUID().toString();
			RollingLicenceTemplate rollingLicenceTemplate = new RollingLicenceTemplate();
			rollingLicenceTemplate.setId(id);
			rollingLicenceTemplate.setStartDate(null);
			rollingLicenceTemplate.setEndDate(new LocalDate().plusDays(5));
			rollingLicenceTemplate.setVersion(0);
			rollingLicenceTemplate.setTimePeriod(5);
			rollingLicenceTemplate
			.setBeginOn(RollingLicenceTemplate.RollingBeginOn.FIRST_USE);
			rollingLicenceTemplate
			.setUnitType(RollingLicenceTemplate.RollingUnitType.DAY);
			return rollingLicenceTemplate;
		}

		/**
		 * Template for licences that will have null expiry until first use,
		 * with an end date and a past start date. End date is greater than now
		 * + licence period.
		 * 
		 * Start date is set to now - 2, End date is set to now + 6. Rolling for
		 * 5 days.
		 * 
		 * @return a RollingLicenceTemplate
		 */
		public static RollingLicenceTemplate createRollingLicenceTemplateNullExpiryUntilFirstUseWithEndDateAndPastStartDateExpiryIsNull() {
			String id = UUID.randomUUID().toString();
			RollingLicenceTemplate rollingLicenceTemplate = new RollingLicenceTemplate();
			rollingLicenceTemplate.setId(id);
			rollingLicenceTemplate.setStartDate(new LocalDate().minusDays(2));
			rollingLicenceTemplate.setEndDate(new LocalDate().plusDays(6));
			rollingLicenceTemplate.setVersion(0);
			rollingLicenceTemplate.setTimePeriod(5);
			rollingLicenceTemplate
			.setBeginOn(RollingLicenceTemplate.RollingBeginOn.FIRST_USE);
			rollingLicenceTemplate
			.setUnitType(RollingLicenceTemplate.RollingUnitType.DAY);
			return rollingLicenceTemplate;
		}

		/**
		 * Template for licences that will have end date expiry until first use,
		 * with an end date and a past start date. End date is less than now +
		 * licence period.
		 * 
		 * Start date is set to now - 2, End date is set to now + 4. Rolling for
		 * 5 days.
		 * 
		 * @return a RollingLicenceTemplate
		 */
		public static RollingLicenceTemplate createRollingLicenceTemplateNullExpiryUntilFirstUseWithEndDateAndPastStartDateExpiryIsEndDate() {
			String id = UUID.randomUUID().toString();
			RollingLicenceTemplate rollingLicenceTemplate = new RollingLicenceTemplate();
			rollingLicenceTemplate.setId(id);
			rollingLicenceTemplate.setStartDate(new LocalDate().minusDays(2));
			rollingLicenceTemplate.setEndDate(new LocalDate().plusDays(4));
			rollingLicenceTemplate.setVersion(0);
			rollingLicenceTemplate.setTimePeriod(5);
			rollingLicenceTemplate
			.setBeginOn(RollingLicenceTemplate.RollingBeginOn.FIRST_USE);
			rollingLicenceTemplate
			.setUnitType(RollingLicenceTemplate.RollingUnitType.DAY);
			return rollingLicenceTemplate;
		}

		/**
		 * Template for licences that will have end date expiry until first use,
		 * with an end date and a future start date. Expiry is end date.
		 * 
		 * Start date is set to now + 2, End date is set to now + 4. Rolling for
		 * 5 days.
		 * 
		 * @return a RollingLicenceTemplate
		 */
		public static RollingLicenceTemplate createRollingLicenceTemplateNullExpiryUntilFirstUseWithEndDateAndFutureStartDateExpiryIsEndDate() {
			String id = UUID.randomUUID().toString();
			RollingLicenceTemplate rollingLicenceTemplate = new RollingLicenceTemplate();
			rollingLicenceTemplate.setId(id);
			rollingLicenceTemplate.setStartDate(new LocalDate().plusDays(2));
			rollingLicenceTemplate.setEndDate(new LocalDate().plusDays(4));
			rollingLicenceTemplate.setVersion(0);
			rollingLicenceTemplate.setTimePeriod(5);
			rollingLicenceTemplate
			.setBeginOn(RollingLicenceTemplate.RollingBeginOn.FIRST_USE);
			rollingLicenceTemplate
			.setUnitType(RollingLicenceTemplate.RollingUnitType.DAY);
			return rollingLicenceTemplate;
		}

		/**
		 * Template for licences that will have null expiry until first use,
		 * with an end date greater than start date + licence period.
		 * 
		 * Start date is set to now + 2, End date is set to now + 4. Rolling for
		 * 5 days.
		 * 
		 * @return a RollingLicenceTemplate
		 */
		public static RollingLicenceTemplate createRollingLicenceTemplateNullExpiryUntilFirstUseWithEndDateAndFutureStartDateExpiryIsNull() {
			String id = UUID.randomUUID().toString();
			RollingLicenceTemplate rollingLicenceTemplate = new RollingLicenceTemplate();
			rollingLicenceTemplate.setId(id);
			rollingLicenceTemplate.setStartDate(new LocalDate().plusDays(2));
			rollingLicenceTemplate.setEndDate(new LocalDate().plusDays(8));
			rollingLicenceTemplate.setVersion(0);
			rollingLicenceTemplate.setTimePeriod(5);
			rollingLicenceTemplate
			.setBeginOn(RollingLicenceTemplate.RollingBeginOn.FIRST_USE);
			rollingLicenceTemplate
			.setUnitType(RollingLicenceTemplate.RollingUnitType.DAY);
			return rollingLicenceTemplate;
		}

		/**
		 * @return a UsageLicenceTemplate
		 */
		public static UsageLicenceTemplate createUsageLicenceTemplate() {
			String id = UUID.randomUUID().toString();
			UsageLicenceTemplate rollingLicenceTemplate = new UsageLicenceTemplate();
			rollingLicenceTemplate.setId(id);
			rollingLicenceTemplate.setStartDate(new LocalDate());
			rollingLicenceTemplate.setEndDate(new LocalDate().plusDays(2));
			rollingLicenceTemplate.setVersion(0);
			rollingLicenceTemplate.setAllowedUsages(ALLOWED_USAGES);
			return rollingLicenceTemplate;
		}
	}


	@Test
	public void testgetSessionsByUserId() throws Exception {

		// Test Success

		// Authenticate user
		AuthenticationResponseDto authDto = erightsFacade
				.authenticateUser(new LoginPasswordCredentialDto(email,
						"password"));
		assertEquals("Check user created id is same as authenticated id", id,
				authDto.getUserIds().get(0));

		// Get sessions for id
		List<String> sessions = erightsFacade.getSessionsByUserId(id);

		// size of session should be 1
		assertEquals("Check correct size of sessions", 1, sessions.size());

		String sessionKey = sessions.get(0);

		// check the user id is returned correctly for the session key
		List<String> ids = erightsFacade.getCustomerIdsFromSession(sessionKey);
		assertEquals("Check correct customer id returned for session key", id,
				ids.get(0));

		// Check we can log this session out
		erightsFacade.logout(sessionKey);

		// Test SESSION_NOT_FOUND Exception

		try {
			// check session not found after logout
			sessions = erightsFacade.getSessionsByUserId(id);
			assertTrue("Check an exception was thrown", false);
		} catch (ErightsSessionNotFoundException e) {
			assertEquals("Check correct error was thrown by erights",
					UserAccountResponseSTATUS.SESSION_NOT_FOUND.value(),
					e.getCode());
		}

		// Test multiple sessions are returned or not for Shared user

		// Change customer type to SHARED
		Customer newCustomer = customerNew;
		newCustomer.setCustomerType(CustomerType.SHARED);
		CustomerDto newCustomerDto = new CustomerDto(newCustomer);

		// Update customer so that it can have multiple logins
		erightsFacade.updateUserAccount(newCustomerDto);

		// Authenticate user again
		authDto = erightsFacade
				.authenticateUser(new LoginPasswordCredentialDto(email,
						"passwordTest"));
		assertEquals("Check user created id is same as authenticated id", id,
				authDto.getUserIds().get(0));

		// Authenticate user again
		authDto = erightsFacade
				.authenticateUser(new LoginPasswordCredentialDto(email,
						"passwordTest"));
		assertEquals("Check user created id is same as authenticated id", id,
				authDto.getUserIds().get(0));

		// Get sessions for id
		sessions = erightsFacade.getSessionsByUserId(id);

		// size of session should be 2
		assertEquals("Check correct size of sessions", 2, sessions.size());

	}


	@Test
	public final void testAuthorizeRequest() throws Exception {
		EnforceableProductDto enforceableProduct = new EnforceableProductDto(
				"A Product"+unique);
		enforceableProduct.setDivisionId(divisionId);
		enforceableProduct.setAdminEmail(email);
		enforceableProduct.setRegistrationDefinitionType("PRODUCT_REGISTRATION");
		EnforceableProductUrlDto url = new EnforceableProductUrlDto(null, null,
				"/abc/123/test/", null, null, null);
		enforceableProduct.addEnforceableProductUrl(url);
		enforceableProduct.setState("ACTIVE");

		LicenceTemplate licetemplate =createLicenceTemplate(LicenceType.USAGE);
		LicenceDetailDto licenceDetailDto = ErightsObjectFactory.getLicenceTemplateDto(licetemplate);
		EnforceableProductDto returnedParentProduct = erightsFacade
				.createProduct(enforceableProduct, licenceDetailDto);

		// Add rolling licence
		RollingLicenceTemplate rollingLicenceTemplate = SampleDataFactory
				.createRollingLicenceTemplate();
		rollingLicenceTemplate.setTimePeriod(10);
		rollingLicenceTemplate.setUnitType(RollingUnitType.MINUTE);
		rollingLicenceTemplate.setBeginOn(RollingBeginOn.FIRST_USE);
		String erightsLicenceId = erightsFacade.addLicense(customerDto
				.getUserId(), rollingLicenceTemplate,
				Arrays.asList(new String[] { returnedParentProduct
						.getProductId() }), false);
		List<LicenceDto> licences = erightsFacade
				.getLicensesForUser(customerDto.getUserId(),erightsLicenceId);

		assertTrue("Check licence is inactive", !licences.get(0).isActive());
		assertEquals("Check correct licence", erightsLicenceId, licences.get(0)
				.getLicenseId());

		// Authenticate
		AuthenticationResponseDto authDto = erightsFacade
				.authenticateUser(customerDto.getLoginPasswordCredential());

		// Authorize Request
		erightsFacade.authorizeRequest(authDto.getSessionKey(),
				"/abc/123/test/", customerDto.getUserId(), erightsLicenceId);

		// Re-check licence is now active
		licences = erightsFacade.getLicensesForUser(customerDto.getUserId(),erightsLicenceId);

		assertTrue("Check licence is inactive", licences.get(0).isActive());
		assertEquals("Check correct licence", erightsLicenceId, licences.get(0)
				.getLicenseId());

		try {
			erightsFacade.authorizeRequest(authDto.getSessionKey(),
					"/abc/123/test/", id, INVALID_LICENCE_ID);
			assertTrue("Check the correct exception was thrown", false);
		} catch (LicenseNotFoundException e) {
			// Expected
		}

		try {
			erightsFacade.authorizeRequest(authDto.getSessionKey(),
					"/abc/123/test/", INVALID_USER_ID, erightsLicenceId);
			assertTrue("Check the correct exception was thrown", false);
		} catch (UserNotFoundException e) {
			// Expected
		}

		try {
			erightsFacade.authorizeRequest(authDto.getSessionKey(), "path", id,
					erightsLicenceId);
			assertTrue("Check the correct exception was thrown", false);
		} catch (AccessDeniedException e) {
			// Expected
		}
		erightsFacade.logout(authDto.getSessionKey());
		try {
			erightsFacade.authorizeRequest(authDto.getSessionKey(),
					"/abc/123/test/", id, erightsLicenceId);
			assertTrue("Check the correct exception was thrown", false);
		} catch (AccessDeniedException e) {
			// Expected
		}
	}

	private Platform getPaltformFilledObject(){
		String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789~`!@#$%^&*()-_=+[{]}\\|;:\'\",<.>/?";
		String platformCode = RandomStringUtils.random( 10, characters );
		Platform platform = new Platform() ;
		platform.setCode(platformCode);
		platform.setName("Test Case");
		platform.setDescription("Test Case description");
		return platform ;
	}
	
	private Platform createAndGetPlatform(){
		Platform platform = getPaltformFilledObject() ;
		erightsRestFacade.createPlatform(platform) ;
		delay(10000);
		GetAllPlatformsResponse response = 	erightsRestFacade.getAllPlatforms() ;
		List<OupPlatform> dto = response.getPlatforms() ;
		for(int i=0;i<dto.size();i++){
			if ( dto.get(i).getCode().equals(platform.getCode())) {
				Platform pt = new Platform();
				pt.setPlatformId(dto.get(i).getId());
				pt.setCode(dto.get(i).getCode());
				pt.setName(dto.get(i).getName());
				pt.setDescription(dto.get(i).getDescription());
				return pt ;
			}
		}
		return null ;
	}
	
	private boolean deletePlatform(Platform platform ) {
		DeletePlatformResponse response = erightsRestFacade.deletePlatform(platform) ;
		return true ;
	}
}
