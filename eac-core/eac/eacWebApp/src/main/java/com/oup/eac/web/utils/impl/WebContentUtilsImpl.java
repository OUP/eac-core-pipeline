package com.oup.eac.web.utils.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.oup.eac.domain.Customer;
import com.oup.eac.web.utils.WebContentUtils;

/**
 * This is a spring bean.
 * 
 * @author David Hay
 * 
 */
@Component("webContentUtils")
public class WebContentUtilsImpl implements WebContentUtils {

    private final boolean fallBackOnUserName;
    private final int usernameMaxLength;

    /**
     * Instantiates a new web content utils.
     * 
     * @param fallBackOnUserNameP
     *            the fall back on user name p
     * @param usernameMaxLengthP
     *            the username max length p
     */
    @Autowired
    public WebContentUtilsImpl(
            @Value("${web.content.utils.fall.back.on.user.name}") final boolean fallBackOnUserNameP, 
            @Value("${web.content.utils.user.name.max.length}") final int usernameMaxLengthP) {
        this.fallBackOnUserName = fallBackOnUserNameP;
        this.usernameMaxLength = usernameMaxLengthP;
    }

    /**
     * Checks if is fall back on user name.
     * 
     * @return true, if is fall back on user name
     */
    public final boolean isFallBackOnUserName() {
        return fallBackOnUserName;
    }

    /**
     * Gets the username max length.
     * 
     * @return the username max length
     */
    public final int getUsernameMaxLength() {
        return usernameMaxLength;
    }

    /**
     * Gets the customer name.
     * 
     * @param customer
     *            the customer
     * @return the customer name
     */
    public final String getCustomerName(final Customer customer) {
        if (customer == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        boolean spaceReqd = false;
        String first = customer.getFirstName();
        if (StringUtils.isNotBlank(first)) {
            sb.append(first);
            spaceReqd = true;

        }
        String family = customer.getFamilyName();
        if (StringUtils.isNotBlank(family)) {
            if (spaceReqd) {
                sb.append(" ");
            }
            sb.append(family);
            spaceReqd = true;
        }

        if (!spaceReqd && this.fallBackOnUserName) {
            String username = customer.getUsername();
            if (usernameMaxLength > 0) {
                if (username.length() <= usernameMaxLength) {
                    sb.append(username);
                }
            } else {
                sb.append(username);
            }
        }
        String result = sb.toString();
        return result;
    }

}
