package com.oup.eac.service.impl;

import static com.oup.eac.domain.RegistrationActivation.ActivationStrategy.VALIDATED;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.hibernate.Hibernate;
import org.hibernate.LockOptions;
import org.hibernate.Session;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.constants.SearchDomainFields;
import com.oup.eac.cloudSearch.search.impl.AmazonCloudSearchServiceImpl;
import com.oup.eac.common.utils.EACSettings;
import com.oup.eac.common.utils.MessageTextSource;
import com.oup.eac.common.utils.email.EmailUtils;
import com.oup.eac.common.utils.email.VelocityUtils;
import com.oup.eac.common.utils.token.TokenConverter;
import com.oup.eac.data.ActivationCodeDao;
import com.oup.eac.data.AnswerDao;
import com.oup.eac.data.LinkedRegistrationDao;
import com.oup.eac.data.RegistrationDao;
import com.oup.eac.data.RegistrationDefinitionDao;
import com.oup.eac.domain.AccountPageDefinition;
import com.oup.eac.domain.AccountRegistrationDefinition;
import com.oup.eac.domain.ActivationCode;
import com.oup.eac.domain.ActivationCodeBatch;
import com.oup.eac.domain.ActivationCodeRegistration;
import com.oup.eac.domain.ActivationCodeRegistrationDefinition;
import com.oup.eac.domain.Answer;
import com.oup.eac.domain.Component;
import com.oup.eac.domain.ConcurrentLicenceTemplate;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.Customer.CustomerType;
import com.oup.eac.domain.Division;
import com.oup.eac.domain.Element;
import com.oup.eac.domain.EmailType;
import com.oup.eac.domain.Field;
import com.oup.eac.domain.LicenceTemplate;
import com.oup.eac.domain.LicenceTemplate.LicenceType;
import com.oup.eac.domain.LinkedProduct;
/*import com.oup.eac.domain.LinkedProduct.ActivationMethod;*/
import com.oup.eac.domain.LinkedProductNew;
import com.oup.eac.domain.LinkedRegistration;
import com.oup.eac.domain.MailCriteria;
import com.oup.eac.domain.PageComponent;
import com.oup.eac.domain.PageConstant;
import com.oup.eac.domain.PageDefinition;
import com.oup.eac.domain.Product;
import com.oup.eac.domain.ProductPageDefinition;
import com.oup.eac.domain.ProductRegistration;
import com.oup.eac.domain.ProductRegistrationDefinition;
import com.oup.eac.domain.ProductSpecificAnswer;
import com.oup.eac.domain.Question;
import com.oup.eac.domain.RegisterableProduct;
import com.oup.eac.domain.Registration;
import com.oup.eac.domain.RegistrationActivation;
import com.oup.eac.domain.RegistrationActivation.ActivationStrategy;
import com.oup.eac.domain.RegistrationDefinition;
import com.oup.eac.domain.RegistrationDefinition.RegistrationDefinitionType;
import com.oup.eac.domain.RollingLicenceTemplate;
import com.oup.eac.domain.RollingLicenceTemplate.RollingBeginOn;
import com.oup.eac.domain.SelfRegistrationActivation;
import com.oup.eac.domain.UsageLicenceTemplate;
import com.oup.eac.domain.User.EmailVerificationState;
import com.oup.eac.domain.ValidatedRegistrationActivation;
import com.oup.eac.domain.search.AmazonSearchFields;
import com.oup.eac.domain.search.AmazonSearchRequest;
import com.oup.eac.domain.search.AmazonSearchResponse;
import com.oup.eac.domain.utils.audit.AuditLogger;
import com.oup.eac.dto.AccountRegistrationDto;
import com.oup.eac.dto.CustomerDto;
import com.oup.eac.dto.CustomerRegistrationsDto;
import com.oup.eac.dto.EnforceableProductDto;
import com.oup.eac.dto.EnforceableProductUrlDto;
import com.oup.eac.dto.LicenceDto;
import com.oup.eac.dto.Message;
import com.oup.eac.dto.ProductRegistrationDto;
import com.oup.eac.dto.RegistrationActivationDto;
import com.oup.eac.dto.RegistrationDto;
import com.oup.eac.dto.RollingLicenceDetailDto;
import com.oup.eac.dto.StandardConcurrentLicenceDetailDto;
import com.oup.eac.dto.TokenDto;
import com.oup.eac.dto.UsageLicenceDetailDto;
import com.oup.eac.dto.licence.LicenceDescriptionGenerator;
import com.oup.eac.dto.licence.LicenceDescriptionGeneratorSource;
import com.oup.eac.integration.facade.ErightsFacade;
import com.oup.eac.integration.facade.exceptions.DivisionNotFoundException;
import com.oup.eac.integration.facade.exceptions.ErightsException;
import com.oup.eac.integration.facade.exceptions.ErightsSessionNotFoundException;
import com.oup.eac.integration.facade.exceptions.GroupNotFoundException;
import com.oup.eac.integration.facade.exceptions.LicenseNotFoundException;
import com.oup.eac.integration.facade.exceptions.ProductNotFoundException;
import com.oup.eac.integration.facade.exceptions.UserNotFoundException;
import com.oup.eac.service.ActivationCodeService;
import com.oup.eac.service.DivisionService;
import com.oup.eac.service.EmailService;
import com.oup.eac.service.LicenceService;
import com.oup.eac.service.PageDefinitionService;
import com.oup.eac.service.ProductService;
import com.oup.eac.service.RegistrationDefinitionService;
import com.oup.eac.service.RegistrationService;
import com.oup.eac.service.ServiceLayerException;
import com.oup.eac.service.exceptions.ServiceLayerValidationException;

/**
 * @author harlandd Registration service providing registration related business processes.
 */
@Service(value = "registrationService")
public class RegistrationServiceImpl implements RegistrationService {

	private static final String LICENCE_ACCEPTED_VM = "com/oup/eac/service/velocity/licenceAccepted.vm";

	private static final String LICENCE_ACTIVATED_VM = "com/oup/eac/service/velocity/licenceActivated.vm";

	private static final Logger LOG = Logger.getLogger(RegistrationServiceImpl.class);

	private final AnswerDao answerDao;

	private final RegistrationDao registrationDao;

	private final ErightsFacade erightsFacade;

	private final EmailService emailService;

	private final LicenceService licenceService;

	private final VelocityEngine velocityEngine;

	private final RegistrationDefinitionDao registrationDefinitionDao;

	private final LinkedRegistrationDao linkedRegistrationDao;

	private final MessageSource messageSource;

	private final ActivationCodeService activationCodeService;

	private final PageDefinitionService pageDefinitionService;

	private final ActivationCodeDao activationCodeDao;
	
	private final ProductService productService;
	
	private final RegistrationDefinitionService registrationDefinitionService;

	private LicenceDescriptionGeneratorSource licenceDescriptionGeneratorSource;
	
	private Map<Integer,String> divisionsMap; 
	
	private final DivisionService divisionService;
	
	

	/**
	 * Instantiates a new registration service impl.
	 * 
	 * @param productPageDefinitionDao
	 *            the product page definition dao
	 * @param accountPageDefinitionDao
	 *            the account page definition dao
	 * @param answerDao
	 *            the answer dao
	 * @param registrationDao
	 *            the registration dao
	 * @param erightsFacade
	 *            the erights facade
	 * @param emailService
	 *            the email service
	 * @param velocityEngine
	 *            the velocity engine
	 * @param activationCodeDao
	 *            the activation code dao
	 * @param registrationDefinitionDao
	 *            the registration definition dao
	 * @param linkedRegistrationDao
	 *            the linked registration dao
	 * @param messageSource
	 *            the message source
	 * @param licenceService
	 *            the licence service
	 */
	@Autowired
	public RegistrationServiceImpl(final AnswerDao answerDao, final RegistrationDao registrationDao, final ErightsFacade erightsFacade,
			final EmailService emailService, final VelocityEngine velocityEngine, final ActivationCodeService activationCodeService,
			final RegistrationDefinitionDao registrationDefinitionDao, final LinkedRegistrationDao linkedRegistrationDao, final MessageSource messageSource,
			final LicenceService licenceService, final PageDefinitionService pageDefinitionService,  
			final LicenceDescriptionGeneratorSource licenceDescriptionGeneratorSource,
			final ActivationCodeDao activationCodeDao, final ProductService productService, 
			final RegistrationDefinitionService registrationDefinitionService, DivisionService divisionService) {
		super();
		Assert.notNull(answerDao);
		Assert.notNull(registrationDao);
		Assert.notNull(erightsFacade);
		Assert.notNull(emailService);
		Assert.notNull(velocityEngine);
		Assert.notNull(activationCodeService);
		Assert.notNull(registrationDefinitionDao);
		Assert.notNull(linkedRegistrationDao);
		Assert.notNull(messageSource);
		Assert.notNull(licenceService);
		Assert.notNull(activationCodeDao);
		Assert.notNull(productService);
		Assert.notNull(registrationDefinitionService);
		this.answerDao = answerDao;
		this.registrationDao = registrationDao;
		this.erightsFacade = erightsFacade;
		this.emailService = emailService;
		this.velocityEngine = velocityEngine;
		this.activationCodeService = activationCodeService;
		this.registrationDefinitionDao = registrationDefinitionDao;
		this.linkedRegistrationDao = linkedRegistrationDao;
		this.messageSource = messageSource;
		this.licenceService = licenceService;
		this.pageDefinitionService = pageDefinitionService;
		this.activationCodeDao = activationCodeDao;
		this.licenceDescriptionGeneratorSource = licenceDescriptionGeneratorSource;
		this.productService = productService;
		this.registrationDefinitionService = registrationDefinitionService;
		this.divisionService = divisionService;
	}

	public final List<ActivationCodeRegistration> getActivationCodeRegistrationByCode(final ActivationCode activationCode) {
		return registrationDao.getActivationCodeRegistrationByCode(activationCode);
	}

	/**
	 * @param productDefinition the product definition
	 * @param pageDefinitionType the pageDefinitionType
	 * @return the PageDefinition
	 */
	@Override
	public final ProductRegistrationDto getProductPageDefinitionByRegistrationDefinition(final ProductRegistrationDefinition productRegistrationDefinition,
			final Customer customer, final Locale locale) {
		// Get id for page definition
		
		ProductPageDefinition productPageDefinition =null; 
		if(productRegistrationDefinition.getPageDefinition()!=null){
			productPageDefinition = pageDefinitionService.getFullyFetchedProductPageDefinitionById(productRegistrationDefinition.getPageDefinition().getId());
		}

		// Load dto with values
		ProductRegistrationDto registrationDto = new ProductRegistrationDto();
		initRegistrationDto(productPageDefinition, customer, registrationDto, productRegistrationDefinition, locale);

		// We can't trust that the productRegistrationDefinition we were passed in was loaded in this session - we have to reload it.
		ProductRegistrationDefinition prodRegDef = this.registrationDefinitionDao.getProductRegistrationDefinitionWithLicence(productRegistrationDefinition.getId());
		Assert.isTrue(prodRegDef != null, "Failed to lookup productRegistrationDefinition");
		String productDescription = productRegistrationDefinition.getProduct().getProductName();
		registrationDto.setProductDescription(productDescription);
		return registrationDto;
	}

	/*@Override
	public final Registration<?> getProductRegistration(final String id) {
		return registrationDao.getRegistrationById(id);
	}*/

	@Override
	public final Registration<? extends ProductRegistrationDefinition> getProductRegistration(final String registrationId, final String customerId) throws ServiceLayerException{
		CustomerDto customerDto;
		try {
			customerDto = erightsFacade.getUserAccount(customerId);
		} catch (NumberFormatException | ErightsException e) {
			throw new ServiceLayerException(e.getMessage());
		}
		Customer customer = new Customer();
		//customer.setErightsId(customerDto.getUserId());
		customer.setLocale(customerDto.getLocale());
		customer.setId(customerDto.getUserId());
		List<LicenceDto> licences = licenceService.getLicencesForCustomer(customer, registrationId);
		//List<Registration<? extends ProductRegistrationDefinition>> registrations = new ArrayList<Registration<? extends ProductRegistrationDefinition>>(); 
				//registrationDefinitionService.getProductRegistrationDefinitionByProduct(product);
		Registration e = null;
		Set<LinkedRegistration> linkedRegistrations = setLinkedRegistration(licences);
	
		for(LicenceDto dto : licences){
			//List<ProductRegistration> productRegistrations = new ArrayList<ProductRegistration>();
			
			if(dto.getActivationCode()!=null){
				e = new ActivationCodeRegistration();
			}else{
				e = new ProductRegistration();
			}
			Product product = convertEnforceableProductToProduct(dto.getProducts());
			
			if(dto.getActivationCode()!=null){
				ActivationCodeRegistrationDefinition actRegistrationDefinition = registrationDefinitionService.getActivationCodeRegistrationDefinitionByProduct(product);
				actRegistrationDefinition.setProduct(product);
				e.setRegistrationDefinition(actRegistrationDefinition);
			}else{
				ProductRegistrationDefinition registrationDefinition = registrationDefinitionService.getProductRegistrationDefinitionByProduct(product);
				registrationDefinition.setProduct(product);
				e.setRegistrationDefinition(registrationDefinition);
			}
			/*for(String productId:dto.getProductIds()){
				try {
					EnforceableProductDto enfoProduct = erightsFacade.getProduct(productId);
					Product product = convertEnforceableProductToProduct(enfoProduct);
					
					if(dto.getActivationCode()!=null){
						ActivationCodeRegistrationDefinition actRegistrationDefinition = registrationDefinitionService.getActivationCodeRegistrationDefinitionByProduct(product);
						actRegistrationDefinition.setProduct(product);
						e.setRegistrationDefinition(actRegistrationDefinition);
					}else{
						ProductRegistrationDefinition registrationDefinition = registrationDefinitionService.getProductRegistrationDefinitionByProduct(product);
						registrationDefinition.setProduct(product);
						e.setRegistrationDefinition(registrationDefinition);
					}
					
				} catch (ErightsException e1) {
					throw new ServiceLayerException(e1.getMessage());
				}
			}*/
			e.setCompleted(dto.isCompleted());
			e.setActivated(dto.isActive());
			e.setAwaitingValidation(dto.isAwaitingValidation());
			e.setDenied(dto.isDenied());
			//e.setErightsLicenceId(dto.getErightsId());
			e.setCreatedDate(dto.getCreatedDate());
			e.setUpdatedDate(dto.getUpdatedDate());
			e.setId(dto.getLicenseId());
			if(linkedRegistrations!=null && linkedRegistrations.size()>0)
				e.getLinkedRegistrations().addAll(linkedRegistrations);
			if(dto.getActivationCode()!=null){
				ActivationCodeRegistration acReg = (ActivationCodeRegistration)e;
				ActivationCode activationCode = new ActivationCode();
				activationCode.setCode(dto.getActivationCode());
				acReg.setActivationCode(activationCode);
				e = (Registration)acReg;
			}
			e.setCustomer(customer);
			//registrations.add(e);

		}
		return (Registration<? extends ProductRegistrationDefinition>) e;
	}
	
	/*@Override
	public Registration<?> getProductRegistrationInitialised(String id) {
		return registrationDao.getRegistrationByIdInitialised(id);
	}*/

	@Override
	public final AccountRegistrationDto getAccountPageDefinitionByRegistrationDefinition(final AccountRegistrationDefinition accountRegistrationDefinition,
			final Customer customer, final Locale locale) {

		AccountRegistrationDto accountRegistrationDto = new AccountRegistrationDto();
		
		if(accountRegistrationDefinition == null){
			return accountRegistrationDto;
		}
		if(accountRegistrationDefinition.getPageDefinition() == null){
			return accountRegistrationDto;
		}
		AccountPageDefinition apd = accountRegistrationDefinition.getPageDefinition();
		// Get id for page definition
		AccountPageDefinition accountPageDefinition = pageDefinitionService.getFullyFetchedAccountPageDefinitionById(apd.getId());

		// Load dto with values
		initRegistrationDto(accountPageDefinition, customer, accountRegistrationDto, accountRegistrationDefinition, locale);

		return accountRegistrationDto;
	}

	@Override
	public AccountRegistrationDto getAccountRegistrationDto(final AccountPageDefinition pageDefinition, final Locale locale) {
		AccountRegistrationDto accountRegistrationDto = new AccountRegistrationDto();
		initRegistrationDto(pageDefinition, null, accountRegistrationDto, null, locale);
		return accountRegistrationDto;
	}

	@Override
	public ProductRegistrationDto getProductRegistrationDto(final ProductPageDefinition pageDefinition, final Locale locale) {
		ProductRegistrationDto productRegistrationDto = new ProductRegistrationDto();
		initRegistrationDto(pageDefinition, null, productRegistrationDto, null, locale);
		return productRegistrationDto;
	}

	private void initRegistrationDto(final PageDefinition productPageDefinition, final Customer customer, final RegistrationDto registrationDto,
			final RegistrationDefinition registrationDefinition, final Locale locale) {

		if (productPageDefinition != null) {
			registrationDto.setTitle(productPageDefinition.getTitle());
			registrationDto.setPreamble(productPageDefinition.getPreamble());
		}

		if (productPageDefinition == null || CollectionUtils.isEmpty(productPageDefinition.getPageComponents())) {
			return;
		}

		for (PageComponent pageComponent : productPageDefinition.getPageComponents()) {
			processComponents(customer, registrationDto, pageComponent.getComponent(), registrationDefinition, locale);
		}
	}

	private void processComponents(final Customer customer, final RegistrationDto registrationDto, final Component component,
			final RegistrationDefinition registrationDefinition, final Locale locale) {
		// Making copy of component as the PageDefinition is cached and
		// each field should only be visible if it's locale is valid
		Set<Field> fields = new LinkedHashSet<Field>();
		Component componentCopy = Component.valueOf(component.getId(), component.getVersion(), component.getName(), component.getLabelKey(), fields);
		for (Field field : component.getFields()) {
			Element element = field.getElement();
			if (isVisibleForLocale(element, locale)) {
				if (customer == null) {
					registrationDto.addField(field);
				} else {
					Question question = element.getQuestion();
					if (question.isProductSpecific()) {
						ProductSpecificAnswer answer = answerDao.getCustomerProductSpecificAnswerByQuestion(question, customer, (RegisterableProduct)registrationDefinition.getProduct());
						registrationDto.processAnswer(field, answer);
					} else {
						Answer answer = answerDao.getCustomerAnswerByQuestion(question, customer);
						registrationDto.processAnswer(field, answer);
					}
				}
				fields.add(field);
			}
		}
		registrationDto.addComponent(componentCopy);
	}

	private boolean isVisibleForLocale(final Element element, final Locale locale) {
		boolean visible;
		if (element.isNotLocaleRestricted()) {
			visible = true;
		} else {
			visible = (locale != null) && element.isValidForLocale(locale);
		}
		return visible;
	}

	@Override
	public final Registration<ProductRegistrationDefinition> saveProductRegistration(final Customer customer,
			final ProductRegistrationDefinition registrationDefinition) throws ServiceLayerException {

		ProductRegistration registration = new ProductRegistration();
		registration.setCustomer(customer);
		registration.setRegistrationDefinition(registrationDefinition);
		LOG.debug("Registration complete decision : "+ registrationDefinition.getPageDefinition());
		// If the page definition is empty then the registration is complete
		registration.setCompleted(registrationDefinition.getPageDefinition() == null);
		registration.setActivated(false);
		registration.setDenied(false);
		registration.setAwaitingValidation(false);
		//registrationDao.save(registration);
		//AuditLogger.logEvent(customer, "Save ProductRegistration", "RegistrationDefinitionId:"+registrationDefinition.getId(), AuditLogger.product(registrationDefinition.getProduct()));
		return registration;
	}

	/**
	 * @param registrationDto the registrationDto
	 * @param customer the customer
	 * @param productDefinition the productDefinition
	 * @param locale the locale
	 * @param orignalUrl the original url
	 * @throws ServiceLayerException the exception
	 */
	@Override
	public final void saveProductRegistrationDetails(final RegistrationDto registrationDto, final Customer customer,
			final RegisterableProduct product, final String registrationId) throws ServiceLayerException {
		for (Component component : registrationDto.getComponents()) {
			for (Field field : component.getFields()) {
				Question question = field.getElement().getQuestion();
				String answers = registrationDto.getAnswersForQuestion(field);
				if (question.isProductSpecific()) {
					ProductSpecificAnswer oldAnswer = answerDao.getCustomerProductSpecificAnswerByQuestion(question, customer, product);
					if (oldAnswer == null) {
						if(StringUtils.isNotBlank(answers) || field.getElement().getQuestion().getDescription().equalsIgnoreCase(PageConstant.MARKETING_PREF)) {
							ProductSpecificAnswer newAnswer = new ProductSpecificAnswer(question, customer.getId(), answers, product.getId());
							answerDao.save(newAnswer);
						}
					} else {
						updateAnswer(oldAnswer, answers, question);
					}
				} else {
					Answer oldAnswer = answerDao.getCustomerAnswerByQuestion(question, customer);
					if (oldAnswer == null) {
						if(StringUtils.isNotBlank(answers) || field.getElement().getQuestion().getDescription().equalsIgnoreCase(PageConstant.MARKETING_PREF)) {
							Answer newAnswer = new Answer(question, customer.getId(), answers);
							answerDao.save(newAnswer);
						}
					} else {
						updateAnswer(oldAnswer, answers, question);
					}
				}
			}
		}
		if (registrationId != null ) {
			this.updateLicenseModifiedDate(registrationId);
		}
	}
	
	private final void updateAnswer(final Answer oldAnswer, String answers, final Question question) {
		boolean isMarketPref = question.getDescription().equalsIgnoreCase(PageConstant.MARKETING_PREF) ;
		if(StringUtils.isBlank(answers)) {
			if (isMarketPref) {
				oldAnswer.setAnswerText(PageConstant.MARKETING_PREF_OPT_IN);
				answerDao.update(oldAnswer);  
			} else {
				answerDao.delete(oldAnswer);
			}
		} else {
			if (isMarketPref ) {
				if ( answers.equalsIgnoreCase(Boolean.FALSE.toString())) {
					answers = PageConstant.MARKETING_PREF_OPT_IN ;
				} else {
					answers = PageConstant.MARKETING_PREF_OPT_OUT ;
				}
				oldAnswer.setAnswerText(answers);
			} else {
				oldAnswer.setAnswerText(answers);
			}
			answerDao.update(oldAnswer);        
		}
	}

	@Override
	public final void saveCompleteRegsitration(final RegistrationDto registrationDto, final Customer customer, final String productId)
			throws ServiceLayerException {
		//Registration<ProductRegistrationDefinition> registration = registrationDao.getRegistrationWithProductById(registrationId);
		LOG.debug("Custoemr Id : " + customer.getId() + "Username: " + customer.getUsername());
		Product product = new RegisterableProduct();
		product.setId(productId);
		saveProductRegistrationDetails(registrationDto, customer, (RegisterableProduct)product, null);
		/*registration.setCompleted(true);
		registrationDao.update(registration);*/
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void updateRegistration(final RegistrationDto registrationDto, final Customer customer, final Registration registration)
			throws ServiceLayerException {

		//Registration<ProductRegistrationDefinition> registration = registrationDao.getRegistrationWithProductById(reg.getId());
		RegisterableProduct product = (RegisterableProduct)registration.getRegistrationDefinition().getProduct();
		//RegisterableProduct product = (RegisterableProduct)registration.getRegistrationDefinition().getProduct();
		saveProductRegistrationDetails(registrationDto, customer, product, registration.getId());

		// dirty the registration object so that the updatedDate timestamp will be set by EACEntityInterceptor.
		registration.setUpdatedDate(null);

		//registrationDao.update(registration);
		AuditLogger.logEvent(customer, "Registration Updated", "id:"+registration.getId(), AuditLogger.product(product));
	}
	
	private void sendReedeemEmail(final List<LicenceDto> licences, final RegistrationActivationDto registrationActivationDto) 
			throws ServiceLayerValidationException, Exception {

		ActivationCodeRegistrationDefinition activationCodeRegistrationDefinition = 
				(ActivationCodeRegistrationDefinition) registrationActivationDto.getRegistrationDef();
		//Registration<?> registration = registrationActivationDto.getRegistration();
		Customer customer = registrationActivationDto.getCustomer();
		Locale locale = registrationActivationDto.getLocale();
		//Product eacProduct = productService.getProductById(activationCodeRegistrationDefinition.getProduct().getId());
		//EnforceableProductDto product = productService.getEnforceableProductByErightsId(activationCodeRegistrationDefinition.getProduct().getId());
				//activationCodeRegistrationDefinition.getProduct().getErightsId());
		//Product product = activationCodeRegistrationDefinition.getProduct();
		LicenceDto licenceDto = licences.get(0);
		EnforceableProductDto product = licenceDto.getProducts() ;
		String landingPageUrl = product.getLandingPage();
		String homePageUrl = product.getHomePage();
		String orignalUrl = landingPageUrl != null ? landingPageUrl
				: homePageUrl;
		String actStrategy = product.getActivationStrategy() ;
		TokenDto tokenDto = new TokenDto(licenceDto.getLicenseId(), customer.getId(), 
				orignalUrl, customer.getUsername(), product.getName());
		
		if (actStrategy.equals(ActivationStrategy.SELF.toString())) {
			/*if(activationCodeRegistrationDefinition.isConfirmationEmailEnabled()){*/
				emailSelfLicenceActivationNew(customer, product, locale, tokenDto);
			/*}*/
		} else if (actStrategy.equals(ActivationStrategy.VALIDATED.toString())) {
			/*if(activationCodeRegistrationDefinition.isConfirmationEmailEnabled()){*/
				emailValidatorLicenceActivationNew(customer, product, locale, tokenDto);
			/*}*/
		} else {
			LOG.debug("Instant type");
			if (product.getConfirmationEmailEnabled()) {
				emailLicenceActivatedOrAcceptedNew(customer, locale, EmailType.ACTIVATION_EMAIL, licenceDto, product);
			}
		}
				
	}
	
	@Override
	public void sendRedeemEmailForGroup(final List<LicenceDto> licenses, final Customer customer, final Locale locale ) 
			throws ServiceLayerValidationException, Exception {

		for (LicenceDto licenceDto : licenses) {
			EnforceableProductDto product = productService.getEnforceableProductByErightsId(licenceDto.getProducts().getProductId());
			
					//activationCodeRegistrationDefinition.getProduct().getErightsId());
			//Product product = activationCodeRegistrationDefinition.getProduct();
			String landingPageUrl = product.getLandingPage();
			String homePageUrl = product.getHomePage();
			String orignalUrl = landingPageUrl != null ? landingPageUrl
					: homePageUrl;
			String actStrategy = product.getActivationStrategy() ;
			TokenDto tokenDto = new TokenDto(licenceDto.getLicenseId(), customer.getId(), 
					orignalUrl, customer.getUsername(), product.getName());
			
			if (actStrategy.equals(ActivationStrategy.SELF.toString())) {
				/*if(activationCodeRegistrationDefinition.isConfirmationEmailEnabled()){*/
					emailSelfLicenceActivationNew(customer, product, locale, tokenDto);
				/*}*/
			} else if (actStrategy.equals(ActivationStrategy.VALIDATED.toString())) {
				/*if(activationCodeRegistrationDefinition.isConfirmationEmailEnabled()){*/
					emailValidatorLicenceActivationNew(customer, product, locale, tokenDto);
			/*}*/
			} else {
				LOG.debug("Instant type");
				if (product.getConfirmationEmailEnabled()) {
					emailLicenceActivatedOrAcceptedNew(customer, locale, EmailType.ACTIVATION_EMAIL, licenceDto, product);
				}
			}
		}
				
	}
	@Override
	public void activateLicense(final String userId, final String licenseId, final String productName) 
			throws UserNotFoundException, LicenseNotFoundException, ErightsException, 
			UnsupportedEncodingException, MessagingException {
		
		erightsFacade.activateLicense(userId, licenseId);
		
		Customer customer = convertCustomer(erightsFacade.getUserAccount(userId));
		EnforceableProductDto product = erightsFacade.getProductByName(productName);
		List<LicenceDto> licence = erightsFacade.getLicensesForUser(userId, licenseId);
		Locale locale = customer.getLocale()!=null?customer.getLocale():Locale.getDefault();
		
		if (product.getConfirmationEmailEnabled()) {
			if (product.getActivationStrategy().equals(ActivationStrategy.SELF.toString())) {
				emailLicenceActivatedOrAcceptedNew(customer, locale, null, licence.get(0), product);
			} else if (product.getActivationStrategy().equals(ActivationStrategy.VALIDATED.toString())) {
				emailLicenceActivatedOrAcceptedNew(customer, locale, EmailType.ACCEPTANCE_EMAIL, licence.get(0), product);
			} else {
				emailLicenceActivatedOrAcceptedNew(customer, locale, EmailType.ACCEPTANCE_EMAIL, licence.get(0), product);
			}
		}
	}
	
	@Override
	public void deactivateLicense(final String userId, final String licenseId, final String productName) 
			throws UserNotFoundException, LicenseNotFoundException, ErightsException, 
			UnsupportedEncodingException, MessagingException {
		
		erightsFacade.deactivateLicense(userId, licenseId);
		
		EnforceableProductDto product = erightsFacade.getProductByName(productName);
		if (product.getConfirmationEmailEnabled()) {
			Customer customer = convertCustomer(erightsFacade.getUserAccount(userId));
			emailLicenceDeniedNew(customer, product);
		}
	}

	private void processLicenceActivation(final RegistrationActivationDto registrationActivationDto, EnforceableProductDto product) throws 
	MessagingException, ErightsSessionNotFoundException, ErightsException, ServiceLayerValidationException, Exception{

		Registration<?> registration = registrationActivationDto.getRegistration();
		Customer customer = registrationActivationDto.getCustomer();
		Locale locale = registrationActivationDto.getLocale();
  
		//ProductRegistrationDefinition registrationDefinition = registrationDefinitionDao.getProductRegistrationDefinitionWithLicence(registrationActivationDto.getRegistrationDef().getId());
		//registration = registrationDao.getEntity(registration.getId());
		LicenceTemplate licenceTemplate = licenseDtoToLicenceTemplate(product.getLicenceDetail() );
		String url = null;
		List<String> sessions = null;
		boolean rollingFlag = false;
		if(checkIfRollingLicenceTypewithFirstUse(licenceTemplate)){
			rollingFlag = true;
			LOG.info("Rolling licence type with begin on first use.");
			sessions = erightsFacade.getSessionsByUserId(customer.getId());
			url = getUrlFromProduct(product);
			if(CollectionUtils.isEmpty(sessions) || StringUtils.isBlank(url)){
				LOG.error("One of the parameter for Rolling licence authorize request is not found.");
				throw new ServiceLayerValidationException("Problem Adding licence.");
			}
		}
		//replace addLicense call to addLicenseUserProduct
		String licenceId="";
		if(registrationActivationDto.getIsAdmin())
			licenceId = erightsFacade.addLicense(customer.getId(), licenceTemplate, 
					Arrays.asList(registrationActivationDto.getRegistrationDef().getProduct().getId()), false);
		else{
			licenceId = erightsFacade.addLicenseUserProduct(customer.getId(), 
					registrationActivationDto.getRegistrationDef().getProduct().getId());
			LOG.info("License Id : "+ licenceId);
		}

		//registration.setErightsLicenceId(licenceId);
		/*ActivationStrategy strategy = registration.getRegistrationDefinition().getRegistrationActivation().getActivationStrategy(
		registrationActivationDto.getLocale());*/
		ActivationStrategy strategy = null ;
		String actStrategy = product.getActivationStrategy() ;
		if (actStrategy.equals(ActivationStrategy.SELF.toString())) {
			strategy = ActivationStrategy.SELF ;
		} else if (actStrategy.equals(ActivationStrategy.VALIDATED.toString())) {
			strategy = ActivationStrategy.VALIDATED ;
		} else {
			strategy = ActivationStrategy.INSTANT ;
		}
		
		if (strategy == ActivationStrategy.INSTANT) {
			if(rollingFlag){
				//erightsFacade.authorizeRequest(null, url, customer.getId(), licenceId);
				erightsFacade.authorizeRequest(sessions.get(0), url, customer.getId(), licenceId);
			}else{
				erightsFacade.activateLicense(customer.getId(), licenceId);
			}
			//Delete null when completed
			//updateActivateLinkedProducts(customer, locale, null, registration);
			//registration.setActivated(true);
			//At this point we do have a licence
			//commented below code as Mail Sent from ACES for confimationEmail
			List<LicenceDto> license = erightsFacade.getLicensesForUser(customer.getId(), licenceId);
//			emailLicenceActivatedOrAccepted(customer, locale, registrationDefinition, licenceTemplate, EmailType.ACTIVATION_EMAIL, licence);
			
			if (product.getConfirmationEmailEnabled()) {
				emailLicenceActivatedOrAcceptedNew(customer, locale,  EmailType.ACTIVATION_EMAIL, license.get(0), product);
			}
			registration.setCompleted(true);
			registration.setActivated(true);
		} else {
			String landingPageUrl = product.getLandingPage();
			String homePageUrl = product.getHomePage();
			String orignalUrl = landingPageUrl != null ? landingPageUrl : homePageUrl;
			//updateActivateLinkedProducts(customer, locale, null, registration);
//			TokenDto tokenDto = new TokenDto(Integer.valueOf(licenceId), customer.getErightsId(), registration.getId(), registrationActivationDto.getOriginalUrl());
			TokenDto tokenDto = new TokenDto(licenceId, customer.getId(), 
					orignalUrl, customer.getUsername(), product.getName());
			if (strategy.equals(ActivationStrategy.SELF)) {
				//CR15
				if(!registrationActivationDto.getIsAdmin()){
					LOG.info("It is Admin-------------------");
					emailSelfLicenceActivationNew(customer, product, locale, tokenDto);
					//emailSelfLicenceActivation(customer, registrationActivationDto.getRegistrationDef(), locale, tokenDto);
				}
			} else {
				//CR15
				if(!registrationActivationDto.getIsAdmin()){
					LOG.info("It is Admin-------------------");
					emailValidatorLicenceActivationNew(customer,  product, locale, tokenDto);
					//emailValidatorLicenceActivation(customer, registrationActivationDto.getRegistrationDef(), locale, tokenDto);
				}
			}
			registration.setCompleted(true);
			registration.setActivated(false);
		}
		LOG.info("Active state : "+registration.isActivated());
		//registration.setErightsLicenceId(licenceId);	
		registration.setAwaitingValidation(true);
		registration.setId(licenceId);
	
		//this.registrationDao.save(registration);
		//this.registrationDao.flush();
	}
	
	private void processLicenceActivationCode(final RegistrationActivationDto registrationActivationDto, Product product, List<LicenceDto> licences) throws 
	MessagingException, ErightsSessionNotFoundException, ErightsException, ServiceLayerValidationException, Exception{

		Registration<?> registration = registrationActivationDto.getRegistration();
		Customer customer = registrationActivationDto.getCustomer();
		Locale locale = registrationActivationDto.getLocale();
  
		//ProductRegistrationDefinition registrationDefinition = registrationDefinitionDao.getProductRegistrationDefinitionWithLicence(registrationActivationDto.getRegistrationDef().getId());
		//registration = registrationDao.getEntity(registration.getId());
		LicenceTemplate licenceTemplate = getLicenceTemplate(registration) ; //licenseDtoToLicenceTemplate(registrationActivationDto.getLicenceDetail() );
		String url = null;
		List<String> sessions = null;
		boolean rollingFlag = false;
		if(checkIfRollingLicenceTypewithFirstUse(licenceTemplate)){
			rollingFlag = true;
			LOG.info("Rolling licence type with begin on first use.");
			sessions = erightsFacade.getSessionsByUserId(customer.getId());
//url = getUrlFromProduct(registrationActivationDto.getRegistrationDef().getProduct().getId());
			
			List<EnforceableProductUrlDto> urlList = product.getProductUrls();
			if(CollectionUtils.isNotEmpty(urlList)){
				EnforceableProductUrlDto urlDto = urlList.get(0);
				if(StringUtils.isBlank(urlDto.getExpression())){
					String path,host,protocol;
					int  count=0;
					protocol=StringUtils.deleteWhitespace(urlDto.getProtocol());
					host=StringUtils.deleteWhitespace(urlDto.getHost());
					path=StringUtils.deleteWhitespace(urlDto.getPath());

					if(StringUtils.isBlank(host) || host.equalsIgnoreCase("*") ){
						host ="www.dummy.com";
						count++;
					}
					if(StringUtils.isBlank(protocol) || protocol.equalsIgnoreCase("*")){  
						protocol="http";
						count++;
					}

					if(StringUtils.isBlank(path) || path.equalsIgnoreCase("*") ){
						url=protocol+"://"+host;
						count++;
					}else if(count==2)
						url=path;
					else url=protocol+"://"+host+path;

					//checking if all three are null
					if(count==3)
						url="";
					//removing * from fullAdd
					url=StringUtils.replaceChars(url, "*", "");
				}else{
					String expression = urlDto.getExpression();
					url=StringUtils.substring(expression, (StringUtils.indexOfAny(expression, "/")), (StringUtils.lastIndexOf(expression, "/")+1));
				}
			}
				
			if(CollectionUtils.isEmpty(sessions) || StringUtils.isBlank(url)){
				LOG.error("One of the parameter for Rolling licence authorize request is not found.");
				throw new ServiceLayerValidationException("Problem Adding licence.");
			}
		}
		
		ActivationStrategy strategy = null ;
		String actStrategy = product.getActivationStrategy() ;
		if (actStrategy.equals(ActivationStrategy.SELF.toString())) {
			strategy = ActivationStrategy.SELF ;
		} else if (actStrategy.equals(ActivationStrategy.VALIDATED.toString())) {
			strategy = ActivationStrategy.VALIDATED ;
		} else {
			strategy = ActivationStrategy.INSTANT ;
		}
		String licenceId = licences.get(0).getLicenseId() ;
		if (strategy == ActivationStrategy.INSTANT) {
			if(rollingFlag){
				erightsFacade.authorizeRequest(sessions.get(0), url, customer.getId(), licenceId);
			}/*else{
				erightsFacade.activateLicense(customer.getId(), licenceId);
			}*/
		} 
	}
	private LicenceTemplate licenseDtoToLicenceTemplate(LicenceDto licenseDto) {
		LicenceTemplate licenceTemplate = null;
		if (licenseDto.getLicenceDetail().getLicenceType().toString().equals(LicenceType.CONCURRENT.toString())) {
			ConcurrentLicenceTemplate concurrentLicenceTemplate = new ConcurrentLicenceTemplate() ;
			StandardConcurrentLicenceDetailDto standardDto = (StandardConcurrentLicenceDetailDto)licenseDto.getLicenceDetail() ;
			concurrentLicenceTemplate.setTotalConcurrency(standardDto.getTotalConcurrency());
			concurrentLicenceTemplate.setUserConcurrency(standardDto.getUserConcurrency());
			licenceTemplate = concurrentLicenceTemplate ;
			
		} else if (licenseDto.getLicenceDetail().getLicenceType().toString().equals(LicenceType.ROLLING.toString())) {
			RollingLicenceTemplate rollingLicenceTemplate = new RollingLicenceTemplate() ;
			RollingLicenceDetailDto rollingDto = (RollingLicenceDetailDto)licenseDto.getLicenceDetail() ;
			rollingLicenceTemplate.setBeginOn(rollingDto.getBeginOn());
			rollingLicenceTemplate.setTimePeriod(rollingDto.getTimePeriod());
			rollingLicenceTemplate.setUnitType(rollingDto.getUnitType());
			licenceTemplate = rollingLicenceTemplate ;
		} else if (licenseDto.getLicenceDetail().getLicenceType().toString().equals(LicenceType.USAGE.toString())) {
			UsageLicenceTemplate usageLicenceTemplate = new UsageLicenceTemplate() ; 
			UsageLicenceDetailDto usageDto = (UsageLicenceDetailDto)licenseDto.getLicenceDetail() ;
			usageLicenceTemplate.setAllowedUsages(usageDto.getAllowedUsages());
			licenceTemplate = usageLicenceTemplate ;
		} 
		if (licenseDto.getLicenceDetail().getStartDate() != null ){
			licenceTemplate.setStartDate(licenseDto.getLicenceDetail().getStartDate());
		}
		if (licenseDto.getLicenceDetail().getEndDate() != null ) {
			licenceTemplate.setEndDate(licenseDto.getLicenceDetail().getEndDate());
		}
		return licenceTemplate ;
	}
	private boolean checkIfRollingLicenceTypewithFirstUse(LicenceTemplate licenceTemplate){
		boolean checkFlag = false;
		if(licenceTemplate != null && licenceTemplate.getLicenceType() == LicenceType.ROLLING){
			if(((RollingLicenceTemplate)licenceTemplate).getBeginOn() == RollingBeginOn.FIRST_USE){
				checkFlag = true;
			}
		}
		return checkFlag;
	}

	private String getUrlFromProduct(EnforceableProductDto enforceableProduct) throws ErightsException, Exception{
		String fullUrl = "";
		//EnforceableProductDto enforceableProduct = erightsFacade.getProduct(id);
		if(enforceableProduct != null){
			List<EnforceableProductUrlDto> urlList = enforceableProduct.getUrls();
			if(CollectionUtils.isNotEmpty(urlList)){
				EnforceableProductUrlDto urlDto = urlList.get(0);
				if(StringUtils.isBlank(urlDto.getExpression())){
					String path,host,protocol;
					int  count=0;
					protocol=StringUtils.deleteWhitespace(urlDto.getProtocol());
					host=StringUtils.deleteWhitespace(urlDto.getHost());
					path=StringUtils.deleteWhitespace(urlDto.getPath());

					if(StringUtils.isBlank(host) || host.equalsIgnoreCase("*") ){
						host ="www.dummy.com";
						count++;
					}
					if(StringUtils.isBlank(protocol) || protocol.equalsIgnoreCase("*")){  
						protocol="http";
						count++;
					}

					if(StringUtils.isBlank(path) || path.equalsIgnoreCase("*") ){
						fullUrl=protocol+"://"+host;
						count++;
					}else if(count==2)
						fullUrl=path;
					else fullUrl=protocol+"://"+host+path;

					//checking if all three are null
					if(count==3)
						fullUrl="";
					//removing * from fullAdd
					fullUrl=StringUtils.replaceChars(fullUrl, "*", "");
				}else{
					String expression = urlDto.getExpression();
					fullUrl=StringUtils.substring(expression, (StringUtils.indexOfAny(expression, "/")), (StringUtils.lastIndexOf(expression, "/")+1));
				}
			}
		}
		LOG.info("Product URL : "+fullUrl);
		return fullUrl;
	}

	private final LicenceTemplate getLicenceTemplate(final Registration<? extends ProductRegistrationDefinition> registration) {
		LicenceTemplate licenceTemplate = registration.getRegistrationDefinition().getLicenceTemplate();
		if(registration.getRegistrationDefinition().getRegistrationDefinitionType() == RegistrationDefinitionType.ACTIVATION_CODE_REGISTRATION) {
			ActivationCodeRegistration activationCodeRegistration = ((ActivationCodeRegistration)registration);
			if(activationCodeRegistration.getActivationCode() != null) {
				licenceTemplate = ((ActivationCodeRegistration)registration).getActivationCode().getActivationCodeBatch().getLicenceTemplate();
			}
		}
		return licenceTemplate;
	}

	private void emailValidatorLicenceActivation(final Customer customer, final ProductRegistrationDefinition registrationDefinition, final Locale locale,
			final TokenDto token) throws Exception, UnsupportedEncodingException, MessagingException {

		AccountRegistrationDefinition accountRegistrationDefinition = registrationDefinitionDao.getAccountRegistrationDefinitionByProduct(registrationDefinition.getProduct());
		AccountRegistrationDto accountRegistrationDto = getAccountPageDefinitionByRegistrationDefinition(accountRegistrationDefinition, customer, locale);
		RegistrationDto registrationDto = getProductPageDefinitionByRegistrationDefinition(registrationDefinition, customer, locale);

		String allowlink = EACSettings.getProperty(EACSettings.PRODUCT_REGISTRATION_VALIDATOR_TOKEN_ALLOW_URL) + TokenConverter.encrypt(token);
		String denylink = EACSettings.getProperty(EACSettings.PRODUCT_REGISTRATION_VALIDATOR_TOKEN_DENY_URL) + TokenConverter.encrypt(token);

		//For emails sent to validators, we should use ENGLISH - NOT the customer's locale.
		MessageTextSource resource = getResource(Locale.ENGLISH);

		String productName = registrationDefinition.getProduct().getProductName();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("customer", customer);
		map.put("productname", productName);
		map.put("allowlink", allowlink);
		map.put("denylink", denylink);
		map.put("accountRegDto", accountRegistrationDto);
		map.put("regDto", registrationDto);
		map.put("resource", resource);
		//mantis ticket 0018657
		if(null!=locale.getDisplayName()){
			LOG.info("Inside the if condition of local.getDisplayName");
			map.put("locale", locale.getDisplayName());
		}else{
			LOG.info("Inside the Else condition of local.getDisplayName");
			map.put("locale", "");
		}

		MailCriteria mailCriteria = new MailCriteria();
		mailCriteria.setReplyTo(EmailUtils.getInternetAddressReplyTo());
		String msg = EACSettings.getProperty(EACSettings.PRODUCT_REGISTRATION_VALIDATE_EMAIL_SUBJECT);
		String subject = String.format("%s - %s",productName,msg);
		mailCriteria.setSubject(subject);
		mailCriteria.setFrom(EmailUtils.getInternetAddressFrom());
		LOG.debug(registrationDefinition.getRegistrationActivation().getProperty("validatorEmail", locale));
		
		String validatorEmail = registrationDefinition.getRegistrationActivation().getProperty("validatorEmail", locale);
		map.put("adminName", validatorEmail);
		mailCriteria.addToAddress(new InternetAddress(validatorEmail, "EAC Admin"));
		String text = VelocityUtils.mergeTemplateIntoString(velocityEngine, "com/oup/eac/service/velocity/validatedRegistration.vm", map);
		mailCriteria.setText(text);
		emailService.sendMail(mailCriteria);
	}
	
	private void emailValidatorLicenceActivationNew(final Customer customer, 
			final EnforceableProductDto product, final Locale locale, final TokenDto token) 
			throws Exception, UnsupportedEncodingException, MessagingException {
		Product productForAccountOrProductReg = new RegisterableProduct() ;
		productForAccountOrProductReg.setId(product.getProductId());
		AccountRegistrationDefinition accountRegistrationDefinition = registrationDefinitionDao.getAccountRegistrationDefinitionByProduct(productForAccountOrProductReg);
		ProductRegistrationDefinition productregistrationDefinition =  registrationDefinitionDao.getRegistrationDefinitionByProduct(ProductRegistrationDefinition.class, productForAccountOrProductReg);
		AccountRegistrationDto accountRegistrationDto = null;
		Product regProduct = new RegisterableProduct();
		regProduct.setId(productregistrationDefinition.getProductId());
		productregistrationDefinition.setProduct(regProduct);
		RegistrationDto registrationDto = null;
		if (accountRegistrationDefinition != null) {
			accountRegistrationDto = getAccountPageDefinitionByRegistrationDefinition(accountRegistrationDefinition, customer, locale);
		}
		if (productregistrationDefinition != null) {
			registrationDto = getProductPageDefinitionByRegistrationDefinition(productregistrationDefinition, customer, locale);
		}
		String allowlink = EACSettings.getProperty(EACSettings.EAC_HOST_URL)
				+ EACSettings.getProperty(EACSettings.PRODUCT_REGISTRATION_VALIDATOR_TOKEN_ALLOW_URL_NEW) 
				+ TokenConverter.encrypt(token);
		String denylink = EACSettings.getProperty(EACSettings.EAC_HOST_URL) 
				+ EACSettings.getProperty(EACSettings.PRODUCT_REGISTRATION_VALIDATOR_TOKEN_DENY_URL_NEW) 
				+ TokenConverter.encrypt(token);
		LOG.info("allow link : "+allowlink);
		LOG.info("deny link : "+denylink);
		//For emails sent to validators, we should use ENGLISH - NOT the customer's locale.
		MessageTextSource resource = getResource(Locale.ENGLISH);

		String productName = product.getName();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("customer", customer);
		map.put("productname", productName);
		map.put("allowlink", allowlink);
		map.put("denylink", denylink);
		map.put("accountRegDto", accountRegistrationDto);
		map.put("regDto", registrationDto);
		map.put("resource", resource);
		//mantis ticket 0018657
		if(null!=locale.getDisplayName()){
			LOG.info("Inside the if condition of local.getDisplayName");
			map.put("locale", locale.getDisplayName());
		}else{
			LOG.info("Inside the Else condition of local.getDisplayName");
			map.put("locale", "");
		}

		MailCriteria mailCriteria = new MailCriteria();
		mailCriteria.setReplyTo(EmailUtils.getInternetAddressReplyTo());
		String msg = EACSettings.getProperty(EACSettings.PRODUCT_REGISTRATION_VALIDATE_EMAIL_SUBJECT);
		String subject = String.format("%s - %s",productName,msg);
		mailCriteria.setSubject(subject);
		mailCriteria.setFrom(EmailUtils.getInternetAddressFrom());
		String validatorEmail = product.getValidatorEmail(); 
				//registrationDefinition.getRegistrationActivation().getProperty("validatorEmail", locale);
		map.put("adminName", validatorEmail);
		mailCriteria.addToAddress(new InternetAddress(validatorEmail, "EAC Admin"));
		String text = VelocityUtils.mergeTemplateIntoString(velocityEngine, "com/oup/eac/service/velocity/validatedRegistration.vm", map);
		mailCriteria.setText(text);
		emailService.sendMail(mailCriteria);
	}

	@Override
	public void emailSelfLicenceActivation(final Customer customer, final ProductRegistrationDefinition registrationDefinition, final Locale locale,
			final TokenDto token) throws ServiceLayerException {
		try {
			String registrationlink = EACSettings.getProperty(EACSettings.PRODUCT_REGISTRATION_TOKEN_ALLOW_URL) + TokenConverter.encrypt(token);

			MessageTextSource resource = getResource(locale);

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("username", customer.getUsername());
			map.put("productname", registrationDefinition.getProduct().getProductName());
			map.put("registrationlink", registrationlink);
			map.put("resource", resource);

			String text = VelocityUtils.mergeTemplateIntoString(velocityEngine, "com/oup/eac/service/velocity/productRegistration.vm", map);
			MailCriteria mailCriteria = new MailCriteria();
			mailCriteria.setReplyTo(EmailUtils.getInternetAddressReplyTo());
			mailCriteria.setSubject(getSubjectProductNameRegistration(registrationDefinition));
			mailCriteria.setFrom(EmailUtils.getInternetAddressFrom());
			mailCriteria.addToAddress(customer.getEmailInternetAddress());
			mailCriteria.setText(text);
			emailService.sendMail(mailCriteria);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceLayerException("Problem mailing registration link to user.", e, new Message("error.problem.registering.product",
					"There was a problem registering the product. Please contact the system administrator."));
		}
	}
	
	@Override
	public void emailSelfLicenceActivationNew(final Customer customer, 
			final EnforceableProductDto product, final Locale locale,
			final TokenDto token) throws ServiceLayerException {
		try {
			String registrationlink = EACSettings.getProperty(EACSettings.EAC_HOST_URL) 
					+ EACSettings.getProperty(EACSettings.PRODUCT_REGISTRATION_TOKEN_ALLOW_URL_NEW) 
					+ TokenConverter.encrypt(token);

			MessageTextSource resource = getResource(locale);

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("username", customer.getUsername());
			map.put("productname", product.getName());
			map.put("registrationlink", registrationlink);
			map.put("resource", resource);

			String text = VelocityUtils.mergeTemplateIntoString(velocityEngine, "com/oup/eac/service/velocity/productRegistration.vm", map);
			MailCriteria mailCriteria = new MailCriteria();
			mailCriteria.setReplyTo(EmailUtils.getInternetAddressReplyTo());
			mailCriteria.setSubject(product.getName() + " " + EACSettings.getProperty(EACSettings.PRODUCT_REGISTRATION_EMAIL_SUBJECT));
			mailCriteria.setFrom(EmailUtils.getInternetAddressFrom());
			mailCriteria.addToAddress(customer.getEmailInternetAddress());
			mailCriteria.setText(text);
			emailService.sendMail(mailCriteria);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceLayerException("Problem mailing registration link to user.", e, new Message("error.problem.registering.product",
					"There was a problem registering the product. Please contact the system administrator."));
		}
	}

	@Override
	public ActivationCodeRegistration saveActivationCodeRegistrationWithoutActivationCode(final Customer customer, final ActivationCodeRegistrationDefinition activationCodeRegistrationDefinition) throws ServiceLayerException {
		ActivationCodeRegistration registration = saveActivationCodeRegistration(customer, activationCodeRegistrationDefinition, null);
		return registration;
	}		

	private ActivationCodeRegistration saveActivationCodeRegistration(final Customer customer, final ActivationCodeRegistrationDefinition activationCodeRegistrationDefinition, ActivationCode activationCode) throws ServiceLayerException {

		ActivationCodeRegistration registration = new ActivationCodeRegistration();
		registration.setCustomer(customer);
		registration.setRegistrationDefinition(activationCodeRegistrationDefinition);
		registration.setCreatedDate(new DateTime());
		//If the page definition is empty then the registration is complete
		registration.setCompleted(activationCodeRegistrationDefinition.getPageDefinition() == null);
		registration.setActivated(false);
		registration.setDenied(false);
		registration.setAwaitingValidation(false);
		if(activationCode != null) {
			registration.setActivationCode(activationCode);
		}
		//registrationDao.save(registration);
		AuditLogger.logEvent(customer, "Save ActivationCodeRegistration", " Registration id : "+ registration.getId() +" RegistrationDefinitionId:"+activationCodeRegistrationDefinition.getId(), AuditLogger.product(activationCodeRegistrationDefinition.getProduct()));
		return registration;
	}	

	private ActivationCodeRegistration saveActivationCodeRegistration(final Customer customer, ActivationCode activationCode,
			ActivationCodeBatch activationCodeBatch) throws ServiceLayerException {

		activationCodeService.validateActivationCode(activationCode);
		/*if (activationCode.getAllowedUsage() != null) {
			activationCode.incrementActualUsage();
			activationCodeService.updateActivationCode(activationCode);
		}*/

		ActivationCodeRegistrationDefinition activationCodeRegistrationDefinition = activationCodeBatch.getActivationCodeRegistrationDefinition();

		ActivationCodeRegistration registration = saveActivationCodeRegistration(customer, activationCodeRegistrationDefinition, activationCode);

		return registration;
	}

	@Override
	public final ActivationCodeRegistration saveActivationCodeRegistration(final ActivationCode code, final Customer customer) throws ServiceLayerException, ErightsException, Exception {

		ActivationCode activationCode = activationCodeService.getActivationCodeWithDetails(code.getCode());
		ActivationCodeRegistration registration = createRegistrationRedeemActivationCode(customer, activationCode, null, false); 
				//saveActivationCodeRegistration(customer, activationCode, activationCode.getActivationCodeBatch());
		
		return registration;
	}

	@Override
	public final void saveRegistrationActivation(final RegistrationActivationDto registrationActivationDto, EnforceableProductDto product) throws ServiceLayerException {
		try {
			processLicenceActivation(registrationActivationDto,product);
		} catch (ErightsSessionNotFoundException e) {
			//decrementUsageAndDeleteRegistration(registrationActivationDto);
			throw new ServiceLayerException("Erights problem adding licence.", e, new Message("error.problem.registering.product",
					"There was a problem registering the product. Please contact the system administrator."));
		} catch (ErightsException e) {
			//decrementUsageAndDeleteRegistration(registrationActivationDto);
			throw new ServiceLayerException("Erights problem adding licence.", e, new Message("error.problem.registering.product",
					"There was a problem registering the product. Please contact the system administrator."));
		} catch (UnsupportedEncodingException e) {
			throw new ServiceLayerException("Problem mailing registration link to user.", e, new Message("error.problem.registering.product",
					"There was a problem registering the product. Please contact the system administrator."));
		} catch (MessagingException e) {
			throw new ServiceLayerException("Problem mailing registration link to user.", e, new Message("error.problem.registering.product",
					"There was a problem registering the product. Please contact the system administrator."));
		} catch(ServiceLayerValidationException e){
			throw new ServiceLayerException("Erights problem adding licence.", e, new Message("error.problem.registering.product",
					"There was a problem registering the product. Please contact the system administrator."));
		} catch (ServiceLayerException e) {
			throw e; // So we don't get caught by the generic exception handler below
		} catch (Exception e) {
			throw new ServiceLayerException("Problem adding licence.", e, new Message("error.problem.registering.product",
					"There was a problem registering the product. Please contact the system administrator."));
		}
	}

	private final void saveRegistrationActivationCode(final RegistrationActivationDto registrationActivationDto, Product product, List<LicenceDto> licences) throws ServiceLayerException {
		try {
			processLicenceActivationCode(registrationActivationDto,product,licences);
		} catch (ErightsSessionNotFoundException e) {
			//decrementUsageAndDeleteRegistration(registrationActivationDto);
			throw new ServiceLayerException("Erights problem adding licence.", e, new Message("error.problem.registering.product",
					"There was a problem registering the product. Please contact the system administrator."));
		} catch (ErightsException e) {
			//decrementUsageAndDeleteRegistration(registrationActivationDto);
			throw new ServiceLayerException("Erights problem adding licence.", e, new Message("error.problem.registering.product",
					"There was a problem registering the product. Please contact the system administrator."));
		} catch (UnsupportedEncodingException e) {
			throw new ServiceLayerException("Problem mailing registration link to user.", e, new Message("error.problem.registering.product",
					"There was a problem registering the product. Please contact the system administrator."));
		} catch (MessagingException e) {
			throw new ServiceLayerException("Problem mailing registration link to user.", e, new Message("error.problem.registering.product",
					"There was a problem registering the product. Please contact the system administrator."));
		} catch(ServiceLayerValidationException e){
			throw new ServiceLayerException("Erights problem adding licence.", e, new Message("error.problem.registering.product",
					"There was a problem registering the product. Please contact the system administrator."));
		} catch (ServiceLayerException e) {
			throw e; // So we don't get caught by the generic exception handler below
		} catch (Exception e) {
			throw new ServiceLayerException("Problem adding licence.", e, new Message("error.problem.registering.product",
					"There was a problem registering the product. Please contact the system administrator."));
		}
	}
	private void emailLicenceActivatedOrAccepted(final Customer customer, final Locale locale,
			final ProductRegistrationDefinition productRegistrationDefinition, final LicenceTemplate licenceTemplate, final EmailType emailType, LicenceDto licence)
					throws UnsupportedEncodingException, MessagingException {
		if (productRegistrationDefinition.isConfirmationEmailEnabled()) {
			String text = prepareActivationOrAcceptanceEmails(customer, productRegistrationDefinition, licenceTemplate, locale, emailType, licence);
			emailLicenceInfo(customer, productRegistrationDefinition, text);
		}
	}

	private void emailLicenceActivatedOrAcceptedNew(final Customer customer, final Locale locale,
			final EmailType emailType, final LicenceDto licence, final EnforceableProductDto product)
					throws UnsupportedEncodingException, MessagingException {
		LicenceDescriptionGenerator descriptionGenerator = licenceDescriptionGeneratorSource
				.getLicenceDescriptionGenerator(locale, customer.getTimeZone(), false);
		String licenceDescription = descriptionGenerator.getLicenceDescription(licence, new DateTime());

		MessageTextSource resource = getResource(locale);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("username", customer.getUsername());
		map.put("productname", product.getName());
		map.put("resource", resource);
		map.put("productdivision", getDivisionTypeByDivisionOrId(product.getDivision(),product.getDivisionId()));

		if (StringUtils.isNotBlank(licenceDescription)) {
			map.put("licenceDescription", licenceDescription.trim());
		}

		String result = "";
		String email = "";
		String vmType = "";
		
		if (emailType!=null) {
			if (emailType == EmailType.ACTIVATION_EMAIL) { 
				// Instant Activation
				vmType = LICENCE_ACTIVATED_VM;				
				email = product.getAdminEmail();
			} else { 
				//Validated Activation
				vmType = LICENCE_ACCEPTED_VM;
				email = product.getValidatorEmail();
			}
			if (StringUtils.isNotBlank(email)) {
				map.put("email", email);
			}
		} else {  
			// Self
			vmType = LICENCE_ACCEPTED_VM;
		}
		
		result = VelocityUtils.mergeTemplateIntoString(velocityEngine, vmType, map);
		MailCriteria mailCriteria = new MailCriteria();
		mailCriteria.setReplyTo(EmailUtils.getInternetAddressReplyTo());
		mailCriteria.setSubject(product.getName() + " " + EACSettings.getProperty(EACSettings.PRODUCT_REGISTRATION_EMAIL_SUBJECT));
		mailCriteria.setFrom(EmailUtils.getInternetAddressFrom());
		mailCriteria.addToAddress(customer.getEmailInternetAddress());
		mailCriteria.setText(result);
		emailService.sendMail(mailCriteria);
	}
	
	@Override
	public String prepareActivationOrAcceptanceEmails(final Customer customer, final ProductRegistrationDefinition productRegistrationDefinition,
			final LicenceTemplate licenceTemplate, final Locale locale, final EmailType emailType, final LicenceDto licence) {

		LicenceDescriptionGenerator descriptionGenerator = licenceDescriptionGeneratorSource.getLicenceDescriptionGenerator(locale, customer.getTimeZone(), false);
		String licenceDescription = descriptionGenerator.getLicenceDescription(licence, new DateTime());

		if (emailType == EmailType.ACTIVATION_EMAIL) {
			return prepareEmail(customer, locale, productRegistrationDefinition, licenceTemplate, productRegistrationDefinition.getProduct().getEmail(),
					LICENCE_ACTIVATED_VM, licenceDescription);
		}
		if (productRegistrationDefinition.getRegistrationActivation().getActivationStrategy() == VALIDATED) {
			String email = productRegistrationDefinition.getRegistrationActivation().getProperty("validatorEmail", locale);
			return prepareEmail(customer, locale, productRegistrationDefinition, licenceTemplate, email, LICENCE_ACCEPTED_VM, licenceDescription);
		}
		return prepareEmail(customer, locale, productRegistrationDefinition, licenceTemplate, null, LICENCE_ACCEPTED_VM, licenceDescription);
	}

	private void emailLicenceInfo(final Customer customer, final ProductRegistrationDefinition productRegistrationDefinition, final String text)
			throws UnsupportedEncodingException, MessagingException {
		MailCriteria mailCriteria = new MailCriteria();
		mailCriteria.setReplyTo(EmailUtils.getInternetAddressReplyTo());
		mailCriteria.setSubject(getSubjectProductNameActivation(productRegistrationDefinition));
		mailCriteria.setFrom(EmailUtils.getInternetAddressFrom());
		mailCriteria.addToAddress(customer.getEmailInternetAddress());
		mailCriteria.setText(text);
		emailService.sendMail(mailCriteria);
	}

	private void emailLicenceDenied(final Customer customer, final ProductRegistrationDefinition productRegistrationDefinition)
			throws UnsupportedEncodingException, MessagingException {
		Locale locale = customer.getLocale();
		MessageTextSource resource = getResource(locale);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("username", customer.getUsername());
		map.put("productname", productRegistrationDefinition.getProduct().getProductName());
		map.put("validatoremail", productRegistrationDefinition.getRegistrationActivation().getProperty("validatorEmail", locale));
		map.put("resource", resource);

		String text = VelocityUtils.mergeTemplateIntoString(velocityEngine, "com/oup/eac/service/velocity/licenceDenied.vm", map);
		MailCriteria mailCriteria = new MailCriteria();
		mailCriteria.setReplyTo(EmailUtils.getInternetAddressReplyTo());
		mailCriteria.setSubject(productRegistrationDefinition.getProduct().getProductName() + " " + EACSettings.getProperty(EACSettings.PRODUCT_REGISTRATION_DENIED_EMAIL_SUBJECT));
		mailCriteria.setFrom(EmailUtils.getInternetAddressFrom());
		mailCriteria.addToAddress(customer.getEmailInternetAddress());
		mailCriteria.setText(text);
		emailService.sendMail(mailCriteria);
	}
	
	private void emailLicenceDeniedNew(final Customer customer, final EnforceableProductDto product)
			throws UnsupportedEncodingException, MessagingException {
		Locale locale = customer.getLocale();
		MessageTextSource resource = getResource(locale);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("username", customer.getUsername());
		map.put("productname", product.getName());
		map.put("validatoremail", product.getValidatorEmail());
		map.put("resource", resource);

		String text = VelocityUtils.mergeTemplateIntoString(velocityEngine, "com/oup/eac/service/velocity/licenceDenied.vm", map);
		MailCriteria mailCriteria = new MailCriteria();
		mailCriteria.setReplyTo(EmailUtils.getInternetAddressReplyTo());
		mailCriteria.setSubject(product.getName() + " " + EACSettings.getProperty(EACSettings.PRODUCT_REGISTRATION_DENIED_EMAIL_SUBJECT));
		mailCriteria.setFrom(EmailUtils.getInternetAddressFrom());
		mailCriteria.addToAddress(customer.getEmailInternetAddress());
		mailCriteria.setText(text);
		emailService.sendMail(mailCriteria);
	}

	private String prepareEmail(final Customer customer, final Locale locale, final ProductRegistrationDefinition productRegistrationDefinition,
			final LicenceTemplate licenceTemplate, final String email, final String emailTemplate, final String licenceDescription) {
		MessageTextSource resource = getResource(locale);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("username", customer.getUsername());
		map.put("productname", productRegistrationDefinition.getProduct().getProductName());
		map.put("resource", resource);
		map.put("productdivision", getDivisionTypeByDivisionOrId(productRegistrationDefinition.getProduct().getDivision(),0));

		if (StringUtils.isNotBlank(licenceDescription)) {
			map.put("licenceDescription", licenceDescription.trim());
		}

		if (StringUtils.isNotBlank(email)) {
			map.put("email", email);
		}
		String result = VelocityUtils.mergeTemplateIntoString(velocityEngine, emailTemplate, map);
		return result;
	}

	/**
	 * Update registration status with the token string.
	 * 
	 * @param tokenString
	 *            the token string
	 * @param sendEmail
	 *            whether to send an email to the user confirming the registration has been allowed.
	 * @throws Exception
	 *             the exception
	 * @return the updated registration status
	 * @throws LicenceActivatedAlreadyException
	 *             , Exception
	 */
	@Override
	public final Map<String, Object> updateAllowRegistrationFromToken(final String tokenString, final boolean sendEmail) throws Exception {
		TokenDto token = (TokenDto) TokenConverter.decrypt(tokenString, new TokenDto());
		List<LicenceDto> licenses = erightsFacade.getLicensesForUser(token.getUserId(), token.getLicenseId()); 
				//registrationDao.getRegistrationByIdInitialised(token.getRegistrationId());
		Registration registration = new ProductRegistration();
		
		CustomerDto customerDto = erightsFacade.getUserAccount(token.getUserId());
		//registration.setErightsLicenceId(licenses.get(0).getErightsId());
		registration.setCustomer(convertCustomer(customerDto));
		registration.setCompleted(licenses.get(0).isCompleted());
		registration.setId(token.getLicenseId().toString());
		ProductRegistrationDefinition regdef = new ProductRegistrationDefinition();
		//regdef.setProduct(convertEnforceableProductToProduct(licenses.get(0).getProducts()));
		//regdef.setConfirmationEmailEnabled(licenses.get(0).getProducts().getConfirmationEmailEnabled());
		EnforceableProductDto product = productService.getEnforceableProductByErightsId(
				licenses.get(0).getProducts().getProductId());
		regdef.setProduct(convertEnforceableProductToProduct(product));
		registration.setRegistrationDefinition(regdef);
		LOG.debug("Registration definition"+ registration.getRegistrationDefinition());
		LOG.debug("Registration definition Product"+ registration.getRegistrationDefinition().getProduct());
		LOG.debug("Registration definition Product Id"+ registration.getRegistrationDefinition().getProduct().getErightsId());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("registration", registration);
		map.put("originalUrl", token.getOrignalUrl());
		if (licenses.get(0).isActive()) {
			map.put("activated", Boolean.TRUE);
			return map;
		}
		map.put("activated", Boolean.FALSE);
		updateAllowRegistration(registration, product.getConfirmationEmailEnabled(), product);
		customerDto.setEmailVerificationState(EmailVerificationState.VERIFIED);
		
		customerDto.mergeCustomerChanges(convertCustomer(customerDto), false);
        //customerDao.update(customer);
        
        erightsFacade.updateUserAccount(customerDto);
		//customerService.updateCustomer(convertCustomer(customerDto), false);
		return map;
	}

	@Override
	public void updateAllowRegistration(final Registration<? extends ProductRegistrationDefinition> registration, final boolean sendEmail, EnforceableProductDto enfProduct) throws Exception {
		registration.setActivated(true);
		registration.setDenied(false);
		//registrationDao.update(registration);
		Customer customer = registration.getCustomer();
		//customer.setEmailVerificationState(EmailVerificationState.VERIFIED);
		//customerDao.update(customer);

		//Replace activateLicense with adminActivateLicense W/S
		//erightsFacade.activateLicense(customer.getErightsId(), registration.getErightsLicenceId());
		erightsFacade.adminActivateLicense(customer.getId(), registration.getId(),sendEmail);
		//LicenceTemplate licenceTemplate = getLicenceTemplate(registration);
		//ProductRegistrationDefinition productRegistrationDefinition = registration.getRegistrationDefinition();
		Locale locale = customer.getLocale();
		//updateActivateLinkedProducts(registration.getCustomer(), locale, productRegistrationDefinition, registration);
		
		if (sendEmail) {
			//At this point we do have a licence
			LOG.debug("registration.getRegistrationDefinition().getProduct().getErightsId()"+registration.getRegistrationDefinition().getProduct().getErightsId());
			LicenceDto licence = getLicenceDto(registration, customer);
			if(enfProduct == null)
				enfProduct = productService.getEnforceableProductByErightsId(
					registration.getRegistrationDefinition().getProduct().getId());
			emailLicenceActivatedOrAcceptedNew(customer, locale, EmailType.ACCEPTANCE_EMAIL, licence, enfProduct);
			//emailLicenceActivatedOrAccepted(customer, locale, registration.getRegistrationDefinition(), licenceTemplate, EmailType.ACCEPTANCE_EMAIL, licence);
		}
	}

	/**
	 * Update registration status with the token string.
	 * 
	 * @param tokenString
	 *            the token string
	 * @param sendEmail
	 *            whether to send an email to the user confirming that the registration has been denied.
	 * @throws Exception
	 *             the exception
	 * @return the updated registration status
	 * @throws LicenceActivatedAlreadyException
	 *             , Exception
	 */
	@Override
	public final Map<String, String> updateDenyRegistrationFromToken(final String tokenString, final boolean sendEmail) throws Exception {
		TokenDto token = (TokenDto) TokenConverter.decrypt(tokenString, new TokenDto());
		//Registration<? extends ProductRegistrationDefinition> registration = registrationDao.getRegistrationByIdInitialised(token.getRegistrationId());

		List<LicenceDto> licenses = erightsFacade.getLicensesForUser(token.getUserId(), token.getLicenseId()); 
		//registrationDao.getRegistrationByIdInitialised(token.getRegistrationId());
		Registration registration = new ProductRegistration();
		CustomerDto customerDto = erightsFacade.getUserAccount(token.getUserId());
		//registration.setErightsLicenceId(licenses.get(0).getLicenseId());
		registration.setCustomer(convertCustomer(customerDto));
		registration.setCompleted(licenses.get(0).isCompleted());
		registration.setId(token.getLicenseId().toString());
		ProductRegistrationDefinition regdef = new ProductRegistrationDefinition();
		/*regdef.setProduct(convertEnforceableProductToProduct(licenses.get(0).getProducts()));
		regdef.setConfirmationEmailEnabled(licenses.get(0).getProducts().getConfirmationEmailEnabled());*/
		EnforceableProductDto product = productService.getEnforceableProductByErightsId(
				licenses.get(0).getProducts().getProductId());
		regdef.setProduct(convertEnforceableProductToProduct(product));
		registration.setRegistrationDefinition(regdef);
		
		if (registration == null) {
			// Better than an anonymous NullPointerException!
			throw new RuntimeException("Failed to lookup registration for tokenString : " + tokenString);
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("orignalUrl", token.getOrignalUrl());
		map.put("username", registration.getCustomer().getUsername());
		map.put("product", registration.getRegistrationDefinition().getProduct().getProductName());

		updateDenyRegistration(registration, product.getConfirmationEmailEnabled(), product);
		return map;
	}

	@Override
	public void updateDenyRegistration(Registration<? extends ProductRegistrationDefinition> registration, final boolean sendEmail, EnforceableProductDto enfProduct) throws Exception {
		registration.setActivated(false);
		registration.setDenied(true);
		//registrationDao.update(registration);
		Customer customer = registration.getCustomer();
		erightsFacade.deactivateLicense(customer.getId(), registration.getId());
		//updateDeactivateLinkedProducts(registration, locale);
		if (sendEmail) {
			//emailLicenceDenied(customer, registration.getRegistrationDefinition());
			if(enfProduct == null)
				enfProduct = productService.getEnforceableProductByErightsId(
					registration.getRegistrationDefinition().getProduct().getId());
			
			emailLicenceDeniedNew(customer, enfProduct);
		}
	}

	private final void updateActivateLinkedProducts(final Customer customer, final Locale locale, final ProductRegistrationDefinition productRegistrationDefinition,
			final Registration<? extends ProductRegistrationDefinition> registration) throws ErightsException {
		RegisterableProduct registerableProduct = (RegisterableProduct)productRegistrationDefinition.getProduct();
		//product de-duplication
		/*for (LinkedProduct linkedProduct : registerableProduct.getLinkedProducts()) {
			if (isAppropriateForActivationOrDeactivation(linkedProduct, registration, locale)) {
				Integer licenceId = erightsFacade.addLicense(customer.getErightsId(), getLicenceTemplate(registration), Arrays.asList(linkedProduct.getLinkedProduct().getErightsId()),true);				//registerableProduct.getErightsId()),linkedProduct.getLinkedProduct().getErightsId())
				addLinkedRegistration(registration, linkedProduct, licenceId);
			}
		}*/
	}
	//unused method
	
	/*private void updateDeactivateLinkedProducts(final Registration<? extends ProductRegistrationDefinition> registration, final Locale locale)
			throws ErightsException {
		Customer customer = registration.getCustomer();
		for (Iterator<LinkedRegistration> iter = registration.getLinkedRegistrations().iterator(); iter.hasNext();) {
			LinkedRegistration linkedRegistration = iter.next();
			if (isAppropriateForActivationOrDeactivation(linkedRegistration.getLinkedProduct(), registration, locale)) {
				erightsFacade.removeLicence(customer.getId(), linkedRegistration.getId());
				linkedRegistrationDao.delete(linkedRegistration);
				iter.remove();
			}
		}
	}*/

/*	private boolean isAppropriateForActivationOrDeactivation(final LinkedProduct linkedProduct,
			final Registration<? extends ProductRegistrationDefinition> registration, final Locale locale) {
		ProductRegistrationDefinition productRegistrationDefinition = registration.getRegistrationDefinition();
		return productRegistrationDefinition.getRegistrationActivation().getActivationStrategy(locale) == ActivationStrategy.INSTANT
				|| (!registration.isAwaitingValidation() && linkedProduct.getActivationMethod() == ActivationMethod.PRE_PARENT)
				|| (registration.isAwaitingValidation() && linkedProduct.getActivationMethod() == ActivationMethod.POST_PARENT);
	}*/

	private LinkedRegistration addLinkedRegistration(Registration<? extends ProductRegistrationDefinition> registration, LinkedProduct linkedProduct, Integer eRightsLicenceId) {
		LinkedRegistration linkedReg = new LinkedRegistration();
		linkedReg.setErightsLicenceId(eRightsLicenceId);
		linkedReg.setRegistration(registration);
		linkedReg.setLinkedProduct(linkedProduct);
		this.linkedRegistrationDao.save(linkedReg);// ensure we save the linked registration to the db at the end of the transaction
		registration.getLinkedRegistrations().add(linkedReg);// registration is a detached object at this point
		return linkedReg;
	}

	/**
	 * Gets the message text source.
	 * 
	 * @param locale
	 *            the locale
	 * @return the resource
	 */
	private MessageTextSource getResource(final Locale locale) {
		MessageTextSource result = new MessageTextSource(this.messageSource, locale);
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final CustomerRegistrationsDto getEntitlementsForCustomerRegistrations(final Customer customer, final String licenceId, final boolean regInfoReq) throws ServiceLayerException {
		//List<Registration<? extends ProductRegistrationDefinition>> registrations = getProductRegistrationsForCustomer(customer);
		List<LicenceDto> licences;
		EnforceableProductDto productDto ;
		
		if (licenceId == null) {
			licences = licenceService.getLicencesForCustomer(customer);
		} else {
			licences = licenceService.getLicencesForCustomer(customer, licenceId);
		}
		List<Registration<? extends ProductRegistrationDefinition>> registrations = new ArrayList<Registration<? extends ProductRegistrationDefinition>>();

		Set<LinkedRegistration> linkedRegistrations = setLinkedRegistration(licences);
	
		for(LicenceDto dto : licences){
			//List<ProductRegistration> productRegistrations = new ArrayList<ProductRegistration>();
			Registration registration = null;
			if(dto.getActivationCode()!=null){
				registration = new ActivationCodeRegistration();
			}else{
				registration = new ProductRegistration();
			}
			ProductRegistrationDefinition productregistrationDefinition = new ProductRegistrationDefinition();
			Product product = convertEnforceableProductToProduct(dto.getProducts());
			ProductPageDefinition productPageDefinition = null;
			if(regInfoReq){
				productregistrationDefinition =  registrationDefinitionDao.getRegistrationDefinitionByProduct(ProductRegistrationDefinition.class, product);
				if (productregistrationDefinition != null && productregistrationDefinition.getPageDefinition() != null) {
					productPageDefinition = pageDefinitionService.getFullyFetchedProductPageDefinitionById(productregistrationDefinition.getPageDefinition().getId());
				}
			}
			RegistrationActivation registrationActivation =null;
			if(dto.getActivationCode()!=null){
				ActivationCodeRegistrationDefinition actRegistrationDefinition = new ActivationCodeRegistrationDefinition();
				actRegistrationDefinition.setProduct(product);
				if (productPageDefinition != null) {
					actRegistrationDefinition.setPageDefinition(productPageDefinition);
				}
				if(licenceId!=null){
					try {
						productDto = erightsFacade.getProduct(dto.getProducts().getProductId());
					} catch (ErightsException e) {
						throw new ServiceLayerException(e.getMessage());
					}
					if(productDto.getActivationStrategy().toString().equals(RegistrationActivation.ActivationStrategy.SELF.toString())){
						registrationActivation = new SelfRegistrationActivation();
						actRegistrationDefinition.setRegistrationActivation(registrationActivation);
					}else if(productDto.getActivationStrategy().toString().equals(RegistrationActivation.ActivationStrategy.VALIDATED.toString())){
						registrationActivation = new ValidatedRegistrationActivation();
						actRegistrationDefinition.setRegistrationActivation(registrationActivation);
					}
				}
				registration.setRegistrationDefinition(actRegistrationDefinition);
			}else{
				ProductRegistrationDefinition registrationDefinition = new ProductRegistrationDefinition();
				registrationDefinition.setProduct(product);
				if (productPageDefinition != null) {
					registrationDefinition.setPageDefinition(productPageDefinition);
				}
				if(licenceId!=null){
					try {
						productDto = erightsFacade.getProduct(dto.getProducts().getProductId());
					} catch (ErightsException e) {
						throw new ServiceLayerException(e.getMessage());
					}
					if(productDto.getActivationStrategy().toString().equals(RegistrationActivation.ActivationStrategy.SELF.toString())){
						registrationActivation = new SelfRegistrationActivation();
						registrationDefinition.setRegistrationActivation(registrationActivation);
					}else if(productDto.getActivationStrategy().toString().equals(RegistrationActivation.ActivationStrategy.VALIDATED.toString())){
						registrationActivation = new ValidatedRegistrationActivation();
						registrationDefinition.setRegistrationActivation(registrationActivation);
					}
				}
				registration.setRegistrationDefinition(registrationDefinition);
			}
			registration.setExpired(dto.isExpired());
			registration.setCompleted(dto.isCompleted());
			registration.setActivated(dto.isActive());
			registration.setAwaitingValidation(dto.isAwaitingValidation());
			registration.setDenied(dto.isDenied());
			//e.setErightsLicenceId(dto.getErightsId());
			registration.setCreatedDate(dto.getCreatedDate());
			registration.setUpdatedDate(dto.getUpdatedDate());
			registration.setId(dto.getLicenseId());
			if(linkedRegistrations!=null && !linkedRegistrations.isEmpty()){
				registration.getLinkedRegistrations().addAll(linkedRegistrations);
			}
			registration.setCustomer(customer);
			if(dto.getActivationCode()!=null){
				ActivationCodeRegistration acReg = (ActivationCodeRegistration)registration;
				ActivationCode activationCode = new ActivationCode();
				activationCode.setCode(dto.getActivationCode());
				acReg.setActivationCode(activationCode);
				registration = (Registration)acReg;
			}
			registrations.add(registration);
		}
		
		CustomerRegistrationsDto result = new CustomerRegistrationsDto(customer,registrations, licences);
		return result;
	}


	/*private  Set<LinkedRegistration> setLinkedRegistration(List<LicenceDto> licences) {
		Set<LinkedRegistration> linkedRegistrations=null;
		for(LicenceDto dto : licences){
			LinkedRegistration linkedRegistration =null;
			//if(dto.LinkedProductIds()!=null){
			if(dto.getProducts().getLinkedProducts()!=null && !dto.getProducts().getLinkedProducts().isEmpty()){	
				linkedRegistration = new LinkedRegistration();
				for(LinkedProductNew a : dto.getProducts().getLinkedProducts()){
					linkedRegistration.setId(dto.getLicenseId());
					LinkedProduct product = new LinkedProduct();
					product.setId("2eb00dc0-df35-11e7-bd8b-017d40f44f44");
							//a.getProductId());
					product.setProductName("Test product");
							//a.getName());
					linkedRegistration.setLinkedProduct(product);
				}
				linkedRegistrations = new HashSet<LinkedRegistration>();
				linkedRegistrations.add(linkedRegistration);

			}
		}
		return linkedRegistrations;

	}*/
	
	private  Set<LinkedRegistration> setLinkedRegistration(List<LicenceDto> licences) {
		Set<LinkedRegistration> linkedRegistrations= new HashSet<LinkedRegistration>();
		for(LicenceDto dto : licences){
			//if(dto.LinkedProductIds()!=null){
			if(dto.getProducts()!=null && dto.getProducts().getLinkedProducts()!=null && !dto.getProducts().getLinkedProducts().isEmpty()){	
				for(LinkedProductNew a : dto.getProducts().getLinkedProducts()){
					LinkedRegistration linkedRegistration =new LinkedRegistration();;
					linkedRegistration.setId(a.getProductId());
					LinkedProduct product = new LinkedProduct();
					product.setId(a.getProductId());
					product.setProductName(a.getName());
					linkedRegistration.setLinkedProduct(product);
					linkedRegistrations.add(linkedRegistration);
				}

			}
		}
		return linkedRegistrations;

	}

	/**
	 * Calls the eRightsFacade to get the licences for a user product with some extra exception processing.
	 * 
	 * @param customerErightsId
	 *            the erights customer id
	 * @param productErightsId
	 *            the erights product id
	 * @return the list of licence information from atypon
	 * @throws ServiceLayerException
	 *             if there's a problem
	 */
	private List<LicenceDto> getLicensesForUserProduct(String customerErightsId, String productErightsId) throws ServiceLayerException {
		try {
			List<LicenceDto> licences = erightsFacade.getLicensesForUserProduct(customerErightsId, productErightsId);
			if (licences == null) {
				licences = new ArrayList<LicenceDto>();
			}
			if (licences.isEmpty()) {
				String msg = String.format("Unexepected : no licences from erights for user : %d and product : %d", customerErightsId, productErightsId);
				LOG.warn(msg);
			}
			return licences;
		} catch (ErightsException ex) {
			String msg = String.format("problem getting licences from erights for user : %d and product %d", customerErightsId, productErightsId);
			LOG.error(msg);
			throw new ServiceLayerException(msg, ex);
		}
	}

	/**
	 * Gets the licences associated with a registration from atypon web service. Used to get the licences for a
	 * registration created via activation code redemption.
	 * 
	 * @param registration
	 *            - the registration holding the products, linked registrations and external identfiers.
	 * @param customer
	 *            the customer object
	 * @param products
	 *            the list of products associated with the registration
	 * @return the licences associated with the registration's products.
	 * @throws ServiceLayerException
	 */
	private List<LicenceDto> getLicencesForUserProducts(Registration<? extends ProductRegistrationDefinition> registration, Customer customer,
			List<Product> products) throws ServiceLayerException {
		Map<String, String> productIdToLicenceIdMap = new HashMap<String, String>();
		if (registration.getId() != null) {
			productIdToLicenceIdMap.put(registration.getRegistrationDefinition().getProduct().getId(), registration.getId());
		}
		for (LinkedRegistration linked : registration.getLinkedRegistrations()) {
			productIdToLicenceIdMap.put(linked.getLinkedProduct().getId(), linked.getId());
		}

		List<LicenceDto> result = new ArrayList<LicenceDto>();

		String customerErightsId = customer.getId();

		if (products == null) {
			return result;
		}
		for (Product p : products) {
			String productErightsId = p.getId();
			List<LicenceDto> prodLicences = getLicensesForUserProduct(customerErightsId, productErightsId);
			LicenceDto prodLicence = null;
			String expectedLicenceId = productIdToLicenceIdMap.get(productErightsId);
			if (expectedLicenceId != null) {
				Iterator<LicenceDto> iter = prodLicences.iterator();
				while (iter.hasNext() && prodLicence == null) {
					LicenceDto lic = iter.next();
					if (expectedLicenceId.equals(lic.getLicenseId())) {
						prodLicence = lic;
					}
				}
			}
			if (prodLicence != null) {
				result.add(prodLicence); // we knew what to look for and we found it.
			} else {
				if (expectedLicenceId != null) {
					String msg = String.format("Licences from eRights do not contain expected licenceId[%d] for customerId[%d] and productId[%d]", expectedLicenceId, customerErightsId, productErightsId);
					LOG.error(msg);
				}
				// If we have got here and we haven't been able to find the specific licence
				// so Add all the licences for this product - this will happen if we haven't stored the licences ids in
				// the eac database.
				result.addAll(prodLicences);
			}
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final CustomerRegistrationsDto getEntitlementsForCustomerRegistration(final Customer customer,
			Registration<? extends ProductRegistrationDefinition> registration) throws ServiceLayerException {

		// we populate the registration with product entitlement information, linked products, linked registrations
		Registration<? extends ProductRegistrationDefinition> registration1 = this.registrationDao.getRegistrationPopulatedWithEntitlements(registration);

		@SuppressWarnings("unchecked")
		List<Registration<? extends ProductRegistrationDefinition>> registrations = Arrays.<Registration<? extends ProductRegistrationDefinition>> asList(registration1);
		List<Product> products = getProductsInRegistrations(registrations);

		List<LicenceDto> licences = getLicencesForUserProducts(registration1, customer, products);

		CustomerRegistrationsDto result = new CustomerRegistrationsDto(customer, registrations, licences);
		return result;
	}

	private List<LicenceDto> getLicencesForUserProductsForEacGroup(List<Registration<? extends ProductRegistrationDefinition>> registrations, Customer customer,
			List<Product> products) throws ServiceLayerException {
		Map<String, String> productIdToLicenceIdMap = new HashMap<String, String>();
		for (Registration<? extends ProductRegistrationDefinition> registration : registrations) {
			if (registration.getId() != null) {
				productIdToLicenceIdMap.put(registration.getRegistrationDefinition().getProduct().getId(), registration.getId());
			}
			for (LinkedRegistration linked : registration.getLinkedRegistrations()) {
				productIdToLicenceIdMap.put(linked.getLinkedProduct().getId(), linked.getId());
			}
		}

		List<LicenceDto> result = new ArrayList<LicenceDto>();

		String customerErightsId = customer.getId();

		if (products == null) {
			return result;
		}
		for (Product p : products) {
			String productErightsId = p.getId();
			List<LicenceDto> prodLicences = getLicensesForUserProduct(customerErightsId, productErightsId);
			LicenceDto prodLicence = null;
			String expectedLicenceId = productIdToLicenceIdMap.get(productErightsId);
			if (expectedLicenceId != null) {
				Iterator<LicenceDto> iter = prodLicences.iterator();
				while (iter.hasNext() && prodLicence == null) {
					LicenceDto lic = iter.next();
					if (expectedLicenceId.equals(lic.getLicenseId())) {
						prodLicence = lic;
					}
				}
			}
			if (prodLicence != null) {
				result.add(prodLicence); // we knew what to look for and we found it.
			} else {
				if (expectedLicenceId != null) {
					String msg = String.format("Licences from eRights do not contain expected licenceId[%d] for customerId[%d] and productId[%d]", expectedLicenceId, customerErightsId, productErightsId);
					LOG.error(msg);
				}
				// If we have got here and we haven't been able to find the specific licence
				// so Add all the licences for this product - this will happen if we haven't stored the licences ids in
				// the eac database.
				result.addAll(prodLicences);
			}
		}
		return result;
	}

	/**
	 * Gets the product registrations for customer.
	 * 
	 * @param customer
	 *            the customer
	 * @return the product registrations for customer
	 * @throws ServiceLayerException
	 *             the service layer exception
	 */
	public List<Registration<? extends ProductRegistrationDefinition>> getProductRegistrationsForCustomer(final Customer customer)
			throws ServiceLayerException {
		
		List<LicenceDto> licences = licenceService.getLicencesForCustomer(customer);
		List<Registration<? extends ProductRegistrationDefinition>> registrations = new ArrayList<Registration<? extends ProductRegistrationDefinition>>();
		
		Set<LinkedRegistration> linkedRegistrations = setLinkedRegistration(licences);
	
		for(LicenceDto dto : licences){
			//List<ProductRegistration> productRegistrations = new ArrayList<ProductRegistration>();
			Registration registration = new ProductRegistration();
			ProductRegistrationDefinition registrationDefinition = new ProductRegistrationDefinition();
			Product product = convertEnforceableProductToProduct(dto.getProducts());
			registrationDefinition.setProduct(product);
			
			registration.setRegistrationDefinition(registrationDefinition);
			/*for(String productId:dto.getProductIds()){
				try {
					EnforceableProductDto enfoProduct = erightsFacade.getProduct(productId);
					ProductRegistrationDefinition registrationDefinition = new ProductRegistrationDefinition();
					Product product = convertEnforceableProductToProduct(enfoProduct);
					registrationDefinition.setProduct(product);
					
					e.setRegistrationDefinition(registrationDefinition);
				} catch (ErightsException e1) {
					throw new ServiceLayerException(e1.getMessage());
				}
			}*/
			registration.setCompleted(dto.isCompleted());
			registration.setActivated(dto.isActive());
			registration.setAwaitingValidation(dto.isAwaitingValidation());
			registration.setDenied(dto.isDenied());
			//e.setErightsLicenceId(dto.getErightsId());
			registration.setId(dto.getLicenseId());
			registration.setCreatedDate(dto.getCreatedDate());
			registration.setUpdatedDate(dto.getUpdatedDate());
			registration.getLinkedRegistrations().addAll(linkedRegistrations);
			if(dto.getActivationCode()!=null){
				ActivationCodeRegistration acReg = (ActivationCodeRegistration)registration;
				ActivationCode activationCode = new ActivationCode();
				activationCode.setCode(dto.getActivationCode());
				acReg.setActivationCode(activationCode);
				registration = (Registration)acReg;
			}
			
			registrations.add(registration);

		}
		return registrations;
		/*List<Registration<? extends ProductRegistrationDefinition>> result = new ArrayList<Registration<? extends ProductRegistrationDefinition>>();
		List<ActivationCodeRegistration> activationCodeRegistrations = this.registrationDao.getActivationCodeRegistrationsForCustomer(customer);
		List<ProductRegistration> productRegistrations = this.registrationDao.getProductRegistrationsForCustomer(customer);
		if (activationCodeRegistrations != null) {
			result.addAll(activationCodeRegistrations);
		}
		if (productRegistrations != null) {
			result.addAll(productRegistrations);
		}
		return result;*/
	}

	private List<Product> getProductsInRegistrations(List<Registration<? extends ProductRegistrationDefinition>> registrations) {
		// get a list of products to populate with external ids
		List<Product> products = new ArrayList<Product>();
		for (Registration<? extends ProductRegistrationDefinition> reg : registrations) {
			RegisterableProduct regProd = (RegisterableProduct)reg.getRegistrationDefinition().getProduct();
			products.add(regProd);
			Set<LinkedProduct> linked = null ;//regProd.getLinkedProducts();
			//product de-duplication
			/*			if (linked != null && linked.isEmpty() == false) {
				products.addAll(regProd.getLinkedProducts());
			}
*/		}
		return products;
	}

	private String getSubjectProductNameActivation(ProductRegistrationDefinition productRegistrationDefinition){
		String result = productRegistrationDefinition.getProduct().getProductName() + " " + EACSettings.getProperty(EACSettings.PRODUCT_REGISTRATION_ACTIVATED_EMAIL_SUBJECT);
		return result;
	}

	private String getSubjectProductNameRegistration(ProductRegistrationDefinition productRegistrationDefinition){
		String result = productRegistrationDefinition.getProduct().getProductName() + " " + EACSettings.getProperty(EACSettings.PRODUCT_REGISTRATION_EMAIL_SUBJECT);
		return result;
	}

	private LicenceDto getLicenceDto(Registration<? extends ProductRegistrationDefinition> registration, Customer customer) {
		LicenceDto result = null;
		try {
			List<LicenceDto> licences = erightsFacade.getLicensesForUser(customer.getId(), registration.getId());
			Iterator<LicenceDto> iter = licences.iterator();
			LicenceDto licence = null;
			while (iter.hasNext() && result == null) {
				licence = iter.next();
				if (licence.getLicenseId().equals(registration.getId())) {
					result = licence;
				}
			}
		} catch (ErightsException ex) {
			LOG.warn("problem getting licences for customer with eRightsId of " + customer.getId());
		}
		return result;
	}


	private ActivationCodeRegistration saveACRegistrationForProduct(final Customer customer, final ActivationCodeRegistrationDefinition activationCodeRegistrationDefinition, ActivationCode activationCode) throws ServiceLayerException {

		ActivationCodeRegistration registration = new ActivationCodeRegistration();
		registration.setCustomer(customer);
		registration.setRegistrationDefinition(activationCodeRegistrationDefinition);
		registration.setCreatedDate(new DateTime());
		//If the page definition is empty then the registration is complete
		registration.setCompleted(activationCodeRegistrationDefinition.getPageDefinition() == null);
		registration.setActivated(false);
		registration.setDenied(false);
		registration.setAwaitingValidation(false);
		if(activationCode != null) {
			registration.setActivationCode(activationCode);
		}
		//registrationDao.save(registration);
		//registrationDao.flush();
		AuditLogger.logEvent(customer, "Save ActivationCodeRegistration", "Registration ID : "+ registration.getId()+" RegistrationDefinitionId:"+activationCodeRegistrationDefinition.getId(), AuditLogger.product(activationCodeRegistrationDefinition.getProduct()));
		return registration;
	}

	private ActivationCodeRegistration saveACRegistrationForEacGroup(final Customer customer, final Product product, ActivationCode activationCode) throws ServiceLayerException {

		ActivationCodeRegistrationDefinition activationCodeRegistrationDefinition = registrationDefinitionDao.getRegistrationDefinitionByProduct(ActivationCodeRegistrationDefinition.class, product);

		ActivationCodeRegistration registration = new ActivationCodeRegistration();
		registration.setCustomer(customer);
		registration.setRegistrationDefinition(activationCodeRegistrationDefinition);
		registration.setCreatedDate(new DateTime());
		//If the page definition is empty then the registration is complete
		registration.setCompleted(activationCodeRegistrationDefinition.getPageDefinition() == null);
		registration.setActivated(false);
		registration.setDenied(false);
		registration.setAwaitingValidation(false);
		if(activationCode != null) {
			registration.setActivationCode(activationCode);
		}
		//registrationDao.save(registration);
		/*registrationDao.flush();*/
		AuditLogger.logEvent(customer, "Save ActivationCodeRegistration", "Registration ID : "+ registration.getId()+" RegistrationDefinitionId:"+activationCodeRegistrationDefinition.getId(), AuditLogger.product(activationCodeRegistrationDefinition.getProduct()));
		return registration;
	}

	@Override
	public CustomerRegistrationsDto getEntitlementsForCustomerRegistrationForAfterRedeem(Customer customer, List<Registration<? extends ProductRegistrationDefinition>> registrations)
			throws ServiceLayerException {
		List<Registration<? extends ProductRegistrationDefinition>> registrationsWithInfo = new ArrayList<Registration<? extends ProductRegistrationDefinition>>();
		for (Registration<? extends ProductRegistrationDefinition> registration : registrations) {
			Registration<? extends ProductRegistrationDefinition> registration1 = this.registrationDao.getRegistrationPopulatedWithEntitlements(registration);
			registrationsWithInfo.add(registration1);
		}

		List<Product> products = getProductsInRegistrations(registrationsWithInfo);
		List<LicenceDto> licences = getLicencesForUserProductsForEacGroup(registrationsWithInfo, customer, products);
		CustomerRegistrationsDto result = new CustomerRegistrationsDto(customer, registrations, licences);
		return result;
	}

	private void createRegistrationActivation(List<ActivationCodeRegistration> registrationList, Customer customer, Locale locale) throws ServiceLayerException {
		for (ActivationCodeRegistration activationCodeRegistration : registrationList) {
			String originalUrl = activationCodeRegistration.getRegistrationDefinition().getProduct().getLandingPage();
			RegistrationActivationDto raDto =new RegistrationActivationDto(customer, locale, originalUrl, activationCodeRegistration, activationCodeRegistration.getRegistrationDefinition());
			//this.saveRegistrationActivation(raDto);
		}
	}

	@Override
	public ActivationCode incrementActivationCodeUsage(ActivationCode activationCode) throws ServiceLayerException {
		Session session = activationCodeDao.getSessionFromDao();
		session.refresh(activationCode, LockOptions.UPGRADE);
		activationCodeService.validateActivationCode(activationCode);
		if (activationCode.getAllowedUsage() != null) {
			activationCode.incrementActualUsage();
			//activationCodeDao.update(activationCode);
			//activationCodeDao.flush();
		}

		Hibernate.initialize(activationCode.getActivationCodeBatch());
		Hibernate.initialize(activationCode.getActivationCodeBatch().getActivationCodeRegistrationDefinition());
		Hibernate.initialize(activationCode.getActivationCodeBatch().getActivationCodeRegistrationDefinition().getProduct());
		Hibernate.initialize(activationCode.getActivationCodeBatch().getActivationCodeRegistrationDefinition().getRegistrationActivation());
		if(activationCode.getActivationCodeBatch().getActivationCodeRegistrationDefinition().getProduct() != null){
			Hibernate.initialize(activationCode.getActivationCodeBatch().getActivationCodeRegistrationDefinition().getProduct());
		}
		Hibernate.initialize(activationCode.getActivationCodeBatch().getActivationCodeRegistrationDefinition().getEacGroup());
		if(activationCode.getActivationCodeBatch().getActivationCodeRegistrationDefinition().getEacGroup()!=null){
			Hibernate.initialize(activationCode.getActivationCodeBatch().getActivationCodeRegistrationDefinition().getEacGroup().getProducts());
			for(Product product: activationCode.getActivationCodeBatch().getActivationCodeRegistrationDefinition().getEacGroup().getProducts()){
				Hibernate.initialize(product);
			}
		}
		return activationCode;
	}

	@Override
	public List<LicenceDto> createRegistrationAndAddLicence(Customer customer, ActivationCode activationCode, Locale locale) 
			throws ServiceLayerException, ProductNotFoundException, UserNotFoundException, LicenseNotFoundException, AccessDeniedException, 
			GroupNotFoundException, ErightsException, Exception {
		ActivationCodeBatch activationCodeBatch = activationCode.getActivationCodeBatch();
		ActivationCodeRegistrationDefinition activationCodeRegistrationDefinition = activationCodeBatch.getActivationCodeRegistrationDefinition();
		String originalUrl = activationCodeRegistrationDefinition.getProduct().getLandingPage();		
		LicenceType licenseType = activationCode.getActivationCodeBatch().getLicenceTemplate().getLicenceType() ;
		//EnforceableProductDto product = productService.getEnforceableProductByErightsId(activationCodeRegistrationDefinition.getProduct().getId());
		List<EnforceableProductUrlDto> productUrls = activationCodeRegistrationDefinition.getProduct().getProductUrls() ;
		if (licenseType == LicenceType.ROLLING ) {
			RollingLicenceTemplate licenseTemplate = (RollingLicenceTemplate) activationCode.getActivationCodeBatch().getLicenceTemplate() ;
			if ( licenseTemplate.getBeginOn() == RollingBeginOn.FIRST_USE && ( productUrls.isEmpty() || productUrls.size() < 1) ) {
				throw new ServiceLayerException("Erights problem adding licence.", new ErightsException("Erights problem adding licence."), new Message("error.problem.registering.product",
	                    "There was a problem registering the product. Please contact the system administrator."));
			}
		}
		long startTime = System.currentTimeMillis();
		ActivationCodeRegistration registration = saveACRegistrationForProduct(customer, activationCodeRegistrationDefinition, activationCode);
		AuditLogger.logEvent("Time to saveACRegistrationForProduct :: " + (System.currentTimeMillis() - startTime));
		/*
		String originalUrl = activationCodeRegistrationDefinition.getProduct().getLandingPage();
		RegistrationActivationDto raDto = new RegistrationActivationDto(registration.getCustomer(), locale, originalUrl, registration, activationCodeRegistrationDefinition);*/
		
		startTime = System.currentTimeMillis();
		List<LicenceDto> licences = 
				erightsFacade.redeemActivationCode(activationCode, customer.getId(), originalUrl, 
						activationCodeRegistrationDefinition.isConfirmationEmailEnabled(), true);
		AuditLogger.logEvent("Time to redeemActivationCode :: " + (System.currentTimeMillis() - startTime));
		/*ActivationCodeRegistration registration = null;
		if (licences != null && licences.size() > 0) {
			registration = saveACRegistrationForProduct(customer, activationCodeRegistrationDefinition, 
					activationCode);
			registration.setId(licences.get(0).getLicenseId());
		}
		if (licences.size() > 0 && licences.get(0).getProducts().getLinkedProducts() != null && licences.get(0).getProducts().getLinkedProducts().size() > 0 ) {
			List<LinkedProductNew> linkedRegistration = licences.get(0).getProducts().getLinkedProducts() ;
			for (LinkedProductNew product : linkedRegistration) {
				LinkedRegistration linkedRegistration2 = new LinkedRegistration() ;
				linkedRegistration2.setId(licences.get(0).getLicenseId());
				LinkedProduct linkedProduct = new LinkedProduct();
				linkedProduct.setId(product.getProductId());
				linkedProduct.setProductName(product.getName());
				linkedRegistration2.setLinkedProduct(linkedProduct);
				registration.getLinkedRegistrations().add(linkedRegistration2);
			}
			//licences.get(0).getLinkedProductIds()
		}
		ActivationCodeRegistration registration = 
				saveACRegistrationForProduct(customer, activationCodeRegistrationDefinition, activationCode);
		RegistrationActivationDto raDto = 
				new RegistrationActivationDto(registration.getCustomer(), locale, originalUrl, registration, activationCodeRegistrationDefinition);
		EnforceableProductDto enforceableProductDto = productService.getEnforceableProductByErightsId(
				registration.getRegistrationDefinition().getProduct().getErightsId());
		//this.saveRegistrationActivation(raDto, enforceableProductDto);
		List<Registration<? extends ProductRegistrationDefinition>> regList = new ArrayList<Registration<? extends ProductRegistrationDefinition>>();
		regList.add(registration);
		if (registration.getLinkedRegistrations().size() > 0 ) {
			ActivationCodeRegistration linkRegistration = null;
			
			for (LinkedRegistration product : registration.getLinkedRegistrations()) {
				ActivationCodeRegistrationDefinition activationCodeRegistrationDefinitionLink = new ActivationCodeRegistrationDefinition() ;
				Product linkedProduct = new RegisterableProduct() ;
				linkedProduct.setId(product.getLinkedProduct().getId());
				linkedProduct.setProductName(product.getLinkedProduct().getProductName());
				
				LicenceDto license = licenseClone(licences.get(0),linkedProduct) ;
				
				licences.add(license) ;
				
				activationCodeRegistrationDefinitionLink.setProduct(linkedProduct);
				linkRegistration = saveACRegistrationForProduct(customer, activationCodeRegistrationDefinitionLink, 
						activationCode);
				linkRegistration.setId(linkedProduct.getId());
				regList.add(linkRegistration);
			}
			//licences.get(0).getLinkedProductIds()
		}
		AuditLogger.logEvent(customer, "Redeemed Activation Code successfully", AuditLogger.activationCode(activationCode.getCode()), AuditLogger.product(activationCodeRegistrationDefinition.getProduct()));
		CustomerRegistrationsDto dto = new CustomerRegistrationsDto(customer, regList, licences);
		
		//this.registrationDao.save(registration);
		//this.registrationDefinitionDao.save(activationCodeRegistrationDefinition);
		//this.licenceTemplateDao.save(activationCodeRegistrationDefinition.getLicenceTemplate());
		//List<OupLicenseInfoWS> licenseInfo = redeemActivationCodeResponse.getLicenses();
		
*/		RegistrationActivationDto raDto = 
		new RegistrationActivationDto(registration.getCustomer(), locale, originalUrl, registration, activationCodeRegistrationDefinition);
		this.saveRegistrationActivationCode(raDto, activationCodeRegistrationDefinition.getProduct(), licences);
		if (licences.get(0).getProducts().getProductId().equals(activationCodeRegistrationDefinition.getProduct().getId())) {
			licences.get(0).getProducts().setActivationStrategy(activationCodeRegistrationDefinition.getProduct().getActivationStrategy());
			licences.get(0).getProducts().setValidatorEmail(activationCodeRegistrationDefinition.getProduct().getValidatorEmail());
			licences.get(0).getProducts().setConfirmationEmailEnabled(activationCodeRegistrationDefinition.isConfirmationEmailEnabled());
		}
		startTime = System.currentTimeMillis();
		sendReedeemEmail(licences, raDto);
		AuditLogger.logEvent("Time to sendRedeemEmail :: " + (System.currentTimeMillis() - startTime));
		
		return licences;
	}


	public ActivationCodeRegistration createRegistrationRedeemActivationCode(Customer customer, ActivationCode activationCode, Locale locale, boolean completed) 
			throws ServiceLayerException, ProductNotFoundException, UserNotFoundException, LicenseNotFoundException, AccessDeniedException, 
			GroupNotFoundException, ErightsException, Exception {
		ActivationCodeBatch activationCodeBatch = activationCode.getActivationCodeBatch();
		ActivationCodeRegistrationDefinition activationCodeRegistrationDefinition = activationCodeBatch.getActivationCodeRegistrationDefinition();
		String originalUrl = activationCodeRegistrationDefinition.getProduct().getLandingPage();		
		
		
		/*ActivationCodeRegistration registration = saveACRegistrationForProduct(customer, activationCodeRegistrationDefinition, activationCode);

		String originalUrl = activationCodeRegistrationDefinition.getProduct().getLandingPage();
		RegistrationActivationDto raDto = new RegistrationActivationDto(registration.getCustomer(), locale, originalUrl, registration, activationCodeRegistrationDefinition);*/
		
		List<LicenceDto> licences = 
				erightsFacade.redeemActivationCode(activationCode, customer.getId(), originalUrl, activationCodeRegistrationDefinition.isConfirmationEmailEnabled(), completed);
		ActivationCodeRegistration registration = null;
		if (licences != null && licences.size() > 0) {
			registration = saveACRegistrationForProduct(customer, activationCodeRegistrationDefinition, 
					activationCode);
		}
		/*ActivationCodeRegistration registration = 
				saveACRegistrationForProduct(customer, activationCodeRegistrationDefinition, activationCode);*/
		/*RegistrationActivationDto raDto = 
				new RegistrationActivationDto(registration.getCustomer(), locale, originalUrl, registration, activationCodeRegistrationDefinition);*/
		//registration.setErightsLicenceId(licences.get(0).getErightsId());
		registration.setId(licences.get(0).getLicenseId());
		/*EnforceableProductDto enforceableProductDto = productService.getEnforceableProductByErightsId(
				registration.getRegistrationDefinition().getProduct().getErightsId());*/
		//this.saveRegistrationActivation(raDto, enforceableProductDto);
		AuditLogger.logEvent(customer, "Redeemed Activation Code successfully", AuditLogger.activationCode(activationCode.getCode()), AuditLogger.product(activationCodeRegistrationDefinition.getProduct()));
		//this.registrationDao.save(registration);
		//this.registrationDefinitionDao.save(activationCodeRegistrationDefinition);
		//this.licenceTemplateDao.save(activationCodeRegistrationDefinition.getLicenceTemplate());
		//List<OupLicenseInfoWS> licenseInfo = redeemActivationCodeResponse.getLicenses();
		//sendReedeemEmail(licences, raDto);
		return registration;
	}
	
	@Override
	public void decrementUsage( ActivationCode activationCode) {
		//Session session = activationCodeDao.getSessionFromDao();
		//session.refresh(activationCode, LockOptions.UPGRADE);
		activationCode.decrementActualUsage();
		//activationCodeDao.update(activationCode);
		//activationCodeDao.flush();
	}

	@Override
	public List<LicenceDto> createRegistrationAndAddLicenceForEacGroup(Customer customer, ActivationCode activationCode, 
			Locale locale) throws ServiceLayerException, ProductNotFoundException, UserNotFoundException, LicenseNotFoundException, 
			AccessDeniedException, GroupNotFoundException, ErightsException {
		ActivationCodeBatch activationCodeBatch = activationCode.getActivationCodeBatch();
		ActivationCodeRegistrationDefinition activationCodeRegistrationDefinition = activationCodeBatch.getActivationCodeRegistrationDefinition();
		LicenceType licenseType = activationCodeBatch.getLicenceTemplate().getLicenceType() ;
		if (licenseType == LicenceType.ROLLING ) {
			RollingLicenceTemplate licenseTemplate = (RollingLicenceTemplate) activationCode.getActivationCodeBatch().getLicenceTemplate() ;
			if ( licenseTemplate.getBeginOn() == RollingBeginOn.FIRST_USE ) {
				throw new ServiceLayerException("Erights problem adding licence.", new ErightsException("Erights problem adding licence."), new Message("error.problem.registering.product",
	                    "There was a problem registering the product. Please contact the system administrator."));
			}
		}
		//this.createRegistrationActivation(registrationList, customer, locale);
		long startTime = System.currentTimeMillis();
		List<LicenceDto> licenses = erightsFacade.redeemActivationCode(activationCode, customer.getId(), null ,
				activationCodeRegistrationDefinition.isConfirmationEmailEnabled(),true);
		AuditLogger.logEvent("Total Time to redeemActivationCode :: " + (System.currentTimeMillis() - startTime));
		AsyncEmailServiceForRedeemProductGroup asyncEmailServiceForRedeemProductGroup = new AsyncEmailServiceForRedeemProductGroup(licenses, customer, locale); 
		Thread sendMailThread = new Thread(asyncEmailServiceForRedeemProductGroup);
		startTime = System.currentTimeMillis();
		sendMailThread.start();
		AuditLogger.logEvent("Time to execute sendMailThread :: " + (System.currentTimeMillis() - startTime));
		//.sendMailRedeemActivationCodeForProductGroup(licenses, customer, locale);
			
		
		return licenses;
	}

	/*
	 * Gaurav Soni : Performance improvements CR
	 * */
	@Override
	public CustomerRegistrationsDto getEntitlementsForCustomerRegistrationsFiltered( Customer customer, Set<String> productOrgUnitSet, Set<String> productSystemIdSet, String licenceState) throws ServiceLayerException {
		//List<Registration<? extends ProductRegistrationDefinition>> registrations = getProductRegistrationsForCustomerFiltered(customer, productSystemIdSet, productOrgUnitSet);

		List<LicenceDto> licences = licenceService.getLicencesForCustomer(customer);
		List<Registration<? extends ProductRegistrationDefinition>> registrations = getProductRegistrationsForCustomerFiltered(customer, productSystemIdSet, productOrgUnitSet, licences);
		if(StringUtils.isNotBlank(licenceState) && CollectionUtils.isNotEmpty(licences)){
			licences = getfilterLicencesByLicenceState(licences, licenceState);
		}

		CustomerRegistrationsDto result = new CustomerRegistrationsDto(customer, registrations, licences);
		return result;
	}
	
	@Override
	public boolean getCompletedRegistrationInformation(
			Product product, Customer customer) {
		
		ProductRegistrationDefinition pageRegDefinition = null ;
		PageDefinition pageDefinition = null;
		try {
			pageRegDefinition = registrationDefinitionService.getProductRegistrationDefinitionByProduct(product);
		} catch (ServiceLayerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LOG.error(e.getMessage());
		}
		if (pageRegDefinition != null ) {
    		pageDefinition = pageRegDefinition.getPageDefinition() ;
    		if (pageDefinition != null ) 
            	return false;
    	}
    	return true;
		//return registrationDefinitionDao.getAnswersForCustomerAndRegistrationDefinition(product,customer);
	}
	
	
	


	/*
	 * Gaurav Soni : Performance improvements CR
	 * */
	private List<Registration<? extends ProductRegistrationDefinition>> getProductRegistrationsForCustomerFiltered(final Customer customer, Set<String> productSystemIdSet, Set<String> productOrgUnitSet, List<LicenceDto> licences)
			throws ServiceLayerException {
		List<Registration<? extends ProductRegistrationDefinition>> result = new ArrayList<Registration<? extends ProductRegistrationDefinition>>();
		List<ProductRegistration> productRegistrations = new ArrayList<ProductRegistration>();
		//List<LicenceDto> licences = licenceService.getLicencesForCustomer(customer);
		//licences.get(0).getProductIds().get(0);
		
		for(LicenceDto dto : licences){
			
			ProductRegistration e = new ProductRegistration();
		
			Product product = convertEnforceableProductToProduct(dto.getProducts());
			ProductRegistrationDefinition registrationDefinition = new ProductRegistrationDefinition();
			registrationDefinition.setProduct(product);
			
			e.setRegistrationDefinition(registrationDefinition);
			/*for(String productId:dto.getProductIds()){
				try {
					//ProductBean productBean = new ProductBean(product, accountRegistrationDefinition, productRegistrationDefinition, enforceableProduct2, regDefs, linked)
					EnforceableProductDto enfoProduct = erightsFacade.getProduct(productId);
					Product product = convertEnforceableProductToProduct(enfoProduct);
					ProductRegistrationDefinition registrationDefinition = new ProductRegistrationDefinition();
					registrationDefinition.setProduct(product);
					
					e.setRegistrationDefinition(registrationDefinition);
				} catch (ErightsException e1) {
					e1.printStackTrace();
				}
			}*/
			e.setCompleted(dto.isCompleted());
			e.setActivated(dto.isActive());
			e.setAwaitingValidation(dto.isAwaitingValidation());
			e.setDenied(dto.isDenied());
			e.setId(dto.getLicenseId());
			//e.getLinkedRegistrations().addAll(linkedRegistrations);
			productRegistrations.add(e);
		}
		/*List<ActivationCodeRegistration> activationCodeRegistrations = this.registrationDao.getActivationCodeRegistrationsForCustomerFiltered(customer, productOrgUnitSet, productSystemIdSet);
		List<ProductRegistration> productRegistrations = this.registrationDao.getProductRegistrationsForCustomerFiltered(customer, productOrgUnitSet, productSystemIdSet);
		*/
			
		/*if (activationCodeRegistrations != null) {
			result.addAll(activationCodeRegistrations);
		}*/
		if (productRegistrations != null) {
			result.addAll(productRegistrations);
		}
		return result;
	}


	private Product convertEnforceableProductToProduct(EnforceableProductDto enfoProduct) {
		Product regProduct = new RegisterableProduct();
		Division division=null;
		//RegisterableProduct regProduct = new RegisterableProduct();
		LOG.debug("enfoProduct : "+ enfoProduct.getProductId());
		regProduct.setProductName(enfoProduct.getName());
		regProduct.getExternalIds().addAll(enfoProduct.getExternalIds());
		//regProduct.setId(enfoProduct.getProductId());
		regProduct.setEmail(enfoProduct.getAdminEmail());
		
		if(enfoProduct.getLandingPage()!=null)
		regProduct.setLandingPage(enfoProduct.getLandingPage());
		
		if(enfoProduct.getHomePage()!=null)
		regProduct.setHomePage(enfoProduct.getHomePage());
		
		if(enfoProduct.getDivision()!=null)
		{
			division=new Division();
			division.setDivisionType(enfoProduct.getDivision().getDivisionType());
			regProduct.setDivision(division);
		}	
		
	
		List<Product> linkProducts=new ArrayList<Product>();
		for(LinkedProductNew linked : enfoProduct.getLinkedProducts())
		{
			Product linkProduct=new RegisterableProduct();
			linkProduct.setId(linked.getProductId());
			linkProduct.getExternalIds().addAll(linked.getExternalIds());
			
			if(linked.getLandingPage()!=null)
			linkProduct.setLandingPage(linked.getLandingPage());
			
			if(linked.getHomePage()!=null)
			linkProduct.setHomePage(linked.getHomePage());
			
			if(linked.getDivision()!=null)
			{
				division=new Division();
				division.setDivisionType(linked.getDivision().getDivisionType());
				linkProduct.setDivision(division);
			}	
			linkProducts.add(linkProduct);
		}
		
		regProduct.setLinkedProducts(linkProducts);
		
			
		regProduct.setId(enfoProduct.getProductId());
		return regProduct;
	}

	/*
	 * Gaurav Soni : Performance improvements CR
	 * Filter licences by it's state returned from Atypon call
	 * */
	private List<LicenceDto> getfilterLicencesByLicenceState(List<LicenceDto> licences, String licenceState){
		List<LicenceDto> filteredLicenceDto = new ArrayList<LicenceDto>();
		for (LicenceDto licenceDto : licences) {
			if(licenceState.equalsIgnoreCase("active")){
				if(licenceDto.isActive()){
					filteredLicenceDto.add(licenceDto);
				}
			}else if(licenceState.equalsIgnoreCase("expired")){
				if(licenceDto.isExpired()){
					filteredLicenceDto.add(licenceDto);
				}
			}
		}
		return filteredLicenceDto;
	}
	
	/**
     * convertCustomer 
     * @param cdto
     * @return 
     * Customer 
     * @author Developed by TCS
     * @throws ServiceLayerException 
     */
    private Customer convertCustomer(CustomerDto cdto) {
    	Customer customer = new Customer(); 
    	if(cdto.getConcurrency()==CustomerType.SELF_SERVICE.getConcurrency())
    		customer.setCustomerType(CustomerType.SELF_SERVICE);
    	if(cdto.getConcurrency()==CustomerType.SHARED.getConcurrency())
    		customer.setCustomerType(CustomerType.SHARED);
    	if(cdto.getConcurrency()>CustomerType.SELF_SERVICE.getConcurrency())
    		customer.setCustomerType(CustomerType.SPECIFIC_CONCURRENCY);
    	CustomerType ct=customer.getCustomerType();
    	ct.setConcurrency(cdto.getConcurrency());
    	customer.setUsername(cdto.getUsername());
    	customer.setEmailAddress(cdto.getEmailAddress());
    	customer.setEmailVerificationState(cdto.getEmailVerificationState());
    	customer.setEnabled(!cdto.isSuspended());
    	customer.setFamilyName(cdto.getFamilyName());
    	customer.setFirstName(cdto.getFirstName());
    	customer.setLocale(cdto.getLocale());
    	customer.setTimeZone(cdto.getTimeZone());
    	customer.setResetPassword(cdto.isResetPassword());
    	customer.setExternalIds(cdto.getExternalIds());
    	customer.setId(cdto.getUserId());
    	//customer.setErightsId(cdto.getErightsId());
    	//TODO:Change This and add Created date from Erights.
    	customer.setCreatedDate(new DateTime());
    	return customer;
    }

	@Override
	public void saveActivationRegistrationActivation(
			RegistrationActivationDto registrationActivationDto,
			EnforceableProductDto enforceableProduct) throws ServiceLayerException {
		Customer customer = registrationActivationDto.getCustomer();		
		Registration<?> registration = registrationActivationDto.getRegistration();
		String licenceId = registration.getId();
		try{
			LOG.debug("In activation save"+licenceId);
			List<LicenceDto> lictDto = erightsFacade.getLicensesForUser(customer.getId(), licenceId);
			lictDto.get(0).setProducts(enforceableProduct);
			LOG.debug("Activation type"+enforceableProduct.getActivationStrategy());
			if(enforceableProduct.getActivationStrategy()== ActivationStrategy.INSTANT.toString()){
				erightsFacade.activateLicense(customer.getId(), licenceId);
				registration.setAwaitingValidation(true);
				registration.setCompleted(true);
				registration.setActivated(true);
			}
			sendReedeemEmail(lictDto, registrationActivationDto);
		} catch (ErightsException e) {
			throw new ServiceLayerException("Erights problem adding licence.", e, new Message("error.problem.registering.product",
					"There was a problem registering the product. Please contact the system administrator."));
		} catch (Exception e) {
			throw new ServiceLayerException("Erights problem adding licence.", e, new Message("error.problem.registering.product",
					"There was a problem registering the product. Please contact the system administrator."));
		}
	}

	@Override
	public void updateLicenseModifiedDate(String licenseId) {
		erightsFacade.updateLicenseModifiedDate(licenseId);
	}
	
	private  Map<Integer,String> getAllDivisions() {
		LOG.info("getAllDivisions:: == >> start");
		if(divisionsMap != null){
			return divisionsMap;
		}
		List<Division> allDivisions = new ArrayList<Division>();
		Map<Integer,String> divisionsMapTemp = new HashMap<Integer,String>();
		try {
			allDivisions=divisionService.getAllDivisions();
			for(Division div : allDivisions){
				divisionsMapTemp.put(div.getErightsId(), div.getDivisionType());
			}
		} catch (AccessDeniedException e) {
			e.printStackTrace();
		} catch (DivisionNotFoundException e) {
			e.printStackTrace();
		} catch (ErightsException e) {
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
		divisionsMap = divisionsMapTemp;
		LOG.info("getAllDivisions:: == >> end");
		return divisionsMap;
	}

	public String getDivisionTypeByDivisionOrId(final Division division, int divisionid){
		LOG.info("getDivisionTypeByDivisionOrId :: == >> Start with id: "+divisionid);
		String emailExclusionDivisionType = EACSettings.getProperty(EACSettings.EMAIL_EXCLUSION_DIVISIONTYPE);
		List<String> emailExclusionDivList = Arrays.asList(emailExclusionDivisionType.trim().split(","));
    	String divisionType= null;
    	if(divisionid>0){
    		try{
    			Map<Integer,String> divisionsMapL = getAllDivisions();
    			divisionType = divisionsMapL.get(divisionid);
    		}catch(Exception e){
    			e.printStackTrace();
    		}
    	}else if (division!=null){
	    	divisionType = division.getDivisionType();
    	}
    	
    	for (String emailExclusionDiv : emailExclusionDivList) {
    	   	if(divisionType.toUpperCase().contains(emailExclusionDiv.trim())){
    				divisionType = EACSettings.getProperty(EACSettings.EMAIL_EXCLUSION);
    				break;
    	   	}
       	}
    	
    	LOG.info("Division Type :: == >> "+divisionType);
    	return divisionType;
    }
	
}
