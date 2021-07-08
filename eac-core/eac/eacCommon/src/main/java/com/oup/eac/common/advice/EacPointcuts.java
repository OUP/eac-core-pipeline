package com.oup.eac.common.advice;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class EacPointcuts {

    public static final String WEB_SERVICE_ENDPOINT_METHODS = "com.oup.eac.common.advice.EacPointcuts.webServiceEndpointMethods()";
    public static final String SERVICE_LAYER_METHODS = "com.oup.eac.common.advice.EacPointcuts.serviceLayerMethods()";    
    public static final String WS_V2_ADAPTER_METHODS = "com.oup.eac.common.advice.EacPointcuts.v2WsAdapterMethods()";    
    public static final String XML_MESSAGE_VALIDATION = "com.oup.eac.common.advice.EacPointcuts.xmlMessageValidation()";    
    public static final String WS_AUTHENTICATION = "com.oup.eac.common.advice.EacPointcuts.webServiceAuthentication()";    
    
    @Pointcut("execution(@org.springframework.ws.server.endpoint.annotation.PayloadRoot * *.*(..))")
    public void webServiceEndpointMethods(){}
    
    @Pointcut("execution(* com.oup.eac.service.*Service.*(..))")
    public void serviceLayerMethods(){}
    
    @Pointcut("execution(* com.oup.eac.ws.v2.service.*Adapter.*(..))")
    public void v2WsAdapterMethods(){}
    
    @Pointcut("execution(* org.springframework.xml.validation.XmlValidatorFactory.createValidator(org.springframework.core.io.Resource[],String))")
    public void xmlMessageValidation(){}

    @Pointcut("execution(* com.sun.xml.wss.impl.misc.DefaultSecurityEnvironmentImpl.authenticateUser(java.util.Map, java.lang.String, java.lang.String))")
    public void webServiceAuthentication(){}

}
