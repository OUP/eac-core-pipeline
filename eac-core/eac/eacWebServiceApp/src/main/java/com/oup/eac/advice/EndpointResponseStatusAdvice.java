package com.oup.eac.advice;

import java.util.Arrays;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.util.Assert;

import com.oup.eac.common.advice.EacPointcuts;
import com.oup.eac.service.ServiceLayerException;
import com.oup.eac.ws.v2.ex.WebServiceException;

@Aspect
public class EndpointResponseStatusAdvice {

    public static final Logger LOG = Logger.getLogger(EndpointResponseStatusAdvice.class);

    private final ErrorResponseFactory responseFactory;

    public EndpointResponseStatusAdvice() {
        responseFactory = new ErrorResponseFactory();
    }

    @Pointcut(EacPointcuts.WEB_SERVICE_ENDPOINT_METHODS)
    public void pcWsEndpoints() {
    }

    /**
     * This aspect catches ServiceLayerException (and runtime exceptions ) thrown by the 'spring-ws endpoints' and uses reflection to set the ErrorStatus's StatusCode to SERVER_ERROR.
     * In addition, the ErrorStatus's statusReason to be the Exception's message text.
     * 
     * @param pjp
     *            the proceeding join point
     * @return
     * @throws Throwable
     *             - these will only be Errors or RuntimeExceptions - these will be translated into faults in the response message.
     */
    @Around("pcWsEndpoints()")
    public Object handleServiceLayerException(ProceedingJoinPoint pjp) throws Throwable {
        Object result = null;
        try {
            result = pjp.proceed();
        } catch (ServiceLayerException sle) {
            result = getResultWithResponseCodeSet(Level.WARN, pjp, sle);
        } catch (WebServiceException wse) {
            result = getResultWithResponseCodeSet(Level.WARN, pjp, wse);
        } catch (RuntimeException rte) {
            result = getResultWithResponseCodeSet(Level.ERROR, pjp, rte);
        }
        return result;
    }

    private Object getResultWithResponseCodeSet(Level level,ProceedingJoinPoint pjp, Throwable th) throws Throwable {
        Assert.isTrue(th != null);
        boolean returnedAsFault = false;
        try {
            Object result = null;
            Signature sig = pjp.getSignature();
            if (sig instanceof MethodSignature) {
                MethodSignature mSig = (MethodSignature) sig;
                Class<?> responseType = mSig.getReturnType();
                String message = th.getMessage();
                result = responseFactory.getErrorResponse(responseType, message);
            }
            if (result == null) {
                returnedAsFault = true;
                throw th;
            }
            return result;
        } finally {
            String method = pjp.getSignature().toLongString();
            String args = Arrays.deepToString(pjp.getArgs());
            String msg = String.format(
                    "Problem executing Eac Web Service method[%s] args[%s] returnedAsFault[%b] message[%s]",
                    method,
                    args, 
                    returnedAsFault, 
                    th.getMessage());
            if (th instanceof AccessDeniedException) {
                LOG.log(level, msg);
            } else {
                LOG.log(level, msg, th);
            }
        }
    }

}
