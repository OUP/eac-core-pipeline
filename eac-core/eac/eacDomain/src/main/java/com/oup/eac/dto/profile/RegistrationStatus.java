package com.oup.eac.dto.profile;

import com.oup.eac.domain.Registration;
import com.oup.eac.domain.RegistrationDefinition;

/**
 * Enumeration of Registration Status with internationalisation support for
 * Basic Profile Page.
 * 
 * @see ProfileRegistrationDto
 * @author David Hay
 * 
 */
public enum RegistrationStatus {

    ACTIVATED("registration.status.activated"),
    DENIED("registration.status.denied"),
    AWAITING_VALIDATION("registration.status.awaiting.validation"),
    INCOMPLETE("registration.status.incomplete"),
    OTHER("registration.status.other"),
    NO_REGISTRATION("registration.status.no.registration"),
    EXPIRED("registration.status.expired");
    private final String messageCode;

    /**
     * Instantiates a new registration status.
     * 
     * @param msgCodeP
     *            the msg code p
     */
    RegistrationStatus(final String msgCodeP) {
        messageCode = msgCodeP;
    }

    /**
     * Gets the message code.
     * 
     * @return the message code
     */
    public String getMessageCode() {
        return messageCode;
    }

    /**
     * Gets the from registration.
     * 
     * @param registration
     *            the registration
     * @return the from registration
     */
    public static RegistrationStatus getFromRegistration(final Registration<? extends RegistrationDefinition> registration) {
        if(registration == null){
            return RegistrationStatus.NO_REGISTRATION;
        }
        RegistrationStatus result;
        if (registration.isDenied()) {
            result = RegistrationStatus.DENIED;
        } else if (registration.isActivated()) {
           result = RegistrationStatus.ACTIVATED;
        } else if (registration.isAwaitingValidation()) {
            result = RegistrationStatus.AWAITING_VALIDATION;
        } else if (!registration.isCompleted()) {
            result = RegistrationStatus.INCOMPLETE;
        } else if (registration.isExpired()){
        	result = RegistrationStatus.EXPIRED;
        }
        else {
            result = RegistrationStatus.OTHER;
        }
        return result;
    }

}
