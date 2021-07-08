package com.oup.eac.admin.validators;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.LocaleUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.oup.eac.common.utils.email.InternationalEmailAddress;
import com.oup.eac.common.utils.url.URLUtils;
import com.oup.eac.domain.CountryMatchRegistrationActivation;
import com.oup.eac.domain.ExternalProductId;
import com.oup.eac.domain.ExternalSystem;
import com.oup.eac.domain.ExternalSystemIdType;
import com.oup.eac.domain.LicenceTemplate.LicenceType;
import com.oup.eac.domain.Platform;
import com.oup.eac.domain.Product;
import com.oup.eac.domain.RegistrationActivation;
import com.oup.eac.domain.beans.ProductBean;

@Component
public class ProductBeanValidator implements Validator {

	
    public ProductBeanValidator() {
        super();
    }
   
    
    
    @Override
	public void validate(final Object target, final Errors errors) {
		ProductBean productBean = (ProductBean) target;
		
		validateProductName(productBean, errors);
		//validateProductId(productBean, errors);
		validateRegistrationType(productBean, errors);
		validateHomePageUrl(productBean, errors);
		validateLandingPage(productBean, errors);
		validateEmail(productBean, errors);
		validateValidatedRegistrationActivation(productBean, errors);
		validateCountryMatchRegistrationActivation(productBean, errors);
		validateLinkedProducts(productBean, errors);
		validateLicenceProperties(productBean, errors);
		validateNewExternalIds(productBean, errors);
		validateSelectedDivision(productBean,errors);
		validatePlatformAndAddToObject(productBean,errors);
	}
    private void validatePlatformAndAddToObject(final ProductBean productBean,final Errors errors) {
    	if (productBean.getPlatformToAdd() != null && !productBean.getPlatformToAdd().isEmpty()) {
    		String RECORD_SEPARATOR = "\\|";
    		boolean platformNotEmpty = true ;
    		List<Platform> platformList = new ArrayList<Platform>() ;
	    	String[] records = productBean.getPlatformToAdd().split(RECORD_SEPARATOR) ;
	    	for (String record : records) {
	    		String[] platform = splitIntoFields(record);
	    		if (platform[0] == null && platform[0].isEmpty()){
	    			errors.rejectValue("", "error.emptyPlatformId");
	    			platformNotEmpty = false ;
	    		} 
	    		if ( platform[1] == null && platform[1].isEmpty()) {
	    			errors.rejectValue("", "error.emptyPlatformCode");
	    			platformNotEmpty = false ;
	    		} 
	    		if ( platform[2] == null && platform[2].isEmpty()) {
	    			errors.rejectValue("", "error.emptyPlatformName");
	    			platformNotEmpty = false ;
	    		}
	    		if ( platformNotEmpty ) {
		    		Platform platformObj = new Platform();
		    		platformObj.setPlatformId(Integer.parseInt(platform[0]));
		    		platformObj.setCode(platform[1]);
		    		platformObj.setName(platform[2]);
		    		platformList.add(platformObj);
	    		}
	    	}
	    	productBean.setPlatformList(platformList);
    	}
    }
    private void validateSelectedDivision(final ProductBean productBean,final Errors errors){
    	if (StringUtils.isBlank(productBean.getDivisionId())) {
			errors.rejectValue("divisionId", "error.emptyDivision");
		}
    }
    private void validateProductName(final ProductBean productBean, final Errors errors) {
    	if (StringUtils.isBlank(productBean.getProductName())) {
			errors.rejectValue("productName", "error.emptyProductName");
		}	
	}

	/*private void validateProductId(final ProductBean productBean, final Errors errors) {
		if (StringUtils.isBlank(productBean.getProductId())) {
			errors.rejectValue("productId", "error.emptyProductId");
		}
	}*/

	private void validateRegistrationType(final ProductBean productBean, final Errors errors) {
		if (StringUtils.isBlank(productBean.getRegistrationType())) {
			errors.rejectValue("registrationType", "error.emptyRegistrationType");
		}
	}

	private void validateHomePageUrl(final ProductBean productBean, final Errors errors) {
		if (StringUtils.isNotBlank(productBean.getHomePage())) {
			try {
				URLUtils.safeEncode(productBean.getHomePage());
			} catch (Exception e) {
				errors.rejectValue("homePage", "error.homePage.malformedUrl");
			}
		}
	}

	private void validateLandingPage(final ProductBean productBean, final Errors errors) {
		if (StringUtils.isNotBlank(productBean.getLandingPage())) {
			try {
				URLUtils.safeEncode(productBean.getLandingPage());
			} catch (Exception e) {
				errors.rejectValue("landingPage", "error.landingPage.malformedUrl");
			}
		}
	}

	private void validateEmail(final ProductBean productBean, final Errors errors) {
		if (StringUtils.isBlank(productBean.getEmail()) || !InternationalEmailAddress.isValid(productBean.getEmail())) {
			errors.rejectValue("email", "error.invalidEmail");
		}
	}

	private void validateValidatedRegistrationActivation(final ProductBean productBean, final Errors errors) {
		if (productBean.isRegistrationActivationIdValidatedActivation()
				&& (StringUtils.isBlank(productBean.getValidator()) || !InternationalEmailAddress.isValid(productBean.getValidator()))) {
			errors.rejectValue("email", "error.validatorInvalidEmail");
		}
	}
	
	private void validateCountryMatchRegistrationActivation(final ProductBean productBean, final Errors errors) {
		if (productBean.isRegistrationActivationIdCountryMatchActivation()) {
			validateDescription(productBean, errors);
			if (StringUtils.isBlank(productBean.getUnmatchedRegistrationActivationId())) {
				errors.rejectValue("unmatchedRegistrationActivation", "error.emptyUnmatchedActivation");
			}
			if (StringUtils.isBlank(productBean.getMatchedRegistrationActivationId())) {
				errors.rejectValue("matchedRegistrationActivation", "error.emptyMatchedActivation");
			}
			validateLocaleList(productBean, errors);
		}
	}

	private void validateDescription(final ProductBean productBean, final Errors errors) {
		if (StringUtils.isBlank(productBean.getDescription())) {
			errors.rejectValue("description", "error.emptyCountryMatchDescription");
		} else if (productBean.getDescription().length() > 100) {
			errors.rejectValue("description", "error.descriptionTooBig");
		} else {
			for (RegistrationActivation registrationActivation : productBean.getRegistrationActivations()) {
				if (registrationActivation instanceof CountryMatchRegistrationActivation) {
					CountryMatchRegistrationActivation countryMatchRegistrationActivation = (CountryMatchRegistrationActivation) registrationActivation;
					if (!StringUtils.equals(countryMatchRegistrationActivation.getId(), productBean.getRegistrationActivationId()) && descriptionsAreTheSame(productBean, countryMatchRegistrationActivation)) {
						errors.rejectValue("description", "error.descriptionAlreadyExists");
						break;
					}
				}
			}
		}
	}

	private boolean descriptionsAreTheSame(final ProductBean productBean, final CountryMatchRegistrationActivation countryMatchRegistrationActivation) {
		return productBean.getDescription().trim().toLowerCase().equals(countryMatchRegistrationActivation.getDescription().trim().toLowerCase());
	}

	private void validateLocaleList(final ProductBean productBean, final Errors errors) {
		if (StringUtils.isBlank(productBean.getLocaleList())) {
			errors.rejectValue("localeList", "error.localeListInvalid");
		} else {
			if (productBean.getLocaleList().length() > 255) {
				errors.rejectValue("localeList", "error.localeListTooBig");
			} else {
				String[] locales = productBean.getLocaleList().split(",");
				if (ArrayUtils.isEmpty(locales)) {
					errors.rejectValue("localeList", "error.localeListInvalid");
				} else {
					for (String localeStr : locales) {
						String trimmed = localeStr.trim();
						if (StringUtils.isNotBlank(trimmed)) {
							if (!isValidLocale(trimmed)) {
								errors.rejectValue("localeList", "error.localeListInvalid");
								break;
							}
						}
					}
				}
			}
		}
	}
	
	private boolean isValidLocale(final String localeStr) {
		boolean valid = true;
		try {
			Locale locale = LocaleUtils.toLocale(localeStr);
			if (StringUtils.isNotBlank(locale.getLanguage())) {
				valid = isValidLanguageCode(locale.getLanguage());
			}
			if (StringUtils.isNotBlank(locale.getCountry())) {
				valid = isValidCountryCode(locale.getCountry());
			}
		} catch (final Exception e) {
			valid = false;
		}
		return valid;
	}
	
	private boolean isValidLanguageCode(final String languageCode) {
		boolean validLanguageCode = false;
		for (Object localeObj : LocaleUtils.availableLocaleSet()) {
			Locale locale = (Locale) localeObj;
			if (StringUtils.equals(locale.getLanguage(), languageCode)) {
				validLanguageCode = true;
				break;
			}
		}
		return validLanguageCode;
	}
	
	private boolean isValidCountryCode(final String countryCode) {
		boolean validCountryCode = false;
		for (Object localeObj : LocaleUtils.availableLocaleSet()) {
			Locale locale = (Locale) localeObj;
			if (StringUtils.equals(locale.getCountry(), countryCode)) {
				validCountryCode = true;
				break;
			}
		}
		return validCountryCode;
	}
	
	private void validateLinkedProducts(final ProductBean productBean, final Errors errors) {
		String productId = productBean.getProductId();
		if (StringUtils.contains(productBean.getLinkedProductsToAdd(), productId) || StringUtils.contains(productBean.getLinkedProductsToUpdate(), productId)) {
			errors.rejectValue("productId", "error.selfReferentialAssociation");
		}
	}

	private void validateLicenceProperties(final ProductBean productBean, final Errors errors) {
		LicenceType licenceType = productBean.getLicenceType();

		if (LicenceType.CONCURRENT.equals(licenceType)) {
			LicencePropertyValidatorHelper.validateTotalConcurrency(productBean.getTotalConcurrency(), errors);
			LicencePropertyValidatorHelper.validateUserConcurrency(productBean.getUserConcurrency(), errors);
		} else if (LicenceType.ROLLING.equals(licenceType)) {
			LicencePropertyValidatorHelper.validateTimePeriod(productBean.getTimePeriod(), errors);
		} else if (LicenceType.USAGE.equals(licenceType)) {
			LicencePropertyValidatorHelper.validateLicenceAllowedUsages(productBean.getLicenceAllowedUsages(), errors);
		}
	}
	
	private void validateNewExternalIds(final ProductBean productBean, final Errors errors) {
	    
	  
	    Product product = productBean.getProduct();
	    List<ExternalProductId> externalProductIds = productBean.getExternalIds();
	    
	    Set<ExternalProductId> newExternalIds= new HashSet<ExternalProductId>();
	    String RECORD_SEPARATOR = "\\|";
        if (StringUtils.isNotBlank(productBean.getExternalIdsToAdd())) {
            String[] records = productBean.getExternalIdsToAdd().split(RECORD_SEPARATOR);
            for (String record : records) {
                
                String[] fields = splitIntoFields(record);
                
                ExternalProductId externalProductId = new ExternalProductId();
                
                externalProductId.setExternalId(fields[0].trim());
                externalProductId.setId(fields[0].trim());
                externalProductId.setProduct(product);
                ExternalSystemIdType externalSystemIdType = new ExternalSystemIdType() ;
                externalSystemIdType.setName(fields[2].trim());
                externalSystemIdType.setId(fields[2].trim());
                ExternalSystem externalSystem = new ExternalSystem() ;
                externalSystem.setId(fields[1]);
                externalSystem.setName(fields[1]);
                externalSystemIdType.setExternalSystem(externalSystem);
                externalProductId.setExternalSystemIdType(externalSystemIdType);
                
                if(newExternalIds.contains(externalProductId.getExternalSystemIdType().getExternalSystem()) && 
                		(newExternalIds.contains(externalProductId.getExternalSystemIdType().getExternalSystem().getExternalSystemIdTypes()))){
                /*if(newExternalIds.contains(externalProductId)){*/
                    errors.rejectValue("", "error.duplicateExternalId");
                }else{
                    newExternalIds.add(externalProductId);  
                    if(externalProductIds.contains(externalProductId)){
                        errors.rejectValue("", "error.existsExternalId");
                    }
                }
                
            }
                
        }   
            
    }
        
    private String[] splitIntoFields(final String record) {
        String FIELD_SEPARATOR = ",";
        String[] fields = record.trim().split(FIELD_SEPARATOR);
        try{
            for (int i = 0; i < fields.length; i++) {
                fields[i] = URLDecoder.decode(fields[i], "UTF-8");
            }
            
        } catch(UnsupportedEncodingException uee){
            uee.printStackTrace();
        }
        
        return fields; 
    }        

	@Override
	public boolean supports(final Class<?> clazz) {
		final boolean supports = ProductBean.class.isAssignableFrom(clazz);
		return supports;
	}

}
