package com.oup.eac.web.tags;

import javax.servlet.jsp.JspException;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.servlet.tags.form.TagWriter;

/**
 * @author harlandd
 * 
 * @see org.springframework.web.servlet.tags.form.CheckboxesTag
 * 
 * This is a direct copy of the spring CheckboxesTag except extends the eac version
 * of AbstractMultiCheckedElementTag.
 *
 */
public class CheckboxesTag extends AbstractMultiCheckedElementTag {

	@Override
	protected int writeTagContent(TagWriter tagWriter) throws JspException {
		super.writeTagContent(tagWriter);

		if (!isDisabled()) {
			// Write out the 'field was present' marker.
			tagWriter.startTag("input");
			tagWriter.writeAttribute("type", "hidden");
			tagWriter.writeAttribute("name", WebDataBinder.DEFAULT_FIELD_MARKER_PREFIX + getName());
			tagWriter.writeAttribute("value", "on");
			tagWriter.endTag();
		}

		return SKIP_BODY;
	}

	@Override
	protected String getInputType() {
		return "checkbox";
	}

}