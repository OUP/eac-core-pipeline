package com.oup.eac.web.validators.registration;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;

import com.oup.eac.domain.Component;
import com.oup.eac.domain.Field;
import com.oup.eac.domain.Question;
import com.oup.eac.domain.Tag.TagType;
import com.oup.eac.dto.RegistrationDto;
import com.oup.eac.web.validators.EACValidator;

/**
 * @author harlandd Registration form validator
 */
@org.springframework.stereotype.Component
public class RegistrationFormValidator extends EACValidator {

    /**
     * @param clazz
     *            the class
     * @return does this validator support this class
     */
    @SuppressWarnings("rawtypes")
	public boolean supports(final Class clazz) {
        return RegistrationDto.class.isAssignableFrom(clazz);
    }

    /**
     * @param obj
     *            the command object
     * @param errors
     *            the errors
     */
    public void validate(final Object obj, final Errors errors) {
        RegistrationDto registrationDto = (RegistrationDto) obj;
        registrationDto.cleanAnswers();
        for(Component component : registrationDto.getComponents()) {
	        for (Field field : component.getFields()) {
	        	Question question = field.getElement().getQuestion();
	            String answer = registrationDto.getAnswersForQuestion(field);
	            if (StringUtils.isBlank(answer)) {
	                // validate required fields
	                if (field.isRequired()) {
	                    errors.rejectValue("answers['" + question.getId() + "']", "error.not-specified", new Object[] { field.getElement().getTitleText() },
	                    		field.getElement().getTitleText() + " is required.");
	                }
	            } else {
	                if (field.getElement().getTag().getTagType() != TagType.MULTISELECT && StringUtils.isNotBlank(field.getElement().getRegularExpression())) {
	                    Pattern pattern = Pattern.compile(field.getElement().getRegularExpression());
	                    Matcher matcher = pattern.matcher(answer);
	                    if (!matcher.find()) {
	                        errors.rejectValue("answers['" + question.getId() + "']", "error.regularexpression", new Object[] { field.getElement()
	                                .getTitleText() }, field.getElement().getTitleText() + " is invalid");
	                    }
	                }
	            }
	
	        }
        }
    }
    
}
