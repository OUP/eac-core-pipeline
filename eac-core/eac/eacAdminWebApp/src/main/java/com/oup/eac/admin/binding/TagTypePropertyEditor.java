package com.oup.eac.admin.binding;

import java.beans.PropertyEditorSupport;

import com.oup.eac.domain.Tag.TagType;

public class TagTypePropertyEditor extends PropertyEditorSupport {

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
			TagType tagType = TagType.valueOf(text);
			setValue(tagType);
		}
	}
}
