package com.oup.eac.advice;

import org.apache.log4j.Logger;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import com.oup.eac.common.advice.BaseTimeLoggingAdvice;
import com.oup.eac.common.advice.EacPointcuts;

@Aspect
public class ServiceTimeLoggingAdvice extends BaseTimeLoggingAdvice {

    public static final Logger LOG = Logger.getLogger(ServiceTimeLoggingAdvice.class);

    @Override
    public Logger getLogger() {
        return LOG;
    }

	@Override
	@Pointcut(EacPointcuts.SERVICE_LAYER_METHODS)
	public void logTiming() {
	}
}
