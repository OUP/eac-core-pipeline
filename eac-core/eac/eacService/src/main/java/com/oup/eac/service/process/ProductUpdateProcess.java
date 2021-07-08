package com.oup.eac.service.process;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.oup.eac.domain.AccountPageDefinition;
import com.oup.eac.domain.AccountRegistrationDefinition;
import com.oup.eac.domain.ActivationCodeRegistrationDefinition;
import com.oup.eac.domain.ConcurrentLicenceTemplate;
import com.oup.eac.domain.CountryMatchRegistrationActivation;
import com.oup.eac.domain.ExternalProductId;
import com.oup.eac.domain.InstantRegistrationActivation;
import com.oup.eac.domain.LicenceTemplate;
import com.oup.eac.domain.LicenceTemplate.LicenceType;
import com.oup.eac.domain.LinkedProduct;
/*import com.oup.eac.domain.LinkedProduct.ActivationMethod;*/
import com.oup.eac.domain.Product;
import com.oup.eac.domain.ProductPageDefinition;
import com.oup.eac.domain.ProductRegistrationDefinition;
import com.oup.eac.domain.RegisterableProduct;
import com.oup.eac.domain.RegistrationActivation;
import com.oup.eac.domain.RegistrationDefinition;
import com.oup.eac.domain.RollingLicenceTemplate;
import com.oup.eac.domain.UsageLicenceTemplate;
import com.oup.eac.domain.ValidatedRegistrationActivation;
import com.oup.eac.domain.beans.ProductBean;
import com.oup.eac.service.ProductService;
import com.oup.eac.service.ServiceLayerException;

/**
 * Stateful helper class that creates or updates a {@link Product} and associated {@link RegistrationDefinition}s given
 * a {@link ProductBean}.
 * 
 * @author keelingw
 *
 */
public class ProductUpdateProcess {
	
	private static final String RECORD_SEPARATOR = "\\|";
	private static final String FIELD_SEPARATOR = ",";
    //product de-duplication
	/*private final ExternalIdService externalIdService;
	private final DivisionService divisionService;*/
	
	private Product product;
	private AccountRegistrationDefinition accountRegistrationDefinition;
	private ProductRegistrationDefinition productRegistrationDefinition;
	private ProductService productService;

	public ProductUpdateProcess(final ProductService productService) {
        //product de-duplication
		/*
		 * de-duplication
		this.externalIdService = externalIdService;
		this.productService = productService;
		this.divisionService = divisionService ;*/
		this.productService = productService;
	}
	
	public void process(final ProductBean productBean) throws UnsupportedEncodingException, ServiceLayerException {
		product = productBean.getProduct() ;
		accountRegistrationDefinition = productBean.getAccountRegistrationDefinition();
		productRegistrationDefinition = productBean.getProductRegistrationDefinition();
		
		if (product == null) {
			product = new RegisterableProduct();
			product.setId(productBean.getProductId());
			//product.setErightsId(productBean.getErightsId());
			accountRegistrationDefinition = new AccountRegistrationDefinition();
			accountRegistrationDefinition.setRegistrationActivation(getInstantRegistrationActivation(productBean));
			if (productBean.isActivationCode()) {
				productRegistrationDefinition = new ActivationCodeRegistrationDefinition();
			} else {
				productRegistrationDefinition = new ProductRegistrationDefinition();
			}
			accountRegistrationDefinition.setProduct(product);
			productRegistrationDefinition.setProduct(product);
            //product de-duplication
			//productRegistrationDefinition.setLicenceTemplate(createLicenceTemplate(productBean));
		}
        //product de-duplication
		//updateProductProperties(productBean);
		updateAccountRegistrationDefinitionProperties(productBean);
		updateProductRegistrationDefinitionProperties(productBean);
	}
	
	public Product getUpdatedProduct() {
		return product;
	}
	
	public Product getProduct() {
		return product;
	}
	
	public AccountRegistrationDefinition getUpdatedAccountRegistrationDefinition() {
		return accountRegistrationDefinition;
	}
	
	public ProductRegistrationDefinition getUpdatedProductRegistrationDefinition() {
		return productRegistrationDefinition;
	}
	
	private InstantRegistrationActivation getInstantRegistrationActivation(final ProductBean productBean) {
		InstantRegistrationActivation instantRegistrationActivation = null;
		for (RegistrationActivation registrationActivation : productBean.getRegistrationActivations()) {
			if (registrationActivation instanceof InstantRegistrationActivation) {
				instantRegistrationActivation = (InstantRegistrationActivation) registrationActivation;
				break;
			}
		}
		return instantRegistrationActivation;
	}

	private LicenceTemplate createLicenceTemplate(final ProductBean productBean) {
		LicenceType licenceType = productBean.getLicenceType();
		if (LicenceType.CONCURRENT == licenceType) return new ConcurrentLicenceTemplate();
		if (LicenceType.ROLLING == licenceType) return new RollingLicenceTemplate();
		if (LicenceType.USAGE == licenceType) return new UsageLicenceTemplate();
		throw new IllegalStateException("Unsupported licence type: " + licenceType);
	}
		//unused method
	
	/*private void updateProductProperties(final ProductBean productBean) throws UnsupportedEncodingException, ServiceLayerException {		
        //product de-duplication
		//product.setRegisterableType(productBean.getRegisterableType());
		product.setHomePage(productBean.getHomePage());
		product.setLandingPage(productBean.getLandingPage());
		product.setEmail(productBean.getEmail());
		product.setServiceLevelAgreement(productBean.getSla());
		product.setProductName(productBean.getProductName());
		//product.setErightsId(productBean.getErightsId());
		product.setState(productBean.getState());
        //product de-duplication
		//product.setDivision(divisionService.getDivisionById(productBean.getDivisionId()));
		updateProductWithUpdatedExternalIds(productBean);
		updateProductWithNewExternalIds(productBean);
		updateProductRemovingAnyExternalIds(productBean);
		
		updateProductWithUpdatedLinkedProducts(productBean);
		updateProductWithNewLinkedProducts(productBean);
		updateProductRemovingAnyLinkedProducts(productBean);
	}*/
	
	private void updateProductWithUpdatedExternalIds(final ProductBean productBean) throws UnsupportedEncodingException {
		String externalIdsToUpdate = productBean.getExternalIdsToUpdate();
		if (StringUtils.isNotBlank(externalIdsToUpdate)) {
			String[] records = externalIdsToUpdate.split(RECORD_SEPARATOR);
			for (String record : records) {
				String[] fields = splitIntoFields(record);
				ExternalProductId externalId = getExternalProductIdFromProduct(fields[0].trim());
				if (externalId != null) {
					externalId.setExternalId(fields[1].trim());
				}
			}
		}
	}
	
	private String[] splitIntoFields(final String record) throws UnsupportedEncodingException {
		String[] fields = record.trim().split(FIELD_SEPARATOR);
		for (int i = 0; i < fields.length; i++) {
			fields[i] = URLDecoder.decode(fields[i], "UTF-8");
		}
		return fields;
	}
	
	private ExternalProductId getExternalProductIdFromProduct(final String guid) {
		ExternalProductId externalIdToReturn = null;
		for (ExternalProductId externalId : product.getExternalIds()) {
			if (externalId.getId().equals(guid)) {
				externalIdToReturn = externalId;
				break;
			}
		}
		return externalIdToReturn;
	}
	
	private void updateProductWithNewExternalIds(final ProductBean productBean) throws UnsupportedEncodingException {
		Set<ExternalProductId> externalIdsToAdd = new HashSet<ExternalProductId>();
		if (StringUtils.isNotBlank(productBean.getExternalIdsToAdd())) {
			String[] records = productBean.getExternalIdsToAdd().split(RECORD_SEPARATOR);
			for (String record : records) {
				String[] fields = splitIntoFields(record);
				ExternalProductId externalProductId = new ExternalProductId();
				externalProductId.setExternalId(fields[0].trim());
				externalProductId.setProduct(product);
                //product de-duplication
				/*ExternalSystemIdType externalSystemIdType = externalIdService.getExternalSystemIdTypeById(fields[2].trim());
				externalProductId.setExternalSystemIdType(externalSystemIdType);
				externalIdsToAdd.add(externalProductId);*/
			}
			product.getExternalIds().addAll(externalIdsToAdd);
		}
	}
	
	private void updateProductRemovingAnyExternalIds(final ProductBean productBean) throws UnsupportedEncodingException {
		String externalIdsToRemove = productBean.getExternalIdsToRemove();
		if (StringUtils.isNotBlank(externalIdsToRemove)) {
			String[] records = externalIdsToRemove.split(RECORD_SEPARATOR);
			for (String record : records) {
				for (Iterator<ExternalProductId> iter = product.getExternalIds().iterator(); iter.hasNext();) {
					ExternalProductId externalId = iter.next();
					if (externalId.getId().equals(URLDecoder.decode(record.trim(), "UTF-8"))) {
						iter.remove();
						break;
					}
				}
			}
		}
	}
	
	private void updateAccountRegistrationDefinitionProperties(final ProductBean productBean) {
		String accountPageDefinitionId = productBean.getAccountPageDefinitionId();
		if (StringUtils.isBlank(accountPageDefinitionId)) {
			accountRegistrationDefinition.setPageDefinition(null);
		} else {
			if(productBean!=null && productBean.getAccountPageDefinitions()!=null){
			for (AccountPageDefinition pageDefinition : productBean.getAccountPageDefinitions()) {
				if (pageDefinition.getId().equals(accountPageDefinitionId)) {
					accountRegistrationDefinition.setPageDefinition(pageDefinition);
					break;
				}
			}
		  }
		}
		accountRegistrationDefinition.setValidationRequired(productBean.isEmailValidation());
	}
	
	private void updateProductRegistrationDefinitionProperties(final ProductBean productBean) {
		String productPageDefinitionId = productBean.getProductPageDefinitionId();
		if (StringUtils.isBlank(productPageDefinitionId)) {
			productRegistrationDefinition.setPageDefinition(null);
		} else {
			if(productBean!=null && productBean.getAccountPageDefinitions()!=null){
			for (ProductPageDefinition pageDefinition : productBean.getProductPageDefinitions()) {
				if (pageDefinition.getId().equals(productPageDefinitionId)) {
					productRegistrationDefinition.setPageDefinition(pageDefinition);
					break;
				}
			}
		  }
		}
		productRegistrationDefinition.setConfirmationEmailEnabled(Boolean.valueOf(productBean.isEmailConfirmationEnabled()));
        //product de-duplication
		//updateRegistrationActivation(productBean);
		//updateLicenceTemplate(productBean);
	}
	
	private void updateRegistrationActivation(final ProductBean productBean) {
		if (ProductBean.NEW_VALIDATOR_ACTIVATION.equals(productBean.getRegistrationActivationId())) {
			updateRegistrationActivationForNewValidator(productBean);
		} else if (ProductBean.NEW_COUNTRY_MATCH_ACTIVATION.equals(productBean.getRegistrationActivationId())) {
			updateRegistrationActivationForNewCountryMatch(productBean);
		} else {
			updateExistingRegistrationActivation(productBean);
		}
	}

	private void updateRegistrationActivationForNewValidator(final ProductBean productBean) {
		boolean foundValidator = false;
		for (RegistrationActivation registrationActivation : productBean.getRegistrationActivations()) {
			if (registrationActivation instanceof ValidatedRegistrationActivation
					&& productBean.getValidator().trim().toLowerCase().equals(((ValidatedRegistrationActivation) registrationActivation).getValidatorEmail().toLowerCase())) {
				productRegistrationDefinition.setRegistrationActivation(registrationActivation);
				foundValidator = true;
				break;
			}
		}
		if (!foundValidator) {
			ValidatedRegistrationActivation registrationActivation = new ValidatedRegistrationActivation();
			registrationActivation.setValidatorEmail(productBean.getValidator());
			productRegistrationDefinition.setRegistrationActivation(registrationActivation);
		}
	}
	
	private void updateRegistrationActivationForNewCountryMatch(final ProductBean productBean) {
		CountryMatchRegistrationActivation countryMatchRegistrationActivation = new CountryMatchRegistrationActivation();
		countryMatchRegistrationActivation.setDescription(productBean.getDescription());
		countryMatchRegistrationActivation.setLocaleList(productBean.getLocaleList());
		countryMatchRegistrationActivation.setMatchedActivation(productBean.getMatchedRegistrationActivation());
		countryMatchRegistrationActivation.setUnmatchedActivation(productBean.getUnmatchedRegistrationActivation());
		productRegistrationDefinition.setRegistrationActivation(countryMatchRegistrationActivation);
	}
	
	private void updateExistingRegistrationActivation(final ProductBean productBean) {
		String registrationActivationId = productBean.getRegistrationActivationId();
		for (RegistrationActivation registrationActivation : productBean.getRegistrationActivations()) {
			if (registrationActivation.getId().equals(registrationActivationId)) {
				productRegistrationDefinition.setRegistrationActivation(registrationActivation);
				if (registrationActivation instanceof ValidatedRegistrationActivation) {
					((ValidatedRegistrationActivation) registrationActivation).setValidatorEmail(productBean.getValidator());
				}
				if (registrationActivation instanceof CountryMatchRegistrationActivation) {
					CountryMatchRegistrationActivation countryMatchRegistrationActivation = (CountryMatchRegistrationActivation) registrationActivation;
					countryMatchRegistrationActivation.setDescription(productBean.getDescription());
					countryMatchRegistrationActivation.setUnmatchedActivation(productBean.getUnmatchedRegistrationActivation());
					countryMatchRegistrationActivation.setMatchedActivation(productBean.getMatchedRegistrationActivation());
					countryMatchRegistrationActivation.setLocaleList(productBean.getLocaleList());
				}
				break;
			}
		}
	}
	
	private LicenceTemplate updateLicenceTemplate(final ProductBean productBean) {
		LicenceTemplate licenceTemplate = productRegistrationDefinition.getLicenceTemplate();
		LicenceType licenceType = licenceTemplate.getLicenceType();
		
		switch (licenceType) {
		case CONCURRENT:
			((ConcurrentLicenceTemplate) licenceTemplate).setUserConcurrency(Integer.parseInt(productBean.getUserConcurrency()));
			((ConcurrentLicenceTemplate) licenceTemplate).setTotalConcurrency(Integer.parseInt(productBean.getTotalConcurrency()));
			break;
		case ROLLING:
			((RollingLicenceTemplate) licenceTemplate).setTimePeriod(Integer.parseInt(productBean.getTimePeriod()));
			((RollingLicenceTemplate) licenceTemplate).setBeginOn(productBean.getRollingBeginOn());
			((RollingLicenceTemplate) licenceTemplate).setUnitType(productBean.getRollingUnitType());
			break;
		case USAGE:
			((UsageLicenceTemplate) licenceTemplate).setAllowedUsages(Integer.parseInt(productBean.getLicenceAllowedUsages()));
			break;
		}
		
		licenceTemplate.setStartDate(productBean.getLicenceStartDate());
		licenceTemplate.setEndDate(productBean.getLicenceEndDate());
		
		return licenceTemplate;
	}
	
	/*private void updateProductWithUpdatedLinkedProducts(final ProductBean productBean) throws UnsupportedEncodingException, ServiceLayerException {
		String linkedProductsToUpdate = productBean.getLinkedProductsToUpdate();
		if (StringUtils.isNotBlank(linkedProductsToUpdate)) {
			String[] records = linkedProductsToUpdate.split(RECORD_SEPARATOR);
			for (String record : records) {
				String[] fields = splitIntoFields(record);
				LinkedProduct linkedProduct = getLinkedProductFromProduct(fields[0].trim());
				if (linkedProduct != null) {
					
					linkedProduct.setLinkedProduct(productService.getProductById(fields[0].trim()));
					
					linkedProduct.setActivationMethod(ActivationMethod.valueOf(fields[2].trim()));
					Product LinkedProductUpdated = productService.getProductById(linkedProduct.getId());
					linkedProduct.setId(LinkedProductUpdated.getId());
					linkedProduct.setErightsId(LinkedProductUpdated.getErightsId());
					linkedProduct.setProductName(LinkedProductUpdated.getProductName());
				}
			}
		}
	}*/
	
	private LinkedProduct getLinkedProductFromProduct(final String guid) {
		LinkedProduct linkedProductToReturn = null;
		//PRODUCT de-duplication
		/*for (LinkedProduct linkedProduct : product.getLinkedProducts()) {
			if (linkedProduct.getLinkedProduct().getId().equals(guid)) {
				linkedProductToReturn = linkedProduct;
				break;
			}
		}*/
		return linkedProductToReturn;
	}
	//unused method 
	
	/*private void updateProductWithNewLinkedProducts(final ProductBean productBean) throws UnsupportedEncodingException, ServiceLayerException {
		Set<LinkedProduct> linkedProductsToAdd = new HashSet<LinkedProduct>();
		if (StringUtils.isNotBlank(productBean.getLinkedProductsToAdd())) {
			String[] records = productBean.getLinkedProductsToAdd().split(RECORD_SEPARATOR);
			for (String record : records) {
				String[] fields = splitIntoFields(record);
				LinkedProduct linkedProduct = new LinkedProduct();
				
				linkedProduct.setLinkedProduct(productService.getProductById(fields[0].trim()));
                //product de-duplication
				//linkedProduct.setRegisterableProduct(product);
				linkedProduct.setEmail(product.getEmail());
				linkedProduct.setServiceLevelAgreement(product.getServiceLevelAgreement());
				linkedProduct.setActivationMethod(ActivationMethod.valueOf(fields[1].trim()));
				linkedProduct.setLandingPage(product.getLandingPage());
				linkedProduct.setHomePage(product.getHomePage());
				
				linkedProductsToAdd.add(linkedProduct);
			}
            //product de-duplication
			//product.getLinkedProducts().addAll(linkedProductsToAdd);
		}
	}*/
	
	private void updateProductRemovingAnyLinkedProducts(final ProductBean productBean) throws UnsupportedEncodingException {
		String linkedProductsToRemove = productBean.getLinkedProductsToRemove();
		if (StringUtils.isNotBlank(linkedProductsToRemove)) {
			String[] records = linkedProductsToRemove.split(RECORD_SEPARATOR);
			for (String record : records) {
                //product de-duplication
				/*for (Iterator<LinkedProduct> iter = product.getLinkedProducts().iterator(); iter.hasNext();) {
					LinkedProduct linkedProduct = iter.next();
					if (linkedProduct.getLinkedProduct().getId().equals(URLDecoder.decode(record.trim(), "UTF-8"))) {
						iter.remove();
						break;
					}
				}*/
			}
		}
	}
}
