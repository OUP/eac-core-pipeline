package com.oup.eac.ws.v2.service.impl.entitlements;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.oup.eac.domain.ActivationCodeRegistration;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.LinkedRegistration;
import com.oup.eac.domain.ProductRegistration;
import com.oup.eac.dto.CustomerRegistrationsDto;
import com.oup.eac.dto.LicenceDto;
import com.oup.eac.ws.v2.service.entitlements.CustomerLicenceMergeInfo;


public class CustomerLicenceMergeInfoImplTest  {

    private static final List<String> EMPTY = Collections.<String>emptyList();
    private ProductRegistration preg1;
    private ActivationCodeRegistration acReg2;
    private Customer customer;

    private LicenceDto lic1;
    private LicenceDto lic2;
    private LicenceDto lic3;
    private LicenceDto lic4;
    private LicenceDto lic5;
    private LicenceDto lic6;
    
    @Before
    public void setup(){
        customer = new Customer();
        preg1 = new ProductRegistration();
        preg1.setId("111");
        LinkedRegistration linked222 = new LinkedRegistration();
       // linked222.setErightsLicenceId(222);
        linked222.setId("222");
        LinkedRegistration linked333 = new LinkedRegistration();
       // linked333.setErightsLicenceId(333);
        linked333.setId("333");
        preg1.setLinkedRegistrations(new HashSet<LinkedRegistration>(Arrays.asList(linked222, linked333)));
        
        acReg2 = new ActivationCodeRegistration();
        LinkedRegistration linked444 = new LinkedRegistration();
       // linked444.setErightsLicenceId(444);
        linked444.setId("444");
        LinkedRegistration linkedNULL = new LinkedRegistration();        
        acReg2.setLinkedRegistrations(new HashSet<LinkedRegistration>(Arrays.asList(linked444, linkedNULL)));
        
        lic1 = new LicenceDto();
        lic1.setLicenseId("111");
        
        lic2 = new LicenceDto();
        lic2.setLicenseId("222");
        
        lic3 = new LicenceDto();
        lic3.setLicenseId("333");
        
        lic4 = new LicenceDto();
        lic4.setLicenseId("444");
        
        lic5 = new LicenceDto();
        lic5.setLicenseId("555");
        
        lic6 = new LicenceDto();
        lic6.setLicenseId(null);
    }
    
    
    @Test
    public void testEacOnly(){
        @SuppressWarnings("unchecked")
        CustomerRegistrationsDto dto = new CustomerRegistrationsDto(customer, Arrays.asList(preg1,acReg2), null);
        CustomerLicenceMergeInfo info = new CustomerLicenceMergeInfoImpl(dto);
        System.out.println(info);
        Assert.assertEquals(customer,info.getCustomer());
        checkSame(EMPTY, info.getMatchedLicences());
        checkSame(EMPTY, info.getUnMatchedLicencesInErightsOnly());
        checkSame(Arrays.asList("111","222","333","444"), info.getUnMatchedLicencesInEacOnly());
        Assert.assertFalse(info.isFullyMatched());
    }
    
    @Test
    public void testErightsOnly(){
        CustomerRegistrationsDto dto = new CustomerRegistrationsDto(customer, null, Arrays.asList(lic2, lic3, lic4, lic5, lic6));
        CustomerLicenceMergeInfo info = new CustomerLicenceMergeInfoImpl(dto);
        System.out.println(info);
        Assert.assertEquals(customer,info.getCustomer());        
        checkSame(EMPTY, info.getMatchedLicences());
        checkSame(EMPTY, info.getUnMatchedLicencesInEacOnly());
        checkSame(Arrays.asList("222","333","444","555"), info.getUnMatchedLicencesInErightsOnly());
        Assert.assertFalse(info.isFullyMatched());
    }
    
    @Test
    public void testMismatch(){
        @SuppressWarnings("unchecked")
        CustomerRegistrationsDto dto = new CustomerRegistrationsDto(customer, Arrays.asList(preg1,acReg2), Arrays.asList(lic2, lic3, lic4, lic5, lic6));
        CustomerLicenceMergeInfo info = new CustomerLicenceMergeInfoImpl(dto);
        System.out.println(info);
        Assert.assertEquals(customer,info.getCustomer());
        System.out.println("MatchedLicences :"+info.getMatchedLicences());
        checkSame(Arrays.asList("222","333","444"), info.getMatchedLicences());
        checkSame(Arrays.asList("111"), info.getUnMatchedLicencesInEacOnly());
        checkSame(Arrays.asList("555"), info.getUnMatchedLicencesInErightsOnly());
        Assert.assertFalse(info.isFullyMatched());
    }
    
    @Test
    public void testFullMatch(){
        @SuppressWarnings("unchecked")
        CustomerRegistrationsDto dto = new CustomerRegistrationsDto(customer, Arrays.asList(preg1,acReg2), Arrays.asList(lic1, lic2, lic3, lic4 ));
        CustomerLicenceMergeInfo info = new CustomerLicenceMergeInfoImpl(dto);
        System.out.println(info);
        Assert.assertEquals(customer,info.getCustomer());
        checkSame(Arrays.asList("111","222","333","444"), info.getMatchedLicences());
        checkSame(EMPTY, info.getUnMatchedLicencesInEacOnly());
        checkSame(EMPTY, info.getUnMatchedLicencesInErightsOnly());
        Assert.assertTrue(info.isFullyMatched());
    }
    
    public void checkSame(List<String> expected, Set<String> set){
        HashSet<String> exp = new HashSet<String>(expected);        
        Assert.assertEquals(exp,set);
    }
}
