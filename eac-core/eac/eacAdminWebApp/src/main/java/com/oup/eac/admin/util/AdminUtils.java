package com.oup.eac.admin.util;

import org.apache.commons.lang.StringUtils;

public class AdminUtils {
    
    public static String escapeSpecialChar(String value){     
        
        if (null != value && (StringUtils.contains(value, "%") || StringUtils.contains(value, "_"))) {          
            value = StringUtils.replace(value, "%", "[%]");
           
            value = StringUtils.replace(value, "_", "[_]");                
        }

        return value;
    }

}
