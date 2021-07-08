package com.oup.eac.advice;

import java.util.Arrays;
import java.util.concurrent.Semaphore;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import com.oup.eac.common.advice.EacPointcuts;

@Aspect
public class WsAuthenticationAdvice {

	public static final Logger LOG = Logger.getLogger(WsAuthenticationAdvice.class);

	private Semaphore sem = new Semaphore(1);
	
	public WsAuthenticationAdvice() {
	}

	@Pointcut(EacPointcuts.WS_AUTHENTICATION)
	public void pcWsAuthentication() {
	}

	@Around("pcWsAuthentication()")
	public Object interceptWsAuthentication(ProceedingJoinPoint pjp) throws Throwable {
		String answer = "undef";		
		try {
			sem.acquire();
			Object result = pjp.proceed();
			answer = String.valueOf(result);			
			return result;
		} catch (Exception ex) {
			answer = ex.toString();
			throw ex;
		} finally {
			String msg = String.format("%s(%s) returns %s", pjp.getSignature(), Arrays.toString(pjp.getArgs()), answer);
			LOG.info(msg);
			sem.release();
		}
	}


}
