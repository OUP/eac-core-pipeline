package com.oup.eac.admin.validators;

import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.oup.eac.admin.beans.ElementBean;
import com.oup.eac.admin.beans.ElementBean.LocaleBean;
import com.oup.eac.domain.OptionsTag;
import com.oup.eac.domain.Tag;
import com.oup.eac.domain.Tag.TagType;
import com.oup.eac.domain.TagOption;

@Component
public class ElementBeanValidator implements Validator {

	@Override
	public void validate(final Object target, final Errors errors) {
		ElementBean elementBean = (ElementBean) target;
		validateName(elementBean, errors);
		validateTitleText(elementBean, errors);
		validateRegularExpression(elementBean, errors);
		validateElementCountryRestrictions(elementBean, errors);
		validateTagOptions(elementBean, errors);
		validateUrl(elementBean, errors);
	}
	
	private void validateName(final ElementBean elementBean, final Errors errors) {
		if (StringUtils.isBlank(elementBean.getSelectedElement().getName())) {
			errors.rejectValue("selectedElement.name", "error.element.name.empty");
		}
	}

	private void validateTitleText(final ElementBean elementBean, final Errors errors) {
		if (StringUtils.isBlank(elementBean.getSelectedElement().getTitleText())) {
			errors.rejectValue("selectedElement.titleText", "error.element.title.empty");
		}
	}

	private void validateRegularExpression(final ElementBean elementBean, final Errors errors) {
		if (StringUtils.isNotBlank(elementBean.getSelectedElement().getRegularExpression())) {
			try {
				Pattern.compile(elementBean.getSelectedElement().getRegularExpression());
			} catch (final Exception e) {
				errors.rejectValue("selectedElement.regularExpression", "error.element.validation.invalid");
			}
		}
	}

	private void validateElementCountryRestrictions(final ElementBean elementBean, final Errors errors) {
		List<LocaleBean> newRestrictions = elementBean.getNewRestrictions();
		if (CollectionUtils.isNotEmpty(newRestrictions)) {
			for (int i = 0; i < newRestrictions.size(); i++) {
				LocaleBean restriction = newRestrictions.get(i);
				LocaleValidatorHelper.validateISOLanguage(restriction.getLanguage(), "newRestrictions[" + i + "].language", errors);
				if (StringUtils.isNotBlank(restriction.getCountry())) {
					LocaleValidatorHelper.validateISOCountry(restriction.getCountry(), "newRestrictions[" + i + "].country", errors);
				}
			}
		}
		List<LocaleBean> existingRestrictions = elementBean.getExistingRestrictions();
		if (CollectionUtils.isNotEmpty(existingRestrictions)) {
			for (int i = 0; i < existingRestrictions.size(); i++) {
				LocaleBean restriction = existingRestrictions.get(i);
				LocaleValidatorHelper.validateISOLanguage(restriction.getLanguage(), "existingRestrictions[" + i + "].language",
						errors);
				if (StringUtils.isNotBlank(restriction.getCountry())) {
					LocaleValidatorHelper.validateISOCountry(restriction.getCountry(), "existingRestrictions[" + i + "].country", errors);
				}
			}
		}
	}

	private void validateTagOptions(final ElementBean elementBean, final Errors errors) {
		List<TagOption> newOptions = elementBean.getNewOptions();
		if (CollectionUtils.isNotEmpty(newOptions)) {
			for (int i = 0; i < newOptions.size(); i++) {
				TagOption tagOption = newOptions.get(i);
				if (StringUtils.isBlank(tagOption.getLabel())) {
					errors.rejectValue("newOptions[" + i + "].label", "error.element.option.label.empty");
				}
				if (StringUtils.isBlank(tagOption.getValue())) {
					errors.rejectValue("newOptions[" + i + "].value", "error.element.option.value.empty");
				}
			}
		}
		Tag tag = elementBean.getSelectedElement().getTag();
		if (tag instanceof OptionsTag) {
			OptionsTag optionsTag = (OptionsTag) tag;
			List<TagOption> existingOptions = optionsTag.getOptionsAsList();
			if (CollectionUtils.isNotEmpty(existingOptions)) {
				for (int i = 0; i < existingOptions.size(); i++) {
					TagOption tagOption = existingOptions.get(i);
					if (StringUtils.isBlank(tagOption.getLabel())) {
						errors.rejectValue("selectedElement.tag.optionsAsList[" + i + "].label", "error.element.option.label.empty");
					}
					if (StringUtils.isBlank(tagOption.getValue())) {
						errors.rejectValue("selectedElement.tag.optionsAsList[" + i + "].value", "error.element.option.value.empty");
					}
				}
			}
		}
	}

	private void validateUrl(final ElementBean elementBean, final Errors errors) {
		TagType tagType = elementBean.getTagType();
		if (tagType != null && tagType.equals(TagType.URLLINK) && StringUtils.isEmpty(elementBean.getUrl())) {
			errors.rejectValue("url", "error.element.url.empty");
		}
	}

	
	@Override
	public boolean supports(final Class<?> clazz) {
		final boolean supports = ElementBean.class.isAssignableFrom(clazz);
		return supports;
	}

}
