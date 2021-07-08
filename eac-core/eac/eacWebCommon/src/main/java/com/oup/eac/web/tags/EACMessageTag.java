package com.oup.eac.web.tags;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.tags.HtmlEscapingAwareTag;
import org.springframework.web.util.ExpressionEvaluationUtils;
import org.springframework.web.util.HtmlUtils;
import org.springframework.web.util.JavaScriptUtils;
import org.springframework.web.util.TagUtils;

/**
 * Copy of spring MessageTag but with added functionality. The tag also resolves the arguments as they are message resource keys in EAC.
 * 
 * @author harlandd
 * @see org.springframework.web.servlet.tags.MessageTag
 */
public class EACMessageTag extends HtmlEscapingAwareTag {

    /**
     * Default separator for splitting an arguments String: a comma (",").
     */
    public static final String DEFAULT_ARGUMENT_SEPARATOR = ",";

    private Object message;

    private String code;

    private Object arguments;

    private String argumentSeparator = DEFAULT_ARGUMENT_SEPARATOR;

    private String text;

    private String var;

    private String scope = TagUtils.SCOPE_PAGE;

    private boolean javaScriptEscape = false;

    /**
     * Set the MessageSourceResolvable for this tag. Accepts a direct MessageSourceResolvable instance as well as a JSP expression language String that points
     * to a MessageSourceResolvable.
     * <p>
     * If a MessageSourceResolvable is specified, it effectively overrides any code, arguments or text specified on this tag.
     * 
     * @param message
     *            The message for this tag
     */
    public final void setMessage(final Object message) {
        this.message = message;
    }

    /**
     * Set the message code for this tag.
     * 
     * @param code
     *            The code for this tag
     */
    public final void setCode(final String code) {
        this.code = code;
    }

    /**
     * Set optional message arguments for this tag, as a comma-delimited String (each String argument can contain JSP EL), an Object array (used as argument
     * array), or a single Object (used as single argument).
     * 
     * @param arguments
     *            The arguments for this message
     */
    public final void setArguments(final Object arguments) {
        this.arguments = arguments;
    }

    /**
     * Set the separator to use for splitting an arguments String. Default is a comma (",").
     * 
     * @param argumentSeparator
     *            The separator
     * @see #setArguments
     */
    public final void setArgumentSeparator(final String argumentSeparator) {
        this.argumentSeparator = argumentSeparator;
    }

    /**
     * Set the message text for this tag.
     * 
     * @param text
     *            The text to set for this tag
     */
    public final void setText(final String text) {
        this.text = text;
    }

    /**
     * Set PageContext attribute name under which to expose a variable that contains the resolved message.
     * 
     * @param var
     *            The var for this tag
     * @see #setScope
     * @see javax.servlet.jsp.PageContext#setAttribute
     */
    public final void setVar(final String var) {
        this.var = var;
    }

    /**
     * Set the scope to export the variable to. Default is SCOPE_PAGE ("page").
     * 
     * @param scope
     *            The scope for this tag
     * @see #setVar
     * @see org.springframework.web.util.TagUtils#SCOPE_PAGE
     * @see javax.servlet.jsp.PageContext#setAttribute
     */
    public final void setScope(final String scope) {
        this.scope = scope;
    }

    /**
     * Set JavaScript escaping for this tag, as boolean value. Default is "false".
     * 
     * @param javaScriptEscape
     *            Should javascript escaping be set to escape
     * 
     * @throws JspException
     *             The exception to be thrown
     */
    public final void setJavaScriptEscape(final String javaScriptEscape) throws JspException {
        this.javaScriptEscape = ExpressionEvaluationUtils.evaluateBoolean("javaScriptEscape", javaScriptEscape, pageContext);
    }

    /**
     * Resolves the message, escapes it if demanded, and writes it to the page (or exposes it as variable).
     * 
     * @return int
     * @throws JspException
     *             The jsp exception
     * @throws IOException
     *             An io exception
     * @see #resolveMessage()
     * @see org.springframework.web.util.HtmlUtils#htmlEscape(String)
     * @see org.springframework.web.util.JavaScriptUtils#javaScriptEscape(String)
     * @see #writeMessage(String)
     */
    protected final int doStartTagInternal() throws JspException, IOException {
        try {
            // Resolve the unescaped message.
            String msg = resolveMessage();

            // HTML and/or JavaScript escape, if demanded.
            if (isHtmlEscape()) {
                msg = HtmlUtils.htmlEscape(msg);
            }
            if (this.javaScriptEscape) {
                msg = JavaScriptUtils.javaScriptEscape(msg);
            }

            // Expose as variable, if demanded, else write to the page.
            String resolvedVar = ExpressionEvaluationUtils.evaluateString("var", this.var, pageContext);
            if (resolvedVar != null) {
                String resolvedScope = ExpressionEvaluationUtils.evaluateString("scope", this.scope, pageContext);
                pageContext.setAttribute(resolvedVar, msg, TagUtils.getScope(resolvedScope));
            } else {
                writeMessage(msg);
            }

            return EVAL_BODY_INCLUDE;
        } catch (NoSuchMessageException ex) {
            throw new JspTagException(getNoSuchMessageExceptionDescription(ex));
        }
    }

    /**
     * Resolve the specified message into a concrete message String. The returned message String should be un-escaped.
     * 
     * @return The spring representing the resolved message.
     * @throws JspException
     *             the jsp exception
     */
    protected final String resolveMessage() throws JspException {
        MessageSource messageSource = getMessageSource();
        if (messageSource == null) {
            throw new JspTagException("No corresponding MessageSource found");
        }

        // Evaluate the specified MessageSourceResolvable, if any.
        MessageSourceResolvable resolvedMessage = null;
        if (this.message instanceof MessageSourceResolvable) {
            resolvedMessage = (MessageSourceResolvable) this.message;
        } else if (this.message != null) {
            String expr = this.message.toString();
            resolvedMessage = (MessageSourceResolvable) ExpressionEvaluationUtils.evaluate("message", expr, MessageSourceResolvable.class, pageContext);
        }

        if (resolvedMessage != null) {
            // We have a given MessageSourceResolvable.
            return messageSource.getMessage(resolvedMessage, getRequestContext().getLocale());
        }

        String resolvedCode = ExpressionEvaluationUtils.evaluateString("code", this.code, pageContext);
        String resolvedText = ExpressionEvaluationUtils.evaluateString("text", this.text, pageContext);

        if (resolvedCode != null || resolvedText != null) {
            // We have a code or default text that we need to resolve.
            Object[] argumentsArray = resolveArguments(this.arguments);
            if (resolvedText != null) {
                // We have a fallback text to consider.
                return messageSource.getMessage(resolvedCode, resolveArgumentMessages(argumentsArray), resolvedText, getRequestContext().getLocale());
            }
            // We have no fallback text to consider.
            return messageSource.getMessage(resolvedCode, resolveArgumentMessages(argumentsArray), getRequestContext().getLocale());

        }

        // All we have is a specified literal text.
        return resolvedText;
    }

    /**
     * The argument messages to resolve.
     * 
     * @param argumentsArray
     *            The argument messagesto resolve
     * @return The resolved argument messages
     */
    protected final Object[] resolveArgumentMessages(final Object[] argumentsArray) {
        if (argumentsArray == null) {
            return null;
        }
        MessageSource messageSource = getMessageSource();
        Object[] resolvedArguments = new Object[argumentsArray.length];
        for (int i = 0; i < argumentsArray.length; i++) {
        	try {
        		resolvedArguments[i] = messageSource.getMessage((String) argumentsArray[i], null, getRequestContext().getLocale());
        	} catch (NoSuchMessageException ex) {
        		//Fallback to argument being argument instead of argument being a key
        		resolvedArguments[i] = argumentsArray[i];
        	}
        }
        return resolvedArguments;
    }

    /**
     * Resolve the given arguments Object into an arguments array.
     * 
     * @param arguments
     *            the specified arguments Object
     * @return the resolved arguments as array
     * @throws JspException
     *             if argument conversion failed
     * @see #setArguments
     */
    protected final Object[] resolveArguments(final Object arguments) throws JspException {
        if (arguments instanceof String) {
            String[] stringArray = StringUtils.delimitedListToStringArray((String) arguments, this.argumentSeparator);
            if (stringArray.length == 1) {
                Object argument = ExpressionEvaluationUtils.evaluate("argument", stringArray[0], pageContext);
                if (argument != null && argument.getClass().isArray()) {
                    return ObjectUtils.toObjectArray(argument);
                }
                return new Object[] { argument };
            }
            Object[] argumentsArray = new Object[stringArray.length];
            for (int i = 0; i < stringArray.length; i++) {
                argumentsArray[i] = ExpressionEvaluationUtils.evaluate("argument[" + i + "]", stringArray[i], pageContext);
            }
            return argumentsArray;
        } else if (arguments instanceof Object[]) {
            return (Object[]) arguments;
        } else if (arguments instanceof Collection) {
            return ((Collection) arguments).toArray();
        } else if (arguments != null) {
            // Assume a single argument object.
            return new Object[] { arguments };
        } else {
            return null;
        }
    }

    /**
     * Write the message to the page.
     * <p>
     * Can be overridden in subclasses, e.g. for testing purposes.
     * 
     * @param msg
     *            the message to write
     * @throws IOException
     *             if writing failed
     */
    protected final void writeMessage(final String msg) throws IOException {
        pageContext.getOut().write(String.valueOf(msg));
    }

    /**
     * Use the current RequestContext's application context as MessageSource.
     * 
     * @return The message source available in the request context.
     */
    protected final MessageSource getMessageSource() {
        return getRequestContext().getMessageSource();
    }

    /**
     * Return default exception message.
     * 
     * @param ex
     *            The exception
     * @return The exception description
     */
    protected final String getNoSuchMessageExceptionDescription(final NoSuchMessageException ex) {
        return ex.getMessage();
    }
}
