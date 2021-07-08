package com.oup.eac.advice;

import org.apache.log4j.Logger;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import com.oup.eac.common.advice.BaseTimeLoggingAdvice;
import com.oup.eac.common.advice.EacPointcuts;

@Aspect
public class EndpointTimeLoggingAdvice extends BaseTimeLoggingAdvice {

    public static final Logger LOG = Logger.getLogger(EndpointTimeLoggingAdvice.class);

    @Override
    public Logger getLogger() {
        return LOG;
    }

    @Override
    @Pointcut(EacPointcuts.WEB_SERVICE_ENDPOINT_METHODS)
    public void logTiming() {
    }

}
