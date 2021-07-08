package com.oup.eac.web.controllers.authentication;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.oup.eac.service.RegistrationService;

public class RegistrationAllowController extends AbstractAllowController {

    private static Logger LOG = Logger.getLogger(RegistrationAllowController.class);
    
    /**
     * Construct new RegistrationConfirmationController.
     * 
     * @param registrationService
     *            The service this controller should use.
     */
    public RegistrationAllowController(final RegistrationService registrationService) {
        super(registrationService);
    }

	@Override
	protected ModelAndView getNotActivatedView(Map<String, String> paramMap) {
		String originalUrl = paramMap.get("originalUrl");
		if (!StringUtils.isEmpty(originalUrl)) {
			return new ModelAndView(new RedirectView(originalUrl, false));
		}
		return new ModelAndView(new RedirectView(paramMap.get("productHome"), false));
	}

    @Override
    public Logger getLog() {
        return LOG;
    }

}
