package com.oup.eac.domain;

import java.util.HashMap;
import java.util.Map;

public enum ErightsLicenceDecision {

	DECISION_ACCEPT(5),
	DECISION_DENY(-5),
	DENY_CONCURRENCY(-510),
	DENY_EMBARGO(-520),
	DENY_EXPIRED(-511),
	DENY_INSUFFICIENT_AUTH_LEVEL(-519),
	DENY_INVALID(-512),
	DENY_NO_PRODUCTS_IN_LICENSE(-518),
	DENY_NOT_ACTIVE(-521),
	DENY_PAUSED(-523),
	DENY_SLOT_EXPIRED(-516),
	DENY_SLOT_NOT_PICKED(-514),
	DENY_SLOT_SELECTION_EXPIRED(-525),
	DENY_SLOTS_EXHAUSTED(-515),
	DENY_SUBJECT_TO_AVAIL(-509),
	DENY_SUSPENDED(-517),
	DENY_TERMINATED(-522),
	DENY_UNSPECIFIED(-1001),
	DENY_UNSUPPORTED_RESULT(-2002),
	DENY_USAGE_EXHAUSTED(-513),
	NOT_OCCUPYING_CONCURRENCY(1),
	OCCUPYING_CONCURRENCY(2);
	
	private int decisionCode;
	
    private static Map<Integer, ErightsLicenceDecision> decisions = new HashMap<Integer, ErightsLicenceDecision>();
    
    static {
        for (ErightsLicenceDecision erightsLicenceDecision : ErightsLicenceDecision.values()) {
        	decisions.put(Integer.valueOf(erightsLicenceDecision.getDecisionCode()), erightsLicenceDecision);
        }
    }

	private ErightsLicenceDecision(int decisionCode) {
		this.decisionCode = decisionCode;
	}

	/**
	 * @return the decisionCode
	 */
	public int getDecisionCode() {
		return decisionCode;
	}
	
    /**
     * @param reasonCode
     *            reason code
     * @return Erights deny reason
     */
    public static ErightsLicenceDecision getErightsLicenceDecision(final int decisionCode) {
        return decisions.get(Integer.valueOf(decisionCode));
    }
	
}
