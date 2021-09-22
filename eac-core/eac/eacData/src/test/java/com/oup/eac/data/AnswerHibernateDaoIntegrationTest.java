package com.oup.eac.data;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.oup.eac.data.util.SampleDataFactory;
import com.oup.eac.domain.Answer;
import com.oup.eac.domain.Component;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.Element;
import com.oup.eac.domain.InstantRegistrationActivation;
import com.oup.eac.domain.ProductPageDefinition;
import com.oup.eac.domain.Question;
import com.oup.eac.domain.RegisterableProduct;
import com.oup.eac.domain.RegisterableProduct.RegisterableType;
import com.oup.eac.domain.StandardLicenceTemplate;

/**
 * @author harlandd User dao hibernate test
 */
public class AnswerHibernateDaoIntegrationTest extends AbstractDBTest {

    @Autowired
    private AnswerDao answerDao;

    private Answer answer1;

    private Element element;
    
    private Question question, question2, question3;

    private Customer customer;
    
    private ProductPageDefinition productPageDefinition;

    /**
     * @throws Exception
     *             Sets up data ready for test.
     */
    @Before
    public final void setUp() throws Exception {
        StandardLicenceTemplate standardLicenceTemplate = getSampleDataCreator().createStandardLicenceTemplate();
        RegisterableProduct product = getSampleDataCreator().createRegisterableProduct(1,"MALAYSIA",RegisterableType.SELF_REGISTERABLE);
        InstantRegistrationActivation licenceActivation = getSampleDataCreator().createInstantRegistrationActivation();
        productPageDefinition = getSampleDataCreator().createProductPageDefinition();
        getSampleDataCreator().createProductRegistrationDefinition(product, standardLicenceTemplate, licenceActivation, productPageDefinition);
        Component component = getSampleDataCreator().createComponent("label.marketinginformation");
        getSampleDataCreator().createPageComponent(productPageDefinition, component, 1);
        question = getSampleDataCreator().createQuestion();
        element = getSampleDataCreator().createElement(question);
        question2 = getSampleDataCreator().createQuestion();
        Element element2 = getSampleDataCreator().createElement(question2);
        getSampleDataCreator().createField(component, element, 1);
        getSampleDataCreator().createField(component, element2, 2);
        getSampleDataCreator().createTextField(element);
        getSampleDataCreator().createTextField(element2);
        customer = getSampleDataCreator().createCustomerForAnswer();
        answer1 = getSampleDataCreator().createAnswer(question, customer);
        getSampleDataCreator().createAnswer(question2, customer);
        question3 = getSampleDataCreator().createQuestion();
        Answer answer = SampleDataFactory.createAnswer(customer, question3);
        answer.setAnswerText("");
        getSampleDataCreator().loadAnswer(answer);
        loadAllDataSets();
    }

    /**
     * Test getting an answer and comparing it to the expected answer.
     * 
     * @throws Exception
     *             the exception
     */
    @Test
    public final void testGetAnswer() throws Exception {
        Answer answer = answerDao.getEntity(answer1.getId());
        assertEquals(answer1.getAnswerText(), answer.getAnswerText());
    }

    /**
     * Test saving an answer and checking the number of rows in the table afterwards.
     * 
     * @throws Exception
     *             the exception
     */
    @Test
    public final void testSaveAnswer() throws Exception {
        assertEquals(3, countRowsInTable(Answer.class.getSimpleName()));

        Answer answer = new Answer(question2);
        answer.setAnswerText("answerText");
        answer.setCreatedDate(new DateTime());
        answer.setCustomerId(customer.getId());

        answerDao.save(answer);
        answerDao.flush();
        answerDao.clear();

        assertEquals(4, countRowsInTable(Answer.class.getSimpleName()));
    }

    /**
     * Test updating an existing answer.
     * 
     * @throws Exception
     *             the exception
     */
    @Test
    public final void testUpdateAnswer() throws Exception {
        Answer answer = answerDao.getEntity(answer1.getId());
        answer.setAnswerText("some other answer");
        answerDao.update(answer);

        answer = answerDao.getEntity(answer1.getId());
        assertEquals("some other answer", answer.getAnswerText());
    }
    
    /**
     * Test updating an existing answer.
     * 
     * @throws Exception
     *             the exception
     */
    @Test
    public final void testGetAnswersByPageDefinitionAndCustomer() throws Exception {
        List<Answer> answers = answerDao.getCustomerAnswerByPageDefinition(productPageDefinition, customer);
        Assert.assertNotNull(answers);
        assertEquals(2, answers.size());
    }
    
    @Test
    public final void testGetAnswersByQuestions1() throws Exception {
        List<Question> questions = new ArrayList<Question>();
        questions.add(question);
        List<Answer> answers = answerDao.getCustomerAnswerByQuestions(questions, customer);
        Assert.assertNotNull(answers);
        assertEquals(1, answers.size());
    }
    
    @Test
    public final void testGetAnswersByQuestions2() throws Exception {
        List<Question> questions = new ArrayList<Question>();
        questions.add(question);
        questions.add(question2);
        List<Answer> answers = answerDao.getCustomerAnswerByQuestions(questions, customer);
        Assert.assertNotNull(answers);
        assertEquals(2, answers.size());
    }
    
    @Test
    public final void testGetAnswersByQuestions3() throws Exception {
        List<Question> questions = new ArrayList<Question>();
        questions.add(question);
        questions.add(question2);
        questions.add(question3);
        List<Answer> answers = answerDao.getCustomerAnswerByQuestions(questions, customer);
        Assert.assertNotNull(answers);
        assertEquals(2, answers.size());
    }

}
