package com.oup.eac.admin.validators;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.oup.eac.admin.beans.WhiteListUrlBean;
import com.oup.eac.domain.WhiteListUrl;
@Component
public class UrlValidator implements Validator{
	


    private static final String ERR_FIELD_URL_BLANK     = "error.url.blank";
    private static final String ERR_FIELD_URL_DUPLICATE = "error.url.duplicate";
    private static final String ERR_FIELD_URL_INVALID   = "error.url.invalid";
    private static final String ERR_HAS_BLANK                = "error.url.global.blank";
    private static final String ERR_HAS_DUPLICATE            = "error.url.global.duplidate";
    private static final String ERR_HAS_INVALID              = "error.url.global.invalid";
    private static final String DEFAULT_URL_BLANK       = "The Url cannot be blank";
    private static final String DEFAULT_URL_DUPLICATE   = "The Url must be unique";
    private static final String DEFAULT_URL_INVALID     = "Invalid Url";
    private static final String DEFAULT_HAS_BLANK            = "At least one Url is blank.";
    private static final String DEFAULT_HAS_DUPLICATE        = "The Urls contain duplicates.";
    private static final String DEFAULT_HAS_INVALID          = "At least one Url is invalid.";
    
    public static final String URL_REGEX = "^http(s{0,1}://)[A-Za-z0-9-]+[.]{1}[A-Za-z0-9-]+([.]{0,1}[A-Za-z0-9-])*";
    
    
    @Override
    public boolean supports(Class<?> clazz) {
        return WhiteListUrlBean.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        boolean hasBlankErrors = false;
        boolean hasDuplicateErrors = false;
        boolean hasValidationErrors = false;
        
        WhiteListUrlBean bean = (WhiteListUrlBean) target;
        Set<String> values = new HashSet<String>();
        Set<String> duplicateValues = new HashSet<String>();

        Set<Integer> indexesToDelete = bean.getIndexesToDelete();
        Map<String, String> pathToValue = new HashMap<String, String>();

        for (int i = 0; i < bean.getUrlList().size(); i++) {
            if (indexesToDelete.contains(i) == false) {
                WhiteListUrl urlList = bean.getUrlList().get(i);
                pathToValue.put("urlList[" + i + "].url", urlList.getUrl().toLowerCase());
            }
        }
        for (int i = 0; i < bean.getNewUrl().size(); i++) {
        	WhiteListUrl newUrlList = bean.getNewUrl().get(i);
            pathToValue.put("newUrl[" + i + "].url", newUrlList.getUrl().toLowerCase());

        }
        for (Map.Entry<String, String> entry : pathToValue.entrySet()) {
            String value = entry.getValue();
            String path = entry.getKey();
            if (StringUtils.isBlank(value)) {
                errors.rejectValue(path, ERR_FIELD_URL_BLANK, DEFAULT_URL_BLANK);
                hasBlankErrors = true;
            } else {
                if (values.contains(value)) {
                    duplicateValues.add(value);
                    hasDuplicateErrors = true;
                } else {
                    if(isValidUrl(value)){
                        values.add(value);    
                    }else{
                        errors.rejectValue(path, ERR_FIELD_URL_INVALID, DEFAULT_URL_INVALID);
                        hasValidationErrors = true;
                    }
                    
                }
            }
        }
        for (Map.Entry<String, String> entry : pathToValue.entrySet()) {
            String value = entry.getValue();
            String path = entry.getKey();
            if (duplicateValues.contains(value)) {
                errors.rejectValue(path, ERR_FIELD_URL_DUPLICATE, DEFAULT_URL_DUPLICATE);
            }
        }
        if (hasBlankErrors) {
            errors.reject(ERR_HAS_BLANK, DEFAULT_HAS_BLANK);
        }
        if (hasDuplicateErrors) {
            errors.reject(ERR_HAS_DUPLICATE, DEFAULT_HAS_DUPLICATE);
        }
        if (hasValidationErrors) {
            errors.reject(ERR_HAS_INVALID, DEFAULT_HAS_INVALID);
        }
    }

    
    public static boolean isValidUrl(String url) {
    	url.matches(URL_REGEX);
        boolean doesMatch = url.matches(URL_REGEX);
        return doesMatch;
    }
}
