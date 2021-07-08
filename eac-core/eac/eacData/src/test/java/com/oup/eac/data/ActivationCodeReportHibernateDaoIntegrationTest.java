/*package com.oup.eac.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.oup.eac.common.utils.activationcode.ActivationCodeGenerator;
import com.oup.eac.common.utils.activationcode.EacNumericActivationCode;
import com.oup.eac.domain.ActivationCode;
import com.oup.eac.domain.ActivationCodeBatch;
import com.oup.eac.domain.ActivationCodeBatch.ActivationCodeFormat;
import com.oup.eac.domain.ActivationCodeRegistrationDefinition;
import com.oup.eac.domain.AdminUser;
import com.oup.eac.domain.ConcurrentLicenceTemplate;
import com.oup.eac.domain.Division;
import com.oup.eac.domain.InstantRegistrationActivation;
import com.oup.eac.domain.LicenceTemplate;
import com.oup.eac.domain.LicenceTemplate.LicenceType;
import com.oup.eac.domain.Product;
import com.oup.eac.domain.RegisterableProduct;
import com.oup.eac.domain.RegisterableProduct.RegisterableType;
import com.oup.eac.domain.RollingLicenceTemplate;
import com.oup.eac.domain.StandardLicenceTemplate;
import com.oup.eac.domain.util.SampleDataFactory;
import com.oup.eac.dto.ActivationCodeBatchReportCriteria;
import com.oup.eac.dto.ActivationCodeDto;

public class ActivationCodeReportHibernateDaoIntegrationTest extends AbstractDBTest {
	
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    
    @Autowired
    private ActivationCodeBatchDao activationCodeBatchDao;
    
    private ActivationCodeGenerator eacNumericActivationCodeGenerator = new EacNumericActivationCode();
    
    private ActivationCodeBatch activationCodeBatch1, activationCodeBatch2, activationCodeBatch3;
    
    private AdminUser adminUser1;
    
    private StandardLicenceTemplate standardLicenceTemplate;
    
    private ConcurrentLicenceTemplate concurrentLicenceTemplate;
    
    private RollingLicenceTemplate rollingLicenceTemplate;
    
    private RegisterableProduct malaysiaProduct1;
    
    //private Asset asset1;
    
    private DateTime startDate = new DateTime().minusDays(5);
    
    private DateTime endDate = new DateTime();
    
    private Division malaysiaDivision, malaysiaDivision2;
    
    *//**
     * @throws Exception
     *             Sets up data ready for test.
     *//*
    @Before
    public final void setUp() throws Exception {

        malaysiaDivision = getSampleDataCreator().createDivision("MALAYSIA");
        malaysiaDivision2 = getSampleDataCreator().createDivision("CANADA");
        adminUser1 = getSampleDataCreator().createAdminUser();
        getSampleDataCreator().createDivisionAdminUser(malaysiaDivision, adminUser1);
        //asset1 = getSampleDataCreator().createAsset(Integer.valueOf(1), malaysiaDivision);
        malaysiaProduct1 = getSampleDataCreator().createRegisterableProduct(201193, "SampleTestProduct_01", RegisterableType.SELF_REGISTERABLE);
        InstantRegistrationActivation instantLicenceActivation = getSampleDataCreator().createInstantRegistrationActivation();
        standardLicenceTemplate = getSampleDataCreator().createStandardLicenceTemplate();
        concurrentLicenceTemplate = getSampleDataCreator().createConcurrentLicenceTemplate();
        rollingLicenceTemplate = getSampleDataCreator().createRollingLicenceTemplate();
        ActivationCodeRegistrationDefinition malaysiaActivationCodeRegistrationDefinition1 = getSampleDataCreator().createActivationCodeRegistrationDefinition(malaysiaProduct1, standardLicenceTemplate, instantLicenceActivation, null);
        activationCodeBatch1 = SampleDataFactory.createActivationCodeBatch(ActivationCodeFormat.EAC_NUMERIC, standardLicenceTemplate, malaysiaActivationCodeRegistrationDefinition1, startDate, endDate, "batch1");
        getSampleDataCreator().loadActivationCodeBatch(activationCodeBatch1);
        activationCodeBatch2 = SampleDataFactory.createActivationCodeBatch(ActivationCodeFormat.EAC_NUMERIC, concurrentLicenceTemplate, malaysiaActivationCodeRegistrationDefinition1, startDate, endDate, "batch2");
        getSampleDataCreator().loadActivationCodeBatch(activationCodeBatch2);
        activationCodeBatch3 = SampleDataFactory.createActivationCodeBatch(ActivationCodeFormat.EAC_NUMERIC, rollingLicenceTemplate, malaysiaActivationCodeRegistrationDefinition1, startDate, endDate, "batch3");
        getSampleDataCreator().loadActivationCodeBatch(activationCodeBatch3);
        ActivationCode activationCode = SampleDataFactory.createActivationCode(activationCodeBatch1, eacNumericActivationCodeGenerator);
        ActivationCode activationCode2 = SampleDataFactory.createActivationCode(activationCodeBatch2, eacNumericActivationCodeGenerator);
        ActivationCode activationCode3 = SampleDataFactory.createActivationCode(activationCodeBatch3, eacNumericActivationCodeGenerator);
        ActivationCode activationCode4 = SampleDataFactory.createActivationCode(activationCodeBatch1, eacNumericActivationCodeGenerator);
        createActivationCode(activationCode, 10, 10);
        createActivationCode(activationCode2, 10, 10);
        createActivationCode(activationCode3, 10, 10);
        createActivationCode(activationCode4, 6, 10);
        loadAllDataSets();
    }

    private void createActivationCode(ActivationCode activationCode, int actual, int allowed)
            throws Exception {
        activationCode.setActualUsage(actual);
        activationCode.setAllowedUsage(allowed);
        getSampleDataCreator().loadActivationCode(activationCode);
    }
    
    @Test
    public void testBatchAllCount() {
        ActivationCodeBatchReportCriteria activationCodeReportCriteria = new ActivationCodeBatchReportCriteria(null, null , activationCodeBatch1.getBatchId(), 1000);
        Long count = activationCodeBatchDao.getActivationCodeReportCount(activationCodeReportCriteria);
        assertNotNull(count);
        assertEquals(1, count.intValue());
    }    
    
    @Test
    public void testBatchAllCountBatch() {
        ActivationCodeBatchReportCriteria activationCodeReportCriteria = new ActivationCodeBatchReportCriteria(null, null , null, 1000);
        Long count = activationCodeBatchDao.getActivationCodeReportCount(activationCodeReportCriteria);
        assertNotNull(count);
        assertEquals(3, count.intValue());
    }    
    
    @Test
    public void testBatchAllNull() {
        ActivationCodeBatchReportCriteria activationCodeReportCriteria = new ActivationCodeBatchReportCriteria(null, null , null, 1000);
        List<ActivationCodeDto> activationCodeBatches = activationCodeBatchDao.getActivationCodeReport(activationCodeReportCriteria);
        assertNotNull(activationCodeBatches);
        assertEquals(3, activationCodeBatches.size());
        checkDto(activationCodeBatches.get(0), standardLicenceTemplate, activationCodeBatch1, malaysiaProduct1, asset1, 1, 1, 20, 16);
    }
    
    @Test
    public void testBatch() {
        ActivationCodeBatchReportCriteria activationCodeReportCriteria = new ActivationCodeBatchReportCriteria(null, null ,activationCodeBatch1.getBatchId(), 1000);
        List<ActivationCodeDto> activationCodeBatches = activationCodeBatchDao.getActivationCodeReport(activationCodeReportCriteria);
        assertNotNull(activationCodeBatches);
        assertEquals(1, activationCodeBatches.size());
        checkDto(activationCodeBatches.get(0), standardLicenceTemplate, activationCodeBatch1, malaysiaProduct1, asset1, 1, 1, 20, 16);
    }

    @Test
    public void testBatchValidDivisionAndBatchId() {
        ActivationCodeBatchReportCriteria activationCodeReportCriteria = new ActivationCodeBatchReportCriteria(malaysiaDivision.getId(), null ,activationCodeBatch1.getBatchId(), 1000);
        List<ActivationCodeDto> activationCodeBatches = activationCodeBatchDao.getActivationCodeReport(activationCodeReportCriteria);
        assertNotNull(activationCodeBatches);
        assertEquals(1, activationCodeBatches.size());
        checkDto(activationCodeBatches.get(0), standardLicenceTemplate, activationCodeBatch1, malaysiaProduct1, asset1, 1, 1, 20, 16);
    }
    
    @Test
    public void testBatchValidDivisionAndBatchIdAndProduct() {
        ActivationCodeBatchReportCriteria activationCodeReportCriteria = new ActivationCodeBatchReportCriteria(malaysiaDivision.getId(), malaysiaProduct1.getId() ,activationCodeBatch1.getBatchId(), 1000);
        List<ActivationCodeDto> activationCodeBatches = activationCodeBatchDao.getActivationCodeReport(activationCodeReportCriteria);
        assertNotNull(activationCodeBatches);
        assertEquals(1, activationCodeBatches.size());
        checkDto(activationCodeBatches.get(0), standardLicenceTemplate, activationCodeBatch1, malaysiaProduct1, asset1, 1, 1, 20, 16);
    }
    
    @Test
    public void testBatchInvalidDivision() {
        ActivationCodeBatchReportCriteria activationCodeReportCriteria = new ActivationCodeBatchReportCriteria(malaysiaDivision2.getId(), null ,activationCodeBatch1.getBatchId(), 1000);
        List<ActivationCodeDto> activationCodeBatches = activationCodeBatchDao.getActivationCodeReport(activationCodeReportCriteria);
        assertNotNull(activationCodeBatches);
        assertEquals(0, activationCodeBatches.size());
    }
    
    @Test
    public void testBatchConcurrentLicence() {
        ActivationCodeBatchReportCriteria activationCodeReportCriteria = new ActivationCodeBatchReportCriteria(null, null ,activationCodeBatch2.getBatchId(), 1000);
        List<ActivationCodeDto> activationCodeBatches = activationCodeBatchDao.getActivationCodeReport(activationCodeReportCriteria);
        assertNotNull(activationCodeBatches);
        assertEquals(1, activationCodeBatches.size());
        checkDto(activationCodeBatches.get(0), concurrentLicenceTemplate, activationCodeBatch2, malaysiaProduct1, asset1, 1, 0, 10, 10);
    }
    
    @Test
    public void testBatchConcurrentLicenceWithDivision() {
        ActivationCodeBatchReportCriteria activationCodeReportCriteria = new ActivationCodeBatchReportCriteria(malaysiaDivision.getId(), null ,activationCodeBatch2.getBatchId(), 1000);
        List<ActivationCodeDto> activationCodeBatches = activationCodeBatchDao.getActivationCodeReport(activationCodeReportCriteria);
        assertNotNull(activationCodeBatches);
        assertEquals(1, activationCodeBatches.size());
        checkDto(activationCodeBatches.get(0), concurrentLicenceTemplate, activationCodeBatch2, malaysiaProduct1, asset1, 1, 0, 10, 10);
    }
    
    @Test
    public void testBatchRollingLicence() {
        ActivationCodeBatchReportCriteria activationCodeReportCriteria = new ActivationCodeBatchReportCriteria(null, null ,activationCodeBatch3.getBatchId(), 1000);
        List<ActivationCodeDto> activationCodeBatches = activationCodeBatchDao.getActivationCodeReport(activationCodeReportCriteria);
        assertNotNull(activationCodeBatches);
        assertEquals(1, activationCodeBatches.size());
        checkDto(activationCodeBatches.get(0), rollingLicenceTemplate, activationCodeBatch3, malaysiaProduct1, asset1, 1, 0, 10, 10);
    }
    
    @Test
    public void testBatchRollingLicenceWithDivision() {
        ActivationCodeBatchReportCriteria activationCodeReportCriteria = new ActivationCodeBatchReportCriteria(malaysiaDivision.getId(), null ,activationCodeBatch3.getBatchId(), 1000);
        List<ActivationCodeDto> activationCodeBatches = activationCodeBatchDao.getActivationCodeReport(activationCodeReportCriteria);
        assertNotNull(activationCodeBatches);
        assertEquals(1, activationCodeBatches.size());
        checkDto(activationCodeBatches.get(0), rollingLicenceTemplate, activationCodeBatch3, malaysiaProduct1, asset1, 1, 0, 10, 10);
    }
    
    private void checkDto(ActivationCodeDto activationCodeDto, LicenceTemplate licenceTemplate, ActivationCodeBatch activationCodeBatch, Product product, Asset asset, 
                            int codesWithNoUsages, int codesWithUsages, int totalAllowed, int totalActual) {
        assertEquals(activationCodeBatch.getId(), activationCodeDto.getBatchId());
        assertEquals(activationCodeBatch.getBatchId(), activationCodeDto.getBatchName());
        assertTrue(startDate.toString(DATE_TIME_FORMAT).equals(activationCodeDto.getBatchStartDate().toString(DATE_TIME_FORMAT)));
        assertTrue(endDate.toString(DATE_TIME_FORMAT).equals(activationCodeDto.getBatchEndDate().toString(DATE_TIME_FORMAT)));
        assertEquals(product.getId(), activationCodeDto.getProductId());
        assertEquals(asset.getProductName(), activationCodeDto.getAssetName());
        assertEquals(licenceTemplate.getLicenceType().toString(), activationCodeDto.getLicenceType());
        assertTrue(licenceTemplate.getStartDate().toString(DATE_TIME_FORMAT).equals(activationCodeDto.getLicenceStartDate().toString(DATE_TIME_FORMAT)));
        assertTrue(licenceTemplate.getEndDate().toString(DATE_TIME_FORMAT).equals(activationCodeDto.getLicenceEndDate().toString(DATE_TIME_FORMAT)));
        assertEquals(codesWithNoUsages, activationCodeDto.getActivationCodeWithNoUsages().intValue());
        assertEquals(codesWithUsages, activationCodeDto.getActivationCodeWithAvailableUsages().intValue());
        assertEquals(totalAllowed, activationCodeDto.getTotalAllowedUsages().intValue());
        assertEquals(totalActual, activationCodeDto.getTotalActualUsages().intValue());
        assertEquals(licenceTemplate.getLicenceType().toString(), activationCodeDto.getLicenceType());
        if(licenceTemplate.getLicenceType() == LicenceType.CONCURRENT) {
            ConcurrentLicenceTemplate concurrentLicenceTemplate = (ConcurrentLicenceTemplate)licenceTemplate;
            assertEquals(concurrentLicenceTemplate.getTotalConcurrency(), activationCodeDto.getLicenceTotalConcurrency().intValue());
        }
        if(licenceTemplate.getLicenceType() == LicenceType.ROLLING) {
            RollingLicenceTemplate rollingLicenceTemplate = (RollingLicenceTemplate)licenceTemplate;
            assertEquals(rollingLicenceTemplate.getUnitType(), activationCodeDto.getLicenceUnitType());
            assertEquals(rollingLicenceTemplate.getBeginOn(), activationCodeDto.getLicenceBeginOn());
        }
    }
    

    
}
*/