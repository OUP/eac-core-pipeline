package com.oup.eac.admin.validators;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.binding.message.MessageResolver;
import org.springframework.binding.validation.ValidationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.amazonaws.services.cloudfront.model.AccessDeniedException;
import com.oup.eac.admin.beans.ActivationCodeBatchBean;
import com.oup.eac.domain.ActivationCodeBatch;
import com.oup.eac.domain.LicenceTemplate.LicenceType;
import com.oup.eac.integration.erights.ActivationCodeResponseSTATUS;
import com.oup.eac.integration.erights.GetActivationCodeBatchByBatchIdResponse;
import com.oup.eac.integration.facade.exceptions.ErightsException;
import com.oup.eac.integration.facade.exceptions.GroupNotFoundException;
import com.oup.eac.integration.facade.exceptions.LicenseNotFoundException;
import com.oup.eac.integration.facade.exceptions.ProductNotFoundException;
import com.oup.eac.integration.facade.exceptions.UserNotFoundException;
import com.oup.eac.service.ActivationCodeService;
import com.oup.eac.service.ServiceLayerException;

@Component(value = "activationCodeBatchBeanValidator")
public class ActivationCodeBatchBeanValidator implements Validator {

	//see 
    private static final LocalDate SQL_SERVER_DATE_MIN = new LocalDate(0001, 01, 01);
    private static final LocalDate SQL_SERVER_DATE_MAX = new LocalDate(9999, 12, 31);
    
    private static final String SQL_SERVER_DATE_MIN_DESC;
    private static final String SQL_SERVER_DATE_MAX_DESC;
    
    static {
        DateTimeFormatter fmt = DateTimeFormat.forPattern("d MMMM YYYY");
        SQL_SERVER_DATE_MIN_DESC = fmt.print(SQL_SERVER_DATE_MIN);
        SQL_SERVER_DATE_MAX_DESC = fmt.print(SQL_SERVER_DATE_MAX);
    }

    private static final Object[] SQL_SERVER_DATE_MIN_ARGS = new Object[] { SQL_SERVER_DATE_MIN_DESC };
    private static final Object[] SQL_SERVER_DATE_MAX_ARGS = new Object[] { SQL_SERVER_DATE_MAX_DESC };
    
    private ActivationCodeService activationCodeService;

    @Autowired
    public ActivationCodeBatchBeanValidator(ActivationCodeService activationCodeService) {
        this.activationCodeService = activationCodeService;
    }

    public void validateCreateOrEditActivationCodeBatch(final ActivationCodeBatchBean activationCodeBatchBean,
            ValidationContext context) throws ProductNotFoundException, UserNotFoundException, LicenseNotFoundException, 
            AccessDeniedException, GroupNotFoundException, ErightsException, org.springframework.security.access.AccessDeniedException, ServiceLayerException {
        MessageContext messageCtx = context.getMessageContext();
        List<EacError> errors = validate(activationCodeBatchBean);
        populateMessageContext(errors, messageCtx);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ActivationCodeBatchBean activationCodeBatchBean = (ActivationCodeBatchBean) target;
        List<EacError> eacErrors = new ArrayList<ActivationCodeBatchBeanValidator.EacError>();
		try {
				eacErrors = validate(activationCodeBatchBean);
			} catch (org.springframework.security.access.AccessDeniedException
					| ServiceLayerException | AccessDeniedException | ErightsException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
        populateErrors(eacErrors, errors);
    }

    private List<EacError> validate(ActivationCodeBatchBean activationCodeBatchBean) 
    		throws ProductNotFoundException, UserNotFoundException, LicenseNotFoundException, AccessDeniedException, 
    		GroupNotFoundException, ErightsException, org.springframework.security.access.AccessDeniedException, ServiceLayerException {
        List<EacError> errors = new ArrayList<EacError>();
        validateDates(activationCodeBatchBean, errors);
        validateBatch(activationCodeBatchBean, errors);
        validateProductAndLicence(activationCodeBatchBean, errors);
        return errors;
    }

    private void validateDates(ActivationCodeBatchBean activationCodeBatchBean, List<EacError> messages) {
        //sqlserver has min date of 01/01/0001 and max date of 31/12/9999 [for 'date' and 'datetime2']
        
        //TOO SOON?
        if (isBeforeMinDate(activationCodeBatchBean.getLicenceStartDate())) {
            messages.add(new EacError("error.batch.licStartDateBeforeMin", "The licence start date is before " + SQL_SERVER_DATE_MIN_DESC, SQL_SERVER_DATE_MIN_ARGS));
        }
        if (isBeforeMinDate(activationCodeBatchBean.getLicenceEndDate())) {
            messages.add(new EacError("error.batch.licEndDateBeforeMin", "The licence end date is before "+ SQL_SERVER_DATE_MIN_DESC, SQL_SERVER_DATE_MIN_ARGS));
        }
        if (isBeforeMinDate(activationCodeBatchBean.getActivationCodeBatch().getStartDate())) {
            messages.add(new EacError("error.batch.startDateBeforeMin", "The token validity start date is before " + SQL_SERVER_DATE_MIN_DESC, SQL_SERVER_DATE_MIN_ARGS));
        }
        if (isBeforeMinDate(activationCodeBatchBean.getActivationCodeBatch().getEndDate())) {
            messages.add(new EacError("error.batch.endDateBeforeMin", "The token validity end date is before " +  SQL_SERVER_DATE_MIN_DESC, SQL_SERVER_DATE_MIN_ARGS));
        }
        //TOO LATE?
        if (isAfterMaxDate(activationCodeBatchBean.getLicenceStartDate())) {
            messages.add(new EacError("error.batch.licStartDateAfterMax", "The licence start date is after " + SQL_SERVER_DATE_MAX_DESC, SQL_SERVER_DATE_MAX_ARGS));
        }
        if (isAfterMaxDate(activationCodeBatchBean.getLicenceEndDate())) {
            messages.add(new EacError("error.batch.licEndDateAfterMax", "The licence end date is after "+ SQL_SERVER_DATE_MAX_DESC, SQL_SERVER_DATE_MAX_ARGS));
        }
        if (isAfterMaxDate(activationCodeBatchBean.getActivationCodeBatch().getStartDate())) {
            messages.add(new EacError("error.batch.startDateAfterMax", "The token validity start date is after " + SQL_SERVER_DATE_MAX_DESC, SQL_SERVER_DATE_MAX_ARGS));
        }
        if (isAfterMaxDate(activationCodeBatchBean.getActivationCodeBatch().getEndDate())) {
            messages.add(new EacError("error.batch.endDateAfterMax", "The token validity end date is after " +  SQL_SERVER_DATE_MAX_DESC, SQL_SERVER_DATE_MAX_ARGS));
        }
    }

    private boolean isBeforeMinDate(LocalDate value) {
        return value != null && value.isBefore(SQL_SERVER_DATE_MIN);
    }
    
    private boolean isAfterMaxDate(LocalDate value) {
        return value != null && value.isAfter(SQL_SERVER_DATE_MAX);
    }

    private boolean isBeforeMinDate(DateTime dateTime) {
        return dateTime != null && dateTime.isBefore(SQL_SERVER_DATE_MIN.toDateTimeAtStartOfDay());
    }
    
    private boolean isAfterMaxDate(DateTime dateTime) {
        return dateTime != null && dateTime.isAfter(SQL_SERVER_DATE_MAX.toDateTimeAtStartOfDay());
    }

    private void validateBatch(final ActivationCodeBatchBean activationCodeBatchBean, final List<EacError> messages) 
    		throws ProductNotFoundException, UserNotFoundException, LicenseNotFoundException, AccessDeniedException, 
    		GroupNotFoundException, ErightsException, org.springframework.security.access.AccessDeniedException, ServiceLayerException {

        validateActivationCodeBatch(activationCodeBatchBean, messages);
        validateBatchName(activationCodeBatchBean, messages);
        //validateCodePrefix(activationCodeBatchBean, messages);
        if (activationCodeBatchBean.isEditMode() == false) {
            validateNumberOfTokens(activationCodeBatchBean, messages);
            validateTokenAllowedUsages(activationCodeBatchBean, messages);
        }
        validateBatchStartEndDates(activationCodeBatchBean, messages);
    }

    private void validateStartDateBeforeMidnight(DateTime startDate, List<EacError> messages) {
        try {
            Assert.isTrue(!startDate.isBefore(new DateMidnight()));
        } catch (IllegalArgumentException iae) {
            messages.add(new EacError("error.batch.startDateBeforeNow",
                    "The batch start date is before the current date"));
        }
    }

    private void validateEndDateBeforeMidnight(DateTime endDate, List<EacError> messages) {
        try {
            Assert.isTrue(!endDate.isBefore(new DateMidnight()));
        } catch (IllegalArgumentException iae) {
            messages.add(new EacError("error.batch.endDateBeforeNow", "The batch end date is before the current date"));
        }
    }

    private void validateBatchStartEndDates(ActivationCodeBatchBean activationCodeBatchBean, List<EacError> messages) {
        boolean isEditMode = activationCodeBatchBean.isEditMode();
        ActivationCodeBatch batch = activationCodeBatchBean.getActivationCodeBatch();
        DateTime startDate = batch.getStartDate();
        DateTime endDate = batch.getEndDate();

        if (startDate != null && endDate != null) {
            try {
                boolean endDateIsNotBeforeStartDate = !endDate.isBefore(startDate.getMillis());
                Assert.isTrue(endDateIsNotBeforeStartDate);
            } catch (IllegalArgumentException iae) {
                messages.add(new EacError("error.batch.endDateBeforeStartDate",
                        "The batch end date is before the batch start date"));
            }
        }
        
        if (!isEditMode && startDate != null) {
            validateStartDateBeforeMidnight(startDate, messages);
        }
        if(!isEditMode && endDate != null) {
            validateEndDateBeforeMidnight(endDate, messages);
        }
    }

    private void validateTokenAllowedUsages(final ActivationCodeBatchBean activationCodeBatchBean,
            final List<EacError> messages) {
        if (StringUtils.isBlank(activationCodeBatchBean.getTokenAllowedUsages())) {
            messages.add(new EacError("error.batch.noTokenAllowedUsages",
                    "The number of token allowed usages is required"));
        } else {
            EacError message = new EacError("error.batch.invalidTokenAllowedUsages",
                    "Please enter a valid number of allowed usages");
            try {
                int tokenAllowedUsagesInt = Integer.parseInt(activationCodeBatchBean.getTokenAllowedUsages());
                if (tokenAllowedUsagesInt < 1 || tokenAllowedUsagesInt > 200000) {
                    messages.add(message);
                }
            } catch (NumberFormatException e) {
                messages.add(message);
            }
        }
    }

    private void validateNumberOfTokens(final ActivationCodeBatchBean activationCodeBatchBean,
            final List<EacError> messages) {
        if (StringUtils.isBlank(activationCodeBatchBean.getNumberOfTokens())) {
            messages.add(new EacError("error.batch.noTokens", "The number of tokens is required"));
        } else {
            EacError message = new EacError("error.batch.invalidNumberOfTokens",
                    "Please enter a valid number of tokens");
            try {
                int numberOfTokensInt = Integer.parseInt(activationCodeBatchBean.getNumberOfTokens());
                if (numberOfTokensInt < 1) {
                    messages.add(message);
                }
            } catch (NumberFormatException e) {
                messages.add(message);
            }
        }
    }

    private void validateCodePrefix(final ActivationCodeBatchBean activationCodeBatchBean, final List<EacError> messages) {
        if (activationCodeBatchBean.getActivationCodeBatch().getActivationCodeFormat().isPrefixed()) {
            try {
                Assert.hasText(activationCodeBatchBean.getActivationCodeBatch().getCodePrefix());
            } catch (IllegalArgumentException iae) {
                messages.add(new EacError("error.batch.noCodePrefix", "Code prefix is required for the format selected"));
            }
        }
    }

    private void validateBatchName(final ActivationCodeBatchBean activationCodeBatchBean, final List<EacError> messages) 
    		throws ProductNotFoundException, UserNotFoundException, LicenseNotFoundException, AccessDeniedException, 
    		GroupNotFoundException, ErightsException, org.springframework.security.access.AccessDeniedException, ServiceLayerException {
        String batchName = activationCodeBatchBean.getActivationCodeBatch().getBatchId();
        try {
            Assert.hasText(batchName);
        } catch (IllegalArgumentException iae) {
            messages.add(new EacError("error.batch.noBatchId", "Batch name is required"));
            return;
        }
        boolean alreadyInUse = false;
        try {
            GetActivationCodeBatchByBatchIdResponse activationCodeBatch = 
            		activationCodeService.checkActivationCodeBatchExistsByBatchId(batchName);
            if (activationCodeBatchBean.isEditMode()) {
                if (activationCodeBatch.getStatus() == ActivationCodeResponseSTATUS.SUCCESS) {
                    String foundId = activationCodeBatch.getBatchId();
                    String batchId = activationCodeBatchBean.getActivationCodeBatch().getBatchId();
                    Assert.isTrue(batchId.equals(foundId));
                }
            } /*else {
                Assert.isNull(activationCodeBatch);
                boolean archiveBatchWithSameName = activationCodeService.doesArchivedBatchExist(batchName);
                alreadyInUse = archiveBatchWithSameName;
            }*/
        } catch (IllegalArgumentException iae) {
            alreadyInUse = true;
        }
        if (alreadyInUse) {
            messages.add(new EacError("error.batch.duplicateBatchId", "Batch name is already in use"));
        }
    }

    private void validateActivationCodeBatch(final ActivationCodeBatchBean activationCodeBatchBean,
            final List<EacError> messages) {
        try {
            Assert.notNull(activationCodeBatchBean.getActivationCodeBatch());
        } catch (IllegalArgumentException iae) {
            messages.add(new EacError("error.batch.activationCodeBatchIsNull", "Activation code batch cannot be null"));
        }
    }

    private void validateProductAndLicence(final ActivationCodeBatchBean activationCodeBatchBean,
            final List<EacError> messages) {

    	if((activationCodeBatchBean.getEacGroupId() != null)  && (activationCodeBatchBean.getProductId() != null)){
    		 messages.add(new EacError("error.licence.noProduct", "Please select either a product or a product group."));
    	}
    	
        if (activationCodeBatchBean.getActivationCodeRegistrationDefinition() == null) {
            messages.add(new EacError("error.licence.noProduct", "Please select a product or a product group."));
        }
        
        LocalDate start = activationCodeBatchBean.getLicenceStartDate();
        LocalDate end = activationCodeBatchBean.getLicenceEndDate();
        if(start != null && end != null && end.isBefore(start)){
            messages.add(new EacError("error.batch.licEndDateBeforeLicStartDate"));
        }

        LicenceType licenceType = activationCodeBatchBean.getLicenceType();

        List<String> simpleMessages = new ArrayList<String>();

        List<String> msgs1 = null;
        List<String> msgs2 = null;
        List<String> msgs3 = null;
        List<String> msgs4 = null;

        if (LicenceType.CONCURRENT.equals(licenceType)) {
            msgs1 = LicencePropertyValidatorHelper.validateTotalConcurrency(activationCodeBatchBean
                    .getTotalConcurrency());
            msgs2 = LicencePropertyValidatorHelper
                    .validateUserConcurrency(activationCodeBatchBean.getUserConcurrency());
        } else if (LicenceType.ROLLING.equals(licenceType)) {
            msgs3 = LicencePropertyValidatorHelper.validateTimePeriod(activationCodeBatchBean.getTimePeriod());
        } else if (LicenceType.USAGE.equals(licenceType)) {
            msgs4 = LicencePropertyValidatorHelper.validateLicenceAllowedUsages(activationCodeBatchBean
                    .getLicenceAllowedUsages());
        }
        add(simpleMessages, msgs1);
        add(simpleMessages, msgs2);
        add(simpleMessages, msgs3);
        add(simpleMessages, msgs4);

        for (String simple : simpleMessages) {
            messages.add(new EacError(simple));
        }
    }

    private void add(List<String> collector, List<String> values) {
        if (values == null) {
            return;
        }
        collector.addAll(values);
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(ActivationCodeBatchBean.class);
    }

    public static class EacError {
        final String code;
        final String defaultText;
        final Object[] params;

        public EacError(String code, String defaultText, Object[] params) {
            this.code = code;
            this.defaultText = defaultText;
            this.params = params;
        }

        public EacError(String code, String defaultText) {
            this.code = code;
            this.defaultText = defaultText;
            this.params = null;
        }

        public EacError(String code) {
            this.code = code;
            this.defaultText = null;
            this.params = null;
        }
    }

    private void populateMessageContext(final List<EacError> errors, final MessageContext context) {
        if (errors == null || context == null) {
            return;
        }
        for (EacError error : errors) {
            MessageBuilder temp = new MessageBuilder().error().code(error.code);
            if (error.defaultText != null) {
                temp = temp.defaultText(error.defaultText);
            }
            MessageResolver message = temp.build();
            context.addMessage(message);
        }
    }

    private void populateErrors(final List<EacError> errors, final Errors errorMsgs) {
        if (errors == null || errorMsgs == null) {
            return;
        }
        for (EacError error : errors) {
            if (error.defaultText != null) {
                if(error.params == null){
                    errorMsgs.reject(error.code, error.defaultText);
                }else{
                    errorMsgs.reject(error.code, error.params, error.defaultText );
                }
                
            } else {
                if(error.params == null){
                    errorMsgs.reject(error.code);
                }else{
                    errorMsgs.reject(error.code, error.params, "");
                }
            }
        }
    }
    
    public static boolean inOrderButNoOverlap(LocalDate licStartP, LocalDate licEndP, LocalDate batchStartP, LocalDate batchEndP){
        boolean result = false;
        LocalDate licStart = getStart(licStartP);
        LocalDate licEnd = getEnd(licEndP);
        LocalDate batchStart = getStart(batchStartP);
        LocalDate batchEnd = getEnd(batchEndP);
        result = !(licEnd.isBefore(licStart)) && !(batchEnd.isBefore(batchStart));
        if (result) {
            Interval lic = new Interval(licStart.toDateTimeAtStartOfDay(DateTimeZone.UTC),licEnd.toDateTimeAtStartOfDay(DateTimeZone.UTC));     
            Interval batch = new Interval(batchStart.toDateTimeAtStartOfDay(DateTimeZone.UTC),batchEnd.toDateTimeAtStartOfDay(DateTimeZone.UTC));
            result = !lic.overlaps(batch);
        }
        return result;
    }

    private static LocalDate getStart(LocalDate value) {
        return value == null ? SQL_SERVER_DATE_MIN : value;
    }
    
    private static LocalDate getEnd(LocalDate value) {
        return value == null ? SQL_SERVER_DATE_MAX : value;
    }

}
