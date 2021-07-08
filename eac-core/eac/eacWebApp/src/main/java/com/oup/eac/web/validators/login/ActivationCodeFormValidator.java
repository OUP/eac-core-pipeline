package com.oup.eac.web.validators.login;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.validation.Errors;

import com.oup.eac.domain.ActivationCode;
import com.oup.eac.domain.ActivationCodeBatch;
import com.oup.eac.dto.ActivationCodeBatchDto;
import com.oup.eac.service.ActivationCodeService;
import com.oup.eac.service.ServiceLayerException;
import com.oup.eac.web.validators.EACValidator;

/**
 * @author harlandd Activation code form validator
 */
public class ActivationCodeFormValidator extends EACValidator {
    
    private static Logger LOG = Logger.getLogger(ActivationCodeFormValidator.class);

    private final ActivationCodeService activationCodeService;
    
    public ActivationCodeFormValidator(final ActivationCodeService activationCodeService) {
        super();
        this.activationCodeService = activationCodeService;
    }

    /**
     * @param clazz
     *            the class
     * @return does this validator support this class
     */
    public final boolean supports(final Class clazz) {
        return ActivationCode.class.isAssignableFrom(clazz);
    }

    /**
     * @param obj
     *            the command object
     * @param errors
     *            the errors
     */
    public final void validate(final Object obj, final Errors errors) {
        ActivationCode activationCode = (ActivationCode) obj;
        if (StringUtils.isBlank(activationCode.getCode())) {
            errors.rejectValue("code", "error.not-specified", new Object[] { "label.redeemcode" }, "Activation Code is required.");
            return;
        }
        
        ActivationCodeBatchDto activationCodeBatchDto = activationCodeService.getActivationCodeByCode(activationCode);
        ActivationCodeBatch activationCodeBatch = new ActivationCodeBatch();
        ActivationCode actCode = new ActivationCode();
        if(activationCodeBatchDto.getValidFrom()!=null){
	        DateTime startDate = new DateTime(activationCodeBatchDto.getValidFrom().toDate());
	        activationCodeBatch.setStartDate(startDate);
        }
        if(activationCodeBatchDto.getValidTo()!=null){
	        DateTime endDate = new DateTime(activationCodeBatchDto.getValidTo().toDate());
	        activationCodeBatch.setEndDate(endDate);       
        }
        actCode.setAllowedUsage(activationCodeBatchDto.getAllowedUsages());
        actCode.setActualUsage(activationCodeBatchDto.getActualUsage());
        actCode.setActivationCodeBatch(activationCodeBatch);
               
        //Activation code not found
        if (actCode == null) {
            errors.rejectValue("code", "error.regularexpression", new Object[] { "label.redeemcode" }, "Activation Code is invalid");
            return;
        }

        //Check activation code and batch
        try {
        	activationCodeService.validateActivationCode(actCode);
        } catch (ServiceLayerException sle) {
            LOG.warn("Problem validating activation code. " + sle.getMessage());
            errors.rejectValue("code", "error.problem.activating.token", new Object[0], "There was a problem with your activation code.");
        }
    }
}
