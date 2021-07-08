package com.oup.eac.ws.v2.service.impl;

/**
 * This interface stores the error message text ( and error message formats ) used in web service responses.
 *  
 * @author David Hay
 *
 */
public interface WebServiceMessages {

    //CANNOT BE BLANK
    public static final String ERR_PRODUCTID_CANNOT_BE_BLANK = "The product id cannot be blank.";
    public static final String ERR_USERID_CANNOT_BE_BLANK = "The user id cannot be blank.";
    public static final String ERR_USERNAME_CANNOT_BE_BLANK = "The username cannot be blank.";
    public static final String ERR_PASSWORD_CANNOT_BE_BLANK = "The password cannot be blank.";
    public static final String ERR_EMAIL_CANNOT_BE_BLANK = "The email address cannot be blank.";
    public static final String ERR_LOCALE_LANGUAGE_CANNOT_BE_BLANK = "The locale language cannot be blank.";
    public static final String ERR_LOCALE_COUNTRY_CANNOT_BE_BLANK = "The locale country cannot be blank.";
    public static final String ERR_SESSION_CANNOT_BE_BLANK = "The session token cannot be blank.";
    
    //INVALID
    public static final String ERR_EMAIL_IS_INVALID = "The email address is invalid.";
    public static final String ERR_USERNAME_IS_INVALID = "The username is invalid.";
    public static final String ERR_LANGUAGE_INVALID = "The language is not valid : ";
    public static final String ERR_COUNTRY_INVALID = "The country is not valid : ";
    public static final String ERR_TIME_ZONE_ID_INVALID = "The Time Zone ID is not valid : ";
    public static final String ERR_CUSTOMER_TYPE_INVALID = "The customer type is not valid : ";    
    
    //UNSUPPORTED
    public static final String ERR_IP_CREDENTIALS_UNSUPPORTED = "IP Credentials are not supported.";
    public static final String ERR_IP_ADDRESS_UNSUPPORTED = "IP Address is not supported.";

    //NOT FOUND
    public static final String ERR_CUSTOMER_NOT_FOUND = "No customer found.";
    public static final String ERR_CUSTOMER_NOT_FOUND_FOR_USERID = "No customer found for User Id : ";
    public static final String ERR_CUSTOMER_NOT_FOUND_FOR_USERNAME = "No customer found for User Name : ";
    public static final String ERR_CUSTOMER_NOT_FOUND_FOR_SESSION = "No customer found for Session Token : ";
    
    //MISC
    public static final String ERR_MSG_EXTERNAL =  "The 3 parts of the External Identifer must not be blank.";
    public static final String ERR_USERNAME_ALREADY_TAKEN = "The username is already taken.";
    public static final String ERR_OLD_AND_NEW_PASSWORDS_MATCH = "The new password cannot be the same as the old password.";
    public static final String ERR_USER_CONCURRENCY_NEGATIVE = "The user concurrency cannot be negative : ";
    public static final String ERR_USER_CONCURRENCY_LESS_THAN_DEFAULT = "Specific concurrency can not be less than default concurrency value.";
    

    //MESSAGE FORMATS
    public static final String FMT_CUSTOMER_NOT_FOUND_FOR_EXTERNAL_ID = "No customer found for External Id : systemId[%s] typeId[%s] id[%s]";
    public static final String FMT_PRODUCT_NOT_FOUND_FOR_EXTERNAL_ID = "No product found for External Id : systemId[%s] typeId[%s] id[%s]";
    public static final String FMT_PRODUCT_FOR_ID_NOT_FOUND = "The Product for Id [%s] cannot be found.";
    
    //PRODUCT REGISTRATION VALIDATION ERROR
    public static final String ERR_FMT_DUPLICATE_REGISTRATION_KEYS = "The following registration keys are duplicated : %s";
 
    public static final String ERR_NOT_REGISTERABLE_PRODUCT = "The product is not a registerable product";
    
    public static final String ERR_PRODUCT_REGISTRATION_CREATION_UNEXPECTED = "An unexpected problem occurred attempting to create a product registration";
    
    public static final String ERR_CANNOT_REGISTER_FOR_THIS_PRODUCT = "You cannot register for this product via the EAC Web Service";

}
