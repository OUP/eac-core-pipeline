package com.oup.eac.web.tags;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.web.servlet.tags.RequestContextAwareTag;

import com.oup.eac.domain.ActivationCode;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.ProgressBar;
import com.oup.eac.domain.ProgressBarContext;
import com.oup.eac.domain.ProgressBarElement;
import com.oup.eac.domain.ProgressBarElement.ElementType;
import com.oup.eac.domain.RegisterableProduct;
import com.oup.eac.service.ProgressBarService;
import com.oup.eac.web.controllers.helpers.SessionHelper;

public class ProgressBarTag extends RequestContextAwareTag {

	private static final String PROGRESS_BAR_SERVICE = "progressBarService";
    
    private String page;

    /**
	 * @return the page
	 */
	public final String getPage() {
		return page;
	}

	/**
	 * @param page the page to set
	 */
	public final void setPage(String page) {
		this.page = page;
	}

	/**
     * {@inheritDoc}
     */
    @Override
    public final int doStartTagInternal() throws JspException {
        if (StringUtils.isBlank(page)) {
            return SKIP_BODY;
        }     
        
        ProgressBarService progressBarService = getService(ProgressBarService.class, PROGRESS_BAR_SERVICE);
        
        HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
        
        Customer customer = SessionHelper.getCustomer(request);
        ActivationCode activationCode = SessionHelper.getActivationCode(request);
        RegisterableProduct registerableProduct = SessionHelper.getRegisterableProduct(request);
        String registrationId = SessionHelper.getRegistrationId(request);
        Locale locale = SessionHelper.getLocale(request);
        
        ProgressBar progressBar = progressBarService.getProgressBar(new ProgressBarContext(page, customer, activationCode, registerableProduct, registrationId, locale));
        
        if(progressBar == null) {
        	return SKIP_BODY;
        }
        
        try {
        	
        	pageContext.getOut().write(getHtml(progressBar));
        } catch (Exception e) {
            e.printStackTrace();
        }        
        return EVAL_BODY_INCLUDE;
    }
    
    private String getHtml(ProgressBar progressBar) {
    	
    	MessageSource messageSource = getMessageSource();
    	StringBuilder html = new StringBuilder();
    	int count=1;
    	html.append("<div class=\"row\">");
    	html.append("<div class=\"col large-12 large-centered\">");
    	html.append("<ol class=\"progressBar\" role=\"progressbar\" aria-valuenow=\"2\" aria-valuemin=\"1\" aria-valuetext=\"Billing address\" aria-valuemax=\"3\">");
    	for(ProgressBarElement element : progressBar.getElements()) {
    		String label = messageSource.getMessage(element.getLabel(), null, element.getDefaultMessage(), getRequestContext().getLocale());
    		if(element.getElementType() == ElementType.CURRENT_COMPLETED_STEP)
    		{
    			if(StringUtils.isNotBlank(label)) {
    			html.append("<li class=\"progressBar_step progressBar_step--current\">");
    			html.append("<span class=\"progressBar_stepNumber progressBar_stepNumber--current\">");
    			html.append(count);
    			html.append("</span>");
    			html.append("<span class=\"progressBar_stepText progressBar_stepText--current\">");
    			html.append(label);
    			html.append("</span></li>");
    			}
    			else
    			{
    				html.append("<li class=\"progressBar_step progressBar_completeStep\">");//.append("style=\" content: '';width: 151%;height: 0.625rem;background: #165EAA;background-image: linear-gradient(to bottom,#196ac1 0,#165EAA 90%,#135293 100%);z-index: 1;margin-top: 1.25em;\">");
        			html.append("<span class=\"progressBar_stepNumber progressBar_stepNumber--complete\"").append("style=\"top: -0.8em;\">");
        			html.append("&#x2713");
        			html.append("</span>");
        			html.append("<span class=\"progressBar_stepText progressBar_stepText--current\">");
        			if(StringUtils.isNotBlank(label))
        			html.append(label);
        			html.append("</span></li>");
    			}
    		}
    		else if(element.getElementType() == ElementType.INCOMPLETE_STEP) {
    			html.append("<li class=\"progressBar_step\">");
    			html.append("<span class=\"progressBar_stepNumber\">");
    			html.append(count);
    			html.append("</span>");
    			html.append("<span class=\"progressBar_stepText\">");
    			if(StringUtils.isNotBlank(label))
    			html.append(label);
    			html.append("</span></li>");    			
    		}
    		count++;
    	}
    	html.append("</ol>");
    	html.append("</div>");
    	html.append("</div>");
    	return html.toString();
    }


    protected final MessageSource getMessageSource() {
        return getRequestContext().getMessageSource();
    }
    
	private final <T> T getService(Class<T> aClass, String serviceName) {
    	return getRequestContext().getWebApplicationContext().getBean(serviceName, aClass);
    }
    
}
