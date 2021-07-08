package com.oup.eac.ws.v2.service.impl;

import java.util.List;

import org.exolab.castor.types.Date;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.amazonaws.services.cloudfront.model.AccessDeniedException;
import com.oup.eac.domain.ActivationCode;
import com.oup.eac.domain.ActivationCodeBatch.ActivationCodeFormat;
import com.oup.eac.domain.ConcurrentLicenceTemplate;
import com.oup.eac.domain.LicenceTemplate;
import com.oup.eac.domain.LicenceTemplate.LicenceType;
import com.oup.eac.domain.RollingLicenceTemplate;
import com.oup.eac.domain.RollingLicenceTemplate.RollingBeginOn;
import com.oup.eac.domain.RollingLicenceTemplate.RollingUnitType;
import com.oup.eac.domain.StandardLicenceTemplate;
import com.oup.eac.domain.UsageLicenceTemplate;
import com.oup.eac.domain.utils.audit.AuditLogger;
import com.oup.eac.dto.EnforceableProductDto;
import com.oup.eac.integration.facade.exceptions.ErightsException;
import com.oup.eac.integration.facade.exceptions.GroupNotFoundException;
import com.oup.eac.integration.facade.exceptions.LicenseNotFoundException;
import com.oup.eac.integration.facade.exceptions.ProductNotFoundException;
import com.oup.eac.integration.facade.exceptions.UserNotFoundException;
import com.oup.eac.service.ActivationCodeService;
import com.oup.eac.service.ServiceLayerException;
import com.oup.eac.service.exceptions.ActivationCodeServiceLayerException;
import com.oup.eac.service.exceptions.ServiceLayerValidationException;
import com.oup.eac.ws.v2.binding.access.CreateActivationCodeBatchResponse;
import com.oup.eac.ws.v2.binding.common.ActivationCodeBatch;
import com.oup.eac.ws.v2.binding.common.ActivationCodeLicence;
import com.oup.eac.ws.v2.binding.common.ConcurrencyLicenceDetails;
import com.oup.eac.ws.v2.binding.common.ErrorStatus;
import com.oup.eac.ws.v2.binding.common.Identifier;
import com.oup.eac.ws.v2.binding.common.LicenceDetails;
import com.oup.eac.ws.v2.binding.common.RollingLicenceDetail;
import com.oup.eac.ws.v2.binding.common.UsageLicenceDetails;
import com.oup.eac.ws.v2.binding.common.types.BeginEnum;
import com.oup.eac.ws.v2.binding.common.types.UnitEnum;
import com.oup.eac.ws.v2.ex.WebServiceException;
import com.oup.eac.ws.v2.ex.WebServiceValidationException;
import com.oup.eac.ws.v2.service.ActivationCodeServiceAdapter;
import com.oup.eac.ws.v2.service.WsProductLookup;
import com.oup.eac.ws.v2.service.utils.ErrorStatusUtils;

@Component(value = "activationCodeServiceAdapter")
@Primary
public class ActivationCodeServiceAdapterImpl implements ActivationCodeServiceAdapter {

    private final ActivationCodeService activationCodeService;
    private final WsProductLookup productLookup;
    
    @Autowired
    public ActivationCodeServiceAdapterImpl(final ActivationCodeService activationCodeService, final WsProductLookup productLookup) {
    	Assert.notNull(activationCodeService);
    	Assert.notNull(productLookup);
        this.activationCodeService = activationCodeService;
        this.productLookup = productLookup;
    }

    @Override
    @PreAuthorize("hasRole('ROLE_WS_CREATE_ACTIVATION_CODE_BATCH')")
    public CreateActivationCodeBatchResponse createActivationCodeBatch(ActivationCodeBatch activationCodeBatch, ActivationCodeLicence activationCodeLicence)
    		throws WebServiceException, ProductNotFoundException, 
    		UserNotFoundException, LicenseNotFoundException, AccessDeniedException, GroupNotFoundException, 
    		ErightsException {

    	long startTime = System.currentTimeMillis();
        CreateActivationCodeBatchResponse result = new CreateActivationCodeBatchResponse();
        try {
            Identifier id = activationCodeLicence.getProductId();
            EnforceableProductDto enforceableProductDto =null ;
            if (id.getInternalId() != null ) {
            	productLookup.checkOupIdPattern(id.getInternalId().getId());
            	enforceableProductDto = new EnforceableProductDto();
            	enforceableProductDto.setProductId(id.getInternalId().getId());
            } else {
            	enforceableProductDto = this.productLookup.lookupEnforceableProductByIdentifier(id);
            }
           
            LicenceDetails licenceDetails = activationCodeLicence.getLicenceDetails();
            LicenceTemplate licenceTemplate = getLicenceTemplate(licenceDetails);

            com.oup.eac.domain.ActivationCodeBatch acBatch = getBatch(activationCodeBatch);

           	
            com.oup.eac.domain.ActivationCodeBatch actCodeBatch = this.activationCodeService.saveActivationCodeBatchWithTemplate(acBatch, enforceableProductDto, licenceTemplate, activationCodeBatch.getNumberOfTokens(),
                        activationCodeBatch.getAllowedUsages());
            	
            List<ActivationCode> codes = actCodeBatch.getActivationCodes();
            String[] resultActivationCodes = new String[codes.size()] ;
            int i = 0;
            for (ActivationCode code : codes) {
            	resultActivationCodes[i] = code.getCode();
            	i++;
            }
            result.setActivationCode(resultActivationCodes);

        } catch (ActivationCodeServiceLayerException acsle) {
            setErrorStatus(acsle,result);
        } catch (WebServiceValidationException wsve) {
            setErrorStatus(wsve,result);
        } catch (ServiceLayerException ex) {
        	if (ex instanceof ServiceLayerValidationException) {
        		setServerErrorStatus(ex,result);
        	} else {
        		throw new WebServiceException(ex.getMessage(), ex);
        	}
        }
        AuditLogger.logEvent(":: Time to createActivationCodeBatch :: " + (System.currentTimeMillis() - startTime));
        return result;
    }

    private com.oup.eac.domain.ActivationCodeBatch getBatch(ActivationCodeBatch activationCodeBatch) throws ServiceLayerException {
        com.oup.eac.domain.ActivationCodeBatch result = new com.oup.eac.domain.ActivationCodeBatch();
        result.setBatchId(activationCodeBatch.getBatchId());
        result.setActivationCodeFormat(ActivationCodeFormat.valueOf(activationCodeBatch.getCodeFormat().name()));
        result.setStartDate(getDateTime(activationCodeBatch.getValidFrom()));
        result.setEndDate(getDateTime(activationCodeBatch.getValidTo()));
        return result;
    }

    private DateTime getDateTime(Date value) {
        if (value == null) {
            return null;
        }
        DateTime result = new DateTime(value.toDate());
        return result;
    }

    private LicenceTemplate getLicenceTemplate(LicenceDetails licenceDetails) throws ServiceLayerException {
        LicenceType licType = LicenceType.STANDARD;
        Object exLicDetails = licenceDetails.getExtendedDetails() == null ? null : licenceDetails.getExtendedDetails().getChoiceValue();
        ConcurrencyLicenceDetails exCon = null;
        RollingLicenceDetail exRoll = null;
        UsageLicenceDetails exUsage = null;
        if (exLicDetails != null) {
            if (exLicDetails instanceof ConcurrencyLicenceDetails) {
                exCon = (ConcurrencyLicenceDetails) exLicDetails;
                licType = LicenceType.CONCURRENT;
            } else if (exLicDetails instanceof RollingLicenceDetail) {
                exRoll = (RollingLicenceDetail) exLicDetails;
                licType = LicenceType.ROLLING;
            } else if (exLicDetails instanceof UsageLicenceDetails) {
                exUsage = (UsageLicenceDetails) exLicDetails;
                licType = LicenceType.USAGE;
            } else {
                throw new ServiceLayerException("Cannot determine licence type");
            }
        }
        LicenceTemplate template = newLicenceTemplate(licType);
        Date endDate = licenceDetails.getEndDate();
        Date startDate = licenceDetails.getStartDate();
        template.setStartDate(getLocalDate(startDate));
        template.setEndDate(getLocalDate(endDate));
        switch (licType) {
        case CONCURRENT:
            ConcurrentLicenceTemplate conTemplate = (ConcurrentLicenceTemplate) template;
            conTemplate.setUserConcurrency(exCon.getConcurrency());
            conTemplate.setTotalConcurrency(exCon.getConcurrency());
            break;
        case ROLLING:
            RollingLicenceTemplate rollTemplate = (RollingLicenceTemplate) template;
            rollTemplate.setUnitType(getUnitType(exRoll.getPeriodUnit()));
            rollTemplate.setTimePeriod(exRoll.getPeriodValue());
            rollTemplate.setBeginOn(getBeginOn(exRoll.getBeginOn()));
            break;
        case USAGE:
            UsageLicenceTemplate usageTemplate = (UsageLicenceTemplate) template;
            usageTemplate.setAllowedUsages(exUsage.getAllowedUsages());
            break;
        case STANDARD:
            @SuppressWarnings("unused")
            StandardLicenceTemplate stdTemplate = (StandardLicenceTemplate) template;
            // nothing else to do here.
            break;
        }
        return template;
    }

    private RollingBeginOn getBeginOn(BeginEnum beginOn) {
        if (beginOn == null) {
            return null;
        } else {
            return RollingBeginOn.valueOf(beginOn.name());
        }
    }

    private RollingUnitType getUnitType(UnitEnum periodUnit) {
        if (periodUnit == null) {
            return null;
        } else {
            return RollingUnitType.valueOf(periodUnit.name());
        }
    }

    private LicenceTemplate newLicenceTemplate(LicenceType licType) throws ServiceLayerException {
        LicenceTemplate result = null;
        try {
            result = licType.getLicenceClass().newInstance();
        } catch (InstantiationException e) {
            throw new ServiceLayerException("problem create licence template", e);
        } catch (IllegalAccessException e) {
            throw new ServiceLayerException("problem create licence template", e);
        }
        return result;
    }

    private LocalDate getLocalDate(Date value) {
        if (value == null) {
            return null;
        } else {
            LocalDate ld = new LocalDate(value.toLong());
            return ld;
        }
    }

    private String[] getCodeList(List<ActivationCode> set) {
        String[] result = new String[set.size()];
        int i = 0;
        for (ActivationCode ac : set) {
            result[i++] = ac.getCode();
        }
        return result;
    }
    
    private void setErrorStatus(Exception ex, CreateActivationCodeBatchResponse resp){
        ErrorStatus errorStatus = ErrorStatusUtils.getClientErrorStatus(ex.getMessage());        
        resp.setErrorStatus(errorStatus);
    }
    private void setServerErrorStatus(Exception ex, CreateActivationCodeBatchResponse resp){
        ErrorStatus errorStatus = ErrorStatusUtils.getServerErrorStatus(ex.getMessage());        
        resp.setErrorStatus(errorStatus);
    }

}
