package com.oup.eac.web.tags;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.oup.eac.common.utils.EACSettings;
import com.oup.eac.web.controllers.helpers.SessionHelper;

public class EACLoginUrlTag extends SimpleTagSupport {

	private static final Logger LOG = Logger.getLogger(EACLoginUrlTag.class);

	private static final String FORWARD_URL = "FORWARDURL";
	public static final String URL_REGEX = "^http(s{0,1}://)[A-Za-z0-9-]+[.]{1}[A-Za-z0-9-]+([.]{0,1}[A-Za-z0-9-])*";

	private static char QUESTION_MARK = '?';
	private static char EQUALS = '=';
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
        
        HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
        
        String loginUrl = getLoginUrl(request);
        
        try {
        	if (var == null) {
        		pageContext.getOut().print(loginUrl);
        	} else {
        		pageContext.setAttribute(var, loginUrl);
        	}
        } catch (Exception e) {
			LOG.error(e, e);
        }
    }
	
	private String getLoginUrl(HttpServletRequest request) {

		String forwardUrl = "";
		String loginUrl = EACSettings.getProperty(propertyName, defaultVaule);
		StringBuilder url = new StringBuilder(loginUrl);

		if(request.getSession().getAttribute(FORWARD_URL)!=null && isValidUrl((String)request.getSession().getAttribute(FORWARD_URL))){
			forwardUrl = SessionHelper.getForwardUrl(request);
			String loginParamName = EACSettings.getProperty(EACSettings.ERIGHTS_URL_PARAMETER_NAME);
			if(StringUtils.isNotBlank(forwardUrl)) {
				url.append(QUESTION_MARK).append(loginParamName).append(EQUALS).append(forwardUrl);
			}
		}
		return url.toString();
	}
	
	public static boolean isValidUrl(String url) {
    	url.matches(URL_REGEX);
        boolean doesMatch = url.matches(URL_REGEX);
        return doesMatch;
    }
}
