package com.oup.eac.data;

import java.util.List;

import com.oup.eac.domain.Answer;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.PageDefinition;
import com.oup.eac.domain.ProductSpecificAnswer;
import com.oup.eac.domain.Question;
import com.oup.eac.domain.RegisterableProduct;

/**
 * @author harlandd Answer dao interface
 */
public interface AnswerDao extends BaseDao<Answer, String> {
    
    List<Answer> getCustomerAnswerByQuestions(List<Question> questions, Customer customer);
	
	List<Answer> getCustomerAnswerByPageDefinition(PageDefinition pageDefinition, Customer customer);
	
	Answer getCustomerAnswerByQuestion(Question question, Customer customer);
	
	ProductSpecificAnswer getCustomerProductSpecificAnswerByQuestion(Question question, Customer customer, RegisterableProduct product);
	
	List<Object[]> getAnswerDetailsByCustomerAndProduct(String customerId, String productId, boolean isCMDP);
}
