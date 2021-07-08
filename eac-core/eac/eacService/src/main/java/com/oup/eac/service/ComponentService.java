package com.oup.eac.service;

import java.util.List;

import com.oup.eac.domain.Component;

public interface ComponentService {

	Component getComponentById(final String id);
	
	List<Component> getComponentsOrderedByLabelKey();
	
	List<Component> getComponentsOrderedByName();
	
	void saveComponent(final Component component);
	
	void deleteComponent(final Component component);
}
