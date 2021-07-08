package com.oup.eac.common.utils;

import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

/**
 * Utility class which ensures pre-requisite system properties are set based on supplied {@link Properties}. If a system
 * property already exists, then it will *not* be overwritten.
 * <p>
 * Implements {@link BeanFactoryPostProcessor} to ensure that the Properties are set once Spring has initalised the
 * application context.
 * 
 * @author Will Keeling
 * 
 */
public class SystemPreRequisites implements BeanFactoryPostProcessor {

	private final Properties preRequisites;

	public SystemPreRequisites(final Properties preRequisites) {
		this.preRequisites = preRequisites;
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		for (Object key : preRequisites.keySet()) {
			String propName = (String) key;
			if (System.getProperty(propName) == null) {
				System.setProperty(propName, preRequisites.getProperty(propName));
			}
		}
	}

}
