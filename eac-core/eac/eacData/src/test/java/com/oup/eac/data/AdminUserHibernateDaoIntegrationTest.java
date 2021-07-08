package com.oup.eac.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.UUID;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

import com.oup.eac.domain.AdminUser;
import com.oup.eac.domain.Password;


public class AdminUserHibernateDaoIntegrationTest extends AbstractDBTest {

    @Autowired
    private AdminUserDao adminUserDao;   
    
    private AdminUser adminUser;
        
    
    /**
     * @throws Exception
     *             Sets up data ready for test.
     */
    @Before
    public final void setUp() throws Exception {        

        adminUser = getSampleDataCreator().createAdminUser();
        getSampleDataCreator().createAdminUser();
        loadAllDataSets();
    }

    /**
     * Create AdminUser and check fields are set and retrieved correctly.
     * 
     * @throws Exception
     */
    @Test
    public final void testCreateAdminUser() throws Exception {
        AdminUser user = new AdminUser();
        user.setFirstName("Admin");
        user.setFamilyName("User");
        user.setEmailAddress("adminuser@mailinator.com");
        user.setPassword(new Password("Passw0rd", false));
        user.setUsername("sha256");
        user.setCreatedDate(new DateTime());
        UUID uuid = UUID.randomUUID();
        user.setId(uuid.toString());
        
        adminUserDao.save(user);
        adminUserDao.flush();
        adminUserDao.clear();
        
        //assertEquals(2, countRowsInTable("customer"));        
        
        AdminUser newUser = adminUserDao.getAdminUserByUsername("sha256");
        StandardPasswordEncoder standardEncoder = new StandardPasswordEncoder();
        assertNotNull(newUser.getId());
        assertTrue(standardEncoder.matches("Passw0rd",newUser.getPassword()));
       
        //assertEquals("Check password has been encoded", "5d6e7fb4062c649564bcd9873d16461a7233d2b355f1a6691852a2113de7e61556a6e40a1730c7e0", newUser.getPassword());
                
        assertNotNull(adminUserDao.getAdminUserByUsername(adminUser.getUsername()));        
    }
}