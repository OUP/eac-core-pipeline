package com.oup.eac.dto.license;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;

import com.oup.eac.dto.LicenceDto;

public class LicenceStatusTest {

    
    
    private LicenceDto getLicenceDto(boolean enabled, boolean active, boolean expired){
        LicenceDto dto = new LicenceDto("123", new DateTime(), expired, active, false, false, false);
        dto.setEnabled(enabled);
        return dto;
    }
    
    @Test
    public void testActive() {
        LicenceDto licenceDto = getLicenceDto(true, true, false);
        check(LicenceStatus.ACTIVE, licenceDto);
    }
    
    @Test
    public void testExpired() {
        LicenceDto licenceDto = getLicenceDto(false, false, true);
         check(LicenceStatus.EXPIRED, licenceDto);
    }
    
    @Test
    public void testDisabled() {
        LicenceDto licenceDto = getLicenceDto(false, false, false);
        check(LicenceStatus.DISABLED, licenceDto);
    }
    
    @Test
    public void testInactive() {
        LicenceDto licenceDto = getLicenceDto(true, false, false);
        check(LicenceStatus.INACTIVE, licenceDto);
    }
    
    @Test
    public void testNoLicence() {
         check(LicenceStatus.NO_LICENCE, null);
    }
    
    private void check(LicenceStatus expected, LicenceDto licenceDto){
        Assert.assertEquals(expected, LicenceStatus.getLicenceStatus(licenceDto));
        Assert.assertTrue(expected.getMessageCode().startsWith("licence.status."));
    }

}
