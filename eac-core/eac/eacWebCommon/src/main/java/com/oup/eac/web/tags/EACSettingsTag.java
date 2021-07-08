package com.oup.eac.web.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.oup.eac.common.utils.EACSettings;

public class EACSettingsTag extends SimpleTagSupport {

	private String var;
	private String propertyName;
	private String defaultVaule;
	
	public String getVar() {
		return var;
	}

	public void setVar(String var) {
		this.var = var;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
	
	public String getDefaultVaule() {
		return defaultVaule;
	}

	public void setDefaultVaule(String defaultVaule) {
		this.defaultVaule = defaultVaule;
	}

	public final void doTag() throws JspException {

        PageContext pageContext = (PageContext) getJspContext();
        
        try {
        	if (var == null) {
        		pageContext.getOut().print(EACSettings.getProperty(propertyName, defaultVaule));
        	} else {
        		pageContext.setAttribute(var, EACSettings.getProperty(propertyName, defaultVaule));
        	}
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
