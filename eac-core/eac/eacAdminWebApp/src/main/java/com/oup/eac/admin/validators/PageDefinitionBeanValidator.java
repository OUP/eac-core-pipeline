package com.oup.eac.admin.validators;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.oup.eac.admin.beans.PageDefinitionBean;
import com.oup.eac.domain.PageComponent;
import com.oup.eac.domain.PageDefinition;

@Component
public class PageDefinitionBeanValidator implements Validator {

	@Override
	public void validate(final Object target, final Errors errors) {
		PageDefinitionBean pageDefinitionBean = (PageDefinitionBean) target;
		validateName(pageDefinitionBean, errors);
		validateComponents(pageDefinitionBean, errors);
	}

	private void validateComponents(final PageDefinitionBean pageDefinitionBean, final Errors errors) {
		List<PageComponent> allComponents = new ArrayList<PageComponent>();
		allComponents.addAll(pageDefinitionBean.getSelectedPageDefinition().getPageComponents());
		for (int i = 0; i < allComponents.size(); i++) {
			if (isDuplicate(allComponents.get(i), allComponents)) {
				errors.rejectValue("selectedPageDefinition.pageComponents[" + i + "]", "error.pageDefinition.duplicateComponent");
				break;
			}
		}
		List<PageComponent> newComponents = pageDefinitionBean.getNewComponents();
		allComponents.addAll(newComponents);
		for (int i = 0; i < newComponents.size(); i++) {
			if (isDuplicate(newComponents.get(i), allComponents)) {
				errors.rejectValue("newComponents[" + i + "]", "error.pageDefinition.duplicateComponent");
				break;
			}
		}
	}

	private boolean isDuplicate(final PageComponent pageComponent, List<PageComponent> components) {
		boolean isDuplicate = false;
		for (PageComponent component : components) {
			if (component != pageComponent) {
				if (referenceSameComponent(pageComponent, component)) {
					isDuplicate = true;
					break;
				}
			}
		}
		return isDuplicate;
	}

	private boolean referenceSameComponent(final PageComponent pageComponent, final PageComponent component) {
		return component.getComponent() != null && pageComponent.getComponent() != null
				&& StringUtils.equals(component.getComponent().getId(), pageComponent.getComponent().getId());
	}

	private void validateName(final PageDefinitionBean pageDefinitionBean, final Errors errors) {
		PageDefinition selectedPageDefinition = pageDefinitionBean.getSelectedPageDefinition();
		if (selectedPageDefinition != null) {
			if (StringUtils.isBlank(pageDefinitionBean.getSelectedPageDefinition().getName())) {
				errors.rejectValue("selectedPageDefinition.name", "error.pageDefinition.name.empty");
			} else {
				for (PageDefinition pageDefinition : pageDefinitionBean.getPageDefinitions()) {
					if (StringUtils.equalsIgnoreCase(pageDefinition.getName(), selectedPageDefinition.getName())
							&& !StringUtils.equals(pageDefinition.getId(), selectedPageDefinition.getId())) {
						errors.rejectValue("selectedPageDefinition.name", "error.pageDefinition.name.duplicate");
					}
				}
			}
		}
	}

	@Override
	public boolean supports(final Class<?> clazz) {
		final boolean supports = PageDefinitionBean.class.isAssignableFrom(clazz);
		return supports;
	}

}
