package com.oup.eac.dto;

import java.util.Arrays;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class LicenceDtoTest {

    @Test
    public void testSort1(){
        LicenceDto lic1 = new LicenceDto();
        LicenceDto lic2 = new LicenceDto();
        
        List<LicenceDto> lics = Arrays.asList(lic1, lic2);
        LicenceDto.sortByExpiryDateDate(lics, true);
        
        Assert.assertEquals(lic1, lics.get(0));
        Assert.assertEquals(lic2, lics.get(1));
        
        LicenceDto.sortByExpiryDateDate(lics, false);
        
        Assert.assertEquals(lic1, lics.get(0));
        Assert.assertEquals(lic2, lics.get(1));
        
    }
    
    @Test
    public void testSort2(){
        LicenceDto lic1 = new LicenceDto();
        LicenceDto lic2 = new LicenceDto();
        
        lic1.setEndDateTime(new DateTime().plusYears(2));
        lic2.setEndDateTime(new DateTime().plusYears(1));
        List<LicenceDto> lics = Arrays.asList(lic1, lic2);
        LicenceDto.sortByExpiryDateDate(lics, true);
        
        Assert.assertEquals(lic2, lics.get(0));
        Assert.assertEquals(lic1, lics.get(1));
        
        LicenceDto.sortByExpiryDateDate(lics, false);
        
        Assert.assertEquals(lic1, lics.get(0));
        Assert.assertEquals(lic2, lics.get(1));
        
    }
    
    @Test
    public void testSort3(){
        LicenceDto lic1 = new LicenceDto("123", new DateTime().plusYears(2),false, true, false, false, false);
        LicenceDto lic2 = new LicenceDto("123", new DateTime().plusYears(1),false, true, false, false, false);
        
        List<LicenceDto> lics = Arrays.asList(lic1, lic2);
        LicenceDto.sortByExpiryDateDate(lics, true);
        
        Assert.assertEquals(lic2, lics.get(0));
        Assert.assertEquals(lic1, lics.get(1));
        
        LicenceDto.sortByExpiryDateDate(lics, false);
        
        Assert.assertEquals(lic1, lics.get(0));
        Assert.assertEquals(lic2, lics.get(1));
        
    }
    
    @Test
    public void testSort4() {
        LicenceDto lic1 = new LicenceDto();
        LicenceDto lic2 = new LicenceDto("123", new DateTime().plusYears(1),false, true, false, false, false);
        
        List<LicenceDto> lics = Arrays.asList(lic1, lic2);
        LicenceDto.sortByExpiryDateDate(lics, true);
        
        Assert.assertEquals(lic2, lics.get(0));
        Assert.assertEquals(lic1, lics.get(1));
        
        LicenceDto.sortByExpiryDateDate(lics, false);
        
        Assert.assertEquals(lic1, lics.get(0));
        Assert.assertEquals(lic2, lics.get(1));
        

        
    }
    
    @Test
    public void testSort5() {
        LicenceDto lic1 = new LicenceDto("123", new DateTime().plusYears(1),false, true, false, false, false);
        LicenceDto lic2 = new LicenceDto();
        
        List<LicenceDto> lics = Arrays.asList(lic1, lic2);
        LicenceDto.sortByExpiryDateDate(lics, true);
        
        Assert.assertEquals(lic1, lics.get(0));
        Assert.assertEquals(lic2, lics.get(1));
        
        LicenceDto.sortByExpiryDateDate(lics, false);
        
        Assert.assertEquals(lic2, lics.get(0));
        Assert.assertEquals(lic1, lics.get(1));
        
    }
    
    @Test
    public void testSort6() {
        LicenceDto lic1 = new LicenceDto("123", null, false, true, false, false, false);
        LicenceDto lic2 = new LicenceDto();
        
        List<LicenceDto> lics = Arrays.asList(lic1, lic2);
        LicenceDto.sortByExpiryDateDate(lics, true);
        
        Assert.assertEquals(lic1, lics.get(0));
        Assert.assertEquals(lic2, lics.get(1));
        
        LicenceDto.sortByExpiryDateDate(lics, false);
        
        Assert.assertEquals(lic1, lics.get(0));
        Assert.assertEquals(lic2, lics.get(1));
        
    }
    
    
    @Test
    public void testSort7() {
        DateTime expiry = new DateTime();
        LicenceDto lic1 = new LicenceDto("123", expiry, false, true, false, false, false);
        //LicenceDto lic1 = new LicenceDto(123, null, false, true);//NOT expired but DISABLED
        LicenceDto lic2 = new LicenceDto("123", expiry.plusDays(1), false, true, false, false, false);//NOT expired but DISABLED
        lic1.setEndDateTime(expiry.plusDays(1));
        lic2.setEndDateTime(expiry.plusDays(2));
        
        LicenceDto lic3 = new LicenceDto("123", expiry.plusDays(2), false, true, false, false, false);//Expired but ACTIVE
        LicenceDto lic4 = new LicenceDto("123", expiry.plusDays(3) ,false, true, false, false, false);//Expired but ACTIVE
        
        List<LicenceDto> lics = Arrays.asList(lic1, lic2, lic3, lic4);
        LicenceDto.sortByExpiryDateDate(lics, true);
        
        Assert.assertEquals(lic1, lics.get(0));
        Assert.assertEquals(lic2, lics.get(1));
        Assert.assertEquals(lic3, lics.get(2));
        Assert.assertEquals(lic4, lics.get(3));
        
        LicenceDto.sortByExpiryDateDate(lics, false);
        
        Assert.assertEquals(lic4, lics.get(0));        
        Assert.assertEquals(lic3, lics.get(1));
        Assert.assertEquals(lic2, lics.get(2));
        Assert.assertEquals(lic1, lics.get(3));
        
    }
}
