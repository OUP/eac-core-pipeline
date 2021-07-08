package com.oup.eac.web.validators.registration;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.support.RequestContext;

import com.oup.eac.domain.Component;
import com.oup.eac.domain.Field;
import com.oup.eac.domain.Question;
import com.oup.eac.domain.Tag.TagType;
import com.oup.eac.dto.RegistrationDto;

public class AccountProductFormValidator {

	public static final String DEFAULT_ARGUMENT_SEPARATOR = ",";
	public void validate(final Object obj, final Errors errors, HttpServletRequest request, HttpServletResponse response) {
        RegistrationDto registrationDto = (RegistrationDto) obj;
        registrationDto.cleanAnswers();
        for(Component component : registrationDto.getComponents()) {
	        for (Field field : component.getFields()) {
	        	Question question = field.getElement().getQuestion();
	            String answer = registrationDto.getAnswersForQuestion(field);
	            if (StringUtils.isBlank(answer)) {
	                // validate required fields
	                if (field.isRequired()) {
	                    /*errors.rejectValue("answers['" + question.getId() + "']", "error.not-specified", new Object[] { field.getElement().getTitleText() },
	                    		field.getElement().getTitleText() + " is required.");*/
	                	try {
	        				Object[] msg=resolveMessage(request, "error.not-specified", field.getElement().getTitleText() + " is required.", new Object[] { field.getElement().getTitleText() }, response);
	        				errors.rejectValue("answers['" + question.getId() + "']", "error.not-specified", new Object[] { msg[0] }, field.getElement().getTitleText() + " is required.");
	        			} catch (JspException e) {
	        				// TODO Auto-generated catch block
	        				e.printStackTrace();
	        			}
	                    
	                }
	            } else {
	                if (field.getElement().getTag().getTagType() != TagType.MULTISELECT && StringUtils.isNotBlank(field.getElement().getRegularExpression())) {
	                    Pattern pattern = Pattern.compile(field.getElement().getRegularExpression());
	                    Matcher matcher = pattern.matcher(answer);
	                    if (!matcher.find()) {
	                        /*errors.rejectValue("answers['" + question.getId() + "']", "error.regularexpression", new Object[] { field.getElement()
	                                .getTitleText() }, field.getElement().getTitleText() + " is invalid");*/
	                    	try {
		        				Object[] msg=resolveMessage(request, "error.regularexpression", field.getElement().getTitleText() + " is required.", new Object[] { field.getElement().getTitleText() }, response);
		        				errors.rejectValue("answers['" + question.getId() + "']", "error.regularexpression", new Object[] { msg[0] }, field.getElement().getTitleText() + " is required.");
		        			} catch (JspException e) {
		        				// TODO Auto-generated catch block
		        				e.printStackTrace();
		        			}
	                    }
	                }
	            }
	
	        }
        }
    }
    
    public final Object[] resolveMessage(HttpServletRequest request,String code,String text,Object arguments,HttpServletResponse response) throws JspException {
		/*JspFactory factory = JspFactory.getDefaultFactory();
		PageContext pageContext = factory.getPageContext( (Servlet) this, request, response, null, true, JspWriter.DEFAULT_BUFFER, true);*/
		RequestContext requestContext = new RequestContext(request);
        MessageSource messageSource = requestContext.getMessageSource();
        if (messageSource == null) {
            throw new JspTagException("No corresponding MessageSource found");
        }

      /*  // Evaluate the specified MessageSourceResolvable, if any.
        MessageSourceResolvable resolvedMessage = null;
        if (this.message instanceof MessageSourceResolvable) {
            resolvedMessage = (MessageSourceResolvable) this.message;
        } else if (this.message != null) {
            String expr = this.message.toString();
            resolvedMessage = (MessageSourceResolvable) ExpressionEvaluationUtils.evaluate("message", expr, MessageSourceResolvable.class, pageContext);
        }

        if (resolvedMessage != null) {
            // We have a given MessageSourceResolvable.
            return messageSource.getMessage(resolvedMessage, request.getLocale());
        }*/

       /* String resolvedCode = ExpressionEvaluationUtils.evaluateString("code", code, pageContext);
        String resolvedText = ExpressionEvaluationUtils.evaluateString("text", text, pageContext);
*/Object[] textFinal=null;
        if (code != null || text != null) {
            // We have a code or default text that we need to resolve.
            Object[] argumentsArray = resolveArguments(arguments);
            
            if (text != null) {
                // We have a fallback text to consider.
                /*return messageSource.getMessage(code, resolveArgumentMessages(argumentsArray,request),text, request.getLocale());*/
            	textFinal=resolveArgumentMessages(argumentsArray,request);
            }
            // We have no fallback text to consider.
            /*return messageSource.getMessage(code, resolveArgumentMessages(argumentsArray,request), request.getLocale());*/

        }

        // All we have is a specified literal text.
        return textFinal;
    }
    protected final Object[] resolveArgumentMessages(final Object[] argumentsArray,HttpServletRequest request) {
        if (argumentsArray == null) {
            return null;
        }
        RequestContext requestContext = new RequestContext(request);
        MessageSource messageSource = requestContext.getMessageSource();
        Object[] resolvedArguments = new Object[argumentsArray.length];
        for (int i = 0; i < argumentsArray.length; i++) {
        	try {
        		resolvedArguments[i] = messageSource.getMessage((String) argumentsArray[i], null, request.getLocale());
        	} catch (NoSuchMessageException ex) {
        		//Fallback to argument being argument instead of argument being a key
        		resolvedArguments[i] = argumentsArray[i];
        	}
        }
        return resolvedArguments;
    }

    protected final Object[] resolveArguments(final Object arguments) throws JspException {
        if (arguments instanceof String) {
        	
            String[] stringArray = ((String) arguments).split(DEFAULT_ARGUMENT_SEPARATOR);//StringUtils.delimitedListToStringArray((String) arguments, this.argumentSeparator);
            if (stringArray.length == 1) {
                /*Object argument = ExpressionEvaluationUtils.evaluate("argument", stringArray[0], pageContext);*/
                Object argument = stringArray[0];
                if (argument != null && argument.getClass().isArray()) {
                    return ObjectUtils.toObjectArray(argument);
                }
                return new Object[] { argument };
            }
            Object[] argumentsArray = new Object[stringArray.length];
            for (int i = 0; i < stringArray.length; i++) {
                /*argumentsArray[i] = ExpressionEvaluationUtils.evaluate("argument[" + i + "]", stringArray[i], pageContext);*/
            	argumentsArray[i] = stringArray[i];
                
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

	
}
