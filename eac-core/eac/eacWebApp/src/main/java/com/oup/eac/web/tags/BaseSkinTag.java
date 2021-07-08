package com.oup.eac.web.tags;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.tags.RequestContextAwareTag;

import com.oup.eac.domain.UrlSkin;
import com.oup.eac.domain.UrlSkinInfo;
import com.oup.eac.service.DefaultUrlSkinService;
import com.oup.eac.web.controllers.helpers.SessionHelper;
import com.oup.eac.web.utils.UrlCustomiser;

public abstract class BaseSkinTag extends RequestContextAwareTag {

    private static final Logger LOG = Logger.getLogger(BaseSkinTag.class);
    
    public static final String BEAN_NAME_DEFAULT_URL_SKIN_SERVICE = "defaultUrlSkinService";

    private String var;

    /**
     * {@inheritDoc}
     */
    @Override
    protected final int doStartTagInternal() throws Exception {

        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        String value = getValue(request);
        if (StringUtils.isBlank(value)) {
            ApplicationContext appContext = getApplicationContext();
            DefaultUrlSkinService service = appContext.getBean(BEAN_NAME_DEFAULT_URL_SKIN_SERVICE, DefaultUrlSkinService.class);
            if (service != null) {
                UrlSkin defaultUrlSkin = service.getDefaultUrlSkin();
                value = getDefaultValue(defaultUrlSkin);
            }
        }
        request.setAttribute(var, value);
        return SKIP_BODY;
    }

    /**
     * Gets the value.
     * 
     * @param request
     *            the request
     * @return the value
     */
    public abstract String getValue(HttpServletRequest request);

    /**
     * Gets the default value.
     * 
     * @param defaultUrlSkin
     *            the default url skin
     * @return the default value
     */
    public abstract String getDefaultValue(UrlSkin defaultUrlSkin);

    /**
     * Gets the var.
     * 
     * @return the var
     */
    public final String getVar() {
        return var;
    }

    /**
     * Sets the var.
     * 
     * @param var
     *            the new var
     */
    public final void setVar(final String var) {
        this.var = var;
    }

    /**
     * Gets the application context.
     *
     * @return the application context
     */
    protected final ApplicationContext getApplicationContext() {
        ApplicationContext appContext = getRequestContext().getWebApplicationContext();
        return appContext;
    }

    protected UrlSkinInfo getUrlSkin(final HttpServletRequest request){
    	final UrlSkin origSkin = SessionHelper.getUrlSkin(request);    	
    	if(origSkin == null){
    		return origSkin;
    	}
    	UrlSkinInfo result = origSkin;
    	String customiserBeanName = origSkin.getUrlCustomiserBean();
    	
    	UrlCustomiser temp = null;
    	if(StringUtils.isNotBlank(customiserBeanName)){
    		try{
    			temp = getApplicationContext().getBean(customiserBeanName, UrlCustomiser.class);
    		}catch(Exception ex){
    			String msg = String.format("failed to get UrlCustomiserBean with name[%s]", customiserBeanName); 
    			LOG.warn(msg,ex);
    		}
    	}
    	final UrlCustomiser iser = temp;
    	if(iser != null){
    		UrlSkinInfo skin = new UrlSkinInfo(){

				@Override
				public String getSkinPath() {
					return origSkin.getSkinPath();
				}

				@Override
				public String getUrl() {
					String result = iser.customiseUrl(origSkin.getUrl(), request);
					return result;
				}

				@Override
				public String getSiteName() {
					return origSkin.getSiteName();
				}

				@Override
				public String getContactPath() {
				    String result = iser.customiseUrl(origSkin.getContactPath(), request);
					return result;
				}
    			
    		};
    		result = skin;
    	}
    	return result;
    }
}
