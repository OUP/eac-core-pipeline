package com.oup.eac.common.utils.username.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apache.commons.lang.StringUtils;

import com.oup.eac.common.utils.username.UsernameValidator;

/**
 * The Class UsernameValidatorImpl.
 */
public class UsernameValidatorImpl implements UsernameValidator {

    private final Pattern usernamePattern;
    private final String usernamePolicyRegex;

    /**
     * Instantiates a new username validator impl.
     * 
     * @param minLengthP
     *            the min length p
     * @param maxLengthP
     *            the max length p
     * @param whiteSpaceInvalidP
     *            the white space invalid p
     * @param invalidRegexP
     *            the invalid regex
     */
    public UsernameValidatorImpl(final String usernamePolicyRegex) {

        this.usernamePolicyRegex = usernamePolicyRegex;
        try {
        	 if (StringUtils.isBlank(usernamePolicyRegex)) {
                 this.usernamePattern = null;
             } else {
                 this.usernamePattern = Pattern.compile(usernamePolicyRegex);
             }
        } catch (PatternSyntaxException pse) {
            throw new IllegalArgumentException("invalid invalidRegexP", pse);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean isValid(final String username) {

		boolean valid = StringUtils.isNotBlank(username);

		if (username != null && usernamePattern!=null) {
	        Matcher res = usernamePattern.matcher(username);
			valid = res.find();
		}
		return valid;
    }
} 