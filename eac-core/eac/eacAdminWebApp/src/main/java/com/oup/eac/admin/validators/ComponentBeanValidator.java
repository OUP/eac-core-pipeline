package com.oup.eac.admin.validators;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.oup.eac.admin.beans.ComponentBean;
import com.oup.eac.domain.Field;

@Component
public class ComponentBeanValidator implements Validator {

	@Override
	public void validate(final Object target, final Errors errors) {
		ComponentBean componentBean = (ComponentBean) target;
		validateName(componentBean, errors);
		validateLabelKey(componentBean, errors);
		validateFields(componentBean, errors);
	}
	
	private void validateName(final ComponentBean componentBean, final Errors errors) {
		com.oup.eac.domain.Component selectedComponent = componentBean.getSelectedComponent();
		if (StringUtils.isBlank(componentBean.getSelectedComponent().getName())) {
			errors.rejectValue("selectedComponent.name", "error.component.name.empty");
		} else {
			for (com.oup.eac.domain.Component component : componentBean.getComponents()) {
				if (StringUtils.equalsIgnoreCase(component.getName(), selectedComponent.getName())
						&& !StringUtils.equals(component.getId(), selectedComponent.getId())) {
					errors.rejectValue("selectedComponent.name", "error.component.name.duplicate");
				}
			}
		}
	}
	
	private void validateLabelKey(final ComponentBean componentBean, final Errors errors) {
		if (StringUtils.isBlank(componentBean.getSelectedComponent().getLabelKey())) {
			errors.rejectValue("selectedComponent.labelKey", "error.component.title.empty");
		}
	}

	private void validateFields(final ComponentBean componentBean, final Errors errors) {
		List<Field> allFields = new ArrayList<Field>(componentBean.getSelectedComponent().getFields());
		for (int i = 0; i < allFields.size(); i++) {
			if (referencesSameElementAsExistingField(allFields.get(i), allFields)) {
				errors.rejectValue("selectedComponent.fields[" + i + "].element", "error.component.field.sameElement");
				break;
			}
		}
		List<Field> newFields = componentBean.getNewFields();
		allFields.addAll(newFields);
		for (int i = 0; i < newFields.size(); i++) {
			if (referencesSameElementAsExistingField(newFields.get(i), allFields)) {
				errors.rejectValue("newFields[" + i + "].element", "error.component.field.sameElement");
				break;
			}
		}
	}

	private boolean referencesSameElementAsExistingField(final Field field, final List<Field> existingFields) {
		boolean referencesSameElement = false;
		for (Field existingField : existingFields) {
			if (fieldsAreDifferent(field, existingField) && referencedElementsAreTheSame(field, existingField)) {
				referencesSameElement = true;
				break;
			}
		}
		return referencesSameElement;
	}

	private boolean fieldsAreDifferent(final Field field, final Field existingField) {
		return existingField != field || !StringUtils.equals(existingField.getId(), field.getId());
	}

	private boolean referencedElementsAreTheSame(final Field field, final Field existingField) {
		return existingField.getElement().getId().equals(field.getElement().getId());
	}

	@Override
	public boolean supports(final Class<?> clazz) {
		final boolean supports = ComponentBean.class.isAssignableFrom(clazz);
		return supports;
	}

}
