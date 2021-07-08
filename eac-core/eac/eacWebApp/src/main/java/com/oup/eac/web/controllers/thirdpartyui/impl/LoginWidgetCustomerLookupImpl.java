package com.oup.eac.web.controllers.thirdpartyui.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oup.eac.domain.Customer;
import com.oup.eac.dto.WebCustomerDto;
import com.oup.eac.service.CustomerService;
import com.oup.eac.service.exceptions.CustomerNotFoundServiceLayerException;
import com.oup.eac.web.controllers.thirdpartyui.LoginWidgetCustomerLookup;
import com.oup.eac.web.utils.WebContentUtils;

@Component("loginWidgetCustomerLookup")
public class LoginWidgetCustomerLookupImpl implements LoginWidgetCustomerLookup {

    private static final String CACHE_NAME = "loginWidgetNonSessionCustomerCache";
    private static final Logger LOG = Logger.getLogger(LoginWidgetCustomerLookup.class);

    private final CustomerService customerService;
    private final WebContentUtils webContentUtils;

    /**
     * Instantiates a new login widget customer lookup.
     * 
     * @param customerService
     *            the customer service
     * @param webContentUtils
     *            the web content utils
     */
    @Autowired
    public LoginWidgetCustomerLookupImpl(final CustomerService customerService, final WebContentUtils webContentUtils) {
        this.customerService = customerService;
        this.webContentUtils = webContentUtils;
    }

    /**
     * {@inheritDoc}
     */

    public final WebCustomerDto getAndCacheCustomerFromErightsSessionKey(final String eRightsSessionKey) {
        LOG.debug("Getting and Caching Customer for key" + eRightsSessionKey);
        return getLoggedInCustomer(eRightsSessionKey);
    }

    /**
     * {@inheritDoc}
     */
    public final WebCustomerDto getCustomerFromErightsSessionKey(final String eRightsSessionKey) {
        LOG.debug("Getting Customer for key" + eRightsSessionKey);
        return getLoggedInCustomer(eRightsSessionKey);
    }

    /**
     * {@inheritDoc}
     */

    public final void removeCacheEntryForErightsSessionKey(final String eRightsSessionKey) {
        LOG.debug("Just removed cached Customer for erightsSessionKey " + eRightsSessionKey);
    }

    /**
     * Gets the logged in user.
     * 
     * @param sessionKey
     *            the session key
     * @return the logged in user
     */
    private WebCustomerDto getLoggedInCustomer(final String sessionKey) {
        WebCustomerDto result = null;
        Customer customer = null;
        if (StringUtils.isNotBlank(sessionKey)) {
            try {
                customer = customerService.getCustomerFromSession(sessionKey);
            } catch (CustomerNotFoundServiceLayerException ex) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("No customer for sessionKey " + sessionKey, ex);
                }
            }
        }
        if (customer != null) {
            String webUserName = webContentUtils.getCustomerName(customer);
            result = new WebCustomerDto(customer, webUserName);
        }
        return result;
    }

}
