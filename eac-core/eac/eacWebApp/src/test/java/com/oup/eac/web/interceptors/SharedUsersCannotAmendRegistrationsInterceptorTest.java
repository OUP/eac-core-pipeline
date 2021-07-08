package com.oup.eac.web.interceptors;

import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;

import com.oup.eac.common.mock.AbstractMockTest;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.Customer.CustomerType;
import com.oup.eac.web.controllers.helpers.SessionHelper;

public class SharedUsersCannotAmendRegistrationsInterceptorTest extends AbstractMockTest {

	public SharedUsersCannotAmendRegistrationsInterceptorTest() throws NamingException {
		super();
	}

	@Test
	public void testInterceptorNoCustomerInSession() {

		try {
			SharedUsersCannotAmendRegistrationsInterceptor sut = new SharedUsersCannotAmendRegistrationsInterceptor();
			Object handler = null;
			MockHttpServletRequest request = new MockHttpServletRequest();
			MockHttpServletResponse response = new MockHttpServletResponse();
			boolean canProceed = sut.preHandle(request, response, handler);
			Assert.assertTrue(canProceed);

		} catch (Exception ex) {
			Assert.fail("unexpected exception " + ex);
		}
	}
	
	@Test
	public void testInterceptorWithSelfServiceCustomerInSession() {

		try {
			SharedUsersCannotAmendRegistrationsInterceptor sut = new SharedUsersCannotAmendRegistrationsInterceptor();
			Object handler = null;
			Customer customer = new Customer();
			customer.setCustomerType(CustomerType.SELF_SERVICE);
			
			MockHttpSession session = new MockHttpSession();
			MockHttpServletRequest request = new MockHttpServletRequest();
			request.setSession(session);
			SessionHelper.setCustomer(request, customer);
			MockHttpServletResponse response = new MockHttpServletResponse();
			boolean canProceed = sut.preHandle(request, response, handler);
			Assert.assertTrue(canProceed);

		} catch (Exception ex) {
			Assert.fail("unexpected exception " + ex);
		}
	}
	
	@Test
    public void testInterceptorWithSpecificConcurrencyCustomerInSession() {

        try {
            SharedUsersCannotAmendRegistrationsInterceptor sut = new SharedUsersCannotAmendRegistrationsInterceptor();
            Object handler = null;
            Customer customer = new Customer();
            customer.setCustomerType(CustomerType.SPECIFIC_CONCURRENCY);
            
            MockHttpSession session = new MockHttpSession();
            MockHttpServletRequest request = new MockHttpServletRequest();
            request.setSession(session);
            SessionHelper.setCustomer(request, customer);
            MockHttpServletResponse response = new MockHttpServletResponse();
            boolean canProceed = sut.preHandle(request, response, handler);
            Assert.assertTrue(canProceed);

        } catch (Exception ex) {
            Assert.fail("unexpected exception " + ex);
        }
    }

	@Test
	public void testInterceptorWithSharedCustomerInSession() {

		try {
			final RequestDispatcher rd = EasyMock.createMock(RequestDispatcher.class);
			setMocks(rd);
			SharedUsersCannotAmendRegistrationsInterceptor sut = new SharedUsersCannotAmendRegistrationsInterceptor();
			Object handler = null;
			Customer customer = new Customer();
			customer.setCustomerType(CustomerType.SHARED);			
			MockHttpSession session = new MockHttpSession();
			MockHttpServletRequest request = new MockHttpServletRequest() {
					@Override
					public RequestDispatcher getRequestDispatcher(final String path) {
						Assert.assertEquals(SharedUsersCannotAmendRegistrationsInterceptor.AMEND_REGISTRATION_DENIED_ERROR_JSP,path);
						return rd;
					}
			};
			
			request.setSession(session);
			MockHttpServletResponse response = new MockHttpServletResponse();
			SessionHelper.setCustomer(request, customer);
			
			rd.forward(request, response);
			EasyMock.expectLastCall();
			
			replayMocks();
			boolean canProceed = sut.preHandle(request, response, handler);
			Assert.assertFalse(canProceed);
			verifyMocks();
			
		} catch (Exception ex) {
			Assert.fail("unexpected exception " + ex);
		}
	}
}
