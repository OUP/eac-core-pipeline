package com.oup.eac.integration.erights;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.HandlerResolver;
import javax.xml.ws.handler.PortInfo;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.oup.eac.integration.facade.impl.ErightsFacadeImpl;

@Component(value="erightsHandlerResolver")
public class ErightsHandlerResolver implements HandlerResolver {
	
	private static final Logger LOGGER = Logger.getLogger(ErightsFacadeImpl.class);
	
	private final Map<PortInfo, List<Handler>> handlerMap = new HashMap<PortInfo, List<Handler>>();
	
	//TODO setting property like this does not work. Check with spring 3.1.0 or higher (does not work without :true either)
	@Value(value="${ws.security.enabled:\"true\"}")
	private boolean securityEnabled = true;

	@Override
	public List<Handler> getHandlerChain(PortInfo portInfo) {

		List<Handler> handlerChain = handlerMap.get(portInfo);
        
		if (handlerChain == null) {
            handlerChain = createHandlerChain(portInfo);
            handlerMap.put(portInfo, handlerChain);
        }
		
        return handlerChain;
	}
	
	private List<Handler> createHandlerChain(PortInfo portInfo) {
	
		List<Handler> chain =  new ArrayList<Handler>();
	        
        if (securityEnabled) {
        	chain.add(new SecurityHeaderHandler());
        }

        // If debug is enabled lets enable some extra handlers
        if (LOGGER.isDebugEnabled()) {
        	chain.add(new LoggingSOAPHandler());
        	chain.add(new PerformanceSOAPHandler());
        }
        
	        
        return chain;
    }

}
