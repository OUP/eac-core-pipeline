package com.oup.eac.domain;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Index;

/**
 * @author harlandd Class representing Answer to question
 */
@Entity
@org.hibernate.annotations.Table(
        indexes = { 
                @Index(name = "answer_question_idx", columnNames = "question_id") },
        appliesTo = "answer")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "ANSWER_TYPE", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue(value = "ANSWER")
public class Answer extends CreatedAudit {

	public static enum AnswerType {
		ANSWER,
		PRODUCT_SPECIFIC_ANSWER;
	}
	
	
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    @Index (name = "answer_question_idx")
    private Question question;
    
    /*@Index (name = "answer_customer_question_idx")*/
    @Column(name = "customer_id")
    private String customerId;
	 
    private String answerText;
    
    public AnswerType getAnswerType() {
    	return AnswerType.ANSWER;
    }

    /**
     * Default constructor.
     */
    public Answer() {
        super();
    }

    /**
     * @param element
     *            supplied element
     */
    public Answer(final Question question) {
        super();
        this.question = question;
    }

    /**
     * @param question
     * @param answerText
     */
    public Answer(final Question question, final String answerText) {
		super();
		this.question = question;
		this.answerText = answerText;
	}

    /**
	 * @param question
	 * @param customer
	 * @param answerText
	 */
	public Answer(final Question question, final String customerId, final String answerText) {
		super();
		this.question = question ;
		this.customerId = customerId ;
		if (question.getDescription().equalsIgnoreCase(PageConstant.MARKETING_PREF)) {
			if(StringUtils.isBlank(answerText) || answerText.equalsIgnoreCase(Boolean.FALSE.toString())) {
				this.answerText = PageConstant.MARKETING_PREF_OPT_IN;
			} else {
				this.answerText = PageConstant.MARKETING_PREF_OPT_OUT;
			}
		} else {
			this.answerText = answerText;
		}
		
	}

	/**
     * @return the text
     */
	public String getAnswerText() {
        return answerText;
    }

    /**
     * @param answerText
     *            answer
     */
	public void setAnswerText(final String answerText) {
        this.answerText = answerText;
    }

    /**
	 * @return the question
	 */
	public Question getQuestion() {
		return question;
	}

	/**
	 * @param question the question to set
	 */
	public void setQuestion(Question question) {
		this.question = question;
	}

    /**
	 * @return the customerId
	 */
	public String getCustomerId() {
		return customerId;
	}

	/**
	 * @param customerId the customerId to set
	 */
	public void setCustomerId(String customer_id) {
		this.customerId = customer_id;
	}

	public boolean isProductSpecfic() {
    	return getAnswerType() == AnswerType.PRODUCT_SPECIFIC_ANSWER;
    }
    
    public boolean isNotProductSpecfic() {
    	return !isProductSpecfic();
    }
}
