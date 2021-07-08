package com.oup.eac.dto.profile;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.oup.eac.domain.ProductRegistration;
import com.oup.eac.domain.Registration;

/**
 * @author David Hay
 * 
 */
public class RegistrationStatusTest {

    private Registration<?> registration;
   
    @Before
    public void setup() {
        registration = new ProductRegistration();
    }
    
    @Test
    public void testActivated() {
         registration.setCompleted(true);
         registration.setActivated(true);
         check(RegistrationStatus.ACTIVATED, registration);
    }
    
    @Test
    public void testDenied() {
         registration.setDenied(true);
         check(RegistrationStatus.DENIED, registration);
    }
    
    @Test
    public void testAwaitingValidation() {
         registration.setAwaitingValidation(true);
         check(RegistrationStatus.AWAITING_VALIDATION, registration);
    }
    
    @Test
    public void testIncomplete() {
         check(RegistrationStatus.INCOMPLETE, registration);
    }
    
    @Test
    public void testOther() {
         registration.setCompleted(true);
         check(RegistrationStatus.OTHER, registration);
    }
    
    @Test
    public void testNoRegistration() {
         check(RegistrationStatus.NO_REGISTRATION, null);
    }
    
    private void check(RegistrationStatus expected, Registration<?> registration){
        Assert.assertEquals(expected, RegistrationStatus.getFromRegistration(registration));
        Assert.assertTrue(expected.getMessageCode().startsWith("registration.status."));
    }

}
