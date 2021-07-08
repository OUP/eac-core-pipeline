package com.oup.eac.data.migrationtool;

import java.util.Locale;
import java.util.UUID;

import org.hibernate.SessionFactory;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import com.oup.eac.data.AbstractDbMessageTest;
import com.oup.eac.domain.migrationtool.CustomerMigration;
import com.oup.eac.domain.migrationtool.CustomerMigrationData;
import com.oup.eac.domain.migrationtool.CustomerMigrationState;

/**
 * 
 * @author Chirag Joshi
 *
 */
public abstract class BaseEacMigrationDaoTest extends AbstractDbMessageTest {

    @Autowired
    protected CustomerMigrationDao customerMigrationDao;
    
    @Autowired
    protected SessionFactory sessionFactory;
    
    protected final CustomerMigration createCM(int customerNumber, CustomerMigrationState state) throws Exception {
        DateTime modifiedDate = new DateTime().minus(Duration.standardDays(40 - customerNumber));
        DateTime lastLoginDate = new DateTime().minus(Duration.standardDays(30 - customerNumber));
        CustomerMigration cm =  createCustomerMigration(state, customerNumber, modifiedDate, lastLoginDate);
        getSampleDataCreator().createCustomerMigration(cm);
        return cm;
    }
    
    protected final CustomerMigration createCustomerMigration(CustomerMigrationState state, int customerNumber, DateTime modifiedDate, DateTime lastLoginDate) {
        CustomerMigration result = new CustomerMigration();
        CustomerMigrationData data = new CustomerMigrationData();
        result.setId(String.valueOf(customerNumber));
        result.setVersion(0);
        
        result.setCustomer(null);
        result.setState(state);
        
        data.setId(result.getId());
        data.setVersion(0);
        data.setUsername("username" + customerNumber);
        data.setPassword("plainTextPassword");
        data.setFirstName("first" + customerNumber);
        data.setLastName("last" + customerNumber);
        data.setEmailAddress("tc.customer" + customerNumber + "@test,com");
        data.setResetPassword(true);
        data.setFailedAttempts(0);
        data.setLocked(false);            
        data.setCustomerType("SELF_SERVICE");        
        data.setEnabled(true);
        data.setLocale(new Locale("en/gb"));
        data.setTimeZone("Europe/London"); // set time zone to Europe/London
        data.setLastLogin(lastLoginDate);
        data.setEmailVerificationState("UNKNOWN");
        data.setExternalId(UUID.randomUUID().toString());
        data.setCreatedDate(new DateTime());
        data.setUpdatedDate(modifiedDate);
        
        result.setData(data);        
        return result;
    }

    protected final void checkSame(CustomerMigration expected, CustomerMigration actual) {
        // Check customer migration
        Assert.assertNotNull(expected.getCreatedDate());
        Assert.assertNotNull(actual.getCreatedDate());
        Assert.assertEquals(expected.getId(), actual.getId());
        Assert.assertEquals(expected.getState(), actual.getState());
                
        Assert.assertNotNull(expected.getData());
        Assert.assertNotNull(actual.getData());
        
        // Check customer migration data
        CustomerMigrationData expectedData = expected.getData();
        CustomerMigrationData actualData = actual.getData();
        Assert.assertNotNull(expectedData);
        Assert.assertNotNull(actualData);
        
        Assert.assertEquals(expectedData.getId(), actualData.getId());
        Assert.assertEquals(expectedData.getUsername(), actualData.getUsername());
        Assert.assertEquals(expectedData.getPassword(), actualData.getPassword());
        Assert.assertEquals(expectedData.getFirstName(), actualData.getFirstName());
        Assert.assertEquals(expectedData.getLastName(), actualData.getLastName());
        Assert.assertEquals(expectedData.getEmailAddress(), actualData.getEmailAddress());
        Assert.assertEquals(expectedData.isResetPassword(), actualData.isResetPassword());
        Assert.assertEquals(expectedData.getFailedAttempts(), actualData.getFailedAttempts());
        Assert.assertEquals(expectedData.isLocked(), actualData.isLocked());
        Assert.assertEquals(expectedData.getCustomerType(), actualData.getCustomerType());    
        Assert.assertEquals(expectedData.isEnabled(), actualData.isEnabled());
        Assert.assertEquals(expectedData.getLocale(), actualData.getLocale());
        Assert.assertEquals(expectedData.getTimeZone(), actualData.getTimeZone());
        Assert.assertEquals(expectedData.getEmailVerificationState(), actualData.getEmailVerificationState());
        Assert.assertEquals(expectedData.getExternalId(), actualData.getExternalId());
        Assert.assertNotNull(expectedData.getCreatedDate());
        Assert.assertNotNull(actualData.getCreatedDate());
         
    }

    /**
     * This method clears the session preventing any further lazy loading.
     */
    protected final void sessionClear() {
        sessionFactory.getCurrentSession().clear();
    }




}
