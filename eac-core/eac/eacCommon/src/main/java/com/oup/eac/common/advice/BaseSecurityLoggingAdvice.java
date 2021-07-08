package com.oup.eac.common.advice;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Aspect
public abstract class BaseSecurityLoggingAdvice extends BaseLoggingAdvice {

	@Pointcut()
	public abstract void secureMethod();

	@Before("secureMethod()")
	public void profile(JoinPoint jp) throws Throwable {
		Logger log = getLogger();
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			String name = auth.getName();
			if(log.isDebugEnabled()){
				String msg = String.format("method [%s] called by [%s]",jp.toLongString(),name);
				log.debug(msg);
			}
		} catch (Exception ex) {
			log.error("problem getting authenticated user ",ex);
		}
	}


}
