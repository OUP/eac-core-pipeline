package com.oup.eac.web.controllers.authentication;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oup.eac.common.utils.crypto.PasswordUtils;
import com.oup.eac.web.json.ValidationResponse;

/**
 * @author David Hay
 * For Ajax password validation
 */
@Controller("passwordValidatorController")
public class PasswordValidatorController {

    @RequestMapping(value="validatePassword.htm", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody  ValidationResponse validatePasswordQueryString(@RequestParam("password") String password, HttpSession session) {
        boolean result = PasswordUtils.isPasswordValid(password);
        ValidationResponse response = new ValidationResponse();
        response.setValid(result);
        return response;
    }
    
}
