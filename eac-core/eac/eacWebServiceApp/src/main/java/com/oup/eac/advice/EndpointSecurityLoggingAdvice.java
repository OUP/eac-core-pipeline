package com.oup.eac.advice;

import org.apache.log4j.Logger;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import com.oup.eac.common.advice.BaseSecurityLoggingAdvice;
import com.oup.eac.common.advice.EacPointcuts;

@Aspect
public class EndpointSecurityLoggingAdvice extends BaseSecurityLoggingAdvice {

    public static final Logger LOG = Logger.getLogger(EndpointSecurityLoggingAdvice.class);

    @Override
    public Logger getLogger() {
        return LOG;
    }

    @Override
    @Pointcut(EacPointcuts.WEB_SERVICE_ENDPOINT_METHODS)
    public void secureMethod() {
    }

}
