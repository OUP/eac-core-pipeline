package com.oup.eac.common.utils.email;

import static com.oup.eac.common.utils.email.InternationalEmailAddress.isValid;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import junit.framework.Assert;

import org.junit.Test;

public class InternationalEmailAddressTest {

    @Test
    public void testNull(){
        Assert.assertFalse(isValid(null));
    }
    
    @Test
    public void testEmpty(){
        Assert.assertFalse(isValid(" "));
    }
    
    @Test
    public void testBlank(){
        Assert.assertFalse(isValid("     "));
    }
    
    @Test
    public void testSimple(){
        Assert.assertTrue(isValid("test.user@test.com"));
    }
    
    @Test
    public void testInvalid1(){
        Assert.assertFalse(isValid("test.usertest.com"));
    }
    
    @Test
    public void testInvalidPadding(){
        Assert.assertFalse(isValid(" test.user@test.com"));
        Assert.assertFalse(isValid("test.user @test.com"));
        Assert.assertFalse(isValid("test.user@ test.com"));
        Assert.assertFalse(isValid("test.user@test.com "));
    }
    
    @Test
    public void shouldNotCareAboutCase() {
    	assertThat(isValid("USER@TEST.COM"), equalTo(true));	
    	assertThat(isValid("user@TEST.COM"), equalTo(true));	
    }
}
