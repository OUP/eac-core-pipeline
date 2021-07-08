package com.oup.eac.admin.validators;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.oup.eac.dto.RoleCriteria;

@Component("rolePermissionsValidator")
public class RolePermissionsValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return RoleCriteria.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        RoleCriteria roleCriteria = (RoleCriteria) target;
        if (StringUtils.isBlank(roleCriteria.getRoleName())) {
        	errors.rejectValue("roleName", "error.role.name.empty");
        }
    }

}
