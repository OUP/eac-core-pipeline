package com.oup.eac.data.util;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.io.IOUtils;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ReplacementDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.joda.time.DateTime;

import com.oup.eac.domain.AccountPageDefinition;
import com.oup.eac.domain.AccountRegistrationDefinition;
import com.oup.eac.domain.ActivationCode;
import com.oup.eac.domain.ActivationCodeBatch;
import com.oup.eac.domain.ActivationCodeRegistration;
import com.oup.eac.domain.ActivationCodeRegistrationDefinition;
import com.oup.eac.domain.AdminUser;
import com.oup.eac.domain.Answer;
import com.oup.eac.domain.BaseDomainObject;
import com.oup.eac.domain.Checkbox;
import com.oup.eac.domain.Component;
import com.oup.eac.domain.ConcurrentLicenceTemplate;
import com.oup.eac.domain.CreatedAudit;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.DbMessage;
import com.oup.eac.domain.Division;
import com.oup.eac.domain.DivisionAdminUser;
import com.oup.eac.domain.EacGroups;
import com.oup.eac.domain.Element;
import com.oup.eac.domain.ElementCountryRestriction;
import com.oup.eac.domain.ExportName;
import com.oup.eac.domain.ExternalCustomerId;
import com.oup.eac.domain.ExternalProductId;
import com.oup.eac.domain.ExternalSystem;
import com.oup.eac.domain.ExternalSystemIdType;
import com.oup.eac.domain.Field;
import com.oup.eac.domain.InstantRegistrationActivation;
import com.oup.eac.domain.Label;
import com.oup.eac.domain.LicenceTemplate;
import com.oup.eac.domain.LinkedProduct;
import com.oup.eac.domain.LinkedRegistration;
import com.oup.eac.domain.Message;
import com.oup.eac.domain.MultiSelect;
import com.oup.eac.domain.OUPBaseDomainObject;
import com.oup.eac.domain.OUPCreatedAudit;
import com.oup.eac.domain.OUPUpdatedAudit;
import com.oup.eac.domain.PageComponent;
import com.oup.eac.domain.PageDefinition;
import com.oup.eac.domain.PasswordField;
import com.oup.eac.domain.Permission;
import com.oup.eac.domain.Product;
import com.oup.eac.domain.ProductPageDefinition;
import com.oup.eac.domain.ProductRegistrationDefinition;
import com.oup.eac.domain.ProductSpecificAnswer;
import com.oup.eac.domain.ProgressBar;
import com.oup.eac.domain.ProgressBarElement;
import com.oup.eac.domain.QuartzLogEntry;
import com.oup.eac.domain.Question;
import com.oup.eac.domain.Radio;
import com.oup.eac.domain.RegisterableProduct;
import com.oup.eac.domain.Registration;
import com.oup.eac.domain.WhiteListUrl;
import com.oup.eac.domain.Registration.RegistrationType;
import com.oup.eac.domain.RegistrationActivation;
import com.oup.eac.domain.RegistrationDefinition;
import com.oup.eac.domain.Role;
import com.oup.eac.domain.RollingLicenceTemplate;
import com.oup.eac.domain.Select;
import com.oup.eac.domain.SelfRegistrationActivation;
import com.oup.eac.domain.StandardLicenceTemplate;
import com.oup.eac.domain.Tag;
import com.oup.eac.domain.TagOption;
import com.oup.eac.domain.TextField;
import com.oup.eac.domain.UpdatedAudit;
import com.oup.eac.domain.UrlLink;
import com.oup.eac.domain.UrlSkin;
import com.oup.eac.domain.UsageLicenceTemplate;
import com.oup.eac.domain.User;
import com.oup.eac.domain.ValidatedRegistrationActivation;
import com.oup.eac.domain.migrationtool.CustomerMigration;
import com.oup.eac.domain.migrationtool.CustomerMigrationData;

/**
 * @author harlandd DataLoader stores all IDataSets and returns them all an array for loading.
 */
public class DataLoader {

    private static final String SEED_FILE_POSTFIX = ".xml";

    private static final String DATE_FORMAT = "yyyy-MM-dd";

    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private List<IDataSet> dataSets = new ArrayList<IDataSet>();

    private FlatXmlDataSetBuilder flatXmlDataSetBuilder = new FlatXmlDataSetBuilder();

    public final void addProgressBarElement(final ProgressBarElement progressBarElement) throws Exception {
        InputStream inputStream = getInputStream(ProgressBarElement.class);
        ReplacementDataSet replacementDataSet = new ReplacementDataSet(flatXmlDataSetBuilder.build(inputStream));
        replacementDataSet.addReplacementObject("[PROGRESS_BAR_ID]", progressBarElement.getProgressBar().getId());
        replacementDataSet.addReplacementObject("[LABEL]", progressBarElement.getLabel());
        replacementDataSet.addReplacementObject("[DEFAULT_MESSAGE]", progressBarElement.getDefaultMessage());
        replacementDataSet.addReplacementObject("[SEQUENCE]", progressBarElement.getSequence());
        replacementDataSet.addReplacementObject("[ELEMENT_TYPE]", progressBarElement.getElementType().toString());
        addBaseDomainObject(replacementDataSet, progressBarElement);
        IOUtils.closeQuietly(inputStream);  	
    }        
    
    public final void addProgressBar(final ProgressBar progressBar) throws Exception {
        InputStream inputStream = getInputStream(ProgressBar.class);
        ReplacementDataSet replacementDataSet = new ReplacementDataSet(flatXmlDataSetBuilder.build(inputStream));
        replacementDataSet.addReplacementObject("[PAGE]", progressBar.getPage());
        replacementDataSet.addReplacementObject("[USER_STATE]", progressBar.getUserState().toString());
        replacementDataSet.addReplacementObject("[REGISTRATION_STATE]", progressBar.getRegistrationState().toString());
        replacementDataSet.addReplacementObject("[REGISTRATION_TYPE]", progressBar.getRegistrationType().toString());
        replacementDataSet.addReplacementObject("[ACTIVATION_STRATEGY]", progressBar.getActivationStrategy().toString());
        replacementDataSet.addReplacementObject("[ACTIVATION_STATE]", progressBar.getActivationState().toString());
        replacementDataSet.addReplacementObject("[TOKEN_STATE]", progressBar.getTokenState().toString());
        replacementDataSet.addReplacementObject("[ACCOUNT_VALIDATED]", progressBar.getAccountValidated().toString());        
        addBaseDomainObject(replacementDataSet, progressBar);
        IOUtils.closeQuietly(inputStream);  	
    }     
    
    public final void addUserRoles(final User user) throws Exception {
        for (Role role : user.getRoles()) {
        	InputStream inputStream = User.class.getResourceAsStream("CustomerRoles" + SEED_FILE_POSTFIX);
            ReplacementDataSet replacementDataSet = new ReplacementDataSet(flatXmlDataSetBuilder.build(inputStream));
        	replacementDataSet.addReplacementObject("[CUSTOMER_ID]", user.getId());
            replacementDataSet.addReplacementObject("[ROLE_ID]", role.getId());
            addDataSet(replacementDataSet);
            IOUtils.closeQuietly(inputStream);  	
        }        
    }   
    
    public final void addRolePermissions(final Role role) throws Exception {
        for (Permission permission : role.getPermissions()) {
        	InputStream inputStream = Role.class.getResourceAsStream("RolePermissions" + SEED_FILE_POSTFIX);
            ReplacementDataSet replacementDataSet = new ReplacementDataSet(flatXmlDataSetBuilder.build(inputStream));
        	replacementDataSet.addReplacementObject("[ROLE_ID]", role.getId());
            replacementDataSet.addReplacementObject("[PERMISSION_ID]", permission.getId());
            addDataSet(replacementDataSet);
            IOUtils.closeQuietly(inputStream);  	
        }        
    }   
    
    public final void addRole(final Role role) throws Exception {
        InputStream inputStream = getInputStream(Role.class);
        ReplacementDataSet replacementDataSet = new ReplacementDataSet(flatXmlDataSetBuilder.build(inputStream));
        replacementDataSet.addReplacementObject("[NAME]", role.getName());
        addBaseDomainObject(replacementDataSet, role);
        IOUtils.closeQuietly(inputStream);  	
    }   
    
    public final void addPermission(final Permission permission) throws Exception {
        InputStream inputStream = getInputStream(Permission.class);
        ReplacementDataSet replacementDataSet = new ReplacementDataSet(flatXmlDataSetBuilder.build(inputStream));
        replacementDataSet.addReplacementObject("[NAME]", permission.getName());
        addBaseDomainObject(replacementDataSet, permission);
        IOUtils.closeQuietly(inputStream);  	
    }   
    
    public final void addActivationCode(final ActivationCode activationCode) throws Exception {
        InputStream inputStream = getInputStream(ActivationCode.class);
        ReplacementDataSet replacementDataSet = new ReplacementDataSet(flatXmlDataSetBuilder.build(inputStream));
        replacementDataSet.addReplacementObject("[ACTIVATION_CODE_BATCH_ID]", activationCode.getActivationCodeBatch().getId());
        replacementDataSet.addReplacementObject("[CODE]", activationCode.getCode());
        replacementDataSet.addReplacementObject("[ALLOWED_USAGE]", activationCode.getAllowedUsage());
        replacementDataSet.addReplacementObject("[ACTUAL_USAGE]", activationCode.getActualUsage());
        addBaseDomainObject(replacementDataSet, activationCode);
        IOUtils.closeQuietly(inputStream);  	
    }   
    
    public final void addActivationCodeBatch(final ActivationCodeBatch activationCodeBatch) throws Exception {
        InputStream inputStream = getInputStream(ActivationCodeBatch.class);
        ReplacementDataSet replacementDataSet = new ReplacementDataSet(flatXmlDataSetBuilder.build(inputStream));
        replacementDataSet.addReplacementObject("[LICENCE_TEMPLATE_ID]", activationCodeBatch.getLicenceTemplate().getId());
        replacementDataSet.addReplacementObject("[ACTIVATION_CODE_FORMAT]", activationCodeBatch.getActivationCodeFormat().name());
        replacementDataSet.addReplacementObject("[CODE_PREFIX]", activationCodeBatch.getCodePrefix());
        replacementDataSet.addReplacementObject("[BATCH_ID]", activationCodeBatch.getBatchId());
        replacementDataSet.addReplacementObject("[ACTIVATION_CODE_REGISTRATION_DEFINITION_ID]", activationCodeBatch.getActivationCodeRegistrationDefinition().getId());

        if (activationCodeBatch.getStartDate() != null) {
            replacementDataSet.addReplacementObject("[START_DATE]", activationCodeBatch.getStartDate().toString(DATE_TIME_FORMAT));
        } else {
            replacementDataSet.addReplacementObject("[START_DATE]", null);
        }
        
        if (activationCodeBatch.getEndDate() != null) {
            replacementDataSet.addReplacementObject("[END_DATE]", activationCodeBatch.getEndDate().toString(DATE_TIME_FORMAT));
        } else {
            replacementDataSet.addReplacementObject("[END_DATE]", null);
        }

        addCreatedAudit(replacementDataSet, activationCodeBatch);
        IOUtils.closeQuietly(inputStream);  	
    }  
    
    public final void addActivationCodeRegistration(final ActivationCodeRegistration activationCodeRegistration) throws Exception {
        InputStream inputStream = getInputStream(Registration.class);
        ReplacementDataSet replacementDataSet = new ReplacementDataSet(flatXmlDataSetBuilder.build(inputStream));
        ActivationCode ac = activationCodeRegistration.getActivationCode();
        if(ac == null){
            replacementDataSet.addReplacementObject("[ACTIVATION_CODE_ID]", null);
        }else{
            replacementDataSet.addReplacementObject("[ACTIVATION_CODE_ID]", ac.getId());
        }
        addRegistration(activationCodeRegistration, replacementDataSet);
        IOUtils.closeQuietly(inputStream);  	
    } 
    
    /**
     * @param standardLicenceTemplate
     *            the standardLicenceTemplate
     * @throws Exception
     *             the exception the exception
     */
    public final void addDivision(final Division division) throws Exception {
        InputStream inputStream = getInputStream(Division.class);
        ReplacementDataSet replacementDataSet = new ReplacementDataSet(flatXmlDataSetBuilder.build(inputStream));
        replacementDataSet.addReplacementObject("[DIVISION_TYPE]", division.getDivisionType());
        addCreatedAudit(replacementDataSet, division);
        IOUtils.closeQuietly(inputStream);
    }    
    
    /**
     * @param standardLicenceTemplate
     *            the standardLicenceTemplate
     * @throws Exception
     *             the exception the exception
     */
    public final void addStandardLicenceTemplate(final StandardLicenceTemplate standardLicenceTemplate) throws Exception {
        InputStream inputStream = getInputStream(LicenceTemplate.class);
        ReplacementDataSet replacementDataSet = new ReplacementDataSet(flatXmlDataSetBuilder.build(inputStream));
        replacementDataSet.addReplacementObject("[TOTAL_CONCURRENCY]", null);
        replacementDataSet.addReplacementObject("[USER_CONCURRENCY]", null);
        replacementDataSet.addReplacementObject("[TIME_PERIOD]", null);
        replacementDataSet.addReplacementObject("[ALLOWED_USAGES]", null);
        replacementDataSet.addReplacementObject("[UNIT_TYPE]", null);
        replacementDataSet.addReplacementObject("[BEGIN_ON]", null);
        addLicenceTemplate(standardLicenceTemplate, replacementDataSet);
        IOUtils.closeQuietly(inputStream);
    }
    
    /**
     * @param standardLicenceTemplate
     *            the standardLicenceTemplate
     * @throws Exception
     *             the exception the exception
     */
    public final void addRollingLicenceTemplate(final RollingLicenceTemplate rollingLicenceTemplate) throws Exception {
        InputStream inputStream = getInputStream(LicenceTemplate.class);
        ReplacementDataSet replacementDataSet = new ReplacementDataSet(flatXmlDataSetBuilder.build(inputStream));
        replacementDataSet.addReplacementObject("[TOTAL_CONCURRENCY]", null);
        replacementDataSet.addReplacementObject("[USER_CONCURRENCY]", null);
        replacementDataSet.addReplacementObject("[TIME_PERIOD]", rollingLicenceTemplate.getTimePeriod());
        replacementDataSet.addReplacementObject("[ALLOWED_USAGES]", null);
        replacementDataSet.addReplacementObject("[UNIT_TYPE]", rollingLicenceTemplate.getUnitType().name());
        replacementDataSet.addReplacementObject("[BEGIN_ON]", rollingLicenceTemplate.getBeginOn().name());
        addLicenceTemplate(rollingLicenceTemplate, replacementDataSet);
        IOUtils.closeQuietly(inputStream);
    }
    
    /**
     * @param standardLicenceTemplate
     *            the standardLicenceTemplate
     * @throws Exception
     *             the exception the exception
     */
	public final void addConcurrentLicenceTemplate(final ConcurrentLicenceTemplate concurrentLicenceTemplate) throws Exception {
        InputStream inputStream = getInputStream(LicenceTemplate.class);
        ReplacementDataSet replacementDataSet = new ReplacementDataSet(flatXmlDataSetBuilder.build(inputStream));
        replacementDataSet.addReplacementObject("[TOTAL_CONCURRENCY]", concurrentLicenceTemplate.getTotalConcurrency());
        replacementDataSet.addReplacementObject("[USER_CONCURRENCY]", concurrentLicenceTemplate.getUserConcurrency());
        replacementDataSet.addReplacementObject("[TIME_PERIOD]", null);
        replacementDataSet.addReplacementObject("[ALLOWED_USAGES]", null);
        replacementDataSet.addReplacementObject("[UNIT_TYPE]", null);
        replacementDataSet.addReplacementObject("[BEGIN_ON]", null);
        addLicenceTemplate(concurrentLicenceTemplate, replacementDataSet);
        IOUtils.closeQuietly(inputStream);
    }
    
    /**
     * @param standardLicenceTemplate
     *            the standardLicenceTemplate
     * @throws Exception
     *             the exception the exception
     */
    public final void addUsageLicenceTemplate(final UsageLicenceTemplate usageLicenceTemplate) throws Exception {
        InputStream inputStream = getInputStream(LicenceTemplate.class);
        ReplacementDataSet replacementDataSet = new ReplacementDataSet(flatXmlDataSetBuilder.build(inputStream));
        replacementDataSet.addReplacementObject("[TOTAL_CONCURRENCY]", null);
        replacementDataSet.addReplacementObject("[USER_CONCURRENCY]", null);
        replacementDataSet.addReplacementObject("[TIME_PERIOD]", null);
        replacementDataSet.addReplacementObject("[ALLOWED_USAGES]", usageLicenceTemplate.getAllowedUsages());
        replacementDataSet.addReplacementObject("[UNIT_TYPE]", null);
        replacementDataSet.addReplacementObject("[BEGIN_ON]", null);
        addLicenceTemplate(usageLicenceTemplate, replacementDataSet);
        IOUtils.closeQuietly(inputStream);
    }
    
    /**
     * @param licenceTemplate
     *            the licenceTemplate
     * @param replacementDataSet
     *            the replacement data set
     * @throws Exception
     *             the exception
     */
    private void addLicenceTemplate(final LicenceTemplate licenceTemplate, final ReplacementDataSet replacementDataSet) throws Exception {
        replacementDataSet.addReplacementObject("[LICENCE_TYPE]", licenceTemplate.getLicenceType().toString());
        replacementDataSet.addReplacementObject("[START_DATE]", licenceTemplate.getStartDate() == null ? null : licenceTemplate.getStartDate().toString(DATE_FORMAT));
        replacementDataSet.addReplacementObject("[END_DATE]", licenceTemplate.getEndDate() == null ? null : licenceTemplate.getEndDate().toString(DATE_FORMAT));
        replacementDataSet.addReplacementObject("[CREATED_DATE]", licenceTemplate.getCreatedDate() == null ? null : licenceTemplate.getCreatedDate().toString(DATE_FORMAT));
        replacementDataSet.addReplacementObject("[UPDATED_DATE]", licenceTemplate.getUpdatedDate() == null ? null : licenceTemplate.getUpdatedDate().toString(DATE_FORMAT));
        addBaseDomainObject(replacementDataSet, licenceTemplate);
    }

    /**
     * @param tagOption
     *            the tag option
     * @throws Exception
     *             the exception
     */
    public final void addTagOption(final TagOption tagOption) throws Exception {
        InputStream inputStream = getInputStream(TagOption.class);
        ReplacementDataSet replacementDataSet = new ReplacementDataSet(flatXmlDataSetBuilder.build(inputStream));
        replacementDataSet.addReplacementObject("[TAG_ID]", tagOption.getTag().getId());
        replacementDataSet.addReplacementObject("[LABEL]", tagOption.getLabel());
        replacementDataSet.addReplacementObject("[VALUE]", tagOption.getValue());
        replacementDataSet.addReplacementObject("[SEQUENCE]", Integer.valueOf(tagOption.getSequence()));
        addBaseDomainObject(replacementDataSet, tagOption);
        IOUtils.closeQuietly(inputStream);
    }

    /**
     * @param radio
     *            the radio tag
     * @throws Exception
     *             the exception the exception
     */
    public final void addRadio(final Radio radio) throws Exception {
        InputStream inputStream = getInputStream(Tag.class);
        ReplacementDataSet replacementDataSet = new ReplacementDataSet(flatXmlDataSetBuilder.build(inputStream));
        replacementDataSet.addReplacementObject("[EMPTY_OPTION]", Boolean.FALSE);
        replacementDataSet.addReplacementObject("[VALUE]", null);
        replacementDataSet.addReplacementObject("[NEW_WINDOW]", null);
        addTag(radio, replacementDataSet);
        IOUtils.closeQuietly(inputStream);
    }

    /**
     * @param select
     *            the select
     * @throws Exception
     *             the exception
     */
    public final void addSelect(final Select select) throws Exception {
        InputStream inputStream = getInputStream(Tag.class);
        ReplacementDataSet replacementDataSet = new ReplacementDataSet(flatXmlDataSetBuilder.build(inputStream));
        replacementDataSet.addReplacementObject("[EMPTY_OPTION]", Boolean.valueOf(select.isEmptyOption()));
        replacementDataSet.addReplacementObject("[VALUE]", null);
        replacementDataSet.addReplacementObject("[NEW_WINDOW]", null);
        addTag(select, replacementDataSet);
        IOUtils.closeQuietly(inputStream);
    }

    /**
     * @param select
     *            the select
     * @throws Exception
     *             the exception
     */
    public final void addMultiSelect(final MultiSelect select) throws Exception {
        InputStream inputStream = getInputStream(Tag.class);
        ReplacementDataSet replacementDataSet = new ReplacementDataSet(flatXmlDataSetBuilder.build(inputStream));
        replacementDataSet.addReplacementObject("[EMPTY_OPTION]", Boolean.FALSE);
        replacementDataSet.addReplacementObject("[VALUE]", null);
        replacementDataSet.addReplacementObject("[NEW_WINDOW]", null);
        addTag(select, replacementDataSet);
        IOUtils.closeQuietly(inputStream);
    }    
    
    /**
     * @param checkbox
     *            the checkbox
     * @throws Exception
     *             the exception
     */
    public final void addCheckbox(final Checkbox checkbox) throws Exception {
        InputStream inputStream = getInputStream(Tag.class);
        ReplacementDataSet replacementDataSet = new ReplacementDataSet(flatXmlDataSetBuilder.build(inputStream));
        replacementDataSet.addReplacementObject("[VALUE]", checkbox.getValue());
        replacementDataSet.addReplacementObject("[EMPTY_OPTION]", Boolean.FALSE);
        replacementDataSet.addReplacementObject("[NEW_WINDOW]", null);
        addTag(checkbox, replacementDataSet);
        IOUtils.closeQuietly(inputStream);
    }

    /**
     * @param passwordField
     *            the password field
     * @throws Exception
     *             the exception
     */
    public final void addPasswordField(final PasswordField passwordField) throws Exception {
        InputStream inputStream = getInputStream(Tag.class);
        ReplacementDataSet replacementDataSet = new ReplacementDataSet(flatXmlDataSetBuilder.build(inputStream));
        replacementDataSet.addReplacementObject("[EMPTY_OPTION]", Boolean.FALSE);
        replacementDataSet.addReplacementObject("[VALUE]", null);
        replacementDataSet.addReplacementObject("[NEW_WINDOW]", null);
        addTag(passwordField, replacementDataSet);
        IOUtils.closeQuietly(inputStream);
    }

    /**
     * @param textField
     *            the text field
     * @throws Exception
     *             the exception
     */
    public final void addTextField(final TextField textField) throws Exception {
        InputStream inputStream = getInputStream(Tag.class);
        ReplacementDataSet replacementDataSet = new ReplacementDataSet(flatXmlDataSetBuilder.build(inputStream));
        replacementDataSet.addReplacementObject("[EMPTY_OPTION]", Boolean.FALSE);
        replacementDataSet.addReplacementObject("[VALUE]", null);
        replacementDataSet.addReplacementObject("[NEW_WINDOW]", null);
        addTag(textField, replacementDataSet);
        IOUtils.closeQuietly(inputStream);
    }

    /**
     * @param label
     *            the label
     * @throws Exception
     *             the exception
     */
    public final void addLabel(final Label label) throws Exception {
        InputStream inputStream = getInputStream(Tag.class);
        ReplacementDataSet replacementDataSet = new ReplacementDataSet(flatXmlDataSetBuilder.build(inputStream));
        replacementDataSet.addReplacementObject("[EMPTY_OPTION]", Boolean.FALSE);
        replacementDataSet.addReplacementObject("[VALUE]", null);
        replacementDataSet.addReplacementObject("[NEW_WINDOW]", null);
        addTag(label, replacementDataSet);
        IOUtils.closeQuietly(inputStream);
    }

    /**
     * @param urlLink
     *            the urlLink
     * @throws Exception
     *             the exception
     */
    public final void addUrlLink(final UrlLink urlLink) throws Exception {
        InputStream inputStream = getInputStream(Tag.class);
        ReplacementDataSet replacementDataSet = new ReplacementDataSet(flatXmlDataSetBuilder.build(inputStream));
        replacementDataSet.addReplacementObject("[EMPTY_OPTION]", Boolean.FALSE);
        replacementDataSet.addReplacementObject("[VALUE]", null);
        replacementDataSet.addReplacementObject("[URL]", urlLink.getUrl());
        replacementDataSet.addReplacementObject("[NEW_WINDOW]", Boolean.valueOf(urlLink.getUrl()));
        addTag(urlLink, replacementDataSet);
        IOUtils.closeQuietly(inputStream);
    }

    /**
     * @param tag
     *            the tag
     * @param replacementDataSet
     *            the replacement data set
     * @throws Exception
     *             the exception
     */
    private void addTag(final Tag tag, final ReplacementDataSet replacementDataSet) throws Exception {
        replacementDataSet.addReplacementObject("[TAG_TYPE]", tag.getTagType().toString());
        replacementDataSet.addReplacementObject("[ELEMENT_ID]", tag.getElement().getId());
        addBaseDomainObject(replacementDataSet, tag);
    }

    /**
     * @param elementCountryRestriction
     *            the elementCountryRestriction
     * @throws Exception
     *             the exception
     */
    public final void addElementCountryRestriction(final ElementCountryRestriction elementCountryRestriction) throws Exception {
        InputStream inputStream = getInputStream(ElementCountryRestriction.class);
        ReplacementDataSet replacementDataSet = new ReplacementDataSet(flatXmlDataSetBuilder.build(inputStream));
        replacementDataSet.addReplacementObject("[ELEMENT_ID]", elementCountryRestriction.getElement().getId());
        replacementDataSet.addReplacementObject("[LOCALE]", elementCountryRestriction.getLocale());
        addBaseDomainObject(replacementDataSet, elementCountryRestriction);
        IOUtils.closeQuietly(inputStream);
    }    
    
    /**
     * @param answer
     *            the answer
     * @throws Exception
     *             the exception
     */
    public final void addAnswer(final Answer answer) throws Exception {
        InputStream inputStream = getInputStream(Answer.class);
        ReplacementDataSet replacementDataSet = new ReplacementDataSet(flatXmlDataSetBuilder.build(inputStream));
        replacementDataSet.addReplacementObject("[QUESTION_ID]", answer.getQuestion().getId());
        replacementDataSet.addReplacementObject("[CUSTOMER_ID]", answer.getCustomerId());
        replacementDataSet.addReplacementObject("[ANSWER_TEXT]", answer.getAnswerText());
        replacementDataSet.addReplacementObject("[ANSWER_TYPE]", answer.getAnswerType().toString());
        replacementDataSet.addReplacementObject("[REGISTERABLE_PRODUCT_ID]", null);
        addCreatedAudit(replacementDataSet, answer);
        IOUtils.closeQuietly(inputStream);
    }
    
    /**
     * @param answer
     *            the answer
     * @throws Exception
     *             the exception
     */
    public final void addProductSpecificAnswer(final ProductSpecificAnswer productSpecificAnswer) throws Exception {
        InputStream inputStream = getInputStream(Answer.class);
        ReplacementDataSet replacementDataSet = new ReplacementDataSet(flatXmlDataSetBuilder.build(inputStream));
        replacementDataSet.addReplacementObject("[QUESTION_ID]", productSpecificAnswer.getQuestion().getId());
        replacementDataSet.addReplacementObject("[CUSTOMER_ID]", productSpecificAnswer.getCustomerId());
        replacementDataSet.addReplacementObject("[ANSWER_TEXT]", productSpecificAnswer.getAnswerText());
        replacementDataSet.addReplacementObject("[ANSWER_TYPE]", productSpecificAnswer.getAnswerType().toString());
        replacementDataSet.addReplacementObject("[REGISTERABLE_PRODUCT_ID]", productSpecificAnswer.getProductId());
        addCreatedAudit(replacementDataSet, productSpecificAnswer);
        IOUtils.closeQuietly(inputStream);
    }

    /**
     * @param element
     *            the element
     * @throws Exception
     *             the exception
     */
    public final void addElement(final Element element) throws Exception {
        InputStream inputStream = getInputStream(Element.class);
        ReplacementDataSet replacementDataSet = new ReplacementDataSet(flatXmlDataSetBuilder.build(inputStream));
        replacementDataSet.addReplacementObject("[REGULAR_EXPRESSION]", element.getRegularExpression());
        replacementDataSet.addReplacementObject("[QUESTION_ID]", element.getQuestion().getId());
        replacementDataSet.addReplacementObject("[TITLE_TEXT]", element.getTitleText());
        replacementDataSet.addReplacementObject("[HELP_TEXT]", element.getHelpText());
        addBaseDomainObject(replacementDataSet, element);
        IOUtils.closeQuietly(inputStream);
    }
    
    /**
     * @param element
     *            the element
     * @throws Exception
     *             the exception
     */
    public final void addQuestion(final Question question) throws Exception {
        InputStream inputStream = getInputStream(Question.class);
        ReplacementDataSet replacementDataSet = new ReplacementDataSet(flatXmlDataSetBuilder.build(inputStream));
        replacementDataSet.addReplacementObject("[PRODUCT_SPECIFIC]", question.isProductSpecific());
        replacementDataSet.addReplacementObject("[ELEMENT_TEXT]", question.getElementText());
        replacementDataSet.addReplacementObject("[DESCRIPTION]", question.getDescription());
        addBaseDomainObject(replacementDataSet, question);
        IOUtils.closeQuietly(inputStream);
    }
    
    /**
     * @param element
     *            the element
     * @throws Exception
     *             the exception
     */
    public final void addExportName(final ExportName exportName) throws Exception {
        InputStream inputStream = getInputStream(ExportName.class);
        ReplacementDataSet replacementDataSet = new ReplacementDataSet(flatXmlDataSetBuilder.build(inputStream));
        replacementDataSet.addReplacementObject("[QUESTION_ID]", exportName.getQuestion().getId());
        replacementDataSet.addReplacementObject("[NAME]", exportName.getName());
        replacementDataSet.addReplacementObject("[EXPORT_TYPE]", exportName.getExportType().toString());
        replacementDataSet.addReplacementObject("[CREATED_DATE]", exportName.getCreatedDate().toString(DATE_TIME_FORMAT));
        addBaseDomainObject(replacementDataSet, exportName);
        IOUtils.closeQuietly(inputStream);
    }

    /**
     * @param pageComponent
     *            the component
     * @throws Exception
     *             the exception
     */
    public final void addPageComponent(final PageComponent pageComponent) throws Exception {
        InputStream inputStream = getInputStream(PageComponent.class);
        ReplacementDataSet replacementDataSet = new ReplacementDataSet(flatXmlDataSetBuilder.build(inputStream));
        replacementDataSet.addReplacementObject("[PAGE_DEFINITION_ID]", pageComponent.getPageDefinition().getId());
        replacementDataSet.addReplacementObject("[SEQUENCE]", Integer.valueOf(pageComponent.getSequence()));
        replacementDataSet.addReplacementObject("[COMPONENT_ID]", pageComponent.getComponent().getId());
        addBaseDomainObject(replacementDataSet, pageComponent);
        IOUtils.closeQuietly(inputStream);
    }

    /**
     * @param component
     *            the component
     * @throws Exception
     *             the exception
     */
    public final void addComponent(final Component component) throws Exception {
        InputStream inputStream = getInputStream(Component.class);
        ReplacementDataSet replacementDataSet = new ReplacementDataSet(flatXmlDataSetBuilder.build(inputStream));
        replacementDataSet.addReplacementObject("[LABEL_KEY]", component.getLabelKey());
        addBaseDomainObject(replacementDataSet, component);
        IOUtils.closeQuietly(inputStream);
    }
    
    /**
     * @param component
     *            the component
     * @throws Exception
     *             the exception
     */
    public final void addField(final Field field) throws Exception {
        InputStream inputStream = getInputStream(Field.class);
        ReplacementDataSet replacementDataSet = new ReplacementDataSet(flatXmlDataSetBuilder.build(inputStream));
        replacementDataSet.addReplacementObject("[SEQUENCE]", field.getSequence());
        replacementDataSet.addReplacementObject("[COMPONENT_ID]", field.getComponent().getId());
        replacementDataSet.addReplacementObject("[ELEMENT_ID]", field.getElement().getId());
        replacementDataSet.addReplacementObject("[REQUIRED]", Boolean.valueOf(field.isRequired()));
        addBaseDomainObject(replacementDataSet, field);
        IOUtils.closeQuietly(inputStream);
    }

    /**
     * @param productPageDefinition
     *            the page definition
     * @throws Exception
     *             the exception
     */
    public final void addAccountPageDefinition(final AccountPageDefinition accountPageDefinition) throws Exception {
        InputStream inputStream = getInputStream(PageDefinition.class);
        ReplacementDataSet replacementDataSet = new ReplacementDataSet(flatXmlDataSetBuilder.build(inputStream));
        addPageDefinition(accountPageDefinition, replacementDataSet);
        IOUtils.closeQuietly(inputStream);
    }      
    
    /**
     * @param productPageDefinition
     *            the page definition
     * @throws Exception
     *             the exception
     */
    public final void addProductPageDefinition(final ProductPageDefinition productPageDefinition) throws Exception {
        InputStream inputStream = getInputStream(PageDefinition.class);
        ReplacementDataSet replacementDataSet = new ReplacementDataSet(flatXmlDataSetBuilder.build(inputStream));
        addPageDefinition(productPageDefinition, replacementDataSet);
        IOUtils.closeQuietly(inputStream);
    }    
    
    /**
     * @param pageDefinition
     *            the page definition
     * @throws Exception
     *             the exception
     */
    private final void addPageDefinition(final PageDefinition pageDefinition, final ReplacementDataSet replacementDataSet) throws Exception {        
        replacementDataSet.addReplacementObject("[PAGE_DEFINITION_TYPE]", pageDefinition.getPageDefinitionType().toString());
        replacementDataSet.addReplacementObject("[NAME]", pageDefinition.getName());
        replacementDataSet.addReplacementObject("[TITLE]", pageDefinition.getTitle());
        addBaseDomainObject(replacementDataSet, pageDefinition);
    }

    public final void addRegisterableProduct(final RegisterableProduct registerableProduct) throws Exception {
        InputStream inputStream = getNewInputStream(Product.class);
        ReplacementDataSet replacementDataSet = new ReplacementDataSet(flatXmlDataSetBuilder.build(inputStream));
        replacementDataSet.addReplacementObject("[PRODUCT_TYPE]", "REGISTERABLE");
        replacementDataSet.addReplacementObject("[REGISTERABLE_TYPE]", registerableProduct.getRegisterableType().toString());
        replacementDataSet.addReplacementObject("[REGISTERABLE_PRODUCT_ID]", null);
        addProduct(registerableProduct, replacementDataSet);
        IOUtils.closeQuietly(inputStream);
    }    
    
    private InputStream getNewInputStream(Class<Product> aClass) {
    	return aClass.getResourceAsStream(aClass.getSimpleName() + SEED_FILE_POSTFIX);
	}
    
    private InputStream getCustomerInputStream(Class<Customer> aClass) {
    	return aClass.getResourceAsStream(aClass.getSimpleName() + SEED_FILE_POSTFIX);
	}
    
    private InputStream getWhiteListUrlInputStream(Class<WhiteListUrl> aClass) {
    	return aClass.getResourceAsStream(aClass.getSimpleName() + SEED_FILE_POSTFIX);
	}

	public final void addEacGroups(final EacGroups eacGroups) throws Exception{
        InputStream inputStream = getInputStream(EacGroups.class);
        ReplacementDataSet replacementDataSet = new ReplacementDataSet(flatXmlDataSetBuilder.build(inputStream));
        replacementDataSet.addReplacementObject("[ID]", eacGroups.getId());
        replacementDataSet.addReplacementObject("[GROUP_NAME]", eacGroups.getGroupName());
        replacementDataSet.addReplacementObject("[OBJ_VERSION]", eacGroups.getVersion());
        
        if (eacGroups.getCreatedBy()!=null){
            replacementDataSet.addReplacementObject("[CREATED_BY_ADMIN_USER_ID]", eacGroups.getCreatedBy().getId());
        } else {
            replacementDataSet.addReplacementObject("[CREATED_BY_ADMIN_USER_ID]", "ADMIN");
        }
        
        if (eacGroups.getUpdatedBy()!=null){
            replacementDataSet.addReplacementObject("[UPDATED_BY_ADMIN_USER_ID]", eacGroups.getUpdatedBy().getId());
        } else {
            replacementDataSet.addReplacementObject("[UPDATED_BY_ADMIN_USER_ID]", null);
        }
        
        if (eacGroups.getCreatedDate() != null) {
            replacementDataSet.addReplacementObject("[CREATED_DATE]", eacGroups.getCreatedDate().toString(DATE_TIME_FORMAT));
        } else {
            replacementDataSet.addReplacementObject("[CREATED_DATE]", null);
        }
        
        if (eacGroups.getUpdatedDate() != null) {
            replacementDataSet.addReplacementObject("[UPDATED_DATE]", eacGroups.getUpdatedDate().toString(DATE_TIME_FORMAT));
        } else {
            replacementDataSet.addReplacementObject("[UPDATED_DATE]", null);
        }
        
        replacementDataSet.addReplacementObject("[IS_EDITABLE]", eacGroups.isEditable());
        addBaseDomainObject(replacementDataSet, eacGroups);
        IOUtils.closeQuietly(inputStream);
    }  
    
    public final void addProductGroupsMapping(final EacGroups eacGroups) throws Exception{
        for(Product products: eacGroups.getProducts()) {
            InputStream inputStream = Role.class.getResourceAsStream("ProductGroupMapping" + SEED_FILE_POSTFIX);
            ReplacementDataSet replacementDataSet = new ReplacementDataSet(flatXmlDataSetBuilder.build(inputStream));
            replacementDataSet.addReplacementObject("[GROUP_ID]", eacGroups.getId());
            replacementDataSet.addReplacementObject("[PRODUCT_ID]", products.getId());
            addDataSet(replacementDataSet);
            IOUtils.closeQuietly(inputStream);
        }
    }
    
    public final void addLinkedProduct(final LinkedProduct linkedProduct) throws Exception {
        InputStream inputStream = getNewInputStream(Product.class);
        ReplacementDataSet replacementDataSet = new ReplacementDataSet(flatXmlDataSetBuilder.build(inputStream));
        replacementDataSet.addReplacementObject("[REGISTERABLE_PRODUCT_ID]", linkedProduct.getId());
        replacementDataSet.addReplacementObject("[PRODUCT_TYPE]", "LINKED");
        //replacementDataSet.addReplacementObject("[ACTIVATION_METHOD]", linkedProduct.getActivationMethod().toString());
        addProduct(linkedProduct, replacementDataSet);
        IOUtils.closeQuietly(inputStream);
    }   
    
    public final void addAccountRegistrationDefinition(final AccountRegistrationDefinition accountRegistrationDefinition) throws Exception {
    	InputStream inputStream = getInputStream(RegistrationDefinition.class);
        ReplacementDataSet replacementDataSet = new ReplacementDataSet(flatXmlDataSetBuilder.build(inputStream));
        
        replacementDataSet.addReplacementObject("[REGISTRATION_DEFINITION_TYPE]", accountRegistrationDefinition.getRegistrationDefinitionType().toString());
        replacementDataSet.addReplacementObject("[VALIDATION_REQUIRED]", Boolean.valueOf(accountRegistrationDefinition.isValidationRequired()));
        replacementDataSet.addReplacementObject("[CONFIRMATION_EMAIL_ENABLED]", Boolean.FALSE);
        replacementDataSet.addReplacementObject("[PRODUCT_ID]", accountRegistrationDefinition.getProduct().getId());
        replacementDataSet.addReplacementObject("[LICENCE_TEMPLATE_ID]", null);
        if (accountRegistrationDefinition.getPageDefinition() != null) {
        	replacementDataSet.addReplacementObject("[PAGE_DEFINITION_ID]", accountRegistrationDefinition.getPageDefinition().getId());
        }
        else {
        	replacementDataSet.addReplacementObject("[PAGE_DEFINITION_ID]", null);
        }
        //replacementDataSet.addReplacementObject("[REGISTRATION_ACTIVATION_ID]", accountRegistrationDefinition.getRegistrationActivation().getId());
        addBaseDomainObject(replacementDataSet, accountRegistrationDefinition);
        
        IOUtils.closeQuietly(inputStream);
    }
    
    public final void addProductRegistrationDefinition(final ProductRegistrationDefinition productRegistrationDefinition) throws Exception {
    	InputStream inputStream = getInputStream(RegistrationDefinition.class);
        ReplacementDataSet replacementDataSet = new ReplacementDataSet(flatXmlDataSetBuilder.build(inputStream));
        
        replacementDataSet.addReplacementObject("[REGISTRATION_DEFINITION_TYPE]", productRegistrationDefinition.getRegistrationDefinitionType().toString());
        replacementDataSet.addReplacementObject("[PRODUCT_ID]", productRegistrationDefinition.getProduct().getId());
        replacementDataSet.addReplacementObject("[VALIDATION_REQUIRED]", Boolean.FALSE);
        replacementDataSet.addReplacementObject("[CONFIRMATION_EMAIL_ENABLED]", productRegistrationDefinition.isConfirmationEmailEnabled());
        LicenceTemplate licTemp = productRegistrationDefinition.getLicenceTemplate();
        if(licTemp != null){
            replacementDataSet.addReplacementObject("[LICENCE_TEMPLATE_ID]", licTemp.getId());
        }else{
            replacementDataSet.addReplacementObject("[LICENCE_TEMPLATE_ID]", null);
        }
        if (productRegistrationDefinition.getPageDefinition() != null) {
        	replacementDataSet.addReplacementObject("[PAGE_DEFINITION_ID]", productRegistrationDefinition.getPageDefinition().getId());
        }
        else {
        	replacementDataSet.addReplacementObject("[PAGE_DEFINITION_ID]", null);
        }
        //replacementDataSet.addReplacementObject("[REGISTRATION_ACTIVATION_ID]", productRegistrationDefinition.getRegistrationActivation().getId());
        addBaseDomainObject(replacementDataSet, productRegistrationDefinition);
        
        IOUtils.closeQuietly(inputStream);
    }
    
    public final void addActivationCodeRegistrationDefinition(final ActivationCodeRegistrationDefinition activationCodeRegistrationDefinition) throws Exception {
    	InputStream inputStream = getInputStream(RegistrationDefinition.class);
        ReplacementDataSet replacementDataSet = new ReplacementDataSet(flatXmlDataSetBuilder.build(inputStream));
        
        replacementDataSet.addReplacementObject("[REGISTRATION_DEFINITION_TYPE]", activationCodeRegistrationDefinition.getRegistrationDefinitionType().toString());
        if(activationCodeRegistrationDefinition.getProduct() != null){
        	replacementDataSet.addReplacementObject("[PRODUCT_ID]", activationCodeRegistrationDefinition.getProduct().getId());
        }else{
        	replacementDataSet.addReplacementObject("[PRODUCT_ID]", null);
        }
        
        if(activationCodeRegistrationDefinition.getEacGroup() != null){
        	replacementDataSet.addReplacementObject("[GROUP_ID]", activationCodeRegistrationDefinition.getEacGroup().getId());
        }else{
        	replacementDataSet.addReplacementObject("[GROUP_ID]", null);
        }
        
        //replacementDataSet.addReplacementObject("[LICENCE_TEMPLATE_ID]", activationCodeRegistrationDefinition.getLicenceTemplate().getId());
        if (activationCodeRegistrationDefinition.getPageDefinition() != null) {
        	replacementDataSet.addReplacementObject("[PAGE_DEFINITION_ID]", activationCodeRegistrationDefinition.getPageDefinition().getId());
        }
        else {
        	replacementDataSet.addReplacementObject("[PAGE_DEFINITION_ID]", null);
        }
        //replacementDataSet.addReplacementObject("[REGISTRATION_ACTIVATION_ID]", activationCodeRegistrationDefinition.getRegistrationActivation().getId());
        replacementDataSet.addReplacementObject("[VALIDATION_REQUIRED]", Boolean.FALSE);
        //replacementDataSet.addReplacementObject("[CONFIRMATION_EMAIL_ENABLED]", Boolean.FALSE);
        addBaseDomainObject(replacementDataSet, activationCodeRegistrationDefinition);
        
        IOUtils.closeQuietly(inputStream);
    }
  
    public final void addInstantRegistrationActivation(final InstantRegistrationActivation instantLicenceActivation) throws Exception {
    	InputStream inputStream = getInputStream(RegistrationActivation.class);
        ReplacementDataSet replacementDataSet = new ReplacementDataSet(flatXmlDataSetBuilder.build(inputStream));
        addLicenceActivation(instantLicenceActivation, replacementDataSet);
        IOUtils.closeQuietly(inputStream);
    }    
    
    public final void addSelfRegistrationActivation(final SelfRegistrationActivation selfLicenceActivation) throws Exception {
    	InputStream inputStream = getInputStream(RegistrationActivation.class);
        ReplacementDataSet replacementDataSet = new ReplacementDataSet(flatXmlDataSetBuilder.build(inputStream));
        addLicenceActivation(selfLicenceActivation, replacementDataSet);
        IOUtils.closeQuietly(inputStream);
    }
    
    public final void addValidatedRegistrationActivation(final ValidatedRegistrationActivation validatedRegistrationActivation) throws Exception {
    	InputStream inputStream = getInputStream(RegistrationActivation.class);
        ReplacementDataSet replacementDataSet = new ReplacementDataSet(flatXmlDataSetBuilder.build(inputStream));
        replacementDataSet.addReplacementObject("[VALIDATOR_EMAIL]", validatedRegistrationActivation.getValidatorEmail());
        addLicenceActivation(validatedRegistrationActivation, replacementDataSet);
        IOUtils.closeQuietly(inputStream);
    }
    
    private final void addLicenceActivation(final RegistrationActivation licenceActivation, final ReplacementDataSet replacementDataSet) throws Exception {
        replacementDataSet.addReplacementObject("[ACTIVATION_TYPE]", licenceActivation.getActivationStrategy().toString());
        addBaseDomainObject(replacementDataSet, licenceActivation);
    }
    
    /**
     * @param product
     *            the product 
     * @throws Exception
     *             the exception
     */
    private final void addProduct(final Product product, final ReplacementDataSet replacementDataSet) throws Exception {
        replacementDataSet.addReplacementObject("[ID]", product.getId());
        replacementDataSet.addReplacementObject("[STATE]", product.getState());
        replacementDataSet.addReplacementObject("[ERIGHTS_ID]", product.getErightsId());
        replacementDataSet.addReplacementObject("[PRODUCT_NAME]", product.getProductName());
        replacementDataSet.addReplacementObject("[LANDING_PAGE]", product.getLandingPage());
       // addBaseDomainObject(replacementDataSet, product);
    }
    
    /**
     * @param customer
     *            the user
     * @throws Exception
     *             the exception
     */
    public final void addRegistration(final Registration<? extends ProductRegistrationDefinition> registration) throws Exception {
        InputStream inputStream = getInputStream(Registration.class);
        ReplacementDataSet replacementDataSet = new ReplacementDataSet(flatXmlDataSetBuilder.build(inputStream));
        
        if (registration.getRegistrationType().equals(RegistrationType.ACTIVATION_CODE)) {
        	replacementDataSet.addReplacementObject("[ACTIVATION_CODE_ID]", ((ActivationCodeRegistration)registration).getActivationCode().getId());
        } else {
        	replacementDataSet.addReplacementObject("[ACTIVATION_CODE_ID]", null);
        }
        
        addRegistration(registration, replacementDataSet);
        IOUtils.closeQuietly(inputStream);
    }    
    
    /**
     * @param customer
     *            the user
     * @throws Exception
     *             the exception
     */
    private final void addRegistration(final Registration<? extends ProductRegistrationDefinition> registration, final ReplacementDataSet replacementDataSet) 
    																						throws Exception {
        replacementDataSet.addReplacementObject("[CUSTOMER_ID]", registration.getCustomer().getId());
        replacementDataSet.addReplacementObject("[REGISTRATION_TYPE]", registration.getRegistrationType().toString());
        replacementDataSet.addReplacementObject("[REGISTRATION_DEFINITION_ID]", registration.getRegistrationDefinition().getId());
        replacementDataSet.addReplacementObject("[ACTIVATED]", Boolean.valueOf(registration.isActivated()));
        replacementDataSet.addReplacementObject("[COMPLETED]", Boolean.valueOf(registration.isCompleted()));
        replacementDataSet.addReplacementObject("[DENIED]", Boolean.valueOf(registration.isDenied()));
        replacementDataSet.addReplacementObject("[AWAITING_VALIDATION]", Boolean.valueOf(registration.isAwaitingValidation()));
        String erightsLicenceId = registration.getId();
        String erightsLicenceIdVal = null;
        if(erightsLicenceId != null){
            erightsLicenceIdVal = String.valueOf(erightsLicenceId);
        }
        replacementDataSet.addReplacementObject("[ERIGHTS_LICENCE_ID]", erightsLicenceIdVal);
        addUpdatedAudit(replacementDataSet, registration);
    }
    
    /**
     * @param standardLicenceTemplate
     *            the standardLicenceTemplate
     * @throws Exception
     *             the exception the exception
     */
    public final void addAdminUser(final AdminUser adminUser) throws Exception {
        InputStream inputStream = getCustomerInputStream(Customer.class);
        ReplacementDataSet replacementDataSet = new ReplacementDataSet(flatXmlDataSetBuilder.build(inputStream));
        addAdminUser(adminUser, replacementDataSet);
        IOUtils.closeQuietly(inputStream);
    }
    
    /**
     * @param licenceTemplate
     *            the licenceTemplate
     * @param replacementDataSet
     *            the replacement data set
     * @throws Exception
     *             the exception
     */
    private void addAdminUser(final AdminUser adminUser, final ReplacementDataSet replacementDataSet) throws Exception {
        replacementDataSet.addReplacementObject("[ERIGHTS_ID]", null);
        replacementDataSet.addReplacementObject("[CUSTOMER_TYPE]", null);
        addUser(replacementDataSet, adminUser);
    }
    
    /**
     * @param customer
     *            the user
     * @throws Exception
     *             the exception
     */
    public final void addCustomer(final Customer customer) throws Exception {
        InputStream inputStream = getCustomerInputStream(Customer.class);
        ReplacementDataSet replacementDataSet = new ReplacementDataSet(flatXmlDataSetBuilder.build(inputStream));
        replacementDataSet.addReplacementObject("[CUSTOMER_TYPE]", customer.getCustomerType().name().toString());
        replacementDataSet.addReplacementObject("[ERIGHTS_ID]", customer.getId());        
        Locale loc = customer.getLocale();
        String locString = loc == null ? null : loc.toString();
        replacementDataSet.addReplacementObject("[LOCALE]",locString);
        replacementDataSet.addReplacementObject("[TIME_ZONE]",customer.getTimeZone()); 
        addUser(replacementDataSet, customer);
        IOUtils.closeQuietly(inputStream);
    }
    
    public final void addWhiteListUrlService(final WhiteListUrl whiteListUrl) throws DataSetException {
        InputStream inputStream = getInputStream(WhiteListUrl.class);
        ReplacementDataSet replacementDataSet = new ReplacementDataSet(flatXmlDataSetBuilder.build(inputStream));
        replacementDataSet.addReplacementObject("[URL]",whiteListUrl.getUrl());
        replacementDataSet.addReplacementObject("[ID]",whiteListUrl.getId());
        replacementDataSet.addReplacementObject("[DATE_CREATED]","2018-02-14 11:39:05.217");
        replacementDataSet.addReplacementObject("[DATE_DELETED]",null);
        replacementDataSet.addReplacementObject("[DATE_UPDATED]","2018-02-14 11:39:05.217");
        addBaseDomainObject(replacementDataSet, whiteListUrl);
        IOUtils.closeQuietly(inputStream);
    }
    
    /**
     * @param customer
     *            the user
     * @throws Exception
     *             the exception
     */
    private final <T extends User> void addUser(final ReplacementDataSet replacementDataSet, final T baseClass) throws Exception {
        replacementDataSet.addReplacementObject("[FIRST_NAME]", baseClass.getFirstName());
        replacementDataSet.addReplacementObject("[FAMILY_NAME]", baseClass.getFamilyName());
        replacementDataSet.addReplacementObject("[USERNAME]", baseClass.getUsername());
        replacementDataSet.addReplacementObject("[PASSWORD]", baseClass.getPassword());
        replacementDataSet.addReplacementObject("[RESET_PASSWORD]", Boolean.valueOf(baseClass.isResetPassword()));
        replacementDataSet.addReplacementObject("[EMAIL_VERIFICATION_STATE]", baseClass.getEmailVerificationState().toString());
        replacementDataSet.addReplacementObject("[FAILED_ATTEMPTS]", Integer.valueOf(baseClass.getFailedAttempts()));
        replacementDataSet.addReplacementObject("[LOCKED]", Boolean.valueOf(baseClass.isLocked()));
        replacementDataSet.addReplacementObject("[USER_TYPE]", baseClass.getUserType().name().toString());
        replacementDataSet.addReplacementObject("[EMAIL_ADDRESS]", baseClass.getEmailAddress());
        replacementDataSet.addReplacementObject("[ENABLED]", baseClass.isEnabled());
        if (baseClass.getLastLoginDateTime() != null) {
            replacementDataSet.addReplacementObject("[LAST_LOGIN]", baseClass.getLastLoginDateTime().toString(DATE_TIME_FORMAT));
        } else {
            replacementDataSet.addReplacementObject("[LAST_LOGIN]", null);
        }
        addCustomerUpdatedAudit(replacementDataSet, baseClass);
    }

    /**
     * Adds a UrlSkin
     * @param UrlSkin the UrlSkin to add
     * @throws Exception if there's a problem
     */
    public void addUrlSkin(UrlSkin urlSkin) throws Exception {
        InputStream inputStream = getInputStream(UrlSkin.class);
        ReplacementDataSet replacementDataSet = new ReplacementDataSet(flatXmlDataSetBuilder.build(inputStream));
        addUrlSkin(urlSkin, replacementDataSet);
        IOUtils.closeQuietly(inputStream);
    }

    /**
     * Adds a UrlSkin 
     * @param UrlSkin the UrlSkin to add
     * @param replacementDataSet the replacement dataset.
     */
    private void addUrlSkin(UrlSkin urlSkin, ReplacementDataSet replacementDataSet) {
        replacementDataSet.addReplacementObject("[URL]", urlSkin.getUrl());        
        replacementDataSet.addReplacementObject("[SKIN_PATH]", urlSkin.getSkinPath());
        replacementDataSet.addReplacementObject("[PRIMARY_SITE]", urlSkin.isPrimarySite());
        
        addBaseDomainObject(replacementDataSet, urlSkin);
    }

    /**
     * Adds a QuartzLogEntry
     * @param logEntry the QuartzLogEntry to add
     * @throws Exception if there's a problem
     */
    public void addQuartzLogEntry(QuartzLogEntry logEntry) throws Exception {
        InputStream inputStream = getInputStream(QuartzLogEntry.class);
        ReplacementDataSet replacementDataSet = new ReplacementDataSet(flatXmlDataSetBuilder.build(inputStream));
        addQuartzLogEntry(logEntry, replacementDataSet);
        IOUtils.closeQuietly(inputStream);
    }

    /**
     * Adds a QuartzLogEntry 
     * @param  the UrlSkin to add
     * @param replacementDataSet the replacement dataset.
     */
    private void addQuartzLogEntry(QuartzLogEntry logEntry, ReplacementDataSet replacementDataSet) {
        
        replacementDataSet.addReplacementObject("[TRG_NAME]", logEntry.getTriggerName());
        replacementDataSet.addReplacementObject("[TRG_GROUP]", logEntry.getTriggerGroup());
        replacementDataSet.addReplacementObject("[JOB_NAME]", logEntry.getJobName());
        replacementDataSet.addReplacementObject("[JOB_GROUP]", logEntry.getJobGroup());
        
        replacementDataSet.addReplacementObject("[REFIRE_COUNT]", logEntry.getRefireCount());
        replacementDataSet.addReplacementObject("[TRG_INSTR_CODE]", logEntry.getTriggerInstructionCode());
        
        if (logEntry.getActualFireTime() != null) {
            replacementDataSet.addReplacementObject("[ACT_FIRE_TIME]", logEntry.getActualFireTime().toString(DATE_TIME_FORMAT));
        } else {
            replacementDataSet.addReplacementObject("[ACT_FIRE_TIME]", null);
        }
        
        if (logEntry.getSchedFireTime() != null) {
            replacementDataSet.addReplacementObject("[SCH_FIRE_TIME]", logEntry.getSchedFireTime().toString(DATE_TIME_FORMAT));
        } else {
            replacementDataSet.addReplacementObject("[SCH_FIRE_TIME]", null);
        }
        
        if (logEntry.getNextFireTime() != null) {
            replacementDataSet.addReplacementObject("[NXT_FIRE_TIME]", logEntry.getNextFireTime().toString(DATE_TIME_FORMAT));
        } else {
            replacementDataSet.addReplacementObject("[NXT_FIRE_TIME]", null);
        }
        
        replacementDataSet.addReplacementObject("[JOB_RUN_TIME]", logEntry.getJobRunTime());
        replacementDataSet.addReplacementObject("[HOST_NAME]", logEntry.getHostName());
        replacementDataSet.addReplacementObject("[HOST_ADDRESS]", logEntry.getHostAddress());
        replacementDataSet.addReplacementObject("[JOB_CLASS_NAME]", logEntry.getJobClassName());
        addBaseDomainObject(replacementDataSet, logEntry);
    }
    
    /**
     * Adds the db message.
     * This is deprecated. Prefer <code>addMessage(Message)</code> instead.
     *
     * @param dbMessage the db message
     * @throws Exception the exception
     */
    @Deprecated
    public void addDbMessage(DbMessage dbMessage) throws Exception {
        Class<DbMessage> claz = DbMessage.class;
        InputStream inputStream = claz.getResourceAsStream(claz.getSimpleName() + SEED_FILE_POSTFIX);
        ReplacementDataSet replacementDataSet = new ReplacementDataSet(flatXmlDataSetBuilder.build(inputStream));
        addDbMessage(dbMessage, replacementDataSet);
        IOUtils.closeQuietly(inputStream);
    }

    /**
     * Adds the db message.
     * This is deprecated. Prefer <code>addMessage(Message, ReplacementDataSet)</code> instead.
     *
     * @param dbMessage the db message
     * @param replacementDataSet the replacement data set
     */
    @Deprecated
    private void addDbMessage(DbMessage dbMessage, ReplacementDataSet replacementDataSet) {
        replacementDataSet.addReplacementObject("[BASENAME]", dbMessage.getBasename());
        replacementDataSet.addReplacementObject("[COUNTRY]", dbMessage.getCountry());
        replacementDataSet.addReplacementObject("[LANGUAGE]", dbMessage.getLanguage());
        replacementDataSet.addReplacementObject("[VARIANT]", dbMessage.getVariant());
        replacementDataSet.addReplacementObject("[KEY]", dbMessage.getKey());
        replacementDataSet.addReplacementObject("[MESSAGE]", dbMessage.getMessage());
        addDataSet(replacementDataSet);
    }
    
    /**
     * Adds the message.
     *
     * @param message the message
     * @throws Exception the exception
     */
    public void addMessage(Message message) throws Exception {
    	Class<Message> claz = Message.class;
    	InputStream inputStream = claz.getResourceAsStream(claz.getSimpleName() + SEED_FILE_POSTFIX);
    	ReplacementDataSet replacementDataSet = new ReplacementDataSet(flatXmlDataSetBuilder.build(inputStream));
    	addMessage(message, replacementDataSet);
    	IOUtils.closeQuietly(inputStream);
    }
    
    /**
     * Adds the message.
     *
     * @param message the message
     * @param replacementDataSet the replacement data set
     */
    private void addMessage(Message message, ReplacementDataSet replacementDataSet) {
    	replacementDataSet.addReplacementObject("[BASENAME]", message.getBasename());
    	replacementDataSet.addReplacementObject("[COUNTRY]", message.getCountry());
    	replacementDataSet.addReplacementObject("[LANGUAGE]", message.getLanguage());
    	replacementDataSet.addReplacementObject("[VARIANT]", message.getVariant());
    	replacementDataSet.addReplacementObject("[KEY]", message.getKey());
    	replacementDataSet.addReplacementObject("[MESSAGE]", message.getMessage());
    	
    	addBaseDomainObject(replacementDataSet, message);
    }

    /**
     * Adds the external id.
     *
     * @param externalId the external id
     * @throws Exception the exception
     */
    public void addExternalProductId(ExternalProductId externalId) throws Exception {
        Class<ExternalProductId> claz = ExternalProductId.class;
        InputStream inputStream = claz.getResourceAsStream(claz.getSimpleName() + SEED_FILE_POSTFIX);
        ReplacementDataSet replacementDataSet = new ReplacementDataSet(flatXmlDataSetBuilder.build(inputStream));
        addExternalProductId(externalId, replacementDataSet);
        IOUtils.closeQuietly(inputStream);
    }

    /**
     * Adds the external id.
     *
     * @param externalId the external id
     * @param replacementDataSet the replacement data set
     */
    private void addExternalProductId(ExternalProductId externalId, ReplacementDataSet replacementDataSet) {
        replacementDataSet.addReplacementObject("[CUSTOMER_ID]",  null);
        replacementDataSet.addReplacementObject("[PRODUCT_ID]", externalId.getProduct().getId());
        replacementDataSet.addReplacementObject("[EXTERNAL_ID_TYPE]", externalId.getDomainObjectType().name());
        replacementDataSet.addReplacementObject("[EXTERNAL_SYSTEM_ID_TYPE_ID]", externalId.getExternalSystemIdType().getId());
        replacementDataSet.addReplacementObject("[EXTERNAL_ID]", externalId.getExternalId());
        addBaseDomainObject(replacementDataSet, externalId);
    }

    /**
     * Adds the external id.
     *
     * @param externalId the external id
     * @throws Exception the exception
     */
    public void addExternalCustomerId(ExternalCustomerId externalId) throws Exception {
        Class<ExternalCustomerId> claz = ExternalCustomerId.class;
        InputStream inputStream = claz.getResourceAsStream(claz.getSimpleName() + SEED_FILE_POSTFIX);
        ReplacementDataSet replacementDataSet = new ReplacementDataSet(flatXmlDataSetBuilder.build(inputStream));
        addExternalCustomerId(externalId, replacementDataSet);
        IOUtils.closeQuietly(inputStream);
    }

    private void addExternalCustomerId(ExternalCustomerId externalId, ReplacementDataSet replacementDataSet) {
        replacementDataSet.addReplacementObject("[CUSTOMER_ID]", externalId.getCustomer().getId());
        replacementDataSet.addReplacementObject("[PRODUCT_ID]",  null);
        replacementDataSet.addReplacementObject("[EXTERNAL_ID_TYPE]", externalId.getDomainObjectType().name());
        replacementDataSet.addReplacementObject("[EXTERNAL_SYSTEM_ID_TYPE_ID]", externalId.getExternalSystemIdType().getId());
        replacementDataSet.addReplacementObject("[EXTERNAL_ID]", externalId.getExternalId());
        addBaseDomainObject(replacementDataSet, externalId);
    }

    public void addExternalSystem(ExternalSystem externalSystem) throws Exception {
        Class<ExternalSystem> claz = ExternalSystem.class;
        InputStream inputStream = claz.getResourceAsStream(claz.getSimpleName() + SEED_FILE_POSTFIX);
        ReplacementDataSet replacementDataSet = new ReplacementDataSet(flatXmlDataSetBuilder.build(inputStream));
        addExternalSystem(externalSystem, replacementDataSet);
        IOUtils.closeQuietly(inputStream);
    }

    private void addExternalSystem(ExternalSystem externalSystem, ReplacementDataSet replacementDataSet) {
        replacementDataSet.addReplacementObject("[NAME]", externalSystem.getName());
        replacementDataSet.addReplacementObject("[DESCRIPTION]", externalSystem.getDescription());
        addBaseDomainObject(replacementDataSet, externalSystem);
    }

    public void addExternalSystemIdType(ExternalSystemIdType externalSystemIdType) throws Exception {
        Class<ExternalSystemIdType> claz = ExternalSystemIdType.class;
        InputStream inputStream = claz.getResourceAsStream(claz.getSimpleName() + SEED_FILE_POSTFIX);
        ReplacementDataSet replacementDataSet = new ReplacementDataSet(flatXmlDataSetBuilder.build(inputStream));
        addExternalSystemIdType(externalSystemIdType, replacementDataSet);
        IOUtils.closeQuietly(inputStream);
    }

    private void addExternalSystemIdType(ExternalSystemIdType externalSystemIdType, ReplacementDataSet replacementDataSet) {
        replacementDataSet.addReplacementObject("[EXTERNAL_SYSTEM_ID]", externalSystemIdType.getExternalSystem().getId());
        replacementDataSet.addReplacementObject("[NAME]", externalSystemIdType.getName());
        replacementDataSet.addReplacementObject("[DESCRIPTION]", externalSystemIdType.getDescription());
        addBaseDomainObject(replacementDataSet, externalSystemIdType);
    }

    public void addLinkedRegistration(LinkedRegistration linkedRegistration) throws Exception {
        Class<LinkedRegistration> claz = LinkedRegistration.class;
        InputStream inputStream = claz.getResourceAsStream(claz.getSimpleName() + SEED_FILE_POSTFIX);
        ReplacementDataSet replacementDataSet = new ReplacementDataSet(flatXmlDataSetBuilder.build(inputStream));
        addLinkedRegistration(linkedRegistration, replacementDataSet);
        IOUtils.closeQuietly(inputStream);
    }

    private void addLinkedRegistration(LinkedRegistration linkedRegistration, ReplacementDataSet replacementDataSet) {
        replacementDataSet.addReplacementObject("[REGISTRATION_ID]", linkedRegistration.getRegistration().getId());
        replacementDataSet.addReplacementObject("[LINKED_PRODUCT_ID]", linkedRegistration.getLinkedProduct().getId());
        replacementDataSet.addReplacementObject("[ERIGHTS_LICENCE_ID]", linkedRegistration.getErightsLicenceId());
        addBaseDomainObject(replacementDataSet, linkedRegistration);
    }

    /**
     * @param <T>
     *            the type
     * @param replacementDataSet
     *            the replacement data set
     * @param baseClass
     *            the base class
     */
    private <T extends UpdatedAudit> void addUpdatedAudit(final ReplacementDataSet replacementDataSet, final T baseClass) {
        replacementDataSet.addReplacementObject("[UPDATED_DATE]", baseClass.getUpdatedDate() == null ? null : baseClass.getUpdatedDate().toString(DATE_TIME_FORMAT));
        addCreatedAudit(replacementDataSet, baseClass);
    }       
    
    private <T extends OUPUpdatedAudit> void addCustomerUpdatedAudit(final ReplacementDataSet replacementDataSet, final T baseClass) {
        replacementDataSet.addReplacementObject("[UPDATED_DATE]", baseClass.getUpdatedDate() == null ? null : baseClass.getUpdatedDate().toString(DATE_TIME_FORMAT));
        addCustomerCreatedAudit(replacementDataSet, baseClass);
    }
    /**
     * @param <T>
     *            the type
     * @param replacementDataSet
     *            the replacement data set
     * @param baseClass
     *            the base class
     */
    private <T extends CreatedAudit> void addCreatedAudit(final ReplacementDataSet replacementDataSet, final T baseClass) {
        replacementDataSet.addReplacementObject("[CREATED_DATE]", baseClass.getCreatedDate().toString(DATE_TIME_FORMAT));
        addBaseDomainObject(replacementDataSet, baseClass);
    }    
    
    private <T extends OUPCreatedAudit> void addCustomerCreatedAudit(final ReplacementDataSet replacementDataSet, final T baseClass) {
        replacementDataSet.addReplacementObject("[CREATED_DATE]", baseClass.getCreatedDate().toString(DATE_TIME_FORMAT));
        addCustomerBaseDomainObject(replacementDataSet, baseClass);
    } 
    
    /**
     * @param <T>
     *            the type
     * @param replacementDataSet
     *            the replacement data set
     * @param baseClass
     *            the base class
     */
    private <T extends BaseDomainObject> void addBaseDomainObject(final ReplacementDataSet replacementDataSet, final T baseClass) {
        replacementDataSet.addReplacementObject("[ID]", baseClass.getId());
        replacementDataSet.addReplacementObject("[OBJ_VERSION]", Long.valueOf(baseClass.getVersion()));
        addDataSet(replacementDataSet);
    }
    private <T extends OUPBaseDomainObject> void addCustomerBaseDomainObject(final ReplacementDataSet replacementDataSet, final T baseClass) {
        replacementDataSet.addReplacementObject("[ID]", baseClass.getId());
        replacementDataSet.addReplacementObject("[OBJ_VERSION]", Long.valueOf(baseClass.getVersion()));
        addDataSet(replacementDataSet);
    }

    /**
     * @param dataSet
     *            the data set
     */
    private void addDataSet(final IDataSet dataSet) {
        dataSets.add(dataSet);
    }

    /**
     * @return the list of data sets as an array
     */
    public final IDataSet[] getAllDataSets() {
        return dataSets.toArray(new IDataSet[dataSets.size()]);
    }

    /**
     * Get a stream.
     * 
     * @param aClass
     *            a class
     * @return A stream from the class
     */
    private InputStream getInputStream(final Class<? extends BaseDomainObject> aClass) {
        return aClass.getResourceAsStream(aClass.getSimpleName() + SEED_FILE_POSTFIX);
    }
    
    
    public void addCustomerMigrationData(CustomerMigrationData customerMigrationData) throws Exception {
        Class<CustomerMigrationData> claz = CustomerMigrationData.class;
        String file = claz.getSimpleName() + SEED_FILE_POSTFIX;
        InputStream inputStream = claz.getResourceAsStream(file);
        ReplacementDataSet replacementDataSet = new ReplacementDataSet(flatXmlDataSetBuilder.build(inputStream));
        addCustomerMigrationData(customerMigrationData, replacementDataSet);
        IOUtils.closeQuietly(inputStream);
    }
    
private void addCustomerMigrationData(CustomerMigrationData customerMigrationData, final ReplacementDataSet replacementDataSet) {
        
        replacementDataSet.addReplacementObject("[ID]", customerMigrationData.getId());
        replacementDataSet.addReplacementObject("[USERNAME]", customerMigrationData.getUsername());
        replacementDataSet.addReplacementObject("[PASSWORD]", customerMigrationData.getPassword());
        replacementDataSet.addReplacementObject("[FIRST_NAME]", customerMigrationData.getFirstName());
        replacementDataSet.addReplacementObject("[LAST_NAME]", customerMigrationData.getLastName());
        replacementDataSet.addReplacementObject("[EMAIL_ADDRESS]", customerMigrationData.getEmailAddress());
        replacementDataSet.addReplacementObject("[RESET_PASSWORD]", customerMigrationData.isResetPassword());
        replacementDataSet.addReplacementObject("[FAILED_ATTEMPTS]", customerMigrationData.getFailedAttempts());
        replacementDataSet.addReplacementObject("[LOCKED]", customerMigrationData.isLocked());
        replacementDataSet.addReplacementObject("[CUSTOMER_TYPE]", customerMigrationData.getCustomerType());
        replacementDataSet.addReplacementObject("[ENABLED]", customerMigrationData.isEnabled());
        replacementDataSet.addReplacementObject("[LOCALE]", customerMigrationData.getLocale());
        replacementDataSet.addReplacementObject("[TIME_ZONE]", customerMigrationData.getTimeZone());
        replacementDataSet.addReplacementObject("[LAST_LOGIN]", customerMigrationData.getLastLogin() == null ? null : customerMigrationData.getLastLogin().toString(DATE_TIME_FORMAT));
        replacementDataSet.addReplacementObject("[EMAIL_VERIFICATION_STATE]", customerMigrationData.getEmailVerificationState());
        replacementDataSet.addReplacementObject("[EXTERNAL_ID]", customerMigrationData.getExternalId());
        replacementDataSet.addReplacementObject("[CREATED_DATE]", customerMigrationData.getCreatedDate() == null ? new DateTime() : customerMigrationData.getCreatedDate().toString(DATE_TIME_FORMAT));
        replacementDataSet.addReplacementObject("[UPDATED_DATE]", customerMigrationData.getUpdatedDate() == null ? null : customerMigrationData.getUpdatedDate().toString(DATE_TIME_FORMAT));
        
        addDataSet(replacementDataSet);        
    }

    public void addCustomerMigration(CustomerMigration customerMigration) throws Exception {
        Class<CustomerMigration> claz = CustomerMigration.class;
        String file = claz.getSimpleName() + SEED_FILE_POSTFIX;
        InputStream inputStream = claz.getResourceAsStream(file);
        ReplacementDataSet replacementDataSet = new ReplacementDataSet(flatXmlDataSetBuilder.build(inputStream));
        addCustomerMigration(customerMigration, replacementDataSet);
        IOUtils.closeQuietly(inputStream);
    }
    
    private void addCustomerMigration(CustomerMigration customerMigration, final ReplacementDataSet replacementDataSet){
        replacementDataSet.addReplacementObject("[ID]", customerMigration.getData().getId());
        replacementDataSet.addReplacementObject("[MIGRATION_STATE]", customerMigration.getState().name());
        Customer cust = customerMigration.getCustomer();
        if(cust == null){
            replacementDataSet.addReplacementObject("[EAC_CUSTOMER_ID]", null);
        }else{
            replacementDataSet.addReplacementObject("[EAC_CUSTOMER_ID]", customerMigration.getCustomer().getId());
        }
        addUpdatedAudit(replacementDataSet, customerMigration);
    }
    
 
    /**
     * @param standardLicenceTemplate
     *            the standardLicenceTemplate
     * @throws Exception
     *             the exception the exception
     */
    public final void addDivisionAdminUser(final DivisionAdminUser divisionAdminUser) throws Exception {
        InputStream inputStream = getInputStream(DivisionAdminUser.class);
        ReplacementDataSet replacementDataSet = new ReplacementDataSet(flatXmlDataSetBuilder.build(inputStream));
        replacementDataSet.addReplacementObject("[DIVISION_ID]", divisionAdminUser.getDivisionErightsId());
        replacementDataSet.addReplacementObject("[ADMIN_USER_ID]", divisionAdminUser.getAdminUser().getId());
        addBaseDomainObject(replacementDataSet, divisionAdminUser);
        IOUtils.closeQuietly(inputStream);
    }   
}
