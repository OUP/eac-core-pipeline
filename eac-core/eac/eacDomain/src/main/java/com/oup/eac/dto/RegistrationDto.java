package com.oup.eac.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.oup.eac.common.utils.lang.EACStringUtils;
import com.oup.eac.domain.Answer;
import com.oup.eac.domain.Component;
import com.oup.eac.domain.Field;
import com.oup.eac.domain.Question;
import com.oup.eac.domain.Tag;
import com.oup.eac.domain.Tag.TagType;

/**
 * @author harlandd Account registration dto.
 */
public abstract class RegistrationDto implements Serializable {

    private static final Logger LOG = Logger.getLogger(RegistrationDto.class);
    
    private final List<Component> components = new ArrayList<Component>();

    private final Map<String, Object> answers = new DefaultIfNullMap<String, Object>("");

    private String title;
    
    private String preamble;
    
    private boolean readOnly;
    
    /**
	 * @return the components
	 */
	public final List<Component> getComponents() {
		return components;
	}

	/**
	 * @param components the components to set
	 */
	public final void setComponents(final List<Component> components) {
		this.components.clear();
		if(components != null){
		    this.components.addAll(components);
		}
	}

	/**
     * Add a page component.
     * 
     * @param pageComponent
     *            The page component to add.
     */
    public final void addComponent(final Component component) {
    	components.add(component);
    }
   
    public final void addField(final Field field) {
    	if(StringUtils.isNotBlank(field.getDefaultValue())) {
    		addFieldWithAnswer(field, field.getDefaultValue());
    	} else {
    		addFieldWithAnswer(field, "");
    	}
    }    
    
    private void addFieldWithAnswer(final Field field, final String answer) {
    	Question question = field.getElement().getQuestion();
    	answers.put(question.getId(), answer);
    }
    
    private void addFieldWithAnswers(final Field field, final String[] answerArray) {
    	Question question = field.getElement().getQuestion();
    	if(answerArray == null || answerArray.length == 0) {
    		addField(field);
    	} else if(answerArray.length == 1) {
    		answers.put(question.getId(), answerArray[0]);
    	} else {
    		answers.put(question.getId(), answerArray);
    	}
    }
    
    public final void processAnswerText(final Field field, final String answerText) {
        Tag tag = field.getElement().getTag();
        if(tag.getTagType() == TagType.MULTISELECT) {
            addFieldWithAnswers(field, StringUtils.split(answerText, ','));
        } else {
            addFieldWithAnswer(field, answerText);
        }
    }

    public final void processAnswer(final Field field, final Answer answer) {
        if(answer == null) {
            addField(field);
        } else {
            processAnswerText(field, answer.getAnswerText());
        }
    }

    /**
     * @return the answers
     */
    public final Map<String, Object> getAnswers() {
        return answers;
    }

    /**
     * @param answers
     *            the answers
     */
    public final void setAnswers(final Map<String, Object> answers) {
        this.answers.clear();
        
        Set<String> questionsThatAreMultiSelect = new HashSet<String>();
        
        //component can't be null
        for (Component comp : components) {
            if (comp != null) {
                Set<Field> fields = comp.getFields();
                if (fields != null) {
                    for (Field field : fields) {
                        if (field != null) {
                            boolean isMultiSelect = field.getElement().getTag().getTagType() == TagType.MULTISELECT;
                            String questionId = field.getElement().getQuestion().getId();
                            if(isMultiSelect){
                                questionsThatAreMultiSelect.add(questionId);
                            }
                        }
                    }
                }
            }
        }
        
        for (Map.Entry<String, Object> entry : answers.entrySet()) {
            String questionId = entry.getKey();
            Object value = entry.getValue();            
            if (value instanceof String[]) {
                if (questionsThatAreMultiSelect.contains(questionId)) {
                    this.answers.put(questionId, value);
                } else {
                    // problem - we have an array for a non-multiselect answer
                    String[] bad = (String[]) value;
                    if (bad.length > 0) {                        
                        this.answers.put(questionId, bad[0]);
                        if(LOG.isInfoEnabled()){
                            List<Object> discard = new ArrayList<Object>();
                            for(int i=1;i<bad.length;i++){
                                discard.add(bad[i]);
                            }
                            String msg = String.format("multiple values for question with id[%s] keeping [%s] ignoring %s",questionId,bad[0],discard);
                            LOG.info(msg);
                        }
                    } else {
                        this.answers.put(questionId, "");
                    }
                }
            } else {
                this.answers.put(questionId, value);
            }
        }
    }

    /**
     * @param element
     *            the element
     * @return the answer for the element
     */
    public final String getAnswersForQuestion(final Field field) {
    	Question question = field.getElement().getQuestion();
    	if(field.getElement().getTag().getTagType() == TagType.MULTISELECT) {
    		return EACStringUtils.safeConvertToDelimitedString(getAnswersForQuestion(question.getId()), ',');
    	}
    	Object ans = answers.get(question.getId());
    	if(ans != null && ans instanceof String == false){
            throw new IllegalStateException("Unexpected answer type : " + ans.getClass());
    	}
        return (String)ans;
    }

    /**
     * @param elementId
     *            the element id
     * @return the answer
     */
    private final String[] getAnswersForQuestion(final String questionId) {
    	Object object = answers.get(questionId);
    	if(object == null) {
    		return null;
    	}
    	if(object instanceof String[]) {
    		return (String[])object;
    	}
    	if(object instanceof String) {
    		return new String[] {(String)object};
    	}
        throw new IllegalStateException("Unknown answer type");
    }

    /**
     * Get the title.
     * 
     * @return The title
     */
    public final String getTitle() {
        return title;
    }

    /**
     * Set the title for this registration.
     * 
     * @param title The title to set.
     */
    public final void setTitle(final String title) {
        this.title = title;
    }

	/**
	 * @return the preamble
	 */
	public final String getPreamble() {
		return preamble;
	}

	/**
	 * @param preamble the preamble to set
	 */
	public final void setPreamble(final String preamble) {
		this.preamble = preamble;
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}
	
	public String getQuestionAnswer(String questionId) {
	    Object object = answers.get(questionId);
	    if(object == null) {
	        return null;
	    }
	    if(object instanceof String[]) {
	        StringBuilder answers = new StringBuilder();
	        String[] answersArray = (String[])object;
	        for(String answer : answersArray) {
	            if(answers.length() > 0) {
	                answers.append(", ");
	            }
	            answers.append(answer);
	        }
	        return answers.toString();
	    }
	    return (String)object;
	}
	
	public static class DefaultIfNullMap<K, V> extends HashMap<K, V> {
		
		private V defaultIfNull;
		
		public DefaultIfNullMap(final V defaultIfNull) {
			this.defaultIfNull = defaultIfNull;
		}

		@Override
		public V get(final Object key) {
			V val = super.get(key);
			if (val == null) {
				val = defaultIfNull;
			}
			return val;
		}
		
	}
	
    public void cleanAnswers() {
        Map<String, Object> ans = new HashMap<String, Object>();
        ans.putAll(this.answers);
        setAnswers(ans);
    }
}
