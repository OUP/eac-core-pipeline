package com.oup.eac.admin.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.oup.eac.domain.Component;
import com.oup.eac.domain.Element;
import com.oup.eac.domain.Field;

public class ComponentBean implements Serializable {

	private List<Component> components;
	private List<Element> elements;
	private Component selectedComponent;
	private boolean newComponent;
	private String fieldsToRemove;
	private String seq;
	private List<Field> newFields = new ArrayList<Field>();

	public ComponentBean(final List<Component> components, final List<Element> elements) {
		this.components = components;
		this.elements = elements;
	}

	public List<Component> getComponents() {
		return components;
	}

	public void setComponents(final List<Component> components) {
		this.components = components;
	}

	public Component getSelectedComponent() {
		return selectedComponent;
	}

	public void setSelectedComponent(final Component selectedComponent) {
		this.selectedComponent = selectedComponent;
	}

	public void clearSelectedComponent() {
		selectedComponent = null;
	}

	public boolean isSelectedComponentDeletable() {
		boolean deletable = false;
		if (selectedComponent != null) {
			deletable = selectedComponent.getFields().isEmpty();
		}
		return deletable;
	}
	
	public Component getUpdatedComponent() {
		if (StringUtils.isNotBlank(fieldsToRemove)) {
			for (String id : fieldsToRemove.split(",")) {
				if (StringUtils.isNotBlank(id)) {
					selectedComponent.removeField(id);
				}
			}
		}
		
		resequenceFields();
		removeTemporaryFieldIds();
		
		return selectedComponent;
	}

	private void resequenceFields() {
		Set<Field> resequencedFields = new HashSet<Field>(newFields);
		resequencedFields.addAll(selectedComponent.getFields());
		
		for (Field field : resequencedFields) {
			if (field.getComponent() == null) {
				field.setComponent(selectedComponent);
			}
			field.setSequence(findSequenceFor(field));
		}
		
		selectedComponent.getFields().clear();
		selectedComponent.getFields().addAll(resequencedFields);
	}
	
	private int findSequenceFor(final Field field) {
		int sequence = 0;
		if (StringUtils.isNotBlank(seq)) {
			String[] orderedSeqKeys = seq.split(",");
			for (int i = 0; i < orderedSeqKeys.length; i++) {
				if (orderedSeqKeys[i].equals(field.getId())) {
					sequence = i;
					break;
				}
			}
		}
		return sequence;
	}

	private void removeTemporaryFieldIds() {
		for (Field field : newFields) {
			if (StringUtils.isNotBlank(field.getId())) {
				field.setId(null);
			}
		}
	}
	
	public List<Element> getElements() {
		return elements;
	}

	public void setElements(final List<Element> elements) {
		this.elements = elements;
	}

	public boolean isNewComponent() {
		return newComponent;
	}

	public void setNewComponent(final boolean newComponent) {
		this.newComponent = newComponent;
	}

	public String getFieldsToRemove() {
		return fieldsToRemove;
	}

	public void setFieldsToRemove(final String fieldsToRemove) {
		this.fieldsToRemove = fieldsToRemove;
	}

	public String getSeq() {
		return seq;
	}

	public void setSeq(final String seq) {
		this.seq = seq;
	}

	public List<Field> getNewFields() {
		return newFields;
	}

	public void setNewFields(final List<Field> newFields) {
		this.newFields = newFields;
	}
	
}
