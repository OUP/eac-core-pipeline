package com.oup.eac.web.tags;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.tags.RequestContextAwareTag;

import com.oup.eac.service.ActivationCodeService;

public class BatchSizeTag extends RequestContextAwareTag {

    private String batchId;
    
    private String var;
    
    @Override
    protected int doStartTagInternal() throws Exception {
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        ActivationCodeService activationCodeService = getService("activationCodeService", ActivationCodeService.class);
        Integer value = activationCodeService.getNumberOfTokensInBatch(batchId);
        request.setAttribute(var, value);
        return SKIP_BODY;
    }

    private <T> T getService(String serviceName, Class<T> serviceType) {
        ApplicationContext appContext = getRequestContext().getWebApplicationContext();
        T service;
        if (serviceName != null) {
            service = appContext.getBean(serviceName, serviceType);
        } else {
            service = appContext.getBean(serviceType);
        }
        return service;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getVar() {
        return var;
    }

    public void setVar(String var) {
        this.var = var;
    }

}
