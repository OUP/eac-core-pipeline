package com.oup.eac.web.tags;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.tags.RequestContextAwareTag;

public abstract class BaseGetValueTag extends RequestContextAwareTag {

    private String var;

    @Override
    protected int doStartTagInternal() throws Exception {
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        request.setAttribute(var, getValue());
        return SKIP_BODY;
    }
    
    public abstract Object getValue() throws Exception;

    protected <T> T getService(String serviceName, Class<T> serviceType) {
        ApplicationContext appContext = getRequestContext().getWebApplicationContext();
        T service;
        if (serviceName != null) {
            service = appContext.getBean(serviceName, serviceType);
        } else {
            service = appContext.getBean(serviceType);
        }
        return service;
    }

   

    public String getVar() {
        return var;
    }

    public void setVar(String var) {
        this.var = var;
    }

}
