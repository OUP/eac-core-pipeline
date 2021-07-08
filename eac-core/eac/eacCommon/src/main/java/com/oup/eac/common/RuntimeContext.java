package com.oup.eac.common;

import java.util.Properties;

import org.springframework.beans.factory.InitializingBean;

/**
 * @author packardi Interface for accessing runtime properties and system settings.
 */
public interface RuntimeContext extends InitializingBean {

    /**
     * Returns a property from the runtime context.
     * 
     * @param key
     *            The key of the property to return.
     * @return The string value of the property.
     */
    String getProperty(String key);

    /**
     * Returns a property from the runtime context, returning the default value if another value is not available.
     * 
     * @param key
     *            The key of the property to return.
     * @param defaultValue
     *            The value to return if another is not available.
     * @return The string value of the property.
     */
    String getProperty(String key, String defaultValue);

    /**
     * Returns a property from the runtime context. Will throw an exception if the property is not available.
     * 
     * @param key
     *            The key of the property to return.
     * @return The string value of the property.
     */
    String getRequiredProperty(String key);

    /**
     * Returns a property from the runtime context which is expected to be a boolean value.
     * 
     * @param key
     *            The key of the property to return.
     * @return The boolean value of the property.
     */
    boolean getBoolProperty(String key);

    /**
     * Returns a property from the runtime context which is expected to be a int value.
     * 
     * @param key
     *            The key of the property to return.
     * @return The int value of the property.
     */
    int getIntProperty(String key);
    
    /**
     * Get the underlying properties object.
     * 
     * @return the list of available properties
     */
    Properties getProperties();
    
    /**
     * Return properties prepared after consideration of system property override.
     * 
     * @return the effective properties
     */
    Properties getEffectiveProperties();

}
