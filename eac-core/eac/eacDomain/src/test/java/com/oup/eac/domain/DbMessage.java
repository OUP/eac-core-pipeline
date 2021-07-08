package com.oup.eac.domain;

/**
 * Pojo for DbMessage so that we can add Message Entries for the DbMessageSourceReloader to reload during testing
 * We never need to add DbMessages when the EAC applications are running.
 * FOR TESTING ONLY
 * @author David Hay
 *
 */
public class DbMessage {

    private String basename;
    private String language;
    private String country;
    private String key;
    private String variant;
    private String message;
    public String getBasename() {
        return basename;
    }
    public void setBasename(String basename) {
        this.basename = basename;
    }
    public String getLanguage() {
        return language;
    }
    public void setLanguage(String language) {
        this.language = language;
    }
    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
    public String getVariant() {
        return variant;
    }
    public void setVariant(String variant) {
        this.variant = variant;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    
}
