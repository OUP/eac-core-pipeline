package com.oup.eac.admin.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.oup.eac.domain.AccountPageDefinition;
import com.oup.eac.domain.Component;
import com.oup.eac.domain.Division;
import com.oup.eac.domain.PageComponent;
import com.oup.eac.domain.PageDefinition;
import com.oup.eac.domain.ProductPageDefinition;

public class PageDefinitionBean implements Serializable {

	private List<PageDefinition> pageDefinitions;
	private List<Component> components;
	private List<Division> divisions;
	private PageDefinition selectedPageDefinition;
	private boolean newProductPageDefinition;
	private boolean newAccountPageDefinition;
	private String pageDefinitionType;
	private List<PageComponent> newComponents = new ArrayList<PageComponent>();
	private String componentsToRemove;
	private String seq;
	
	public PageDefinitionBean(
			final List<PageDefinition> pageDefinitions, 
			final List<Component> components,
			final List<Division> divisions) {
		this.pageDefinitions = pageDefinitions;
		this.components = components;
		this.divisions = divisions;
	}

	public List<PageDefinition> getPageDefinitions() {
		return pageDefinitions;
	}

	public void setPageDefinitions(final List<PageDefinition> pageDefinitions) {
		this.pageDefinitions = pageDefinitions;
	}

	public List<Component> getComponents() {
		return components;
	}

	public void setComponents(final List<Component> components) {
		this.components = components;
	}

	public List<Division> getDivisions() {
		return divisions;
	}

	public void setDivisions(final List<Division> divisions) {
		this.divisions = divisions;
	}

	public PageDefinition getSelectedPageDefinition() {
		return selectedPageDefinition;
	}

	public void setSelectedPageDefinition(final PageDefinition selectedPageDefinition) {
		this.selectedPageDefinition = selectedPageDefinition;
	}
	
	public PageDefinition getUpdatedPageDefinition() {
		if (StringUtils.isNotBlank(componentsToRemove)) {
			for (String id : componentsToRemove.split(",")) {
				if (StringUtils.isNotBlank(id)) {
					selectedPageDefinition.removePageComponent(id);
				}
			}
		}
		
		resequenceComponents();
		removeTemporaryComponentIds();
		
		return selectedPageDefinition;
	}

	private void resequenceComponents() {
		Set<PageComponent> resequencedComponents = new HashSet<PageComponent>(newComponents);
		resequencedComponents.addAll(selectedPageDefinition.getPageComponents());
		
		for (PageComponent component : resequencedComponents) {
			if (component.getPageDefinition() == null) {
				component.setPageDefinition(selectedPageDefinition);
			}
			component.setSequence(findSequenceFor(component));
		}
		
		selectedPageDefinition.getPageComponents().clear();
		selectedPageDefinition.getPageComponents().addAll(resequencedComponents);
	}
	
	private int findSequenceFor(final PageComponent component) {
		int sequence = 0;
		if (StringUtils.isNotBlank(seq)) {
			String[] orderedSeqKeys = seq.split(",");
			for (int i = 0; i < orderedSeqKeys.length; i++) {
				if (orderedSeqKeys[i].equals(component.getId())) {
					sequence = i;
					break;
				}
			}
		}
		return sequence;
	}

	private void removeTemporaryComponentIds() {
		for (PageComponent component : newComponents) {
			if (StringUtils.isNotBlank(component.getId())) {
				component.setId(null);
			}
		}
	}

	public boolean isNewPageDefinition() {
		return newAccountPageDefinition || newProductPageDefinition;
	}
	
	public boolean isNewProductPageDefinition() {
		return newProductPageDefinition;
	}

	public void setNewProductPageDefinition(final boolean newProductPageDefinition) {
		this.newProductPageDefinition = newProductPageDefinition;
	}

	public boolean isNewAccountPageDefinition() {
		return newAccountPageDefinition;
	}

	public void setNewAccountPageDefinition(final boolean newAccountPageDefinition) {
		this.newAccountPageDefinition = newAccountPageDefinition;
	}

	public String getPageDefinitionType() {
		return pageDefinitionType;
	}

	public void setPageDefinitionType(final String pageDefinitionType) {
		this.pageDefinitionType = pageDefinitionType;
	}

	public List<PageComponent> getNewComponents() {
		return newComponents;
	}

	public void setNewComponents(final List<PageComponent> newComponents) {
		this.newComponents = newComponents;
	}

	public boolean isSelectedPageDefinitionDeletable() {
		boolean deletable = false;
		if (selectedPageDefinition != null) {
			if (selectedPageDefinition instanceof AccountPageDefinition) {
				deletable = ((AccountPageDefinition) selectedPageDefinition).getRegistrationDefinitions().size() == 0;
			} else if (selectedPageDefinition instanceof ProductPageDefinition) {
				deletable = ((ProductPageDefinition) selectedPageDefinition).getRegistrationDefinitions().size() == 0;
			}
		}
		return deletable;
	}

	public String getComponentsToRemove() {
		return componentsToRemove;
	}

	public void setComponentsToRemove(final String componentsToRemove) {
		this.componentsToRemove = componentsToRemove;
	}

	public String getSeq() {
		return seq;
	}

	public void setSeq(final String seq) {
		this.seq = seq;
	}
	
	public void clearSelectedPageDefinition() {
		selectedPageDefinition = null;
	}
	
}
