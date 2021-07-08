package com.oup.eac.service.impl;

import static org.easymock.EasyMock.expect;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.naming.NamingException;

import junit.framework.Assert;

import org.apache.velocity.app.VelocityEngine;
import org.easymock.EasyMock;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.context.MessageSource;

import com.oup.eac.common.mock.AbstractMockTest;
import com.oup.eac.common.utils.EACSettings;
import com.oup.eac.data.ActivationCodeBatchDao;
import com.oup.eac.domain.MailCriteria;
import com.oup.eac.domain.RollingLicenceTemplate.RollingBeginOn;
import com.oup.eac.domain.RollingLicenceTemplate.RollingUnitType;
import com.oup.eac.dto.ActivationCodeBatchReportCriteria;
import com.oup.eac.dto.ActivationCodeDto;
import com.oup.eac.service.EmailService;

@Ignore
public class ActivationCodeBatchReportServiceImplTest extends AbstractMockTest {
    
    private static final String DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm:ss";
    private static final String DATE_FORMAT = "dd/MM/yyyy";

    private static final String ER_DB_NAME = EACSettings.getProperty("rs.db.name") ;
    private static final String ER_DB_SCHEMA = EACSettings.getProperty("rs.db.schema") ;
    private static final String ER_DB_NAME_WITH_SCHEMA = ER_DB_NAME + "." + ER_DB_SCHEMA  ;

    
    private static final String BATCH_ID_COL = "batch id";
    private static final String BATCH_CREATED_DATE_COL = "batch created date";
    private static final String BATCH_START_DATE_COL = "batch start date";
    private static final String BATCH_END_DATE_COL = "batch end date";

    private static final String PRODUCT_ID_COL = "product id";

    
    private static final String LICENCE_TYPE_COL = "licence type";
    private static final String LICENCE_START_DATE_COL = "licence start date";
    private static final String LICENCE_END_DATE_COL = "licence end date";
    private static final String LICENCE_CONCURRENCY_COL = "licence concurrency";
    private static final String LICENCE_BEGIN_ON_COL = "licence begin on";
    private static final String LICENCE_PERIOD_COL = "licence period";
    private static final String LICENCE_UNITS_COL = "licence units";
    private static final String LICENCE_ALLOWED_USAGE_COL = "licence allowed usage";
    
    private static final String CODES_NUMBER_COL = "number of codes";
    private static final String CODES_ALLOWED_USAGE_COL = "allowed usages";
    private static final String CODES_ACTUAL_USAGE_COL = "actual usages";
    private static final String CODES_TOTAL_AVAILABLE_COL = "total available";
    private static final String CODES_TOTAL_UNAVAILABLE_COL = "total unavailable";

    
    private String batchId = "id";
    
    private String batchName = "batch id";
    
    private DateTime batchCreatedDate = new DateTime();
    
    private Long count = Long.valueOf(10);
    
    private Long activationCodeWithNoUsages = Long.valueOf(10);
    
    private Long activationCodeWithAvailableUsages = Long.valueOf(10);
    
    private Long totalAllowedUsages = Long.valueOf(10);
    
    private Long totalActualUsages = Long.valueOf(10);
    
    private String productId = "product id";
    
    private DateTime batchStartDate = new DateTime();
    
    private DateTime batchEndDate = new DateTime();
    
    private String licenceType = "Rolling";
    
    private LocalDate licenceStartDate = new LocalDate();

    private LocalDate licenceEndDate = new LocalDate();

    private Integer licenceTotalConcurrency = Integer.valueOf(1);
    
    private RollingUnitType licenceUnitType = RollingUnitType.HOUR;
    
    private RollingBeginOn licenceBeginOn = RollingBeginOn.CREATION;
    
    private Integer licenceTimePeriod = Integer.valueOf(1);
    
    private Integer licenceAllowedUsages = Integer.valueOf(4);    
    
    /**
     * Create a new test.
     * 
     * @throws NamingException
     *             the exception
     */
    public ActivationCodeBatchReportServiceImplTest() throws NamingException {
        super();
    }

    private ActivationCodeBatchReportServiceImpl service;
    private EmailService emailService;
    private VelocityEngine velocityEngine;
    private MessageSource messageSource;
    private ActivationCodeBatchDao activationCodeBatchDao;
    
    /**
     * @throws Exception
     *             Sets up data and create mocks ready for testing.
     */
    @Before
    public final void setUp() throws Exception {
        emailService = EasyMock.createMock(EmailService.class);
        velocityEngine = new VelocityEngine();
        messageSource = EasyMock.createMock(MessageSource.class);
        activationCodeBatchDao = EasyMock.createMock(ActivationCodeBatchDao.class);
        velocityEngine.setProperty("resource.loader", "class");
        velocityEngine.setProperty("runtime.log.logsystem.class", "org.apache.velocity.runtime.log.Log4JLogChute");
        velocityEngine.setProperty("runtime.log.logsystem.log4j.logger", "org.apache.velocity");
        velocityEngine.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        setMocks(emailService, messageSource, activationCodeBatchDao);
        service = new ActivationCodeBatchReportServiceImpl(emailService, velocityEngine, messageSource, activationCodeBatchDao, null, null, null);
    }
    
    @Test
    public void testMap() throws Exception {
        ActivationCodeDto activationCodeDto = new ActivationCodeDto(batchId, batchName, batchCreatedDate, count, activationCodeWithNoUsages, activationCodeWithAvailableUsages, totalAllowedUsages, totalActualUsages, 
                                                                    batchStartDate, batchEndDate, productId, licenceType, licenceStartDate, licenceEndDate, licenceTotalConcurrency, licenceUnitType, licenceBeginOn, licenceTimePeriod, 
                                                                    licenceAllowedUsages);
        ActivationCodeDto[] actDtos = new ActivationCodeDto[]{activationCodeDto};
        ActivationCodeBatchReportCriteria activationCodeReportCriteria = new ActivationCodeBatchReportCriteria();
        Map<String, String> map = service.getActivationCodeData(actDtos, activationCodeReportCriteria);
        
        Assert.assertEquals(batchName, map.get(BATCH_ID_COL));
        Assert.assertEquals(batchCreatedDate.toString(DATE_TIME_FORMAT), map.get(BATCH_CREATED_DATE_COL));
        Assert.assertEquals(batchStartDate.toString(DATE_FORMAT), map.get(BATCH_START_DATE_COL));
        Assert.assertEquals(batchEndDate.toString(DATE_FORMAT), map.get(BATCH_END_DATE_COL));
        
        Assert.assertEquals(productId, map.get(PRODUCT_ID_COL));
        
        Assert.assertEquals(licenceType, map.get(LICENCE_TYPE_COL));
        Assert.assertEquals(licenceStartDate.toString(DATE_FORMAT), map.get(LICENCE_START_DATE_COL));
        Assert.assertEquals(licenceEndDate.toString(DATE_FORMAT), map.get(LICENCE_END_DATE_COL));
        Assert.assertEquals(licenceTotalConcurrency.toString(), map.get(LICENCE_CONCURRENCY_COL));
        Assert.assertEquals(licenceBeginOn.toString(), map.get(LICENCE_BEGIN_ON_COL));
        Assert.assertEquals(licenceTimePeriod.toString(), map.get(LICENCE_PERIOD_COL));
        Assert.assertEquals(licenceUnitType.toString(), map.get(LICENCE_UNITS_COL));
        Assert.assertEquals(licenceAllowedUsages.toString(), map.get(LICENCE_ALLOWED_USAGE_COL));
        
        Assert.assertEquals(count.toString(), map.get(CODES_NUMBER_COL));
        Assert.assertEquals(totalAllowedUsages.toString(), map.get(CODES_ALLOWED_USAGE_COL));
        Assert.assertEquals(totalActualUsages.toString(), map.get(CODES_ACTUAL_USAGE_COL));
        Assert.assertEquals(activationCodeWithAvailableUsages.toString(), map.get(CODES_TOTAL_AVAILABLE_COL));
        Assert.assertEquals(activationCodeWithNoUsages.toString(), map.get(CODES_TOTAL_UNAVAILABLE_COL));
    }
    
    @Test
    public void testCreateActivationCodeReport() throws Exception {
        ActivationCodeBatchReportCriteria activationCodeReportCriteria = new ActivationCodeBatchReportCriteria();
        ActivationCodeDto activationCodeDto = new ActivationCodeDto(batchId, batchName, batchCreatedDate, count, activationCodeWithNoUsages, activationCodeWithAvailableUsages, totalAllowedUsages, totalActualUsages,
                                                                    batchStartDate, batchEndDate, productId, licenceType, licenceStartDate, licenceEndDate, licenceTotalConcurrency, licenceUnitType, licenceBeginOn, licenceTimePeriod, 
                                                                    licenceAllowedUsages);
        
        ActivationCodeDto[] actDtos = new ActivationCodeDto[]{activationCodeDto};
        List<Object[]> list = new ArrayList<Object[]>();
        list.add(actDtos);
        EasyMock.expect(activationCodeBatchDao.getActivationCodeReport(activationCodeReportCriteria, ER_DB_NAME_WITH_SCHEMA)).andReturn(list);
        expect(messageSource.getMessage(EasyMock.isA(String.class), EasyMock.isA(Object[].class), EasyMock.isA(Locale.class))).andReturn("a string").times(9);
        emailService.sendMail(EasyMock.isA(MailCriteria.class));
        EasyMock.expectLastCall();
        
        replayMocks();
        service.createActivationCodeReport(activationCodeReportCriteria, "test@mailinator.com");
        verifyMocks();
    }
    

}
