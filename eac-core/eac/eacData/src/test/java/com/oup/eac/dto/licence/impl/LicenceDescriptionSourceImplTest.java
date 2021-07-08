package com.oup.eac.dto.licence.impl;

import java.text.MessageFormat;
import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.oup.eac.domain.RollingLicenceTemplate.RollingBeginOn;
import com.oup.eac.domain.RollingLicenceTemplate.RollingUnitType;
import com.oup.eac.dto.LicenceDto;
import com.oup.eac.dto.RollingLicenceDetailDto;
import com.oup.eac.dto.StandardConcurrentLicenceDetailDto;
import com.oup.eac.dto.StandardLicenceDetailDto;
import com.oup.eac.dto.UsageLicenceDetailDto;
import com.oup.eac.dto.licence.LicenceDescriptionGenerator;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:/licenceDescriptionConfig.xml" })
public class LicenceDescriptionSourceImplTest {

    private Locale locale;
    private String dateTimeStyle;
    private String dateStyle;
    private String timeZone;
    private boolean generateHtml;
    
    @Autowired
    private MessageSource messageSource;
        
    private LicenceDescriptionGeneratorSourceImpl genSourceBug;
    private LicenceDescriptionGenerator generatorBug;
    
    private LicenceDescriptionGeneratorSourceImpl genSourceBugFixed;
    private LicenceDescriptionGenerator generatorBugFixed;
    
    private DateTime now;
    
    @Before
    public void setup() {
        this.locale = Locale.UK;
        this.dateTimeStyle="ML";
        this.dateStyle="M-";
        this.timeZone = DateTimeZone.UTC.getID();
        this.generateHtml = false;
        Assert.assertTrue(this.messageSource != null);
        now = new DateTime(2013, 2, 21, 12, 0, 0, 0, DateTimeZone.forID(this.timeZone));
        genSourceBug = new LicenceDescriptionGeneratorSourceImpl(messageSource, dateStyle, dateTimeStyle, false);
        generatorBug = genSourceBug.getLicenceDescriptionGenerator(locale, timeZone, generateHtml);
        
        genSourceBugFixed = new LicenceDescriptionGeneratorSourceImpl(messageSource, dateStyle, dateTimeStyle, true);
        generatorBugFixed = genSourceBugFixed.getLicenceDescriptionGenerator(locale, timeZone, generateHtml);
    }
   
    @Test
    public void testBlankLicenceDto() {
        LicenceDto licenceDto = new LicenceDto();
        String desc = generatorBug.getLicenceDescription(licenceDto, now);
        Assert.assertEquals("", desc);
    }
    
    @Test
    public void testNullLicenceDto() {
        LicenceDto licenceDto = null;
        String desc = generatorBug.getLicenceDescription(licenceDto, now);
        Assert.assertEquals("", desc);
    }
    
    @Test
    public void testUsageLicenceDto() {
        LicenceDto licenceDto = new LicenceDto("123", null, false, false, false, false, false);
        UsageLicenceDetailDto usageLicenceDetail = new UsageLicenceDetailDto(4);
        usageLicenceDetail.setAllowedUsages(5);
        licenceDto.setLicenceDetail(usageLicenceDetail);
        String desc = generatorBug.getLicenceDescription(licenceDto, now);
        Assert.assertEquals("4 out of 5 use(s) remaining", desc);
    }
    
    @Test
    public void testConcurrent1() {
        LicenceDto licenceDto = new LicenceDto("123", null, false, false, false, false, false);
        StandardConcurrentLicenceDetailDto detail = new StandardConcurrentLicenceDetailDto(3, 2);
        licenceDto.setLicenceDetail(detail);
        String desc = generatorBug.getLicenceDescription(licenceDto, now);
        Assert.assertEquals("Access for 2 concurrent users", desc);
    }
    
    @Test
    public void testConcurrent2() {
        LicenceDto licenceDto = new LicenceDto("123", null, false, false, false, false, false);
        StandardConcurrentLicenceDetailDto detail = new StandardConcurrentLicenceDetailDto(1, 1);
        licenceDto.setLicenceDetail(detail);
        String desc = generatorBug.getLicenceDescription(licenceDto, now);
        Assert.assertEquals("", desc);
    }
    
    @Test
    public void testConcurrent3() {
        LicenceDto licenceDto = new LicenceDto("123", null, false, false, false, false, false);
        StandardConcurrentLicenceDetailDto detail = new StandardConcurrentLicenceDetailDto(3, 2);
        licenceDto.setLicenceDetail(detail);
        licenceDto.setStartDate(now.minusDays(30).toLocalDate());
        String desc = generatorBug.getLicenceDescription(licenceDto, now);
        Assert.assertEquals("Access for 2 concurrent users Start 22-Jan-2013", desc);
    }
    
    @Test
    public void testConcurrent4() {
        LicenceDto licenceDto = new LicenceDto("123", null, false, false, false, false, false);
        StandardConcurrentLicenceDetailDto detail = new StandardConcurrentLicenceDetailDto(3, 2);
        licenceDto.setLicenceDetail(detail);
        licenceDto.setEndDate(now.plusDays(30).toLocalDate());
        String desc = generatorBug.getLicenceDescription(licenceDto, now);
        Assert.assertEquals("Access for 2 concurrent users End 23-Mar-2013", desc);
    }
    
    @Test
    public void testConcurrent5() {
        LicenceDto licenceDto = new LicenceDto("123", null, false, false, false, false, false);
        StandardConcurrentLicenceDetailDto detail = new StandardConcurrentLicenceDetailDto(3, 2);
        licenceDto.setLicenceDetail(detail);
        licenceDto.setStartDate(now.minusDays(30).toLocalDate());
        licenceDto.setEndDate(now.plusDays(30).toLocalDate());
        String desc = generatorBug.getLicenceDescription(licenceDto, now);
        Assert.assertEquals("Access for 2 concurrent users Start 22-Jan-2013 End 23-Mar-2013", desc);
    }
    
    @Test
    public void testStandard1() {
        LicenceDto licenceDto = new LicenceDto("123", null, false, false, false, false, false);
        StandardLicenceDetailDto detail = new StandardLicenceDetailDto();
        licenceDto.setLicenceDetail(detail);
        String desc = generatorBug.getLicenceDescription(licenceDto, now);
        Assert.assertEquals("", desc);
    }
    
    @Test
    public void testStandard2() {
        LicenceDto licenceDto = new LicenceDto("123", null, false, false, false, false, false);
        StandardLicenceDetailDto detail = new StandardLicenceDetailDto();
        licenceDto.setLicenceDetail(detail);
        licenceDto.setStartDate(now.minusDays(30).toLocalDate());
        String desc = generatorBug.getLicenceDescription(licenceDto, now);
        Assert.assertEquals("Start 22-Jan-2013", desc);
    }
    
    @Test
    public void testStandard3() {
        LicenceDto licenceDto = new LicenceDto("123", null, false, false, false, false, false);
        StandardLicenceDetailDto detail = new StandardLicenceDetailDto();
        licenceDto.setLicenceDetail(detail);
        licenceDto.setEndDate(now.plusDays(30).toLocalDate());
        String desc = generatorBug.getLicenceDescription(licenceDto, now);
        Assert.assertEquals("End 23-Mar-2013", desc);
    }
    
    @Test
    public void testStandard4() {
        LicenceDto licenceDto = new LicenceDto("123", null, false, false, false, false, false);
        StandardLicenceDetailDto detail = new StandardLicenceDetailDto();
        licenceDto.setLicenceDetail(detail);
        licenceDto.setStartDate(now.minusDays(30).toLocalDate());
        licenceDto.setEndDate(now.plusDays(30).toLocalDate());
        String desc = generatorBug.getLicenceDescription(licenceDto, now);
        Assert.assertEquals("Start 22-Jan-2013 End 23-Mar-2013", desc);
    }
    
    @Test
    public void testRollingFirstUse1() {
        LicenceDto licenceDto = new LicenceDto("123", now.plusMonths(11), false, false, false, false, false);
        RollingLicenceDetailDto rolling = new RollingLicenceDetailDto(RollingBeginOn.FIRST_USE, RollingUnitType.YEAR, 1, now.minusMonths(1));        
        licenceDto.setLicenceDetail(rolling);
        String desc = generatorBug.getLicenceDescription(licenceDto, now);
        Assert.assertEquals("Access from 21-Jan-2013 12:00:00 UTC to 21-Jan-2014 12:00:00 UTC", desc);
    }
    
    @Test
    public void testRollingFirstUse2() {
        //not used but has expiry date
        LicenceDto licenceDto = new LicenceDto("123", now.plusMonths(11), false, false, false, false, false);
        RollingLicenceDetailDto rolling = new RollingLicenceDetailDto(RollingBeginOn.FIRST_USE, RollingUnitType.YEAR, 1, null);               
        licenceDto.setLicenceDetail(rolling);
        String desc = generatorBug.getLicenceDescription(licenceDto, now);
        Assert.assertEquals("Access period: 1 year(s) from First Use Subject to End 21-Jan-2014", desc);
    }
    
    @Test
    public void testRollingFirstUse3() {
        //not used but has expiry date, start date in past
        LicenceDto licenceDto = new LicenceDto("123", now.plusMonths(11), false, false, false, false, false);
        RollingLicenceDetailDto rolling = new RollingLicenceDetailDto(RollingBeginOn.FIRST_USE, RollingUnitType.YEAR, 1, null);               
        licenceDto.setLicenceDetail(rolling);
        licenceDto.setStartDateTime(now.minusDays(1));
        String desc = generatorBug.getLicenceDescription(licenceDto, now);
        Assert.assertEquals("Access period: 1 year(s) from First Use Subject to End 21-Jan-2014", desc);
    }
    
    @Test
    public void testRollingFirstUse4() {
        //not used but has expiry date, start date in future
        LicenceDto licenceDto = new LicenceDto("123", now.plusMonths(11), false, false, false, false, false);
        RollingLicenceDetailDto rolling = new RollingLicenceDetailDto(RollingBeginOn.FIRST_USE, RollingUnitType.YEAR, 1, null);               
        licenceDto.setLicenceDetail(rolling);
        licenceDto.setStartDateTime(now.plusDays(1));
        licenceDto.setStartDate(licenceDto.getStartDateTime().toLocalDate());
        String desc = generatorBug.getLicenceDescription(licenceDto, now);
        Assert.assertEquals("Access period: 1 year(s) from First Use Subject to Start 22-Feb-2013 End 21-Jan-2014", desc);
    }
    
    @Test
    public void testRollingFirstUse5() {
        //not used and no expiry date, no start, no end
        LicenceDto licenceDto = new LicenceDto("123", null, false, false, false, false, false);
        RollingLicenceDetailDto rolling = new RollingLicenceDetailDto(RollingBeginOn.FIRST_USE, RollingUnitType.YEAR, 1, null);               
        licenceDto.setLicenceDetail(rolling);
        String desc = generatorBug.getLicenceDescription(licenceDto, now);
        Assert.assertEquals("Access period: 1 year(s) from First Use", desc);
    }

    @Test
    public void testRollingFirstUse6() {
        //not used and no expiry date, with start, no end
        LicenceDto licenceDto = new LicenceDto("123", null, false, false, false, false, false);
        RollingLicenceDetailDto rolling = new RollingLicenceDetailDto(RollingBeginOn.FIRST_USE, RollingUnitType.YEAR, 1, null);               
        licenceDto.setLicenceDetail(rolling);
        licenceDto.setStartDateTime(now.plusDays(1));
        licenceDto.setStartDate(licenceDto.getStartDateTime().toLocalDate());
        String desc = generatorBug.getLicenceDescription(licenceDto, now);
        Assert.assertEquals("Access period: 1 year(s) from First Use Subject to Start 22-Feb-2013", desc);
    }

    
    @Test
    public void testRollingFirstUse7() {
        //not used and no expiry date, no start, with end
        LicenceDto licenceDto = new LicenceDto("123", null, false, false, false, false, false);
        RollingLicenceDetailDto rolling = new RollingLicenceDetailDto(RollingBeginOn.FIRST_USE, RollingUnitType.YEAR, 1, null);               
        licenceDto.setLicenceDetail(rolling);
        
        licenceDto.setLicenceDetail(rolling);
        licenceDto.setEndDateTime(now.plusDays(30));
        licenceDto.setEndDate(licenceDto.getEndDateTime().toLocalDate());
        
        String desc = generatorBug.getLicenceDescription(licenceDto, now);
        Assert.assertEquals("Access period: 1 year(s) from First Use Subject to End 23-Mar-2013", desc);
    }
    
    @Test
    public void testRollingFirstUse8() {
        //not used and no expiry date, with start, with end
        LicenceDto licenceDto = new LicenceDto("123", null, false, false, false, false, false);
        RollingLicenceDetailDto rolling = new RollingLicenceDetailDto(RollingBeginOn.FIRST_USE, RollingUnitType.YEAR, 1, null);               
        licenceDto.setLicenceDetail(rolling);
        
        licenceDto.setStartDateTime(now.plusDays(1));
        licenceDto.setStartDate(licenceDto.getStartDateTime().toLocalDate());
        
        licenceDto.setEndDateTime(now.plusDays(30));
        licenceDto.setEndDate(licenceDto.getEndDateTime().toLocalDate());
        
        String desc = generatorBug.getLicenceDescription(licenceDto, now);
        Assert.assertEquals("Access period: 1 year(s) from First Use Subject to Start 22-Feb-2013 End 23-Mar-2013", desc);
    }
    
    @Test
    public void testCreation1() {
        //With expiry date, no start, no end
        LicenceDto licenceDto = new LicenceDto("123", now.plusMonths(11), false, false, false, false, false);
        RollingLicenceDetailDto rolling = new RollingLicenceDetailDto(RollingBeginOn.CREATION, RollingUnitType.YEAR, 1, null);             
        licenceDto.setLicenceDetail(rolling);
        
        String desc = generatorBug.getLicenceDescription(licenceDto, now);
        Assert.assertEquals("Access from 21-Jan-2013 12:00:00 UTC to 21-Jan-2014 12:00:00 UTC", desc);
    }
    
    @Test
    public void testCreation2() {
        //With expiry date, no start, no end
        LicenceDto licenceDto = new LicenceDto("123", now.plusMonths(11), false, false, false, false, false);
        RollingLicenceDetailDto rolling = new RollingLicenceDetailDto(RollingBeginOn.CREATION, RollingUnitType.YEAR, 1, null);             
        licenceDto.setLicenceDetail(rolling);
        
        String desc = generatorBug.getLicenceDescription(licenceDto, now);
        Assert.assertEquals("Access from 21-Jan-2013 12:00:00 UTC to 21-Jan-2014 12:00:00 UTC", desc);
    }
    
    @Test
    public void testCreation3() {

        LicenceDto licenceDto = new LicenceDto("123", now.plusMonths(11), false, false, false, false, false);
        RollingLicenceDetailDto rolling = new RollingLicenceDetailDto(RollingBeginOn.CREATION, RollingUnitType.YEAR, 1, null);             
        licenceDto.setLicenceDetail(rolling);
        
        String desc = generatorBug.getLicenceDescription(licenceDto, now);
        Assert.assertEquals("Access from 21-Jan-2013 12:00:00 UTC to 21-Jan-2014 12:00:00 UTC", desc);
    }

    @Test
    public void testCreation4() {
        //With expiry date, no end date but has start date which is after creation date
        LicenceDto licenceDto = new LicenceDto("123", now.plusMonths(11), false, false, false, false, false);
        RollingLicenceDetailDto rolling = new RollingLicenceDetailDto(RollingBeginOn.CREATION, RollingUnitType.YEAR, 1, null);             
        licenceDto.setLicenceDetail(rolling);
        
        licenceDto.setStartDateTime(now.minusDays(10));
        licenceDto.setStartDate(licenceDto.getStartDateTime().toLocalDate());

        String desc = generatorBug.getLicenceDescription(licenceDto, now);
        Assert.assertEquals("Access from 11-Feb-2013 12:00:00 UTC to 21-Jan-2014 12:00:00 UTC", desc);
    }
    
    @Test
    public void testCreation5() {
        //With expiry date and end date but end date > expiry date
        LicenceDto licenceDto = new LicenceDto("123", now.plusMonths(11), false, false, false, false, false);
        RollingLicenceDetailDto rolling = new RollingLicenceDetailDto(RollingBeginOn.CREATION, RollingUnitType.YEAR, 1, null);             
        licenceDto.setLicenceDetail(rolling);
        
        licenceDto.setEndDateTime(licenceDto.getExpiryDateAndTime().plusMonths(1));
        licenceDto.setEndDate(licenceDto.getEndDateTime().toLocalDate());

        String desc = generatorBug.getLicenceDescription(licenceDto, now);
        Assert.assertEquals("Access from 21-Jan-2013 12:00:00 UTC to 21-Jan-2014 12:00:00 UTC", desc);
    }
    
    @Test
    public void testCreation6() {
        
        DateTime endDate = now.plusMonths(2);
        
        //With expiry date and end date but end date NOT AFTER expiry date, we say 'to expiry date'. ( even though we can't calculate creation date )
        LicenceDto licenceDto = new LicenceDto("123", endDate,  false, false, false, false, false);
        RollingLicenceDetailDto rolling = new RollingLicenceDetailDto(RollingBeginOn.CREATION, RollingUnitType.YEAR, 1, null);             
        licenceDto.setLicenceDetail(rolling);
        
        licenceDto.setEndDateTime(endDate);
        licenceDto.setEndDate(endDate.toLocalDate());

        String desc = generatorBug.getLicenceDescription(licenceDto, now);
        Assert.assertEquals("Access period: 1 year(s) from Registration Subject to End 21-Apr-2013", desc);
    }
    
    @Test
    public void testCreation7() {
        //With expiry date and start date before creation date
        LicenceDto licenceDto = new LicenceDto("123", now.plusMonths(11), false, false, false, false, false);
        RollingLicenceDetailDto rolling = new RollingLicenceDetailDto(RollingBeginOn.CREATION, RollingUnitType.YEAR, 1, null);             
        licenceDto.setLicenceDetail(rolling);
        
        //start date will be ignored
        licenceDto.setStartDateTime(licenceDto.getExpiryDateAndTime().minusMonths(13));
        licenceDto.setStartDate(licenceDto.getStartDateTime().toLocalDate());

        String desc = generatorBug.getLicenceDescription(licenceDto, now);
        Assert.assertEquals("Access from 21-Jan-2013 12:00:00 UTC to 21-Jan-2014 12:00:00 UTC", desc);
    }
    
    @Test
    public void testCreation8() {
        //start date after creation date AND end date after expiry date
        LicenceDto licenceDto = new LicenceDto("123", now.plusMonths(11), false, false, false, false, false);
        RollingLicenceDetailDto rolling = new RollingLicenceDetailDto(RollingBeginOn.CREATION, RollingUnitType.YEAR, 1, null);             
        licenceDto.setLicenceDetail(rolling);
        
        licenceDto.setStartDateTime(now.minusDays(1));
        licenceDto.setStartDate(licenceDto.getStartDateTime().toLocalDate());

        licenceDto.setEndDateTime(licenceDto.getExpiryDateAndTime().plusMonths(1));
        licenceDto.setEndDate(licenceDto.getEndDateTime().toLocalDate());

        String desc = generatorBug.getLicenceDescription(licenceDto, now);
        Assert.assertEquals("Access from 20-Feb-2013 12:00:00 UTC to 21-Jan-2014 12:00:00 UTC", desc);
    }
    
    @Test
    public void testCreation9() {
        //start date before creation date AND end date after expiry date
        LicenceDto licenceDto = new LicenceDto("123", now.plusMonths(11), false, false, false, false, false);
        RollingLicenceDetailDto rolling = new RollingLicenceDetailDto(RollingBeginOn.CREATION, RollingUnitType.YEAR, 1, null);             
        licenceDto.setLicenceDetail(rolling);
        
        licenceDto.setStartDateTime(now.minusDays(35));
        licenceDto.setStartDate(licenceDto.getStartDateTime().toLocalDate());

        licenceDto.setEndDateTime(licenceDto.getExpiryDateAndTime().plusMonths(1));
        licenceDto.setEndDate(licenceDto.getEndDateTime().toLocalDate());

        String desc = generatorBug.getLicenceDescription(licenceDto, now);
        Assert.assertEquals("Access from 21-Jan-2013 12:00:00 UTC to 21-Jan-2014 12:00:00 UTC", desc);
    }

    @Test
    public void testCreation10() {
        //start date after creation date AND end date after expiry date
        LicenceDto licenceDto = new LicenceDto("123", now.plusMonths(11), false, false, false, false, false);
        RollingLicenceDetailDto rolling = new RollingLicenceDetailDto(RollingBeginOn.CREATION, RollingUnitType.YEAR, 1, null);             
        licenceDto.setLicenceDetail(rolling);
        
        licenceDto.setStartDateTime(now.minusDays(1));
        licenceDto.setStartDate(licenceDto.getStartDateTime().toLocalDate());

        licenceDto.setEndDateTime(licenceDto.getExpiryDateAndTime());
        licenceDto.setEndDate(licenceDto.getEndDateTime().toLocalDate());

        String desc = generatorBug.getLicenceDescription(licenceDto, now);
        Assert.assertEquals("Access from 20-Feb-2013 12:00:00 UTC to 21-Jan-2014 12:00:00 UTC", desc);
    }
    
    @Test
    public void testCreation11AtyponBug() {
        //start date after now
        LicenceDto licenceDto = new LicenceDto("123", now.plusMonths(11), false, false, false, false, false);
        RollingLicenceDetailDto rolling = new RollingLicenceDetailDto(RollingBeginOn.CREATION, RollingUnitType.YEAR, 1, null);             
        licenceDto.setLicenceDetail(rolling);
        
        licenceDto.setStartDateTime(now.plusDays(5));
        licenceDto.setStartDate(licenceDto.getStartDateTime().toLocalDate());

        String desc = generatorBug.getLicenceDescription(licenceDto, now);
        Assert.assertEquals("Access period: 1 year(s) from Registration Subject to Start 26-Feb-2013", desc);
    }
    
    @Test
    public void testCreation12AtyponBug() {
        //start date after now, end date too
        LicenceDto licenceDto = new LicenceDto("123", now.plusMonths(11), false, false, false, false, false);
        RollingLicenceDetailDto rolling = new RollingLicenceDetailDto(RollingBeginOn.CREATION, RollingUnitType.YEAR, 1, null);             
        licenceDto.setLicenceDetail(rolling);
        
        licenceDto.setStartDateTime(now.plusDays(5));
        licenceDto.setStartDate(licenceDto.getStartDateTime().toLocalDate());

        licenceDto.setEndDateTime(now.plusMonths(2));
        licenceDto.setEndDate(licenceDto.getEndDateTime().toLocalDate());

        String desc = generatorBug.getLicenceDescription(licenceDto, now);
        Assert.assertEquals("Access period: 1 year(s) from Registration Subject to Start 26-Feb-2013 End 21-Apr-2013", desc);
    }
    
    @Test
    public void testCreation11AtyponBugFixed() {
        //start date after now
        LicenceDto licenceDto = new LicenceDto("123", now.plusMonths(11), false, false, false, false, false);
        RollingLicenceDetailDto rolling = new RollingLicenceDetailDto(RollingBeginOn.CREATION, RollingUnitType.YEAR, 1, null);             
        licenceDto.setLicenceDetail(rolling);
        
        licenceDto.setStartDateTime(now.plusDays(5));
        licenceDto.setStartDate(licenceDto.getStartDateTime().toLocalDate());

        String desc = generatorBugFixed.getLicenceDescription(licenceDto, now);
        Assert.assertEquals("Access from 26-Feb-2013 12:00:00 UTC to 21-Jan-2014 12:00:00 UTC", desc);
    }
    
    @Test
    public void testCreation12AtyponBugFixed() {
        //start date after now, end date too
        LicenceDto licenceDto = new LicenceDto("123", now.plusMonths(11), false, false, false, false, false);
        RollingLicenceDetailDto rolling = new RollingLicenceDetailDto(RollingBeginOn.CREATION, RollingUnitType.YEAR, 1, null);             
        licenceDto.setLicenceDetail(rolling);
        
        licenceDto.setStartDateTime(now.plusDays(5));
        licenceDto.setStartDate(licenceDto.getStartDateTime().toLocalDate());

        licenceDto.setEndDateTime(now.plusMonths(2));
        licenceDto.setEndDate(licenceDto.getEndDateTime().toLocalDate());

        String desc = generatorBugFixed.getLicenceDescription(licenceDto, now);
        Assert.assertEquals("Access from 26-Feb-2013 12:00:00 UTC to 21-Apr-2013 12:00:00 UTC", desc);
    }
    
    @Test
    public void testAtyponBugFixedNoExpiryDate1() {
        //start date < end date < creation date
        LicenceDto licenceDto = new LicenceDto("123", null, false, false, false, false, false);
        RollingLicenceDetailDto rolling = new RollingLicenceDetailDto(RollingBeginOn.CREATION, RollingUnitType.YEAR, 1, null);             
        licenceDto.setLicenceDetail(rolling);
        
        licenceDto.setStartDateTime(now.minusMonths(7));
        licenceDto.setStartDate(licenceDto.getStartDateTime().toLocalDate());

        licenceDto.setEndDateTime(now.minusMonths(6));
        licenceDto.setEndDate(licenceDto.getEndDateTime().toLocalDate());

        String desc = generatorBugFixed.getLicenceDescription(licenceDto, now);
        Assert.assertEquals("Access period: 1 year(s) from Registration Subject to Start 21-Jul-2012 End 21-Aug-2012", desc);
    }
    
    @Test
    public void testAtyponBugFixedNoExpiryDate2() {
        //creation + expiry < start date < end date
        LicenceDto licenceDto = new LicenceDto("123", null, false, false, false, false, false);
        RollingLicenceDetailDto rolling = new RollingLicenceDetailDto(RollingBeginOn.CREATION, RollingUnitType.YEAR, 1, null);             
        licenceDto.setLicenceDetail(rolling);
        
        licenceDto.setStartDateTime(now.plusMonths(24));
        licenceDto.setStartDate(licenceDto.getStartDateTime().toLocalDate());

        licenceDto.setEndDateTime(now.plusMonths(25));
        licenceDto.setEndDate(licenceDto.getEndDateTime().toLocalDate());

        String desc = generatorBugFixed.getLicenceDescription(licenceDto, now);
        Assert.assertEquals("Access period: 1 year(s) from Registration Subject to Start 21-Feb-2015 End 21-Mar-2015", desc);
    }
    
    @Test
    public void testAtyponBugFixedNoExpiryDate3() {
        //
        LicenceDto licenceDto = new LicenceDto("123", null, false, false, false, false, false);
        RollingLicenceDetailDto rolling = new RollingLicenceDetailDto(RollingBeginOn.CREATION, RollingUnitType.YEAR, 1, null);             
        licenceDto.setLicenceDetail(rolling);
        
        String desc = generatorBugFixed.getLicenceDescription(licenceDto, now);
        Assert.assertEquals("Access period: 1 year(s) from Registration", desc);
    }
    
    @Test
    public void testAtyponBugFixedNoExpiryDate4() {
        //
        LicenceDto licenceDto = new LicenceDto("123", null, false, false, false, false, false);
        RollingLicenceDetailDto rolling = new RollingLicenceDetailDto(RollingBeginOn.CREATION, RollingUnitType.YEAR, 1, null);             
        licenceDto.setLicenceDetail(rolling);
        
        licenceDto.setStartDateTime(now.plusMonths(24));
        licenceDto.setStartDate(licenceDto.getStartDateTime().toLocalDate());

        String desc = generatorBugFixed.getLicenceDescription(licenceDto, now);
        Assert.assertEquals("Access period: 1 year(s) from Registration Subject to Start 21-Feb-2015", desc);
    }
    
    @Test
    public void testAtyponBugFixedNoExpiryDate5() {
        //
        LicenceDto licenceDto = new LicenceDto("123", null, false, false, false, false, false);
        RollingLicenceDetailDto rolling = new RollingLicenceDetailDto(RollingBeginOn.CREATION, RollingUnitType.YEAR, 1, null);             
        licenceDto.setLicenceDetail(rolling);
        
        licenceDto.setEndDateTime(now.minusMonths(12));
        licenceDto.setEndDate(licenceDto.getEndDateTime().toLocalDate());

        String desc = generatorBugFixed.getLicenceDescription(licenceDto, now);
        Assert.assertEquals("Access period: 1 year(s) from Registration Subject to End 21-Feb-2012", desc);
    }
    
    @Test
    public void testConcurrentNullStartandEndTimes() {
        LicenceDto licenceDto = new LicenceDto("123", now.plusYears(1),  false, false, false, false, false);
        StandardConcurrentLicenceDetailDto detail = new StandardConcurrentLicenceDetailDto(1, 1);
        licenceDto.setLicenceDetail(detail);
        licenceDto.setStartDate(now.plusDays(2).toLocalDate());
        licenceDto.setEndDate(now.plusDays(5).toLocalDate());
        String desc = generatorBug.getLicenceDescription(licenceDto, now);
        Assert.assertEquals("Start 23-Feb-2013 End 26-Feb-2013", desc);
    }
    
    @Test
    public void testRollingCreationNullStartandEndTimes1() {
        LicenceDto licenceDto = new LicenceDto("123", now.plusYears(1),  false, false, false, false, false);
        RollingLicenceDetailDto rolling = new RollingLicenceDetailDto(RollingBeginOn.CREATION, RollingUnitType.YEAR, 1, null);
        licenceDto.setLicenceDetail(rolling);
        licenceDto.setStartDate(now.plusDays(2).toLocalDate());
        licenceDto.setEndDate(now.plusDays(5).toLocalDate());
        String desc = generatorBug.getLicenceDescription(licenceDto, now);
        Assert.assertEquals("Access period: 1 year(s) from Registration Subject to Start 23-Feb-2013 End 26-Feb-2013", desc);
    }
    
    @Test
    public void testRollingCreationNullStartandEndTimes2() {
        LicenceDto licenceDto = new LicenceDto("123", now.plusYears(1),  false, false, false, false, false);
        RollingLicenceDetailDto rolling = new RollingLicenceDetailDto(RollingBeginOn.CREATION, RollingUnitType.YEAR, 1, null);
        licenceDto.setLicenceDetail(rolling);
        licenceDto.setStartDate(now.plusDays(2).toLocalDate());
        String desc = generatorBug.getLicenceDescription(licenceDto, now);
        Assert.assertEquals("Access period: 1 year(s) from Registration Subject to Start 23-Feb-2013", desc);
    }

    @Test
    public void testRollingCreationNullStartandEndTimes3() {
        LicenceDto licenceDto = new LicenceDto("123", now.plusYears(1),  false, false, false, false, false);
        RollingLicenceDetailDto rolling = new RollingLicenceDetailDto(RollingBeginOn.CREATION, RollingUnitType.YEAR, 1, null);
        licenceDto.setLicenceDetail(rolling);
        licenceDto.setEndDate(now.plusDays(5).toLocalDate());
        String desc = generatorBug.getLicenceDescription(licenceDto, now);
        Assert.assertEquals("Access period: 1 year(s) from Registration Subject to End 26-Feb-2013", desc);
    }

    @Test
    public void testRollingCreationNullStartandEndTimes4() {
        LicenceDto licenceDto = new LicenceDto("123", now.plusYears(1),  false, false, false, false, false);
        RollingLicenceDetailDto rolling = new RollingLicenceDetailDto(RollingBeginOn.CREATION, RollingUnitType.YEAR, 1, null);
        licenceDto.setLicenceDetail(rolling);
        String desc = generatorBug.getLicenceDescription(licenceDto, now);
        Assert.assertEquals("Access from 21-Feb-2013 12:00:00 UTC to 21-Feb-2014 12:00:00 UTC", desc);
    }
    
    @Test
    public void testRollingFirstUseNullStartandEndTimes2() {
        LicenceDto licenceDto = new LicenceDto("123", now.plusYears(1),  false, false, false, false, false);
        RollingLicenceDetailDto rolling = new RollingLicenceDetailDto(RollingBeginOn.FIRST_USE, RollingUnitType.YEAR, 1, null);
        licenceDto.setLicenceDetail(rolling);
        licenceDto.setStartDate(now.plusDays(2).toLocalDate());        
        String desc = generatorBug.getLicenceDescription(licenceDto, now);
        Assert.assertEquals("Access period: 1 year(s) from First Use Subject to Start 23-Feb-2013 End 21-Feb-2014", desc);
    }
    
    @Test
    public void testRollingFirstUseNullStartandEndTimes3() {
        LicenceDto licenceDto = new LicenceDto("123", now.plusYears(1),  false, false, false, false, false);
        RollingLicenceDetailDto rolling = new RollingLicenceDetailDto(RollingBeginOn.FIRST_USE, RollingUnitType.YEAR, 1, null);
        licenceDto.setLicenceDetail(rolling);
        licenceDto.setEndDate(now.plusDays(5).toLocalDate());        
        String desc = generatorBug.getLicenceDescription(licenceDto, now);
        Assert.assertEquals("Access period: 1 year(s) from First Use Subject to End 21-Feb-2014", desc);
    }
    
    @Test
    public void testRollingFirstUseNullStartandEndTimes4() {
        LicenceDto licenceDto = new LicenceDto("123", now.plusYears(1),  false, false, false, false, false);
        RollingLicenceDetailDto rolling = new RollingLicenceDetailDto(RollingBeginOn.FIRST_USE, RollingUnitType.YEAR, 1, null);
        licenceDto.setLicenceDetail(rolling);
        String desc = generatorBug.getLicenceDescription(licenceDto, now);
        Assert.assertEquals("Access period: 1 year(s) from First Use Subject to End 21-Feb-2014", desc);
    }

    @Test
    public void testNumberFormatWithNullLocale(){
        String res1 = null;
        String res2 = null;
        String res3 = null;
        try {
            String fmt = "Access for {0} concurrent users";

            res1 = new MessageFormat(fmt, null).format(new Object[] { "1234" });//strings okay
            res2 = new MessageFormat(fmt, Locale.UK).format(new Object[] { 1234L });//numbers and a locale okay
            res3 = new MessageFormat(fmt, null).format(new Object[] { 1234L });//numbers without a locale - problem

            Assert.fail("exception expected");
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        } finally {
            Assert.assertEquals("Access for 1234 concurrent users", res1);
            Assert.assertEquals("Access for 1,234 concurrent users", res2);
            Assert.assertEquals(null, res3);
        }
    }
}

