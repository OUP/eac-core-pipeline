package com.oup.eac.accounts.controllers;

import java.beans.PropertyEditorSupport;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;

import com.oup.eac.dto.FieldMessage;
import com.oup.eac.dto.Message;
import com.oup.eac.dto.Message.MessageType;
import com.oup.eac.service.ServiceLayerException;

/**
 * @author Gaurav Soni
 * 
 */

public class AccountsBindingErrorController {

    /**
     * Convert messages.
     * 
     * @param e
     *            the exception
     * @param errors
     *            the errors
     */
    public static final void convertMessages(final ServiceLayerException e, final Errors errors) {
        if (e.getMessages() != null && e.getMessages().size() > 0) {
            for (Message message : e.getMessages()) {
                if (message.getMessageType() == MessageType.MESSAGE) {
                    if (message.getArgs() != null && message.getArgs().size() > 0) {
                        errors.reject(message.getMessageKey(), message.getArgs().toArray(), message.getDefaultMessage());
                    } else {
                        errors.reject(message.getMessageKey(), message.getDefaultMessage());
                    }
                } else {
                    FieldMessage fieldMessage = (FieldMessage) message;
                    if (fieldMessage.getArgs() != null && fieldMessage.getArgs().size() > 0) {
                        errors.rejectValue(fieldMessage.getFieldName(), fieldMessage.getMessageKey(), fieldMessage.getArgs().toArray(), fieldMessage.getDefaultMessage());
                    } else {
                        errors.rejectValue(fieldMessage.getFieldName(), fieldMessage.getMessageKey(), fieldMessage.getDefaultMessage());
                    }
                }
            }
        }
    }
    
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception  {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));  
        binder.registerCustomEditor(String.class, "username", new LowerCaseStringPropertyEditor());
        binder.registerCustomEditor(String.class, "email", new LowerCaseStringPropertyEditor());
    }
    
    public final static class LowerCaseStringPropertyEditor extends PropertyEditorSupport {
        @Override
        public void setAsText(String text) {            
            if (StringUtils.isBlank(text)) {
                setValue(null);
            } else {
            	setValue(text.toLowerCase().trim());
            }

        }
        
        public String getAsText() {
            if (getValue() instanceof String) {
                return (String)getValue();
            }
            return ("");
        }
    }
}
