package com.oup.eac.advice;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.log4j.Logger;

import com.oup.eac.ws.v2.binding.common.ErrorStatus;
import com.oup.eac.ws.v2.binding.common.types.StatusCode;

public final class ErrorResponseFactory {

    private static final Logger LOG = Logger.getLogger(ErrorResponseFactory.class);

    private static final String RESPONSE_STATUS = "errorStatus";

    private static final Class<? extends ErrorStatus> RESPONSE_STATUS_CLASS = ErrorStatus.class;

    protected Object getErrorResponse(Class<?> responseType, String message) {
        Object result = null;
        try {

            PropertyDescriptor pd = new PropertyDescriptor(RESPONSE_STATUS, responseType);

            Method getter = pd.getReadMethod();
            Method setter = pd.getWriteMethod();

            boolean isValid = getter != null && setter != null;
            isValid = isValid && RESPONSE_STATUS_CLASS.equals(getter.getReturnType());
            isValid = isValid && setter.getParameterTypes().length == 1;
            isValid = isValid && RESPONSE_STATUS_CLASS.equals(setter.getParameterTypes()[0]);

            if (isValid) {

                ErrorStatus status = new ErrorStatus();
                status.setStatusCode(StatusCode.SERVER_ERROR);
                status.setStatusReason(message);

                Object response = responseType.newInstance();
                setter.invoke(response, status);
                result = response;
            }
            // we ignore checked exceptions from reflection - just means the response type does not support set/get ErrorStatus.
        } catch (IllegalArgumentException e) {
            logReflectionProblem(responseType, e);
        } catch (IllegalAccessException e) {
            logReflectionProblem(responseType, e);
        } catch (InvocationTargetException e) {
            logReflectionProblem(responseType, e);
        } catch (InstantiationException e) {
            logReflectionProblem(responseType, e);
        } catch (IntrospectionException e) {
            logReflectionProblem(responseType, e);
        }
        return result;
    }

    private void logReflectionProblem(Class<?> responseType, Exception e) {
        if (LOG.isTraceEnabled()) {
            String msg = String.format("The class %s does not support the 'errorStatus' property", responseType.getName());
            LOG.trace(msg, e);
        }
    }
}
