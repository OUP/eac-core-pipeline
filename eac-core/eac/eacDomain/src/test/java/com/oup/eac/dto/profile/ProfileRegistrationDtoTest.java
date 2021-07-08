package com.oup.eac.dto.profile;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;

import com.oup.eac.domain.ProductPageDefinition;
import com.oup.eac.domain.ProductRegistration;
import com.oup.eac.domain.ProductRegistrationDefinition;
import com.oup.eac.domain.RegisterableProduct;
import com.oup.eac.dto.LicenceDto;

public class ProfileRegistrationDtoTest {

    private static final String PRODUCT_NAME = "productABC";
    private static final String REG_ID = "registrationId123";

    @SuppressWarnings("unused")
    @Test(expected = IllegalArgumentException.class)
    public void testErrorNullArg1() {
        new ProfileRegistrationDto(null, new LicenceDto());
    }


    @SuppressWarnings("unused")
    @Test(expected = IllegalArgumentException.class)
    public void testErrorNoRegistrationDefinition() {
        ProductRegistration prodReg = new ProductRegistration();
        LicenceDto licDto = new LicenceDto();
        new ProfileRegistrationDto(prodReg, licDto);
    }

    @SuppressWarnings("unused")
    @Test(expected = IllegalArgumentException.class)
    public void testErrorNoProduct() {
        ProductRegistration prodReg = new ProductRegistration();
        ProductRegistrationDefinition prd = new ProductRegistrationDefinition();
        prodReg.setRegistrationDefinition(prd);
        LicenceDto licDto = new LicenceDto();
        new ProfileRegistrationDto(prodReg, licDto);
    }
    
    private ProductRegistration getProductRegistration(boolean withPage){
        ProductRegistration prodReg = new ProductRegistration();
        ProductRegistrationDefinition prd = new ProductRegistrationDefinition();
        prodReg.setRegistrationDefinition(prd);
        prodReg.setId(REG_ID);
        
        RegisterableProduct rp = new RegisterableProduct();
        rp.setProductName(PRODUCT_NAME);
        
        prd.setProduct(rp);
        if(withPage){
            ProductPageDefinition page = new ProductPageDefinition();
            prd.setPageDefinition(page);
        }
        return prodReg;
    }

    @Test
    public void testSuccess() {
        ProductRegistration prodReg = getProductRegistration(true);
        prodReg.setCompleted(true);
        prodReg.setActivated(true);
        
        LicenceDto licDto = new LicenceDto("123", new DateTime(), false, true, false, false, false);
        ProfileRegistrationDto dto = new ProfileRegistrationDto(prodReg, licDto);
        Assert.assertNotNull(dto);

        Assert.assertTrue(dto.getHasRegistrationPage());
        Assert.assertTrue(dto.getIsManageable());
        Assert.assertEquals(licDto, dto.getLicenceDto());
        Assert.assertEquals(PRODUCT_NAME, dto.getProductName());
        Assert.assertEquals(prodReg, dto.getRegistration());
        Assert.assertEquals(REG_ID, dto.getRegistrationId());
        Assert.assertEquals(RegistrationStatus.ACTIVATED, dto.getRegistrationStatus());
    }
    
    @Test
    public void testSuccess2() {
        ProductRegistration prodReg = getProductRegistration(false);
        prodReg.setCompleted(true);
        prodReg.setActivated(true);
        
        LicenceDto licDto = new LicenceDto("123", new DateTime(), false, true, false, false, false);
        ProfileRegistrationDto dto = new ProfileRegistrationDto(prodReg, licDto);
        Assert.assertNotNull(dto);

        Assert.assertFalse(dto.getHasRegistrationPage());
        Assert.assertFalse(dto.getIsManageable());
        Assert.assertEquals(licDto, dto.getLicenceDto());
        Assert.assertEquals(PRODUCT_NAME, dto.getProductName());
        Assert.assertEquals(prodReg, dto.getRegistration());
        Assert.assertEquals(REG_ID, dto.getRegistrationId());
        Assert.assertEquals(RegistrationStatus.ACTIVATED, dto.getRegistrationStatus());
    }
    
    @Test
    public void testSuccess3() {
        ProductRegistration prodReg = getProductRegistration(true);
        
        LicenceDto licDto = new LicenceDto("123", new DateTime(), false, true, false, false, false);
        ProfileRegistrationDto dto = new ProfileRegistrationDto(prodReg, licDto);
        Assert.assertNotNull(dto);

        Assert.assertTrue(dto.getHasRegistrationPage());
        Assert.assertFalse(dto.getIsManageable());
        Assert.assertEquals(licDto, dto.getLicenceDto());
        Assert.assertEquals(PRODUCT_NAME, dto.getProductName());
        Assert.assertEquals(prodReg, dto.getRegistration());
        Assert.assertEquals(REG_ID, dto.getRegistrationId());
        Assert.assertEquals(RegistrationStatus.INCOMPLETE, dto.getRegistrationStatus());
    }

    @Test
    public void testSuccessNoLicence() {
        boolean expired = false;
        boolean active = true;
        LicenceDto licDto = new LicenceDto("123", new DateTime(), expired, active, false, false, false);
        ProductRegistration prodReg = getProductRegistration(true);
        ProfileRegistrationDto dto = new ProfileRegistrationDto(prodReg, licDto);
        Assert.assertNotNull(dto);

        Assert.assertTrue(dto.getHasRegistrationPage());
        Assert.assertFalse(dto.getIsManageable());
        Assert.assertEquals(licDto, dto.getLicenceDto());
        Assert.assertEquals(PRODUCT_NAME, dto.getProductName());
        Assert.assertEquals(prodReg, dto.getRegistration());
        Assert.assertEquals(REG_ID, dto.getRegistrationId());
        Assert.assertEquals(RegistrationStatus.INCOMPLETE, dto.getRegistrationStatus());
    }

}