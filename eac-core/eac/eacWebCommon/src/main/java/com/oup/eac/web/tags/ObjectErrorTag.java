package com.oup.eac.web.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

public class ObjectErrorTag extends SimpleTagSupport {

    private ObjectError error;
    
    private Object argument;

    private String var;
    
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
     * @param var the new var
     */
    public final void setVar(final String var) {
        this.var = var;
    }


    /**
     * Gets the error.
     *
     * @return the error
     */
    public final ObjectError getError() {
        return error;
    }

    /**
     * Sets the error.
     *
     * @param error the new error
     */
    public final void setError(final ObjectError error) {
        this.error = error;
    }

    /**
     * Gets the argument.
     *
     * @return the argument
     */
    public final Object getArgument() {
        return argument;
    }

    /**
     * Sets the argument.
     *
     * @param argument the new argument
     */
    public final void setArgument(final Object argument) {
        this.argument = argument;
    }

    @Override    
    public final void doTag() throws JspException {
        ObjectError result;
         if (error instanceof FieldError) {
             FieldError orig = (FieldError) error;
             result = new FieldError(error.getObjectName(), orig.getField(), orig.getRejectedValue(), orig.isBindingFailure(),
                     error.getCodes(), new Object[]{ argument }, error.getDefaultMessage());
         } else {
             result = new ObjectError(error.getObjectName(), error.getCodes(), new Object[]{ argument }, error.getDefaultMessage());
         }
         getJspContext().setAttribute(var, result);
    }

}
