package com.oup.eac.web.controllers.authentication;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.oup.eac.web.controllers.registration.EACViews;

public class ValidatorRegistrationAllowController implements Controller {

    private static Logger LOG = Logger.getLogger(ValidatorRegistrationAllowController.class);
    
    
	private ModelAndView getNotActivatedView(Map<String, String> paramMap) {
		return new ModelAndView(EACViews.VALIDATOR_LICENCE_ALLOWED_PAGE, paramMap);
	}

    
	@Override
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String username = request.getParameter("username");
		String productName = request.getParameter("product");
		
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(productName)) {
        	return new ModelAndView(EACViews.ACTIVATION_ERROR_PAGE);
        } else {
        	return getNotActivatedView(getParamMap(username,productName));
        }
		
	}
	private Map<String, String> getParamMap(String username, String product) {
    	
    	Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("username", username);
        paramMap.put("product", product);
        return paramMap;
    }

}
