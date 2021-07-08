package com.oup.eac.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import junit.framework.Assert;

import org.junit.Test;

import com.oup.eac.domain.Customer.CustomerType;

public class CustomerTest {

	private static final int MAX_FAILED_AUTH_ATTEMPTS = 3;
	
	/**
	 * Test registering login attempts and locking a customer. The tests
	 * are looped a few times to ensure state rolls over correctly.
	 */
	@Test
	public final void testFailedAttemptsWithLocking() {
		Customer customer = new Customer();
		
		for (int i = 0; i < 5;i++) {
			assertFalse("Check customer is not locked", customer.isLocked());
			assertEquals("Check number of failed login attempts", 0, customer.getFailedAttempts());
			
			customer.registerFailedAttempt(MAX_FAILED_AUTH_ATTEMPTS);
			
			assertEquals("Check number of failed login attempts", 1, customer.getFailedAttempts());
			assertFalse("Check customer is not locked", customer.isLocked());
			
			customer.registerFailedAttempt(MAX_FAILED_AUTH_ATTEMPTS);
			
			assertEquals("Check number of failed login attempts", 2, customer.getFailedAttempts());
			assertFalse("Check customer is not locked", customer.isLocked());
			
			customer.registerFailedAttempt(MAX_FAILED_AUTH_ATTEMPTS);
			
			assertEquals("Check number of failed login attempts", 3, customer.getFailedAttempts());
			assertTrue("Check customer is locked", customer.isLocked());
			
			customer.setLocked(false);
		}		
	}

    /**
     * Test registering login attempts and no locking for shared customer.
     */
    @Test
    public final void testSharedUserFailedAttemptsNoLocking() {
        Customer customer = new Customer();
        customer.setCustomerType(CustomerType.SHARED);
        
        for (int i = 0; i < 5;i++) {
            assertFalse("Check customer is not locked", customer.isLocked());
            assertEquals("Check number of failed login attempts", 0, customer.getFailedAttempts());
            
            customer.registerFailedAttempt(MAX_FAILED_AUTH_ATTEMPTS);
            
            assertEquals("Check number of failed login attempts", 0, customer.getFailedAttempts());
            assertFalse("Check customer is not locked", customer.isLocked());
            
            customer.registerFailedAttempt(MAX_FAILED_AUTH_ATTEMPTS);
            
            assertEquals("Check number of failed login attempts", 0, customer.getFailedAttempts());
            assertFalse("Check customer is not locked", customer.isLocked());
            
            customer.registerFailedAttempt(MAX_FAILED_AUTH_ATTEMPTS);
            
            // shared customer shouldnt be locked
            assertEquals("Check number of failed login attempts", 0, customer.getFailedAttempts());
            assertFalse("Check customer is locked", customer.isLocked());
            
        }       
    }
	
	
	/**
	 * Tests that the username is held in lower case.
	 */
	@Test
	public void oupUATname(){
	    Customer customer = new Customer();
	    customer.setUsername("JOEBLOGGS");
	    String username = customer.getUsername();
	    Assert.assertEquals("joebloggs",username);
	    
	    customer.setUsername(null);
	    Assert.assertNull(customer.getUsername());
	}
}
