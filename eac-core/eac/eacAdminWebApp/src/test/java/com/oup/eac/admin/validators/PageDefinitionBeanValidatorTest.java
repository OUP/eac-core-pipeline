package com.oup.eac.admin.validators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import org.easymock.EasyMock;
import org.junit.Test;
import org.springframework.validation.Errors;

import com.oup.eac.admin.beans.PageDefinitionBean;
import com.oup.eac.domain.Component;
import com.oup.eac.domain.Division;
import com.oup.eac.domain.PageComponent;
import com.oup.eac.domain.PageDefinition;
import com.oup.eac.domain.ProductPageDefinition;

public class PageDefinitionBeanValidatorTest {
	
	private final PageDefinitionBeanValidator validator = new PageDefinitionBeanValidator();

	@Test
	public void shouldRejectWhenNameIsEmpty() {
		ProductPageDefinition pageDef = new ProductPageDefinition();
		pageDef.setName("");
		pageDef.setTitle("title");
		PageDefinitionBean pageDefBean = new PageDefinitionBean(new ArrayList<PageDefinition>(), new ArrayList<Component>(), new ArrayList<Division>());
		pageDefBean.setSelectedPageDefinition(pageDef);
		
		Errors mockErrors = EasyMock.createMock(Errors.class);
		mockErrors.rejectValue("selectedPageDefinition.name", "error.pageDefinition.name.empty");

		EasyMock.replay(mockErrors);
		
		validator.validate(pageDefBean, mockErrors);
		
		EasyMock.verify(mockErrors);
	}
	
	@Test
	public void shouldRejectWhenNewPageComponentReferencesSameComponentAsExistingPageComponent() {
		ProductPageDefinition pageDef = new ProductPageDefinition();
		pageDef.setName("name");
		pageDef.setTitle("title");
		Component component = new Component();
		component.setId("123");
		PageComponent existingPageComponent = new PageComponent();
		existingPageComponent.setComponent(component);
		existingPageComponent.setPageDefinition(pageDef);
		pageDef.setPageComponents(new HashSet<PageComponent>(Arrays.asList(existingPageComponent)));
		PageDefinitionBean pageDefBean = new PageDefinitionBean(new ArrayList<PageDefinition>(), new ArrayList<Component>(), new ArrayList<Division>());
		pageDefBean.setSelectedPageDefinition(pageDef);
		
		PageComponent newPageComponent = new PageComponent();
		newPageComponent.setComponent(component);
		
		pageDefBean.setNewComponents(Arrays.asList(newPageComponent));
		
		Errors mockErrors = EasyMock.createMock(Errors.class);
		mockErrors.rejectValue("newComponents[0]", "error.pageDefinition.duplicateComponent");

		EasyMock.replay(mockErrors);
		
		validator.validate(pageDefBean, mockErrors);
		
		EasyMock.verify(mockErrors);
	}
	
	@Test
	public void shouldRejectWhenTwoNewPageComponentsReferenceTheSameComponent() {
		ProductPageDefinition pageDef = new ProductPageDefinition();
		pageDef.setName("name");
		pageDef.setTitle("title");
		Component component = new Component();
		component.setId("123");
		PageDefinitionBean pageDefBean = new PageDefinitionBean(new ArrayList<PageDefinition>(), new ArrayList<Component>(), new ArrayList<Division>());
		pageDefBean.setSelectedPageDefinition(pageDef);
		
		PageComponent newPageComponent1 = new PageComponent();
		newPageComponent1.setComponent(component);
		
		PageComponent newPageComponent2 = new PageComponent();
		newPageComponent2.setComponent(component);
		
		
		pageDefBean.setNewComponents(Arrays.asList(newPageComponent1, newPageComponent2));
		
		Errors mockErrors = EasyMock.createMock(Errors.class);
		mockErrors.rejectValue("newComponents[0]", "error.pageDefinition.duplicateComponent");
		
		EasyMock.replay(mockErrors);
		
		validator.validate(pageDefBean, mockErrors);
		
		EasyMock.verify(mockErrors);
	}
	
	@Test
	public void shouldRejectWhenTwoExistingPageComponentsReferenceTheSameComponent() {
		ProductPageDefinition pageDef = new ProductPageDefinition();
		pageDef.setName("name");
		pageDef.setTitle("title");
		Component component = new Component();
		component.setId("123");
		PageComponent existingPageComponent1 = new PageComponent();
		existingPageComponent1.setComponent(component);
		existingPageComponent1.setPageDefinition(pageDef);
		PageComponent existingPageComponent2 = new PageComponent();
		existingPageComponent2.setComponent(component);
		existingPageComponent2.setPageDefinition(pageDef);

		pageDef.setPageComponents(new HashSet<PageComponent>(Arrays.asList(existingPageComponent1, existingPageComponent2)));
		PageDefinitionBean pageDefBean = new PageDefinitionBean(new ArrayList<PageDefinition>(), new ArrayList<Component>(), new ArrayList<Division>());
		pageDefBean.setSelectedPageDefinition(pageDef);
		
		Errors mockErrors = EasyMock.createMock(Errors.class);
		mockErrors.rejectValue("selectedPageDefinition.pageComponents[0]", "error.pageDefinition.duplicateComponent");

		EasyMock.replay(mockErrors);
		
		validator.validate(pageDefBean, mockErrors);
		
		EasyMock.verify(mockErrors);
	}
	
	@Test
	public void shouldNotErrorWhenTwoExistingPageComponentsReferenceNothing() {
		ProductPageDefinition pageDef = new ProductPageDefinition();
		pageDef.setName("name");
		pageDef.setTitle("title");
		Component component = null;
		PageComponent existingPageComponent1 = new PageComponent();
		existingPageComponent1.setComponent(component);
		existingPageComponent1.setPageDefinition(pageDef);
		PageComponent existingPageComponent2 = new PageComponent();
		existingPageComponent2.setComponent(component);
		existingPageComponent2.setPageDefinition(pageDef);
		
		pageDef.setPageComponents(new HashSet<PageComponent>(Arrays.asList(existingPageComponent1, existingPageComponent2)));
		PageDefinitionBean pageDefBean = new PageDefinitionBean(new ArrayList<PageDefinition>(), new ArrayList<Component>(), new ArrayList<Division>());
		pageDefBean.setSelectedPageDefinition(pageDef);
		
		Errors mockErrors = EasyMock.createMock(Errors.class);
		
		EasyMock.replay(mockErrors);
		
		validator.validate(pageDefBean, mockErrors);
		
		EasyMock.verify(mockErrors);
	}
	
	@Test
	public void shouldRejectWhenNewPageHasSameNameAsExistingPage() {
		ProductPageDefinition pageDef = new ProductPageDefinition();
		pageDef.setName("existing_name"); // Set to same name as existing
		pageDef.setTitle("title");
		Component component = new Component();
		component.setId("123");
		PageComponent pageComponent = new PageComponent();
		pageComponent.setComponent(component);
		pageComponent.setPageDefinition(pageDef);
		
		ProductPageDefinition existingPageDef = new ProductPageDefinition();
		existingPageDef.setName("Existing_Name");
		existingPageDef.setId("12345");

		pageDef.setPageComponents(new HashSet<PageComponent>(Arrays.asList(pageComponent)));
		PageDefinitionBean pageDefBean = new PageDefinitionBean(Arrays.<PageDefinition>asList(existingPageDef), new ArrayList<Component>(), new ArrayList<Division>());
		pageDefBean.setSelectedPageDefinition(pageDef);
		
		Errors mockErrors = EasyMock.createMock(Errors.class);
		mockErrors.rejectValue("selectedPageDefinition.name", "error.pageDefinition.name.duplicate");

		EasyMock.replay(mockErrors);
		
		validator.validate(pageDefBean, mockErrors);
		
		EasyMock.verify(mockErrors);
	}
	
	@Test
	public void shouldRejectWhenExistingPageHasSameNameAsOtherExistingPage() {
		ProductPageDefinition existingPageDef = new ProductPageDefinition();
		existingPageDef.setName("existing_name"); // Set to same name as existing
		existingPageDef.setTitle("title");
		existingPageDef.setId("54321");
		Component component = new Component();
		component.setId("123");
		PageComponent pageComponent = new PageComponent();
		pageComponent.setComponent(component);
		pageComponent.setPageDefinition(existingPageDef);
		
		ProductPageDefinition otherExistingPageDef = new ProductPageDefinition();
		otherExistingPageDef.setName("Existing_Name");
		otherExistingPageDef.setId("12345");
		
		existingPageDef.setPageComponents(new HashSet<PageComponent>(Arrays.asList(pageComponent)));
		PageDefinitionBean pageDefBean = new PageDefinitionBean(Arrays.<PageDefinition>asList(existingPageDef, otherExistingPageDef), new ArrayList<Component>(), new ArrayList<Division>());
		pageDefBean.setSelectedPageDefinition(existingPageDef);
		
		Errors mockErrors = EasyMock.createMock(Errors.class);
		mockErrors.rejectValue("selectedPageDefinition.name", "error.pageDefinition.name.duplicate");
		
		EasyMock.replay(mockErrors);
		
		validator.validate(pageDefBean, mockErrors);
		
		EasyMock.verify(mockErrors);
	}
}
