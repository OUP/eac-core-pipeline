package com.oup.eac.common.advice;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.util.StopWatch;

@Aspect
public abstract class BaseTimeLoggingAdvice extends BaseLoggingAdvice {

    @Pointcut
    public abstract void logTiming();

    @Around("logTiming()")
    public Object profile(ProceedingJoinPoint pjp) throws Throwable {
        Logger log = getLogger();
        StopWatch sw = new StopWatch(getClass().getSimpleName());
        Object result = null;
        try {
            sw.start(pjp.getSignature().getName());
            result = pjp.proceed();

            return result;
        } finally {
            sw.stop();
            String msg = String.format("Timing of [%s] took [%s]ms", pjp.toLongString(), sw.getTotalTimeMillis());
            log.debug(msg);
        }
    }

}
