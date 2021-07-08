package com.oup.eac.service.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.oup.eac.data.ComponentDao;
import com.oup.eac.domain.Component;
import com.oup.eac.service.ComponentService;

@Service("componentService")
public class ComponentServiceImpl implements ComponentService {

	private final ComponentDao componentDao;

	@Autowired
	public ComponentServiceImpl(final ComponentDao componentDao) {
		this.componentDao = componentDao;
	}

	@Override
	public Component getComponentById(final String id) {
		return componentDao.findById(id, true);
	}

	@Override
	public List<Component> getComponentsOrderedByLabelKey() {
		List<Component> sortedComponents = componentDao.findAll();
		Collections.sort(sortedComponents, new Comparator<Component>() {
			@Override
			public int compare(final Component o1, final Component o2) {
				return o1.getLabelKey().compareTo(o2.getLabelKey());
			}
		});
		return sortedComponents;
	}
	
	@Override
	public List<Component> getComponentsOrderedByName() {
		List<Component> sortedComponents = componentDao.findAll();
		Collections.sort(sortedComponents, new Comparator<Component>() {
			@Override
			public int compare(final Component o1, final Component o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
		return sortedComponents;
	}

	@Override	
	public void saveComponent(final Component component) {
		componentDao.saveOrUpdate(component);
	}

	@Override
	public void deleteComponent(final Component component) {
		componentDao.delete(component);
	}

}
