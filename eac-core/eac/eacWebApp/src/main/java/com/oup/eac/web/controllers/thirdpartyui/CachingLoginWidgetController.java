package com.oup.eac.web.controllers.thirdpartyui;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.oup.eac.domain.Customer;
import com.oup.eac.dto.WebCustomerDto;
import com.oup.eac.web.controllers.helpers.SessionHelper;

/**
 * The Class LoginWidgetController.
 * 
 * @author David Hay
 * 
 */
@Controller("loginWidgetController")
public class CachingLoginWidgetController extends BaseLoginWidgetController {

    public static final Logger LOG = Logger.getLogger(CachingLoginWidgetController.class);

    private final LoginWidgetCustomerLookup customerLookup;

    /**
     * Instantiates a new caching login widget controller.
     * 
     * @param customerLookup
     *            the customer lookup
     */
    @Autowired
    public CachingLoginWidgetController(final LoginWidgetCustomerLookup customerLookup) {
        this.customerLookup = customerLookup;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Customer getCustomer(final String eRightsSessionKey, final HttpServletRequest request) {
        Customer customer = null;

        if (StringUtils.isNotBlank(eRightsSessionKey)) {

            HttpSession session = request.getSession();

            if (session.isNew()) {
                // lookup customer from eac cookie and cache it
                customer = lookupCustomerUsingErightsSessionKey(request, eRightsSessionKey, true);

            } else {
                // browser has joined session so no keep cache entry for non
                // session requests
                customerLookup.removeCacheEntryForErightsSessionKey(eRightsSessionKey);

                // customer may or may not be logged in
                customer = SessionHelper.getCustomer(request);
                if (customer == null) {
                    // lookup customer from eac cookie but don't cache it
                    customer = lookupCustomerUsingErightsSessionKey(request, eRightsSessionKey, false);
                }
            }
            // Always set the session timeout to 10 minutes
        }
        return customer;
    }

    /**
     * Gets the customer.
     * 
     * @param request
     *            the request
     * @param eRightsSessionKey
     *            the eac sessionkey
     * @param cacheCustomer
     *            the cache customer
     * @return the customer
     */
    private Customer lookupCustomerUsingErightsSessionKey(final HttpServletRequest request, final String eRightsSessionKey, final boolean cacheCustomer) {
        Customer customer = null;
        String webUserName = null;
        WebCustomerDto webCustomer;
        if (cacheCustomer) {
            webCustomer = customerLookup.getAndCacheCustomerFromErightsSessionKey(eRightsSessionKey);
        } else {
            webCustomer = customerLookup.getCustomerFromErightsSessionKey(eRightsSessionKey);
        }
        if (webCustomer != null) {
            customer = webCustomer.getCustomer();
            webUserName = webCustomer.getWebUserName();
        }
        SessionHelper.setCustomerAndWebUserName(request, customer, webUserName);
        return customer;
    }
    
}
