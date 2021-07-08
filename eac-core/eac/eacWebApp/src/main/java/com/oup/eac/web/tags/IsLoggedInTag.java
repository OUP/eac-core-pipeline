package com.oup.eac.web.tags;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.oup.eac.domain.Customer;
import com.oup.eac.web.controllers.helpers.SessionHelper;

public class IsLoggedInTag extends SimpleTagSupport {

    private String var;

    /**
     * @return the var
     */
    public final String getVar() {
        return var;
    }

    /**
     * @param varP
     *            the var to set
     */
    public final void setVar(final String varP) {
        this.var = varP;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void doTag() throws JspException {
        if (var == null) {
            return;
        }        
        PageContext pageContext = (PageContext) getJspContext();
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        Customer customer = SessionHelper.getCustomer(request);
        Boolean isLoggedIn = customer != null && customer.getCreatedDate() != null;
        request.setAttribute(var, isLoggedIn);
    }
}
