package com.oup.eac.data.message;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.synyx.messagesource.InitializableMessageSource;

public class DbMessageSourceBeanPostProcessor implements BeanFactoryPostProcessor {

    private static final Logger LOG = Logger.getLogger(DbMessageSourceBeanPostProcessor.class);

    private DbMessageSourceReloader reloader;
    
    public DbMessageSourceBeanPostProcessor(DbMessageSourceReloader reloader){
        this.reloader = reloader;
    }
    
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        
        Map<String,InitializableMessageSource> beanMap = BeanFactoryUtils.beansOfTypeIncludingAncestors(beanFactory,InitializableMessageSource.class);
        List<InitializableMessageSource> messageSources = new ArrayList<InitializableMessageSource>();
        if(beanMap == null){
            LOG.warn("No DbMessageSources found to register with the DbMessageSourceReloader");
        }else{
            for(Map.Entry<String,InitializableMessageSource> entry : beanMap.entrySet()){
                InitializableMessageSource source = entry.getValue();
                String beanName = entry.getKey();
                messageSources.add(source);
                LOG.debug("found InitializableMessageSource called " + beanName);
            }
            LOG.debug(messageSources.size() + " InitializableMessageSource found to register with the DbMessageSourceReloader");
            this.reloader.setMessageSources(messageSources);
        }
    }

}
