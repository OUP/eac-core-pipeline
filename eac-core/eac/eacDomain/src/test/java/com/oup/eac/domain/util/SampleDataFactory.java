/**
 * 
 */
package com.oup.eac.domain.util;

import java.net.URL;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.Minutes;
import org.joda.time.Seconds;

import com.oup.eac.common.utils.EACSettings;
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
import com.oup.eac.domain.Customer.CustomerType;
import com.oup.eac.domain.DbMessage;
import com.oup.eac.domain.Division;
import com.oup.eac.domain.DivisionAdminUser;
import com.oup.eac.domain.EacGroups;
import com.oup.eac.domain.Element;
import com.oup.eac.domain.ElementCountryRestriction;
import com.oup.eac.domain.ExportName;
import com.oup.eac.domain.ExportType;
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
import com.oup.eac.domain.Password;
import com.oup.eac.domain.PasswordField;
import com.oup.eac.domain.Permission;
import com.oup.eac.domain.Product;
import com.oup.eac.domain.Product.ProductState;
import com.oup.eac.domain.ProductPageDefinition;
import com.oup.eac.domain.ProductRegistration;
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
import com.oup.eac.domain.RollingLicenceTemplate.RollingBeginOn;
import com.oup.eac.domain.RollingLicenceTemplate.RollingUnitType;
import com.oup.eac.domain.Select;
import com.oup.eac.domain.SelfRegistrationActivation;
import com.oup.eac.domain.StandardLicenceTemplate;
import com.oup.eac.domain.TagOption;
import com.oup.eac.domain.TextField;
import com.oup.eac.domain.TokenState;
import com.oup.eac.domain.UrlLink;
import com.oup.eac.domain.UrlSkin;
import com.oup.eac.domain.UsageLicenceTemplate;
import com.oup.eac.domain.User.EmailVerificationState;
import com.oup.eac.domain.UserState;
import com.oup.eac.domain.ValidatedRegistrationActivation;
import com.oup.eac.domain.migrationtool.CustomerMigration;

/**
 * @author harlandd Sample data factory for creating objects for testing.
 */
public final class SampleDataFactory {

    private static final int INT = 123;

    /**
     * Default constructor.
     */
    private SampleDataFactory() {

    }
    
    
    
    public static final ProgressBar createProgressBar(final ActivationState activationState, final String page, final UserState userState, final RegistrationState registrationState, final ActivationStrategy activationStrategy, 
    													final TokenState tokenState, final RegistrationType registrationType, final AccountValidated accountValidated) {
    	String id = UUID.randomUUID().toString();
    	ProgressBar progressBar = new ProgressBar(page, userState, registrationState, activationState, tokenState, registrationType, activationStrategy, accountValidated);
    	progressBar.setId(id);
    	progressBar.setVersion(0);
    	return progressBar;
    }
    
    public static final ProgressBarElement createProgressBarElement(final ProgressBar progressBar, final ElementType elementType, final int sequence) {
    	String id = UUID.randomUUID().toString();
    	ProgressBarElement progressBarElement = new ProgressBarElement();
    	progressBarElement.setId(id);
    	progressBarElement.setVersion(0);
    	progressBarElement.setDefaultMessage("defaultMessage-" + id);
    	progressBarElement.setElementType(elementType);
    	progressBarElement.setLabel("label-" + id);
    	progressBarElement.setProgressBar(progressBar);
    	progressBarElement.setSequence(sequence);
    	return progressBarElement;
    }
	
    public static final ActivationCode createActivationCode(final ActivationCodeBatch activationCodeBatch, final ActivationCodeGenerator acg, final String code) {
    	ActivationCode activationCode = createActivationCode(activationCodeBatch, acg);
    	activationCode.setCode(code);
    	return activationCode;
    }         
    
    public static final Role createRole(final String roleName) {
    	String id = UUID.randomUUID().toString();
    	Role role = new Role();
    	role.setName(roleName);
    	role.setId(id);
    	role.setVersion(0);
    	return role;
    }
    
    public static final Permission createPermission(final String permissionName) {
    	String id = UUID.randomUUID().toString();
    	Permission permission = new Permission();
    	permission.setName(permissionName);
    	permission.setId(id);
    	permission.setVersion(0);
    	return permission;
    }
    
    public static final ActivationCode createActivationCode(final ActivationCodeBatch activationCodeBatch, final ActivationCodeGenerator acg) {
    	String id = UUID.randomUUID().toString();
    	ActivationCode activationCode = new ActivationCode();
    	activationCode.setActivationCodeBatch(activationCodeBatch);
    	activationCode.setActualUsage(0);
    	activationCode.setAllowedUsage(2);
    	activationCode.setCode(acg.createActivationCode("AABB"));
    	activationCode.setId(id);
    	activationCode.setVersion(0);
    	return activationCode;
    }
    
    public static final ActivationCodeBatch createActivationCodeBatch(final ActivationCodeFormat activationCodeFormat, 
    		final LicenceTemplate licenceTemplate, final ActivationCodeRegistrationDefinition activationCodeRegistrationDefinition, final DateTime startDate, final DateTime endDate, String batchId) {
    	String id = UUID.randomUUID().toString();
    	ActivationCodeBatch activationCodeBatch = new ActivationCodeBatch();
    	activationCodeBatch.setActivationCodeFormat(activationCodeFormat);
    	activationCodeBatch.setActivationCodeRegistrationDefinition(activationCodeRegistrationDefinition);
    	if(StringUtils.isBlank(batchId)) {
    	    activationCodeBatch.setBatchId("batchId-" + id);
    	} else {
    	    activationCodeBatch.setBatchId(batchId);
        }
    	
    	if (activationCodeFormat.isPrefixed()) {
    	    activationCodeBatch.setCodePrefix("1234");
    	} else {
    	    activationCodeBatch.setCodePrefix(null);
    	}
    	
    	activationCodeBatch.setCreatedDate(new DateTime());
    	activationCodeBatch.setStartDate(startDate);
    	activationCodeBatch.setEndDate(endDate);
    	activationCodeBatch.setLicenceTemplate(licenceTemplate);
    	activationCodeBatch.setId(id);
    	activationCodeBatch.setVersion(0);
    	return activationCodeBatch;
    }
    
    public static final AccountRegistrationDefinition createAccountRegistrationDefinition(final RegisterableProduct product, 
    		final RegistrationActivation registrationActivation, final AccountPageDefinition pageDefinition) {
    	String id = UUID.randomUUID().toString();
    	AccountRegistrationDefinition accountCodeRegistrationDefinition = new AccountRegistrationDefinition();	
		accountCodeRegistrationDefinition.setId(id);
		accountCodeRegistrationDefinition.setVersion(0);
		accountCodeRegistrationDefinition.setProduct(product);
		accountCodeRegistrationDefinition.setRegistrationActivation(registrationActivation);
		accountCodeRegistrationDefinition.setPageDefinition(pageDefinition);
		accountCodeRegistrationDefinition.setValidationRequired(false);
		return accountCodeRegistrationDefinition;
	}
    
    public static final ProductRegistrationDefinition createProductRegistrationDefinition(final RegisterableProduct product, 
    		final LicenceTemplate licenceTemplate, final RegistrationActivation registrationActivation, final ProductPageDefinition pageDefinition) {
		return createProductRegistrationDefinition(new ProductRegistrationDefinition(), product, null, licenceTemplate, registrationActivation, pageDefinition);	
	}
    
    public static final ActivationCodeRegistrationDefinition createActivationCodeRegistrationDefinition(final RegisterableProduct product, 
    		final LicenceTemplate licenceTemplate, final RegistrationActivation registrationActivation, final ProductPageDefinition pageDefinition) {
		return (ActivationCodeRegistrationDefinition)createProductRegistrationDefinition(new ActivationCodeRegistrationDefinition(), product, null, licenceTemplate, registrationActivation, pageDefinition);	
	}
    
    public static final ActivationCodeRegistrationDefinition createActivationCodeRegistrationDefinitionForEacGroup(final EacGroups eacGroup, 
    		final LicenceTemplate licenceTemplate, final RegistrationActivation registrationActivation, final ProductPageDefinition pageDefinition) {
		return (ActivationCodeRegistrationDefinition)createProductRegistrationDefinition(new ActivationCodeRegistrationDefinition(), null, eacGroup, licenceTemplate, registrationActivation, pageDefinition);	
	}
    
    private static final ProductRegistrationDefinition createProductRegistrationDefinition(final ProductRegistrationDefinition productRegistrationDefinition, final RegisterableProduct product, final EacGroups eacGroup,
    		final LicenceTemplate licenceTemplate, final RegistrationActivation registrationActivation, final ProductPageDefinition pageDefinition) {
    	String id = UUID.randomUUID().toString();
    	productRegistrationDefinition.setId(id);
    	productRegistrationDefinition.setVersion(0);
    	//productRegistrationDefinition.setConfirmationEmailEnabled(Boolean.TRUE);
    	productRegistrationDefinition.setProduct(product);
    	productRegistrationDefinition.setEacGroup(eacGroup);
    	//productRegistrationDefinition.setRegistrationActivation(registrationActivation);
		productRegistrationDefinition.setPageDefinition(pageDefinition);
		//productRegistrationDefinition.setLicenceTemplate(licenceTemplate);
		return productRegistrationDefinition;
	}
    
    private static final void initRegistrationActivation(final RegistrationActivation registrationActivation) {
		String id = UUID.randomUUID().toString();
		registrationActivation.setId(id);
		registrationActivation.setVersion(0);
	}
    
    public static final ValidatedRegistrationActivation createValidatedRegistrationActivation() {
		ValidatedRegistrationActivation registrationActivation = new ValidatedRegistrationActivation();
		initRegistrationActivation(registrationActivation);
		registrationActivation.setValidatorEmail("validatorEmail-" + registrationActivation.getId());
		return registrationActivation;
	}
    
    public static final InstantRegistrationActivation createInstantRegistrationActivation() {
		InstantRegistrationActivation instantActivation = new InstantRegistrationActivation();
		initRegistrationActivation(instantActivation);
		return instantActivation;
	}
    
    public static final SelfRegistrationActivation createSelfRegistrationActivation() {
		SelfRegistrationActivation selfActivation = new SelfRegistrationActivation();
		initRegistrationActivation(selfActivation);
		return selfActivation;
	}
    
    /**
     * @return a sample user
     */
    public static final DivisionAdminUser createAdminUser(AdminUser adminUser) {
        String id = UUID.randomUUID().toString();
        DivisionAdminUser divisionAdminUser = new DivisionAdminUser(adminUser);
        divisionAdminUser.setId(id);
        divisionAdminUser.setVersion(0);
        return divisionAdminUser;
    }      
    
    /**
     * @return a sample user
     */
    public static final Division createDivision(String divisionType) {
        String id = UUID.randomUUID().toString();
        Division division = new Division();
        division.setId(id);
        division.setDivisionType(divisionType);
        division.setVersion(0);
        division.setCreatedDate(new DateTime());
        return division;
    }   
    
    /**
     * @return a sample user
     */
    public static final Customer createCustomer() {
        String id = UUID.randomUUID().toString();
        Customer user = new Customer();
        user.setId(id);
        user.setCustomerType(CustomerType.SELF_SERVICE);
        user.setFirstName("firstName-" + id);
        user.setFamilyName("familyName-" + id);
        user.setUsername("username-" + id);
        user.setPassword(new Password("password-" + id, false));
        user.setId(Integer.valueOf(INT).toString());
        user.setEmailAddress(EACSettings.getProperty(EACSettings.EAC_ADMIN_EMAIL));
        user.setEmailVerificationState(EmailVerificationState.VERIFIED);
        user.setCreatedDate(new DateTime());
        user.setUpdatedDate(new DateTime());
        user.setLocale(Locale.getDefault());
        user.setVersion(0);
        return user;
    }
    
    /**
     * @param createdDate the created date
     * @param updatedDate the updated date
     * @return a sample registration
     */
    public static final ProductRegistration createProductRegistration(final Customer customer, final ProductRegistrationDefinition registrationDefinition, DateTime createdDate, DateTime updatedDate) {
        ProductRegistration result = createProductRegistration(customer, registrationDefinition, false, false, false, false, createdDate, updatedDate);
        return result;
    }
    
    /**
     * @param createdDate the created date
     * @param updatedDate the updated date
     * @return a sample registration
     */
    public static final ProductRegistration createProductRegistration(final Customer customer, final ProductRegistrationDefinition registrationDefinition, boolean activated, boolean completed, boolean awaitValidation, boolean denied, DateTime createdDate, DateTime updatedDate) {
        String id = UUID.randomUUID().toString();
        ProductRegistration registration = new ProductRegistration();
        registration.setId(id);
        registration.setRegistrationDefinition(registrationDefinition);
        registration.setCompleted(completed);
        registration.setActivated(activated);
        registration.setAwaitingValidation(awaitValidation);
        registration.setDenied(denied);
        registration.setCreatedDate(createdDate);
        registration.setUpdatedDate(updatedDate);
        registration.setCustomer(customer);
        registration.setVersion(0);
        return registration;
    }
    
    /**
     * @return a sample registration
     */
    public static final ActivationCodeRegistration createActivationCodeRegistration(final Customer customer, 
                                                    final ActivationCodeRegistrationDefinition activationCodeRegistrationDefinition,
                                                    final ActivationCode activationCode) {
        ActivationCodeRegistration result = createActivationCodeRegistration(customer, activationCodeRegistrationDefinition, activationCode, false, false, false, false, new DateTime(), new DateTime());
        return result;
    }
    /**
     * @param createdDate TODO
     * @param updatedDate TODO
     * @return a sample registration
     */
    public static final ActivationCodeRegistration createActivationCodeRegistration(final Customer customer, 
    												final ActivationCodeRegistrationDefinition activationCodeRegistrationDefinition,
    												final ActivationCode activationCode, 
    												boolean activated,
    												boolean completed,
    												boolean awaitingValidation,
    												boolean denied, DateTime createdDate, DateTime updatedDate
    										) {
        String id = UUID.randomUUID().toString();
        ActivationCodeRegistration registration = new ActivationCodeRegistration();
        registration.setId(id);
        registration.setRegistrationDefinition(activationCodeRegistrationDefinition);
        registration.setCompleted(completed);
        registration.setActivated(activated);
        registration.setAwaitingValidation(awaitingValidation);
        registration.setDenied(denied);
        registration.setCreatedDate(createdDate);
        registration.setUpdatedDate(updatedDate);
        registration.setCustomer(customer);
        registration.setActivationCode(activationCode);
        registration.setVersion(0);
        return registration;
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
    public static final Field createField(final Component component, final Element element, final int sequence) throws Exception {
        Field field = new Field();
        String id = UUID.randomUUID().toString();
        field.setId(id);
        field.setElement(element);
        field.setVersion(0);
        field.setRequired(true);
        field.setSequence(sequence);
        component.addField(field);
        return field;
    }  
    
    /**
     * Create an Answer.
     * 
     * @param customer
     *            The answers customer
     * @param element
     *            The answers element
     * @return The answer
     */
    public static final Answer createAnswer(final Customer customer, final Question question) {
        String id = UUID.randomUUID().toString();
        Answer answer = new Answer();
        answer.setId(id);
        answer.setAnswerText("answerText-" + id);
        answer.setCreatedDate(new DateTime());
        answer.setCustomerId(customer.getId());
        answer.setQuestion(question);
        answer.setVersion(0);
        customer.getAnswers().add(answer);
        return answer;
    }
    
    /**
     * Create an Answer.
     * 
     * @param customer
     *            The answers customer
     * @param element
     *            The answers element
     * @return The answer
     */
    public static final ProductSpecificAnswer createProductSpecificAnswer(final Customer customer, final Question question, final RegisterableProduct registerableProduct) {
        String id = UUID.randomUUID().toString();
        ProductSpecificAnswer answer = new ProductSpecificAnswer();
        answer.setId(id);
        answer.setAnswerText("answerText-" + id);
        answer.setCreatedDate(new DateTime());
        answer.setCustomerId(customer.getId());
        answer.setQuestion(question);
        //answer.setRegisterableProduct(registerableProduct);
        answer.setProductId(registerableProduct.getId());
        answer.setVersion(0);
        customer.getAnswers().add(answer);
        return answer;
    }

    /**
     * Create an Answer.
     * @param element
     *            The answers element
     * @param locale TODO
     * @param customer
     *            The answers customer
     * 
     * @return The answer
     */
    public static final ElementCountryRestriction createElementCountryRestriction(final Element element, final Locale locale) {
        String id = UUID.randomUUID().toString();
        ElementCountryRestriction elementCountryRestriction = new ElementCountryRestriction();
        elementCountryRestriction.setId(id);
        elementCountryRestriction.setElement(element);
        elementCountryRestriction.setLocale(locale);
        elementCountryRestriction.setVersion(0);
        return elementCountryRestriction;
    }

    /**
     * @return a StandardLicenceTemplate
     */
    public static final StandardLicenceTemplate createStandardLicenceTemplate() {
        StandardLicenceTemplate standardLicenceTemplate = new StandardLicenceTemplate();
        initLicenceTemplate(standardLicenceTemplate);
        return standardLicenceTemplate;
    }
    
    /**
     * @return a StandardLicenceTemplate
     */
    public static final RollingLicenceTemplate createRollingLicenceTemplate() {
        RollingLicenceTemplate rollingLicenceTemplate = new RollingLicenceTemplate();
        initLicenceTemplate(rollingLicenceTemplate);
        rollingLicenceTemplate.setBeginOn(RollingBeginOn.FIRST_USE);
        rollingLicenceTemplate.setUnitType(RollingUnitType.MINUTE);
        rollingLicenceTemplate.setTimePeriod(20);
        return rollingLicenceTemplate;
    }
    
    /**
     * @return a StandardLicenceTemplate
     */
    public static final UsageLicenceTemplate createUsageLicenceTemplate() {
        UsageLicenceTemplate usageLicenceTemplate = new UsageLicenceTemplate();
        initLicenceTemplate(usageLicenceTemplate);
        usageLicenceTemplate.setAllowedUsages(10);
        return usageLicenceTemplate;
    }
    
    /**
     * @return a StandardLicenceTemplate
     */
    public static final ConcurrentLicenceTemplate createConcurrentLicenceTemplate() {
        ConcurrentLicenceTemplate concurrentLicenceTemplate = new ConcurrentLicenceTemplate();
        initLicenceTemplate(concurrentLicenceTemplate);
        concurrentLicenceTemplate.setTotalConcurrency(1);
        concurrentLicenceTemplate.setUserConcurrency(1);
        return concurrentLicenceTemplate;
    }
    
    private static final void initLicenceTemplate(final LicenceTemplate licenceTemplate) {
        String id = UUID.randomUUID().toString();
        licenceTemplate.setId(id);
        licenceTemplate.setStartDate(new LocalDate());
        licenceTemplate.setEndDate(new LocalDate().plusWeeks(1));
        licenceTemplate.setVersion(0);    	
        licenceTemplate.setCreatedDate(new DateTime());
        licenceTemplate.setUpdatedDate(new DateTime());
    }

    /**
     * @param registerableType TODO
     * @param erightsId
     *            the erights id
     * @param registrationType
     *            the registrationType
     * @param productOwner
     *            the productOwner
     * @param licenceTemplate
     *            the licence template
     * @return the product definition
     * @throws Exception
     *             checked exception thrown by method
     */
    public static final RegisterableProduct createRegisterableProduct(final Integer erightsId,final String prodName, RegisterableType registerableType) throws Exception {
    	RegisterableProduct product = new RegisterableProduct();
        String id = UUID.randomUUID().toString();
        product.setId(id);
        product.setProductName(prodName);
        product.setRegisterableType(registerableType);
        product.setState(ProductState.ACTIVE);
        product.setLandingPage("landingPage");
       /* product.setErightsId(erightsId);
        product.setProductName(prodName);
        product.setState(ProductState.ACTIVE);
        product.setRegisterableType(registerableType);
        product.setLandingPage("landingPage");
        product.setVersion(0);*/
        return product;
    }
 
    
    
    public static final LinkedProduct createLinkedProduct(final RegisterableProduct registerableProduct,
    												final Integer erightsId) throws Exception {
    	LinkedProduct product = new LinkedProduct();
        String id = UUID.randomUUID().toString();
        product.setId(id);
        product.setErightsId(erightsId);
        product.setProductName("productName");
        product.setLandingPage("landingPage");
        //product.setRegisterableProduct(registerableProduct);
       // product.setActivationMethod(activationMethod);
        product.setVersion(0);
        return product;
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
    public static final ProductPageDefinition createProductPageDefinition() throws Exception {
    	return (ProductPageDefinition)createPageDefinition(new ProductPageDefinition());
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
    public static final AccountPageDefinition createAccountPageDefinition() throws Exception {
    	return (AccountPageDefinition)createPageDefinition(new AccountPageDefinition());
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
    private static final PageDefinition createPageDefinition(final PageDefinition pageDefinition) throws Exception {
        String id = UUID.randomUUID().toString();
        pageDefinition.setId(id);        
        pageDefinition.setVersion(0);
        pageDefinition.setName("name");
        pageDefinition.setTitle("title");
        return pageDefinition;
    }

    /**
     * @param pageDefinition
     *            the page definition
     * @param labelKey
     *            the labelKey
     * @param sequence
     *            the sequence
     * @return the component
     * @throws Exception
     *             checked exception thrown by method
     */
    public static final PageComponent createPageComponent(final PageDefinition pageDefinition, final Component component, final int sequence) throws Exception {
        PageComponent pageComponent = new PageComponent();
        String id = UUID.randomUUID().toString();
        pageComponent.setId(id);
        pageComponent.setSequence(sequence);
        pageComponent.setVersion(0);
        pageComponent.setComponent(component);
        pageDefinition.addPageComponent(pageComponent);
        return pageComponent;
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
    public static final Component createComponent(final String labelKey) throws Exception {
        Component component = new Component();
        String id = UUID.randomUUID().toString();
        component.setId(id);
        component.setLabelKey(labelKey);
        component.setVersion(0);
        return component;
    }

    /**
     * @return the element
     * @throws Exception
     *             checked exception thrown by method
     */
    public static final Element createElement(final Question question) throws Exception {
        Element element = new Element();
        String id = UUID.randomUUID().toString();
        element.setId(id);
        element.setQuestion(question);
        element.setRegularExpression("regularExpression-" + id);
        element.setHelpText("");
        element.setVersion(0);
        return element;
    }
    
    /**
     * @return the element
     * @throws Exception
     *             checked exception thrown by method
     */
    public static final Question createQuestion() throws Exception {
        Question question = new Question();
        String id = UUID.randomUUID().toString();
        question.setId(id);
        question.setProductSpecific(true);
        question.setDescription("exportName-" + id);
        question.setElementText("elementText-" + id);
        question.setVersion(0);
        return question;
    }
    
    public static final ExportName createExportName(final Question question) throws Exception {
    	ExportName exportName = new ExportName();
    	exportName.setName("export_name");
    	exportName.setExportType(ExportType.CMDP);
    	exportName.setQuestion(question);
    	exportName.setVersion(0);
    	exportName.setId(UUID.randomUUID().toString());
    	exportName.setCreatedDate(new DateTime());
    	return exportName;
    }

    /**
     * @param element
     *            the element
     * @return the Label
     */
    public static final Label createLabel(final Element element) {
        Label label = new Label();
        String id = UUID.randomUUID().toString();
        label.setId(id);
        label.setVersion(0);
        element.addTag(label);
        return label;
    }

    /**
     * @param element
     *            the element
     * @return the UrlLink
     */
    public static final UrlLink createUrlLink(final Element element) {
        UrlLink urlLink = new UrlLink();
        String id = UUID.randomUUID().toString();
        urlLink.setId(id);
        urlLink.setVersion(0);
        urlLink.setUrl("http://www.google.com");
        urlLink.setNewWindow(true);
        element.addTag(urlLink);
        return urlLink;
    }

    /**
     * @param element
     *            the element
     * @return the text field
     */
    public static final TextField createTextField(final Element element) {
        TextField textField = new TextField();
        String id = UUID.randomUUID().toString();
        textField.setId(id);
        textField.setVersion(0);
        element.addTag(textField);
        return textField;
    }

    /**
     * @param element
     *            the element
     * @return the password field
     */
    public static final PasswordField createPasswordField(final Element element) {
        PasswordField passwordField = new PasswordField();
        String id = UUID.randomUUID().toString();
        passwordField.setId(id);
        passwordField.setVersion(0);
        element.addTag(passwordField);
        return passwordField;
    }

    /**
     * @param element
     *            the element
     * @return the checkbox
     */
    public static final Checkbox createCheckbox(final Element element) {
        Checkbox checkbox = new Checkbox();
        String id = UUID.randomUUID().toString();
        checkbox.setId(id);
        checkbox.setVersion(0);
        element.addTag(checkbox);
        return checkbox;
    }

    /**
     * @param element
     *            the element
     * @return the select
     */
    public static final Select createSelect(final Element element) {
        Select select = new Select();
        String id = UUID.randomUUID().toString();
        select.setId(id);
        select.setVersion(0);
        element.addTag(select);
        return select;
    }

    /**
     * @param element
     *            the element
     * @return the select
     */
    public static final MultiSelect createMultiSelect(final Element element) {
    	MultiSelect select = new MultiSelect();
        String id = UUID.randomUUID().toString();
        select.setId(id);
        select.setVersion(0);
        element.addTag(select);
        return select;
    }    
    
    /**
     * @param element
     *            the element
     * @return the radio tag
     */
    public static final Radio createRadio(final Element element) {
        Radio radio = new Radio();
        String id = UUID.randomUUID().toString();
        radio.setId(id);
        radio.setVersion(0);
        element.addTag(radio);
        return radio;
    }

    /**
     * @param optionsTag
     *            the options tag
     * @return the TagOption
     */
    public static final TagOption createTagOption(final OptionsTag optionsTag) {
        TagOption tagOption = new TagOption();
        String id = UUID.randomUUID().toString();
        tagOption.setId(id);
        tagOption.setLabel("label-" + id);
        tagOption.setValue("value-" + id);
        tagOption.setSequence(1);
        tagOption.setVersion(0);
        optionsTag.addOption(tagOption);
        return tagOption;
    }
    
    /**
     * @return a sample admin user
     */
    public static final AdminUser createAdminUser() {
        String id = UUID.randomUUID().toString();
        AdminUser user = new AdminUser();
        user.setId(id);
        user.setFirstName("firstName-" + id);
        user.setFamilyName("familyName-" + id);
        user.setUsername("username-" + id);
        user.setPassword(new Password("password-" + id, false));
        user.setCreatedDate(new DateTime());
        user.setEmailVerificationState(EmailVerificationState.VERIFIED);
        user.setUpdatedDate(new DateTime());
        user.setVersion(0);
        return user;
    }
    
    /**
     * Creates a sample UrlSkin
     * @param url the url for the resource
     * @param skinPath the skin path associated with the url
     * @return a UrlSkin object
     * @throws Exception if there's a problem with the URL
     */
    public static final UrlSkin createUrlSkin(URL url, String skinPath) throws Exception {
        UrlSkin skin = new UrlSkin();
        String urlString = url.toExternalForm();
        
        String id = UUID.randomUUID().toString();
        skin.setId(id);
        skin.setVersion(0);
        skin.setUrl(urlString);
        skin.setSkinPath(skinPath);
        
        return skin;
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
     * @param jobTimeTaken the amount of time after the start of the job that the job took to complete
     * @param refireCount the refire count
     * @param trigerCode the trigger instruction code which is the result of running the trigger
     * @param jobClassName
     * @return a sample QuartzLogEntry object
     */
    public static final QuartzLogEntry createQuartzLogEntry(
            String triggerName, String triggerGroup, 
            String jobName, String jobGroup, 
            DateTime scheduled, Seconds delayed, Minutes next, Seconds jobTimeTaken, 
            int refireCount, int trigerCode, String hostName, String hostAddress, String jobClassName){
        
        DateTime actualTime = scheduled.plus(delayed); 
        DateTime nextTime = scheduled.plus(next);
        
        long runTime = jobTimeTaken.getSeconds() * 1000;

        QuartzLogEntry entry = new QuartzLogEntry();
        
        String id = UUID.randomUUID().toString();
        entry.setId(id);
        entry.setVersion(0);

        entry.setRefireCount(refireCount);
        entry.setTriggerInstructionCode(trigerCode);
        
        entry.setTriggerName(triggerName);
        entry.setTriggerGroup(triggerGroup);
        entry.setJobName(jobName);
        entry.setJobGroup(jobGroup);
        
        entry.setSchedFireTime(scheduled);
        entry.setActualFireTime(actualTime);
        entry.setNextFireTime(nextTime);
        entry.setJobRunTime(runTime);
        
        entry.setHostName(hostName);
        entry.setHostAddress(hostAddress);
        entry.setJobClassName(jobClassName);
        return entry;
    }
 
    /**
     * Creates a new DbMessage object.
     * This is deprecated. Prefer <code>createMessage</code> instead.
     *
     * @param basename the basename
     * @param language the language
     * @param country the country
     * @param variant the variant
     * @param key the key
     * @param message the message
     * @return the db message object
     */
    @Deprecated
    public static final DbMessage createDbMessage(String basename, String language, String country, String variant, String key, String message){
        DbMessage result = new DbMessage();
        result.setBasename(basename);
        result.setCountry(country);
        result.setVariant(variant);
        result.setKey(key);
        result.setLanguage(language);
        result.setMessage(message);
        return result;
    }
    
    public static final Message createMessage(String basename, String language, String country, String variant, String key, String message) {
    	Message result = new Message();
    	String id = UUID.randomUUID().toString();
    	result.setId(id);
    	result.setVersion(0);
    	result.setBasename(basename);
        result.setCountry(country);
        result.setVariant(variant);
        result.setKey(key);
        result.setLanguage(language);
        result.setMessage(message);
        return result;
    }

    public static ExternalProductId createExternalProductId(Product product, String externalId, ExternalSystemIdType eternalSystemType) {
        ExternalProductId result = new ExternalProductId();
        //common id and version fields
        result.setId(UUID.randomUUID().toString());
        result.setVersion(0);
        //
        result.setProduct(product);
        result.setExternalId(externalId);
        result.setExternalSystemIdType(eternalSystemType);
        return result;
    }

    public static ExternalCustomerId createExternalCustomerId(Customer customer, String externalId, ExternalSystemIdType eternalSystemType) {
        ExternalCustomerId result = new ExternalCustomerId();
        //common id and version fields
        result.setId(UUID.randomUUID().toString());
        result.setVersion(0);
        //
        result.setCustomer(customer);
        result.setExternalId(externalId);
        result.setExternalSystemIdType(eternalSystemType);
        return result;
    }

    public static ExternalSystem createExternalSystem(String name, String desc) {
        ExternalSystem result = new ExternalSystem();
        String id = UUID.randomUUID().toString();
        result.setId(id);
        result.setName(name);
        result.setDescription(desc);
        return result;
    }

    public static ExternalSystemIdType createExternalSystemType(ExternalSystem externalSystem1, String name, String desc) {
        ExternalSystemIdType result = new ExternalSystemIdType();
        String id = UUID.randomUUID().toString();
        result.setId(id);
        result.setName(name);
        result.setDescription(desc);
        result.setExternalSystem(externalSystem1);
        return result;
    }
    
    public static LinkedRegistration createLinkedRegistration(Registration<? extends ProductRegistrationDefinition> registration, LinkedProduct linkedProduct, Integer eRightsLicenceId){   
        LinkedRegistration result = new LinkedRegistration();
        String id = UUID.randomUUID().toString();
        result.setId(id);
        result.setRegistration(registration);
        result.setLinkedProduct(linkedProduct);
        result.setErightsLicenceId(eRightsLicenceId);
        return result;
    }
    
    public static CustomerMigration createCustomerMigration(CustomerMigration customerMigration){                           
        customerMigration.setVersion(0);
        customerMigration.setCreatedDate(new DateTime());
        customerMigration.setUpdatedDate(new DateTime());
        return customerMigration;
    }
    
    public static EacGroups createEacGroups(String groupName, AdminUser createdBy, AdminUser updatedBy, boolean editable, Set<Product> products){
        EacGroups retObj = new EacGroups();
        String id = UUID.randomUUID().toString();
        retObj.setId(id);
        retObj.setGroupName(groupName);
        retObj.setCreatedBy(createdBy);
        retObj.setUpdatedBy(updatedBy);
        retObj.setEditable(editable);
        retObj.setProducts(products);
        retObj.setCreatedDate(new DateTime());
        retObj.setUpdatedDate(null);
        retObj.setVersion(0);
        return retObj;
    }
    /**
     * @return a sample user
     */
    public static final DivisionAdminUser createDivisionAdminUser(Division division, AdminUser adminUser) {
        String id = UUID.randomUUID().toString();
        DivisionAdminUser divisionAdminUser = new DivisionAdminUser(adminUser, division);
        divisionAdminUser.setId(id);
        divisionAdminUser.setVersion(0);
        divisionAdminUser.setDivisionErightsId(2451);
        return divisionAdminUser;
    }  
}
