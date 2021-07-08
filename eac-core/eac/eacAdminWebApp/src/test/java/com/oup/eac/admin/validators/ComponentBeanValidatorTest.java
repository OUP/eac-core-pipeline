package com.oup.eac.admin.validators;

import java.util.ArrayList;
import java.util.Arrays;

import org.easymock.EasyMock;
import org.junit.Test;
import org.springframework.validation.Errors;

import com.oup.eac.admin.beans.ComponentBean;
import com.oup.eac.domain.Component;
import com.oup.eac.domain.Element;
import com.oup.eac.domain.Field;

public class ComponentBeanValidatorTest {

	private ComponentBeanValidator validator = new ComponentBeanValidator();
	
	@Test
	public void shouldRejectWhenNameyEmpty() {
		Component component = new Component();
		component.setName("");
		component.setLabelKey("label");
		ComponentBean componentBean = new ComponentBean(new ArrayList<Component>(), new ArrayList<Element>());
		componentBean.setSelectedComponent(component);
		
		Errors mockErrors = EasyMock.createMock(Errors.class);
		mockErrors.rejectValue("selectedComponent.name", "error.component.name.empty");
		
		EasyMock.replay(mockErrors);
		
		validator.validate(componentBean, mockErrors);
		
		EasyMock.verify(mockErrors);
	}
	
	@Test
	public void shouldRejectWhenNewComponentHasSameNameAsExistingComponent() {
		Component newComponent = new Component();
		newComponent.setName("a_name");
		newComponent.setLabelKey("label");
		Component existingComponent = new Component();
		existingComponent.setName("a_Name");
		existingComponent.setLabelKey("label");
		existingComponent.setId("123545");
		ComponentBean componentBean = new ComponentBean(Arrays.asList(existingComponent), new ArrayList<Element>());
		componentBean.setSelectedComponent(newComponent);
		
		Errors mockErrors = EasyMock.createMock(Errors.class);
		mockErrors.rejectValue("selectedComponent.name", "error.component.name.duplicate");
		
		EasyMock.replay(mockErrors);
		
		validator.validate(componentBean, mockErrors);
		
		EasyMock.verify(mockErrors);
	}
	
	@Test
	public void shouldRejectWhenExistingComponentHasSameNameAsOtherExistingComponent() {
		Component existingComponent1 = new Component();
		existingComponent1.setId("54321");
		existingComponent1.setName("a_name");
		existingComponent1.setLabelKey("label");
		Component existingComponent2 = new Component();
		existingComponent2.setName("a_Name");
		existingComponent2.setLabelKey("label");
		existingComponent2.setId("123545");
		ComponentBean componentBean = new ComponentBean(Arrays.asList(existingComponent1, existingComponent2), new ArrayList<Element>());
		componentBean.setSelectedComponent(existingComponent1);
		
		Errors mockErrors = EasyMock.createMock(Errors.class);
		mockErrors.rejectValue("selectedComponent.name", "error.component.name.duplicate");
		
		EasyMock.replay(mockErrors);
		
		validator.validate(componentBean, mockErrors);
		
		EasyMock.verify(mockErrors);
	}
	
	@Test
	public void shouldRejectWhenLabelKeyEmpty() {
		Component component = new Component();
		component.setName("name");
		component.setLabelKey("");
		ComponentBean componentBean = new ComponentBean(new ArrayList<Component>(), new ArrayList<Element>());
		componentBean.setSelectedComponent(component);
		
		Errors mockErrors = EasyMock.createMock(Errors.class);
		mockErrors.rejectValue("selectedComponent.labelKey", "error.component.title.empty");

		EasyMock.replay(mockErrors);
		
		validator.validate(componentBean, mockErrors);
		
		EasyMock.verify(mockErrors);
	}
	
	@Test
	public void shouldRejectWhenNewFieldReferencingSameElementAsExisting() {
		Component component = new Component();
		component.setName("name");
		component.setLabelKey("title");
		Element element1 = new Element();
		element1.setId("1");
		Element element2 = new Element();
		element2.setId("2");
		Field newField1 = new Field();
		newField1.setElement(element1); // New field referencing element1
		Field newField2 = new Field();
		newField2.setElement(element2);
		Field existingField = new Field();
		existingField.setId("abc");
		existingField.setElement(element1);
		component.addField(existingField); // Existing field referencing element1
		ComponentBean componentBean = new ComponentBean(new ArrayList<Component>(), Arrays.asList(element1, element2));
		componentBean.setSelectedComponent(component);
		componentBean.setNewFields(Arrays.asList(newField1, newField2));
		
		Errors mockErrors = EasyMock.createMock(Errors.class);
		mockErrors.rejectValue("newFields[0].element", "error.component.field.sameElement");

		EasyMock.replay(mockErrors);
		
		validator.validate(componentBean, mockErrors);
		
		EasyMock.verify(mockErrors);
	}
	
	@Test
	public void shouldRejectWhenTwoNewFieldsReferencingSameElement() {
		Component component = new Component();
		component.setName("name");
		component.setLabelKey("title");
		Element element1 = new Element();
		element1.setId("1");
		Element element2 = new Element();
		element2.setId("2");
		Field newField1 = new Field();
		newField1.setElement(element1); // New field referencing element1
		Field newField2 = new Field();
		newField2.setElement(element1); // New field referencing element1
		Field existingField = new Field();
		existingField.setId("abc");
		existingField.setElement(element2);
		component.addField(existingField); // Existing field referencing element1
		ComponentBean componentBean = new ComponentBean(new ArrayList<Component>(), Arrays.asList(element1, element2));
		componentBean.setSelectedComponent(component);
		componentBean.setNewFields(Arrays.asList(newField1, newField2));
		
		Errors mockErrors = EasyMock.createMock(Errors.class);
		mockErrors.rejectValue("newFields[0].element", "error.component.field.sameElement");
		
		EasyMock.replay(mockErrors);
		
		validator.validate(componentBean, mockErrors);
		
		EasyMock.verify(mockErrors);
	}
	
	@Test
	public void shouldRejectWhenExistingFieldReferencingSameElementAsAnotherExisting() {
		Component component = new Component();
		component.setName("name");
		component.setLabelKey("title");
		Element element1 = new Element();
		element1.setId("1");
		Element element2 = new Element();
		element2.setId("2");
		Element element3 = new Element();
		element3.setId("3");
		Field newField1 = new Field();
		newField1.setElement(element1); 
		Field newField2 = new Field();
		newField2.setElement(element2);
		Field existingField1 = new Field();
		existingField1.setId("abc");
		existingField1.setSequence(1);
		existingField1.setElement(element3); // Existing field referencing element3
		Field existingField2 = new Field();
		existingField2.setId("xyz");
		existingField2.setSequence(2);
		existingField2.setElement(element3); // Existing field referencing element3
		component.addField(existingField1); 
		component.addField(existingField2); 
		ComponentBean componentBean = new ComponentBean(new ArrayList<Component>(), Arrays.asList(element1, element2));
		componentBean.setSelectedComponent(component);
		componentBean.setNewFields(Arrays.asList(newField1, newField2));
		
		Errors mockErrors = EasyMock.createMock(Errors.class);
		mockErrors.rejectValue("selectedComponent.fields[0].element", "error.component.field.sameElement");
		
		EasyMock.replay(mockErrors);
		
		validator.validate(componentBean, mockErrors);
		
		EasyMock.verify(mockErrors);
	}
}
