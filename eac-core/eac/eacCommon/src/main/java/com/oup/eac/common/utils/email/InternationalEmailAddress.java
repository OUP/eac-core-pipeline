package com.oup.eac.common.utils.email;

import java.net.IDN;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.internet.MimeUtility;

import org.apache.log4j.Logger;


/**
 * Utils to handle international email addresses
 *
 */
public final class InternationalEmailAddress {
    private static final String ENCODING = "B";
    private static final String UTF_8 = "UTF-8";
    private static final String AT_SYMBOL = "@";
    private static final Logger LOG = Logger.getLogger(InternationalEmailAddress.class);
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$", Pattern.CASE_INSENSITIVE);

    /**
     * Hidden constructor
     */
    private InternationalEmailAddress() {
        super();
    }
    
    /**
     * Convert email address to ASCII Compatible Encoding (ACE)
     * @param emailAddress
     * @return
     */
    public static String convertToAscii(final String emailAddress) {
        String email = null;
        if (emailAddress != null) {
            final int atPosition = emailAddress.indexOf(AT_SYMBOL);
            if (atPosition > 0) {
                final String localPart = emailAddress.substring(0, atPosition);
                final String domain = emailAddress.substring(atPosition + 1);
                // handle local part by quoted printable conversion to UTF-8
                // handle IDNs by punycode conversion to ASCII Compatible Encoding (ACE)
                try {
                    final String localPartQP = MimeUtility.encodeText(localPart, UTF_8, ENCODING);
                    final String domainAce = IDN.toASCII(domain);
                    email = localPartQP + AT_SYMBOL + domainAce;
                    if (!emailAddress.equals(email)) {
                        LOG.debug("Converted:" + emailAddress + " to:" + email);
                    }
                } catch (Exception e) {                
                    LOG.warn("Unable to convert email address", e);
                    throw new IllegalArgumentException("Unable to convert email address", e);
                }
            } else {
                LOG.warn("Invalid email address:" + emailAddress);
                throw new IllegalArgumentException("Invalid email address");
            }
        }
        return email;
    }
    
    /**
     * Validate email address format, including IDN's
     * @param emailAddress
     * @return
     */
    public static boolean isValid(final String emailAddress) {
        boolean valid = emailAddress != null;
        
        if(valid && emailAddress.endsWith(".")){
            valid = false;
        }
        if (valid) {           
            final int atPosition = emailAddress.indexOf(AT_SYMBOL);
            valid = atPosition > 0;
            if (valid) {
                final String localPart = emailAddress.substring(0, atPosition);
                final String domain = emailAddress.substring(atPosition + 1);
                // handle IDNs by punycode conversion to ASCII Compatible Encoding (ACE)
                // before performing regex check
                try {
                    final String localPartAce = IDN.toASCII(localPart);
                    final String domainAce = IDN.toASCII(domain);
                    final String email = localPartAce + AT_SYMBOL + domainAce;
                    if (!emailAddress.equals(email)) {
                        LOG.debug("Converted:" + emailAddress + " to:" + email);
                    }
                    final Matcher matcher = EMAIL_PATTERN.matcher(InternationalEmailAddress.convertToAscii(email));
                    valid =  matcher.find();
                } catch (Exception e) {                
                    LOG.warn("Unable to convert email address", e);
                    valid = false;
                }
            }
        }
        return valid;
    }
}
