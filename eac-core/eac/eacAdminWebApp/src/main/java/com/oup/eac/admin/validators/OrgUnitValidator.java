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

import com.oup.eac.admin.beans.OrgUnitsBean;
import com.oup.eac.domain.Division;

@Component
public class OrgUnitValidator implements Validator {

    private static final String ERR_FIELD_ORG_UNIT_BLANK     = "error.org.unit.blank";
    private static final String ERR_FIELD_ORG_UNIT_DUPLICATE = "error.org.unit.duplicate";
    private static final String ERR_FIELD_ORG_UNIT_INVALID   = "error.org.unit.invalid";
    private static final String ERR_HAS_BLANK                = "error.org.unit.global.blank";
    private static final String ERR_HAS_DUPLICATE            = "error.org.unit.global.duplidate";
    private static final String ERR_HAS_INVALID              = "error.org.unit.global.invalid";
    private static final String DEFAULT_ORG_UNIT_BLANK       = "The Org Unit cannot be blank";
    private static final String DEFAULT_ORG_UNIT_DUPLICATE   = "The Org Unit must be unique";
    private static final String DEFAULT_ORG_UNIT_INVALID     = "Only use characters in A..Z,a..z,0..9,-,_,( and )";
    private static final String DEFAULT_HAS_BLANK            = "At least one Org Unit is blank.";
    private static final String DEFAULT_HAS_DUPLICATE        = "The Org Units contain duplicates.";
    private static final String DEFAULT_HAS_INVALID          = "At least one Org Unit is invalid.";
    
    public static final String ORG_UNIT_REGEX = "^[A-Za-z_\\-0-9\\(\\)]+$";
    
    
    @Override
    public boolean supports(Class<?> clazz) {
        return OrgUnitsBean.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        boolean hasBlankErrors = false;
        boolean hasDuplicateErrors = false;
        boolean hasValidationErrors = false;
        
        OrgUnitsBean bean = (OrgUnitsBean) target;
        Set<String> values = new HashSet<String>();
        Set<String> duplicateValues = new HashSet<String>();

        Set<Integer> indexesToDelete = bean.getIndexesToDelete();
        Map<String, String> pathToValue = new HashMap<String, String>();

        for (int i = 0; i < bean.getOrgUnits().size(); i++) {
            if (indexesToDelete.contains(i) == false) {
                Division orgUnit = bean.getOrgUnits().get(i);
                pathToValue.put("orgUnits[" + i + "].divisionType", orgUnit.getDivisionType().toLowerCase());
            }
        }
        for (int i = 0; i < bean.getNewOrgUnits().size(); i++) {
            Division newOrgUnit = bean.getNewOrgUnits().get(i);
            pathToValue.put("newOrgUnits[" + i + "].divisionType", newOrgUnit.getDivisionType().toLowerCase());

        }
        for (Map.Entry<String, String> entry : pathToValue.entrySet()) {
            String value = entry.getValue();
            String path = entry.getKey();
            if (StringUtils.isBlank(value)) {
                errors.rejectValue(path, ERR_FIELD_ORG_UNIT_BLANK, DEFAULT_ORG_UNIT_BLANK);
                hasBlankErrors = true;
            } else {
                if (values.contains(value)) {
                    duplicateValues.add(value);
                    hasDuplicateErrors = true;
                } else {
                    if(isValidOrgUnit(value)){
                        values.add(value);    
                    }else{
                        errors.rejectValue(path, ERR_FIELD_ORG_UNIT_INVALID, DEFAULT_ORG_UNIT_INVALID);
                        hasValidationErrors = true;
                    }
                    
                }
            }
        }
        for (Map.Entry<String, String> entry : pathToValue.entrySet()) {
            String value = entry.getValue();
            String path = entry.getKey();
            if (duplicateValues.contains(value)) {
                errors.rejectValue(path, ERR_FIELD_ORG_UNIT_DUPLICATE, DEFAULT_ORG_UNIT_DUPLICATE);
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

    
    public static boolean isValidOrgUnit(String orgUnit) {
        Pattern p = Pattern.compile(ORG_UNIT_REGEX);
        Matcher matcher = p.matcher(orgUnit);
        boolean doesMatch = matcher.find();
        return doesMatch;
    }
}
