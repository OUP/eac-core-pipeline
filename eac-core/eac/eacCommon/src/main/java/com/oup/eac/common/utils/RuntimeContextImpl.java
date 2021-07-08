package com.oup.eac.common.utils;

import java.util.Properties;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.springframework.util.Assert;

import com.oup.eac.common.RuntimeContext;
import com.oup.eac.common.utils.ssl.SSLUtilities;

/**
 * Implementation of RuntimeContext which provides system settings. The properties can be overridden by system properties if systemPropertyOverride is true.
 * 
 * @author packardi
 * 
 */
public class RuntimeContextImpl implements RuntimeContext {

    private static final Logger LOGGER = Logger.getLogger(RuntimeContextImpl.class);
    
    protected static final Pattern PASSWORD_PATTERN = Pattern.compile("^.*\\.password.*$", Pattern.CASE_INSENSITIVE);
    
    /**
     * The underlying properties object containing java properties.
     */
    private final Properties properties;

    private final boolean systemPropertyOverride;

    /**
     * 
     * @param properties
     *            The properties available to this context.
     * @param systemPropertyOverride
     *            true if the value of system properties should override context properties.
     */
    public RuntimeContextImpl(final Properties properties, final boolean systemPropertyOverride) {
        this.properties = properties;
        this.systemPropertyOverride = systemPropertyOverride;

        setSystemWideSettings();
    }

    /**
     * Set system wide properties.
     */
    private void setSystemWideSettings() {
        if (getBoolProperty(EACSettings.WS_HTTPS_TRUST_ALL_HOSTS)) {
            SSLUtilities.trustAllHostnames();
        }

        if (getBoolProperty(EACSettings.WS_HTTPS_TRUST_ALL_CERTIFICATES)) {
            SSLUtilities.trustAllHttpsCertificates();
        }
    }

    @Override
    public final String getProperty(final String key) {
        String property = null;

        if (systemPropertyOverride) {
            property = System.getProperty(key);
        }

        // fallback
        if (property == null) {
            property = properties.getProperty(key);
        }

        return property;
    }

    @Override
    public final String getRequiredProperty(final String key) {
        String value = getProperty(key);
        Assert.notNull(value);
        return value;
    }

    @Override
    public final boolean getBoolProperty(final String property) {
        String s = getProperty(property, "false");
        return Boolean.parseBoolean(s);
    }

    @Override
    public final int getIntProperty(final String property) {
        String s = getProperty(property);
        return Integer.parseInt(s);
    }

    /**
     * @param isTest
     *            the isTest to set
     */
    public final void setIsTest(final String isTest) {

    }

    @Override
    public final String getProperty(final String key, final String defaultValue) {
        String property = getProperty(key);
        if (property == null) {
            property = defaultValue;
        }
        return property;
    }

    /**
     * Get the underlying properties object.
     * 
     * @return the list of available properties
     */
    @Override
    public final Properties getProperties() {
        return properties;
    }

    /**
     * Return properties prepared after consideration of system property override.
     * 
     * @return the effective properties
     */
    @Override
    public final Properties getEffectiveProperties() {
        if (!systemPropertyOverride) {
            return properties;
        }
        Properties effective = new Properties(properties);
        for (String key : properties.stringPropertyNames()) {
            effective.setProperty(key, System.getProperty(key, properties.getProperty(key)));
        }
        return effective;
    }
    
    private final void outputEntries() {
        for(Entry<Object, Object> entry : getEffectiveProperties().entrySet()) {
            LOGGER.info("Key: " + entry.getKey() + "   Value: " + confuscatePasswords((String)entry.getKey(), (String)entry.getValue()));
        }
    }
    
    private final String confuscatePasswords(final String key, final String value) {
    	Matcher matcher = PASSWORD_PATTERN.matcher(key);
    	if(matcher.find()) {
    		return "**************";
    	}
    	return value;
    }

    @Override
    public final void afterPropertiesSet() throws Exception {
        outputEntries();
    }

}
