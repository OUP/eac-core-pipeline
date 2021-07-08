package com.oup.eac.domain;

import java.util.Locale;

/**
 * Represents the available languages supported in Eac. Named EacLang (rather than EacLanguage or Language) because
 * of trouble with ClassNotFoundException when the class was named differently (mysterious...).
 *  
 * @author packardi
 *
 */
public class EacLang {

    private final Locale locale;    
    private final String labelCode;
    private final String defaultLabel;
    
    public EacLang(String localeCode, String labelCode, String defaultLabel) {
        this.locale = new Locale(localeCode);        
        this.labelCode = labelCode;
        this.defaultLabel = defaultLabel;
    }
    
    public Locale getLocale() {
        return locale;
    }
    
    public String getDefaultLabel() {
        return defaultLabel;
    }
    
    public String getLabelCode() {
        return labelCode;
    }

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EacLang [defaultLabel=" + defaultLabel + ", labelCode="
				+ labelCode + ", locale=" + locale + "]";
	}
    
}