package com.oup.eac.service.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.oup.eac.data.ElementDao;
import com.oup.eac.domain.Element;
import com.oup.eac.service.ElementService;

@Service("elementService")
public class ElementServiceImpl implements ElementService {

	private final ElementDao elementDao;

	@Autowired
	public ElementServiceImpl(final ElementDao elementDao) {
		this.elementDao = elementDao;
	}

	@Override
	public Element getElementById(final String id) {
		return elementDao.getElementByIdWithFullyFetchedQuestion(id);
	}

	@Override
	public List<Element> getElementsOrderedByTitleText() {
		List<Element> sortedElements = elementDao.findAll();
		Collections.sort(sortedElements, new Comparator<Element>() {
			@Override
			public int compare(final Element o1, final Element o2) {
				return o1.getTitleText().compareTo(o2.getTitleText());
			}
		});
		return sortedElements;
	}
	
	@Override
	public List<Element> getElementsOrderedByName() {
		List<Element> sortedElements = elementDao.findAll();
		Collections.sort(sortedElements, new Comparator<Element>() {
			@Override
			public int compare(final Element o1, final Element o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
		return sortedElements;
	}

	@Override
	public List<Locale> getOrderedElementCountryRestrictionLocales() {
		return elementDao.getOrderedElementCountryRestrictionLocales();
	}

	@Override
	public void saveElement(final Element element) {
		elementDao.saveOrUpdate(element);
	}

	@Override
	public void deleteElement(final Element element) {
		elementDao.delete(element);
	}

}
