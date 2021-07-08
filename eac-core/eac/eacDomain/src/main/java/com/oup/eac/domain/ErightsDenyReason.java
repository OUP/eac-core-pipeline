package com.oup.eac.domain;

import java.util.HashMap;
import java.util.Map;

/**
 * Enum holding all the erights deny reasons.
 * 
 * @author packardi
 */
public enum ErightsDenyReason {
   
    /** All license denied **/
    DENY_ALL_LICENSE_DENIED(-134),

    /** Deny coarse product **/
    DENY_COARSE_PRODUCT(-104),

    /** Deny coarse user **/
    DENY_COARSE_USER(-103),

    /** Deny default response **/
    DENY_DEFAULT_RESPONSE(-151),

    /** Deny embargo **/
    DENY_EMBARGO(-137),

    /** Deny implicit authentication failed **/
    DENY_IMPLICIT_AUTH_FAILED(-121),

    /** Deny implicit auth no credential  **/
    DENY_IMPLICIT_AUTH_NO_CREDENTIAL(-122),

    /** Deny implicit auth not available **/
    DENY_IMPLICIT_AUTH_NOT_AVAILABLE(-123),

    /** Deny internal error **/
    DENY_INTERNAL_ERROR(-141),

    /** Deny invalid session key **/
    DENY_INVALID_SESSION_KEY(-102),

    /** Deny license optimizer **/
    DENY_LICENSE_OPTIMIZER(-138),

    /** Deny malformed request **/
    DENY_MALFORMED_REQUEST(-101),

    /** Deny no licenses in session **/
    DENY_NO_LICENSES_IN_SESSION(-131),

    /** Deny no products in session matched **/
    DENY_NO_PRODUCTS_IN_SESSION_MATCHED(-133),

    /** Deny no resource **/
    DENY_NO_RESOURCE(-132),

    /** Deny partner timeout **/
    DENY_PARTNER_TIMEOUT(-136),

    /** Deny session expired **/
    DENY_SESSION_EXPIRED(-113),

    /** Deny session key not found **/
    DENY_SESSION_KEY_NOT_FOUND(-111),

    /** Deny session not found **/
    DENY_SESSION_NOT_FOUND(-112),

    /** Deny task queue full **/
    DENY_TASK_QUEUE_FULL(-140);

    private int reasonCode;
    
    private static Map<Integer, ErightsDenyReason> reasons = new HashMap<Integer, ErightsDenyReason>();
    
    static {
        for (ErightsDenyReason erightsDenyReason : ErightsDenyReason.values()) {
            reasons.put(Integer.valueOf(erightsDenyReason.getReasonCode()), erightsDenyReason);
        }
    }

    /**
     * @param reasonCode
     *            the erights reason code
     */
    private ErightsDenyReason(final int reasonCode) {
        this.reasonCode = reasonCode;
    }

    /**
     * @return reason code.
     */
	public int getReasonCode() {
        return reasonCode;
    }

    /**
     * @param reasonCode
     *            reason code
     * @return Erights deny reason
     */
    public static ErightsDenyReason getErightsDenyReason(final int reasonCode) {
        return reasons.get(Integer.valueOf(reasonCode));
    }

}
