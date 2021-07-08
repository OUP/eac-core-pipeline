package com.oup.eac.web.profile;

import java.util.Arrays;
import java.util.List;

import javax.naming.NamingException;
import javax.servlet.http.HttpSession;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpSession;

import com.oup.eac.common.mock.AbstractMockTest;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.ProductRegistration;
import com.oup.eac.domain.ProductRegistrationDefinition;
import com.oup.eac.domain.RegisterableProduct;
import com.oup.eac.dto.LicenceDto;
import com.oup.eac.dto.profile.ProfileRegistrationDto;
import com.oup.eac.service.ServiceLayerException;

public class CachingProfileRegistrationDtoSourceImplMockTest extends AbstractMockTest {

    private CachingProfileRegistrationDtoSourceImpl sut;
    private ProfileRegistrationDtoSource source;
    private Customer customer;
    private HttpSession session;
    private List<ProfileRegistrationDto> data;
    private ProfileRegistrationDto profRegDto1;
    private ProfileRegistrationDto profRegDto2;
    private ProfileRegistrationDto profRegDto3;
    private ProfileRegistrationDto profRegDto4;

    public CachingProfileRegistrationDtoSourceImplMockTest() throws NamingException {
        super();
    }

    @Before
    public void setup() {
        source = EasyMock.createMock(ProfileRegistrationDtoSource.class);
        sut = new CachingProfileRegistrationDtoSourceImpl(source);
        setMocks(source);
        session = new MockHttpSession();
        customer = new Customer();
        ProductRegistration reg1 = getRegistration();
        ProductRegistration reg2 = getRegistration();
        ProductRegistration reg3 = getRegistration();
        ProductRegistration reg4 = getRegistration();
        
        reg1.setCompleted(true);
        reg1.setActivated(true);
        
        reg2.setDenied(true);
        
        reg3.setCompleted(false);
        
        reg4.setCompleted(true);
        reg4.setAwaitingValidation(true);
        
        LicenceDto licDto1 = new LicenceDto("111", new DateTime(), true, false, true, false, false);
        LicenceDto licDto2 = new LicenceDto("112", new DateTime(), false, false, true, false, false);
        LicenceDto licDto3 = new LicenceDto("113", new DateTime(), true, false, true, false, false);
        LicenceDto licDto4 = new LicenceDto("114", new DateTime(), false, false, true, false, false);
        profRegDto1 = new ProfileRegistrationDto(reg1, licDto1);
        profRegDto2 = new ProfileRegistrationDto(reg2, licDto2);
        profRegDto3 = new ProfileRegistrationDto(reg3, licDto3);
        profRegDto4 = new ProfileRegistrationDto(reg4, licDto4);
        data = Arrays.asList(profRegDto1, profRegDto2, profRegDto3, profRegDto4);
    }

    private ProductRegistration getRegistration() {
        ProductRegistration result = new ProductRegistration();
        ProductRegistrationDefinition regDef = new ProductRegistrationDefinition();
        RegisterableProduct product = new RegisterableProduct();
        regDef.setProduct(product);
        result.setRegistrationDefinition(regDef);
        return result;
    }

    @Test
    public void testHappyPath1() throws ServiceLayerException {
        EasyMock.expect(source.getProfileRegistrationDtos(customer)).andReturn(data);
        replayMocks();
        List<ProfileRegistrationDto> result = sut.getProfileRegistrationDtos(customer, session);
        verifyMocks();
        Assert.assertEquals(3,result.size());
        Assert.assertTrue(result.contains(profRegDto1));
        Assert.assertTrue(result.contains(profRegDto2));
        Assert.assertTrue(result.contains(profRegDto4));
    }

    @Test
    public void testHappyPath2() throws ServiceLayerException {
        EasyMock.expect(source.getProfileRegistrationDtos(customer)).andReturn(null);
        replayMocks();
        List<ProfileRegistrationDto> result = sut.getProfileRegistrationDtos(customer, session);
        verifyMocks();
        Assert.assertEquals(0,result.size());        
    }
    
    @Test
    public void testUnHappyPath() throws ServiceLayerException {
        ServiceLayerException sle = new ServiceLayerException();
        EasyMock.expect(source.getProfileRegistrationDtos(customer)).andThrow(sle);
        replayMocks();
        try {
            sut.getProfileRegistrationDtos(customer, session);
            Assert.fail("exception expected");
        } catch (ServiceLayerException ex) {
            Assert.assertEquals(sle, ex);
        }
        verifyMocks();
    }
    
    @Test
    public void testRemoveFromCache()  {
        replayMocks();
        sut.removeFromCache(customer, session);
        verifyMocks();
    }

}
