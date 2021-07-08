package com.oup.eac.common.utils.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;

/**
 * Provides a method for allowing non-spring managed classes from accessing spring managed beans.
 * 
 * @author packardi
 * 
 */
public class ApplicationContextSupport implements ApplicationContextAware {

    /**
     * The application context to return beans from.
     */
    private static ApplicationContext applicationContext;

    /**
     * Sets the application context.
     * 
     * @param applicationContext
     *            The spring applicationContext
     */
    @Override
    public final void setApplicationContext(final ApplicationContext applicationContext) {
        ApplicationContextSupport.applicationContext = applicationContext;
    }

    /**
     * Gets the application context.
     * 
     * @return The application context
     */
    private static ApplicationContext getApplicationContext() {
        Assert.notNull(applicationContext);
        return applicationContext;
    }

    /**
     * Get a spring managed bean from the underlying application context.
     * 
     * @param beanName
     *            The name of the bean to return
     * @return The bean
     */
    public static Object getBean(final String beanName) {
        return getApplicationContext().getBean(beanName);
    }

}