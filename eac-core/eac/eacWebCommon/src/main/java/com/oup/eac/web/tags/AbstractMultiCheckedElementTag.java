package com.oup.eac.web.tags;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.jsp.JspException;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.tags.form.AbstractCheckedElementTag;
import org.springframework.web.servlet.tags.form.TagWriter;

/**
 * @author harlandd
 * 
 * @see org.springframework.web.servlet.tags.form.AbstractMultiCheckedElementTag
 * 
 * This is a direct copy of the spring AbstractMultiCheckedElementTag except that it attempts
 * to resolve the label from the message resources <code>writeElementTag</code> using 
 * <code>getMessage</code> This also output the label before the checkbox which id the inverse 
 * to how the spring version outputs.
 *
 */
public abstract class AbstractMultiCheckedElementTag extends AbstractCheckedElementTag {

	/**
	 * The HTML '<code>span</code>' tag.
	 */
	private static final String SPAN_TAG = "span";


	/**
	 * The {@link java.util.Collection}, {@link java.util.Map} or array of objects
	 * used to generate the '<code>input type="checkbox/radio"</code>' tags.
	 */
	private Object items;

	/**
	 * The name of the property mapped to the '<code>value</code>' attribute
	 * of the '<code>input type="checkbox/radio"</code>' tag.
	 */
	private String itemValue;

	/**
	 * The value to be displayed as part of the '<code>input type="checkbox/radio"</code>' tag.
	 */
	private String itemLabel;

	/**
	 * The HTML element used to enclose the '<code>input type="checkbox/radio"</code>' tag.
	 */
	private String element = SPAN_TAG;

	/**
	 * Delimiter to use between each '<code>input type="checkbox/radio"</code>' tags.
	 */
	private String delimiter;


	/**
	 * Set the {@link java.util.Collection}, {@link java.util.Map} or array of objects
	 * used to generate the '<code>input type="checkbox/radio"</code>' tags.
	 * <p>Typically a runtime expression.
	 * @param items said items
	 */
	public void setItems(Object items) {
		Assert.notNull(items, "'items' must not be null");
		this.items = items;
	}

	/**
	 * Get the {@link java.util.Collection}, {@link java.util.Map} or array of objects
	 * used to generate the '<code>input type="checkbox/radio"</code>' tags.
	 */
	protected Object getItems() {
		return this.items;
	}

	/**
	 * Set the name of the property mapped to the '<code>value</code>' attribute
	 * of the '<code>input type="checkbox/radio"</code>' tag.
	 * <p>May be a runtime expression.
	 */
	public void setItemValue(String itemValue) {
		Assert.hasText(itemValue, "'itemValue' must not be empty");
		this.itemValue = itemValue;
	}

	/**
	 * Get the name of the property mapped to the '<code>value</code>' attribute
	 * of the '<code>input type="checkbox/radio"</code>' tag.
	 */
	protected String getItemValue() {
		return this.itemValue;
	}

	/**
	 * Set the value to be displayed as part of the
	 * '<code>input type="checkbox/radio"</code>' tag.
	 * <p>May be a runtime expression.
	 */
	public void setItemLabel(String itemLabel) {
		Assert.hasText(itemLabel, "'itemLabel' must not be empty");
		this.itemLabel = itemLabel;
	}

	/**
	 * Get the value to be displayed as part of the
	 * '<code>input type="checkbox/radio"</code>' tag.
	 */
	protected String getItemLabel() {
		return this.itemLabel;
	}

	/**
	 * Set the delimiter to be used between each
	 * '<code>input type="checkbox/radio"</code>' tag.
	 * <p>By default, there is <em>no</em> delimiter.
	 */
	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	/**
	 * Return the delimiter to be used between each
	 * '<code>input type="radio"</code>' tag.
	 */
	public String getDelimiter() {
		return this.delimiter;
	}

	/**
	 * Set the HTML element used to enclose the
	 * '<code>input type="checkbox/radio"</code>' tag.
	 * <p>Defaults to an HTML '<code>&lt;span/&gt;</code>' tag.
	 */
	public void setElement(String element) {
		Assert.hasText(element, "'element' cannot be null or blank");
		this.element = element;
	}

	/**
	 * Get the HTML element used to enclose
	 * '<code>input type="checkbox/radio"</code>' tag.
	 */
	public String getElement() {
		return this.element;
	}


	/**
	 * Appends a counter to a specified id as well,
	 * since we're dealing with multiple HTML elements.
	 */
	@Override
	protected String resolveId() throws JspException {
		Object id = evaluate("id", getId());
		if (id != null) {
			String idString = id.toString();
			return (StringUtils.hasText(idString) ? TagIdGenerator.nextId(idString, this.pageContext) : null);
		}
		return autogenerateId();
	}

    protected final MessageSource getMessageSource() {
        return getRequestContext().getMessageSource();
    }
	
    protected String getMessage(final String message) {
    	MessageSource messageSource = getMessageSource();
    	try {
    		String resolvedMessage = messageSource.getMessage(message, null, getRequestContext().getLocale());
    		return resolvedMessage;
    	} catch (NoSuchMessageException e) {
			return message;
		}
    }
    
	/**
	 * Renders the '<code>input type="radio"</code>' element with the configured
	 * {@link #setItems(Object)} values. Marks the element as checked if the
	 * value matches the bound value.
	 */
	@SuppressWarnings("rawtypes")
	@Override
	protected int writeTagContent(TagWriter tagWriter) throws JspException {
		Object items = getItems();
		Object itemsObject = (items instanceof String ? evaluate("items", items) : items);
		
		String itemValue = getItemValue();
		String itemLabel = getMessage(getItemLabel());
		
		String valueProperty =
				(itemValue != null ? ObjectUtils.getDisplayString(evaluate("itemValue", itemValue)) : null);
		String labelProperty =
				(itemLabel != null ? ObjectUtils.getDisplayString(evaluate("itemLabel", itemLabel)) : null);

		Class<?> boundType = getBindStatus().getValueType();
		if (itemsObject == null && boundType != null && boundType.isEnum()) {
			itemsObject = boundType.getEnumConstants();
		}
		
		if (itemsObject == null) {
			throw new IllegalArgumentException("Attribute 'items' is required and must be a Collection, an Array or a Map");
		}

		if (itemsObject.getClass().isArray()) {
			Object[] itemsArray = (Object[]) itemsObject;
			for (int i = 0; i < itemsArray.length; i++) {
				Object item = itemsArray[i];
				writeObjectEntry(tagWriter, valueProperty, labelProperty, item, i);
			}
		}
		else if (itemsObject instanceof Collection) {
			final Collection optionCollection = (Collection) itemsObject;
			int itemIndex = 0;
			for (Iterator it = optionCollection.iterator(); it.hasNext(); itemIndex++) {
				Object item = it.next();
				writeObjectEntry(tagWriter, valueProperty, labelProperty, item, itemIndex);
			}
		}
		else if (itemsObject instanceof Map) {
			final Map optionMap = (Map) itemsObject;
			int itemIndex = 0;
			for (Iterator it = optionMap.entrySet().iterator(); it.hasNext(); itemIndex++) {
				Map.Entry entry = (Map.Entry) it.next();
				writeMapEntry(tagWriter, valueProperty, labelProperty, entry, itemIndex);
			}
		}
		else {
			throw new IllegalArgumentException("Attribute 'items' must be an array, a Collection or a Map");
		}

		return SKIP_BODY;
	}

	private void writeObjectEntry(TagWriter tagWriter, String valueProperty,
			String labelProperty, Object item, int itemIndex) throws JspException {

		BeanWrapper wrapper = PropertyAccessorFactory.forBeanPropertyAccess(item);
		Object renderValue;
		if (valueProperty != null) {
			renderValue = wrapper.getPropertyValue(valueProperty);
		} 
		else if (item instanceof Enum) {
			renderValue = ((Enum<?>) item).name();
		} 
		else {
			renderValue = item;
		}
		Object renderLabel = (labelProperty != null ? wrapper.getPropertyValue(labelProperty) : item);
		writeElementTag(tagWriter, item, renderValue, renderLabel, itemIndex);
	}

	@SuppressWarnings("rawtypes")
	private void writeMapEntry(TagWriter tagWriter, String valueProperty,
			String labelProperty, Map.Entry entry, int itemIndex) throws JspException {

		Object mapKey = entry.getKey();
		Object mapValue = entry.getValue();
		BeanWrapper mapKeyWrapper = PropertyAccessorFactory.forBeanPropertyAccess(mapKey);
		BeanWrapper mapValueWrapper = PropertyAccessorFactory.forBeanPropertyAccess(mapValue);
		Object renderValue = (valueProperty != null ?
				mapKeyWrapper.getPropertyValue(valueProperty) : mapKey.toString());
		Object renderLabel = (labelProperty != null ?
				mapValueWrapper.getPropertyValue(labelProperty) : mapValue.toString());
		writeElementTag(tagWriter, mapKey, renderValue, renderLabel, itemIndex);
	}

	private void writeElementTag(TagWriter tagWriter, Object item, Object value, Object label, int itemIndex)
			throws JspException {

		tagWriter.startTag(getElement());
		if (itemIndex > 0) {
			Object resolvedDelimiter = evaluate("delimiter", getDelimiter());
			if (resolvedDelimiter != null) {
				tagWriter.appendValue(resolvedDelimiter.toString());
			}
		}
		String id = resolveId();
		tagWriter.startTag("label");
		tagWriter.writeAttribute("for", id);
		tagWriter.appendValue(getMessage(convertToDisplayString(label)));
		tagWriter.endTag();
		tagWriter.startTag("input");		
		writeOptionalAttribute(tagWriter, "id", id);
		writeOptionalAttribute(tagWriter, "name", getName());
		writeOptionalAttributes(tagWriter);
		tagWriter.writeAttribute("type", getInputType());
		renderFromValue(item, value, tagWriter);
		tagWriter.endTag();
		tagWriter.endTag();
	}

	/**
	 * Return the type of the HTML input element to generate:
	 * "checkbox" or "radio".
	 */
	protected abstract String getInputType();

}
