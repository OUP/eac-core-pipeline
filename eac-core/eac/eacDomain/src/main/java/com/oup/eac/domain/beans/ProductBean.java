package com.oup.eac.domain.beans;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.joda.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.oup.eac.common.utils.url.URLUtils;
import com.oup.eac.domain.AccountPageDefinition;
import com.oup.eac.domain.AccountRegistrationDefinition;
import com.oup.eac.domain.ConcurrentLicenceTemplate;
import com.oup.eac.domain.CountryMatchRegistrationActivation;
import com.oup.eac.domain.Division;
import com.oup.eac.domain.ExternalProductId;
import com.oup.eac.domain.ExternalSystem;
import com.oup.eac.domain.ExternalSystemIdType;
import com.oup.eac.domain.InstantRegistrationActivation;
import com.oup.eac.domain.LicenceTemplate;
import com.oup.eac.domain.LicenceTemplate.LicenceType;
import com.oup.eac.domain.LinkedProduct;/*
import com.oup.eac.domain.LinkedProduct.ActivationMethod;*/
import com.oup.eac.domain.LinkedProductNew;
import com.oup.eac.domain.Platform;
import com.oup.eac.domain.Product;
import com.oup.eac.domain.Product.ProductState;
import com.oup.eac.domain.ProductPageDefinition;
import com.oup.eac.domain.ProductRegistrationDefinition;
import com.oup.eac.domain.RegisterableProduct;
import com.oup.eac.domain.RegisterableProduct.RegisterableType;
import com.oup.eac.domain.RegistrationActivation;
import com.oup.eac.domain.RegistrationActivation.ActivationStrategy;
import com.oup.eac.domain.RegistrationDefinition;
import com.oup.eac.domain.RegistrationDefinition.RegistrationDefinitionType;
import com.oup.eac.domain.RollingLicenceTemplate;
import com.oup.eac.domain.RollingLicenceTemplate.RollingBeginOn;
import com.oup.eac.domain.RollingLicenceTemplate.RollingUnitType;
import com.oup.eac.domain.SelfRegistrationActivation;
import com.oup.eac.domain.UsageLicenceTemplate;
import com.oup.eac.domain.ValidatedRegistrationActivation;
import com.oup.eac.dto.EnforceableProductDto;
import com.oup.eac.dto.EnforceableProductUrlDto;
import com.oup.eac.dto.LicenceDto;
import com.oup.eac.dto.RollingLicenceDetailDto;
import com.oup.eac.dto.StandardConcurrentLicenceDetailDto;
import com.oup.eac.dto.UsageLicenceDetailDto;

public class ProductBean implements Serializable {

	public static final String NEW_VALIDATOR_ACTIVATION = "newValidator";
	public static final String NEW_COUNTRY_MATCH_ACTIVATION = "newCountryMatch";
	private List<ExternalProductId> externalIds = new ArrayList<ExternalProductId>();
	private boolean editMode;
	private Product product;
	private LicenceDto licenceDto ;
	private String productId;
	private String activationStrategy;
	private String divisionType;
	private String productName;
	private String registrationType = RegisterableType.SELF_REGISTERABLE.name();
	private String homePage;
	private String landingPage;
	private String email;
	private String sla;
	private AccountRegistrationDefinition accountRegistrationDefinition;
	private ProductRegistrationDefinition productRegistrationDefinition;
	private String accountPageDefinitionId;
	private List<AccountPageDefinition> accountPageDefinitions;
	private String productPageDefinitionId;
	private List<ProductPageDefinition> productPageDefinitions;
	private boolean emailValidation;
	private boolean emailConfirmationEnabled;
	private boolean activationCode;
	private String registrationActivationId;
	private List<RegistrationActivation> registrationActivations = new ArrayList<RegistrationActivation>();
	private String validator;
	private LicenceType licenceType = LicenceType.ROLLING;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private LocalDate licenceStartDate;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private LocalDate licenceEndDate;
	private String totalConcurrency = "1";
	private String userConcurrency = "1";
	private String timePeriod = "1";
	private RollingUnitType rollingUnitType;
	private RollingBeginOn rollingBeginOn;
	private String licenceAllowedUsages = "1";
	private Map<ExternalSystem, List<ExternalSystemIdType>> externalSystemMap = new HashMap<ExternalSystem, List<ExternalSystemIdType>>();
	private Map<Integer,Platform> platformMap = new HashMap<Integer, Platform>() ;
	private String externalIdsToAdd;
	private String externalIdsToUpdate;
	private String externalIdsToRemove;
	private String linkedProductsToAdd;
	private String linkedProductsToUpdate;
	private String linkedProductsToRemove;
	private List<String> linkedProductsToRemoveErightsId;
	private String description;
	private String unmatchedRegistrationActivationId;
	private String matchedRegistrationActivationId;
	private String localeList;
	private ProductState state = ProductState.ACTIVE;
	private List<ProductState> states = new ArrayList<ProductState>();
	//private Integer erightsId;
	private EnforceableProductDto enforceableProduct;
	private List<EnforceableProductUrlDto> urls = new ArrayList<EnforceableProductUrlDto>();
	private List<EnforceableProductUrlDto> newUrls = createEmptyUrls();
	private List<Platform> platformList = new ArrayList<Platform>() ; 
	private String platformToAdd ;
	private String urlIndexesToRemove;
	private String divisionId;
	private final Set<ProductData> productData       = new LinkedHashSet<ProductData>(); // Maintains Insertion Order
    private final Set<LinkedProductNew> directLinkedProducts   = new LinkedHashSet<LinkedProductNew>();
    private final List<LinkedProductNew> linkedProducts   = new ArrayList<LinkedProductNew>();
    private final Set<LinkedProduct> indirectLinkedProducts = new LinkedHashSet<LinkedProduct>();
    private List<Division> divisions = new ArrayList<Division>();
	private Set<String> productGroupIds;
	public ProductBean(final String productId) {
		this.productId = productId;
		this.states.clear();
		for(ProductState state : ProductState.values()){
			states.add(state);
		}
		this.state = ProductState.ACTIVE;
	}
	
	public ProductBean() {
		this.states.clear();
		for(ProductState state : ProductState.values()){
			states.add(state);
		}
		this.state = ProductState.ACTIVE;
	}

	

	public Set<String> getProductGroupIds() {
		return productGroupIds;
	}

	public void setProductGroupIds(Set<String> productGroupIds) {
		this.productGroupIds = productGroupIds;
	}

	public String getActivationStrategy() {
		return activationStrategy;
	}

	public void setActivationStrategy(String activationStrategy) {
		this.activationStrategy = activationStrategy;
	}

	public String getDivisionType() {
		return divisionType;
	}

	public void setDivisionType(String divisionType) {
		this.divisionType = divisionType;
	}
	public ProductBean(
			final RegisterableProduct product, 
			final AccountRegistrationDefinition accountRegistrationDefinition,
			final ProductRegistrationDefinition productRegistrationDefinition, 
			EnforceableProductDto enforceableProduct2, 
			List<RegistrationDefinition> regDefs, 
			List<LinkedProductNew> linked) {
		List<EnforceableProductUrlDto> urls = enforceableProduct2.getUrls();
		if (CollectionUtils.isNotEmpty(urls)) {
			this.urls.addAll(urls);
		}
		this.editMode = true;
		for(ProductState state : ProductState.values()){
			states.add(state);
		}
		initialiseProductData(regDefs, linked);
		initialiseWithProductData(product);
		initialiseWithRegistrationDefinitionData(accountRegistrationDefinition, productRegistrationDefinition);
		initialiseWithLicenceData(productRegistrationDefinition.getLicenceTemplate());
	}
	
	public ProductBean(final AccountRegistrationDefinition accountRegistrationDefinition,
			final ProductRegistrationDefinition productRegistrationDefinition,
			final EnforceableProductDto enforceableProduct ) {
		List<EnforceableProductUrlDto> urls = enforceableProduct.getUrls();
		if (CollectionUtils.isNotEmpty(urls)) {
			this.urls.addAll(urls);
		}
		this.editMode = true;
		for(ProductState state : ProductState.values()){
			states.add(state);
		}
		initialiseWithRegistrationDefinitionData(accountRegistrationDefinition, productRegistrationDefinition);
		initialiseProductData(enforceableProduct);
	}
	private Division getSelectedDivision() {
		Division selectedDivision = null;
		for (Division division : divisions) {
			if (division.getId().equals(divisionId)) {
				selectedDivision = division;
				break;
			}
		}
		return selectedDivision;
	}
	private void initialiseProductData (EnforceableProductDto enforceableProduct) {
		this.linkedProducts.clear();
        this.productData.clear();
        this.platformList.clear();
        platformList = enforceableProduct.getPlatformList() ;
        /*RegisterableType registerableType ;
        RegistrationDefinitionType registrationDefinitionType ;
        RegistrationActivation registrationActivation ;
        registrationActivation.getActivationStrategy(this.)
        this.re
        if (enforceableProduct.getLinkedProducts().size() > 0){
        	this.directLinkedProducts.addAll(enforceableProduct.getLinkedProducts()) ;
        }
        if (enforceableProduct.getRegistrationDefinitionType().equals(RegistrationDefinitionType.ACCOUNT_REGISTRATION.toString())){
        	registrationDefinitionType = RegistrationDefinitionType.ACCOUNT_REGISTRATION ;
        } else if (enforceableProduct.getRegistrationDefinitionType().equals(RegistrationDefinitionType.ACTIVATION_CODE_REGISTRATION.toString())){
        	registrationDefinitionType = RegistrationDefinitionType.ACTIVATION_CODE_REGISTRATION ;
        } else if (enforceableProduct.getRegistrationDefinitionType().equals(RegistrationDefinitionType.PRODUCT_REGISTRATION.toString())){
        	registrationDefinitionType = RegistrationDefinitionType.PRODUCT_REGISTRATION ;
        }
        
        if (enforceableProduct.getRegisterableType().equals(RegisterableType.ADMIN_REGISTERABLE.toString())) {
        	registerableType = RegisterableType.ADMIN_REGISTERABLE ;
        } else if (enforceableProduct.getRegisterableType().equals(RegisterableType.SELF_REGISTERABLE.toString())) {
        	registerableType = RegisterableType.SELF_REGISTERABLE ;
        }
        
        if (enforceableProduct.getActivationStrategy().equals(ActivationStrategy.INSTANT.toString())) {
        	activationStrategy = ActivationStrategy.INSTANT ;
        } else if (enforceableProduct.getActivationStrategy().equals(ActivationStrategy.SELF.toString())) {
        	activationStrategy = ActivationStrategy.SELF ;
        } else if (enforceableProduct.getActivationStrategy().equals(ActivationStrategy.VALIDATED.toString())) {
        	activationStrategy = ActivationStrategy.VALIDATED ;
        } */
        
        //ProductData pd = new ProductData(enforceableProduct.getEacId(), registrationDefinitionType , registerableType , activationStrategy);
        this.product = new RegisterableProduct() ;
        //this.product.setErightsId(enforceableProduct.getProductId());
        this.product.setId(enforceableProduct.getProductId());
        
        if(enforceableProduct.getLinkedProducts() != null && enforceableProduct.getLinkedProducts().size() > 0){
            this.linkedProducts.addAll(enforceableProduct.getLinkedProducts());
        }
        if (enforceableProduct.getExternalIds().size() > 0)
        {
        	externalIds.addAll(enforceableProduct.getExternalIds()) ;
        }
       /* if ( enforceableProduct.getParentIds().size() > 0) {
        	
        }*/
        /*if(regDefs != null && regDefs.isEmpty() == false){
            for(RegistrationDefinition rd : regDefs) {
                RegisterableProduct rp = rd.getProduct();
                RegisterableType registerableType = rp.getRegisterableType();
                RegistrationDefinitionType regDefType = rd.getRegistrationDefinitionType();
                String productId = rd.getProduct().getId();
                ProductData pd = new ProductData(productId, regDefType, registerableType, rd.getRegistrationActivation());
                this.productData.add(pd);
                Set<LinkedProduct> linked = rp.getLinkedProducts();
                if(linked != null){
                    for(LinkedProduct lp : linked) {
                        this.indirectLinkedProducts.add(lp);
                    }
                }
            }
        }*/
		if (enforceableProduct.getActivationStrategy() == ActivationStrategy.SELF.toString()) {
			this.registrationActivationId = enforceableProduct.getActivationStrategy() ;
		}else if ( enforceableProduct.getActivationStrategy() == ActivationStrategy.INSTANT.toString() ) {
			this.registrationActivationId = enforceableProduct.getActivationStrategy() ;
        } else if ( enforceableProduct.getActivationStrategy() == ActivationStrategy.VALIDATED.toString() ) {
        	this.registrationActivationId =  enforceableProduct.getValidatorEmail() ;
        	this.validator = enforceableProduct.getValidatorEmail() ;
        }
        	
        
        //this.erightsId = enforceableProduct.getErightsId();
        this.productName = enforceableProduct.getName() ;
        if (enforceableProduct.getState().equals(ProductState.ACTIVE.toString())){
        	this.state = ProductState.ACTIVE ;
        } else if (enforceableProduct.getState().equals(ProductState.REMOVED.toString())){
        	this.state = ProductState.REMOVED ;
        } else if (enforceableProduct.getState().equals(ProductState.RETIRED.toString())){
        	this.state = ProductState.RETIRED ;
        } else if (enforceableProduct.getState().equals(ProductState.SUSPENDED.toString())){
        	this.state = ProductState.SUSPENDED ;
        }
        
        this.productId = enforceableProduct.getProductId() ;
        this.landingPage = enforceableProduct.getLandingPage() ;
        this.homePage = enforceableProduct.getHomePage() ;
        this.email = enforceableProduct.getAdminEmail() ;
        //this.emailValidation = enforceableProduct.getValidatorEmail() ;
        this.sla = enforceableProduct.getSla() ;
        this.divisionId = String.valueOf(enforceableProduct.getDivisionId()) ; 
        this.registrationType = enforceableProduct.getRegisterableType() ;
        
        
        
        if ( enforceableProduct.getRegistrationDefinitionType().toString().equals(RegistrationDefinitionType.ACTIVATION_CODE_REGISTRATION.toString()) ) {
        	this.activationCode = true ;
        } else {
        	this.activationCode = false ;
        }
        if (enforceableProduct.getConfirmationEmailEnabled() ) {
        	this.emailConfirmationEnabled = true ;
        } else {
        	this.emailConfirmationEnabled = false ;
        }
        
        //license template set
        if (enforceableProduct.getLicenceDetail()!= null) {
        	if (enforceableProduct.getLicenceDetail().getLicenceDetail() != null && enforceableProduct.getLicenceDetail().getLicenceDetail().getStartDate() != null )
        		this.licenceStartDate = enforceableProduct.getLicenceDetail().getLicenceDetail().getStartDate();
        	if(enforceableProduct.getLicenceDetail().getLicenceDetail() != null && enforceableProduct.getLicenceDetail().getLicenceDetail().getEndDate() != null)
        		this.licenceEndDate = enforceableProduct.getLicenceDetail().getLicenceDetail().getEndDate();
        
        	if ( enforceableProduct.getLicenceDetail().getLicenceDetail() != null )
			switch (enforceableProduct.getLicenceDetail().getLicenceDetail().getLicenceType()) {
			case CONCURRENT:
				this.licenceType = LicenceType.CONCURRENT ;
				StandardConcurrentLicenceDetailDto standartLicense = (StandardConcurrentLicenceDetailDto)enforceableProduct.getLicenceDetail().getLicenceDetail() ;
				this.totalConcurrency =  standartLicense.getTotalConcurrency()+ "";
				this.userConcurrency = standartLicense.getUserConcurrency() + "";
				break;
			case ROLLING:
				this.licenceType = LicenceType.ROLLING ;
				RollingLicenceDetailDto roolingLicense = (RollingLicenceDetailDto)enforceableProduct.getLicenceDetail().getLicenceDetail() ;
				this.timePeriod = roolingLicense.getTimePeriod() + "";
				this.rollingUnitType = roolingLicense.getUnitType();
				this.rollingBeginOn = roolingLicense.getBeginOn();
				break;
			case USAGE:
				this.licenceType = LicenceType.USAGE ;
				UsageLicenceDetailDto usageLicense = (UsageLicenceDetailDto)enforceableProduct.getLicenceDetail().getLicenceDetail() ;
				this.licenceAllowedUsages = usageLicense.getAllowedUsages() + "";
				break;
			default:
				break;
			}
        }
        this.licenceDto = enforceableProduct.getLicenceDetail() ;
	}
	
	public EnforceableProductDto getEnforceableProduct() {
		if (enforceableProduct == null) {
			enforceableProduct = new EnforceableProductDto();
			enforceableProduct.setConfirmationEmailEnabled(emailConfirmationEnabled);
		}
		enforceableProduct.setName(productName);
		//enforceableProduct.setErightsId(erightsId);
		enforceableProduct.setProductId(productId);
		enforceableProduct.setSuspended(this.state.getErightsSuspend());
		enforceableProduct.setUrls(getUrlsToKeep());
		enforceableProduct.setState(this.state.toString());
		enforceableProduct.setAdminEmail(email);
		enforceableProduct.setLinkedProducts(linkedProducts);
		for (RegistrationActivation registrationActivation : registrationActivations) {
			if ( registrationActivation instanceof InstantRegistrationActivation) {
				InstantRegistrationActivation instantRegistrationActivation = (InstantRegistrationActivation)registrationActivation ;
				if ( instantRegistrationActivation.getId().equals(registrationActivationId) ) {
					enforceableProduct.setActivationStrategy(ActivationStrategy.INSTANT.toString());
					break ;
				}
			} else if ( registrationActivation instanceof SelfRegistrationActivation) {
				SelfRegistrationActivation selfRegistrationActivation = (SelfRegistrationActivation)registrationActivation ;
				if ( selfRegistrationActivation.getId().equals(registrationActivationId) ) {
					enforceableProduct.setActivationStrategy(ActivationStrategy.SELF.toString());
					break ;
				}
			} else if ( registrationActivation instanceof ValidatedRegistrationActivation) {
				ValidatedRegistrationActivation validatedRegistrationActivation = (ValidatedRegistrationActivation)registrationActivation ;
				if ( validatedRegistrationActivation.getId().equals(registrationActivationId) || registrationActivationId.equals("newValidator") ) {
					enforceableProduct.setActivationStrategy(ActivationStrategy.VALIDATED.toString());
					enforceableProduct.setValidatorEmail(validator);
					break ;
				}
			}
		}
		
		
		if ( externalIdsToAdd != null ) {
			List<ExternalProductId> externalProductIds = new ArrayList<ExternalProductId>() ;
			String externalIds[] = externalIdsToAdd.split("\\|") ;
			for (String externalId : externalIds) {
				ExternalProductId externalProductId = new ExternalProductId();
				String external [] = externalId.split(",") ;
				ExternalSystem externalSystem = new ExternalSystem() ;
				ExternalSystemIdType externalSystemIdType = new ExternalSystemIdType() ;
				externalSystem.setName(external[1]);
				externalSystemIdType.setExternalSystem(externalSystem);
				externalSystemIdType.setName(external[2]) ;
				String extId = null;
				 try{
					 extId = URLDecoder.decode(external[0], "UTF-8");
				 } catch (UnsupportedEncodingException e){
					 e.printStackTrace();
				 }
				externalProductId.setExternalId(extId);
				externalProductId.setExternalSystemIdType(externalSystemIdType);
				externalProductIds.add(externalProductId);
			}
			enforceableProduct.setExternalIds(externalProductIds);
			
		}
		
		if (platformToAdd != null ) {
			String platformRecords[] = platformToAdd.split("\\|") ;
			List<Platform> platformList = new ArrayList<Platform>() ;
			for (String record : platformRecords) {
				Platform platform = new Platform() ;
				String[] platformrRec = record.split(",") ;
				platform.setPlatformId(Integer.parseInt(platformrRec[0])) ;
				platform.setCode(platformrRec[1]);
				platform.setName(platformrRec[2]);
				platformList.add(platform);
			}
			enforceableProduct.setPlatformList(platformList);
		}
		enforceableProduct.setLandingPage(landingPage);
		enforceableProduct.setHomePage(homePage);
		enforceableProduct.setSla(sla);
		enforceableProduct.setRegisterableType(registrationType);
		enforceableProduct.setDivisionId(Integer.parseInt(divisionId));
		if (activationCode) {
			enforceableProduct.setRegistrationDefinitionType(RegistrationDefinitionType.ACTIVATION_CODE_REGISTRATION.toString());
		} else {
			enforceableProduct.setRegistrationDefinitionType(RegistrationDefinitionType.PRODUCT_REGISTRATION.toString());
		}
		for (EnforceableProductUrlDto newUrl : newUrls) {
			if (!isEmpty(newUrl)) {
				enforceableProduct.addEnforceableProductUrl(newUrl);
			}
		}
		
		if (licenceDto == null ){
			licenceDto = new LicenceDto() ;
		}
		switch (licenceType) {
		case CONCURRENT:
			StandardConcurrentLicenceDetailDto standartLicense =new StandardConcurrentLicenceDetailDto(Integer.parseInt(this.totalConcurrency), Integer.parseInt(this.userConcurrency));
			licenceDto.setLicenceDetail(standartLicense);
			break;
		case ROLLING:
			RollingLicenceDetailDto roolingLicense = new RollingLicenceDetailDto(rollingBeginOn, rollingUnitType, Integer.parseInt(timePeriod), null) ;
			licenceDto.setLicenceDetail(roolingLicense);
			break;
		case USAGE:
			UsageLicenceDetailDto usageLicense = new UsageLicenceDetailDto() ;
			usageLicense.setAllowedUsages(Integer.parseInt(this.licenceAllowedUsages));
			licenceDto.setLicenceDetail(usageLicense);
			break;
		default:
			break;
		}
		if (licenceStartDate != null ) {
			licenceDto.getLicenceDetail().setStartDate(licenceStartDate);
		}
		
		if (licenceEndDate != null ) {
			licenceDto.getLicenceDetail().setEndDate(licenceEndDate);
		}
		enforceableProduct.setLicenceDetail(licenceDto);
		return enforceableProduct;
	}
	private void initialiseProductData(List<RegistrationDefinition> regDefs, List<LinkedProductNew> directLinked) {
        this.indirectLinkedProducts.clear();
        this.directLinkedProducts.clear();
        this.productData.clear();
        
        // LINKED PRODUCT-> (Direct)
        if(directLinked != null && directLinked.isEmpty() == false){
            this.directLinkedProducts.addAll(directLinked);
        }
        
        if(regDefs != null && regDefs.isEmpty() == false){
            for(RegistrationDefinition rd : regDefs) {
            	//de-duplication
                /*Product rp = rd.getProduct();
                RegisterableType registerableType = rp.getRegisterableType();
                RegistrationDefinitionType regDefType = rd.getRegistrationDefinitionType();
                String productId = rd.getProduct().getId();
                ProductData pd = new ProductData(productId, regDefType, registerableType, rd.getRegistrationActivation());
                this.productData.add(pd);
                Set<LinkedProduct> linked = rp.getLinkedProducts();
                if(linked != null){
                    for(LinkedProduct lp : linked) {
                        this.indirectLinkedProducts.add(lp);
                    }*/
                
            }
        }
    }


	private void initialiseWithProductData(final RegisterableProduct product) {
		this.product = product;
		this.productId = product.getId();
		this.productName = product.getProductName();
		this.registrationType = product.getRegisterableType() + "";
		this.homePage = product.getHomePage();
		this.landingPage = product.getLandingPage();
		this.email = product.getEmail();
		this.sla = product.getServiceLevelAgreement();
		this.state = product.getState();
		//this.erightsId = product.getErightsId();
		//this.divisionId = product.getDivision().getId();
	}

	private void initialiseWithRegistrationDefinitionData(final AccountRegistrationDefinition accountRegistrationDefinition,
			final ProductRegistrationDefinition productRegistrationDefinition) {
		this.accountRegistrationDefinition = accountRegistrationDefinition;
		this.productRegistrationDefinition = productRegistrationDefinition;

		AccountPageDefinition accountPageDefinition = accountRegistrationDefinition.getPageDefinition();
		if (accountPageDefinition != null) {
			this.accountPageDefinitionId = accountPageDefinition.getId();
		}
		this.emailValidation = accountRegistrationDefinition.isValidationRequired();

		ProductPageDefinition productPageDefinition = productRegistrationDefinition.getPageDefinition();
		if (productPageDefinition != null) {
			this.productPageDefinitionId = productPageDefinition.getId();
		}
		//product de-duplication
		//this.emailConfirmationEnabled = productRegistrationDefinition.isConfirmationEmailEnabled().booleanValue();

		//RegistrationActivation registrationActivation = productRegistrationDefinition.getRegistrationActivation();
		//product de-duplication
		//this.registrationActivationId = registrationActivation.getId();
		/*if (registrationActivation instanceof ValidatedRegistrationActivation) {
			this.validator = ((ValidatedRegistrationActivation) registrationActivation).getValidatorEmail();
		}*/
		/*if (registrationActivation instanceof CountryMatchRegistrationActivation) {
			CountryMatchRegistrationActivation countryMatchRegistrationActivation = (CountryMatchRegistrationActivation) registrationActivation;
			this.description = countryMatchRegistrationActivation.getDescription();
			this.unmatchedRegistrationActivationId = countryMatchRegistrationActivation.getUnmatchedActivation().getId();
			this.matchedRegistrationActivationId = countryMatchRegistrationActivation.getMatchedActivation().getId();
			this.localeList = countryMatchRegistrationActivation.getLocaleList();
		}*/
	}

	private void initialiseWithLicenceData(final LicenceTemplate licenceTemplate) {
		this.licenceType = licenceTemplate.getLicenceType();
		this.licenceStartDate = licenceTemplate.getStartDate();
		this.licenceEndDate = licenceTemplate.getEndDate();

		switch (licenceTemplate.getLicenceType()) {
		case CONCURRENT:
			ConcurrentLicenceTemplate concurrentLicenceTemplate = (ConcurrentLicenceTemplate) licenceTemplate;
			this.totalConcurrency = concurrentLicenceTemplate.getTotalConcurrency() + "";
			this.userConcurrency = concurrentLicenceTemplate.getUserConcurrency() + "";
			break;
		case ROLLING:
			RollingLicenceTemplate rollingLicenceTemplate = (RollingLicenceTemplate) licenceTemplate;
			this.timePeriod = rollingLicenceTemplate.getTimePeriod() + "";
			this.rollingUnitType = rollingLicenceTemplate.getUnitType();
			this.rollingBeginOn = rollingLicenceTemplate.getBeginOn();
			break;
		case USAGE:
			UsageLicenceTemplate usageLicenceTemplate = (UsageLicenceTemplate) licenceTemplate;
			this.licenceAllowedUsages = usageLicenceTemplate.getAllowedUsages() + "";
			break;
		default:
			break;
		}
	}

	public boolean isEditMode() {
		return editMode;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(final Product product) {
		this.product = product;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(final String productId) {
		this.productId = productId;
	}
	
	public String getDivisionId() {
		return divisionId;
	}

	public void setDivisionId(String divisionId) {
		this.divisionId = divisionId;
	}

	public List<EnforceableProductUrlDto> getNewUrls() {
		return newUrls;
	}

	public void setNewUrls(List<EnforceableProductUrlDto> newUrls) {
		this.newUrls = newUrls;
	}

	public AccountRegistrationDefinition getAccountRegistrationDefinition() {
		return accountRegistrationDefinition;
	}

	public ProductRegistrationDefinition getProductRegistrationDefinition() {
		return productRegistrationDefinition;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getRegistrationType() {
		return registrationType;
	}

	public RegisterableType getRegisterableType() {
		return RegisterableType.valueOf(registrationType);
	}

	public void setRegistrationType(final String registrationType) {
		this.registrationType = registrationType;
	}

	public String getHomePage() {
		return homePage;
	}

	public String getEncodedHomePageUrl() throws Exception {
		return URLUtils.safeEncode(homePage);
	}

	public void setHomePage(final String homePage) {
		this.homePage = homePage;
	}

	public String getLandingPage() {
		return landingPage;
	}

	public String getEncodedLandingPageUrl() throws Exception {
		return URLUtils.safeEncode(landingPage);
	}

	public void setLandingPage(final String landingPage) {
		this.landingPage = landingPage;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	public String getSla() {
		return sla;
	}

	public void setSla(final String sla) {
		this.sla = sla;
	}

	public RegisterableType[] getRegisterableTypes() {
		return RegisterableProduct.RegisterableType.values();
	}

	public String getAccountPageDefinitionId() {
		return accountPageDefinitionId;
	}

	public void setAccountPageDefinitionId(final String accountPageDefinitionId) {
		this.accountPageDefinitionId = accountPageDefinitionId;
	}

	public List<AccountPageDefinition> getAccountPageDefinitions() {
		return accountPageDefinitions;
	}

	public void setAccountPageDefinitions(final List<AccountPageDefinition> accountPageDefinitions) {
		this.accountPageDefinitions = accountPageDefinitions;
	}

	public String getProductPageDefinitionId() {
		return productPageDefinitionId;
	}

	public void setProductPageDefinitionId(final String productPageDefinitionId) {
		this.productPageDefinitionId = productPageDefinitionId;
	}

	public List<ProductPageDefinition> getProductPageDefinitions() {
		return productPageDefinitions;
	}

	public void setProductPageDefinitions(final List<ProductPageDefinition> productPageDefinitions) {
		this.productPageDefinitions = productPageDefinitions;
	}

	public boolean isEmailValidation() {
		return emailValidation;
	}

	public void setEmailValidation(final boolean emailValidation) {
		this.emailValidation = emailValidation;
	}

	public boolean isEmailConfirmationEnabled() {
		return emailConfirmationEnabled;
	}

	public void setEmailConfirmationEnabled(final boolean emailConfirmationEnabled) {
		this.emailConfirmationEnabled = emailConfirmationEnabled;
	}

	public boolean isActivationCode() {
		return activationCode;
	}

	public void setActivationCode(final boolean activationCode) {
		this.activationCode = activationCode;
	}

	public String getRegistrationActivationId() {
		return registrationActivationId;
	}

	public void setRegistrationActivationId(final String registrationActivationId) {
		this.registrationActivationId = registrationActivationId;
	}

	public List<RegistrationActivation> getRegistrationActivations() {
		return registrationActivations;
	}

	public List<RegistrationActivation> getSelfRegistrationActivations() {
		return getRegistrationActivationsByName(new SelfRegistrationActivation().getName());
	}

	public List<RegistrationActivation> getInstantRegistrationActivations() {
		return getRegistrationActivationsByName(new InstantRegistrationActivation().getName());
	}

	public List<RegistrationActivation> getValidatedRegistrationActivations() {
		return getRegistrationActivationsByName(new ValidatedRegistrationActivation().getName());
	}

	public List<RegistrationActivation> getCountryMatchRegistrationActivations() {
		return getRegistrationActivationsByName(new CountryMatchRegistrationActivation().getName());
	}

	private List<RegistrationActivation> getRegistrationActivationsByName(final String name) {
		List<RegistrationActivation> registrationActivations = new ArrayList<RegistrationActivation>();
		for (RegistrationActivation registrationActivation : this.registrationActivations) {
			if (registrationActivation.getName() == name) {
				registrationActivations.add(registrationActivation);
			}
		}
		return registrationActivations;
	}

	public boolean isRegistrationActivationIdValidatedActivation() {
		boolean validatedActivation = false;
		if (NEW_VALIDATOR_ACTIVATION.equals(registrationActivationId)) {
			validatedActivation = true;
		} else {
			if (CollectionUtils.isNotEmpty(registrationActivations)) {
				for (RegistrationActivation registrationActivation : registrationActivations) {
					if (registrationActivation.getId().equals(registrationActivationId) && registrationActivation instanceof ValidatedRegistrationActivation) {
						validatedActivation = true;
						break;
					}
				}
			}
		}
		return validatedActivation;
	}

	public boolean isRegistrationActivationIdCountryMatchActivation() {
		boolean validatedActivation = false;
		if (NEW_COUNTRY_MATCH_ACTIVATION.equals(registrationActivationId)) {
			validatedActivation = true;
		} else {
			if (CollectionUtils.isNotEmpty(registrationActivations)) {
				for (RegistrationActivation registrationActivation : registrationActivations) {
					if (registrationActivation.getId().equals(registrationActivationId) && registrationActivation instanceof CountryMatchRegistrationActivation) {
						validatedActivation = true;
						break;
					}
				}
			}
		}
		return validatedActivation;
	}

	public void setRegistrationActivations(final List<RegistrationActivation> registrationActivations) {
		this.registrationActivations = registrationActivations;
	}

	public String getValidator() {
		return validator;
	}

	public void setValidator(final String validator) {
		this.validator = validator;
	}

	public LicenceType getLicenceType() {
		return licenceType;
	}

	public void setLicenceType(LicenceType licenceType) {
		this.licenceType = licenceType;
	}

	public List<LicenceType> getAllowedLicenceTypes() {
		List<LicenceType> licenceTypes = new ArrayList<LicenceType>();
		licenceTypes.addAll(Arrays.asList(LicenceType.values()));
		licenceTypes.remove(LicenceType.STANDARD);
		return licenceTypes;
	}

	public LocalDate getLicenceStartDate() {
		return licenceStartDate;
	}

	public void setLicenceStartDate(final LocalDate licenceStartDate) {
		this.licenceStartDate = licenceStartDate;
	}

	public LocalDate getLicenceEndDate() {
		return licenceEndDate;
	}

	public void setLicenceEndDate(final LocalDate licenceEndDate) {
		this.licenceEndDate = licenceEndDate;
	}

	public String getTotalConcurrency() {
		return totalConcurrency;
	}

	public void setTotalConcurrency(final String totalConcurrency) {
		this.totalConcurrency = totalConcurrency;
	}

	public String getUserConcurrency() {
		return userConcurrency;
	}

	public void setUserConcurrency(final String userConcurrency) {
		this.userConcurrency = userConcurrency;
	}

	public String getTimePeriod() {
		return timePeriod;
	}

	public void setTimePeriod(final String timePeriod) {
		this.timePeriod = timePeriod;
	}

	public RollingUnitType getRollingUnitType() {
		return rollingUnitType;
	}

	public void setRollingUnitType(final RollingUnitType rollingUnitType) {
		this.rollingUnitType = rollingUnitType;
	}

	public List<RollingUnitType> getAllowedRollingUnitTypes() {
		List<RollingUnitType> rollingUnitTypes = new ArrayList<RollingUnitType>();
		rollingUnitTypes.addAll(Arrays.asList(RollingUnitType.values()));
		rollingUnitTypes.remove(RollingUnitType.SECOND);
		rollingUnitTypes.remove(RollingUnitType.MILLISECOND);
		return rollingUnitTypes;
	}

	public RollingBeginOn getRollingBeginOn() {
		return rollingBeginOn;
	}

	public void setRollingBeginOn(final RollingBeginOn rollingBeginOn) {
		this.rollingBeginOn = rollingBeginOn;
	}

	public RollingBeginOn[] getRollingBeginOns() {
		return RollingBeginOn.values();
	}

	public String getLicenceAllowedUsages() {
		return licenceAllowedUsages;
	}

	public void setLicenceAllowedUsages(final String licenceAllowedUsages) {
		this.licenceAllowedUsages = licenceAllowedUsages;
	}

	public List<ExternalProductId> getExternalIds() {
		//product de-duplication
		/*List<ExternalProductId> externalIds = new ArrayList<ExternalProductId>();
		if (product != null) {
			externalIds.addAll(product.getExternalIds());
		}*/
		return externalIds;
	}

	public void addToExternalSystemMap(final ExternalSystem externalSystem, final List<ExternalSystemIdType> externalSystemIdTypes) {
		externalSystemMap.put(externalSystem, externalSystemIdTypes);
	}

	public Map<ExternalSystem, List<ExternalSystemIdType>> getExternalSystemMap() {
		return externalSystemMap;
	}

	public void addToPlatformMap(final Integer platformId, final Platform platform) {
		platformMap.put(platformId,platform);
	}
	public Map<Integer, Platform> getPlatformMap() {
		return platformMap ;
	}

	public void setPlatformMap(Map<Integer, Platform> platformMap) {
		this.platformMap = platformMap;
	}

	public void setExternalSystemMap(final Map<ExternalSystem, List<ExternalSystemIdType>> externalSystemMap) {
		this.externalSystemMap = externalSystemMap;
	}

	public String getExternalIdsToAdd() {
		return externalIdsToAdd;
	}

	public void setExternalIdsToAdd(final String externalIdsToAdd) {
		this.externalIdsToAdd = externalIdsToAdd;
	}

	public String getExternalIdsToUpdate() {
		return externalIdsToUpdate;
	}

	public void setExternalIdsToUpdate(final String externalIdsToUpdate) {
		this.externalIdsToUpdate = externalIdsToUpdate;
	}

	public String getExternalIdsToRemove() {
		return externalIdsToRemove;
	}

	public void setExternalIdsToRemove(final String externalIdsToRemove) {
		this.externalIdsToRemove = externalIdsToRemove;
	}

	public List<LinkedProductNew> getLinkedProducts() {
		//product de-duplication
		/*List<LinkedProductNew> linkedProducts = new ArrayList<LinkedProductNew>();
		if (product != null) {
			linkedProducts.addAll(this.linkedProducts);
		}
		Collections.sort(linkedProducts, new Comparator<LinkedProductNew>() {
			@Override
			public int compare(LinkedProductNew o1, LinkedProductNew o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});*/
		return this.linkedProducts;
	}

	public String getLinkedProductsToAdd() {
		return linkedProductsToAdd;
	}

	public void setLinkedProductsToAdd(final String linkedProductsToAdd) {
		this.linkedProductsToAdd = linkedProductsToAdd;
	}

	public String getLinkedProductsToUpdate() {
		return linkedProductsToUpdate;
	}

	public void setLinkedProductsToUpdate(final String linkedProductsToUpdate) {
		this.linkedProductsToUpdate = linkedProductsToUpdate;
	}

	public String getLinkedProductsToRemove() {
		return linkedProductsToRemove;
	}

	public void setLinkedProductsToRemove(final String linkedProductsToRemove) {
		this.linkedProductsToRemove = linkedProductsToRemove;
	}

	/*public ActivationMethod[] getActivationMethods() {
		return ActivationMethod.values();
	}*/

	public boolean isSystemAdmin() {
		boolean systemAdmin = false;
		for (GrantedAuthority authority : SecurityContextHolder.getContext().getAuthentication().getAuthorities()) {
			if (authority.getAuthority().equals("ROLE_SYSTEM_ADMIN")) {
				systemAdmin = true;
				break;
			}
		}
		return systemAdmin;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public String getUnmatchedRegistrationActivationId() {
		return unmatchedRegistrationActivationId;
	}

	public void setUnmatchedRegistrationActivationId(final String unmatchedRegistrationActivationId) {
		this.unmatchedRegistrationActivationId = unmatchedRegistrationActivationId;
	}

	public RegistrationActivation getUnmatchedRegistrationActivation() {
		RegistrationActivation registrationActivation = null;
		for (RegistrationActivation regAct : registrationActivations) {
			if (regAct.getId().equals(unmatchedRegistrationActivationId)) {
				registrationActivation = regAct;
				break;
			}
		}
		return registrationActivation;
	}

	public String getMatchedRegistrationActivationId() {
		return matchedRegistrationActivationId;
	}

	public void setMatchedRegistrationActivationId(final String matchedRegistrationActivationId) {
		this.matchedRegistrationActivationId = matchedRegistrationActivationId;
	}

	public RegistrationActivation getMatchedRegistrationActivation() {
		RegistrationActivation registrationActivation = null;
		for (RegistrationActivation regAct : registrationActivations) {
			if (regAct.getId().equals(matchedRegistrationActivationId)) {
				registrationActivation = regAct;
				break;
			}
		}
		return registrationActivation;
	}

	public String getLocaleList() {
		return localeList;
	}

	public void setLocaleList(final String localeList) {
		this.localeList = localeList;
	}

	public List<String> getLinkedProductsToRemoveErightsId() {
		return linkedProductsToRemoveErightsId;
	}

	public void setLinkedProductsToRemoveErightsId(
			List<String> linkedProductsToRemoveErightsId) {
		this.linkedProductsToRemoveErightsId = linkedProductsToRemoveErightsId;
	}

	public ProductState getState() {
		return state;
	}

	public void setState(ProductState state) {
		this.state = state;
	}

	public List<ProductState> getStates() {
		return states;
	}

	public void setStates(List<ProductState> states) {
		this.states = states;
	}
	
	public String getUrlIndexesToRemove() {
		return urlIndexesToRemove;
	}


	public void setUrlIndexesToRemove(String urlIndexesToRemove) {
		this.urlIndexesToRemove = urlIndexesToRemove;
	}

	private List<EnforceableProductUrlDto> createEmptyUrls() {
		List<EnforceableProductUrlDto> newUrls = new ArrayList<EnforceableProductUrlDto>();
		for (int i = 0; i < 100; i++) {
			newUrls.add(new EnforceableProductUrlDto());
		}
		return newUrls;
	}

	private boolean isEmpty(final EnforceableProductUrlDto url) {
		return StringUtils.isBlank(url.getHost()) 
				&& StringUtils.isBlank(url.getProtocol()) 
				&& StringUtils.isBlank(url.getPath())
				&& StringUtils.isBlank(url.getQuery()) 
				&& StringUtils.isBlank(url.getExpression()) 
				&& StringUtils.isBlank(url.getFragment());
	}

	private List<EnforceableProductUrlDto> getUrlsToKeep() {
		List<EnforceableProductUrlDto> urlsToKeep = new ArrayList<EnforceableProductUrlDto>();
		int[] urlIndexesToRemove = getUrlIndexesToRemoveAsArray();
		for (int i = 0; i < urls.size(); i++) {
			if (!ArrayUtils.contains(urlIndexesToRemove, i)) {
				if(!isEmpty(urls.get(i))){
					urlsToKeep.add(urls.get(i));
				}
			}
		}
		return urlsToKeep;
	}

	int[] getUrlIndexesToRemoveAsArray() {
		if (StringUtils.isNotBlank(urlIndexesToRemove)) {
			String[] urlIndexStrs = urlIndexesToRemove.split(",");
			int[] urlIndexes = new int[urlIndexStrs.length];
			for (int i = 0; i < urlIndexStrs.length; i++) {
				urlIndexes[i] = Integer.parseInt(urlIndexStrs[i].trim());
			}
			return urlIndexes;
		}
		return new int[0];
	}


	public List<EnforceableProductUrlDto> getUrls() {
		return urls;
	}


	public void setUrls(List<EnforceableProductUrlDto> urls) {
		this.urls = urls;
	}
	
	public static class ProductData {
        private final String productId;
        private final RegistrationDefinitionType regDefType;
        private final RegisterableType registerableType;
        private final RegistrationActivation registrationActivation;
        
        public ProductData(String productId, RegistrationDefinitionType  regDefType, RegisterableType registerableType,
        		RegistrationActivation registrationActivation) {
            this.productId = productId;
            this.regDefType = regDefType;
            this.registerableType = registerableType;
            this.registrationActivation = registrationActivation;
        }
        
        public String getProductId() {
            return productId;
        }
        
        public RegisterableType getRegisterableType() {
            return registerableType;
        }
        
        public RegistrationActivation getRegistrationActivation() {
            return registrationActivation;
        }
        
        public RegistrationDefinitionType getRegDefType() {
            return regDefType;
        }
        
        @Override
        public boolean equals(Object o){
            if(o instanceof ProductData == false){
                return false;
            }
            ProductData other = (ProductData)o;
            EqualsBuilder eb = new EqualsBuilder()
                .append(this.regDefType, other.regDefType)
                .append(this.productId, other.productId)
                .append(this.registrationActivation, other.registrationActivation)
                .append(this.registerableType, other.registerableType);
            boolean result =  eb.isEquals();
            return result;
        }
	}
	
	
	
	public List<Division> getDivisions() {
		return divisions;
	}


	public void setDivisions(List<Division> divisions) {
		this.divisions = divisions;
	}


	public Set<ProductData> getProductData() {
		return productData;
	}


	public Set<LinkedProductNew> getDirectLinkedProducts() {
		return directLinkedProducts;
	}


	public Set<LinkedProduct> getIndirectLinkedProducts() {
		return indirectLinkedProducts;
	}

	public List<Platform> getPlatformList() {
		return platformList;
	}

	public void setPlatformList(List<Platform> platformList) {
		this.platformList = platformList;
	}

	public String getPlatformToAdd() {
		return platformToAdd;
	}

	public void setPlatformToAdd(String platformToAdd) {
		this.platformToAdd = platformToAdd;
	}
	
	
	
}
