package com.oup.eac.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.UUID;

import javax.mail.Session;
import javax.naming.NamingException;

import junit.framework.Assert;

import org.apache.commons.io.IOUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.oup.eac.common.utils.email.VelocityUtils;
import com.oup.eac.domain.Answer;
import com.oup.eac.domain.Checkbox;
import com.oup.eac.domain.Component;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.Element;
import com.oup.eac.domain.Field;
import com.oup.eac.domain.Label;
import com.oup.eac.domain.PasswordField;
import com.oup.eac.domain.Question;
import com.oup.eac.domain.Radio;
import com.oup.eac.domain.Select;
import com.oup.eac.domain.Tag;
import com.oup.eac.domain.TextField;
import com.oup.eac.domain.UrlLink;
import com.oup.eac.dto.AccountRegistrationDto;
import com.oup.eac.dto.ProductRegistrationDto;
import com.oup.eac.dto.RegistrationDto;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:/eac/eac*-beans.xml", "classpath*:/eac/test.eac*-beans.xml" })
public class ValidatedRegistrationVelocityTest {

    @Autowired
    @Qualifier("velocityEngine")
    private VelocityEngine ve;

    private Resource expected = new ClassPathResource("com/oup/eac/service/impl/validatedRegistrationExpected.text");

    private ResourceBundle resource = ResourceBundle.getBundle("com.oup.eac.service.impl.validatedRegistrationResources");

    public ValidatedRegistrationVelocityTest() throws NamingException {
        SimpleNamingContextBuilder builder = SimpleNamingContextBuilder.emptyActivatedContextBuilder();
        builder.bind("java:/Mail", Session.getInstance(new Properties()));
    }

    @Test
    public void testValidatedRegistrationTemplate() throws Exception {
        Assert.assertNotNull(resource);

        Assert.assertNotNull(ve);
        Template t = ve.getTemplate("com/oup/eac/service/velocity/validatedRegistration.vm");
        Assert.assertNotNull(t);

        VelocityContext ctx = VelocityUtils.createVelocityContext();

        Customer customer = new Customer();
        customer.setFirstName("John");
        customer.setFamilyName("Smith");
        customer.setUsername("jsmith");
        customer.setEmailAddress("jsmith@mailinator.com");
        customer.setTimeZone("Europe/London");

        ctx.put("resource", resource);
        ctx.put("adminName", "Admin Name");
        ctx.put("customer", customer);
        ctx.put("productname", "ProductXYZ");
        ctx.put("allowlink", "http://allowLink");
        ctx.put("denylink", "http://denyLink");
        ctx.put("locale", "English (United Kingdom)");

        Map<String, String> qaMap1 = new LinkedHashMap<String, String>();
        qaMap1.put("label.q1", "answer 1");
        qaMap1.put("label.q2", "answer 2");
        qaMap1.put("label.q3", "answer 3");

        Map<String, String> qaMap2 = new LinkedHashMap<String, String>();
        qaMap2.put("label.q4", "answer 4");
        qaMap2.put("label.q5", "answer 5");
        qaMap2.put("label.q6", "answer 6");

        RegistrationDto accountRegDto = getRegistrationDto(qaMap1, AccountRegistrationDto.class);
        ctx.put("accountRegDto", accountRegDto);

        RegistrationDto regDto = getRegistrationDto(qaMap2, ProductRegistrationDto.class);
        ctx.put("regDto", regDto);

        StringWriter writer = new StringWriter();
        t.merge(ctx, writer);

        String result = writer.toString();
        
        String expected = getExpected();
        
        /* show the World */
        System.out.println(result);
        
        Assert.assertEquals(expected, result);
    }

    private RegistrationDto getRegistrationDto(Map<String, String> questionLabelsAndAnswers, Class<? extends RegistrationDto> clazz)
            throws InstantiationException, IllegalAccessException {
        RegistrationDto result = clazz.newInstance();
        Component comp = new Component();
        List<Component> comps = Arrays.asList(comp);
        result.setComponents(comps);
        Set<Field> fields = new LinkedHashSet<Field>();
        comp.setFields(fields);
        Map<String, Object> ansMap = new HashMap<String, Object>();
        
        Context context = new Context(ansMap, fields);
        context.addQandAs(questionLabelsAndAnswers);

        context.addLink();
        context.addLabel();
        context.addCheckbox();
        context.addRadio();
        context.addSelect();
        context.addPassword();
        result.setAnswers(ansMap);
        
        return result;
    }

    private static class Context {
        private Map<String, Object> ansMap;
        private Set<Field> fields;

        public Context(Map<String, Object> ansMap, Set<Field> fields) {
            this.ansMap = ansMap;
            this.fields = fields;
        }

        protected void addQandA(String elemText, String titleText, String answerText, Class<? extends Tag> tagClass) {
            try {
            	Question question = new Question();
            	question.setId(UUID.randomUUID().toString());
                Element e = new Element();
                e.setQuestion(question);
                question.setElementText(elemText);
                e.setTitleText(titleText);

                Field f = new Field();
                f.setId(UUID.randomUUID().toString());
                f.setElement(e);

                Set<Tag> fTags = new HashSet<Tag>();

                Tag fTag = tagClass.newInstance();

                fTags.add(fTag);
                e.setTags(fTags);

                fields.add(f);
                if (answerText != null) {
                    Answer ans = new Answer();
                    ans.setAnswerText(answerText);
                    ansMap.put(question.getId(), ans.getAnswerText());
                }
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }

        }

        public void addCheckbox() {
            addQandA("label.cbox", "title text for cbox", "Y", Checkbox.class);
        }

        public void addRadio() {
            addQandA("label.radio", "title text for radio", "No", Radio.class);
        }

        public void addSelect() {
            addQandA("label.select", "title text for select", "DropDownValue", Select.class);
        }

        public void addLink() {
            addQandA("label.link", "title text for link", "http://link", UrlLink.class);
        }

        public void addLabel() {
            addQandA("label.label", "title text for label", null, Label.class);
        }

        public void addPassword() {
            addQandA("label.password", "title text for password", "password", PasswordField.class);
        }

        public void addQandAs(Map<String, String> questionLabelsAndAnswers) {
            for (Map.Entry<String, String> entry : questionLabelsAndAnswers.entrySet()) {
                String questionLabel = entry.getKey();
                String answerText = entry.getValue();
                addQandA(questionLabel, "title text for text", answerText, TextField.class);
            }
        }

    }

    public String getExpected() {
        try {
            InputStream is = this.expected.getInputStream();
            String result = IOUtils.toString(is);
            return result.trim();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
