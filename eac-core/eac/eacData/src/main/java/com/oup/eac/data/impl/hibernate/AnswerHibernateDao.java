package com.oup.eac.data.impl.hibernate;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.oup.eac.data.AnswerDao;
import com.oup.eac.domain.Answer;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.PageDefinition;
import com.oup.eac.domain.PageConstant;
import com.oup.eac.domain.ProductSpecificAnswer;
import com.oup.eac.domain.Question;
import com.oup.eac.domain.RegisterableProduct;

/**
 * The Answer dao hibernate implementation.
 * 
 * @author harlandd
 * @author Ian Packard
 */
@Repository(value="answerDao")
public class AnswerHibernateDao extends HibernateBaseDao<Answer, String> implements AnswerDao {

    /**
     * @param sf
     *            the session factory
     */
	@Autowired
    public AnswerHibernateDao(final SessionFactory sf) {
        super(sf);
    }

	/* Gaurav Soni:
	 * #EAC Performance
	 * Added setCacheable
	 * */
	public final Answer getCustomerAnswerByQuestion(final Question question, final Customer customer) {
		Answer answer = (Answer)getSession().createQuery("select a from Answer a " 
				+ "where a.customerId = :customerId " 
				+ "and a.question = :question")
				.setParameter("customerId", customer.getId())
				.setParameter("question", question)
				.uniqueResult() ;
		if (question.getDescription().equalsIgnoreCase(PageConstant.MARKETING_PREF) && answer != null ) {
			if(StringUtils.isBlank(answer.getAnswerText()) || answer.getAnswerText().equalsIgnoreCase(PageConstant.MARKETING_PREF_OPT_IN)) {
				answer.setAnswerText(Boolean.FALSE.toString().toLowerCase());
			} else {
				answer.setAnswerText(Boolean.TRUE.toString().toLowerCase());
			}
		}
        return answer;
    }    
	
    @SuppressWarnings("unchecked")
    public final List<Answer> getCustomerAnswerByQuestions(final List<Question> questions, final Customer customer) {
        return getSession().createQuery("select a from Answer a " 
                                    + "where a.customerId = :customerId and " 
                                    + "a.question in (:questions) "
                                    + "and a.answerText != ''") 
                                    .setParameter("customerId", customer.getId())
                                    .setParameterList("questions", questions)
                                    .list();
    }   
	
	@SuppressWarnings("unchecked")
	public final List<Answer> getCustomerAnswerByPageDefinition(final PageDefinition pageDefinition, final Customer customer) {
        return getSession().createQuery("select a from Answer a " 
					        		+ "where a.question in " 
					        		+ "(select q from Element e " 
					        		+ "join e.question q " 
									+ "join e.fields f " 
									+ "join f.component c " 
									+ "join c.pageComponents pc " 
									+ "join pc.pageDefinition pd "
									+ "where pd = :pageDefinition) "
									+ "and a.customerId = :customer ")
									.setParameter("customer", customer.getId())
									.setParameter("pageDefinition", pageDefinition)
									.list();
    }   
	
	public final ProductSpecificAnswer getCustomerProductSpecificAnswerByQuestion(final Question question, final Customer customer, 
																				final RegisterableProduct product) {
        ProductSpecificAnswer productSpecificAnswer = (ProductSpecificAnswer)getSession().createQuery("select a from ProductSpecificAnswer a " 
				+ "where a.customerId = :customerId " 
				+ "and a.productId = :productId " 
				+ "and a.question = :question ")
				.setParameter("customerId", customer.getId())
				.setParameter("question", question)
				.setParameter("productId", product.getId())
				.uniqueResult();
		if (question.getDescription().equalsIgnoreCase(PageConstant.MARKETING_PREF) && productSpecificAnswer != null ) {
			if(StringUtils.isBlank(productSpecificAnswer.getAnswerText()) || productSpecificAnswer.getAnswerText().equalsIgnoreCase(PageConstant.MARKETING_PREF_OPT_IN)) {
				productSpecificAnswer.setAnswerText(Boolean.FALSE.toString().toLowerCase());
			} else {
				productSpecificAnswer.setAnswerText(Boolean.TRUE.toString().toLowerCase());
			}
		}
		return productSpecificAnswer ;		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getAnswerDetailsByCustomerAndProduct(
			String customerId, String productId, boolean isCMDP) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT distinct cast(ans.answer_text as varchar(max)),\n"); 
		if(isCMDP){
			sql.append("       CASE\n" + 
					"         WHEN ( en.name IS NOT NULL AND\n" + 
					"                en.export_type = 'CMDP' ) THEN cast(en.name as varchar(max))\n" + 
					"         ELSE cast(que.description as varchar(max))\n" + 
					"       END question_desc,\n"); 
		}
		else{
			sql.append("cast(que.description as varchar(max)) question_desc,\n");
		}
		sql.append("       cast(rd.page_definition_id as varchar(max)) page_definition_id\n"); 
		sql.append("FROM   ANSWER ans\n"); 
		sql.append("       join QUESTION que\n"); 
		sql.append("         ON ans.question_id = que.id\n"); 
		if(isCMDP){
			sql.append("       left join EXPORT_NAME en\n"); 
			sql.append("              ON en.question_id = que.id\n"); 
		}
		sql.append("       join ELEMENT el\n"); 
		sql.append("         ON el.question_id = que.id\n"); 
		sql.append("       join FIELD fi\n"); 
		sql.append("         ON fi.element_id = el.id\n"); 
		sql.append("       join PAGE_COMPONENT pc\n"); 
		sql.append("         ON pc.component_id = fi.component_id\n"); 
		sql.append("       join REGISTRATION_DEFINITION rd\n"); 
		sql.append("         ON rd.page_definition_id = pc.page_definition_id\n"); 
		sql.append("WHERE  ISNULL(ans.registerable_product_id,:productId) = :productId\n ");
		sql.append("     and rd.product_id = :productId \n"); 
		sql.append("       AND ans.customer_id = :customerId 	");
		return getSession().createSQLQuery(sql.toString())
				.setParameter("productId", productId).setParameter("customerId", customerId).list();
	}
    
}
