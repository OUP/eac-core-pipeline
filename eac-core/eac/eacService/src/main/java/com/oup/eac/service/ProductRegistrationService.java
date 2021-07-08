package com.oup.eac.service;

import java.util.Map;

import com.oup.eac.domain.Customer;
import com.oup.eac.domain.Field;
import com.oup.eac.domain.ProductRegistrationDefinition;
import com.oup.eac.domain.RegisterableProduct;
import com.oup.eac.domain.Registration;
import com.oup.eac.dto.EnforceableProductDto;

public interface ProductRegistrationService {

    //PRODUCT REGISTRATION VALIDATION ERRORS
    public static final String ERR_INVALID_REGISTRATION_DEFINITION_TYPE = "The Registration Definition Type for this product is not PRODUCT_REGISTRATION";
    public static final String ERR_NO_REGISTRATION_INFORMATION_REQUIRED = "Registration Information is invalid for this product registration";
    public static final String ERR_FMT_INVALID_REGISTRATION_KEYS = "The following registration keys are invalid : %s";
    public static final String ERR_FMT_REQUIRED_REGISTRATION_KEYS_BLANK = "The values of the following required registration keys cannot be blank : %s";
    public static final String ERR_FMT_PRODUCT_REGISTRATION_VALUE_REGEX = "The value for registration key [%s] must meet the regular expression : %s";
    public static final String ERR_FMT_REGISTRATION_VALUE_INVALID = "The value for registration key [%s] must be one of : %s";
    public static final String ERR_FMT_REGISTRATION_VALUES_INVALID = "The values for registration key [%s] must come from : %s";
    
    //PRODUCT REGISTRATION UNEXPECTED ERROR
    public static final String ERR_PRODUCT_REGISTRATION_CREATION_UNEXPECTED = "An unexpected problem occurred attempting to create a product registration";
    public static final String ERR_FMT_PRODUCT_REGISTRATION_UNEXPECTED_TAG_TYPE = "Unexpected tag type : %s ";

    /**
     * Creates the product registration.
     * The registration keys in the web service request map to the 'export name' in the Field class.
     * @param prodRegDef the prod reg def
     * @param customer the customer
     * @param registrationInformation the registration information
     * @throws ServiceLayerException the web service exception
     * @throws ServiceLayerException the service layer exception
     * @see Field
     */
    Registration<?> createProductRegistration(EnforceableProductDto product, Customer customer, Map<String, String> registrationPageData) throws ServiceLayerException;

    ProductRegistrationDefinition getProductRegistrationDefinition(RegisterableProduct product) throws ServiceLayerException;

}