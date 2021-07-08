package com.oup.eac.admin.binding;

import java.beans.PropertyEditorSupport;

import com.oup.eac.domain.Element;
import com.oup.eac.service.ElementService;

public class ElementTypePropertyEditor extends PropertyEditorSupport {

	private final ElementService elementService;

	public ElementTypePropertyEditor(final ElementService elementService) {
		this.elementService = elementService;
	}

	@Override
	public String getAsText() {
		Object value = getValue();
		return (value != null ? ((Element) value).getId() : "");
	}

	@Override
	public void setAsText(final String text) throws IllegalArgumentException {
		if (text == null) {
			setValue(null);
		} else {
			Element element = elementService.getElementById(text);
			setValue(element);
		}
	}
}
