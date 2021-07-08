package com.oup.eac.service.util.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.StyledEditorKit.BoldAction;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.oup.eac.domain.Checkbox;
import com.oup.eac.domain.Component;
import com.oup.eac.domain.Element;
import com.oup.eac.domain.Field;
import com.oup.eac.domain.MultiSelect;
import com.oup.eac.domain.PageConstant;
import com.oup.eac.domain.Question;
import com.oup.eac.domain.Radio;
import com.oup.eac.domain.Select;
import com.oup.eac.domain.Tag;
import com.oup.eac.domain.TagOption;
import com.oup.eac.domain.TextField;
import com.oup.eac.dto.RegistrationDto;
import com.oup.eac.service.ProductRegistrationService;
import com.oup.eac.service.ServiceLayerException;
import com.oup.eac.service.exceptions.ServiceLayerValidationException;
import com.oup.eac.service.util.ProductRegistrationHelper;

/**
 * @author David Hay
 */
@org.springframework.stereotype.Component("productRegistrationHelper")
public class ProductRegistrationHelperImpl implements ProductRegistrationHelper {

    private  static final Logger LOG = Logger.getLogger(ProductRegistrationHelperImpl.class);

    private  static final String TRUE = Boolean.TRUE.toString(); // 'true'

    private  static final String FALSE = Boolean.FALSE.toString(); // 'false'

    private  static final List<String> TRUE_OR_FALSE = Arrays.asList(TRUE, FALSE);
    
    private static final String PADDED_COMMA_SEPARATOR = "\\s*,\\s*";


    /**
     * Gets the registration page config.
     *
     * @param registrationDto the registration dto
     * @return the registration page config
     */
    @Override
    public final Map<String, Field> getRegistrationPageConfig(final RegistrationDto registrationDto) {
        Map<String, Field> registrationPageConfig = new HashMap<String, Field>();
        
        // build up map of fields for this product registration page
        for (Component component : registrationDto.getComponents()) {
            for (Field f : component.getFields()) {
            	Question q = f.getElement().getQuestion();
            	String exportName = q.getDescription();
                if (StringUtils.isNotBlank(exportName)) {
                    registrationPageConfig.put(exportName, f);
                }
            }
        }
        return registrationPageConfig;
    }

    /**
     * Populate registration dto with validated data.
     * 
     * @param validRegData
     *            the valid reg data
     * @param registrationDto
     *            the registration dto
     */
    @Override
    public  final void populateRegistrationDtoWithValidatedData(final Map<Field, String> validRegData, final RegistrationDto registrationDto) {
        for (Map.Entry<Field, String> entry : validRegData.entrySet()) {
            Field f = entry.getKey();
            String answerText = entry.getValue();
            registrationDto.processAnswerText(f, answerText);
        }
    }

    /**
     * We check to see if the expected set of 'field export names' matches the supplied set of 'export names' and return an appropriate error otherwise.
     *
     * @param registrationPageConfig the registration page config
     * @param registrationPageData the registration page data
     * @return the validated registration page data
     * @throws ServiceLayerException the service layer exception
     */
    @Override
    public  final Map<Field, String> getValidatedRegistrationPageData(
            final Map<String, Field> registrationPageConfig,
            final Map<String, String> registrationPageData, boolean strict)
            throws ServiceLayerException {
        return getValidatedRegistrationPageData(registrationPageConfig, registrationPageData, strict, null);
    }

    /**
     * We check to see if the expected set of 'field export names' matches the supplied set of 'export names' and return an appropriate error otherwise.
     *
     * @param registrationPageConfig the registration page config
     * @param registrationPageData the registration page data
     * @param strict the strict
     * @param ignoreReqdFields the ignore reqd fields
     * @return the validated registration page data
     * @throws ServiceLayerException the service layer exception
    */
    @Override
    public  final Map<Field, String> getValidatedRegistrationPageData(
            final Map<String, Field> registrationPageConfig,
            final Map<String, String> registrationPageData, 
            final boolean strict, 
            final Set<String> ignoreReqdFields)
            throws ServiceLayerException {

        Set<String> validCodes = registrationPageConfig.keySet();
        Set<String> suppliedCodes = registrationPageData.keySet();
        Set<String> extraCodes = getExtraCodes(validCodes, suppliedCodes);
        
        if (extraCodes.isEmpty() == false) {
        	if(strict){
        		String msg = String.format(ProductRegistrationService.ERR_FMT_INVALID_REGISTRATION_KEYS, extraCodes);
        		throw new ServiceLayerValidationException(msg);
        	}else{
        		for(String extraCode : extraCodes) {
        			registrationPageData.remove(extraCode);
        		}
           	}
    	}

        Set<String> requiredCodes = new HashSet<String>();
        for (Map.Entry<String, Field> entry : registrationPageConfig.entrySet()) {
            Field f = entry.getValue();
            String code = entry.getKey();
            if (f != null && f.isRequired()) {
                requiredCodes.add(code);
            }
        }
        if(ignoreReqdFields != null) {
            for(String ignoreReqdField : ignoreReqdFields) {
                requiredCodes.remove(ignoreReqdField);
            }
        }
        validateRequiredFieldsNotBlank(requiredCodes, registrationPageData);

        Map<Field, String> result = validateRegistrationPageDataValues(registrationPageConfig, registrationPageData, ignoreReqdFields);
        return result;
    }

    private Set<String> getExtraCodes(final Set<String> validRegistrationKeys, final Set<String> suppliedCodes){
    	Set<String> extraCodes = new HashSet<String>(suppliedCodes);
        extraCodes.removeAll(validRegistrationKeys);
        return extraCodes;
    }

    /**
     * Validate registration values.
     *
     * @param registrationPageConfig the field map
     * @param registrationPageData the data map
     * @param ignoreReqdFields the ignore reqd fields
     * @return the map
     * @throws ServiceLayerException the web service exception
     */
    private  Map<Field, String> validateRegistrationPageDataValues(
            final Map<String, Field> registrationPageConfig,
            final Map<String, String> registrationPageData,
            final Set<String> ignoreReqdFields)
            throws ServiceLayerException {
        Map<Field, String> result = new HashMap<Field, String>();

        // validate supplied registration values and associate the Fields with the validated values
        for (Map.Entry<String, String> entry : registrationPageData.entrySet()) {
            String regKey = entry.getKey();
            String regValue = entry.getValue();
            // we know at this point that the registration key is valid
            Field f = registrationPageConfig.get(regKey);
            if(f == null) {
            	throw new RuntimeException("No Field for reg key " + regKey);
            }
            Element e = f.getElement();
            if(e == null) {
            	throw new RuntimeException("No Element for reg key " + regKey);
            }
            Tag tag = e.getTag();

            final String validatedValue;
            if (ignoreReqdFields != null && ignoreReqdFields.contains(regKey) && StringUtils.isBlank(regValue)) {
                validatedValue = "";
            } else {
                validatedValue = validateTagValue(regKey, tag, regValue);
            }
            result.put(f, validatedValue);
        }
        return result;
    }

    /**
     * Validate tag value.
     * 
     * @param regKey
     *            the reg key
     * @param tag
     *            the tag
     * @param regValue
     *            the reg value
     * @throws ServiceLayerException
     *             the web service exception
     */
    private String validateTagValue(final String regKey, final Tag tag, final String regValue) throws ServiceLayerException {
        String result = regValue;
        switch (tag.getTagType()) {
        case CHECKBOX:
            validateCheckboxValue(regKey, (Checkbox) tag, regValue);
            break;
        case RADIO:
        	validateRadioValue(regKey, (Radio) tag, regValue);
            break;
        case SELECT:
        	result = validateSelectValue(regKey, (Select) tag, regValue);
            break;
        case TEXTFIELD:
        	validateTextValue(regKey, (TextField) tag, regValue);
            break;
        case MULTISELECT:
        	result = validateMultiSelectValue(regKey, (MultiSelect) tag, regValue);
            break;
        default:
            // ERROR
            String msg = String.format(ProductRegistrationService.ERR_FMT_PRODUCT_REGISTRATION_UNEXPECTED_TAG_TYPE, tag.getTagType().getName());
            throw new ServiceLayerException(msg);
        }
        if (regKey.equalsIgnoreCase(PageConstant.MARKETING_PREF)) {
        	if ( regValue.equalsIgnoreCase(PageConstant.MARKETING_PREF_OPT_IN)) {
        		result = Boolean.FALSE.toString().toLowerCase() ;
        	} else if( regValue.equalsIgnoreCase(PageConstant.MARKETING_PREF_OPT_OUT) ) {
        		result = Boolean.TRUE.toString().toLowerCase() ;
        	}
        }
        return result;
        
    }

    /**
     * Validate required fields not blank.
     * 
     * @param requiredCodes
     *            the required codes
     * @param registrationPageData
     *            the data map
     * @throws ServiceLayerValidationException
     *             the web service validation exception
     */
    private  void validateRequiredFieldsNotBlank(final Set<String> requiredCodes, final Map<String, String> registrationPageData)
            throws ServiceLayerValidationException {
        // Check for blank values in required fields
        List<String> emptyValueFields = new ArrayList<String>();
        for (String requiredCode : requiredCodes) {
            String suppliedValue = registrationPageData.get(requiredCode);
            if (StringUtils.isBlank(suppliedValue)) {
                emptyValueFields.add(requiredCode);
            }
        }
        if (emptyValueFields.isEmpty() == false) {
            String msg = String.format(ProductRegistrationService.ERR_FMT_REQUIRED_REGISTRATION_KEYS_BLANK, emptyValueFields);
            throw new ServiceLayerValidationException(msg);
        }
    }

    /**
     * Utility method for creating web service validation exceptions.
     * 
     * @param fmt
     *            the message format
     * @param args
     *            the message arguments
     * @return the ServiceLayerValidationException the web service validation exception where the message is constructed from the method parameters.
     */
    private ServiceLayerValidationException createValidationEx(final String fmt, String value,  final Object... args) {
        String msg = String.format(fmt, args);
        return new ServiceLayerValidationException(msg,value);
    }

    /**
     * Validate text value.
     * 
     * @param code
     *            the code
     * @param textTag
     *            the text tag
     * @param value
     *            the value
     * @throws ServiceLayerValidationException
     *             the web service validation exception
     */
    private void validateTextValue(final String code, final TextField textTag, final String value) throws ServiceLayerValidationException {
        String regEx = textTag.getElement().getRegularExpression();
        if (StringUtils.isNotBlank(regEx)) {
            Pattern pattern = Pattern.compile(regEx);
            Matcher matcher = pattern.matcher(value);
            if (!matcher.find()) {
                throw createValidationEx(ProductRegistrationService.ERR_FMT_PRODUCT_REGISTRATION_VALUE_REGEX, value, code, regEx);
            }
        }
    }

    /**
     * Validate select value.
     * 
     * @param code
     *            the code
     * @param selectTag
     *            the select tag
     * @param value
     *            the value
     * @throws ServiceLayerValidationException
     *             the web service validation exception
     */
    private String validateSelectValue(final String code, final Select selectTag, final String value) throws ServiceLayerValidationException {
        List<String> options = new ArrayList<String>();
        for (TagOption tagOption : selectTag.getOptions()) {
            options.add(tagOption.getValue());
        }
        String result = null;
        for( String option : options){
        	if(value.equalsIgnoreCase(option)){
        		result = option;
        		break;
        	}
        }
        if(result == null){
        	
        	String msg = String.format("problem with code [%s] value [%s] valid values [%s]",code,value,options);
        	LOG.error(msg);
        	
            throw createValidationEx(ProductRegistrationService.ERR_FMT_REGISTRATION_VALUE_INVALID, value, code, options);
        }
		return result;
    }

    /**
     * Validate radio value.
     * 
     * @param code
     *            the code
     * @param radioTag
     *            the radio tag
     * @param value
     *            the value
     * @throws ServiceLayerValidationException
     *             the web service validation exception
     */
    private void validateRadioValue(final String code, final Radio radioTag, final String value) throws ServiceLayerValidationException {
        List<String> options = new ArrayList<String>();
        for (TagOption tagOption : radioTag.getOptions()) {
            options.add(tagOption.getValue());
        }
        if (options.contains(value) == false) {
        	
        	String msg = String.format("problem with code [%s] value [%s] valid values [%s]",code,value,options);
        	LOG.error(msg);
        	
            throw createValidationEx(ProductRegistrationService.ERR_FMT_REGISTRATION_VALUE_INVALID, value, code, options);
        }
    }

    /**
	 * Validate checkbox value.
	 * 
	 * @param code
	 *            the code
	 * @param cbTag
	 *            the cb tag
	 * @param value
	 *            the value
	 * @throws ServiceLayerValidationException
	 *             the web service validation exception
	 */
	private void validateCheckboxValue(final String code, final Checkbox cbTag, final String value) throws ServiceLayerValidationException {
	    if (code.equalsIgnoreCase(PageConstant.MARKETING_PREF)) {
	    	ArrayList<String> optList = new ArrayList<String>(Arrays.asList(PageConstant.MARKETING_PREF_OPT_IN,PageConstant.MARKETING_PREF_OPT_OUT)) ;
	    	if ( !optList.contains(value)) {
	    		throw createValidationEx(ProductRegistrationService.ERR_FMT_REGISTRATION_VALUE_INVALID, value, code, optList) ;
	    	}
	    } else if (TRUE_OR_FALSE.contains(value) == false) {
	        throw createValidationEx(ProductRegistrationService.ERR_FMT_REGISTRATION_VALUE_INVALID, value, code, TRUE_OR_FALSE);
	    }
	}

	/**
	 * Validate multi select value.
	 *
	 * @param regKey the reg key
	 * @param multiSelectTag the multi select tag
	 * @param regValues the reg values
	 * @throws ServiceLayerValidationException the service layer validation exception
	 */
	private String validateMultiSelectValue(String regKey, MultiSelect multiSelectTag, String regValues) throws ServiceLayerValidationException {
		String[] answers = regValues.trim().split(PADDED_COMMA_SEPARATOR);
		List<String> options = new ArrayList<String>();
        for (TagOption tagOption : multiSelectTag.getOptions()) {
            options.add(tagOption.getValue());
        }
        for(String answer : answers) {
			if(options.contains(answer) == false){
				throw createValidationEx(ProductRegistrationService.ERR_FMT_REGISTRATION_VALUES_INVALID, answer, regKey, options);
			}
		}
        String result = StringUtils.join(Arrays.asList(answers),",");
        return result;
	}

}
