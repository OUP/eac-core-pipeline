package com.oup.eac.web.controllers.helpers.impl;

import static com.oup.eac.domain.Customer.CustomerType.SELF_SERVICE;
import static com.oup.eac.domain.Customer.CustomerType.SHARED;
import static com.oup.eac.domain.Customer.CustomerType.SPECIFIC_CONCURRENCY;
import static com.oup.eac.domain.Product.ProductState.ACTIVE;
import static com.oup.eac.domain.Product.ProductState.REMOVED;
import static com.oup.eac.domain.Product.ProductState.RETIRED;
import static com.oup.eac.domain.Product.ProductState.SUSPENDED;
import static com.oup.eac.domain.RegisterableProduct.RegisterableType.ADMIN_REGISTERABLE;
import static com.oup.eac.domain.RegisterableProduct.RegisterableType.SELF_REGISTERABLE;
import static com.oup.eac.web.controllers.authentication.AuthenticationWorkFlow.RegistrationNotAllowedReason.CUSTOMER;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.mail.Session;
import javax.naming.NamingException;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.oup.eac.domain.Customer;
import com.oup.eac.domain.Customer.CustomerType;
import com.oup.eac.domain.Product.ProductState;
import com.oup.eac.domain.RegisterableProduct;
import com.oup.eac.domain.RegisterableProduct.RegisterableType;
import com.oup.eac.domain.utils.audit.TestingAppender;
import com.oup.eac.web.controllers.authentication.AuthenticationWorkFlow.RegistrationNotAllowedReason;
import com.oup.eac.web.controllers.helpers.RegistrationNotAllowedMessageCodeSource;

/**
 * 
 * @author David Hay
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:/eac/eac*-beans.xml", "classpath*:/eac/test.eac*-beans.xml",
        "classpath*:/eac/web.eac*-beans.xml", "classpath*:/eac/web.eac-servlet.xml" })
public class RegistrationNotAllowedMessageCodeSourceTest {
    
    private static final String MSGCODE1 = "registration.denied.msg.1";
    private static final String MSGCODE2 = "registration.denied.msg.2";
    private static final String MSGCODE3 = "registration.denied.msg.3";
    private static final String MSGCODE4 = "registration.denied.msg.4";
    private static final String MSGCODE5 = "registration.denied.msg.5";
    
    public RegistrationNotAllowedMessageCodeSourceTest() throws NamingException {
        SimpleNamingContextBuilder builder = SimpleNamingContextBuilder.emptyActivatedContextBuilder();
        builder.bind("java:/Mail", Session.getInstance(new Properties()));
    }

    @Resource(name="test.registrationNotAllowedMessageConfig")
    private Map<RegistrationNotAllowedMessageCodeConfig, String> config;

    @Resource(name="test.registrationNotAllowedValues")
    private List<RegistrationNotAllowedMessageCodeConfig> values;
    
    @Autowired
    private MessageSource messageSource;
    
    @Autowired
    private RegistrationNotAllowedMessageCodeSource messageCodeSource;
    
    private TestingAppender appender;

    @Before
    public void setup(){
        this.appender = new TestingAppender();
        Logger.getLogger(RegistrationNotAllowedMessageCodeSourceImpl.class).addAppender(this.appender);
    }

    @Test
    public void testMessageParameters() {
        Object[] args = {"<CONTACT_US>", "<PRODUCT_NAME>", "<EMAIL_LINK>", "<LOGOUT_LINK>"};
        Locale locale = null;
        String msg1 = messageSource.getMessage(MSGCODE1, args, locale);
        String msg2 = messageSource.getMessage(MSGCODE2, args, locale);
        String msg3 = messageSource.getMessage(MSGCODE3, args, locale);
        String msg4 = messageSource.getMessage(MSGCODE4, args, locale);
        String msg5 = messageSource.getMessage(MSGCODE5, args, locale);
        
        Assert.assertEquals("You are logged on with a shared user account that does not permit you to access this resource. Please <LOGOUT_LINK> and then log back in again with your personal username/password. In case of difficulty please contact  <EMAIL_LINK>.", msg1);
        Assert.assertEquals("You are logged on with a shared user account that does not permit you to access this resource. Please check that you have entered the correct username/password for the product you are trying to access. In case of difficulty please contact <EMAIL_LINK>.", msg2);
        Assert.assertEquals("You have attempted to access a resource which does not exist. Please check you have entered the address correctly and <CONTACT_US> if you require any further assistance.", msg3);
        Assert.assertEquals("You do not have access this resource. If you think you should have access, please contact <EMAIL_LINK>.", msg4);
        Assert.assertEquals("It is not currently possible to register for <PRODUCT_NAME>.  For more information please contact <EMAIL_LINK>.", msg5);
        
    }

    @Test
    public void testValues() {
        HashSet<RegistrationNotAllowedMessageCodeConfig> set = new HashSet<RegistrationNotAllowedMessageCodeConfig>();
        for(RegistrationNotAllowedMessageCodeConfig config : values){
            int before = set.size();
            set.add(config);
            int after = set.size();
            if(before == after){
                System.out.println("PROBLEM ADDING "  + config);
            }else{
                System.out.println("ADDED "  + config);
            }
        }
        Assert.assertEquals(24,set.size());
    }

    @Test
    public void testConfigSize() {
        Assert.assertNotNull(config);
        Assert.assertEquals(24, config.size());        
    }
    
    private void checkMessage(String expectedMessageCode, RegistrationNotAllowedReason notAllowedReason, CustomerType customerType, RegisterableType registerableType, ProductState lifecycleState){
        Customer customer = new Customer();
        RegisterableProduct product = new RegisterableProduct();
        customer.setCustomerType(customerType);
        product.setRegisterableType(registerableType);
        String actual = this.messageCodeSource.getMessageCode(notAllowedReason, customer, product, lifecycleState);        
        Assert.assertEquals(expectedMessageCode, actual);
    }
    
    @Test
    public void testMessageCodeLookups() {
        
        //CHECK DEFAULT
        Assert.assertEquals(0, this.appender.getMessages().size());
        checkMessage(this.messageCodeSource.getDefaultMessageCode(),  null, null, null, null);//CHECK THAT WE ALWAYS GET THE DEFAULT BACK
        Assert.assertEquals(1, this.appender.getMessages().size());
        LoggingEvent event = this.appender.getMessages().get(0);
        Assert.assertEquals(Level.ERROR, event.getLevel());
        String errorMessage = (String)event.getMessage();
        
        this.appender.getMessages().clear();

        Assert.assertTrue(errorMessage.startsWith("Failed to find RegisrationDeniedMessageCode for [com.oup.eac.web.controllers.helpers.impl.RegistrationNotAllowedMessageCodeConfig@"));
        Assert.assertTrue(errorMessage.endsWith("[customerType=<null>,registerableType=<null>,notAllowedReason=<null>,lifecycleState=<null>]] using default[registration.denied.msg.3]"));
        
        //Failed to find RegisrationDeniedMessageCode for [com.oup.eac.web.controllers.helpers.impl.RegistrationNotAllowedMessageCodeConfig@593be416[customerType=<null>,registerableType=<null>,notAllowedReason=<null>,lifecycleState=<null>]] using default[registration.denied.msg.3]
        //CHECK PRODUCT
        checkMessage(this.messageCodeSource.getProductRegDefMessageCode(), RegistrationNotAllowedReason.PRODUCT_REG_DEF, null,       null, null);//1
        
        //CHECK 25 customer values
        checkMessage(MSGCODE1,  CUSTOMER, SHARED,       SELF_REGISTERABLE,  ACTIVE);//2
        checkMessage(MSGCODE2,  CUSTOMER, SHARED,       ADMIN_REGISTERABLE, ACTIVE);//3
        checkMessage("N/A", CUSTOMER, SELF_SERVICE, SELF_REGISTERABLE,  ACTIVE);//4
        checkMessage(MSGCODE4,  CUSTOMER, SELF_SERVICE, ADMIN_REGISTERABLE, ACTIVE);//5
        checkMessage("N/A", CUSTOMER, SPECIFIC_CONCURRENCY, SELF_REGISTERABLE,  ACTIVE);//6
        checkMessage(MSGCODE4,  CUSTOMER, SPECIFIC_CONCURRENCY, ADMIN_REGISTERABLE, ACTIVE);//7
        
        checkMessage(MSGCODE1,  CUSTOMER, SHARED,       SELF_REGISTERABLE,  RETIRED);//8
        checkMessage(MSGCODE5,  CUSTOMER, SELF_SERVICE, SELF_REGISTERABLE,  RETIRED);//9
        checkMessage(MSGCODE5,  CUSTOMER, SPECIFIC_CONCURRENCY, SELF_REGISTERABLE,  RETIRED);//10
        checkMessage(MSGCODE1,  CUSTOMER, SHARED,       ADMIN_REGISTERABLE, RETIRED);//11
        checkMessage(MSGCODE5,  CUSTOMER, SELF_SERVICE, ADMIN_REGISTERABLE, RETIRED);//12
        checkMessage(MSGCODE5,  CUSTOMER, SPECIFIC_CONCURRENCY, ADMIN_REGISTERABLE, RETIRED);//13

        
        checkMessage(MSGCODE5,  CUSTOMER, SHARED,       SELF_REGISTERABLE,  SUSPENDED);//14
        checkMessage(MSGCODE5,  CUSTOMER, SELF_SERVICE, SELF_REGISTERABLE,  SUSPENDED);//15
        checkMessage(MSGCODE5,  CUSTOMER, SPECIFIC_CONCURRENCY, SELF_REGISTERABLE,  SUSPENDED);//16
        checkMessage(MSGCODE5,  CUSTOMER, SHARED,       ADMIN_REGISTERABLE, SUSPENDED);//17
        checkMessage(MSGCODE5,  CUSTOMER, SELF_SERVICE, ADMIN_REGISTERABLE, SUSPENDED);//18
        checkMessage(MSGCODE5,  CUSTOMER, SPECIFIC_CONCURRENCY, ADMIN_REGISTERABLE, SUSPENDED);//19
        
        checkMessage(MSGCODE5,  CUSTOMER, SHARED,       SELF_REGISTERABLE,  REMOVED);//20
        checkMessage(MSGCODE5,  CUSTOMER, SELF_SERVICE, SELF_REGISTERABLE,  REMOVED);//21
        checkMessage(MSGCODE5,  CUSTOMER, SPECIFIC_CONCURRENCY, SELF_REGISTERABLE,  REMOVED);//22
        checkMessage(MSGCODE5,  CUSTOMER, SHARED,       ADMIN_REGISTERABLE, REMOVED);//23
        checkMessage(MSGCODE5,  CUSTOMER, SELF_SERVICE, ADMIN_REGISTERABLE, REMOVED);//24
        checkMessage(MSGCODE5,  CUSTOMER, SPECIFIC_CONCURRENCY, ADMIN_REGISTERABLE, REMOVED);//25
        
        Assert.assertTrue(this.appender.getMessages().isEmpty());
    }
    
    @Test 
    public void testMap() {
        
        Assert.assertEquals("1",  config.get(getKey(SELF_SERVICE, SELF_REGISTERABLE, CUSTOMER, ACTIVE)));
        Assert.assertEquals("2",  config.get(getKey(SELF_SERVICE, SELF_REGISTERABLE, CUSTOMER, SUSPENDED)));
        Assert.assertEquals("3",  config.get(getKey(SELF_SERVICE, SELF_REGISTERABLE, CUSTOMER, REMOVED)));
        Assert.assertEquals("4",  config.get(getKey(SELF_SERVICE, SELF_REGISTERABLE, CUSTOMER, RETIRED)));
        
        Assert.assertEquals("5",  config.get(getKey(SPECIFIC_CONCURRENCY, SELF_REGISTERABLE, CUSTOMER, ACTIVE)));
        Assert.assertEquals("6",  config.get(getKey(SPECIFIC_CONCURRENCY, SELF_REGISTERABLE, CUSTOMER, SUSPENDED)));
        Assert.assertEquals("7",  config.get(getKey(SPECIFIC_CONCURRENCY, SELF_REGISTERABLE, CUSTOMER, REMOVED)));
        Assert.assertEquals("8",  config.get(getKey(SPECIFIC_CONCURRENCY, SELF_REGISTERABLE, CUSTOMER, RETIRED)));
        
        Assert.assertEquals("9",  config.get(getKey(SHARED, SELF_REGISTERABLE, CUSTOMER, ACTIVE)));
        Assert.assertEquals("10",  config.get(getKey(SHARED, SELF_REGISTERABLE, CUSTOMER, SUSPENDED)));
        Assert.assertEquals("11",  config.get(getKey(SHARED, SELF_REGISTERABLE, CUSTOMER, REMOVED)));
        Assert.assertEquals("12",  config.get(getKey(SHARED, SELF_REGISTERABLE, CUSTOMER, RETIRED)));
        
        Assert.assertEquals("13",  config.get(getKey(SELF_SERVICE, ADMIN_REGISTERABLE, CUSTOMER, ACTIVE)));
        Assert.assertEquals("14", config.get(getKey(SELF_SERVICE, ADMIN_REGISTERABLE, CUSTOMER, SUSPENDED)));
        Assert.assertEquals("15", config.get(getKey(SELF_SERVICE, ADMIN_REGISTERABLE, CUSTOMER, REMOVED)));
        Assert.assertEquals("16", config.get(getKey(SELF_SERVICE, ADMIN_REGISTERABLE, CUSTOMER, RETIRED)));

        Assert.assertEquals("17",  config.get(getKey(SPECIFIC_CONCURRENCY, ADMIN_REGISTERABLE, CUSTOMER, ACTIVE)));
        Assert.assertEquals("18", config.get(getKey(SPECIFIC_CONCURRENCY, ADMIN_REGISTERABLE, CUSTOMER, SUSPENDED)));
        Assert.assertEquals("19", config.get(getKey(SPECIFIC_CONCURRENCY, ADMIN_REGISTERABLE, CUSTOMER, REMOVED)));
        Assert.assertEquals("20", config.get(getKey(SPECIFIC_CONCURRENCY, ADMIN_REGISTERABLE, CUSTOMER, RETIRED)));

        Assert.assertEquals("21", config.get(getKey(SHARED, ADMIN_REGISTERABLE, CUSTOMER, ACTIVE)));
        Assert.assertEquals("22", config.get(getKey(SHARED, ADMIN_REGISTERABLE, CUSTOMER, SUSPENDED)));
        Assert.assertEquals("23", config.get(getKey(SHARED, ADMIN_REGISTERABLE, CUSTOMER, REMOVED)));
        Assert.assertEquals("24", config.get(getKey(SHARED, ADMIN_REGISTERABLE, CUSTOMER, RETIRED)));
    }
    
    private RegistrationNotAllowedMessageCodeConfig getKey(CustomerType customerType,RegisterableType registerableType,RegistrationNotAllowedReason notAllowedReason,ProductState lifecycleState ) {
        RegistrationNotAllowedMessageCodeConfig result = new RegistrationNotAllowedMessageCodeConfig();
        result.setCustomerType(customerType);
        result.setRegisterableType(registerableType);
        result.setNotAllowedReason(notAllowedReason);
        result.setLifecycleState(lifecycleState);
        return result;
    }

}
