package com.oup.eac.dto;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;

import com.oup.eac.domain.Answer;
import com.oup.eac.domain.Checkbox;
import com.oup.eac.domain.Component;
import com.oup.eac.domain.Element;
import com.oup.eac.domain.Field;
import com.oup.eac.domain.MultiSelect;
import com.oup.eac.domain.PasswordField;
import com.oup.eac.domain.Question;
import com.oup.eac.domain.Radio;
import com.oup.eac.domain.Select;
import com.oup.eac.domain.Tag;
import com.oup.eac.domain.TextField;
import com.oup.eac.domain.UrlLink;

/**
 * @author David Hay
 */
public class RegistrationDtoTest {

    private int counter = 0;
    
    /**
     * Test add component.
     */
    @Test
    public final void testAddComponents() {
        RegistrationDto dto = new ProductRegistrationDto();
        Component component1 = new Component();
        Component component2 = new Component();
        Component component3 = new Component();
        Component component4 = new Component();
        // add components
        dto.addComponent(component1);
        dto.addComponent(component2);
        checkComponents(dto, component1, component2);

        // setComponents
        dto.setComponents(Arrays.asList(component3, component4));
        checkComponents(dto, component3, component4);

        // add component
        dto.addComponent(component1);
        checkComponents(dto, component3, component4, component1);

    }

    /**
     * Test add component.
     */
    @Test
    public final void testAddField() {
        RegistrationDto dto = new ProductRegistrationDto();

        Field f1 = createField(new TextField(), "Q1", null);
        Field f2 = createField(new TextField(), "Q2", "   ");
        Field f3 = createField(new TextField(), "Q3", "Q3_DEFAULT");
        Field f4 = createField(new MultiSelect(), "Q4", null);

        dto.addField(f1);
        dto.addField(f2);
        dto.addField(f3);
        dto.addField(f4);

        Assert.assertEquals("", dto.getAnswersForQuestion(f1));
        Assert.assertEquals("", dto.getAnswersForQuestion(f2));
        Assert.assertEquals("Q3_DEFAULT", dto.getAnswersForQuestion(f3));
        Assert.assertEquals("", dto.getAnswersForQuestion(f4));

        Map<String, Object> answers = dto.getAnswers();
        Assert.assertEquals(4, answers.size());
        Assert.assertEquals("", answers.get("Q1"));
        Assert.assertEquals("", answers.get("Q2"));
        Assert.assertEquals("Q3_DEFAULT", answers.get("Q3"));
        Assert.assertEquals("", answers.get("Q1"));

    }

    /**
     * Creates the field.
     * 
     * @param tag
     *            the tag
     * @param questionId
     *            the question id
     * @param defaultValue
     *            the default value
     * @return the field
     */
    private Field createField(final Tag tag, final String questionId, final String defaultValue) {
        Field f = new Field();
        f.setId(UUID.randomUUID().toString());
        f.setSequence(counter++);
        Element elem = new Element();
        f.setElement(elem);
        Question question3 = new Question();
        question3.setId(questionId);
        elem.setQuestion(question3);
        f.setElement(elem);
        elem.addTag(tag);
        f.setDefaultValue(defaultValue);
        return f;
    }

    /**
     * Check components.
     * 
     * @param dto
     *            the dto
     * @param expectedComps
     *            the expected comps
     */
    private void checkComponents(final RegistrationDto dto, final Component... expectedComps) {
        int expectedSize = expectedComps.length;
        List<Component> actual = dto.getComponents();
        Assert.assertEquals(expectedSize, actual.size());
        for (Component expectedComp : expectedComps) {
            Assert.assertTrue(actual.contains(expectedComp));
        }
    }

    @Test
    public void testSimpleProperties() {
        RegistrationDto dto = new ProductRegistrationDto();
        dto.setPreamble("PREAMBLE");
        dto.setTitle("TITLE");
        Assert.assertEquals("TITLE", dto.getTitle());
        Assert.assertEquals("PREAMBLE", dto.getPreamble());
    }

    @Test
    public void testProcessNullAnswer() {
        RegistrationDto dto = new ProductRegistrationDto();
        Field f1 = createField(new TextField(), "Q1", "Q1_DEFAULT");
        dto.processAnswer(f1, null);
        Assert.assertEquals("Q1_DEFAULT", dto.getAnswersForQuestion(f1));
    }

    private void checkSimpleAnswer(Tag tag) {
        RegistrationDto dto = new ProductRegistrationDto();
        Field f1 = createField(tag, "Q1", "Q1_DEFAULT");
        Answer ans = new Answer(f1.getElement().getQuestion(), "ANSWER");
        dto.processAnswer(f1, ans);
        Assert.assertEquals("ANSWER", dto.getAnswersForQuestion(f1));
    }

    @Test
    public void testProcessSimpleAnswers() {
        checkSimpleAnswer(new TextField());
        checkSimpleAnswer(new Radio());
        checkSimpleAnswer(new Select());
        checkSimpleAnswer(new Checkbox());
        checkSimpleAnswer(new UrlLink());
        checkSimpleAnswer(new PasswordField());
    }

    @Test
    public void testMultiSelectAnswer() {
        RegistrationDto dto = new ProductRegistrationDto();
        Field f1 = createField(new MultiSelect(), "Q1", "Q1_DEFAULT");
        Answer ans = new Answer(f1.getElement().getQuestion(), "ANS1,ANS2,ANS3");
        dto.processAnswer(f1, ans);
        Assert.assertEquals("ANS1,ANS2,ANS3", dto.getAnswersForQuestion(f1));

        Field f2 = createField(new MultiSelect(), "Q2", "Q2_DEFAULT");
        Answer ans2 = new Answer(f2.getElement().getQuestion(), "ANSA");
        dto.processAnswer(f2, ans2);
        Assert.assertEquals("ANSA", dto.getAnswersForQuestion(f2));

        Field f3 = createField(new MultiSelect(), "Q3", "Q3_DEFAULT");
        Answer ans3 = new Answer(f3.getElement().getQuestion(), "ANS1,ANS2,");
        dto.processAnswer(f3, ans3);
        Assert.assertEquals("ANS1,ANS2", dto.getAnswersForQuestion(f3));

        Field f4 = createField(new MultiSelect(), "Q4", "Q4_DEFAULT");
        Answer ans4 = new Answer(f4.getElement().getQuestion(), "");
        dto.processAnswer(f4, ans4);
        Assert.assertEquals("Q4_DEFAULT", dto.getAnswersForQuestion(f4));

    }

    @Test
    public void testSetAnswers() {
        RegistrationDto dto = new ProductRegistrationDto();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("Q1", "Q1.ANSWER");
        map.put("Q2", new Integer(123));
        Field f1 = createField(new TextField(), "Q1", "Q1_DEFAULT");
        Field f2 = createField(new TextField(), "Q2", "Q2_DEFAULT");
        dto.setAnswers(map);

        Assert.assertEquals("Q1.ANSWER", dto.getAnswersForQuestion(f1));
        try {
            dto.getAnswersForQuestion(f2);
            Assert.fail("Exception expected");
        } catch (Exception ex) {            
            Assert.assertEquals(IllegalStateException.class, ex.getClass());
            Assert.assertEquals("Unexpected answer type : class java.lang.Integer", ex.getMessage());
        }
    }
    
    @Test
    public void testSetAnswers2() {
        RegistrationDto dto = new ProductRegistrationDto();
        Field f1 = createField(new TextField(),   "Q1", "Q1_DEFAULT");
        Field f2 = createField(new MultiSelect(), "Q2", "Q2_DEFAULT");
        Field f3 = createField(new TextField(),   "Q3", "Q3_DEFAULT");
        
        Field f4 = createField(new TextField(),   "Q4", "Q4_DEFAULT");
        Field f5 = createField(new MultiSelect(), "Q5", "Q5_DEFAULT");
        Field f6 = createField(new TextField(),   "Q6", "Q6_DEFAULT");
        
        Component comp1 = new Component();
        comp1.addField(f1);
        comp1.addField(f2);
        comp1.addField(f3);
        
        Component comp2 = new Component();
        comp2.addField(f4);
        comp2.addField(f5);
        comp2.addField(f6);
        
        List<Component> components = Arrays.asList(comp1, comp2);
        dto.setComponents(components);
        
        Map<String,Object> data = new HashMap<String,Object>();
        data.put("Q1",new String[]{"answer1a","answer1b"});//nope
        data.put("Q2",new String[]{"answer2a","answer2b"});//yep
        data.put("Q3","answer3a");//yep
    
        data.put("Q4",new String[]{"answer4a","answer4b"});//nope
        data.put("Q5",new String[]{"answer5a","answer5b"});//yep
        data.put("Q6","answer6a");//yep
        
        dto.setAnswers(data);
        
        Assert.assertEquals("answer1a",dto.getAnswersForQuestion(f1));
        Assert.assertEquals("answer2a,answer2b",dto.getAnswersForQuestion(f2));
        Assert.assertEquals("answer3a",dto.getAnswersForQuestion(f3));
        
        Assert.assertEquals("answer4a",dto.getAnswersForQuestion(f4));
        Assert.assertEquals("answer5a,answer5b",dto.getAnswersForQuestion(f5));
        Assert.assertEquals("answer6a",dto.getAnswersForQuestion(f6));
    }

    @Test
    public void testSetAnswers3() {
        RegistrationDto dto = new ProductRegistrationDto();
        Field f1 = createField(new TextField(),   "Q1", "Q1_DEFAULT");
        Field f2 = createField(new MultiSelect(), "Q2", "Q2_DEFAULT");
        Field f3 = createField(new TextField(),   "Q3", "Q3_DEFAULT");
        
        Component comp1 = new Component();
        comp1.addField(f1);
        comp1.addField(f2);
        comp1.addField(f3);
        
        Component comp2 = new Component();
        comp2.setFields(null);
        
        List<Component> components = Arrays.asList(comp1, comp2);
        dto.setComponents(components);
        
        Map<String,Object> data = new HashMap<String,Object>();
        data.put("Q1",new String[]{"answer1a","answer1b"});//nope
        data.put("Q2",new String[]{"answer2a","answer2b"});//yep
        data.put("Q3","answer3a");//yep
    
        dto.setAnswers(data);
        
        Assert.assertEquals("answer1a",dto.getAnswersForQuestion(f1));
        Assert.assertEquals("answer2a,answer2b",dto.getAnswersForQuestion(f2));
        Assert.assertEquals("answer3a",dto.getAnswersForQuestion(f3));
        
    }
    
    @Test
    public void testSetAnswers4() {
        RegistrationDto dto = new ProductRegistrationDto();
        Field f1 = createField(new TextField(),   "Q1", "Q1_DEFAULT");
        Field f2 = createField(new MultiSelect(), "Q2", "Q2_DEFAULT");
        Field f3 = createField(new TextField(),   "Q3", "Q3_DEFAULT");
        
        Component comp1 = new Component();
        comp1.addField(f1);
        comp1.addField(f2);
        comp1.addField(f3);
        
        Component comp2 = null;
        
        List<Component> components = Arrays.asList(comp1, comp2);
        dto.setComponents(components);
        
        Map<String,Object> data = new HashMap<String,Object>();
        data.put("Q1",new String[]{"answer1a","answer1b"});//nope
        data.put("Q2",new String[]{"answer2a","answer2b"});//yep
        data.put("Q3","answer3a");//yep
    
        dto.setAnswers(data);
        
        Assert.assertEquals("answer1a",dto.getAnswersForQuestion(f1));
        Assert.assertEquals("answer2a,answer2b",dto.getAnswersForQuestion(f2));
        Assert.assertEquals("answer3a",dto.getAnswersForQuestion(f3));
        
    }

    @Test
    public void testSetAnswers5() {
        RegistrationDto dto = new ProductRegistrationDto();
        
        Field f1 = createField(new TextField(),   "Q1", "Q1_DEFAULT");
        Field f2 = createField(new MultiSelect(), "Q2", "Q2_DEFAULT");
        Field f3 = createField(new TextField(),   "Q3", "Q3_DEFAULT");
        
        dto.setComponents(null);
        
        Map<String,Object> data = new HashMap<String,Object>();
        data.put("Q1",new String[]{"answer1a","answer1b"});
        data.put("Q2",new String[]{"answer2a","answer2b"});
        data.put("Q3","answer3a");
    
        dto.setAnswers(data);
        
        Assert.assertEquals("answer1a",dto.getAnswersForQuestion(f1));
        Assert.assertEquals("answer2a",dto.getAnswersForQuestion(f2));
        Assert.assertEquals("answer3a",dto.getAnswersForQuestion(f3));
        
    }
    
    @Test
    public void testSetAnswers6() {
        RegistrationDto dto = new ProductRegistrationDto();
        
        Field f1 = createField(new TextField(),   "Q1", "Q1_DEFAULT");
        Field f2 = createField(new MultiSelect(), "Q2", "Q2_DEFAULT");
        Field f3 = createField(new TextField(),   "Q3", "Q3_DEFAULT");
        
        Component comp1 = new Component();
        Set<Field> fields = new HashSet<Field>();
        fields.add(f1);
        f1.setComponent(comp1);
        
        fields.add(f2);
        f2.setComponent(comp1);
        
        fields.add(f3);
        f3.setComponent(comp1);
        
        fields.add(null);
        
        comp1.setFields(fields);
        List<Component> comps = Arrays.asList(comp1);
        dto.setComponents(comps);
        
        Map<String,Object> data = new HashMap<String,Object>();
        data.put("Q1",new String[]{"answer1a","answer1b"});
        data.put("Q2",new String[]{"answer2a","answer2b"});
        data.put("Q3","answer3a");
        data.put("XX",new String[0]);
    
        dto.setAnswers(data);
        
        Assert.assertEquals("answer1a",dto.getAnswersForQuestion(f1));
        Assert.assertEquals("answer2a,answer2b",dto.getAnswersForQuestion(f2));
        Assert.assertEquals("answer3a",dto.getAnswersForQuestion(f3));
        
    }
}
