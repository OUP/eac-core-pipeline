package com.oup.eac.accounts.validators;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.oup.eac.accounts.dto.CodeRedeemDto;

/**
 * Spring MVC form validator for redeem Activation Code.  
 * @author Gaurav Soni
 */

@Component
public class AccountsActivationCodeFormValidator implements Validator{

    @Override
    public boolean supports(Class<?> clazz) {
        final boolean supports = CodeRedeemDto.class.isAssignableFrom(clazz);
        return supports;
    }

    @Override
    public void validate(Object target, Errors errors) {
        CodeRedeemDto codeRedeemDto = (CodeRedeemDto) target;
        
        if( !codeRedeemDto.isSharedUser() && StringUtils.isBlank(codeRedeemDto.getCode())){
            errors.rejectValue("code", "error.not-specified", new Object[] { "label.redeemcode" }, "Activation Code is required.");
            return;
        }
    }

}
