package com.oup.eac.admin.binding;

import java.beans.PropertyEditorSupport;

import com.oup.eac.domain.Component;
import com.oup.eac.service.ComponentService;

public class ComponentTypePropertyEditor extends PropertyEditorSupport {

	private final ComponentService componentService;

	public ComponentTypePropertyEditor(final ComponentService componentService) {
		this.componentService = componentService;
	}

	@Override
	public String getAsText() {
		Object value = getValue();
		return (value != null ? ((Component) value).getId() : "");
	}

	@Override
	public void setAsText(final String text) throws IllegalArgumentException {
		if (text == null) {
			setValue(null);
		} else {
			Component component = componentService.getComponentById(text);
			setValue(component);
		}
	}
}
