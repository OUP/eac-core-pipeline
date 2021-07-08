package com.oup.eac.domain;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "PRODUCT_SPECIFIC_ANSWER")
public class ProductSpecificAnswer extends Answer {

	@Column(name ="registerable_product_id")
	private String productId;
	
	public ProductSpecificAnswer() {
		super();
	}

	public ProductSpecificAnswer(Question question, String customerId,String answerText, String productId) {
		super(question, customerId, answerText);
		this.productId = productId;
	}	
	
	public ProductSpecificAnswer(Question question, String customerId,String answerText) {
		super(question, customerId, answerText);
	}

	public ProductSpecificAnswer(Question question, String answerText) {
		super(question, answerText);
	}

	public ProductSpecificAnswer(Question question) {
		super(question);
	}

	/**
	 * @return the registerableProduct
	 */
	public String getProductId() {
		return productId;
	}

	/**
	 * @param registerableProduct the registerableProduct to set
	 */
	public void setProductId(final String productId) {
		this.productId = productId;
	}
	
	@Override
	public AnswerType getAnswerType() {
		return AnswerType.PRODUCT_SPECIFIC_ANSWER;
	}
}
