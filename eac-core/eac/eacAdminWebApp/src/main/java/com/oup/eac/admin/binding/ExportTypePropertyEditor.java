package com.oup.eac.admin.binding;

import java.beans.PropertyEditorSupport;

import com.oup.eac.domain.ExportType;

public class ExportTypePropertyEditor extends PropertyEditorSupport {

	@Override
	public String getAsText() {
		Object value = getValue();
		return (value != null ? value.toString() : "");
	}

	@Override
	public void setAsText(final String text) throws IllegalArgumentException {
		if (text == null) {
			setValue(null);
		} else {
			ExportType exportType = ExportType.valueOf(text);
			setValue(exportType);
		}
	}

}
