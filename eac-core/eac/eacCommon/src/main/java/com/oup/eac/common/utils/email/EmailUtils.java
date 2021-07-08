package com.oup.eac.common.utils.email;

import java.io.UnsupportedEncodingException;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import com.oup.eac.common.utils.EACSettings;

public class EmailUtils {
    
    private EmailUtils() {
        
    }
    
    public static InternetAddress getInternetAddressFrom() throws UnsupportedEncodingException {
        InternetAddress result = new InternetAddress(EACSettings.getProperty(EACSettings.EMAIL_FROM_ADDRESS), EACSettings.getProperty(EACSettings.EMAIL_FROM_TITLE));
        return result;
    }

    public static InternetAddress getInternetAddressReplyTo() throws AddressException { 
        InternetAddress result = new InternetAddress(EACSettings.getProperty(EACSettings.EAC_EMAIL_REPLY_TO_ADDRESS));
        return result;
    }

}
