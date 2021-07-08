package com.oup.eac.ws.v2.service.impl.entitlements;

import java.util.Calendar;

import junit.framework.Assert;

import org.exolab.castor.types.Date;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import com.oup.eac.domain.RollingLicenceTemplate.RollingBeginOn;
import com.oup.eac.domain.RollingLicenceTemplate.RollingUnitType;
import com.oup.eac.dto.LicenceDetailDto;
import com.oup.eac.dto.LicenceDto;
import com.oup.eac.dto.RollingLicenceDetailDto;
import com.oup.eac.dto.StandardConcurrentLicenceDetailDto;
import com.oup.eac.dto.UsageLicenceDetailDto;
import com.oup.eac.ws.v2.binding.common.ConcurrencyLicenceDetails;
import com.oup.eac.ws.v2.binding.common.ExtendedLicenceDetails;
import com.oup.eac.ws.v2.binding.common.LicenceDetails;
import com.oup.eac.ws.v2.binding.common.LicenceInfo;
import com.oup.eac.ws.v2.binding.common.RollingLicenceDetail;
import com.oup.eac.ws.v2.binding.common.RollingLicenceInfo;
import com.oup.eac.ws.v2.binding.common.UsageLicenceDetails;
import com.oup.eac.ws.v2.binding.common.UsageLicenceInfo;
import com.oup.eac.ws.v2.binding.common.types.BeginEnum;
import com.oup.eac.ws.v2.binding.common.types.UnitEnum;
import com.oup.eac.ws.v2.service.entitlements.LicenceData;
import com.oup.eac.ws.v2.service.entitlements.LicenceDtoConverter;

public class LicenceDtoConverterTest {

    private LicenceDtoConverter converter = new LicenceDtoConverterImpl();
    
    @Before
    public void setup(){
        converter = new LicenceDtoConverterImpl();
    }
    
    @Test
    public void testNull(){
        LicenceData convDetail = this.converter.convertLicenceDto(null);
        Assert.assertNull(convDetail.getDetail());
        Assert.assertNull(convDetail.getInfo());
    }
    
    @Test
    public void testPartial(){
    	DateTime now = new DateTime();
        LicenceDto dto = new LicenceDto(String.valueOf(12345), now, true, false,true,false,false);
        
        dto.setEnabled(true);//2
        dto.setStartDate(null);//5
        dto.setEndDate(null);//6
        dto.setLicenceDetail(null);//7        

        //dto.setErightsId(null);//8 not put into LicenceDetails
        dto.setProductIds(null);//9 not put into LicenceDetails
        dto.setLicenceTemplateId(null);//10 not put into LicenceDetails
        
        LicenceData data = this.converter.convertLicenceDto(dto);
        LicenceDetails convDetail = data.getDetail();
        LicenceInfo convInfo = data.getInfo();
        
        Assert.assertNotNull(convDetail);
        Assert.assertTrue(convDetail.isEnabled());//1
        
        Assert.assertFalse(convInfo.isActive());//2
        Assert.assertTrue(convInfo.isExpired());//3
        Assert.assertEquals(now.toDateTime().getMillis(),convInfo.getExpiryDateAndTime().getTime());//4
        
        Assert.assertNull(convDetail.getStartDate());//5
        Assert.assertNull(convDetail.getEndDate());//6
        Assert.assertNull(convDetail.getExtendedDetails());//7
    }
    
    @Test
    public void testEmpty(){
    	DateTime now = new DateTime();
        LicenceDto dto = new LicenceDto(String.valueOf(12345), now,false,false,true,false,false);
        LicenceData data = this.converter.convertLicenceDto(dto);
        LicenceDetails convDetail = data.getDetail();
        LicenceInfo convInfo = data.getInfo();
        Assert.assertNotNull(convDetail);
        
        Assert.assertFalse(convDetail.isEnabled());//1

        Assert.assertFalse(convInfo.isActive());//2
        Assert.assertFalse(convInfo.isExpired());//3
        Assert.assertEquals(now.toDateTime().getMillis(),convInfo.getExpiryDateAndTime().getTime());//4
        
        Assert.assertNull(convDetail.getStartDate());//5
        Assert.assertNull(convDetail.getEndDate());//6
        Assert.assertNull(convDetail.getExtendedDetails());//7
    }
    
    @Test
    public void testDates(){
    	DateTime now = new DateTime();
        LicenceDto dto = new LicenceDto(String.valueOf(12345), now,false,false,true,false,false);
        dto.setStartDate(new LocalDate(2010,9,8));
        dto.setEndDate(new LocalDate(2013,12,11));
        LicenceData data = this.converter.convertLicenceDto(dto);
        LicenceDetails convDetail = data.getDetail();
        
        Assert.assertNotNull(convDetail);
        
        Assert.assertFalse(convDetail.isEnabled());//2
        Assert.assertNotNull(convDetail.getStartDate());//5
        Assert.assertNotNull(convDetail.getEndDate());//6
        Assert.assertNull(convDetail.getExtendedDetails());//7
 
        checkCastorDate(8, Calendar.SEPTEMBER, 2010, convDetail.getStartDate());
        checkCastorDate(11, Calendar.DECEMBER, 2013, convDetail.getEndDate());
        
    }
    
    private void checkCastorDate(int dayOfMonth, int monthOfYear, int year, Date date){
        Calendar cal = date.toCalendar();
        Assert.assertEquals(dayOfMonth,cal.get(Calendar.DAY_OF_MONTH));
        Assert.assertEquals(monthOfYear,cal.get(Calendar.MONTH));
        Assert.assertEquals(year,cal.get(Calendar.YEAR));
    }
    
    @Test
    public void testConcurrent(){
    	DateTime expiryDateAndTime = new DateTime();
        LicenceDto dto = new LicenceDto(String.valueOf(12345), expiryDateAndTime, true, false, true, false, false);

        int total = 100;
        int user = 123;
        LicenceDetailDto licenceDetail = new StandardConcurrentLicenceDetailDto(total, user);
        dto.setLicenceDetail(licenceDetail);
        
        LicenceData data = this.converter.convertLicenceDto(dto);
        LicenceDetails convDetail = data.getDetail(); 
        ExtendedLicenceDetails extended = convDetail.getExtendedDetails();
        ConcurrencyLicenceDetails con = extended.getConcurrentLicence();
        Assert.assertNotNull(con);
        Assert.assertEquals(user, con.getConcurrency());
        LicenceInfo info = data.getInfo();
        checkInfo(info, expiryDateAndTime);
    }
    
    @Test
    public void testStandard(){
    	DateTime expiryDateAndTime = new DateTime();
        LicenceDto dto = new LicenceDto(String.valueOf(12345), expiryDateAndTime, true, false, true, false, false);
        dto.setLicenceDetail(null);
        
        LicenceData data = this.converter.convertLicenceDto(dto);
        LicenceDetails convDetail = data.getDetail(); 
        ExtendedLicenceDetails extended = convDetail.getExtendedDetails();
        Assert.assertNull(extended);
        
        LicenceInfo info = data.getInfo();
        checkInfo(info, expiryDateAndTime);
    }

    
    @Test
    public void testRolling1(){
    	DateTime expiryDateAndTime = new DateTime();
        DateTime firstUse = new DateTime(2013, 2, 1, 12, 0, 0 , 0);
        LicenceDto dto = new LicenceDto(String.valueOf(12345), expiryDateAndTime, true, false, true, false, false);
        RollingBeginOn beginOn = RollingBeginOn.CREATION;
        RollingUnitType unitType = RollingUnitType.YEAR;
        int timePeriod = 2;
        RollingLicenceDetailDto licenceDetail = new RollingLicenceDetailDto(beginOn, unitType, timePeriod, firstUse);
        dto.setLicenceDetail(licenceDetail);
        
        LicenceData data = this.converter.convertLicenceDto(dto);
        
        LicenceDetails convDetail = data.getDetail();
        ExtendedLicenceDetails extended = convDetail.getExtendedDetails();
        RollingLicenceDetail roll = extended.getRollingLicence();
        Assert.assertNotNull(roll);
        
        BeginEnum bon = roll.getBeginOn();
        Assert.assertEquals(BeginEnum.CREATION,bon);
        
        UnitEnum punit = roll.getPeriodUnit();
        Assert.assertEquals(UnitEnum.YEAR,punit);
        
        Assert.assertEquals(2, roll.getPeriodValue());
        
        LicenceInfo info = data.getInfo();
        checkInfo(info, expiryDateAndTime);
        
        Assert.assertTrue(info instanceof RollingLicenceInfo);
        RollingLicenceInfo rInfo = (RollingLicenceInfo)info;
        Assert.assertEquals(licenceDetail.getFirstUse().toDateTime().getMillis(), rInfo.getFirstUsedDate().getTime());
        checkCastorDate(1, Calendar.FEBRUARY, 2013, new Date(rInfo.getFirstUsedDate()));
    }
    
    @Test
    public void testRolling2(){
    	DateTime expiryDateAndTime = null;
        DateTime firstUse = null;
        LicenceDto dto = new LicenceDto(String.valueOf(12345), expiryDateAndTime, true, false, true, false, false);
        RollingBeginOn beginOn = null;
        RollingUnitType unitType = null;
        int timePeriod = 2;
        RollingLicenceDetailDto licenceDetail = new RollingLicenceDetailDto(beginOn, unitType, timePeriod, firstUse);
        dto.setLicenceDetail(licenceDetail);
        
        LicenceData data = this.converter.convertLicenceDto(dto);
        
        LicenceDetails convDetail = data.getDetail();
        ExtendedLicenceDetails extended = convDetail.getExtendedDetails();
        RollingLicenceDetail roll = extended.getRollingLicence();
        Assert.assertNotNull(roll);
        
        BeginEnum bon = roll.getBeginOn();
        Assert.assertNull(bon);
        
        UnitEnum punit = roll.getPeriodUnit();
        Assert.assertNull(punit);
        
        Assert.assertEquals(2, roll.getPeriodValue());
        
        LicenceInfo info = data.getInfo();
        checkInfo(info, expiryDateAndTime);
        
        Assert.assertTrue(info instanceof RollingLicenceInfo);
        RollingLicenceInfo rInfo = (RollingLicenceInfo)info;
        Assert.assertNull(rInfo.getFirstUsedDate());        
    }
    @Test
    public void testUsage(){
    	DateTime expiryDateAndTime = new DateTime();
        LicenceDto dto = new LicenceDto(String.valueOf(12345), expiryDateAndTime, true, false, true, false, false);
        
        UsageLicenceDetailDto licenceDetail = new UsageLicenceDetailDto(123);
        licenceDetail.setAllowedUsages(123);
        dto.setLicenceDetail(licenceDetail);
        
        LicenceData data = this.converter.convertLicenceDto(dto);
        LicenceDetails convDetail =data.getDetail();
        ExtendedLicenceDetails extended = convDetail.getExtendedDetails();
        UsageLicenceDetails usage = extended.getUsageLicence();
        Assert.assertEquals(123, usage.getAllowedUsages());
        
        LicenceInfo info = data.getInfo();
        checkInfo(info, expiryDateAndTime);
        
        Assert.assertTrue(info instanceof UsageLicenceInfo);
        UsageLicenceInfo uInfo = (UsageLicenceInfo)info;
        Assert.assertEquals(123, uInfo.getUsagesRemaining());
    }
    
    private void checkInfo(LicenceInfo info, DateTime expiryDateAndTime){
        Assert.assertFalse(info.getActive());
        Assert.assertTrue(info.getExpired());
        if(expiryDateAndTime != null){
        	Assert.assertEquals(expiryDateAndTime.toDateTime().getMillis(),info.getExpiryDateAndTime().getTime());
        }else{
        	Assert.assertNull(info.getExpiryDateAndTime());
        }
    }
}
