package com.oup.eac.common.utils.email;

import java.util.HashMap;
import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;
import org.springframework.ui.velocity.VelocityEngineUtils;

public class VelocityUtils {

    public static final String NEW_LINE_VALUE;
    
    public static final String NEW_LINE_KEY="nl";
    
    static {
        NEW_LINE_VALUE = System.getProperty("line.separator");
    }

    private VelocityUtils() {
        
    }

    public static String mergeTemplateIntoString(
            VelocityEngine velocityEngine, String templateLocation, Map<String,Object> model)
            throws VelocityException {        
        addNewLineToMap(model);
        
        String result = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, templateLocation, model);
        return result;
    }

    private static void addNewLineToMap(Map<String,Object> model){
        model.put(NEW_LINE_KEY, NEW_LINE_VALUE);
    }
    
    public static VelocityContext createVelocityContext() {
        Map<String,Object> model = new HashMap<String,Object>();
        addNewLineToMap(model);
        VelocityContext ctx = new VelocityContext(model);
        return ctx;
    }
}
