package com.oup.eac.data.util;

import java.net.URL;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dbunit.dataset.Column;
import org.dbunit.dataset.CompositeDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.ITableMetaData;
import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.joda.time.Seconds;

import com.oup.eac.common.utils.activationcode.ActivationCodeGenerator;
import com.oup.eac.domain.AccountPageDefinition;
import com.oup.eac.domain.AccountRegistrationDefinition;
import com.oup.eac.domain.AccountValidated;
import com.oup.eac.domain.ActivationCode;
import com.oup.eac.domain.ActivationCodeBatch;
import com.oup.eac.domain.ActivationCodeBatch.ActivationCodeFormat;
import com.oup.eac.domain.ActivationCodeRegistration;
import com.oup.eac.domain.ActivationCodeRegistrationDefinition;
import com.oup.eac.domain.ActivationState;
import com.oup.eac.domain.AdminUser;
import com.oup.eac.domain.Answer;
import com.oup.eac.domain.Checkbox;
import com.oup.eac.domain.Component;
import com.oup.eac.domain.ConcurrentLicenceTemplate;
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
import com.oup.eac.domain.OptionsTag;
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
import com.oup.eac.domain.ProgressBarElement.ElementType;
import com.oup.eac.domain.QuartzLogEntry;
import com.oup.eac.domain.Question;
import com.oup.eac.domain.Radio;
import com.oup.eac.domain.RegisterableProduct;
import com.oup.eac.domain.RegisterableProduct.RegisterableType;
import com.oup.eac.domain.Registration;
import com.oup.eac.domain.RegistrationActivation;
import com.oup.eac.domain.RegistrationActivation.ActivationStrategy;
import com.oup.eac.domain.RegistrationState;
import com.oup.eac.domain.RegistrationType;
import com.oup.eac.domain.Role;
import com.oup.eac.domain.RollingLicenceTemplate;
import com.oup.eac.domain.Select;
import com.oup.eac.domain.SelfRegistrationActivation;
import com.oup.eac.domain.StandardLicenceTemplate;
import com.oup.eac.domain.TagOption;
import com.oup.eac.domain.TextField;
import com.oup.eac.domain.TokenState;
import com.oup.eac.domain.UrlLink;
import com.oup.eac.domain.UrlSkin;
import com.oup.eac.domain.UsageLicenceTemplate;
import com.oup.eac.domain.User;
import com.oup.eac.domain.UserState;
import com.oup.eac.domain.ValidatedRegistrationActivation;
import com.oup.eac.domain.WhiteListUrl;
import com.oup.eac.domain.migrationtool.CustomerMigration;
import com.oup.eac.domain.migrationtool.CustomerMigrationData;

/**
 * @author harlandd Creates sample data for tests.
 */
public class SampleDataCreator {
	
	private static final Logger LOGGER = Logger.getLogger(SampleDataCreator.class);

    private DataLoader dataLoader = new DataLoader();

    /**
     * Default constructor.
     */
    public SampleDataCreator() {
        super();
    }
    
  
    public final ProgressBarElement createProgressBarElement(final ProgressBar progressBar, final ElementType elementType, final int sequence) throws Exception {
    	ProgressBarElement progressBarElement = SampleDataFactory.createProgressBarElement(progressBar, elementType, sequence);
    	loadProgressBarElement(progressBarElement);
    	return progressBarElement;
    }
    
    public final void loadProgressBarElement(final ProgressBarElement progressBarElement) throws Exception {
    	dataLoader.addProgressBarElement(progressBarElement);
    }    
    
    public final ProgressBar createProgressBar(final ActivationState activationState, final String page, final UserState userState, final RegistrationState registrationState, final ActivationStrategy activationStrategy, 
			final TokenState tokenState, final RegistrationType registrationType, final AccountValidated accountValidated) throws Exception {
    	ProgressBar progressBar = SampleDataFactory.createProgressBar(activationState, page, userState, registrationState, activationStrategy, tokenState, registrationType, accountValidated);
    	loadProgressBar(progressBar);
    	return progressBar;
    }
    
    public final void loadProgressBar(final ProgressBar progressBar) throws Exception {
    	dataLoader.addProgressBar(progressBar);
    }
    
    public final User createUserRoles(final User user) throws Exception {
    	loadUserRoles(user);
    	return user;
    }
    
    public final void loadUserRoles(final User user) throws Exception {
    	dataLoader.addUserRoles(user);
    }
    
    public final Role createRolePermissions(final Role role) throws Exception {
    	loadRolePermissions(role);
    	return role;
    }
    
    public final void loadRolePermissions(final Role role) throws Exception {
    	dataLoader.addRolePermissions(role);
    }
    
    public final Role createRole(final String roleName) throws Exception {
    	Role role = SampleDataFactory.createRole(roleName);
    	loadRole(role);
    	return role;
    }
    
    public final void loadRole(final Role role) throws Exception {
    	dataLoader.addRole(role);
    }
    
    public final Permission createPermission(final String permissionName) throws Exception {
    	Permission permission = SampleDataFactory.createPermission(permissionName);
    	loadPermission(permission);
    	return permission;
    }
    
    public final void loadPermission(final Permission permission) throws Exception {
    	dataLoader.addPermission(permission);
    }
    
    public final ValidatedRegistrationActivation createValidatedRegistrationActivation() throws Exception {
        ValidatedRegistrationActivation validatedRegistrationActivation = SampleDataFactory.createValidatedRegistrationActivation();
        loadValidatedRegistrationActivation(validatedRegistrationActivation);
        return validatedRegistrationActivation;
    }
    
    public final void loadValidatedRegistrationActivation(final ValidatedRegistrationActivation validatedRegistrationActivation) throws Exception {
        dataLoader.addValidatedRegistrationActivation(validatedRegistrationActivation);
    }     
    
    public final SelfRegistrationActivation createSelfRegistrationActivation() throws Exception {
    	SelfRegistrationActivation selfRegistrationActivation = SampleDataFactory.createSelfRegistrationActivation();
    	loadSelfRegistrationActivation(selfRegistrationActivation);
    	return selfRegistrationActivation;
    }

	public final void loadSelfRegistrationActivation(final SelfRegistrationActivation selfRegistrationActivation) throws Exception {
		dataLoader.addSelfRegistrationActivation(selfRegistrationActivation);
	}
	
    public final InstantRegistrationActivation createInstantRegistrationActivation() throws Exception {
    	InstantRegistrationActivation selfRegistrationActivation = SampleDataFactory.createInstantRegistrationActivation();
    	loadInstantRegistrationActivation(selfRegistrationActivation);
    	return selfRegistrationActivation;
    }

	public final void loadInstantRegistrationActivation(final InstantRegistrationActivation selfRegistrationActivation) throws Exception {
		dataLoader.addInstantRegistrationActivation(selfRegistrationActivation);
	}
   
    public final ActivationCode createActivationCode(final ActivationCodeBatch activationCodeBatch, final ActivationCodeGenerator acg, final String code) throws Exception {
        ActivationCode activationCode = SampleDataFactory.createActivationCode(activationCodeBatch, acg, code);
        loadActivationCode(activationCode);
        return activationCode;
    }	
	
    public final ActivationCode createActivationCode(final ActivationCodeBatch activationCodeBatch, final ActivationCodeGenerator acg) throws Exception {
        ActivationCode activationCode = SampleDataFactory.createActivationCode(activationCodeBatch, acg);
        loadActivationCode(activationCode);
        return activationCode;
    }
    
    public final void loadActivationCode(final ActivationCode activationCode) throws Exception {
        dataLoader.addActivationCode(activationCode);
    } 
    
    public final ActivationCodeBatch createActivationCodeBatch(final LicenceTemplate licenceTemplate, 
    															final ActivationCodeFormat activationCodeFormat,
    															final ActivationCodeRegistrationDefinition activationCodeRegistrationDefinition, final DateTime startDate, final DateTime endDate)
    															throws Exception {
        ActivationCodeBatch activationCodeBatch = SampleDataFactory.createActivationCodeBatch(activationCodeFormat, licenceTemplate, 
        																					activationCodeRegistrationDefinition, startDate, endDate, null);
        loadActivationCodeBatch(activationCodeBatch);
        return activationCodeBatch;
    }
    
    public final void loadActivationCodeBatch(final ActivationCodeBatch activationCodeBatch) throws Exception {
        dataLoader.addActivationCodeBatch(activationCodeBatch);
    }     
    
    public final AccountRegistrationDefinition createAccountRegistrationDefinition(final RegisterableProduct product, final RegistrationActivation registrationActivation, final AccountPageDefinition accountPageDefinition) throws Exception {
    	AccountRegistrationDefinition accountRegistrationDefinition = SampleDataFactory.createAccountRegistrationDefinition(product, registrationActivation, accountPageDefinition);
    	loadAccountRegistrationDefinition(accountRegistrationDefinition);
    	return accountRegistrationDefinition;
    }
    
    public final void loadAccountRegistrationDefinition(final AccountRegistrationDefinition registrationDefinition) throws Exception {
        dataLoader.addAccountRegistrationDefinition(registrationDefinition);
    } 

    public final ProductRegistrationDefinition createProductRegistrationDefinition(final RegisterableProduct product, final LicenceTemplate licenceTemplate, final RegistrationActivation registrationActivation, final ProductPageDefinition productPageDefinition) throws Exception {
    	ProductRegistrationDefinition productRegistrationDefinition = SampleDataFactory.createProductRegistrationDefinition(product, licenceTemplate, registrationActivation, productPageDefinition);
    	loadProductRegistrationDefinition(productRegistrationDefinition);
    	return productRegistrationDefinition;
    }
    
    public final void loadProductRegistrationDefinition(final ProductRegistrationDefinition registrationDefinition) throws Exception {
        dataLoader.addProductRegistrationDefinition(registrationDefinition);
    } 
    
   
    /**
     * @return the StandardLicenceTemplate
     * @throws Exception
     *             checked exception thrown by method
     */
    public final Division createDivision(String divisionType) throws Exception {
        Division division = SampleDataFactory.createDivision(divisionType);
        loadDivision(division);
        return division;
    }

    /**
     * @param standardLicenceTemplate
     *            the standardLicenceTemplate
     * @throws Exception
     *             checked exception thrown by method
     */
    public final void loadDivision(final Division division) throws Exception {
        dataLoader.addDivision(division);
    }    
    
    /**
     * @return the StandardLicenceTemplate
     * @throws Exception
     *             checked exception thrown by method
     */
    public final StandardLicenceTemplate createStandardLicenceTemplate() throws Exception {
        StandardLicenceTemplate standardLicenceTemplate = SampleDataFactory.createStandardLicenceTemplate();
        loadStandardLicenceTemplate(standardLicenceTemplate);
        return standardLicenceTemplate;
    }

    /**
     * @param standardLicenceTemplate
     *            the standardLicenceTemplate
     * @throws Exception
     *             checked exception thrown by method
     */
    public final void loadStandardLicenceTemplate(final StandardLicenceTemplate standardLicenceTemplate) throws Exception {
        dataLoader.addStandardLicenceTemplate(standardLicenceTemplate);
    }
    
    /**
     * @return the StandardLicenceTemplate
     * @throws Exception
     *             checked exception thrown by method
     */
    public final RollingLicenceTemplate createRollingLicenceTemplate() throws Exception {
        RollingLicenceTemplate rollingLicenceTemplate = SampleDataFactory.createRollingLicenceTemplate();
        loadRollingLicenceTemplate(rollingLicenceTemplate);
        return rollingLicenceTemplate;
    }

    /**
     * @param standardLicenceTemplate
     *            the standardLicenceTemplate
     * @throws Exception
     *             checked exception thrown by method
     */
    public final void loadRollingLicenceTemplate(final RollingLicenceTemplate rollingLicenceTemplate) throws Exception {
        dataLoader.addRollingLicenceTemplate(rollingLicenceTemplate);
    }
    
    /**
     * @return the StandardLicenceTemplate
     * @throws Exception
     *             checked exception thrown by method
     */
    public final UsageLicenceTemplate createUsageLicenceTemplate() throws Exception {
        UsageLicenceTemplate usageLicenceTemplate = SampleDataFactory.createUsageLicenceTemplate();
        loadUsageLicenceTemplate(usageLicenceTemplate);
        return usageLicenceTemplate;
    }

    /**
     * @param standardLicenceTemplate
     *            the standardLicenceTemplate
     * @throws Exception
     *             checked exception thrown by method
     */
    public final void loadUsageLicenceTemplate(final UsageLicenceTemplate usageLicenceTemplate) throws Exception {
        dataLoader.addUsageLicenceTemplate(usageLicenceTemplate);
    }
    
    /**
     * @return the StandardLicenceTemplate
     * @throws Exception
     *             checked exception thrown by method
     */
    public final ConcurrentLicenceTemplate createConcurrentLicenceTemplate() throws Exception {
        ConcurrentLicenceTemplate concurrentLicenceTemplate = SampleDataFactory.createConcurrentLicenceTemplate();
        loadConcurrentLicenceTemplate(concurrentLicenceTemplate);
        return concurrentLicenceTemplate;
    }

    /**
     * @param standardLicenceTemplate
     *            the standardLicenceTemplate
     * @throws Exception
     *             checked exception thrown by method
     */
    public final void loadConcurrentLicenceTemplate(final ConcurrentLicenceTemplate concurrentLicenceTemplate) throws Exception {
        dataLoader.addConcurrentLicenceTemplate(concurrentLicenceTemplate);
    }

    /**
     * @param optionsTag
     *            the options tag
     * @return the created and loaded TagOption
     * @throws Exception
     *             checked exception thrown by method throws checked exceptions
     */
    public final TagOption createTagOption(final OptionsTag optionsTag) throws Exception {
        TagOption tagOption = SampleDataFactory.createTagOption(optionsTag);
        loadTagOption(tagOption);
        return tagOption;
    }

    /**
     * @param tagOption
     *            the tagOption
     * @throws Exception
     *             checked exception thrown by method
     */
    public final void loadTagOption(final TagOption tagOption) throws Exception {
        dataLoader.addTagOption(tagOption);
    }

    /**
     * @param element
     *            the element
     * @return the created and loaded radio tag
     * @throws Exception
     *             checked exception thrown by method
     */
    public final Radio createRadio(final Element element) throws Exception {
        Radio radio = SampleDataFactory.createRadio(element);
        loadRadio(radio);
        return radio;
    }

    /**
     * @param radio
     *            the radio tag
     * @throws Exception
     *             checked exception thrown by method
     */
    public final void loadRadio(final Radio radio) throws Exception {
        dataLoader.addRadio(radio);
    }

    /**
     * @param element
     *            the element
     * @return the created and loaded select
     * @throws Exception
     *             checked exception thrown by method
     */
    public final Select createSelect(final Element element) throws Exception {
        Select select = SampleDataFactory.createSelect(element);
        loadSelect(select);
        return select;
    }

    /**
     * @param select
     *            the select
     * @throws Exception
     *             checked exception thrown by method
     */
    public final void loadSelect(final Select select) throws Exception {
        dataLoader.addSelect(select);
    }
    
    /**
     * @param element
     *            the element
     * @return the created and loaded select
     * @throws Exception
     *             checked exception thrown by method
     */
    public final MultiSelect createMultiSelect(final Element element) throws Exception {
    	MultiSelect select = SampleDataFactory.createMultiSelect(element);
        loadMultiSelect(select);
        return select;
    }

    /**
     * @param select
     *            the select
     * @throws Exception
     *             checked exception thrown by method
     */
    public final void loadMultiSelect(final MultiSelect select) throws Exception {
        dataLoader.addMultiSelect(select);
    }

    /**
     * @param element
     *            the element
     * @return the checkbox
     * @throws Exception
     *             checked exception thrown by method
     */
    public final Checkbox createCheckbox(final Element element) throws Exception {
        Checkbox checkbox = SampleDataFactory.createCheckbox(element);
        loadCheckbox(checkbox);
        return checkbox;
    }

    /**
     * @param checkbox
     *            the checkbox
     * @throws Exception
     *             checked exception thrown by method
     */
    public final void loadCheckbox(final Checkbox checkbox) throws Exception {
        dataLoader.addCheckbox(checkbox);
    }

    /**
     * @param element
     *            the element
     * @return the password field
     * @throws Exception
     *             checked exception thrown by method
     */
    public final PasswordField createPasswordField(final Element element) throws Exception {
        PasswordField passwordField = SampleDataFactory.createPasswordField(element);
        loadPasswordField(passwordField);
        return passwordField;
    }

    /**
     * @param passwordField
     *            the password field
     * @throws Exception
     *             checked exception thrown by method
     */
    public final void loadPasswordField(final PasswordField passwordField) throws Exception {
        dataLoader.addPasswordField(passwordField);
    }

    /**
     * @param element
     *            the element
     * @return the text field
     * @throws Exception
     *             checked exception thrown by method
     */
    public final TextField createTextField(final Element element) throws Exception {
        TextField textField = SampleDataFactory.createTextField(element);
        loadTextField(textField);
        return textField;
    }

    /**
     * @param textField
     *            the text field
     * @throws Exception
     *             checked exception thrown by method
     */
    public final void loadTextField(final TextField textField) throws Exception {
        dataLoader.addTextField(textField);
    }

    /**
     * @param element
     *            the element
     * @param customer
     *            the customer
     * @return the Answer
     * @throws Exception
     *             checked exception thrown by method
     */
    public final Answer createAnswer(final Question question, final Customer customer) throws Exception {
        Answer answer = SampleDataFactory.createAnswer(customer, question);
        loadAnswer(answer);
        return answer;
    }

    /**
     * @param answer
     *            the answer
     * @throws Exception
     *             checked exception thrown by method
     */
    public final void loadAnswer(final Answer answer) throws Exception {
        dataLoader.addAnswer(answer);
    }
    
    /**
     * @param element
     *            the element
     * @param customer
     *            the customer
     * @return the Answer
     * @throws Exception
     *             checked exception thrown by method
     */
    public final ProductSpecificAnswer createProductSpecificAnswer(final Question question, final Customer customer, final RegisterableProduct registerableProduct) throws Exception {
        ProductSpecificAnswer answer = SampleDataFactory.createProductSpecificAnswer(customer, question, registerableProduct);
        loadProductSpecificAnswer(answer);
        return answer;
    }

    /**
     * @param answer
     *            the answer
     * @throws Exception
     *             checked exception thrown by method
     */
    public final void loadProductSpecificAnswer(final ProductSpecificAnswer answer) throws Exception {
        dataLoader.addProductSpecificAnswer(answer);
    }
    
    /**
     * @param element
     *            the element
     * @return the ElementCountryRestriction
     * @throws Exception
     *             checked exception thrown by method
     */
    public final ElementCountryRestriction createElementCountryRestriction(final Element element, final Locale locale) throws Exception {
        ElementCountryRestriction elementCountryRestriction = SampleDataFactory.createElementCountryRestriction(element, locale);
        loadElementCountryRestriction(elementCountryRestriction);
        return elementCountryRestriction;
    }

    /**
     * @param elementCountryRestriction
     *            the elementCountryRestriction
     * @throws Exception
     *             checked exception thrown by method
     */
    public final void loadElementCountryRestriction(final ElementCountryRestriction elementCountryRestriction) throws Exception {
        dataLoader.addElementCountryRestriction(elementCountryRestriction);
    }

    /**
     * @param element
     *            the element
     * @return the Label
     * @throws Exception
     *             checked exception thrown by method
     */
    public final Label createLabel(final Element element) throws Exception {
        Label label = SampleDataFactory.createLabel(element);
        loadLabel(label);
        return label;
    }

    /**
     * @param label
     *            the label
     * @throws Exception
     *             checked exception thrown by method
     */
    public final void loadLabel(final Label label) throws Exception {
        dataLoader.addLabel(label);
    }

    /**
     * @param element
     *            the element
     * @return the urlLink
     * @throws Exception
     *             checked exception thrown by method
     */
    public final UrlLink createUrlLink(final Element element) throws Exception {
        UrlLink urlLink = SampleDataFactory.createUrlLink(element);
        loadUrlLink(urlLink);
        return urlLink;
    }

    /**
     * @param urlLink
     *            the urlLink
     * @throws Exception
     *             checked exception thrown by method
     */
    public final void loadUrlLink(final UrlLink urlLink) throws Exception {
        dataLoader.addUrlLink(urlLink);
    }

    /**
     * @return the element
     * @throws Exception
     *             checked exception thrown by method
     */
    public final Element createElement(final Question question) throws Exception {
        Element element = SampleDataFactory.createElement(question);
        loadElement(element);
        return element;
    }

    /**
     * @param element
     *            the element
     * @throws Exception
     *             checked exception thrown by method
     */
    public final void loadElement(final Element element) throws Exception {
        dataLoader.addElement(element);
    }

    /**
     * @return the element
     * @throws Exception
     *             checked exception thrown by method
     */
    public final Question createQuestion() throws Exception {
        Question question = SampleDataFactory.createQuestion();
        loadQuestion(question);
        return question;
    }
    
    public final ExportName createExportName(final Question question) throws Exception {
    	ExportName exportName = SampleDataFactory.createExportName(question);
    	loadExportName(exportName);
    	return exportName;
    }

    public final void loadExportName(final ExportName exportName) throws Exception {
    	dataLoader.addExportName(exportName);
    }
    
    /**
     * @param question
     *            the question
     * @throws Exception
     *             checked exception thrown by method
     */
    public final void loadQuestion(final Question question) throws Exception {
        dataLoader.addQuestion(question);
    }
    
    /**
     * @param pageComponent
     *            the pageComponent
     * @param element
     *            the element
     * @param sequence
     *            the sequence
     * @return the component
     * @throws Exception
     *             checked exception thrown by method
     */
    public final Component createComponent(final String labelKey) throws Exception {
        Component component = SampleDataFactory.createComponent(labelKey);
        loadComponent(component);
        return component;
    }

    /**
     * @param component
     *            the component
     * @throws Exception
     *             checked exception thrown by method
     */
    public final void loadComponent(final Component component) throws Exception {
        dataLoader.addComponent(component);
    }
    
    /**
     * @param pageComponent
     *            the pageComponent
     * @param element
     *            the element
     * @param sequence
     *            the sequence
     * @return the component
     * @throws Exception
     *             checked exception thrown by method
     */
    public final Field createField(final Component component, final Element element, final int sequence) throws Exception {
        Field field = SampleDataFactory.createField(component, element, sequence);
        loadField(field);
        return field;
    }

    /**
     * @param component
     *            the component
     * @throws Exception
     *             checked exception thrown by method
     */
    public final void loadField(final Field field) throws Exception {
        dataLoader.addField(field);
    }

    /**
     * @param pageDefinition
     *            the page definition
     * @param labelKey
     *            the labelKey
     * @param sequence
     *            the sequence for the component
     * @return the component
     * @throws Exception
     *             checked exception thrown by method
     */
    public final PageComponent createPageComponent(final PageDefinition pageDefinition, final Component component, final int sequence) throws Exception {
        PageComponent pageComponent = SampleDataFactory.createPageComponent(pageDefinition, component, sequence);
        loadPageComponent(pageComponent);
        return pageComponent;
    }

    /**
     * @param component
     *            the component
     * @throws Exception
     *             checked exception thrown by method
     */
    public final void loadPageComponent(final PageComponent pageComponent) throws Exception {
        dataLoader.addPageComponent(pageComponent);
    }

    /**
     * @param product
     *            the product
     * @param pageDefinitionType
     *            the pageDefinitionType
     * @return the page definition
     * @throws Exception
     *             checked exception thrown by method
     */
    public final AccountPageDefinition createAccountPageDefinition() throws Exception {
    	AccountPageDefinition accountPageDefinition = SampleDataFactory.createAccountPageDefinition();
    	loadAccountPageDefinition(accountPageDefinition);
        return accountPageDefinition;
    }

    /**
     * @param pageDefinition
     *            the page definition
     * @throws Exception
     *             checked exception thrown by method
     */
    public final void loadAccountPageDefinition(final AccountPageDefinition accountPageDefinition) throws Exception {
        dataLoader.addAccountPageDefinition(accountPageDefinition);
    }
    
    /**
     * @param product
     *            the product
     * @param pageDefinitionType
     *            the pageDefinitionType
     * @return the page definition
     * @throws Exception
     *             checked exception thrown by method
     */
    public final ProductPageDefinition createProductPageDefinition() throws Exception {
    	ProductPageDefinition productPageDefinition = SampleDataFactory.createProductPageDefinition();
    	loadProductPageDefinition(productPageDefinition);
        return productPageDefinition;
    }

    /**
     * @param pageDefinition
     *            the page definition
     * @throws Exception
     *             checked exception thrown by method
     */
    public final void loadProductPageDefinition(final ProductPageDefinition productPageDefinition) throws Exception {
        dataLoader.addProductPageDefinition(productPageDefinition);
    }

    public final EacGroups createEacGroups(String groupName, AdminUser createdBy, AdminUser updatedBy, boolean editable, Set<Product> products) throws Exception{
        EacGroups eacGroups = SampleDataFactory.createEacGroups(groupName, createdBy, updatedBy, editable, products);
        loadEacGroups(eacGroups);
        return eacGroups;
    }
    
    public final void loadEacGroups(final EacGroups eacGroups) throws Exception {
        dataLoader.addEacGroups(eacGroups);
        dataLoader.addProductGroupsMapping(eacGroups);
    }
    /**
     * @param erightsId
     *            the erights id
     * @param registrationType
     *            the registrationType
     * @param productOwner
     *            the productOwner
     * @param licenceTemplate
     *            The licence template
     * @return the product definition
     * @throws Exception
     *             checked exception thrown by method
     */

    /**
     * @param product
     *            the product definition
     * @throws Exception
     *             checked exception thrown by method
     */
    
    /**
     * @param registerableType TODO
     * @param erightsId
     *            the erights id
     * @param registrationType
     *            the registrationType
     * @param productOwner
     *            the productOwner
     * @param licenceTemplate
     *            The licence template
     * @return the product definition
     * @throws Exception
     *             checked exception thrown by method
     */
    public final RegisterableProduct createRegisterableProduct(final Integer erightsId,final String prodName, RegisterableType registerableType) throws Exception {
    	RegisterableProduct product = SampleDataFactory.createRegisterableProduct(erightsId,prodName,registerableType);
        //loadRegisterableProduct(product);
        return product;
    }

    /**
     * @param product
     *            the product definition
     * @throws Exception
     *             checked exception thrown by method
     */
    public final void loadRegisterableProduct(final RegisterableProduct product) throws Exception {
        dataLoader.addRegisterableProduct(product);
    }
    
    public final LinkedProduct createLinkedProduct(final RegisterableProduct registerableProduct,
    										final Integer erightsId) throws Exception {
    	LinkedProduct product = SampleDataFactory.createLinkedProduct(registerableProduct,erightsId);
        //loadLinkedProduct(product);
        return product;
    }

    /**
     * @param product
     *            the product definition
     * @throws Exception
     *             checked exception thrown by method
     */
    public final void loadLinkedProduct(final LinkedProduct product) throws Exception {
        dataLoader.addLinkedProduct(product);
    }
	
    public final ActivationCodeRegistrationDefinition createActivationCodeRegistrationDefinition(final RegisterableProduct product, 
    		final LicenceTemplate licenceTemplate, final RegistrationActivation registrationActivation, final ProductPageDefinition productPageDefinition) throws Exception {
    	ActivationCodeRegistrationDefinition activationCodeRegistrationDefinition = SampleDataFactory.createActivationCodeRegistrationDefinition(product, 
        		licenceTemplate, registrationActivation, productPageDefinition);
    	loadActivationCodeRegistrationDefinition(activationCodeRegistrationDefinition);
    	return activationCodeRegistrationDefinition;
    }
    
    public final ActivationCodeRegistrationDefinition createActivationCodeRegistrationDefinitionForEacGroup(final EacGroups eacGroup, 
    		final LicenceTemplate licenceTemplate, final RegistrationActivation registrationActivation, final ProductPageDefinition productPageDefinition) throws Exception {
    	ActivationCodeRegistrationDefinition activationCodeRegistrationDefinition = SampleDataFactory.createActivationCodeRegistrationDefinitionForEacGroup(eacGroup, 
        		licenceTemplate, registrationActivation, productPageDefinition);
    	loadActivationCodeRegistrationDefinition(activationCodeRegistrationDefinition);
    	return activationCodeRegistrationDefinition;
    }
    
	public final void loadActivationCodeRegistrationDefinition(final ActivationCodeRegistrationDefinition activationCodeRegistrationDefinition) throws Exception {
		dataLoader.addActivationCodeRegistrationDefinition(activationCodeRegistrationDefinition);
	}
	
    public final ActivationCodeRegistration createActivationCodeRegistration(final Customer customer, 
			final ActivationCodeRegistrationDefinition activationCodeRegistrationDefinition,
			final ActivationCode activationCode) throws Exception {
		ActivationCodeRegistration acrdb = SampleDataFactory.createActivationCodeRegistration(customer, activationCodeRegistrationDefinition, 
																						activationCode);
		loadActivationCodeRegistration(acrdb);
		return acrdb;
	}
	
    /**
     * Creates the activation code registration.
     *
     * @param customer the customer
     * @param activationCodeRegistrationDefinition the activation code registration definition
     * @param activationCode the activation code
     * @param activated the activated flag
     * @param completed the completed flag
     * @param awaitingValidation the awaiting validation flag
     * @param denied the denied flag
     * @return the activation code registration
     * @throws Exception the exception
     */
    public final ActivationCodeRegistration createActivationCodeRegistration(final Customer customer, 
            final ActivationCodeRegistrationDefinition activationCodeRegistrationDefinition,
            final ActivationCode activationCode,
            final boolean activated, final boolean completed,
            final boolean awaitingValidation, final boolean denied) throws Exception {
        ActivationCodeRegistration acrdb = SampleDataFactory.createActivationCodeRegistration(
                customer, activationCodeRegistrationDefinition, 
                activationCode,
                activated, completed,
                awaitingValidation, denied, new DateTime(), new DateTime());
        loadActivationCodeRegistration(acrdb);
        return acrdb;
    }

    public final void loadActivationCodeRegistration(final ActivationCodeRegistration acrdb) throws Exception {
		dataLoader.addActivationCodeRegistration(acrdb);
	}
	
    
    /**
     * @return the created and loaded user
     * @throws Exception
     *             checked exception thrown by method
     */
    public final Customer createCustomer() throws Exception {
        Customer customer = SampleDataFactory.createCustomer();
        //loadCustomer(customer);
        return customer;
    }

    public final Customer createCustomerForAnswer() throws Exception {
        Customer customer = SampleDataFactory.createCustomer();
        loadCustomer(customer);
        return customer;
    }
    /**
     * @param customer
     *            the user
     * @throws Exception
     *             checked exception thrown by method
     */
    public final void loadCustomer(final Customer customer) throws Exception {
        dataLoader.addCustomer(customer);
    }
    
    public final void loadWhiteListUrl(final WhiteListUrl whiteListUrl) throws Exception {
        dataLoader.addWhiteListUrlService(whiteListUrl);
    }
    
    /**
     * @return registration for a customer
     * @throws Exception
     *             checked exception thrown by method
     */
    public final Registration<ProductRegistrationDefinition> createRegistration(final Customer customer, 
    											final ProductRegistrationDefinition productRegistrationDefinition) throws Exception {
        Registration<ProductRegistrationDefinition> registration = SampleDataFactory.createProductRegistration(customer, productRegistrationDefinition, new DateTime(), new DateTime());
        //loadRegistration(registration);
        return registration;
    }
    
    /**
     * Creates the registration.
     *
     * @param customer the customer
     * @param productRegistrationDefinition the product registration definition
     * @param activated the activated flag
     * @param completed the completed flag
     * @param awaitingValidation the awaiting validation flag
     * @param denied the denied flag
     * @return the registration
     * @throws Exception the exception
     */
    public final Registration<ProductRegistrationDefinition> createRegistration(final Customer customer, 
                                                final ProductRegistrationDefinition productRegistrationDefinition, 
                                                final boolean activated, final boolean completed,
                                                final boolean awaitingValidation, final boolean denied) throws Exception {
        
        Registration<ProductRegistrationDefinition> registration = SampleDataFactory.createProductRegistration(customer, productRegistrationDefinition, activated, completed, awaitingValidation, denied, new DateTime(), new DateTime());
        //loadRegistration(registration);
        return registration;
    }

    /**
     * @param customer
     *            the user
     * @throws Exception
     *             checked exception thrown by method
     */
    public final void loadRegistration(final Registration<? extends ProductRegistrationDefinition> registration) throws Exception {
        dataLoader.addRegistration(registration);
    }
    
    
    
    /**
     * @return the created and loaded user
     * @throws Exception
     *             checked exception thrown by method
     */
    public final AdminUser createAdminUser() throws Exception {
        AdminUser adminUser = SampleDataFactory.createAdminUser();
        loadAdminUser(adminUser);
        return adminUser;
    }

    /**
     * @param customer
     *            the user
     * @throws Exception
     *             checked exception thrown by method
     */
    public final void loadAdminUser(final AdminUser adminUser) throws Exception {
        dataLoader.addAdminUser(adminUser);
    }

    /**
     * @return the composite data set
     * @throws Exception
     *             checked exception thrown by method
     */
    public final CompositeDataSet getAllDataSets(boolean logData) throws Exception {
    	CompositeDataSet compositeDataSet = new CompositeDataSet(dataLoader.getAllDataSets());
    	if(logData) {
    		outputdata(compositeDataSet);
    	}
    	return compositeDataSet;
    }    
    
    /**
     * @return the composite data set
     * @throws Exception
     *             checked exception thrown by method
     */
    public final CompositeDataSet getAllDataSets() throws Exception {
    	return getAllDataSets(true);
    }
    
    private void outputdata(final CompositeDataSet compositeDataSet) {
    	try {
    		if(compositeDataSet.getTableNames() != null && compositeDataSet.getTableNames().length > 0) {
	    		StringBuilder data = new StringBuilder();
	    		data.append("\n");
	    		data.append("\n***********************************");
	    		data.append("\nLOADING DATA INTO DB USING DBUNIT..");
	    		data.append("\n***********************************\n");
		    	for(String tableName : compositeDataSet.getTableNames()) {
		    		ITable iTable = compositeDataSet.getTable(tableName);
		    		data.append("\nLOADING DATA INTO TABLE: " + tableName);
		    		ITableMetaData iTableMetaData = iTable.getTableMetaData();
		    		Column[] columns = iTableMetaData.getColumns();
		    		int rowCount = iTable.getRowCount();
		    		for(int i=0;i<rowCount;i++) {
		    			data.append("\n[ROW]: " + (i + 1));
		    			for(Column column : columns) {
		    				data.append("   [").append(column.getColumnName()).append("]").append(": ").append(iTable.getValue(i, column.getColumnName()));
		    			}
		    		}
		    	}
		    	data.append("\n\n");
		    	LOGGER.info(data.toString());
    		}
    	} catch (Exception e) {
			// TODO: handle exception
		}
    }

    /**
     * Creates a UrlSkin from a url and skinPath. 
     * @param url the url containing the domain and the resource path
     * @param skinPath the skin path
     * @return the UrlSkin for the url and skinPath
     * @throws Exception if there's a problem
     */
    public final UrlSkin createUrlSkin(URL url, String skinPath) throws Exception {
        UrlSkin skin = SampleDataFactory.createUrlSkin(url,skinPath);
        loadUrlSkin(skin);
        return skin;
    }

    /**
     * Loads the supplied UrlSkin into the dataLoader
     * @param UrlSkin the UrlSkin to load
     * @throws Exception if there's a problem
     */
    private void loadUrlSkin(UrlSkin UrlSkin) throws Exception {
        dataLoader.addUrlSkin(UrlSkin);
    }
    
    /**
     * Creates a sample QuartzLogEntry
     * @param triggerName the name of the trigger
     * @param triggerGroup the name of the trigger group
     * @param jobName the name of the job
     * @param jobGroup the name of the job group
     * @param scheduled the time this job was scheduled for
     * @param delayed the amount of delay until before the job was executed after the scheduled time
     * @param next the amount of delay between the scheduled execution time and the next execution time
     * @param summary a summary string
     * @param jobTimeTaken the amount of time after the start of the job that the job took to complete
     * @param refireCount the refire count
     * @param trigerCode the trigger instruction code which is the result of running the trigger
     * @param hostName the host name
     * @param hostAddress the host address
     * @param jobClassName the java class of the job
     * @return a sample QuartzLogEntry object
     * @throws Exception if there is a problem
     */
    public final QuartzLogEntry createQuartzLogEntry(
            String triggerName, String triggerGroup, 
            String jobName, String jobGroup, 
            DateTime scheduled, Seconds delayed, Minutes next, 
            Seconds jobTimeTaken, int refireCount, int trigerCode, String hostName, String hostAddress, String jobClassName)
    throws Exception {
        
        QuartzLogEntry logEntry = SampleDataFactory.createQuartzLogEntry(triggerName, triggerGroup, jobName, jobGroup, scheduled, delayed, next, jobTimeTaken, refireCount, trigerCode, hostName, hostAddress, jobClassName);
        dataLoader.addQuartzLogEntry(logEntry);
        return logEntry;
    }
    
    /**
     * Creates the db message.
     * This is deprecated. Prefer <code>createMessage</code> instead.
     *
     * @param basename the basename
     * @param locale the locale
     * @param key the key
     * @param message the message text
     * @return the db message
     * @throws Exception the exception
     */
    @Deprecated
    public final DbMessage createDbMessage(String basename, Locale locale, String key, String message ) throws Exception {
        String language = null;
        String country = null;
        String variant = null;
        if(locale != null){
          language = locale.getLanguage();
          country = locale.getCountry();
          variant = locale.getVariant();
        }
        DbMessage result = SampleDataFactory.createDbMessage(basename, language, country, variant, key, message);
        dataLoader.addDbMessage(result);
        return result;
    }
    
	public final Message createMessage(String basename, Locale locale, String key, String message) throws Exception {
		String language = null;
		String country = null;
		String variant = null;
		if (locale != null) {
			language = StringUtils.defaultIfEmpty(locale.getLanguage(), null);
			country = StringUtils.defaultIfEmpty(locale.getCountry(), null);
			variant = StringUtils.defaultIfEmpty(locale.getVariant(), null);
		}
		Message result = SampleDataFactory.createMessage(basename, language, country, variant, key, message);
		dataLoader.addMessage(result);
		return result;
	}
	
	public final Message createMessage(String basename, String key, String message) throws Exception {
		return createMessage(basename, null, key, message);
	}
	
	public void loadMessage(Message message) throws Exception {
		dataLoader.addMessage(message);
	}

    public final ExternalProductId createExternalProductId(Product product, String externalId, ExternalSystemIdType externalSystemType) throws Exception {
        ExternalProductId result = SampleDataFactory.createExternalProductId(product, externalId, externalSystemType);
        dataLoader.addExternalProductId(result);
        return result;
    }
    
    public final ExternalCustomerId createExternalCustomerId(Customer customer, String externalId, ExternalSystemIdType externalSystemType) throws Exception {
        ExternalCustomerId result = SampleDataFactory.createExternalCustomerId(customer, externalId, externalSystemType);
        dataLoader.addExternalCustomerId(result);
        return result;
    }

    public final ExternalSystem createExternalSystem(String name, String desc) throws Exception {
        ExternalSystem result = SampleDataFactory.createExternalSystem(name.toLowerCase(), desc);
        //dataLoader.addExternalSystem(result);        
        return result;
    }

    public ExternalSystemIdType createExternalSystemIdType(ExternalSystem externalSystem1, String name, String desc) throws Exception {
        ExternalSystemIdType result = SampleDataFactory.createExternalSystemType(externalSystem1, name.toLowerCase(), desc);
        //dataLoader.addExternalSystemIdType(result);
        return result;
    }
    
    public LinkedRegistration createLinkedRegistration(Registration<? extends ProductRegistrationDefinition> registration, LinkedProduct linkedProduct, Integer eRightsLicenceId) throws Exception{
        LinkedRegistration result = SampleDataFactory.createLinkedRegistration(registration, linkedProduct, eRightsLicenceId);
        dataLoader.addLinkedRegistration(result);
        return result;
    }
    
    
    public CustomerMigration createCustomerMigration(CustomerMigration customerMigration) throws Exception {
        CustomerMigration result = SampleDataFactory.createCustomerMigration(customerMigration);        
        CustomerMigrationData data = customerMigration.getData();
        if(data != null){
            dataLoader.addCustomerMigrationData(data);
        }
        dataLoader.addCustomerMigration(result);
        return result;
    }
    
    /**
     * @return the StandardLicenceTemplate
     * @throws Exception
     *             checked exception thrown by method
     */
    public final DivisionAdminUser createDivisionAdminUser(Division division, AdminUser adminUser) throws Exception {
        DivisionAdminUser divisionAdminUser = SampleDataFactory.createDivisionAdminUser(division, adminUser);
        loadDivisionAdminUser(divisionAdminUser);
        return divisionAdminUser;
    }

    /**
     * @param standardLicenceTemplate
     *            the standardLicenceTemplate
     * @throws Exception
     *             checked exception thrown by method
     */
    public final void loadDivisionAdminUser(final DivisionAdminUser divisionAdminUser) throws Exception {
        dataLoader.addDivisionAdminUser(divisionAdminUser);
    }  
}
