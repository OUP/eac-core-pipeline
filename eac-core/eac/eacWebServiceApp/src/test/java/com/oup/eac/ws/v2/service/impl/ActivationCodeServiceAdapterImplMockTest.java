package com.oup.eac.ws.v2.service.impl;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.naming.NamingException;

import junit.framework.Assert;

import org.apache.velocity.app.VelocityEngine;
import org.easymock.EasyMock;
import org.easymock.IArgumentMatcher;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.AccessDeniedException;

import com.oup.eac.common.mock.AbstractMockTest;
import com.oup.eac.data.ActivationCodeBatchDao;
import com.oup.eac.data.ActivationCodeDao;
import com.oup.eac.data.AnswerDao;
import com.oup.eac.data.CustomerDao;
import com.oup.eac.data.ExternalIdDao;
import com.oup.eac.data.ExternalSystemDao;
import com.oup.eac.data.ExternalSystemIdTypeDao;
import com.oup.eac.data.RegistrationDao;
import com.oup.eac.data.RegistrationDefinitionDao;
import com.oup.eac.domain.ActivationCode;
import com.oup.eac.domain.ActivationCodeBatch;
import com.oup.eac.domain.ConcurrentLicenceTemplate;
import com.oup.eac.domain.LicenceTemplate;
import com.oup.eac.domain.LicenceTemplate.LicenceType;
import com.oup.eac.domain.RollingLicenceTemplate.RollingBeginOn;
import com.oup.eac.domain.RollingLicenceTemplate.RollingUnitType;
import com.oup.eac.domain.MailCriteria;
import com.oup.eac.domain.Product;
import com.oup.eac.domain.RegisterableProduct;
import com.oup.eac.domain.RollingLicenceTemplate;
import com.oup.eac.domain.StandardLicenceTemplate;
import com.oup.eac.domain.UsageLicenceTemplate;
import com.oup.eac.dto.ActivationCodeBatchDto;
import com.oup.eac.dto.EnforceableProductDto;
import com.oup.eac.dto.StandardLicenceDetailDto;
import com.oup.eac.dto.licence.LicenceDescriptionGeneratorSource;
import com.oup.eac.integration.facade.ErightsFacade;
import com.oup.eac.integration.facade.exceptions.ErightsException;
import com.oup.eac.integration.facade.exceptions.GroupNotFoundException;
import com.oup.eac.integration.facade.exceptions.LicenseNotFoundException;
import com.oup.eac.integration.facade.exceptions.ProductNotFoundException;
import com.oup.eac.integration.facade.exceptions.UserNotFoundException;
import com.oup.eac.service.ActivationCodeService;
import com.oup.eac.service.EacGroupService;
import com.oup.eac.service.EmailService;
import com.oup.eac.service.LicenceService;
import com.oup.eac.service.PageDefinitionService;
import com.oup.eac.service.ProductService;
import com.oup.eac.service.RegistrationDefinitionService;
import com.oup.eac.service.ServiceLayerException;
import com.oup.eac.service.exceptions.ServiceLayerValidationException;
import com.oup.eac.service.impl.ActivationCodeServiceImpl;
import com.oup.eac.ws.v2.binding.access.CreateActivationCodeBatchResponse;
import com.oup.eac.ws.v2.binding.common.ActivationCodeLicence;
import com.oup.eac.ws.v2.binding.common.ConcurrencyLicenceDetails;
import com.oup.eac.ws.v2.binding.common.ExtendedLicenceDetails;
import com.oup.eac.ws.v2.binding.common.ExternalIdentifier;
import com.oup.eac.ws.v2.binding.common.Identifier;
import com.oup.eac.ws.v2.binding.common.LicenceDetails;
import com.oup.eac.ws.v2.binding.common.RollingLicenceDetail;
import com.oup.eac.ws.v2.binding.common.UsageLicenceDetails;
import com.oup.eac.ws.v2.binding.common.types.BeginEnum;
import com.oup.eac.ws.v2.binding.common.types.CodeFormatEnum;
import com.oup.eac.ws.v2.binding.common.types.StatusCode;
import com.oup.eac.ws.v2.binding.common.types.UnitEnum;
import com.oup.eac.ws.v2.ex.WebServiceException;
import com.oup.eac.ws.v2.ex.WebServiceValidationException;
import com.oup.eac.ws.v2.service.WsProductLookup;
import com.oup.eac.ws.v2.service.util.TestIdUtils;

public class ActivationCodeServiceAdapterImplMockTest extends AbstractMockTest {

    public ActivationCodeServiceAdapterImplMockTest() throws NamingException {
        super();
    }

    private ActivationCodeServiceAdapterImpl sut;
    private ActivationCodeService mActivationCodeService;
    private WsProductLookup mProductLookup;
    private EmailService emailService;
    private ActivationCodeBatchDao activationCodeBatchDao;
    private ActivationCodeDao activationCodeDao;
    private RegistrationDefinitionDao registrationDefinitionDao;
    @Autowired
    private ErightsFacade erightsFacade;
    private ProductService productService;
    private EacGroupService eacGroupService;
    private MessageSource messageSource;
    private VelocityEngine velocityEngine;
    
    
 

    @Before
    public void setup() {
    	emailService = new EmailService() {
			
			@Override
			public void sendMail(MailCriteria mailCriteria) {
				// TODO Auto-generated method stub
				
			}
		};
		velocityEngine = createMock(VelocityEngine.class);
		activationCodeBatchDao = createMock(ActivationCodeBatchDao.class);
		activationCodeDao = createMock(ActivationCodeDao.class);
		registrationDefinitionDao = createMock(RegistrationDefinitionDao.class);
		//erightsFacade = createMock(ErightsFacade.class);
        productService = createMock(ProductService.class);
        eacGroupService = createMock(EacGroupService.class);
        messageSource = createMock(MessageSource.class);
       
    	 mProductLookup = createMock(WsProductLookup.class);
    	 mActivationCodeService = createMock(ActivationCodeService.class);
         setMocks(velocityEngine, activationCodeBatchDao, activationCodeDao, registrationDefinitionDao,  productService, eacGroupService, messageSource, mProductLookup, mActivationCodeService);
         /*mActivationCodeService = new ActivationCodeServiceImpl(activationCodeBatchDao, activationCodeDao, registrationDefinitionDao, erightsFacade, productService, eacGroupService, messageSource, velocityEngine, emailService);*/
       
        sut = new ActivationCodeServiceAdapterImpl(mActivationCodeService, mProductLookup);
    }

    @Test
    public void testCreateBatchWithRollingLicence() throws ServiceLayerException, WebServiceException, ProductNotFoundException, UserNotFoundException, LicenseNotFoundException, AccessDeniedException, GroupNotFoundException, ErightsException {
        
        RollingLicenceDetail rollingDetails = new RollingLicenceDetail();
        rollingDetails.setPeriodUnit(UnitEnum.DAY);
        rollingDetails.setPeriodValue(365);

        Date start = getRandomDate();
        Date end = getRandomDate();
        
        ExtendedLicenceDetails ex = new ExtendedLicenceDetails();
        ex.setRollingLicence(rollingDetails);
        checkCreateBatch(ex, start, end);
    }

    @Test
    public void testCreateBatchWithStandardLicence() throws ServiceLayerException, WebServiceException, ProductNotFoundException, UserNotFoundException, LicenseNotFoundException, AccessDeniedException, GroupNotFoundException, ErightsException {
        
        
        Date start = getRandomDate();
        Date end = getRandomDate();
        checkStandardCreateBatch(null, start, end);
    }
    
    @Test
    public void testCreateBatchWithUsageLicence() throws ServiceLayerException, WebServiceException, ProductNotFoundException, UserNotFoundException, LicenseNotFoundException, AccessDeniedException, GroupNotFoundException, ErightsException {
        UsageLicenceDetails usage = new UsageLicenceDetails();
        usage.setAllowedUsages(123);

        Date start = getRandomDate();
        Date end = getRandomDate();
        
        ExtendedLicenceDetails ex = new ExtendedLicenceDetails();
        ex.setUsageLicence(usage);
        
        checkCreateBatch(ex, start, end);
    }
    
    @Test
    public void testCreateBatchWithConcurrentLicence() throws ServiceLayerException, WebServiceException, ProductNotFoundException, UserNotFoundException, LicenseNotFoundException, AccessDeniedException, GroupNotFoundException, ErightsException {
        ConcurrencyLicenceDetails con = new ConcurrencyLicenceDetails();
        con.setConcurrency(123);

        Date start = getRandomDate();
        Date end = getRandomDate();
        
        ExtendedLicenceDetails ex = new ExtendedLicenceDetails();
        ex.setConcurrentLicence(con);
        
        checkCreateBatch(ex, start, end);
    }

    private void checkCreateBatch(ExtendedLicenceDetails exLicDetails, Date start, Date end) throws ServiceLayerException, WebServiceException, ProductNotFoundException, UserNotFoundException, LicenseNotFoundException, AccessDeniedException, GroupNotFoundException, ErightsException {

        com.oup.eac.ws.v2.binding.common.ActivationCodeBatch batch = new com.oup.eac.ws.v2.binding.common.ActivationCodeBatch();
        batch.setBatchId(UUID.randomUUID().toString());// random batch id
        batch.setAllowedUsages(123);
        batch.setCodeFormat(CodeFormatEnum.EAC_NUMERIC);
        batch.setNumberOfTokens(321);
        batch.setValidFrom(new org.exolab.castor.types.Date(start.getTime()));
        batch.setValidTo(new org.exolab.castor.types.Date(end.getTime()));

        ActivationCodeLicence acl  = new ActivationCodeLicence();
        LicenceDetails details = new LicenceDetails();
        details.setEnabled(true);
        details.setStartDate(new org.exolab.castor.types.Date(start));
        details.setEndDate(new org.exolab.castor.types.Date(end));
        details.setExtendedDetails(exLicDetails);
        
        acl.setLicenceDetails(details);
        acl.setProductId(TestIdUtils.getInternalId(UUID.randomUUID().toString()));
        
        checkCreateActivationCodeBatch(batch, acl);
    }
    
    private void checkStandardCreateBatch(ExtendedLicenceDetails exLicDetails, Date start, Date end) throws ServiceLayerException, WebServiceException, ProductNotFoundException, UserNotFoundException, LicenseNotFoundException, AccessDeniedException, GroupNotFoundException, ErightsException {

        com.oup.eac.ws.v2.binding.common.ActivationCodeBatch batch = new com.oup.eac.ws.v2.binding.common.ActivationCodeBatch();
        batch.setBatchId(UUID.randomUUID().toString());// random batch id
        batch.setAllowedUsages(123);
        batch.setCodeFormat(CodeFormatEnum.EAC_NUMERIC);
        batch.setNumberOfTokens(321);
        batch.setValidFrom(new org.exolab.castor.types.Date(start.getTime()));
        batch.setValidTo(new org.exolab.castor.types.Date(end.getTime()));

        ActivationCodeLicence acl  = new ActivationCodeLicence();
        LicenceDetails details = new LicenceDetails();
        details.setEnabled(true);
        details.setStartDate(new org.exolab.castor.types.Date(start));
        details.setEndDate(new org.exolab.castor.types.Date(end));
        details.setExtendedDetails(exLicDetails);
        
        acl.setLicenceDetails(details);
        acl.setProductId(TestIdUtils.getInternalId(UUID.randomUUID().toString()));
        
        ActivationCodeBatch actCodeBatch = new ActivationCodeBatch();
    	List<ActivationCode> activationCodes = new ArrayList<ActivationCode>();
    	ActivationCode actCode = new ActivationCode();
    	actCode.setCode("1234567819");
    	actCodeBatch.setActivationCodes(activationCodes);
        
    	 mProductLookup.checkOupIdPattern(acl.getProductId().getInternalId().getId());
         expectLastCall();
        
        EasyMock.expect(mActivationCodeService.saveActivationCodeBatchWithTemplate(
                EasyMock.anyObject(ActivationCodeBatch.class), 
                EasyMock.anyObject(EnforceableProductDto.class), 
                EasyMock.anyObject(LicenceTemplate.class),
                EasyMock.eq(batch.getNumberOfTokens()),
                EasyMock.eq(batch.getAllowedUsages()))).andReturn(actCodeBatch);
        
        EasyMock.expectLastCall();

        
        // 1 REPLAY
         replay(getMocks());

         // 2 CALL THE METHOD WE ARE TESTING!!!!
         CreateActivationCodeBatchResponse result = sut.createActivationCodeBatch(batch, acl);

        // 3 PERFORM ASSERTIONS ON RESPONSE
         Assert.assertNull(result.getErrorStatus());

         // 3 VERIFY
         verify(getMocks());
    }

    private void checkCreateActivationCodeBatch(com.oup.eac.ws.v2.binding.common.ActivationCodeBatch activationCodeBatch, ActivationCodeLicence acl)
            throws ServiceLayerException, WebServiceException, ProductNotFoundException, UserNotFoundException, LicenseNotFoundException, AccessDeniedException, GroupNotFoundException, ErightsException {
    	ActivationCodeBatch actCodeBatch = new ActivationCodeBatch();
    	List<ActivationCode> activationCodes = new ArrayList<ActivationCode>();
    	ActivationCode actCode = new ActivationCode();
    	actCode.setCode("1234567819");
    	actCodeBatch.setActivationCodes(activationCodes);
        Product product = new RegisterableProduct();
        EnforceableProductDto productDto = new EnforceableProductDto();
        productDto.setProductId(acl.getProductId().getInternalId().getId());
        //EasyMock.expect(mProductLookup.lookupProductByIdentifier(acl.getProductId())).andReturn(product);
        //EasyMock.expect(mActivationCodeService.getActivationCodeBatchByBatchId(activationCodeBatch.getBatchId(), false)).andReturn(null);
        mProductLookup.checkOupIdPattern(acl.getProductId().getInternalId().getId());
        EasyMock.expect(mActivationCodeService.saveActivationCodeBatchWithTemplate(
                EasyMock.anyObject(ActivationCodeBatch.class), 
                EasyMock.anyObject(EnforceableProductDto.class), 
                EasyMock.anyObject(LicenceTemplate.class),
                EasyMock.eq(activationCodeBatch.getNumberOfTokens()),
                EasyMock.eq(activationCodeBatch.getAllowedUsages()))).andReturn(actCodeBatch);
        EasyMock.expectLastCall();

       
       // 1 REPLAY
        replay(getMocks());

        // 2 CALL THE METHOD WE ARE TESTING!!!!
        CreateActivationCodeBatchResponse result = sut.createActivationCodeBatch(activationCodeBatch, acl);

        // 3 PERFORM ASSERTIONS ON RESPONSE
        Assert.assertNull(result.getErrorStatus());

        // 3 VERIFY
        verify(getMocks());
    }

    private ActivationCodeBatch eqAcBatch(final com.oup.eac.ws.v2.binding.common.ActivationCodeBatch acBatch) {
        IArgumentMatcher matcher = new IArgumentMatcher() {

            @Override
            public boolean matches(Object arg) {
                if (arg instanceof ActivationCodeBatch == false) {
                    return false;
                }
                ActivationCodeBatch actual = (ActivationCodeBatch) arg;
                
                boolean chk1 = actual.getActivationCodeFormat().name().equals(acBatch.getCodeFormat().name());
                boolean chk2 = actual.getBatchId().equals(acBatch.getBatchId());
                boolean chk3 = checkDateTime(actual.getStartDate(), acBatch.getValidFrom());
                return chk1 && chk2 && chk3;
            }

            @Override
            public void appendTo(StringBuffer out) {
                out.append("eqAbBatch(");
                out.append(acBatch.toString());
                out.append(")");
            }
        };
        EasyMock.reportMatcher(matcher);
        return null;
    }

    @SuppressWarnings("unused")
    private LicenceTemplate eqIdentifier(final Identifier identifier) {
        IArgumentMatcher matcher = new IArgumentMatcher() {

            @Override
            public boolean matches(Object arg) {
                if (arg instanceof Identifier == false) {
                    return false;
                }
                Identifier actual = (Identifier) arg;

                boolean result = false;
                if (identifier.getInternalId() != null) {
                    result = identifier.getInternalId().getId().equals(actual.getInternalId().getId());
                } else if (identifier.getExternalId() != null) {
                    ExternalIdentifier expectedID = identifier.getExternalId();
                    ExternalIdentifier actualID = actual.getExternalId();
                    boolean part1 = expectedID.getSystemId().equals(actualID.getSystemId());
                    boolean part2 = expectedID.getTypeId().equals(actualID.getTypeId());
                    boolean part3 = expectedID.getId().equals(actualID.getId());
                    result = part1 && part2 && part3;
                }
                return result;
            }

            @Override
            public void appendTo(StringBuffer out) {
                out.append("eqIdentifier(");
                out.append(identifier.toString());
                out.append(")");
            }
        };
        EasyMock.reportMatcher(matcher);
        return null;
    }

    private LicenceTemplate eqLicenceTemplate(final LicenceDetails licDetails) {
        IArgumentMatcher matcher = new IArgumentMatcher() {

            @Override
            public boolean matches(Object arg) {
                if (arg instanceof LicenceTemplate == false) {
                    return false;
                }
                LicenceTemplate actual = (LicenceTemplate) arg;
                boolean chk1 = checkLicenceType(actual.getLicenceType(), licDetails.getExtendedDetails());

                boolean chk2 = checkDate(actual.getStartDate(), licDetails.getStartDate());
                boolean chk3 = checkDate(actual.getEndDate(), licDetails.getEndDate());

                boolean chk4 = false;
                switch (actual.getLicenceType()) {
                case STANDARD:
                    chk4 = true;
                    break;
                case ROLLING:
                    RollingLicenceTemplate rolling = (RollingLicenceTemplate)actual;
                    RollingLicenceDetail rollDet = licDetails.getExtendedDetails().getRollingLicence();
                    boolean r1 = rolling.getUnitType().name().equals(rollDet.getPeriodUnit().name());
                    boolean r2 = rolling.getTimePeriod() == rollDet.getPeriodValue();
                    chk4 = r1 && r2;
                    break;
                case USAGE:
                    UsageLicenceTemplate usage = (UsageLicenceTemplate)actual;
                    chk4 = usage.getAllowedUsages() == licDetails.getExtendedDetails().getUsageLicence().getAllowedUsages();
                    break;
                case CONCURRENT:
                    ConcurrentLicenceTemplate con = (ConcurrentLicenceTemplate)actual;
                    chk4 = con.getUserConcurrency() == licDetails.getExtendedDetails().getConcurrentLicence().getConcurrency();
                    break;

                }

                return chk1 && chk2 && chk3 && chk4;
            }

            @Override
            public void appendTo(StringBuffer out) {
                out.append("eqLicenceTemplate(");
                out.append(licDetails.toString());
                out.append(")");
            }
        };
        EasyMock.reportMatcher(matcher);
        return null;
    }

    private Date getRandomDate() {
        return new java.util.Date();
    }

    private boolean checkDate(LocalDate actual, org.exolab.castor.types.Date expected) {
        if (expected == null) {
            return actual == null;
        } else {
            long actualT = actual.toDateMidnight().getMillis();
            long expectedT = new LocalDate(expected.toLong()).toDateMidnight().getMillis();
            return actualT == expectedT;
        }
    }
    
    private boolean checkDateTime(DateTime actual, org.exolab.castor.types.Date expected) {
        if (expected == null) {
            return actual == null;
        } else {
            long actualT = actual.toDateMidnight().getMillis();
            long expectedT = new LocalDate(expected.toLong()).toDateMidnight().getMillis();
            return actualT == expectedT;
        }
    }

    private boolean checkLicenceType(LicenceType actual, ExtendedLicenceDetails expectedLicDetails) {
        boolean result = false;
        if (expectedLicDetails == null || expectedLicDetails.getChoiceValue() == null) {
            result = LicenceType.STANDARD == actual;
        }else if (expectedLicDetails.getChoiceValue() instanceof RollingLicenceDetail) {
            result = LicenceType.ROLLING == actual;
        } else if (expectedLicDetails.getChoiceValue() instanceof ConcurrencyLicenceDetails) {
            result = LicenceType.CONCURRENT == actual;
        } else if (expectedLicDetails.getChoiceValue() instanceof UsageLicenceDetails) {
            result = LicenceType.USAGE == actual;
        }
        return result;
    }
    
    @Test
    public void testUnknownProduct() throws ServiceLayerException, WebServiceException, com.amazonaws.services.cloudfront.model.AccessDeniedException, ProductNotFoundException, UserNotFoundException, LicenseNotFoundException, GroupNotFoundException, ErightsException {
        
        ActivationCodeLicence acl = new ActivationCodeLicence();
        LicenceDetails licDetails = new LicenceDetails();
        acl.setLicenceDetails(licDetails);
        Identifier id = TestIdUtils.getInternalId("internal-id-1");
        
        acl.setProductId(id);
        com.oup.eac.ws.v2.binding.common.ActivationCodeBatch acb = new com.oup.eac.ws.v2.binding.common.ActivationCodeBatch();
        acb.setCodeFormat(CodeFormatEnum.EAC_NUMERIC);

        mProductLookup.checkOupIdPattern("internal-id-1");
        expectLastCall();
        
        ActivationCodeBatch actCodeBatch = new ActivationCodeBatch();
    	List<ActivationCode> activationCodes = new ArrayList<ActivationCode>();
    	ActivationCode actCode = new ActivationCode();
    	actCode.setCode("");
    	actCodeBatch.setActivationCodes(activationCodes);
        
    	 EasyMock.expect(mActivationCodeService.saveActivationCodeBatchWithTemplate(
                 EasyMock.anyObject(ActivationCodeBatch.class), 
                 EasyMock.anyObject(EnforceableProductDto.class), 
                 EasyMock.anyObject(LicenceTemplate.class),
                 EasyMock.eq(acb.getNumberOfTokens()),
                 EasyMock.eq(acb.getAllowedUsages()))).andThrow(new ServiceLayerValidationException());
      //  expectLastCall();
        
        replay(getMocks());
        CreateActivationCodeBatchResponse resp = this.sut.createActivationCodeBatch(acb, acl);
        verify(getMocks());
        Assert.assertNotNull(resp.getErrorStatus());
        
    }

    
}
