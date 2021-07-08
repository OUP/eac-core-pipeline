package com.oup.eac.ws.endpoint;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.ws.soap.server.endpoint.SimpleSoapExceptionResolver;

/**
 * This BeanPostProcessor makes sure the SimpleSoapExceptionResolver is correctly configured to log exceptions.
 * Without this configuration, exceptions that happened in our own Spring-WS interceptors were being returned to the ws-client
 * as soap faults [good] but there was no associated log message and/or stack trace [not so good].
 * 
 * @author David Hay
 *
 */
public class EacExceptionResolverConfigurer implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        
        if(SimpleSoapExceptionResolver.class == bean.getClass()){
            SimpleSoapExceptionResolver resolver = (SimpleSoapExceptionResolver)bean;
            Class<?> clazz = resolver.getClass();
            Logger log = Logger.getLogger(clazz);
            log.setLevel(Level.DEBUG);//without this we were missing exceptions being logged
            resolver.setWarnLogCategory(clazz.getName());//without this we were missing exceptions being logged
        }
        return bean;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

}
