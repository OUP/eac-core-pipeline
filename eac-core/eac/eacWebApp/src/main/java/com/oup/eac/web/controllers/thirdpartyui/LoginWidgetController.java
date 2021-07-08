package com.oup.eac.web.controllers.thirdpartyui;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.oup.eac.domain.Customer;
import com.oup.eac.service.CustomerService;
import com.oup.eac.service.exceptions.CustomerNotFoundServiceLayerException;

/**
 * The Class LoginWidgetController.
 * 
 * @author David Hay
 * 
 */
//@Controller("loginWidgetController")
public class LoginWidgetController extends BaseLoginWidgetController {


    private final CustomerService customerService;

    /**
     * Instantiates a new login widget controller.
     *
     * @param customerService the customer service
     */
    @Autowired
    public LoginWidgetController(final CustomerService customerService) {
        this.customerService = customerService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Customer getCustomer(final String erightsSessionKey, final HttpServletRequest request) {
        Customer result = null;
        if (StringUtils.isNotBlank(erightsSessionKey)) {
            try {
                result = customerService.getCustomerFromSession(erightsSessionKey);
            } catch (CustomerNotFoundServiceLayerException e) {
                result = null;
            }
        }
        return result;
    }

}
