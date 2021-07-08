package com.oup.eac.admin.validators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import org.easymock.EasyMock;
import org.junit.Test;
import org.springframework.validation.Errors;

import com.oup.eac.admin.beans.ElementBean;
import com.oup.eac.admin.beans.ElementBean.LocaleBean;
import com.oup.eac.domain.Element;
import com.oup.eac.domain.Question;
import com.oup.eac.domain.Select;
import com.oup.eac.domain.Tag;
import com.oup.eac.domain.Tag.TagType;
import com.oup.eac.domain.TagOption;
import com.oup.eac.domain.UrlLink;

public class ElementBeanValidatorTest {
	
	private ElementBeanValidator validator = new ElementBeanValidator();

	@Test
	public void shouldRejectWhenNameEmpty() {
		Element element = new Element();
		element.setName("");
		element.setTitleText("title");
		ElementBean elementBean = new ElementBean(new ArrayList<Element>(), new ArrayList<Question>());
		elementBean.setSelectedElement(element);
		
		Errors mockErrors = EasyMock.createMock(Errors.class);
		mockErrors.rejectValue("selectedElement.name", "error.element.name.empty");
		
		EasyMock.replay(mockErrors);
		
		validator.validate(elementBean, mockErrors);
		
		EasyMock.verify(mockErrors);
	}
	
	@Test
	public void shouldRejectWhenTitleEmpty() {
		Element element = new Element();
		element.setName("name");
		element.setTitleText("");
		ElementBean elementBean = new ElementBean(new ArrayList<Element>(), new ArrayList<Question>());
		elementBean.setSelectedElement(element);
		
		Errors mockErrors = EasyMock.createMock(Errors.class);
		mockErrors.rejectValue("selectedElement.titleText", "error.element.title.empty");

		EasyMock.replay(mockErrors);
		
		validator.validate(elementBean, mockErrors);
		
		EasyMock.verify(mockErrors);
	}
	
	@Test
	public void shouldRejectWhenInvalidRegex() {
		Element element = new Element();
		element.setName("name");
		element.setTitleText("title");
		element.setRegularExpression("(.*");
		ElementBean elementBean = new ElementBean(new ArrayList<Element>(), new ArrayList<Question>());
		elementBean.setSelectedElement(element);
		
		Errors mockErrors = EasyMock.createMock(Errors.class);
		mockErrors.rejectValue("selectedElement.regularExpression", "error.element.validation.invalid");

		EasyMock.replay(mockErrors);
		
		validator.validate(elementBean, mockErrors);
		
		EasyMock.verify(mockErrors);
	}
	
	@Test
	public void shouldRejectWhenNewLanguageNotValidISOCode() {
		Element element = new Element();
		element.setName("name");
		element.setTitleText("title");
		ElementBean elementBean = new ElementBean(new ArrayList<Element>(), new ArrayList<Question>());
		elementBean.setSelectedElement(element);
		
		LocaleBean restriction = new LocaleBean();
		restriction.setCountry("JP");
		restriction.setLanguage("z2");
		
		elementBean.setNewRestrictions(Arrays.asList(restriction));
		
		Errors mockErrors = EasyMock.createMock(Errors.class);
		mockErrors.rejectValue("newRestrictions[0].language", "error.locale.language.notValid");

		EasyMock.replay(mockErrors);
		
		validator.validate(elementBean, mockErrors);
		
		EasyMock.verify(mockErrors);
	}
	
	@Test
	public void shouldRejectWhenExistingLanguageNotValidISOCode() {
		Element element = new Element();
		element.setName("name");
		element.setTitleText("title");
		ElementBean elementBean = new ElementBean(new ArrayList<Element>(), new ArrayList<Question>());
		elementBean.setSelectedElement(element);
		
		LocaleBean restriction = new LocaleBean();
		restriction.setCountry("JP");
		restriction.setLanguage("z2");
		
		elementBean.setExistingRestrictions(Arrays.asList(restriction));
		
		Errors mockErrors = EasyMock.createMock(Errors.class);
		mockErrors.rejectValue("existingRestrictions[0].language", "error.locale.language.notValid");
		
		EasyMock.replay(mockErrors);
		
		validator.validate(elementBean, mockErrors);
		
		EasyMock.verify(mockErrors);
	}
	
	@Test
	public void shouldRejectWhenNewCountryNotValidISOCode() {
		Element element = new Element();
		element.setName("name");
		element.setTitleText("title");
		ElementBean elementBean = new ElementBean(new ArrayList<Element>(), new ArrayList<Question>());
		elementBean.setSelectedElement(element);
		
		LocaleBean restriction = new LocaleBean();
		restriction.setCountry("J2");
		restriction.setLanguage("en");
		
		elementBean.setNewRestrictions(Arrays.asList(restriction));
		
		Errors mockErrors = EasyMock.createMock(Errors.class);
		mockErrors.rejectValue("newRestrictions[0].country", "error.locale.country.notValid");
		
		EasyMock.replay(mockErrors);
		
		validator.validate(elementBean, mockErrors);
		
		EasyMock.verify(mockErrors);
	}
	
	@Test
	public void shouldRejectWhenExistingCountryNotValidISOCode() {
		Element element = new Element();
		element.setName("name");
		element.setTitleText("title");
		ElementBean elementBean = new ElementBean(new ArrayList<Element>(), new ArrayList<Question>());
		elementBean.setSelectedElement(element);
		
		LocaleBean restriction = new LocaleBean();
		restriction.setCountry("J2");
		restriction.setLanguage("en");
		
		elementBean.setExistingRestrictions(Arrays.asList(restriction));
		
		Errors mockErrors = EasyMock.createMock(Errors.class);
		mockErrors.rejectValue("existingRestrictions[0].country", "error.locale.country.notValid");
		
		EasyMock.replay(mockErrors);
		
		validator.validate(elementBean, mockErrors);
		
		EasyMock.verify(mockErrors);
	}
	
	@Test
	public void shouldNotRejectWhenNewCountryHasEmptyISOCode() {
		Element element = new Element();
		element.setName("name");
		element.setTitleText("title");
		ElementBean elementBean = new ElementBean(new ArrayList<Element>(), new ArrayList<Question>());
		elementBean.setSelectedElement(element);
		
		LocaleBean restriction = new LocaleBean();
		restriction.setLanguage("en");
		
		elementBean.setNewRestrictions(Arrays.asList(restriction));
		
		Errors mockErrors = EasyMock.createMock(Errors.class);
		
		EasyMock.replay(mockErrors);
		
		validator.validate(elementBean, mockErrors);
		
		EasyMock.verify(mockErrors);
	}
	
	@Test
	public void shouldNotRejectWhenExistingCountryHasEmptyISOCode() {
		Element element = new Element();
		element.setName("name");
		element.setTitleText("title");
		ElementBean elementBean = new ElementBean(new ArrayList<Element>(), new ArrayList<Question>());
		elementBean.setSelectedElement(element);
		
		LocaleBean restriction = new LocaleBean();
		restriction.setLanguage("en");
		
		elementBean.setExistingRestrictions(Arrays.asList(restriction));
		
		Errors mockErrors = EasyMock.createMock(Errors.class);
		
		EasyMock.replay(mockErrors);
		
		validator.validate(elementBean, mockErrors);
		
		EasyMock.verify(mockErrors);
	}
	
	@Test
	public void shouldNotRejectWhenNewCountryHasValidISOCode() {
		Element element = new Element();
		element.setName("name");
		element.setTitleText("title");
		ElementBean elementBean = new ElementBean(new ArrayList<Element>(), new ArrayList<Question>());
		elementBean.setSelectedElement(element);
		
		LocaleBean restriction = new LocaleBean();
		restriction.setLanguage("en");
		restriction.setCountry("AO");
		
		elementBean.setNewRestrictions(Arrays.asList(restriction));
		
		Errors mockErrors = EasyMock.createMock(Errors.class);
		
		EasyMock.replay(mockErrors);
		
		validator.validate(elementBean, mockErrors);
		
		EasyMock.verify(mockErrors);
	}
	
	@Test
	public void shouldRejectWhenNewTagOptionHasEmptyLabel() {
		Element element = new Element();
		element.setName("name");
		element.setTitleText("title");
		ElementBean elementBean = new ElementBean(new ArrayList<Element>(), new ArrayList<Question>());
		elementBean.setSelectedElement(element);
		elementBean.setTagType(TagType.SELECT);
		
		TagOption tagOption = new TagOption("", "value");
		
		elementBean.setNewOptions(Arrays.asList(tagOption));
		
		Errors mockErrors = EasyMock.createMock(Errors.class);
		mockErrors.rejectValue("newOptions[0].label", "error.element.option.label.empty");
		
		EasyMock.replay(mockErrors);
		
		validator.validate(elementBean, mockErrors);
		
		EasyMock.verify(mockErrors);
	}
	
	@Test
	public void shouldRejectWhenNewTagOptionHasEmptyValue() {
		Element element = new Element();
		element.setName("name");
		element.setTitleText("title");
		ElementBean elementBean = new ElementBean(new ArrayList<Element>(), new ArrayList<Question>());
		elementBean.setSelectedElement(element);
		elementBean.setTagType(TagType.SELECT);
		
		TagOption tagOption = new TagOption("label", "");
		
		elementBean.setNewOptions(Arrays.asList(tagOption));
		
		Errors mockErrors = EasyMock.createMock(Errors.class);
		mockErrors.rejectValue("newOptions[0].value", "error.element.option.value.empty");
		
		EasyMock.replay(mockErrors);
		
		validator.validate(elementBean, mockErrors);
		
		EasyMock.verify(mockErrors);
	}
	
	@Test
	public void shouldRejectWhenExistingTagOptionHasEmptyLabel() {
		Element element = new Element();
		element.setName("name");
		element.setTitleText("title");
		ElementBean elementBean = new ElementBean(new ArrayList<Element>(), new ArrayList<Question>());
		elementBean.setSelectedElement(element);
		
		TagOption tagOption = new TagOption("", "value");
		Select selectTag = new Select();
		selectTag.addOption(tagOption);
		element.setTags(new HashSet<Tag>(Arrays.<Tag>asList(selectTag)));
		
		Errors mockErrors = EasyMock.createMock(Errors.class);
		mockErrors.rejectValue("selectedElement.tag.optionsAsList[0].label", "error.element.option.label.empty");
		
		EasyMock.replay(mockErrors);
		
		validator.validate(elementBean, mockErrors);
		
		EasyMock.verify(mockErrors);
	}
	
	@Test
	public void shouldRejectWhenExistingTagOptionHasEmptyValue() {
		Element element = new Element();
		element.setName("name");
		element.setTitleText("title");
		ElementBean elementBean = new ElementBean(new ArrayList<Element>(), new ArrayList<Question>());
		elementBean.setSelectedElement(element);
		
		TagOption tagOption = new TagOption("label", "");
		Select selectTag = new Select();
		selectTag.addOption(tagOption);
		element.setTags(new HashSet<Tag>(Arrays.<Tag>asList(selectTag)));
		
		Errors mockErrors = EasyMock.createMock(Errors.class);
		mockErrors.rejectValue("selectedElement.tag.optionsAsList[0].value", "error.element.option.value.empty");
		
		EasyMock.replay(mockErrors);
		
		validator.validate(elementBean, mockErrors);
		
		EasyMock.verify(mockErrors);
	}
	
	@Test
	public void shouldRejectWhenExistingUrlLinkHasNoUrlSet() {
		Element element = new Element();
		element.setName("name");
		element.setTitleText("title");
		ElementBean elementBean = new ElementBean(new ArrayList<Element>(), new ArrayList<Question>());
		elementBean.setSelectedElement(element);
		elementBean.setTagType(TagType.URLLINK);
		
		UrlLink urlLink = new UrlLink();
		urlLink.setUrl("http://localhost");
		element.setTags(new HashSet<Tag>(Arrays.asList(urlLink)));
		
		Errors mockErrors = EasyMock.createMock(Errors.class);
		mockErrors.rejectValue("url", "error.element.url.empty");
		
		EasyMock.replay(mockErrors);
		
		validator.validate(elementBean, mockErrors);
		
		EasyMock.verify(mockErrors);
	}
}
